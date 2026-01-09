// this is simple java calculator using methods. 

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the First Number: ");
        int firstNum = scanner.nextInt();

        System.out.print("Enter the Second Number: ");
        int secondNum = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Choose what operation: [x, -, +, /]: ");
        String operation = scanner.nextLine();

        compute(firstNum, secondNum, operation);

    }

    public static void compute(int n1, int n2, String op) {
        int result = 0;

        switch (op) {
            case "+":
                result = n1 + n2;
                System.out.println("Result: " + result);
                break;
            case "-":
                result = n1 - n2;
                System.out.println("Result: " + result);
                break;
            case "x":
                result = n1 * n2;
                System.out.println("Result: " + result);
                break;
            case "/":
               if (n2 != 0) {
                System.out.println("Result: " + (double) n1 / n2);
               } else {
                System.out.println("ERROR: You cannot divide any number to zero");
               }
               break;
            default:
                System.out.println("Invalid Input");
        }
    }
}
