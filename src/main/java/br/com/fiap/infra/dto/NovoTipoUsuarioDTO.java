package br.com.fiap.infra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NovoTipoUsuarioDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 3, max = 200, message = "Descrição deve ter entre 3 e 200 caracteres")
        String descricao
) {
}
