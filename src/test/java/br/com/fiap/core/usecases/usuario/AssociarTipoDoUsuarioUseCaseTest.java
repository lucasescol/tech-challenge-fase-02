package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.TipoUsuarioInvalidoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.usuario.AssociarTipoDoUsuarioUseCase.InputModel;
import br.com.fiap.core.usecases.usuario.AssociarTipoDoUsuarioUseCase.OutputModel;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssociarTipoDoUsuarioUseCase - Testes Unitários")
class AssociarTipoDoUsuarioUseCaseTest {

    @Mock
    private IUsuarioGateway usuarioGateway;

    @Mock
    private ITipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private IAuthenticationGateway authenticationGateway;

    private AssociarTipoDoUsuarioUseCase useCase;

    private Usuario usuarioExistente;
    private TipoUsuario tipoUsuarioCliente;
    private TipoUsuario tipoUsuarioDono;

    @BeforeEach
    void setUp() {
        useCase = AssociarTipoDoUsuarioUseCase.create(
                usuarioGateway,
                tipoUsuarioGateway,
                authenticationGateway);

        tipoUsuarioCliente = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        tipoUsuarioDono = TipoUsuario.create(2L, "DONO_RESTAURANTE", "Dono de Restaurante");

        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        usuarioExistente = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioCliente);
    }

    @Test
    @DisplayName("Deve associar tipo de usuário com sucesso quando é administrador")
    void deveAssociarTipoUsuarioQuandoEhAdministrador() {
        InputModel inputModel = new InputModel("DONO_RESTAURANTE");

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioDono);

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(tipoUsuarioGateway.obterPorNome("DONO_RESTAURANTE")).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("João Silva");
        assertThat(resultado.email()).isEqualTo("joao@email.com");
        assertThat(resultado.login()).isEqualTo("joao");
        assertThat(resultado.tipoUsuario()).isEqualTo("DONO_RESTAURANTE");

        verify(authenticationGateway).isAdministrador();
        verify(usuarioGateway).buscarPorId(1L);
        verify(tipoUsuarioGateway).obterPorNome("DONO_RESTAURANTE");
        verify(usuarioGateway).atualizar(eq(1L), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é administrador")
    void deveLancarExcecaoQuandoUsuarioNaoEhAdministrador() {
        InputModel inputModel = new InputModel("DONO_RESTAURANTE");

        when(authenticationGateway.isAdministrador()).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Apenas administradores podem alterar o tipo de usuário");

        verify(authenticationGateway).isAdministrador();
        verify(usuarioGateway, never()).buscarPorId(anyLong());
        verify(usuarioGateway, never()).atualizar(anyLong(), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        InputModel inputModel = new InputModel("DONO_RESTAURANTE");

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L, inputModel))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessageContaining("999");

        verify(usuarioGateway, never()).atualizar(anyLong(), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não existe")
    void deveLancarExcecaoQuandoTipoUsuarioNaoExiste() {
        InputModel inputModel = new InputModel("TIPO_INVALIDO");

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(tipoUsuarioGateway.obterPorNome("TIPO_INVALIDO")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(TipoUsuarioInvalidoException.class)
                .hasMessageContaining("TIPO_INVALIDO");

        verify(usuarioGateway, never()).atualizar(anyLong(), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve normalizar tipo de usuário para uppercase")
    void deveNormalizarTipoUsuarioParaUppercase() {
        InputModel inputModel = new InputModel("dono_restaurante");

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioDono);

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(tipoUsuarioGateway.obterPorNome("DONO_RESTAURANTE")).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado.tipoUsuario()).isEqualTo("DONO_RESTAURANTE");
        verify(tipoUsuarioGateway).obterPorNome("DONO_RESTAURANTE");
    }

    @Test
    @DisplayName("Deve associar tipo CLIENTE a um usuário")
    void deveAssociarTipoCliente() {
        TipoUsuario tipoDono = TipoUsuario.create(2L, "DONO_RESTAURANTE", "Dono de Restaurante");
        Endereco endereco = new Endereco("Rua B", "200", "", "Vila Nova", "São Paulo", "SP", "02000-000");
        Usuario usuarioDono = Usuario.create(
                2L,
                "Maria Silva",
                "maria@email.com",
                "maria",
                "senha456",
                endereco,
                tipoDono);

        InputModel inputModel = new InputModel("CLIENTE");

        Usuario usuarioAtualizado = Usuario.create(
                2L,
                "Maria Silva",
                "maria@email.com",
                "maria",
                "senha456",
                endereco,
                tipoUsuarioCliente);

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(2L)).thenReturn(Optional.of(usuarioDono));
        when(tipoUsuarioGateway.obterPorNome("CLIENTE")).thenReturn(Optional.of(tipoUsuarioCliente));
        when(usuarioGateway.atualizar(eq(2L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(2L, inputModel);

        assertThat(resultado.tipoUsuario()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Deve associar tipo ADMINISTRADOR a um usuário")
    void deveAssociarTipoAdministrador() {
        TipoUsuario tipoAdmin = TipoUsuario.create(3L, "ADMINISTRADOR", "Administrador");
        InputModel inputModel = new InputModel("ADMINISTRADOR");

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoAdmin);

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(tipoUsuarioGateway.obterPorNome("ADMINISTRADOR")).thenReturn(Optional.of(tipoAdmin));
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado.tipoUsuario()).isEqualTo("ADMINISTRADOR");
    }

    @Test
    @DisplayName("Deve manter outros dados do usuário ao alterar tipo")
    void deveManterOutrosDadosAoAlterarTipo() {
        InputModel inputModel = new InputModel("DONO_RESTAURANTE");

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioDono);

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(tipoUsuarioGateway.obterPorNome("DONO_RESTAURANTE")).thenReturn(Optional.of(tipoUsuarioDono));
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado.nome()).isEqualTo("João Silva");
        assertThat(resultado.email()).isEqualTo("joao@email.com");
        assertThat(resultado.login()).isEqualTo("joao");
        assertThat(resultado.tipoUsuario()).isEqualTo("DONO_RESTAURANTE");
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        AssociarTipoDoUsuarioUseCase novoUseCase = AssociarTipoDoUsuarioUseCase.create(
                usuarioGateway,
                tipoUsuarioGateway,
                authenticationGateway);

        assertThat(novoUseCase).isNotNull();
    }
}