package assignment2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Driver {
	protected static HashMap<Integer, Customer> client = new HashMap<Integer, Customer>();
	static String doublenumber = "([0-9]*)\\.([0-9]*)";
	static NumberFormat df = new DecimalFormat("#0.00");
	
	public static void main(String[] args) {

		String input = null;
		boolean bank_open = true;
		while (bank_open) {
			// Input transaction
			input = JOptionPane.showInputDialog("Enter Transaction Detail(case-insensitive)");
			/**
			 * each order is separates by space(s), then check if the input is
			 * valid
			 */
			String[] order = input.toUpperCase().split(" ");
			String message = excute_order(order);
			JOptionPane.showMessageDialog(null, message);
			//ask customer to process with the next transaction
			int choice = JOptionPane.showConfirmDialog(null, "Would you like to contiue your Transcation?", null,
					JOptionPane.YES_NO_OPTION);
			if (choice == 1) {
				bank_open = false;
			}
			
		}
		// generate a summary for each customer
		for(Entry<Integer, Customer> temp : client.entrySet()){
			Customer holder = temp.getValue();
			System.out.println(bank_Statement(holder));
		}

	}
	
	/**
	 * generates a bank statement
	 * 
	 * @param holder
	 * @return
	 */
	public static String bank_Statement(Customer holder) {
		//get the balance from each account
		String check = df.format(holder.check.balance);
		String save = df.format(holder.save.balance);
		String student = df.format(holder.student.balance);
		String auto = df.format(holder.auto.balance);
		return "Customer" + holder.customer_number + "'s bank statement\n" 
				+"Checking: " + check + "\n"
				+"Saving: "+ save +"\n"
				+"Auto:" + auto + "\n"
				+"Student: " + student + "\n";
	}

	public static String excute_order(String[] order) {
		/**
		 * base on the property of the input string, each order is separates by
		 * space
		 */

		// contains at least 3 parts dependes on the type of transcation
		if (order.length < 3) {
			return "Illegal Transactions Input";
		}

		// if the id is a number
		if (!order[0].matches("[0-9]+")) {
			return "Illegal Customer ID#(should be a positive integer)";
		}
		// find the customer in the hashmap
		int key = Integer.parseInt(order[0]);
		if (key == 0) {
			return "Illegal Customer ID#(ID# should be greater than 0)";
		}
		if (!client.containsKey(key)) {
			Customer new_client = new Customer(key);
			client.put(key, new_client);
		}
		// check for valid transaction type[D,W,I,T,G]
		if (!order[1].matches("[D,W,I,T,G]")) {
			return "Illegal Transaction Type Codes: " + order[1];
		}
		/**
		 * process the appropriate transaction type
		 */
		Customer a = client.get(key);
		switch (order[1]) {

		case "D":
			if (order.length != 4) {
				return "Illegal Transactions Input";
			}
			return process_Deposit(a, order);

		case "W":
			if (order.length != 4) {
				return "Illegal Transactions Input";
			}
			return process_Withdraw(a, order);
		case "I":
			if (order.length != 3) {
				return "Illegal Transactions Input";
			}
			return process_Interest(a, order);
		case "T":
			if (order.length != 5) {
				return "Illegal Transactions Input";
			}
			return process_Transfer(a, order);
		case "G":
			if (order.length != 3) {
				return "Illegal Transactions Input";
			}
			return process_Getbalance(a, order);

		default:
			return "Illegal Transaction Type Codes";
		}

	}

	/**
	 * deposit money
	 * 
	 * @param holder
	 * @param order
	 * @return
	 */
	public static String process_Deposit(Customer holder, String[] order) {
		//check if the amount is valid
		if (!Pattern.matches(doublenumber, order[2])) {
			return "Illegal Transactions Amount";
		}
		double amount = Double.parseDouble(order[2]);
		if (amount < 0) {
			return "Amount is Negative";
		}
		//find the account that the customer want to process
		BankAccount account_type = account(holder, order[3]);
		
		if(account_type == null){
			return "Illegal Account Type";
		}
		account_type.deposit(amount);  
		
		//return the activity
		return "Customer " + holder.getNumber() + " deposit " + df.format(amount) + " dollars";
	}

	/**
	 * withdraw money
	 * 
	 * @param holder
	 * @param order
	 * @return
	 */
	public static String process_Withdraw(Customer holder, String[] order) {
		//check if the amount is valid
		if (!Pattern.matches(doublenumber, order[2])) {
			return "Illegal Transactions Amount";
		}

		double amount = Double.parseDouble(order[2]);
		if (amount < 0) {
			return "Amount is Negative";
		}
		boolean withdraw = true;
		
		//find the account that the customer want to process
		switch (order[3]) {

		case "C":
			withdraw = holder.check.withdraw(holder, amount);
			break;
		case "S":
			withdraw = holder.save.withdraw(amount);
			break;
		case "L":

			withdraw = holder.student.withdraw(amount);
			break;
		case "A":

			withdraw = holder.auto.withdraw(amount);
			break;
		default:
			return "Illegal Account Type";
		}
		//report the activity
		if (withdraw) {
			return "Customer " + holder.getNumber() + " withdraw " + df.format(amount) + " dollars";
		} else {
			return "Customer " + holder.getNumber() + " cannot withdraw " + df.format(amount) + " dollars";
		}
	}

	/**
	 * calculate interest
	 * 
	 * @param holder
	 * @param order
	 * @return
	 */
	public static String process_Interest(Customer holder, String[] order) {
		double interest = 0.04;
		boolean cal = true;
		
		//find the account
		switch (order[2]) {

		case "S":
			cal = holder.save.calculateInterest(interest);
			break;
		case "A":
			cal = holder.auto.calculateInterest(interest);
			break;
		case "L":
			cal = holder.student.calculateInterest(interest);
			break;

		case "C":
			return "Checking account can not add in interest";

		default:
			return "Illegal Account Input ";
		}
		
		//report activity
		if (cal) {
			return "Customer " + holder.getNumber() + "'s account has compute and add in interest";
		} else {
			return "Customer " + holder.getNumber() + " cannot add in interest";
		}

	}

	/**
	 * transfer money from accounts
	 * 
	 * @param holder
	 * @param order
	 * @return
	 */
	public static String process_Transfer(Customer holder, String[] order) {
		//check if the amount is valid
		if (!Pattern.matches(doublenumber, order[2])) {
			return "Illegal Transactions Amount";
		}

		double amount = Double.parseDouble(order[2]);
		if (amount < 0) {
			return "Amount is Negative";
		}
		//find the two account 
		BankAccount from = account(holder, order[3]);
		
		BankAccount to = account(holder, order[4]);
		
		if(from == null || to == null) {
			return "Illegal Account Type";
		}

		boolean worked = from.transferMoney(to, amount);
		
		//report activity
		if (worked) {
			return "Customer " + holder.getNumber() + " transfer " + amount + " dollars";
		} else {
			return "Customer " + holder.getNumber() + " was unable to transfer " + amount + " dollars";
			
		}
	}

	/**
	 * get the balance of the account
	 * 
	 * @param holder
	 * @param order
	 * @return
	 */
	public static String process_Getbalance(Customer holder, String[] order) {
		
		double balance = 0.00;
		BankAccount account_type = account(holder, order[2]);
		if(account_type == null){
			return "Illegal Account Type";
		}
		balance = account_type.balance;
		
		return "Customer" + holder.customer_number + " has a balance of " + df.format(balance) + " dollars";

	}

	/**
	 * return the right account<only works for transfer, deposit, get balance>
	 * 
	 * @param holder
	 * @param order
	 * @return
	 */
	public static BankAccount account(Customer holder, String order) {

		switch (order) {

		case "C":
			return holder.check;
		case "S":
			return holder.save;

		case "A":
			return holder.auto;

		case "L":
			return holder.student;

		default:
			return null;
		}
	}
}
