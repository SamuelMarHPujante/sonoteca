package es.iesejemplo.sonoteca.front;

import javax.swing.*;
import java.awt.*;

/**
 * UNIDAD 5 — Spring (API REST). Esta unidad NO se implementa dentro de esta
 * aplicación de escritorio: es un proyecto Spring Boot totalmente aparte
 * (carpeta {@code sonoteca-api/}, al lado de esta carpeta {@code sonoteca/}).
 * <p>
 * Esta pestaña solo recuerda a los alumnos y alumnas dónde está esa práctica
 * y cómo probarla, ya que no tiene sentido incrustar un cliente REST en la
 * propia aplicación que están construyendo con Swing.
 */
public class PanelUnidad5 extends JPanel {

    public PanelUnidad5() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JTextArea texto = new JTextArea();
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setFont(texto.getFont().deriveFont(14f));
        texto.setText(
                "La unidad 5 (Spring) no se programa dentro de SonoTeca (esta aplicación de escritorio).\n\n" +
                "Es un proyecto independiente: la carpeta \"sonoteca-api\", al mismo nivel que la carpeta " +
                "\"sonoteca\" de este proyecto de escritorio.\n\n" +
                "Ese proyecto es una API REST hecha con Spring Boot que expone la misma biblioteca de " +
                "canciones, álbumes, artistas y géneros, pero por HTTP en vez de con una ventana de Swing.\n\n" +
                "Para hacer la práctica de la unidad 5:\n" +
                "  1. Abre la carpeta \"sonoteca-api\" como un proyecto Maven aparte (en un nuevo módulo o " +
                "ventana del IDE).\n" +
                "  2. Sigue el enunciado de la Práctica Unidad 5.\n" +
                "  3. Arranca la aplicación Spring Boot y prueba los endpoints con Postman (hay una colección " +
                "de ejemplo: SonoTeca.postman_collection.json) o con el navegador para las peticiones GET.\n\n" +
                "No hace falta tocar nada de esta ventana para la unidad 5."
        );

        JScrollPane scroll = new JScrollPane(texto);
        add(scroll, BorderLayout.CENTER);
    }
}
