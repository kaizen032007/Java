import java.util.InputMismatchException;
import java.util.Scanner;

public class FrontEnd {

    // THIS IS THE FRONT PAGE OF THE PROGRAM
    public void frontpage(backEnd backEnd, Scanner scanner, InputMismatchException inputError) {
        System.out.println("=".repeat(70));
        System.out.println("");
        backEnd.printCentered("Welcome to Finance Tracker!");
        System.out.println("");
        System.out.println("=".repeat(70));

        System.out.println("Welcome to Finance Tracker! Do you want to create an account?");
        System.out.println("Note: All credentials inputed will be protected under the R.A 10173");

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter Here (y/n): ");
                String createAcct = scanner.nextLine();
                System.out.println();

                if (!createAcct.equalsIgnoreCase("y") && !createAcct.equalsIgnoreCase("n")) {
                    throw new IllegalArgumentException("Your input must be Y and N only!");
                }

                if (createAcct.equalsIgnoreCase("n")) {
                    System.out.println("Thank you for visiting!");
                    return;
                }

                validInput = true;
                System.out.println("Let us proceed!\n");
                this.registerAccount(scanner, inputError, backEnd);

            } catch (IllegalArgumentException e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    // THIS IS THE REGISTER ACCOUNTPAGE (This here will ask the user to create an
    // account)
    public void registerAccount(Scanner scanner, InputMismatchException inputError, backEnd backEnd) {
        System.out.println("=".repeat(70));
        backEnd.printCentered("Welcome to your Dashboard");
        System.out.println("=".repeat(70));

        System.out.println("1. Create Account \n" +
                "2. Exit\n");

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter Here: ");
                String createAcct = scanner.nextLine();
                System.out.println();

                if (!createAcct.equals("1") && !createAcct.equals("2")) {
                    throw new IllegalArgumentException("It must be 1 and 2 options only");
                }

                if (createAcct.equals("2")) {
                    System.out.println("Going back to Main menu");
                    this.frontpage(null, scanner, inputError);
                    return;
                }

                validInput = true;
                System.out.println("Let us proceed!\n");
                this.createAccount(scanner, backEnd);

            } catch (IllegalArgumentException e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    // THIS IS THE CREATE ACCOUNT PAGE (This here will ask the user to create an
    // account)
    public void createAccount(Scanner scanner, backEnd backEnd) {
        String name = "";
        int age = 0;

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter your name: ");
                name = scanner.nextLine();

                if (!name.matches("[a-zA-Z ]+")) {
                    throw new IllegalArgumentException("You must put letters only in your name");
                }

                validInput = true;

            } catch (IllegalArgumentException e) {
                System.out.println("Error " + e.getMessage());
            }
        }

        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter Your Age: ");
                age = scanner.nextInt();
                scanner.nextLine(); // Clear the empty newline left over from nextInt()

                if (age < 5) {
                    throw new IllegalArgumentException("You must be at least 5 years old");
                } else if (age > 120) {
                    throw new IllegalArgumentException("You must be less than 120 years old");
                }

                validInput = true;

            } catch (IllegalArgumentException e) {
                System.out.println("Error " + e.getMessage());
            }
        }

        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Is that correct? (y/n): ");
                String correct = scanner.nextLine();

                if (!correct.equalsIgnoreCase("y") && !correct.equalsIgnoreCase("n")) {
                    throw new InputMismatchException("Your input must be Y or N only");
                }

                if (correct.equalsIgnoreCase("n")) {
                    System.out.println("Going back");
                    this.createAccount(scanner, backEnd);
                    return;
                }

                validInput = true;
                System.out.println("Great!");
                this.financeTrackerMainWindow(name, age, scanner, backEnd);

            } catch (InputMismatchException e) {
                System.out.println("Error! " + e.getMessage());
            }
        }
    }

    public void financeTrackerMainWindow(String name, int age, Scanner scanner, backEnd backEnd) {
        System.out.println("=".repeat(70));
        System.out.println("");
        backEnd.printCentered("Account Created Successfully!");
        backEnd.printCentered("Welcome to your Dashboard " + name + " || " + "Age: " + age);
        System.out.println("");
        System.out.println("=".repeat(70));
        System.out.println();

        System.out.println("1. Add Money: \n" +
                "2. Withdraw Money: \n" +
                "3. View Balance: \n" +
                "4. View Transaction History: \n" +
                "5. Exit \n");

        System.out.print("Please choose an option: ");
        int options = scanner.nextInt();
        scanner.nextLine();

        backEnd.financeTrackerMainWindowOptions(scanner, options);

    }

    public void balance() {
        System.out.println("Your Balance: ");
        System.out.println("The current Date: ");
    }
}

