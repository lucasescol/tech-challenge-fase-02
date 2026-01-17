package br.com.fiap.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearer-jwt";
        
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Restaurantes")
                        .version("1.0.0")
                        .description("""
                                # API REST - Sistema de Gerenciamento de Restaurantes
                                
                                Esta API permite gerenciar restaurantes, usuários e tipos de usuário com autenticação JWT.
                                
                                ## Funcionalidades Principais
                                
                                ### Restaurantes
                                - Cadastrar novos restaurantes com endereço completo e tipo de cozinha
                                - Buscar restaurantes por ID
                                - Listar todos os restaurantes cadastrados
                                - Atualizar dados de restaurantes existentes
                                - Remover restaurantes do sistema
                                
                                ### Usuários
                                - Cadastrar usuários com diferentes tipos (CLIENTE, ADMIN, etc.)
                                - Buscar usuários por nome (busca parcial)
                                - Atualizar dados pessoais e endereço
                                - Trocar senha de forma segura
                                - Excluir usuários
                                
                                ### Tipos de Usuário
                                - Gerenciar tipos de usuário (CLIENTE, ADMIN, FUNCIONÁRIO, etc.)
                                - Criar, listar, atualizar e remover tipos
                                
                                ## Autenticação
                                
                                A API utiliza **JWT (JSON Web Token)** para autenticação.
                                
                                ### Endpoints Públicos (não requerem autenticação):
                                - `POST /auth/login` - Realizar login
                                - `POST /api/usuarios` - Cadastrar novo usuário (auto-registro)
                                
                                ### Endpoints Protegidos (requerem autenticação):
                                Todos os demais endpoints necessitam de autenticação JWT.
                                
                                ### Como autenticar:
                                
                                1. Faça login em `/auth/login` com suas credenciais
                                2. Copie o token retornado
                                3. Clique no botão **Authorize** no topo da página
                                4. Cole o token (apenas o valor, sem "Bearer")
                                5. Clique em **Authorize**
                                6. Agora você pode acessar os endpoints protegidos
                                
                                ## Tipos de Cozinha Disponíveis
                                - ITALIANA
                                - JAPONESA
                                - BRASILEIRA
                                - MEXICANA
                                - CHINESA
                                - FRANCESA
                                - INDIANA
                                - ARABE
                                - FUSION
                                - VEGETARIANA
                                - VEGANA
                                
                                ## Formato de Endereço
                                Todos os endpoints que trabalham com endereço seguem o padrão brasileiro:
                                - **Logradouro**: Nome da rua/avenida
                                - **Número**: Número do imóvel
                                - **Complemento**: Apartamento, sala, etc. (opcional)
                                - **Bairro**: Nome do bairro
                                - **Cidade**: Nome da cidade
                                - **Estado**: Sigla do estado (2 letras)
                                - **CEP**: Formato XXXXX-XXX
                                
                                ## Códigos de Status HTTP
                                - **200 OK**: Requisição bem-sucedida
                                - **201 Created**: Recurso criado com sucesso
                                - **204 No Content**: Operação bem-sucedida sem retorno de dados
                                - **400 Bad Request**: Dados inválidos ou violação de regras de negócio
                                - **401 Unauthorized**: Não autenticado ou token inválido
                                - **404 Not Found**: Recurso não encontrado
                                - **500 Internal Server Error**: Erro interno do servidor
                                """)
                        .contact(new Contact()
                                .name("FIAP Tech Challenge - Fase 02")
                                .email("contato@fiap.com.br")
                                .url("https://fiap.com.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento Local")
                ))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("**Autenticação JWT**\n\n" +
                                        "Insira o token JWT obtido através do endpoint `/auth/login`.\n\n" +
                                        "**Formato**: `seu-token-jwt-aqui` (sem o prefixo 'Bearer')\n\n" +
                                        "O token é válido por **24 horas** após a geração.")));
    }
}
