package br.com.fiap.core.usecases;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.gateways.ICardapioItemGateway;

public class CadastrarCardapioItemUseCase {

    private final ICardapioItemGateway cardapioItemGateway;

    public CadastrarCardapioItemUseCase(ICardapioItemGateway cardapioItemGateway) {
        this.cardapioItemGateway = cardapioItemGateway;
    }

    public CardapioItem executar(CardapioItem item) {
        return cardapioItemGateway.incluir(item);
    }
}
