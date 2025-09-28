
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import javax.swing.*;

public class Main extends JFrame {
    public Random random = new Random();
    private Container cp;
    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 540;
    private GetResponse getResponse;
    private Center_Display center_display;
    /// style
    public static final Font BASIC_FONT = new Font("Consolas", Font.PLAIN, 11);
    public static final Font STYLE_FONT = new Font("Consolas", Font.PLAIN, 14);
    public static final Font TITLE_FONT = new Font("Consolas", Font.BOLD, 16);
    public static final Color PANEL_COLOR = new Color(15, 15, 20);
    public static final Color BORDER_COLOR = new Color(70, 70, 75);
    public static final Color ORANGE_COLOR = new Color(255, 165, 0);
    public static final Color GRAY_COLOR = new Color(80, 80, 80);
    public static final Color BLUE_COLOR = new Color(64, 160, 255);

    Main() {
        setTitle("App");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Custom content pane with background
        AnimatedCircuitPanel cp = new AnimatedCircuitPanel();
        setContentPane(cp);
        setContentPane((JPanel) cp);
        ((JPanel) cp).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        // Initialize the response system with enhanced features
        initialize_chat(center_display);
        // items
        Center_Display center_display = new Center_Display(this, cp);
        center_display.Initialize_Items();
        Bottom_Display bottom_display = new Bottom_Display(this, cp, center_display, getResponse);
        bottom_display.Initialize_Items();
        // Initialize the response system with enhanced features
        displayBotResponse("menu", center_display);
        // Visible
        setVisible(true);
    }

    public void initialize_chat(Center_Display center_display) {
        this.getResponse = new GetResponse(center_display);
    }

    public void refreshDisplay(Center_Display center_display) {
        center_display.centerDisplay_panel.revalidate();
        center_display.centerDisplay_panel.repaint();
    }

    private void displayBotResponse(String userInput, Center_Display center_display) {
        String botReply = getResponse.GenerateResponse(userInput);
        center_display.sourceList.add("l");
        center_display.bubble_text.add(botReply);
        Center_Display.RowPanel_Panel rowPanel = center_display.new RowPanel_Panel(Arrays.asList("l"), Arrays.asList(botReply));
        center_display.centerDisplay_panel.add(rowPanel.apply_RawPanel());
        center_display.centerDisplay_panel.revalidate();
        center_display.centerDisplay_panel.repaint();
    }

    public class AnimatedCircuitPanel extends JPanel {
        private int offset = 0;
        private int pulsesSpeed;
        private static final int GRID_SIZE = 100;
        private static final int CIRCUIT_SIZE = 25;

        public AnimatedCircuitPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            // Timer for smooth animation loop
            Timer timer = new Timer(50, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    offset = (offset + 1) % GRID_SIZE; // Smooth movement over grid size
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Radial gradient background
            Point2D center = new Point2D.Float(getWidth() / 2f, getHeight() / 2f);
            float radius = Math.max(getWidth(), getHeight()) / 2f;
            float[] dist = {0.0f, 0.8f, 1.0f};
            Color[] colors = {
                new Color(25, 25, 35),
                new Color(15, 15, 25),
                new Color(10, 10, 15)
            };
            RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            // Circuit pattern
            drawAnimatedCircuitPattern(g2);
            g2.dispose();
        }

        private void drawAnimatedCircuitPattern(Graphics2D g2) {
            g2.setColor(new Color(ORANGE_COLOR.getRed(), ORANGE_COLOR.getGreen(), ORANGE_COLOR.getBlue(), 25));
            g2.setStroke(new BasicStroke(1.5f));
            // Calculate starting positions to ensure smooth movement
            int startX = -GRID_SIZE + (offset % GRID_SIZE);
            int startY = -GRID_SIZE + (offset % GRID_SIZE);
            // Draw circuit pattern extending beyond visible bounds
            for (int i = startX; i <= getWidth() + GRID_SIZE; i += GRID_SIZE) {
                for (int j = startY; j <= getHeight() + GRID_SIZE; j += GRID_SIZE) {
                    drawCircuitNode(g2, i, j);
                }
            }
            // Add some animated "data flow" effects
            drawDataFlowEffects(g2, startX, startY);
        }

        private void drawCircuitNode(Graphics2D g2, int x, int y) {
            // Main circuit node (square)
            g2.drawRect(x, y, CIRCUIT_SIZE, CIRCUIT_SIZE);
            // Horizontal connection line
            if (x + CIRCUIT_SIZE < getWidth() + GRID_SIZE) {
                g2.drawLine(x + CIRCUIT_SIZE, y + CIRCUIT_SIZE / 2,
                        x + GRID_SIZE - 5, y + CIRCUIT_SIZE / 2);
            }
            // Vertical connection line
            if (y + CIRCUIT_SIZE < getHeight() + GRID_SIZE) {
                g2.drawLine(x + CIRCUIT_SIZE / 2, y + CIRCUIT_SIZE,
                        x + CIRCUIT_SIZE / 2, y + GRID_SIZE - 5);
            }
            // Small corner connectors for detail
            g2.fillRect(x + CIRCUIT_SIZE - 3, y + CIRCUIT_SIZE - 3, 6, 6);
        }

        private void drawDataFlowEffects(Graphics2D g2, int startX, int startY) {
            // Add animated "energy pulses" along the circuit lines
            g2.setColor(new Color(BLUE_COLOR.getRed(), BLUE_COLOR.getGreen(), BLUE_COLOR.getBlue(), 80));
            // Horizontal pulses
            for (int i = startX; i <= getWidth() + GRID_SIZE; i += GRID_SIZE) {
                for (int j = startY; j <= getHeight() + GRID_SIZE; j += GRID_SIZE) {
                    int max = 3;
                    int min = 1;
                    pulsesSpeed = (int) ((Math.random() * (max - min)) + min);
                    // Calculate pulse position along horizontal line
                    int pulsePos = (offset * pulsesSpeed) % (GRID_SIZE - CIRCUIT_SIZE);
                    int pulseX = i + CIRCUIT_SIZE + pulsePos;
                    int pulseY = j + CIRCUIT_SIZE / 2;
                    if (pulseX < getWidth() && pulseY < getHeight() && pulseX > 0 && pulseY > 0) {
                        g2.fillOval(pulseX - 2, pulseY - 2, 4, 4);
                    }
                    // Vertical pulses
                    int pulsePosV = (offset * 2) % (GRID_SIZE - CIRCUIT_SIZE);
                    int pulseXV = i + CIRCUIT_SIZE / 2;
                    int pulseYV = j + CIRCUIT_SIZE + pulsePosV;
                    if (pulseXV < getWidth() && pulseYV < getHeight() && pulseXV > 0 && pulseYV > 0) {
                        g2.fillOval(pulseXV - 2, pulseYV - 2, 4, 4);
                    }
                }
            }
            // Add some randomly positioned glowing nodes
            g2.setColor(new Color(ORANGE_COLOR.getRed(), ORANGE_COLOR.getGreen(), ORANGE_COLOR.getBlue(), 60));
            for (int i = startX; i <= getWidth() + GRID_SIZE; i += GRID_SIZE) {
                for (int j = startY; j <= getHeight() + GRID_SIZE; j += GRID_SIZE) {
                    // Create a subtle glow effect at intersections
                    if ((i + j + offset) % 300 < 50) { // Randomly light up nodes
                        g2.setStroke(new BasicStroke(3f));
                        g2.drawRect(i - 2, j - 2, CIRCUIT_SIZE + 4, CIRCUIT_SIZE + 4);
                        g2.setStroke(new BasicStroke(1.5f));
                    }
                }
            }
        }
    }

    // run
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(Main::new);
    }
}
