package alertasegurard.views;

import alertasegurard.dao.UsuarioDAO;
import alertasegurard.models.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Clase que representa la ventana para la gestión de usuarios.
 * Permite visualizar, buscar, actualizar y eliminar usuarios.
 * @author Grupo #1
 */
public class UsuariosFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private final UsuarioDAO usuarioDAO;

    // Componentes para la funcionalidad de búsqueda por cédula.
    private JTextField txtBuscarCedula;
    private JButton btnBuscar;

    /**
     * Constructor de la clase UsuariosFrame.
     * @param parentFrame La ventana principal que se mostrará al cerrar esta ventana.
     */
    public UsuariosFrame(MainMenuFrame parentFrame) {
        // Inicializa el objeto DAO para interactuar con la base de datos de usuarios.
        usuarioDAO = new UsuarioDAO();
        setTitle("Gestión de Usuarios");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Agrega un listener para manejar el cierre de la ventana.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Al cerrar esta ventana, se vuelve a mostrar la ventana principal (MainMenuFrame).
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });
        
        initComponents();
    }

    /**
     * Inicializa y configura todos los componentes de la interfaz de usuario.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel superior que contiene los campos y botones de búsqueda.
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JLabel lblBuscar = new JLabel("Buscar por Cédula:");
        txtBuscarCedula = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        
        panelTop.add(lblBuscar);
        panelTop.add(txtBuscarCedula);
        panelTop.add(btnBuscar);
        
        // Agrega el panel de búsqueda en la parte superior del frame.
        add(panelTop, BorderLayout.NORTH);

        // Configuración de la tabla para mostrar la lista de usuarios.
        String[] columnas = {"ID", "Nombre", "Cédula", "Teléfono", "Email"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Panel inferior para los botones de acciones (Actualizar, Eliminar, Refrescar).
        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar Lista");
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.SOUTH);

        // Carga la lista inicial de usuarios al abrir la ventana.
        cargarUsuarios();

        // Asigna los ActionListeners a los botones.
        btnRefrescar.addActionListener(e -> cargarUsuarios());
        
        // Llama al método buscarUsuarios() cuando se presiona el botón de búsqueda.
        btnBuscar.addActionListener(e -> buscarUsuarios());

        // Manejador de eventos para el botón de eliminar.
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
                return;
            }
            int id = (int) modelo.getValueAt(fila, 0);
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                if (usuarioDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                    cargarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
                }
            }
        });

        // Manejador de eventos para el botón de actualizar.
        btnActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario para actualizar.");
                return;
            }
            int id = (int) modelo.getValueAt(fila, 0);
            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:");
            if (nuevoNombre != null && !nuevoNombre.isBlank()) {
                // Crea un nuevo objeto Usuario con los datos actualizados para pasarlo al DAO.
                Usuario u = new Usuario();
                u.setId(id);
                u.setNombre(nuevoNombre);
                u.setTelefono(modelo.getValueAt(fila, 3).toString());
                u.setEmail(modelo.getValueAt(fila, 4).toString());
                if (usuarioDAO.actualizar(u)) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado.");
                    cargarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }
            }
        });
    }

    /**
     * Carga todos los usuarios desde la base de datos y los muestra en la tabla.
     */
    private void cargarUsuarios() {
        // Limpia las filas actuales del modelo de la tabla.
        modelo.setRowCount(0);
        // Obtiene la lista completa de usuarios del DAO.
        List<Usuario> lista = usuarioDAO.listarTodos();
        // Itera sobre la lista y agrega cada usuario como una nueva fila en la tabla.
        for (Usuario u : lista) {
            modelo.addRow(new Object[]{u.getId(), u.getNombre(), u.getUsuario(), u.getTelefono(), u.getEmail()});
        }
    }

    /**
     * Busca un usuario por cédula en la base de datos y actualiza la tabla
     * con los resultados.
     */
    private void buscarUsuarios() {
        String cedula = txtBuscarCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce una cédula.");
            return;
        }

        // Limpia la tabla antes de mostrar el resultado de la búsqueda.
        modelo.setRowCount(0);

        // Llama al método en UsuarioDAO para buscar un usuario por cédula.
        Usuario usuario = usuarioDAO.buscarPorCedula(cedula);

        if (usuario != null) {
            // Si se encuentra el usuario, lo agrega como una fila en la tabla.
            modelo.addRow(new Object[]{usuario.getId(), usuario.getNombre(), usuario.getUsuario(), usuario.getTelefono(), usuario.getEmail()});
        } else {
            // Muestra un mensaje si no se encuentra el usuario.
            JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con esa cédula.");
        }
    }
}
