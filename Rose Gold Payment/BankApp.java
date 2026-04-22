import java.util.List;
import java.util.Scanner;

public class BankApp {
    private BankService bankService;
    private Scanner scanner;

    public BankApp() {
        this.bankService = new BankService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=".repeat(40));
        System.out.println("   WELCOME TO ROSE GOLD PAYMENT");
        System.out.println("=".repeat(40));

        while (true) {
            if (bankService.getCurrentCustomer() == null) {
                showMainMenu();
            } else {
                showBankingMenu();
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                handleRegistration();
                break;
            case 2:
                handleLogin();
                break;
            case 3:
                System.out.println("Thank you for banking with us. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void handleRegistration() {
        System.out.println("\n--- REGISTRATION ---");
        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter desired Customer ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter your gender: ");
        String gender = scanner.nextLine();

        boolean success = bankService.registerCustomer(name, id, password, age, email, phone, gender);
        if (success) {
            System.out.println("\n✅ Registration successful! You can now login.");
        } else {
            System.out.println("\n❌ Registration failed. Customer ID already exists.");
        }
    }

    private void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter Customer ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = bankService.login(id, password);
        if (success) {
            Customer current = bankService.getCurrentCustomer();
            System.out.println("\n✅ Welcome back, " + current.getCustomerName() + "!");
        } else {
            System.out.println("\n❌ Login failed. Invalid ID or password.");
        }
    }

    private void showBankingMenu() {
        Customer current = bankService.getCurrentCustomer();
        System.out.println("\n--- BANKING MENU (" + current.getCustomerName() + ") ---");
        System.out.println("1. View My Accounts");
        System.out.println("2. Create New Savings Account");
        System.out.println("3. Create New Checking Account");
        System.out.println("4. Deposit");
        System.out.println("5. Withdraw");
        System.out.println("6. Transfer");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                viewAccounts();
                break;
            case 2:
                createSavingsAccount();
                break;
            case 3:
                createCheckingAccount();
                break;
            case 4:
                handleDeposit();
                break;
            case 5:
                handleWithdraw();
                break;
            case 6:
                handleTransfer();
                break;
            case 7:
                bankService.logout();
                System.out.println("You have been logged out.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void viewAccounts() {
        List<Account> accounts = bankService.getAccountsForCurrentCustomer();
        if (accounts.isEmpty()) {
            System.out.println("\nYou have no accounts. Create one first.");
            return;
        }
        System.out.println("\n--- YOUR ACCOUNTS ---");
        for (Account acc : accounts) {
            System.out.printf("%s (%s) - Balance: $%.2f%n",
                    acc.getAccountNumber(),
                    acc.getAccountType(),
                    acc.getBalance());
        }
    }

    private void createSavingsAccount() {
        System.out.print("\nEnter initial deposit amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        String accNum = bankService.createSavingsAccount(amount);
        if (accNum != null) {
            System.out.println("✅ Savings account created! Account Number: " + accNum);
        } else {
            System.out.println("❌ Failed to create account. Are you logged in?");
        }
    }

    private void createCheckingAccount() {
        System.out.print("\nEnter initial deposit amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter overdraft limit: $");
        double limit = scanner.nextDouble();
        scanner.nextLine();

        String accNum = bankService.createCheckingAccount(amount, limit);
        if (accNum != null) {
            System.out.println("✅ Checking account created! Account Number: " + accNum);
        } else {
            System.out.println("❌ Failed to create account. Are you logged in?");
        }
    }

    private void handleDeposit() {
        System.out.print("\nEnter account number: ");
        String accNum = scanner.nextLine();

        System.out.print("Enter amount to deposit: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (bankService.deposit(accNum, amount)) {
            System.out.println("✅ Deposit successful.");
        } else {
            System.out.println("❌ Deposit failed. Check account number.");
        }
    }

    private void handleWithdraw() {
        System.out.print("\nEnter account number: ");
        String accNum = scanner.nextLine();

        System.out.print("Enter amount to withdraw: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (bankService.withdraw(accNum, amount)) {
            System.out.println("✅ Withdrawal successful.");
        } else {
            System.out.println("❌ Withdrawal failed. Check account number, balance, or ownership.");
        }
    }

    private void handleTransfer() {
        System.out.print("\nEnter YOUR account number (source): ");
        String fromAcc = scanner.nextLine();

        System.out.print("Enter DESTINATION account number: ");
        String toAcc = scanner.nextLine();

        System.out.print("Enter amount to transfer: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (bankService.transfer(fromAcc, toAcc, amount)) {
            System.out.println("✅ Transfer successful.");
        } else {
            System.out.println("❌ Transfer failed. Check account numbers, balance, or ownership.");
        }
    }
}