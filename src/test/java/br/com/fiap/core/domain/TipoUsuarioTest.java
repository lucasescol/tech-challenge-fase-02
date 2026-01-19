package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TipoUsuario - Testes de Domínio")
class TipoUsuarioTest {

    @Test
    @DisplayName("Deve criar tipo de usuário com sucesso")
    void deveCriarTipoUsuarioComSucesso() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");

        assertThat(tipo).isNotNull();
        assertThat(tipo.getId()).isEqualTo(1L);
        assertThat(tipo.getNome()).isEqualTo("CLIENTE");
        assertThat(tipo.getDescricao()).isEqualTo("Cliente do sistema");
    }

    @Test
    @DisplayName("Deve criar tipo de usuário sem ID")
    void deveCriarTipoUsuarioSemId() {
        TipoUsuario tipo = TipoUsuario.create(null, "DONO_RESTAURANTE", "Dono de restaurante");

        assertThat(tipo.getId()).isNull();
        assertThat(tipo.getNome()).isEqualTo("DONO_RESTAURANTE");
    }

    @Test
    @DisplayName("Deve fazer trim no nome e descrição")
    void deveFazerTrimNosCampos() {
        TipoUsuario tipo = TipoUsuario.create(
                1L,
                "  ADMINISTRADOR  ",
                "  Administrador do sistema  ");

        assertThat(tipo.getNome()).isEqualTo("ADMINISTRADOR");
        assertThat(tipo.getDescricao()).isEqualTo("Administrador do sistema");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        assertThatThrownBy(() -> TipoUsuario.create(1L, null, "Descrição"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do tipo de usuário não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é vazio")
    void deveLancarExcecaoQuandoNomeVazio() {
        assertThatThrownBy(() -> TipoUsuario.create(1L, "", "Descrição"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do tipo de usuário não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoCurto() {
        assertThatThrownBy(() -> TipoUsuario.create(1L, "AB", "Descrição"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do tipo de usuário deve ter no mínimo 3 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem mais de 50 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoLongo() {
        String nomeLongo = "A".repeat(51);

        assertThatThrownBy(() -> TipoUsuario.create(1L, nomeLongo, "Descrição"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do tipo de usuário deve ter no máximo 50 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando descrição é nula")
    void deveLancarExcecaoQuandoDescricaoNula() {
        assertThatThrownBy(() -> TipoUsuario.create(1L, "CLIENTE", null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Descrição do tipo de usuário não pode ser vazia");
    }

    @Test
    @DisplayName("Deve lançar exceção quando descrição é vazia")
    void deveLancarExcecaoQuandoDescricaoVazia() {
        assertThatThrownBy(() -> TipoUsuario.create(1L, "CLIENTE", ""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Descrição do tipo de usuário não pode ser vazia");
    }

    @Test
    @DisplayName("Deve lançar exceção quando descrição tem menos de 3 caracteres")
    void deveLancarExcecaoQuandoDescricaoMuitoCurta() {
        assertThatThrownBy(() -> TipoUsuario.create(1L, "CLIENTE", "AB"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Descrição do tipo de usuário deve ter no mínimo 3 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando descrição tem mais de 200 caracteres")
    void deveLancarExcecaoQuandoDescricaoMuitoLonga() {
        String descricaoLonga = "A".repeat(201);

        assertThatThrownBy(() -> TipoUsuario.create(1L, "CLIENTE", descricaoLonga))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Descrição do tipo de usuário deve ter no máximo 200 caracteres");
    }

    @Test
    @DisplayName("Deve criar tipo com nome no limite mínimo (3 caracteres)")
    void deveCriarTipoComNomeLimiteMinimo() {
        TipoUsuario tipo = TipoUsuario.create(1L, "ABC", "Descrição teste");

        assertThat(tipo.getNome()).isEqualTo("ABC");
    }

    @Test
    @DisplayName("Deve criar tipo com nome no limite máximo (50 caracteres)")
    void deveCriarTipoComNomeLimiteMaximo() {
        String nome50 = "A".repeat(50);

        TipoUsuario tipo = TipoUsuario.create(1L, nome50, "Descrição teste");

        assertThat(tipo.getNome()).hasSize(50);
    }

    @Test
    @DisplayName("Deve criar tipo com descrição no limite mínimo (3 caracteres)")
    void deveCriarTipoComDescricaoLimiteMinimo() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "ABC");

        assertThat(tipo.getDescricao()).isEqualTo("ABC");
    }

    @Test
    @DisplayName("Deve criar tipo com descrição no limite máximo (200 caracteres)")
    void deveCriarTipoComDescricaoLimiteMaximo() {
        String descricao200 = "A".repeat(200);

        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", descricao200);

        assertThat(tipo.getDescricao()).hasSize(200);
    }

    @Test
    @DisplayName("Deve verificar igualdade entre tipos de usuário")
    void deveVerificarIgualdade() {
        TipoUsuario tipo1 = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        TipoUsuario tipo2 = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");

        assertThat(tipo1).isEqualTo(tipo2);
        assertThat(tipo1.hashCode()).isEqualTo(tipo2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");

        String toString = tipo.toString();

        assertThat(toString)
                .contains("id=1")
                .contains("nome='CLIENTE'")
                .contains("descricao='Cliente do sistema'");
    }
}