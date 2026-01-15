package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class ExcluirUsuarioUseCase {
    private final IUsuarioGateway usuarioGateway;
    private final IAuthenticationGateway authenticationGateway;

    private ExcluirUsuarioUseCase(IUsuarioGateway usuarioGateway, IAuthenticationGateway authenticationGateway) {
        this.usuarioGateway = usuarioGateway;
        this.authenticationGateway = authenticationGateway;
    }

    public static ExcluirUsuarioUseCase create(IUsuarioGateway usuarioGateway, IAuthenticationGateway authenticationGateway) {
        return new ExcluirUsuarioUseCase(usuarioGateway, authenticationGateway);
    }

    public void execute(Long idUsuario) {
        validarPermissao(idUsuario);
        
        this.usuarioGateway.excluir(idUsuario);
    }
    
    private void validarPermissao(Long idUsuario) {
        if (authenticationGateway.isAdministrador()) {
            return;
        }
        
        Usuario usuario = usuarioGateway.buscarPorId(idUsuario)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
        
        String loginUsuarioLogado = authenticationGateway.getUsuarioLogado();
        
        if (!usuario.getLogin().equals(loginUsuarioLogado)) {
            throw new AcessoNegadoException("Você só pode excluir sua própria conta");
        }
    }
}
