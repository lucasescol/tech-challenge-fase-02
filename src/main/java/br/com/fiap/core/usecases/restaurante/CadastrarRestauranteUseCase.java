package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.gateways.IRestauranteGateway;

public class CadastrarRestauranteUseCase {
    private final IRestauranteGateway restauranteGateway;

    public record InputModel(
        String nome,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        String tipoCozinha,
        String horarioFuncionamento
    ) {}

    private CadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public static CadastrarRestauranteUseCase create(IRestauranteGateway restauranteGateway) {
        return new CadastrarRestauranteUseCase(restauranteGateway);
    }

    public Restaurante execute(InputModel input) {
        Restaurante novoRestaurante = Restaurante.create(
            null,
            input.nome(),
            input.logradouro(),
            input.numero(),
            input.complemento(),
            input.bairro(),
            input.cidade(),
            input.estado(),
            input.cep(),
            input.tipoCozinha(),
            input.horarioFuncionamento(),
            null
        );
        return this.restauranteGateway.incluir(novoRestaurante);
    }
}
