package es.iesejemplo.sonoteca.repositorio.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * UNIDAD 3 — Ejercicio 1.
 * <p>
 * Lee los datos de conexión de {@code config.properties} (en {@code src/main/resources})
 * y abre una conexión JDBC a la base de datos.
 */
public class ConexionBD {

    public static Connection obtenerConexion() throws SQLException {
        Properties propiedades = new Properties();
        try (InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encuentra config.properties en resources");
            }
            propiedades.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Error leyendo config.properties", e);
        }

        String url = propiedades.getProperty("bd.url");
        String usuario = propiedades.getProperty("bd.usuario");
        String password = propiedades.getProperty("bd.password");

        // TODO: comprueba que url/usuario/password se han leído correctamente y
        // devuelve la conexión con DriverManager.getConnection(url, usuario, password).
        throw new UnsupportedOperationException("TODO: implementar (unidad 3, ejercicio 1)");
    }
}
