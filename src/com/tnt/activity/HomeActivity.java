package com.tnt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.tnt.R;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.UserDetails;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	UserDetails userDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
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

	public void onPersonalExpenseClick(View view) {
		Intent personalExpenseIntent = new Intent(this, PersonalExpenseActivity.class);	
		startActivity(personalExpenseIntent);
	}

    public void onEditTypesClick(View view) {
        Intent editTypesIntent = new Intent(this, EditTypeActivity.class);
        startActivity(editTypesIntent);
    }

}
