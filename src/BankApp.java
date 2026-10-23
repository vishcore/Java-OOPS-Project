
import java.util.ArrayList;
import java.util.Scanner;

// Interface: defines common transaction operations
interface TransactionOperations {
    void deposit(double amount);
    void withdraw(double amount);
}

// Composition: stores transaction history for one account
class TransactionHistory {
    private ArrayList<String> transactions = new ArrayList<String>();

    public void addTransaction(String message) {
        transactions.add(message);
    }

    public void showHistory() {
        if (transactions.size() == 0) {
            System.out.println("No transactions yet.");
        }

        for (int i = 0; i < transactions.size(); i++) {
            System.out.println(transactions.get(i));
        }
    }
}

// Abstract parent class
abstract class BankAccount implements TransactionOperations {
    private String name;
    private int accountNumber;
    private double balance;

    protected TransactionHistory history = new TransactionHistory();

    static int accountCount = 0;
    static final double MINIMUM_BALANCE = 500;

    // Constructor
    public BankAccount(String name, int accountNumber,
                       double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        accountCount++;
    }

    // Constructor overloading
    public BankAccount(String name, int accountNumber) {
        this(name, accountNumber, 0);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    // Method overloading
    public void deposit(double amount) {
        deposit(amount, "Normal deposit");
    }

    public void deposit(double amount, String description) {
        if (amount > 0) {
            balance = balance + amount;
            history.addTransaction("Deposited: " + amount
                    + " - " + description);
            System.out.println("Deposit successful.");
        } else {
            System.out.println("Invalid amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount.");
        } else if (balance - amount < MINIMUM_BALANCE) {
            System.out.println("Minimum balance of "
                    + MINIMUM_BALANCE
                    + " must be maintained.");
        } else {
            balance = balance - amount;
            history.addTransaction("Withdrawn: " + amount);
            System.out.println("Withdrawal successful.");
        }
    }

    // Abstract method: child classes must implement it
    public abstract double calculateInterest();

    public void showDetails() {
        System.out.println("Name: " + name);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Balance: " + balance);
        System.out.println("Interest: " + calculateInterest());
    }

    public void showTransactions() {
        history.showHistory();
    }
}

// Savings account inherits BankAccount
class SavingsAccount extends BankAccount {
    static final double INTEREST_RATE = 0.04;

    public SavingsAccount(String name, int number, double balance) {
        super(name, number, balance);
    }

    // Method overriding
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    @Override
    public void showDetails() {
        System.out.println("\nSavings Account");
        super.showDetails();
    }
}

// Checking account inherits BankAccount
class CheckingAccount extends BankAccount {
    static final double INTEREST_RATE = 0.01;

    public CheckingAccount(String name, int number, double balance) {
        super(name, number, balance);
    }

    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    @Override
    public void showDetails() {
        System.out.println("\nChecking Account");
        super.showDetails();
    }
}

// Main class
public class BankApp {

    // Linear search: O(n)
    static BankAccount findAccount(
            ArrayList<BankAccount> accounts, int number) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber() == number) {
                return accounts.get(i);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<BankAccount> accounts =
                new ArrayList<BankAccount>();

        int choice;

        do {
            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Checking Account");
            System.out.println("3. Display All Accounts");
            System.out.println("4. Deposit Money");
            System.out.println("5. Withdraw Money");
            System.out.println("6. Search Account");
            System.out.println("7. Show Transaction History");
            System.out.println("8. Show Total Accounts");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                case 2: {
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter account number: ");
                    int number = sc.nextInt();

                    if (findAccount(accounts, number) != null) {
                        System.out.println("Account already exists.");
                        break;
                    }

                    System.out.print("Enter opening balance: ");
                    double balance = sc.nextDouble();

                    if (balance < 0) {
                        System.out.println("Invalid balance.");
                        break;
                    }

                    BankAccount account;

                    if (choice == 1) {
                        account = new SavingsAccount(
                                name, number, balance);
                    } else {
                        account = new CheckingAccount(
                                name, number, balance);
                    }

                    accounts.add(account);
                    System.out.println("Account created.");
                    break;
                }

                case 3:
                    for (int i = 0; i < accounts.size(); i++) {
                        accounts.get(i).showDetails();
                    }
                    if (accounts.size() == 0) {
                        System.out.println("No accounts available.");
                    }
                    break;

                case 4:
                case 5: {
                    System.out.print("Enter account number: ");
                    int number = sc.nextInt();

                    BankAccount account = findAccount(accounts, number);

                    if (account == null) {
                        System.out.println("Account not found.");
                        break;
                    }

                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    if (choice == 4) {
                        System.out.print("Enter description: ");
                        String description = sc.nextLine();

                        // Overloaded deposit method
                        account.deposit(amount, description);
                    } else {
                        account.withdraw(amount);
                    }
                    break;
                }

                case 6: {
                    System.out.print("Enter account number: ");
                    int number = sc.nextInt();

                    BankAccount account = findAccount(accounts, number);

                    if (account != null) {
                        account.showDetails();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                }

                case 7: {
                    System.out.print("Enter account number: ");
                    int number = sc.nextInt();

                    BankAccount account = findAccount(accounts, number);

                    if (account != null) {
                        account.showTransactions();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                }

                case 8:
                    System.out.println("Total accounts created: "
                            + BankAccount.accountCount);
                    break;

                case 9:
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 9);

        sc.close();
    }
}
