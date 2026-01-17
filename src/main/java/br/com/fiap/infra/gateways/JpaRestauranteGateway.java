package br.com.fiap.infra.gateways;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return this.restauranteRepository.findById(id)
                .map(RestauranteMapper::toDomain);
    }

    @Override
    public List<Restaurante> listarTodos() {
        return this.restauranteRepository.findAll()
                .stream()
                .map(RestauranteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Restaurante atualizar(Restaurante restaurante) {
        RestauranteEntity restauranteEntity = RestauranteMapper.toEntity(restaurante);
        return RestauranteMapper.toDomain(this.restauranteRepository.save(restauranteEntity));
    }

    @Override
    public void deletar(Long id) {
        this.restauranteRepository.deleteById(id);
    }
    
}
