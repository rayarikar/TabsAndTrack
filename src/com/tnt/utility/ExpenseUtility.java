package com.tnt.utility;

import java.util.ArrayList;
import java.util.List;

import com.tnt.entity.Account;
import com.tnt.entity.TransactionType;

/**
 * This class contains all the utility methods related to the expenses (personal or group)
 * which are used by the application.
 * @author Rohan
 *
 */
public class ExpenseUtility {

	private static String selectFromList = "- Select -";
	
	/**
	 * This method returns all the defaults of the transactionType dropdown list
	 * @return
	 */
	public static List<TransactionType> getTransactionTypeSpinnerDefault(){
		List<TransactionType> transactionTypes = new ArrayList<TransactionType>();
		
		TransactionType transactionType1 = new TransactionType();
		transactionType1.setTransactionType(selectFromList);
		transactionTypes.add(transactionType1);
		TransactionType transactionType2 = new TransactionType();
		transactionType2.setTransactionType("Grocery");
		transactionTypes.add(transactionType2);
		TransactionType transactionType3 = new TransactionType();
		transactionType3.setTransactionType("Rent");		
		transactionTypes.add(transactionType3);
		return transactionTypes;
	}
	
	/**
	 * This method returns all the defaults of the accounts dropdown list
	 * @return
	 */
	public static List<Account> getAccountNameSpinnerDefault(){
		List<Account> accounts = new ArrayList<Account>();
		
		Account acc = new Account();
		acc.setAccountName(selectFromList);
		acc.setAccountLimit(0.0);
		accounts.add(acc);
		return accounts;
	}
	
	/**
	 * This method takes in a List and a String and adds the data to the list of the data is not null or empty
	 * @param inputList
	 * @param data
	 * @return
	 */
	public static List<String> appendToList(List<String> inputList, String data){
		if (!data.equalsIgnoreCase("") || data != null){
			inputList.add(data);
		}
		return inputList;
	}
	
}
