package br.com.fiap.core.exceptions;

public class EmailJaCadastradoException extends DomainException {
    public EmailJaCadastradoException(String email) {
        super("Email já cadastrado: " + email);
    }
}
