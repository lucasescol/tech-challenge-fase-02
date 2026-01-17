package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.gateways.IRestauranteGateway;

import java.util.Optional;

public class ObterRestaurantePorIdUseCase {

    private final IRestauranteGateway restauranteGateway;

    private ObterRestaurantePorIdUseCase(IRestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
    
    public static ObterRestaurantePorIdUseCase create(IRestauranteGateway restauranteGateway) {
        return new ObterRestaurantePorIdUseCase(restauranteGateway);
    }

    public Optional<OutputModel> execute(Long id) {
        return restauranteGateway.obterPorId(id)
                .map(restaurante -> new OutputModel(
                    restaurante.getId(),
                    restaurante.getNome(),
                    restaurante.getEndereco().getEnderecoCompleto(),
                    restaurante.getTipoCozinha().getValor(),
                    restaurante.getHorarioFuncionamento().getValor()
                ));
    }

    public record OutputModel(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento
    ) {}
}
