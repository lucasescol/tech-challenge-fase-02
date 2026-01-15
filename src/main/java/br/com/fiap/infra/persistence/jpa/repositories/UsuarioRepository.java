package br.com.fiap.infra.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    
    List<UsuarioEntity> findByNomeContainingIgnoreCase(String nome);
    
    Optional<UsuarioEntity> findByLogin(String login);
    
    Optional<UsuarioEntity> findByEmail(String email);
    
    boolean existsByLogin(String login);
    
    boolean existsByEmail(String email);
}
