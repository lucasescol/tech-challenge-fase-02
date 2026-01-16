package br.com.fiap.infra.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovoTipoUsuarioDTO {

    @NotBlank(message = "Nome do tipo de usuário é obrigatório")
    private String nome;

    @NotBlank(message = "Tipo de conta é obrigatório")
    private String tipo;

    private String descricao;
}
