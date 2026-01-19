package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExcluirUsuarioUseCase - Testes Unitários")
class ExcluirUsuarioUseCaseTest {

    @Mock
    private IUsuarioGateway usuarioGateway;

    @Mock
    private IAuthenticationGateway authenticationGateway;

    private ExcluirUsuarioUseCase useCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        useCase = ExcluirUsuarioUseCase.create(usuarioGateway, authenticationGateway);

        TipoUsuario tipoCliente = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        usuario = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "senha123", endereco, tipoCliente);
    }

    @Test
    @DisplayName("Deve excluir usuário com sucesso quando é o próprio usuário")
    void deveExcluirUsuarioQuandoEhProprioUsuario() {
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        doNothing().when(usuarioGateway).excluir(1L);

        assertThatCode(() -> useCase.execute(1L))
                .doesNotThrowAnyException();

        verify(usuarioGateway).excluir(1L);
    }

    @Test
    @DisplayName("Deve excluir usuário com sucesso quando é administrador")
    void deveExcluirUsuarioQuandoEhAdministrador() {
        when(authenticationGateway.isAdministrador()).thenReturn(true);
        doNothing().when(usuarioGateway).excluir(1L);

        assertThatCode(() -> useCase.execute(1L))
                .doesNotThrowAnyException();

        verify(usuarioGateway).excluir(1L);
        verify(usuarioGateway, never()).buscarPorId(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioGateway, never()).excluir(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário tenta excluir outro usuário")
    void deveLancarExcecaoQuandoUsuarioTentaExcluirOutroUsuario() {
        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("maria");

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Você só pode excluir sua própria conta");

        verify(usuarioGateway, never()).excluir(anyLong());
    }
}