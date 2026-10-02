package ui;


import java.util.Scanner;

public class InputHelper {
    private final Scanner scanner = new Scanner(System.in);

    public String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("'" + input + "' is not a number. Try again.");
            }
        }
    }
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int input = readInt(prompt);
            if (input >= min && input <= max) {
                return input;
            }
            System.out.println("Input must be between " + min + " and " + max + ". Try again.");
        }
    }
}

