package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Usuario {
    private final Long id;
    private final String nome;
    private final Email email;
    private final TipoUsuario tipoUsuario;

    private Usuario(Long id, String nome, Email email, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public static Usuario create(Long id, String nome, String email, TipoUsuario tipoUsuario) {
        
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome do usuário não pode ser vazio");
        }
        
        if (tipoUsuario == null) {
            throw new DomainException("Tipo de usuário não pode ser nulo");
        }
        
        Email emailVO = new Email(email);

        return new Usuario(id, nome.trim(), emailVO, tipoUsuario);
    }

    public enum TipoUsuario {
        DONO_RESTAURANTE,
        CLIENTE,
        ADMINISTRADOR
    }
}
