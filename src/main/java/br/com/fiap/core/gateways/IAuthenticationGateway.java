package br.com.fiap.core.gateways;

import br.com.fiap.core.domain.Usuario;
import java.util.Optional;

public interface IAuthenticationGateway {

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorLogin(String login);

    void atualizarSenha(Long usuarioId, String novaSenhaHash);
}
