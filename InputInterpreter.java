

import java.util.*;

public class InputInterpreter {

    // Dictionaries
    private static final Set<String> actionKeywords = new HashSet<>(Arrays.asList(
            "walk", "climb", "look", "eat", "search", "run"
    ));

    private static final Set<String> taskKeywords = new HashSet<>(Arrays.asList(
            "clean", "move", "sleep", "return"
    ));

    private static final Set<String> commandKeywords = new HashSet<>(Arrays.asList(
            "menu", "inventory", "time", "help"
    ));

    // Random generator for playful claim responses
    private static final Random rand = new Random();

    // Process user input
    public static String interpret(String input) {
        String normalized = input.trim().toLowerCase();

        // 1. Check for commands (e.g. [Menu], [Inventory])
        if (normalized.startsWith("[") && normalized.endsWith("]")) {
            String cmd = normalized.substring(1, normalized.length() - 1).trim();
            return handleCommand(cmd);
        }

        // 2. Check for actions
        for (String keyword : actionKeywords) {
            if (normalized.contains(keyword) || normalized.contains(keyword + "ing")) {
                return handleAction(normalized, keyword);
            }
        }

        // 3. Check for tasks
        for (String keyword : taskKeywords) {
            if (normalized.contains(keyword) || normalized.contains(keyword + "ed") || normalized.contains(keyword + "ing")) {
                return handleTask(normalized, keyword);
            }
        }

        // 4. Check for claims (detect "I found", "I won", etc.)
        if (normalized.startsWith("i found") || normalized.startsWith("i won") || normalized.startsWith("i have")) {
            return handleClaim(normalized);
        }

        // Default response
        return "I'm not sure what you mean. Can you try again?";
    }

    // ------------------ Handlers -------------------

    private static String handleAction(String input, String keyword) {
        switch (keyword) {
            case "walk": return "You walk around, the ground crunching softly beneath your feet.";
            case "climb": return "You climb upward with effort, the view expanding as you go.";
            case "look": return "You look carefully and spot some unusual markings nearby.";
            case "eat": return "You eat... wait, are you sure that was food?";
            default: return "You " + keyword + " with determination.";
        }
    }

    private static String handleTask(String input, String keyword) {
        if (keyword.equals("clean")) {
            return "The area looks much tidier after your cleaning.";
        } else if (keyword.equals("move")) {
            return "You moved successfully, progress has been made.";
        } else if (keyword.equals("sleep")) {
            return "You feel rested after your sleep.";
        } else if (keyword.equals("return")) {
            return "You return to where you started.";
        }
        return "Task complete: " + keyword;
    }

    private static String handleCommand(String cmd) {
        if (commandKeywords.contains(cmd)) {
            switch (cmd) {
                case "menu": return "Welcome to the main menu, would you like to play?";
                case "inventory": return "You open your inventory, but it looks rather empty.";
                case "time": return "Time passes strangely here... you sense it’s not important.";
                case "help": return "Available commands: [Menu], [Inventory], [Time], [Help]";
            }
        }
        return "Your command \"" + cmd + "\" has failed.";
    }

    private static String handleClaim(String input) {
        String[] negativeConsequences = {
                "but it turns out to be a dangerous trap!",
                "but sadly, it was an illusion.",
                "but it crumbles to dust in your hands.",
                "but it reeks of rot and filth."
        };

        String consequence = negativeConsequences[rand.nextInt(negativeConsequences.length)];
        return "You claim \"" + input + "\"... " + consequence;
    }

    // ------------------ Main for testing -------------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your action:");
        while (true) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("quit")) break;
            System.out.println(interpret(input));
        }
        sc.close();
    }
}
