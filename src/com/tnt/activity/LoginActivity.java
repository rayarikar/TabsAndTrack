package com.tnt.activity;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.tabsandtrack.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.UserDetails;

public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper>{

	RuntimeExceptionDao<UserDetails, Integer> userDetailsDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		userDetailsDao = getHelper().getUserDetailsDao();
		} catch(Exception e){
			e.printStackTrace();
		}
		setContentView(R.layout.activity_login);
	}
	
	public void onSignUp(View v){
		Intent signUpIntent = new Intent(this, SignUpActivity.class);
		startActivity(signUpIntent);
	}
	
	public void onSignInClick(View v) throws SQLException{
		// get all the details of login activity
		String enteredUserName = ((EditText) findViewById(R.id.userName)).getText().toString();
		String enteredPassword = ((EditText) findViewById(R.id.password)).getText().toString();
		boolean isRemeberMeChecked = ((CheckBox) findViewById(R.id.rememberMe)).isChecked();		
				
		List<UserDetails> userDetailsList = userDetailsDao.queryForAll();
		boolean isLoginSuccess = false;
		
		// loop over the list to get the user name
		for (UserDetails user : userDetailsList){
			if (enteredUserName.equalsIgnoreCase(user.getUsername()) && enteredPassword.equals(user.getPassword()))
			{
				isLoginSuccess = true;
				break;
			}
			else{		
				Intent loadSameActivity = new Intent();
				finish();
				startActivity(loadSameActivity);
			}
		}
		
		// if user has checked the remember me checkbox
		if (isRemeberMeChecked){
			
		}

				
	}
}
