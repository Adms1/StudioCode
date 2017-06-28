package com.waterworks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailViewScheduleActivity extends Activity {
	ImageButton ib_back;
	TextView tv_date,tv_day,tv_time,tv_student,tv_age,tv_level,tv_instructor,tv_att,tv_lt,tv_location,tv_count,tv_comment;
	String date,day,time,student,age,level,instructor,att,lt,location,count,comment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_view_schedule);
		date = getIntent().getStringExtra("date");
		day = getIntent().getStringExtra("day");
		time = getIntent().getStringExtra("time");
		student = getIntent().getStringExtra("student");
		age = getIntent().getStringExtra("age");
		level = getIntent().getStringExtra("level");
		instructor = getIntent().getStringExtra("instructor");
		att = getIntent().getStringExtra("att");
		lt = getIntent().getStringExtra("lt");
		location = getIntent().getStringExtra("location");
		count = getIntent().getStringExtra("count");
		comment = getIntent().getStringExtra("comment");
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Initialization();
	}
	
	private void Initialization() {
		// TODO Auto-generated method stub
		tv_date = (TextView)findViewById(R.id.tv_dvs_date_item);
		tv_day = (TextView)findViewById(R.id.tv_dvs_day_item);
		tv_time = (TextView)findViewById(R.id.tv_dvs_time_item);
		tv_student = (TextView)findViewById(R.id.tv_dvs_student_item);
		tv_age = (TextView)findViewById(R.id.tv_dvs_age_item);
		tv_level = (TextView)findViewById(R.id.tv_dvs_level_item);
		tv_instructor = (TextView)findViewById(R.id.tv_dvs_instructor_item);
		tv_att = (TextView)findViewById(R.id.tv_dvs_abbr_item);
		tv_lt = (TextView)findViewById(R.id.tv_dvs_lt_item);
		tv_location = (TextView)findViewById(R.id.tv_dvs_location_item);
		tv_count = (TextView)findViewById(R.id.tv_dvs_count_item);
		tv_comment = (TextView)findViewById(R.id.tv_dvs_comments_item);
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		SetDisply();
	}

	private void SetDisply() {
		// TODO Auto-generated method stub
		tv_date.setText(date);
		tv_day.setText(day);
		tv_time.setText(time);
		tv_student.setText(student);
		tv_age.setText(age);
		tv_level.setText(level);
		tv_instructor.setText(instructor);
		tv_att.setText(att);
		tv_lt.setText(lt);
		tv_location.setText(location);
		tv_count.setText(count);
		tv_comment.setText(comment);
		ib_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
