package com.waterworks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class TodaysScheduleInstructorActivity extends Activity {
	boolean isInternetPrecent = false, getinstructor = false,
			other_problem = false, server_response = false,
			connectionout = false, getlevel = false, getschedule = false,
			getISA = false, ISAFlag = false;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todays_schedule_instructor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todays_schedule_instructor, menu);
		return true;
	}

}
