package br.com.fiap.core.usecases.restaurante;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.AcessoNegadoException;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.restaurante.CadastrarRestauranteUseCase.InputModel;
import br.com.fiap.core.usecases.restaurante.CadastrarRestauranteUseCase.OutputModel;
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
@DisplayName("CadastrarRestauranteUseCase - Testes Unitários")
class CadastrarRestauranteUseCaseTest {

        @Mock
        private IRestauranteGateway restauranteGateway;

        @Mock
        private IAuthenticationGateway authenticationGateway;

        @Mock
        private IUsuarioGateway usuarioGateway;

        private CadastrarRestauranteUseCase useCase;

        private Usuario usuarioDono;
        private InputModel inputModel;

        @BeforeEach
        void setUp() {
                useCase = CadastrarRestauranteUseCase.create(
                                restauranteGateway,
                                authenticationGateway,
                                usuarioGateway);

                TipoUsuario tipoDono = TipoUsuario.create(1L, "DONO_RESTAURANTE", "Dono de Restaurante");
                Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
                usuarioDono = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "senha123", endereco,
                                tipoDono);

                inputModel = new InputModel(
                                "Restaurante Teste",
                                "Rua X",
                                "100",
                                "Sala 1",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01000-000",
                                "ITALIANA",
                                "08:00-22:00");
        }

        @Test
        @DisplayName("Deve cadastrar restaurante com sucesso quando usuário é dono de restaurante")
        void deveCadastrarRestauranteQuandoUsuarioEhDonoRestaurante() {
                when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
                when(authenticationGateway.getUsuarioLogado()).thenReturn("joao");
                when(usuarioGateway.buscarPorLogin("joao")).thenReturn(Optional.of(usuarioDono));

                Restaurante restauranteSalvo = Restaurante.create(
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

                when(restauranteGateway.incluir(any(Restaurante.class))).thenReturn(restauranteSalvo);

                OutputModel resultado = useCase.execute(inputModel);

                assertThat(resultado).isNotNull();
                assertThat(resultado.id()).isEqualTo(1L);
                assertThat(resultado.nome()).isEqualTo("Restaurante Teste");
                assertThat(resultado.tipoCozinha()).isEqualTo("ITALIANA");
                assertThat(resultado.horarioFuncionamento()).isEqualTo("08:00-22:00");

                verify(restauranteGateway).incluir(any(Restaurante.class));
                verify(authenticationGateway).getUsuarioLogado();
                verify(usuarioGateway).buscarPorLogin("joao");
        }

        @Test
        @DisplayName("Deve cadastrar restaurante com sucesso quando usuário é administrador")
        void deveCadastrarRestauranteQuandoUsuarioEhAdministrador() {
                TipoUsuario tipoAdmin = TipoUsuario.create(2L, "ADMINISTRADOR", "Administrador");
                Endereco enderecoAdmin = new Endereco("Rua B", "200", "", "Centro", "São Paulo", "SP", "01000-000");
                Usuario usuarioAdmin = Usuario.create(2L, "Admin", "admin@email.com", "admin", "senha123",
                                enderecoAdmin, tipoAdmin);

                when(authenticationGateway.isDonoRestaurante()).thenReturn(false);
                when(authenticationGateway.isAdministrador()).thenReturn(true);
                when(authenticationGateway.getUsuarioLogado()).thenReturn("admin");
                when(usuarioGateway.buscarPorLogin("admin")).thenReturn(Optional.of(usuarioAdmin));

                Restaurante restauranteSalvo = Restaurante.create(
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
                                2L);

                when(restauranteGateway.incluir(any(Restaurante.class))).thenReturn(restauranteSalvo);

                OutputModel resultado = useCase.execute(inputModel);

                assertThat(resultado).isNotNull();
                assertThat(resultado.id()).isEqualTo(1L);
                verify(restauranteGateway).incluir(any(Restaurante.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não tem permissão")
        void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {
                when(authenticationGateway.isDonoRestaurante()).thenReturn(false);
                when(authenticationGateway.isAdministrador()).thenReturn(false);

                assertThatThrownBy(() -> useCase.execute(inputModel))
                                .isInstanceOf(AcessoNegadoException.class)
                                .hasMessageContaining(
                                                "Apenas donos de restaurante e administradores podem cadastrar restaurantes");

                verify(restauranteGateway, never()).incluir(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário logado não é encontrado")
        void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
                when(authenticationGateway.isDonoRestaurante()).thenReturn(true);
                when(authenticationGateway.getUsuarioLogado()).thenReturn("usuario_inexistente");
                when(usuarioGateway.buscarPorLogin("usuario_inexistente")).thenReturn(Optional.empty());

                assertThatThrownBy(() -> useCase.execute(inputModel))
                                .isInstanceOf(AcessoNegadoException.class)
                                .hasMessageContaining("Usuário não encontrado");

                verify(restauranteGateway, never()).incluir(any());
        }

        @Test
        @DisplayName("Deve criar useCase usando factory method")
        void deveCriarUseCaseUsandoFactoryMethod() {
                CadastrarRestauranteUseCase novoUseCase = CadastrarRestauranteUseCase.create(
                                restauranteGateway,
                                authenticationGateway,
                                usuarioGateway);

                assertThat(novoUseCase).isNotNull();
        }
}