package com.tnt.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
	private RuntimeExceptionDao<Account,Integer> accountDao;
	Intent userDetailsIntent;
	Intent accountIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			accountDao = getHelper().getAccountDao();
		} catch(Exception e){
			e.printStackTrace();
		}
		setContentView(R.layout.activity_add_account);

		// gets the userDetails object
		userDetails = (UserDetails) getIntent().getExtras().get("userInfoObj");
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

		// persist user details into the db
		if (userDetails.getUsername() != null)
			persistIntoUserDetails();

		Account account = new Account();
		String accountName = ((EditText) findViewById(R.id.accountName))
				.getText().toString();
		double accountLimit = Double
				.parseDouble(((EditText) findViewById(R.id.accountLimit))
						.getText().toString());
		account.setAccountName(accountName);
		account.setAccountLimit(accountLimit);

		boolean isAddMoreAccountsChecked = ((CheckBox) findViewById(R.id.addMoreAccounts)).isChecked();	
		boolean isDuplicateAccount = false;

		// check if there are any accounts which have the same name. If they have same name then give out a toast
		List<Account> accounts = accountDao.queryForAll();
		if (accounts != null){
			for (Account acc : accounts){
				if (account.getAccountName().equalsIgnoreCase(acc.getAccountName())){
					isDuplicateAccount = true;
					break;
				}
			}
		}


		// if it is a duplicate account then give a toast 
		// else persists the account details and check for add more accounts checked
		// if the add more accounts is not checked then redirect to home
		if (isDuplicateAccount){
			Toast duplicateAccToast = Toast.makeText(this, "Account with same name already exists. Please try again", Toast.LENGTH_LONG);
			duplicateAccToast.show();			
		} else{
			// persists account details into db
			persistIntoAccount(account);
			if ( !isAddMoreAccountsChecked){
				Intent homeIntent = new Intent(this, HomeActivity.class);
				startActivity(homeIntent);	
			}
		}

		// pass the user details intent again and call this activity again
		if (isAddMoreAccountsChecked){
			Intent addAccountsIntent = new Intent(this, AddAccountActivity.class);
			addAccountsIntent.putExtra("userInfoObj", new UserDetails());
			finish();
			startActivity(addAccountsIntent);
		}

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
			// persist the user details
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
	private void persistIntoAccount(Account account) {
		try {
			// gets all DAOs
			RuntimeExceptionDao<Account, Integer> accountDao = getHelper()
					.getAccountDao();
			// persist the account
			accountDao.create(account);
		} catch (Exception e) {
			Log.e("Error Inserting Data", "Error Inserting Data");
		}
	}

}
