package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.usecases.cardapio_item.ListarTodosCardapioItensUseCase.OutputModel;
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
@DisplayName("ListarTodosCardapioItensUseCase - Testes Unitários")
class ListarTodosCardapioItensUseCaseTest {

    @Mock
    private ICardapioItemGateway cardapioItemGateway;

    private ListarTodosCardapioItensUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ListarTodosCardapioItensUseCase.create(cardapioItemGateway);
    }

    @Test
    @DisplayName("Deve listar todos os itens do cardápio com sucesso")
    void deveListarTodosItensComSucesso() {
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
                2L,
                "Sushi Combinado",
                "Combinado com 20 peças variadas de sushi e sashimi",
                78.90,
                false,
                "/images/sushi.jpg");

        CardapioItem item3 = CardapioItem.criar(
                3L,
                1L,
                "Lasanha Bolonhesa",
                "Lasanha tradicional com molho bolonhesa e queijo gratinado",
                38.50,
                true,
                "/images/lasanha.jpg");

        List<CardapioItem> itens = Arrays.asList(item1, item2, item3);
        when(cardapioItemGateway.listarTodos()).thenReturn(itens);

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).restauranteId()).isEqualTo(1L);
        assertThat(resultado.get(0).nome()).isEqualTo("Pizza Margherita");

        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).restauranteId()).isEqualTo(2L);
        assertThat(resultado.get(1).nome()).isEqualTo("Sushi Combinado");

        assertThat(resultado.get(2).id()).isEqualTo(3L);
        assertThat(resultado.get(2).restauranteId()).isEqualTo(1L);
        assertThat(resultado.get(2).nome()).isEqualTo("Lasanha Bolonhesa");

        verify(cardapioItemGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há itens cadastrados")
    void deveRetornarListaVaziaQuandoNaoHaItens() {
        when(cardapioItemGateway.listarTodos()).thenReturn(Collections.emptyList());

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).isEmpty();
        verify(cardapioItemGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve mapear corretamente todos os campos dos itens")
    void deveMapejarCorretamenteTodosCampos() {
        CardapioItem item = CardapioItem.criar(
                10L,
                5L,
                "Feijoada Completa",
                "Feijoada tradicional brasileira com todos os acompanhamentos",
                65.00,
                true,
                "/images/feijoada.jpg");

        when(cardapioItemGateway.listarTodos()).thenReturn(Collections.singletonList(item));

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).hasSize(1);
        OutputModel output = resultado.get(0);
        assertThat(output.id()).isEqualTo(10L);
        assertThat(output.restauranteId()).isEqualTo(5L);
        assertThat(output.nome()).isEqualTo("Feijoada Completa");
        assertThat(output.descricao()).isEqualTo("Feijoada tradicional brasileira com todos os acompanhamentos");
        assertThat(output.preco()).isEqualTo(65.00);
        assertThat(output.apenasPresencial()).isTrue();
        assertThat(output.caminhoFoto()).isEqualTo("/images/feijoada.jpg");
    }

    @Test
    @DisplayName("Deve listar itens de diferentes restaurantes")
    void deveListarItensDeDiferentesRestaurantes() {
        CardapioItem itemRest1 = CardapioItem.criar(
                1L,
                1L,
                "Pizza Calabresa",
                "Pizza com calabresa, cebola e mussarela",
                42.90,
                false,
                "/images/pizza-calabresa.jpg");

        CardapioItem itemRest2 = CardapioItem.criar(
                2L,
                2L,
                "Hamburguer Artesanal",
                "Hamburguer com blend especial e ingredientes selecionados",
                35.90,
                false,
                "/images/hamburguer.jpg");

        CardapioItem itemRest3 = CardapioItem.criar(
                3L,
                3L,
                "Sashimi de Salmão",
                "Sashimi fresco de salmão com wasabi e shoyu",
                55.00,
                false,
                "/images/sashimi.jpg");

        List<CardapioItem> itens = Arrays.asList(itemRest1, itemRest2, itemRest3);
        when(cardapioItemGateway.listarTodos()).thenReturn(itens);

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).hasSize(3);
        assertThat(resultado).extracting(OutputModel::restauranteId)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Deve listar itens com diferentes disponibilidades")
    void deveListarItensComDiferentesDisponibilidades() {
        CardapioItem itemPresencial = CardapioItem.criar(
                1L,
                1L,
                "Rodízio de Carnes",
                "Rodízio completo com cortes nobres",
                89.90,
                true,
                "/images/rodizio-carnes.jpg");

        CardapioItem itemDelivery = CardapioItem.criar(
                2L,
                1L,
                "Marmita Executiva",
                "Marmita completa para delivery",
                28.90,
                false,
                "/images/marmita.jpg");

        List<CardapioItem> itens = Arrays.asList(itemPresencial, itemDelivery);
        when(cardapioItemGateway.listarTodos()).thenReturn(itens);

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).apenasPresencial()).isTrue();
        assertThat(resultado.get(1).apenasPresencial()).isFalse();
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        ListarTodosCardapioItensUseCase novoUseCase = ListarTodosCardapioItensUseCase.create(cardapioItemGateway);

        assertThat(novoUseCase).isNotNull();
    }
}