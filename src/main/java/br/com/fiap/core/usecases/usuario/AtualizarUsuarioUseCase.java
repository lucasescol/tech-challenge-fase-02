package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.EmailJaCadastradoException;
import br.com.fiap.core.exceptions.LoginJaCadastradoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class AtualizarUsuarioUseCase {
    
    public record InputModel(
        String nome,
        String email,
        String login,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
    ) {}

    public record OutputModel(
        Long id,
        String nome,
        String email,
        String login,
        String endereco,
        String tipoUsuario
    ) {}
    
    private final IUsuarioGateway usuarioGateway;
    private final IAuthenticationGateway authenticationGateway;

    private AtualizarUsuarioUseCase(IUsuarioGateway usuarioGateway, IAuthenticationGateway authenticationGateway) {
        this.usuarioGateway = usuarioGateway;
        this.authenticationGateway = authenticationGateway;
    }

    public static AtualizarUsuarioUseCase create(IUsuarioGateway usuarioGateway, IAuthenticationGateway authenticationGateway) {
        return new AtualizarUsuarioUseCase(usuarioGateway, authenticationGateway);
    }

    public OutputModel execute(Long idUsuario, InputModel input) {
        validarPermissao(idUsuario);
        
        Usuario usuarioExistente = usuarioGateway.buscarPorId(idUsuario)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
        
        String nomeAtualizado = input.nome() != null ? input.nome() : usuarioExistente.getNome();
        String emailAtualizado = input.email() != null ? input.email() : usuarioExistente.getEmail().getValor();
        String loginAtualizado = input.login() != null ? input.login() : usuarioExistente.getLogin();
        
        if (input.login() != null && !input.login().equals(usuarioExistente.getLogin())) {
            if (usuarioGateway.existeLoginCadastrado(input.login(), idUsuario)) {
                throw new LoginJaCadastradoException(input.login());
            }
        }
        
        if (input.email() != null && !input.email().equals(usuarioExistente.getEmail().getValor())) {
            if (usuarioGateway.existeEmailCadastrado(input.email(), idUsuario)) {
                throw new EmailJaCadastradoException(input.email());
            }
        }
        
        Endereco enderecoExistente = usuarioExistente.getEndereco();
        Endereco enderecoAtualizado = new Endereco(
            input.logradouro() != null ? input.logradouro() : enderecoExistente.getLogradouro(),
            input.numero() != null ? input.numero() : enderecoExistente.getNumero(),
            input.complemento() != null ? input.complemento() : enderecoExistente.getComplemento(),
            input.bairro() != null ? input.bairro() : enderecoExistente.getBairro(),
            input.cidade() != null ? input.cidade() : enderecoExistente.getCidade(),
            input.estado() != null ? input.estado() : enderecoExistente.getEstado(),
            input.cep() != null ? input.cep() : enderecoExistente.getCepLimpo()
        );
        
        Usuario usuarioAtualizado = Usuario.create(
            idUsuario,
            nomeAtualizado,
            emailAtualizado,
            loginAtualizado,
            usuarioExistente.getSenha(),
            enderecoAtualizado,
            usuarioExistente.getTipoUsuario()
        );
        
        Usuario usuarioSalvo = this.usuarioGateway.atualizar(idUsuario, usuarioAtualizado);

        return new OutputModel(
            usuarioSalvo.getId(),
            usuarioSalvo.getNome(),
            usuarioSalvo.getEmail().getValor(),
            usuarioSalvo.getLogin(),
            usuarioSalvo.getEndereco().getEnderecoCompleto(),
            usuarioSalvo.getTipoUsuario().getNome()
        );
    }
    
    private void validarPermissao(Long idUsuario) {
        if (authenticationGateway.isAdministrador()) {
            return;
        }
        
        Usuario usuario = usuarioGateway.buscarPorId(idUsuario)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
        
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        
        if (!usuario.getLogin().equals(loginUsuarioLogado)) {
            throw new AcessoNegadoException("Você só pode modificar seus próprios dados");
        }
    }
}
