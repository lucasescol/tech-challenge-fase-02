package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.CardapioItemNaoEncontradoException;
import br.com.fiap.core.exceptions.RestauranteNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeletarCardapioItemUseCase - Testes Unitários")
class DeletarCardapioItemUseCaseTest {

    @Mock
    private ICardapioItemGateway cardapioItemGateway;

    @Mock
    private IRestauranteGateway restauranteGateway;

    @Mock
    private IAuthenticationGateway authenticationGateway;

    @Mock
    private IUsuarioGateway usuarioGateway;

    private DeletarCardapioItemUseCase useCase;

    private Restaurante restaurante;
    private Usuario usuarioDono;
    private CardapioItem cardapioItem;

    @BeforeEach
    void setUp() {
        useCase = DeletarCardapioItemUseCase.create(
                cardapioItemGateway,
                restauranteGateway,
                authenticationGateway,
                usuarioGateway);

        TipoUsuario tipoDono = TipoUsuario.create(1L, "DONO_RESTAURANTE", "Dono de Restaurante");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        usuarioDono = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "senha123", endereco, tipoDono);

        restaurante = Restaurante.create(
                1L,
                "Restaurante Italiano",
                "Rua X",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        cardapioItem = CardapioItem.criar(
                1L,
                1L,
                "Pizza Margherita",
                "Deliciosa pizza com molho de tomate, mussarela e manjericão fresco",
                45.90,
                false,
                "/images/pizza-margherita.jpg");
    }

    @Test
    @DisplayName("Deve deletar item do cardápio com sucesso quando usuário é dono do restaurante")
    void deveDeletarItemQuandoUsuarioEhDonoDoRestaurante() {
        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(cardapioItem));
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.buscarPorLogin("joao")).thenReturn(Optional.of(usuarioDono));
        doNothing().when(cardapioItemGateway).deletar(1L);

        useCase.execute(1L);

        verify(cardapioItemGateway).obterPorId(1L);
        verify(restauranteGateway).obterPorId(1L);
        verify(cardapioItemGateway).deletar(1L);
    }

    @Test
    @DisplayName("Deve deletar item do cardápio com sucesso quando usuário é administrador")
    void deveDeletarItemQuandoUsuarioEhAdministrador() {
        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(cardapioItem));
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.isAdministrador()).thenReturn(true);
        doNothing().when(cardapioItemGateway).deletar(1L);

        useCase.execute(1L);

        verify(cardapioItemGateway).deletar(1L);
        verify(authenticationGateway, never()).getUsuarioLogado();
    }

    @Test
    @DisplayName("Deve lançar exceção quando item não existe")
    void deveLancarExcecaoQuandoItemNaoExiste() {
        when(cardapioItemGateway.obterPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L))
                .isInstanceOf(CardapioItemNaoEncontradoException.class)
                .hasMessageContaining("999");

        verify(cardapioItemGateway).obterPorId(999L);
        verify(cardapioItemGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(cardapioItem));
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(RestauranteNaoEncontradoException.class)
                .hasMessageContaining("1");

        verify(cardapioItemGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é dono de restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoEhDonoDeRestaurante() {
        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(cardapioItem));
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(authenticationGateway.isDonoRestaurante()).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Apenas donos de restaurante podem deletar itens do cardápio");

        verify(cardapioItemGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não é encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(cardapioItem));
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
        when(authenticationGateway.getUsuarioLogado()).thenReturn("usuario_inexistente");
        when(usuarioGateway.buscarPorLogin("usuario_inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(cardapioItemGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário tenta deletar item de restaurante de outro dono")
    void deveLancarExcecaoQuandoUsuarioTentaDeletarItemDeRestauranteDeOutroDono() {
        TipoUsuario tipoDono = TipoUsuario.create(1L, "DONO_RESTAURANTE", "Dono de Restaurante");
        Endereco endereco = new Endereco("Rua C", "300", "", "Centro", "São Paulo", "SP", "01000-000");
        Usuario outroDono = Usuario.create(2L, "Pedro", "pedro@email.com", "pedro", "senha123", endereco, tipoDono);

        when(cardapioItemGateway.obterPorId(1L)).thenReturn(Optional.of(cardapioItem));
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
        when(authenticationGateway.getUsuarioLogado()).thenReturn("pedro");
        when(usuarioGateway.buscarPorLogin("pedro")).thenReturn(Optional.of(outroDono));

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Você só pode deletar itens do cardápio do seu próprio restaurante");

        verify(cardapioItemGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        DeletarCardapioItemUseCase novoUseCase = DeletarCardapioItemUseCase.create(
                cardapioItemGateway,
                restauranteGateway,
                authenticationGateway,
                usuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}