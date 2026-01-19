package br.com.fiap.core.usecases.tipo_usuario;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.core.usecases.tipo_usuario.ListarTodosTiposUsuarioUseCase.OutputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarTodosTiposUsuarioUseCase - Testes Unitários")
class ListarTodosTiposUsuarioUseCaseTest {

    @Mock
    private ITipoUsuarioGateway tipoUsuarioGateway;

    private ListarTodosTiposUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ListarTodosTiposUsuarioUseCase.create(tipoUsuarioGateway);
    }

    @Test
    @DisplayName("Deve listar todos os tipos de usuário com sucesso")
    void deveListarTodosTiposUsuarioComSucesso() {
        TipoUsuario tipo1 = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        TipoUsuario tipo2 = TipoUsuario.create(2L, "DONO_RESTAURANTE", "Dono de restaurante");
        TipoUsuario tipo3 = TipoUsuario.create(3L, "ADMINISTRADOR", "Administrador do sistema");

        when(tipoUsuarioGateway.listarTodos()).thenReturn(Arrays.asList(tipo1, tipo2, tipo3));

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).nome()).isEqualTo("CLIENTE");
        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).nome()).isEqualTo("DONO_RESTAURANTE");
        assertThat(resultado.get(2).id()).isEqualTo(3L);
        assertThat(resultado.get(2).nome()).isEqualTo("ADMINISTRADOR");

        verify(tipoUsuarioGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há tipos de usuário")
    void deveRetornarListaVaziaQuandoNaoHaTipos() {
        when(tipoUsuarioGateway.listarTodos()).thenReturn(Collections.emptyList());

        List<OutputModel> resultado = useCase.execute();

        assertThat(resultado).isEmpty();
        verify(tipoUsuarioGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        ListarTodosTiposUsuarioUseCase novoUseCase = ListarTodosTiposUsuarioUseCase.create(tipoUsuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}