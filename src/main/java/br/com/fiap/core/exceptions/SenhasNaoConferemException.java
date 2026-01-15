package br.com.fiap.core.exceptions;

public class SenhasNaoConferemException extends DomainException {
    public SenhasNaoConferemException() {
        super("Nova senha e confirmação não conferem");
    }
}
