package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class GetFamilySwimNights extends Activity{

	LinearLayout list_registration,get_upcoming; 
	String SwimCampData = "False",UpTitle,Sc_title;
	String token, familyID;
	Context mContext=this;
	TextView title,txt_register,txt_checkin,upcoming_title,already_title;
	ArrayList<String>  _UpDate,_UpSite,_ReDate;
	Button register_btn;
	View sep1,sep2;
	Boolean isInternetPresent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_competition);
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");

		init();

        View view = findViewById(R.id.layout_top);

        TextView title = (TextView)view.findViewById(R.id.page_title);
        title.setText("Family Swim Night");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.menu_icon_new);
        LinearLayout ll_ProgressReport,ll_ViewCertificate,ll_RibbonCount;
        ll_ProgressReport = (LinearLayout) view.findViewById(R.id.scdl_lsn);
        ll_ViewCertificate = (LinearLayout) view.findViewById(R.id.scdl_mkp);
        ll_RibbonCount = (LinearLayout) view.findViewById(R.id.waitlist);

        View vert_1 = (View) view.findViewById(R.id.vert_1);
        vert_1.setVisibility(View.GONE);
        ll_ProgressReport.setVisibility(View.GONE);
        ll_ViewCertificate.setVisibility(View.GONE);
        TextView txt_2 = (TextView)view.findViewById(R.id.txt_2);
        TextView txt_3 = (TextView)view.findViewById(R.id.txt_3);

        txt_2.setText("Check In");
        txt_3.setText("Register");

        Button relMenu = (Button)view.findViewById(R.id.returnStack);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity.onLeft();
            }
        });

        ll_ViewCertificate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ll_RibbonCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,FamilySwimNightActivity.class);
                //				Intent i = new Intent(mContext,SwimCompititionRegisterAcitivity.class);
                startActivity(i);
                finish();
            }
        });

	}

	public void init(){
		list_registration = (LinearLayout)findViewById(R.id.list_registration);
		title = (TextView)findViewById(R.id.title);
		txt_checkin = (TextView)findViewById(R.id.txt_checkin);
		txt_register = (TextView)findViewById(R.id.txt_register);
		get_upcoming =  (LinearLayout)findViewById(R.id.get_upcoming);
		register_btn = (Button)findViewById(R.id.register_btn);
		sep1 = (View)findViewById(R.id.sep1);
		sep2 = (View)findViewById(R.id.sep2);
		already_title = (TextView)findViewById(R.id.already_title);
		upcoming_title = (TextView)findViewById(R.id.upcoming_title);

		//		register_btn.setVisibility(View.VISIBLE);

		sep1.setVisibility(View.GONE);
		sep2.setVisibility(View.VISIBLE);

		txt_checkin.setVisibility(View.GONE);
		txt_register.setVisibility(View.VISIBLE);
		isInternetPresent = Utility.isNetworkConnected(GetFamilySwimNights.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new GetUpcomingSwimNights().execute();
		}
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

		txt_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	class GetUpcomingSwimNights extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(GetFamilySwimNights.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("siteid",  AppConfiguration.basket_siteid);

			String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN+AppConfiguration.Get_UpcomingFamilySwimNights, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				SwimCampData = reader.getString("Success");
				UpTitle = reader.getString("Title");

				if (SwimCampData.toString().equals("True")) {

					_UpDate = new ArrayList<String>();
					_UpSite = new ArrayList<String>();

					JSONArray jsonMainNode = reader
							.optJSONArray("FinalArray");
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						_UpDate.add(jsonChildNode.getString("Date"));
						_UpSite.add(jsonChildNode.getString("SiteName"));
					}
				} else {

				}
			} catch (JSONException e) {
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
			if(SwimCampData.equals("True")){
				if(_UpDate.size()>0){
					setLayout_Upcoming();	
				}
			}else{
			}
			upcoming_title.setText(UpTitle);
			isInternetPresent = Utility.isNetworkConnected(GetFamilySwimNights.this);
			if (!isInternetPresent) {
				onDetectNetworkState().show();
			} else {
				new Get_FamilySwimNightRegistrations().execute();
				pd.dismiss();
			}
		}
	}

	public void setLayout_Upcoming(){

		for (int position = 0; position <_UpDate.size(); position++) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View view = inflater.inflate(R.layout.custom_swim_static_info, null);
			TextView Name = (TextView)view.findViewById(R.id.participant);
			TextView capacity = (TextView)view.findViewById(R.id.date);
			TextView start_date = (TextView)view.findViewById(R.id.location);
			TextView end_date = (TextView)view.findViewById(R.id.event);
			TextView age_group = (TextView)view.findViewById(R.id.age_group);
			TextView stroke = (TextView)view.findViewById(R.id.stroke);
			TextView distance = (TextView)view.findViewById(R.id.distance);

			start_date.setVisibility(View.GONE);
			end_date.setVisibility(View.GONE);
			age_group.setVisibility(View.GONE);
			stroke.setVisibility(View.GONE);
			distance.setVisibility(View.GONE);

			Name.setText(Html.fromHtml("<b>Date: </b>"+_UpDate.get(position)));
			capacity.setText(Html.fromHtml("<b>Site: </b>"+_UpSite.get(position)));

			get_upcoming.addView(view);
		}
	}

	class Get_FamilySwimNightRegistrations extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(GetFamilySwimNights.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("familyid",  familyID);
					
			String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN+AppConfiguration.Get_FamilySwimNightRegistrations, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				SwimCampData = reader.getString("Success");
				Sc_title = reader.getString("Title");

				if (SwimCampData.toString().equals("True")) {

					_ReDate = new ArrayList<String>();

					JSONArray jsonMainNode = reader
							.optJSONArray("FinalArray");
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						_ReDate.add(jsonChildNode.getString("Date"));
					}
				} else {

				}
			} catch (JSONException e) {
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
			if(SwimCampData.equals("True")){
				if(_ReDate.size()>0){
					SetLayout();
				}
			}else{
			}
			already_title.setText(Sc_title);
			pd.dismiss();
		}
	}

	public void SetLayout(){
		for (int position = 0; position <_ReDate.size(); position++) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View view = inflater.inflate(R.layout.custom_swim_static_info, null);
			TextView Name = (TextView)view.findViewById(R.id.participant);
			TextView capacity = (TextView)view.findViewById(R.id.date);
			TextView start_date = (TextView)view.findViewById(R.id.location);
			TextView end_date = (TextView)view.findViewById(R.id.event);
			TextView age_group = (TextView)view.findViewById(R.id.age_group);
			TextView stroke = (TextView)view.findViewById(R.id.stroke);
			TextView distance = (TextView)view.findViewById(R.id.distance);

			capacity.setVisibility(View.GONE);
			start_date.setVisibility(View.GONE);
			end_date.setVisibility(View.GONE);
			age_group.setVisibility(View.GONE);
			stroke.setVisibility(View.GONE);
			distance.setVisibility(View.GONE);

			Name.setText(Html.fromHtml("<b>Date: </b>"+_ReDate.get(position)));

			list_registration.addView(view);
		}
	}

}
