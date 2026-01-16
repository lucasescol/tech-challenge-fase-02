package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.infra.dto.NovoTipoUsuarioDTO;
import br.com.fiap.infra.dto.TipoUsuarioResponseDTO;

public class TipoUsuarioMapper {

    public static TipoUsuario toDomain(NovoTipoUsuarioDTO dto) {
        return TipoUsuario.criar(
                null,
                dto.getNome(),
                TipoUsuario.TipoConta.valueOf(dto.getTipo()),
                dto.getDescricao());
    }

    public static TipoUsuarioResponseDTO toResponse(TipoUsuario domain) {
        return new TipoUsuarioResponseDTO(
                domain.getId(),
                domain.getNome(),
                domain.getTipo().name(),
                domain.getDescricao());
    }
}
