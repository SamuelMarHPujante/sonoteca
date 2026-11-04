package es.iesejemplo.sonoteca.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un género musical (Rock, Pop, Jazz...).
 * <p>
 * UNIDAD 4: añade aquí la anotación {@code @Entity} (y, si el nombre de la tabla
 * no coincide con "genero", {@code @Table(name = "...")}).
 */
// TODO unidad 4: @Entity
public class Genero implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO unidad 4: @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;

    public Genero() {
    }

    public Genero(String nombre) {
        this.nombre = nombre;
    }

    public Genero(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genero)) return false;
        Genero genero = (Genero) o;
        return id == genero.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
