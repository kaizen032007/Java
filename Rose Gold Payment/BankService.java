import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankService {
    private Map<String, Customer> customers;
    private Map<String, Account> accounts;
    private Customer currentCustomer;

    public BankService() {
        customers = new HashMap<>();
        accounts = new HashMap<>();
    }

    public boolean registerCustomer(String customerName, String customerID, String password, int age, String email,
            String phoneNumber, String gender) {
        if (customers.containsKey(customerID)) {
            return false;
        } else {
            Customer customer = new Customer(customerName, customerID, password, age, email, phoneNumber, gender);
            customers.put(customerID, customer);
            return true;
        }
    }

    public boolean login(String customerID, String password) {
        Customer customer = customers.get(customerID);
        if (customer != null && customer.validatePassword(password)) {
            currentCustomer = customer;
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        currentCustomer = null;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public String createSavingsAccount(double initialBalance) {
        if (currentCustomer == null) {
            return null;
        }

        String accountNumber = "SAV-" + System.currentTimeMillis();
        SavingsAccount newAccount = new SavingsAccount(accountNumber, initialBalance, currentCustomer, 0.02);
        accounts.put(accountNumber, newAccount);
        return accountNumber;
    }

    public String createCheckingAccount(double initialBalance, double overdraftlimit) {
        if (currentCustomer == null) {
            return null;
        }

        String accountNumber = "CHK-" + System.currentTimeMillis();
        CheckingAccount newAccount = new CheckingAccount(accountNumber, initialBalance, currentCustomer,
                overdraftlimit);
        accounts.put(accountNumber, newAccount);
        return accountNumber;
    }

    public boolean deposit(String accountNumber, double amount) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            return false;
        }

        return acc.deposit(amount);
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            return false;
        }

        if (acc.getOwner() != currentCustomer) {
            return false;
        }

        return acc.withdraw(amount);
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAcc = accounts.get(fromAccountNumber);
        if (fromAcc == null) {
            return false;
        }

        Account toAcc = accounts.get(toAccountNumber);
        if (toAcc == null) {
            return false;
        }

        if (fromAcc.getOwner() != currentCustomer) {
            return false;
        }

        boolean withdraw = fromAcc.withdraw(amount);
        if (!withdraw) {
            return false;
        }

        toAcc.deposit(amount);
        return true;
    }

    public List<Account> getAccountsForCurrentCustomer() {
        List<Account> result = new ArrayList<>();
        if (currentCustomer == null) {
            return result;
        }
        for (Account acc : accounts.values()) {
            if (acc.getOwner() == currentCustomer) {
                result.add(acc);
            }
        }
        return result;
    }

}
