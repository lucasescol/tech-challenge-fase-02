package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class TipoUsuario {

    public enum TipoConta {
        DONO_RESTAURANTE,
        CLIENTE
    }

    private final Long id;
    private final String nome;
    private final TipoConta tipo;
    private final String descricao;

    private TipoUsuario(Long id, String nome, TipoConta tipo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public static TipoUsuario criar(String nome, TipoConta tipo) {
        return criar(null, nome, tipo, "");
    }

    public static TipoUsuario criar(Long id, String nome, TipoConta tipo, String descricao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome do tipo de usuário não pode ser vazio");
        }
        if (tipo == null) {
            throw new DomainException("Tipo de conta deve ser DONO_RESTAURANTE ou CLIENTE");
        }

        return new TipoUsuario(id, nome.trim(), tipo, descricao != null ? descricao.trim() : "");
    }

    @Override
    public String toString() {
        return "TipoUsuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
