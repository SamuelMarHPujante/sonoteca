package es.iesejemplo.sonoteca;

import es.iesejemplo.sonoteca.util.Duracion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de referencia para la Unidad 1 - Ejercicio 2 (clase Duracion).
 * No modifiques este fichero: tu implementación de Duracion debe superarlo tal cual.
 */
class DuracionTest {

    @Test
    void unaDuracionNormalSeFormateaComoMinutosYSegundos() {
        assertEquals("3:05", new Duracion(185).aTexto());
    }

    @Test
    void ceroSegundosSeFormateaComoCeroCero() {
        assertEquals("0:00", new Duracion(0).aTexto());
    }

    @Test
    void masDeUnaHoraSigueMostrandoSoloMinutosYSegundos() {
        // 1 hora, 5 minutos y 3 segundos = 3903 segundos = "65:03"
        assertEquals("65:03", new Duracion(3903).aTexto());
    }

    @Test
    void segundosPorDebajoDeDiezLlevanUnCeroDelante() {
        assertEquals("2:04", new Duracion(124).aTexto());
    }

    @Test
    void unaDuracionNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Duracion(-1));
    }

    @Test
    void desdeTextoReconstruyeLosSegundosTotales() {
        assertEquals(185, Duracion.desdeTexto("3:05").getSegundosTotales());
    }

    @Test
    void aTextoYDesdeTextoSonProcesosInversos() {
        Duracion original = new Duracion(247);
        Duracion reconstruida = Duracion.desdeTexto(original.aTexto());
        assertEquals(original.getSegundosTotales(), reconstruida.getSegundosTotales());
    }
}
