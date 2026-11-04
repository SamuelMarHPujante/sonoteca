package es.iesejemplo.sonoteca.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Una lista de reproducción. Se relaciona con {@link Cancion} en una relación
 * muchos a muchos (una canción puede estar en varias listas, y cada canción
 * ocupa una posición distinta en cada lista en la que aparece).
 * <p>
 * UNIDAD 4 - Ejercicio 4: añade {@code @Entity}. La relación M:N con Cancion
 * se modela con la entidad intermedia {@code ListaCancion} (ver el paquete
 * {@code repositorio.hibernate}), no directamente aquí.
 */
// TODO unidad 4: @Entity
public class ListaReproduccion implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO unidad 4: @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;

    // Canciones de la lista, en orden (índice 0 = posición 1).
    private List<Cancion> canciones = new ArrayList<>();

    public ListaReproduccion() {
    }

    public ListaReproduccion(String nombre) {
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

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaReproduccion)) return false;
        ListaReproduccion that = (ListaReproduccion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
