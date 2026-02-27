import java.util.InputMismatchException;
import java.util.Scanner;

public class frontEnd {
    
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

                if (createAcct.equals("n")) {
                    System.out.println("Thank you for visiting!");
                    break;
                }
                validInput = true;
                System.out.println("Let us proceed!\n");
                this.registerAccount(scanner, inputError);


            } catch (IllegalArgumentException e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    public void registerAccount(Scanner scanner, InputMismatchException inputError) {
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

                validInput = true;
                System.out.println("Let us proceed!\n");
                this.createAccount(scanner);


            } catch (IllegalArgumentException e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    public static void createAccount(Scanner scanner) {
        boolean validInput =  false;
        while (!validInput) {
            try {
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();
                
                if (!name.matches("[a-zA-Z ]+")) {
                    throw new IllegalArgumentException("You must put letters only in your name");
                }

                validInput = true;
                System.out.println("Welcome, " + name);

            } catch (InputMismatchException e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }
}
