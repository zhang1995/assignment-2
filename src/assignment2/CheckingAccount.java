package assignment2;

class CheckingAccount extends BankAccount {

	// Constructor
	public CheckingAccount(double initBalance) {
		super(initBalance);
	}

	public CheckingAccount(int acct, Customer owner, double initBalance) {
		super(acct, owner, initBalance);
		// TODO Auto-generated constructor stub
	}

	// overdraft protection
	public boolean withdraw(Customer a, double amount) {
		if (balance < amount) {
			double diff = amount - balance;
			if ((diff + 20.00) <= a.save.balance) {
				a.save.withdraw(diff+20.00);
				a.check.balance = 0.0;
				return true;
			}
			return false;
		} else {
			return super.withdraw(amount);
		}

	}

}
