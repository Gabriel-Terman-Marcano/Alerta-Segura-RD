package controllers;

import alertasegurard.dao.UsuarioDAO;
import alertasegurard.models.Usuario;

import java.util.List;

/**
 * Clase controladora para la gestión de las operaciones de la entidad Usuario.
 *
 * <p>Esta clase actúa como una capa de servicio entre la interfaz de usuario
 * y la capa de acceso a datos (DAO), proporcionando la lógica de negocio
 * para crear, leer, actualizar, eliminar y buscar usuarios.</p>
 *
 * @author G1
 */
public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor de la clase UsuarioController.
     * <p>Inicializa la instancia de {@link UsuarioDAO} que se utilizará
     * para interactuar con la base de datos de usuarios.</p>
     */
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Obtiene una lista de todos los usuarios activos.
     *
     * @return una {@link List} de objetos {@link Usuario}.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    /**
     * Actualiza la información de un usuario existente.
     * <p>Realiza una validación simple para asegurar que el objeto usuario y su
     * ID sean válidos antes de intentar la actualización.</p>
     *
     * @param u el objeto {@link Usuario} con la información actualizada.
     * @return {@code true} si el usuario se actualiza exitosamente, {@code false} en caso contrario.
     */
    public boolean actualizarUsuario(Usuario u) {
        if (u == null || u.getId() <= 0) {
            return false;
        }
        return usuarioDAO.actualizar(u);
    }

    /**
     * Elimina un usuario de forma lógica por su ID.
     *
     * @param id el ID del usuario a "eliminar" (cambiar su estado a 'inactivo').
     * @return {@code true} si el usuario se elimina exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarUsuario(int id) {
        return usuarioDAO.eliminar(id);
    }

    /**
     * Busca usuarios cuyo nombre, cédula o email coincidan con un término de búsqueda.
     *
     * @param termino el término de búsqueda.
     * @return una {@link List} de objetos {@link Usuario} que coinciden con el término.
     */
    public List<Usuario> buscar(String termino) {
        return usuarioDAO.buscar(termino);
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     * <p>Realiza validaciones para asegurar que el objeto {@link Usuario} y sus
     * campos obligatorios no sean nulos. También verifica si un usuario con
     * la misma cédula ya existe antes de intentar la creación.</p>
     *
     * @param u el objeto {@link Usuario} a crear.
     * @return {@code true} si el usuario se crea exitosamente, {@code false} en caso de error.
     */
    public boolean crearUsuario(Usuario u) {
        if (u == null || u.getUsuario() == null || u.getPassword() == null) {
            return false;
        }
        if (usuarioDAO.existeUsuario(u.getUsuario())) {
            return false;
        }
        return usuarioDAO.crear(u);
    }
}
