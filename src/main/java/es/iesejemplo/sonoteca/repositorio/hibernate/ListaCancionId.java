package es.iesejemplo.sonoteca.repositorio.hibernate;

import java.io.Serializable;
import java.util.Objects;

/**
 * UNIDAD 4 - EJERCICIO 4.
 * <p>
 * Clave primaria compuesta de la relación muchos a muchos entre
 * {@code ListaReproduccion} y {@code Cancion}: la pareja (idLista, idCancion).
 * <p>
 * TODO: añade la anotación {@code @Embeddable} a esta clase.
 * Una clase @Embeddable debe ser Serializable e implementar equals()/hashCode()
 * comparando TODOS sus campos (ya te los dejamos hechos más abajo).
 */
public class ListaCancionId implements Serializable {

    private int idLista;
    private int idCancion;

    public ListaCancionId() {
    }

    public ListaCancionId(int idLista, int idCancion) {
        this.idLista = idLista;
        this.idCancion = idCancion;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public int getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(int idCancion) {
        this.idCancion = idCancion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaCancionId)) return false;
        ListaCancionId that = (ListaCancionId) o;
        return idLista == that.idLista && idCancion == that.idCancion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLista, idCancion);
    }
}
