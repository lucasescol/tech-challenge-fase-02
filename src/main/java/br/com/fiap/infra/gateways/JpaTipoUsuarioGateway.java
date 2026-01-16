package br.com.fiap.infra.gateways;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.TipoUsuario.TipoConta;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.repositories.TipoUsuarioRepository;

@Component
public class JpaTipoUsuarioGateway implements ITipoUsuarioGateway {

    private final TipoUsuarioRepository repository;

    public JpaTipoUsuarioGateway(TipoUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoUsuario incluir(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.setNome(tipoUsuario.getNome());
        entity.setTipo(TipoUsuarioEntity.TipoConta.valueOf(tipoUsuario.getTipo().name()));
        entity.setDescricao(tipoUsuario.getDescricao());

        TipoUsuarioEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<TipoUsuario> obterPorId(Long id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<TipoUsuario> obterPorNome(String nome) {
        return repository.findByNome(nome).map(this::mapToDomain);
    }

    @Override
    public List<TipoUsuario> listarTodos() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public TipoUsuario atualizar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = repository.findById(tipoUsuario.getId())
                .orElseThrow(() -> new RuntimeException("TipoUsuario não encontrado"));

        entity.setNome(tipoUsuario.getNome());
        entity.setDescricao(tipoUsuario.getDescricao());

        TipoUsuarioEntity updated = repository.save(entity);
        return mapToDomain(updated);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    private TipoUsuario mapToDomain(TipoUsuarioEntity entity) {
        return TipoUsuario.criar(
                entity.getId(),
                entity.getNome(),
                TipoConta.valueOf(entity.getTipo().name()),
                entity.getDescricao());
    }
}
