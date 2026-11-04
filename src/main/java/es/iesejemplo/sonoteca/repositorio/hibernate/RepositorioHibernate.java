package es.iesejemplo.sonoteca.repositorio.hibernate;

import es.iesejemplo.sonoteca.modelo.*;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import java.util.List;

/**
 * UNIDAD 4 — Persistencia con Hibernate.
 * <p>
 * Antes de empezar: completa las anotaciones JPA de las clases del paquete
 * {@code modelo} (Genero, Artista, Album, Cancion, ListaReproduccion) y de
 * {@link ListaCancion}/{@link ListaCancionId} (ejercicio 4), y revisa
 * {@code hibernate.cfg.xml}.
 */
public class RepositorioHibernate implements RepositorioSonoTeca {

    // ---------- Ejercicio 2: CRUD con la API moderna ----------

    @Override
    public void altaCancion(Cancion cancion) {
        // TODO: session.persist(cancion), dentro de una transacción.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 2)");
    }

    /**
     * Busca una canción por su clave primaria. TODO: session.get(Cancion.class, id).
     */
    public Cancion buscarPorId(int id) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 2)");
    }

    @Override
    public void actualizarCancion(Cancion cancion) {
        // TODO: session.merge(cancion), dentro de una transacción.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 2)");
    }

    @Override
    public void eliminarCancion(int idCancion) {
        // TODO: session.get(...) + session.remove(...), dentro de una transacción.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 2)");
    }

    // ---------- Ejercicio 3: consultas HQL ----------

    public List<Cancion> cancionesDeAlbum(int idAlbum) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 3)");
    }

    public List<Cancion> cancionesDeArtista(String nombreArtista) {
        // Recuerda: hay que atravesar Cancion -> Album -> Artista en el HQL.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 3)");
    }

    public List<Artista> artistasSinAlbumes() {
        // Pista: LEFT JOIN con la colección de álbumes del artista, comprobando que es null.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 3)");
    }

    @Override
    public List<Cancion> buscarCanciones(String texto) {
        // Puedes reutilizar aquí una consulta HQL parecida a las del ejercicio 3.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4)");
    }

    // ---------- Ejercicio 4: listas de reproducción (M:N con datos adicionales) ----------

    @Override
    public void altaLista(ListaReproduccion lista) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 4)");
    }

    @Override
    public void anyadirCancionALista(int idLista, int idCancion, int posicion) {
        // Crea un ListaCancion(lista, cancion, posicion) y persístelo.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 4)");
    }

    @Override
    public List<Cancion> cancionesDeLista(int idLista) {
        // Consulta las ListaCancion de esa lista ordenadas por "posicion" y extrae la canción de cada una.
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 4)");
    }

    // ---------- Ejercicio 5: operación transaccional ----------

    /**
     * Elimina la relación (idCancion, idListaOrigen) y crea (idCancion, idListaDestino,
     * nuevaPosicion) dentro de una única transacción: si el segundo paso falla, el
     * primero también debe deshacerse.
     */
    public void moverCancionDeLista(int idCancion, int idListaOrigen, int idListaDestino, int nuevaPosicion) {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4, ejercicio 5)");
    }

    // ---------- Resto de la interfaz ----------

    @Override
    public List<Genero> listarGeneros() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4)");
    }

    @Override
    public List<Artista> listarArtistas() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4)");
    }

    @Override
    public List<Album> listarAlbumes() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4)");
    }

    @Override
    public List<Cancion> listarCanciones() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4)");
    }

    @Override
    public List<ListaReproduccion> listarListas() {
        throw new UnsupportedOperationException("TODO: implementar (unidad 4)");
    }
}
