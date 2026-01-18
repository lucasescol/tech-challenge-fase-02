package br.com.fiap.core.exceptions;

public class TipoUsuarioInvalidoException extends DomainException {
    public TipoUsuarioInvalidoException(String tipoUsuario) {
        super("Tipo de usuário inválido: " + tipoUsuario);
    }
}
