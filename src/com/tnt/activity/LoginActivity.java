package com.tnt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tabsandtrack.R;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void onSignUp(View v){

		Intent signUpIntent = new Intent(this, SignUpActivity.class);
		startActivity(signUpIntent);
	}
}
