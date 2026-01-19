package br.com.fiap.infra.gateways;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.repositories.TipoUsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JpaTipoUsuarioGateway - Testes Unitários")
class JpaTipoUsuarioGatewayTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private JpaTipoUsuarioGateway gateway;

    @Test
    @DisplayName("Deve incluir tipo de usuário")
    void deveIncluirTipoUsuario() {
        TipoUsuario tipo = TipoUsuario.create(null, "CLIENTE", "Cliente");
        TipoUsuarioEntity entitySalva = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");

        when(tipoUsuarioRepository.save(any(TipoUsuarioEntity.class))).thenReturn(entitySalva);

        TipoUsuario resultado = gateway.incluir(tipo);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Deve obter tipo de usuário por ID")
    void deveObterPorId() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<TipoUsuario> resultado = gateway.obterPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Deve obter tipo de usuário por nome")
    void deveObterPorNome() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");
        when(tipoUsuarioRepository.findByNome("CLIENTE")).thenReturn(Optional.of(entity));

        Optional<TipoUsuario> resultado = gateway.obterPorNome("CLIENTE");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Deve listar todos os tipos de usuário")
    void deveListarTodos() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");
        when(tipoUsuarioRepository.findAll()).thenReturn(List.of(entity));

        List<TipoUsuario> resultado = gateway.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("Deve atualizar tipo de usuário")
    void deveAtualizarTipoUsuario() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE_VIP", "Cliente Vip");
        TipoUsuarioEntity entitySalva = new TipoUsuarioEntity(1L, "CLIENTE_VIP", "Cliente Vip");

        when(tipoUsuarioRepository.save(any(TipoUsuarioEntity.class))).thenReturn(entitySalva);

        TipoUsuario resultado = gateway.atualizar(tipo);

        assertThat(resultado.getNome()).isEqualTo("CLIENTE_VIP");
    }

    @Test
    @DisplayName("Deve deletar tipo de usuário")
    void deveDeletarTipoUsuario() {
        doNothing().when(tipoUsuarioRepository).deleteById(1L);

        gateway.deletar(1L);

        verify(tipoUsuarioRepository).deleteById(1L);
    }
}