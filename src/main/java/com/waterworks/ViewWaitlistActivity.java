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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.waterworks.adapter.ViewWaitListAdapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ViewWaitlistActivity extends Activity implements OnClickListener{
	ImageButton ib_back;
	Boolean isInternetPresent = false;
	private static final String TAG = "View wait list";
	String data_load="False";
	ArrayList<String> student,startDate,startTime,day,lessonType,instructor,classType,tbid;
	ListView lv_vw_list;
	Button btn_save;
	String token,familyID,msg; 
	TextView tv_vw_info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_waitlist);
		
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		
		isInternetPresent = Utility.isNetworkConnected(ViewWaitlistActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			new ViewWaitListLoad().execute();
		}
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		ib_back.setOnClickListener(this);
		lv_vw_list = (ListView)findViewById(R.id.lv_vw_list);
		btn_save = (Button)findViewById(R.id.btn_vw_save);
		tv_vw_info = (TextView)findViewById(R.id.tv_vw_info); 
	}
	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewWaitlistActivity.this);
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
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(ViewWaitlistActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		
	}
	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isInternetPresent){
			switch (v.getId()) {
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
	
	public class ViewWaitListLoad extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ViewWaitlistActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("FamilyID",familyID );

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.viewwaitlistpageload, param);
			try {
    			JSONObject reader = new JSONObject(responseString);
    			data_load = reader.getString("Success");
    			if(data_load.toString().equals("True"))
    			{
    				student = new ArrayList<String>();
    				startDate = new ArrayList<String>();
    				startTime = new ArrayList<String>();
    				day = new ArrayList<String>();
    				lessonType = new ArrayList<String>();
    				instructor = new ArrayList<String>();
    				classType = new ArrayList<String>();
    				tbid = new ArrayList<String>();
    				 JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
    		         for(int i = 0 ;i < jsonMainNode.length();i++)
    		         {
    		        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
    			         student.add(jsonChildNode.getString("student"));
    			         startDate.add(jsonChildNode.getString("startDate"));
    			         startTime.add(jsonChildNode.getString("startTime"));
    			         day.add(jsonChildNode.getString("day"));
    			         lessonType.add(jsonChildNode.getString("lessonType"));
    			         instructor.add(jsonChildNode.getString("instructor"));
    			         classType.add(jsonChildNode.getString("classType"));
    			         tbid.add(jsonChildNode.getString("tbid"));
    			         
    			         
    		         }
    			}
    			else{
    				 JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
    				 JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
    				 msg =  jsonChildNode.getString("Msg");
    				 
    			}
    		}
    		catch(JSONException e){
    			e.printStackTrace();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if(data_load.toString().equalsIgnoreCase("True")){
				btn_save.setVisibility(View.VISIBLE);
				lv_vw_list.setAdapter(new ViewWaitListAdapter(ViewWaitlistActivity.this,
						student, startDate, startTime, day, lessonType, instructor, classType, tbid,btn_save));
			}else{
				tv_vw_info.setText(msg);
				btn_save.setVisibility(View.GONE);
			}
		}
		
	}
}
