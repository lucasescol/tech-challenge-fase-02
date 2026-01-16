package br.com.fiap.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.core.exceptions.EnderecoInvalidoException;

@DisplayName("Endereco Value Object Tests")
class EnderecoTest {

    @Test
    @DisplayName("Deve criar endereço válido com sucesso")
    void deveCriarEnderecoValido() {
        Endereco endereco = new Endereco("Rua A", "123", "Apt 456", "Centro", "São Paulo", "SP", "01310010");

        assertNotNull(endereco);
        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("123", endereco.getNumero());
        assertEquals("01310-010", endereco.getCep());
    }

    @Test
    @DisplayName("Deve lançar exceção para CEP inválido com 4 dígitos")
    void deveLancarExcecaoParaCepComQuatroDígitos() {
        assertThrows(EnderecoInvalidoException.class,
                () -> new Endereco("Rua A", "123", "", "Centro", "São Paulo", "SP", "0131"));
    }

    @Test
    @DisplayName("Deve lançar exceção para CEP inválido com 9 dígitos")
    void deveLancarExcecaoParaCepComNoveDígitos() {
        assertThrows(EnderecoInvalidoException.class,
                () -> new Endereco("Rua A", "123", "", "Centro", "São Paulo", "SP", "013100101"));
    }

    @Test
    @DisplayName("Deve lançar exceção para CEP vazio")
    void deveLancarExcecaoParaCepVazio() {
        assertThrows(EnderecoInvalidoException.class,
                () -> new Endereco("Rua A", "123", "", "Centro", "São Paulo", "SP", ""));
    }

    @Test
    @DisplayName("Deve lançar exceção para logradouro vazio")
    void deveLancarExcecaoParaLogradouroVazio() {
        assertThrows(EnderecoInvalidoException.class,
                () -> new Endereco("", "123", "", "Centro", "São Paulo", "SP", "01310010"));
    }

    @Test
    @DisplayName("Deve lançar exceção para número vazio")
    void deveLancarExcecaoParaNumeroVazio() {
        assertThrows(EnderecoInvalidoException.class,
                () -> new Endereco("Rua A", "", "", "Centro", "São Paulo", "SP", "01310010"));
    }

    @Test
    @DisplayName("Deve aceitar CEP com máscara (formatado)")
    void deveAceitarCepComMascara() {
        Endereco endereco = new Endereco("Rua A", "123", "", "Centro", "São Paulo", "SP", "01310010");

        assertEquals("01310-010", endereco.getCep());
    }

    @Test
    @DisplayName("Deve normalizar estado para maiúscula")
    void deveNormalizarEstadoParaMaiuscula() {
        Endereco endereco = new Endereco("Rua A", "123", "", "Centro", "São Paulo", "sp", "01310010");

        assertEquals("SP", endereco.getEstado());
    }

    @Test
    @DisplayName("Deve gerar endereço completo formatado")
    void deveGerarEnderecoCompletoFormatado() {
        Endereco endereco = new Endereco("Rua A", "123", "Apt 456", "Centro", "São Paulo", "SP", "01310010");
        String completo = endereco.getEnderecoCompleto();

        assertNotNull(completo);
        assertFalse(completo.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção para estado vazio")
    void deveLancarExcecaoParaEstadoVazio() {
        assertThrows(EnderecoInvalidoException.class,
                () -> new Endereco("Rua A", "123", "", "Centro", "São Paulo", "", "01310010"));
    }

    @Test
    @DisplayName("Deve lançar exceção para cidade vazia")
    void deveLancarExcecaoParaCidadeVazia() {
        assertThrows(EnderecoInvalidoException.class,
                () -> new Endereco("Rua A", "123", "", "Centro", "", "SP", "01310010"));
    }
}
