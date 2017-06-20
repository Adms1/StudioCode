package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class FeedbackActivity extends Activity {
	String TAG = "Feedback";
	LinearLayout ll_secondary_parent;
	Button btnSend;
	Spinner spinner1, spinner2_reference;
	ArrayList<HashMap<String, String>> phoneTypeList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> typeList = new ArrayList<String>();

	String primaryPhoneType;
	String secondaryPhoneType;
	
	EditText edtFirstName,edtLastName,edtEmail,edtSubject,edtBestCall,edtPrimarytele,edtAlternate,edtDetailedMessage;

	RelativeLayout rel_back;
	ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> siteName = new ArrayList<String>();
	String DeptName,DeptValue;
	String selectedSiteID,selectedSiteName;
	String firstname,lastname,email,bestcall,pTelephone,sTelephone,subject,detailMessage;
	String successFeedback;
	String messageFeedback;
Context mContext=this;
	Boolean isInternetPresent = false;
	String token,familyID; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		
		spinner1 = (Spinner) findViewById(R.id.spinner1_sites);
		spinner2_reference = (Spinner) findViewById(R.id.spinner2_reference);
		ll_secondary_parent = (LinearLayout) findViewById(R.id.ll_secondary_parent);
		btnSend = (Button) findViewById(R.id.btnSend);
		edtFirstName = (EditText)findViewById(R.id.edtFirstName);
		edtLastName = (EditText)findViewById(R.id.edtLastName);
		edtEmail = (EditText)findViewById(R.id.edtEmail);
		edtBestCall = (EditText)findViewById(R.id.edtBestCall);
		edtPrimarytele = (EditText)findViewById(R.id.edtPrimarytele);
		edtAlternate = (EditText)findViewById(R.id.edtAlternate);
		edtSubject = (EditText)findViewById(R.id.edtSubject);
		edtDetailedMessage = (EditText)findViewById(R.id.edtDetailedMessage);
		
		edtFirstName.setText(""+AppConfiguration.loginParentFirstname);;
		edtLastName.setText(""+AppConfiguration.loginParentLastname);
		edtEmail.setText(""+AppConfiguration.loginParentEmail);
		edtPrimarytele.setText(""+AppConfiguration.loginParentPhone1);
		isInternetPresent = Utility.isNetworkConnected(FeedbackActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new SitesListAsyncTask().execute();
		}
		typeList.add("FrontDesk");
		typeList.add("Maintenance");
		typeList.add("Management");
		typeList.add("Accounting");
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(FeedbackActivity.this,android.R.layout.simple_spinner_item, typeList);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2_reference.setAdapter(dataAdapter1);
		// Spinner1 on item click listener
		spinner2_reference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				DeptName = arg0.getItemAtPosition(position).toString();
				DeptValue = ""+(position+1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		
		
		

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				firstname = edtFirstName.getText().toString();
				lastname = edtLastName.getText().toString();
				email = edtEmail.getText().toString();
				bestcall = edtBestCall.getText().toString();
				pTelephone = edtPrimarytele.getText().toString();
				sTelephone = edtAlternate.getText().toString();
				subject = edtSubject.getText().toString();
				detailMessage = edtDetailedMessage.getText().toString();
				
				if(firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || bestcall.isEmpty() || pTelephone.isEmpty() || subject.isEmpty() || detailMessage.isEmpty())
				{
					Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
				}
				else
				{
					
					if (!AppConfiguration.isValidEmail(email)) {
						Toast.makeText(getApplicationContext(),
								"Invalid Email", Toast.LENGTH_SHORT).show();
					} else {
						isInternetPresent = Utility.isNetworkConnected(FeedbackActivity.this);
						if (!isInternetPresent) {
							onDetectNetworkState().show();
						} else {
							new FeedBackSendingAsyncTask().execute();
						}
					}
					
				}
			}
		});


		// fetching header view

        View view = findViewById(R.id.header);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Feedback");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
	        
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
	//==========================================Start of Site List ===================================================
		public void loadSitesList() {
			siteMainList.clear();
			
			HashMap<String, String > param = new HashMap<String, String>();
			
			String responseString = WebServicesCall.RunScript(AppConfiguration.getSiteListURL, param);
			readAndParseJSON(responseString);
			

		}

		public void readAndParseJSON(String in) {
			try {
				JSONObject reader = new JSONObject(in);
				JSONArray jsonMainNode = reader.optJSONArray("Sites");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					HashMap<String, String> hashmap = new HashMap<String, String>();

					hashmap.put("SiteID", jsonChildNode.getString("SiteID"));
					hashmap.put("SiteName",jsonChildNode.getString("SiteName"));

					siteName.add(""+jsonChildNode.getString("SiteName"));
					siteMainList.add(hashmap);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {
			ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(FeedbackActivity.this);
				pd.setMessage("Please wait...");
				pd.setCancelable(false);
				pd.show();
				
				siteMainList.clear();
				siteName.clear();
			}

			@Override
			protected Void doInBackground(Void... params) {
				loadSitesList();

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (pd != null) {
					pd.dismiss();
				}
				
				ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(FeedbackActivity.this,android.R.layout.simple_spinner_item, siteName);
				dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner1.setAdapter(dataAdapter1);
				// Spinner1 on item click listener
				spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						selectedSiteName = siteMainList.get(position).get("SiteName");
						selectedSiteID = siteMainList.get(position).get("SiteID");
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

			}
		}
		
		//===================================== feedback =======================================
		
		public void sendingFeedback() {

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("firstname", ""+firstname);
			param.put("lastname", ""+lastname);
			param.put("phone1", ""+pTelephone);
			param.put("phone2", ""+sTelephone);
			param.put("Email", ""+email);
			param.put("callback", ""+bestcall);
			param.put("subject", ""+subject);
			param.put("problemDetails", detailMessage);
			param.put("DeptName", DeptName);
			param.put("DeptValue", DeptValue);
			param.put("Sitename", selectedSiteName);
			param.put("sitevalue", selectedSiteID);
			String responseString = WebServicesCall.RunScript(AppConfiguration.feedbackURL, param);
			readAndParseJSONFeedback(responseString);
			}

		public void readAndParseJSONFeedback(String in) {
			
			try {
				JSONObject reader = new JSONObject(in);
				successFeedback = reader.getString("Success");
				if(successFeedback.toString().equals("True"))
				{
					JSONArray jsonMainNode = reader.optJSONArray("ContactMail");
					
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						messageFeedback = jsonChildNode.getString("Msg"); 
					
					}
				}
				else
				{
					JSONArray jsonMainNode = reader.optJSONArray("ContactMail");
					
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						messageFeedback = jsonChildNode.getString("Msg"); 
					
					}
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		   }
		
		
		class FeedBackSendingAsyncTask extends AsyncTask<Void, Void, Void> {
			ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(FeedbackActivity.this);
				pd.setMessage(getResources().getString(R.string.pleasewait));
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				sendingFeedback();

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (pd != null) {
					pd.dismiss();
				}
				
				if(successFeedback.toString().equals("True"))
				{
					Toast.makeText(getApplicationContext(), ""+messageFeedback,  Toast.LENGTH_LONG).show();
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), ""+messageFeedback,  Toast.LENGTH_LONG).show();
				}
				

				
			}
		}
	
	
}
