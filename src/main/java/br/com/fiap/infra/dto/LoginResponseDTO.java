package br.com.fiap.infra.dto;

public record LoginResponseDTO(
    String token,
    String tipo,
    String login,
    String tipoUsuario
) {}
