package es.iesejemplo.sonoteca;

import es.iesejemplo.sonoteca.front.SonoTecaFrame;
import es.iesejemplo.sonoteca.repositorio.ficheros.RepositorioFicheros;
import es.iesejemplo.sonoteca.repositorio.hibernate.RepositorioHibernate;
import es.iesejemplo.sonoteca.repositorio.jdbc.RepositorioJDBC;
import es.iesejemplo.sonoteca.repositorio.mongo.RepositorioMongo;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 * <p>
 * La ventana tiene una pestaña por cada unidad con persistencia (2, 3, 4 y 6),
 * cada una con su propio repositorio: así puedes probar los ejercicios de cada
 * práctica de forma independiente, todos desde la misma aplicación. Que una
 * pestaña falle (porque todavía no has implementado esa unidad) no afecta a
 * las demás.
 */
public class Main {

    public static void main(String[] args) {
        RepositorioFicheros repositorioFicheros = new RepositorioFicheros("sonoteca.dat");
        RepositorioJDBC repositorioJDBC = new RepositorioJDBC();
        RepositorioHibernate repositorioHibernate = new RepositorioHibernate();
        RepositorioMongo repositorioMongo = crearRepositorioMongoSinFallar();

        SwingUtilities.invokeLater(() -> {
            SonoTecaFrame frame = new SonoTecaFrame(
                    repositorioFicheros, repositorioJDBC, repositorioHibernate, repositorioMongo);
            frame.setVisible(true);
        });
    }

    /**
     * Crear un RepositorioMongo intenta abrir un MongoClient; si en tu equipo
     * todavía no tienes MongoDB instalado esto no debería fallar (el driver no
     * conecta de verdad hasta la primera operación), pero por si acaso no
     * dejamos que un problema aquí impida arrancar el resto de la aplicación.
     */
    private static RepositorioMongo crearRepositorioMongoSinFallar() {
        try {
            return new RepositorioMongo();
        } catch (Exception e) {
            System.out.println("No se ha podido preparar RepositorioMongo todavía: " + e);
            return null;
        }
    }
}

