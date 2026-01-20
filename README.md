# Tech Challenge Fase 02

Sistema de gerenciamento de restaurantes desenvolvido como parte do Tech Challenge da FIAP. O projeto implementa uma API REST completa para gestão de restaurantes, cardápios, usuários e tipos de usuário, utilizando Clean Architecture e boas práticas de desenvolvimento.

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Configuração](#configuração)
- [Documentação da API](#documentação-da-api)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Autenticação e Autorização](#autenticação-e-autorização)
- [Banco de Dados](#banco-de-dados)
- [Docker](#docker)

## Sobre o Projeto

O Tech Challenge Fase 02 é um sistema backend desenvolvido para gerenciar restaurantes e seus cardápios. O projeto permite o cadastro de restaurantes, itens de cardápio, usuários com diferentes perfis de acesso e implementa autenticação JWT para segurança das operações.

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação principal
- **Spring Boot 3.5.9**: Framework para desenvolvimento da aplicação
- **Spring Data JPA**: Persistência de dados
- **Spring Security**: Autenticação e autorização
- **JWT (JSON Web Token)**: Gerenciamento de tokens de autenticação
- **MySQL 8.0**: Banco de dados relacional (produção)
- **H2 Database**: Banco de dados em memória (desenvolvimento)
- **Lombok**: Redução de código boilerplate
- **Maven**: Gerenciamento de dependências e build
- **Docker**: Containerização da aplicação
- **SpringDoc OpenAPI**: Documentação automática da API
- **JUnit 5**: Framework de testes

## Arquitetura

O projeto segue os princípios da **Clean Architecture**, separando as responsabilidades em camadas:

### Core (Domínio)
- **domain**: Entidades de negócio (Restaurante, Usuario, CardapioItem, etc.)
- **usecases**: Casos de uso da aplicação
- **gateways**: Interfaces para comunicação com a camada de infraestrutura
- **exceptions**: Exceções de domínio
- **services**: Serviços de domínio

### Infraestrutura
- **controllers**: Controladores REST
- **dto**: Objetos de transferência de dados
- **persistence**: Entidades JPA e repositórios
- **mappers**: Conversão entre entidades de domínio e JPA
- **security**: Configurações de segurança e JWT
- **gateways**: Implementações das interfaces de gateway
- **config**: Configurações da aplicação

## Funcionalidades

### Gestão de Usuários
- Cadastro, atualização, consulta e remoção de usuários
- Diferentes tipos de usuário (Admin, Cliente, Dono de Restaurante)
- Validação de dados (email, senha, endereço)

### Gestão de Restaurantes
- Cadastro de restaurantes com informações completas
- Endereço, tipo de cozinha e horário de funcionamento
- Associação com dono do restaurante
- Listagem e busca de restaurantes

### Gestão de Cardápio
- Cadastro de itens de cardápio por restaurante
- Informações como nome, descrição, preço
- Indicação de disponibilidade (presencial ou delivery)
- Upload de fotos dos itens

### Autenticação e Autorização
- Sistema de login com JWT
- Proteção de endpoints por tipo de usuário
- Controle de acesso baseado em roles

### Tipos de Usuário
- Gerenciamento de perfis de acesso
- CRUD completo de tipos de usuário
- Tipos padrão: ADMIN, CLIENTE, DONO_RESTAURANTE

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Docker e Docker Compose (opcional, para ambiente containerizado)
- MySQL 8.0 (se não usar Docker)

## Instalação e Execução

### Execução com Docker Compose

1. Construa e inicie os containers:
```bash
docker-compose up -d
```

2. Verifique os logs:
```bash
docker-compose logs -f app
```

3. Para parar os containers:
```bash
docker-compose down
```

A aplicação estará disponível em `http://localhost:8080` e o MySQL na porta `3307`.


## Configuração

### Perfis de Aplicação

O projeto possui dois perfis configurados:

#### Default (H2)
- Banco de dados em memória
- Ideal para desenvolvimento local
- Console H2 disponível em `/h2-console`

#### MySQL
- Banco de dados MySQL
- Usado em produção e Docker
- Configurações em [application.yml](src/main/resources/application.yml)

### Variáveis de Ambiente

As principais configurações podem ser sobrescritas via variáveis de ambiente:

- `SPRING_PROFILES_ACTIVE`: Perfil ativo (default ou mysql)
- `SPRING_DATASOURCE_URL`: URL do banco de dados
- `SPRING_DATASOURCE_USERNAME`: Usuário do banco
- `SPRING_DATASOURCE_PASSWORD`: Senha do banco
- `JWT_SECRET`: Chave secreta para JWT
- `JWT_EXPIRATION`: Tempo de expiração do token (ms)
- `JAVA_OPTS`: Opções da JVM

## Documentação da API

### Swagger UI

A documentação interativa da API está disponível através do Swagger:

- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Endpoints Principais

#### Autenticação
- `POST /auth/login` - Autenticação de usuário

#### Usuários
- `POST /api/usuarios` - Cadastrar usuário
- `GET /api/usuarios` - Listar usuários
- `GET /api/usuarios/{id}` - Buscar usuário por ID
- `PUT /api/usuarios/{id}` - Atualizar usuário
- `DELETE /api/usuarios/{id}` - Deletar usuário

#### Restaurantes
- `POST /api/restaurantes` - Cadastrar restaurante
- `GET /api/restaurantes` - Listar restaurantes
- `GET /api/restaurantes/{id}` - Buscar restaurante por ID
- `PUT /api/restaurantes/{id}` - Atualizar restaurante
- `DELETE /api/restaurantes/{id}` - Deletar restaurante

#### Cardápio
- `POST /api/cardapio-itens` - Cadastrar item de cardápio
- `GET /api/cardapio-itens` - Listar itens
- `GET /api/cardapio-itens/{id}` - Buscar item por ID
- `GET /api/cardapio-itens/restaurante/{id}` - Listar itens por restaurante
- `PUT /api/cardapio-itens/{id}` - Atualizar item
- `DELETE /api/cardapio-itens/{id}` - Deletar item

#### Tipos de Usuário
- `POST /api/tipos-usuario` - Cadastrar tipo
- `GET /api/tipos-usuario` - Listar tipos
- `GET /api/tipos-usuario/{id}` - Buscar tipo por ID
- `PUT /api/tipos-usuario/{id}` - Atualizar tipo
- `DELETE /api/tipos-usuario/{id}` - Deletar tipo


## Testes

### Estrutura de Testes

- **Testes Unitários**: Localizados em `src/test/java`
- **Cobertura**: Testes para domain, usecases, controllers, mappers, gateways e services
- **Profile de Teste**: Configurado em `application-test.properties`

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/fiap/
│   │   ├── Main.java
│   │   ├── core/
│   │   │   ├── domain/              # Entidades de domínio
│   │   │   ├── exceptions/          # Exceções de negócio
│   │   │   ├── gateways/            # Interfaces de gateway
│   │   │   ├── services/            # Serviços de domínio
│   │   │   └── usecases/            # Casos de uso
│   │   │       ├── cardapio_item/
│   │   │       ├── restaurante/
│   │   │       ├── tipo_usuario/
│   │   │       └── usuario/
│   │   └── infra/
│   │       ├── config/              # Configurações
│   │       ├── controllers/         # Controllers REST
│   │       ├── dto/                 # DTOs
│   │       ├── gateways/            # Implementações de gateway
│   │       ├── mappers/             # Mapeadores
│   │       ├── persistence/         # JPA e repositórios
│   │       ├── security/            # Segurança e JWT
│   │       └── services/            # Serviços de infraestrutura
│   └── resources/
│       ├── application.yml          # Configurações da aplicação
│       └── data.sql                 # Dados iniciais
└── test/
    └── java/br/com/fiap/            # Testes unitários
```

## Autenticação e Autorização

### JWT Token

O sistema utiliza JWT (JSON Web Token) para autenticação:

1. Faça login através do endpoint `/auth/login`
2. Receba o token JWT na resposta
3. Inclua o token no header das requisições:
   ```
   Authorization: Bearer {seu-token}
   ```

### Usuário Padrão

Um usuário administrador é criado automaticamente:
- **Login**: admin
- **Senha**: admin
- **Tipo**: ADMIN

### Fluxo de Autenticação

1. Cliente envia credenciais para `/auth/login`
2. Sistema valida as credenciais
3. Sistema gera token JWT com duração de 24 horas
4. Cliente armazena o token
5. Cliente inclui o token em requisições subsequentes
6. Sistema valida o token antes de processar requisições

## Banco de Dados

### Modelo de Dados

O sistema possui as seguintes entidades principais:

- **usuarios**: Dados dos usuários do sistema
- **tipos_usuario**: Perfis de acesso
- **enderecos**: Endereços de usuários e restaurantes
- **restaurantes**: Dados dos restaurantes
- **cardapio_itens**: Itens do cardápio dos restaurantes

### Inicialização

O arquivo [data.sql](src/main/resources/data.sql) contém dados iniciais:
- Tipos de usuário padrão (ADMIN, CLIENTE, DONO_RESTAURANTE)
- Usuário administrador
- Endereço padrão

### Migrations

O Hibernate está configurado com `ddl-auto: update`, criando e atualizando automaticamente as tabelas do banco.

## Docker

### Arquitetura Docker

O projeto utiliza multi-stage build:

1. **Stage Build**: Compilação com Maven
2. **Stage Runtime**: Execução com JRE Alpine (imagem otimizada)

### Containers

- **mysql**: Banco de dados MySQL 8.0
- **app**: Aplicação Spring Boot

### Healthchecks

Ambos os containers possuem healthchecks configurados:
- MySQL: Ping no banco de dados
- App: Verificação do endpoint `/actuator/health`

### Volumes

- `mysql-data`: Persistência dos dados do MySQL

### Network

- `tech-challenge-network`: Rede bridge para comunicação entre containers

### Comandos Úteis

```bash
# Iniciar ambiente
docker-compose up -d

# Ver logs em tempo real
docker-compose logs -f

# Parar ambiente
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Reconstruir imagens
docker-compose build --no-cache

# Reiniciar apenas a aplicação
docker-compose restart app
```

## Monitoramento

A aplicação expõe endpoints do Spring Boot Actuator para monitoramento:

- `/actuator/health` - Status da aplicação
- `/actuator/info` - Informações da aplicação

## Autores

- Filipe Ferreira ([gf-filipe](https://github.com/gf-filipe))
- Lucas Escolástico ([lucasescol](https://github.com/lucasescol))
- Leandro Gonçalves ([CabeloSG](https://github.com/CabeloSG))