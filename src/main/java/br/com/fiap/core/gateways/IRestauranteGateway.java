package br.com.fiap.core.gateways;

import java.util.List;
import java.util.Optional;

import br.com.fiap.core.domain.Restaurante;

public interface IRestauranteGateway {

    Restaurante incluir(Restaurante novoRestaurante);

    Optional<Restaurante> obterPorId(Long id);

    List<Restaurante> listarTodos();

    Restaurante atualizar(Restaurante restaurante);

    void deletar(Long id);
}
