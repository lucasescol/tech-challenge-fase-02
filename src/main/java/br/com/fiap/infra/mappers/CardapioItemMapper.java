package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.infra.dto.NovoItemCardapioDTO;
import br.com.fiap.infra.dto.CardapioItemResponseDTO;

public class CardapioItemMapper {

    public static CardapioItem toDomain(NovoItemCardapioDTO dto) {
        return CardapioItem.criar(
                null,
                dto.getRestauranteId(),
                dto.getNome(),
                dto.getDescricao(),
                dto.getPreco(),
                dto.isApenasPresencial(),
                dto.getCaminhoFoto());
    }

    public static CardapioItemResponseDTO toResponse(CardapioItem domain) {
        return new CardapioItemResponseDTO(
                domain.getId(),
                domain.getRestauranteId(),
                domain.getNome(),
                domain.getDescricao(),
                domain.getPreco(),
                domain.isApenasPresencial(),
                domain.getCaminhoFoto());
    }
}
