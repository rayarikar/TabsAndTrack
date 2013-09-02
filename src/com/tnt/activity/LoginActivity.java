package com.tnt.activity;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.R;
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

		// check if remember me is clicked. If yes redirect to home activity else load this activity
		if (isRemeberMeChecked(this)){
			Intent redirectToHomeIntent = new Intent(this, HomeActivity.class);
			startActivity(redirectToHomeIntent);
		}

		// if remember me is not checked
		setContentView(R.layout.activity_login);		
	}

	/**
	 * This method returns the value selected by user.
	 * The default return type is false
	 * @param context
	 * @return
	 * @author Rohan / Ameya
	 */
	private boolean isRemeberMeChecked(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isRememberMeChecked = preferences.getBoolean("rememberMe", false);
		return isRememberMeChecked;
	}

	/**
	 * This method is used to put in the user input remember me
	 * @param rememberMeChecked
	 * @author Rohan / Ameya
	 */
	private void setRemeberMeInSharedPreferences(boolean rememberMeChecked){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor sharedPrefEditor = preferences.edit();
		sharedPrefEditor.putBoolean("rememberMe", rememberMeChecked);
		sharedPrefEditor.commit();
	}


	/**
	 * On click of sign up the user should be redirected to sign up activity
	 * @param v
	 * @author Rohan / Ameya
	 */
	public void onSignUp(View v){
		Intent signUpIntent = new Intent(this, SignUpActivity.class);
		startActivity(signUpIntent);
	}

	/**
	 * this method authenticates the user. If the user is authenticated he is redirected to the home screen
	 * If the user checks the remember me flag, then the flag is stored in the shared preferences and the user is 
	 * always redirected to the home activity. 
	 * @param v
	 * @throws SQLException
	 * @author Rohan / Ameya
	 */
	public void onSignInClick(View v) throws SQLException{
		// get all the details of login activity
		String enteredUserName = ((EditText) findViewById(R.id.userName)).getText().toString();
		String enteredPassword = ((EditText) findViewById(R.id.password)).getText().toString();
		boolean isRemeberMeChecked = ((CheckBox) findViewById(R.id.rememberMe)).isChecked();

		List<UserDetails> userDetailsList = userDetailsDao.queryForAll();
		boolean isLoginSuccess = false;

		// loop over the list to get the user name and stores the remember me selection in a shared preference
		for (UserDetails user : userDetailsList){
			if (enteredUserName.equalsIgnoreCase(user.getUsername()) && enteredPassword.equals(user.getPassword()))
			{
				isLoginSuccess = true;
				// set the user input of remember me
				setRemeberMeInSharedPreferences(isRemeberMeChecked);
				finish();
				Intent redirectToHome = new Intent(this, HomeActivity.class);
				redirectToHome.putExtra("com.tnt.activity.userObj", user);
				startActivity(redirectToHome);
				break;
			}
		}
		if (!isLoginSuccess){
			Toast invalidLoginToast = Toast.makeText(this, "Invalid username or password. Please try again", Toast.LENGTH_LONG);
			invalidLoginToast.show();		
		}		
	}

	/**
	 * Once the user logs out, the shared preference will get reset to false.
	 * This makes sure that the user wont be redirected to home screen when he logs in again
	 * @param context
	 * @author Rohan / Ameya
	 */
	public void logout(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sharedPrefEditor = preferences.edit();
		sharedPrefEditor.putBoolean("rememberMe", false);
		sharedPrefEditor.commit();
	}
}
