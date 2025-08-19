package alertasegurard.views;

import alertasegurard.dao.UsuarioDAO;
import alertasegurard.models.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Clase que representa la ventana de registro de usuario.
 * Provee una interfaz gráfica (GUI) con un diseño personalizado
 * para que los usuarios puedan crear una nueva cuenta en el sistema.
 * @author Grupo #1
 */
public class RegisterFrame extends JFrame {
    
    // Declaración de componentes de la interfaz de usuario para el formulario de registro.
    private JTextField txtNombre, txtCedula, txtEmail, txtTelefono;
    private JPasswordField txtPassword, txtConfirmar;
    private JButton btnRegistrar, btnCancelar;
    
    // Objetos de apoyo para la lógica de negocio y navegación entre ventanas.
    private final UsuarioDAO usuarioDAO;
    private final LoginFrame loginFrame;
    
    // Paleta de colores consistente con el resto de la aplicación para mantener la identidad visual.
    private final Color PRIMARY_COLOR = new Color(30, 144, 255);       // Azul principal para acciones importantes.
    private final Color SECONDARY_COLOR = new Color(25, 118, 210);     // Azul más oscuro para efectos de 'press'.
    private final Color ACCENT_COLOR = new Color(64, 169, 255);          // Azul claro para efectos de 'hover'.
    private final Color BACKGROUND_COLOR = new Color(8, 15, 29);       // Azul muy oscuro para el fondo principal.
    private final Color SURFACE_COLOR = new Color(16, 27, 45);         // Azul grisáceo para paneles y superficies.
    private final Color TEXT_COLOR = Color.WHITE;                      // Color de texto general.
    private final Color INPUT_BACKGROUND = new Color(25, 40, 65);      // Azul oscuro para los campos de entrada.
    private final Color CANCEL_COLOR = new Color(178, 34, 34);         // Rojo oscuro para el botón de cancelar.

    /**
     * Constructor principal de la clase.
     * Configura la ventana y sus propiedades iniciales, como el tamaño y la apariencia.
     *
     * @param loginFrame Una referencia a la ventana de login para poder regresar a ella.
     */
    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        // Inicializa el Data Access Object (DAO) para interactuar con la base de datos de usuarios.
        this.usuarioDAO = new UsuarioDAO();
        
        // Configuración de las propiedades de la ventana (JFrame).
        setTitle("Alerta Segura RD - Registro de Usuario");
        setSize(550, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        setResizable(false);
        setUndecorated(true); // Elimina la barra de título nativa para permitir un diseño personalizado.
        
        // Aplica bordes redondeados a la ventana completa.
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        // Llama a los métodos para inicializar los componentes y agregar la funcionalidad de arrastre.
        initComponents();
        addWindowDragListener();
    }

    /**
     * Inicializa y organiza todos los componentes de la interfaz de usuario en la ventana.
     * Define la estructura de los paneles, etiquetas, campos de texto y botones.
     */
    private void initComponents() {
        // Se crea un panel principal personalizado que permite un fondo con gradiente y textura.
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Aplica un gradiente de fondo entre BACKGROUND_COLOR y SURFACE_COLOR.
                GradientPaint gp = new GradientPaint(
                    0, 0, BACKGROUND_COLOR,
                    0, getHeight(), SURFACE_COLOR
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Dibuja un patrón de puntos sutiles para añadir textura visual.
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

        // Creación y posicionamiento del botón de cerrar.
        JButton closeBtn = createCloseButton();
        closeBtn.setBounds(500, 15, 35, 35);
        mainPanel.add(closeBtn);

        // Creación y posicionamiento del título principal de la ventana.
        JLabel title = new JLabel("REGISTRO DE USUARIO");
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 60, 550, 35);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);

        // Creación y posicionamiento del subtítulo.
        JLabel subtitle = new JLabel("Únase al Sistema Nacional de Alertas");
        subtitle.setForeground(new Color(156, 163, 175));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setBounds(0, 95, 550, 20);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitle);

        // Creación y posicionamiento del panel que contendrá el formulario.
        JPanel formPanel = createFormPanel();
        formPanel.setBounds(40, 140, 470, 600);
        mainPanel.add(formPanel);

        // Creación y posicionamiento del pie de página informativo.
        JLabel footerLabel = new JLabel("Todos los campos marcados con * son obligatorios");
        footerLabel.setForeground(new Color(107, 114, 128));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setBounds(0, 740, 550, 20);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(footerLabel);
    }

    /**
     * Crea el panel que contiene el formulario de registro.
     * Este panel tiene un fondo semitransparente con bordes redondeados.
     *
     * @return JPanel con el diseño del formulario.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibuja el fondo del panel con transparencia y bordes redondeados.
                g2d.setColor(new Color(16, 27, 45, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Dibuja un borde sutil alrededor del panel.
                g2d.setColor(new Color(71, 85, 105, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setLayout(null);

        // Definición de variables para el posicionamiento de los componentes del formulario.
        int yPosition = 30;
        int fieldHeight = 45;
        int labelHeight = 20;
        int spacing = 75;

        // Se crean los campos de entrada para el formulario (nombre, cédula, email, etc.).
        // Cada campo tiene una etiqueta y un JText/PasswordField asociado.
        // Campo nombre completo
        JLabel lblNombre = new JLabel("NOMBRE COMPLETO *");
        lblNombre.setForeground(TEXT_COLOR);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setBounds(30, yPosition, 200, labelHeight);
        formPanel.add(lblNombre);

        txtNombre = createStyledTextField();
        txtNombre.setBounds(30, yPosition + 25, 410, fieldHeight);
        formPanel.add(txtNombre);

        yPosition += spacing;

        // Campo cédula
        JLabel lblCedula = new JLabel("NÚMERO DE CÉDULA *");
        lblCedula.setForeground(TEXT_COLOR);
        lblCedula.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCedula.setBounds(30, yPosition, 200, labelHeight);
        formPanel.add(lblCedula);

        txtCedula = createStyledTextField();
        txtCedula.setBounds(30, yPosition + 25, 410, fieldHeight);
        formPanel.add(txtCedula);

        yPosition += spacing;

        // Campo email
        JLabel lblEmail = new JLabel("CORREO ELECTRÓNICO");
        lblEmail.setForeground(TEXT_COLOR);
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setBounds(30, yPosition, 200, labelHeight);
        formPanel.add(lblEmail);

        txtEmail = createStyledTextField();
        txtEmail.setBounds(30, yPosition + 25, 410, fieldHeight);
        formPanel.add(txtEmail);

        yPosition += spacing;

        // Campo teléfono
        JLabel lblTelefono = new JLabel("NÚMERO DE TELÉFONO");
        lblTelefono.setForeground(TEXT_COLOR);
        lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTelefono.setBounds(30, yPosition, 200, labelHeight);
        formPanel.add(lblTelefono);

        txtTelefono = createStyledTextField();
        txtTelefono.setBounds(30, yPosition + 25, 410, fieldHeight);
        formPanel.add(txtTelefono);

        yPosition += spacing;

        // Campo contraseña
        JLabel lblPassword = new JLabel("CONTRASEÑA *");
        lblPassword.setForeground(TEXT_COLOR);
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setBounds(30, yPosition, 200, labelHeight);
        formPanel.add(lblPassword);

        txtPassword = createStyledPasswordField();
        txtPassword.setBounds(30, yPosition + 25, 410, fieldHeight);
        formPanel.add(txtPassword);

        yPosition += spacing;

        // Campo confirmar contraseña
        JLabel lblConfirmar = new JLabel("CONFIRMAR CONTRASEÑA *");
        lblConfirmar.setForeground(TEXT_COLOR);
        lblConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblConfirmar.setBounds(30, yPosition, 200, labelHeight);
        formPanel.add(lblConfirmar);

        txtConfirmar = createStyledPasswordField();
        txtConfirmar.setBounds(30, yPosition + 25, 410, fieldHeight);
        formPanel.add(txtConfirmar);

        yPosition += 85;

        // Botón REGISTRAR USUARIO
        btnRegistrar = createPrimaryButton("REGISTRAR USUARIO");
        btnRegistrar.setBounds(30, yPosition, 410, 50);
        // Agrega un listener para ejecutar la lógica de registro al hacer clic.
        btnRegistrar.addActionListener(e -> registrarUsuario());
        formPanel.add(btnRegistrar);

        yPosition += 65;

        // Botón CANCELAR - AHORA EN ROJO
        btnCancelar = createRedButton("CANCELAR");
        btnCancelar.setBounds(30, yPosition, 410, 40);
        // Agrega un listener para regresar a la ventana de login.
        btnCancelar.addActionListener(e -> cancelarRegistro());
        formPanel.add(btnCancelar);

        return formPanel;
    }

    /**
     * Crea un JTextField con un estilo personalizado (fondo, bordes redondeados y foco).
     *
     * @return JTextField estilizado.
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibuja el fondo del campo de texto.
                g2d.setColor(INPUT_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Dibuja un borde de color primario cuando el campo tiene el foco.
                if (hasFocus()) {
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
                }
                
                super.paintComponent(g);
            }
        };
        // Configuración de propiedades adicionales del campo de texto.
        field.setOpaque(false);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    /**
     * Crea un JPasswordField con un estilo personalizado similar a los campos de texto.
     *
     * @return JPasswordField estilizado.
     */
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibuja el fondo del campo de contraseña.
                g2d.setColor(INPUT_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Dibuja un borde de color primario cuando el campo tiene el foco.
                if (hasFocus()) {
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
                }
                
                super.paintComponent(g);
            }
        };
        // Configuración de propiedades adicionales del campo de contraseña.
        field.setOpaque(false);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    /**
     * Crea un botón principal con estilo personalizado (gradiente y bordes redondeados).
     *
     * @param text El texto a mostrar en el botón.
     * @return JButton con el estilo principal.
     */
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Define el color de fondo basado en el estado del botón (presionado, hover o normal).
                Color bgColor = getModel().isPressed() ? SECONDARY_COLOR : 
                               getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;
                
                // Aplica un gradiente de color para el fondo del botón.
                GradientPaint gp = new GradientPaint(0, 0, bgColor, 0, getHeight(), 
                    new Color(bgColor.getRed() - 20, bgColor.getGreen() - 20, bgColor.getBlue() - 20));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Dibuja el texto centrado del botón.
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        // Configuración de propiedades adicionales del botón.
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Crea un botón con un estilo rojo para acciones de "cancelar" o "destructivas".
     *
     * @param text El texto a mostrar en el botón.
     * @return JButton con el estilo rojo.
     */
    private JButton createRedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = getModel().isPressed() ? new Color(139, 0, 0) :
                                getModel().isRollover() ? new Color(205, 50, 50) :
                                CANCEL_COLOR;

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(new Color(255, 100, 100)); // Un borde más claro.
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2d.setColor(Color.WHITE);
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
     * Crea un botón secundario con estilo personalizado (fondo sólido, borde y bordes redondeados).
     *
     * @param text El texto a mostrar en el botón.
     * @return JButton con el estilo secundario.
     */
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Define el color de fondo basado en el estado del botón.
                Color bgColor = getModel().isPressed() ? new Color(25, 40, 65) : 
                               getModel().isRollover() ? new Color(16, 27, 45) : 
                               new Color(8, 15, 29);
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Dibuja un borde sutil alrededor del botón.
                g2d.setColor(new Color(40, 65, 100));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                // Dibuja el texto centrado del botón.
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        // Configuración de propiedades adicionales del botón.
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Crea un botón de cerrar con estilo personalizado (forma de 'x', fondo circular en hover).
     *
     * @return JButton estilizado para cerrar la ventana.
     */
    private JButton createCloseButton() {
        JButton closeBtn = new JButton("×") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Define el color de fondo basado en el estado del botón (presionado, hover o normal).
                Color bgColor = getModel().isPressed() ? PRIMARY_COLOR : 
                               getModel().isRollover() ? ACCENT_COLOR : 
                               new Color(255, 255, 255, 0); // Transparente por defecto.
                
                // Dibuja un círculo para el fondo del botón.
                g2d.setColor(bgColor);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Dibuja el texto '×' centrado.
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        // Configuración de propiedades adicionales del botón de cerrar.
        closeBtn.setOpaque(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Agrega un listener para cerrar la ventana al hacer clic.
        closeBtn.addActionListener(e -> cancelarRegistro());
        return closeBtn;
    }

    /**
     * Permite arrastrar la ventana sin la barra de título nativa.
     * Añade listeners de mouse al JFrame para capturar los eventos de arrastre.
     */
    private void addWindowDragListener() {
        final Point[] mouseDownCompCoords = {null};
        
        // Listener para capturar la posición del mouse al presionar.
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords[0] = null;
            }

            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }
        });

        // Listener para actualizar la posición de la ventana al arrastrar el mouse.
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords[0].x, 
                           currCoords.y - mouseDownCompCoords[0].y);
            }
        });
    }

    /**
     * Valida los datos del formulario y procede con el registro del usuario.
     * Muestra mensajes de error o éxito según el resultado de la validación y la operación de base de datos.
     */
    private void registrarUsuario() {
        // Obtiene los datos de los campos de texto.
        String nombre = txtNombre.getText().trim();
        String cedula = txtCedula.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());

        // Lógica de validación del formulario.
        if (nombre.isEmpty() || cedula.isEmpty() || password.isEmpty()) {
            showStyledMessage("Los campos marcados con * son obligatorios.", "warning");
            return;
        }

        if (!password.equals(confirmar)) {
            showStyledMessage("Las contraseñas no coinciden. Verifique e intente nuevamente.", "error");
            txtPassword.setText("");
            txtConfirmar.setText("");
            return;
        }

        if (password.length() < 6) {
            showStyledMessage("La contraseña debe tener al menos 6 caracteres.", "warning");
            return;
        }

        // Verifica si ya existe un usuario con la misma cédula en la base de datos.
        if (usuarioDAO.existeUsuario(cedula)) {
            showStyledMessage("Ya existe un usuario registrado con esta cédula.", "error");
            return;
        }

        // Si todas las validaciones pasan, se crea un nuevo objeto Usuario.
        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setUsuario(cedula); // En este caso, el nombre de usuario es la cédula.
        nuevo.setEmail(email);
        nuevo.setTelefono(telefono);
        nuevo.setPassword(password);

        // Intenta registrar el usuario en la base de datos.
        boolean registrado = usuarioDAO.crear(nuevo);
        if (registrado) {
            showStyledMessage("Usuario registrado exitosamente. Ya puede iniciar sesión en el sistema.", "success");
            // Pausa la ejecución por 2 segundos para que el usuario vea el mensaje, y luego cierra la ventana.
            Timer timer = new Timer(2000, e -> {
                if (loginFrame != null) {
                    loginFrame.setVisible(true);
                }
                dispose();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showStyledMessage("Error en el registro. Verifique la información e intente nuevamente.", "error");
        }
    }

    /**
     * Cierra la ventana de registro y vuelve a hacer visible la ventana de login.
     */
    private void cancelarRegistro() {
        if (loginFrame != null) {
            loginFrame.setVisible(true);
        }
        dispose();
    }

    /**
     * Muestra un diálogo de mensaje personalizado y estilizado.
     *
     * @param message El mensaje de texto a mostrar.
     * @param type El tipo de mensaje ("success", "error", "warning") que define el estilo.
     */
    private void showStyledMessage(String message, String type) {
        JDialog dialog = new JDialog(this, true); // Crea un JDialog modal.
        dialog.setUndecorated(true);
        dialog.setSize(420, 180);
        dialog.setLocationRelativeTo(this); // Centra el diálogo con respecto a la ventana principal.
        
        // Crea un panel para el contenido del diálogo con un estilo personalizado.
        JPanel mainPanel;
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibuja el fondo del diálogo con gradiente y bordes redondeados.
                GradientPaint baseBg = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                g2d.setPaint(baseBg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Define el color de la barra superior según el tipo de mensaje.
                Color barColor;
                barColor = switch (type) {
                    case "success" -> new Color(34, 139, 34); // Verde para éxito.
                    case "error" -> PRIMARY_COLOR; // Azul para error.
                    default -> new Color(255, 140, 0); // Naranja para advertencia.
                };
                
                // Dibuja la barra superior indicadora.
                g2d.setColor(barColor);
                g2d.fillRoundRect(0, 0, getWidth(), 6, 20, 20);
                
                // Dibuja un borde sutil alrededor del diálogo.
                g2d.setColor(new Color(71, 85, 105));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        mainPanel.setLayout(null);
        
        // Define el título del diálogo según el tipo de mensaje.
        String title = type.equals("success") ? "REGISTRO EXITOSO" : 
                       type.equals("error") ? "ERROR EN REGISTRO" : "ADVERTENCIA";
        
        // Agrega el título y el mensaje de texto al panel.
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBounds(25, 20, 300, 25);
        mainPanel.add(titleLabel);
        
        JLabel msgLabel = new JLabel("<html><div style='line-height: 1.4;'>" + message + "</div></html>");
        msgLabel.setForeground(new Color(156, 163, 175));
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msgLabel.setBounds(25, 50, 370, 50);
        mainPanel.add(msgLabel);
        
        // Lógica para mostrar diferentes botones según el tipo de mensaje.
        if (type.equals("success")) {
            // Solo botón ACEPTAR para mensajes de éxito.
            JButton acceptBtn = createDialogButton("ACEPTAR", true);
            acceptBtn.setBounds(260, 120, 120, 40);
            acceptBtn.addActionListener(e -> dialog.dispose());
            mainPanel.add(acceptBtn);
        } else {
            // Dos botones para mensajes de error y advertencia.
            String mainBtnText = type.equals("error") ? "REINTENTAR" : "ENTENDIDO";
            
            JButton mainBtn = createDialogButton(mainBtnText, true);
            mainBtn.setBounds(220, 120, 110, 40);
            mainBtn.addActionListener(e -> dialog.dispose());
            mainPanel.add(mainBtn);
            
            JButton backBtn = createDialogButton("VOLVER", false);
            backBtn.setBounds(90, 120, 110, 40);
            backBtn.addActionListener(e -> {
                dialog.dispose();
                cancelarRegistro();
            });
            mainPanel.add(backBtn);
        }
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Crea un botón para los diálogos de mensaje con un estilo personalizado.
     *
     * @param text El texto del botón.
     * @param isPrimary Define si el botón tendrá el estilo primario o secundario.
     * @return JButton con el estilo de diálogo.
     */
    private JButton createDialogButton(String text, boolean isPrimary) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Define el color de fondo basado en el estado y tipo de botón.
                Color bgColor;
                if (isPrimary) {
                    bgColor = getModel().isPressed() ? SECONDARY_COLOR : 
                              getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;
                } else {
                    bgColor = getModel().isPressed() ? new Color(25, 40, 65) : 
                              getModel().isRollover() ? new Color(16, 27, 45) : 
                              new Color(8, 15, 29);
                }
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Dibuja un borde para los botones secundarios.
                if (!isPrimary) {
                    g2d.setColor(new Color(40, 65, 100));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                }
                
                // Dibuja el texto centrado del botón.
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        
        // Configuración de propiedades adicionales del botón.
        button.setFont(new Font("Segoe UI", isPrimary ? Font.BOLD : Font.PLAIN, 12));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
}
