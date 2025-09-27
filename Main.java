import java.awt.*;
import javax.swing.*;

public class Main extends JFrame{
    private Container cp;
    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 540;
    /// style
    public static final Font BASIC_FONT = new Font("Arial", Font.PLAIN, 11);
    public static final Color PANEL_COLOR = new Color(20, 20, 20);
    public static final Color BORDER_COLOR = Color.GRAY;

    Main() {
        setTitle("App");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cp = getContentPane();
        cp.setLayout(new BorderLayout(10, 10));
        cp.setBackground(Color.WHITE);
        ((JPanel) cp).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // items
        Center_Display center_display = new Center_Display(this, cp);
        center_display.Initialize_Items();
        GetResponse getResponse = new GetResponse(center_display);
        Bottom_Display bottom_display = new Bottom_Display(this, cp, center_display, getResponse);
        bottom_display.Initialize_Items();
        // Visible
        setVisible(true);
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
