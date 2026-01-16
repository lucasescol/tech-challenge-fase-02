package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.infra.dto.EnderecoDTO;
import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;

public class EnderecoMapper {

    public static Endereco toDomain(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Endereco(
                dto.logradouro(),
                dto.numero(),
                dto.complemento(),
                dto.bairro(),
                dto.cidade(),
                dto.estado(),
                dto.cep());
    }

    public static Endereco toDomain(EnderecoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Endereco(
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep());
    }

    public static EnderecoDTO toDto(Endereco domain) {
        if (domain == null) {
            return null;
        }
        return new EnderecoDTO(
                domain.getLogradouro(),
                domain.getNumero(),
                domain.getComplemento(),
                domain.getBairro(),
                domain.getCidade(),
                domain.getEstado(),
                domain.getCep());
    }

    public static EnderecoEntity toPersistence(Endereco domain) {
        if (domain == null) {
            return null;
        }
        EnderecoEntity entity = new EnderecoEntity();
        entity.setLogradouro(domain.getLogradouro());
        entity.setNumero(domain.getNumero());
        entity.setComplemento(domain.getComplemento());
        entity.setBairro(domain.getBairro());
        entity.setCidade(domain.getCidade());
        entity.setEstado(domain.getEstado());
        entity.setCep(domain.getCep());
        return entity;
    }
}
