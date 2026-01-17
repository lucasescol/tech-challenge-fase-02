package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.gateways.ITipoUsuarioGateway;

import java.util.List;
import java.util.stream.Collectors;

public class ListarTodosTiposUsuarioUseCase {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    private ListarTodosTiposUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }
    
    public static ListarTodosTiposUsuarioUseCase create(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new ListarTodosTiposUsuarioUseCase(tipoUsuarioGateway);
    }

    public List<OutputModel> execute() {
        return tipoUsuarioGateway.listarTodos()
                .stream()
                .map(tipoUsuario -> new OutputModel(
                    tipoUsuario.getId(),
                    tipoUsuario.getNome(),
                    tipoUsuario.getDescricao()
                ))
                .collect(Collectors.toList());
    }

    public record OutputModel(Long id, String nome, String descricao) {}
}
