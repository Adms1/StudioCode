package com.waterworks;

import java.util.HashMap;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.RemoveLessonAdapter;
import com.waterworks.adapter.RemoveLessonAdapterInfo;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class CancelRemoveLessonActivityInfo extends Activity{

	TextView tv_cr_info,tv_notice_info,tv_cncl_name;
	CheckBox chb_cr_confirm;
	RemoveLessonAdapterInfo adapter;
	Button btn_release;
	CheckBox chb_confirm;
	ListView lv_cr_list;
	ImageButton ib_back;
	String token,familyID,formatedString;
	private static final String TAG = "Remove wait list";
	String successRelease;
	Boolean isInternetPresent = false;
	Context mContext=this;
	/**
	 * Confirm release a Class
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancel_remove_lesson_new_1);
		//setContentView(R.layout.activity_cancel_remove_lesson);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(CancelRemoveLessonActivityInfo.this);
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);


		Intent i = getIntent();
		formatedString = i.getStringExtra("formatedString");
		Log.d(TAG,"cancel Remove Lsson InfoformatedString="+formatedString);
		init();
	}
	public void init(){
		View view = findViewById(R.id.cancel_frag);
		ib_back = (ImageButton)view.findViewById(R.id.ib_back);

		tv_cr_info = (TextView)findViewById(R.id.tv_cr_info);
		tv_cr_info.setVisibility(View.GONE);
		tv_notice_info = (TextView)findViewById(R.id.tv_notice_info);
		tv_notice_info.setVisibility(View.GONE);
		chb_cr_confirm = (CheckBox)findViewById(R.id.chb_cr_confirm);
		chb_cr_confirm.setVisibility(View.GONE);
		btn_release = (Button)findViewById(R.id.btn_cr_save);
		chb_confirm = (CheckBox)findViewById(R.id.chb_cr_confirm);
		lv_cr_list = (ListView)findViewById(R.id.lv_cr_list);
		tv_cncl_name = (TextView)findViewById(R.id.tv_cncl_name);
		tv_cncl_name.setText("Confirm Release");
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_release.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CancelRemoveLessonActivity.AllTrue.contains("true")) {
					Intent i = new Intent(CancelRemoveLessonActivityInfo.this, CancelSurvey.class);
					i.putExtra("formatedString", formatedString);
					startActivity(i);
				} else if (CancelRemoveLessonActivity.AllTrue.contains("false")) {
					isInternetPresent = Utility.isNetworkConnected(CancelRemoveLessonActivityInfo.this);
					if (!isInternetPresent) {
						onDetectNetworkState().show();
					} else {
						new ReleaseAsyncTask().execute();
					}
				}
			}
		});

		lv_cr_list.setAdapter(adapter);

	}
	public static String replaceCharAt(String s, int pos, char c) {
		return s.substring(0, pos) + "" + s.substring(pos + 1);
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	class ReleaseAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(CancelRemoveLessonActivityInfo.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			releasingClass();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if(successRelease.toString().equals("True"))
			{
				Toast.makeText(getApplicationContext(), "The selected classes have been removed successfully.",  Toast.LENGTH_LONG).show();
			}
			else
			{
			}
		}
	}

	//================================== Release Class=============================

	public void releasingClass() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("FamilyID",familyID );
		param.put("RemoveClass", CancelRemoveLessonActivity.format_class);
		param.put("ScheduleIDs", CancelRemoveLessonActivity.ScheduleIDs);
		String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN+AppConfiguration.releaseClassURL_new, param);
		readAndParseJSON(responseString);

		System.out.println("Final array List responseString Cancel remove Lesson Activity Info: " + responseString);
		System.out.println("Final array List responseString Cancel remove Lesson Activity Info param: " + param);
	}


	public void readAndParseJSON(String in) {
		try {
			JSONObject reader = new JSONObject(in);
			successRelease = reader.getString("Success");
			if(successRelease.toString().equals("True"))
			{
				Intent intent = new Intent(CancelRemoveLessonActivityInfo.this, CancelRemoveLessonActivity.class);
                intent.putExtra("from", "All");
				startActivity(intent);
				finish();
			}
			else
			{
				Toast.makeText(CancelRemoveLessonActivityInfo.this, "Something's went wrong.", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
