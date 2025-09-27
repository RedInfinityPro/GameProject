
import java.awt.*;
import javax.swing.*;

public class Populate_ScrollPane extends JPanel {
    Populate_ScrollPane(String text) {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Main.BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        setBackground(Color.GREEN);
        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea(text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(textArea, BorderLayout.CENTER);
    }
}