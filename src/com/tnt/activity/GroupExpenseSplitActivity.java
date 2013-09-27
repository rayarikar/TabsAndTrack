package com.tnt.activity;

import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.R.layout;
import com.tnt.R.menu;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Contact;
import com.tnt.entity.Transaction;
import com.tnt.entity.TransactionType;
import com.tnt.utility.ExpenseUtility;
import com.tnt.utility.Validation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GroupExpenseSplitActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	String callingActivity;
	private RuntimeExceptionDao<Contact,Integer> contactDao;
	private static int NUMBER_INPUT_TYPE = 2;
	private static int ALLOW_DECIMAL_NUMBERS =  8192;
	private static int WIDTH_AMOUNT_BOX = 200;
	private static int HEIGHT_AMOUNT_BOX = 50;
	private static int SERIAL_NUMBER = 1;
	Intent groupExpenseIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_expense_split);
		try{
			contactDao = getHelper().getContactDao();
		} catch(Exception e){
			e.printStackTrace();
		}
		// get the data from intent
		groupExpenseIntent = getIntent();
		// Render the view
		addListToView();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.group_expenses_split, menu);
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


	public void onAddContactClick(View view){
		// Fetch the type name
		EditText editText = (EditText)findViewById(R.id.groupExpenseSplitContactName);
		String contactName = editText.getText().toString();

		// clear the text box
		editText.setText("");

		// checks if the contact exits or not
		if(Validation.isInputBlank(contactName))
		{
			Toast blankContactNameToast = Toast.makeText(this, EditContactActivity.BLANK_CONTACT_NAME_ERR, Toast.LENGTH_LONG);
			blankContactNameToast.show();
		}
		else
		{
			if(!isContactNameDuplicate(contactName))
			{
				saveContact(contactName);
				// Render the view
				addListToView();
			}
			else
			{
				Toast duplicateContactNameToast = Toast.makeText(this, EditContactActivity.DUPLICATE_CONTACT_NAME_ERR, Toast.LENGTH_LONG);
				duplicateContactNameToast.show();
			}
		}
	}

	// checks for the duplicate contacts
	public boolean isContactNameDuplicate(String contactName)
	{
		List<Contact> contacts = getAllContacts();

		for(Contact contact: contacts)
		{
			if(contact.getContactName().equalsIgnoreCase(contactName))
				return true;
		}

		return false;
	}

	/**
	 * This method persists the contact name into the database
	 * @param contactName
	 */
	private void saveContact(String contactName){
		Contact contactObj = new Contact();
		contactObj.setContactName(contactName);

		// persist into database
		contactDao.create(contactObj);
	}


	private void addListToView()
	{
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.groupExpenseSplitContactDisplay);

		List<Contact> contacts = getAllContacts();
		String splitType = groupExpenseIntent.getStringExtra("RadioButtonValue");
		Transaction transObj = (Transaction) groupExpenseIntent.getExtras().get("TransactionObject");        
		View view = null;

		// based on the type of split choice- equally or unequally, the list display is rendered 
		if (getString(R.string.splitEqually).equals(splitType)){
			view = generateListViewForEqualSplit(contacts, transObj.getTransactionTotalAmount());
		}
		else if (getString(R.string.splitUnequally).equals(splitType)){
			view = generateListViewForUnequalSplit(contacts);        	
		}

		// Clear previous data
		linearLayout.removeAllViews();

		// Add new data
		linearLayout.addView(view);
	}

	private List<Contact> getAllContacts()
	{
		List<Contact> contacts = contactDao.queryForAll();
		return contacts;
	}

	// generates the view when split selected is equally
	private View generateListViewForEqualSplit(List<Contact> contacts, double transactionAmount)
	{
		// get the number of people involved
		double noOfPeopleInvolved = contacts.size();
		
		// Create the table layout
		TableLayout tableLayout = new TableLayout(this);
		for(Contact contact : contacts)
		{	        	
			// Create a table row
			TableRow tableRow = new TableRow(this);

			// set the counter number followed by 2 blank spaces
			TextView counterView = new TextView(this);
			counterView.setText(SERIAL_NUMBER + ".  ");

			// Create textview that has the contact name
			TextView textView = new TextView(this);
			textView.setText(contact.getContactName() + "  ");

			EditText amountBox = new EditText(this);
			amountBox.setInputType(NUMBER_INPUT_TYPE);
			amountBox.setInputType(ALLOW_DECIMAL_NUMBERS);
			amountBox.setFocusable(false);
			amountBox.setText(transactionAmount/noOfPeopleInvolved + "");
			amountBox.setWidth(ExpenseUtility.getIntInPixels(this, WIDTH_AMOUNT_BOX));
			amountBox.setHeight(ExpenseUtility.getIntInPixels(this, HEIGHT_AMOUNT_BOX));

			//	            // Create a checkbox for the type entry
			//	            CheckBox checkBox = new CheckBox(this);
			//	            // Bind the checkbox with the id from the db
			//	            checkBox.setId(contact.getContactId());

			// Add the 2 views into the table row
			tableRow.addView(counterView);
			tableRow.addView(textView);
			tableRow.addView(amountBox);
			SERIAL_NUMBER++;

			// Add the table row to the table
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)); 
		}
		return tableLayout;
	}

	// generates view when the split selected is unequally
	private View generateListViewForUnequalSplit(List<Contact> contacts)
	{
		// Create the table layout
		TableLayout tableLayout = new TableLayout(this);
		for(Contact contact : contacts)
		{	        	
			// Create a table row
			TableRow tableRow = new TableRow(this);

			// set the counter number followed by 2 blank spaces
			TextView counterView = new TextView(this);
			counterView.setText(SERIAL_NUMBER + ".  ");

			// Create textview that has the contact name
			TextView textView = new TextView(this);
			textView.setText(contact.getContactName() + "  ");

			EditText amountBox = new EditText(this);
			amountBox.setInputType(NUMBER_INPUT_TYPE);
			amountBox.setInputType(ALLOW_DECIMAL_NUMBERS);
			amountBox.setWidth(ExpenseUtility.getIntInPixels(this, WIDTH_AMOUNT_BOX));
			amountBox.setHeight(ExpenseUtility.getIntInPixels(this, HEIGHT_AMOUNT_BOX));

			//	            // Create a checkbox for the type entry
			//	            CheckBox checkBox = new CheckBox(this);
			//	            // Bind the checkbox with the id from the db
			//	            checkBox.setId(contact.getContactId());

			// Add the 2 views into the table row
			tableRow.addView(counterView);
			tableRow.addView(textView);
			tableRow.addView(amountBox);
			SERIAL_NUMBER++;

			// Add the table row to the table
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
		}

		return tableLayout;
	}

	public void onDoneClick(View view){
		String splitType = groupExpenseIntent.getStringExtra("RadioButtonValue");

	}


}
