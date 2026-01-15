package br.com.fiap.infra.gateways;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.exceptions.EmailJaCadastradoException;
import br.com.fiap.core.exceptions.LoginJaCadastradoException;
import br.com.fiap.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.core.exceptions.DomainException;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.infra.mappers.UsuarioMapper;
import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;
import br.com.fiap.infra.persistence.jpa.repositories.UsuarioRepository;

@Component
public class JpaUsuarioGateway implements IUsuarioGateway {

    private final UsuarioRepository usuarioRepository;

    public JpaUsuarioGateway(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario incluir(Usuario novoUsuario) {
        if (existeLoginCadastrado(novoUsuario.getLogin(), null)) {
            throw new LoginJaCadastradoException(novoUsuario.getLogin());
        }
        if (existeEmailCadastrado(novoUsuario.getEmail().getValor(), null)) {
            throw new EmailJaCadastradoException(novoUsuario.getEmail().getValor());
        }
        
        UsuarioEntity novoUsuarioEntity = UsuarioMapper.toEntity(novoUsuario);
        UsuarioEntity savedEntity = usuarioRepository.save(novoUsuarioEntity);
        return UsuarioMapper.toDomain(savedEntity);
    }

    @Override
    public Usuario atualizar(Long id, Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException(id);
        }
        
        if (existeLoginCadastrado(usuario.getLogin(), id)) {
            throw new LoginJaCadastradoException(usuario.getLogin());
        }
        
        if (existeEmailCadastrado(usuario.getEmail().getValor(), id)) {
            throw new EmailJaCadastradoException(usuario.getEmail().getValor());
        }
        
        UsuarioEntity usuarioEntity = UsuarioMapper.toEntity(usuario);
        usuarioEntity.setId(id);
        UsuarioEntity updatedEntity = usuarioRepository.save(usuarioEntity);
        return UsuarioMapper.toDomain(updatedEntity);
    }

    @Override
    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException(id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public void trocarSenha(Long id, String senhaAtual, String novaSenha) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        
        if (!usuario.getSenha().equals(senhaAtual)) {
            throw new DomainException("Senha atual incorreta");
        }
        
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new DomainException("Nova senha não pode ser vazia");
        }
        
        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> buscarPorNome(String nome) {
        List<UsuarioEntity> entities = usuarioRepository.findByNomeContainingIgnoreCase(nome);
        return entities.stream()
            .map(UsuarioMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
            .map(UsuarioMapper::toDomain);
    }

    @Override
    public boolean existeEmailCadastrado(String email, Long idExcluir) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return false;
        }
        return idExcluir == null || !usuario.get().getId().equals(idExcluir);
    }

    @Override
    public boolean existeLoginCadastrado(String login, Long idExcluir) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByLogin(login);
        if (usuario.isEmpty()) {
            return false;
        }
        return idExcluir == null || !usuario.get().getId().equals(idExcluir);
    }
}
