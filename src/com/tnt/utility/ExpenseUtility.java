package com.tnt.utility;

import java.util.ArrayList;
import java.util.List;

import com.tnt.entity.Account;
import com.tnt.entity.TransactionType;

/**
 * This class contains all the utility methods and global variables related to the expenses (personal or group)
 * which are used by the application.
 * @author Rohan
 *
 */
public class ExpenseUtility {

	public static String selectFromList = "- Select -";
	
	/**
	 * This method returns all the defaults of the transactionType dropdown list
	 * @return
	 */
	public static List<TransactionType> getTransactionTypeSpinnerDefault(){
		List<TransactionType> transactionTypes = new ArrayList<TransactionType>();
		
		TransactionType transactionType1 = new TransactionType();
		transactionType1.setTransactionType("Grocery");
		transactionTypes.add(transactionType1);
		TransactionType transactionType2 = new TransactionType();
		transactionType2.setTransactionType("Rent");		
		transactionTypes.add(transactionType2);
		return transactionTypes;
	}
}
