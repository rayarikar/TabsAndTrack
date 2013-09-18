package com.tnt.activity;

import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.R.layout;
import com.tnt.R.menu;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Contact;
import com.tnt.entity.TransactionType;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GroupExpenseSplitActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	String callingActivity;
	private RuntimeExceptionDao<Contact,Integer> contactDao;
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

		// check here for null or repetitive data and then clear it

		// clear the text box
		editText.setText("");


		// TODO - Ameya just check for the condition commented below
		// Save it in db and add show the view if its not empty or not null
		saveType(contactName);
		// Render the view
		addListToView();
	}

	/**
	 * This method persists the contact name into the database
	 * @param contactName
	 */
	private void saveType(String contactName){
		Contact contactObj = new Contact();
		contactObj.setContactName(contactName);

		// persist into database
		contactDao.create(contactObj);
	}
	

	private void addListToView()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.groupExpenseSplitContactDisplay);

        List<Contact> contacts = getContacts();
        View view = generateListViewForTypes(contacts);

        // Clear previous data
        linearLayout.removeAllViews();

        // Add new data
        linearLayout.addView(view);
    }
	
	 private List<Contact> getContacts()
	    {
	        List<Contact> contacts = contactDao.queryForAll();
	        return contacts;
	    }

	    private View generateListViewForTypes(List<Contact> contacts)
	    {
	        // Create the table layout
	        TableLayout tableLayout = new TableLayout(this);
	        for(Contact contact : contacts)
	        {
	            // Create a table row
	            TableRow tableRow = new TableRow(this);

	            // Create textview that has the contact name
	            TextView textView = new TextView(this);
	            textView.setText(contact.getContactName());

	            // Create a checkbox for the type entry
	            CheckBox checkBox = new CheckBox(this);
	            // Bind the checkbox with the id from the db
	            checkBox.setId(contact.getContactId());

	            // Add the 2 views into the table row
	            tableRow.addView(textView);
	            tableRow.addView(checkBox);

	            // Add the table row to the table
	            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
	        }

	        return tableLayout;
	    }


}
