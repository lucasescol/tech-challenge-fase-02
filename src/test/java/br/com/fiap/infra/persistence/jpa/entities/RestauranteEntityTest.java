package br.com.fiap.infra.persistence.jpa.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RestauranteEntity - Testes Unitários")
class RestauranteEntityTest {

    @Test
    @DisplayName("Deve criar entidade corretamente")
    void deveCriarEntidade() {
        EnderecoEntity endereco = new EnderecoEntity();
        LocalDateTime now = LocalDateTime.now();
        RestauranteEntity entity = new RestauranteEntity(
                1L, "Restaurante", endereco, "ITALIANA", "08:00-22:00", 10L, now, now, "ATIVO");

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getNome()).isEqualTo("Restaurante");
        assertThat(entity.getEndereco()).isEqualTo(endereco);
        assertThat(entity.getTipoCozinha()).isEqualTo("ITALIANA");
        assertThat(entity.getHorarioFuncionamento()).isEqualTo("08:00-22:00");
        assertThat(entity.getDonoRestaurante()).isEqualTo(10L);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
        assertThat(entity.getStatus()).isEqualTo("ATIVO");
    }

    @Test
    @DisplayName("Deve testar getters e setters")
    void deveTestarGettersESetters() {
        RestauranteEntity entity = new RestauranteEntity();
        EnderecoEntity endereco = new EnderecoEntity();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(2L);
        entity.setNome("Novo");
        entity.setEndereco(endereco);
        entity.setTipoCozinha("JAPONESA");
        entity.setHorarioFuncionamento("10:00-23:00");
        entity.setDonoRestaurante(20L);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setStatus("INATIVO");

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getNome()).isEqualTo("Novo");
        assertThat(entity.getEndereco()).isEqualTo(endereco);
        assertThat(entity.getTipoCozinha()).isEqualTo("JAPONESA");
        assertThat(entity.getHorarioFuncionamento()).isEqualTo("10:00-23:00");
        assertThat(entity.getDonoRestaurante()).isEqualTo(20L);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
        assertThat(entity.getStatus()).isEqualTo("INATIVO");
    }
}