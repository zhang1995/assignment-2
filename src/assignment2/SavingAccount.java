package assignment2;

class SavingAccount extends BankAccount {

	public SavingAccount(double initBalance) {
		super(initBalance);
	}

	public SavingAccount(int acct, Customer owner, double initBalance) {
		super(acct, owner, initBalance);
		// TODO Auto-generated constructor stub
	}
	
	public boolean calculateInterest(double interest){
		if(this.balance >= 1000) {
			this.balance = this.balance * (1+interest);
			return true;
		}
		
		return false;
		
	}

}
