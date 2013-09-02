package com.tnt.activity;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
import com.tnt.R.layout;
import com.tnt.R.menu;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.TransactionType;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddTypeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText typeName;
	private RuntimeExceptionDao<TransactionType,Integer> transactionTypeDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			transactionTypeDao = getHelper().getTransactionTypeDao();
		} catch(Exception e){
			e.printStackTrace();
		}
		setContentView(R.layout.activity_add_type);
		typeName = ((EditText) findViewById(R.id.transactionType));
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
	 * This methods add the activity into the database
	 * @param view
	 * @author Rohan / Ameya
	 */
	public void onDoneClick(View view){
		String transactionName = typeName.getText().toString();
	}

}
