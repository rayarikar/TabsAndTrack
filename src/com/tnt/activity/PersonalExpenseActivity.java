package com.tnt.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.tnt.R;

public class PersonalExpenseActivity extends Activity {

	private EditText transactionName;
	private Spinner transactionType;
	private DatePicker transactionDate;
	private EditText trasactionAmount;
	private Spinner accountName;
	
	final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_expense);
		transactionName = ((EditText) findViewById(R.id.transactionName));
		transactionType = ((Spinner) findViewById(R.id.transactionType));
		transactionDate = ((DatePicker) findViewById(R.id.datePicker));
		trasactionAmount = ((EditText) findViewById(R.id.amount));
		accountName = ((Spinner) findViewById(R.id.accountType));
	}
	
	/**
	 * resets the entire layout
	 * @author Rohan / Ameya
	 */
	public void onResetClick(View view){
		transactionName.setText("");
		transactionType.setSelection(0);
		transactionDate.init(year, month, day, null);
		trasactionAmount.setText("");
		accountName.setSelection(0);
	}
	
	/**
	 * persist all the details on click of done button
	 * @author Rohan / Ameya
	 */
	public void onDoneClick(){
	}
	
	/**
	 * this method should open a new activity is used to add a transaction type
	 * @author Rohan / Ameya
	 */
	public void onAddTypeClick(){
	 Intent redirectToAddType = new Intent(this, AddTypeActivity.class);
	 startActivity(redirectToAddType);
	}
}
