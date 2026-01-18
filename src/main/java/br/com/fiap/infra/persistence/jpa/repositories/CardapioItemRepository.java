package br.com.fiap.infra.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;

public interface CardapioItemRepository extends JpaRepository<CardapioItemEntity, Long> {
    List<CardapioItemEntity> findByRestauranteId(Long restauranteId);
}
