package com.tnt.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.TextView;

import com.example.tabsandtrack.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.UserDetails;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	UserDetails userDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		// gets the userDetails object
		userDetails = (UserDetails) getIntent().getExtras().get("com.tnt.activity.userObj");
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("User Id:   ").append(userDetails.getUserId());
		sb.append("\n");
		sb.append("User Name:   ").append(userDetails.getUsername());
		sb.append("\n");
		sb.append("Password:   ").append(userDetails.getPassword());
		tv.setText(sb);
		
		setContentView(tv);
		//		setContentView(R.layout.activity_home);

	}
}
