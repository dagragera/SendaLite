-- Datos de ejemplo SendaLite (MySQL 8)
-- Inserta información básica para pruebas
USE sendalite;

-- Usuarios
INSERT INTO usuario (email, password, nombre, avatar, fecha_registro, activo)
VALUES
    ('david@example.com', 'hash123', 'David Gragera', NULL, '2023-01-10', TRUE),
    ('shunya@example.com', 'hash456', 'Shunya Zhan', NULL, '2023-02-15', TRUE),
    ('laura@example.com', 'hash789', 'Laura Gómez', NULL, '2023-03-20', TRUE);

-- Rutas
INSERT INTO ruta (id_autor, titulo, descripcion, dificultad, distancia_km, desnivel_m, tiempo_estimado_min, tipo_actividad, ubicacion, coordenadas, fotos, etiquetas, fecha_creacion, fecha_actualizacion, activa)
VALUES
    (1, 'Sendero del Águila', 'Ruta sencilla con vistas al valle.', 'FACIL', 5.2, 120, 90, 'SENDERISMO', 'Valle del Águila', '40.123,-5.123', NULL, 'mirador', '2023-04-01', NULL, TRUE),
    (2, 'Cima del Dragón', 'Ascenso exigente con vistas espectaculares.', 'DIFICIL', 13.4, 900, 300, 'ESCALADA', 'Sierra del Dragón', '40.456,-5.456', NULL, 'montaña', '2023-04-10', NULL, TRUE),
    (3, 'Bosque Encantado', 'Camino entre árboles y pequeños arroyos.', 'MEDIA', 8.1, 350, 180, 'SENDERISMO', 'Bosque Encantado', '40.789,-5.789', NULL, 'bosque', '2023-04-15', NULL, TRUE);

-- Comentarios
INSERT INTO comentario (id_usuario, id_ruta, texto, fecha_comentario, fecha_edicion)
VALUES
    (1, 1, '¡Preciosa ruta, muy recomendable!', '2023-04-02', NULL),
    (2, 2, 'Difícil pero merece la pena por las vistas.', '2023-04-11', NULL),
    (3, 3, 'Ideal para ir en familia.', '2023-04-16', NULL);

-- Valoraciones
INSERT INTO valoracion (id_usuario, id_ruta, puntuacion, fecha_valoracion)
VALUES
    (1, 1, 8, '2023-04-02'),
    (2, 1, 9, '2023-04-03'),
    (3, 2, 10, '2023-04-11'),
    (1, 3, 7, '2023-04-16');

-- Ejemplo de favoritos (si decides usar esta tabla)
-- CREATE TABLE favorito (
--     id_usuario BIGINT NOT NULL,
--     id_ruta BIGINT NOT NULL,
--     creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     PRIMARY KEY (id_usuario, id_ruta),
--     FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
--     FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta)
-- );
-- INSERT INTO favorito (id_usuario, id_ruta)
-- VALUES
--     (1, 2),
--     (2, 1);

-- Ejemplo de etiquetas y asociación ruta-etiqueta (si decides usar estas tablas)
-- CREATE TABLE etiqueta (
--     id_etiqueta BIGINT PRIMARY KEY AUTO_INCREMENT,
--     nombre VARCHAR(50) NOT NULL UNIQUE
-- );
-- CREATE TABLE ruta_etiqueta (
--     id_ruta BIGINT NOT NULL,
--     id_etiqueta BIGINT NOT NULL,
--     PRIMARY KEY (id_ruta, id_etiqueta),
--     FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta),
--     FOREIGN KEY (id_etiqueta) REFERENCES etiqueta(id_etiqueta)
-- );
-- INSERT INTO etiqueta (nombre)
-- VALUES
--     ('montaña'),
--     ('bosque'),
--     ('mirador');
-- INSERT INTO ruta_etiqueta (id_ruta, id_etiqueta)
-- VALUES
--     (1, 3),
--     (2, 1),
--     (3, 2);
