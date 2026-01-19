package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.usecases.cardapio_item.ObterCardapioItemPorIdUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterCardapioItemPorIdUseCase - Testes Unitários")
class ObterCardapioItemPorIdUseCaseTest {

    @Mock
    private ICardapioItemGateway cardapioItemGateway;

    private ObterCardapioItemPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterCardapioItemPorIdUseCase.create(cardapioItemGateway);
    }

    @Test
    @DisplayName("Deve obter item do cardápio por ID com sucesso")
    void deveObterItemPorIdComSucesso() {
        CardapioItem item = CardapioItem.criar(
                1L,
                1L,
                "Pizza Margherita",
                "Deliciosa pizza com molho de tomate, mussarela e manjericão fresco",
                45.90,
                false,
                "/images/pizza-margherita.jpg");

        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(item));

        Optional<OutputModel> resultado = useCase.execute(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().id()).isEqualTo(1L);
        assertThat(resultado.get().restauranteId()).isEqualTo(1L);
        assertThat(resultado.get().nome()).isEqualTo("Pizza Margherita");
        assertThat(resultado.get().descricao()).contains("Deliciosa pizza");
        assertThat(resultado.get().preco()).isEqualTo(45.90);
        assertThat(resultado.get().apenasPresencial()).isFalse();
        assertThat(resultado.get().caminhoFoto()).isEqualTo("/images/pizza-margherita.jpg");

        verify(cardapioItemGateway).obterPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio quando item não existe")
    void deveRetornarVazioQuandoItemNaoExiste() {
        when(cardapioItemGateway.obterPorId(999L)).thenReturn(Optional.empty());

        Optional<OutputModel> resultado = useCase.execute(999L);

        assertThat(resultado).isEmpty();
        verify(cardapioItemGateway).obterPorId(999L);
    }

    @Test
    @DisplayName("Deve mapear corretamente item apenas presencial")
    void deveMapejarCorretamenteItemApenasPresencial() {
        CardapioItem item = CardapioItem.criar(
                5L,
                3L,
                "Rodízio de Pizza",
                "Rodízio completo com diversas opções de pizzas artesanais",
                59.90,
                true,
                "/images/rodizio-pizza.jpg");

        when(cardapioItemGateway.obterPorId(5L)).thenReturn(Optional.of(item));

        Optional<OutputModel> resultado = useCase.execute(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().apenasPresencial()).isTrue();
        assertThat(resultado.get().preco()).isEqualTo(59.90);
    }

    @Test
    @DisplayName("Deve mapear corretamente item com todos os campos preenchidos")
    void deveMapejarCorretamenteItemCompleto() {
        CardapioItem item = CardapioItem.criar(
                10L,
                7L,
                "Lasanha Bolonhesa",
                "Lasanha tradicional italiana com molho bolonhesa caseiro",
                42.50,
                false,
                "/images/lasanha-bolonhesa.jpg");

        when(cardapioItemGateway.obterPorId(10L)).thenReturn(Optional.of(item));

        Optional<OutputModel> resultado = useCase.execute(10L);

        assertThat(resultado).isPresent();
        OutputModel output = resultado.get();
        assertThat(output.id()).isEqualTo(10L);
        assertThat(output.restauranteId()).isEqualTo(7L);
        assertThat(output.nome()).isEqualTo("Lasanha Bolonhesa");
        assertThat(output.descricao()).isEqualTo("Lasanha tradicional italiana com molho bolonhesa caseiro");
        assertThat(output.preco()).isEqualTo(42.50);
        assertThat(output.apenasPresencial()).isFalse();
        assertThat(output.caminhoFoto()).isEqualTo("/images/lasanha-bolonhesa.jpg");
    }

    @Test
    @DisplayName("Deve mapear corretamente item sem foto")
    void deveMapejarCorretamenteItemSemFoto() {
        CardapioItem item = CardapioItem.criar(
                2L,
                1L,
                "Suco Natural",
                "Suco natural de laranja fresquinho",
                8.50,
                false,
                "");

        when(cardapioItemGateway.obterPorId(2L)).thenReturn(Optional.of(item));

        Optional<OutputModel> resultado = useCase.execute(2L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().caminhoFoto()).isEmpty();
    }

    @Test
    @DisplayName("Deve obter itens de diferentes restaurantes")
    void deveObterItensDeDiferentesRestaurantes() {
        CardapioItem itemRest1 = CardapioItem.criar(
                1L,
                1L,
                "Pizza Calabresa",
                "Pizza com calabresa, cebola e azeitonas",
                42.90,
                false,
                "/images/pizza-calabresa.jpg");

        CardapioItem itemRest2 = CardapioItem.criar(
                2L,
                5L,
                "Hamburguer",
                "Hamburguer artesanal com batata frita",
                35.90,
                false,
                "/images/hamburguer.jpg");

        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(itemRest1));
        when(cardapioItemGateway.obterPorId(2L)).thenReturn(Optional.of(itemRest2));

        Optional<OutputModel> resultado1 = useCase.execute(1L);
        Optional<OutputModel> resultado2 = useCase.execute(2L);

        assertThat(resultado1).isPresent();
        assertThat(resultado1.get().restauranteId()).isEqualTo(1L);

        assertThat(resultado2).isPresent();
        assertThat(resultado2.get().restauranteId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        ObterCardapioItemPorIdUseCase novoUseCase = ObterCardapioItemPorIdUseCase.create(cardapioItemGateway);

        assertThat(novoUseCase).isNotNull();
    }
}