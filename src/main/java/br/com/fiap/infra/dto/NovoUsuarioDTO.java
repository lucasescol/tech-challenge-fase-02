package br.com.fiap.infra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NovoUsuarioDTO(
    @NotBlank(message = "Nome é obrigatório")
    String nome,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,
    
    @NotBlank(message = "Login é obrigatório")
    String login,
    
    @NotBlank(message = "Senha é obrigatória")
    String senha,
    
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
    
    @NotNull(message = "Tipo de usuário é obrigatório")
    String tipoUsuario
) {}
