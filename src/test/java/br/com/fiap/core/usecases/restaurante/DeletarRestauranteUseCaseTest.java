package br.com.fiap.core.usecases.restaurante;

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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeletarRestauranteUseCase - Testes Unitários")
class DeletarRestauranteUseCaseTest {

    @Mock
    private IRestauranteGateway restauranteGateway;

    @Mock
    private IAuthenticationGateway authenticationGateway;

    @Mock
    private IUsuarioGateway usuarioGateway;

    @Mock
    private ICardapioItemGateway cardapioItemGateway;

    private DeletarRestauranteUseCase useCase;

    private Restaurante restaurante;
    private Usuario usuarioProprietario;

    @BeforeEach
    void setUp() {
        useCase = DeletarRestauranteUseCase.create(
                restauranteGateway,
                authenticationGateway,
                usuarioGateway, cardapioItemGateway);

        TipoUsuario tipoDono = TipoUsuario.create(1L, "DONO_RESTAURANTE", "Dono de Restaurante");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        usuarioProprietario = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "senha123", endereco,
                tipoDono);

        restaurante = Restaurante.create(
                1L,
                "Restaurante Teste",
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
    }

    @Test
    @DisplayName("Deve deletar restaurante com sucesso quando usuário é proprietário")
    void deveDeletarRestauranteQuandoUsuarioEhProprietario() {
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.buscarPorLogin("joao")).thenReturn(Optional.of(usuarioProprietario));
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        doNothing().when(restauranteGateway).deletar(1L);

        assertDoesNotThrow(() -> useCase.execute(1L));

        verify(restauranteGateway).obterPorId(1L);
        verify(restauranteGateway).deletar(1L);
    }

    @Test
    @DisplayName("Deve deletar restaurante com sucesso quando usuário é administrador")
    void deveDeletarRestauranteQuandoUsuarioEhAdministrador() {
        TipoUsuario tipoAdmin = TipoUsuario.create(2L, "ADMINISTRADOR", "Administrador");
        Endereco enderecoAdmin = new Endereco("Rua B", "200", "", "Centro", "São Paulo", "SP", "01000-000");
        Usuario usuarioAdmin = Usuario.create(2L, "Admin", "admin@email.com", "admin", "senha123", enderecoAdmin,
                tipoAdmin);

        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("admin");
        when(usuarioGateway.buscarPorLogin("admin")).thenReturn(Optional.of(usuarioAdmin));
        when(authenticationGateway.isAdministrador()).thenReturn(true);
        doNothing().when(restauranteGateway).deletar(1L);

        assertDoesNotThrow(() -> useCase.execute(1L));
        verify(restauranteGateway).deletar(1L);
    }

    @Test
    @DisplayName("Deve retornar false quando restaurante não existe")
    void deveRetornarFalseQuandoRestauranteNaoExiste() {
        Long id = 1L;
        when(restauranteGateway.obterPorId(id)).thenReturn(Optional.empty());
        assertThrows(RestauranteNaoEncontradoException.class, () -> useCase.execute(id));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é proprietário nem administrador")
    void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {
        TipoUsuario tipoCliente = TipoUsuario.create(3L, "CLIENTE", "Cliente");
        Endereco enderecoOutro = new Endereco("Rua C", "300", "", "Centro", "São Paulo", "SP", "01000-000");
        Usuario outroUsuario = Usuario.create(2L, "Maria", "maria@email.com", "maria", "senha123", enderecoOutro,
                tipoCliente);

        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("maria");
        when(usuarioGateway.buscarPorLogin("maria")).thenReturn(Optional.of(outroUsuario));
        when(authenticationGateway.isAdministrador()).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining(
                        "Apenas o proprietário do restaurante ou administradores podem deletar este restaurante");

        verify(restauranteGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não é encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restaurante));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("usuario_inexistente");
        when(usuarioGateway.buscarPorLogin("usuario_inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(restauranteGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        DeletarRestauranteUseCase novoUseCase = DeletarRestauranteUseCase.create(
                restauranteGateway,
                authenticationGateway,
                usuarioGateway,
                cardapioItemGateway);

        assertThat(novoUseCase).isNotNull();
    }
}