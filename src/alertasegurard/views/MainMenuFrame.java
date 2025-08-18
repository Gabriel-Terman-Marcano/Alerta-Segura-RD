// Archivo: src/alertasegurard/views/MainMenuFrame.java
package alertasegurard.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import alertasegurard.models.Usuario;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URL;

/**
 * Menú principal de la aplicación con diseño moderno.
 * Esta clase extiende JFrame y se encarga de mostrar la interfaz principal
 * para los usuarios, con opciones de navegación basadas en su rol (administrador o estándar).
 * * @author Grupo #1
 */
public class MainMenuFrame extends JFrame {
    // Referencias a objetos y componentes de la interfaz
    private final Usuario usuarioLogueado;
    private final LoginFrame loginFrame;
    private JButton btnUsuarios, btnAlertas, btnAcercaDe, btnSalir;

    // Paleta de colores para mantener un diseño coherente.
    // Los nombres de las variables describen su propósito en la UI.
    private final Color PRIMARY_COLOR = new Color(220, 38, 38);      // Rojo de emergencia, principal para botones y acentos.
    private final Color SECONDARY_COLOR = new Color(185, 28, 28);    // Rojo más oscuro para efectos de 'pressed'.
    private final Color ACCENT_COLOR = new Color(239, 68, 68);      // Rojo claro para efectos de 'hover'.
    private final Color BACKGROUND_COLOR = new Color(15, 23, 42);    // Azul muy oscuro, color base del fondo.
    private final Color SURFACE_COLOR = new Color(30, 41, 59);        // Gris azulado, usado para gradientes y superficies secundarias.
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color CARD_BACKGROUND = new Color(51, 65, 85);       // Gris oscuro para el fondo de las tarjetas.
    private final Color SUCCESS_COLOR = new Color(34, 139, 34);      // Verde para indicar éxito o el rol de administrador.
    
    // Variable para la imagen de fondo
    private ImageIcon backgroundImage;
    // Lista de cédulas de los usuarios administradores.
    // Se utiliza para determinar dinámicamente qué botones mostrar.
    private final List<String> ADMIN_CEDULAS = Arrays.asList(
        "40256388170",
        "40212728949",
        "40218086474"
    );

    private JLabel timeLabel;


    /**
     * Constructor de la clase MainMenuFrame.
     * @param usuario El objeto Usuario del usuario que ha iniciado sesión.
     * @param loginFrame La instancia de LoginFrame para poder regresar a ella al cerrar sesión.
     */
    public MainMenuFrame(Usuario usuario, LoginFrame loginFrame) {
        this.usuarioLogueado = usuario;
        this.loginFrame = loginFrame;

        // Configuración básica de la ventana
        setTitle("Alerta Segura RD - Menú Principal");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Elimina la barra de título nativa del sistema.

        // Establece la forma de la ventana con bordes redondeados.
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        loadResources(); // Carga la imagen de fondo antes de inicializar los componentes
        
        // Inicializa y configura los componentes de la interfaz gráfica.
        initComponents();
        // Agrega la funcionalidad para arrastrar la ventana sin la barra de título.
        addWindowDragListener();
        // Inicia el hilo que actualiza la hora en tiempo real.
        startTimeUpdater();
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
     * Inicializa y organiza todos los componentes de la interfaz de usuario en el panel principal.
     */
    private void initComponents() {
        // Crea un JPanel personalizado con un gradiente de fondo y textura.
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

        // Llama a los métodos para construir cada sección de la UI.
        createHeader(mainPanel);
        createUserInfoCard(mainPanel);
        createMenuButtons(mainPanel);
        createFooter(mainPanel);
    }

    /**
     * Crea la sección del encabezado de la ventana con el título y el botón de cierre.
     * @param parent El JPanel al que se añadirán los componentes.
     */
    private void createHeader(JPanel parent) {
        // Botón de cerrar/minimizar
        JButton closeBtn = createCloseButton();
        closeBtn.setBounds(550, 15, 35, 35);
        parent.add(closeBtn);

        // Logo/Icono de emergencia
        JLabel iconLabel = new JLabel("AS", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        iconLabel.setForeground(PRIMARY_COLOR);
        iconLabel.setBounds(0, 30, 600, 50);
        parent.add(iconLabel);

        // Título principal
        JLabel title = new JLabel("CENTRO DE CONTROL");
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(0, 85, 600, 35);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        parent.add(title);

        // Subtítulo
        JLabel subtitle = new JLabel("Sistema Nacional de Alertas de Emergencia");
        subtitle.setForeground(new Color(156, 163, 175));
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        subtitle.setBounds(0, 120, 600, 20);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        parent.add(subtitle);
    }

    /**
     * Crea y muestra la tarjeta con la información del usuario logueado.
     * Incluye nombre, tipo de usuario y un indicador de administrador.
     * @param parent El JPanel al que se añadirán los componentes.
     */
    private void createUserInfoCard(JPanel parent) {
        JPanel userCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo de la tarjeta con gradiente sutil y bordes redondeados.
                GradientPaint gp = new GradientPaint(
                    0, 0, CARD_BACKGROUND,
                    0, getHeight(), new Color(CARD_BACKGROUND.getRed() - 10,
                                             CARD_BACKGROUND.getGreen() - 10,
                                             CARD_BACKGROUND.getBlue() - 10)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde sutil
                g2d.setColor(new Color(71, 85, 105, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

                // Dibuja un distintivo de "ADMIN" si el usuario es administrador.
                if (ADMIN_CEDULAS.contains(usuarioLogueado.getUsuario())) {
                    g2d.setColor(SUCCESS_COLOR);
                    g2d.fillRoundRect(getWidth() - 80, 10, 70, 25, 12, 12);

                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    g2d.drawString("ADMIN", getWidth() - 65, 26);
                }
            }
        };
        userCard.setOpaque(false);
        userCard.setLayout(null);
        userCard.setBounds(50, 160, 500, 120);
        parent.add(userCard);

        // Muestra el avatar del usuario.
        // La imagen de origen se redimensiona automáticamente.
        ImageIcon avatarIcon = getResizedIcon("src/images/login.png", 60, 60);
        JLabel avatarLabel = new JLabel(avatarIcon);
        avatarLabel.setBounds(20, 20, 60, 60);
        userCard.add(avatarLabel);

        // Mensaje de bienvenida y detalles del usuario.
        JLabel welcomeLabel = new JLabel("¡Bienvenid@ de vuelta!");
        welcomeLabel.setForeground(new Color(156, 163, 175));
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        welcomeLabel.setBounds(100, 20, 300, 20);
        userCard.add(welcomeLabel);

        // Muestra el nombre del usuario.
        JLabel nameLabel = new JLabel(usuarioLogueado.getNombre());
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setBounds(100, 40, 300, 25);
        userCard.add(nameLabel);

        // Muestra el tipo de usuario (Admin o Estándar).
        String userType = ADMIN_CEDULAS.contains(usuarioLogueado.getUsuario()) ?
                                     "Administrador/a del Sistema" : "Usuario Estándar";
        JLabel typeLabel = new JLabel(userType);
        typeLabel.setForeground(new Color(156, 163, 175));
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setBounds(100, 65, 300, 20);
        userCard.add(typeLabel);

        // Etiqueta para la hora actual que se actualiza en tiempo real.
        timeLabel = new JLabel();
        timeLabel.setForeground(new Color(156, 163, 175));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setBounds(100, 85, 300, 20);
        userCard.add(timeLabel);
    }

    /**
     * Crea y posiciona los botones de menú principales.
     * El botón de "Usuarios" solo se muestra si el usuario es administrador.
     * @param parent El JPanel al que se añadirán los componentes.
     */
    private void createMenuButtons(JPanel parent) {
        int yStart = 300;
        int buttonHeight = 60;
        int spacing = 75;

        // Cargamos los iconos de los recursos
        ImageIcon usuariosIcon = loadIcon("/alertasegurard/main/resources/images/usuarios_icon.png", 30, 30);
        ImageIcon alertasIcon = loadIcon("/alertasegurard/main/resources/images/alertas_icon.png", 30, 30);
        ImageIcon infoIcon = loadIcon("/alertasegurard/main/resources/images/info_icon.png", 30, 30);
        ImageIcon salirIcon = loadIcon("/alertasegurard/main/resources/images/salir_icon.png", 30, 30);

        // Condición para mostrar el botón de gestión de usuarios solo a los administradores.
        if (ADMIN_CEDULAS.contains(usuarioLogueado.getUsuario())) {
            btnUsuarios = createMenuButton(usuariosIcon, "GESTIÓN DE USUARIOS", "Administrar usuarios del sistema");
            btnUsuarios.setBounds(50, yStart, 500, buttonHeight);
            // ActionListener para abrir la ventana de gestión de usuarios.
            btnUsuarios.addActionListener(e -> {
                UsuariosFrame usuariosFrame = new UsuariosFrame(this);
                usuariosFrame.setVisible(true);
                this.setVisible(false); // Oculta la ventana actual.
            });
            parent.add(btnUsuarios);
            yStart += spacing;
        }

        // Botón de Gestión de Alertas
        btnAlertas = createMenuButton(alertasIcon, "GESTIÓN DE ALERTAS", "Crear y gestionar alertas de emergencia");
        btnAlertas.setBounds(50, yStart, 500, buttonHeight);
        btnAlertas.addActionListener(e -> {
            AlertasApartado alertasApartado = new AlertasApartado(this.usuarioLogueado, this);
            alertasApartado.setVisible(true);
            this.setVisible(false); // Oculta la ventana actual.
        });
        parent.add(btnAlertas);
        yStart += spacing;

        // Botón Acerca del Sistema
        btnAcercaDe = createMenuButton(infoIcon, "ACERCA DEL SISTEMA", "Información sobre la aplicación");
        btnAcercaDe.setBounds(50, yStart, 500, buttonHeight);
        btnAcercaDe.addActionListener(e -> {
            AcercaDeFrame acercaDe = new AcercaDeFrame(this.usuarioLogueado, this);
            acercaDe.setVisible(true);
            this.setVisible(false);
        });
        parent.add(btnAcercaDe);
        yStart += spacing;

        // Botón Salir
        btnSalir = createExitButton(salirIcon, "CERRAR SESIÓN", "Salir del sistema de forma segura");
        btnSalir.setBounds(50, yStart, 500, buttonHeight);
        btnSalir.addActionListener(e -> {
            this.dispose(); // Cierra esta ventana.
            loginFrame.setVisible(true); // Vuelve a la ventana de login.
        });
        parent.add(btnSalir);
    }

    /**
     * Carga y escala un icono desde los recursos.
     *
     * @param path La ruta del icono en los recursos.
     * @param width El ancho deseado.
     * @param height El alto deseado.
     * @return Un ImageIcon escalado o null si no se encuentra.
     */
    private ImageIcon loadIcon(String path, int width, int height) {
        URL iconUrl = getClass().getResource(path);
        if (iconUrl != null) {
            ImageIcon originalIcon = new ImageIcon(iconUrl);
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("Error: Icono no encontrado en la ruta: " + path);
            return null;
        }
    }

    /**
     * Crea un botón de menú con estilo personalizado.
     * @param icon El icono del botón.
     * @param title El título principal del botón.
     * @param description La descripción del botón.
     * @return El JButton estilizado.
     */
    private JButton createMenuButton(ImageIcon icon, String title, String description) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cambia el color del fondo del botón según el estado (normal, hover, presionado).
                Color bgColor = getModel().isPressed() ? SECONDARY_COLOR :
                                getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;

                // Aplica un gradiente al fondo del botón.
                GradientPaint gp = new GradientPaint(0, 0, bgColor, 0, getHeight(),
                    new Color(Math.max(0, bgColor.getRed() - 20),
                              Math.max(0, bgColor.getGreen() - 20),
                              Math.max(0, bgColor.getBlue() - 20)));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Efecto de brillo sutil al pasar el mouse.
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 12, 12);
                }
            }
        };

        // Configuración de la apariencia del botón.
        button.setLayout(null);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Añade el icono, título, descripción y flecha al botón.
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(20, (button.getPreferredSize().height - icon.getIconHeight()) / 250, 40, 60);
        button.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBounds(80, 12, 300, 20);
        button.add(titleLabel);

        JLabel descLabel = new JLabel(description);
        descLabel.setForeground(new Color(255, 255, 255, 180));
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setBounds(80, 32, 300, 15);
        button.add(descLabel);

        JLabel arrowLabel = new JLabel(">");
        arrowLabel.setForeground(new Color(255, 255, 255, 150));
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        arrowLabel.setBounds(460, 18, 30, 25);
        button.add(arrowLabel);

        return button;
    }

    /**
     * Crea un botón de "Salir" con un estilo diferente para distinguirlo del resto.
     * El estilo es más sutil, sin el gradiente rojo.
     */
    private JButton createExitButton(ImageIcon icon, String title, String description) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cambia el color del fondo según el estado. Colores oscuros para el estilo sutil.
                Color bgColor = getModel().isPressed() ? new Color(71, 85, 105) :
                                getModel().isRollover() ? new Color(51, 65, 85) :
                                new Color(30, 41, 59);

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Dibuja un borde sutil.
                g2d.setColor(new Color(71, 85, 105));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            }
        };

        // Configuración y adición de etiquetas similar a los otros botones de menú.
        button.setLayout(null);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(20, (button.getPreferredSize().height - icon.getIconHeight()) / 250, 40, 60);
        button.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBounds(80, 12, 300, 20);
        button.add(titleLabel);

        JLabel descLabel = new JLabel(description);
        descLabel.setForeground(new Color(255, 255, 255, 180));
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setBounds(80, 32, 300, 15);
        button.add(descLabel);

        JLabel arrowLabel = new JLabel(">");
        arrowLabel.setForeground(new Color(255, 255, 255, 150));
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        arrowLabel.setBounds(460, 18, 30, 25);
        button.add(arrowLabel);

        return button;
    }

    /**
     * Crea el botón de cierre de la ventana con un diseño circular y personalizado.
     * @return El JButton para cerrar la ventana.
     */
    private JButton createCloseButton() {
        JButton closeBtn = new JButton("X") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cambia el color de fondo para el efecto 'hover' y 'pressed'.
                Color bgColor = getModel().isPressed() ? PRIMARY_COLOR :
                                getModel().isRollover() ? ACCENT_COLOR :
                                new Color(255, 255, 255, 0); // Transparente por defecto

                g2d.setColor(bgColor);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Dibuja el texto 'X' centrado.
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
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
        // Agrega un listener para mostrar un diálogo de confirmación al presionar.
        closeBtn.addActionListener(e -> confirmarSalida());
        return closeBtn;
    }

    /**
     * Crea la sección del pie de página con un texto informativo.
     * @param parent El JPanel al que se añadirá la etiqueta.
     */
    private void createFooter(JPanel parent) {
        JLabel footerLabel = new JLabel("Sistema Nacional de Alertas de Emergencia - República Dominicana");
        footerLabel.setForeground(new Color(107, 114, 128));
        footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        footerLabel.setBounds(0, 660, 600, 20);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        parent.add(footerLabel);
    }

    /**
     * Implementa la funcionalidad para arrastrar la ventana sin la barra de título nativa.
     * Captura las coordenadas del mouse al presionar y las usa para mover la ventana.
     */
    private void addWindowDragListener() {
        final Point[] mouseDownCompCoords = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords[0] = null; // Libera las coordenadas al soltar el botón.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint(); // Guarda la posición inicial del mouse.
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen(); // Obtiene la posición actual en la pantalla.
                // Mueve la ventana basándose en el desplazamiento del mouse.
                setLocation(currCoords.x - mouseDownCompCoords[0].x,
                            currCoords.y - mouseDownCompCoords[0].y);
            }
        });
    }

    /**
     * Inicia un temporizador que actualiza la etiqueta de la hora cada segundo.
     */
    private void startTimeUpdater() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            // Formatea la fecha y hora al español.
            String timeText = now.format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy - HH:mm:ss"));
            timeLabel.setText(timeText);
        });
        timer.start();
    }

    /**
     * Muestra el diálogo de confirmación de salida con un estilo personalizado.
     */
    private void confirmarSalida() {
        showStyledConfirmDialog();
    }

    /**
     * Crea y muestra un JDialog de confirmación de salida con un diseño moderno y estilizado.
     */
    private void showStyledConfirmDialog() {
        JDialog dialog = new JDialog(this, true); // Crea un diálogo modal (bloquea la ventana principal).
        dialog.setUndecorated(true);
        dialog.setSize(450, 200);
        dialog.setLocationRelativeTo(this); // Centra el diálogo con respecto a la ventana principal.

        // Panel principal del diálogo con fondo personalizado.
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con gradiente y bordes redondeados.
                GradientPaint baseBg = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                g2d.setPaint(baseBg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Barra superior de advertencia.
                g2d.setColor(new Color(255, 140, 0));
                g2d.fillRoundRect(0, 0, getWidth(), 6, 20, 20);

                // Borde sutil.
                g2d.setColor(new Color(71, 85, 105));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        mainPanel.setLayout(null);

        // Icono y etiquetas de texto del diálogo.
        JLabel iconLabel = new JLabel("!", SwingConstants.CENTER);
        iconLabel.setForeground(new Color(255, 140, 0));
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(255, 140, 0, 30));
        iconLabel.setBounds(30, 30, 40, 40);
        mainPanel.add(iconLabel);

        JLabel titleLabel = new JLabel("CONFIRMAR SALIDA");
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBounds(90, 25, 300, 25);
        mainPanel.add(titleLabel);

        JLabel msgLabel = new JLabel("<html><div style='line-height: 1.4;'>¿Está seguro que desea cerrar sesión y salir del sistema?</div></html>");
        msgLabel.setForeground(new Color(156, 163, 175));
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setBounds(90, 55, 320, 40);
        mainPanel.add(msgLabel);

        // Botón para confirmar la salida.
        JButton exitBtn = createDialogButton("SALIR", true, PRIMARY_COLOR);
        exitBtn.setBounds(250, 130, 100, 40);
        exitBtn.addActionListener(e -> {
            dialog.dispose(); // Cierra el diálogo.
            dispose(); // Cierra la ventana principal.
            loginFrame.setVisible(true); // Muestra la ventana de login.
        });
        mainPanel.add(exitBtn);

        // Botón para cancelar la acción.
        JButton cancelBtn = createDialogButton("CANCELAR", false, null);
        cancelBtn.setBounds(120, 130, 100, 40);
        cancelBtn.addActionListener(e -> dialog.dispose()); // Simplemente cierra el diálogo.
        mainPanel.add(cancelBtn);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Crea un botón estilizado para usar en los diálogos.
     * @param text El texto del botón.
     * @param isPrimary Booleano que indica si el botón es primario (con color de acento) o secundario.
     * @param primaryColor El color principal del botón si es primario. Puede ser nulo.
     * @return El JButton estilizado.
     */
    private JButton createDialogButton(String text, boolean isPrimary, Color primaryColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor;
                if (isPrimary) {
                    // Botón primario con efectos de hover/pressed.
                    Color baseColor = primaryColor != null ? primaryColor : PRIMARY_COLOR;
                    bgColor = getModel().isPressed() ? baseColor.darker() :
                                getModel().isRollover() ? baseColor.brighter() : baseColor;
                } else {
                    // Botón secundario con efectos de hover/pressed.
                    bgColor = getModel().isPressed() ? new Color(71, 85, 105) :
                                getModel().isRollover() ? new Color(51, 65, 85) :
                                new Color(30, 41, 59);
                }

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Borde para botones secundarios.
                if (!isPrimary) {
                    g2d.setColor(new Color(71, 85, 105));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                }

                // Dibuja el texto del botón.
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };

        // Configuración de la apariencia general del botón.
        button.setFont(new Font("Segoe UI", isPrimary ? Font.BOLD : Font.PLAIN, 13));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Método auxiliar para redimensionar un ImageIcon de forma segura y eficiente.
     * @param path La ruta del archivo de imagen.
     * @param width El nuevo ancho de la imagen.
     * @param height El nuevo alto de la imagen.
     * @return El ImageIcon redimensionado, o null si ocurre un error.
     */
    private ImageIcon getResizedIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(path);
            Image originalImage = originalIcon.getImage();
            // Redimensiona la imagen usando un algoritmo de escalado de alta calidad.
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            // Manejo de errores en caso de que el archivo de imagen no se encuentre.
            System.err.println("Error al cargar o redimensionar la imagen: " + e.getMessage());
            return null;
        }
    }
}
