package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.gateways.ICardapioItemGateway;

import java.util.List;
import java.util.stream.Collectors;

public class ListarCardapioItensPorRestauranteUseCase {

    private final ICardapioItemGateway cardapioItemGateway;

    private ListarCardapioItensPorRestauranteUseCase(ICardapioItemGateway cardapioItemGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
    }

    public static ListarCardapioItensPorRestauranteUseCase create(ICardapioItemGateway cardapioItemGateway) {
        return new ListarCardapioItensPorRestauranteUseCase(cardapioItemGateway);
    }

    public List<OutputModel> execute(Long restauranteId) {
        return cardapioItemGateway.listarPorRestaurante(restauranteId)
                .stream()
                .map(item -> new OutputModel(
                    item.getId(),
                    item.getRestauranteId(),
                    item.getNome(),
                    item.getDescricao(),
                    item.getPreco(),
                    item.isApenasPresencial(),
                    item.getCaminhoFoto()
                ))
                .collect(Collectors.toList());
    }

    public record OutputModel(
        Long id,
        Long restauranteId,
        String nome,
        String descricao,
        Double preco,
        boolean apenasPresencial,
        String caminhoFoto
    ) {}
}
