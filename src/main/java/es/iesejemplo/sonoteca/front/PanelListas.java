package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.modelo.ListaReproduccion;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pestaña de listas de reproducción: crear listas, añadirles canciones y ver
 * su contenido ordenado por posición.
 */
public class PanelListas extends JPanel {

    private final RepositorioSonoTeca repositorio;
    private final DefaultListModel<ListaReproduccion> modeloListas = new DefaultListModel<>();
    private final JList<ListaReproduccion> listaDeListas = new JList<>(modeloListas);
    private final JComboBox<Cancion> comboCanciones = new JComboBox<>();
    private final DefaultTableModel modeloCanciones = new DefaultTableModel(new String[]{"Posición", "Canción"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public PanelListas(RepositorioSonoTeca repositorio) {
        this.repositorio = repositorio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(crearPanelIzquierdo(), BorderLayout.WEST);
        add(new JScrollPane(new JTable(modeloCanciones)), BorderLayout.CENTER);
        add(crearPanelAñadirCancion(), BorderLayout.SOUTH);

        listaDeListas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                recargarCancionesDeListaSeleccionada();
            }
        });

        refrescar();
    }

    /**
     * Vuelve a consultar el repositorio y actualiza tanto la lista de listas
     * como el combo de canciones disponibles. Llámalo cada vez que esta
     * pestaña pueda mostrar datos nuevos (por ejemplo, al seleccionarla).
     */
    public void refrescar() {
        recargarListas();
        refrescarComboCanciones();
    }

    private void refrescarComboCanciones() {
        comboCanciones.removeAllItems();
        try {
            repositorio.listarCanciones().forEach(comboCanciones::addItem);
        } catch (Exception ex) {
            // Todavía no implementado: dejamos el combo vacío sin interrumpir al usuario.
        }
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setPreferredSize(new Dimension(220, 0));
        panel.add(new JLabel("Listas de reproducción"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listaDeListas), BorderLayout.CENTER);

        JButton botonNuevaLista = new JButton("Nueva lista…");
        botonNuevaLista.addActionListener(e -> nuevaLista());
        panel.add(botonNuevaLista, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelAñadirCancion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JSpinner spinnerPosicion = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        JButton botonAñadir = new JButton("Añadir canción a la lista seleccionada");
        botonAñadir.addActionListener(e -> {
            ListaReproduccion lista = listaDeListas.getSelectedValue();
            Cancion cancion = (Cancion) comboCanciones.getSelectedItem();
            if (lista == null || cancion == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una lista y una canción.");
                return;
            }
            try {
                repositorio.anyadirCancionALista(lista.getId(), cancion.getId(), (Integer) spinnerPosicion.getValue());
                recargarCancionesDeListaSeleccionada();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Esta operación todavía no está implementada en tu repositorio:\n" + ex,
                        "Funcionalidad no disponible todavía", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(new JLabel("Canción:"));
        panel.add(comboCanciones);
        panel.add(new JLabel("Posición:"));
        panel.add(spinnerPosicion);
        panel.add(botonAñadir);
        return panel;
    }

    private void nuevaLista() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva lista:");
        if (nombre != null && !nombre.isBlank()) {
            try {
                repositorio.altaLista(new ListaReproduccion(nombre.trim()));
                recargarListas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Esta operación todavía no está implementada en tu repositorio:\n" + ex,
                        "Funcionalidad no disponible todavía", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void recargarListas() {
        modeloListas.clear();
        try {
            repositorio.listarListas().forEach(modeloListas::addElement);
        } catch (Exception ex) {
            // Todavía no implementado: dejamos la lista vacía sin interrumpir al usuario.
        }
    }

    private void recargarCancionesDeListaSeleccionada() {
        modeloCanciones.setRowCount(0);
        ListaReproduccion lista = listaDeListas.getSelectedValue();
        if (lista == null) {
            return;
        }
        try {
            List<Cancion> canciones = repositorio.cancionesDeLista(lista.getId());
            int posicion = 1;
            for (Cancion c : canciones) {
                modeloCanciones.addRow(new Object[]{posicion++, c.getTitulo()});
            }
        } catch (Exception ex) {
            // Todavía no implementado: dejamos la tabla vacía sin interrumpir al usuario.
        }
    }
}
