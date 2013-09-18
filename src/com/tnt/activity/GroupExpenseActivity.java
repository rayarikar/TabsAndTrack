package com.tnt.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Account;
import com.tnt.entity.Transaction;
import com.tnt.entity.TransactionType;
import com.tnt.utility.ExpenseUtility;
import com.tnt.utility.Validation;

/**
 * This class implements all the functionality related to group expense activity
 * @author Rohan
 *
 */
public class GroupExpenseActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText groupTransactionName;
	private Spinner groupTransactionTypeSpinner;
	private DatePicker groupTransactionDate;
	private EditText groupTrasactionAmount;
	private Spinner groupAccountNameSpinner;
	private RadioGroup groupSplitRadioChoice;

	private RuntimeExceptionDao<TransactionType, Integer> transactionTypeDao;
	private RuntimeExceptionDao<Account, Integer> accountDao;

	final Calendar c = Calendar.getInstance();
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initializing all Doas
		try {
			transactionTypeDao = getHelper().getTransactionTypeDao();
			accountDao = getHelper().getAccountDao();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContentView(R.layout.activity_group_expense);
		groupTransactionName = ((EditText) findViewById(R.id.groupTransactionName));
		groupTransactionTypeSpinner = ((Spinner) findViewById(R.id.groupTransactionType));
		groupTransactionDate = ((DatePicker) findViewById(R.id.groupDatePicker));
		groupTrasactionAmount = ((EditText) findViewById(R.id.groupAmount));
		groupAccountNameSpinner = ((Spinner) findViewById(R.id.groupAccountType));
		groupSplitRadioChoice = (RadioGroup) findViewById(R.id.split_transaction_radio_group);

		// populates all the spinners
		loadTransactionTypeSpinnerData();
		loadAccountsSpinnerData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.group_expense, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.logout:
			LoginActivity loginActivityObj = new LoginActivity();
			loginActivityObj.logout(this);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			break;
		}
		return true;
	}


	/**
	 * resets the entire layout
	 */
	public void onResetClick(View view) {
		groupTransactionName.setText("");
		groupTransactionTypeSpinner.setSelection(0);
		groupTransactionDate.init(year, month, day, null);
		groupTrasactionAmount.setText("");
		groupAccountNameSpinner.setSelection(0);
	}

	/**
	 * persist all the details on click of done button
	 */
	public void onNextClick(View view) {
		// check whether select is selected from the spinner drop downs. If yes then
		// give out a toast else persist the data
		String transactionType = groupTransactionTypeSpinner.getSelectedItem().toString();
		String trasactionAccount = groupAccountNameSpinner.getSelectedItem().toString();		

		// if account or transaction type selected is not select word then only process
		// else give out a toast
		if (Validation.isValidAccountSelected(trasactionAccount) && Validation.isValidTransactionTypeSelected(transactionType)){

			String transAmount = groupTrasactionAmount.getText().toString();
			// check whether the amount is valid. Then only proceed
			if (Validation.isAmountValid(transAmount)){

			double transactionTotalAmount = Double.parseDouble(transAmount);	
			String transactionName = groupTransactionName.getText().toString();
			String transactionDate = groupTransactionDate.getDayOfMonth() + "/" + groupTransactionDate.getMonth() + "/" + groupTransactionDate.getYear();
			// gets the id of selected radio button
			int radioId=  groupSplitRadioChoice.getCheckedRadioButtonId();
			String radioSelectedValue = ((RadioButton) findViewById(radioId)).getText().toString();

			int accountId = 0;
			int transactionTypeId = 0;
			// get the accountId
			List<Account> allAccounts = accountDao.queryForAll();
			for (Account account : allAccounts){
				if (account.getAccountName().equalsIgnoreCase(trasactionAccount)){
					accountId = account.getAccountId();
				}
			}

			// get the transactionId
			List<TransactionType> allTransactionTypes = transactionTypeDao.queryForAll();
			for (TransactionType transType : allTransactionTypes){
				if (transType.getTransactionType().equalsIgnoreCase(transactionType)){
					transactionTypeId = transType.getTransactionTypeId();
				}
			}
				
				// set the variables in Transaction class
				Transaction groupTransaction = new Transaction();
				groupTransaction.setAccountId(accountId);
				groupTransaction.setTransactionName(transactionName);
				groupTransaction.setTransactionTypeId(transactionTypeId);
				groupTransaction.setTransactionDate(transactionDate);
				groupTransaction.setTransactionTotalAmount(transactionTotalAmount);
				groupTransaction.setExpenseType(ExpenseUtility.expenseTypeGroup);
				
				// redirect to the contacts activity
				Intent redirectToGrpExpSplitIntent = new Intent(this, GroupExpenseSplitActivity.class);
				// pass the transaction object and the string which indicates how to split the transaction
				redirectToGrpExpSplitIntent.putExtra("CallingActivity", "GroupExpenseActivity");
				redirectToGrpExpSplitIntent.putExtra("TransactionObject", groupTransaction);
				redirectToGrpExpSplitIntent.putExtra("RadioButtonValue", radioSelectedValue);
				finish();
				startActivity(redirectToGrpExpSplitIntent);
			} else {
				Toast invalidNumberToast = Toast.makeText(this, "Amount should be a valid number", Toast.LENGTH_LONG);
				invalidNumberToast.show();
			}
		} else {
			Toast invalidSelectToast = Toast.makeText(this, "Please select proper Transaction type and Account.", Toast.LENGTH_LONG);
			invalidSelectToast.show();
		}

	}


	/**
	 * this method should open a new activity is used to add a transaction type
	 */
	public void onGroupAddTypeClick(View view) {
		Intent redirectToAddType = new Intent(this, AddTypeActivity.class);
		redirectToAddType.putExtra("activityName",
				"GroupExpenseAddTypeActivity");
		startActivity(redirectToAddType);
	}

	/**
	 * loads the spinner data from SQLite database for transaction type
	 * @author Rohan
	 */
	public void loadTransactionTypeSpinnerData() {
		// Spinner Drop down elements
		List<String> transactionTypeNames = new ArrayList<String>();
		// always populate it with default select
		transactionTypeNames.add(ExpenseUtility.selectTransactionFromList);

		// then add the data
		List<TransactionType> transactionTypeAllDetails = transactionTypeDao
				.queryForAll();
		String transType = null;
		for (TransactionType transactionType : transactionTypeAllDetails) {
			transType = transactionType.getTransactionType().toString();
			if (!transType.equalsIgnoreCase("") || transType != null) {
				transactionTypeNames.add(transType);
			}
		}
		// Creating adapter for spinner
		ArrayAdapter<String> transactionTypeDataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, transactionTypeNames);

		// Drop down layout style - list view with radio button
		transactionTypeDataAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		groupTransactionTypeSpinner.setAdapter(transactionTypeDataAdapter);
	}


	/**
	 * loads the spinner data from SQLite database for accounts
	 * @author Rohan
	 */
	public void loadAccountsSpinnerData() {
		// Spinner Drop down elements
		List<String> accountNames = new ArrayList<String>();
		// populate with default select word
		accountNames.add(ExpenseUtility.selectAccountFromList);

		// then add the data
		List<Account> accountAllDetails = accountDao.queryForAll();
		String accountName = null;
		for (Account accName : accountAllDetails) {
			accountName = accName.getAccountName().toString();
			if (!accountName.equalsIgnoreCase("") || accountName != null) {
				accountNames.add(accountName);
			}
		}
		// Creating adapter for spinner
		ArrayAdapter<String> accountNameAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, accountNames);

		// Drop down layout style - list view with radio button
		accountNameAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		groupAccountNameSpinner.setAdapter(accountNameAdapter);
	}



}
