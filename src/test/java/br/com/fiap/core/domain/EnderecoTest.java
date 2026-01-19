package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.EnderecoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Endereco - Testes de Value Object")
class EnderecoTest {

    @Test
    @DisplayName("Deve criar endereço completo com sucesso")
    void deveCriarEnderecoCompletoComSucesso() {
        Endereco endereco = new Endereco(
                "Rua das Flores",
                "123",
                "Apto 45",
                "Centro",
                "São Paulo",
                "SP",
                "01234-567");

        assertThat(endereco).isNotNull();
        assertThat(endereco.getLogradouro()).isEqualTo("Rua das Flores");
        assertThat(endereco.getNumero()).isEqualTo("123");
        assertThat(endereco.getComplemento()).isEqualTo("Apto 45");
        assertThat(endereco.getBairro()).isEqualTo("Centro");
        assertThat(endereco.getCidade()).isEqualTo("São Paulo");
        assertThat(endereco.getEstado()).isEqualTo("SP");
        assertThat(endereco.getCep()).isEqualTo("01234-567");
    }

    @Test
    @DisplayName("Deve criar endereço sem complemento")
    void deveCriarEnderecoSemComplemento() {
        Endereco endereco = new Endereco(
                "Rua das Flores",
                "123",
                null,
                "Centro",
                "São Paulo",
                "SP",
                "01234567");

        assertThat(endereco.getComplemento()).isEmpty();
    }

    @Test
    @DisplayName("Deve criar endereço sem bairro")
    void deveCriarEnderecoSemBairro() {
        Endereco endereco = new Endereco(
                "Rua das Flores",
                "123",
                "",
                null,
                "São Paulo",
                "SP",
                "01234567");

        assertThat(endereco.getBairro()).isEmpty();
    }

    @Test
    @DisplayName("Deve normalizar estado para maiúsculas")
    void deveNormalizarEstadoParaMaiusculas() {
        Endereco endereco = new Endereco(
                "Rua A", "100", "", "", "São Paulo", "sp", "01000000");

        assertThat(endereco.getEstado()).isEqualTo("SP");
    }

    @Test
    @DisplayName("Deve formatar CEP corretamente")
    void deveFormatarCepCorretamente() {
        Endereco endereco = new Endereco(
                "Rua A", "100", "", "", "São Paulo", "SP", "01234567");

        assertThat(endereco.getCep()).isEqualTo("01234-567");
        assertThat(endereco.getCepLimpo()).isEqualTo("01234567");
    }

    @Test
    @DisplayName("Deve aceitar CEP com formatação")
    void deveAceitarCepComFormatacao() {
        Endereco endereco = new Endereco(
                "Rua A", "100", "", "", "São Paulo", "SP", "01234-567");

        assertThat(endereco.getCepLimpo()).isEqualTo("01234567");
    }

    @ParameterizedTest
    @ValueSource(strings = { "SP", "RJ", "MG", "RS", "BA", "PR", "SC", "PE", "CE", "DF" })
    @DisplayName("Deve aceitar UFs válidas")
    void deveAceitarUfsValidas(String uf) {
        Endereco endereco = new Endereco(
                "Rua A", "100", "", "", "Cidade", uf, "01234567");
        assertThat(endereco.getEstado()).isEqualTo(uf.toUpperCase());
    }

    @Test
    @DisplayName("Deve fazer trim em todos os campos")
    void deveFazerTrimEmTodosOsCampos() {
        Endereco endereco = new Endereco(
                "  Rua A  ",
                "  100  ",
                "  Apto 1  ",
                "  Centro  ",
                "  São Paulo  ",
                " SP ",
                " 01234567 ");

        assertThat(endereco.getLogradouro()).isEqualTo("Rua A");
        assertThat(endereco.getNumero()).isEqualTo("100");
        assertThat(endereco.getComplemento()).isEqualTo("Apto 1");
        assertThat(endereco.getBairro()).isEqualTo("Centro");
        assertThat(endereco.getCidade()).isEqualTo("São Paulo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando logradouro é nulo")
    void deveLancarExcecaoQuandoLogradouroNulo() {
        assertThatThrownBy(() -> new Endereco(
                null, "100", "", "", "São Paulo", "SP", "01234567"))
                .isInstanceOf(EnderecoInvalidoException.class)
                .hasMessageContaining("Logradouro não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando número é nulo")
    void deveLancarExcecaoQuandoNumeroNulo() {
        assertThatThrownBy(() -> new Endereco(
                "Rua A", null, "", "", "São Paulo", "SP", "01234567"))
                .isInstanceOf(EnderecoInvalidoException.class)
                .hasMessageContaining("Número não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando cidade é nula")
    void deveLancarExcecaoQuandoCidadeNula() {
        assertThatThrownBy(() -> new Endereco(
                "Rua A", "100", "", "", null, "SP", "01234567"))
                .isInstanceOf(EnderecoInvalidoException.class)
                .hasMessageContaining("Cidade não pode ser vazia");
    }

    @Test
    @DisplayName("Deve lançar exceção quando estado é nulo")
    void deveLancarExcecaoQuandoEstadoNulo() {
        assertThatThrownBy(() -> new Endereco(
                "Rua A", "100", "", "", "São Paulo", null, "01234567"))
                .isInstanceOf(EnderecoInvalidoException.class)
                .hasMessageContaining("Estado não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP é nulo")
    void deveLancarExcecaoQuandoCepNulo() {
        assertThatThrownBy(() -> new Endereco(
                "Rua A", "100", "", "", "São Paulo", "SP", null))
                .isInstanceOf(EnderecoInvalidoException.class)
                .hasMessageContaining("CEP não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando UF é inválida")
    void deveLancarExcecaoQuandoUfInvalida() {
        assertThatThrownBy(() -> new Endereco(
                "Rua A", "100", "", "", "São Paulo", "XX", "01234567"))
                .isInstanceOf(EnderecoInvalidoException.class)
                .hasMessageContaining("Estado inválido");
    }

    @Test
    @DisplayName("Deve lançar exceção quando CEP tem formato inválido")
    void deveLancarExcecaoQuandoCepInvalido() {
        assertThatThrownBy(() -> new Endereco(
                "Rua A", "100", "", "", "São Paulo", "SP", "123"))
                .isInstanceOf(EnderecoInvalidoException.class)
                .hasMessageContaining("CEP inválido");
    }

    @Test
    @DisplayName("Deve gerar endereço completo corretamente")
    void deveGerarEnderecoCompletoCorretamente() {
        Endereco endereco = new Endereco(
                "Rua das Flores",
                "123",
                "Apto 45",
                "Centro",
                "São Paulo",
                "SP",
                "01234567");

        String enderecoCompleto = endereco.getEnderecoCompleto();

        assertThat(enderecoCompleto)
                .contains("Rua das Flores")
                .contains("123")
                .contains("Apto 45")
                .contains("Centro")
                .contains("São Paulo")
                .contains("SP")
                .contains("01234-567");
    }

    @Test
    @DisplayName("Deve gerar endereço completo sem complemento")
    void deveGerarEnderecoCompletoSemComplemento() {
        Endereco endereco = new Endereco(
                "Rua das Flores",
                "123",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01234567");

        String enderecoCompleto = endereco.getEnderecoCompleto();

        assertThat(enderecoCompleto)
                .contains("Rua das Flores, 123")
                .doesNotContain(" - ,")
                .contains("Centro")
                .contains("São Paulo/SP");
    }

    @Test
    @DisplayName("Deve verificar igualdade entre endereços")
    void deveVerificarIgualdade() {
        Endereco endereco1 = new Endereco(
                "Rua A", "100", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        Endereco endereco2 = new Endereco(
                "Rua A", "100", "Apto 1", "Centro", "São Paulo", "SP", "01234567");

        assertThat(endereco1).isEqualTo(endereco2);
        assertThat(endereco1.hashCode()).isEqualTo(endereco2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString igual ao endereço completo")
    void deveGerarToStringIgualAoEnderecoCompleto() {
        Endereco endereco = new Endereco(
                "Rua A", "100", "", "Centro", "São Paulo", "SP", "01234567");

        assertThat(endereco.toString()).isEqualTo(endereco.getEnderecoCompleto());
    }
}