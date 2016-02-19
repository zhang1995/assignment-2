package assignment2;

public class Customer {

	protected String customer_name;
	protected int customer_number;
	protected String customer_address;
	// Account under customers name
	protected SavingAccount save;
	protected CheckingAccount check;
	protected SavingAccount auto;
	protected SavingAccount student;
	// Constructor
	public Customer(int number){
		customer_number = number;
		customer_name = null;
		customer_address = null;
		// Account under the customer
		save = new SavingAccount(0.0);
		check = new CheckingAccount(0.0);
		auto = new SavingAccount(0.0);
		student = new SavingAccount(0.0);
	}

	public Customer(String name, int number, String address) {
		// TODO Auto-generated constructor stub
		customer_number = number;
		customer_name = name;
		customer_address = address;
		// Account under the customer
		save = new SavingAccount(0.0);
		check = new CheckingAccount(0.0);
		auto = new SavingAccount(0.0);
		student = new SavingAccount(0.0);
	}

	/**
	 * @return customer name
	 */
	public String getName() {
		return this.customer_name;
	}

	/**
	 * @return customer address
	 */
	public String getAddress() {
		return this.customer_address;
	}

	/**
	 * @return customer unique number
	 */
	public int getNumber() {
		return this.customer_number;
	}

	/**
	 * set a new name
	 */
	public void setName(String name) {
		this.customer_name = name;
	}

	/**
	 * set a new address
	 */
	public void setAddress(String address) {
		this.customer_address = address;
	}

	/**
	 * set a new unique number
	 */
	public void setNumber(int number) {
		this.customer_number = number;
	}
}
