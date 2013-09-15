package com.tnt.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_expense, menu);
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

		String transactionName = groupTransactionName.getText().toString();
		String transactionType = groupTransactionTypeSpinner.getSelectedItem().toString();
		String transAmount = groupTrasactionAmount.getText().toString();
		String transactionDate = groupTransactionDate.getDayOfMonth() + "/" + groupTransactionDate.getMonth() + "/" + groupTransactionDate.getYear();
		String trasactionAccount = groupAccountNameSpinner.getSelectedItem().toString();
		
		// gets the id of selected radio button
		int radioId=  groupSplitRadioChoice.getCheckedRadioButtonId();
		String radioSelectedValue = ((RadioButton) findViewById(radioId)).getText().toString();

		
			/**
			 * @TODO- 
			 * pass all the values to the next activity!!
			 */
		
		
		// check whether the amount is valid. Then only proceed
		if (Validation.isAmountValid(transAmount)){
			// redirect to the contacts activity
			Intent redirectToGrpExpSplitIntent = new Intent(this, GroupExpenseSplitActivity.class);
			finish();
			startActivity(redirectToGrpExpSplitIntent);
		} else {
			Toast invalidLoginToast = Toast.makeText(this, "Amount is not a  number", Toast.LENGTH_LONG);
			invalidLoginToast.show();
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
