package br.com.fiap.core.exceptions;

public class LoginJaCadastradoException extends DomainException {
    public LoginJaCadastradoException(String login) {
        super("Login já cadastrado: " + login);
    }
}
