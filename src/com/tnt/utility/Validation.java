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

}
