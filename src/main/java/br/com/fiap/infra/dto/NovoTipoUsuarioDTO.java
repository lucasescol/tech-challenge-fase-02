package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de um novo tipo de usuário")
public record NovoTipoUsuarioDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
        @Schema(description = "Nome do tipo de usuário", example = "GERENTE", requiredMode = Schema.RequiredMode.REQUIRED)
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 3, max = 200, message = "Descrição deve ter entre 3 e 200 caracteres")
        @Schema(description = "Descrição detalhada do tipo de usuário", example = "Gerente de restaurante com permissões administrativas", requiredMode = Schema.RequiredMode.REQUIRED)
        String descricao
) {
}
