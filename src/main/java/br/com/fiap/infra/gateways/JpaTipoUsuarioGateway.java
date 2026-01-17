package br.com.fiap.infra.gateways;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.gateways.ITipoUsuarioGateway;
import br.com.fiap.infra.mappers.TipoUsuarioMapper;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.repositories.TipoUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaTipoUsuarioGateway implements ITipoUsuarioGateway {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public JpaTipoUsuarioGateway(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public TipoUsuario incluir(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = TipoUsuarioMapper.toEntity(tipoUsuario);
        TipoUsuarioEntity savedEntity = tipoUsuarioRepository.save(entity);
        return TipoUsuarioMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TipoUsuario> obterPorId(Long id) {
        return tipoUsuarioRepository.findById(id)
                .map(TipoUsuarioMapper::toDomain);
    }

    @Override
    public Optional<TipoUsuario> obterPorNome(String nome) {
        return tipoUsuarioRepository.findByNome(nome)
                .map(TipoUsuarioMapper::toDomain);
    }

    @Override
    public List<TipoUsuario> listarTodos() {
        return tipoUsuarioRepository.findAll()
                .stream()
                .map(TipoUsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public TipoUsuario atualizar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = TipoUsuarioMapper.toEntity(tipoUsuario);
        TipoUsuarioEntity updatedEntity = tipoUsuarioRepository.save(entity);
        return TipoUsuarioMapper.toDomain(updatedEntity);
    }

    @Override
    public void deletar(Long id) {
        tipoUsuarioRepository.deleteById(id);
    }
}
