package es.iesejemplo.sonoteca.repositorio.jdbc;

import es.iesejemplo.sonoteca.modelo.*;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        // TODO: consulta que combine canciones + álbumes + artistas + géneros
        // (con JOIN en el SQL, o con varias consultas encadenadas).
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 3)");
    }

    /**
     * Igual que {@link #listarCanciones()}, pero devolviendo directamente la
     * información aplanada en {@link CancionCompleta} (título de la canción,
     * álbum, artista, género y año), tal como pide el ejercicio 3.
     */
    public List<CancionCompleta> listarCancionesCompletas() {
        // TODO: implementar con una consulta JOIN (o varias consultas encadenadas),
        // construyendo un CancionCompleta por cada fila del resultado.
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 3)");
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
