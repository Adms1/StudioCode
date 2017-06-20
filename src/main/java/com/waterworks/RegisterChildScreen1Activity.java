package com.waterworks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class RegisterChildScreen1Activity extends Activity {

	String TAG = "RegisterChildScreen1";
	ArrayList<String> studentGenderList = new ArrayList<String>();
	ArrayList<String> genderList = new ArrayList<String>();
	ArrayList<String> natureList = new ArrayList<String>();
	ArrayList<HashMap<String, String>> instructorList = new ArrayList<HashMap<String,String>>();
	String successGetInstructorPreferences;
	String instructorGender,instructorNatureType;
	Spinner spinner1LessionType,spinner2_nature_type;
	ImageView imgNext;
	EditText edtSFirstname, edtSLastname, edtAge;

	String successGetEmailPreferences;
	String successSaveEmailPreferences;
	String studentGender;
	String successAgeCalculation;

	ImageView imgBack;
	StringBuilder builder;
	String message;
	Spinner spinnerStudentGender;
	EditText edtStudentBdate;

	private Calendar calendar;
	private int year, month, day;
	Boolean isInternetPresent = false;
	String token, familyID;
	Context mContext=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerchild_screen1);
		
		// getting token
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
		isInternetPresent = Utility.isNetworkConnected(RegisterChildScreen1Activity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new checkStudentCountAsyncTask().execute();

			imgNext = (ImageView) findViewById(R.id.imgNext);
			edtSFirstname = (EditText) findViewById(R.id.edtSFirstname);
			edtSLastname = (EditText) findViewById(R.id.edtSLastname);
			edtSLastname.setText("" + AppConfiguration.loginParentLastname);

			spinnerStudentGender = (Spinner) findViewById(R.id.spinner1_child_gender);
			edtStudentBdate = (EditText) findViewById(R.id.edtStudentBdate);
			edtAge = (EditText) findViewById(R.id.edtAge);
			spinner1LessionType = (Spinner) findViewById(R.id.spinner1LessionType);
			spinner2_nature_type = (Spinner) findViewById(R.id.spinner2_nature_type);

			// current date setting
			calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
			// showDate(year, month+1, day);

			studentGenderList.add("Male");
			studentGenderList.add("Female");
			// Student Gender drop down.
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					RegisterChildScreen1Activity.this,
					android.R.layout.simple_spinner_item, studentGenderList);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerStudentGender.setAdapter(dataAdapter);

			spinnerStudentGender
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
												   int position, long arg3) {

							studentGender = spinnerStudentGender.getSelectedItem()
									.toString();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			edtStudentBdate.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					showDialog(999);
				}
			});

			setGenderValue();
			setNatureType();

			// fetching header view

			View view = findViewById(R.id.header);
			TextView title = (TextView) view.findViewById(R.id.action_title);
			title.setText("Register a Child");
			ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
			ib_menusad.setBackgroundResource(R.drawable.back_arrow);
			Button relMenu = (Button) view.findViewById(R.id.relMenu);
			relMenu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (AppConfiguration.logCount.equals("0")) {
						Intent i = new Intent(RegisterChildScreen1Activity.this, DashBoardActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);
						finish();
					} else
						finish();
				}
			});


			imgNext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String firstname = edtSFirstname.getText().toString();
					String lastname = edtSLastname.getText().toString();
					String dob = edtStudentBdate.getText().toString();
					String age = edtAge.getText().toString();
					if (firstname.isEmpty() || lastname.isEmpty()) {
						Toast.makeText(getApplicationContext(),
								R.string.empty_fields, Toast.LENGTH_LONG).show();
					} else if (age.isEmpty()) {
						Toast.makeText(getApplicationContext(),
								"Please select birthdate", Toast.LENGTH_LONG)
								.show();
					} else {
						AppConfiguration.studentFirstname = firstname;
						AppConfiguration.studentLastname = lastname;
						AppConfiguration.studentGender = studentGender;
						AppConfiguration.studentDOB = dob;
						AppConfiguration.studentAge = age;
						AppConfiguration.instructorGender = instructorGender;
						AppConfiguration.instructorNatureType = instructorNatureType;


						if (firstname.isEmpty() || lastname.isEmpty()) {
							Toast.makeText(getApplicationContext(),
									R.string.empty_fields, Toast.LENGTH_LONG)
									.show();
						} else if (age.isEmpty()) {
							Toast.makeText(getApplicationContext(),
									R.string.hint_select_gender, Toast.LENGTH_LONG)
									.show();
						} else {
							isInternetPresent = Utility.isNetworkConnected(RegisterChildScreen1Activity.this);
							if (!isInternetPresent) {
								onDetectNetworkState().show();
							} else {
								new MyAccountAgeCalculationAsyncTask().execute();
							}
						}

					}
				}
			});
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
		builder1.setIcon(R.drawable.logo);
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		return builder1.create();
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2 + 1, arg3);
		}
	};

	// mm/dd/yyyy
	private void showDate(int year, int month, int day) {
		edtStudentBdate.setText(new StringBuilder().append(month).append("/")
				.append(day).append("/").append(year));

		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.set(year, month, day);
		Date birthday = cal.getTime();

		long dateSubtract = today.getTime() - birthday.getTime();
		long time = 1000 * 60 * 60 * 24;
		System.out.println("I've lived " + dateSubtract / time + " Day");

		double age = (double) (((dateSubtract / time) + 30) / 365.25);
		DecimalFormat df = new DecimalFormat("#.##");
		age = Double.valueOf(df.format(age));

		edtAge.setText("" + age);

	}

	public void getAgeCalculationLoading() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("age",
				AppConfiguration.studentAge);

		String responseString = WebServicesCall
		.RunScript(AppConfiguration.MyAcnt_AgeCalculation, param);
		
		readAndParseJSON(responseString);
		
	}

	public void readAndParseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successAgeCalculation = reader.getString("Success");
			if (successAgeCalculation.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("ClassByAge");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					String lessonid = jsonChildNode.getString("lessonid")
							.trim();
					String lessonname = jsonChildNode.getString("lessonname")
							.trim();
					String EnableTrueFalse = jsonChildNode.getString(
							"EnableTrueFalse").trim();

					hashmap.put("lessonid", lessonid);
					hashmap.put("lessonname", lessonname);
					hashmap.put("EnableTrueFalse", EnableTrueFalse);
					
					AppConfiguration.lessionTypeList.add(hashmap);

				}

			} else {
				JSONArray jsonMainNode = reader.optJSONArray("ClassByAge");

				for (int i = 0; i < jsonMainNode.length(); i++) {

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					message = jsonChildNode.getString("Msg").trim();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyAccountAgeCalculationAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(RegisterChildScreen1Activity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			AppConfiguration.lessionTypeList.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			getAgeCalculationLoading();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successAgeCalculation.toString().equals("True")) {

				Intent i = new Intent(RegisterChildScreen1Activity.this,RegisterChildScreen2Activity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);

			} else {
				Toast.makeText(getApplicationContext(), "" + message,
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void setGenderValue()
	{
		genderList.add("Any");
		genderList.add("Male");
		genderList.add("Female");
		// Student Gender drop down.
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegisterChildScreen1Activity.this,android.R.layout.simple_spinner_item, genderList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1LessionType.setAdapter(dataAdapter);
		spinner1LessionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {

				instructorGender = spinner1LessionType.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	public void setNatureType()
	{
		natureList.add("Any");
		natureList.add("Firm");
		natureList.add("Gentle");
		// Student Gender drop down.
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(RegisterChildScreen1Activity.this,android.R.layout.simple_spinner_item, natureList);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2_nature_type.setAdapter(dataAdapter1);
		spinner2_nature_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {

				instructorNatureType = spinner2_nature_type.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (AppConfiguration.logCount.equals("0")) {
			Intent i = new Intent(RegisterChildScreen1Activity.this, DashBoardActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
		} else
			finish();
	}
	
	
	String successStudentCount="",Message="";
	class checkStudentCountAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(RegisterChildScreen1Activity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			studentCountInfo();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if(successStudentCount.equalsIgnoreCase("True"))
			{
				
			}
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterChildScreen1Activity.this);

				alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
				alertDialogBuilder.setMessage(Html.fromHtml(""+Message));
				// set positive button: Yes message
				alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						Intent i = new Intent(RegisterChildScreen1Activity.this,DashBoardActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						i.putExtra("POS", 2);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						finish();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show alert
				alertDialog.show();
			}
		}

	}

	public void studentCountInfo() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		
		String responseString = WebServicesCall.RunScript(AppConfiguration.getChildCountURL, param);
		readAndParseJSONStudntCount(responseString);
		
	}
	public void readAndParseJSONStudntCount(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successStudentCount = reader.getString("Success");
			if(successStudentCount.toString().equals("True"))
			{
				JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
				for (int i = 0; i < jsonMainNode.length(); i++) {

				}
			}
			else
			{
				JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					Message = jsonChildNode.getString("Msg").trim();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
