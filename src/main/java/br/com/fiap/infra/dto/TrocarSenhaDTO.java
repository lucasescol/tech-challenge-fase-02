package br.com.fiap.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record TrocarSenhaDTO(
    @NotBlank(message = "Senha atual é obrigatória")
    String senhaAtual,
    
    @NotBlank(message = "Nova senha é obrigatória")
    String novaSenha,
    
    @NotBlank(message = "Confirmação de senha é obrigatória")
    String confirmacaoSenha
) {}
