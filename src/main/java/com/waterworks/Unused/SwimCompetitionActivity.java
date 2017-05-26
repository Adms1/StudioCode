package com.waterworks.Unused;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.SwimCompititionRegisterAcitivity;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

public class SwimCompetitionActivity extends Activity {

	LinearLayout list_registration,get_upcoming; 
	ImageButton ib_back;
	String SwimCampData = "False",Title;
	String token, familyID,Sc_title;
	Context mContext=this;
	TextView title,txt_register,txt_checkin,upcoming_title,already_title;
	ArrayList<String>  _Participant,_Date,_Location,_EventNo,_AgeGroup,_Stroke,_Distance,_UpcomingDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_competition);
		// getting token
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");

		init();
	}

	public void init(){
//		View view = findViewById(R.id.layout_top);
//		ib_back = (ImageButton)view.findViewById(R.id.ib_back);

		list_registration = (LinearLayout)findViewById(R.id.list_registration);
		get_upcoming= (LinearLayout)findViewById(R.id.get_upcoming);
		title = (TextView)findViewById(R.id.title);
		txt_checkin = (TextView)findViewById(R.id.txt_checkin);
		txt_register = (TextView)findViewById(R.id.txt_register);
		already_title = (TextView)findViewById(R.id.already_title);
		upcoming_title = (TextView)findViewById(R.id.upcoming_title);

        View view = findViewById(R.id.layout_top);
        TextView title = (TextView)view.findViewById(R.id.page_title);
        title.setText("Programs");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.menu_icon_new);
        LinearLayout ll_ProgressReport,ll_ViewCertificate,ll_RibbonCount;
        ll_ProgressReport = (LinearLayout) view.findViewById(R.id.scdl_lsn);
        ll_ViewCertificate = (LinearLayout) view.findViewById(R.id.scdl_mkp);
        ll_RibbonCount = (LinearLayout) view.findViewById(R.id.waitlist);

        View vert_1 = (View) view.findViewById(R.id.vert_1);
        vert_1.setVisibility(View.GONE);
        ll_ProgressReport.setVisibility(View.GONE);

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
                Intent i = new Intent(mContext,DashBoardActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("POS", 1);
                startActivity(i);
                finish();
            }
        });

        ll_RibbonCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,SwimCompititionRegisterAcitivity.class);
                startActivity(i);
                finish();
            }
        });

		
		new GetSwimCompDetail().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


		txt_checkin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		txt_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.swim_competition, menu);
		return true;
	}

	class GetUpcomingSwimsDate extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(SwimCompetitionActivity.this);
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

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.DOMAIN+AppConfiguration.Get_UpcomingSwimcompetitions, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				SwimCampData = reader.getString("Success");
				Sc_title = reader.getString("Title");
				if (SwimCampData.toString().equals("True")) {

					_UpcomingDate = new ArrayList<String>();

					JSONArray jsonMainNode = reader
							.optJSONArray("FinalArray");
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						_UpcomingDate.add(jsonChildNode.getString("Date"));
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
				if(_UpcomingDate.size()>0){
					setLayout_Upcoming();
				}
			}
			upcoming_title.setText(Sc_title);
			pd.dismiss();
			
		}
	}

	public void setLayout_Upcoming(){
		for (int position = 0; position <_UpcomingDate.size(); position++) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View view = inflater.inflate(R.layout.custom_textview, null);

			TextView dates_tv = (TextView)view.findViewById(R.id.text);
			System.out.println("Dates : "+_UpcomingDate.get(position));
			String fetched_date = _UpcomingDate.get(position).trim();
			dates_tv.setText(fetched_date);
			
			get_upcoming.addView(view);
		}
		
	}
	private class GetSwimCompDetail extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pd = new ProgressDialog(SwimCompetitionActivity.this);
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
			param.put("FamilyID",familyID );

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.DOMAIN+AppConfiguration.Get_SwimCompetitions_Registrations, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				SwimCampData = reader.getString("Success");
				Title = reader.getString("Title");

				if (SwimCampData.toString().equals("True")) {

					_AgeGroup = new ArrayList<String>();
					_Date = new ArrayList<String>();
					_Distance = new ArrayList<String>();
					_EventNo = new ArrayList<String>();
					_Location = new ArrayList<String>();
					_Participant = new ArrayList<String>();
					_Stroke = new ArrayList<String>();

					JSONArray jsonMainNode = reader
							.optJSONArray("FinalArray");
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						_AgeGroup.add(jsonChildNode.getString("Age Group"));
						_Date.add(jsonChildNode.getString("Date"));
						_Distance.add(jsonChildNode.getString("Distance"));
						_EventNo.add(jsonChildNode.getString("EventNo"));
						_Location.add(jsonChildNode.getString("Location"));
						_Participant.add(jsonChildNode.getString("Participant"));
						_Stroke.add(jsonChildNode.getString("Stroke"));

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
				if(_Participant.size()>0){
					SetLayout();	
				}
			}else{
			}
			already_title.setText(Title);
			pd.dismiss();
			new GetUpcomingSwimsDate().execute();
		}
	}


	public void SetLayout(){

		for (int position = 0; position <_Participant.size(); position++) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View view = inflater.inflate(R.layout.custom_swim_static_info, null);
			TextView participant = (TextView)view.findViewById(R.id.participant);
			TextView date = (TextView)view.findViewById(R.id.date);
			TextView location = (TextView)view.findViewById(R.id.location);
			TextView event = (TextView)view.findViewById(R.id.event);
			TextView age_group = (TextView)view.findViewById(R.id.age_group);
			TextView stroke = (TextView)view.findViewById(R.id.stroke);
			TextView distance = (TextView)view.findViewById(R.id.distance);

			participant.setText(Html.fromHtml("<b>Participant: </b>"+_Participant.get(position)));
			date.setText(Html.fromHtml("<b>Date: </b>"+_Date.get(position)));
			location.setText(Html.fromHtml("<b>Location: </b>"+_Location.get(position)));
			event.setText(Html.fromHtml("<b>Event: </b>"+_EventNo.get(position)));
			age_group.setText(Html.fromHtml("<b>Age Group: </b>"+_AgeGroup.get(position)));
			stroke.setText(Html.fromHtml("<b>Stroke: </b>"+_Stroke.get(position)));
			distance.setText(Html.fromHtml("<b>Distance: </b>"+_Distance.get(position)));

			list_registration.addView(view);
		}
	}

}
