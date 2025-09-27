
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;

public class Bottom_Display {
    public JFrame parentFrame;
    public Container cp;
    private Center_Display centerDisplay;
    private GetResponse getResponse;
    private JTextArea textArea;

    Bottom_Display(JFrame parentFrame, Container container, Center_Display centerDisplay, GetResponse getResponse) {
        this.parentFrame = parentFrame;
        this.cp = container;
        this.centerDisplay = centerDisplay;
        this.getResponse = getResponse;
    }

    public void Initialize_Items() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 30),
                    0, getHeight(), new Color(35, 35, 40)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Add top border glow
                g2.setColor(Main.ORANGE_COLOR);
                g2.drawLine(0, 0, getWidth(), 0);
                g2.setColor(new Color(Main.ORANGE_COLOR.getRed(), Main.ORANGE_COLOR.getGreen(), Main.ORANGE_COLOR.getBlue(), 100));
                g2.drawLine(0, 1, getWidth(), 1);
                g2.dispose();
            }
        };
        bottomPanel.setPreferredSize(new Dimension(0, 60));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        bottomPanel.setOpaque(false);

        textArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background with gradient
                GradientPaint gradient = new GradientPaint(
                     0, 0, new Color(20, 20, 25),
                     0, getHeight(), new Color(30, 30, 35)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // Glow border
                g2.setColor(isFocusOwner() ? Main.ORANGE_COLOR : Main.BLUE_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        textArea.setFont(Main.STYLE_FONT);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(320, 100));
        scrollPane.setBorder(null);
        // button
        SendButton send_button = new SendButton(">", textArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER); // west
        bottomPanel.add(send_button, BorderLayout.EAST);
        cp.add(bottomPanel, BorderLayout.SOUTH);
    }

    public class SendButton extends JButton implements ActionListener {
        private JTextArea textArea;
        public String output;

        SendButton(String text, JTextArea textArea) {
            super(text);
            setFont(Main.BASIC_FONT);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.BLACK);
            setBackground(Color.GREEN);
            addActionListener(this);
            this.textArea = textArea;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                if (!(this.textArea.getText().equals(""))) {
                    set_OutputString(this.textArea.getText());
                    // keep track in static lists
                    Center_Display.sourceList.add("r");
                    Center_Display.bubble_text.add(this.output.toString());
                    // create one new row bubble and add it to the panel
                    Center_Display.RowPanel_Panel rowPanel = centerDisplay.new RowPanel_Panel(1, Arrays.asList("r"), Arrays.asList(this.output));
                    centerDisplay.centerDisplay_panel.add(rowPanel.apply_RawPanel());
                    // refresh UI
                    centerDisplay.centerDisplay_panel.revalidate();
                    centerDisplay.centerDisplay_panel.repaint();
                    getResponse.GenerateResponse(this.textArea.getText());
                    getResponse.DisplayMenu();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            this.textArea.setText("");
        }

        public void set_OutputString(String new_output) {
            this.output = new_output;
        }

        public String get_OutputString() {
            return this.output;
        }
    }
}
