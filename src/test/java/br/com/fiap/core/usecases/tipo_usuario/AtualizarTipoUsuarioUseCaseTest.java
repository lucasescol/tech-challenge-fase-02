package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.usecases.tipo_usuario.AtualizarTipoUsuarioUseCase.InputModel;
import br.com.fiap.core.usecases.tipo_usuario.AtualizarTipoUsuarioUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarTipoUsuarioUseCase - Testes Unitários")
class AtualizarTipoUsuarioUseCaseTest {

    @Mock
    private ITipoUsuarioGateway tipoUsuarioGateway;

    private AtualizarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = AtualizarTipoUsuarioUseCase.create(tipoUsuarioGateway);
    }

    @Test
    @DisplayName("Deve atualizar tipo de usuário com sucesso")
    void deveAtualizarTipoUsuarioComSucesso() {
        TipoUsuario tipoExistente = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        InputModel inputModel = new InputModel("CLIENTE_VIP", "Cliente VIP com benefícios");

        when(tipoUsuarioGateway.obterPorId(1L)).thenReturn(Optional.of(tipoExistente));

        TipoUsuario tipoAtualizado = TipoUsuario.create(1L, "CLIENTE_VIP", "Cliente VIP com benefícios");
        when(tipoUsuarioGateway.atualizar(any(TipoUsuario.class))).thenReturn(tipoAtualizado);

        Optional<OutputModel> resultado = useCase.execute(1L, inputModel);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().id()).isEqualTo(1L);
        assertThat(resultado.get().nome()).isEqualTo("CLIENTE_VIP");
        assertThat(resultado.get().descricao()).isEqualTo("Cliente VIP com benefícios");

        verify(tipoUsuarioGateway).obterPorId(1L);
        verify(tipoUsuarioGateway).atualizar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve retornar vazio quando tipo de usuário não existe")
    void deveRetornarVazioQuandoTipoUsuarioNaoExiste() {
        InputModel inputModel = new InputModel("TESTE", "Descrição teste");
        when(tipoUsuarioGateway.obterPorId(999L)).thenReturn(Optional.empty());

        Optional<OutputModel> resultado = useCase.execute(999L, inputModel);

        assertThat(resultado).isEmpty();
        verify(tipoUsuarioGateway).obterPorId(999L);
        verify(tipoUsuarioGateway, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        AtualizarTipoUsuarioUseCase novoUseCase = AtualizarTipoUsuarioUseCase.create(tipoUsuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}