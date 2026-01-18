package br.com.fiap.infra.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "cardapio_itens")
@Entity
public class CardapioItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long restauranteId;
    private String nome;
    private String descricao;
    private Double preco;
    private boolean apenasPresencial;
    private String caminhoFoto;

    public CardapioItemEntity() {}

    public CardapioItemEntity(Long id, Long restauranteId, String nome, String descricao,
            Double preco, boolean apenasPresencial, String caminhoFoto) {
        this.id = id;
        this.restauranteId = restauranteId;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.apenasPresencial = apenasPresencial;
        this.caminhoFoto = caminhoFoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public boolean isApenasPresencial() {
        return apenasPresencial;
    }

    public void setApenasPresencial(boolean apenasPresencial) {
        this.apenasPresencial = apenasPresencial;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
