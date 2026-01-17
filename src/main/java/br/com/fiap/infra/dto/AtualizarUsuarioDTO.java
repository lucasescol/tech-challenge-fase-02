package br.com.fiap.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Dados para atualização de um usuário")
public record AtualizarUsuarioDTO(
    @Schema(description = "Nome completo do usuário", example = "Maria Silva Santos")
    String nome,
    
    @Email(message = "Email deve ser válido")
    @Schema(description = "Endereço de email", example = "maria.silva@email.com")
    String email,
    
    @Schema(description = "Login de acesso ao sistema", example = "maria.silva")
    String login,
    
    @Schema(description = "Nome da rua/avenida", example = "Rua das Flores")
    String logradouro,
    
    @Schema(description = "Número do imóvel", example = "250")
    String numero,
    
    @Schema(description = "Complemento do endereço", example = "Apto 42")
    String complemento,
    
    @Schema(description = "Bairro", example = "Jardim Paulista")
    String bairro,
    
    @Schema(description = "Nome da cidade", example = "São Paulo")
    String cidade,
    
    @Schema(description = "Sigla do estado (UF)", example = "SP")
    String estado,
    
    @Schema(description = "CEP no formato XXXXX-XXX", example = "01414-001")
    String cep
) {}
