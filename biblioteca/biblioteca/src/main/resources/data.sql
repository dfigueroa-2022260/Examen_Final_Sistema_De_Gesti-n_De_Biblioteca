-- ADMIN y BIBLIOTECARIO: Admin123*  |  LECTOR: Lector123*
INSERT INTO usuario (nombre, email, password, estado, rol) VALUES
 ('Administrador', 'admin@biblioteca.com', '$2a$10$6QjFEozCMbCwNQVYsNlFW.NW/aE01TOQV6iU840srbU2UIAqOA9m.', 'ACTIVO', 'ADMIN'),
 ('Bibliotecario', 'biblio@biblioteca.com', '$2a$10$6QjFEozCMbCwNQVYsNlFW.NW/aE01TOQV6iU840srbU2UIAqOA9m.', 'ACTIVO', 'BIBLIOTECARIO'),
 ('Lector Demo', 'lector@biblioteca.com', '$2a$10$jWC6IF2u0phG6.Ni9VLE1OQ1hGwYxLyRQrmVZtlaZJyxBhDPA67Gq', 'ACTIVO', 'LECTOR')
ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password, rol = EXCLUDED.rol;
