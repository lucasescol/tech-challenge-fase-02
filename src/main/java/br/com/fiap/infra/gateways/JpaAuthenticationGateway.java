package br.com.fiap.infra.gateways;

import java.util.Optional;
import org.springframework.stereotype.Component;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.gateways.IAuthenticationGateway;
import br.com.fiap.infra.mappers.UsuarioMapper;
import br.com.fiap.infra.persistence.jpa.repositories.UsuarioRepository;

@Component
public class JpaAuthenticationGateway implements IAuthenticationGateway {

    private final UsuarioRepository usuarioRepository;

    public JpaAuthenticationGateway(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(entity -> UsuarioMapper.toDomain(entity));
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .map(entity -> UsuarioMapper.toDomain(entity));
    }

    @Override
    public void atualizarSenha(Long usuarioId, String novaSenhaHash) {
        usuarioRepository.findById(usuarioId).ifPresent(entity -> {
            entity.setSenha(novaSenhaHash);
            usuarioRepository.save(entity);
        });
    }
}
