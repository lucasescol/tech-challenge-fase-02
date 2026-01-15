package br.com.fiap.core.exceptions;

public class AcessoNegadoException extends DomainException {
    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
    
    public AcessoNegadoException() {
        super("Você não tem permissão para realizar esta operação");
    }
}
