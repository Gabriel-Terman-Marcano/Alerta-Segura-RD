/*
 * Este archivo contiene la clase 'AgregarAlerta', una ventana de interfaz gráfica (GUI)
 * que permite a los usuarios agregar una nueva alerta a la base de datos.
 * Utiliza el framework Swing para la creación de componentes visuales.
 * El diseño es personalizado para seguir una paleta de colores moderna y oscura.
 */
package alertasegurard.views;

/**
 * Clase que representa la ventana para agregar una nueva alerta al sistema.
 * Extiende de JFrame para crear una ventana.
 *
 * @author Grupo #1
 */
import alertasegurard.dao.AlertaDAO;
import alertasegurard.models.Alerta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class AgregarAlerta extends JFrame{
    // === Variables de instancia para los componentes de la GUI ===
    private JTextField txtTitulo, txtTipoAlerta, txtSector;
    private JTextArea txtDescripcion, txtInstrucciones;
    private JComboBox<String> nivelComboBox; // Cambiado de JTextField a JComboBox
    private JButton btnAgregar, btnCancelar, btnSubirImagen;
    private JLabel lblImagenUrl;
    private String rutaImagen;
    
    // === Variables de negocio y de referencia ===
    private AlertaDAO alertaDAO; // Objeto para interactuar con la base de datos
    private AlertasApartado parentFrame; // Referencia a la ventana padre para refrescar la lista de alertas

    // === Paleta de colores utilizada para el diseño de la interfaz ===
    private final Color PRIMARY_COLOR = new Color(220, 38, 38);
    private final Color SECONDARY_COLOR = new Color(185, 28, 28);
    private final Color ACCENT_COLOR = new Color(239, 68, 68);
    private final Color BACKGROUND_COLOR = new Color(15, 23, 42);
    private final Color SURFACE_COLOR = new Color(30, 41, 59);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color INPUT_BACKGROUND = new Color(51, 65, 85);
    private final Color CARD_BACKGROUND = new Color(30, 41, 59, 180);

    /**
     * Constructor de la clase AgregarAlerta.
     * @param parentFrame Referencia a la ventana principal para poder interactuar con ella.
     */
    public AgregarAlerta(AlertasApartado parentFrame) {
        this.parentFrame = parentFrame;
        this.alertaDAO = new AlertaDAO();
        
        // === Configuración básica de la ventana ===
        setTitle("Alerta Segura RD - Agregar Alerta");
        setSize(650, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Cierra solo esta ventana, no toda la aplicación
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setResizable(false);
        setUndecorated(true); // Elimina el marco de la ventana predeterminado
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20)); // Aplica esquinas redondeadas
        
        // Inicializa los componentes de la interfaz
        initComponents();
        
        // Agrega un listener para arrastrar la ventana sin marco
        addWindowDragListener();
    }

    /**
     * Método para inicializar todos los componentes de la GUI y su layout.
     */
    private void initComponents() {
        // Panel principal con un fondo degradado personalizado
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Pinta el fondo con un degradado de color
                GradientPaint gp = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Dibuja pequeños círculos como patrón de fondo
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

        // Botón para cerrar la ventana
        JButton closeBtn = createCloseButton();
        closeBtn.setBounds(600, 15, 35, 35);
        mainPanel.add(closeBtn);

        // Título principal de la ventana
        JLabel title = new JLabel("AGREGAR NUEVA ALERTA");
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 25, 650, 35);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);

        // Subtítulo de la ventana
        JLabel subtitle = new JLabel("Complete los campos para emitir una alerta oficial");
        subtitle.setForeground(new Color(156, 163, 175));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setBounds(0, 60, 650, 20);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitle);

        // Panel que contiene el formulario de entrada de datos
        JPanel formPanel = createFormPanel();
        formPanel.setBounds(70, 85, 510, 675);
        mainPanel.add(formPanel);

        // Etiqueta de pie de página (footer)
        JLabel footerLabel = new JLabel("Todos los campos marcados con * son obligatorios");
        footerLabel.setForeground(new Color(107, 114, 128));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setBounds(0, 740, 650, 20);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(footerLabel);
    }

    /**
     * Crea y configura el panel del formulario con todos los campos de entrada.
     * @return JPanel con el formulario.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Rellena el panel con un color de tarjeta y esquinas redondeadas
                g2d.setColor(new Color(30, 41, 59, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Dibuja un borde de color en el panel
                g2d.setColor(new Color(71, 85, 105, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setLayout(null);
        
        // Posicionamiento de los componentes dentro del formulario
        int yPosition = 30;
        int fieldHeight = 45;
        int labelHeight = 20;
        int spacing = 75;
        int padding = 30;
        int formWidth = 510;
        int singleFieldWidth = formWidth - 2 * padding;
        int halfFieldWidth = (formWidth - 3 * padding) / 2;

        // Campo Título
        JLabel lblTitulo = new JLabel("TÍTULO *");
        lblTitulo.setForeground(TEXT_COLOR);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setBounds(padding, yPosition, 200, labelHeight);
        formPanel.add(lblTitulo);

        txtTitulo = createStyledTextField();
        txtTitulo.setBounds(padding, yPosition + 25, singleFieldWidth, fieldHeight);
        formPanel.add(txtTitulo);

        yPosition += spacing;
        
        // Campo Tipo y Nivel (uno al lado del otro)
        JLabel lblTipo = new JLabel("TIPO *");
        lblTipo.setForeground(TEXT_COLOR);
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTipo.setBounds(padding, yPosition, 200, labelHeight);
        formPanel.add(lblTipo);
        
        txtTipoAlerta = createStyledTextField();
        txtTipoAlerta.setBounds(padding, yPosition + 25, halfFieldWidth, fieldHeight);
        formPanel.add(txtTipoAlerta);

        JLabel lblNivel = new JLabel("NIVEL *");
        lblNivel.setForeground(TEXT_COLOR);
        lblNivel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNivel.setBounds(padding + halfFieldWidth + padding, yPosition, 200, labelHeight);
        formPanel.add(lblNivel);
        
        String[] niveles = {"ALTA", "MEDIA", "BAJA"};
        nivelComboBox = new JComboBox<>(niveles);
        nivelComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nivelComboBox.setBackground(INPUT_BACKGROUND);
        nivelComboBox.setForeground(TEXT_COLOR);
        nivelComboBox.setFocusable(false);
        nivelComboBox.setBounds(padding + halfFieldWidth + padding, yPosition + 25, halfFieldWidth, fieldHeight);
        
        // Renderer personalizado para estilizar los elementos del JComboBox
        nivelComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                this.setBackground(INPUT_BACKGROUND);
                this.setForeground(TEXT_COLOR);
                if (isSelected) {
                    this.setBackground(ACCENT_COLOR);
                }
                this.setBorder(new EmptyBorder(5, 15, 5, 15));
                return this;
            }
        });
        
        // Listener para simular el borde de enfoque de los JTextfields
        nivelComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nivelComboBox.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nivelComboBox.setBorder(BorderFactory.createLineBorder(INPUT_BACKGROUND, 1, true));
            }
        });
        
        formPanel.add(nivelComboBox);

        yPosition += spacing;
        
        // Campo Sector
        JLabel lblSector = new JLabel("LUGAR/ES AFECTADO/S *");
        lblSector.setForeground(TEXT_COLOR);
        lblSector.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSector.setBounds(padding, yPosition, 200, labelHeight);
        formPanel.add(lblSector);

        txtSector = createStyledTextField();
        txtSector.setBounds(padding, yPosition + 25, singleFieldWidth, fieldHeight);
        formPanel.add(txtSector);
        
        yPosition += spacing;

        // Campo Descripción
        JLabel lblDescripcion = new JLabel("DESCRIPCIÓN DETALLADA *");
        lblDescripcion.setForeground(TEXT_COLOR);
        lblDescripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDescripcion.setBounds(padding, yPosition, 200, labelHeight);
        formPanel.add(lblDescripcion);

        txtDescripcion = createStyledTextArea();
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setBounds(padding, yPosition + 25, singleFieldWidth, 80);
        scrollDescripcion.setBorder(BorderFactory.createLineBorder(INPUT_BACKGROUND, 1, true));
        scrollDescripcion.getViewport().setBackground(INPUT_BACKGROUND);
        formPanel.add(scrollDescripcion);

        yPosition += 115;
        
        // Campo Instrucciones
        JLabel lblInstrucciones = new JLabel("INSTRUCCIONES DE SEGURIDAD");
        lblInstrucciones.setForeground(TEXT_COLOR);
        lblInstrucciones.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblInstrucciones.setBounds(padding, yPosition, 250, labelHeight);
        formPanel.add(lblInstrucciones);

        txtInstrucciones = createStyledTextArea();
        JScrollPane scrollInstrucciones = new JScrollPane(txtInstrucciones);
        scrollInstrucciones.setBounds(padding, yPosition + 25, singleFieldWidth, 80);
        scrollInstrucciones.setBorder(BorderFactory.createLineBorder(INPUT_BACKGROUND, 1, true));
        scrollInstrucciones.getViewport().setBackground(INPUT_BACKGROUND);
        formPanel.add(scrollInstrucciones);
        
        yPosition += 115;

        // Botón de subir imagen
        btnSubirImagen = createSecondaryButton("SUBIR IMAGEN");
        btnSubirImagen.setBounds(padding, yPosition, 150, 40);
        btnSubirImagen.addActionListener(e -> seleccionarImagen());
        formPanel.add(btnSubirImagen);

        // Etiqueta para mostrar el nombre del archivo de imagen
        lblImagenUrl = new JLabel("La imagen es opcional. No se ha seleccionado una.");
        lblImagenUrl.setForeground(new Color(156, 163, 175));
        lblImagenUrl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblImagenUrl.setBounds(padding + 160, yPosition, singleFieldWidth - 160, 40);
        formPanel.add(lblImagenUrl);

        yPosition += 65;

        // Botón para agregar la alerta a la base de datos
        btnAgregar = createPrimaryButton("AGREGAR ALERTA");
        btnAgregar.setBounds(padding, yPosition, singleFieldWidth, 50);
        btnAgregar.addActionListener(e -> agregarAlerta());
        formPanel.add(btnAgregar);

        yPosition += 65;

        // Botón para cancelar y cerrar la ventana
        btnCancelar = createSecondaryButton("CANCELAR");
        btnCancelar.setBounds(padding, yPosition, singleFieldWidth, 40);
        btnCancelar.addActionListener(e -> cancelar());
        formPanel.add(btnCancelar);

        return formPanel;
    }

    /**
     * Crea un JTextArea con un estilo personalizado (fondo, color de texto, etc.).
     * @return JTextArea estilizado.
     */
    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setForeground(TEXT_COLOR);
        area.setBackground(INPUT_BACKGROUND);
        area.setCaretColor(TEXT_COLOR);
        area.setBorder(new EmptyBorder(10, 15, 10, 15));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return area;
    }
    
    /**
     * Crea un JTextField con un estilo personalizado, incluyendo un efecto de borde
     * cuando está en foco.
     * @return JTextField estilizado.
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(INPUT_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
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
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    /**
     * Crea un botón principal con un estilo personalizado (color de fondo, degradado,
     * efectos de rollover y presionado).
     * @param text El texto a mostrar en el botón.
     * @return JButton con estilo primario.
     */
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

    /**
     * Crea un botón secundario con un estilo personalizado (color de fondo,
     * efectos de rollover y presionado).
     * @param text El texto a mostrar en el botón.
     * @return JButton con estilo secundario.
     */
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = getModel().isPressed() ? new Color(71, 85, 105) : getModel().isRollover() ? new Color(51, 65, 85) : new Color(30, 41, 59);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.setColor(new Color(71, 85, 105));
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

    /**
     * Crea un botón de cierre con estilo circular.
     * @return JButton para cerrar la ventana.
     */
    private JButton createCloseButton() {
        JButton closeBtn = new JButton("×") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = getModel().isPressed() ? PRIMARY_COLOR : getModel().isRollover() ? ACCENT_COLOR : new Color(255, 255, 255, 0);
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
        closeBtn.addActionListener(e -> cancelar());
        return closeBtn;
    }
    
    /**
     * Agrega la funcionalidad de arrastrar la ventana a la barra de título (o cualquier
     * parte de la ventana en este caso, ya que no hay una barra de título).
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
                setLocation(currCoords.x - mouseDownCompCoords[0].x, currCoords.y - mouseDownCompCoords[0].y);
            }
        });
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Agregar Alerta".
     * Realiza la validación de los campos y llama al método DAO para crear la alerta.
     */
    private void agregarAlerta() {
        // Obtiene los valores de los campos de texto
        String titulo = txtTitulo.getText().trim();
        String tipoAlerta = txtTipoAlerta.getText().trim();
        String nivelAlerta = (String) nivelComboBox.getSelectedItem(); // Obtiene el valor del JComboBox
        String sectorAfectado = txtSector.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String instrucciones = txtInstrucciones.getText().trim();
        
        // Validación de campos obligatorios
        if (titulo.isEmpty() || tipoAlerta.isEmpty() || nivelAlerta == null || sectorAfectado.isEmpty() || descripcion.isEmpty()) {
            showStyledMessage("Los campos marcados con * son obligatorios.", "warning");
            return;
        }

        // Crea un nuevo objeto Alerta y establece sus propiedades
        Alerta nuevaAlerta = new Alerta();
        nuevaAlerta.setTitulo(titulo);
        nuevaAlerta.setTipoAlerta(tipoAlerta);
        nuevaAlerta.setNivelAlerta(nivelAlerta);
        nuevaAlerta.setSectorAfectado(sectorAfectado);
        nuevaAlerta.setDescripcion(descripcion);
        nuevaAlerta.setInstrucciones(instrucciones);
        
        // Se guarda directamente la ruta absoluta del archivo en la base de datos
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            nuevaAlerta.setImagenUrl(rutaImagen);
        } else {
            nuevaAlerta.setImagenUrl(null);
        }
        
        // Llama al método DAO para crear la alerta en la base de datos
        boolean agregada = alertaDAO.crear(nuevaAlerta);
        if (agregada) {
            showStyledMessage("Alerta agregada exitosamente.", "success");
            // Cierra la ventana después de 2 segundos y refresca la ventana padre
            Timer timer = new Timer(2000, e -> {
                if (parentFrame != null) {
                    parentFrame.cargarAlertasEnTarjetas();
                    parentFrame.setVisible(true);
                }
                dispose();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showStyledMessage("Error al agregar la alerta. Verifique la información e intente nuevamente.", "error");
        }
    }
    
    /**
     * Abre un JFileChooser para que el usuario seleccione una imagen.
     * Almacena la ruta del archivo seleccionado.
     */
    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Imagen de la Alerta");
        int userSelection = fileChooser.showOpenDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            rutaImagen = archivoSeleccionado.getAbsolutePath(); // Guarda la ruta absoluta
            lblImagenUrl.setText("Imagen seleccionada: " + archivoSeleccionado.getName());
            lblImagenUrl.setForeground(new Color(102, 255, 102));
        }
    }

    /**
     * Cierra la ventana actual y muestra la ventana padre.
     */
    private void cancelar() {
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
        dispose();
    }

    /**
     * Muestra una ventana de diálogo (JDialog) personalizada con un estilo único.
     * @param message El mensaje a mostrar.
     * @param type El tipo de mensaje ("success", "error", "warning") para cambiar el estilo.
     */
    private void showStyledMessage(String message, String type) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(420, 180);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Pinta el fondo del diálogo con degradado y esquinas redondeadas
                GradientPaint baseBg = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, getHeight(), SURFACE_COLOR);
                g2d.setPaint(baseBg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Pinta una barra de color en la parte superior del diálogo según el tipo de mensaje
                Color barColor;
                if (type.equals("success")) {
                    barColor = new Color(34, 139, 34);
                } else if (type.equals("error")) {
                    barColor = PRIMARY_COLOR;
                } else {
                    barColor = new Color(255, 140, 0);
                }
                
                g2d.setColor(barColor);
                g2d.fillRoundRect(0, 0, getWidth(), 6, 20, 20);
                g2d.setColor(new Color(71, 85, 105));
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
    
    /**
     * Crea un botón para la ventana de diálogo con un estilo específico.
     * @param text El texto del botón.
     * @param isPrimary Booleano para decidir si el botón es primario o secundario.
     * @return JButton estilizado.
     */
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
                    bgColor = getModel().isPressed() ? new Color(71, 85, 105) : getModel().isRollover() ? new Color(51, 65, 85) : new Color(30, 41, 59);
                }
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                if (!isPrimary) {
                    g2d.setColor(new Color(71, 85, 105));
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
