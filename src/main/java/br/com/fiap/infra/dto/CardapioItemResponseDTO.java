package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de resposta de um item do cardápio")
public record CardapioItemResponseDTO(
    @Schema(description = "ID do item", example = "1")
    Long id,
    
    @Schema(description = "ID do restaurante ao qual o item pertence", example = "1")
    Long restauranteId,
    
    @Schema(description = "Nome do item do cardápio", example = "Pizza Margherita")
    String nome,
    
    @Schema(description = "Descrição detalhada do item", example = "Pizza tradicional com molho de tomate, mussarela e manjericão fresco")
    String descricao,
    
    @Schema(description = "Preço do item", example = "45.90")
    Double preco,
    
    @Schema(description = "Indica se o item está disponível apenas para consumo presencial", example = "false")
    boolean apenasPresencial,
    
    @Schema(description = "Caminho da foto do item", example = "/images/pizza-margherita.jpg")
    String caminhoFoto
) {
}
