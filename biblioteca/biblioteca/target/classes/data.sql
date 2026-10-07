-- ADMIN y BIBLIOTECARIO: Admin123*  (el LECTOR se registra por /auth/register)
INSERT INTO usuario (nombre, email, password, estado, rol) VALUES
 ('Administrador', 'admin@biblioteca.com', '$2a$10$6QjFEozCMbCwNQVYsNlFW.NW/aE01TOQV6iU840srbU2UIAqOA9m.', 'ACTIVO', 'ADMIN'),
 ('Bibliotecario', 'biblio@biblioteca.com', '$2a$10$6QjFEozCMbCwNQVYsNlFW.NW/aE01TOQV6iU840srbU2UIAqOA9m.', 'ACTIVO', 'BIBLIOTECARIO')
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password, rol = EXCLUDED.rol;
