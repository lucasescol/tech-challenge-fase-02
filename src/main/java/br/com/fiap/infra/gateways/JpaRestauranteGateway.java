package br.com.fiap.infra.gateways;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.gateways.IRestauranteGateway;
import br.com.fiap.infra.mappers.RestauranteMapper;
import br.com.fiap.infra.persistence.jpa.entities.RestauranteEntity;
import br.com.fiap.infra.persistence.jpa.repositories.RestauranteRepository;

@Component
public class JpaRestauranteGateway implements IRestauranteGateway {

    private final RestauranteRepository restauranteRepository;

    public JpaRestauranteGateway(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public Restaurante incluir(Restaurante novoRestaurante) {
        RestauranteEntity novoRestauranteEntity = RestauranteMapper.toEntity(novoRestaurante);
        return RestauranteMapper.toDomain(this.restauranteRepository.save(novoRestauranteEntity));
    }

    @Override
    public Optional<Restaurante> obterPorId(Long id) {
        return restauranteRepository.findById(id)
                .map(RestauranteMapper::toDomain);
    }

    @Override
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll()
                .stream()
                .map(RestauranteMapper::toDomain)
                .toList();
    }

    @Override
    public Restaurante atualizar(Restaurante restaurante) {
        RestauranteEntity entity = restauranteRepository.findById(restaurante.getId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        RestauranteEntity atualizada = RestauranteMapper.toEntity(restaurante);
        atualizada.setId(entity.getId());

        return RestauranteMapper.toDomain(restauranteRepository.save(atualizada));
    }

    @Override
    public void deletar(Long id) {
        restauranteRepository.deleteById(id);
    }
}
