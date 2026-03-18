import java.util.InputMismatchException;
import java.util.Scanner;

public class backEnd {

    private String name;
    private int age;
    private double balance = 0.0;

    // This method allows the print to be centered
    public static void printCentered(String text) {
        int terminalWidth = 65;
        int padding = (terminalWidth - text.length()) / 2;
        String centered = " ".repeat(padding) + text;
        System.out.println(centered);
    }

    public void loadingBar() {
        System.out.print("Loading: [");
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException time) {
                Thread.currentThread().interrupt();
            }
            System.out.print("=");
        }
    }

    // This method handles the main window options for the finance tracker
    public void financeTrackerMainWindowOptions(Scanner scanner, int options, FrontEnd frontEnd,
            InputMismatchException e, String name) {

        this.name = name;

        if (options == 1) {
            this.addMoney(scanner, frontEnd);
        } else if (options == 2) {
            this.withdrawMoney(scanner);
        } else if (options == 3) {
            this.addMoney(scanner, frontEnd);
        }
    }

    public void addMoney(Scanner scanner, FrontEnd frontEnd) {

        // --- Step 1: Ask if user wants to add money ---
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Do you want to add money? (y/n): ");
                String option = scanner.nextLine().trim();

                if (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n")) {
                    throw new InputMismatchException("Your input must be Y or N only");
                }

                if (option.equalsIgnoreCase("n")) {
                    System.out.println("Going back!");
                    return;
                }

                validInput = true;

            } catch (InputMismatchException inputError) {
                System.out.println("Error! " + inputError.getMessage());
            }
        }

        // --- Step 2: Show deposit dashboard ---
        System.out.println("Please wait....");
        // loadingBar();
        System.out.println("Done!");
        System.out.println("Going in!");

        System.out.println("=".repeat(70));
        printCentered("Welcome to your Deposit Dashboard! " + name);
        printCentered("Here you can add your money to an existing balance");
        System.out.println("=".repeat(70));

        // --- Step 3: Help prompt ---
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Do you need help? (y/n): ");
                String help = scanner.nextLine().trim();

                if (!help.equalsIgnoreCase("y") && !help.equalsIgnoreCase("n")) {
                    throw new InputMismatchException("Your option must be Y or N only");
                }

                if (help.equalsIgnoreCase("y")) {
                    validInput = true;
                    System.out.println("Great, let me help you!");
                    System.out
                            .println("This section is where you can deposit or add your money to an existing balance.");
                    System.out.println("1. Enter the amount of money you wish to add.");
                    System.out.println();
                }

                if (help.equalsIgnoreCase("n")) {
                    validInput = true;
                    System.out.println("Good! Wait to proceed.");
                }

            } catch (InputMismatchException inputError) {
                System.out.println("Error! " + inputError.getMessage());
            }
        }

        // --- Step 4: Enter deposit amount ---
        validInput = false;
        while (!validInput) {
            try {
                System.out.println();
                System.out.println("=".repeat(70));
                System.out.println("Current Balance: " + balance);
                System.out.print("Enter the money you wish to add: ");

                double addMoney = scanner.nextDouble();
                scanner.nextLine();

                if (addMoney <= 0) {
                    throw new InputMismatchException("You must enter more than 0");
                }

                balance += addMoney;
                System.out.println("Success!");
                System.out.println("New Balance: " + balance);
                validInput = true;

            } catch (InputMismatchException moneyInputError) {
                System.out.println("Error! " + moneyInputError.getMessage());
                scanner.nextLine();
            }
        }

        // --- Step 5: Another transaction? ---
        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Do you want to create another transaction? (y/n): ");
                String option = scanner.nextLine().trim();

                if (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n")) {
                    throw new InputMismatchException("Your input must be Y or N only");
                }

                if (option.equalsIgnoreCase("y")) {
                    validInput = true;
                    System.out.println("Okay, going back!");
                    this.addMoney(scanner, frontEnd);
                }

                if (option.equalsIgnoreCase("n")) {
                    validInput = true;
                    System.out.println("Great! Returning to main menu.");
                    return;
                }

            } catch (InputMismatchException errorInput) {
                System.out.println("Error! " + errorInput.getMessage());
            }
        }
    }

    public void withdrawMoney(Scanner scanner) {

        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Please enter your desire withdrawal amount: ");
                int withdraw = scanner.nextInt();

                if (withdraw < 0) {
                    System.out.println("Cannot withdraw less than 0 or negative. Please Try again");
                }

                if (withdraw <= balance) {
                    System.out.println("You are withdrawing more than the balance you have. Please Try Again");
                }

                balance -= withdraw;
                System.out.println("Withdraw Success");
                System.out.println("New Balance: " + balance);
                validInput = true;
            } catch (InputMismatchException errorInput) {
                System.out.println("Error!" + errorInput.getMessage());
            }
        }

        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Do you want to create another transaction? (y/n): ");
                String option = scanner.nextLine();

                if (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n")) {
                    throw new InputMismatchException("You must put y or n only");
                }

                if (option.equals("y")) {
                    validInput = true;
                    System.out.println("Okay going back!");
                    this.withdrawMoney(scanner);
                }

                if (option.equals("n")) {
                    validInput = true;
                    System.out.println("Great! Returning to Main Menu");
                    return;
                }
            } catch (InputMismatchException errorInput) {
                System.out.println("Erorr!" + errorInput.getMessage());
            }
        }

    }
}
