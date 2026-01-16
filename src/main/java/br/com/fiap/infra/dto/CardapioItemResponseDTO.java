package br.com.fiap.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardapioItemResponseDTO {

    private Long id;
    private Long restauranteId;
    private String nome;
    private String descricao;
    private Double preco;
    private boolean apenasPresencial;
    private String caminhoFoto;
}
