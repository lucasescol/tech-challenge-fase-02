package br.com.fiap.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizarRestauranteDTO(
    @NotBlank(message = "Nome é obrigatório")
    String nome, 
    
    @NotBlank(message = "Logradouro é obrigatório")
    String logradouro,
    
    @NotBlank(message = "Número é obrigatório")
    String numero,
    
    String complemento,
    String bairro,
    
    @NotBlank(message = "Cidade é obrigatória")
    String cidade,
    
    @NotBlank(message = "Estado é obrigatório")
    String estado,
    
    @NotBlank(message = "CEP é obrigatório")
    String cep,
    
    @NotBlank(message = "Tipo de cozinha é obrigatório")
    String tipoCozinha, 
    
    @NotBlank(message = "Horário de funcionamento é obrigatório")
    String horarioFuncionamento
) {}
