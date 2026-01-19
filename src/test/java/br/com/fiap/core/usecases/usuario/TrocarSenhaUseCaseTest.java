package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.exceptions.SenhaAtualIncorretaException;
import br.com.fiap.core.exceptions.SenhasNaoConferemException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.services.IPasswordHasherService;
import br.com.fiap.core.usecases.usuario.TrocarSenhaUseCase.InputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrocarSenhaUseCase - Testes Unitários")
class TrocarSenhaUseCaseTest {

    @Mock
    private IUsuarioGateway usuarioGateway;

    @Mock
    private IPasswordHasherService passwordHasherService;

    @Mock
    private IAuthenticationGateway authenticationGateway;

    private TrocarSenhaUseCase useCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        useCase = TrocarSenhaUseCase.create(usuarioGateway, passwordHasherService, authenticationGateway);

        TipoUsuario tipoCliente = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        usuario = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "$2a$10$hashedpassword", endereco,
                tipoCliente);
    }

    @Test
    @DisplayName("Deve trocar senha com sucesso quando é o próprio usuário")
    void deveTrocarSenhaQuandoEhProprioUsuario() {
        InputModel inputModel = new InputModel("senha123", "novaSenha123", "novaSenha123");

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(passwordHasherService.matches("senha123", "$2a$10$hashedpassword")).thenReturn(true);
        when(passwordHasherService.encode("novaSenha123")).thenReturn("$2a$10$newhashedpassword");
        doNothing().when(usuarioGateway).trocarSenha(1L, "$2a$10$newhashedpassword");

        assertThatCode(() -> useCase.execute(1L, inputModel))
                .doesNotThrowAnyException();

        verify(usuarioGateway).trocarSenha(1L, "$2a$10$newhashedpassword");
        verify(passwordHasherService).matches("senha123", "$2a$10$hashedpassword");
        verify(passwordHasherService).encode("novaSenha123");
    }

    @Test
    @DisplayName("Deve trocar senha com sucesso quando é administrador")
    void deveTrocarSenhaQuandoEhAdministrador() {
        InputModel inputModel = new InputModel("senha123", "novaSenha123", "novaSenha123");

        when(authenticationGateway.isAdministrador()).thenReturn(true);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(passwordHasherService.matches("senha123", "$2a$10$hashedpassword")).thenReturn(true);
        when(passwordHasherService.encode("novaSenha123")).thenReturn("$2a$10$newhashedpassword");
        doNothing().when(usuarioGateway).trocarSenha(1L, "$2a$10$newhashedpassword");

        assertThatCode(() -> useCase.execute(1L, inputModel))
                .doesNotThrowAnyException();

        verify(usuarioGateway).trocarSenha(1L, "$2a$10$newhashedpassword");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senhas não conferem")
    void deveLancarExcecaoQuandoSenhasNaoConferem() {
        InputModel inputModel = new InputModel("senha123", "novaSenha123", "senhaDiferente");

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(SenhasNaoConferemException.class);

        verify(usuarioGateway, never()).trocarSenha(anyLong(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha atual está incorreta")
    void deveLancarExcecaoQuandoSenhaAtualIncorreta() {
        InputModel inputModel = new InputModel("senhaErrada", "novaSenha123", "novaSenha123");

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(passwordHasherService.matches("senhaErrada", "$2a$10$hashedpassword")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(SenhaAtualIncorretaException.class);

        verify(passwordHasherService).matches("senhaErrada", "$2a$10$hashedpassword");
        verify(usuarioGateway, never()).trocarSenha(anyLong(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        InputModel inputModel = new InputModel("senha123", "novaSenha123", "novaSenha123");

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(999L, inputModel))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioGateway, never()).trocarSenha(anyLong(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário tenta trocar senha de outro usuário")
    void deveLancarExcecaoQuandoUsuarioTentaTrocarSenhaDeOutroUsuario() {
        InputModel inputModel = new InputModel("senha123", "novaSenha123", "novaSenha123");

        when(authenticationGateway.isAdministrador()).thenReturn(false);
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("maria");

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Você só pode trocar sua própria senha");

        verify(usuarioGateway, never()).trocarSenha(anyLong(), anyString());
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        TrocarSenhaUseCase novoUseCase = TrocarSenhaUseCase.create(
                usuarioGateway,
                passwordHasherService,
                authenticationGateway);

        assertThat(novoUseCase).isNotNull();
    }
}