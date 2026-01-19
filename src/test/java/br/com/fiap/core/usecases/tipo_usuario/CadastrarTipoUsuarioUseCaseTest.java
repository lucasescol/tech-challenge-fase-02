package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.usecases.tipo_usuario.CadastrarTipoUsuarioUseCase.InputModel;
import br.com.fiap.core.usecases.tipo_usuario.CadastrarTipoUsuarioUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarTipoUsuarioUseCase - Testes Unitários")
class CadastrarTipoUsuarioUseCaseTest {

    @Mock
    private ITipoUsuarioGateway tipoUsuarioGateway;

    private CadastrarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = CadastrarTipoUsuarioUseCase.create(tipoUsuarioGateway);
    }

    @Test
    @DisplayName("Deve cadastrar tipo de usuário com sucesso")
    void deveCadastrarTipoUsuarioComSucesso() {
        InputModel inputModel = new InputModel("CLIENTE", "Cliente do sistema");

        TipoUsuario tipoSalvo = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        when(tipoUsuarioGateway.incluir(any(TipoUsuario.class))).thenReturn(tipoSalvo);

        OutputModel resultado = useCase.execute(inputModel);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("CLIENTE");
        assertThat(resultado.descricao()).isEqualTo("Cliente do sistema");

        verify(tipoUsuarioGateway).incluir(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        CadastrarTipoUsuarioUseCase novoUseCase = CadastrarTipoUsuarioUseCase.create(tipoUsuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}