package br.com.fiap.infra.persistence.jpa.repositories;

import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;
import br.com.fiap.infra.services.JwtTokenService;
import br.com.fiap.infra.security.JwtAuthenticationFilter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UsuarioRepository - Testes de Integração")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository repository;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Deve buscar usuário por login")
    void deveBuscarPorLogin() {
        criarUsuario("joao", "joao@email.com", "João Silva");

        Optional<UsuarioEntity> encontrado = repository.findByLogin("joao");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("Deve buscar usuário por email")
    void deveBuscarPorEmail() {
        criarUsuario("maria", "maria@email.com", "Maria Silva");

        Optional<UsuarioEntity> encontrado = repository.findByEmail("maria@email.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getLogin()).isEqualTo("maria");
    }

    @Test
    @DisplayName("Deve buscar usuários por nome contendo string (case insensitive)")
    void deveBuscarPorNomeContendo() {
        criarUsuario("user1", "u1@email.com", "João Silva");
        criarUsuario("user2", "u2@email.com", "Maria Silva");
        criarUsuario("user3", "u3@email.com", "Pedro Santos");

        List<UsuarioEntity> encontrados = repository.findByNomeContainingIgnoreCase("silva");

        assertThat(encontrados).hasSize(2);
        assertThat(encontrados).extracting(UsuarioEntity::getNome)
                .containsExactlyInAnyOrder("João Silva", "Maria Silva");
    }

    @Test
    @DisplayName("Deve verificar existência por login")
    void deveVerificarExistenciaPorLogin() {
        String login = "admin_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        criarUsuario(login, "admin_teste@email.com", "Admin");

        assertThat(repository.existsByLogin(login)).isTrue();
        assertThat(repository.existsByLogin("outro")).isFalse();
    }

    @Test
    @DisplayName("Deve verificar existência por email")
    void deveVerificarExistenciaPorEmail() {
        criarUsuario("teste", "teste@email.com", "Teste");

        assertThat(repository.existsByEmail("teste@email.com")).isTrue();
        assertThat(repository.existsByEmail("outro@email.com")).isFalse();
    }

    private void criarUsuario(String login, String email, String nome) {
        String nomeTipo = "TIPO_" + login + "_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        TipoUsuarioEntity tipo = new TipoUsuarioEntity(nomeTipo, "Cliente");
        entityManager.persist(tipo);

        EnderecoEntity endereco = new EnderecoEntity(null, "Rua", "1", "", "Bairro", "Cidade", "SP", "00000000");

        UsuarioEntity usuario = new UsuarioEntity(null, nome, email, login, "senha", endereco, tipo, null, null);
        entityManager.persist(usuario);
        entityManager.flush();
    }
}
