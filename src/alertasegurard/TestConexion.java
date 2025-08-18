package alertasegurard;

import java.sql.Connection;
import alertasegurard.dao.DatabaseConnection;

/**
 * Clase de utilidad para probar la conexión a la base de datos.
 *
 * <p>Esta clase contiene un método {@code main} que se utiliza para verificar si
 * la aplicación puede establecer una conexión exitosa con la base de datos
 * a través de la clase {@link alertasegurard.dao.DatabaseConnection}.</p>
 *
 * @author G1
 */
public class TestConexion {
    
    /**
     * El método principal para ejecutar la prueba de conexión.
     *
     * <p>Intenta obtener una conexión a la base de datos, imprime un mensaje de
     * éxito si la conexión es válida y se cierra, o un mensaje de error si
     * la conexión falla.</p>
     *
     * @param args los argumentos de la línea de comandos (no se usan en esta aplicación).
     */
    public static void main(String[] args) {
        try {
            // Se intenta obtener una conexión a la base de datos.
            Connection conn = DatabaseConnection.getConnection();
            
            // Se verifica si la conexión es exitosa y no está cerrada.
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa con la base de datos.");
                // Se cierra la conexión para liberar los recursos.
                conn.close();
            } else {
                System.out.println("❌ La conexión fue nula o se cerró inesperadamente.");
            }
        } catch (Exception e) {
            // Se captura cualquier excepción que ocurra durante el proceso de conexión.
            System.out.println("❌ Error al conectar: " + e.getMessage());
            // Se imprime el rastreo de la pila para una depuración más detallada.
            e.printStackTrace();
        }
    }
}
