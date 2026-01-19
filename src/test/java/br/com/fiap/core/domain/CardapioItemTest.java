package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CardapioItem - Testes de Domínio")
class CardapioItemTest {

    @Test
    @DisplayName("Deve criar item do cardápio com sucesso")
    void deveCriarItemComSucesso() {
        CardapioItem item = CardapioItem.criar(
                1L,
                10L,
                "Pizza Margherita",
                "Pizza tradicional italiana com molho de tomate e queijo",
                45.90,
                false,
                "/images/pizza.jpg");

        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getRestauranteId()).isEqualTo(10L);
        assertThat(item.getNome()).isEqualTo("Pizza Margherita");
        assertThat(item.getDescricao()).isEqualTo("Pizza tradicional italiana com molho de tomate e queijo");
        assertThat(item.getPreco()).isEqualTo(45.90);
        assertThat(item.isApenasPresencial()).isFalse();
        assertThat(item.getCaminhoFoto()).isEqualTo("/images/pizza.jpg");
    }

    @Test
    @DisplayName("Deve criar item sem caminho de foto")
    void deveCriarItemSemFoto() {
        CardapioItem item = CardapioItem.criar(
                1L,
                10L,
                "Sushi",
                "Combinado especial de sushi e sashimi",
                89.90,
                true,
                null);

        assertThat(item.getCaminhoFoto()).isEmpty();
    }

    @Test
    @DisplayName("Deve refatorar nome e descrição")
    void deveRefatorarCampos() {
        CardapioItem item = CardapioItem.criar(
                1L,
                10L,
                "  Pizza Margherita  ",
                "  Descrição com espaços  ",
                45.90,
                false,
                "  /images/pizza.jpg  ");

        assertThat(item.getNome()).isEqualTo("Pizza Margherita");
        assertThat(item.getDescricao()).isEqualTo("Descrição com espaços");
        assertThat(item.getCaminhoFoto()).isEqualTo("/images/pizza.jpg");
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do restaurante é nulo")
    void deveLancarExcecaoQuandoRestauranteIdNulo() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, null, "Pizza", "Descrição da pizza", 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("ID do restaurante é obrigatório");
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do restaurante é menor ou igual a zero")
    void deveLancarExcecaoQuandoRestauranteIdInvalido() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 0L, "Pizza", "Descrição da pizza", 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("ID do restaurante é obrigatório e deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, null, "Descrição teste", 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do item não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoCurto() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, "AB", "Descrição teste", 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do item deve ter no mínimo 3 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem mais de 100 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoLongo() {
        String nomeLongo = "A".repeat(101);

        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, nomeLongo, "Descrição teste", 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do item deve ter no máximo 100 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando descrição é nula")
    void deveLancarExcecaoQuandoDescricaoNula() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, "Pizza", null, 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Descrição do item não pode ser vazia");
    }

    @Test
    @DisplayName("Deve lançar exceção quando descrição tem menos de 10 caracteres")
    void deveLancarExcecaoQuandoDescricaoMuitoCurta() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, "Pizza", "Curta", 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Descrição do item deve ter no mínimo 10 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando descrição tem mais de 500 caracteres")
    void deveLancarExcecaoQuandoDescricaoMuitoLonga() {
        String descricaoLonga = "A".repeat(501);

        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, "Pizza", descricaoLonga, 45.90, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Descrição do item deve ter no máximo 500 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando preço é nulo")
    void deveLancarExcecaoQuandoPrecoNulo() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, "Pizza", "Descrição da pizza", null, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção quando preço é menor ou igual a zero")
    void deveLancarExcecaoQuandoPrecoInvalido() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, "Pizza", "Descrição da pizza", 0.0, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve lançar exceção quando preço é maior que 999999.99")
    void deveLancarExcecaoQuandoPrecoMuitoAlto() {
        assertThatThrownBy(() -> CardapioItem.criar(
                1L, 10L, "Pizza", "Descrição da pizza", 1000000.00, false, ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Preço deve ser no máximo 999999.99");
    }

    @Test
    @DisplayName("Deve criar item com preço no limite máximo")
    void deveCriarItemComPrecoLimiteMaximo() {
        CardapioItem item = CardapioItem.criar(
                1L, 10L, "Pizza", "Descrição da pizza", 999999.99, false, "");

        assertThat(item.getPreco()).isEqualTo(999999.99);
    }

    @Test
    @DisplayName("Deve verificar igualdade entre itens")
    void deveVerificarIgualdade() {
        CardapioItem item1 = CardapioItem.criar(
                1L, 10L, "Pizza", "Descrição da pizza", 45.90, false, "/images/pizza.jpg");

        CardapioItem item2 = CardapioItem.criar(
                1L, 10L, "Pizza", "Descrição da pizza", 45.90, false, "/images/pizza.jpg");

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        CardapioItem item = CardapioItem.criar(
                1L, 10L, "Pizza", "Descrição da pizza", 45.90, true, "");

        String toString = item.toString();

        assertThat(toString)
                .contains("id=1")
                .contains("restauranteId=10")
                .contains("nome='Pizza'")
                .contains("preco=45.9")
                .contains("apenasPresencial=true");
    }
}