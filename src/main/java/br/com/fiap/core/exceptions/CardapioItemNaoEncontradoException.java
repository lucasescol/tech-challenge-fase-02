package br.com.fiap.core.exceptions;

public class CardapioItemNaoEncontradoException extends DomainException {
    public CardapioItemNaoEncontradoException(Long id) {
        super("Item do cardápio não encontrado com ID: " + id);
    }
}
