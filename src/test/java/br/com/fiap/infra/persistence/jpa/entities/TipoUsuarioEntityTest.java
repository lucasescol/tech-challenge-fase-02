package br.com.fiap.infra.persistence.jpa.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TipoUsuarioEntity - Testes Unitários")
class TipoUsuarioEntityTest {

    @Test
    @DisplayName("Deve criar entidade corretamente")
    void deveCriarEntidade() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity(1L, "ADMIN", "Administrador");

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getNome()).isEqualTo("ADMIN");
        assertThat(entity.getDescricao()).isEqualTo("Administrador");
    }

    @Test
    @DisplayName("Deve testar construtor sem ID")
    void deveTestarConstrutorSemId() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity("CLIENTE", "Cliente");

        assertThat(entity.getId()).isNull();
        assertThat(entity.getNome()).isEqualTo("CLIENTE");
        assertThat(entity.getDescricao()).isEqualTo("Cliente");
    }

    @Test
    @DisplayName("Deve testar getters e setters")
    void deveTestarGettersESetters() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.setId(2L);
        entity.setNome("TESTE");
        entity.setDescricao("Teste");

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getNome()).isEqualTo("TESTE");
        assertThat(entity.getDescricao()).isEqualTo("Teste");
    }
}