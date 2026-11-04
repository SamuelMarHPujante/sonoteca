package es.iesejemplo.sonoteca.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un artista o grupo musical.
 * <p>
 * UNIDAD 4: añade {@code @Entity} y, si quieres poder navegar desde un artista
 * a sus álbumes, un {@code @OneToMany(mappedBy = "artista")} sobre una colección
 * {@code Set<Album>} (no es obligatorio para el resto de ejercicios).
 */
// TODO unidad 4: @Entity
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO unidad 4: @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String nacionalidad;

    public Artista() {
    }

    public Artista(String nombre) {
        this.nombre = nombre;
    }

    public Artista(int id, String nombre, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
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

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artista)) return false;
        Artista artista = (Artista) o;
        return id == artista.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
