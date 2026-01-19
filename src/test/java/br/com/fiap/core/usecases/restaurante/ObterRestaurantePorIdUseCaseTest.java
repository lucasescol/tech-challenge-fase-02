package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.exceptions.RestauranteNaoEncontradoException;
import br.com.fiap.core.gateways.IRestauranteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterRestaurantePorIdUseCase - Testes Unitários")
class ObterRestaurantePorIdUseCaseTest {

    @Mock
    private IRestauranteGateway restauranteGateway;

    private ObterRestaurantePorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterRestaurantePorIdUseCase.create(restauranteGateway);
    }

    @Test
    @DisplayName("Deve obter restaurante por ID com sucesso")
    void deveObterRestaurantePorIdComSucesso() {
        Restaurante restaurante = Restaurante.create(
                1L,
                "Restaurante Italiano",
                "Rua A",
                "100",
                "Sala 1",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));

        var resultado = useCase.execute(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
    }

    @Test
    @DisplayName("Deve retornar vazio quando restaurante não existe")
    void deveRetornarVazioQuandoRestauranteNaoExiste() {
        Long id = 1L;
        when(restauranteGateway.obterPorId(id)).thenReturn(Optional.empty());
        assertThrows(RestauranteNaoEncontradoException.class, () -> useCase.execute(id));
        verify(restauranteGateway).obterPorId(id);

    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        ObterRestaurantePorIdUseCase novoUseCase = ObterRestaurantePorIdUseCase.create(restauranteGateway);

        assertThat(novoUseCase).isNotNull();
    }
}