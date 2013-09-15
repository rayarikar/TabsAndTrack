package com.tnt.activity;

import com.tnt.R;
import com.tnt.R.layout;
import com.tnt.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GroupExpenseSplitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_expense_split);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_expenses_split, menu);
		return true;
	}

}
