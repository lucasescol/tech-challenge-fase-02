-- Inserir tipos de usuário padrões (apenas se não existirem)
INSERT INTO tipo_usuario (nome, descricao) 
SELECT 'ADMIN', 'Administrador do sistema' 
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'ADMIN');

INSERT INTO tipo_usuario (nome, descricao) 
SELECT 'CLIENTE', 'Cliente comum do sistema' 
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'CLIENTE');

INSERT INTO tipo_usuario (nome, descricao) 
SELECT 'DONO_RESTAURANTE', 'Dono de restaurante' 
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'DONO_RESTAURANTE');

-- Inserir endereço do administrador (apenas se não existir)
INSERT INTO enderecos (logradouro, numero, complemento, bairro, cidade, estado, cep) 
SELECT 'Av. Paulista', '1000', 'Sala 100', 'Bela Vista', 'São Paulo', 'SP', '01310-100'
WHERE NOT EXISTS (SELECT 1 FROM enderecos WHERE logradouro = 'Av. Paulista' AND numero = '1000');

-- Inserir usuário administrador padrão (apenas se não existir)
-- Login: admin | Senha: admin
INSERT INTO usuarios (nome, email, login, senha, endereco_id, tipo_usuario_id, created_at, updated_at) 
SELECT 
    'Administrador', 
    'admin@sistema.com', 
    'admin', 
    '$2a$10$cQ7BCgRchAwg49pyHdI/FOA44Aaug/F.nG4EJwbqXpzxZ0Q9NjvF.',
    (SELECT id FROM enderecos WHERE logradouro = 'Av. Paulista' AND numero = '1000' LIMIT 1),
    (SELECT id FROM tipo_usuario WHERE nome = 'ADMIN' LIMIT 1),
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE login = 'admin');


