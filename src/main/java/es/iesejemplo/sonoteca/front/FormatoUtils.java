package es.iesejemplo.sonoteca.front;

/**
 * Pequeña utilidad interna del front para mostrar segundos como "mm:ss" en las
 * tablas, independiente de la clase {@code Duracion} que completaréis en la
 * unidad 1 (así la aplicación se puede ejecutar desde el primer día).
 */
final class FormatoUtils {

    private FormatoUtils() {
    }

    static String segundosATexto(int segundosTotales) {
        int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;
        return minutos + ":" + (segundos < 10 ? "0" + segundos : segundos);
    }
}
