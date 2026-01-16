package br.com.fiap.infra.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByLogin(String login);

    List<UsuarioEntity> findByNomeContainingIgnoreCase(String nome);

    List<UsuarioEntity> findByTipoUsuarioId(Long tipoUsuarioId);
}
