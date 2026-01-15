package br.com.fiap.infra.dto;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    String login,
    String endereco,
    String tipoUsuario
) {}
