package br.com.fiap.core.exceptions;

public class UsuarioNaoEncontradoException extends DomainException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
    
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado com id: " + id);
    }
}
