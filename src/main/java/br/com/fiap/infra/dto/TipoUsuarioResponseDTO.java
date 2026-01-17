package br.com.fiap.infra.dto;

public record TipoUsuarioResponseDTO(
        Long id,
        String nome,
        String descricao
) {
}
