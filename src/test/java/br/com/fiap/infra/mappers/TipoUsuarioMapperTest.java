package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.TipoUsuario;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TipoUsuarioMapper - Testes Unitários")
class TipoUsuarioMapperTest {

    @Test
    @DisplayName("Deve converter Domain para Entity")
    void deveConverterDomainParaEntity() {
        TipoUsuario domain = TipoUsuario.create(1L, "CLIENTE", "Cliente do sistema");

        TipoUsuarioEntity entity = TipoUsuarioMapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getNome()).isEqualTo("CLIENTE");
        assertThat(entity.getDescricao()).isEqualTo("Cliente do sistema");
    }

    @Test
    @DisplayName("Deve converter Entity para Domain")
    void deveConverterEntityParaDomain() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity(1L, "ADMINISTRADOR", "Administrador do sistema");

        TipoUsuario domain = TipoUsuarioMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getNome()).isEqualTo("ADMINISTRADOR");
        assertThat(domain.getDescricao()).isEqualTo("Administrador do sistema");
    }

    @Test
    @DisplayName("Deve retornar null quando Domain é null")
    void deveRetornarNullQuandoDomainNull() {
        TipoUsuarioEntity entity = TipoUsuarioMapper.toEntity(null);

        assertThat(entity).isNull();
    }

    @Test
    @DisplayName("Deve retornar null quando Entity é null")
    void deveRetornarNullQuandoEntityNull() {
        TipoUsuario domain = TipoUsuarioMapper.toDomain(null);

        assertThat(domain).isNull();
    }
}