package es.iesejemplo.sonoteca.front;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Utilidades compartidas por los paneles de unidad (tarjetas con título,
 * tablas de solo lectura, y el aviso estándar de "todavía no implementado").
 */
final class FrontUtils {

    private FrontUtils() {
    }

    static JPanel tarjetaConTitulo(String titulo) {
        JPanel tarjeta = new JPanel(new BorderLayout(6, 6));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(titulo),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tarjeta;
    }

    static DefaultTableModel soloLecturaModel(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    static void mostrarErrorNoImplementado(Component padre, Exception ex) {
        JOptionPane.showMessageDialog(padre,
                "Esta operación todavía no está implementada:\n" + ex,
                "Funcionalidad no disponible todavía", JOptionPane.WARNING_MESSAGE);
    }
}
