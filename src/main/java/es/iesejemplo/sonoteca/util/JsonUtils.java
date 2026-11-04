package es.iesejemplo.sonoteca.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.iesejemplo.sonoteca.modelo.Biblioteca;
import es.iesejemplo.sonoteca.modelo.Cancion;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * UNIDAD 1 - EJERCICIO 3 (canciones sueltas) y UNIDAD 2 - EJERCICIO 4 (biblioteca completa).
 * <p>
 * Utilidades de conversión entre los objetos del modelo y JSON, usando Gson.
 */
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtils() {
        // clase de utilidades: no sé instancia
    }

    // ----- Unidad 1: una canción suelta -----

    public static String exportarCancion(Cancion cancion) {
        // DONE: implementar con Gson (unidad 1, ejercicio 3).
        return GSON.toJson(cancion);
    }

    public static Cancion importarCancion(String json) {
        // DONE: implementar con Gson (unidad 1, ejercicio 3).
        return GSON.fromJson(json, Cancion.class);
    }

    public static String exportarLista(List<Cancion> canciones) {
        // DONE: implementar con Gson (unidad 1, ejercicio 3).
        return GSON.toJson(canciones);
    }

    public static List<Cancion> importarLista(String json) {
        // DONE: implementar con Gson, usando un TypeToken para conservar el tipo genérico (unidad 1, ejercicio 3).
        Type tipoLista = new TypeToken<List<Cancion>>(){}.getType();
        return GSON.fromJson(json, tipoLista);
    }

    // ----- Unidad 2: la biblioteca completa -----

    /**
     * Escribe la biblioteca completa en un fichero de texto en formato JSON legible.
     */
    // DONE: implementar (usar GSON.toJson(...) y un PrintWriter/FileWriter) (unidad 2, ejercicio 4).
    public static void exportarBibliotecaJson(Biblioteca biblioteca, String rutaFichero) throws IOException {
        Path rutaAbsoluta = Path.of(rutaFichero).toAbsolutePath();
        if (rutaAbsoluta.getParent() != null && Files.notExists(rutaAbsoluta.getParent())) {
            Files.createDirectories(rutaAbsoluta.getParent());
        }

        try (BufferedWriter escritor = Files.newBufferedWriter(rutaAbsoluta, StandardCharsets.UTF_8)) {
            GSON.toJson(biblioteca, escritor);
        }
    }

    /**
     * Lee un fichero JSON con una biblioteca completa y la reconstruye.
     */
    // DONE: implementar (leer el fichero completo y usar GSON.fromJson(...)) (unidad 2, ejercicio 4).
    public static Biblioteca importarBibliotecaJson(String rutaFichero) throws IOException {
        Path rutaAbsoluta = Path.of(rutaFichero).toAbsolutePath();
        if (Files.notExists(rutaAbsoluta)) return new Biblioteca();
        try (BufferedReader lector = Files.newBufferedReader(rutaAbsoluta, StandardCharsets.UTF_8)) {
            Biblioteca biblioteca = GSON.fromJson(lector, Biblioteca.class);
            return biblioteca != null ? biblioteca : new Biblioteca();
        }
    }
}
