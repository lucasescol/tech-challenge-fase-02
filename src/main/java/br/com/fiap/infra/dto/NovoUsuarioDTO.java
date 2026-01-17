package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para cadastro de um novo usuário")
public record NovoUsuarioDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "Maria Silva Santos", requiredMode = Schema.RequiredMode.REQUIRED)
    String nome,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(description = "Endereço de email", example = "maria.silva@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    String email,
    
    @NotBlank(message = "Login é obrigatório")
    @Schema(description = "Login de acesso ao sistema", example = "maria.silva", requiredMode = Schema.RequiredMode.REQUIRED)
    String login,
    
    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha (mínimo 8 caracteres, incluindo maiúsculas, minúsculas, números e especiais)", example = "Senha@123", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    String senha,
    
    @NotBlank(message = "Logradouro é obrigatório")
    @Schema(description = "Nome da rua/avenida", example = "Rua das Flores", requiredMode = Schema.RequiredMode.REQUIRED)
    String logradouro,
    
    @NotBlank(message = "Número é obrigatório")
    @Schema(description = "Número do imóvel", example = "250", requiredMode = Schema.RequiredMode.REQUIRED)
    String numero,
    
    @Schema(description = "Complemento do endereço", example = "Apto 42")
    String complemento,
    
    @Schema(description = "Bairro", example = "Jardim Paulista")
    String bairro,
    
    @NotBlank(message = "Cidade é obrigatória")
    @Schema(description = "Nome da cidade", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
    String cidade,
    
    @NotBlank(message = "Estado é obrigatório")
    @Schema(description = "Sigla do estado (UF)", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
    String estado,
    
    @NotBlank(message = "CEP é obrigatório")
    @Schema(description = "CEP no formato XXXXX-XXX", example = "01414-001", requiredMode = Schema.RequiredMode.REQUIRED)
    String cep,
    
    @NotNull(message = "Tipo de usuário é obrigatório")
    @Schema(description = "Tipo do usuário (CLIENTE, ADMIN, etc.)", example = "CLIENTE", requiredMode = Schema.RequiredMode.REQUIRED)
    String tipoUsuario
) {}
