package edu.marhuendasamuel.sonoteca.repositorio.jdbc;

import edu.marhuendasamuel.sonoteca.modelo.*;
import edu.marhuendasamuel.sonoteca.repositorio.RepositorioSonoTeca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UNIDAD 3 — Persistencia en base de datos relacional con JDBC.
 * <p>
 * Antes de nada, ejecuta el script {@code sonoteca.sql} proporcionado sobre tu
 * gestor (MariaDB/PostgreSQL) para crear el esquema.
 * <p>
 * Recuerda: usa siempre {@code PreparedStatement}, nunca concatenación de
 * cadenas para construir el SQL con datos introducidos por el usuario.
 */
public class RepositorioJDBC implements RepositorioSonoTeca {

    // region COMPROBACIONES DE ELEMENTOS EN BBDD

    private int obtenerOCrearGenero(Connection conexion, String nombre) throws SQLException {
        String consulta = "SELECT id FROM generos WHERE LOWER(nombre) = LOWER(?)";
        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        String insercion = "INSERT INTO generos (nombre) VALUES (?) RETURNING id";
        try (PreparedStatement ps = conexion.prepareStatement(insercion)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }

    private int obtenerOCrearArtista(Connection conexion, String nombre, String nacionalidad) throws SQLException {
        String consulta = "SELECT id FROM artistas WHERE LOWER(nombre) = LOWER(?)";
        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        String insercion = "INSERT INTO artistas (nombre, nacionalidad) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conexion.prepareStatement(insercion)) {
            ps.setString(1, nombre);
            ps.setString(2, nacionalidad);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }

    private int obtenerOCrearAlbum(Connection conexion, String titulo, int anyo, int idArtista, int idGenero) throws SQLException {
        String consulta = "SELECT id FROM albumes WHERE LOWER(titulo) = LOWER(?) AND artista_id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, titulo);
            ps.setInt(2, idArtista);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        String insercion = "INSERT INTO albumes (titulo, anio, artista_id, genero_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conexion.prepareStatement(insercion)) {
            ps.setString(1, titulo);
            ps.setInt(2, anyo);
            ps.setInt(3, idArtista);
            ps.setInt(4, idGenero);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }

    // endregion


    // ---------- Ejercicio 2: alta en cascada ----------

    @Override
    public void altaCancion(Cancion cancion) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                Album albumCancion = cancion.getAlbum();
                Genero generoCancion = albumCancion.getGenero();
                Artista artistaCancion = albumCancion.getArtista();

                int idGenero = obtenerOCrearGenero(conexion, generoCancion.getNombre());
                int idArtista = obtenerOCrearArtista(conexion, artistaCancion.getNombre(), artistaCancion.getNacionalidad());
                int idAlbum = obtenerOCrearAlbum(conexion, albumCancion.getTitulo(), albumCancion.getAnio(), idArtista, idGenero);

                String insercion = "INSERT INTO canciones (titulo, numero_pista, duracion_segundos, album_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conexion.prepareStatement(insercion)) {
                    ps.setString(1, cancion.getTitulo());
                    ps.setInt(2, cancion.getNumeroPista());
                    ps.setInt(3, cancion.getDuracionSegundos());
                    ps.setInt(4, idAlbum);
                    ps.executeUpdate();
                }
                conexion.commit();
            } catch (SQLException error) {
                conexion.rollback();
                System.out.println("Error en el alta de canción: " + error.getMessage());
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error al dar de alta la canción: " + error);
        }
    }

    // ---------- Ejercicio 3: listar con datos relacionados ----------

    @Override
    public List<Cancion> listarCanciones() {
        String sql = """
            SELECT
                c.id AS cancion_id,
                c.titulo AS cancion_titulo,
                c.duracion_segundos,
                c.numero_pista,
                al.id AS album_id,
                al.titulo AS album_titulo,
                al.anio AS album_anio,
                ar.id AS artista_id,
                ar.nombre AS artista_nombre,
                ar.nacionalidad AS artista_nacionalidad,
                g.id AS genero_id,
                g.nombre AS genero_nombre
            FROM canciones c
            JOIN albumes al ON c.album_id = al.id
            JOIN artistas ar ON al.artista_id = ar.id
            JOIN generos g ON al.genero_id = g.id
            ORDER BY c.titulo
            """;
        List<Cancion> canciones = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                canciones.add(mapearCancion(rs));
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error al listar las canciones", error);
        }

        return canciones;
    }

    /* Convierte la fila actual del ResultSet en un objeto del modelo, con su álbum completo. */
    private Cancion mapearCancion(ResultSet rs) throws SQLException {
        Genero genero = new Genero();
        genero.setId(rs.getInt("genero_id"));
        genero.setNombre(rs.getString("genero_nombre"));

        Artista artista = new Artista();
        artista.setId(rs.getInt("artista_id"));
        artista.setNombre(rs.getString("artista_nombre"));
        artista.setNacionalidad(rs.getString("artista_nacionalidad"));

        Album album = new Album();
        album.setId(rs.getInt("album_id"));
        album.setTitulo(rs.getString("album_titulo"));
        int anio = rs.getInt("album_anio");
        if (!rs.wasNull()) {
            album.setAnio(anio);
        }
        album.setArtista(artista);
        album.setGenero(genero);

        Cancion cancion = new Cancion();
        cancion.setId(rs.getInt("cancion_id"));
        cancion.setTitulo(rs.getString("cancion_titulo"));
        cancion.setDuracionSegundos(rs.getInt("duracion_segundos"));
        int numeroPista = rs.getInt("numero_pista");
        if (!rs.wasNull()) {
            cancion.setNumeroPista(numeroPista);
        }
        cancion.setAlbum(album);

        return cancion;
    }

    /**
     * Igual que {@link #listarCanciones()}, pero devolviendo directamente la
     * información aplanada en {@link CancionCompleta} (título de la canción,
     * álbum, artista, género y año), tal como pide el ejercicio 3.
     */
    // DONE: implementar con una consulta JOIN (o varias consultas encadenadas),
    public List<CancionCompleta> listarCancionesCompletas() {
        String sql = """
            SELECT
                c.titulo AS titulo_cancion,
                al.titulo AS titulo_album,
                ar.nombre AS nombre_artista,
                g.nombre AS nombre_genero,
                al.anio AS anio
            FROM canciones c
            JOIN albumes al ON c.album_id = al.id
            JOIN artistas ar ON al.artista_id = ar.id
            JOIN generos g ON al.genero_id = g.id
            ORDER BY ar.nombre, al.anio, c.numero_pista
            """;

        List<CancionCompleta> cancionesCompletas = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tituloCancion = rs.getString("titulo_cancion");
                String tituloAlbum = rs.getString("titulo_album");
                String nombreArtista = rs.getString("nombre_artista");
                String nombreGenero = rs.getString("nombre_genero");
                int anio = rs.getInt("anio");
                if (rs.wasNull()) {
                    anio = 0;
                }

                cancionesCompletas.add(new CancionCompleta(
                        tituloCancion,
                        tituloAlbum,
                        nombreArtista,
                        nombreGenero,
                        anio
                ));
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error al listar las canciones completas", error);
        }

        return cancionesCompletas;
    }

    // ---------- Ejercicio 4: búsqueda parametrizada ----------

    @Override
    public List<Cancion> buscarCanciones(String texto) {
        // TODO: PreparedStatement con LIKE sobre título/artista/álbum.
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 4)");
    }

    // ---------- Ejercicio 5: actualizar / eliminar ----------

    @Override
    public void actualizarCancion(Cancion cancion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 5)");
    }

    @Override
    public void eliminarCancion(int idCancion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 5)");
    }

    /**
     * Elimina un álbum. Si tiene canciones asociadas, la base de datos rechazará
     * el borrado por la restricción de clave ajena: captura ese error y muestra
     * un mensaje claro en lugar de dejar que se propague la excepción SQL en crudo.
     */
    public void eliminarAlbum(int idAlbum) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 5)");
    }

    // ---------- Ejercicio 6: importación masiva con transacción ----------

    /**
     * Importa una biblioteca completa desde un fichero JSON (unidad 1/2) dentro
     * de una única transacción: usa executeBatch() para cada tabla y, si algo
     * falla, deshaz toda la importación con rollback().
     */
    public void importarDesdeJson(String rutaFichero) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 6)");
    }

    // ---------- Resto de la interfaz ----------

    @Override
    public List<Genero> listarGeneros() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3)");
    }

    @Override
    public List<Artista> listarArtistas() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3)");
    }

    @Override
    public List<Album> listarAlbumes() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3)");
    }

    @Override
    public List<ListaReproduccion> listarListas() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3)");
    }

    @Override
    public void altaLista(ListaReproduccion lista) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3)");
    }

    @Override
    public void anyadirCancionALista(int idLista, int idCancion, int posicion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3)");
    }

    @Override
    public List<Cancion> cancionesDeLista(int idLista) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 3)");
    }
}
