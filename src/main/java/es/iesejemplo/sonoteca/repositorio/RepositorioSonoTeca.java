package es.iesejemplo.sonoteca.repositorio;

import es.iesejemplo.sonoteca.modelo.Artista;
import es.iesejemplo.sonoteca.modelo.Album;
import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.modelo.Genero;
import es.iesejemplo.sonoteca.modelo.ListaReproduccion;

import java.util.List;

/**
 * Contrato que debe cumplir cualquier forma de persistir la biblioteca de SonoTeca.
 * <p>
 * La interfaz gráfica (paquete {@code front}) solo conoce esta interfaz: no le
 * importa si, por debajo, los datos se guardan en ficheros, en una base de datos
 * relacional con JDBC, con Hibernate o en MongoDB. Cada unidad implementaréis
 * una clase distinta que cumpla este contrato:
 * <ul>
 *     <li>Unidad 2 — {@code repositorio.ficheros.RepositorioFicheros}</li>
 *     <li>Unidad 3 — {@code repositorio.jdbc.RepositorioJDBC}</li>
 *     <li>Unidad 4 — {@code repositorio.hibernate.RepositorioHibernate}</li>
 *     <li>Unidad 6 — {@code repositorio.mongo.RepositorioMongo}</li>
 * </ul>
 * (La unidad 5 es un proyecto Spring Boot aparte, no una implementación de esta interfaz).
 */
public interface RepositorioSonoTeca {

    List<Genero> listarGeneros();

    List<Artista> listarArtistas();

    List<Album> listarAlbumes();

    List<Cancion> listarCanciones();

    /**
     * Busca canciones cuyo título, álbum o artista contengan el texto indicado
     * (sin distinguir mayúsculas/minúsculas). Si {@code texto} está vacío, se
     * comporta igual que {@link #listarCanciones()}.
     */
    List<Cancion> buscarCanciones(String texto);

    /**
     * Da de alta una canción completa. El {@link Cancion#getAlbum()} de la canción
     * (y, dentro de él, su artista y su género) puede venir con {@code id == 0}
     * si el usuario ha introducido un álbum/artista/género nuevo desde el
     * formulario: en ese caso, la implementación debe crearlos antes de crear
     * la canción (alta "en cascada").
     */
    void altaCancion(Cancion cancion);

    void actualizarCancion(Cancion cancion);

    void eliminarCancion(int idCancion);

    List<ListaReproduccion> listarListas();

    void altaLista(ListaReproduccion lista);

    /**
     * Añade una canción a una lista de reproducción, en la posición indicada
     * (1 = primera canción de la lista).
     */
    void anyadirCancionALista(int idLista, int idCancion, int posicion);

    /**
     * @return las canciones de la lista indicada, ordenadas por su posición en ella.
     */
    List<Cancion> cancionesDeLista(int idLista);
}
