package br.com.fiap.infra.gateways;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.infra.mappers.CardapioItemMapper;
import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;
import br.com.fiap.infra.persistence.jpa.repositories.CardapioItemRepository;

@Component
public class JpaCardapioItemGateway implements ICardapioItemGateway {

    private final CardapioItemRepository cardapioItemRepository;

    public JpaCardapioItemGateway(CardapioItemRepository cardapioItemRepository) {
        this.cardapioItemRepository = cardapioItemRepository;
    }

    @Override
    public CardapioItem incluir(CardapioItem item) {
        CardapioItemEntity entity = CardapioItemMapper.toEntity(item);
        return CardapioItemMapper.toDomain(this.cardapioItemRepository.save(entity));
    }

    @Override
    public Optional<CardapioItem> obterPorId(Long id) {
        return this.cardapioItemRepository.findById(id)
                .map(CardapioItemMapper::toDomain);
    }

    @Override
    public List<CardapioItem> listarPorRestaurante(Long restauranteId) {
        return this.cardapioItemRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(CardapioItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardapioItem> listarTodos() {
        return this.cardapioItemRepository.findAll()
                .stream()
                .map(CardapioItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public CardapioItem atualizar(CardapioItem item) {
        CardapioItemEntity entity = CardapioItemMapper.toEntity(item);
        return CardapioItemMapper.toDomain(this.cardapioItemRepository.save(entity));
    }

    @Override
    public void deletar(Long id) {
        this.cardapioItemRepository.deleteById(id);
    }
}
