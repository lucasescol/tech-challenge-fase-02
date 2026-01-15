package br.com.fiap.core.exceptions;

public class SenhaInvalidaException extends DomainException {
    public SenhaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
