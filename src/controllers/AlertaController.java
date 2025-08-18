package controllers;

import alertasegurard.dao.AlertaDAO;
import alertasegurard.models.Alerta;

import java.util.List;

/**
 * Clase controladora para gestionar las operaciones de la entidad Alerta.
 *
 * <p>Esta clase actúa como una capa de servicio entre la interfaz de usuario
 * y la capa de acceso a datos (DAO), proporcionando la lógica de negocio
 * para crear, obtener, eliminar y buscar alertas.</p>
 *
 * @author G1
 */
public class AlertaController {

    private final AlertaDAO alertaDAO;

    /**
     * Constructor de la clase AlertaController.
     * <p>Inicializa la instancia de {@link AlertaDAO} para interactuar con la base de datos.</p>
     */
    public AlertaController() {
        this.alertaDAO = new AlertaDAO();
    }

    /**
     * Crea una nueva alerta en la base de datos.
     * <p>Realiza una validación simple para asegurar que el objeto alerta y su
     * título no estén vacíos antes de intentar la creación.</p>
     *
     * @param alerta el objeto {@link Alerta} a crear.
     * @return {@code true} si la alerta se crea exitosamente, {@code false} en caso contrario.
     */
    public boolean crearAlerta(Alerta alerta) {
        if (alerta == null || alerta.getTitulo() == null || alerta.getTitulo().isBlank()) {
            return false;
        }
        return alertaDAO.crear(alerta);
    }

    /**
     * Obtiene una lista de todas las alertas.
     *
     * @return una {@link List} de objetos {@link Alerta}.
     */
    public List<Alerta> obtenerTodas() {
        return alertaDAO.obtenerTodas();
    }

    /**
     * Elimina una alerta de la base de datos por su ID.
     *
     * @param id el ID de la alerta a eliminar.
     * @return {@code true} si la alerta se elimina exitosamente, {@code false} en caso contrario.
     */
    public boolean eliminarAlerta(int id) {
        return alertaDAO.eliminar(id);
    }

    /**
     * Busca alertas por su título.
     *
     * @param titulo el título o parte del título a buscar.
     * @return una {@link List} de objetos {@link Alerta} que coinciden con el título.
     */
    public List<Alerta> buscarPorTitulo(String titulo) {
        return alertaDAO.buscarPorTitulo(titulo);
    }

    /**
     * Actualiza la información de una alerta existente.
     * <p>Realiza una validación para asegurar que el objeto alerta y su
     * ID sean válidos antes de intentar la actualización.</p>
     *
     * @param alerta el objeto {@link Alerta} con la información actualizada.
     * @return {@code true} si la alerta se actualiza exitosamente, {@code false} en caso contrario.
     */
    public boolean actualizarAlerta(Alerta alerta) {
        if (alerta == null || alerta.getId() <= 0) {
            return false;
        }
        return alertaDAO.actualizar(alerta);
    }
}
