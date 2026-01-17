package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.gateways.ITipoUsuarioGateway;

public class DeletarTipoUsuarioUseCase {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    private DeletarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }
    
    public static DeletarTipoUsuarioUseCase create(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new DeletarTipoUsuarioUseCase(tipoUsuarioGateway);
    }

    public boolean execute(Long id) {
        return tipoUsuarioGateway.obterPorId(id)
                .map(tipoUsuario -> {
                    tipoUsuarioGateway.deletar(id);
                    return true;
                })
                .orElse(false);
    }
}
