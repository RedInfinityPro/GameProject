
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class Center_Display {
    public JFrame parentFrame;
    public Container cp;
    static List<String> sourceList = new ArrayList<>();
    static List<String> bubble_text = new ArrayList<>();
    public JPanel centerDisplay_panel;

    Center_Display(JFrame parentFrame, Container container) {
        this.parentFrame = parentFrame;
        this.cp = container;
    }

    public void Initialize_Items() {
        centerDisplay_panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Create subtle grid pattern
                g2.setColor(new Color(70, 70, 75, 30));
                for (int i = 0; i < getWidth(); i += 51) {
                    g2.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 51) {
                    g2.drawLine(0, i, getWidth(), i);
                }
                g2.dispose();
            }
        };
        centerDisplay_panel.setLayout(new BoxLayout(centerDisplay_panel, BoxLayout.Y_AXIS));
        centerDisplay_panel.setBackground(Main.PANEL_COLOR);
        JScrollPane scrollPane = new JScrollPane(centerDisplay_panel) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(Main.ORANGE_COLOR.getRed(), Main.ORANGE_COLOR.getGreen(), Main.ORANGE_COLOR.getBlue(), 20),
                        getWidth(), 0, new Color(Main.BLUE_COLOR.getRed(), Main.BLUE_COLOR.getGreen(), Main.BLUE_COLOR.getBlue(), 20)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        scrollPane.setBorder(new Populate_ScrollPane(null).new GlowBorder(Color.GRAY, 2));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUI(new PortalScrollBarUI());
        cp.add(scrollPane, BorderLayout.CENTER);
    }

    public class RowPanel_Panel extends JPanel {
        private List<String> sourceList;
        private List<String> bubble_text;
        private List<String> targets_left = Arrays.asList("l", "L", "Left");
        private List<String> targets_right = Arrays.asList("r", "R", "Right");

        RowPanel_Panel(int amount, List<String> sourceList, List<String> bubble_text) {
            this.sourceList = sourceList;
            this.bubble_text = bubble_text;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
        }

        public JPanel apply_RawPanel() {
            for (int i = 0; i < sourceList.size(); i++) {
                String direction = sourceList.get(i);
                String text = (i < bubble_text.size()) ? bubble_text.get(i) : "";
                Color bubbleColor;
                Color glowColor;
                if (targets_left.contains(direction)) {
                    bubbleColor = new Color(45, 45, 50, 200); // dark
                    glowColor = Main.BLUE_COLOR;
                } else if (targets_right.contains(direction)) {
                    bubbleColor = new Color(40, 40, 45, 200); // light
                    glowColor = Main.ORANGE_COLOR;
                } else {
                    bubbleColor = Color.WHITE; // fallback
                    glowColor = Color.GRAY;
                }
                JPanel bubbleWrapper = new JPanel(new FlowLayout(targets_left.contains(direction) ? FlowLayout.LEFT : FlowLayout.RIGHT));
                bubbleWrapper.setOpaque(false);
                bubbleWrapper.add(createBubble(text, bubbleColor, glowColor));
                bubbleWrapper.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                add(bubbleWrapper);
            }
            return this;
        }

        private JComponent createBubble(String text, Color color, Color glowColor) {
            JLabel label = new JLabel("<html><body style='width: 220px; color: #FFFFFF'>" + text + "</body></html>");
            label.setFont(Main.STYLE_FONT);
            label.setForeground(Color.WHITE);
            JPanel bubble = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Create gradient background
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(color.getRed(), color.getGreen(), color.getBlue()),
                            0, getHeight(), new Color(color.getRed() - 10, color.getGreen() - 10, color.getBlue() - 10)
                    );
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    // Add glow effect
                    g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 200));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            bubble.add(label, BorderLayout.CENTER);
            bubble.setOpaque(false);
            bubble.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
            bubble.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
            return bubble;
        }
    }

    // Custom ScrollBar UI for Portal theme
    class PortalScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(Main.GRAY_COLOR.getRed(), Main.GRAY_COLOR.getGreen(), Main.GRAY_COLOR.getBlue(), 150);
            this.trackColor = new Color(20, 20, 25);
        }

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
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Create gradient for thumb
            GradientPaint gradient = new GradientPaint(
                thumbBounds.x, thumbBounds.y, Main.GRAY_COLOR,
                thumbBounds.x + thumbBounds.width, thumbBounds.y, Main.GRAY_COLOR
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4, thumbBounds.height, 8, 8);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }
}
