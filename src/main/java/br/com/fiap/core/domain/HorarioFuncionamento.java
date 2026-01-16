package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.HorarioInvalidoException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalTime;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class HorarioFuncionamento {

    private static final Pattern HORARIO_PATTERN = Pattern
            .compile("^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$");

    private final String valor;
    private final String horaAbertura;
    private final String horaFechamento;

    public HorarioFuncionamento(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new HorarioInvalidoException("Horário de funcionamento não pode ser vazio");
        }

        String horarioNormalizado = valor.trim();

        if (!HORARIO_PATTERN.matcher(horarioNormalizado).matches()) {
            throw new HorarioInvalidoException(
                    "Horário inválido: " + valor + ". Formato esperado: HH:mm-HH:mm (ex: 08:00-22:00 ou 11:00-00:00)");
        }

        String[] partes = horarioNormalizado.split("-");
        this.horaAbertura = partes[0];
        this.horaFechamento = partes[1];

        validarHorarios(horaAbertura, horaFechamento);
        this.valor = horarioNormalizado;
    }

    private void validarHorarios(String abertura, String fechamento) {
        LocalTime timeAbertura = LocalTime.parse(abertura);
        LocalTime timeFechamento = LocalTime.parse(fechamento);

        if (timeAbertura.equals(timeFechamento)) {
            throw new HorarioInvalidoException(
                    "Hora de abertura e fechamento não podem ser iguais");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
