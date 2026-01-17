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
        
        String nomeTrimmed = nome.trim();
        if (nomeTrimmed.length() < 3) {
            throw new DomainException("Nome do usuário deve ter no mínimo 3 caracteres");
        }
        
        if (nomeTrimmed.length() > 100) {
            throw new DomainException("Nome do usuário deve ter no máximo 100 caracteres");
        }
        
        if (tipoUsuario == null) {
            throw new DomainException("Tipo de usuário não pode ser nulo");
        }
        
        Email emailVO = new Email(email);

        if (login == null || login.trim().isEmpty()) {
            throw new DomainException("Login do usuário não pode ser vazio");
        }
        
        String loginTrimmed = login.trim();
        if (loginTrimmed.length() < 3) {
            throw new DomainException("Login do usuário deve ter no mínimo 3 caracteres");
        }
        
        if (loginTrimmed.length() > 50) {
            throw new DomainException("Login do usuário deve ter no máximo 50 caracteres");
        }

        validarSenha(senha);

        return new Usuario(id, nomeTrimmed, emailVO, loginTrimmed, senha, endereco, tipoUsuario);
    }

    public static void validarSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new SenhaInvalidaException("Senha não pode ser vazia");
        }
        
        if (senha.length() < 6) {
            throw new SenhaInvalidaException("Senha deve ter no mínimo 6 caracteres");
        }
        
        if (senha.length() > 100) {
            throw new SenhaInvalidaException("Senha deve ter no máximo 100 caracteres");
        }
        
    }
    
    public static void validarNovaSenha(String novaSenha, String confirmacaoSenha) {
        validarSenha(novaSenha);
        
        if (!novaSenha.equals(confirmacaoSenha)) {
            throw new SenhasNaoConferemException();
        }
    }

}
