package br.com.fiap.infra.gateways;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;
import br.com.fiap.infra.persistence.jpa.repositories.CardapioItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JpaCardapioItemGateway - Testes Unitários")
class JpaCardapioItemGatewayTest {

    @Mock
    private CardapioItemRepository cardapioItemRepository;

    @InjectMocks
    private JpaCardapioItemGateway gateway;

    @Test
    @DisplayName("Deve incluir item do cardápio com sucesso")
    void deveIncluirItem() {
        CardapioItem item = CardapioItem.criar(null, 1L, "Pizza", "Pizza deliciosa com queijo", 50.0, false,
                "/img.jpg");
        CardapioItemEntity entitySalva = new CardapioItemEntity(1L, 1L, "Pizza", "Pizza deliciosa com queijo", 50.0,
                false, "/img.jpg");

        when(cardapioItemRepository.save(any(CardapioItemEntity.class))).thenReturn(entitySalva);

        CardapioItem resultado = gateway.incluir(item);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Pizza");
        verify(cardapioItemRepository).save(any(CardapioItemEntity.class));
    }

    @Test
    @DisplayName("Deve obter item por ID com sucesso")
    void deveObterPorId() {
        CardapioItemEntity entity = new CardapioItemEntity(1L, 1L, "Pizza", "Pizza deliciosa com queijo", 50.0, false,
                "/img.jpg");
        when(cardapioItemRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<CardapioItem> resultado = gateway.obterPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Pizza");
    }

    @Test
    @DisplayName("Deve listar itens por restaurante")
    void deveListarPorRestaurante() {
        CardapioItemEntity entity1 = new CardapioItemEntity(1L, 1L, "Pizza", "Descrição detalhada do item 1", 50.0,
                false, "/img1.jpg");
        CardapioItemEntity entity2 = new CardapioItemEntity(2L, 1L, "Refri", "Refrigerante bem gelado", 10.0, false,
                "/img2.jpg");

        when(cardapioItemRepository.findByRestauranteId(1L)).thenReturn(List.of(entity1, entity2));

        List<CardapioItem> resultado = gateway.listarPorRestaurante(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Pizza");
        assertThat(resultado.get(1).getNome()).isEqualTo("Refri");
    }

    @Test
    @DisplayName("Deve listar todos os itens")
    void deveListarTodos() {
        CardapioItemEntity entity = new CardapioItemEntity(1L, 1L, "Pizza", "Descrição detalhada do item", 50.0, false,
                "/img.jpg");
        when(cardapioItemRepository.findAll()).thenReturn(List.of(entity));

        List<CardapioItem> resultado = gateway.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("Deve atualizar item com sucesso")
    void deveAtualizarItem() {
        CardapioItem item = CardapioItem.criar(1L, 1L, "Pizza Atualizada", "Descrição detalhada atualizada", 55.0,
                false, "/img.jpg");
        CardapioItemEntity entitySalva = new CardapioItemEntity(1L, 1L, "Pizza Atualizada",
                "Descrição detalhada atualizada", 55.0, false,
                "/img.jpg");

        when(cardapioItemRepository.save(any(CardapioItemEntity.class))).thenReturn(entitySalva);

        CardapioItem resultado = gateway.atualizar(item);

        assertThat(resultado.getNome()).isEqualTo("Pizza Atualizada");
        assertThat(resultado.getPreco()).isEqualTo(55.0);
    }

    @Test
    @DisplayName("Deve deletar item por ID")
    void deveDeletarItem() {
        doNothing().when(cardapioItemRepository).deleteById(1L);

        gateway.deletar(1L);

        verify(cardapioItemRepository).deleteById(1L);
    }
}