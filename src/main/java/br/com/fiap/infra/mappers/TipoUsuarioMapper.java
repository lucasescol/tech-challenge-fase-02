package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;

public class TipoUsuarioMapper {

    private TipoUsuarioMapper() {
        // Utility class
    }

    public static TipoUsuario toDomain(TipoUsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return TipoUsuario.create(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    public static TipoUsuarioEntity toEntity(TipoUsuario domain) {
        if (domain == null) {
            return null;
        }

        return new TipoUsuarioEntity(
                domain.getId(),
                domain.getNome(),
                domain.getDescricao()
        );
    }
}
