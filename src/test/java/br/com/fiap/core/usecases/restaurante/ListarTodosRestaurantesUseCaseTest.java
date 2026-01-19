package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.usecases.restaurante.ListarTodosRestaurantesUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarTodosRestaurantesUseCase - Testes Unitários")
class ListarTodosRestaurantesUseCaseTest {

    @Mock
    private IRestauranteGateway restauranteGateway;

    private ListarTodosRestaurantesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ListarTodosRestaurantesUseCase.create(restauranteGateway);
    }

    @Test
    @DisplayName("Deve listar todos os restaurantes com sucesso")
    void deveListarTodosRestaurantesComSucesso() {
        Restaurante restaurante1 = Restaurante.create(
                1L,
                "Restaurante Italiano",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        Restaurante restaurante2 = Restaurante.create(
                2L,
                "Restaurante Japonês",
                "Rua B",
                "200",
                "Loja 5",
                "Jardins",
                "São Paulo",
                "SP",
                "01400-000",
                "JAPONESA",
                "11:00-23:00",
                2L);

        when(restauranteGateway.listarTodos()).thenReturn(Arrays.asList(restaurante1, restaurante2));

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).nome()).isEqualTo("Restaurante Italiano");
        assertThat(resultado.get(0).tipoCozinha()).isEqualTo("ITALIANA");

        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).nome()).isEqualTo("Restaurante Japonês");
        assertThat(resultado.get(1).tipoCozinha()).isEqualTo("JAPONESA");

        verify(restauranteGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há restaurantes")
    void deveRetornarListaVaziaQuandoNaoHaRestaurantes() {
        when(restauranteGateway.listarTodos()).thenReturn(Collections.emptyList());

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).isEmpty();
        verify(restauranteGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        ListarTodosRestaurantesUseCase novoUseCase = ListarTodosRestaurantesUseCase.create(restauranteGateway);

        assertThat(novoUseCase).isNotNull();
    }
}