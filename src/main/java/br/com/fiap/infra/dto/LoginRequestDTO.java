package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para realizar login")
public record LoginRequestDTO(
                @NotBlank(message = "Email é obrigatório") @Email(message = "Email deve ser válido") @Schema(example = "usuario@example.com", description = "Email do usuário") String email,

                @NotBlank(message = "Senha é obrigatória") @Schema(example = "senha123", description = "Senha do usuário") String senha) {
}
