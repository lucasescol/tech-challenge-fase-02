package br.com.fiap.core.exceptions;

public class UsuarioNaoEncontradoException extends DomainException {
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado: " + id);
    }

    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}
