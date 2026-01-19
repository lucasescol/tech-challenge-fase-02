package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CardapioItemMapper - Testes Unitários")
class CardapioItemMapperTest {

    @Test
    @DisplayName("Deve converter Entity para Domain corretamente")
    void deveConverterEntityParaDomain() {
        CardapioItemEntity entity = new CardapioItemEntity(
                1L,
                5L,
                "Pizza Margherita",
                "Deliciosa pizza com molho de tomate, mussarela e manjericão",
                45.90,
                false,
                "/images/pizza-margherita.jpg");

        CardapioItem domain = CardapioItemMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getRestauranteId()).isEqualTo(5L);
        assertThat(domain.getNome()).isEqualTo("Pizza Margherita");
        assertThat(domain.getDescricao()).isEqualTo("Deliciosa pizza com molho de tomate, mussarela e manjericão");
        assertThat(domain.getPreco()).isEqualTo(45.90);
        assertThat(domain.isApenasPresencial()).isFalse();
        assertThat(domain.getCaminhoFoto()).isEqualTo("/images/pizza-margherita.jpg");
    }

    @Test
    @DisplayName("Deve converter Domain para Entity corretamente")
    void deveConverterDomainParaEntity() {
        CardapioItem domain = CardapioItem.criar(
                2L,
                10L,
                "Lasanha Bolonhesa",
                "Lasanha tradicional com molho bolonhesa e queijo gratinado",
                38.50,
                true,
                "/images/lasanha.jpg");

        CardapioItemEntity entity = CardapioItemMapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getRestauranteId()).isEqualTo(10L);
        assertThat(entity.getNome()).isEqualTo("Lasanha Bolonhesa");
        assertThat(entity.getDescricao()).isEqualTo("Lasanha tradicional com molho bolonhesa e queijo gratinado");
        assertThat(entity.getPreco()).isEqualTo(38.50);
        assertThat(entity.isApenasPresencial()).isTrue();
        assertThat(entity.getCaminhoFoto()).isEqualTo("/images/lasanha.jpg");
    }

    @Test
    @DisplayName("Deve converter Entity sem foto para Domain")
    void deveConverterEntitySemFotoParaDomain() {
        CardapioItemEntity entity = new CardapioItemEntity(
                3L,
                7L,
                "Suco Natural",
                "Suco natural de laranja fresquinho",
                8.50,
                false,
                "");

        CardapioItem domain = CardapioItemMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getCaminhoFoto()).isEmpty();
    }

    @Test
    @DisplayName("Deve converter item apenas presencial corretamente")
    void deveConverterItemApenasPresencial() {
        CardapioItemEntity entity = new CardapioItemEntity(
                4L,
                8L,
                "Rodízio de Pizza",
                "Rodízio completo com diversas opções",
                59.90,
                true,
                "/images/rodizio.jpg");

        CardapioItem domain = CardapioItemMapper.toDomain(entity);

        assertThat(domain.isApenasPresencial()).isTrue();
    }

    @Test
    @DisplayName("Deve manter integridade na conversão bidirecional")
    void deveManterIntegridadeNaConversaoBidirecional() {
        CardapioItemEntity entityOriginal = new CardapioItemEntity(
                5L,
                15L,
                "Tiramisu",
                "Sobremesa italiana com café e mascarpone",
                22.90,
                false,
                "/images/tiramisu.jpg");

        CardapioItem domain = CardapioItemMapper.toDomain(entityOriginal);
        CardapioItemEntity entityConvertida = CardapioItemMapper.toEntity(domain);

        assertThat(entityConvertida.getId()).isEqualTo(entityOriginal.getId());
        assertThat(entityConvertida.getRestauranteId()).isEqualTo(entityOriginal.getRestauranteId());
        assertThat(entityConvertida.getNome()).isEqualTo(entityOriginal.getNome());
        assertThat(entityConvertida.getDescricao()).isEqualTo(entityOriginal.getDescricao());
        assertThat(entityConvertida.getPreco()).isEqualTo(entityOriginal.getPreco());
        assertThat(entityConvertida.isApenasPresencial()).isEqualTo(entityOriginal.isApenasPresencial());
        assertThat(entityConvertida.getCaminhoFoto()).isEqualTo(entityOriginal.getCaminhoFoto());
    }
}