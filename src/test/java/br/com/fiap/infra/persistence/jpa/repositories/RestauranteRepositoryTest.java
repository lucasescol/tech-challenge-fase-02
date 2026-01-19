package br.com.fiap.infra.persistence.jpa.repositories;

import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.RestauranteEntity;
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
@DisplayName("RestauranteRepository - Testes de Integração")
class RestauranteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestauranteRepository repository;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Deve salvar e buscar restaurante por ID")
    void deveSalvarEBuscarRestaurante() {
        EnderecoEntity endereco = new EnderecoEntity(null, "Rua", "1", "", "Bairro", "Cidade", "SP", "00000000");
        RestauranteEntity restaurante = new RestauranteEntity(null, "Restaurante Teste", endereco, "ITALIANA",
                "08:00-22:00", 1L, null, null, null);

        RestauranteEntity salvo = entityManager.persist(restaurante);
        entityManager.flush();

        Optional<RestauranteEntity> encontrado = repository.findById(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Restaurante Teste");
    }
}