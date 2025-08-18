package alertasegurard.views;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DarkThemeScrollBarUI extends BasicScrollBarUI {
    private final Color PRIMARY_COLOR = new Color(220, 38, 38);
    private final Color SURFACE_COLOR = new Color(30, 41, 59);

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        Dimension zeroDim = new Dimension(0, 0);
        button.setPreferredSize(zeroDim);
        button.setMinimumSize(zeroDim);
        button.setMaximumSize(zeroDim);
        return button;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(SURFACE_COLOR);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ajustar el color del "pulgar"
        Color thumbColor = PRIMARY_COLOR;
        if (isThumbRollover()) {
            thumbColor = thumbColor.brighter();
        }

        g2.setPaint(thumbColor);
        int arc = 8;
        g2.fill(new RoundRectangle2D.Double(
                thumbBounds.x,
                thumbBounds.y,
                thumbBounds.width,
                thumbBounds.height,
                arc,
                arc
        ));
    }
}