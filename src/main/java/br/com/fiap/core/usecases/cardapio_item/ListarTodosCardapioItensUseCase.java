package br.com.fiap.core.usecases.cardapio_item;

import br.com.fiap.core.gateways.ICardapioItemGateway;

import java.util.List;

public class ListarTodosCardapioItensUseCase {

    private final ICardapioItemGateway cardapioItemGateway;

    private ListarTodosCardapioItensUseCase(ICardapioItemGateway cardapioItemGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
    }

    public static ListarTodosCardapioItensUseCase create(ICardapioItemGateway cardapioItemGateway) {
        return new ListarTodosCardapioItensUseCase(cardapioItemGateway);
    }

    public List<OutputModel> execute() {
        return cardapioItemGateway.listarTodos()
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
                .toList();
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
