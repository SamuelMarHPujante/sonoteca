-- Esquema de la base de datos SonoTeca (unidad 3).
-- Sintaxis válida para MariaDB / MySQL. Si usas PostgreSQL, cambia AUTO_INCREMENT
-- por un tipo SERIAL/IDENTITY equivalente.

CREATE DATABASE IF NOT EXISTS sonoteca CHARACTER SET utf8mb4;
USE sonoteca;

CREATE TABLE generos (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE artistas (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    nacionalidad  VARCHAR(50)
);

CREATE TABLE albumes (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR(150) NOT NULL,
    anio        INT,
    artista_id  INT NOT NULL,
    genero_id   INT NOT NULL,
    CONSTRAINT fk_album_artista FOREIGN KEY (artista_id) REFERENCES artistas(id),
    CONSTRAINT fk_album_genero  FOREIGN KEY (genero_id)  REFERENCES generos(id)
);

CREATE TABLE canciones (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    titulo             VARCHAR(150) NOT NULL,
    duracion_segundos  INT NOT NULL,
    numero_pista       INT,
    album_id           INT NOT NULL,
    CONSTRAINT fk_cancion_album FOREIGN KEY (album_id) REFERENCES albumes(id)
);

-- Unidad 4 (Hibernate): tablas para las listas de reproducción y la relación M:N con datos adicionales.
CREATE TABLE listas_reproduccion (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE lista_cancion (
    lista_id   INT NOT NULL,
    cancion_id INT NOT NULL,
    posicion   INT NOT NULL,
    PRIMARY KEY (lista_id, cancion_id),
    CONSTRAINT fk_lc_lista   FOREIGN KEY (lista_id)   REFERENCES listas_reproduccion(id),
    CONSTRAINT fk_lc_cancion FOREIGN KEY (cancion_id) REFERENCES canciones(id)
);
