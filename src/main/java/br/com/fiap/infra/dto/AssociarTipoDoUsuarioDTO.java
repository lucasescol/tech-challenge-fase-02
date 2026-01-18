package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para atualização do tipo de um usuário")
public record AssociarTipoDoUsuarioDTO(
    @NotBlank(message = "Tipo de usuário é obrigatório")
    @Schema(description = "Nome do tipo de usuário", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoUsuario
) {}
