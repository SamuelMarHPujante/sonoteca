package es.iesejemplo.sonoteca.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un álbum musical. Pertenece a un {@link Artista} y a un {@link Genero}.
 * <p>
 * UNIDAD 4: añade {@code @Entity}, y sobre "artista" y "genero" la anotación
 * {@code @ManyToOne} con su {@code @JoinColumn(name = "...")}.
 */
// TODO unidad 4: @Entity
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO unidad 4: @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;
    private int anio;

    // TODO unidad 4: @ManyToOne  @JoinColumn(name = "artista_id")
    private Artista artista;

    // TODO unidad 4: @ManyToOne  @JoinColumn(name = "genero_id")
    private Genero genero;

    public Album() {
    }

    public Album(String titulo, int anio, Artista artista, Genero genero) {
        this.titulo = titulo;
        this.anio = anio;
        this.artista = artista;
        this.genero = genero;
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

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return titulo + " (" + anio + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return id == album.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
