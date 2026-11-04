package es.iesejemplo.sonoteca.front;

import es.iesejemplo.sonoteca.modelo.Album;
import es.iesejemplo.sonoteca.modelo.Artista;
import es.iesejemplo.sonoteca.modelo.Cancion;
import es.iesejemplo.sonoteca.modelo.Genero;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario modal para crear o editar una canción. Como el modelo relaciona
 * Cancion -> Album -> (Artista, Genero), este formulario pide también los
 * datos del álbum/artista/género: si ya existen (mismo nombre), la
 * implementación del repositorio debe reutilizarlos; si no, debe crearlos.
 */
public class DialogoCancion extends JDialog {

    private final JTextField campoTitulo = new JTextField(20);
    private final JSpinner campoDuracionSegundos = new JSpinner(new SpinnerNumberModel(180, 0, 36000, 1));
    private final JSpinner campoNumeroPista = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
    private final JTextField campoAlbum = new JTextField(20);
    private final JSpinner campoAnioAlbum = new JSpinner(new SpinnerNumberModel(2024, 1900, 2100, 1));
    private final JTextField campoArtista = new JTextField(20);
    private final JTextField campoNacionalidadArtista = new JTextField(20);
    private final JTextField campoGenero = new JTextField(20);

    private final Cancion cancionOriginal; // null si es un alta
    private boolean aceptado = false;

    public DialogoCancion(Frame propietario, Cancion cancionAEditar) {
        super(propietario, cancionAEditar == null ? "Nueva canción" : "Editar canción", true);
        this.cancionOriginal = cancionAEditar;

        setLayout(new BorderLayout(8, 8));
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);

        if (cancionAEditar != null) {
            rellenarConCancion(cancionAEditar);
        }

        pack();
        setLocationRelativeTo(propietario);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        int fila = 0;
        fila = añadirFila(panel, c, fila, "Título de la canción:", campoTitulo);
        fila = añadirFila(panel, c, fila, "Duración (segundos):", campoDuracionSegundos);
        fila = añadirFila(panel, c, fila, "Número de pista:", campoNumeroPista);
        fila = añadirFila(panel, c, fila, "Álbum:", campoAlbum);
        fila = añadirFila(panel, c, fila, "Año del álbum:", campoAnioAlbum);
        fila = añadirFila(panel, c, fila, "Artista:", campoArtista);
        fila = añadirFila(panel, c, fila, "Nacionalidad del artista:", campoNacionalidadArtista);
        añadirFila(panel, c, fila, "Género:", campoGenero);

        return panel;
    }

    private int añadirFila(JPanel panel, GridBagConstraints c, int fila, String etiqueta, JComponent campo) {
        c.gridx = 0;
        c.gridy = fila;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        panel.add(campo, c);
        return fila + 1;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        panel.add(botonCancelar);

        JButton botonAceptar = new JButton("Guardar");
        botonAceptar.addActionListener(e -> {
            if (campoTitulo.getText().isBlank() || campoAlbum.getText().isBlank() || campoArtista.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Título, álbum y artista son obligatorios.");
                return;
            }
            aceptado = true;
            dispose();
        });
        panel.add(botonAceptar);

        getRootPane().setDefaultButton(botonAceptar);
        return panel;
    }

    private void rellenarConCancion(Cancion cancion) {
        campoTitulo.setText(cancion.getTitulo());
        campoDuracionSegundos.setValue(cancion.getDuracionSegundos());
        campoNumeroPista.setValue(cancion.getNumeroPista());
        campoAlbum.setText(cancion.getAlbum().getTitulo());
        campoAnioAlbum.setValue(cancion.getAlbum().getAnio());
        campoArtista.setText(cancion.getAlbum().getArtista().getNombre());
        campoNacionalidadArtista.setText(cancion.getAlbum().getArtista().getNacionalidad());
        campoGenero.setText(cancion.getAlbum().getGenero().getNombre());
    }

    public boolean isAceptado() {
        return aceptado;
    }

    /**
     * Construye el objeto Cancion con los datos introducidos. Si se está editando,
     * conserva el id original de la canción (el del álbum/artista/género se
     * resuelve en el repositorio, no aquí).
     */
    public Cancion getCancion() {
        Artista artista = new Artista();
        artista.setNombre(campoArtista.getText().trim());
        artista.setNacionalidad(campoNacionalidadArtista.getText().trim());

        Genero genero = new Genero(campoGenero.getText().trim());

        Album album = new Album();
        album.setTitulo(campoAlbum.getText().trim());
        album.setAnio((Integer) campoAnioAlbum.getValue());
        album.setArtista(artista);
        album.setGenero(genero);

        Cancion cancion = new Cancion();
        if (cancionOriginal != null) {
            cancion.setId(cancionOriginal.getId());
        }
        cancion.setTitulo(campoTitulo.getText().trim());
        cancion.setDuracionSegundos((Integer) campoDuracionSegundos.getValue());
        cancion.setNumeroPista((Integer) campoNumeroPista.getValue());
        cancion.setAlbum(album);
        return cancion;
    }
}
