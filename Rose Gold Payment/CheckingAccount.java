public class CheckingAccount extends Account {
    private double overdraftlimit;
    private static final String ACCOUNT_TYPE = "Checking";

    CheckingAccount(String accountNumber, double balance, Customer owner, double overdraftlimit) {
        super(accountNumber, balance, owner);
        this.overdraftlimit = overdraftlimit;
    }

    @Override
    public void applyInterest() {

    }

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        } else if (amount <= getBalance()) {
            balance -= amount;
            return true;
        } else if (amount <= getBalance() + overdraftlimit) {
            balance -= amount;
            return true;
        } else if (amount > getBalance() + overdraftlimit) {
            return false;
        } else {
            return false;
        }
    }

}
