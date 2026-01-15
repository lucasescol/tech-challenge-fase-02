package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
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
    
    private final IUsuarioGateway usuarioGateway;
    private final IAuthenticationGateway authenticationGateway;

    private AtualizarUsuarioUseCase(IUsuarioGateway usuarioGateway, IAuthenticationGateway authenticationGateway) {
        this.usuarioGateway = usuarioGateway;
        this.authenticationGateway = authenticationGateway;
    }

    public static AtualizarUsuarioUseCase create(IUsuarioGateway usuarioGateway, IAuthenticationGateway authenticationGateway) {
        return new AtualizarUsuarioUseCase(usuarioGateway, authenticationGateway);
    }

    public Usuario execute(Long idUsuario, InputModel input) {
        validarPermissao(idUsuario);
        
        Usuario usuarioExistente = usuarioGateway.buscarPorId(idUsuario)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
        
        String nomeAtualizado = input.nome() != null ? input.nome() : usuarioExistente.getNome();
        String emailAtualizado = input.email() != null ? input.email() : usuarioExistente.getEmail().getValor();
        String loginAtualizado = input.login() != null ? input.login() : usuarioExistente.getLogin();
        
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
        
        return this.usuarioGateway.atualizar(idUsuario, usuarioAtualizado);
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
