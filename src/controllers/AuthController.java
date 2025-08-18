package controllers;

import alertasegurard.dao.UsuarioDAO;
import alertasegurard.models.Usuario;

/**
 * Clase controladora para la gestión de la autenticación de usuarios.
 *
 * <p>Esta clase se encarga de la lógica de negocio para el inicio de sesión
 * y el registro de nuevos usuarios, interactuando con la capa de acceso a
 * datos (DAO) para realizar las operaciones necesarias en la base de datos.</p>
 *
 * @author G1
 */
public class AuthController {

    private UsuarioDAO usuarioDAO;

    /**
     * Constructor de la clase AuthController.
     * <p>Inicializa la instancia de {@link UsuarioDAO} que se utilizará
     * para interactuar con la base de datos de usuarios.</p>
     */
    public AuthController() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autentica a un usuario para el inicio de sesión.
     *
     * <p>Valida que la cédula y la contraseña no estén vacías antes de
     * llamar al método de autenticación del DAO.</p>
     *
     * @param cedula la cédula del usuario.
     * @param password la contraseña del usuario.
     * @return un objeto {@link Usuario} si el inicio de sesión es exitoso,
     * o {@code null} si las credenciales son inválidas o están vacías.
     */
    public Usuario login(String cedula, String password) {
        if (cedula == null || cedula.isBlank() || password == null || password.isBlank()) {
            return null;
        }
        return usuarioDAO.autenticar(cedula, password);
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * <p>Realiza validaciones para asegurar que el objeto {@link Usuario} y sus
     * campos obligatorios no sean nulos. También verifica si un usuario con
     * la misma cédula ya existe en la base de datos antes de intentar crearlo.</p>
     *
     * @param nuevo el objeto {@link Usuario} con los datos del nuevo usuario.
     * @return {@code true} si el registro se realiza con éxito, {@code false} en
     * caso de error en la validación o si el usuario ya existe.
     */
    public boolean registrar(Usuario nuevo) {
        if (nuevo == null || nuevo.getUsuario() == null || nuevo.getPassword() == null) {
            return false;
        }
        if (usuarioDAO.existeUsuario(nuevo.getUsuario())) {
            return false;
        }
        return usuarioDAO.crear(nuevo);
    }
}
