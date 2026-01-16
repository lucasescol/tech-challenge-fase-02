package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de login com JWT token")
public record LoginResponseDTO(
                @Schema(example = "eyJhbGciOiJIUzUxMiJ9...", description = "JWT token válido por 24 horas") String token,

                @Schema(example = "1", description = "ID do usuário autenticado") Long usuarioId,

                @Schema(example = "João Silva", description = "Nome completo do usuário") String nome,

                @Schema(example = "joao@example.com", description = "Email do usuário") String email,

                @Schema(example = "ADMIN", description = "Tipo de usuário (ADMIN, USER)") String tipoUsuario) {
}
