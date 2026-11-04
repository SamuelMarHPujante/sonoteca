package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pestaña principal: listado de canciones, con alta, edición, borrado y búsqueda.
 */
public class PanelCanciones extends JPanel {

    private final RepositorioSonoTeca repositorio;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JTextField campoBusqueda = new JTextField(20);
    private List<Cancion> cancionesMostradas;

    private static final String[] COLUMNAS = {"Título", "Duración", "Pista", "Álbum", "Artista", "Género", "Año"};

    public PanelCanciones(RepositorioSonoTeca repositorio) {
        this.repositorio = repositorio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        try {
            recargarTabla(repositorio.listarCanciones());
        } catch (Exception ex) {
            recargarTabla(java.util.List.of());
        }
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Buscar:"));
        panel.add(campoBusqueda);
        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(e -> recargarTabla(repositorio.buscarCanciones(campoBusqueda.getText())));
        panel.add(botonBuscar);

        JButton botonMostrarTodas = new JButton("Mostrar todas");
        botonMostrarTodas.addActionListener(e -> {
            campoBusqueda.setText("");
            recargarTabla(repositorio.listarCanciones());
        });
        panel.add(botonMostrarTodas);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton botonNueva = new JButton("Nueva canción…");
        botonNueva.addActionListener(e -> nuevaCancion());
        panel.add(botonNueva);

        JButton botonEditar = new JButton("Editar seleccionada…");
        botonEditar.addActionListener(e -> editarCancionSeleccionada());
        panel.add(botonEditar);

        JButton botonEliminar = new JButton("Eliminar seleccionada");
        botonEliminar.addActionListener(e -> eliminarCancionSeleccionada());
        panel.add(botonEliminar);

        return panel;
    }

    private void nuevaCancion() {
        DialogoCancion dialogo = new DialogoCancion((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialogo.setVisible(true);
        if (dialogo.isAceptado()) {
            try {
                repositorio.altaCancion(dialogo.getCancion());
                recargarTabla(repositorio.listarCanciones());
            } catch (Exception ex) {
                mostrarErrorNoImplementado(ex);
            }
        }
    }

    private void editarCancionSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una canción de la tabla.");
            return;
        }
        Cancion cancion = cancionesMostradas.get(fila);
        DialogoCancion dialogo = new DialogoCancion((Frame) SwingUtilities.getWindowAncestor(this), cancion);
        dialogo.setVisible(true);
        if (dialogo.isAceptado()) {
            try {
                repositorio.actualizarCancion(dialogo.getCancion());
                recargarTabla(repositorio.listarCanciones());
            } catch (Exception ex) {
                mostrarErrorNoImplementado(ex);
            }
        }
    }

    private void eliminarCancionSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una canción de la tabla.");
            return;
        }
        Cancion cancion = cancionesMostradas.get(fila);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + cancion.getTitulo() + "\"?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                repositorio.eliminarCancion(cancion.getId());
                recargarTabla(repositorio.listarCanciones());
            } catch (Exception ex) {
                mostrarErrorNoImplementado(ex);
            }
        }
    }

    private void recargarTabla(List<Cancion> canciones) {
        this.cancionesMostradas = canciones;
        modeloTabla.setRowCount(0);
        for (Cancion c : canciones) {
            modeloTabla.addRow(new Object[]{
                    c.getTitulo(),
                    FormatoUtils.segundosATexto(c.getDuracionSegundos()),
                    c.getNumeroPista(),
                    c.getAlbum().getTitulo(),
                    c.getAlbum().getArtista().getNombre(),
                    c.getAlbum().getGenero().getNombre(),
                    c.getAlbum().getAnio()
            });
        }
    }

    private void mostrarErrorNoImplementado(Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Esta operación todavía no está implementada en tu repositorio:\n" + ex,
                "Funcionalidad no disponible todavía", JOptionPane.WARNING_MESSAGE);
    }
}
