package br.com.fiap.core.gateways;

import java.util.List;
import java.util.Optional;

import br.com.fiap.core.domain.Usuario;

public interface IUsuarioGateway {
    Usuario incluir(Usuario novoUsuario);
    Usuario atualizar(Long id, Usuario usuario);
    void excluir(Long id);
    void trocarSenha(Long id, String novaSenha);
    List<Usuario> buscarPorNome(String nome);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorLogin(String login);
    boolean existeEmailCadastrado(String email, Long idExcluir);
    boolean existeLoginCadastrado(String login, Long idExcluir);
}
