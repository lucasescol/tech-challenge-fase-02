package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para cadastro de um novo restaurante")
public record NovoRestauranteDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do restaurante", example = "Trattoria Bella Italia", requiredMode = Schema.RequiredMode.REQUIRED)
    String nome, 
    
    @NotBlank(message = "Logradouro é obrigatório")
    @Schema(description = "Nome da rua/avenida", example = "Avenida Paulista", requiredMode = Schema.RequiredMode.REQUIRED)
    String logradouro,
    
    @NotBlank(message = "Número é obrigatório")
    @Schema(description = "Número do imóvel", example = "1578", requiredMode = Schema.RequiredMode.REQUIRED)
    String numero,
    
    @Schema(description = "Complemento do endereço", example = "Loja 5")
    String complemento,
    
    @Schema(description = "Bairro", example = "Bela Vista")
    String bairro,
    
    @NotBlank(message = "Cidade é obrigatória")
    @Schema(description = "Nome da cidade", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
    String cidade,
    
    @NotBlank(message = "Estado é obrigatório")
    @Schema(description = "Sigla do estado (UF)", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
    String estado,
    
    @NotBlank(message = "CEP é obrigatório")
    @Schema(description = "CEP no formato XXXXX-XXX", example = "01310-100", requiredMode = Schema.RequiredMode.REQUIRED)
    String cep,
    
    @NotBlank(message = "Tipo de cozinha é obrigatório")
    @Schema(description = "Tipo de cozinha (ITALIANA, JAPONESA, BRASILEIRA, etc.)", example = "ITALIANA", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoCozinha, 
    
    @NotBlank(message = "Horário de funcionamento é obrigatório")
    @Schema(description = "Horário de funcionamento", example = "11:00-23:00", requiredMode = Schema.RequiredMode.REQUIRED)
    String horarioFuncionamento
) {
    
}
