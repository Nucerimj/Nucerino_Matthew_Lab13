import java.util.Scanner;

public class SafeInput {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getRegExString(String prompt, String regexPattern) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        } while (!input.matches(regexPattern));
        return input;
    }

    public static int getRangedInt(String prompt, int low, int high) {
        int input;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                scanner.next();
                System.out.println("Invalid input. Please enter an integer.");
                System.out.print(prompt);
            }
            input = scanner.nextInt();
        } while (input < low || input > high);
        scanner.nextLine(); // Consume the newline character left in the buffer
        return input;
    }

    public static boolean getYesNoInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim().toLowerCase();
        } while (!input.equals("y") && !input.equals("n"));
        return input.equals("y");
    }

    public static void closeScanner() {
        scanner.close();
    }
}