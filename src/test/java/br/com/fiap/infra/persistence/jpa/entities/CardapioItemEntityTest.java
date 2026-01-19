package br.com.fiap.infra.persistence.jpa.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CardapioItemEntity - Testes Unitários")
class CardapioItemEntityTest {

    @Test
    @DisplayName("Deve criar entidade corretamente")
    void deveCriarEntidade() {
        CardapioItemEntity entity = new CardapioItemEntity(
                1L, 10L, "Pizza", "Deliciosa", 50.0, false, "/img.jpg");

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getRestauranteId()).isEqualTo(10L);
        assertThat(entity.getNome()).isEqualTo("Pizza");
        assertThat(entity.getDescricao()).isEqualTo("Deliciosa");
        assertThat(entity.getPreco()).isEqualTo(50.0);
        assertThat(entity.isApenasPresencial()).isFalse();
        assertThat(entity.getCaminhoFoto()).isEqualTo("/img.jpg");
    }

    @Test
    @DisplayName("Deve testar getters e setters")
    void deveTestarGettersESetters() {
        CardapioItemEntity entity = new CardapioItemEntity();
        entity.setId(2L);
        entity.setRestauranteId(20L);
        entity.setNome("Burger");
        entity.setDescricao("Suculento");
        entity.setPreco(30.0);
        entity.setApenasPresencial(true);
        entity.setCaminhoFoto("/burger.jpg");

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getRestauranteId()).isEqualTo(20L);
        assertThat(entity.getNome()).isEqualTo("Burger");
        assertThat(entity.getDescricao()).isEqualTo("Suculento");
        assertThat(entity.getPreco()).isEqualTo(30.0);
        assertThat(entity.isApenasPresencial()).isTrue();
        assertThat(entity.getCaminhoFoto()).isEqualTo("/burger.jpg");
    }
}