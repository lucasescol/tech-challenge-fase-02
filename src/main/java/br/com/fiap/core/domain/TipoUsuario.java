package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class TipoUsuario {

    private final Long id;
    private final String nome;
    private final String descricao;

    private TipoUsuario(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public static TipoUsuario create(Long id, String nome, String descricao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome do tipo de usuário não pode ser vazio");
        }

        String nomeTrimmed = nome.trim();
        if (nomeTrimmed.length() < 3) {
            throw new DomainException("Nome do tipo de usuário deve ter no mínimo 3 caracteres");
        }

        if (nomeTrimmed.length() > 50) {
            throw new DomainException("Nome do tipo de usuário deve ter no máximo 50 caracteres");
        }

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DomainException("Descrição do tipo de usuário não pode ser vazia");
        }

        String descricaoTrimmed = descricao.trim();
        if (descricaoTrimmed.length() < 3) {
            throw new DomainException("Descrição do tipo de usuário deve ter no mínimo 3 caracteres");
        }

        if (descricaoTrimmed.length() > 200) {
            throw new DomainException("Descrição do tipo de usuário deve ter no máximo 200 caracteres");
        }

        return new TipoUsuario(id, nomeTrimmed, descricaoTrimmed);
    }

    @Override
    public String toString() {
        return "TipoUsuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
