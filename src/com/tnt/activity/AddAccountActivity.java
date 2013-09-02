package com.tnt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.Account;
import com.tnt.entity.UserDetails;

/**
 * This class adds the user details and account into the database using DAOs.
 * @author Rohan
 */
public class AddAccountActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	UserDetails userDetails;
	Account account = new Account();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_account);

		// gets the userDetails object
		userDetails = (UserDetails) getIntent().getExtras().get("userInfoObj");

		Log.d("UD", userDetails.toString());
	}

	public void onCancelClick(View view) {
		// on cancel click
		finish();
		
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivity(loginIntent);

	}

	/**
	 * @author Rohan / Ameya
	 * @param view
	 */
	public void onDoneClick(View view) {

		String accountName = ((EditText) findViewById(R.id.accountName))
				.getText().toString();
		double accountLimit = Double
				.parseDouble(((EditText) findViewById(R.id.accountLimit))
						.getText().toString());
		account.setAccountName(accountName);
		account.setAccountLimit(accountLimit);
		persistIntoUserDetails();
		persistIntoAccount();

		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivity(loginIntent);

	}

	/**
	 * persists all the details into UserDetails table
	 * @author Rohan / Ameya
	 * 
	 */
	private void persistIntoUserDetails() {
		try {
			// gets all DAOs
			RuntimeExceptionDao<UserDetails, Integer> userDetailsDao = getHelper()
					.getUserDetailsDao();
			// persist
			userDetailsDao.create(userDetails);
		} catch (Exception e) {
			Log.e("Error Inserting Data", "Error Inserting Data");
		}
	}

	/**
	 * persists all the details into Account table 
	 * @author Rohan / Ameya
	 * 
	 */
	private void persistIntoAccount() {
		try {
			// gets all DAOs
			RuntimeExceptionDao<Account, Integer> accountDao = getHelper()
					.getAccountDao();
			// persist
			accountDao.create(account);
			// check for more

		} catch (Exception e) {
			Log.e("Error Inserting Data", "Error Inserting Data");
		}
	}

}
