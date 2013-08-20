package com.tnt.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tnt.dboperation.DatabaseHelper;
import com.tnt.entity.UserDetails;

public class HomeActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		showUserDetailsInsaertedIntoDb(tv);
		setContentView(tv);		
	}	

	public void showUserDetailsInsaertedIntoDb(TextView showDetails){
		try{
		// get UserDetails dao
		RuntimeExceptionDao<UserDetails, Integer> userDetailsDao = getHelper().getUserDetailsDao();	
		// query for all of the data objects in the UserDetails table
		List<UserDetails> userDetailsList = userDetailsDao.queryForAll();
		// our string builder for building the content-view
		StringBuilder sb = new StringBuilder();
		sb.append("Got ").append(userDetailsList.size()).append(" Entries \n");
		// if we already have items in the database
		int simpleC = 0;
		for (UserDetails userDetailsObj : userDetailsList) {
			sb.append("------------------------------------------\n");
			sb.append("[").append(simpleC).append("] = ").append(userDetailsObj).append("\n");
			simpleC++;
		}
		showDetails.setText(sb.toString());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
