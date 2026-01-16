package br.com.fiap.infra.gateways;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.domain.Usuario.TipoUsuario;
import br.com.fiap.core.gateways.IUsuarioGateway;
import br.com.fiap.infra.mappers.EnderecoMapper;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;
import br.com.fiap.infra.persistence.jpa.repositories.TipoUsuarioRepository;
import br.com.fiap.infra.persistence.jpa.repositories.UsuarioRepository;

@Component
public class JpaUsuarioGateway implements IUsuarioGateway {

    private final UsuarioRepository repository;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    public JpaUsuarioGateway(UsuarioRepository repository, TipoUsuarioRepository tipoUsuarioRepository) {
        this.repository = repository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public Usuario incluir(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail().getValor());
        entity.setLogin(usuario.getLogin());
        entity.setSenha(usuario.getSenha());

        if (usuario.getEndereco() != null) {
            entity.setEndereco(EnderecoMapper.toPersistence(usuario.getEndereco()));
        }
        String nomeTipo = usuario.getTipoUsuario().name();

        TipoUsuarioEntity tipoEntity = tipoUsuarioRepository.findByNome(nomeTipo)
                .orElseGet(() -> tipoUsuarioRepository.findByTipo(TipoUsuarioEntity.TipoConta.valueOf(nomeTipo))
                        .orElseThrow(
                                () -> new RuntimeException("Tipo de usuário não cadastrado no banco: " + nomeTipo)));

        entity.setTipoUsuario(tipoEntity);

        UsuarioEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return obterPorId(id);
    }

    @Override
    public Optional<Usuario> obterPorEmail(String email) {
        return repository.findByEmail(email).map(this::mapToDomain);
    }

    @Override
    public Optional<Usuario> obterPorLogin(String login) {
        return repository.findByLogin(login).map(this::mapToDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Usuario> listarPorTipo(Long tipoUsuarioId) {
        return repository.findByTipoUsuarioId(tipoUsuarioId).stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Usuario> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        UsuarioEntity entity = repository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail().getValor());
        entity.setLogin(usuario.getLogin());

        if (usuario.getEndereco() != null) {
            entity.setEndereco(EnderecoMapper.toPersistence(usuario.getEndereco()));
        }

        UsuarioEntity updated = repository.save(entity);
        return mapToDomain(updated);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void trocarSenha(Long id, String novaSenha) {
        UsuarioEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        entity.setSenha(novaSenha);
        repository.save(entity);
    }

    private Usuario mapToDomain(UsuarioEntity entity) {
        Endereco endereco = null;
        if (entity.getEndereco() != null) {
            endereco = EnderecoMapper.toDomain(entity.getEndereco());
        }

        return Usuario.criar(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getSenha(),
                endereco,
                TipoUsuario.valueOf(entity.getTipoUsuario().getTipo().name()));
    }
}
