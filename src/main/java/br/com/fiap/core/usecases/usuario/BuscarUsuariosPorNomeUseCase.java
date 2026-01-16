package br.com.fiap.core.usecases.usuario;

import java.util.List;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.gateways.IUsuarioGateway;

public class BuscarUsuariosPorNomeUseCase {

    private final IUsuarioGateway usuarioGateway;

    public BuscarUsuariosPorNomeUseCase(IUsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> executar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome para busca não pode ser vazio");
        }

        return this.usuarioGateway.buscarPorNome(nome.trim());
    }
}
