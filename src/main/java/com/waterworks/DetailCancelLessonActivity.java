package com.waterworks;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class DetailCancelLessonActivity extends Activity {
	ImageButton ib_back;
	TextView tv_date,tv_day,tv_time,tv_student,tv_instructor,tv_att,tv_lt,tv_count,tv_comment;
	String date,day,time,student,instructor,att,lt,location,count,comment,
	cancelId,cancelenable;
	Button btn_cancel_class;
	private static String TAG = "Cancel Class";
	String message;
	String data_load= "False";
	String token,familyID,abc; 
	Boolean isInternetPresent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_cancle_lesson);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		isInternetPresent = Utility
				.isNetworkConnected(DetailCancelLessonActivity.this);
		if(isInternetPresent){
			date = getIntent().getStringExtra("date");
			day = getIntent().getStringExtra("day");
			time = getIntent().getStringExtra("time");
			student = getIntent().getStringExtra("student");
			instructor = getIntent().getStringExtra("instructor");
			att = getIntent().getStringExtra("att");
			lt = getIntent().getStringExtra("lt");
			count = getIntent().getStringExtra("count");
			comment = getIntent().getStringExtra("comment");
			cancelId = getIntent().getStringExtra("cancelId");
			cancelenable = getIntent().getStringExtra("cancelenable");
			abc = getIntent().getStringExtra("abc");
		}
		else{
			onDetectNetworkState().show();
		}
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
				.isNetworkConnected(DetailCancelLessonActivity.this);
		if(isInternetPresent){
			Initialization();
		}
		else{
			onDetectNetworkState().show();
		}
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailCancelLessonActivity.this);
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        onBackPressed();
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
	private void Initialization() {
		// TODO Auto-generated method stub
		tv_date = (TextView)findViewById(R.id.tv_dcl_date_item);
		tv_day = (TextView)findViewById(R.id.tv_dcl_day_item);
		tv_time = (TextView)findViewById(R.id.tv_dcl_time_item);
		tv_student = (TextView)findViewById(R.id.tv_dcl_student_item);
		tv_instructor = (TextView)findViewById(R.id.tv_dcl_instructor_item);
		tv_att = (TextView)findViewById(R.id.tv_dcl_abbr_item);
		tv_lt = (TextView)findViewById(R.id.tv_dcl_lt_item);
		tv_count = (TextView)findViewById(R.id.tv_dcl_count_item);
		tv_comment = (TextView)findViewById(R.id.tv_dcl_comments_item);
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		btn_cancel_class = (Button)findViewById(R.id.btn_dcl_cancel_lesson);
		if(abc.toString().equalsIgnoreCase("abc")){
			new CancelClass().execute();
		}
		
		SetDisply();
	}
	private void SetDisply() {
		// TODO Auto-generated method stub
		tv_date.setText(date);
		tv_day.setText(day);
		tv_time.setText(time);
		tv_student.setText(student);
		tv_instructor.setText(instructor);
		tv_att.setText(att);
		tv_lt.setText(lt);
		tv_count.setText(count);
		if(comment.equalsIgnoreCase("Unpaid")){
			tv_comment.setTextColor(getResources().getColor(R.color.red));
		}
		else{
			tv_comment.setTextColor(getResources().getColor(R.color.app_text));
		}
		tv_comment.setText(comment);
		if(cancelenable.equalsIgnoreCase("true")){
			btn_cancel_class.setVisibility(View.VISIBLE);
			btn_cancel_class.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isInternetPresent){
						new CancelClass().execute();
					}
					else{
						onDetectNetworkState().show();
					}
				}
			});
		}
		else{
			btn_cancel_class.setVisibility(View.GONE);
		}
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
	
	private class CancelClass extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(DetailCancelLessonActivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("FamilyID",familyID );
			param.put("CancelClass", cancelId);
					
			String responseString = WebServicesCall.RunScript(AppConfiguration.cancelClass, param);
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
				Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_LONG).show();
				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void readAndParseJSON(String in) {
		try {
			JSONObject reader = new JSONObject(in);
			data_load = reader.getString("Success");
			if(data_load.toString().equals("True"))
			{
				
				JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
					message =  jsonChildNode.getString("Msg");
			}
			else{
				JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
				message =  jsonChildNode.getString("Msg");
			}

		}catch(JSONException e){
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	   }
}
