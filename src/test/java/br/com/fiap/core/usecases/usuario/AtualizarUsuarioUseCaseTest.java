package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.EmailJaCadastradoException;
import br.com.fiap.core.exceptions.LoginJaCadastradoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase.InputModel;
import br.com.fiap.core.usecases.usuario.AtualizarUsuarioUseCase.OutputModel;
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
@DisplayName("AtualizarUsuarioUseCase - Testes Unitários")
class AtualizarUsuarioUseCaseTest {

    @Mock
    private IUsuarioGateway usuarioGateway;

    @Mock
    private IAuthenticationGateway authenticationGateway;

    private AtualizarUsuarioUseCase useCase;

    private Usuario usuarioExistente;
    private TipoUsuario tipoUsuarioCliente;

    @BeforeEach
    void setUp() {
        useCase = AtualizarUsuarioUseCase.create(usuarioGateway, authenticationGateway);

        tipoUsuarioCliente = TipoUsuario.create(1L, "CLIENTE", "Cliente");
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
    @DisplayName("Deve atualizar usuário com sucesso quando é o próprio usuário")
    void deveAtualizarUsuarioQuandoEhProprioUsuario() {
        InputModel inputModel = new InputModel(
                "João Silva Santos",
                "joao.novo@email.com",
                "joao_novo",
                "Rua B",
                "200",
                "Apto 10",
                "Jardins",
                "São Paulo",
                "SP",
                "02000-000");

        Endereco novoEndereco = new Endereco("Rua B", "200", "Apto 10", "Jardins", "São Paulo", "SP", "02000-000");
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva Santos",
                "joao.novo@email.com",
                "joao_novo",
                "senha123",
                novoEndereco,
                tipoUsuarioCliente);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.existeLoginCadastrado("joao_novo", 1L)).thenReturn(false);
        when(usuarioGateway.existeEmailCadastrado("joao.novo@email.com", 1L)).thenReturn(false);
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("João Silva Santos");
        assertThat(resultado.email()).isEqualTo("joao.novo@email.com");
        assertThat(resultado.login()).isEqualTo("joao_novo");
        assertThat(resultado.endereco()).contains("Rua B");

        verify(usuarioGateway).atualizar(eq(1L), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso quando é administrador")
    void deveAtualizarUsuarioQuandoEhAdministrador() {
        InputModel inputModel = new InputModel(
                "João Silva Santos",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva Santos",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioCliente);

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado.nome()).isEqualTo("João Silva Santos");
        verify(usuarioGateway).atualizar(eq(1L), any(Usuario.class));
        verify(authenticationGateway, never()).getUsuarioLogado();
    }

    @Test
    @DisplayName("Deve atualizar apenas campos fornecidos")
    void deveAtualizarApenasCamposFornecidos() {
        InputModel inputParcial = new InputModel(
                "João Silva Santos",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva Santos",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioCliente);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputParcial);

        assertThat(resultado.nome()).isEqualTo("João Silva Santos");
        assertThat(resultado.email()).isEqualTo("joao@email.com");
        assertThat(resultado.login()).isEqualTo("joao");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        InputModel inputModel = new InputModel(
                "Nome Teste",
                null, null, null, null, null, null, null, null, null);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L, inputModel))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessageContaining("999");

        verify(usuarioGateway, never()).atualizar(anyLong(), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário tenta atualizar dados de outro usuário")
    void deveLancarExcecaoQuandoUsuarioTentaAtualizarOutroUsuario() {
        InputModel inputModel = new InputModel(
                "Nome Teste",
                null, null, null, null, null, null, null, null, null);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("outro_usuario");

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Você só pode modificar seus próprios dados");

        verify(usuarioGateway, never()).atualizar(anyLong(), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando novo login já está cadastrado")
    void deveLancarExcecaoQuandoNovoLoginJaCadastrado() {
        InputModel inputModel = new InputModel(
                null,
                null,
                "login_existente",
                null, null, null, null, null, null, null);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.existeLoginCadastrado("login_existente", 1L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(LoginJaCadastradoException.class)
                .hasMessageContaining("login_existente");

        verify(usuarioGateway, never()).atualizar(anyLong(), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando novo email já está cadastrado")
    void deveLancarExcecaoQuandoNovoEmailJaCadastrado() {
        InputModel inputModel = new InputModel(
                null,
                "email_existente@email.com",
                null,
                null, null, null, null, null, null, null);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.existeEmailCadastrado("email_existente@email.com", 1L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining("email_existente@email.com");

        verify(usuarioGateway, never()).atualizar(anyLong(), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve permitir manter mesmo login ao atualizar")
    void devePermitirManterMesmoLogin() {
        InputModel inputModel = new InputModel(
                "João Atualizado",
                null,
                "joao",
                null, null, null, null, null, null, null);

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Atualizado",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioCliente);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado.login()).isEqualTo("joao");
        verify(usuarioGateway, never()).existeLoginCadastrado(anyString(), anyLong());
    }

    @Test
    @DisplayName("Deve permitir manter mesmo email ao atualizar")
    void devePermitirManterMesmoEmail() {
        InputModel inputModel = new InputModel(
                "João Atualizado",
                "joao@email.com",
                null,
                null, null, null, null, null, null, null);

        Endereco endereco = usuarioExistente.getEndereco();
        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Atualizado",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioCliente);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputModel);

        assertThat(resultado.email()).isEqualTo("joao@email.com");
        verify(usuarioGateway, never()).existeEmailCadastrado(anyString(), anyLong());
    }

    @Test
    @DisplayName("Deve atualizar endereço parcialmente")
    void deveAtualizarEnderecoParcialmente() {
        InputModel inputEndereco = new InputModel(
                null,
                null,
                null,
                "Rua Nova",
                "500",
                null,
                null,
                null,
                null,
                null);

        Endereco enderecoAtualizado = new Endereco(
                "Rua Nova",
                "500",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000");

        Usuario usuarioAtualizado = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                enderecoAtualizado,
                tipoUsuarioCliente);

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        OutputModel resultado = useCase.execute(1L, inputEndereco);

        assertThat(resultado.endereco()).contains("Rua Nova");
        assertThat(resultado.endereco()).contains("500");
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        AtualizarUsuarioUseCase novoUseCase = AtualizarUsuarioUseCase.create(
                usuarioGateway,
                authenticationGateway);

        assertThat(novoUseCase).isNotNull();
    }
}