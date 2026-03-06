import java.util.InputMismatchException;
import java.util.Scanner;

public class backEnd {

    // this method allows the print to be centered
    public static void printCentered(String text) {
        int terminalWidth = 65;
        int padding = (terminalWidth - text.length()) / 2;
        String centered = " ".repeat(padding) + text;
        System.out.println(centered);
    }

    // this method is the backEnd options for the finance tracker main window
    public void financeTrackerMainWindowOptions(Scanner scanner, int options, FrontEnd frontEnd,
            InputMismatchException e) {
        boolean validInput = false;

        while (!validInput) {
            try {
                if (options == 1) {
                    this.addMoney(scanner, frontEnd, e);
                    break;
                }

                if (options == 2) {
                    this.addMoney(scanner, frontEnd, e);
                }

                if (options == 3) {
                    this.addMoney(scanner, frontEnd, e);
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error! " + ex.getMessage());
            }
        }
    }

    public void addMoney(Scanner scanner, FrontEnd frontEnd, InputMismatchException e) {
        boolean validInput = false;
        // this method is still in development mode still in ERROR
        while (!validInput) {
            System.out.print("Do you want to add Money? (y/n): ");
            String option = scanner.nextLine(); // Ask INSIDE the loop

            if (option.equalsIgnoreCase("y")) {
                validInput = true; // This stops the loop
                System.out.println("Entering the system...");
                // ... (Your printCentered code here)

                try {
                    System.out.print("Please input your desired deposit money: ");
                    int deposit = scanner.nextInt();
                    scanner.nextLine(); // Clear the newline
                    System.out.println("Balance updated!");

                } catch (InputMismatchException ex) {
                    System.out.println("Invalid amount. Please enter numbers only.");
                    scanner.nextLine(); // Important: Clear the "bad" input from scanner
                }

            } else if (option.equalsIgnoreCase("n")) {
                validInput = true;
                System.out.println("Going back...");
                frontEnd.financeTrackerMainWindow(option, 0, scanner, null, e);

            } else {
                // No exception needed here, just a simple message
                System.out.println("Invalid choice! Please type 'y' or 'n'.");
            }
        }
    }
}
