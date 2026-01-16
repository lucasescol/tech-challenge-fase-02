package br.com.fiap.core.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.gateways.IRestauranteGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarRestauranteUseCase Tests")
class CadastrarRestauranteTest {

        @Mock
        private IRestauranteGateway restauranteGatewayMock;

        private CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

        @BeforeEach
        void setup() {
                cadastrarRestauranteUseCase = CadastrarRestauranteUseCase.create(restauranteGatewayMock);
        }

        @Test
        @DisplayName("Deve cadastrar restaurante com sucesso")
        void deveCadastrarRestauranteComSucesso() {
                Restaurante restaurante = Restaurante.create(
                                null,
                                "Cantina do Luigi",
                                "Rua A",
                                "123",
                                "Apt 456",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01310100",
                                "ITALIANA",
                                "09:00-23:00",
                                null);

                Restaurante restauranteSalvo = Restaurante.create(
                                1L,
                                "Cantina do Luigi",
                                "Rua A",
                                "123",
                                "Apt 456",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01310100",
                                "ITALIANA",
                                "09:00-23:00",
                                null);

                when(restauranteGatewayMock.incluir(restaurante)).thenReturn(restauranteSalvo);

                Restaurante resultado = cadastrarRestauranteUseCase.execute(restaurante);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals("Cantina do Luigi", resultado.getNome());
                verify(restauranteGatewayMock, times(1)).incluir(restaurante);
        }

        @Test
        @DisplayName("Deve lançar exceção se gateway falhar")
        void deveLancarExcecaoSeGatewayFalhar() {
                Restaurante restaurante = Restaurante.create(
                                null,
                                "Cantina do Luigi",
                                "Rua A",
                                "123",
                                "Apt 456",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01310100",
                                "ITALIANA",
                                "09:00-23:00",
                                null);

                when(restauranteGatewayMock.incluir(restaurante))
                                .thenThrow(new RuntimeException("Erro ao persistir"));

                assertThrows(RuntimeException.class,
                                () -> cadastrarRestauranteUseCase.execute(restaurante));

                verify(restauranteGatewayMock, times(1)).incluir(restaurante);
        }

        @Test
        @DisplayName("Deve chamar gateway com restaurante correto")
        void deveCharmarGatewayComRestauranteCorreto() {
                Restaurante restaurante = Restaurante.create(
                                null,
                                "Cantina do Luigi",
                                "Rua A",
                                "123",
                                "",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01310100",
                                "ITALIANA",
                                "09:00-23:00",
                                null);

                Restaurante restauranteSalvo = Restaurante.create(
                                1L,
                                "Cantina do Luigi",
                                "Rua A",
                                "123",
                                "",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01310100",
                                "ITALIANA",
                                "09:00-23:00",
                                null);

                when(restauranteGatewayMock.incluir(any(Restaurante.class)))
                                .thenReturn(restauranteSalvo);

                Restaurante resultado = cadastrarRestauranteUseCase.execute(restaurante);

                assertNotNull(resultado);
                assertEquals("Cantina do Luigi", resultado.getNome());
                assertEquals(1L, resultado.getId());
        }
}
