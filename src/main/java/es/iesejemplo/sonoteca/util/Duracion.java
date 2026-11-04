package es.iesejemplo.sonoteca.util;

/**
 * UNIDAD 1 - EJERCICIO 2
 * <p>
 * Utilidad para convertir una duración en segundos a formato "mm:ss" y viceversa.
 * Complétala siguiendo las indicaciones de la práctica y comprueba tu solución
 * ejecutando los tests ya incluidos en el proyecto ({@code DuracionTest}).
 */
public class Duracion {

    private final int segundosTotales;

    /**
     * @param segundosTotales duración total en segundos; debe ser >= 0.
     * @throws IllegalArgumentException si segundosTotales es negativo.
     */
    public Duracion(int segundosTotales) {
        // DONE: valida que segundosTotales no sea negativo antes de asignarlo.
        if (segundosTotales < 0) {
            throw new IllegalArgumentException();
        }
        this.segundosTotales = segundosTotales;
    }

    public int getSegundosTotales() {
        return segundosTotales;
    }

    /**
     * @return la duración con formato "mm:ss" (por ejemplo, 185 segundos -> "3:05").
     *         Los segundos siempre se muestran con dos dígitos.
     */
    public String aTexto() {
        // DONE: implementar.
        int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;
        return minutos + ":" + String.format("%02d", segundos);
    }

    /**
     * Proceso inverso a {@link #aTexto()}.
     *
     * @param texto una duración con formato "mm:ss".
     * @return el objeto Duracion equivalente.
     */
    public static Duracion desdeTexto(String texto) {
        // DONE: implementar.
        String[] tiempos = texto.split(":");
        int minutos = Integer.parseInt(tiempos[0]);
        int segundos = Integer.parseInt(tiempos[1]);
        int segundosCompletos = (60 * minutos) + segundos;
        return new Duracion(segundosCompletos);
    }

    @Override
    public String toString() {
        return aTexto();
    }
}
