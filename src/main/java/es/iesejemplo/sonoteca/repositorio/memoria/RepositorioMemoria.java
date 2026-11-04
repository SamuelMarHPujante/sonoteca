package es.iesejemplo.sonoteca.repositorio.memoria;

import es.iesejemplo.sonoteca.modelo.*;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación en memoria de {@link RepositorioSonoTeca}, usada como demostración
 * y para poder ejecutar la aplicación sin depender de ninguna tecnología de
 * persistencia todavía. Está completa: no hace falta tocarla.
 * <p>
 * Se inicializa con algunos datos de ejemplo para que la aplicación no arranque vacía.
 */
public class RepositorioMemoria implements RepositorioSonoTeca {

    private final List<Genero> generos = new ArrayList<>();
    private final List<Artista> artistas = new ArrayList<>();
    private final List<Album> albumes = new ArrayList<>();
    private final List<Cancion> canciones = new ArrayList<>();
    private final List<ListaReproduccion> listas = new ArrayList<>();
    private final Map<Integer, List<Integer>> cancionesPorLista = new HashMap<>(); // idLista -> ids de canciones, en orden

    private int siguienteIdGenero = 1;
    private int siguienteIdArtista = 1;
    private int siguienteIdAlbum = 1;
    private int siguienteIdCancion = 1;
    private int siguienteIdLista = 1;

    public RepositorioMemoria() {
        cargarDatosDeEjemplo();
    }

    private void cargarDatosDeEjemplo() {
        Genero rock = altaGeneroSiNoExiste("Rock");
        Genero pop = altaGeneroSiNoExiste("Pop");

        Artista queen = altaArtistaSiNoExiste("Queen", "Reino Unido");
        Artista abba = altaArtistaSiNoExiste("ABBA", "Suecia");

        Album nightAtTheOpera = altaAlbumSiNoExiste("A Night at the Opera", 1975, queen, rock);
        Album arrival = altaAlbumSiNoExiste("Arrival", 1976, abba, pop);

        altaCancion(new Cancion("Bohemian Rhapsody", 355, 11, nightAtTheOpera));
        altaCancion(new Cancion("Dancing Queen", 230, 1, arrival));
    }

    // ---------- Consultas ----------

    @Override
    public List<Genero> listarGeneros() {
        return new ArrayList<>(generos);
    }

    @Override
    public List<Artista> listarArtistas() {
        return new ArrayList<>(artistas);
    }

    @Override
    public List<Album> listarAlbumes() {
        return new ArrayList<>(albumes);
    }

    @Override
    public List<Cancion> listarCanciones() {
        return new ArrayList<>(canciones);
    }

    @Override
    public List<Cancion> buscarCanciones(String texto) {
        if (texto == null || texto.isBlank()) {
            return listarCanciones();
        }
        String textoBusqueda = texto.toLowerCase();
        return canciones.stream()
                .filter(c -> c.getTitulo().toLowerCase().contains(textoBusqueda)
                        || c.getAlbum().getTitulo().toLowerCase().contains(textoBusqueda)
                        || c.getAlbum().getArtista().getNombre().toLowerCase().contains(textoBusqueda))
                .collect(Collectors.toList());
    }

    // ---------- Altas en cascada ----------

    @Override
    public void altaCancion(Cancion cancion) {
        Album album = cancion.getAlbum();
        Artista artista = altaArtistaSiNoExiste(album.getArtista().getNombre(), album.getArtista().getNacionalidad());
        Genero genero = altaGeneroSiNoExiste(album.getGenero().getNombre());
        Album albumResuelto = altaAlbumSiNoExiste(album.getTitulo(), album.getAnio(), artista, genero);

        cancion.setAlbum(albumResuelto);
        cancion.setId(siguienteIdCancion++);
        canciones.add(cancion);
    }

    private Genero altaGeneroSiNoExiste(String nombre) {
        return generos.stream()
                .filter(g -> g.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> {
                    Genero nuevo = new Genero(siguienteIdGenero++, nombre);
                    generos.add(nuevo);
                    return nuevo;
                });
    }

    private Artista altaArtistaSiNoExiste(String nombre, String nacionalidad) {
        return artistas.stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> {
                    Artista nuevo = new Artista(siguienteIdArtista++, nombre, nacionalidad);
                    artistas.add(nuevo);
                    return nuevo;
                });
    }

    private Album altaAlbumSiNoExiste(String titulo, int anio, Artista artista, Genero genero) {
        return albumes.stream()
                .filter(a -> a.getTitulo().equalsIgnoreCase(titulo) && a.getArtista().equals(artista))
                .findFirst()
                .orElseGet(() -> {
                    Album nuevo = new Album(titulo, anio, artista, genero);
                    nuevo.setId(siguienteIdAlbum++);
                    albumes.add(nuevo);
                    return nuevo;
                });
    }

    // ---------- Actualizar / eliminar ----------

    @Override
    public void actualizarCancion(Cancion cancion) {
        for (int i = 0; i < canciones.size(); i++) {
            if (canciones.get(i).getId() == cancion.getId()) {
                canciones.set(i, cancion);
                return;
            }
        }
    }

    @Override
    public void eliminarCancion(int idCancion) {
        canciones.removeIf(c -> c.getId() == idCancion);
        cancionesPorLista.values().forEach(lista -> lista.removeIf(id -> id == idCancion));
    }

    // ---------- Listas de reproducción ----------

    @Override
    public List<ListaReproduccion> listarListas() {
        return new ArrayList<>(listas);
    }

    @Override
    public void altaLista(ListaReproduccion lista) {
        lista.setId(siguienteIdLista++);
        listas.add(lista);
        cancionesPorLista.put(lista.getId(), new ArrayList<>());
    }

    @Override
    public void anyadirCancionALista(int idLista, int idCancion, int posicion) {
        List<Integer> idsCanciones = cancionesPorLista.computeIfAbsent(idLista, k -> new ArrayList<>());
        int indice = Math.max(0, Math.min(posicion - 1, idsCanciones.size()));
        idsCanciones.add(indice, idCancion);
    }

    @Override
    public List<Cancion> cancionesDeLista(int idLista) {
        List<Integer> idsCanciones = cancionesPorLista.getOrDefault(idLista, List.of());
        Map<Integer, Cancion> porId = canciones.stream().collect(Collectors.toMap(Cancion::getId, c -> c));
        return idsCanciones.stream()
                .map(porId::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
