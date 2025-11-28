package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.RestauranteEntity;

public class RestauranteMapper {
    
    public static Restaurante toDomain(RestauranteEntity entity) {
        EnderecoEntity endEntity = entity.getEndereco();
        
        return Restaurante.create(
            entity.getId(), 
            entity.getNome(),
            endEntity.getLogradouro(),
            endEntity.getNumero(),
            endEntity.getComplemento(),
            endEntity.getBairro(),
            endEntity.getCidade(),
            endEntity.getEstado(),
            endEntity.getCep(),
            entity.getTipoCozinha(),
            entity.getHorarioFuncionamento(),
            entity.getDonoRestaurante()
        );
    }

    public static RestauranteEntity toEntity(Restaurante domain) {
        Endereco endereco = domain.getEndereco();
        
        EnderecoEntity enderecoEntity = new EnderecoEntity(
            null,
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCepLimpo()
        );
        
        return new RestauranteEntity(
            domain.getId(), 
            domain.getNome(),
            enderecoEntity,
            domain.getTipoCozinha().getValor(),
            domain.getHorarioFuncionamento().getValor(),
            domain.getDonoRestaurante(), 
            null, 
            null, 
            null
        );
    }
}
