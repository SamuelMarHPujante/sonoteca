package es.iesejemplo.sonoteca.repositorio.ficheros;

import com.mongodb.lang.Nullable;
import es.iesejemplo.sonoteca.modelo.*;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

/**
 * UNIDAD 2 — Persistencia en ficheros.
 * <p>
 * Guarda y recupera la biblioteca completa de un fichero binario (serialización)
 * y sabe escanear una carpeta de MP3 para construir la biblioteca inicial a
 * partir de sus cabeceras ID3v1.
 * <p>
 * Complétala siguiendo los ejercicios de la práctica de la unidad 2. Todos los
 * métodos de {@link RepositorioSonoTeca} deben quedar implementados para que la
 * aplicación funcione con ella (cambia la línea correspondiente en {@code Main}).
 */
public class RepositorioFicheros implements RepositorioSonoTeca {

    private final String rutaFicheroBiblioteca;
    private Biblioteca biblioteca;

    // Añadidos por mí
    private final List<ListaReproduccion> listas = new ArrayList<>();
    private final Map<Integer, List<Integer>> cancionesPorLista = new HashMap<>();
    private static final int TAM_CABECERA = 128;

    /**
     * @param rutaFicheroBiblioteca ruta del fichero binario donde se guarda la biblioteca
     *                              (por ejemplo, "sonoteca.dat"). Si no existe todavía,
     *                              se debe partir de una biblioteca vacía.
     */
    public RepositorioFicheros(String rutaFicheroBiblioteca) {
        this.rutaFicheroBiblioteca = rutaFicheroBiblioteca;
        try {
            this.biblioteca = cargarBiblioteca(rutaFicheroBiblioteca);
        } catch (UnsupportedOperationException e) {
            // Todavía no has hecho el ejercicio 3 (cargarBiblioteca): empezamos con una
            // biblioteca vacía en memoria para poder probar ya los ejercicios 1 y 2.
            System.out.println("(cargarBiblioteca aún no implementado; empezando con una biblioteca vacía en memoria)");
            this.biblioteca = new Biblioteca();
        }
    }

    // ---------- Ejercicio 1: escanear un directorio en busca de MP3 ----------

    /**
     * @return la lista de ficheros .mp3 encontrados recursivamente a partir de rutaBase.
     *         Si rutaBase no existe o no es un directorio, devuelve una lista vacía.
     */
    // DONE: implementar (unidad 2, ejercicio 1).
    public List<File> escanearDirectorio(String rutaBase) {
        if (rutaBase == null || rutaBase.isBlank()) {
            return List.of();
        }

        Path base = Path.of(rutaBase);
        if (!Files.isDirectory(base)) return List.of();

        try (Stream<Path> rutas = Files.walk(base)) {
            return rutas
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".mp3"))
                    .map(Path::toFile)
                    .sorted()
                    .toList();
        } catch (IOException e) {
            System.err.println("No se pudo recorrer el directorio: " + e.getMessage());
            return List.of();
        }
    }

    // ---------- Ejercicio 2: leer la cabecera ID3v1 ----------

    /**
     * Lee los últimos 128 bytes de ficheroMp3 (cabecera ID3v1) y construye la Cancion
     * correspondiente (creando también, si hace falta, Artista/Album/Género).
     * Si el fichero no tiene cabecera ID3v1 válida, devuelve null.
     */
    // DONE: implementar (unidad 2, ejercicio 2).
    public Cancion leerCabeceraID3(File ficheroMp3) {
        try {
            Path ruta = ficheroMp3.toPath();
            if (Files.size(ruta) < TAM_CABECERA) return null;

            byte[] bloque = new byte[TAM_CABECERA];
            try (RandomAccessFile raf = new RandomAccessFile(ficheroMp3, "r")) {
                raf.seek(ficheroMp3.length() - TAM_CABECERA);
                raf.readFully(bloque);
            }

            String etiqueta = new String(bloque, 0, 3, StandardCharsets.ISO_8859_1);
            if (!etiqueta.equals("TAG")) return null;

            String titulo = campo(bloque, 3, 30);
            String artistaNombre = campo(bloque, 33, 30);
            String albumTitulo = campo(bloque, 63, 30);
            String anyoTexto = campo(bloque, 93, 4);

            int anyo = 0;
            if (!anyoTexto.isBlank()) {
                try {
                    anyo = Integer.parseInt(anyoTexto);
                } catch (NumberFormatException e) {
                    anyo = 0;
                }
            }

            Genero genero = obtenerOCrearGenero("Desconocido");
            Artista artista = obtenerOCrearArtista(artistaNombre, "Desconocido");
            Album album = obtenerOCrearAlbum(albumTitulo, anyo, artista, genero);

            return new Cancion(titulo, 0, 0, album);
        } catch (IOException e) {
            System.err.println("Error al leer la cabecera ID3 del fichero " + ficheroMp3.getName() + ": " + e.getMessage());
            return null;
        }
    }

    private static String campo(byte[] bloque, int desde, int longitud) {
        String texto = new String(bloque, desde, longitud, StandardCharsets.ISO_8859_1);

        int indiceNulo = texto.indexOf('\0');
        if (indiceNulo != -1)
            texto = texto.substring(0, indiceNulo);

        return texto.trim();
    }

    // ---------- Ejercicio 3: serializar/deserializar la biblioteca ----------
    // DONE: implementar con ObjectOutputStream (unidad 2, ejercicio 3).
    public void guardarBiblioteca(Biblioteca biblioteca, String rutaFichero) {
        try {
            Path rutaAbsoluta = Path.of(rutaFichero).toAbsolutePath();
            System.out.println("Ruta absoluta de guardado: " + rutaAbsoluta);
            if (rutaAbsoluta.getParent() != null && Files.notExists(rutaAbsoluta.getParent())) {
                Files.createDirectories(rutaAbsoluta.getParent());
            }

            try (ObjectOutputStream stream = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(rutaAbsoluta)))) {
                stream.writeObject(biblioteca);
            }
        } catch (NoSuchFileException e) {
            System.err.println("No se pudo guardar la biblioteca en " + rutaFichero + ": " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al guardar la biblioteca en " + rutaFichero + ": " + e.getMessage());
        }
    }

    // DONE: implementar con ObjectInputStream (unidad 2, ejercicio 3).
    public Biblioteca cargarBiblioteca(String rutaFichero) {
        try {
            Path rutaAbsoluta = Path.of(rutaFichero).toAbsolutePath();
            if (Files.notExists(rutaAbsoluta)) {
                return new Biblioteca();
            }

            try (ObjectInputStream stream = new ObjectInputStream(
                    new BufferedInputStream(Files.newInputStream(rutaAbsoluta)))) {
                return (Biblioteca) stream.readObject();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error al procesar el archivo de biblioteca: " + e.getMessage());
            return new Biblioteca();
        } catch (IOException e) {
            System.err.println("No se pudo cargar la biblioteca desde " + rutaFichero + ": " + e.getMessage());
            return new Biblioteca();
        }
    }

    // ---------- Implementación de RepositorioSonoTeca (Ejercicio 0) ----------
    // A partir de aquí, apóyate en el objeto "biblioteca" cargado en memoria,
    // y recuerda volver a llamar a guardarBiblioteca(...) cada vez que la modifiques.
    // Nota: He segmentado el código en bloques region comprimibles para mayor comodidad

    //region LISTADO DE OBJETOS SIMPLES

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public List<Genero> listarGeneros() {
        return biblioteca.getGeneros();
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public List<Artista> listarArtistas() {
        return biblioteca.getArtistas();
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public List<Album> listarAlbumes() {
        return biblioteca.getAlbumes();
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public List<Cancion> listarCanciones() {
        return biblioteca.getCanciones();
    }

    //endregion

    //region GESTIÓN DE CANCIONES

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public List<Cancion> buscarCanciones(String texto) {
        return listarCanciones().stream()
                .filter(c -> c.getTitulo().toLowerCase().contains(texto.toLowerCase()))
                .toList();
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public void altaCancion(Cancion cancion) {
        List<Cancion> listaCanciones = listarCanciones();
        boolean existeCancion = obtenerCoincidencia(listaCanciones, cancion.getTitulo(), Cancion::getTitulo) != null;

        if (!existeCancion) {
            int idNuevo = siguienteId(listaCanciones, Cancion::getId);
            comprobarElementosCancion(cancion);
            cancion.setId(idNuevo);
            listaCanciones.add(cancion);
            biblioteca.setCanciones(listaCanciones);
            guardarBiblioteca(biblioteca, rutaFicheroBiblioteca);
        }
    }

    private void comprobarElementosCancion(Cancion cancion) {
        Album albumOriginal = cancion.getAlbum();
        if (albumOriginal != null) return;

        Artista artistaOriginal = albumOriginal.getArtista();
        Genero generoOriginal = albumOriginal.getGenero();

        String nombreGenero = (generoOriginal != null) ? generoOriginal.getNombre() : "Desconocido";
        Genero generoSeguro = obtenerOCrearGenero(nombreGenero);

        String nombreArtista = (artistaOriginal != null) ? artistaOriginal.getNombre() : "Desconocido";
        String nacionalidadArtista = (artistaOriginal != null) ? artistaOriginal.getNacionalidad() : "Desconocido";
        Artista artistaSeguro = obtenerOCrearArtista(nombreArtista, nacionalidadArtista);

        String tituloAlbum = albumOriginal.getTitulo();
        int anyoAlbum = albumOriginal.getAnio();
        Album albumSeguro = obtenerOCrearAlbum(tituloAlbum, anyoAlbum, artistaSeguro, generoSeguro);

        cancion.setAlbum(albumSeguro);
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public void actualizarCancion(Cancion cancion) {
        // sustituye en la lista la canción con el mismo id
        int idCancion = cancion.getId();
        List<Cancion> listaCanciones = listarCanciones();
        Cancion cancionDeLista = obtenerPorId(listarCanciones(), idCancion, Cancion::getId);

        if (cancionDeLista != null) {
            int posicion = listaCanciones.indexOf(cancionDeLista);
            listaCanciones.set(posicion, cancion);
            biblioteca.setCanciones(listaCanciones);
            guardarBiblioteca(biblioteca, rutaFicheroBiblioteca);
        }
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public void eliminarCancion(int idCancion) {
        Cancion cancion = obtenerPorId(listarCanciones(), idCancion, Cancion::getId);
        if (cancion != null) {
            List<Cancion> listaCanciones = listarCanciones();
            int posicion = listaCanciones.indexOf(cancion);

            listaCanciones.remove(posicion);
            biblioteca.setCanciones(listaCanciones);
            guardarBiblioteca(biblioteca, rutaFicheroBiblioteca);
        }
    }

    //endregion

    //region GESTIÓN CON LISTAS

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public List<ListaReproduccion> listarListas() {
        return listas;
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public void altaLista(ListaReproduccion lista) {
        int nuevoId = siguienteId(listas, ListaReproduccion::getId);
        lista.setId(nuevoId);
        listas.add(lista);
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    @Override
    public void anyadirCancionALista(int idLista, int idCancion, int posicion) {
        ListaReproduccion lista = obtenerPorId(listas, idLista, ListaReproduccion::getId);
        Cancion cancion = obtenerPorId(listarCanciones(), idCancion, Cancion::getId);

        if (cancion != null && lista != null) {
            List<Cancion> cancionesDeLista = lista.getCanciones();
            if (posicion > cancionesDeLista.size()) posicion = cancionesDeLista.size();
            cancionesDeLista.add(posicion, cancion);
        }
    }

    // DONE: implementar (unidad 2, ejercicio 0).
    // Devuelve las canciones ordenadas por posición, no en el orden en que se añadieron.
    @Override
    public List<Cancion> cancionesDeLista(int idLista) {
        ListaReproduccion lista = obtenerPorId(listas, idLista, ListaReproduccion::getId);
        return lista != null ? lista.getCanciones() : List.of();
    }

    //endregion


    //region OPERACIONES POR ID - métodos hechos con clase genérica para que funcionen con cualquier tipo de lista

    private <T> int siguienteId(List<T> lista, ToIntFunction<T> idExtractor) {
        int maxId = lista.stream()
                .mapToInt(idExtractor).max()
                .orElse(0);
        return maxId + 1;
    }

    @Nullable
    private <T> T obtenerPorId(List<T> lista, int idMeta, Function<T, Integer> idExtractor) {
        return lista.stream()
                .filter(obj -> Integer.valueOf(idMeta).equals(idExtractor.apply(obj)))
                .findFirst()
                .orElse(null);
    }

    //endregion

    //region OBTENER O CREAR ELEMENTOS

    private Artista obtenerOCrearArtista(String nombre, String nacionalidad) {
        List<Artista> artistas = listarArtistas();
        Artista artista = obtenerCoincidencia(artistas, nombre, Artista::getNombre);

        if (artista == null) {
            int idNuevo = siguienteId(artistas, Artista::getId);
            artista = new Artista(idNuevo, nombre, nacionalidad);
            listarArtistas().add(artista);
        }

        return artista;
    }

    private Album obtenerOCrearAlbum(String titulo, int anyo, Artista artista, Genero genero) {
        List<Album> albumes = listarAlbumes();
        Album album = obtenerCoincidencia(albumes, titulo, Album::getTitulo);

        if (album == null) {
            int idNuevo = siguienteId(albumes, Album::getId);
            album = new Album(titulo, anyo, artista, genero);
            album.setId(idNuevo);
            listarAlbumes().add(album);
        }

        return album;
    }

    private Genero obtenerOCrearGenero(String nombre) {
        List<Genero> generos = listarGeneros();
        Genero genero = obtenerCoincidencia(listarGeneros(), nombre, Genero::getNombre);

        if (genero == null) {
            int idNuevo = siguienteId(generos, Genero::getId);
            genero = new Genero(idNuevo, nombre);
            listarGeneros().add(genero);
        }

        return genero;
    }

    @Nullable
    private <T> T obtenerCoincidencia(List<T> lista, String nombreOTitulo, Function<T, String> extractor) {
        return lista.stream()
                .filter(obj -> extractor.apply(obj).equalsIgnoreCase(nombreOTitulo))
                .findFirst()
                .orElse(null);
    }

    //endregion
}

