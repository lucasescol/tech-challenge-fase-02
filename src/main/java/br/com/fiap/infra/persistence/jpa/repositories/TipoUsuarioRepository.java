package br.com.fiap.infra.persistence.jpa.repositories;

import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, Long> {

    Optional<TipoUsuarioEntity> findByNome(String nome);

    boolean existsByNome(String nome);
}
