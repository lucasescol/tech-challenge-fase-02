package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import br.com.fiap.core.exceptions.SenhaInvalidaException;
import br.com.fiap.core.exceptions.SenhasNaoConferemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Usuario - Testes de Domínio")
class UsuarioTest {

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        Usuario usuario = Usuario.create(
                1L,
                "João Silva",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipo);

        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isEqualTo(1L);
        assertThat(usuario.getNome()).isEqualTo("João Silva");
        assertThat(usuario.getEmail().getValor()).isEqualTo("joao@email.com");
        assertThat(usuario.getLogin()).isEqualTo("joao");
        assertThat(usuario.getSenha()).isEqualTo("senha123");
    }

    @Test
    @DisplayName("Deve refatorar o nome")
    void deveRefatorarNome() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        Usuario usuario = Usuario.create(
                1L,
                "  João Silva  ",
                "joao@email.com",
                "joao",
                "senha123",
                endereco,
                tipo);

        assertThat(usuario.getNome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        assertThatThrownBy(() -> Usuario.create(
                1L, null, "joao@email.com", "joao", "senha123", endereco, tipo))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do usuário não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoCurto() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        assertThatThrownBy(() -> Usuario.create(
                1L, "AB", "joao@email.com", "joao", "senha123", endereco, tipo))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do usuário deve ter no mínimo 3 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem mais de 100 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoLongo() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        String nomeLongo = "A".repeat(101);

        assertThatThrownBy(() -> Usuario.create(
                1L, nomeLongo, "joao@email.com", "joao", "senha123", endereco, tipo))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do usuário deve ter no máximo 100 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando login é nulo")
    void deveLancarExcecaoQuandoLoginNulo() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        assertThatThrownBy(() -> Usuario.create(
                1L, "João", "joao@email.com", null, "senha123", endereco, tipo))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Login do usuário não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando login tem menos de 3 caracteres")
    void deveLancarExcecaoQuandoLoginMuitoCurto() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        assertThatThrownBy(() -> Usuario.create(
                1L, "João", "joao@email.com", "ab", "senha123", endereco, tipo))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Login do usuário deve ter no mínimo 3 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando login tem mais de 50 caracteres")
    void deveLancarExcecaoQuandoLoginMuitoLongo() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        String loginLongo = "a".repeat(51);

        assertThatThrownBy(() -> Usuario.create(
                1L, "João", "joao@email.com", loginLongo, "senha123", endereco, tipo))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Login do usuário deve ter no máximo 50 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário é nulo")
    void deveLancarExcecaoQuandoTipoUsuarioNulo() {
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        assertThatThrownBy(() -> Usuario.create(
                1L, "João", "joao@email.com", "joao", "senha123", endereco, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Tipo de usuário não pode ser nulo");
    }

    @Test
    @DisplayName("Deve validar senha com sucesso")
    void deveValidarSenhaComSucesso() {
        assertThat(Usuario.class).isNotNull();
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        Usuario usuario = Usuario.create(
                1L, "João", "joao@email.com", "joao", "senha123", endereco, tipo);

        assertThat(usuario.getSenha()).isEqualTo("senha123");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é nula")
    void deveLancarExcecaoQuandoSenhaNula() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        assertThatThrownBy(() -> Usuario.create(
                1L, "João", "joao@email.com", "joao", null, endereco, tipo))
                .isInstanceOf(SenhaInvalidaException.class)
                .hasMessageContaining("Senha não pode ser vazia");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha tem menos de 6 caracteres")
    void deveLancarExcecaoQuandoSenhaMuitoCurta() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        assertThatThrownBy(() -> Usuario.create(
                1L, "João", "joao@email.com", "joao", "12345", endereco, tipo))
                .isInstanceOf(SenhaInvalidaException.class)
                .hasMessageContaining("Senha deve ter no mínimo 6 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha tem mais de 100 caracteres")
    void deveLancarExcecaoQuandoSenhaMuitoLonga() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");
        String senhaLonga = "a".repeat(101);

        assertThatThrownBy(() -> Usuario.create(
                1L, "João", "joao@email.com", "joao", senhaLonga, endereco, tipo))
                .isInstanceOf(SenhaInvalidaException.class)
                .hasMessageContaining("Senha deve ter no máximo 100 caracteres");
    }

    @Test
    @DisplayName("Deve validar nova senha com sucesso")
    void deveValidarNovaSenhaComSucesso() {
        assertThat(Usuario.class).isNotNull();
        Usuario.validarNovaSenha("novaSenha123", "novaSenha123");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senhas não conferem")
    void deveLancarExcecaoQuandoSenhasNaoConferem() {
        assertThatThrownBy(() -> Usuario.validarNovaSenha("senha123", "senha456"))
                .isInstanceOf(SenhasNaoConferemException.class);
    }

    @Test
    @DisplayName("Deve verificar igualdade entre usuários")
    void deveVerificarIgualdade() {
        TipoUsuario tipo = TipoUsuario.create(1L, "CLIENTE", "Cliente");
        Endereco endereco = new Endereco("Rua A", "100", "", "Centro", "São Paulo", "SP", "01000-000");

        Usuario usuario1 = Usuario.create(
                1L, "João", "joao@email.com", "joao", "senha123", endereco, tipo);

        Usuario usuario2 = Usuario.create(
                1L, "João", "joao@email.com", "joao", "senha123", endereco, tipo);

        assertThat(usuario1).isEqualTo(usuario2);
        assertThat(usuario1.hashCode()).isEqualTo(usuario2.hashCode());
    }
}