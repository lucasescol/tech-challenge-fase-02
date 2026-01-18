package br.com.fiap.core.exceptions;

public class RestauranteComCardapioException extends DomainException {
    
    public RestauranteComCardapioException(Long restauranteId) {
        super("Não é possível excluir o restaurante de ID " + restauranteId + " pois ele possui itens de cardápio cadastrados");
    }
}
