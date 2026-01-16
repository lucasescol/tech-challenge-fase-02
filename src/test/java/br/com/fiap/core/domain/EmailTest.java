package br.com.fiap.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.core.exceptions.EmailInvalidoException;

@DisplayName("Email Value Object Tests")
class EmailTest {

    @Test
    @DisplayName("Deve criar email válido com sucesso")
    void deveCriarEmailValido() {
        Email email = new Email("usuario@example.com");

        assertNotNull(email);
        assertEquals("usuario@example.com", email.getValor());
    }

    @Test
    @DisplayName("Deve normalizar email para minúscula")
    void deveNormalizarEmailParaMinuscula() {
        Email email = new Email("USUARIO@EXAMPLE.COM");

        assertEquals("usuario@example.com", email.getValor());
    }

    @Test
    @DisplayName("Deve remover espaços em branco")
    void deveRemoverEspacosEmBranco() {
        Email email = new Email("  usuario@example.com  ");

        assertEquals("usuario@example.com", email.getValor());
    }

    @Test
    @DisplayName("Deve lançar exceção para email vazio")
    void deveLancarExcecaoEmailVazio() {
        assertThrows(EmailInvalidoException.class, () -> new Email(""));
    }

    @Test
    @DisplayName("Deve lançar exceção para email nulo")
    void deveLancarExcecaoEmailNulo() {
        assertThrows(EmailInvalidoException.class, () -> new Email(null));
    }

    @Test
    @DisplayName("Deve lançar exceção para email sem @")
    void deveLancarExcecaoEmailSemArroba() {
        assertThrows(EmailInvalidoException.class, () -> new Email("usuarioexample.com"));
    }

    @Test
    @DisplayName("Deve lançar exceção para email sem domínio")
    void deveLancarExcecaoEmailSemDominio() {
        assertThrows(EmailInvalidoException.class, () -> new Email("usuario@"));
    }

    @Test
    @DisplayName("Deve lançar exceção para email com domínio inválido")
    void deveLancarExcecaoEmailComDominioInvalido() {
        assertThrows(EmailInvalidoException.class, () -> new Email("usuario@example"));
    }

    @Test
    @DisplayName("Deve comparar emails por igualdade de valor")
    void deveCompararEmailsPorIgualdadeDeValor() {
        Email email1 = new Email("usuario@example.com");
        Email email2 = new Email("usuario@example.com");

        assertEquals(email1, email2);
    }

    @Test
    @DisplayName("Deve diferenciar emails com valores diferentes")
    void deveDiferenciarEmailsComValoresDiferentes() {
        Email email1 = new Email("usuario1@example.com");
        Email email2 = new Email("usuario2@example.com");

        assertNotEquals(email1, email2);
    }
}
