package vista;

import modelo.Avenger;
import servicio.GestionAvengers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
public class VentanaPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JPanel panelRegistro, panelModificar, panelListado, panelInforme;
    private JTabbedPane pestanas;
    private JTextField registroId, registroNombre, registroMision, registroPago;
    private JComboBox<Integer> registroPeligrosidad;
    private JButton botonRegistrar;
    private JTextField buscarId, modificarNombre, modificarMision, modificarPago;
    private JComboBox<Integer> modificarPeligrosidad;
    private JButton botonBuscar, botonGuardar;
    private JTable tablaLista, tablaInforme;

    private final GestionAvengers gestion = new GestionAvengers();
    private final DecimalFormat dinero = new DecimalFormat("#,##0.00");
    private Avenger seleccionado;

    private final DefaultTableModel modeloLista = tabla(new String[] {
            "ID", "Nombre", "Misión", "Peligrosidad", "Pago mensual"
    });
    private final DefaultTableModel modeloInforme = tabla(new String[] {
            "ID", "Nombre", "Pago mensual", "Fondo 8 % mensual", "Impuesto anual", "Pago neto mensual"
    });

    public VentanaPrincipal() {
        super("Sistema Avengers");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 520);
        setMinimumSize(new Dimension(760, 440));
        setLocationRelativeTo(null);

        Integer[] niveles = {1, 2, 3, 4, 5};
        registroPeligrosidad.setModel(new DefaultComboBoxModel<>(niveles));
        modificarPeligrosidad.setModel(new DefaultComboBoxModel<>(niveles));
        tablaLista.setModel(modeloLista);
        tablaInforme.setModel(modeloInforme);
        tablaLista.setAutoCreateRowSorter(true);
        tablaInforme.setAutoCreateRowSorter(true);
        tablaLista.setFillsViewportHeight(true);
        tablaInforme.setFillsViewportHeight(true);
        botonRegistrar.addActionListener(e -> registrar());
        botonBuscar.addActionListener(e -> buscar());
        botonGuardar.addActionListener(e -> modificar());
        buscarId.addActionListener(e -> buscar());
        pestanas.addChangeListener(e -> actualizarTablas());
    }



    private static DefaultTableModel tabla(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
    }

    private void registrar() {
        try {
            Avenger nuevo = new Avenger(leerId(registroId), registroNombre.getText(),
                    registroMision.getText(), (Integer) registroPeligrosidad.getSelectedItem(),
                    leerPago(registroPago));
            if (!gestion.registrarAvenger(nuevo)) {
                mensaje("Se encontraron errores en la informacion proporcionada. Intentelo de Nuevo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            registroId.setText(""); registroNombre.setText(""); registroMision.setText("");
            registroPeligrosidad.setSelectedIndex(0); registroPago.setText("");
            actualizarTablas();
            mensaje("Avenger registrado.", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            mensaje(ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscar() {
        try {
            int id = leerId(buscarId);
            seleccionado = gestion.buscarAvenger(id);
            if (seleccionado == null) {
                limpiarModificacion();
                mensaje("No existe un Avenger con ese ID.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            modificarNombre.setText(seleccionado.getNombre());
            modificarMision.setText(seleccionado.getMision());
            modificarPeligrosidad.setSelectedItem(seleccionado.getPeligrosidad());
            modificarPago.setText(Double.toString(seleccionado.getPagoMensual()));
        } catch (IllegalArgumentException ex) {
            seleccionado = null;
            limpiarModificacion();
            mensaje(ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificar() {
        if (seleccionado == null) {
            mensaje("Primero busque un Avenger por ID.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = leerId(buscarId);
            if (id != seleccionado.getId()) {
                seleccionado = null;
                limpiarModificacion();
                mensaje("El ID cambió. Busque nuevamente antes de guardar.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Avenger actualizado = new Avenger(id, modificarNombre.getText(), modificarMision.getText(),
                    (Integer) modificarPeligrosidad.getSelectedItem(), leerPago(modificarPago));
            if (!gestion.modificarAvenger(actualizado)) {
                seleccionado = null;
                mensaje("No se pudo modificar. Busque el ID nuevamente.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            seleccionado = actualizado;
            actualizarTablas();
            mensaje("Avenger modificado.", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            mensaje(ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarModificacion() {
        modificarNombre.setText(""); modificarMision.setText("");
        modificarPeligrosidad.setSelectedIndex(0); modificarPago.setText("");
    }

    private static int leerId(JTextField campo) {
        try { return Integer.parseInt(campo.getText().trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Ingrese un ID numérico válido."); }
    }

    private static double leerPago(JTextField campo) {
        try { return Double.parseDouble(campo.getText().trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Ingrese un pago mensual numérico. Use punto decimal."); }
    }

    private void actualizarTablas() {
        modeloLista.setRowCount(0);
        modeloInforme.setRowCount(0);
        for (Avenger a : gestion.listarAvengers()) {
            modeloLista.addRow(new Object[] {a.getId(), a.getNombre(), a.getMision(),
                    a.getPeligrosidad(), dinero.format(a.getPagoMensual())});
            modeloInforme.addRow(new Object[] {a.getId(), a.getNombre(), dinero.format(a.getPagoMensual()),
                    dinero.format(a.calcularFondoHeroes()), dinero.format(a.calcularImpuesto()),
                    dinero.format(a.calcularPagoNeto())});
        }
    }

    private void mensaje(String texto, int tipo) {
        JOptionPane.showMessageDialog(this, texto, "Sistema Avengers", tipo);
    }



}
