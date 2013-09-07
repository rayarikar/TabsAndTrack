package com.tnt.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the utility methods related to the expenses (personal or group)
 * which are used by the application.
 * @author Rohan
 *
 */
public class ExpenseUtility {

	/**
	 * This method returns all the defaults of the transactionType dropdown list
	 * @return
	 */
	public static List<String> getTransactionTypeSpinnerDefault(){
		List<String> transactionTypes = new ArrayList<String>();
		transactionTypes.add("- Select Type -");
		transactionTypes.add("Grocery");
		transactionTypes.add("Rent");
		return transactionTypes;
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
