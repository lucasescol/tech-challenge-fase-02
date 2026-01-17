-- Inserir tipos de usuário padrões
INSERT INTO tipo_usuario (nome, descricao) VALUES ('ADMIN', 'Administrador do sistema');
INSERT INTO tipo_usuario (nome, descricao) VALUES ('CLIENTE', 'Cliente comum do sistema');
INSERT INTO tipo_usuario (nome, descricao) VALUES ('RESTAURANTE', 'Dono de restaurante');

-- Inserir endereço do administrador
INSERT INTO enderecos (logradouro, numero, complemento, bairro, cidade, estado, cep) 
VALUES ('Av. Paulista', '1000', 'Sala 100', 'Bela Vista', 'São Paulo', 'SP', '01310-100');

-- Inserir usuário administrador padrão
-- Login: admin | Senha: admin
INSERT INTO usuarios (nome, email, login, senha, endereco_id, tipo_usuario_id, created_at, updated_at) 
VALUES (
    'Administrador', 
    'admin@sistema.com', 
    'admin', 
    '$2a$10$cQ7BCgRchAwg49pyHdI/FOA44Aaug/F.nG4EJwbqXpzxZ0Q9NjvF.',
    1,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);


