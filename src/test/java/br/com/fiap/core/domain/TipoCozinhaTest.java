package br.com.fiap.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.core.exceptions.TipoCozinhaInvalidaException;

@DisplayName("TipoCozinha Value Object Tests")
class TipoCozinhaTest {

    @Test
    @DisplayName("Deve criar tipo de cozinha válido")
    void deveCriarTipoCozinhaValido() {
        TipoCozinha tipoCozinha = new TipoCozinha("ITALIANA");

        assertNotNull(tipoCozinha);
        assertEquals("ITALIANA", tipoCozinha.getValor());
    }

    @Test
    @DisplayName("Deve aceitar tipos de cozinha válidos")
    void deveAceitarTiposCozinhaValidos() {
        assertDoesNotThrow(() -> new TipoCozinha("ITALIANA"));
        assertDoesNotThrow(() -> new TipoCozinha("BRASILEIRA"));
        assertDoesNotThrow(() -> new TipoCozinha("JAPONESA"));
        assertDoesNotThrow(() -> new TipoCozinha("CHINESA"));
        assertDoesNotThrow(() -> new TipoCozinha("MEXICANA"));
    }

    @Test
    @DisplayName("Deve lançar exceção para tipo de cozinha inválido")
    void deveLancarExcecaoParaTipoCozinhaInvalido() {
        assertThrows(TipoCozinhaInvalidaException.class,
                () -> new TipoCozinha("DESCONHECIDA"));
    }

    @Test
    @DisplayName("Deve lançar exceção para tipo de cozinha vazio")
    void deveLancarExcecaoParaTipoCozinhaVazio() {
        assertThrows(TipoCozinhaInvalidaException.class,
                () -> new TipoCozinha(""));
    }

    @Test
    @DisplayName("Deve lançar exceção para tipo de cozinha nulo")
    void deveLancarExcecaoParaTipoCozinhaNulo() {
        assertThrows(TipoCozinhaInvalidaException.class,
                () -> new TipoCozinha(null));
    }

    @Test
    @DisplayName("Deve normalizar tipo de cozinha para maiúscula")
    void deveNormalizarTipoCozinhaParaMaiuscula() {
        TipoCozinha tipoCozinha = new TipoCozinha("italiana");

        assertEquals("ITALIANA", tipoCozinha.getValor());
    }

    @Test
    @DisplayName("Deve comparar tipos de cozinha por igualdade")
    void deveCompararTiposCozinhaPorIgualdade() {
        TipoCozinha tipoCozinha1 = new TipoCozinha("ITALIANA");
        TipoCozinha tipoCozinha2 = new TipoCozinha("ITALIANA");

        assertEquals(tipoCozinha1, tipoCozinha2);
    }

    @Test
    @DisplayName("Deve diferenciar tipos de cozinha diferentes")
    void deveDiferenciarTiposCozinhaDiferentes() {
        TipoCozinha tipoCozinha1 = new TipoCozinha("ITALIANA");
        TipoCozinha tipoCozinha2 = new TipoCozinha("JAPONESA");

        assertNotEquals(tipoCozinha1, tipoCozinha2);
    }
}
