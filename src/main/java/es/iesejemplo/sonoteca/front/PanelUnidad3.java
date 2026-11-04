package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.repositorio.jdbc.CancionCompleta;
import es.iesejemplo.sonoteca.repositorio.jdbc.RepositorioJDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * UNIDAD 3 — Bases de datos (JDBC). Ejercicios 1 a 6 de la práctica.
 */
public class PanelUnidad3 extends JPanel {

    private final RepositorioJDBC repositorio;

    public PanelUnidad3(RepositorioJDBC repositorio) {
        this.repositorio = repositorio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel tarjetas = new JPanel();
        tarjetas.setLayout(new BoxLayout(tarjetas, BoxLayout.Y_AXIS));
        tarjetas.add(crearTarjetaEjercicio3());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio5());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio6());

        JTabbedPane subPestañas = new JTabbedPane();
        subPestañas.addTab("Canciones (ejercicios 2 y 4: alta y búsqueda)", new PanelCanciones(repositorio));
        subPestañas.addTab("Álbumes", new PanelAlbumes(repositorio));
        subPestañas.addTab("Artistas y géneros", new PanelCatalogos(repositorio));
        subPestañas.addTab("Listas de reproducción", new PanelListas(repositorio));

        add(new JScrollPane(tarjetas), BorderLayout.NORTH);
        add(subPestañas, BorderLayout.CENTER);
    }

    // ---------- Ejercicio 3: listar con JOIN ----------

    private JPanel crearTarjetaEjercicio3() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo(
                "Ejercicio 3 — Listar canciones con los datos de álbum, artista y género (consulta con JOIN)");

        DefaultTableModel modelo = FrontUtils.soloLecturaModel(
                new String[]{"Canción", "Álbum", "Artista", "Género", "Año"});
        JTable tabla = new JTable(modelo);

        JButton botonListar = new JButton("Listar canciones completas");
        botonListar.addActionListener(e -> {
            modelo.setRowCount(0);
            try {
                List<CancionCompleta> resultado = repositorio.listarCancionesCompletas();
                for (CancionCompleta c : resultado) {
                    modelo.addRow(new Object[]{c.getTituloCancion(), c.getTituloAlbum(), c.getNombreArtista(), c.getNombreGenero(), c.getAnio()});
                }
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(botonListar);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 140));

        tarjeta.add(fila, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 5: eliminar álbum controlando la clave ajena ----------

    private JPanel crearTarjetaEjercicio5() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo(
                "Ejercicio 5 — Eliminar un álbum controlando la restricción de clave ajena");

        JSpinner spinnerIdAlbum = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        JButton botonEliminar = new JButton("Eliminar álbum");
        botonEliminar.addActionListener(e -> {
            try {
                repositorio.eliminarAlbum((Integer) spinnerIdAlbum.getValue());
                JOptionPane.showMessageDialog(this, "Álbum eliminado (si no tenía canciones asociadas).");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Id del álbum:"));
        fila.add(spinnerIdAlbum);
        fila.add(botonEliminar);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 6: importación masiva con transacción ----------

    private JPanel crearTarjetaEjercicio6() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo(
                "Ejercicio 6 — Importar la biblioteca desde JSON, todo dentro de una transacción");

        JTextField campoRuta = new JTextField(28);
        JButton botonElegir = new JButton("Elegir fichero JSON…");
        JButton botonImportar = new JButton("Importar");

        botonElegir.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                campoRuta.setText(selector.getSelectedFile().getAbsolutePath());
            }
        });

        botonImportar.addActionListener(e -> {
            try {
                repositorio.importarDesdeJson(campoRuta.getText());
                JOptionPane.showMessageDialog(this, "Importación completada.");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Fichero JSON (por ejemplo, sonoteca.json):"));
        fila.add(campoRuta);
        fila.add(botonElegir);
        fila.add(botonImportar);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }
}
