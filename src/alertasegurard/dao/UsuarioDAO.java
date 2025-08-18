package alertasegurard.dao;

import alertasegurard.models.Usuario;
import java.sql.*;
import java.util.*;

/**
 * Clase de Objeto de Acceso a Datos (DAO) para la entidad {@link Usuario}.
 *
 * <p>Esta clase proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y otras consultas relacionadas con la tabla de usuarios en la base de datos.</p>
 *
 * @author G1
 */
public class UsuarioDAO {

    private Connection conn;

    /**
     * Constructor de la clase UsuarioDAO.
     * <p>Al instanciar un objeto de esta clase, se establece una conexión a la base
     * de datos a través de la clase {@link DatabaseConnection}, que implementa el patrón Singleton.</p>
     */
    public UsuarioDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario el objeto {@link Usuario} con los datos del nuevo usuario.
     * @return {@code true} si la inserción se realiza con éxito (se afectó al menos una fila),
     * {@code false} en caso de error o si no se insertó ninguna fila.
     */
    public boolean crear(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, cedula, telefono, email, direccion, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asigna los valores del objeto Usuario a los parámetros de la consulta.
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getUsuario()); // En este caso, el campo 'usuario' del modelo se usa para la 'cédula'.
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getDireccion());
            stmt.setString(6, usuario.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al crear el usuario en la base de datos.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Autentica a un usuario verificando su cédula y contraseña.
     *
     * @param cedula la cédula del usuario a autenticar.
     * @param password la contraseña del usuario.
     * @return un objeto {@link Usuario} si las credenciales son correctas y el usuario está activo;
     * {@code null} si la autenticación falla.
     */
    public Usuario autenticar(String cedula, String password) {
        String sql = "SELECT * FROM usuarios WHERE cedula = ? AND password = ? AND estado = 'activo'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("cedula")); // El campo 'usuario' se mapea a la 'cédula' de la base de datos.
                u.setEmail(rs.getString("email"));
                u.setTelefono(rs.getString("telefono"));
                return u;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al autenticar el usuario.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica si un usuario con una cédula específica ya existe en la base de datos.
     *
     * @param cedula la cédula a verificar.
     * @return {@code true} si el usuario existe en la base de datos, {@code false} en caso contrario.
     */
    public boolean existeUsuario(String cedula) {
        String sql = "SELECT id_usuario FROM usuarios WHERE cedula = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("❌ Error al verificar la existencia del usuario.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lista todos los usuarios que tienen un estado de 'activo'.
     *
     * @return una {@link List} de objetos {@link Usuario} activos, ordenados alfabéticamente por nombre.
     */
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE estado = 'activo' ORDER BY nombre";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("cedula")); // El campo 'usuario' se mapea a la 'cédula' de la base de datos.
                u.setTelefono(rs.getString("telefono"));
                u.setEmail(rs.getString("email"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al listar todos los usuarios.");
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Actualiza la información de un usuario existente en la base de datos.
     *
     * @param usuario el objeto {@link Usuario} con los datos actualizados.
     * @return {@code true} si la actualización se realizó con éxito, {@code false} en caso de error.
     */
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, telefono = ?, email = ? WHERE id_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTelefono());
            stmt.setString(3, usuario.getEmail());
            stmt.setInt(4, usuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar el usuario.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * "Elimina" un usuario de forma lógica, cambiando su estado a 'inactivo'.
     *
     * @param id el ID del usuario que se desea "eliminar".
     * @return {@code true} si el estado del usuario se actualiza a 'inactivo', {@code false} en caso de error.
     */
    public boolean eliminar(int id) {
        String sql = "UPDATE usuarios SET estado = 'inactivo' WHERE id_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar el usuario.");
            e.printStackTrace();
            return false;
        }
    }
    
    // =========================================================================
    // Nuevo método implementado: buscarPorCedula
    // =========================================================================
    
    /**
     * Busca un usuario por su número de cédula.
     *
     * @param cedula El número de cédula a buscar.
     * @return El objeto Usuario si se encuentra, o null si no existe.
     */
    public Usuario buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM usuarios WHERE cedula = ? AND estado = 'activo'";
        Usuario usuario = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsuario(rs.getString("cedula"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario por cédula: " + e.getMessage());
        }
        return usuario;
    }

    /**
     * Busca usuarios cuyo nombre, cédula o email coincida con un término de búsqueda.
     *
     * @param termino el término de búsqueda.
     * @return una {@link List} de objetos {@link Usuario} que coinciden con el término.
     */
    public List<Usuario> buscar(String termino) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE estado = 'activo' AND (nombre LIKE ? OR cedula LIKE ? OR email LIKE ?) ORDER BY nombre";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String likeTerm = "%" + termino + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setUsuario(rs.getString("cedula")); // El campo 'usuario' se mapea a la 'cédula'.
                u.setTelefono(rs.getString("telefono"));
                u.setEmail(rs.getString("email"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al buscar usuarios.");
            e.printStackTrace();
        }
        return lista;
    }
}