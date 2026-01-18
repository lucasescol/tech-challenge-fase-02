package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.exceptions.RestauranteNaoEncontradoException;
import br.com.fiap.core.gateways.IRestauranteGateway;

public class ObterRestaurantePorIdUseCase {

    private final IRestauranteGateway restauranteGateway;

    private ObterRestaurantePorIdUseCase(IRestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
    
    public static ObterRestaurantePorIdUseCase create(IRestauranteGateway restauranteGateway) {
        return new ObterRestaurantePorIdUseCase(restauranteGateway);
    }

    public OutputModel execute(Long id) {
        return restauranteGateway.obterPorId(id)
                .map(restaurante -> new OutputModel(
                    restaurante.getId(),
                    restaurante.getNome(),
                    restaurante.getEndereco().getEnderecoCompleto(),
                    restaurante.getTipoCozinha().getValor(),
                    restaurante.getHorarioFuncionamento().getValor()
                ))
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
    }

    public record OutputModel(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento
    ) {}
}
