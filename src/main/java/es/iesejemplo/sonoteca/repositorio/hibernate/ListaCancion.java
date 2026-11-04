package es.iesejemplo.sonoteca.repositorio.hibernate;

import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.modelo.ListaReproduccion;

import java.io.Serializable;

/**
 * UNIDAD 4 - EJERCICIO 4.
 * <p>
 * Entidad intermedia de la relación muchos a muchos entre {@code ListaReproduccion}
 * y {@code Cancion}, con el dato adicional "posicion" (la posición que ocupa esa
 * canción dentro de esa lista concreta).
 * <p>
 * TODO: completa las anotaciones siguiendo el ejemplo de "Escribir" visto en la
 * teoría de la unidad 4 (relación M:N con campos adicionales):
 * <ul>
 *     <li>{@code @Entity} sobre la clase.</li>
 *     <li>{@code @EmbeddedId} sobre el atributo {@code id}.</li>
 *     <li>{@code @ManyToOne} + {@code @MapsId("idLista")} + {@code @JoinColumn(name = "lista_id")}
 *         sobre {@code lista}.</li>
 *     <li>{@code @ManyToOne} + {@code @MapsId("idCancion")} + {@code @JoinColumn(name = "cancion_id")}
 *         sobre {@code cancion}.</li>
 * </ul>
 */
public class ListaCancion implements Serializable {

    private ListaCancionId id;
    private ListaReproduccion lista;
    private Cancion cancion;
    private int posicion;

    public ListaCancion() {
    }

    public ListaCancion(ListaReproduccion lista, Cancion cancion, int posicion) {
        this.id = new ListaCancionId(lista.getId(), cancion.getId());
        this.lista = lista;
        this.cancion = cancion;
        this.posicion = posicion;
    }

    public ListaCancionId getId() {
        return id;
    }

    public void setId(ListaCancionId id) {
        this.id = id;
    }

    public ListaReproduccion getLista() {
        return lista;
    }

    public void setLista(ListaReproduccion lista) {
        this.lista = lista;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}
