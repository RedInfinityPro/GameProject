import java.util.*;
import javax.swing.*;

public class GetResponse {
    private Center_Display center_display;
    private HashMap<List<String>, ResponseData> dictionary = new HashMap<>();
    private HashMap<String, ConversationState> states = new HashMap<>();
    private HashSet<String> usedOneTimeCommands = new HashSet<>();
    private String bestResponse;
    private String currentState = "default";

    // Response data container
    private class ResponseData {
        String response;
        boolean oneTimeOnly;
        String nextState;
        boolean requiresInput;
        
        ResponseData(String response) {
            this(response, false, null, false);
        }
        
        ResponseData(String response, boolean oneTimeOnly) {
            this(response, oneTimeOnly, null, false);
        }
        
        ResponseData(String response, boolean oneTimeOnly, String nextState) {
            this(response, oneTimeOnly, nextState, false);
        }
        
        ResponseData(String response, boolean oneTimeOnly, String nextState, boolean requiresInput) {
            this.response = response;
            this.oneTimeOnly = oneTimeOnly;
            this.nextState = nextState;
            this.requiresInput = requiresInput;
        }
    }
    
    // Conversation state container
    private class ConversationState {
        String prompt;
        String expectedInput;
        String successResponse;
        String failureResponse;
        String nextState;
        
        ConversationState(String prompt, String expectedInput, String successResponse, String failureResponse, String nextState) {
            this.prompt = prompt;
            this.expectedInput = expectedInput;
            this.successResponse = successResponse;
            this.failureResponse = failureResponse;
            this.nextState = nextState;
        }
    }
    
    GetResponse(Center_Display center_display) {
        this.bestResponse = null;
        this.center_display = center_display;
        buildDictionary();
        setupConversationStates();
    }
    
    private void buildDictionary() {
        // Basic greetings and responses
        addResponse(Arrays.asList("help", "commands", "guide"), "Available Commands:");
        addResponse(Arrays.asList("menu"), new ResponseData("Main Menu:", false, "menu_input", true));
    }
    
    private void setupConversationStates() {
        // Question flow
        states.put("menu_input", new ConversationState(
            "Would you like to play? (yes/no)",
            null,
            "Welcome!",
            "Invalid name format. Please try again.",
            "default"
        ));
    }
    
    private void addResponse(List<String> triggers, String response) {
        dictionary.put(triggers, new ResponseData(response));
    }
    
    private void addResponse(List<String> triggers, ResponseData responseData) {
        dictionary.put(triggers, responseData);
    }
    
    public String GenerateResponse(String userInput) {
        String lowerInput = userInput.toLowerCase().trim();
        // Handle conversation states
        if (!currentState.equals("default")) {
            return handleConversationState(lowerInput, userInput);
        }
        // Process normal commands
        List<String> inputWords = Arrays.asList(lowerInput.split("\\s+"));
        double bestScore = 0.0;
        ResponseData bestMatch = null;
        List<String> bestKey = null;
        for (Map.Entry<List<String>, ResponseData> entry : dictionary.entrySet()) {
            List<String> keyList = entry.getKey();
            ResponseData responseData = entry.getValue();
            // Skip one-time commands that have already been used
            if (responseData.oneTimeOnly && usedOneTimeCommands.contains(keyList.toString())) {
                continue;
            }
            int matchCount = 0;
            for (String word : inputWords) {
                if (keyList.contains(word)) {
                    matchCount++;
                }
            }
            // Calculate match percentage based on how many trigger words were found
            double matchPercentage = (double) matchCount / keyList.size();
            // Boost score if more words match
            if (matchCount > 1) {
                matchPercentage *= 1.5;
            }
            if (matchPercentage > bestScore && matchPercentage > 0.5) {
                bestScore = matchPercentage;
                bestMatch = responseData;
                bestKey = keyList;
            }
        }
        if (bestMatch != null) {
            // Mark one-time command as used
            if (bestMatch.oneTimeOnly) {
                usedOneTimeCommands.add(bestKey.toString());
            }
            // Change state if specified
            if (bestMatch.nextState != null) {
                currentState = bestMatch.nextState;
                if (states.containsKey(currentState)) {
                    this.bestResponse = bestMatch.response + "\n" + states.get(currentState).prompt;
                } else {
                    this.bestResponse = bestMatch.response;
                }
            }
        } else {
            // Generate contextual failure responses
            this.bestResponse = generateContextualFailure(userInput);
        }
        return this.bestResponse;
    }
    
    private String handleConversationState(String lowerInput, String originalInput) {
        ConversationState state = states.get(currentState);
        if (state == null) {
            currentState = "default";
            return "System error. Returning to normal operation.";
        }
        boolean validInput = false;
        String response = state.failureResponse;
        switch (currentState) {
            case "menu_input":
                if (lowerInput.equals("yes")) {
                    validInput = true;
                    response = state.successResponse;
                } else if (lowerInput.equals("no")) {
                    validInput = true;
                    response = "Returning to main interface.";
                }
                break;
        }
        if (validInput && state.nextState != null) {
            currentState = state.nextState;
            if (states.containsKey(currentState)) {
                response += "\n" + states.get(currentState).prompt;
            }
        }
        return response;
    }
    
    private String generateContextualFailure(String input) {
        String[] portalFailures = {
            "I'm sorry, I don't understand '" + input + "'. Please speak clearly for better test results.",
            "That command is not recognized by the database.",
            "Error: Command '" + input + "' not found. Try 'help' for available commands.",
            "Cannot process that request. Perhaps you meant something else?",
            "That's not a valid protocol. Please try again."
        };
        Random random = new Random();
        return portalFailures[random.nextInt(portalFailures.length)];
    }
    
    public void DisplayMenu() {
        // keep track in static lists
        Center_Display.sourceList.add("l");
        Center_Display.bubble_text.add(this.bestResponse);
        // create one new row bubble and add it to the panel
        Center_Display.RowPanel_Panel rowPanel = center_display.new RowPanel_Panel(
            Arrays.asList("l"), Arrays.asList(this.bestResponse));
        center_display.centerDisplay_panel.add(rowPanel.apply_RawPanel());
        // refresh UI
        center_display.centerDisplay_panel.revalidate();
        center_display.centerDisplay_panel.repaint();
        // Auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) center_display.centerDisplay_panel.getParent().getParent();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }
    
    // Utility methods for debugging and admin
    public void resetOneTimeCommands() {
        usedOneTimeCommands.clear();
        currentState = "default";
    }
    
    public String getCurrentState() {
        return currentState;
    }
    
    public Set<String> getUsedCommands() {
        return new HashSet<>(usedOneTimeCommands);
    }
}