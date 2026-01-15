package br.com.fiap.infra.dto;

import jakarta.validation.constraints.Email;

public record AtualizarUsuarioDTO(
    String nome,
    
    @Email(message = "Email deve ser válido")
    String email,
    
    String login,
    
    String logradouro,
    
    String numero,
    
    String complemento,
    String bairro,
    
    String cidade,
    
    String estado,
    
    String cep
) {}
