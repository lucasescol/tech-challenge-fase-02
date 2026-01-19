package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.CampoObrigatorioException;
import br.com.fiap.core.exceptions.CredenciaisInvalidasException;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.services.IPasswordHasherService;
import br.com.fiap.core.services.ITokenService;
import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase.InputModel;
import br.com.fiap.core.usecases.usuario.AutenticarUsuarioUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutenticarUsuarioUseCase - Testes Unitários")
class AutenticarUsuarioUseCaseTest {

    @Mock
    private IUsuarioGateway usuarioGateway;

    @Mock
    private IPasswordHasherService passwordHasherService;

    @Mock
    private ITokenService tokenService;

    private AutenticarUsuarioUseCase useCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        useCase = AutenticarUsuarioUseCase.create(usuarioGateway, passwordHasherService, tokenService);

        TipoUsuario tipoCliente = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        usuario = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "$2a$10$hashedpassword", endereco,
                tipoCliente);
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso")
    void deveAutenticarUsuarioComSucesso() {
        InputModel inputModel = new InputModel("joao", "senha123");

        when(usuarioGateway.buscarPorLogin("joao")).thenReturn(Optional.of(usuario));
        when(passwordHasherService.matches("senha123", "$2a$10$hashedpassword")).thenReturn(true);
        when(tokenService.generateToken("joao", "CLIENTE")).thenReturn("token_jwt_generated");

        OutputModel resultado = useCase.execute(inputModel);

        assertThat(resultado).isNotNull();
        assertThat(resultado.token()).isEqualTo("token_jwt_generated");
        assertThat(resultado.tipo()).isEqualTo("Bearer");
        assertThat(resultado.login()).isEqualTo("joao");
        assertThat(resultado.nome()).isEqualTo("João Silva");
        assertThat(resultado.email()).isEqualTo("joao@email.com");
        assertThat(resultado.tipoUsuario()).isEqualTo("CLIENTE");

        verify(usuarioGateway).buscarPorLogin("joao");
        verify(passwordHasherService).matches("senha123", "$2a$10$hashedpassword");
        verify(tokenService).generateToken("joao", "CLIENTE");
    }

    @Test
    @DisplayName("Deve lançar exceção quando login é vazio")
    void deveLancarExcecaoQuandoLoginVazio() {
        InputModel inputModel = new InputModel("", "senha123");

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(CampoObrigatorioException.class)
                .hasMessage("Login obrigatório");

        verify(usuarioGateway, never()).buscarPorLogin(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando login é null")
    void deveLancarExcecaoQuandoLoginNull() {
        InputModel inputModel = new InputModel(null, "senha123");

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(CampoObrigatorioException.class)
                .hasMessage("Login obrigatório");

        verify(usuarioGateway, never()).buscarPorLogin(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é vazia")
    void deveLancarExcecaoQuandoSenhaVazia() {
        InputModel inputModel = new InputModel("joao", "");

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(CampoObrigatorioException.class)
                .hasMessage("Senha obrigatório");

        verify(usuarioGateway, never()).buscarPorLogin(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é null")
    void deveLancarExcecaoQuandoSenhaNull() {
        InputModel inputModel = new InputModel("joao", null);

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(CampoObrigatorioException.class)
                .hasMessage("Senha obrigatório");

        verify(usuarioGateway, never()).buscarPorLogin(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        InputModel inputModel = new InputModel("usuario_inexistente", "senha123");

        when(usuarioGateway.buscarPorLogin("usuario_inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(CredenciaisInvalidasException.class);

        verify(usuarioGateway).buscarPorLogin("usuario_inexistente");
        verify(passwordHasherService, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha está incorreta")
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        InputModel inputModel = new InputModel("joao", "senha_errada");

        when(usuarioGateway.buscarPorLogin("joao")).thenReturn(Optional.of(usuario));
        when(passwordHasherService.matches("senha_errada", "$2a$10$hashedpassword")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(CredenciaisInvalidasException.class);

        verify(usuarioGateway).buscarPorLogin("joao");
        verify(passwordHasherService).matches("senha_errada", "$2a$10$hashedpassword");
        verify(tokenService, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        AutenticarUsuarioUseCase novoUseCase = AutenticarUsuarioUseCase.create(
                usuarioGateway,
                passwordHasherService,
                tokenService);

        assertThat(novoUseCase).isNotNull();
    }
}