
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
        this.centerDisplay_panel = new JPanel();
        this.centerDisplay_panel.setLayout(new BoxLayout(centerDisplay_panel, BoxLayout.Y_AXIS));
        this.centerDisplay_panel.setBackground(Main.PANEL_COLOR);
        this.centerDisplay_panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        // apply items
        // List<String> sourceList = Arrays.asList("l", "L", "Right");
        // List<String> bubble_text = Arrays.asList("Hello", "Test", "Might");
        // RowPanel_Panel rowPanel_panel = new RowPanel_Panel(5, sourceList, bubble_text);
        // centerDisplay_panel.add(rowPanel_panel.apply_RawPanel());
        // implement scrollpane
        JScrollPane scrollPane = new JScrollPane(this.centerDisplay_panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        scrollPane.setBorder(null);
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
                if (targets_left.contains(direction)) {
                    bubbleColor = new Color(0, 128, 0); // dark green
                } else if (targets_right.contains(direction)) {
                    bubbleColor = new Color(143, 188, 143); // light green
                } else {
                    bubbleColor = Color.GRAY; // fallback
                }
                JPanel bubbleWrapper = new JPanel(new FlowLayout(targets_left.contains(direction) ? FlowLayout.LEFT : FlowLayout.RIGHT));
                bubbleWrapper.setOpaque(false);
                bubbleWrapper.add(createBubble(text, bubbleColor));
                bubbleWrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                add(bubbleWrapper);
            }
            return this;
        }

        private JComponent createBubble(String text, Color color) {
            JLabel label = new JLabel("<html><body style='width: 200px;'>" + text + "</body></html>");
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            label.setForeground(Color.BLACK);
            JPanel bubble = new JPanel(new BorderLayout());
            bubble.add(label, BorderLayout.CENTER);
            bubble.setBackground(color);
            bubble.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            bubble.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
            // Rounded corners
            bubble = new RoundedPanel(bubble);
            return bubble;
        }
    }

    class RoundedPanel extends JPanel {

        private final JComponent inner;

        public RoundedPanel(JComponent inner) {
            super(new BorderLayout());
            this.inner = inner;
            setOpaque(false);
            add(inner, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(inner.getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
