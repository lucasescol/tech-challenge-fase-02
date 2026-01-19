package br.com.fiap.infra.persistence.jpa.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UsuarioEntity - Testes Unitários")
class UsuarioEntityTest {

    @Test
    @DisplayName("Deve criar entidade corretamente")
    void deveCriarEntidade() {
        EnderecoEntity endereco = new EnderecoEntity();
        TipoUsuarioEntity tipo = new TipoUsuarioEntity();
        LocalDateTime now = LocalDateTime.now();

        UsuarioEntity entity = new UsuarioEntity(
                1L, "João", "joao@email.com", "joao", "senha", endereco, tipo, now, now);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getNome()).isEqualTo("João");
        assertThat(entity.getEmail()).isEqualTo("joao@email.com");
        assertThat(entity.getLogin()).isEqualTo("joao");
        assertThat(entity.getSenha()).isEqualTo("senha");
        assertThat(entity.getEndereco()).isEqualTo(endereco);
        assertThat(entity.getTipoUsuario()).isEqualTo(tipo);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Deve testar lifecycle callbacks")
    void deveTestarLifecycleCallbacks() {
        UsuarioEntity entity = new UsuarioEntity();

        entity.onCreate();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();

        LocalDateTime oldUpdate = entity.getUpdatedAt();

        // Simula um pequeno delay
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        entity.onUpdate();
        assertThat(entity.getUpdatedAt()).isAfter(oldUpdate);
    }
}