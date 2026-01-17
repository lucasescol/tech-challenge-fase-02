package br.com.fiap.core.gateways;

import java.util.List;
import java.util.Optional;

import br.com.fiap.core.domain.TipoUsuario;

public interface ITipoUsuarioGateway {
    TipoUsuario incluir(TipoUsuario tipoUsuario);

    Optional<TipoUsuario> obterPorId(Long id);

    Optional<TipoUsuario> obterPorNome(String nome);

    List<TipoUsuario> listarTodos();

    TipoUsuario atualizar(TipoUsuario tipoUsuario);

    void deletar(Long id);
}
