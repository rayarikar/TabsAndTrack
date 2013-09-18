package com.tnt.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Account;
import com.tnt.entity.TransactionType;
import com.tnt.utility.ExpenseUtility;

public class PersonalExpenseActivity extends
		OrmLiteBaseActivity<DatabaseHelper> {

	private EditText transactionName;
	private Spinner transactionTypeSpinner;
	private DatePicker transactionDate;
	private EditText trasactionAmount;
	private Spinner accountNameSpinner;

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
		setContentView(R.layout.activity_personal_expense);
		transactionName = ((EditText) findViewById(R.id.transactionName));
		transactionTypeSpinner = ((Spinner) findViewById(R.id.transactionType));
		transactionDate = ((DatePicker) findViewById(R.id.datePicker));
		trasactionAmount = ((EditText) findViewById(R.id.amount));
		accountNameSpinner = ((Spinner) findViewById(R.id.accountType));
		// populates all the spinners
		loadTransactionTypeSpinnerData();
		loadAccountsSpinnerData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.personal_expense, menu);
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
	 * 
	 * @author Rohan / Ameya
	 */
	public void onResetClick(View view) {
		transactionName.setText("");
		transactionTypeSpinner.setSelection(0);
		transactionDate.init(year, month, day, null);
		trasactionAmount.setText("");
		accountNameSpinner.setSelection(0);
	}

	/**
	 * persist all the details on click of done button
	 * 
	 * @author Rohan / Ameya
	 */
	public void onDoneClick(View view) {
		// check whether select is selected from the spinner drop downs. If yes then
		// give out a toast else persist the data
	}

	/**
	 * this method should open a new activity is used to add a transaction type
	 * 
	 * @author Rohan / Ameya
	 */
	public void onAddTypeClick(View view) {
		Intent redirectToAddType = new Intent(this, AddTypeActivity.class);
		redirectToAddType.putExtra("activityName",
				"PersonalExpenseAddTypeActivity");
		startActivity(redirectToAddType);
	}

	/**
	 * loads the spinner data from SQLite database
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
		transactionTypeSpinner.setAdapter(transactionTypeDataAdapter);
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
		accountNameSpinner.setAdapter(accountNameAdapter);
	}
}
