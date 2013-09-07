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
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
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

	final Calendar c = Calendar.getInstance();
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			transactionTypeDao = getHelper().getTransactionTypeDao();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContentView(R.layout.activity_personal_expense);
		transactionName = ((EditText) findViewById(R.id.transactionName));
		transactionTypeSpinner = ((Spinner) findViewById(R.id.transactionType));
		transactionDate = ((DatePicker) findViewById(R.id.datePicker));
		trasactionAmount = ((EditText) findViewById(R.id.amount));
		accountNameSpinner = ((Spinner) findViewById(R.id.accountType));
		
		loadTransactionTypeSpinnerData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
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
	private void loadTransactionTypeSpinnerData() {
		// Spinner Drop down elements
		List<String> transactionTypeNames = new ArrayList<String>();
		
		// get all the default transaction types
		List<String> defaultTransactionTypeData = ExpenseUtility.getTransactionTypeSpinnerDefault();
		// add the defaults to the list
		for (String transTypeName : defaultTransactionTypeData){
			transactionTypeNames.add(transTypeName);
		}
		
		// then add the rest of the data
		List<TransactionType> transactionTypeAllDetails = transactionTypeDao
				.queryForAll();
		String transType = null;
		for (TransactionType transactionType : transactionTypeAllDetails) {
			transType = transactionType.getTransactionType().toString();
			if (transType != null || !transType.equalsIgnoreCase("")) {
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
}
