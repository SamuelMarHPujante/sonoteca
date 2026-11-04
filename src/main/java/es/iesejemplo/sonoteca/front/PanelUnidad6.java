package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Album;
import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.repositorio.mongo.RepositorioMongo;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * UNIDAD 6 — MongoDB. Ejercicios 2 a 6 de la práctica (el ejercicio 1,
 * el diseño de la colección en DISEÑO.md, no tiene pantalla propia).
 */
public class PanelUnidad6 extends JPanel {

    private final RepositorioMongo repositorio;

    public PanelUnidad6(RepositorioMongo repositorio) {
        this.repositorio = repositorio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        if (repositorio == null) {
            JTextArea aviso = new JTextArea(
                    "No se ha podido conectar con MongoDB (¿está arrancado el servicio en localhost:27017?).\n\n" +
                    "Arranca MongoDB y vuelve a abrir la aplicación para poder usar esta pestaña.");
            aviso.setEditable(false);
            aviso.setLineWrap(true);
            aviso.setWrapStyleWord(true);
            aviso.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            add(aviso, BorderLayout.CENTER);
            return;
        }

        JPanel tarjetas = new JPanel();
        tarjetas.setLayout(new BoxLayout(tarjetas, BoxLayout.Y_AXIS));
        tarjetas.add(crearTarjetaEjercicio2());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio3());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio4());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio5());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio6());

        JTabbedPane subPestañas = new JTabbedPane();
        subPestañas.addTab("Canciones", new PanelCanciones(repositorio));
        subPestañas.addTab("Álbumes", new PanelAlbumes(repositorio));
        subPestañas.addTab("Artistas y géneros", new PanelCatalogos(repositorio));
        subPestañas.addTab("Listas de reproducción", new PanelListas(repositorio));

        add(new JScrollPane(tarjetas), BorderLayout.NORTH);
        add(subPestañas, BorderLayout.CENTER);
    }

    // ---------- Ejercicio 2: alta / importación ----------

    private JPanel crearTarjetaEjercicio2() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo("Ejercicio 2 — Dar de alta datos e importar desde JSON");

        JTextField campoRuta = new JTextField(24);
        JButton botonElegir = new JButton("Elegir sonoteca.json…");
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
                JOptionPane.showMessageDialog(this, "Importación completada. Mira la pestaña \"Canciones\".");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Fichero (por ejemplo, sonoteca.json de la unidad 2):"));
        fila.add(campoRuta);
        fila.add(botonElegir);
        fila.add(botonImportar);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 3: consultas con filtros ----------

    private JPanel crearTarjetaEjercicio3() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo("Ejercicio 3 — Consultas con filtros");

        DefaultListModel<String> modeloResultados = new DefaultListModel<>();
        JList<String> listaResultados = new JList<>(modeloResultados);

        JTextField campoGenero = new JTextField(10);
        JButton botonPorGenero = new JButton("Canciones de este género");
        botonPorGenero.addActionListener(e -> ejecutarYMostrar(modeloResultados,
                () -> repositorio.cancionesDeGenero(campoGenero.getText()).stream().map(Cancion::getTitulo).toList()));

        JTextField campoArtista = new JTextField(10);
        JButton botonPorArtista = new JButton("Canciones de este artista");
        botonPorArtista.addActionListener(e -> ejecutarYMostrar(modeloResultados,
                () -> repositorio.cancionesDeArtista(campoArtista.getText()).stream().map(Cancion::getTitulo).toList()));

        JSpinner spinnerDesde = new JSpinner(new SpinnerNumberModel(1970, 1900, 2100, 1));
        JSpinner spinnerHasta = new JSpinner(new SpinnerNumberModel(2026, 1900, 2100, 1));
        JButton botonPorAnios = new JButton("Álbumes entre esos años");
        botonPorAnios.addActionListener(e -> ejecutarYMostrar(modeloResultados,
                () -> repositorio.albumesEntreAnios((Integer) spinnerDesde.getValue(), (Integer) spinnerHasta.getValue())
                        .stream().map(Album::getTitulo).toList()));

        JPanel filaControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaControles.add(new JLabel("Género:"));
        filaControles.add(campoGenero);
        filaControles.add(botonPorGenero);
        filaControles.add(new JLabel("  Artista:"));
        filaControles.add(campoArtista);
        filaControles.add(botonPorArtista);
        filaControles.add(new JLabel("  Años:"));
        filaControles.add(spinnerDesde);
        filaControles.add(spinnerHasta);
        filaControles.add(botonPorAnios);

        JScrollPane scroll = new JScrollPane(listaResultados);
        scroll.setPreferredSize(new Dimension(0, 120));

        tarjeta.add(filaControles, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 4: actualizar / borrar por id ----------

    private JPanel crearTarjetaEjercicio4() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo("Ejercicio 4 — Actualizar y borrar canciones por id");

        JTextField campoId = new JTextField(18);
        JTextField campoTitulo = new JTextField(14);
        JButton botonCorregir = new JButton("Corregir título");
        botonCorregir.addActionListener(e -> {
            try {
                repositorio.corregirTitulo(campoId.getText(), campoTitulo.getText());
                JOptionPane.showMessageDialog(this, "Título corregido.");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JCheckBox checkFavorita = new JCheckBox("Favorita");
        JButton botonFavorita = new JButton("Marcar / desmarcar favorita");
        botonFavorita.addActionListener(e -> {
            try {
                repositorio.marcarFavorita(campoId.getText(), checkFavorita.isSelected());
                JOptionPane.showMessageDialog(this, "Actualizado.");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JButton botonBorrar = new JButton("Borrar canción");
        botonBorrar.addActionListener(e -> {
            try {
                repositorio.borrarCancion(campoId.getText());
                JOptionPane.showMessageDialog(this, "Canción borrada (si existía).");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Id de la canción (_id de Mongo):"));
        fila.add(campoId);
        fila.add(new JLabel("Nuevo título:"));
        fila.add(campoTitulo);
        fila.add(botonCorregir);
        fila.add(checkFavorita);
        fila.add(botonFavorita);
        fila.add(botonBorrar);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 5: agregaciones ----------

    private JPanel crearTarjetaEjercicio5() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo("Ejercicio 5 — Agregaciones");

        DefaultTableModel modelo = FrontUtils.soloLecturaModel(new String[]{"Resultado"});
        JTable tabla = new JTable(modelo);

        JButton botonPorGenero = new JButton("Nº de canciones por género");
        botonPorGenero.addActionListener(e -> mostrarDocumentos(modelo, repositorio::cancionesPorGenero));

        JSpinner spinnerN = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
        JButton botonTopArtistas = new JButton("Top artistas por nº de álbumes");
        botonTopArtistas.addActionListener(e ->
                mostrarDocumentos(modelo, () -> repositorio.topArtistasPorNumeroDeAlbumes((Integer) spinnerN.getValue())));

        JButton botonConDatos = new JButton("Canciones con datos de su álbum");
        botonConDatos.addActionListener(e -> mostrarDocumentos(modelo, repositorio::cancionesConDatosDeAlbum));

        JPanel filaControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaControles.add(botonPorGenero);
        filaControles.add(new JLabel("  N:"));
        filaControles.add(spinnerN);
        filaControles.add(botonTopArtistas);
        filaControles.add(botonConDatos);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 140));

        tarjeta.add(filaControles, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 6: validación con JSON Schema ----------

    private JPanel crearTarjetaEjercicio6() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo(
                "Ejercicio 6 — Configurar la validación de la colección \"canciones\" (JSON Schema)");

        JButton botonConfigurar = new JButton("Crear/recrear colección con validación");
        botonConfigurar.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "Esto recreará la colección \"canciones\" (se perderán los datos que tenga). ¿Continuar?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                repositorio.configurarValidacionCanciones();
                JOptionPane.showMessageDialog(this, "Validación configurada.");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(botonConfigurar);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    private interface Operacion {
        List<String> ejecutar();
    }

    private interface OperacionDocumentos {
        List<Document> ejecutar();
    }

    private void ejecutarYMostrar(DefaultListModel<String> modelo, Operacion operacion) {
        modelo.clear();
        try {
            for (String linea : operacion.ejecutar()) {
                modelo.addElement(linea);
            }
            if (modelo.isEmpty()) {
                modelo.addElement("(sin resultados)");
            }
        } catch (Exception ex) {
            FrontUtils.mostrarErrorNoImplementado(this, ex);
        }
    }

    private void mostrarDocumentos(DefaultTableModel modelo, OperacionDocumentos operacion) {
        modelo.setRowCount(0);
        try {
            List<Document> resultado = operacion.ejecutar();
            for (Document documento : resultado) {
                modelo.addRow(new Object[]{documento.toJson()});
            }
            if (resultado.isEmpty()) {
                modelo.addRow(new Object[]{"(sin resultados)"});
            }
        } catch (Exception ex) {
            FrontUtils.mostrarErrorNoImplementado(this, ex);
        }
    }
}
