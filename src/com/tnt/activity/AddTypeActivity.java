package com.tnt.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.TransactionType;

/**
 * Whenever this class is called following conditions should hold true:
 * 1:	This class should only be involed via intent
 * 2:	There should always be one extras in Intent with key addTypeActivity.
 * @author Rohan / Ameya
 *
 */
public class AddTypeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText typeName;
	private TextView displayTypesTv;
	private RuntimeExceptionDao<TransactionType,Integer> transactionTypeDao;
	Intent activityTypeIntent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			transactionTypeDao = getHelper().getTransactionTypeDao();
		} catch(Exception e){
			e.printStackTrace();
		}
		setContentView(R.layout.activity_add_type);
		typeName = ((EditText) findViewById(R.id.typeName));
		displayTypesTv = ((TextView) findViewById(R.id.showTypes));
		activityTypeIntent = getIntent();
		displayExistingTransactionTypes();
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
	 * This method displays the existing transaction types in a text view
	 * @author Rohan / Ameya
	 */
	private void displayExistingTransactionTypes(){

		List<TransactionType> existingTypes = transactionTypeDao.queryForAll();
		StringBuilder addToTextView = new StringBuilder();
		addToTextView.append("Existing Transaction Types are : ");
		if (existingTypes != null){
			for (TransactionType existingType : existingTypes){
				addToTextView.append("\n").append(existingType.getTransactionType());
			}
		}
		displayTypesTv.setText(addToTextView);
	}

	/** 
	 * This methods add the activity into the database
	 * @param view
	 * @author Rohan / Ameya
	 */
	public void onDoneClick(View view){

		boolean isAddMoreTypesChecked = ((CheckBox) findViewById(R.id.addMoreTypes)).isChecked();
		String activityName = activityTypeIntent.getStringExtra("activityName");	
		persistIntoTransactionTypeDb();

		// redirect based on the calling activity
		if (isAddMoreTypesChecked){
			finish();
			Intent redirectToAddTypeActivity = new Intent(this, AddTypeActivity.class);
			redirectToAddTypeActivity.putExtra("activityName", activityName);
			startActivity(redirectToAddTypeActivity);
		}
		else{
			if (activityName.equalsIgnoreCase("PersonalExpenseAddTypeActivity")){
				Intent redirectToPersonalExpensesActivity = new Intent(this, PersonalExpenseActivity.class);
				finish();
				startActivity(redirectToPersonalExpensesActivity);
			}
		}
	}

	/**
	 * This method persists the values into TranactionType database
	 * @author Rohan / Ameya
	 */
	private void persistIntoTransactionTypeDb(){
		String transactionName = typeName.getText().toString();	

		// if add more is checked then commit in database and open the same activity again
		// else commit in database and redirect to 

		TransactionType transactionTypeObj = new TransactionType();
		transactionTypeObj.setTransactionType(transactionName);

		// persist into database
		transactionTypeDao.create(transactionTypeObj);
	}

}
