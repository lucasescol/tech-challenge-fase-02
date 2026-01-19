package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
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
@DisplayName("DeletarTipoUsuarioUseCase - Testes Unitários")
class DeletarTipoUsuarioUseCaseTest {

    @Mock
    private ITipoUsuarioGateway tipoUsuarioGateway;

    private DeletarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = DeletarTipoUsuarioUseCase.create(tipoUsuarioGateway);
    }

    @Test
    @DisplayName("Deve deletar tipo de usuário com sucesso")
    void deveDeletarTipoUsuarioComSucesso() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        when(tipoUsuarioGateway.obterPorId(1L)).thenReturn(Optional.of(tipo));
        doNothing().when(tipoUsuarioGateway).deletar(1L);

        boolean resultado = useCase.execute(1L);

        assertThat(resultado).isTrue();
        verify(tipoUsuarioGateway).obterPorId(1L);
        verify(tipoUsuarioGateway).deletar(1L);
    }

    @Test
    @DisplayName("Deve retornar false quando tipo de usuário não existe")
    void deveRetornarFalseQuandoTipoUsuarioNaoExiste() {
        when(tipoUsuarioGateway.obterPorId(999L)).thenReturn(Optional.empty());

        boolean resultado = useCase.execute(999L);

        assertThat(resultado).isFalse();
        verify(tipoUsuarioGateway).obterPorId(999L);
        verify(tipoUsuarioGateway, never()).deletar(anyLong());
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        DeletarTipoUsuarioUseCase novoUseCase = DeletarTipoUsuarioUseCase.create(tipoUsuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}