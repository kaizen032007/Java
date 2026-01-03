// Simple Arrays with linear search  

import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalTime now = LocalTime.now();

        System.out.println("=".repeat(25));
        System.out.println(" BANK OF THE PHILIPPINES");
        System.out.println("=".repeat(25));

        System.out.print("Enter your name here: ");
        var name = scanner.nextLine();

        // +500 means they got paid $500
        // -20 means they bought lunch for $20
        int[] transactions = { 500, -20, -50, 1000, -5, -100, 250 };

        System.out.println("Hello " + name);
        System.out.println("BALANCE AS OF " + now);
        for (int i = 0; i < transactions.length; i++) {
            System.out.println("Current Balance: " + "$"+ transactions[i]);
            
        }

        System.out.println("=".repeat(25));
        System.out.println("  For the most expenses");
        System.out.println("=".repeat(25));
        
        int maxExpense = transactions[0];
        for (int x = 0; x < transactions.length; x++) {
            if (transactions[x] < maxExpense) {
                maxExpense = transactions[x];
            }
        }
        System.out.println("The most expense is " + maxExpense);

        System.out.println("=".repeat(25));
        System.out.println("  For the most deposits");
        System.out.println("=".repeat(25));

        int count = 0;
        for (int y = 0; y < transactions.length; y++) {
            if (transactions[y] > 0) {
                count++;
            }
        }

        System.out.println("The most deposits is " + count);

        int total = 0;
        System.out.println("=".repeat(25));
        System.out.println("  For the total balance");
        System.out.println("=".repeat(25));
        for (int z = 0; z < transactions.length; z++) {
            total += transactions[z];
        }

        System.out.println("The total balance is " + "$" + total);


        scanner.close();
    }
}