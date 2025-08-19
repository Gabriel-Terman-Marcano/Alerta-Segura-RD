package alertasegurard.views;

import controllers.AlertaController;
import alertasegurard.models.Alerta;
import alertasegurard.models.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.List;

/**
 * **Clase:** AlertasApartado
 *
 * **Descripción:**
 * Esta clase representa la interfaz de usuario (GUI) para la gestión de alertas en la aplicación.
 * Permite a los usuarios visualizar, buscar, agregar, editar y eliminar alertas a través de
 * una interfaz de usuario moderna y estilizada construida con Swing.
 *
 * @author Grupo #1
 */
public class AlertasApartado extends JFrame {

    // Atributos de la clase
    private final MainMenuFrame parentFrame;
    private final AlertaController alertaController;
    private JPanel cardsPanel;
    private JScrollPane scrollPane;
    private JTextField txtBuscar;
    private JButton btnAgregarAlerta, btnVolver;

    // Constantes de colores para un diseño consistente (Paleta de color azul)
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);   // Azul principal
    private final Color SECONDARY_COLOR = new Color(37, 99, 235);  // Azul más oscuro para botones
    private final Color ACCENT_COLOR = new Color(96, 165, 250);    // Azul claro para hover
    private final Color BACKGROUND_COLOR = new Color(17, 24, 39);   // Fondo oscuro
    private final Color SURFACE_COLOR = new Color(31, 41, 55);    // Superficie de tarjetas/paneles
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color CARD_BACKGROUND = new Color(55, 65, 81);
    private final Color BORDER_COLOR = new Color(75, 85, 99, 100);

    // Colores específicos para los niveles de alerta
    private final Color ALTA_COLOR = new Color(239, 68, 68);   // Rojo
    private final Color MEDIA_COLOR = new Color(251, 191, 36);  // Amarillo
    private final Color BAJA_COLOR = new Color(34, 197, 94);    // Verde

    // Variable para la imagen de fondo
    private ImageIcon backgroundImage;

    /**
     * Constructor de la clase `AlertasApartado`.
     * Inicializa la ventana de gestión de alertas, sus componentes y sus funcionalidades.
     *
     * @param usuario El usuario que ha iniciado sesión.
     * @param parentFrame La ventana principal desde donde se accede a este apartado.
     */
    public AlertasApartado(Usuario usuario, MainMenuFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.alertaController = new AlertaController();

        // Configuración básica de la ventana
        setTitle("Alerta Segura RD - Gestión de Alertas");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        loadResources();
        initComponents();
        addWindowDragListener();
        cargarAlertasEnTarjetas();
    }

    /**
     * Carga los recursos visuales como la imagen de fondo.
     */
    private void loadResources() {
        try {
            URL imageUrl = getClass().getResource("/alertasegurard/main/resources/images/background.png");
            if (imageUrl != null) {
                this.backgroundImage = new ImageIcon(imageUrl);
            } else {
                System.err.println("Error: No se pudo encontrar la imagen de fondo. La aplicación usará un color sólido.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            this.backgroundImage = null;
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

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 150));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

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

        createHeader(mainPanel);
        createToolBar(mainPanel);
        createCardsPanel(mainPanel);

        btnVolver = createSecondaryButton("VOLVER AL MENÚ");
        btnVolver.setBounds(30, 640, 180, 40);
        btnVolver.addActionListener(e -> {
            parentFrame.setVisible(true);
            dispose();
        });
        mainPanel.add(btnVolver);
    }

    private void createHeader(JPanel parent) {
        JLabel title = new JLabel("GESTIÓN DE ALERTAS");
        title.setForeground(PRIMARY_COLOR);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setBounds(30, 30, 600, 40);
        parent.add(title);

        JLabel subtitle = new JLabel("Visualiza, crea, edita y elimina alertas de emergencia.");
        subtitle.setForeground(new Color(156, 163, 175));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setBounds(30, 70, 600, 20);
        parent.add(subtitle);
    }

    private void createToolBar(JPanel parent) {
        JPanel toolBar = new JPanel();
        toolBar.setOpaque(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setBounds(30, 110, 940, 50);

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 15, 5, 15)
        ));
        txtBuscar.setForeground(TEXT_COLOR);
        txtBuscar.setBackground(CARD_BACKGROUND);
        txtBuscar.setCaretColor(TEXT_COLOR);
        toolBar.add(txtBuscar);

        JButton btnBuscar = createSecondaryButton("BUSCAR");
        btnBuscar.addActionListener(e -> buscarAlerta());
        toolBar.add(btnBuscar);

        btnAgregarAlerta = createPrimaryButton("AGREGAR ALERTA");
        btnAgregarAlerta.addActionListener(e -> agregarNuevaAlerta());
        toolBar.add(btnAgregarAlerta);

        parent.add(toolBar);
    }

    /**
     * Crea el panel principal que contendrá las tarjetas de alerta y el panel de desplazamiento.
     * @param parent El panel al que se añadirá el área de tarjetas.
     */
    private void createCardsPanel(JPanel parent) {
        cardsPanel = new JPanel();
        // Cambia el layout a GridLayout con 3 columnas
        cardsPanel.setLayout(new GridLayout(0, 3, 15, 15));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Crea el panel con scroll para que las tarjetas sean desplazables
        scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBounds(30, 180, 940, 450);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new DarkThemeScrollBarUI());
        parent.add(scrollPane);
    }

    public void cargarAlertasEnTarjetas() {
        cardsPanel.removeAll();
        cardsPanel.revalidate();
        cardsPanel.repaint();

        List<Alerta> alertas = alertaController.obtenerTodas();
        for (Alerta alerta : alertas) {
            JPanel card = createAlertCard(alerta);
            cardsPanel.add(card);
        }
    }

    private JPanel createAlertCard(Alerta alerta) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(CARD_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(getNivelColor(alerta.getNivelAlerta()));
                g2d.fillRect(getWidth() - 10, 0, 10, getHeight());

                g2d.setColor(BORDER_COLOR);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        card.setLayout(null);
        card.setPreferredSize(new Dimension(280, 200));
        card.setOpaque(false);
        card.setBorder(null);

        JLabel titleLabel = new JLabel(alerta.getTitulo());
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBounds(15, 15, 250, 25);
        card.add(titleLabel);

        JLabel typeLabel = new JLabel("Tipo: ");
        typeLabel.setForeground(TEXT_COLOR);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setBounds(15, 50, 250, 20);
        card.add(typeLabel);

        JLabel typeeLabel = new JLabel(alerta.getTipoAlerta());
        typeeLabel.setForeground(TEXT_COLOR);
        typeeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeeLabel.setBounds(53, 50, 250, 20);
        card.add(typeeLabel);

        JLabel levelLabel = new JLabel("Nivel: ");
        levelLabel.setForeground(TEXT_COLOR);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        levelLabel.setBounds(15, 75, 250, 20);
        card.add(levelLabel);

        JLabel leveelLabel = new JLabel(alerta.getNivelAlerta());
        leveelLabel.setForeground(TEXT_COLOR);
        leveelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leveelLabel.setBounds(60, 75, 250, 20);
        card.add(leveelLabel);

        JLabel sectorLabel = new JLabel("Lugar/es afectado/s: ");
        sectorLabel.setForeground(TEXT_COLOR);
        sectorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectorLabel.setBounds(15, 100, 250, 20);
        card.add(sectorLabel);

        JLabel sectorrLabel = new JLabel(alerta.getSectorAfectado());
        sectorrLabel.setForeground(TEXT_COLOR);
        sectorrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sectorrLabel.setBounds(155, 100, 250, 20);
        card.add(sectorrLabel);

        JButton detailsButton = createSecondaryButton("VER DETALLES");
        detailsButton.setBounds(15, 140, 150, 35);
        detailsButton.addActionListener(e -> verDetalles(alerta));
        card.add(detailsButton);

        return card;
    }

    private Color getNivelColor(String nivel) {
        if (nivel == null) {
            return new Color(107, 114, 128);
        }
        return switch (nivel.toUpperCase()) {
            case "ALTA" -> ALTA_COLOR;
            case "MEDIA" -> MEDIA_COLOR;
            case "BAJA" -> BAJA_COLOR;
            default -> new Color(107, 114, 128);
        };
    }

    private void buscarAlerta() {
        String query = txtBuscar.getText().trim();
        List<Alerta> resultados = alertaController.buscarPorTitulo(query);

        cardsPanel.removeAll();
        for (Alerta alerta : resultados) {
            JPanel card = createAlertCard(alerta);
            cardsPanel.add(card);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void agregarNuevaAlerta() {
        AgregarAlerta agregarAlertaFrame = new AgregarAlerta(this);
        agregarAlertaFrame.setVisible(true);
        this.setVisible(false);
    }

    private void verDetalles(Alerta alerta) {
        JDialog detalleDialog = new JDialog(this, "Detalles de la Alerta", true);
        detalleDialog.setLayout(new BorderLayout(10, 10));
        detalleDialog.setSize(600, 700);
        detalleDialog.setLocationRelativeTo(this);
        detalleDialog.getContentPane().setBackground(SURFACE_COLOR);
        detalleDialog.setUndecorated(true);
        detalleDialog.setShape(new RoundRectangle2D.Double(0, 0, detalleDialog.getWidth(), detalleDialog.getHeight(), 20, 20));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(SURFACE_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel imagenLabel = new JLabel();
        imagenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String imagenUrl = alerta.getImagenUrl();
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            try {
                ImageIcon originalIcon = new ImageIcon(imagenUrl);
                Image originalImage = originalIcon.getImage();

                int width = 400;
                int height = (originalImage.getHeight(null) * width) / originalImage.getWidth(null);
                Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                imagenLabel.setIcon(new ImageIcon(resizedImage));
            } catch (Exception e) {
                imagenLabel.setText("No se pudo cargar la imagen.");
                imagenLabel.setForeground(PRIMARY_COLOR);
                imagenLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            }
        } else {
            imagenLabel.setText("No hay imagen disponible.");
            imagenLabel.setForeground(new Color(156, 163, 175));
            imagenLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
        contentPanel.add(imagenLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        JLabel tituloLabel = new JLabel("<html><h3 style='color: " + toHex(PRIMARY_COLOR) + ";'>" + alerta.getTitulo() + "</h3></html>");
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(tituloLabel);

        contentPanel.add(createDetailLabel("Tipo:", alerta.getTipoAlerta()));
        contentPanel.add(createDetailLabel("Nivel:", alerta.getNivelAlerta()));
        contentPanel.add(createDetailLabel("Lugar/es afectado/s:", "<html><div style='width: 400px; color: white;'>" + alerta.getSectorAfectado() + "</div></html>"));
        contentPanel.add(createDetailLabel("Descripción:", "<html><div style='width: 400px; color: white;'>" + alerta.getDescripcion() + "</div></html>"));
        contentPanel.add(createDetailLabel("Instrucciones:", "<html><div style='width: 400px; color: white;'>" + alerta.getInstrucciones() + "</div></html>"));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(SURFACE_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnEliminar = createPrimaryButton("ELIMINAR");
        btnEliminar.setPreferredSize(new Dimension(150, 40));
        btnEliminar.addActionListener(e -> {
            boolean confirmacion = JOptionPane.showConfirmDialog(detalleDialog, "¿Está seguro que desea eliminar esta alerta?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            if (confirmacion) {
                if (alertaController.eliminarAlerta(alerta.getId())) {
                    showStyledMessage("Alerta eliminada exitosamente.", "success");
                    cargarAlertasEnTarjetas();
                    detalleDialog.dispose();
                } else {
                    showStyledMessage("Error al eliminar la alerta.", "error");
                }
            }
        });
        buttonPanel.add(btnEliminar);

        JButton btnCerrar = createSecondaryButton("CERRAR");
        btnCerrar.setPreferredSize(new Dimension(150, 40));
        btnCerrar.addActionListener(e -> detalleDialog.dispose());
        buttonPanel.add(btnCerrar);

        detalleDialog.add(scrollPane, BorderLayout.CENTER);
        detalleDialog.add(buttonPanel, BorderLayout.SOUTH);
        detalleDialog.setVisible(true);
    }

    private JLabel createDetailLabel(String labelText, String valueText) {
        JLabel label = new JLabel("<html><p style='color: " + toHex(new Color(156, 163, 175)) + "; font-weight: bold;'>" + labelText + "</p><p style='color: white; margin-top: 5px;'>" + valueText + "</p></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private String toHex(Color c) {
        return String.format("#%06X", (0xFFFFFF & c.getRGB()));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = getModel().isPressed() ? SECONDARY_COLOR : getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;
                GradientPaint gp = new GradientPaint(0, 0, bgColor, 0, getHeight(), new Color(bgColor.getRed() - 20, bgColor.getGreen() - 20, bgColor.getBlue() - 20));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
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

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = getModel().isPressed() ? new Color(75, 85, 99) : getModel().isRollover() ? new Color(55, 65, 81) : new Color(31, 41, 55);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.setColor(new Color(75, 85, 99));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
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
                if (mouseDownCompCoords[0] != null) {
                    setLocation(currCoords.x - mouseDownCompCoords[0].x, currCoords.y - mouseDownCompCoords[0].y);
                }
            }
        });
    }

    private void showStyledMessage(String message, String type) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(420, 180);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel;
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint baseBg = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                g2d.setPaint(baseBg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                Color barColor;
                barColor = switch (type) {
                    case "success" -> new Color(34, 197, 94);
                    case "error" -> ALTA_COLOR;
                    default -> new Color(251, 191, 36);
                };

                g2d.setColor(barColor);
                g2d.fillRoundRect(0, 0, getWidth(), 6, 20, 20);
                g2d.setColor(new Color(75, 85, 99));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        mainPanel.setLayout(null);

        String title = type.equals("success") ? "ÉXITO" : type.equals("error") ? "ERROR" : "ADVERTENCIA";

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

        JButton acceptBtn = createDialogButton("ACEPTAR", true);
        acceptBtn.setBounds(260, 120, 120, 40);
        acceptBtn.addActionListener(e -> dialog.dispose());
        mainPanel.add(acceptBtn);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JButton createDialogButton(String text, boolean isPrimary) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor;
                if (isPrimary) {
                    bgColor = getModel().isPressed() ? SECONDARY_COLOR : getModel().isRollover() ? ACCENT_COLOR : PRIMARY_COLOR;
                } else {
                    bgColor = getModel().isPressed() ? new Color(75, 85, 99) : getModel().isRollover() ? new Color(55, 65, 81) : new Color(31, 41, 55);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                if (!isPrimary) {
                    g2d.setColor(new Color(75, 85, 99));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        button.setFont(new Font("Segoe UI", isPrimary ? Font.BOLD : Font.PLAIN, 12));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
