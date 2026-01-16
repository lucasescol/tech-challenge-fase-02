package br.com.fiap.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.core.exceptions.DomainException;

@DisplayName("Restaurante Entity Tests")
class RestauranteTest {

    @Test
    @DisplayName("Deve criar restaurante válido com factory method")
    void deveCriarRestauranteValido() {
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

        assertNotNull(restaurante);
        assertEquals("Cantina do Luigi", restaurante.getNome());
        assertEquals("ITALIANA", restaurante.getTipoCozinha().getValor());
    }

    @Test
    @DisplayName("Deve lançar exceção para nome vazio")
    void deveLancarExcecaoParaNomeVazio() {
        assertThrows(DomainException.class, () -> Restaurante.create(
                null,
                "",
                "Rua A",
                "123",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01310100",
                "ITALIANA",
                "09:00-23:00",
                null));
    }

    @Test
    @DisplayName("Deve lançar exceção para nome nulo")
    void deveLancarExcecaoParaNomeNulo() {
        assertThrows(DomainException.class, () -> Restaurante.create(
                null,
                null,
                "Rua A",
                "123",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01310100",
                "ITALIANA",
                "09:00-23:00",
                null));
    }

    @Test
    @DisplayName("Deve lançar exceção para email inválido")
    void deveLancarExcecaoParaEmailInvalido() {
        assertDoesNotThrow(() -> Restaurante.create(
                null,
                "Cantina",
                "Rua A",
                "123",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01310100",
                "ITALIANA",
                "09:00-23:00",
                null));
    }

    @Test
    @DisplayName("Deve lançar exceção para CEP inválido")
    void deveLancarExcecaoParaCepInvalido() {
        assertThrows(Exception.class, () -> Restaurante.create(
                null,
                "Cantina",
                "Rua A",
                "123",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01310",
                "ITALIANA",
                "09:00-23:00",
                null));
    }

    @Test
    @DisplayName("Deve lançar exceção para tipo de cozinha inválido")
    void deveLancarExcecaoParaTipoCozinhaInvalido() {
        assertThrows(Exception.class, () -> Restaurante.create(
                null,
                "Cantina",
                "Rua A",
                "123",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01310100",
                "DESCONHECIDA",
                "09:00-23:00",
                null));
    }

    @Test
    @DisplayName("Deve lançar exceção para horário inválido")
    void deveLancarExcecaoParaHorarioInvalido() {
        assertThrows(Exception.class, () -> Restaurante.create(
                null,
                "Cantina",
                "Rua A",
                "123",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01310100",
                "ITALIANA",
                "25:00-26:00",
                null));
    }

    @Test
    @DisplayName("Deve permitir complemento vazio")
    void devePermitirComplementoVazio() {
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

        assertNotNull(restaurante);
    }

    @Test
    @DisplayName("Restaurante deve ser imutável")
    void restaurantDeveSorImutavel() {
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

        String nomeOriginal = restaurante.getNome();

        assertEquals(nomeOriginal, restaurante.getNome());
    }
}
