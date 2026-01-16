package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CardapioItem {

    private final Long id;
    private final Long restauranteId;
    private final String nome;
    private final String descricao;
    private final Double preco;
    private final boolean apenasPresencial;
    private final String caminhoFoto;

    private CardapioItem(Long id, Long restauranteId, String nome, String descricao,
            Double preco, boolean apenasPresencial, String caminhoFoto) {
        this.id = id;
        this.restauranteId = restauranteId;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.apenasPresencial = apenasPresencial;
        this.caminhoFoto = caminhoFoto;
    }

    public static CardapioItem criar(Long id, Long restauranteId, String nome, String descricao,
            Double preco, boolean apenasPresencial, String caminhoFoto) {
        if (restauranteId == null || restauranteId <= 0) {
            throw new DomainException("ID do restaurante é obrigatório e deve ser maior que zero");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome do item não pode ser vazio");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DomainException("Descrição do item não pode ser vazia");
        }
        if (preco == null || preco <= 0) {
            throw new DomainException("Preço deve ser maior que zero");
        }

        return new CardapioItem(id, restauranteId, nome.trim(), descricao.trim(), preco,
                apenasPresencial, caminhoFoto != null ? caminhoFoto.trim() : "");
    }

    @Override
    public String toString() {
        return "CardapioItem{" +
                "id=" + id +
                ", restauranteId=" + restauranteId +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", apenasPresencial=" + apenasPresencial +
                '}';
    }
}
