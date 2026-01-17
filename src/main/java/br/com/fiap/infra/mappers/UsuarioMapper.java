package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.Endereco;
import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.core.domain.Usuario;
import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;

public class UsuarioMapper {
    
    public static Usuario toDomain(UsuarioEntity entity) {
        EnderecoEntity endEntity = entity.getEndereco();
        
        Endereco endereco = new Endereco(
            endEntity.getLogradouro(),
            endEntity.getNumero(),
            endEntity.getComplemento(),
            endEntity.getBairro(),
            endEntity.getCidade(),
            endEntity.getEstado(),
            endEntity.getCep()
        );
        
        TipoUsuario tipoUsuario = TipoUsuarioMapper.toDomain(entity.getTipoUsuario());
        
        return Usuario.create(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getLogin(),
            entity.getSenha(),
            endereco,
            tipoUsuario
        );
    }

    public static UsuarioEntity toEntity(Usuario domain) {
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
        
        TipoUsuarioEntity tipoUsuario = TipoUsuarioMapper.toEntity(domain.getTipoUsuario());
        
        return new UsuarioEntity(
            domain.getId(),
            domain.getNome(),
            domain.getEmail().getValor(),
            domain.getLogin(),
            domain.getSenha(),
            enderecoEntity,
            tipoUsuario,
            null,
            null
        );
    }
}
