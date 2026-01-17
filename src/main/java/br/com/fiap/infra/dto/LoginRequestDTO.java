package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação")
public record LoginRequestDTO(
    @NotBlank(message = "Login é obrigatório")
    @Schema(description = "Login do usuário", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    String login,
    
    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    String senha
) {}
