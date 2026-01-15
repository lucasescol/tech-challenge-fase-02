package br.com.fiap.core.exceptions;

public class SenhaAtualIncorretaException extends DomainException {
    public SenhaAtualIncorretaException() {
        super("Senha atual incorreta");
    }
}
