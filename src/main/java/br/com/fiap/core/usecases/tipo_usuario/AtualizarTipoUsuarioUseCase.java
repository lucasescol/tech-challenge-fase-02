package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;

import java.util.Optional;

public class AtualizarTipoUsuarioUseCase {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    private AtualizarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }
    
    public static AtualizarTipoUsuarioUseCase create(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new AtualizarTipoUsuarioUseCase(tipoUsuarioGateway);
    }

    public Optional<OutputModel> execute(Long id, InputModel input) {
        return tipoUsuarioGateway.obterPorId(id)
                .map(existente -> {
                    TipoUsuario tipoUsuarioAtualizado = TipoUsuario.create(id, input.nome(), input.descricao());
                    TipoUsuario tipoUsuarioSalvo = tipoUsuarioGateway.atualizar(tipoUsuarioAtualizado);
                    
                    return new OutputModel(
                        tipoUsuarioSalvo.getId(),
                        tipoUsuarioSalvo.getNome(),
                        tipoUsuarioSalvo.getDescricao()
                    );
                });
    }

    public record InputModel(String nome, String descricao) {}

    public record OutputModel(Long id, String nome, String descricao) {}
}
