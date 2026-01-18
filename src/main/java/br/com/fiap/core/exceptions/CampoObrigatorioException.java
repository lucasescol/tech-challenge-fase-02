package br.com.fiap.core.exceptions;

public class CampoObrigatorioException extends DomainException {
    public CampoObrigatorioException(String campo) {
        super(campo + " obrigatório");
    }
}
