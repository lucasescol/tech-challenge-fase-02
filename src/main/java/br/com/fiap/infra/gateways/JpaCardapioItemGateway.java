package br.com.fiap.infra.gateways;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.fiap.core.domain.CardapioItem;
import br.com.fiap.core.gateways.ICardapioItemGateway;
import br.com.fiap.infra.persistence.jpa.entities.CardapioItemEntity;
import br.com.fiap.infra.persistence.jpa.entities.RestauranteEntity;
import br.com.fiap.infra.persistence.jpa.repositories.CardapioItemRepository;
import br.com.fiap.infra.persistence.jpa.repositories.RestauranteRepository;

@Component
public class JpaCardapioItemGateway implements ICardapioItemGateway {

    private final CardapioItemRepository repository;
    private final RestauranteRepository restauranteRepository;

    public JpaCardapioItemGateway(CardapioItemRepository repository, RestauranteRepository restauranteRepository) {
        this.repository = repository;
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public CardapioItem incluir(CardapioItem item) {
        RestauranteEntity restaurante = restauranteRepository.findById(item.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        CardapioItemEntity entity = new CardapioItemEntity();
        entity.setRestaurante(restaurante);
        entity.setNome(item.getNome());
        entity.setDescricao(item.getDescricao());
        entity.setPreco(item.getPreco());
        entity.setApenasPresencial(item.isApenasPresencial());
        entity.setCaminhoFoto(item.getCaminhoFoto());

        CardapioItemEntity saved = repository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<CardapioItem> obterPorId(Long id) {
        return repository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<CardapioItem> listarPorRestaurante(Long restauranteId) {
        return repository.findByRestauranteId(restauranteId).stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<CardapioItem> listarTodos() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public CardapioItem atualizar(CardapioItem item) {
        CardapioItemEntity entity = repository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("CardapioItem não encontrado"));

        entity.setNome(item.getNome());
        entity.setDescricao(item.getDescricao());
        entity.setPreco(item.getPreco());
        entity.setApenasPresencial(item.isApenasPresencial());
        entity.setCaminhoFoto(item.getCaminhoFoto());

        CardapioItemEntity updated = repository.save(entity);
        return mapToDomain(updated);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    private CardapioItem mapToDomain(CardapioItemEntity entity) {
        return CardapioItem.criar(
                entity.getId(),
                entity.getRestaurante().getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.isApenasPresencial(),
                entity.getCaminhoFoto());
    }
}
