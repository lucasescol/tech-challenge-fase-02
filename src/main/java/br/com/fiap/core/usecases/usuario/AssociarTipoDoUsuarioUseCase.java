package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.TipoUsuarioInvalidoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class AssociarTipoDoUsuarioUseCase {
    
    public record InputModel(
        String tipoUsuario
    ) {}

    public record OutputModel(
        Long id,
        String nome,
        String email,
        String login,
        String tipoUsuario
    ) {}
    
    private final IUsuarioGateway usuarioGateway;
    private final ITipoUsuarioGateway tipoUsuarioGateway;
    private final IAuthenticationGateway authenticationGateway;

    private AssociarTipoDoUsuarioUseCase(
            IUsuarioGateway usuarioGateway, 
            ITipoUsuarioGateway tipoUsuarioGateway,
            IAuthenticationGateway authenticationGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.authenticationGateway = authenticationGateway;
    }

    public static AssociarTipoDoUsuarioUseCase create(
            IUsuarioGateway usuarioGateway, 
            ITipoUsuarioGateway tipoUsuarioGateway,
            IAuthenticationGateway authenticationGateway) {
        return new AssociarTipoDoUsuarioUseCase(usuarioGateway, tipoUsuarioGateway, authenticationGateway);
    }

    public OutputModel execute(Long idUsuario, InputModel input) {
        validarPermissaoAdministrador();
        
        Usuario usuarioExistente = usuarioGateway.buscarPorId(idUsuario)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
        
        TipoUsuario tipoUsuario = tipoUsuarioGateway.obterPorNome(input.tipoUsuario().toUpperCase())
            .orElseThrow(() -> new TipoUsuarioInvalidoException(
                "Tipo de usuário '" + input.tipoUsuario() + "' não encontrado"
            ));
        
        Usuario usuarioAtualizado = Usuario.create(
            usuarioExistente.getId(),
            usuarioExistente.getNome(),
            usuarioExistente.getEmail().getValor(),
            usuarioExistente.getLogin(),
            usuarioExistente.getSenha(),
            usuarioExistente.getEndereco(),
            tipoUsuario
        );
        
        Usuario usuarioSalvo = this.usuarioGateway.atualizar(idUsuario, usuarioAtualizado);

        return new OutputModel(
            usuarioSalvo.getId(),
            usuarioSalvo.getNome(),
            usuarioSalvo.getEmail().getValor(),
            usuarioSalvo.getLogin(),
            usuarioSalvo.getTipoUsuario().getNome()
        );
    }
    
    private void validarPermissaoAdministrador() {
        if (!authenticationGateway.isAdministrador()) {
            throw new AcessoNegadoException("Apenas administradores podem alterar o tipo de usuário");
        }
    }
}
