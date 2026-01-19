package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.Usuario;
import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.TipoUsuarioEntity;
import br.com.fiap.infra.persistence.jpa.entities.UsuarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UsuarioMapper - Testes Unitários")
class UsuarioMapperTest {

        @Test
        @DisplayName("Deve converter Entity para Domain corretamente")
        void deveConverterEntityParaDomain() {
                EnderecoEntity enderecoEntity = new EnderecoEntity(
                                1L,
                                "Rua A",
                                "100",
                                "Apto 10",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "01000-000");

                TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(
                                1L,
                                "CLIENTE",
                                "Cliente");

                UsuarioEntity entity = new UsuarioEntity(
                                1L,
                                "João Silva",
                                "joao@email.com",
                                "joao",
                                "senha123",
                                enderecoEntity,
                                tipoEntity,
                                null,
                                null);

                Usuario domain = UsuarioMapper.toDomain(entity);

                assertThat(domain).isNotNull();
                assertThat(domain.getId()).isEqualTo(1L);
                assertThat(domain.getNome()).isEqualTo("João Silva");
                assertThat(domain.getEmail().getValor()).isEqualTo("joao@email.com");
                assertThat(domain.getLogin()).isEqualTo("joao");
                assertThat(domain.getSenha()).isEqualTo("senha123");
                assertThat(domain.getEndereco().getLogradouro()).isEqualTo("Rua A");
                assertThat(domain.getEndereco().getNumero()).isEqualTo("100");
                assertThat(domain.getEndereco().getComplemento()).isEqualTo("Apto 10");
                assertThat(domain.getTipoUsuario().getNome()).isEqualTo("CLIENTE");
        }

        @Test
        @DisplayName("Deve converter Domain para Entity corretamente")
        void deveConverterDomainParaEntity() {
                br.com.fiap.core.domain.Endereco endereco = new br.com.fiap.core.domain.Endereco(
                                "Avenida Paulista",
                                "500",
                                "Sala 20",
                                "Bela Vista",
                                "São Paulo",
                                "SP",
                                "01310-100");

                br.com.fiap.core.domain.TipoUsuario tipoUsuario = br.com.fiap.core.domain.TipoUsuario.create(
                                2L,
                                "DONO_RESTAURANTE",
                                "Dono de Restaurante");

                Usuario domain = Usuario.create(
                                2L,
                                "Maria Silva",
                                "maria@email.com",
                                "maria",
                                "senha456",
                                endereco,
                                tipoUsuario);

                UsuarioEntity entity = UsuarioMapper.toEntity(domain);

                assertThat(entity).isNotNull();
                assertThat(entity.getId()).isEqualTo(2L);
                assertThat(entity.getNome()).isEqualTo("Maria Silva");
                assertThat(entity.getEmail()).isEqualTo("maria@email.com");
                assertThat(entity.getLogin()).isEqualTo("maria");
                assertThat(entity.getSenha()).isEqualTo("senha456");
                assertThat(entity.getEndereco().getLogradouro()).isEqualTo("Avenida Paulista");
                assertThat(entity.getEndereco().getNumero()).isEqualTo("500");
                assertThat(entity.getTipoUsuario().getNome()).isEqualTo("DONO_RESTAURANTE");
        }

        @Test
        @DisplayName("Deve converter Entity sem complemento para Domain")
        void deveConverterEntitySemComplementoParaDomain() {
                EnderecoEntity enderecoEntity = new EnderecoEntity(
                                2L,
                                "Rua B",
                                "200",
                                "",
                                "Jardins",
                                "São Paulo",
                                "SP",
                                "02000-000");

                TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");

                UsuarioEntity entity = new UsuarioEntity(
                                3L,
                                "Pedro Santos",
                                "pedro@email.com",
                                "pedro",
                                "senha789",
                                enderecoEntity,
                                tipoEntity,
                                null,
                                null);

                Usuario domain = UsuarioMapper.toDomain(entity);

                assertThat(domain.getEndereco().getComplemento()).isEmpty();
        }

        @Test
        @DisplayName("Deve converter diferentes tipos de usuário corretamente")
        void deveConverterDiferentesTiposUsuario() {
                EnderecoEntity endereco = new EnderecoEntity(
                                3L, "Rua C", "300", "", "Centro", "São Paulo", "SP", "03000-000");

                TipoUsuarioEntity tipoCliente = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");
                TipoUsuarioEntity tipoDono = new TipoUsuarioEntity(2L, "DONO_RESTAURANTE", "Dono de Restaurante");
                TipoUsuarioEntity tipoAdmin = new TipoUsuarioEntity(3L, "ADMINISTRADOR", "Administrador");

                UsuarioEntity entityCliente = new UsuarioEntity(
                                4L, "User 1", "user1@email.com", "user1", "senha123", endereco, tipoCliente, null,
                                null);
                UsuarioEntity entityDono = new UsuarioEntity(
                                5L, "User 2", "user2@email.com", "user2", "senha123", endereco, tipoDono, null, null);
                UsuarioEntity entityAdmin = new UsuarioEntity(
                                6L, "User 3", "user3@email.com", "user3", "senha123", endereco, tipoAdmin, null, null);

                Usuario domainCliente = UsuarioMapper.toDomain(entityCliente);
                Usuario domainDono = UsuarioMapper.toDomain(entityDono);
                Usuario domainAdmin = UsuarioMapper.toDomain(entityAdmin);

                assertThat(domainCliente.getTipoUsuario().getNome()).isEqualTo("CLIENTE");
                assertThat(domainDono.getTipoUsuario().getNome()).isEqualTo("DONO_RESTAURANTE");
                assertThat(domainAdmin.getTipoUsuario().getNome()).isEqualTo("ADMINISTRADOR");
        }

        @Test
        @DisplayName("Deve normalizar email para lowercase na conversão")
        void deveNormalizarEmailParaLowercase() {
                EnderecoEntity enderecoEntity = new EnderecoEntity(
                                4L, "Rua D", "400", "", "Centro", "São Paulo", "SP", "04000-000");
                TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");

                UsuarioEntity entity = new UsuarioEntity(
                                7L,
                                "Ana Paula",
                                "ANA.PAULA@EMAIL.COM",
                                "ana",
                                "senha123",
                                enderecoEntity,
                                tipoEntity,
                                null,
                                null);

                Usuario domain = UsuarioMapper.toDomain(entity);

                assertThat(domain.getEmail().getValor()).isEqualTo("ana.paula@email.com");
        }

        @Test
        @DisplayName("Deve manter integridade na conversão bidirecional")
        void deveManterIntegridadeNaConversaoBidirecional() {
                EnderecoEntity enderecoOriginal = new EnderecoEntity(
                                5L,
                                "Rua E",
                                "500",
                                "Casa",
                                "Jardim",
                                "Curitiba",
                                "PR",
                                "80000-000");

                TipoUsuarioEntity tipoOriginal = new TipoUsuarioEntity(
                                4L,
                                "CLIENTE",
                                "Cliente Comum");

                UsuarioEntity entityOriginal = new UsuarioEntity(
                                8L,
                                "Carlos Eduardo",
                                "carlos@email.com",
                                "carlos",
                                "senhaSegura123",
                                enderecoOriginal,
                                tipoOriginal,
                                null,
                                null);

                Usuario domain = UsuarioMapper.toDomain(entityOriginal);
                UsuarioEntity entityConvertida = UsuarioMapper.toEntity(domain);

                assertThat(entityConvertida.getId()).isEqualTo(entityOriginal.getId());
                assertThat(entityConvertida.getNome()).isEqualTo(entityOriginal.getNome());
                assertThat(entityConvertida.getEmail()).isEqualTo(entityOriginal.getEmail().toLowerCase());
                assertThat(entityConvertida.getLogin()).isEqualTo(entityOriginal.getLogin());
                assertThat(entityConvertida.getSenha()).isEqualTo(entityOriginal.getSenha());
                assertThat(entityConvertida.getTipoUsuario().getNome()).isEqualTo(tipoOriginal.getNome());

                assertThat(entityConvertida.getEndereco().getLogradouro()).isEqualTo(enderecoOriginal.getLogradouro());
                assertThat(entityConvertida.getEndereco().getNumero()).isEqualTo(enderecoOriginal.getNumero());
                assertThat(entityConvertida.getEndereco().getComplemento())
                                .isEqualTo(enderecoOriginal.getComplemento());
                assertThat(entityConvertida.getEndereco().getBairro()).isEqualTo(enderecoOriginal.getBairro());
                assertThat(entityConvertida.getEndereco().getCidade()).isEqualTo(enderecoOriginal.getCidade());
                assertThat(entityConvertida.getEndereco().getEstado()).isEqualTo(enderecoOriginal.getEstado());
        }

        @Test
        @DisplayName("Deve converter CEP removendo formatação")
        void deveConverterCepRemovendoFormatacao() {
                EnderecoEntity enderecoEntity = new EnderecoEntity(
                                6L,
                                "Rua F",
                                "600",
                                "",
                                "Centro",
                                "São Paulo",
                                "SP",
                                "05000-000");

                TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(1L, "CLIENTE", "Cliente");

                UsuarioEntity entity = new UsuarioEntity(
                                9L, "Teste User", "teste@email.com", "teste", "senha123", enderecoEntity, tipoEntity,
                                null,
                                null);

                Usuario domain = UsuarioMapper.toDomain(entity);
                UsuarioEntity entityConvertida = UsuarioMapper.toEntity(domain);

                assertThat(entityConvertida.getEndereco().getCep()).isEqualTo("05000000");
        }
}