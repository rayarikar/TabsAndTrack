package com.tnt.utility;

import android.widget.Toast;

public class Validation {

	/**
	 * This method return a boolean which indicates whether the amount is valid or not. 
	 * Based on the returned value the activity should proceed
	 * @param amountInText
	 * @return
	 */
	public static boolean isAmountValid(String amountInText){
		boolean isAmountValid = false;
		try{
			double amount = Double.parseDouble(amountInText);
			isAmountValid = true;
		}
		catch(NumberFormatException e){
			isAmountValid = false;
		}
		return isAmountValid;
	}

	/**
	 * This method returns boolean which indicates whether the selected account is valid or not
	 * Invalid account means the default word "--Select account--"
	 * @param accountName
	 * @return
	 */
	public static boolean isValidAccountSelected(String accountName){
		if (accountName.equalsIgnoreCase(ExpenseUtility.selectAccountFromList)){
			return false;
		}
		return true;
	}

	/**
	 * This method returns boolean which indicates whether the selected transaction type is valid or not
	 * Invalid account means the default word "--Select--"
	 * @param transactionType
	 * @return
	 */
	public static boolean isValidTransactionTypeSelected(String transactionType){
		if (transactionType.equalsIgnoreCase(ExpenseUtility.selectTransactionFromList)){
			return false;
		}
		return true;
	}

}
