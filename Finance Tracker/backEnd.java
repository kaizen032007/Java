import java.util.InputMismatchException;
import java.util.Scanner;

public class backEnd {

    private String name;
    private int age;

    // this method allows the print to be centered
    public static void printCentered(String text) {
        int terminalWidth = 65;
        int padding = (terminalWidth - text.length()) / 2;
        String centered = " ".repeat(padding) + text;
        System.out.println(centered);
    }

    public void loadingBar() {
        // Loading bar over 10 seconds
        System.out.print("Loading: [");
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000); // 1 second per step
            } catch (InterruptedException time) {
                Thread.currentThread().interrupt();
            }
            System.out.print("="); // Print one bar per second
        }
    }

    // this method is the backEnd options for the finance tracker main window
    public void financeTrackerMainWindowOptions(Scanner scanner, int options, FrontEnd frontEnd,
            InputMismatchException e, String name) {
        boolean validInput = false;

        this.name = name;
        this.age = age;

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
        while (!validInput) {
            try {
                System.out.print("Do you want to add money? (y/n): ");
                String option = scanner.nextLine();

                if (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n")) {
                    throw new InputMismatchException("Your input must be Y or N only");
                }

                if (option.equalsIgnoreCase("n")) {
                    System.out.println("Going back!");
                    this.financeTrackerMainWindowOptions(scanner, 0, frontEnd, e, name);
                    return;
                }

                validInput = true;

            } catch (InputMismatchException inputError) {
                System.out.println("Error! " + inputError.getMessage());
            }
        }

        System.out.println("Please wait....");
        loadingBar();
        System.out.println("] Done!");
        System.out.println("Going in!");

        System.out.println("=".repeat(70));
        printCentered("Welcome to your Deposit Dashboard! " + name);
        printCentered("Here you can add your money to an existing balance");
        System.out.println("=".repeat(70));

        System.out.print("Do you need help? (y/n): ");
        String help = scanner.nextLine();

        validInput = false;
        while (!validInput) {
            try {
                if (!help.equalsIgnoreCase("y") && !help.equalsIgnoreCase("n")) {
                    throw new InputMismatchException("Your option must be Y or N only");
                }

                if (help.equalsIgnoreCase("y")) {
                    validInput = true;
                    System.out.println("Great let me help you!");
                    System.out.println(
                            "This section is where you can deposit or add your money to an existing balance and to do so follow these steps.");
                    System.out.println("1. Enter the amount of money you wish to add");
                }
            } catch (InputMismatchException inputError) {
                System.out.println("Error! " + inputError.getMessage());
            }
        }
    }
}
