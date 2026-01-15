package br.com.fiap.core.usecases.usuario;

import java.util.List;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class BuscarUsuariosPorNomeUseCase {
    private final IUsuarioGateway usuarioGateway;

    private BuscarUsuariosPorNomeUseCase(IUsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public static BuscarUsuariosPorNomeUseCase create(IUsuarioGateway usuarioGateway) {
        return new BuscarUsuariosPorNomeUseCase(usuarioGateway);
    }

    public List<Usuario> execute(String nome) {
        return this.usuarioGateway.buscarPorNome(nome);
    }
}
