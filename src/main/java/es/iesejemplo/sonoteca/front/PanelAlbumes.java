package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Album;
import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pestaña de solo lectura con el listado de álbumes.
 */
public class PanelAlbumes extends JPanel {

    private static final String[] COLUMNAS = {"Título", "Año", "Artista", "Género"};

    private final RepositorioSonoTeca repositorio;
    private final DefaultTableModel modelo;

    public PanelAlbumes(RepositorioSonoTeca repositorio) {
        this.repositorio = repositorio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);

        refrescar();
    }

    /**
     * Vuelve a consultar el repositorio y actualiza la tabla. Llámalo cada vez
     * que esta pestaña pueda mostrar datos nuevos (por ejemplo, al seleccionarla).
     */
    public void refrescar() {
        modelo.setRowCount(0);
        try {
            for (Album album : repositorio.listarAlbumes()) {
                modelo.addRow(new Object[]{
                        album.getTitulo(),
                        album.getAnio(),
                        album.getArtista().getNombre(),
                        album.getGenero().getNombre()
                });
            }
        } catch (Exception ex) {
            // Todavía no implementado: dejamos la tabla vacía sin interrumpir al usuario.
        }
    }
}
