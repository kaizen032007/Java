public class SavingsAccount extends Account {
    private double interestRate = 0.02;
    private static final String ACCOUNT_TYPE = "Savings";

    SavingsAccount(String accountNumber, double balance, Customer owner, double interestRate) {
        super(accountNumber, balance, owner);
        this.interestRate = interestRate;
    }

    @Override
    public void applyInterest() {
        double interestAmount = getBalance() * interestRate;
        deposit(interestAmount);
    }

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }
}
