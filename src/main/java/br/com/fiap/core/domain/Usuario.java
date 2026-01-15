package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import br.com.fiap.core.exceptions.SenhaInvalidaException;
import br.com.fiap.core.exceptions.SenhasNaoConferemException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Usuario {
    private final Long id;
    private final String nome;
    private final Email email;
    private final String login;
    private final String senha;
    private final Endereco endereco;
    private final TipoUsuario tipoUsuario;

    private Usuario(Long id, String nome, Email email, String login, String senha, Endereco endereco, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.endereco = endereco;
        this.tipoUsuario = tipoUsuario;

    }

    public static Usuario create(Long id, String nome, String email, String login, String senha, Endereco endereco, TipoUsuario tipoUsuario) {
        
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome do usuário não pode ser vazio");
        }
        
        if (tipoUsuario == null) {
            throw new DomainException("Tipo de usuário não pode ser nulo");
        }
        
        Email emailVO = new Email(email);

        if (login == null || login.trim().isEmpty()) {
            throw new DomainException("Login do usuário não pode ser vazio");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new DomainException("Senha do usuário não pode ser vazia");
        }

        return new Usuario(id, nome.trim(), emailVO, login, senha, endereco, tipoUsuario);
    }

    public static void validarNovaSenha(String novaSenha, String confirmacaoSenha) {
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new SenhaInvalidaException("Nova senha não pode ser vazia");
        }
        
        if (!novaSenha.equals(confirmacaoSenha)) {
            throw new SenhasNaoConferemException();
        }
    }

    public enum TipoUsuario {
        DONO_RESTAURANTE,
        CLIENTE,
        ADMINISTRADOR
    }
}
