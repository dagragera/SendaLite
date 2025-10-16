-- Datos de ejemplo SendaLite (MySQL 8)
-- Inserta información básica para pruebas
USE sendalite;

-- Usuarios
INSERT INTO usuario (nombre, email, hash_password, avatar_url)
VALUES
    ('David Gragera', 'david@example.com', 'hash123', NULL),
    ('Shunya Zhan', 'shunya@example.com', 'hash456', NULL),
    ('Laura Gómez', 'laura@example.com', 'hash789', NULL);

-- Rutas
INSERT INTO ruta (titulo, resumen, dificultad, distancia_km, desnivel_m, tiempo_estimado_min, tipo_actividad, autor_id)
VALUES
    ('Sendero del Águila', 'Ruta sencilla con vistas al valle.', 'F', 5.2, 120, 90, 'Senderismo', 1),
    ('Cima del Dragón', 'Ascenso exigente con vistas espectaculares.', 'D', 13.4, 900, 300, 'Montañismo', 2),
    ('Bosque Encantado', 'Camino entre árboles y pequeños arroyos.', 'M', 8.1, 350, 180, 'Trekking', 3);

-- Valoraciones
INSERT INTO valoracion (usuario_id, ruta_id, valor)
VALUES
    (1, 1, 8),
    (2, 1, 9),
    (3, 2, 10),
    (1, 3, 7);

-- Favoritos
INSERT INTO favorito (usuario_id, ruta_id)
VALUES
    (1, 2),
    (2, 1);

-- Etiquetas
INSERT INTO etiqueta (nombre)
VALUES
    ('montaña'),
    ('bosque'),
    ('mirador');

-- Asociación ruta-etiqueta
INSERT INTO ruta_etiqueta (ruta_id, etiqueta_id)
VALUES
    (1, 3),
    (2, 1),
    (3, 2);
