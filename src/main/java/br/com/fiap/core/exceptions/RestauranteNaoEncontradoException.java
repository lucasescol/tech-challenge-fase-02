package br.com.fiap.core.exceptions;

public class RestauranteNaoEncontradoException extends DomainException {
    public RestauranteNaoEncontradoException(Long id) {
        super("Restaurante não encontrado com ID: " + id);
    }
}
