package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Restaurante - Testes de Domínio")
class RestauranteTest {

    @Test
    @DisplayName("Deve criar restaurante com sucesso")
    void deveCriarRestauranteComSucesso() {
        Restaurante restaurante = Restaurante.create(
                1L,
                "Restaurante Teste",
                "Rua A",
                "100",
                "Sala 1",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        assertThat(restaurante).isNotNull();
        assertThat(restaurante.getId()).isEqualTo(1L);
        assertThat(restaurante.getNome()).isEqualTo("Restaurante Teste");
        assertThat(restaurante.getDonoRestaurante()).isEqualTo(1L);
        assertThat(restaurante.getTipoCozinha().getValor()).isEqualTo("ITALIANA");
        assertThat(restaurante.getHorarioFuncionamento().getValor()).isEqualTo("08:00-22:00");
    }

    @Test
    @DisplayName("Deve criar restaurante sem ID (para novo cadastro)")
    void deveCriarRestauranteSemId() {
        Restaurante restaurante = Restaurante.create(
                null,
                "Restaurante Novo",
                "Rua B",
                "200",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "JAPONESA",
                "11:00-23:00",
                2L);

        assertThat(restaurante).isNotNull();
        assertThat(restaurante.getId()).isNull();
        assertThat(restaurante.getNome()).isEqualTo("Restaurante Novo");
    }

    @Test
    @DisplayName("Deve fazer trim no nome do restaurante")
    void deveFazerTrimNoNome() {
        Restaurante restaurante = Restaurante.create(
                1L,
                "  Restaurante com Espaços  ",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        assertThat(restaurante.getNome()).isEqualTo("Restaurante com Espaços");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        assertThatThrownBy(() -> Restaurante.create(
                1L,
                null,
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do restaurante não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome é vazio")
    void deveLancarExcecaoQuandoNomeVazio() {
        assertThatThrownBy(() -> Restaurante.create(
                1L,
                "",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do restaurante não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoCurto() {
        assertThatThrownBy(() -> Restaurante.create(
                1L,
                "AB",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do restaurante deve ter no mínimo 3 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tem mais de 100 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoLongo() {
        String nomeLongo = "A".repeat(101);

        assertThatThrownBy(() -> Restaurante.create(
                1L,
                nomeLongo,
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do restaurante deve ter no máximo 100 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do dono é nulo")
    void deveLancarExcecaoQuandoDonoNulo() {
        assertThatThrownBy(() -> Restaurante.create(
                1L,
                "Restaurante Teste",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("ID do dono do restaurante é obrigatório");
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do dono é menor ou igual a zero")
    void deveLancarExcecaoQuandoDonoInvalido() {
        assertThatThrownBy(() -> Restaurante.create(
                1L,
                "Restaurante Teste",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                0L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("ID do dono do restaurante é obrigatório e deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve criar restaurante com nome no limite mínimo (3 caracteres)")
    void deveCriarRestauranteComNomeLimiteMinimo() {
        Restaurante restaurante = Restaurante.create(
                1L,
                "ABC",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        assertThat(restaurante.getNome()).isEqualTo("ABC");
    }

    @Test
    @DisplayName("Deve criar restaurante com nome no limite máximo (100 caracteres)")
    void deveCriarRestauranteComNomeLimiteMaximo() {
        String nome100 = "A".repeat(100);

        Restaurante restaurante = Restaurante.create(
                1L,
                nome100,
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        assertThat(restaurante.getNome()).hasSize(100);
    }

    @Test
    @DisplayName("Deve verificar igualdade entre restaurantes")
    void deveVerificarIgualdade() {
        Restaurante restaurante1 = Restaurante.create(
                1L,
                "Restaurante Teste",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        Restaurante restaurante2 = Restaurante.create(
                1L,
                "Restaurante Teste",
                "Rua A",
                "100",
                "",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000",
                "ITALIANA",
                "08:00-22:00",
                1L);

        assertThat(restaurante1).isEqualTo(restaurante2);
        assertThat(restaurante1.hashCode()).isEqualTo(restaurante2.hashCode());
    }
}