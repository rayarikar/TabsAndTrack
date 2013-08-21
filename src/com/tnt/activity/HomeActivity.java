package com.tnt.activity;

import android.os.Bundle;

import com.example.tabsandtrack.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.tnt.dboperation.DatabaseHelper;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);		
	}	
}
