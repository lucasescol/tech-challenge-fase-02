package br.com.fiap.core.exceptions;

public class CredenciaisInvalidasException extends DomainException {
    public CredenciaisInvalidasException() {
        super("Credenciais inválidas");
    }
}
