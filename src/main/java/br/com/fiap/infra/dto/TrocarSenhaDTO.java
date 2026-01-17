package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para troca de senha")
public record TrocarSenhaDTO(
    @NotBlank(message = "Senha atual é obrigatória")
    @Schema(description = "Senha atual do usuário", example = "SenhaAntiga@123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    String senhaAtual,
    
    @NotBlank(message = "Nova senha é obrigatória")
    @Schema(description = "Nova senha (mínimo 8 caracteres, incluindo maiúsculas, minúsculas, números e especiais)", example = "SenhaNova@456", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    String novaSenha,
    
    @NotBlank(message = "Confirmação de senha é obrigatória")
    @Schema(description = "Confirmação da nova senha (deve ser igual à nova senha)", example = "SenhaNova@456", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    String confirmacaoSenha
) {}
