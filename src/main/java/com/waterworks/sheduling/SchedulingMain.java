/**
 * 
 */
package com.waterworks.sheduling;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

/**
 * @author Harsh Adms
 *
 */

public class SchedulingMain extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	LinearLayout scdl_lsn,scdl_mkp,waitlist;
	View selected_1,selected_2,selected_3;
	TextView txt_1,txt_2,txt_3,noti_count;
	int dispPOS=0;
	Button BackButton;

	String data_load= "False";
	String token,familyID,Mup_cnt; 
	Boolean isInternetPresent =false;
	Context mContext =this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduling_main);
		Initialize();
		dispPOS = getIntent().getIntExtra("POS", 0);
		displayView(dispPOS);
		((AnimationDrawable) selected_1.getBackground()).start();

		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d("HomeFragment","Token="+token+"\nFamilyID="+familyID);
		isInternetPresent = Utility.isNetworkConnected(SchedulingMain.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new GetRemDetail().execute();
		}
	}

	public void Initialize(){
		scdl_lsn = (LinearLayout)findViewById(R.id.scdl_lsn);
		scdl_mkp = (LinearLayout)findViewById(R.id.scdl_mkp);
		waitlist = (LinearLayout)findViewById(R.id.waitlist);

		selected_1 = (View)findViewById(R.id.selected_1);
		selected_2 = (View)findViewById(R.id.selected_2);
		selected_3 = (View)findViewById(R.id.selected_3);

		txt_1 = (TextView)findViewById(R.id.txt_1);
		txt_2 = (TextView)findViewById(R.id.txt_2);
		txt_3 = (TextView)findViewById(R.id.txt_3);

		noti_count = (TextView)findViewById(R.id.noti_count);
		
		BackButton = (Button)findViewById(R.id.relMenu);

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
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		BackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		scdl_lsn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				scdl_lsn.setBackgroundResource(R.color.design_change_d2);
				scdl_mkp.setBackgroundResource(R.color.design_change_d2);
				waitlist.setBackgroundResource(R.color.design_change_d2);

				selected_1.setVisibility(View.VISIBLE);
				selected_2.setVisibility(View.GONE);
				selected_3.setVisibility(View.GONE);

				txt_1.setTextColor(Color.WHITE);
				txt_2.setTextColor(Color.WHITE);
				txt_3.setTextColor(Color.WHITE);

				displayView(0);

				((AnimationDrawable) selected_1.getBackground()).start();
			}
		});
		scdl_mkp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scdl_lsn.setBackgroundResource(R.color.design_change_d2);
				scdl_mkp.setBackgroundResource(R.color.design_change_d2);
				waitlist.setBackgroundResource(R.color.design_change_d2);

				selected_1.setVisibility(View.GONE);
				selected_2.setVisibility(View.VISIBLE);
				selected_3.setVisibility(View.GONE);

				txt_1.setTextColor(Color.WHITE);
				txt_2.setTextColor(Color.WHITE);
				txt_3.setTextColor(Color.WHITE);
				displayView(1);
				((AnimationDrawable) selected_2.getBackground()).start();
			}
		});
		waitlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scdl_lsn.setBackgroundResource(R.color.design_change_d2);
				scdl_mkp.setBackgroundResource(R.color.design_change_d2);
				waitlist.setBackgroundResource(R.color.design_change_d2);

				selected_1.setVisibility(View.GONE);
				selected_2.setVisibility(View.GONE);
				selected_3.setVisibility(View.VISIBLE);

				txt_1.setTextColor(Color.WHITE);
				txt_2.setTextColor(Color.WHITE);
				txt_3.setTextColor(Color.WHITE);
				displayView(2);

				((AnimationDrawable) selected_3.getBackground()).start();
			}
		});
	}

	//FragmentWork
	Fragment fragment = null;
	int myid;
	private void displayView(int position) {
		switch (position) {
//		case 0:
//			AppConfiguration.makeup_Clicked=0;
//			fragment = new ScheduleLessonFragement();
//			myid = fragment.getId();
//			break;
//		case 1:
//			fragment = new ScheduleMakeupFragment();
//			break;
//		case 2:
//			fragment = new WaitListFragment();
//			break;
//
//		case 3:
//			fragment = new ScheduleLessonFragement2();
//			break;
//		case 4:
//			fragment = new ScheduleLessonFragement3();
//			break;
//		case 5:
//			fragment = new ScheduleLessonFragement4();
//			break;
//		case 6:
//			fragment = new ScheduleLessonFragement5();
//			break;
//		case 7:
//			fragment = new ScheduleLessonFragement6();
//			break;
		}

		if (fragment != null) {
			fragment.setRetainInstance(true);
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.addToBackStack(null)
			.replace(R.id.frame_container, fragment).commit();
		} else {
			// error in creating fragment
			Log.e("Dashboard", "Error in creating fragment");
		}
	}

	public void schedulingStep(int i){
		if(i==1){
			displayView(0);
		}else if(i==2){
			displayView(3);
		}else if(i==3){
			displayView(4);
		}else if(i==4){
			displayView(5);
		}else if(i==5){
			displayView(6);
		}else if(i==6){
			displayView(7);
		}
	}

	private class GetRemDetail extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(SchedulingMain.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("familyid",familyID );

			String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN
					+AppConfiguration.Schl_Get_MakeupCountByFamily, param);

			try {
				data_load= "False";

				JSONObject reader = new JSONObject(responseString);
				data_load = reader.getString("Success");
				if(data_load.toString().equals("True")){
					Mup_cnt = reader.getString("Mup_cnt");
				}else{
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
			if(data_load.toString().equals("True")){
				noti_count.setText(Mup_cnt);
//				txt_2.setText("Schedule Makeup( "+Mup_cnt +" )");
			}else{
				
			}
		}
	}
}
