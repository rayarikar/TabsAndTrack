package com.tnt.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.tabsandtrack.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.dboperation.UserDetailsDbOperations;
import com.tnt.entity.UserDetails;
import com.tnt.singleton.DbOperationsSingleton;

public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper>{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void onSignUp(View v){
		Intent signUpIntent = new Intent(this, SignUpActivity.class);
		startActivity(signUpIntent);
	}
	
	public void onSignInClick(View v){
		// get all the details of login activity
		String enteredUserName = ((EditText) findViewById(R.id.userName)).getText().toString();
		String enteredPassword = ((EditText) findViewById(R.id.password)).getText().toString();
		boolean isRemeberMeChecked = ((CheckBox) findViewById(R.id.rememberMe)).isChecked();		
		
		UserDetailsDbOperations userDetailsObj = DbOperationsSingleton.getUserDetailsDbOperationsInstance();
		List<UserDetails> userDetailsList = userDetailsObj.getAllUserDetails();
		boolean isLoginSuccess = false;
		
		// loop over the list to get the user name
		for (UserDetails user : userDetailsList){
			if (enteredUserName.equalsIgnoreCase(user.getUsername()) && enteredPassword.equals(user.getPassword()))
			{
				isLoginSuccess = true;
				break;
			}
		}
		
		// if user has checked the remember me checkbox
		if (isRemeberMeChecked){
			
		}

				
	}
}
