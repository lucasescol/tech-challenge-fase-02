package br.com.fiap.core.gateways;

import java.util.List;
import java.util.Optional;

import br.com.fiap.core.domain.Usuario;

public interface IUsuarioGateway {
    Usuario incluir(Usuario usuario);

    Optional<Usuario> obterPorId(Long id);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> obterPorEmail(String email);

    Optional<Usuario> obterPorLogin(String login);

    List<Usuario> listarTodos();

    List<Usuario> listarPorTipo(Long tipoUsuarioId);

    List<Usuario> buscarPorNome(String nome);

    Usuario atualizar(Usuario usuario);

    void deletar(Long id);

    void trocarSenha(Long id, String novaSenha);
}
