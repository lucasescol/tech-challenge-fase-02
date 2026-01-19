package br.com.fiap.infra.gateways;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.RestauranteEntity;
import br.com.fiap.infra.persistence.jpa.repositories.RestauranteRepository;
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
@DisplayName("JpaRestauranteGateway - Testes Unitários")
class JpaRestauranteGatewayTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private JpaRestauranteGateway gateway;

    @Test
    @DisplayName("Deve incluir restaurante com sucesso")
    void deveIncluirRestaurante() {
        Restaurante restaurante = Restaurante.create(null, "Restaurante Teste", "Rua A", "100", "", "Bairro", "Cidade",
                "SP", "00000-000", "ITALIANA", "08:00-18:00", 1L);
        EnderecoEntity enderecoEntity = new EnderecoEntity(1L, "Rua A", "100", "", "Bairro", "Cidade", "SP",
                "00000000");
        RestauranteEntity entitySalva = new RestauranteEntity(1L, "Restaurante Teste", enderecoEntity, "ITALIANA",
                "08:00-18:00", 1L, null, null, null);

        when(restauranteRepository.save(any(RestauranteEntity.class))).thenReturn(entitySalva);

        Restaurante resultado = gateway.incluir(restaurante);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Restaurante Teste");
        verify(restauranteRepository).save(any(RestauranteEntity.class));
    }

    @Test
    @DisplayName("Deve obter restaurante por ID")
    void deveObterPorId() {
        EnderecoEntity enderecoEntity = new EnderecoEntity(1L, "Rua A", "100", "", "Bairro", "Cidade", "SP",
                "00000000");
        RestauranteEntity entity = new RestauranteEntity(1L, "Restaurante Teste", enderecoEntity, "ITALIANA",
                "08:00-18:00", 1L, null, null, null);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Restaurante> resultado = gateway.obterPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Restaurante Teste");
    }

    @Test
    @DisplayName("Deve listar todos os restaurantes")
    void deveListarTodos() {
        EnderecoEntity enderecoEntity = new EnderecoEntity(1L, "Rua A", "100", "", "Bairro", "Cidade", "SP",
                "00000000");
        RestauranteEntity entity = new RestauranteEntity(1L, "Restaurante Teste", enderecoEntity, "ITALIANA",
                "08:00-18:00", 1L, null, null, null);

        when(restauranteRepository.findAll()).thenReturn(List.of(entity));

        List<Restaurante> resultado = gateway.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Restaurante Teste");
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void deveAtualizarRestaurante() {
        Restaurante restaurante = Restaurante.create(1L, "Restaurante Atualizado", "Rua A", "100", "", "Bairro",
                "Cidade", "SP", "00000-000", "ITALIANA", "08:00-18:00", 1L);
        EnderecoEntity enderecoEntity = new EnderecoEntity(1L, "Rua A", "100", "", "Bairro", "Cidade", "SP",
                "00000000");
        RestauranteEntity entitySalva = new RestauranteEntity(1L, "Restaurante Atualizado", enderecoEntity, "ITALIANA",
                "08:00-18:00", 1L, null, null, null);

        when(restauranteRepository.save(any(RestauranteEntity.class))).thenReturn(entitySalva);

        Restaurante resultado = gateway.atualizar(restaurante);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Restaurante Atualizado");
    }

    @Test
    @DisplayName("Deve deletar restaurante por ID")
    void deveDeletarRestaurante() {
        doNothing().when(restauranteRepository).deleteById(1L);

        gateway.deletar(1L);

        verify(restauranteRepository).deleteById(1L);
    }
}