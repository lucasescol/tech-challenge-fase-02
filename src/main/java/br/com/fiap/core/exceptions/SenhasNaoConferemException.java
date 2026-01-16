package br.com.fiap.core.exceptions;

public class SenhasNaoConferemException extends DomainException {
    public SenhasNaoConferemException() {
        super("Senhas não conferem");
    }
}
