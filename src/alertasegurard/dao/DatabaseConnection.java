package alertasegurard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Clase de utilidad para gestionar la conexión a la base de datos.
 *
 * <p>Esta clase implementa el patrón Singleton para la conexión, asegurando que
 * solo exista una instancia de conexión a la base de datos en toda la aplicación.
 * Los parámetros de conexión se cargan desde un archivo de propiedades.</p>
 *
 * @author G1
 */
public class DatabaseConnection {

    // Variable estática para mantener la única instancia de la conexión.
    private static Connection connection = null;

    /**
     * Obtiene la única instancia de la conexión a la base de datos.
     *
     * <p>Si la conexión aún no ha sido establecida, la crea cargando los
     * parámetros desde el archivo {@code config/database.properties}.
     * En caso de que la conexión falle, lanza una excepción en tiempo de
     * ejecución para detener la aplicación.</p>
     *
     * @return una instancia de {@link java.sql.Connection}.
     */
    public static Connection getConnection() {
        // Si la conexión es nula, se intenta establecer una nueva.
        if (connection == null) {
            try {
                // Se crea un objeto Properties para cargar los datos de configuración.
                Properties props = new Properties();
                // Se intenta cargar el archivo de configuración desde el classpath.
                InputStream input = DatabaseConnection.class.getClassLoader()
                        .getResourceAsStream("config/database.properties");

                // Verificación para asegurar que el archivo de configuración se encontró.
                if (input == null) {
                    throw new RuntimeException("❌ Archivo de configuración 'database.properties' no encontrado en el classpath.");
                }

                // Carga las propiedades desde el archivo.
                props.load(input);
                
                // Obtiene los parámetros de conexión del archivo de propiedades.
                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");
                String driver = props.getProperty("db.driver");

                // Carga dinámicamente la clase del driver de la base de datos.
                Class.forName(driver);
                // Establece la conexión a la base de datos.
                connection = DriverManager.getConnection(url, username, password);
                
                System.out.println("✅ Conexión exitosa a la base de datos.");

            } catch (SQLException e) {
                // Captura y maneja las excepciones de SQL.
                System.out.println("❌ Error de SQL al conectar con la base de datos.");
                e.printStackTrace();
                throw new RuntimeException("Error de SQL al conectar con la base de datos.", e);
            } catch (ClassNotFoundException e) {
                // Captura y maneja la excepción si el driver no se encuentra.
                System.out.println("❌ Error: Driver de la base de datos no encontrado.");
                e.printStackTrace();
                throw new RuntimeException("Error: Driver de la base de datos no encontrado.", e);
            } catch (IOException e) {
                 // Captura y maneja las excepciones de E/S.
                System.out.println("❌ Error de E/S al leer el archivo de propiedades.");
                e.printStackTrace();
                throw new RuntimeException("Error de E/S al leer el archivo de propiedades.", e);
            }
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     * <p>Este método es opcional pero se recomienda para cerrar la conexión de forma
     * ordenada al finalizar la aplicación.</p>
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Reinicia la conexión para futuras llamadas.
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }
}
