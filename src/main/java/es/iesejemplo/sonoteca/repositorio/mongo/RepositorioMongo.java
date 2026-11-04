package es.iesejemplo.sonoteca.repositorio.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import es.iesejemplo.sonoteca.modelo.*;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;
import org.bson.Document;

import java.util.List;

/**
 * UNIDAD 6 — Persistencia en MongoDB.
 * <p>
 * Antes de programar, completa el fichero {@code DISEÑO.md} del ejercicio 1
 * decidiendo qué datos embebes y cuáles referencias entre colecciones.
 */
public class RepositorioMongo implements RepositorioSonoTeca {

    private final MongoDatabase baseDeDatos;

    public RepositorioMongo() {
        // TODO: ajusta la cadena de conexión si usas Atlas en lugar de un servidor local.
        MongoClient cliente = MongoClients.create("mongodb://localhost:27017");
        this.baseDeDatos = cliente.getDatabase("sonoteca");
    }

    // ---------- Ejercicio 2: alta de datos ----------

    @Override
    public void altaCancion(Cancion cancion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 2)");
    }

    public void altaGenero(Genero genero) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 2)");
    }

    public void altaArtista(Artista artista) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 2)");
    }

    public void altaAlbum(Album album) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 2)");
    }

    /** Lee el sonoteca.json de la unidad 2 y da de alta todos los documentos correspondientes. */
    public void importarDesdeJson(String rutaFichero) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 2)");
    }

    // ---------- Ejercicio 3: consultas con filtros ----------

    public List<Cancion> cancionesDeGenero(String nombreGenero) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 3)");
    }

    public List<Album> albumesEntreAnios(int anioDesde, int anioHasta) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 3)");
    }

    public List<Cancion> cancionesDeArtista(String nombreArtista) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 3)");
    }

    @Override
    public List<Cancion> buscarCanciones(String texto) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    // ---------- Ejercicio 4: actualizar / borrar ----------

    public void corregirTitulo(String idCancion, String nuevoTitulo) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 4)");
    }

    public void marcarFavorita(String idCancion, boolean favorita) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 4)");
    }

    public void borrarCancion(String idCancion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 4)");
    }

    // ---------- Ejercicio 5: agregaciones ----------

    public List<Document> cancionesPorGenero() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 5)");
    }

    public List<Document> topArtistasPorNumeroDeAlbumes(int n) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 5)");
    }

    /** Solo necesario si en tu diseño "canciones" referencia a "albumes" en vez de embeberlo. */
    public List<Document> cancionesConDatosDeAlbum() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 5)");
    }

    // ---------- Ejercicio 6: validación con JSON Schema ----------

    /**
     * Crea (o recrea) la colección "canciones" con un validator de JSON Schema:
     * "título" (string, obligatorio) y "duracionSegundos" (int, obligatorio, mínimo 1).
     */
    public void configurarValidacionCanciones() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6, ejercicio 6)");
    }

    // ---------- Resto de la interfaz ----------

    @Override
    public List<Genero> listarGeneros() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public List<Artista> listarArtistas() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public List<Album> listarAlbumes() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public List<Cancion> listarCanciones() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public void actualizarCancion(Cancion cancion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public void eliminarCancion(int idCancion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public List<ListaReproduccion> listarListas() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public void altaLista(ListaReproduccion lista) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public void anyadirCancionALista(int idLista, int idCancion, int posicion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }

    @Override
    public List<Cancion> cancionesDeLista(int idLista) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 6)");
    }
}
