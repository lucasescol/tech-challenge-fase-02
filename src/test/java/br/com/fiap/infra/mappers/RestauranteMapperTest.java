package br.com.fiap.infra.mappers;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.infra.persistence.jpa.entities.EnderecoEntity;
import br.com.fiap.infra.persistence.jpa.entities.RestauranteEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RestauranteMapper - Testes Unitários")
class RestauranteMapperTest {

    @Test
    @DisplayName("Deve converter Entity para Domain corretamente")
    void deveConverterEntityParaDomain() {
        EnderecoEntity enderecoEntity = new EnderecoEntity(
                1L,
                "Rua A",
                "100",
                "Sala 5",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000");

        RestauranteEntity entity = new RestauranteEntity(
                1L,
                "Restaurante Italiano",
                enderecoEntity,
                "ITALIANA",
                "08:00-22:00",
                10L,
                null,
                null,
                null);

        Restaurante domain = RestauranteMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getNome()).isEqualTo("Restaurante Italiano");
        assertThat(domain.getEndereco().getLogradouro()).isEqualTo("Rua A");
        assertThat(domain.getEndereco().getNumero()).isEqualTo("100");
        assertThat(domain.getEndereco().getComplemento()).isEqualTo("Sala 5");
        assertThat(domain.getEndereco().getBairro()).isEqualTo("Centro");
        assertThat(domain.getEndereco().getCidade()).isEqualTo("São Paulo");
        assertThat(domain.getEndereco().getEstado()).isEqualTo("SP");
        assertThat(domain.getEndereco().getCepLimpo()).isEqualTo("01000000");
        assertThat(domain.getTipoCozinha().getValor()).isEqualTo("ITALIANA");
        assertThat(domain.getHorarioFuncionamento().getValor()).isEqualTo("08:00-22:00");
        assertThat(domain.getDonoRestaurante()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Deve converter Domain para Entity corretamente")
    void deveConverterDomainParaEntity() {
        Restaurante domain = Restaurante.create(
                2L,
                "Restaurante Japonês",
                "Avenida Paulista",
                "500",
                "Loja 10",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-100",
                "JAPONESA",
                "12:00-23:00",
                15L);

        RestauranteEntity entity = RestauranteMapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getNome()).isEqualTo("Restaurante Japonês");
        assertThat(entity.getEndereco().getLogradouro()).isEqualTo("Avenida Paulista");
        assertThat(entity.getEndereco().getNumero()).isEqualTo("500");
        assertThat(entity.getEndereco().getComplemento()).isEqualTo("Loja 10");
        assertThat(entity.getEndereco().getBairro()).isEqualTo("Bela Vista");
        assertThat(entity.getEndereco().getCidade()).isEqualTo("São Paulo");
        assertThat(entity.getEndereco().getEstado()).isEqualTo("SP");
        assertThat(entity.getEndereco().getCep()).isEqualTo("01310100");
        assertThat(entity.getTipoCozinha()).isEqualTo("JAPONESA");
        assertThat(entity.getHorarioFuncionamento()).isEqualTo("12:00-23:00");
        assertThat(entity.getDonoRestaurante()).isEqualTo(15L);
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

        RestauranteEntity entity = new RestauranteEntity(
                3L,
                "Restaurante Brasileiro",
                enderecoEntity,
                "BRASILEIRA",
                "11:00-22:00",
                20L,
                null,
                null,
                null);

        Restaurante domain = RestauranteMapper.toDomain(entity);

        assertThat(domain.getEndereco().getComplemento()).isEmpty();
    }

    @Test
    @DisplayName("Deve converter diferentes tipos de cozinha corretamente")
    void deveConverterDiferentesTiposCozinha() {
        EnderecoEntity enderecoEntity = new EnderecoEntity(
                3L,
                "Rua C",
                "300",
                "",
                "Vila Nova",
                "São Paulo",
                "SP",
                "03000-000");

        RestauranteEntity entityItaliana = new RestauranteEntity(
                4L, "Rest Italiano", enderecoEntity, "ITALIANA", "10:00-22:00", 25L, null, null, null);
        RestauranteEntity entityJaponesa = new RestauranteEntity(
                5L, "Rest Japonês", enderecoEntity, "JAPONESA", "10:00-22:00", 25L, null, null, null);
        RestauranteEntity entityBrasileira = new RestauranteEntity(
                6L, "Rest Brasileiro", enderecoEntity, "BRASILEIRA", "10:00-22:00", 25L, null, null, null);

        Restaurante domainItaliana = RestauranteMapper.toDomain(entityItaliana);
        Restaurante domainJaponesa = RestauranteMapper.toDomain(entityJaponesa);
        Restaurante domainBrasileira = RestauranteMapper.toDomain(entityBrasileira);

        assertThat(domainItaliana.getTipoCozinha().getValor()).isEqualTo("ITALIANA");
        assertThat(domainJaponesa.getTipoCozinha().getValor()).isEqualTo("JAPONESA");
        assertThat(domainBrasileira.getTipoCozinha().getValor()).isEqualTo("BRASILEIRA");
    }

    @Test
    @DisplayName("Deve manter integridade na conversão bidirecional")
    void deveManterIntegridadeNaConversaoBidirecional() {
        EnderecoEntity enderecoOriginal = new EnderecoEntity(
                4L,
                "Rua D",
                "400",
                "Apto 100",
                "Centro",
                "Rio de Janeiro",
                "RJ",
                "20000-000");

        RestauranteEntity entityOriginal = new RestauranteEntity(
                7L,
                "Restaurante Teste",
                enderecoOriginal,
                "MEXICANA",
                "18:00-23:00",
                30L,
                null,
                null,
                null);

        Restaurante domain = RestauranteMapper.toDomain(entityOriginal);
        RestauranteEntity entityConvertida = RestauranteMapper.toEntity(domain);

        assertThat(entityConvertida.getId()).isEqualTo(entityOriginal.getId());
        assertThat(entityConvertida.getNome()).isEqualTo(entityOriginal.getNome());
        assertThat(entityConvertida.getTipoCozinha()).isEqualTo(entityOriginal.getTipoCozinha());
        assertThat(entityConvertida.getHorarioFuncionamento()).isEqualTo(entityOriginal.getHorarioFuncionamento());
        assertThat(entityConvertida.getDonoRestaurante()).isEqualTo(entityOriginal.getDonoRestaurante());

        assertThat(entityConvertida.getEndereco().getLogradouro()).isEqualTo(enderecoOriginal.getLogradouro());
        assertThat(entityConvertida.getEndereco().getNumero()).isEqualTo(enderecoOriginal.getNumero());
        assertThat(entityConvertida.getEndereco().getComplemento()).isEqualTo(enderecoOriginal.getComplemento());
        assertThat(entityConvertida.getEndereco().getBairro()).isEqualTo(enderecoOriginal.getBairro());
        assertThat(entityConvertida.getEndereco().getCidade()).isEqualTo(enderecoOriginal.getCidade());
        assertThat(entityConvertida.getEndereco().getEstado()).isEqualTo(enderecoOriginal.getEstado());
    }

    @Test
    @DisplayName("Deve converter CEP com formatação corretamente")
    void deveConverterCepComFormatacao() {
        EnderecoEntity enderecoEntity = new EnderecoEntity(
                5L,
                "Rua E",
                "500",
                "",
                "Centro",
                "Curitiba",
                "PR",
                "80000-000");

        RestauranteEntity entity = new RestauranteEntity(
                8L,
                "Restaurante PR",
                enderecoEntity,
                "BRASILEIRA",
                "11:00-22:00",
                35L,
                null,
                null,
                null);

        Restaurante domain = RestauranteMapper.toDomain(entity);
        RestauranteEntity entityConvertida = RestauranteMapper.toEntity(domain);

        assertThat(entityConvertida.getEndereco().getCep()).isEqualTo("80000000");
    }
}