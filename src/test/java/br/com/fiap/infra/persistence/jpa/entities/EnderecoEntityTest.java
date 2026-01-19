package br.com.fiap.infra.persistence.jpa.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EnderecoEntity - Testes Unitários")
class EnderecoEntityTest {

    @Test
    @DisplayName("Deve criar entidade corretamente")
    void deveCriarEntidade() {
        EnderecoEntity entity = new EnderecoEntity(
                1L, "Rua A", "100", "Apto 1", "Centro", "SP", "SP", "01000000");

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getLogradouro()).isEqualTo("Rua A");
        assertThat(entity.getNumero()).isEqualTo("100");
        assertThat(entity.getComplemento()).isEqualTo("Apto 1");
        assertThat(entity.getBairro()).isEqualTo("Centro");
        assertThat(entity.getCidade()).isEqualTo("SP");
        assertThat(entity.getEstado()).isEqualTo("SP");
        assertThat(entity.getCep()).isEqualTo("01000000");
    }

    @Test
    @DisplayName("Deve testar getters e setters")
    void deveTestarGettersESetters() {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(2L);
        entity.setLogradouro("Rua B");
        entity.setNumero("200");
        entity.setComplemento("Casa");
        entity.setBairro("Bairro");
        entity.setCidade("RJ");
        entity.setEstado("RJ");
        entity.setCep("02000000");

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getLogradouro()).isEqualTo("Rua B");
        assertThat(entity.getNumero()).isEqualTo("200");
        assertThat(entity.getComplemento()).isEqualTo("Casa");
        assertThat(entity.getBairro()).isEqualTo("Bairro");
        assertThat(entity.getCidade()).isEqualTo("RJ");
        assertThat(entity.getEstado()).isEqualTo("RJ");
        assertThat(entity.getCep()).isEqualTo("02000000");
    }
}