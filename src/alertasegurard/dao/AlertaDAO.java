package alertasegurard.dao;

import alertasegurard.models.Alerta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de Objeto de Acceso a Datos (DAO) para la entidad {@link Alerta}.
 *
 * <p>Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar,
 * Eliminar) y otras consultas relacionadas con la tabla de alertas en la
 * base de datos.</p>
 *
 * @author G1
 */
public class AlertaDAO {

    private Connection conn;

    /**
     * Constructor de la clase AlertaDAO.
     * <p>Establece una conexión con la base de datos al ser instanciada,
     * utilizando la clase {@link DatabaseConnection}.</p>
     */
    public AlertaDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    /**
     * Inserta una nueva alerta en la base de datos.
     *
     * @param alerta el objeto {@link Alerta} que se va a crear.
     * @return {@code true} si la alerta se crea exitosamente, {@code false} en caso contrario.
     */
    public boolean crear(Alerta alerta) {
        // SQL corregido, utilizando NOW() directamente en la base de datos
        String sql = "INSERT INTO alertas (titulo, tipo_alerta, nivel_alerta, sector_afectado, descripcion_detallada, instrucciones_seguridad, imagen_url, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignación de los valores del objeto Alerta al PreparedStatement
            stmt.setString(1, alerta.getTitulo());
            stmt.setString(2, alerta.getTipoAlerta());
            stmt.setString(3, alerta.getNivelAlerta());
            stmt.setString(4, alerta.getSectorAfectado());
            stmt.setString(5, alerta.getDescripcion());
            stmt.setString(6, alerta.getInstrucciones());
            stmt.setString(7, alerta.getImagenUrl());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene una lista de todas las alertas de la base de datos.
     *
     * @return una {@link List} de objetos {@link Alerta}, ordenadas por
     * fecha de creación de forma descendente.
     */
    public List<Alerta> obtenerTodas() {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM alertas ORDER BY fecha_creacion DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alerta a = new Alerta();
                a.setId(rs.getInt("id_alerta"));
                a.setTitulo(rs.getString("titulo"));
                a.setTipoAlerta(rs.getString("tipo_alerta"));
                a.setNivelAlerta(rs.getString("nivel_alerta"));
                a.setDescripcion(rs.getString("descripcion_detallada"));
                a.setSectorAfectado(rs.getString("sector_afectado"));
                a.setInstrucciones(rs.getString("instrucciones_seguridad"));
                a.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                a.setImagenUrl(rs.getString("imagen_url"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Actualiza una alerta existente en la base de datos.
     *
     * @param alerta el objeto {@link Alerta} con los datos actualizados.
     * @return {@code true} si la alerta se actualiza exitosamente, {@code false} en caso contrario.
     */
    public boolean actualizar(Alerta alerta) {
        // SQL corregido y nombres de columnas ajustados
        String sql = "UPDATE alertas SET titulo = ?, descripcion_detallada = ?, tipo_alerta = ?, nivel_alerta = ?, sector_afectado = ?, instrucciones_seguridad = ?, imagen_url = ? WHERE id_alerta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, alerta.getTitulo());
            stmt.setString(2, alerta.getDescripcion());
            stmt.setString(3, alerta.getTipoAlerta());
            stmt.setString(4, alerta.getNivelAlerta());
            stmt.setString(5, alerta.getSectorAfectado());
            stmt.setString(6, alerta.getInstrucciones());
            stmt.setString(7, alerta.getImagenUrl());
            stmt.setInt(8, alerta.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una alerta de la base de datos por su ID.
     *
     * @param id el ID de la alerta que se va a eliminar.
     * @return {@code true} si la alerta se elimina exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM alertas WHERE id_alerta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca alertas por título, permitiendo coincidencias parciales.
     *
     * @param titulo el título o parte del título que se desea buscar.
     * @return una {@link List} de objetos {@link Alerta} que coinciden con el criterio de búsqueda.
     */
    public List<Alerta> buscarPorTitulo(String titulo) {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM alertas WHERE titulo LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Alerta a = new Alerta();
                a.setId(rs.getInt("id_alerta"));
                a.setTitulo(rs.getString("titulo"));
                a.setTipoAlerta(rs.getString("tipo_alerta"));
                a.setNivelAlerta(rs.getString("nivel_alerta"));
                a.setDescripcion(rs.getString("descripcion_detallada"));
                a.setSectorAfectado(rs.getString("sector_afectado"));
                a.setInstrucciones(rs.getString("instrucciones_seguridad"));
                a.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                a.setImagenUrl(rs.getString("imagen_url"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}