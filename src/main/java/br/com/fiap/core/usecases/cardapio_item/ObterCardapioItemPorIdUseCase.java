package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.gateways.ICardapioItemGateway;

import java.util.Optional;

public class ObterCardapioItemPorIdUseCase {

    private final ICardapioItemGateway cardapioItemGateway;

    private ObterCardapioItemPorIdUseCase(ICardapioItemGateway cardapioItemGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
    }

    public static ObterCardapioItemPorIdUseCase create(ICardapioItemGateway cardapioItemGateway) {
        return new ObterCardapioItemPorIdUseCase(cardapioItemGateway);
    }

    public Optional<OutputModel> execute(Long id) {
        return cardapioItemGateway.obterPorId(id)
                .map(item -> new OutputModel(
                    item.getId(),
                    item.getRestauranteId(),
                    item.getNome(),
                    item.getDescricao(),
                    item.getPreco(),
                    item.isApenasPresencial(),
                    item.getCaminhoFoto()
                ));
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
