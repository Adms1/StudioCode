package com.waterworks;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.waterworks.adapter.TransferMakeUpLessonAdapter;

public class TransferLessonActivity extends Activity {
	ImageButton ib_back;
	ListView lv_lesson;
	TextView tv_note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_lesson);

		Initialization();
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_note = (TextView) findViewById(R.id.tv_tm_note1);
		if (NewTransferMakeUpActivity.message.toString().equalsIgnoreCase("")) {
			String next = "<font color='#EE0000'>For each make up lesson you transfer, you will receive 1 make up class of the selected type.</font>";
			tv_note.setText(Html.fromHtml(next));
		} else {
			tv_note.setText(NewTransferMakeUpActivity.message);
		}
		lv_lesson = (ListView) findViewById(R.id.lv_tm_lesson_list);
		lv_lesson.setAdapter(new TransferMakeUpLessonAdapter(
				NewTransferMakeUpActivity.tbid, NewTransferMakeUpActivity.date,
				NewTransferMakeUpActivity.time,
				NewTransferMakeUpActivity.sttimehr,
				NewTransferMakeUpActivity.sttimemin,
				NewTransferMakeUpActivity.lessontype,
				NewTransferMakeUpActivity.lessonId,
				NewTransferMakeUpActivity.attendancetype,
				NewTransferMakeUpActivity.attendanceId,
				NewTransferMakeUpActivity.Name,
				NewTransferMakeUpActivity.firstName,
				NewTransferMakeUpActivity.lastName,
				NewTransferMakeUpActivity.siteId,
				NewTransferMakeUpActivity.siteName,
				NewTransferMakeUpActivity.all_lessonid,
				NewTransferMakeUpActivity.all_lessonname,
				TransferLessonActivity.this));
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
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
