package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.usecases.tipo_usuario.ObterTipoUsuarioPorIdUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterTipoUsuarioPorIdUseCase - Testes Unitários")
class ObterTipoUsuarioPorIdUseCaseTest {

    @Mock
    private ITipoUsuarioGateway tipoUsuarioGateway;

    private ObterTipoUsuarioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterTipoUsuarioPorIdUseCase.create(tipoUsuarioGateway);
    }

    @Test
    @DisplayName("Deve obter tipo de usuário por ID com sucesso")
    void deveObterTipoUsuarioPorIdComSucesso() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        when(tipoUsuarioGateway.obterPorId(1L)).thenReturn(Optional.of(tipo));

        Optional<OutputModel> resultado = useCase.execute(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().id()).isEqualTo(1L);
        assertThat(resultado.get().nome()).isEqualTo("CLIENTE");
        assertThat(resultado.get().descricao()).isEqualTo("Cliente do sistema");

        verify(tipoUsuarioGateway).obterPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio quando tipo de usuário não existe")
    void deveRetornarVazioQuandoTipoUsuarioNaoExiste() {
        when(tipoUsuarioGateway.obterPorId(999L)).thenReturn(Optional.empty());

        Optional<OutputModel> resultado = useCase.execute(999L);

        assertThat(resultado).isEmpty();
        verify(tipoUsuarioGateway).obterPorId(999L);
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        ObterTipoUsuarioPorIdUseCase novoUseCase = ObterTipoUsuarioPorIdUseCase.create(tipoUsuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}