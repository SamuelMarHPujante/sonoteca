package es.iesejemplo.sonoteca.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor con toda la biblioteca musical: se usa en la unidad 2 (ficheros)
 * para serializar/deserializar y exportar/importar a JSON de una sola vez.
 */
public class Biblioteca implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Genero> generos = new ArrayList<>();
    private List<Artista> artistas = new ArrayList<>();
    private List<Album> albumes = new ArrayList<>();
    private List<Cancion> canciones = new ArrayList<>();
    private List<ListaReproduccion> listasReproduccion = new ArrayList<>();

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public List<Artista> getArtistas() {
        return artistas;
    }

    public void setArtistas(List<Artista> artistas) {
        this.artistas = artistas;
    }

    public List<Album> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(List<Album> albumes) {
        this.albumes = albumes;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    public List<ListaReproduccion> getListasReproduccion() {
        return listasReproduccion;
    }

    public void setListasReproduccion(List<ListaReproduccion> listasReproduccion) {
        this.listasReproduccion = listasReproduccion;
    }
}
