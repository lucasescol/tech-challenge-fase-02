package br.com.fiap.infra.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;

@Repository
public interface CardapioItemRepository extends JpaRepository<CardapioItemEntity, Long> {
    List<CardapioItemEntity> findByRestauranteId(Long restauranteId);
}
