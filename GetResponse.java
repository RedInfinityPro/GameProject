import java.util.Arrays;
import java.util.HashMap;

public class GetResponse {
    private Center_Display centerDisplay;
    private HashMap<String, String> dictionary = new HashMap<String, String>();
    private String bestResponse;

    GetResponse(Center_Display centerDisplay) {
        this.bestResponse = null;
        this.centerDisplay = centerDisplay;
        BuildHashMap();
    }

    private void BuildHashMap() {
        dictionary.put("Welcome", "This is a new world");
        dictionary.put("Good by", "It has been nice to see you.");
    }

    public String GenerateResponse(String userInput) {
        String lowerInput = userInput.toLowerCase();
        for (String key : dictionary.keySet()) {
            if (lowerInput.contains(key.toLowerCase()) || key.toLowerCase().contains(lowerInput)) {
                this.bestResponse = dictionary.get(key);
                return this.bestResponse;
            }
        }

        this.bestResponse = String.format("I'm sorry, I don't understand '%s'. Please speak clearly for better results.", userInput);
        return this.bestResponse;
    }

    public void DisplayMenu() {
        // keep track in static lists
        Center_Display.sourceList.add("l");
        Center_Display.bubble_text.add(this.bestResponse);
        // create one new row bubble and add it to the panel
        Center_Display.RowPanel_Panel rowPanel = centerDisplay.new RowPanel_Panel(1, Arrays.asList("l"), Arrays.asList(this.bestResponse));
        centerDisplay.centerDisplay_panel.add(rowPanel.apply_RawPanel());
        // refresh UI
        centerDisplay.centerDisplay_panel.revalidate();
        centerDisplay.centerDisplay_panel.repaint();
    }
}
