package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.gateways.IRestauranteGateway;

import java.util.List;
import java.util.stream.Collectors;

public class ListarTodosRestaurantesUseCase {

    private final IRestauranteGateway restauranteGateway;

    private ListarTodosRestaurantesUseCase(IRestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }
    
    public static ListarTodosRestaurantesUseCase create(IRestauranteGateway restauranteGateway) {
        return new ListarTodosRestaurantesUseCase(restauranteGateway);
    }

    public List<OutputModel> execute() {
        return restauranteGateway.listarTodos()
                .stream()
                .map(restaurante -> new OutputModel(
                    restaurante.getId(),
                    restaurante.getNome(),
                    restaurante.getEndereco().getEnderecoCompleto(),
                    restaurante.getTipoCozinha().getValor(),
                    restaurante.getHorarioFuncionamento().getValor()
                ))
                .collect(Collectors.toList());
    }

    public record OutputModel(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento
    ) {}
}
