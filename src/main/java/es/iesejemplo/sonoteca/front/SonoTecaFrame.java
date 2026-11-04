package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.repositorio.ficheros.RepositorioFicheros;
import es.iesejemplo.sonoteca.repositorio.hibernate.RepositorioHibernate;
import es.iesejemplo.sonoteca.repositorio.jdbc.RepositorioJDBC;
import es.iesejemplo.sonoteca.repositorio.mongo.RepositorioMongo;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de SonoTeca. Una pestaña por unidad; no hace falta tocar
 * nada de este paquete {@code front}, ya está completo.
 */
public class SonoTecaFrame extends JFrame {

    public SonoTecaFrame(RepositorioFicheros repositorioFicheros,
                          RepositorioJDBC repositorioJDBC,
                          RepositorioHibernate repositorioHibernate,
                          RepositorioMongo repositorioMongo) {
        super("SonoTeca");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Unidad 2 – Ficheros", new PanelUnidad2(repositorioFicheros));
        pestañas.addTab("Unidad 3 – Bases de datos", new PanelUnidad3(repositorioJDBC));
        pestañas.addTab("Unidad 4 – Hibernate", new PanelUnidad4(repositorioHibernate));
        pestañas.addTab("Unidad 5 – Spring (API REST)", new PanelUnidad5());
        pestañas.addTab("Unidad 6 – MongoDB", new PanelUnidad6(repositorioMongo));

        getContentPane().add(pestañas, BorderLayout.CENTER);
    }
}
