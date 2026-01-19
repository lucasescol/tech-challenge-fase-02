package br.com.fiap.infra.persistence.jpa.repositories;

import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.services.JwtTokenService;
import br.com.fiap.infra.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TipoUsuarioRepository - Testes de Integração")
class TipoUsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TipoUsuarioRepository repository;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Deve buscar tipo de usuário por nome")
    void deveBuscarPorNome() {
        String nome = "CLIENTE_TESTE_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        TipoUsuarioEntity tipo = new TipoUsuarioEntity(nome, "Cliente Teste");
        entityManager.persist(tipo);
        entityManager.flush();

        Optional<TipoUsuarioEntity> encontrado = repository.findByNome(nome);

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getDescricao()).isEqualTo("Cliente Teste");
    }

    @Test
    @DisplayName("Deve verificar se existe por nome")
    void deveVerificarSeExistePorNome() {
        String nome = "ADMIN_TESTE_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        TipoUsuarioEntity tipo = new TipoUsuarioEntity(nome, "Descrição Admin");
        entityManager.persist(tipo);
        entityManager.flush();

        boolean existe = repository.existsByNome(nome);
        boolean naoExiste = repository.existsByNome("OUTRO");

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }
}