package com.tnt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Contact;

import java.util.List;

/**
 * Whenever this class is called following conditions should hold true:
 * 1:	This class should only be involed via intent
 * 2:	There should always be one extras in Intent with key addFriendActivity.
 *
 * @author Rohan / Ameya
 */
public class AddContactActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private EditText contactName;
    private TextView displayContactsTv;
    private RuntimeExceptionDao<Contact, Integer> contactDao;
    Intent activityTypeIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            contactDao = getHelper().getContactDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_add_contact);
        contactName = ((EditText) findViewById(R.id.contactName));
        displayContactsTv = ((TextView) findViewById(R.id.showContacts));
        activityTypeIntent = getIntent();
        displayExistingContacts();
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
     * This method displays the existing contacts in a text view
     *
     * @author Rohan / Ameya
     */
    private void displayExistingContacts() {

        List<Contact> contacts = contactDao.queryForAll();
        StringBuilder addToTextView = new StringBuilder();
        addToTextView.append("Existing Contacts are : ");
        if (contacts != null) {
            for (Contact contact : contacts) {
                addToTextView.append("\n").append(contact.getContactName());
            }
        }
        displayContactsTv.setText(addToTextView);
    }

    /**
     * This methods add the contact into the database
     *
     * @param view
     * @author Rohan / Ameya
     */
    public void onDoneClick(View view) {

        boolean isAddMoreContactsChecked = ((CheckBox) findViewById(R.id.addMoreContacts)).isChecked();
        persistIntoTransactionTypeDb();

        // redirect based on the calling activity
        if (isAddMoreContactsChecked) {
            finish();
            Intent redirectToAddTypeActivity = new Intent(this, AddContactActivity.class);
            startActivity(redirectToAddTypeActivity);
        } else {
                Intent redirectToHomeIntent = new Intent(this, HomeActivity.class);
                finish();
                startActivity(redirectToHomeIntent);
        }
    }

    /**
     * This method persists the values into Contact table in database
     *
     * @author Rohan / Ameya
     */
    private void persistIntoTransactionTypeDb() {
        String contactNameStr = contactName.getText().toString();

        // if add more is checked then commit in database and open the same activity again
        // else commit in database and redirect to

        Contact contact = new Contact();
        contact.setContactName(contactNameStr);

        // persist into database
        contactDao.create(contact);
    }

}
