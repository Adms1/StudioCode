package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class RegisterChildScreen3Activity extends FragmentActivity {

	String TAG = "RegisterChildScreen3";
	ArrayList<String> genderList = new ArrayList<String>();
	ArrayList<String> natureList = new ArrayList<String>();
	ArrayList<HashMap<String, String>> instructorList = new ArrayList<HashMap<String,String>>();
	Button btnSaveAddAnother,btnSaveAndProceed;
	CheckBox checkBox;
	ImageView imgBack;
	StringBuilder builder;
	String message;
	TextView txtAdditionalInfo;
	LinearLayout ll_additional_info;
	TextView txtHideInfo;
	String successRegister;
	RadioButton str1Yes,str1No,str2Yes,str2No;
	RadioButton radio1,radio2,radio3,radio4,radio5,radio6,radio7,radio8,radio9;
	CheckBox chk1_child_register,chk2_child_register,chk3_child_register;
	LinearLayout ll_allergiesOrmedical;
	EditText edtAllergiesOrmedicalInfo,edtDescriptionInfo,edtSwimGoalinfo;
	RadioGroup str1,rgpmedicalinfo;
	String token,familyID;
	boolean isSaveAndProceedClick = false;
	Context mContext =this;
	Boolean isInternetPresent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerchild_screen3);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

		btnSaveAddAnother = (Button) findViewById(R.id.btnSaveAddAnother);
		btnSaveAndProceed = (Button)findViewById(R.id.btnSaveAndProceed);

		txtAdditionalInfo = (TextView)findViewById(R.id.txtAdditionalInfo);
		ll_additional_info = (LinearLayout)findViewById(R.id.ll_additional_info);
		txtHideInfo = (TextView)findViewById(R.id.txtHideInfo);
		ll_allergiesOrmedical = (LinearLayout)findViewById(R.id.ll_allergiesOrmedical);
		edtAllergiesOrmedicalInfo = (EditText)findViewById(R.id.edtAllergiesOrmedicalInfo);
		edtDescriptionInfo = (EditText)findViewById(R.id.edtDescriptionInfo);
		edtSwimGoalinfo = (EditText)findViewById(R.id.edtSwimGoalinfo);

		rgpmedicalinfo = (RadioGroup)findViewById(R.id.rgpmedicalinfo);
		str1 = (RadioGroup)findViewById(R.id.str1);

		str1Yes = (RadioButton)findViewById(R.id.str1Yes);
		str1No = (RadioButton)findViewById(R.id.str1No);
		str1Yes.setId(0);
		str1No.setId(1);

		str2Yes = (RadioButton)findViewById(R.id.str2Yes);
		str2No = (RadioButton)findViewById(R.id.str2No);

		str2Yes.setId(0);
		str2No.setId(1);

		radio1 = (RadioButton)findViewById(R.id.radio1);
		radio2 = (RadioButton)findViewById(R.id.radio2);
		radio3 = (RadioButton)findViewById(R.id.radio3);
		radio4 = (RadioButton)findViewById(R.id.radio4);
		radio5 = (RadioButton)findViewById(R.id.radio5);
		radio6 = (RadioButton)findViewById(R.id.radio6);
		radio7 = (RadioButton)findViewById(R.id.radio7);
		radio8 = (RadioButton)findViewById(R.id.radio8);
		radio9 = (RadioButton)findViewById(R.id.radio9);

		chk1_child_register = (CheckBox)findViewById(R.id.chk1_child_register);
		chk2_child_register = (CheckBox)findViewById(R.id.chk2_child_register);
		chk3_child_register = (CheckBox)findViewById(R.id.chk3_child_register);

		// fetching header view
        View view = findViewById(R.id.header);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Register a Child");
        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


		btnSaveAddAnother.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(radio1_validation()!=-1){
					if(radio2_validation()!=-1){
						isSaveAndProceedClick = false;

						checkRadioStringValue();
						checkChildStrongSensitiveOutging();
						checkCheckboxValues();

						AppConfiguration.Description = edtDescriptionInfo.getText().toString();
						AppConfiguration.Swimgoals = edtSwimGoalinfo.getText().toString();
						AppConfiguration.strallergiesmedical = edtAllergiesOrmedicalInfo.getText().toString();
						isInternetPresent = Utility.isNetworkConnected(RegisterChildScreen3Activity.this);
						if (!isInternetPresent) {
							onDetectNetworkState().show();
						} else {
							new RegisterChildAsyncTask().execute();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Please select valid options", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "Please select valid options", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnSaveAndProceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(radio1_validation()!=-1){
					if(radio2_validation()!=-1){
						isSaveAndProceedClick = true;

						checkRadioStringValue();
						checkChildStrongSensitiveOutging();
						checkCheckboxValues();

						AppConfiguration.Description = edtDescriptionInfo.getText().toString();
						AppConfiguration.Swimgoals = edtSwimGoalinfo.getText().toString();
						AppConfiguration.strallergiesmedical = edtAllergiesOrmedicalInfo.getText().toString();
						isInternetPresent = Utility.isNetworkConnected(RegisterChildScreen3Activity.this);
						if (!isInternetPresent) {
							onDetectNetworkState().show();
						} else {
							new RegisterChildAsyncTask().execute();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Please select valid options", Toast.LENGTH_SHORT).show();
					}
				}else{
					int selected_radio2 = rgpmedicalinfo.getCheckedRadioButtonId();
					if(selected_radio2==0){
						Toast.makeText(getApplicationContext(), "Please write something about allergies.", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "Please select valid options", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		txtAdditionalInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ll_additional_info.setVisibility(View.VISIBLE);
				txtAdditionalInfo.setVisibility(View.GONE);
			}
		});

		txtHideInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_additional_info.setVisibility(View.GONE);
				txtAdditionalInfo.setVisibility(View.VISIBLE);
			}
		});


		str2Yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_allergiesOrmedical.setVisibility(View.VISIBLE);
			}
		});

		str2No.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_allergiesOrmedical.setVisibility(View.GONE);
			}
		});
	}

	public int radio1_validation(){
		int selected_radio1 = str1.getCheckedRadioButtonId();
		return selected_radio1;
	}

	public int radio2_validation(){
		int selected_radio2 = rgpmedicalinfo.getCheckedRadioButtonId();
		if(selected_radio2==0){
			if(edtAllergiesOrmedicalInfo.getText().toString().trim().length()<=0){
				selected_radio2=-1;
			}
		}
		return selected_radio2;
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

	public void checkCheckboxValues()
	{
		if(chk1_child_register.isChecked())
			AppConfiguration.childApplyCheck1 = true;
		else
			AppConfiguration.childApplyCheck1 = false;


		if(chk2_child_register.isChecked())
			AppConfiguration.childApplyCheck2 = true;
		else
			AppConfiguration.childApplyCheck2 = false;


		if(chk3_child_register.isChecked())
			AppConfiguration.childApplyCheck3 = true;
		else
			AppConfiguration.childApplyCheck3 = false;

		AppConfiguration.ChildStrongWilled = "I would like my child to eventually move into competitive swimming/join a swim team.";
		AppConfiguration.ChildSensitive = "I would like to focus more on speed and distance/endurance.";
		AppConfiguration.ChildOutgoing = "I would like to focus on technique and shorter distances.";
	}

	public void checkChildStrongSensitiveOutging()
	{
		//Strong
		if(radio1.isChecked())
		{
			AppConfiguration.ChildStrongWilledValue = "1";

		}
		else if(radio2.isChecked())
		{
			AppConfiguration.ChildStrongWilledValue = "2";

		}
		else if(radio3.isChecked())
		{
			AppConfiguration.ChildStrongWilledValue = "3";

		}


		//Sensitive
		if(radio4.isChecked())
		{
			AppConfiguration.ChildSensitiveValue = "1";

		}
		else if(radio5.isChecked())
		{
			AppConfiguration.ChildSensitiveValue = "2";
		}
		else if(radio6.isChecked())
		{
			AppConfiguration.ChildSensitiveValue = "3";
		}

		//Outgoing
		if(radio7.isChecked())
		{
			AppConfiguration.ChildOutgoingValue = "1";

		}
		else if(radio8.isChecked())
		{
			AppConfiguration.ChildOutgoingValue = "2";
		}
		else if(radio9.isChecked())
		{
			AppConfiguration.ChildOutgoingValue = "3";
		}

	}


	public void checkRadioStringValue()
	{
		if(str1Yes.isChecked())
			AppConfiguration.strYesNo1 = "Y";
		else
			AppConfiguration.strYesNo1 = "N";

		if(str2Yes.isChecked())
		{
			AppConfiguration.strYesNo2 = "Y";
		}
		else
		{
			AppConfiguration.strYesNo2 = "N";
		}
	}
	public void registerChildProcess() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("lessonlist", ""+AppConfiguration.lessionTypes);
		param.put("Description", ""+AppConfiguration.Description);
		param.put("ChildStrongWilledValue", AppConfiguration.ChildStrongWilledValue);
		param.put("ChildSensitiveValue",""+AppConfiguration.ChildSensitiveValue);
		param.put("ChildOutGoingValue", ""+AppConfiguration.ChildOutgoingValue);
		param.put("ChildStrongWilled", ""+AppConfiguration.ChildStrongWilled);
		param.put("ChildSensitive",""+AppConfiguration.ChildSensitive);
		param.put("ChildOutGoing", ""+AppConfiguration.ChildOutgoing);
		param.put("chk1", ""+AppConfiguration.childApplyCheck1);
		param.put("chk2", ""+AppConfiguration.childApplyCheck2);
		param.put("chk3",""+AppConfiguration.childApplyCheck3);
		param.put("Swimgoals", ""+AppConfiguration.Swimgoals);
		param.put("levels", ""+AppConfiguration.levelTypes);
		param.put("FName", ""+AppConfiguration.studentFirstname);param.put("LName", ""+AppConfiguration.studentLastname);
		param.put("Dob", ""+AppConfiguration.studentDOB);
		param.put("Age", ""+AppConfiguration.studentAge);
		param.put("Gender", AppConfiguration.studentGender);
		param.put("InstructorNature", AppConfiguration.instructorNatureType);
		param.put("InstructorGender", AppConfiguration.instructorGender);
		param.put("strYesNo1",""+AppConfiguration.strYesNo1);
		param.put("strYesNo2",""+AppConfiguration.strYesNo2);
		param.put("strallergiesmedical", AppConfiguration.strallergiesmedical);
		
		String responseString = WebServicesCall
		.RunScript(AppConfiguration.registerChild, param);
		
		readAndParseJSON(responseString);
	}
	public void readAndParseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successRegister = reader.getString("Success");
			if(successRegister.toString().equals("True"))
			{
				JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");

				for(int i = 0 ;i < jsonMainNode.length();i++)
				{
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					message = jsonChildNode.getString("Msg");
				}

			}
			else
			{
				JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");

				for(int i = 0 ;i < jsonMainNode.length();i++)
				{
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					message = jsonChildNode.getString("Msg");
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	class RegisterChildAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(RegisterChildScreen3Activity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			registerChildProcess();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if(successRegister.toString().equals("True"))
			{
				Toast.makeText(getApplicationContext(), ""+message,  Toast.LENGTH_LONG).show();

				if(isSaveAndProceedClick == true)
				{

                    Intent i = new Intent(RegisterChildScreen3Activity.this,RegisterChildScreen1Activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
				}
				else
				{
					Intent i = new Intent(RegisterChildScreen3Activity.this,RegisterChildScreen1Activity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}

				RegisterChildScreen2Activity.edtLevel.setText("");
				AppConfiguration.levelTypes = "";
			}
			else
			{
				Toast.makeText(getApplicationContext(), ""+message,  Toast.LENGTH_LONG).show();
			}

		}
	}

	String successStudentCount,Message;


	class checkStudentCountAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = new ProgressDialog(RegisterChildScreen3Activity.this);
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
				Intent i = new Intent(RegisterChildScreen3Activity.this,RegisterChildScreen1Activity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterChildScreen3Activity.this);

				alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
				alertDialogBuilder.setMessage(Html.fromHtml(""+Message));
				// set positive button: Yes message
				alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();

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
		param.put("FamilyID",familyID );

		String responseString = WebServicesCall
		.RunScript(AppConfiguration.getChildCountURL, param);
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
	public boolean Student_Doc_Update(){

		boolean student_available=false;

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		
		String responseString = WebServicesCall
		.RunScript(AppConfiguration.DOMAIN + AppConfiguration.legal_doc_StudentDoc_Status, param);
		
		try {
			JSONObject obj = new JSONObject(responseString);
			String success = obj.getString("Success");

			if(success.equals("True")){
				Intent i = new Intent(RegisterChildScreen3Activity.this,LegalDoc_Check_FamilyDoc.class);
				i.putExtra("Token", token);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}else{

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
		}
		
		return student_available; 
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}


}
