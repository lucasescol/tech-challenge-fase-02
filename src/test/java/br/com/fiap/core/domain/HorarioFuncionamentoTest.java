package br.com.fiap.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.core.exceptions.HorarioInvalidoException;

@DisplayName("HorarioFuncionamento Value Object Tests")
class HorarioFuncionamentoTest {

    @Test
    @DisplayName("Deve criar horário válido")
    void deveCriarHorarioValido() {
        HorarioFuncionamento horario = new HorarioFuncionamento("09:00-23:00");

        assertNotNull(horario);
        assertEquals("09:00-23:00", horario.getValor());
    }

    @Test
    @DisplayName("Deve aceitar horários válidos")
    void deveAceitarHorariosValidos() {
        assertDoesNotThrow(() -> new HorarioFuncionamento("00:00-23:59"));
        assertDoesNotThrow(() -> new HorarioFuncionamento("09:00-17:00"));
        assertDoesNotThrow(() -> new HorarioFuncionamento("10:30-22:30"));
    }

    @Test
    @DisplayName("Deve lançar exceção para horário com hora inválida (>23)")
    void deveLancarExcecaoParaHoraInvalidaMaior() {
        assertThrows(HorarioInvalidoException.class,
                () -> new HorarioFuncionamento("25:00-26:00"));
    }

    @Test
    @DisplayName("Deve lançar exceção para horário com minuto inválido (>59)")
    void deveLancarExcecaoParaMinutoInvalido() {
        assertThrows(HorarioInvalidoException.class,
                () -> new HorarioFuncionamento("09:65-23:00"));
    }

    @Test
    @DisplayName("Deve lançar exceção para horário vazio")
    void deveLancarExcecaoParaHorarioVazio() {
        assertThrows(HorarioInvalidoException.class,
                () -> new HorarioFuncionamento(""));
    }

    @Test
    @DisplayName("Deve lançar exceção para horário nulo")
    void deveLancarExcecaoParaHorarioNulo() {
        assertThrows(HorarioInvalidoException.class,
                () -> new HorarioFuncionamento(null));
    }

    @Test
    @DisplayName("Deve lançar exceção para horário com formato inválido")
    void deveLancarExcecaoParaFormatoInvalido() {
        assertThrows(HorarioInvalidoException.class,
                () -> new HorarioFuncionamento("09-23"));
    }

    @Test
    @DisplayName("Deve comparar horários por igualdade")
    void deveCompararHorariosPorIgualdade() {
        HorarioFuncionamento horario1 = new HorarioFuncionamento("09:00-23:00");
        HorarioFuncionamento horario2 = new HorarioFuncionamento("09:00-23:00");

        assertEquals(horario1, horario2);
    }

    @Test
    @DisplayName("Deve diferenciar horários diferentes")
    void deveDiferenciarHorariosDiferentes() {
        HorarioFuncionamento horario1 = new HorarioFuncionamento("09:00-23:00");
        HorarioFuncionamento horario2 = new HorarioFuncionamento("10:00-22:00");

        assertNotEquals(horario1, horario2);
    }

    @Test
    @DisplayName("Deve aceitar horário que atravessa a meia-noite (turno noturno)")
    void deveAceitarHorarioTurnoNoturno() {
        assertDoesNotThrow(() -> new HorarioFuncionamento("23:00-05:00"));

        HorarioFuncionamento horario = new HorarioFuncionamento("23:00-05:00");
        assertEquals("23:00-05:00", horario.getValor());
    }

    @Test
    @DisplayName("Deve lançar exceção se hora e fechamento forem iguais")
    void deveLancarExcecaoSeHoraInicialIgualFinal() {
        assertThrows(HorarioInvalidoException.class,
                () -> new HorarioFuncionamento("09:00-09:00"));
    }
}
