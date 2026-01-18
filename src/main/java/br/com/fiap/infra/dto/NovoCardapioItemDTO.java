package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para cadastro de um novo item do cardápio")
public record NovoCardapioItemDTO(
    @NotNull(message = "ID do restaurante é obrigatório")
    @Positive(message = "ID do restaurante deve ser maior que zero")
    @Schema(description = "ID do restaurante ao qual o item pertence", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long restauranteId,
    
    @NotBlank(message = "Nome do item é obrigatório")
    @Schema(description = "Nome do item do cardápio", example = "Pizza Margherita", requiredMode = Schema.RequiredMode.REQUIRED)
    String nome,
    
    @NotBlank(message = "Descrição do item é obrigatória")
    @Schema(description = "Descrição detalhada do item", example = "Pizza tradicional com molho de tomate, mussarela e manjericão fresco", requiredMode = Schema.RequiredMode.REQUIRED)
    String descricao,
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    @Schema(description = "Preço do item", example = "45.90", requiredMode = Schema.RequiredMode.REQUIRED)
    Double preco,
    
    @Schema(description = "Indica se o item está disponível apenas para consumo presencial", example = "false")
    boolean apenasPresencial,
    
    @Schema(description = "Caminho da foto do item", example = "/images/pizza-margherita.jpg")
    String caminhoFoto
) {
}
