package br.com.fiap.core.usecases;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.gateways.IRestauranteGateway;

public class CadastrarRestauranteUseCase {
    private final IRestauranteGateway restauranteGateway;

    private CadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public static CadastrarRestauranteUseCase create(IRestauranteGateway restauranteGateway) {
        return new CadastrarRestauranteUseCase(restauranteGateway);
    }

    public Restaurante execute(Restaurante novoRestaurante) {
        return this.restauranteGateway.incluir(novoRestaurante);
    }
}
