package alertasegurard;

import alertasegurard.views.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal que lanza la aplicación "Alerta Segura RD".
 *
 * <p>Esta clase contiene el método {@code main}, que es el punto de entrada de la aplicación.
 * Utiliza {@link javax.swing.SwingUtilities#invokeLater(Runnable) SwingUtilities.invokeLater}
 * para garantizar que la interfaz gráfica de usuario (GUI) se inicie de manera segura
 * en el Hilo de Despacho de Eventos (EDT).</p>
 *
 * @author G1
 */
public class Main {
    
    /**
     * El método principal de la aplicación.
     *
     * @param args los argumentos de la línea de comandos (no se usan en esta aplicación).
     */
    public static void main(String[] args) {
        // Se utiliza SwingUtilities.invokeLater para asegurar que la creación y visualización
        // de la GUI se realicen en el Event Dispatch Thread (EDT). Esto previene
        // problemas de concurrencia y asegura una interfaz de usuario fluida.
        try {
            UIManager.put("ScrollBarUI", "alertasegurard.views.DarkThemeScrollBarUI");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            // Se crea una instancia de la ventana de login, que es el primer
            // punto de interacción para el usuario.
            LoginFrame loginFrame = new LoginFrame();
            // Se hace visible la ventana de login.
            loginFrame.setVisible(true);
        });
    }
}
