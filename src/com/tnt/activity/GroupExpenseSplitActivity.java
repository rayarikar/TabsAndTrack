package com.tnt.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Contact;
import com.tnt.entity.GroupTransaction;
import com.tnt.entity.Transaction;
import com.tnt.utility.ExpenseUtility;
import com.tnt.utility.Validation;

public class GroupExpenseSplitActivity extends
OrmLiteBaseActivity<DatabaseHelper> {

	String callingActivity;
	private RuntimeExceptionDao<Contact, Integer> contactDao;
	private RuntimeExceptionDao<Transaction, Integer> transactionDao;
	private RuntimeExceptionDao<GroupTransaction, Integer> groupTransactionDao;

	// map to store all the checked contacts information
	private HashMap<Integer, Boolean> checkedContactsMap = new HashMap<Integer, Boolean>();

	private static int NUMBER_INPUT_TYPE = 2;
	private static int ALLOW_DECIMAL_NUMBERS = 8192;
	private static int WIDTH_AMOUNT_BOX = 200;
	private static int HEIGHT_AMOUNT_BOX = 50;
	
	private double transactionAmount = 0.0;
	private List<Contact> contacts = null;
	private Transaction transObj = null;
	private LinearLayout linearLayout = null;
	String splitType = null;

	Intent groupExpenseIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_expense_split);
		try {
			contactDao = getHelper().getContactDao();
			transactionDao = getHelper().getTransactionDao();
			groupTransactionDao = getHelper().getGroupTransactionDao();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get the data from intent
		groupExpenseIntent = getIntent();

		// fetching values from intent
		splitType = groupExpenseIntent
				.getStringExtra("RadioButtonValue");
		transObj = (Transaction) groupExpenseIntent.getExtras()
				.get("TransactionObject");
		transactionAmount = transObj.getTransactionTotalAmount();

		// populate the contacts list
		contacts = getAllContacts();
		// populate the checked contacts map
		populateContactsMap();
		// get the linear layout which is the container
		linearLayout = (LinearLayout) findViewById(R.id.groupExpenseSplitContactDisplay);
		// Render the view
		renderView();

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

	public void onAddContactClick(View view) {
		// Fetch the type name
		EditText editText = (EditText) findViewById(R.id.groupExpenseSplitContactName);
		String contactName = editText.getText().toString();

		// clear the text box
		editText.setText("");

		// checks if the contact exits or not
		if (Validation.isInputBlank(contactName)) {
			Toast blankContactNameToast = Toast.makeText(this,
					EditContactActivity.BLANK_CONTACT_NAME_ERR,
					Toast.LENGTH_LONG);
			blankContactNameToast.show();
		} else {
			// if the contact name is not blank then check if its a duplicate contact
			if (!isContactNameDuplicate(contactName)) {
				saveContact(contactName);
				// update the contacts list
				contacts = getAllContacts();
				// update the map
				populateContactsMap();
				// re-render the view
				renderView();
				
			} else {
				Toast duplicateContactNameToast = Toast.makeText(this,
						EditContactActivity.DUPLICATE_CONTACT_NAME_ERR,
						Toast.LENGTH_LONG);
				duplicateContactNameToast.show();
			}
		}
	}

	/**
	 * checks for the duplicate contacts
	 * @param contactName
	 * @return
	 */
	public boolean isContactNameDuplicate(String contactName) {
		List<Contact> contacts = getAllContacts();
		for (Contact contact : contacts) {
			if (contact.getContactName().equalsIgnoreCase(contactName))
				return true;
		}
		return false;
	}

	/**
	 * This method persists the contact name into the database
	 * 
	 * @param contactName
	 */
	private void saveContact(String contactName) {
		Contact contactObj = new Contact();
		contactObj.setContactName(contactName);
		// persist into database
		contactDao.create(contactObj);
	}

	/**
	 * based on the type of split choice- equally or unequally, the list display is rendered
	 */
	private void renderView() {
		if (getString(R.string.splitEqually).equals(splitType)) {
			generateListViewForEqualSplit();
		} else if (getString(R.string.splitUnequally).equals(splitType)) {
			generateListViewForUnequalSplit();
		}

	}

	private List<Contact> getAllContacts() {
		List<Contact> contacts = contactDao.queryForAll();
		return contacts;
	}

	private int getCheckedContactCount()
	{
		Collection<Boolean> checkedList = checkedContactsMap.values();

		int count = 0;
		for(Boolean isChecked : checkedList)
			if(isChecked)
				count++;

		return count;
	}

	// generates the view when split selected is equally
	private void generateListViewForEqualSplit() {

		// Clear previous data
		linearLayout.removeAllViews();

		int noOfPeopleInvolved = getCheckedContactCount();

		// Create the table layout
		TableLayout tableLayout = new TableLayout(this);
		for (Contact contact : contacts) {
			// Create a table row
			TableRow tableRow = new TableRow(this);

			// Create a checkbox for the type entry and bind with contact id
			CheckBox contactSelectedCheckBox = new CheckBox(this);
			int contactId = contact.getContactId();
			contactSelectedCheckBox.setId(contactId);

			boolean isChecked = checkedContactsMap.get(contactId);
			contactSelectedCheckBox.setChecked(isChecked);
			contactSelectedCheckBox.setOnClickListener(customCheckBoxClickListenerForEqualSplit);

			// Create textview that has the contact name
			TextView contactNameTextView = new TextView(this);
			contactNameTextView.setText(contact.getContactName() + "  ");

			// Edit text to display the amount split equally
			EditText amountBox = new EditText(this);
			amountBox.setInputType(NUMBER_INPUT_TYPE);
			amountBox.setInputType(ALLOW_DECIMAL_NUMBERS);
			amountBox.setFocusable(false);

			double finalAmount = 0;

			if(noOfPeopleInvolved == 0 || !isChecked)
				finalAmount = 0;
			else
				finalAmount = transactionAmount / noOfPeopleInvolved;

			amountBox.setText(Math.round(finalAmount*100.0)/100.0 + "");
			amountBox.setWidth(ExpenseUtility.getIntInPixels(this,
					WIDTH_AMOUNT_BOX));
			amountBox.setHeight(ExpenseUtility.getIntInPixels(this,
					HEIGHT_AMOUNT_BOX));

			// Add the checkbox, name and amount into the table row
			tableRow.addView(contactSelectedCheckBox);
			tableRow.addView(contactNameTextView);
			tableRow.addView(amountBox);

			// Add the table row to the table
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));
		}
		// Add new data
		linearLayout.addView(tableLayout);
	}
	
	/**
	 * This on click listener is for Equal splits
	 * this click listener is called every time any action is done on the check-box 
	 * If the check box is clicked we set the value of that contact in the map to true
	 * else false.
	 */
	public OnClickListener customCheckBoxClickListenerForEqualSplit = new OnClickListener()
	{
		public void onClick(final View view)
		{
			CheckBox currCheckBox = (CheckBox) view;
			if (currCheckBox.isChecked())
				checkedContactsMap.put(currCheckBox.getId(), true);
			else
				checkedContactsMap.put(currCheckBox.getId(), false);

			//re rendering the view
			generateListViewForEqualSplit();
		}
	};

	// generates view when the split selected is unequally
	private void generateListViewForUnequalSplit() {
		// Clear previous data
		linearLayout.removeAllViews();

		// Create the table layout
		TableLayout tableLayout = new TableLayout(this);
		for (Contact contact : contacts) {
			// Create a table row
			TableRow tableRow = new TableRow(this);

			// Create a checkbox for the type entry and bind with contact id
			CheckBox contactSelectedCheckBox = new CheckBox(this);
			int contactId = contact.getContactId();
			contactSelectedCheckBox.setId(contactId);

			boolean isChecked = checkedContactsMap.get(contactId);
			contactSelectedCheckBox.setChecked(isChecked);
			contactSelectedCheckBox.setOnClickListener(customCheckBoxClickListenerForUnequalSplit);

			// Create textview that has the contact name
			TextView contactNameTextView = new TextView(this);
			contactNameTextView.setText(contact.getContactName() + "  ");

			// Edit text to display the amount split equally
			EditText amountBox = new EditText(this);
			amountBox.setInputType(NUMBER_INPUT_TYPE);
			amountBox.setInputType(ALLOW_DECIMAL_NUMBERS);
			amountBox.setWidth(ExpenseUtility.getIntInPixels(this,
					WIDTH_AMOUNT_BOX));
			amountBox.setHeight(ExpenseUtility.getIntInPixels(this,
					HEIGHT_AMOUNT_BOX));

			// if the contact is checked enable that checkbox, else disable it
			if(!isChecked)
				amountBox.setEnabled(false);
			else
				amountBox.setEnabled(true);

			// Add the checkbox, name and amount into the table row
			tableRow.addView(contactSelectedCheckBox);
			tableRow.addView(contactNameTextView);
			tableRow.addView(amountBox);

			// Add the table row to the table
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));
		}
		// Add new data
		linearLayout.addView(tableLayout);
	}
	
	/**
	 * This onClick listener is for Unequal splits
	 * this click listener is called every time any action is done on the check-box 
	 * If the check box is clicked we set the value of that contact in the map to true
	 * else false.
	 */
	public OnClickListener customCheckBoxClickListenerForUnequalSplit = new OnClickListener()
	{
		public void onClick(final View view)
		{
			CheckBox currCheckBox = (CheckBox) view;
			if (currCheckBox.isChecked())
				checkedContactsMap.put(currCheckBox.getId(), true);
			else
				checkedContactsMap.put(currCheckBox.getId(), false);

			//re rendering the view
			generateListViewForUnequalSplit();
		}
	};

	// populates the contact details in map
	public void populateContactsMap() {
		List<Contact> allContacts = getAllContacts();
		for (Contact contact : allContacts) {
			checkedContactsMap.put(contact.getContactId(), false);
		}
	}

	public void onDoneClick(View view) {
		// on done click the data for Transaction table should get persisted
		
		// Validate here -> If no contacts are selected then give a Toast. Move ahead only if a contact is selected
		
		if (transObj != null)
		{
			// persist into Transaction
			transactionDao.create(transObj);
			// now persist into groupTransaction for every contact
			
			
		}
		
	}

}
