package src;

import java.util.Scanner;
import java.util.InputMismatchException;

public class UserFunctions {
    private String firstName;
    private String secondName;
    private String locationAddress;
    private int age;
    private String schoolName;
    private boolean validInput;

    public static void printCentered(String text) {
        int terminalWidth = 65;
        int padding = (terminalWidth - text.length()) / 2;
        String centered = " ".repeat(padding) + text;
        System.out.println(centered);
    }

    public void introduction(Scanner scanner) {
        System.out.println("=".repeat(70));
        System.out.println();
        printCentered("Welcome to University of Zodiac");
        System.out.println();
        System.out.println("=".repeat(70));

        System.out.println("This is your dashboard");
        System.out.println("1. Register (for new user account) \n" +
                "2. Enter Student Portal \n" +
                "3. Exit");

        validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter: ");
                int options = scanner.nextInt();

                if (options > 3) {
                    System.out.println("Please choose 1, 2 and 3 only. [TRY AGAIN]");
                    return;
                }

                if (options == (1)) {
                    this.userRegistration(scanner);
                    validInput = true;
                }
            } catch (InputMismatchException errorInput) {

            }
        }

    }

    public void userRegistration(Scanner scanner) {
        System.out.println("=".repeat(70));
        printCentered("Welcome to your dashboard");
        printCentered("Please Enter all the correct information below");
        System.out.println("=".repeat(70));

        System.out.print("Enter your First Name: ");
        firstName = scanner.nextLine();
        scanner.nextLine();
        System.out.println();

        System.out.print("Enter your Second Name: ");
        secondName = scanner.nextLine();
        System.out.println();

        System.out.print("Enter your Age: ");
        age = scanner.nextInt();
        scanner.nextLine();
        System.out.println();

        System.out.print("Enter your Complete Address: ");
        locationAddress = scanner.nextLine();
        scanner.nextLine();
        System.out.println();

        System.out.println("Enter your Past School Name: ");
        schoolName = scanner.nextLine();
        scanner.nextLine();
        System.out.println();

    }

}
