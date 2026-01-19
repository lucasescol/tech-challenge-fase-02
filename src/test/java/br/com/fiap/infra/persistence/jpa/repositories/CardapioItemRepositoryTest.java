package br.com.fiap.infra.persistence.jpa.repositories;

import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CardapioItemRepository - Testes de Integração")
class CardapioItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardapioItemRepository repository;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Deve buscar itens por ID do restaurante")
    void deveBuscarItensPorRestauranteId() {
        CardapioItemEntity item1 = new CardapioItemEntity(null, 1L, "Item 1", "Essa é uma descrição", 10.0, false,
                "/img1.jpg");
        CardapioItemEntity item2 = new CardapioItemEntity(null, 1L, "Item 2", "Essa é uma descrição", 20.0, false,
                "/img2.jpg");
        CardapioItemEntity item3 = new CardapioItemEntity(null, 2L, "Item 3", "Essa é uma descrição", 30.0, false,
                "/img3.jpg");

        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(item3);
        entityManager.flush();

        List<CardapioItemEntity> itensRestaurante1 = repository.findByRestauranteId(1L);

        assertThat(itensRestaurante1).hasSize(2);
        assertThat(itensRestaurante1).extracting(CardapioItemEntity::getNome)
                .containsExactlyInAnyOrder("Item 1", "Item 2");
    }
}