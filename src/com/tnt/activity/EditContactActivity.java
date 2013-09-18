package com.tnt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Contact;
import android.content.Intent;
import com.tnt.utility.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO There are many cases where finish() is not called.  Ameya, make sure the finish() method is called.
 * 		Also please comment where-ever necessary along with the method comments. This is to make sure that 
 * 		any of us who reads your code will understand it
 * @author Ameya
 *
 */
public class EditContactActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private RuntimeExceptionDao<Contact,Integer> contactDao;
    private static final String DUPLICATE_CONTACT_NAME_ERR = "This name already exists! Please enter some other name.";
    private static final String BLANK_CONTACT_NAME_ERR = "Contact Name cannot be blank! Please enter some name.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            contactDao = getHelper().getContactDao();
        } catch(Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_edit_contact);

        // Render the view
        addListToView();
    }



    public void onAddContactClick(View view)
    {
    	// Fetch the contact name
    	EditText editText = (EditText)findViewById(R.id.editContactName);
    	String contactName = editText.getText().toString();

    	// clear the text box
    	editText.setText("");

        if(Validation.isInputBlank(contactName))
        {
            Toast blankContactNameToast = Toast.makeText(this, BLANK_CONTACT_NAME_ERR, Toast.LENGTH_LONG);
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
                Toast duplicateContactNameToast = Toast.makeText(this, DUPLICATE_CONTACT_NAME_ERR, Toast.LENGTH_LONG);
                duplicateContactNameToast.show();
            }
        }
    }

    private boolean isContactNameDuplicate(String contactName)
    {
        List<Contact> contacts = getContacts();

        for(Contact contact: contacts)
        {
            if(contact.getContactName().equalsIgnoreCase(contactName))
                return true;
        }

        return false;
    }

    public void onDeleteClick(View view)
    {
        //  Fetch the list of ids of the checkboxes that are checked
        List<Integer> ids = getSelectedCheckboxIds();

        // Delete these ids
        deleteContacts(ids);

        // Render the view
        addListToView();
    }

    public void onDoneClick(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    private List<Integer> getSelectedCheckboxIds()
    {

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.editContacts);
        TableLayout tableLayout = (TableLayout) linearLayout.getChildAt(0);
        List<Integer> ids = new ArrayList<Integer>();

        int numOfRows = tableLayout.getChildCount();

        for(int i=0; i < numOfRows; ++i) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);

            CheckBox checkBox = (CheckBox) row.getChildAt(1);

            // If checkbox is checked add its id to list
            if(checkBox.isChecked())
                ids.add(checkBox.getId());
        }

        return ids;
    }

    private void deleteContacts(List<Integer> contactIds)
    {
        contactDao.deleteIds(contactIds);
    }


    private void saveContact(String contactName){
        Contact contactObj = new Contact();
        contactObj.setContactName(contactName);

        // persist into database
        contactDao.create(contactObj);
    }

    private void addListToView()
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.editContacts);

        List<Contact> contacts = getContacts();
        View view = generateListViewForContacts(contacts);

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

    private View generateListViewForContacts(List<Contact> contacts)
    {
        // Create the table layout
        TableLayout tableLayout = new TableLayout(this);
        for(Contact contact : contacts)
        {
            // Create a table row
            TableRow tableRow = new TableRow(this);

            // Create textview that has the transaction contact name
            TextView textView = new TextView(this);
            textView.setText(contact.getContactName());

            // Create a checkbox for the contact entry
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
