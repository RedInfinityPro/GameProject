
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

    Bottom_Display(JFrame parentFrame, Container container, Center_Display centerDisplay, GetResponse getResponse) {
        this.parentFrame = parentFrame;
        this.cp = container;
        this.centerDisplay = centerDisplay;
        this.getResponse = getResponse;
    }

    public void Initialize_Items() {
        JPanel textArea_panel = new JPanel();
        textArea_panel.setBackground(Main.PANEL_COLOR);
        textArea_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Main.BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Main.BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textArea.setFont(Main.BASIC_FONT);
        textArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(320, 100));
        scrollPane.setBorder(null);
        // button
        SendButton send_button = new SendButton(">", textArea);
        textArea_panel.add(scrollPane, BorderLayout.CENTER); // west
        textArea_panel.add(send_button, BorderLayout.EAST);
        cp.add(textArea_panel, BorderLayout.SOUTH);
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
