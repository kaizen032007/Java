import java.util.InputMismatchException;
import java.util.Scanner;

public class financeMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputMismatchException inputError = new InputMismatchException();
        FrontEnd frontEnd = new FrontEnd();
        backEnd backEnd = new backEnd();

        frontEnd.frontpage(backEnd, scanner, inputError);

        scanner.close();
    }
}
