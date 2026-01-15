package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.gateways.IUsuarioGateway;

public class ExcluirUsuarioUseCase {
    private final IUsuarioGateway usuarioGateway;

    private ExcluirUsuarioUseCase(IUsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public static ExcluirUsuarioUseCase create(IUsuarioGateway usuarioGateway) {
        return new ExcluirUsuarioUseCase(usuarioGateway);
    }

    public void execute(Long idUsuario) {
        this.usuarioGateway.excluir(idUsuario);
    }
}
