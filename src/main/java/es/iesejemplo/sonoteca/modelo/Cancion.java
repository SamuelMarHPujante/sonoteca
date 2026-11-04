package es.iesejemplo.sonoteca.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Una canción. Pertenece a un {@link Album} (y, a través de él, a un artista y un género).
 * <p>
 * UNIDAD 4: añade {@code @Entity}, y sobre "album" un {@code @ManyToOne} con su
 * {@code @JoinColumn(name = "album_id")}.
 */
// TODO unidad 4: @Entity
public class Cancion implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO unidad 4: @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;
    private int duracionSegundos;
    private int numeroPista;

    // TODO unidad 4: @ManyToOne  @JoinColumn(name = "album_id")
    private Album album;

    public Cancion() {
    }

    public Cancion(String titulo, int duracionSegundos, int numeroPista, Album album) {
        this.titulo = titulo;
        this.duracionSegundos = duracionSegundos;
        this.numeroPista = numeroPista;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public int getNumeroPista() {
        return numeroPista;
    }

    public void setNumeroPista(int numeroPista) {
        this.numeroPista = numeroPista;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cancion)) return false;
        Cancion cancion = (Cancion) o;
        return id == cancion.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
