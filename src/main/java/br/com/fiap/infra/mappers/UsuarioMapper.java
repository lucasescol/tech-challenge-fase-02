package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.core.domain.Usuario.TipoUsuario;
import br.com.fiap.infra.dto.NovoUsuarioDTO;
import br.com.fiap.infra.dto.UsuarioResponseDTO;
import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;

public class UsuarioMapper {

    public static Usuario toDomain(NovoUsuarioDTO dto) {
        return Usuario.criar(
                null,
                dto.getNome(),
                dto.getEmail(),
                dto.getLogin(),
                dto.getSenha(),
                dto.getEndereco() != null ? EnderecoMapper.toDomain(dto.getEndereco()) : null,
                TipoUsuario.valueOf(dto.getTipoUsuario()));
    }

    public static Usuario toDomain(UsuarioEntity entity) {
        return Usuario.criar(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getSenha(),
                entity.getEndereco() != null ? EnderecoMapper.toDomain(entity.getEndereco()) : null,
                TipoUsuario.valueOf(entity.getTipoUsuario().getTipo().name()));
    }

    public static UsuarioResponseDTO toResponse(Usuario domain) {
        return new UsuarioResponseDTO(
                domain.getId(),
                domain.getNome(),
                domain.getEmail().getValor(),
                domain.getLogin(),
                domain.getTipoUsuario().toString(),
                domain.getEndereco() != null ? EnderecoMapper.toDto(domain.getEndereco()) : null);
    }
}
