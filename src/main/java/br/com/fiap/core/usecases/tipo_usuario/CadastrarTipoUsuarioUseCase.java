package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;

public class CadastrarTipoUsuarioUseCase {

    private final ITipoUsuarioGateway tipoUsuarioGateway;

    private CadastrarTipoUsuarioUseCase(ITipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }
    
    public static CadastrarTipoUsuarioUseCase create(ITipoUsuarioGateway tipoUsuarioGateway) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuarioGateway);
    }

    public OutputModel execute(InputModel input) {
        TipoUsuario tipoUsuario = TipoUsuario.create(null, input.nome(), input.descricao());
        TipoUsuario tipoUsuarioSalvo = tipoUsuarioGateway.incluir(tipoUsuario);
        
        return new OutputModel(
            tipoUsuarioSalvo.getId(),
            tipoUsuarioSalvo.getNome(),
            tipoUsuarioSalvo.getDescricao()
        );
    }

    public record InputModel(String nome, String descricao) {}

    public record OutputModel(Long id, String nome, String descricao) {}
}
