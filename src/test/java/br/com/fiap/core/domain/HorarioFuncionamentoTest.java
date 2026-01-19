package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.HorarioInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("HorarioFuncionamento - Testes de Value Object")
class HorarioFuncionamentoTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "08:00-22:00",
            "00:00-23:59",
            "06:30-18:45",
            "11:00-14:00",
            "18:00-23:00"
    })
    @DisplayName("Deve aceitar horários válidos")
    void deveAceitarHorariosValidos(String horario) {
        HorarioFuncionamento horarioFuncionamento = new HorarioFuncionamento(horario);

        assertThat(horarioFuncionamento.getValor()).isEqualTo(horario);
    }

    @Test
    @DisplayName("Deve criar horário e extrair hora de abertura")
    void deveCriarHorarioEExtrairAbertura() {
        HorarioFuncionamento horario = new HorarioFuncionamento("08:00-22:00");

        assertThat(horario.getHoraAbertura()).isEqualTo("08:00");
    }

    @Test
    @DisplayName("Deve criar horário e extrair hora de fechamento")
    void deveCriarHorarioEExtrairFechamento() {
        HorarioFuncionamento horario = new HorarioFuncionamento("08:00-22:00");

        assertThat(horario.getHoraFechamento()).isEqualTo("22:00");
    }

    @Test
    @DisplayName("Deve fazer trim no horário")
    void deveFazerTrimNoHorario() {
        HorarioFuncionamento horario = new HorarioFuncionamento("  08:00-22:00  ");

        assertThat(horario.getValor()).isEqualTo("08:00-22:00");
    }

    @Test
    @DisplayName("Deve aceitar horário que cruza meia-noite")
    void deveAceitarHorarioQueCruzaMeiaNoite() {
        HorarioFuncionamento horario = new HorarioFuncionamento("18:00-02:00");

        assertThat(horario.getValor()).isEqualTo("18:00-02:00");
        assertThat(horario.getHoraAbertura()).isEqualTo("18:00");
        assertThat(horario.getHoraFechamento()).isEqualTo("02:00");
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário é nulo")
    void deveLancarExcecaoQuandoHorarioNulo() {
        assertThatThrownBy(() -> new HorarioFuncionamento(null))
                .isInstanceOf(HorarioInvalidoException.class)
                .hasMessageContaining("Horário de funcionamento não pode ser vazio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário é vazio")
    void deveLancarExcecaoQuandoHorarioVazio() {
        assertThatThrownBy(() -> new HorarioFuncionamento(""))
                .isInstanceOf(HorarioInvalidoException.class)
                .hasMessageContaining("Horário de funcionamento não pode ser vazio");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "08:00", // Falta horário de fechamento
            "08-22", // Falta minutos
            "08:00-25:00", // Hora inválida
            "08:00-22:60", // Minuto inválido
            "8:00-22:00", // Formato incorreto (hora sem zero)
            "08:0-22:00" // Formato incorreto (minuto sem zero)
    })
    @DisplayName("Deve lançar exceção para formatos inválidos")
    void deveLancarExcecaoParaFormatosInvalidos(String horarioInvalido) {
        assertThatThrownBy(() -> new HorarioFuncionamento(horarioInvalido))
                .isInstanceOf(HorarioInvalidoException.class)
                .hasMessageContaining("Horário inválido");
    }

    @Test
    @DisplayName("Deve lançar exceção quando hora abertura igual hora fechamento")
    void deveLancarExcecaoQuandoAberturIgualFechamento() {
        assertThatThrownBy(() -> new HorarioFuncionamento("08:00-08:00"))
                .isInstanceOf(HorarioInvalidoException.class)
                .hasMessageContaining("Hora de abertura e fechamento não podem ser iguais");
    }

    @Test
    @DisplayName("Deve aceitar horário 24 horas")
    void deveAceitarHorario24Horas() {
        HorarioFuncionamento horario = new HorarioFuncionamento("00:00-23:59");

        assertThat(horario.getValor()).isEqualTo("00:00-23:59");
    }

    @Test
    @DisplayName("Deve verificar igualdade entre horários")
    void deveVerificarIgualdade() {
        HorarioFuncionamento horario1 = new HorarioFuncionamento("08:00-22:00");
        HorarioFuncionamento horario2 = new HorarioFuncionamento("08:00-22:00");

        assertThat(horario1).isEqualTo(horario2);
        assertThat(horario1.hashCode()).isEqualTo(horario2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        HorarioFuncionamento horario = new HorarioFuncionamento("08:00-22:00");

        String toString = horario.toString();

        assertThat(toString).isEqualTo("08:00-22:00");
    }
}