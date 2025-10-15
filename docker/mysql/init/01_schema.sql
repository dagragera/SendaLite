-- Schema básico SendaLite (MySQL 8)

CREATE TABLE IF NOT EXISTS usuario (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    hash_password VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS ruta (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    titulo VARCHAR(100) NOT NULL,
    resumen TEXT,
    dificultad ENUM('F','M','D') NOT NULL,
    distancia_km DECIMAL(6,2) NOT NULL,
    desnivel_m INT,
    tiempo_estimado_min INT,
    tipo_actividad VARCHAR(50),
    geojson MEDIUMTEXT, -- o lat/lon si no usáis trazado
    autor_id BIGINT NOT NULL,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    puntuacion_media_cache DECIMAL(4,2),
    CONSTRAINT fk_ruta_autor FOREIGN KEY (autor_id) REFERENCES usuario(id)
    );

CREATE TABLE IF NOT EXISTS valoracion (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          usuario_id BIGINT NOT NULL,
                                          ruta_id BIGINT NOT NULL,
                                          valor TINYINT NOT NULL CHECK (valor BETWEEN 1 AND 10),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_valor UNIQUE (usuario_id, ruta_id),
    CONSTRAINT fk_valor_user FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_valor_ruta FOREIGN KEY (ruta_id) REFERENCES ruta(id)
    );

CREATE TABLE IF NOT EXISTS favorito (
                                        usuario_id BIGINT NOT NULL,
                                        ruta_id BIGINT NOT NULL,
                                        creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        PRIMARY KEY (usuario_id, ruta_id),
    CONSTRAINT fk_fav_user FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_fav_ruta FOREIGN KEY (ruta_id) REFERENCES ruta(id)
    );

CREATE TABLE IF NOT EXISTS etiqueta (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        nombre VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS ruta_etiqueta (
                                             ruta_id BIGINT NOT NULL,
                                             etiqueta_id BIGINT NOT NULL,
                                             PRIMARY KEY (ruta_id, etiqueta_id),
    CONSTRAINT fk_re_ruta FOREIGN KEY (ruta_id) REFERENCES ruta(id),
    CONSTRAINT fk_re_tag FOREIGN KEY (etiqueta_id) REFERENCES etiqueta(id)
    );
