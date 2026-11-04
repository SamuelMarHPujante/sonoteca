package es.iesejemplo.sonoteca.repositorio.jdbc;

/**
 * UNIDAD 3 - EJERCICIO 3.
 * <p>
 * Clase auxiliar (ya creada, no hace falta modificarla) para mostrar una
 * canción junto con los datos de su álbum, artista y género en una sola fila,
 * tal y como los devolverá tu consulta con JOIN.
 */
public class CancionCompleta {

    private final String tituloCancion;
    private final String tituloAlbum;
    private final String nombreArtista;
    private final String nombreGenero;
    private final int anio;

    public CancionCompleta(String tituloCancion, String tituloAlbum, String nombreArtista, String nombreGenero, int anio) {
        this.tituloCancion = tituloCancion;
        this.tituloAlbum = tituloAlbum;
        this.nombreArtista = nombreArtista;
        this.nombreGenero = nombreGenero;
        this.anio = anio;
    }

    public String getTituloCancion() {
        return tituloCancion;
    }

    public String getTituloAlbum() {
        return tituloAlbum;
    }

    public String getNombreArtista() {
        return nombreArtista;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public int getAnio() {
        return anio;
    }

    @Override
    public String toString() {
        return tituloCancion + " - " + nombreArtista + " (" + tituloAlbum + ", " + anio + ") [" + nombreGenero + "]";
    }
}
