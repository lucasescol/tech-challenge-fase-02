package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.EmailInvalidoException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class Email {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    
    private final String valor;
    
    public Email(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new EmailInvalidoException("Email não pode ser vazio");
        }
        
        String emailNormalizado = valor.trim().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(emailNormalizado).matches()) {
            throw new EmailInvalidoException("Email inválido: " + valor);
        }
        
        this.valor = emailNormalizado;
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
