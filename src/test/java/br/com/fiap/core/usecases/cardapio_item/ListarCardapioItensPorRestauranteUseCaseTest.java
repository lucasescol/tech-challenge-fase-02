package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.usecases.cardapio_item.ListarCardapioItensPorRestauranteUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarCardapioItensPorRestauranteUseCase - Testes Unitários")
class ListarCardapioItensPorRestauranteUseCaseTest {

    @Mock
    private ICardapioItemGateway cardapioItemGateway;

    private ListarCardapioItensPorRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ListarCardapioItensPorRestauranteUseCase.create(cardapioItemGateway);
    }

    @Test
    @DisplayName("Deve listar itens do cardápio por restaurante com sucesso")
    void deveListarItensPorRestauranteComSucesso() {
        CardapioItem item1 = CardapioItem.criar(
                1L,
                1L,
                "Pizza Margherita",
                "Deliciosa pizza com molho de tomate, mussarela e manjericão fresco",
                45.90,
                false,
                "/images/pizza-margherita.jpg");

        CardapioItem item2 = CardapioItem.criar(
                2L,
                1L,
                "Lasanha Bolonhesa",
                "Lasanha tradicional com molho bolonhesa e queijo gratinado",
                38.50,
                true,
                "/images/lasanha.jpg");

        List<CardapioItem> itens = Arrays.asList(item1, item2);
        when(cardapioItemGateway.listarPorRestaurante(1L)).thenReturn(itens);

        List<OutputModel> resultado = useCase.execute(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).nome()).isEqualTo("Pizza Margherita");
        assertThat(resultado.get(0).preco()).isEqualTo(45.90);
        assertThat(resultado.get(0).apenasPresencial()).isFalse();

        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).nome()).isEqualTo("Lasanha Bolonhesa");
        assertThat(resultado.get(1).preco()).isEqualTo(38.50);
        assertThat(resultado.get(1).apenasPresencial()).isTrue();

        verify(cardapioItemGateway).listarPorRestaurante(1L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando restaurante não tem itens no cardápio")
    void deveRetornarListaVaziaQuandoRestauranteNaoTemItens() {
        when(cardapioItemGateway.listarPorRestaurante(999L)).thenReturn(Collections.emptyList());

        List<OutputModel> resultado = useCase.execute(999L);

        assertThat(resultado).isEmpty();
        verify(cardapioItemGateway).listarPorRestaurante(999L);
    }

    @Test
    @DisplayName("Deve mapear corretamente todos os campos do item")
    void deveMapejarCorretamenteTodosCampos() {
        CardapioItem item = CardapioItem.criar(
                1L,
                5L,
                "Tiramisu",
                "Sobremesa italiana com café, mascarpone e cacau em pó",
                22.90,
                false,
                "/images/tiramisu.jpg");

        when(cardapioItemGateway.listarPorRestaurante(5L)).thenReturn(Collections.singletonList(item));

        List<OutputModel> resultado = useCase.execute(5L);

        assertThat(resultado).hasSize(1);
        OutputModel output = resultado.get(0);
        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.restauranteId()).isEqualTo(5L);
        assertThat(output.nome()).isEqualTo("Tiramisu");
        assertThat(output.descricao()).isEqualTo("Sobremesa italiana com café, mascarpone e cacau em pó");
        assertThat(output.preco()).isEqualTo(22.90);
        assertThat(output.apenasPresencial()).isFalse();
        assertThat(output.caminhoFoto()).isEqualTo("/images/tiramisu.jpg");
    }

    @Test
    @DisplayName("Deve listar múltiplos itens com diferentes características")
    void deveListarMultiplosItensComDiferentesCaracteristicas() {
        CardapioItem itemPresencial = CardapioItem.criar(
                1L,
                1L,
                "Rodízio de Pizza",
                "Rodízio completo com diversas opções de pizzas",
                59.90,
                true,
                "/images/rodizio.jpg");

        CardapioItem itemDelivery = CardapioItem.criar(
                2L,
                1L,
                "Pizza Individual",
                "Pizza individual para delivery",
                25.90,
                false,
                "/images/pizza-individual.jpg");

        List<CardapioItem> itens = Arrays.asList(itemPresencial, itemDelivery);
        when(cardapioItemGateway.listarPorRestaurante(1L)).thenReturn(itens);

        List<OutputModel> resultado = useCase.execute(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).apenasPresencial()).isTrue();
        assertThat(resultado.get(1).apenasPresencial()).isFalse();
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        ListarCardapioItensPorRestauranteUseCase novoUseCase = ListarCardapioItensPorRestauranteUseCase
                .create(cardapioItemGateway);

        assertThat(novoUseCase).isNotNull();
    }
}