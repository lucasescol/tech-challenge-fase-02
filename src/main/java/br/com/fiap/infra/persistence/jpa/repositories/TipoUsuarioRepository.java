package br.com.fiap.infra.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, Long> {
    Optional<TipoUsuarioEntity> findByNome(String nome);

    Optional<TipoUsuarioEntity> findByTipo(TipoUsuarioEntity.TipoConta tipo);
}
