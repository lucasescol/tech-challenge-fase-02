package br.com.fiap.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioResponseDTO {

    private Long id;
    private String nome;
    private String tipo;
    private String descricao;
}
