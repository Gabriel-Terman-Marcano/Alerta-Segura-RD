package alertasegurard.views;

import alertasegurard.dao.UsuarioDAO;
import alertasegurard.models.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

/**
 * **Clase:** LoginFrame
 *
 * **Descripción:**
 * Esta clase representa la interfaz de usuario (GUI) para el inicio de sesión del sistema.
 * Proporciona campos de entrada para la cédula y la contraseña, y botones para
 * iniciar sesión, registrarse y salir. La interfaz está diseñada con un estilo
 * oscuro y moderno, utilizando componentes de Swing personalizados para lograr
 * una apariencia coherente y profesional.
 *
 * @author Grupo #1
 */
public class LoginFrame extends JFrame {

    // Atributos de la clase
    private JTextField txtCedula;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegistrar, btnSalir;
    private UsuarioDAO usuarioDAO;

    // Constantes de colores para un diseño consistente
    private final Color PRIMARY_COLOR = new Color(220, 38, 38);
    private final Color SECONDARY_COLOR = new Color(185, 28, 28);
    private final Color ACCENT_COLOR = new Color(239, 68, 68);
    private final Color BACKGROUND_COLOR = new Color(15, 23, 42);
    private final Color SURFACE_COLOR = new Color(30, 41, 59);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color INPUT_BACKGROUND = new Color(51, 65, 85);
    
    // Variable para la imagen de fondo
    private ImageIcon backgroundImage;

    /**
     * Constructor de la clase `LoginFrame`.
     * Inicializa la ventana de inicio de sesión, el DAO para la autenticación
     * de usuarios y configura el diseño y la funcionalidad de la GUI.
     */
    public LoginFrame() {
        usuarioDAO = new UsuarioDAO();

        // Configuración básica de la ventana
        setTitle("Alerta Segura RD - Sistema de Emergencias");
        setSize(500, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Permite un diseño de ventana personalizado
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20)); // Define la forma de la ventana con esquinas redondeadas

        loadResources(); // Carga la imagen de fondo antes de inicializar los componentes
        initComponents(); // Carga todos los componentes de la GUI
        addWindowDragListener(); // Habilita la función de arrastrar la ventana
    }
    
    /**
     * Carga los recursos visuales como la imagen de fondo.
     */
    private void loadResources() {
        try {
            // La ruta se asume que está en src/main/resources/alertasegurard/views/background.jpg
            URL imageUrl = getClass().getResource("/alertasegurard/main/resources/images/background.png");
            if (imageUrl != null) {
                this.backgroundImage = new ImageIcon(imageUrl);
            } else {
                System.err.println("Error: No se pudo encontrar la imagen de fondo. La aplicación usará un color sólido.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            this.backgroundImage = null; // En caso de error, el fondo no tendrá imagen
        }
    }

    /**
     * Inicializa y organiza todos los componentes de la interfaz de usuario.
     */
    private void initComponents() {
        // Crea el panel principal
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibuja la imagen de fondo si está disponible
                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                    
                    // Capa oscura semitransparente para mejorar la legibilidad del texto
                    g2d.setColor(new Color(0, 0, 0, 150)); // El valor 150 (de 0 a 255) determina la opacidad
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // Si no hay imagen, usa el degradado de color original
                    GradientPaint gp = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

                // Patrón de puntos sutil para textura
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int x = 0; x < getWidth(); x += 30) {
                    for (int y = 0; y < getHeight(); y += 30) {
                        g2d.fillOval(x, y, 2, 2);
                    }
                }
            }
        };
        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        // Agrega el botón de cerrar
        JButton closeBtn = createCloseButton();
        closeBtn.setBounds(450, 15, 35, 35);
        mainPanel.add(closeBtn);
        
        // --- AGREGANDO EL LOGO ---
        // 1. Obtiene la URL del recurso desde el classpath
        URL logoUrl = getClass().getResource("/alertasegurard/main/resources/images/alert.png");
        ImageIcon logoIcon = null;
        if (logoUrl != null) {
            logoIcon = new ImageIcon(logoUrl);
        } else {
            System.err.println("Error: No se pudo encontrar el archivo del logo en la ruta especificada.");
        }

        // Agrega los títulos principales de la aplicación
        JLabel title = new JLabel("ALERTA SEGURA RD", logoIcon, SwingConstants.CENTER);
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(0, 80, 500, 65);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        // 3. Ajusta la posición del texto en relación al icono
        title.setHorizontalTextPosition(SwingConstants.LEFT);
        title.setVerticalTextPosition(SwingConstants.CENTER);
        mainPanel.add(title);


        JLabel subtitle = new JLabel("Sistema Nacional de Alertas de Emergencia");
        subtitle.setForeground(new Color(156, 163, 175));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setBounds(0, 115, 430, 35);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitle);

        // Crea y añade el panel del formulario
        JPanel formPanel = createFormPanel();
        formPanel.setBounds(60, 170, 380, 320);
        mainPanel.add(formPanel);

        // Agrega el footer
        JLabel footerLabel = new JLabel("© 2024 República Dominicana - Sistema de Emergencias");
        footerLabel.setForeground(new Color(107, 114, 128));
        footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        footerLabel.setBounds(0, 600, 500, 20);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(footerLabel);
    }

    /**
     * Crea y configura el panel que contiene los campos de entrada y los botones del formulario de login.
     * @return Un JPanel con el formulario de login.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo del panel con transparencia y esquinas redondeadas
                g2d.setColor(new Color(30, 41, 59, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde sutil
                g2d.setColor(new Color(71, 85, 105, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setLayout(null);

        // Configuración del campo de cédula
        JLabel lblCedula = new JLabel("NÚMERO DE CÉDULA");
        lblCedula.setForeground(TEXT_COLOR);
        lblCedula.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCedula.setBounds(30, 30, 200, 20);
        formPanel.add(lblCedula);

        txtCedula = createStyledTextField();
        txtCedula.setBounds(30, 55, 320, 45);
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(txtCedula);

        // Configuración del campo de contraseña
        JLabel lblPassword = new JLabel("CONTRASEÑA");
        lblPassword.setForeground(TEXT_COLOR);
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setBounds(30, 115, 200, 20);
        formPanel.add(lblPassword);

        txtPassword = createStyledPasswordField();
        txtPassword.setBounds(30, 140, 320, 45);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(txtPassword);

        // Botón de iniciar sesión
        btnLogin = createPrimaryButton("INICIAR SESIÓN");
        btnLogin.setBounds(30, 200, 320, 50);
        btnLogin.addActionListener(e -> login());
        formPanel.add(btnLogin);

        // Panel para los botones secundarios (Registrar y Salir)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(30, 265, 320, 40);

        btnRegistrar = createSecondaryButton("REGISTRARSE");
        btnRegistrar.addActionListener(e -> {
            new RegisterFrame(this).setVisible(true);
            this.setVisible(false);
        });

        btnSalir = createSecondaryButton("SALIR");
        btnSalir.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnSalir);
        formPanel.add(buttonPanel);

        return formPanel;
    }

    /**
     * Crea un campo de texto con un estilo visual personalizado.
     * @return Un JTextField estilizado.
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibuja el fondo del campo con esquinas redondeadas
                g2d.setColor(INPUT_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Dibuja un borde de color cuando el campo está enfocado
                if (hasFocus()) {
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
                }
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        return field;
    }

    /**
     * Crea un campo de contraseña con un estilo visual personalizado.
     * @return Un JPasswordField estilizado.
     */
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibuja el fondo del campo con esquinas redondeadas
                g2d.setColor(INPUT_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Dibuja un borde de color cuando el campo está enfocado
                if (hasFocus()) {
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
                }
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        return field;
    }

    /**
     * Crea un botón principal (color rojo) con un estilo visual personalizado.
     * @param text El texto a mostrar en el botón.
     * @return Un JButton estilizado.
     */
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = getModel().isPressed() ? SECONDARY_COLOR :
                        getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;

                // Gradiente del botón
                GradientPaint gp = new GradientPaint(0, 0, bgColor, 0, getHeight(),
                        new Color(bgColor.getRed() - 20, bgColor.getGreen() - 20, bgColor.getBlue() - 20));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Texto del botón
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Crea un botón secundario (color oscuro) con un estilo visual personalizado.
     * @param text El texto a mostrar en el botón.
     * @return Un JButton estilizado.
     */
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = getModel().isPressed() ? new Color(71, 85, 105) :
                        getModel().isRollover() ? new Color(51, 65, 85) :
                                new Color(30, 41, 59);

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Borde del botón
                g2d.setColor(new Color(71, 85, 105));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                // Texto del botón
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Crea el botón para cerrar la ventana con un estilo personalizado.
     * @return Un JButton con el icono de cerrar.
     */
    private JButton createCloseButton() {
        JButton closeBtn = new JButton("×") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = getModel().isPressed() ? PRIMARY_COLOR :
                        getModel().isRollover() ? ACCENT_COLOR :
                                new Color(255, 255, 255, 0);

                g2d.setColor(bgColor);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        closeBtn.setOpaque(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        return closeBtn;
    }

    /**
     * Habilita la función de arrastrar la ventana sin bordes.
     */
    private void addWindowDragListener() {
        final Point[] mouseDownCompCoords = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords[0] = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords[0].x,
                        currCoords.y - mouseDownCompCoords[0].y);
            }
        });
    }

    /**
     * Lógica principal de autenticación. Valida los campos de entrada,
     * autentica al usuario a través de `UsuarioDAO` y redirige a la
     * ventana principal en caso de éxito o muestra un mensaje de error.
     */
    private void login() {
        String cedula = txtCedula.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (cedula.isEmpty() || password.isEmpty()) {
            showStyledMessage("Por favor, complete todos los campos.", "warning");
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(cedula, password);
        if (usuario != null) {
            // Cierra la ventana de login
            this.setVisible(false);

            // Abre el menú principal
            new MainMenuFrame(usuario, this).setVisible(true);

            showStyledMessage("¡Bienvenido, " + usuario.getNombre() + "!", "success");
        } else {
            showStyledMessage("Credenciales incorrectas. Verifique sus datos.", "error");
            txtPassword.setText("");
        }
    }

    /**
     * Muestra un diálogo de mensaje con un estilo visual personalizado.
     * @param message El mensaje a mostrar.
     * @param type El tipo de mensaje ("success", "error", "warning") para determinar el color de la barra superior.
     */
    private void showStyledMessage(String message, String type) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);

        // Hace el diálogo con bordes redondeados
        dialog.setShape(new RoundRectangle2D.Double(0, 0, 400, 150, 20, 20));

        JPanel panel;
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo base con gradiente del tema
                GradientPaint baseBg = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                g2d.setPaint(baseBg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Barra superior con color según el tipo de mensaje
                Color barColor;
                barColor = switch (type) {
                    case "success" -> new Color(34, 139, 34);
                    case "error" -> PRIMARY_COLOR;
                    default -> new Color(255, 140, 0);
                }; // Verde para éxito
                // Rojo para error
                // Naranja para advertencia

                // Barra superior indicadora
                g2d.setColor(barColor);
                g2d.fillRoundRect(0, 0, getWidth(), 6, 20, 20);

                // Borde sutil
                g2d.setColor(new Color(71, 85, 105));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        panel.setLayout(null);

        // Título del mensaje
        String title = type.equals("success") ? "ACCESO AUTORIZADO" :
                type.equals("error") ? "ERROR DE AUTENTICACIÓN" : "ADVERTENCIA";

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setBounds(20, 15, 360, 20);
        panel.add(titleLabel);

        // Mensaje principal
        JLabel msgLabel = new JLabel("<html><div style='text-align: left; line-height: 1.4;'>" + message + "</div></html>");
        msgLabel.setForeground(new Color(156, 163, 175));
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msgLabel.setBounds(20, 40, 360, 40);
        panel.add(msgLabel);

        // Botón OK con el estilo del sistema
        JButton okBtn = new JButton("CONTINUAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = getModel().isPressed() ? SECONDARY_COLOR :
                        getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Texto del botón
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        okBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        okBtn.setOpaque(false);
        okBtn.setBorderPainted(false);
        okBtn.setContentAreaFilled(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.setBounds(250, 95, 120, 35);
        okBtn.addActionListener(e -> dialog.dispose());
        panel.add(okBtn);

        // Efecto de sombra sutil
        dialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1)
        ));

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
