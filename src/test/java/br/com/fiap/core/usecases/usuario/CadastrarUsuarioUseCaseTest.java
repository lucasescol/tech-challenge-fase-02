package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.EmailJaCadastradoException;
import br.com.fiap.core.exceptions.LoginJaCadastradoException;
import br.com.fiap.core.exceptions.TipoUsuarioInvalidoException;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.usuario.CadastrarUsuarioUseCase.InputModel;
import br.com.fiap.core.usecases.usuario.CadastrarUsuarioUseCase.OutputModel;
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
@DisplayName("CadastrarUsuarioUseCase - Testes Unitários")
class CadastrarUsuarioUseCaseTest {

    @Mock
    private IUsuarioGateway usuarioGateway;

    @Mock
    private ITipoUsuarioGateway tipoUsuarioGateway;

    private CadastrarUsuarioUseCase useCase;

    private InputModel inputModel;
    private TipoUsuario tipoUsuarioCliente;

    @BeforeEach
    void setUp() {
        useCase = CadastrarUsuarioUseCase.create(usuarioGateway, tipoUsuarioGateway);

        tipoUsuarioCliente = TipoUsuario.create(1L, "CLIENTE", "Cliente");

        inputModel = new InputModel(
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "CLIENTE");
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        Usuario usuarioSalvo = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipoUsuarioCliente);

        when(usuarioGateway.existeLoginCadastrado("joao", null)).thenReturn(false);
        when(usuarioGateway.existeEmailCadastrado("joao@email.com", null)).thenReturn(false);
        when(tipoUsuarioGateway.obterPorNome("CLIENTE")).thenReturn(Optional.of(tipoUsuarioCliente));
        when(usuarioGateway.incluir(any(Usuario.class))).thenReturn(usuarioSalvo);

        OutputModel resultado = useCase.execute(inputModel);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("João Silva");
        assertThat(resultado.email()).isEqualTo("joao@email.com");
        assertThat(resultado.login()).isEqualTo("joao");
        assertThat(resultado.tipoUsuario()).isEqualTo("CLIENTE");
        assertThat(resultado.endereco()).contains("Rua A");

        verify(usuarioGateway).existeLoginCadastrado("joao", null);
        verify(usuarioGateway).existeEmailCadastrado("joao@email.com", null);
        verify(tipoUsuarioGateway).obterPorNome("CLIENTE");
        verify(usuarioGateway).incluir(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve cadastrar usuário do tipo DONO_RESTAURANTE")
    void deveCadastrarUsuarioDonoRestaurante() {
        TipoUsuario tipoDono = TipoUsuario.create(2L, "DONO_RESTAURANTE", "Dono de Restaurante");
        InputModel inputDono = new InputModel(
                "Maria Silva",
                "maria@email.com",
                "maria",
                "senha456",
                "Rua B",
                "200",
                "Sala 5",
                "Vila Nova",
                "São Paulo",
                "SP",
                "02000-000",
                "DONO_RESTAURANTE");

        Endereco endereco = new Endereco("Rua B", "200", "Sala 5", "Vila Nova", "São Paulo", "SP", "02000-000");
        Usuario usuarioSalvo = Usuario.create(
                1L,
                "Maria Silva",
                "maria@email.com",
                "maria",
                "senha456",
                endereco,
                tipoDono);

        when(usuarioGateway.existeLoginCadastrado("maria", null)).thenReturn(false);
        when(usuarioGateway.existeEmailCadastrado("maria@email.com", null)).thenReturn(false);
        when(tipoUsuarioGateway.obterPorNome("DONO_RESTAURANTE")).thenReturn(Optional.of(tipoDono));
        when(usuarioGateway.incluir(any(Usuario.class))).thenReturn(usuarioSalvo);

        OutputModel resultado = useCase.execute(inputDono);

        assertThat(resultado.tipoUsuario()).isEqualTo("DONO_RESTAURANTE");
        verify(tipoUsuarioGateway).obterPorNome("DONO_RESTAURANTE");
    }

    @Test
    @DisplayName("Deve lançar exceção quando login já está cadastrado")
    void deveLancarExcecaoQuandoLoginJaCadastrado() {
        when(usuarioGateway.existeLoginCadastrado("joao", null)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(LoginJaCadastradoException.class)
                .hasMessageContaining("joao");

        verify(usuarioGateway).existeLoginCadastrado("joao", null);
        verify(usuarioGateway, never()).incluir(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já está cadastrado")
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        when(usuarioGateway.existeLoginCadastrado("joao", null)).thenReturn(false);
        when(usuarioGateway.existeEmailCadastrado("joao@email.com", null)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(inputModel))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining("joao@email.com");

        verify(usuarioGateway).existeEmailCadastrado("joao@email.com", null);
        verify(usuarioGateway, never()).incluir(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não existe")
    void deveLancarExcecaoQuandoTipoUsuarioNaoExiste() {
        InputModel inputInvalido = new InputModel(
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "TIPO_INVALIDO");

        when(usuarioGateway.existeLoginCadastrado("joao", null)).thenReturn(false);
        when(usuarioGateway.existeEmailCadastrado("joao@email.com", null)).thenReturn(false);
        when(tipoUsuarioGateway.obterPorNome("TIPO_INVALIDO")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(inputInvalido))
                .isInstanceOf(TipoUsuarioInvalidoException.class)
                .hasMessageContaining("TIPO_INVALIDO");

        verify(usuarioGateway, never()).incluir(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve cadastrar usuário com endereço completo")
    void deveCadastrarUsuarioComEnderecoCompleto() {
        InputModel inputComplemento = new InputModel(
                "Pedro Santos",
                "pedro@email.com",
                "pedro",
                "senha789",
                "Avenida Paulista",
                "1000",
                "Apto 501",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-100",
                "CLIENTE");

        Endereco endereco = new Endereco(
                "Avenida Paulista",
                "1000",
                "Apto 501",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-100");

        Usuario usuarioSalvo = Usuario.create(
                1L,
                "Pedro Santos",
                "pedro@email.com",
                "pedro",
                "senha789",
                endereco,
                tipoUsuarioCliente);

        when(usuarioGateway.existeLoginCadastrado("pedro", null)).thenReturn(false);
        when(usuarioGateway.existeEmailCadastrado("pedro@email.com", null)).thenReturn(false);
        when(tipoUsuarioGateway.obterPorNome("CLIENTE")).thenReturn(Optional.of(tipoUsuarioCliente));
        when(usuarioGateway.incluir(any(Usuario.class))).thenReturn(usuarioSalvo);

        OutputModel resultado = useCase.execute(inputComplemento);

        assertThat(resultado.endereco()).contains("Avenida Paulista");
        assertThat(resultado.endereco()).contains("Apto 501");
    }

    @Test
    @DisplayName("Deve normalizar tipo de usuário para uppercase")
    void deveNormalizarTipoUsuarioParaUppercase() {
        InputModel inputLowercase = new InputModel(
                "Ana Paula",
                "ana@email.com",
                "ana",
                "senha999",
                "Rua C",
                "300",
                "",
                "Jardins",
                "São Paulo",
                "SP",
                "03000-000",
                "cliente");

        Endereco endereco = new Endereco("Rua C", "300", "", "Jardins", "São Paulo", "SP", "03000-000");
        Usuario usuarioSalvo = Usuario.create(
                1L,
                "Ana Paula",
                "ana@email.com",
                "ana",
                "senha999",
                endereco,
                tipoUsuarioCliente);

        when(usuarioGateway.existeLoginCadastrado("ana", null)).thenReturn(false);
        when(usuarioGateway.existeEmailCadastrado("ana@email.com", null)).thenReturn(false);
        when(tipoUsuarioGateway.obterPorNome("CLIENTE")).thenReturn(Optional.of(tipoUsuarioCliente));
        when(usuarioGateway.incluir(any(Usuario.class))).thenReturn(usuarioSalvo);

        OutputModel resultado = useCase.execute(inputLowercase);

        assertThat(resultado).isNotNull();
        verify(tipoUsuarioGateway).obterPorNome("CLIENTE");
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        CadastrarUsuarioUseCase novoUseCase = CadastrarUsuarioUseCase.create(
                usuarioGateway,
                tipoUsuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}