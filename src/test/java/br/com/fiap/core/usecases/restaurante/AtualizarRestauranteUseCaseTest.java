package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.restaurante.AtualizarRestauranteUseCase.InputModel;
import br.com.fiap.core.usecases.restaurante.AtualizarRestauranteUseCase.OutputModel;
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
@DisplayName("AtualizarRestauranteUseCase - Testes Unitários")
class AtualizarRestauranteUseCaseTest {

    @Mock
    private IRestauranteGateway restauranteGateway;

    @Mock
    private IAuthenticationGateway authenticationGateway;

    @Mock
    private IUsuarioGateway usuarioGateway;

    private AtualizarRestauranteUseCase useCase;

    private Restaurante restauranteExistente;
    private Usuario usuarioProprietario;
    private InputModel inputModel;

    @BeforeEach
    void setUp() {
        useCase = AtualizarRestauranteUseCase.create(
                restauranteGateway,
                authenticationGateway,
                usuarioGateway);

        TipoUsuario tipoDonoRestaurante = TipoUsuario.create(1L, "DONO_RESTAURANTE", "Dono de Restaurante");
        Endereco enderecoUsuario = new Endereco("Rua A", "100", "Apto 1", "Centro", "São Paulo", "SP", "01000-000");
        usuarioProprietario = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "senha123", enderecoUsuario,
                tipoDonoRestaurante);

        restauranteExistente = Restaurante.create(
                1L,
                "Restaurante Teste",
                "Rua X",
                "100",
                "Sala 1",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        inputModel = new InputModel(
                "Restaurante Atualizado",
                "Rua Y",
                "200",
                "Sala 2",
                "Bairro Novo",
                "Rio de Janeiro",
                "RJ",
                "20000-000",
                "JAPONESA",
                "10:00-23:00");
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso quando usuário é proprietário")
    void deveAtualizarRestauranteQuandoUsuarioEhProprietario() {
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restauranteExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
        when(usuarioGateway.buscarPorLogin("joao")).thenReturn(Optional.of(usuarioProprietario));
        when(authenticationGateway.isAdministrador()).thenReturn(false);

        Restaurante restauranteAtualizado = Restaurante.create(
                1L,
                inputModel.nome(),
                inputModel.logradouro(),
                inputModel.numero(),
                inputModel.complemento(),
                inputModel.bairro(),
                inputModel.cidade(),
                inputModel.estado(),
                inputModel.cep(),
                inputModel.tipoCozinha(),
                inputModel.horarioFuncionamento(),
                1L);

        when(restauranteGateway.atualizar(any(Restaurante.class))).thenReturn(restauranteAtualizado);

        Optional<OutputModel> resultado = useCase.execute(1L, inputModel);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().id()).isEqualTo(1L);
        assertThat(resultado.get().nome()).isEqualTo("Restaurante Atualizado");
        assertThat(resultado.get().tipoCozinha()).isEqualTo("JAPONESA");

        verify(restauranteGateway).obterPorId(1L);
        verify(restauranteGateway).atualizar(any(Restaurante.class));
        verify(authenticationGateway).getUsuarioLogado();
        verify(usuarioGateway).buscarPorLogin("joao");
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso quando usuário é administrador")
    void deveAtualizarRestauranteQuandoUsuarioEhAdministrador() {
        TipoUsuario tipoAdmin = TipoUsuario.create(2L, "ADMINISTRADOR", "Administrador do Sistema");
        Endereco enderecoAdmin = new Endereco("Rua Admin", "1", "", "Centro", "São Paulo", "SP", "01000-000");
        Usuario usuarioAdmin = Usuario.create(2L, "Admin", "admin@email.com", "admin", "senha123", enderecoAdmin,
                tipoAdmin);

        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restauranteExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("admin");
        when(usuarioGateway.buscarPorLogin("admin")).thenReturn(Optional.of(usuarioAdmin));
        when(authenticationGateway.isAdministrador()).thenReturn(true);

        Restaurante restauranteAtualizado = Restaurante.create(
                1L,
                inputModel.nome(),
                inputModel.logradouro(),
                inputModel.numero(),
                inputModel.complemento(),
                inputModel.bairro(),
                inputModel.cidade(),
                inputModel.estado(),
                inputModel.cep(),
                inputModel.tipoCozinha(),
                inputModel.horarioFuncionamento(),
                1L);

        when(restauranteGateway.atualizar(any(Restaurante.class))).thenReturn(restauranteAtualizado);

        Optional<OutputModel> resultado = useCase.execute(1L, inputModel);

        assertThat(resultado).isPresent();
        verify(restauranteGateway).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve retornar vazio quando restaurante não existe")
    void deveRetornarVazioQuandoRestauranteNaoExiste() {
        when(restauranteGateway.obterPorId(999L)).thenReturn(Optional.empty());

        Optional<OutputModel> resultado = useCase.execute(999L, inputModel);

        assertThat(resultado).isEmpty();
        verify(restauranteGateway).obterPorId(999L);
        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é proprietário nem administrador")
    void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {
        TipoUsuario tipoCliente = TipoUsuario.create(3L, "CLIENTE", "Cliente");
        Endereco enderecoOutro = new Endereco("Rua B", "200", "", "Centro", "São Paulo", "SP", "01000-000");
        Usuario outroUsuario = Usuario.create(2L, "Maria", "maria@email.com", "maria", "senha123", enderecoOutro,
                tipoCliente);

        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restauranteExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("maria");
        when(usuarioGateway.buscarPorLogin("maria")).thenReturn(Optional.of(outroUsuario));
        when(authenticationGateway.isAdministrador()).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining(
                        "Apenas o proprietário do restaurante ou administradores podem atualizar este restaurante");

        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não é encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
        when(restauranteGateway.obterPorId(1L)).thenReturn(Optional.of(restauranteExistente));
        when(authenticationGateway.getUsuarioLogado()).thenReturn("usuario_inexistente");
        when(usuarioGateway.buscarPorLogin("usuario_inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L, inputModel))
                .isInstanceOf(AcessoNegadoException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(restauranteGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        AtualizarRestauranteUseCase novouseCase = AtualizarRestauranteUseCase.create(
                restauranteGateway,
                authenticationGateway,
                usuarioGateway);

        assertThat(novouseCase).isNotNull();
    }
}