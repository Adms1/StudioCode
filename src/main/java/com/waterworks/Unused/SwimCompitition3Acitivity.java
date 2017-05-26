package com.waterworks.Unused;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

public class SwimCompitition3Acitivity extends Activity {
	String TAG = "SwimCampsActivity3";

	ArrayList<HashMap<String, String>> selectedChildDataMainList = new ArrayList<HashMap<String, String>>();
	ArrayList<Boolean> selectedStudents = new ArrayList<Boolean>();

	Boolean isInternetPresent = false;
	// String siteID;
	// String successLoadChildList;
	String token, familyID;
	// String messageMeetDate;
	// String successMeetDate;
	Button btnContinueStep3;
	// boolean isSelectedAgreement = false;
	// String msg1_Hours,msg2_meet,Msg3_Meet;
	// String successSwimCompittionCheck1;
	// String messageNormal;
	TextView txtSelectedStudent, txtMeetDate;
	// String message;
	RadioButton radiobtnVolunteerYes, radiobtnVolunteerWall;
	boolean registerAsVolunteer = false;
	Spinner spinner1_volunteers;

	LinearLayout ll_body;
	public static ArrayList<String> StudentId,StudentName,step3_rg1,step3_jumpingwall,step3_rg2,step3_endlane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_compitition3);

		// getting token
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

		isInternetPresent = Utility
				.isNetworkConnected(SwimCompitition3Acitivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {

		}

		txtSelectedStudent = (TextView) findViewById(R.id.txtSelectedStudent);
		txtSelectedStudent.setText("Participant:"
				+ AppConfiguration.swimComptitionStudentName);

		txtMeetDate = (TextView) findViewById(R.id.txtMeetDate);
		txtMeetDate.setText(Html.fromHtml("<b>Meet Date: </b>"
				+ AppConfiguration.SwimMeetText));

		// fetching header view

        View view = findViewById(R.id.layout_top);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Swim Competition Registration:Step 3 of 5");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_body = (LinearLayout) findViewById(R.id.ll_swim_comp_regi3);
		try {
			StudentName = new ArrayList<String>();
			StudentName = SwimCompitition2Acitivity.t_sname;
			StudentId = new ArrayList<String>();
			StudentId = SwimCompitition2Acitivity.t_sid;
			step3_rg1 = new ArrayList<String>();
			step3_jumpingwall = new ArrayList<String>();
			step3_rg2 = new ArrayList<String>();
			step3_endlane = new ArrayList<String>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < StudentId.size(); i++) {
			step3_rg1.add("0");
			step3_jumpingwall.add("0");
			step3_rg2.add("0");
			step3_endlane.add("0");
		}
		for ( int i = 0; i < StudentName.size(); i++) {
			final int pos = i;
			LayoutInflater minflater = LayoutInflater.from(this);
			View childView = minflater.inflate(
					R.layout.list_row_swim_comp_regi3_body, null);
			TextView tv_swim_comp_regi3_name = (TextView)childView.findViewById(R.id.tv_swim_comp_regi3_name);
			RadioButton radiobtnWater = (RadioButton)childView. findViewById(R.id.radiobtnWater);
			RadioButton radiobtnWall = (RadioButton) childView.findViewById(R.id.radiobtnWall);
			RadioButton radiobtndivingblock = (RadioButton) childView.findViewById(R.id.radiobtndivingblock);
			RadioButton radiobtnYes = (RadioButton) childView.findViewById(R.id.radiobtnYes);
			RadioButton radiobtnNoPreferences = (RadioButton) childView.findViewById(R.id.radiobtnNoPreferences);
			tv_swim_comp_regi3_name.setText(StudentName.get(pos));
			radiobtnWater.setChecked(true);
			step3_rg1.remove(pos);
			step3_rg1.add(pos,"0"+"|"+StudentId.get(pos));
			step3_jumpingwall.remove(pos);
			step3_jumpingwall.add(pos,"from in the water");
			radiobtnNoPreferences.setChecked(true);
			step3_rg2.remove(pos);
			step3_rg2.add(pos,"1"+"|"+StudentId.get(pos));
			step3_endlane.remove(pos);
			step3_endlane.add(pos,"No Preference");
			radiobtnWater.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						step3_rg1.remove(pos);
						step3_rg1.add(pos,"0"+"|"+StudentId.get(pos));
						step3_jumpingwall.remove(pos);
						step3_jumpingwall.add(pos,"from in the water");
					}
				}
			});

			radiobtnWall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						step3_rg1.remove(pos);
						step3_rg1.add(pos,"1"+"|"+StudentId.get(pos));
						step3_jumpingwall.remove(pos);
						step3_jumpingwall.add(pos,"by jumping off the wall");
					}
				}
			});

			radiobtndivingblock.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					step3_rg1.remove(pos);
					step3_rg1.add(pos,"2"+"|"+StudentId.get(pos));
					step3_jumpingwall.remove(pos);
					step3_jumpingwall.add(pos,"by jumping or diving off the tall diving block");
				}
			});

			radiobtnYes.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						step3_rg2.remove(pos);
						step3_rg2.add(pos,"0"+"|"+StudentId.get(pos));
						step3_endlane.remove(pos);
						step3_endlane.add(pos,"Yes");
					}
				}
			});

			radiobtnNoPreferences.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						step3_rg2.remove(pos);
						step3_rg2.add(pos,"1"+"|"+StudentId.get(pos));
						step3_endlane.remove(pos);
						step3_endlane.add(pos,"No Preference");
					}
				}
			});

			if (radiobtnWater.isChecked()) {

				//				AppConfiguration.step3_rg1 = "0" + "|"
				//						+ AppConfiguration.swimComptitionStudentID;
				//				AppConfiguration.step3_jumpingwall = "Would like to Start the race from in the water";
			} else if (radiobtnWall.isChecked()) {

				//				AppConfiguration.step3_rg1 = "1" + "|"
				//						+ AppConfiguration.swimComptitionStudentID;
				//				AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping off the wall";
			} else {

				//				AppConfiguration.step3_rg1 = "2" + "|"
				//						+ AppConfiguration.swimComptitionStudentID;
				//				AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping or diving off the tall diving block";
			}

			if (radiobtnYes.isChecked()) {

				//				AppConfiguration.step3_rg2 = "0" + "|"
				//						+ AppConfiguration.swimComptitionStudentID;
				//				AppConfiguration.step3_endlane = "Yes";
			} else {

				//				AppConfiguration.step3_rg2 = "1" + "|"
				//						+ AppConfiguration.swimComptitionStudentID;
				//				AppConfiguration.step3_endlane = "No Preference";
			}

			ll_body.addView(childView);
		}

		// radiobtnWater = (RadioButton)findViewById(R.id.radiobtnWater);
		// radiobtnWall = (RadioButton)findViewById(R.id.radiobtnWall);
		// radiobtndivingblock =
		// (RadioButton)findViewById(R.id.radiobtndivingblock);
		//
		// radiobtnYes = (RadioButton)findViewById(R.id.radiobtnYes);
		// radiobtnNoPreferences =
		// (RadioButton)findViewById(R.id.radiobtnNoPreferences);
		//
		radiobtnVolunteerYes = (RadioButton) findViewById(R.id.radiobtnVolunteerYes);
		radiobtnVolunteerWall = (RadioButton) findViewById(R.id.radiobtnVolunteerWall);

		radiobtnVolunteerYes
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (radiobtnVolunteerYes.isChecked())
					spinner1_volunteers.setEnabled(true);
				else {
					spinner1_volunteers.setEnabled(false);
					spinner1_volunteers.setSelection(0);
				}
			}
		});

		radiobtnVolunteerWall
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (radiobtnVolunteerWall.isChecked()) {
					spinner1_volunteers.setEnabled(false);
					spinner1_volunteers.setSelection(0);
				} else
					spinner1_volunteers.setEnabled(true);
			}
		});

		spinner1_volunteers = (Spinner) findViewById(R.id.spinner1_volunteers);
		spinner1_volunteers
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String no_of_volunteers = spinner1_volunteers
						.getSelectedItem().toString();
				AppConfiguration.NoofVolunteersvalue = Integer
						.parseInt(no_of_volunteers);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		btnContinueStep3 = (Button) findViewById(R.id.btnContinueStep3);
		btnContinueStep3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				checkRadioStringValue();

				if (registerAsVolunteer == true) {
					if (AppConfiguration.NoofVolunteersvalue == 0) {
						Toast.makeText(getApplicationContext(),
								"Please select no.of volunteers ",
								Toast.LENGTH_LONG).show();
					} else {

						//						Log.d("rg1", AppConfiguration.step3_rg1);
						//						Log.d("rg2", AppConfiguration.step3_rg2);
						//						Log.d("no.of Volu", ""
						//								+ AppConfiguration.NoofVolunteersvalue);
						Log.d(TAG,"rg1 = "+ step3_rg1);
						Log.d(TAG,"rg2 = "+ step3_rg2);
						Log.d(TAG,"no.of Volu = "
								+ AppConfiguration.NoofVolunteersvalue);

						Intent i = new Intent(SwimCompitition3Acitivity.this,
								SwimCompitition4Acitivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					}
				} else {
					AppConfiguration.NoofVolunteersvalue = 0;

					//					Log.d("rg1", AppConfiguration.step3_rg1);
					//					Log.d("rg2", AppConfiguration.step3_rg2);
					//					Log.d("no.of Volu", ""
					//							+ AppConfiguration.NoofVolunteersvalue);
					Log.d(TAG,"rg1 = "+ step3_rg1);
					Log.d(TAG,"rg2 = "+ step3_rg2);
					Log.d(TAG,"no.of Volu = "
							+ AppConfiguration.NoofVolunteersvalue);

					Intent i = new Intent(SwimCompitition3Acitivity.this,
							SwimCompitition4Acitivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}

			}
		});
	}

	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				finish();
			}
		})
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		return builder1.create();
	}




	public void checkRadioStringValue() {
		// Step 3 first radio group
		//		if (radiobtnWater.isChecked()) {
		//			AppConfiguration.step3_rg1 = "0" + "|"
		//					+ AppConfiguration.swimComptitionStudentID;
		//			AppConfiguration.step3_jumpingwall = "Would like to Start the race from in the water";
		//		} else if (radiobtnWall.isChecked()) {
		//			AppConfiguration.step3_rg1 = "1" + "|"
		//					+ AppConfiguration.swimComptitionStudentID;
		//			AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping off the wall";
		//		} else {
		//			AppConfiguration.step3_rg1 = "2" + "|"
		//					+ AppConfiguration.swimComptitionStudentID;
		//			AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping or diving off the tall diving block";
		//		}
		//
		//		// Step 3 second radio group
		//		if (radiobtnYes.isChecked()) {
		//			AppConfiguration.step3_rg2 = "0" + "|"
		//					+ AppConfiguration.swimComptitionStudentID;
		//			AppConfiguration.step3_endlane = "Yes";
		//		} else {
		//			AppConfiguration.step3_rg2 = "1" + "|"
		//					+ AppConfiguration.swimComptitionStudentID;
		//			AppConfiguration.step3_endlane = "No Preference";
		//		}

		// step 3 third radio group
		if (radiobtnVolunteerYes.isChecked())
			registerAsVolunteer = true;
		else
			registerAsVolunteer = false;
	}

}
