import java.util.Scanner;

public class backEnd {

    // this method allows the print to be centered
    public static void printCentered(String text) {
        int terminalWidth = 65;
        int padding = (terminalWidth - text.length()) / 2;
        String centered = " ".repeat(padding) + text;
        System.out.println(centered);
    }

    public void financeTrackerMainWindowOptions(Scanner scanner, int options) {
        boolean validInput = false;

        while (!validInput) {
            if (options == 1) {
                this.addMoney(scanner);
            }
        }
    }

    public void addMoney(Scanner scanner) {
        System.out.print("Do you want to add Money? ");
        String option = scanner.nextLine();

        if (option.equalsIgnoreCase("y") && option.equalsIgnoreCase("n")) {

        }

        System.out.println("=".repeat(70));
        printCentered("Proceeding to add money please wait");
        System.out.println("=".repeat(70));
    }
}
