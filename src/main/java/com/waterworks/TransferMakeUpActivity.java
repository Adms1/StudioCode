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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.TransferMakeUpLessonAdapter;
import com.waterworks.adapter.TransferMakeupSiteAdapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class TransferMakeUpActivity extends Activity implements OnClickListener{
	private TabHost myTabHost;
	ImageButton ib_back;
	Boolean isInternetPresent = false;
	private static final String TAG = "Transfer make up";
	String data_load="False",data_load_site="False",data_load_lesson="False",transferdone="False",
			data_load_basket="False";
	TextView tv_tm_note1,tv_tm_note2,tv_tm_note3;
	ListView lv_site,lv_lesson;
	ArrayList<String> tbid,date,time,sttimehr,sttimemin,lessontype,lessonId,attendancetype,attendanceId,
	Name,firstName,lastName,siteId,siteName;
	ArrayList<String> p_tbid,p_date,p_time,p_sttimehr,p_sttimemin,p_lessontype,p_lessonId,p_attendancetype,p_attendanceId,
	p_Name,p_firstName,p_lastName,p_siteId,p_siteName;
	ArrayList<String> all_sitename,all_siteid,all_lessonname,all_lessonid;
	Boolean data_load_sp=false;
	LinearLayout ll_tm_sp_data;
	TextView tv_tm_sp_name1,tv_tm_sp_date1,tv_tm_sp_lesson1,tv_tm_sp_time1,tv_tm_sp_att1,tv_tm_sp_site1,
				tv_tm_sp_name2,tv_tm_sp_date2,tv_tm_sp_lesson2,tv_tm_sp_time2,tv_tm_sp_att2,tv_tm_sp_site2;
	Button btn_tm_sp_transfer;

	String token,familyID,msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_make_up);
		
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		
		isInternetPresent = Utility
				.isNetworkConnected(TransferMakeUpActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			new GetSiteList().execute();
			new GetLessonList().execute();
			new TransferMakeUpLoad().execute();
			if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
				new GetBasketID().execute();
			}
		}
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		myTabHost =(TabHost) findViewById(R.id.TabHost01);
		myTabHost.setup();
		myTabHost.getTabWidget().setDividerPadding(1);
		myTabHost.getTabWidget().setDividerDrawable(R.drawable.cell_shape);
		myTabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
		myTabHost.addTab(myTabHost.newTabSpec("Lesson").setIndicator("Lesson").setContent(R.id.onglet1));
		myTabHost.addTab(myTabHost.newTabSpec("Semi-private_lessons").setIndicator("Semi-Private").setContent(R.id.Onglet2));
		myTabHost.addTab(myTabHost.newTabSpec("Site").setIndicator("Site").setContent(R.id.Onglet3));
		myTabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.app_bg));
		
		TextView tv = (TextView) myTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextColor(Color.parseColor("#000000"));
        TextView tv1 = (TextView) myTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv1.setTextColor(Color.parseColor("#ffffff"));
        TextView tv2 = (TextView) myTabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        tv2.setTextColor(Color.parseColor("#ffffff"));
        myTabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.app_bg));
        myTabHost.getTabWidget().getChildAt(1).setBackgroundColor(getResources().getColor(R.color.text_blue));
        myTabHost.getTabWidget().getChildAt(2).setBackgroundColor(getResources().getColor(R.color.text_blue));
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
		for(int i=0;i<myTabHost.getTabWidget().getChildCount();i++)
		{
			myTabHost.getTabWidget().getChildAt(i).getLayoutParams().height =100;
			myTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title).setLayoutParams(rllp); 
		}
		myTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String paramString) {
				// TODO Auto-generated method stub
				for(int i=0;i<myTabHost.getTabWidget().getChildCount();i++)
				{
					myTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.text_blue));		
					TextView tv = (TextView) myTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
					tv.setTextColor(Color.parseColor("#ffffff"));
					
				}
					myTabHost.getTabWidget().getChildAt(myTabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.app_bg));
					TextView tv = (TextView) myTabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
			        tv.setTextColor(Color.parseColor("#000000"));

				
			}
		});
		
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		ib_back.setOnClickListener(this);
		tv_tm_note1 = (TextView)findViewById(R.id.tv_tm_note1);
		String arrow = "Transfer Makeups from one lesson type to another<br />--> ";
		String next = "<font color='#EE0000'>For each make up lesson you transfer, you will recieve 1 make up class of the selected type.</font>";
		tv_tm_note1.setText(Html.fromHtml(arrow + next));
		tv_tm_note2 = (TextView)findViewById(R.id.tv_tm_note2);
		String arrow2 = "One site to another site makeup transer<br />--> ";
		String next2 = "<font color='#EE0000'> * Transfer Makeups from one site to another.</font>";
		tv_tm_note2.setText(Html.fromHtml(arrow2 + next2));
		tv_tm_note3 = (TextView)findViewById(R.id.tv_tm_sp_note);
		String arrow3 = "Transfer 2 SP Makeups to a Private Makeup<br />--> ";
		String next3 = "<font color='#EE0000'> Transfer 2 Semi-Private Make up lessons into 1 Private Make up lesson.</font>";
		tv_tm_note3.setText(Html.fromHtml(arrow3+next3));
		lv_lesson = (ListView)findViewById(R.id.lv_tm_lesson_list);
		lv_site = (ListView)findViewById(R.id.lv_tm_site_list);
		ll_tm_sp_data = (LinearLayout)findViewById(R.id.ll_tm_sp_data);
		
		tv_tm_sp_name1 = (TextView)findViewById(R.id.tv_tm_sp_name1);
		tv_tm_sp_date1 = (TextView)findViewById(R.id.tv_tm_sp_date1);
		tv_tm_sp_lesson1 = (TextView)findViewById(R.id.tv_tm_sp_lesson1);
		tv_tm_sp_time1 = (TextView)findViewById(R.id.tv_tm_sp_time1);
		tv_tm_sp_att1 = (TextView)findViewById(R.id.tv_tm_sp_att1);
		tv_tm_sp_site1 = (TextView)findViewById(R.id.tv_tm_sp_site1);
		tv_tm_sp_name2 = (TextView)findViewById(R.id.tv_tm_sp_name2);
		tv_tm_sp_date2 = (TextView)findViewById(R.id.tv_tm_sp_date2);
		tv_tm_sp_lesson2 = (TextView)findViewById(R.id.tv_tm_sp_lesson2);
		tv_tm_sp_time2 = (TextView)findViewById(R.id.tv_tm_sp_time2);
		tv_tm_sp_att2 = (TextView)findViewById(R.id.tv_tm_sp_att2);
		tv_tm_sp_site2  = (TextView)findViewById(R.id.tv_tm_sp_site2);
		btn_tm_sp_transfer = (Button)findViewById(R.id.btn_tm_sp_transfer);
		
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(TransferMakeUpActivity.this);
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
				.isNetworkConnected(TransferMakeUpActivity.this);
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
	
	public class TransferMakeUpLoad extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(TransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token", ""+token);
			param.put("FamilyID", ""+familyID);

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.transfermakeupload, param);
			try {
    			JSONObject reader = new JSONObject(responseString);
    			data_load = reader.getString("Success");
    			if(data_load.toString().equals("True"))
    			{
    				tbid = new ArrayList<String>();
    				date = new ArrayList<String>();
    				time = new ArrayList<String>();
    				sttimehr = new ArrayList<String>();
    				sttimemin = new ArrayList<String>();
    				lessontype = new ArrayList<String>();
    				lessonId = new ArrayList<String>();
    				attendancetype = new ArrayList<String>();
    				attendanceId = new ArrayList<String>();
    				Name = new ArrayList<String>();
    				firstName = new ArrayList<String>();
    				lastName = new ArrayList<String>();
    				siteId = new ArrayList<String>();
    				siteName = new ArrayList<String>();
    				p_tbid = new ArrayList<String>();
    				p_date = new ArrayList<String>();
    				p_time = new ArrayList<String>();
    				p_sttimehr = new ArrayList<String>();
    				p_sttimemin = new ArrayList<String>();
    				p_lessontype = new ArrayList<String>();
    				p_lessonId = new ArrayList<String>();
    				p_attendancetype = new ArrayList<String>();
    				p_attendanceId = new ArrayList<String>();
    				p_Name = new ArrayList<String>();
    				p_firstName = new ArrayList<String>();
    				p_lastName = new ArrayList<String>();
    				p_siteId = new ArrayList<String>();
    				p_siteName = new ArrayList<String>();
    				 JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
    				 JSONArray jsonMainNode1 = reader.optJSONArray("PrivateArray");
    				 if(jsonMainNode.toString().equalsIgnoreCase("")){
    		         
    		         }
    		         else{
    		         for(int i = 0 ;i < jsonMainNode.length();i++)
    		         {
    		        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
    			         tbid.add(jsonChildNode.getString("tbid"));
    			         date.add(jsonChildNode.getString("date"));
    			         time.add(jsonChildNode.getString("time"));
    			         sttimehr.add(jsonChildNode.getString("sttimehr"));
	        				sttimemin.add(jsonChildNode.getString("sttimemin"));
	        				lessontype.add(jsonChildNode.getString("lessontype"));
	        				lessonId.add(jsonChildNode.getString("lessonId"));
	        				attendancetype.add(jsonChildNode.getString("attendancetype"));
	        				attendanceId.add(jsonChildNode.getString("attendanceId"));
	        				Name.add(jsonChildNode.getString("name"));
	        				firstName.add(jsonChildNode.getString("firstName"));
	        				lastName.add(jsonChildNode.getString("lastName"));
	        				siteId.add(jsonChildNode.getString("siteId"));
	        				siteName.add(jsonChildNode.getString("siteName"));
    		         }
    		         }
    				 if(jsonMainNode1.toString().equalsIgnoreCase("[]")){
    					 tv_tm_note3.setText("No semi-private_lessons lesson for transfer.");
    					 ll_tm_sp_data.setVisibility(View.GONE);
    				 }
    				 else{
    					 data_load_sp = true;
    					 ll_tm_sp_data.setVisibility(View.VISIBLE);
    					 for(int i = 0 ;i < jsonMainNode1.length();i++)
        		         {
        		        	 JSONObject jsonChildNode = jsonMainNode1.getJSONObject(i);
        					 p_tbid.add(jsonChildNode.getString("tbid"));
        					 p_date.add(jsonChildNode.getString("date"));
        					 p_time.add(jsonChildNode.getString("time"));
        					 p_sttimehr.add(jsonChildNode.getString("sttimehr"));
        					 p_sttimemin.add(jsonChildNode.getString("sttimemin"));
        					 p_lessontype.add(jsonChildNode.getString("lessontype"));
        					 p_lessonId.add(jsonChildNode.getString("lessonId"));
        					 p_attendancetype.add(jsonChildNode.getString("attendancetype"));
        					 p_attendanceId.add(jsonChildNode.getString("attendanceId"));
        					 p_Name.add(jsonChildNode.getString("name"));
        					 p_firstName.add(jsonChildNode.getString("firstName"));
        					 p_lastName.add(jsonChildNode.getString("lastName"));
        					 p_siteId.add(jsonChildNode.getString("siteId"));
        					 p_siteName.add(jsonChildNode.getString("siteName"));
        					 btn_tm_sp_transfer.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View paramView) {
									// TODO Auto-generated method stub
									new TransferSemiPrivateData().execute();
								}
							});
        				 }
    				 }
    			}
    			else{
    				message = reader.getString("Message");
    				
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
				lv_site.setAdapter(new TransferMakeupSiteAdapter(tbid, date, time, sttimehr, sttimemin,
						lessontype, lessonId, attendancetype, attendanceId, Name, firstName,
						lastName, siteId, siteName, all_siteid,all_sitename,TransferMakeUpActivity.this));
				lv_lesson.setAdapter(new TransferMakeUpLessonAdapter(tbid, date, time, sttimehr, sttimemin,
						lessontype, lessonId, attendancetype, attendanceId, Name, firstName,
						lastName, siteId, siteName, all_lessonid,all_lessonname,TransferMakeUpActivity.this));
				 if(data_load_sp){
					 data_load_sp = false;
					 ll_tm_sp_data.setVisibility(View.VISIBLE);
					 tv_tm_sp_name1.setText(Html.fromHtml("<b>Name:</b>"+p_Name.get(0)));
					 tv_tm_sp_name2.setText(Html.fromHtml("<b>Name:</b>"+p_Name.get(1)));
					 tv_tm_sp_lesson1.setText(Html.fromHtml("<b>Lesson:</b>"+p_lessontype.get(0)));
					 tv_tm_sp_lesson2.setText(Html.fromHtml("<b>Lesson:</b>"+p_lessontype.get(1)));
					 tv_tm_sp_att1.setText(Html.fromHtml("<b>Att:</b>"+p_attendancetype.get(0)));
					 tv_tm_sp_att2.setText(Html.fromHtml("<b>Att:</b>"+p_attendancetype.get(1)));
					 tv_tm_sp_date1.setText(Html.fromHtml("<b>Date:</b>"+p_date.get(0)));
					 tv_tm_sp_date2.setText(Html.fromHtml("<b>Date:</b>"+p_date.get(1)));
					 tv_tm_sp_time1.setText(Html.fromHtml("<b>Time:</b>"+p_time.get(0)));
					 tv_tm_sp_time2.setText(Html.fromHtml("<b>Time:</b>"+p_time.get(1)));
					 tv_tm_sp_site1.setText(Html.fromHtml("<b>Site:</b>"+p_siteName.get(0)));
					 tv_tm_sp_site2.setText(Html.fromHtml("<b>Site:</b>"+p_siteName.get(1)));
				 }
				 else{
					 ll_tm_sp_data.setVisibility(View.GONE);
				 }
			}else{
//				Toast.makeText(getApplicationContext(), "No lesson to transfer.", Toast.LENGTH_LONG).show();
				ll_tm_sp_data.setVisibility(View.GONE);
				tv_tm_note1.setText(message);
				tv_tm_note2.setText(message);
				tv_tm_note3.setText(message);
			}
		}
	}
	String message;
	private class GetSiteList extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(TransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			
			String responseString = WebServicesCall
			.RunScript(AppConfiguration.getSiteListURL, param);
			try {
    			JSONObject reader = new JSONObject(responseString);
    			data_load_site = reader.getString("Success");
    			if(data_load_site.toString().equals("True")){
                	all_siteid = new ArrayList<String>();
                	all_sitename = new ArrayList<String>();
    				
                	
                	JSONArray jsonMainNode = reader.optJSONArray("Sites");

    				for (int i = 0; i < jsonMainNode.length(); i++) {
    					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

    					all_siteid.add(jsonChildNode.getString("SiteID"));
    					all_sitename.add(jsonChildNode.getString("SiteName"));
    				}
    			}
			}
            catch(JSONException e){
            	e.printStackTrace();
            }
            catch (Exception e) {
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
			if(data_load_site.toString().equalsIgnoreCase("True")){
				
			}else{
				Toast.makeText(TransferMakeUpActivity.this, "Some internaml error,Please try after sometime.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	private class GetLessonList extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(TransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String, String > param = new HashMap<String, String>();

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.getInstructorPreferences, param);
			try {
    			JSONObject reader = new JSONObject(responseString);
    			data_load_lesson = reader.getString("Success");
    			if(data_load_lesson.toString().equals("True")){
                	all_lessonid = new ArrayList<String>();
                	all_lessonname = new ArrayList<String>();
    				
                	
                	JSONArray jsonMainNode = reader.optJSONArray("Lesson");

    				for (int i = 0; i < jsonMainNode.length(); i++) {
    					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

    					all_lessonid.add(jsonChildNode.getString("lessonid"));
    					all_lessonname.add(jsonChildNode.getString("lessonname"));
    				}
    			}
			}
            catch(JSONException e){
            	e.printStackTrace();
            }
            catch (Exception e) {
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
			if(data_load_lesson.toString().equalsIgnoreCase("True")){
			}else{
				Toast.makeText(TransferMakeUpActivity.this, "Some internaml error,Please try after sometime.", Toast.LENGTH_LONG).show();
			}
		}

	}
	

	private class TransferSemiPrivateData extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(TransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... paramVarArgs) {
			// TODO Auto-generated method stub
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("FamilyID",familyID );
			param.put("BasketID", AppConfiguration.BasketID);
			param.put("Selected",p_tbid.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.transfermakeup_privatelesson_transfer, param);
			try {
    			JSONObject reader = new JSONObject(responseString);
    			transferdone = reader.getString("Success");
    			if(transferdone.toString().equalsIgnoreCase("true")){
    				 JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
    				 JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
    				 msg = jsonChildNode.getString("showCart");
    			}
            }
            catch(JSONException e){
            	e.printStackTrace();
            }
            catch(Exception e){
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
			if(transferdone.toString().equalsIgnoreCase("True")){
				Toast.makeText(getApplicationContext(), "Lesson Transferred.", Toast.LENGTH_LONG).show();
				if(msg.toString().equalsIgnoreCase("True")){
					Intent i = new Intent(getApplicationContext(),ViewCartActivity.class);
					startActivity(i);
				}
				else{
					finish();
				}
				
			}
			else{
				Toast.makeText(getApplicationContext(), "Not able to transfer lesson.", Toast.LENGTH_LONG).show();
			}
		}
	}
	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(TransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("siteid", "0");

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.Get_BasketID, param);
			GetBasketID(responseString);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if(data_load_basket.toString().equalsIgnoreCase("True")){
				if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
//					Toast.makeText(TransferMakeUpActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
				}
				else{
//					new swimlessonsubmit().execute();
				}
			}
			else{
//				Toast.makeText(TransferMakeUpActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
			}
		}
	}
	public void GetBasketID(String response){
		try {
			JSONObject reader = new JSONObject(response);
			data_load_basket = reader.getString("Success");
			if(data_load_basket.toString().equals("True"))
			{
				 JSONArray jsonMainNode = reader.optJSONArray("BasketDtl");
		         if(jsonMainNode.toString().equalsIgnoreCase("")){
		         
		         }
		         else{
		         for(int i = 0 ;i < jsonMainNode.length();i++)
		         {
		        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			        AppConfiguration.BasketID = jsonChildNode.getString("Basketid");
		         }
		         }
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
