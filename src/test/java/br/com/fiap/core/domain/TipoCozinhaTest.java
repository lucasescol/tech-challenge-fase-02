package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.TipoCozinhaInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TipoCozinha - Testes de Value Object")
class TipoCozinhaTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "ITALIANA", "JAPONESA", "BRASILEIRA", "CHINESA", "FRANCESA",
            "MEXICANA", "INDIANA", "ARABE", "VEGETARIANA", "VEGANA", "OUTRAS"
    })
    @DisplayName("Deve aceitar tipos de cozinha válidos")
    void deveAceitarTiposCozinhaValidos(String tipo) {
        TipoCozinha tipoCozinha = new TipoCozinha(tipo);

        assertThat(tipoCozinha.getValor()).isEqualTo(tipo.toUpperCase());
    }

    @Test
    @DisplayName("Deve normalizar tipo para maiúsculas")
    void deveNormalizarTipoParaMaiusculas() {
        TipoCozinha tipoCozinha = new TipoCozinha("italiana");

        assertThat(tipoCozinha.getValor()).isEqualTo("ITALIANA");
    }

    @Test
    @DisplayName("Deve fazer trim no tipo")
    void deveFazerTrimNoTipo() {
        TipoCozinha tipoCozinha = new TipoCozinha("  JAPONESA  ");

        assertThat(tipoCozinha.getValor()).isEqualTo("JAPONESA");
    }

    @Test
    @DisplayName("Deve aceitar tipo em minúsculas")
    void deveAceitarTipoMinusculas() {
        TipoCozinha tipoCozinha = new TipoCozinha("brasileira");

        assertThat(tipoCozinha.getValor()).isEqualTo("BRASILEIRA");
    }

    @Test
    @DisplayName("Deve aceitar tipo com mix de maiúsculas e minúsculas")
    void deveAceitarTipoMixCase() {
        TipoCozinha tipoCozinha = new TipoCozinha("JaPoNeSa");

        assertThat(tipoCozinha.getValor()).isEqualTo("JAPONESA");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo é nulo")
    void deveLancarExcecaoQuandoTipoNulo() {
        assertThatThrownBy(() -> new TipoCozinha(null))
                .isInstanceOf(TipoCozinhaInvalidaException.class)
                .hasMessageContaining("Tipo de cozinha não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo é vazio")
    void deveLancarExcecaoQuandoTipoVazio() {
        assertThatThrownBy(() -> new TipoCozinha(""))
                .isInstanceOf(TipoCozinhaInvalidaException.class)
                .hasMessageContaining("Tipo de cozinha não pode ser vazio");
    }

    @ParameterizedTest
    @ValueSource(strings = { "ALEMÃ", "PORTUGUESA", "TAILANDESA", "COREANA", "PERUANA" })
    @DisplayName("Deve lançar exceção para tipos inválidos")
    void deveLancarExcecaoParaTiposInvalidos(String tipoInvalido) {
        assertThatThrownBy(() -> new TipoCozinha(tipoInvalido))
                .isInstanceOf(TipoCozinhaInvalidaException.class)
                .hasMessageContaining("Tipo de cozinha inválido");
    }

    @Test
    @DisplayName("Deve verificar igualdade entre tipos de cozinha")
    void deveVerificarIgualdade() {
        TipoCozinha tipo1 = new TipoCozinha("ITALIANA");
        TipoCozinha tipo2 = new TipoCozinha("italiana");

        assertThat(tipo1).isEqualTo(tipo2);
        assertThat(tipo1.hashCode()).isEqualTo(tipo2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        TipoCozinha tipoCozinha = new TipoCozinha("JAPONESA");

        String toString = tipoCozinha.toString();

        assertThat(toString).isEqualTo("JAPONESA");
    }
}