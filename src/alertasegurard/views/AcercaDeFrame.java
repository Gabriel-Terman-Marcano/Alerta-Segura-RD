// Archivo: src/alertasegurard/views/AcercaDeFrame.java
package alertasegurard.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import alertasegurard.models.Usuario;
import java.net.URL;

/**
 * Ventana de "Acerca de" que muestra información sobre la aplicación,
 * el equipo de desarrollo y las funcionalidades del sistema.
 * <p>
 * Incluye un diseño moderno y una paleta de colores consistente con
 * el resto del proyecto. También proporciona un enlace para abrir
 * un manual de usuario.
 * </p>
 * @author Grupo #1
 */
public class AcercaDeFrame extends JFrame {
    private Usuario usuarioLogueado;
    private JFrame menuAnterior;
    
    // Paleta de colores de la interfaz
    private final Color PRIMARY_COLOR = new Color(220, 38, 38);
    private final Color SECONDARY_COLOR = new Color(185, 28, 28);
    private final Color ACCENT_COLOR = new Color(239, 68, 68);
    private final Color BACKGROUND_COLOR = new Color(15, 23, 42);
    private final Color SURFACE_COLOR = new Color(30, 41, 59);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color SECONDARY_TEXT = new Color(156, 163, 175);
    private final Color CARD_BACKGROUND = new Color(51, 65, 85);

    // Variable para la imagen de fondo
    private ImageIcon backgroundImage;
    /**
     * Constructor de la clase AcercaDeFrame.
     *
     * @param usuario        El objeto Usuario del usuario actualmente logueado.
     * @param menuAnterior   La instancia de la ventana del menú principal,
     * necesaria para volver a ella.
     */
    public AcercaDeFrame(Usuario usuario, JFrame menuAnterior) {
        this.usuarioLogueado = usuario;
        this.menuAnterior = menuAnterior;
        setTitle("Alerta Segura RD - Acerca de");
        setSize(650, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        
        // Aplica bordes redondeados a la ventana
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        
        loadResources(); // Carga la imagen de fondo antes de inicializar los componentes
        initComponents();
        addWindowDragListener();
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

        JButton closeBtn = createCloseButton();
        closeBtn.setBounds(600, 15, 35, 35);
        mainPanel.add(closeBtn);

        createHeader(mainPanel);
        createContentCards(mainPanel);
        createActionButtons(mainPanel);
        createFooter(mainPanel);
    }
    
    /**
     * Crea y añade los componentes de la cabecera de la ventana.
     *
     * @param parent El panel principal al que se añadirán los componentes.
     */
    private void createHeader(JPanel parent) {
        JLabel title = new JLabel("ALERTA SEGURA RD");
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(0, 40, 650, 35);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        parent.add(title);

        JLabel subtitle = new JLabel("Sistema Nacional de Gestión de Riesgos y Emergencias");
        subtitle.setForeground(SECONDARY_TEXT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setBounds(0, 75, 650, 20);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        parent.add(subtitle);
        
        JPanel line = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 0),
                    getWidth() / 2f, 0, PRIMARY_COLOR
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 2, 2);
            }
        };
        line.setBounds(225, 105, 200, 2);
        parent.add(line);
    }
    
    /**
     * Crea y añade los paneles de contenido (tarjetas) a la ventana.
     *
     * @param parent El panel principal al que se añadirán las tarjetas.
     */
    private void createContentCards(JPanel parent) {
        JPanel infoCard = createInfoCard();
        infoCard.setBounds(40, 130, 570, 180);
        parent.add(infoCard);
        
        JPanel teamCard = createTeamCard();
        teamCard.setBounds(40, 330, 270, 200);
        parent.add(teamCard);
        
        JPanel featuresCard = createFeaturesCard();
        featuresCard.setBounds(340, 330, 270, 200);
        parent.add(featuresCard);
    }
    
    /**
     * Crea un panel con información general sobre el sistema.
     *
     * @return Un JPanel con el diseño y el contenido de la tarjeta de información.
     */
    private JPanel createInfoCard() {
        JPanel card = createStyledCard();
        card.setLayout(null);
        
        JLabel cardTitle = new JLabel("INFORMACIÓN DEL SISTEMA");
        cardTitle.setForeground(TEXT_COLOR);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cardTitle.setBounds(25, 20, 300, 25);
        card.add(cardTitle);
        
        String infoText = "<html><div style='line-height: 1.6; color: #9CA3AF;'>" +
            "<b style='color: #EF4444;'>Versión:</b> 1.0.0<br><br>" +
            "<b style='color: #EF4444;'>Propósito:</b> Solución integral para la gestión de alertas " +
            "y preparación ciudadana ante desastres naturales en República Dominicana.<br><br>" +
            "<b style='color: #EF4444;'>Tecnologías:</b> Java Swing, MySQL, MVC Architecture<br>" +
            "<b style='color: #EF4444;'>Estado:</b> Producción" +
            "</div></html>";
            
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setBounds(25, 55, 520, 110);
        card.add(infoLabel);
        
        return card;
    }
    
    /**
     * Crea un panel con información sobre el equipo de desarrollo.
     *
     * @return Un JPanel con el diseño y el contenido de la tarjeta del equipo.
     */
    private JPanel createTeamCard() {
        JPanel card = createStyledCard();
        card.setLayout(null);
        
        JLabel cardTitle = new JLabel("EQUIPO DE DESARROLLO");
        cardTitle.setForeground(TEXT_COLOR);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardTitle.setBounds(20, 15, 200, 20);
        card.add(cardTitle);
        
        String teamText = "<html><div style='line-height: 1.8; color: #9CA3AF;'>" +
            "<div style='margin: 8px 0;'>" +
            "<span style='color: #EF4444; font-weight: bold;'>Líder del Proyecto</span><br>" +
            "Candelaria Pereyra" +
            "</div>" +
            "<div style='margin: 8px 0;'>" +
            "<span style='color: #EF4444; font-weight: bold;'>Database Administrator</span><br>" +
            "Gabriel Terman" +
            "</div>" +
            "<div style='margin: 8px 0;'>" +
            "<span style='color: #EF4444; font-weight: bold;'>SQA & UI Designer</span><br>" +
            "Dalexa Matos" +
            "</div>" +
            "</div></html>";
            
        JLabel teamLabel = new JLabel(teamText);
        teamLabel.setBounds(20, 45, 230, 140);
        card.add(teamLabel);
        
        return card;
    }
    
    /**
     * Crea un panel con la lista de funcionalidades clave del sistema.
     *
     * @return Un JPanel con el diseño y el contenido de la tarjeta de funcionalidades.
     */
    private JPanel createFeaturesCard() {
        JPanel card = createStyledCard();
        card.setLayout(null);
        
        JLabel cardTitle = new JLabel("FUNCIONALIDADES");
        cardTitle.setForeground(TEXT_COLOR);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardTitle.setBounds(20, 15, 200, 20);
        card.add(cardTitle);
        
        String featuresText = "<html><div style='line-height: 1.6; color: #9CA3AF;'>" +
            "• <b style='color: #FFF;'>Gestión de Usuarios</b><br>" +
            "  Registro y administración<br><br>" +
            "• <b style='color: #FFF;'>Sistema de Alertas</b><br>" +
            "  Alertas por zona y tipo<br><br>" +
            "• <b style='color: #FFF;'>Notificaciones</b><br>" +
            "  Tiempo real y historial<br><br>" +
            "• <b style='color: #FFF;'>Reportes</b><br>" +
            "  Estadísticas y análisis" +
            "</div></html>";
            
        JLabel featuresLabel = new JLabel(featuresText);
        featuresLabel.setBounds(20, 45, 230, 140);
        card.add(featuresLabel);
        
        return card;
    }
    
    /**
     * Crea un panel con el estilo visual común a todas las tarjetas de información.
     *
     * @return Un JPanel con el estilo de tarjeta aplicado.
     */
    private JPanel createStyledCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Aplica un gradiente sutil al fondo de la tarjeta
                GradientPaint cardBg = new GradientPaint(0, 0, CARD_BACKGROUND, 
                    0, getHeight(), new Color(CARD_BACKGROUND.getRed() - 10, 
                    CARD_BACKGROUND.getGreen() - 10, CARD_BACKGROUND.getBlue() - 10));
                g2d.setPaint(cardBg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Dibuja un borde sutil alrededor de la tarjeta
                g2d.setColor(new Color(71, 85, 105, 120));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                
                // Añade un efecto de brillo en la parte superior
                GradientPaint shine = new GradientPaint(0, 0, new Color(255, 255, 255, 15), 
                    0, 30, new Color(255, 255, 255, 0));
                g2d.setPaint(shine);
                g2d.fillRoundRect(1, 1, getWidth()-2, 30, 11, 11);
            }
        };
        card.setOpaque(false);
        return card;
    }
    
    /**
     * Crea y añade los botones de acción a la ventana.
     *
     * @param parent El panel principal al que se añadirán los botones.
     */
    private void createActionButtons(JPanel parent) {
        JButton manualBtn = createActionButton("MANUAL DE USUARIO", false);
        manualBtn.setBounds(80, 550, 220, 45);
        manualBtn.addActionListener(e -> showUserManual());
        parent.add(manualBtn);
        
        JButton volverBtn = createActionButton("VOLVER AL MENÚ", true);
        volverBtn.setBounds(350, 550, 220, 45);
        volverBtn.addActionListener(e -> {
            this.dispose();
            menuAnterior.setVisible(true);
        });
        parent.add(volverBtn);
    }
    
    /**
     * Crea y añade los componentes del pie de página a la ventana.
     *
     * @param parent El panel principal al que se añadirán los componentes.
     */
    private void createFooter(JPanel parent) {
        JLabel copyright = new JLabel("© 2025 ITLA - Instituto Tecnológico de Las Américas");
        copyright.setForeground(new Color(107, 114, 128));
        copyright.setFont(new Font("Segoe UI", Font.BOLD, 11));
        copyright.setBounds(0, 650, 650, 15);
        copyright.setHorizontalAlignment(SwingConstants.CENTER);
        parent.add(copyright);
        
        JLabel rights = new JLabel("Todos los derechos reservados • Proyecto Académico");
        rights.setForeground(new Color(107, 114, 128));
        rights.setFont(new Font("Segoe UI", Font.BOLD, 10));
        rights.setBounds(0, 670, 650, 15);
        rights.setHorizontalAlignment(SwingConstants.CENTER);
        parent.add(rights);
    }
    
    /**
     * Crea un botón de acción con un estilo visual personalizado.
     *
     * @param text      El texto que se mostrará en el botón.
     * @param isPrimary Define si el botón tendrá el estilo primario (rojo) o secundario (gris).
     * @return El JButton estilizado.
     */
    private JButton createActionButton(String text, boolean isPrimary) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (isPrimary) {
                    bgColor = getModel().isPressed() ? SECONDARY_COLOR : 
                              getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;
                    
                    GradientPaint gp = new GradientPaint(0, 0, bgColor, 0, getHeight(), 
                        new Color(bgColor.getRed() - 15, bgColor.getGreen() - 15, bgColor.getBlue() - 15));
                    g2d.setPaint(gp);
                } else {
                    bgColor = getModel().isPressed() ? new Color(71, 85, 105) : 
                              getModel().isRollover() ? new Color(51, 65, 85) : CARD_BACKGROUND;
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                if (!isPrimary) {
                    g2d.setColor(new Color(71, 85, 105));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                }
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", isPrimary ? Font.BOLD : Font.PLAIN, 13));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Crea un botón estilizado para cerrar la ventana.
     *
     * @return El JButton de cierre.
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
        closeBtn.addActionListener(e -> {
            this.dispose();
            menuAnterior.setVisible(true);
        });
        return closeBtn;
    }
    
    /**
     * Permite arrastrar la ventana sin la barra de título.
     */
    private void addWindowDragListener() {
        final Point[] mouseDownCompCoords = {null};
        
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords[0] = null;
            }

            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords[0].x, 
                           currCoords.y - mouseDownCompCoords[0].y);
            }
        });
    }
    
    /**
     * Muestra una ventana de diálogo con el manual de usuario del sistema.
     * El manual detalla las funcionalidades principales y el flujo de trabajo.
     */
    private void showUserManual() {
        JDialog manualDialog = new JDialog(this, "Manual de Usuario - Alerta Segura RD", true);
        manualDialog.setUndecorated(true);
        manualDialog.setSize(500, 600);
        manualDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(new Color(71, 85, 105));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Header del manual
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new FlowLayout());
        
        JLabel manualTitle = new JLabel("MANUAL DE USUARIO");
        manualTitle.setForeground(TEXT_COLOR);
        manualTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(manualTitle);
        
        // Contenido del manual
        String manualContent = """
                GUÍA DE USO DEL SISTEMA
                
                INICIO DE SESIÓN
                1. Ingrese su número de cédula como usuario
                2. Introduzca su contraseña
                3. Presione "Iniciar Sesión"
                
                REGISTRO DE NUEVOS USUARIOS
                1. Desde la pantalla de login, presione "Registrarse"
                2. Complete todos los campos obligatorios (*)
                3. Confirme su contraseña
                4. Presione "Registrar Usuario"
                
                GESTIÓN DE USUARIOS
                • Crear: Agregar nuevos usuarios al sistema
                • Buscar: Localizar usuarios por cédula
                • Modificar: Actualizar información de usuarios
                • Eliminar: Remover usuarios del sistema
                
                SISTEMA DE ALERTAS
                • Crear Alertas: Por tipo de desastre y zona
                • Gestionar:  Eiminar alertas activas
                • Visualizar: Ver historial y estado actual
                • Notificar: Envío automático a usuarios registrados
                
                TIPOS DE ALERTAS DISPONIBLES
                • Huracanes y Tormentas Tropicales
                • Terremotos y Sismos
                • Inundaciones
                • Incendios Forestales
                • Deslizamientos de Tierra
                • Sequías
                • Tsunamis
                • Evento Local
                
                CONSEJOS IMPORTANTES
                • Mantenga actualizada su información de contacto
                • Revise regularmente las alertas de su zona
                • Siga las recomendaciones de las autoridades
                • Mantenga un plan de emergencia familiar
                
                SOPORTE TÉCNICO
                Para asistencia técnica, contacte al equipo
                de desarrollo del ITLA.
                """;
        
        JTextArea manualText = new JTextArea(manualContent);
        manualText.setEditable(false);
        manualText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        manualText.setForeground(SECONDARY_TEXT);
        manualText.setOpaque(false);
        manualText.setLineWrap(true);
        manualText.setWrapStyleWord(true);
        manualText.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(manualText);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JButton closeManualBtn = createActionButton("CERRAR", true);
        closeManualBtn.addActionListener(e -> manualDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeManualBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        manualDialog.add(mainPanel);
        manualDialog.setVisible(true);
    }
}
