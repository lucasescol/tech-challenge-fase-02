package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.gateways.ITipoUsuarioGateway;

import java.util.Optional;

public class ObterTipoUsuarioPorIdUseCase {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    private ObterTipoUsuarioPorIdUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }
    
    public static ObterTipoUsuarioPorIdUseCase create(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new ObterTipoUsuarioPorIdUseCase(tipoUsuarioGateway);
    }

    public Optional<OutputModel> execute(Long id) {
        return tipoUsuarioGateway.obterPorId(id)
                .map(tipoUsuario -> new OutputModel(
                    tipoUsuario.getId(),
                    tipoUsuario.getNome(),
                    tipoUsuario.getDescricao()
                ));
    }

    public record OutputModel(Long id, String nome, String descricao) {}
}
