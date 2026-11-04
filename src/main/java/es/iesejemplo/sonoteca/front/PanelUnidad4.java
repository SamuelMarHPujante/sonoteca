package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.modelo.ListaReproduccion;
import es.iesejemplo.sonoteca.repositorio.hibernate.RepositorioHibernate;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * UNIDAD 4 — Hibernate. Ejercicios 2 a 5 de la práctica (el ejercicio 1,
 * mapear las entidades, no tiene una pantalla propia: se ve en que el resto
 * de operaciones empiecen a funcionar).
 */
public class PanelUnidad4 extends JPanel {

    private final RepositorioHibernate repositorio;

    public PanelUnidad4(RepositorioHibernate repositorio) {
        this.repositorio = repositorio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel tarjetas = new JPanel();
        tarjetas.setLayout(new BoxLayout(tarjetas, BoxLayout.Y_AXIS));
        tarjetas.add(crearTarjetaEjercicio2());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio3());
        tarjetas.add(Box.createVerticalStrut(8));
        tarjetas.add(crearTarjetaEjercicio5());

        JTabbedPane subPestañas = new JTabbedPane();
        subPestañas.addTab("Canciones", new PanelCanciones(repositorio));
        subPestañas.addTab("Álbumes", new PanelAlbumes(repositorio));
        subPestañas.addTab("Artistas y géneros", new PanelCatalogos(repositorio));
        subPestañas.addTab("Listas de reproducción (ejercicio 4)", new PanelListas(repositorio));

        add(new JScrollPane(tarjetas), BorderLayout.NORTH);
        add(subPestañas, BorderLayout.CENTER);
    }

    // ---------- Ejercicio 2: buscar canción por id ----------

    private JPanel crearTarjetaEjercicio2() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo("Ejercicio 2 — Buscar una canción por su id (session.get)");

        JSpinner spinnerId = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        JLabel resultado = new JLabel(" ");
        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(e -> {
            try {
                Cancion cancion = repositorio.buscarPorId((Integer) spinnerId.getValue());
                resultado.setText(cancion == null
                        ? "No existe ninguna canción con ese id."
                        : "Encontrada: " + cancion.getTitulo() + " — " + cancion.getAlbum().getArtista().getNombre());
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Id:"));
        fila.add(spinnerId);
        fila.add(botonBuscar);
        fila.add(resultado);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 3: consultas HQL ----------

    private JPanel crearTarjetaEjercicio3() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo("Ejercicio 3 — Consultas HQL");

        DefaultListModel<String> modeloResultados = new DefaultListModel<>();
        JList<String> listaResultados = new JList<>(modeloResultados);

        JSpinner spinnerIdAlbum = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        JButton botonPorAlbum = new JButton("Canciones de este álbum");
        botonPorAlbum.addActionListener(e -> ejecutarYMostrar(modeloResultados,
                () -> repositorio.cancionesDeAlbum((Integer) spinnerIdAlbum.getValue())
                        .stream().map(Cancion::getTitulo).toList()));

        JTextField campoArtista = new JTextField(15);
        JButton botonPorArtista = new JButton("Canciones de este artista");
        botonPorArtista.addActionListener(e -> ejecutarYMostrar(modeloResultados,
                () -> repositorio.cancionesDeArtista(campoArtista.getText())
                        .stream().map(Cancion::getTitulo).toList()));

        JButton botonSinAlbumes = new JButton("Artistas sin álbumes");
        botonSinAlbumes.addActionListener(e -> ejecutarYMostrar(modeloResultados,
                () -> repositorio.artistasSinAlbumes().stream().map(Object::toString).toList()));

        JPanel filaControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaControles.add(new JLabel("Id álbum:"));
        filaControles.add(spinnerIdAlbum);
        filaControles.add(botonPorAlbum);
        filaControles.add(new JLabel("  Artista:"));
        filaControles.add(campoArtista);
        filaControles.add(botonPorArtista);
        filaControles.add(botonSinAlbumes);

        JScrollPane scroll = new JScrollPane(listaResultados);
        scroll.setPreferredSize(new Dimension(0, 120));

        tarjeta.add(filaControles, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    // ---------- Ejercicio 5: mover canción de una lista a otra (transacción) ----------

    private JPanel crearTarjetaEjercicio5() {
        JPanel tarjeta = FrontUtils.tarjetaConTitulo(
                "Ejercicio 5 — Mover una canción de una lista a otra (operación transaccional)");

        JSpinner spinnerIdCancion = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        JSpinner spinnerIdListaOrigen = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        JSpinner spinnerIdListaDestino = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        JSpinner spinnerPosicion = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        JButton botonMover = new JButton("Mover");

        botonMover.addActionListener(e -> {
            try {
                repositorio.moverCancionDeLista(
                        (Integer) spinnerIdCancion.getValue(),
                        (Integer) spinnerIdListaOrigen.getValue(),
                        (Integer) spinnerIdListaDestino.getValue(),
                        (Integer) spinnerPosicion.getValue());
                JOptionPane.showMessageDialog(this, "Canción movida correctamente.");
            } catch (Exception ex) {
                FrontUtils.mostrarErrorNoImplementado(this, ex);
            }
        });

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("Canción:"));
        fila.add(spinnerIdCancion);
        fila.add(new JLabel("Lista origen:"));
        fila.add(spinnerIdListaOrigen);
        fila.add(new JLabel("Lista destino:"));
        fila.add(spinnerIdListaDestino);
        fila.add(new JLabel("Nueva posición:"));
        fila.add(spinnerPosicion);
        fila.add(botonMover);
        tarjeta.add(fila, BorderLayout.CENTER);
        return tarjeta;
    }

    private interface Operacion {
        List<String> ejecutar();
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
}
