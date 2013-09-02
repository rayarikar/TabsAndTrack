package com.tnt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tnt.R;
import com.tnt.entity.UserDetails;

public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}
	
	public void onNextClick(View v){
		UserDetails userInfo = new UserDetails();
		String firstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
		String lastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
		String emailId = ((EditText) findViewById(R.id.emailId)).getText().toString();
		String username = ((EditText) findViewById(R.id.username)).getText().toString();
		String password = ((EditText) findViewById(R.id.password)).getText().toString();
		String retypePassword = ((EditText) findViewById(R.id.retypePassword)).getText().toString();
		
		if (password.equals(retypePassword)){
			userInfo.setFirstName(firstName);
			userInfo.setLastName(lastName);
			userInfo.setEmailId(emailId);
			userInfo.setUsername(username);
			userInfo.setPassword(password);
			

			Intent signUpIntent = new Intent(this, AddAccountActivity.class);
			signUpIntent.putExtra("userInfoObj",  userInfo);
			startActivity(signUpIntent);
		}
		
		
	}
	
	public void onCancelNext(View v){
		Intent redirectToLogin = new Intent(this, LoginActivity.class);
		startActivity(redirectToLogin);
	}

}
