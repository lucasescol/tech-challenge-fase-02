package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.EmailInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Email - Testes de Value Object")
class EmailTest {

    @Test
    @DisplayName("Deve criar email válido")
    void deveCriarEmailValido() {
        Email email = new Email("usuario@exemplo.com");

        assertThat(email).isNotNull();
        assertThat(email.getValor()).isEqualTo("usuario@exemplo.com");
    }

    @Test
    @DisplayName("Deve normalizar email para minúsculas")
    void deveNormalizarEmailParaMinusculas() {
        Email email = new Email("USUARIO@EXEMPLO.COM");

        assertThat(email.getValor()).isEqualTo("usuario@exemplo.com");
    }

    @Test
    @DisplayName("Deve fazer trim no email")
    void deveFazerTrimNoEmail() {
        Email email = new Email("  usuario@exemplo.com  ");

        assertThat(email.getValor()).isEqualTo("usuario@exemplo.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "usuario@exemplo.com",
            "nome.sobrenome@empresa.com.br",
            "user123@test.co",
            "test_user@example.org",
            "user-name@sub.domain.com"
    })
    @DisplayName("Deve aceitar emails válidos")
    void deveAceitarEmailsValidos(String emailValido) {
        Email email = new Email(emailValido);
        assertThat(email.getValor()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é nulo")
    void deveLancarExcecaoQuandoEmailNulo() {
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(EmailInvalidoException.class)
                .hasMessageContaining("Email não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é vazio")
    void deveLancarExcecaoQuandoEmailVazio() {
        assertThatThrownBy(() -> new Email(""))
                .isInstanceOf(EmailInvalidoException.class)
                .hasMessageContaining("Email não pode ser vazio");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "email-sem-arroba",
            "@exemplo.com",
            "usuario@",
            "usuario @exemplo.com",
            "usuario@exemplo",
            "usuario@@exemplo.com"
    })
    @DisplayName("Deve lançar exceção para emails inválidos")
    void deveLancarExcecaoParaEmailsInvalidos(String emailInvalido) {
        assertThatThrownBy(() -> new Email(emailInvalido))
                .isInstanceOf(EmailInvalidoException.class)
                .hasMessageContaining("Email inválido");
    }

    @Test
    @DisplayName("Deve verificar igualdade entre emails")
    void deveVerificarIgualdade() {
        Email email1 = new Email("usuario@exemplo.com");
        Email email2 = new Email("usuario@exemplo.com");

        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        Email email = new Email("usuario@exemplo.com");

        String toString = email.toString();

        assertThat(toString).isEqualTo("usuario@exemplo.com");
    }
}