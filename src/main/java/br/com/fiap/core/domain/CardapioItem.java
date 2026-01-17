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
        
        String nomeTrimmed = nome.trim();
        if (nomeTrimmed.length() < 3) {
            throw new DomainException("Nome do item deve ter no mínimo 3 caracteres");
        }
        
        if (nomeTrimmed.length() > 100) {
            throw new DomainException("Nome do item deve ter no máximo 100 caracteres");
        }
        
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DomainException("Descrição do item não pode ser vazia");
        }
        
        String descricaoTrimmed = descricao.trim();
        if (descricaoTrimmed.length() < 10) {
            throw new DomainException("Descrição do item deve ter no mínimo 10 caracteres");
        }
        
        if (descricaoTrimmed.length() > 500) {
            throw new DomainException("Descrição do item deve ter no máximo 500 caracteres");
        }
        
        if (preco == null || preco <= 0) {
            throw new DomainException("Preço deve ser maior que zero");
        }
        
        if (preco > 999999.99) {
            throw new DomainException("Preço deve ser no máximo 999999.99");
        }

        return new CardapioItem(id, restauranteId, nomeTrimmed, descricaoTrimmed, preco,
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
