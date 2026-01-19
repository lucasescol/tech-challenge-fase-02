package br.com.fiap.core.usecases.usuario;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.core.usecases.usuario.BuscarUsuariosPorNomeUseCase.OutputModel;
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
@DisplayName("BuscarUsuariosPorNomeUseCase - Testes Unitários")
class BuscarUsuariosPorNomeUseCaseTest {

    @Mock
    private IUsuarioGateway usuarioGateway;

    private BuscarUsuariosPorNomeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = BuscarUsuariosPorNomeUseCase.create(usuarioGateway);
    }

    @Test
    @DisplayName("Deve buscar usuários por nome com sucesso")
    void deveBuscarUsuariosPorNomeComSucesso() {
        TipoUsuario tipoCliente = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        Endereco endereco1 = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        Endereco endereco2 = new Endereco("Rua B", "200", "", "Jardins", "São Paulo", "SP", "01400-000");

        Usuario usuario1 = Usuario.create(1L, "João Silva", "joao@email.com", "joao", "senha123", endereco1,
                tipoCliente);
        Usuario usuario2 = Usuario.create(2L, "João Santos", "joao.santos@email.com", "joaosantos", "senha123",
                endereco2, tipoCliente);

        when(usuarioGateway.buscarPorNome("João")).thenReturn(Arrays.asList(usuario1, usuario2));

        List<OutputModel> resultado = useCase.execute("João");

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).nome()).isEqualTo("João Silva");
        assertThat(resultado.get(0).email()).isEqualTo("joao@email.com");

        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).nome()).isEqualTo("João Santos");

        verify(usuarioGateway).buscarPorNome("João");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não encontra usuários")
    void deveRetornarListaVaziaQuandoNaoEncontraUsuarios() {
        when(usuarioGateway.buscarPorNome("Inexistente")).thenReturn(Collections.emptyList());

        List<OutputModel> resultado = useCase.execute("Inexistente");

        assertThat(resultado).isEmpty();
        verify(usuarioGateway).buscarPorNome("Inexistente");
    }

    @Test
    @DisplayName("Deve criar useCase usando factory method")
    void deveCriarUseCaseUsandoFactoryMethod() {
        BuscarUsuariosPorNomeUseCase novoUseCase = BuscarUsuariosPorNomeUseCase.create(usuarioGateway);

        assertThat(novoUseCase).isNotNull();
    }
}