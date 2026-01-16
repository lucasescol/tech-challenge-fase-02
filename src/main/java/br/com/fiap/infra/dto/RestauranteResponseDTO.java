package br.com.fiap.infra.dto;

public record RestauranteResponseDTO(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento) {
}
