package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Biblioteca;
import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.repositorio.ficheros.RepositorioFicheros;
import es.iesejemplo.sonoteca.util.JsonUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * UNIDAD 2 — Ficheros. Reúne los 4 ejercicios de la práctica:
 * 1) escanear un directorio en busca de MP3, 2) leer su cabecera ID3v1,
 * 3) guardar/cargar la biblioteca en un fichero binario, y 4) exportar/importar
 * la biblioteca a JSON. Debajo, el resto de la aplicación (canciones, álbumes,
 * artistas y géneros, listas) ya funciona sobre este mismo repositorio.
 */
public class PanelUnidad2 extends JPanel {

    private final RepositorioFicheros repositorio;

    public PanelUnidad2(RepositorioFicheros repositorio) {
        this.repositorio = repositorio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel tarjetas = new JPanel();
        tarjetas.setLayout(new BoxLayout(tarjetas, BoxLayout.Y_AXIS));
        tarjetas.add(crearTarjetaEjercicios1y2());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio3());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio4());

        JTabbedPane subPestañas = new JTabbedPane();
        PanelAlbumes panelAlbumes = new PanelAlbumes(repositorio);
        PanelCatalogos panelCatalogos = new PanelCatalogos(repositorio);
        PanelListas panelListas = new PanelListas(repositorio);
        subPestañas.addTab("Canciones", new PanelCanciones(repositorio));
        subPestañas.addTab("Álbumes", panelAlbumes);
        subPestañas.addTab("Artistas y géneros", panelCatalogos);
        subPestañas.addTab("Listas de reproducción", panelListas);

        // Vuelve a consultar álbumes / artistas / géneros / listas cada vez que se entra
        // en esas sub-pestañas, para que reflejen las altas hechas desde las demás pestañas.
        subPestañas.addChangeListener(e -> {
            Component seleccionada = subPestañas.getSelectedComponent();
            if (seleccionada == panelAlbumes) {
                panelAlbumes.refrescar();
            } else if (seleccionada == panelCatalogos) {
                panelCatalogos.refrescar();
            } else if (seleccionada == panelListas) {
                panelListas.refrescar();
            }
        });

        add(new JScrollPane(tarjetas), BorderLayout.NORTH);
        add(subPestañas, BorderLayout.CENTER);
    }

    // ---------- Ejercicios 1 y 2: escanear MP3 y leer ID3v1 ----------

    private JPanel crearTarjetaEjercicios1y2() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo(
                "Ejercicios 1 y 2 — Escanear una carpeta y leer la cabecera ID3v1 de sus MP3");

        JTextField campoRuta = new JTextField(28);
        JButton botonElegir = new JButton("Elegir carpeta…");
        JButton botonEscanear = new JButton("Escanear");

        JPanel filaSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaSuperior.add(new JLabel("Directorio (busca también en subcarpetas):"));
        filaSuperior.add(campoRuta);
        filaSuperior.add(botonElegir);
        filaSuperior.add(botonEscanear);

        DefaultTableModel modeloResultados = FrontUtils.soloLecturaModel(
                new String[]{"Fichero", "Título", "Artista", "Álbum", "Año", "Género"});
        JTable tablaResultados = new JTable(modeloResultados);

        List<Cancion> cancionesEncontradas = new ArrayList<>();

        JButton botonGuardar = new JButton("Guardar canciones encontradas en la biblioteca");
        botonGuardar.setEnabled(false);

        botonElegir.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            selector.setDialogTitle("Elige la carpeta con los MP3 (por ejemplo, mp3-ejemplo/ del proyecto)");
            if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                campoRuta.setText(selector.getSelectedFile().getAbsolutePath());
            }
        });

        botonEscanear.addActionListener(e -> {
            modeloResultados.setRowCount(0);
            cancionesEncontradas.clear();
            botonGuardar.setEnabled(false);
            try {
                List<File> ficheros = repositorio.escanearDirectorio(campoRuta.getText());
                if (ficheros.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "No se han encontrado ficheros .mp3 en esa ruta (¿existe el directorio?).");
                    return;
                }
                for (File fichero : ficheros) {
                    Cancion cancion = repositorio.leerCabeceraID3(fichero);
                    if (cancion != null) {
                        cancionesEncontradas.add(cancion);
                        modeloResultados.addRow(new Object[]{
                                fichero.getName(), cancion.getTitulo(),
                                cancion.getAlbum().getArtista().getNombre(),
                                cancion.getAlbum().getTitulo(),
                                cancion.getAlbum().getAnio(),
                                cancion.getAlbum().getGenero().getNombre()
                        });
                    } else {
                        modeloResultados.addRow(new Object[]{fichero.getName(), "(sin cabecera ID3v1 válida)", "", "", "", ""});
                    }
                }
                botonGuardar.setEnabled(!cancionesEncontradas.isEmpty());
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        botonGuardar.addActionListener(e -> {
            int guardadas = 0;
            for (Cancion cancion : cancionesEncontradas) {
                try {
                    repositorio.altaCancion(cancion);
                    guardadas++;
                } catch (Exception ex) {
                    FrontUtils.mostrarErrorNoImplementado(this, ex);
                    break;
                }
            }
            if (guardadas > 0) {
                JOptionPane.showMessageDialog(this,
                        guardadas + " canción(es) guardada(s). Mira la pestaña \"Canciones\", más abajo.");
            }
        });

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(new JScrollPane(tablaResultados), BorderLayout.CENTER);
        centro.setPreferredSize(new Dimension(0, 160));

        tarjeta.add(filaSuperior, BorderLayout.NORTH);
        tarjeta.add(centro, BorderLayout.CENTER);
        tarjeta.add(botonGuardar, BorderLayout.SOUTH);
        return tarjeta;
    }

    // ---------- Ejercicio 3: serializar / deserializar la biblioteca ----------

    private JPanel crearTarjetaEjercicio3() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo(
                "Ejercicio 3 — Guardar/cargar la biblioteca completa en un fichero binario");

        JTextField campoRuta = new JTextField("sonoteca.dat", 20);
        JButton botonGuardar = new JButton("Guardar biblioteca");
        JButton botonCargar = new JButton("Cargar biblioteca");

        botonGuardar.addActionListener(e -> {
            try {
                Biblioteca biblioteca = new Biblioteca();
                biblioteca.setGeneros(repositorio.listarGeneros());
                biblioteca.setArtistas(repositorio.listarArtistas());
                biblioteca.setAlbumes(repositorio.listarAlbumes());
                biblioteca.setCanciones(repositorio.listarCanciones());
                repositorio.guardarBiblioteca(biblioteca, campoRuta.getText());
                JOptionPane.showMessageDialog(this, "Biblioteca guardada en " + campoRuta.getText());
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        botonCargar.addActionListener(e -> {
            try {
                Biblioteca biblioteca = repositorio.cargarBiblioteca(campoRuta.getText());
                for (Cancion c : biblioteca.getCanciones()) {
                    repositorio.altaCancion(c);
                }
                JOptionPane.showMessageDialog(this,
                        "Biblioteca cargada (" + biblioteca.getCanciones().size() + " canciones). Mira la pestaña \"Canciones\".");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Fichero:"));
        fila.add(campoRuta);
        fila.add(botonGuardar);
        fila.add(botonCargar);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 4: exportar / importar a JSON ----------

    private JPanel crearTarjetaEjercicio4() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo("Ejercicio 4 — Exportar/importar la biblioteca a JSON");

        JButton botonExportar = new JButton("Exportar a JSON…");
        JButton botonImportar = new JButton("Importar desde JSON…");

        botonExportar.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            selector.setSelectedFile(new File("sonoteca.json"));
            if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    Biblioteca biblioteca = new Biblioteca();
                    biblioteca.setGeneros(repositorio.listarGeneros());
                    biblioteca.setArtistas(repositorio.listarArtistas());
                    biblioteca.setAlbumes(repositorio.listarAlbumes());
                    biblioteca.setCanciones(repositorio.listarCanciones());
                    JsonUtils.exportarBibliotecaJson(biblioteca, selector.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Biblioteca exportada correctamente.");
                } catch (Exception ex) {
                    FrontUtils.mostrarErrorNoImplementado(this, ex);
                }
            }
        });

        botonImportar.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    Biblioteca biblioteca = JsonUtils.importarBibliotecaJson(selector.getSelectedFile().getAbsolutePath());
                    for (Cancion c : biblioteca.getCanciones()) {
                        repositorio.altaCancion(c);
                    }
                    JOptionPane.showMessageDialog(this, "Biblioteca importada. Mira la pestaña \"Canciones\".");
                } catch (Exception ex) {
                    FrontUtils.mostrarErrorNoImplementado(this, ex);
                }
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(botonExportar);
        fila.add(botonImportar);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }
}
