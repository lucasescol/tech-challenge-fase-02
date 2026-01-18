package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;

public class CardapioItemMapper {
    
    public static CardapioItem toDomain(CardapioItemEntity entity) {
        return CardapioItem.criar(
            entity.getId(),
            entity.getRestauranteId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getPreco(),
            entity.isApenasPresencial(),
            entity.getCaminhoFoto()
        );
    }

    public static CardapioItemEntity toEntity(CardapioItem domain) {
        return new CardapioItemEntity(
            domain.getId(),
            domain.getRestauranteId(),
            domain.getNome(),
            domain.getDescricao(),
            domain.getPreco(),
            domain.isApenasPresencial(),
            domain.getCaminhoFoto()
        );
    }
}
