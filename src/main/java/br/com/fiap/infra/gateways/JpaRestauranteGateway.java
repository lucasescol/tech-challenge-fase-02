package br.com.fiap.infra.gateways;

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
    
}
