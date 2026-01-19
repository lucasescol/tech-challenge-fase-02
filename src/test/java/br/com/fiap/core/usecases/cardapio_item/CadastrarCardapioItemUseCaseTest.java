package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarCardapioItemUseCase - Testes Unitários")
class CadastrarCardapioItemUseCaseTest {

        @Mock
        private ICardapioItemGateway cardapioItemGateway;

        @Mock
        private IRestauranteGateway restauranteGateway;

        @Mock
        private IAuthenticationGateway authenticationGateway;

        @Mock
        private IUsuarioGateway usuarioGateway;

        private CadastrarCardapioItemUseCase useCase;

        private Restaurante restaurante;
        private Usuario usuarioDono;
        private CardapioItem cardapioItem;

        @BeforeEach
        void setUp() {
                useCase = CadastrarCardapioItemUseCase.create(
                                cardapioItemGateway,
                                restauranteGateway,
                                authenticationGateway,
                                usuarioGateway);

                TipoUsuario tipoDono = TipoUsuario.create(1L, "DONO_RESTAURANTE", "Dono de Restaurante");
                Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
                usuarioDono = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "senha123", endereco,
                                tipoDono);

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
                                null,
                                1L,
                                "Pizza Margherita",
                                "Deliciosa pizza com molho de tomate, mussarela e manjericão fresco",
                                45.90,
                                false,
                                "/images/pizza-margherita.jpg");
        }

        @Test
        @DisplayName("Deve cadastrar item do cardápio com sucesso quando usuário é dono do restaurante")
        void deveCadastrarItemQuandoUsuarioEhDonoDoRestaurante() {
                when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
                when(authenticationGateway.isAdministrador()).thenReturn(false);
                when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
                when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
                when(usuarioGateway.buscarPorLogin("joao")).thenReturn(Optional.of(usuarioDono));
                when(cardapioItemGateway.incluir(any(CardapioItem.class))).thenReturn(
                                CardapioItem.criar(1L, 1L, "Pizza Margherita",
                                                "Deliciosa pizza com molho de tomate, mussarela e manjericão fresco",
                                                45.90, false, "/images/pizza-margherita.jpg"));

                CardapioItem resultado = useCase.execute(cardapioItem);

                assertThat(resultado).isNotNull();
                assertThat(resultado.getId()).isEqualTo(1L);
                assertThat(resultado.getNome()).isEqualTo("Pizza Margherita");
                assertThat(resultado.getPreco()).isEqualTo(45.90);
                assertThat(resultado.getRestauranteId()).isEqualTo(1L);

                verify(restauranteGateway).obterPorId(1L);
                verify(cardapioItemGateway).incluir(any(CardapioItem.class));
        }

        @Test
        @DisplayName("Deve cadastrar item do cardápio com sucesso quando usuário é administrador")
        void deveCadastrarItemQuandoUsuarioEhAdministrador() {
                when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
                when(authenticationGateway.isAdministrador()).thenReturn(true);
                when(cardapioItemGateway.incluir(any(CardapioItem.class))).thenReturn(
                                CardapioItem.criar(1L, 1L, "Pizza Margherita",
                                                "Deliciosa pizza com molho de tomate, mussarela e manjericão fresco",
                                                45.90, false, "/images/pizza-margherita.jpg"));

                CardapioItem resultado = useCase.execute(cardapioItem);

                assertThat(resultado).isNotNull();
                assertThat(resultado.getId()).isEqualTo(1L);

                verify(restauranteGateway).obterPorId(1L);
                verify(cardapioItemGateway).incluir(any(CardapioItem.class));
                verify(authenticationGateway, never()).getUsuarioLogado();
        }

        @Test
        @DisplayName("Deve lançar exceção quando restaurante não existe")
        void deveLancarExcecaoQuandoRestauranteNaoExiste() {
                when(restauranteGateway.obterPorId(999L)).thenReturn(Optional.empty());

                CardapioItem itemInvalido = CardapioItem.criar(
                                null,
                                999L,
                                "Pizza Margherita",
                                "Deliciosa pizza com molho de tomate, mussarela e manjericão fresco",
                                45.90,
                                false,
                                "/images/pizza-margherita.jpg");

                assertThatThrownBy(() -> useCase.execute(itemInvalido))
                                .isInstanceOf(RestauranteNaoEncontradoException.class)
                                .hasMessageContaining("999");

                verify(restauranteGateway).obterPorId(999L);
                verify(cardapioItemGateway, never()).incluir(any(CardapioItem.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não é dono de restaurante")
        void deveLancarExcecaoQuandoUsuarioNaoEhDonoDeRestaurante() {
                when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
                when(authenticationGateway.isAdministrador()).thenReturn(false);
                when(authenticationGateway.isDonoRestaurante()).thenReturn(false);

                assertThatThrownBy(() -> useCase.execute(cardapioItem))
                                .isInstanceOf(AcessoNegadoException.class)
                                .hasMessageContaining("Apenas donos de restaurante podem cadastrar itens no cardápio");

                verify(cardapioItemGateway, never()).incluir(any(CardapioItem.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário logado não é encontrado")
        void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
                when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
                when(authenticationGateway.isAdministrador()).thenReturn(false);
                when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
                when(authenticationGateway.getUsuarioLogado()).thenReturn("usuario_inexistente");
                when(usuarioGateway.buscarPorLogin("usuario_inexistente")).thenReturn(Optional.empty());

                assertThatThrownBy(() -> useCase.execute(cardapioItem))
                                .isInstanceOf(AcessoNegadoException.class)
                                .hasMessageContaining("Usuário não encontrado");

                verify(cardapioItemGateway, never()).incluir(any(CardapioItem.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário tenta adicionar item em restaurante de outro dono")
        void deveLancarExcecaoQuandoUsuarioTentaAdicionarItemEmRestauranteDeOutroDono() {
                TipoUsuario tipoDono = TipoUsuario.create(1L, "DONO_RESTAURANTE", "Dono de Restaurante");
                Endereco endereco = new Endereco("Rua C", "300", "", "Centro", "São Paulo", "SP", "01000-000");
                Usuario outroDono = Usuario.create(2L, "Pedro", "pedro@email.com", "pedro", "senha123", endereco,
                                tipoDono);

                when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
                when(authenticationGateway.isAdministrador()).thenReturn(false);
                when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
                when(authenticationGateway.getUsuarioLogado()).thenReturn("pedro");
                when(usuarioGateway.buscarPorLogin("pedro")).thenReturn(Optional.of(outroDono));

                assertThatThrownBy(() -> useCase.execute(cardapioItem))
                                .isInstanceOf(AcessoNegadoException.class)
                                .hasMessageContaining(
                                                "Você só pode adicionar itens ao cardápio do seu próprio restaurante");

                verify(cardapioItemGateway, never()).incluir(any(CardapioItem.class));
        }

        @Test
        @DisplayName("Deve criar useCase usando factory method")
        void deveCriarUseCaseUsandoFactoryMethod() {
                CadastrarCardapioItemUseCase novoUseCase = CadastrarCardapioItemUseCase.create(
                                cardapioItemGateway,
                                restauranteGateway,
                                authenticationGateway,
                                usuarioGateway);

                assertThat(novoUseCase).isNotNull();
        }
}