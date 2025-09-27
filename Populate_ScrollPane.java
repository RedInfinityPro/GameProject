import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class Populate_ScrollPane extends JPanel {
    Populate_ScrollPane(String text) {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Main.ORANGE_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        setBackground(new Color(30, 30, 35, 200));
        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea(text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(Main.STYLE_FONT);
        add(textArea, BorderLayout.CENTER);
    }

    class RoundedPanel extends JPanel {
        private final JComponent inner;
        private final Color glowColor;

        public RoundedPanel(JComponent inner) {
            this(inner, Main.ORANGE_COLOR);
        }

        public RoundedPanel(JComponent inner, Color glowColor) {
            super(new BorderLayout());
            this.inner = inner;
            this.glowColor = glowColor;
            setOpaque(false);
            add(inner, BorderLayout.CENTER);
        }

        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Create gradient background
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(45, 45, 50, 220),
                0, getHeight(), new Color(25, 25, 30, 200)
            );
            g2.setPaint(gradient);
            // Draw rounded rectangle with glow effect
            for (int i = 3; i >= 0; i--) {
                float alpha = (4 - i) * 0.1f;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(glowColor);
                g2.fillRoundRect(-i, -i, getWidth() + 2*i, getHeight() + 2*i, 20 + i*2, 20 + i*2);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            // Add subtle inner highlight
            g2.setColor(new Color(255, 255, 255, 30));
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class GlowBorder extends AbstractBorder {
        private final Color glowColor;
        private final int thickness;

        public GlowBorder(Color glowColor, int thickness) {
            this.glowColor = glowColor;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int i = 0; i < thickness; i++) {
                float alpha = (thickness - i) * 0.3f / thickness;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(glowColor);
                g2.drawRoundRect(x + i, y + i, width - 2 * i - 1, height - 2 * i - 1, 15, 15);
            }
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }
}
