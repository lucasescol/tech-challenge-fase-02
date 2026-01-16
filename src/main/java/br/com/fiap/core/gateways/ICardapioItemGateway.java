package br.com.fiap.core.gateways;

import java.util.List;
import java.util.Optional;

import br.com.fiap.core.domain.CardapioItem;

public interface ICardapioItemGateway {
    CardapioItem incluir(CardapioItem item);

    Optional<CardapioItem> obterPorId(Long id);

    List<CardapioItem> listarPorRestaurante(Long restauranteId);

    List<CardapioItem> listarTodos();

    CardapioItem atualizar(CardapioItem item);

    void deletar(Long id);
}
