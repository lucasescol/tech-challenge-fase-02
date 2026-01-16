package br.com.fiap.core.usecases;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;

public class CadastrarTipoUsuarioUseCase {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    public CadastrarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    public TipoUsuario executar(TipoUsuario tipoUsuario) {
        return tipoUsuarioGateway.incluir(tipoUsuario);
    }
}
