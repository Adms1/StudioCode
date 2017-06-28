package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class RequestShadowingActivity extends Activity implements OnClickListener{
	TableLayout table_rs_childs;
	EditText et_message_body;
	Button btn_submit_request;
	ImageButton btn_back;
	Boolean isInternetPresent = false;
	String message;
	String data_load= "False";
	ArrayList<String> StudentName,StudentId,sendingID;
	RelativeLayout rl_rs_inside_body;
	private static String TAG = "Request Shadowing";
	int data =0;
	String message_send="";
	String token,familyID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_shadowing);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		
		isInternetPresent = Utility.isNetworkConnected(RequestShadowingActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			new GetChild().execute();
		}
		
	}
	
	private void Initialization() {
		// TODO Auto-generated method stub
		et_message_body = (EditText)findViewById(R.id.et_rs_message_body);
		btn_submit_request = (Button)findViewById(R.id.btn_rs_submit);
		table_rs_childs = (TableLayout)findViewById(R.id.table_rs_childs);
		btn_submit_request.setOnClickListener(this);
		btn_back = (ImageButton)findViewById(R.id.ib_back);
		btn_back.setOnClickListener(this);
		rl_rs_inside_body =(RelativeLayout)findViewById(R.id.rl_rs_inside_body);
		rl_rs_inside_body.setVisibility(View.GONE);
		StudentId = new ArrayList<String>();
		StudentName = new ArrayList<String>();
	}

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(RequestShadowingActivity.this);
		builder1.setIcon(getResources().getDrawable(R.drawable.alerticon));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        finish();
		    }
		})       
		.setPositiveButton("OK",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		    }
		});
		    return builder1.create();
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
		isInternetPresent = Utility
				.isNetworkConnected(RequestShadowingActivity.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isInternetPresent){
		switch (v.getId()) {
			case R.id.btn_rs_submit:
				message_send = et_message_body.getText().toString();
					new SubmitRequest().execute();
				break;
			case R.id.ib_back:
					onBackPressed();
				break;
	
			default:
				break;
			}
		}
		else{
			onDetectNetworkState().show();
		}
	}
		
	private class GetChild extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(RequestShadowingActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
			data = 1;
			StudentId.clear();
			StudentName.clear();
			table_rs_childs.removeAllViews();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("FamilyID",familyID );

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.requestShadowing, param);
			readAndParseJSON(responseString);
			

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pd != null) {
				pd.dismiss();
			}
			if(data_load.toString().equals("True"))
			{
				rl_rs_inside_body.setVisibility(View.VISIBLE);
				sendingID = new ArrayList<String>();
	            int offset_in_column=0, table_size=StudentName.size();
	            TableRow tr=null;
	            for (int offset_in_table=0; offset_in_table < table_size; offset_in_table++) {
	                /* maybe you want to do something special with the data from the server here ? */

	                if (offset_in_column == 0) {
	                    tr = new TableRow(RequestShadowingActivity.this);
	                    tr.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	                }
	                final CheckBox check = new CheckBox(RequestShadowingActivity.this);
	                check.setButtonDrawable(getResources().getDrawable(R.drawable.customdrawablecheckbox));
	                check.setText(StudentName.get(offset_in_table));
	                check.setId(Integer.parseInt(StudentId.get(offset_in_table)));
	                check.setTextColor(getResources().getColor(R.color.app_text));
	                check.setPadding(10, 5, 0, 5);
	                check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked){
								sendingID.add(""+check.getId());
							}
							else{
								sendingID.remove(""+check.getId());
							}
							Log.e(TAG,""+sendingID);
						}
					});
	                check.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
	                tr.addView(check);

	                offset_in_column++;
	                if (offset_in_column == 2) {

	                    table_rs_childs.addView(tr);
	                    offset_in_column = 0;
	                }
	            }
	            if (offset_in_column != 0)
	            	table_rs_childs.addView(tr);
			}
			else
			{
				rl_rs_inside_body.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "No child for request shadowing. Please add child.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void readAndParseJSON(String in) {
		if(data == 1){
		try {
			JSONObject reader = new JSONObject(in);
			data_load = reader.getString("Success");
			if(data_load.toString().equals("True"))
			{
				 JSONArray jsonMainNode = reader.optJSONArray("PhoneList");
		         
		         for(int i = 0 ;i < jsonMainNode.length();i++)
		         {
		        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		        	 
		        	 StudentId.add(jsonChildNode.getString("StudentID"));
		        	 StudentName.add(jsonChildNode.getString("StudentName"));
		        	 
		         }
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		else if(data == 2){
			try {
				JSONObject reader = new JSONObject(in);
				data_load = reader.getString("Success");
			}
			catch(JSONException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	   }
	
	private class SubmitRequest extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(RequestShadowingActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
			data = 2;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("FamilyID",familyID );
			param.put("StudentList", sendingID.toString().trim().replaceAll("\\[", "").replaceAll("\\]", ""));
			param.put("Reason", message_send);

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.submitrequestshadowing, param);
			
			readAndParseJSON(responseString);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if(data_load.toString().equals("True"))
			{
				AlertDialog alertDialog = new AlertDialog.Builder(RequestShadowingActivity.this).create();
				alertDialog.setTitle("WaterWorks");
				alertDialog.setIcon(getResources().getDrawable(R.drawable.alerticon));
				alertDialog.setCanceledOnTouchOutside(false); 
				alertDialog.setCancelable(false);
				// set the message
				alertDialog.setMessage("Request sent successfully...!!!");
				// set button1 functionality
				alertDialog.setButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// close dialog
								onBackPressed();
								dialog.cancel();

							}
						});
				// show the alert dialog
				alertDialog.show();

			}
			else{
				AlertDialog alertDialog = new AlertDialog.Builder(RequestShadowingActivity.this).create();
				alertDialog.setTitle("WaterWorks");
				alertDialog.setIcon(getResources().getDrawable(R.drawable.alerticon));
				alertDialog.setCanceledOnTouchOutside(false); 
				alertDialog.setCancelable(false);
				// set the message
				alertDialog.setMessage("Request not sent.\nPlease try again.");
				// set button1 functionality
				alertDialog.setButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// close dialog
//								onBackPressed();
								dialog.cancel();
								isInternetPresent = Utility.isNetworkConnected(RequestShadowingActivity.this);
								if (!isInternetPresent) {
									onDetectNetworkState().show();
								} else {
									new GetChild().execute();
								}
							}
						});
				// show the alert dialog
				alertDialog.show();
			}
		}
	}
	
}
