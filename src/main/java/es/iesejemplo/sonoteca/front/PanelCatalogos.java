package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.repositorio.RepositorioSonoTeca;

import javax.swing.*;
import java.awt.*;

/**
 * Pestaña de solo lectura con los listados de artistas y géneros.
 */
public class PanelCatalogos extends JPanel {

    private final RepositorioSonoTeca repositorio;
    private final DefaultListModel<String> modeloArtistas = new DefaultListModel<>();
    private final DefaultListModel<String> modeloGeneros = new DefaultListModel<>();

    public PanelCatalogos(RepositorioSonoTeca repositorio) {
        this.repositorio = repositorio;
        setLayout(new GridLayout(1, 2, 8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(crearPanelLista("Artistas", modeloArtistas));
        add(crearPanelLista("Géneros", modeloGeneros));

        refrescar();
    }

    /**
     * Vuelve a consultar el repositorio y actualiza las dos listas. Llámalo
     * cada vez que esta pestaña pueda mostrar datos nuevos (por ejemplo, al
     * seleccionarla).
     */
    public void refrescar() {
        modeloArtistas.clear();
        try {
            repositorio.listarArtistas().forEach(a ->
                    modeloArtistas.addElement(a.getNombre() + (a.getNacionalidad() != null ? " (" + a.getNacionalidad() + ")" : "")));
        } catch (Exception ex) {
            // Todavía no implementado: dejamos la lista vacía sin interrumpir al usuario.
        }

        modeloGeneros.clear();
        try {
            repositorio.listarGeneros().forEach(g -> modeloGeneros.addElement(g.getNombre()));
        } catch (Exception ex) {
            // Todavía no implementado: dejamos la lista vacía sin interrumpir al usuario.
        }
    }

    private JPanel crearPanelLista(String titulo, DefaultListModel<String> modelo) {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(new JLabel(titulo), BorderLayout.NORTH);
        panel.add(new JScrollPane(new JList<>(modelo)), BorderLayout.CENTER);
        return panel;
    }
}
