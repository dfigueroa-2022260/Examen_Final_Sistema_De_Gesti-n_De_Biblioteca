-- Password de los 3 usuarios: Admin123!
INSERT INTO usuario (nombre, email, password, estado, rol) VALUES
 ('Administrador', 'admin@biblioteca.com', '$2a$10$mvPo0KjViSeC4iStecpKsOL2C3mJy1q9Cm4ibHP3Vg/A6660Ngjo6', 'ACTIVO', 'ADMIN'),
 ('Bibliotecario', 'biblio@biblioteca.com', '$2a$10$mvPo0KjViSeC4iStecpKsOL2C3mJy1q9Cm4ibHP3Vg/A6660Ngjo6', 'ACTIVO', 'BIBLIOTECARIO'),
 ('Lector Demo', 'lector@biblioteca.com', '$2a$10$mvPo0KjViSeC4iStecpKsOL2C3mJy1q9Cm4ibHP3Vg/A6660Ngjo6', 'ACTIVO', 'LECTOR')
ON CONFLICT (email) DO NOTHING;
