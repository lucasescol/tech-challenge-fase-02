package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.HorarioInvalidoException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class HorarioFuncionamento {
    
    private static final Pattern HORARIO_PATTERN = 
        Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$");
    
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
                "Horário inválido: " + valor + ". Formato esperado: HH:mm-HH:mm (ex: 08:00-22:00)"
            );
        }
        
        String[] partes = horarioNormalizado.split("-");
        this.horaAbertura = partes[0];
        this.horaFechamento = partes[1];
        
        if (horaAbertura.compareTo(horaFechamento) >= 0) {
            throw new HorarioInvalidoException(
                "Horário de abertura deve ser anterior ao horário de fechamento"
            );
        }
        
        this.valor = horarioNormalizado;
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
