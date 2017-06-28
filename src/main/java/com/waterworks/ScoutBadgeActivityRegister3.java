package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoutBadgeActivityRegister3 extends Activity{

	Button relMenu;
	private static String TAG = "ScoutActivity3";
	LinearLayout ll_list;
	public ArrayList<String> FullName,Studentid,tbid,sessionname,startdate,enddate,unitprice,sitename,finalstulist, remarks, time;
	TextView tv_dt3_total_prc_sess,tv_dt3_outstndg_bal,tv_dt3_total_sess_ent,tv_dt3_total_ch_reg;
	String int_childno, strStuList, sessonno, childno, studlist, names;
	String token, familyID, dataload="False", siteID, basketID;
	CardView btn_dt3_addtocart;
	Context mContext = this;
	Boolean isInternetPresent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoutactivityregister3);

		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
        siteID = getIntent().getStringExtra("siteid");

        basketID = Utility.getBasketID(this, siteID);

		Log.i(TAG, getIntent().getStringExtra("siteid"));
		System.out.println("Array : "+ScoutBadgeActivityRegister2.mChecked);
		
		int_childno = getIntent().getStringExtra("childno");
		strStuList = getIntent().getStringExtra("strStuList");
		isInternetPresent = Utility.isNetworkConnected(ScoutBadgeActivityRegister3.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new GetDataForList().execute();
			init();
		}
	}
		public void init(){
		View include_layout_step_boxes = (View) findViewById(R.id.include_layout_step_boxes);
		TextView txtBox3 = (TextView) include_layout_step_boxes.findViewById(R.id.txtBox3);
		txtBox3.setTextColor(Color.WHITE);
		txtBox3.setBackgroundColor(getResources().getColor(R.color.design_change_d2));
		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCheckin = new Intent(ScoutBadgeActivityRegister3.this, DashBoardActivity.class);
				intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCheckin);
				finish();
			}
		});
		relMenu = (Button)findViewById(R.id.relMenu);
		ll_list = (LinearLayout) findViewById(R.id.ll_list);
		tv_dt3_total_prc_sess = (TextView)findViewById(R.id.tv_dt3_total_prc_sess);
		tv_dt3_total_sess_ent = (TextView)findViewById(R.id.tv_dt3_total_sess_ent);
		tv_dt3_total_ch_reg = (TextView)findViewById(R.id.tv_dt3_total_ch_reg);
		btn_dt3_addtocart = (CardView)findViewById(R.id.btn_dt3_addtocart);
		
		FullName = new ArrayList<String>();
		Studentid = new ArrayList<String>();
		tbid = new ArrayList<String>();
		sessionname = new ArrayList<String>();
		startdate = new ArrayList<String>();
		enddate = new ArrayList<String>();
		unitprice = new ArrayList<String>();
		sitename = new ArrayList<String>();
		finalstulist = new ArrayList<String>();
		remarks = new ArrayList<String>();
		time = new ArrayList<>();
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		relMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		btn_dt3_addtocart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isInternetPresent = Utility.isNetworkConnected(ScoutBadgeActivityRegister3.this);
				if (!isInternetPresent) {
					onDetectNetworkState().show();
				} else {
					new AddToCart().execute();
				}
			}
		});
	}
	public int total(){
		int total=0;
		for (int i = 0; i < unitprice.size(); i++) {
			Float fl = Float.parseFloat(unitprice.get(i));
			total = total + Math.round(fl);
		}
		System.out.println("Total : "+total);
		return total;
	}
	
	public String array_spliter(ArrayList<String> list1,ArrayList<String> list2){
		String listString = "";
		ArrayList<String> combine = new ArrayList<String>();
		for (int i = 0; i < list1.size(); i++) {
			combine.add(list1.get(i) + ":" +list2.get(i));
		}

		for (String s : combine)
		{
			listString += s +",";
		}
		
		listString = method(listString.trim());
		
		return listString;
	}
	
	public String method(String str) {
	      str = str.substring(0, str.length()-1);
	    return str;
	}

	public void loadList(){
		TextView tv_name,tv_sess,tv_desc,tv_stdate,tv_enddate,tv_site,tv_price,tv_remark;
		CheckBox chb_add;
		try{
			for(int i = 0;i < FullName.size();i++) {
				View convertView = LayoutInflater.from(ScoutBadgeActivityRegister3.this).inflate(R.layout.dive_turns_2_item, null);
				tv_name = (TextView) convertView.findViewById(R.id.dt2_name);
				tv_sess = (TextView) convertView.findViewById(R.id.dt2_sess);
				tv_desc = (TextView) convertView.findViewById(R.id.dt2_desc);
				tv_stdate = (TextView) convertView.findViewById(R.id.dt2_stdate);
				tv_enddate = (TextView) convertView.findViewById(R.id.dt2_eddate);
				tv_site = (TextView) convertView.findViewById(R.id.dt2_site);
				tv_price = (TextView) convertView.findViewById(R.id.dt2_untprice);
				tv_remark = (TextView) convertView.findViewById(R.id.dt2_remark);
				chb_add = (CheckBox)convertView.findViewById(R.id.dt2_add);

				chb_add.setVisibility(View.GONE);
				tv_remark.setVisibility(View.GONE);
				tv_name.setText(Html.fromHtml("<b>Participant:</b> " + FullName.get(i)));
				tv_sess.setText(Html.fromHtml("<b>Session#:</b> " + tbid.get(i)));
				tv_desc.setText(Html.fromHtml("<b>Description:</b> " + sessionname.get(i)));
				tv_stdate.setText(Html.fromHtml("<b>Start Date:</b> " + startdate.get(i)));
				tv_enddate.setText(Html.fromHtml("<b>Start Time:</b> " + time.get(i)));
				tv_site.setText(Html.fromHtml("<b>Site:</b> " + sitename.get(i)));
				tv_price.setText(Html.fromHtml("<b>Unit Price:</b> " + unitprice.get(i)));
				ll_list.addView(convertView);
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private class AddToCart extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ScoutBadgeActivityRegister3.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("basketid", basketID);
			param.put("siteid",""+siteID);
			param.put("Studlist", ""+array_spliter(Studentid, tbid));

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.DOMAIN+AppConfiguration.Prg_SwimmingActivityBadges_AddToCart, param);
			
			try {
				JSONObject reader = new JSONObject(responseString);
				dataload= reader.getString("Success");
				if(dataload.toString().equalsIgnoreCase("True")){
                    Intent i = new Intent(ScoutBadgeActivityRegister3.this,ByMoreMyCart.class);
					startActivity(i);
				}
				else{
				}
			}catch(JSONException e){
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
		}
	}
	
	
String TotalChildrenRegistered="",TotalSessionsEntered="",TotalSessionsPrice="";
	
	private class GetDataForList extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ScoutBadgeActivityRegister3.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
            HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("siteid",""+siteID);
			param.put("strStuList", strStuList);
			param.put("basketid", basketID);

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.DOMAIN+AppConfiguration.Prg_Get_SwimmingActivityBadges_Step3, param);
			
			try {
				JSONObject reader = new JSONObject(responseString);
				dataload= reader.getString("Success");
				if(dataload.toString().equalsIgnoreCase("True")){
					
					TotalChildrenRegistered = reader.getString("TotalChildrenRegistered");
					TotalSessionsEntered = reader.getString("TotalSessionsEntered");
					TotalSessionsPrice = reader.getString("TotalSessionsPrice");
					
					JSONArray jArray = reader.getJSONArray("StudentSessionList");
					
					if(jArray.length()>0){
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jobj = jArray.getJSONObject(i);
							JSONArray jChildArray = jobj.getJSONArray("StudSessionDtl");
							for (int j = 0; j < jChildArray.length(); j++) {
								JSONObject jchildobj = jChildArray.getJSONObject(j);
								Studentid.add(jchildobj.getString("StudentID"));
								FullName.add(jchildobj.getString("StudentName"));
								sessionname.add(jchildobj.getString("Description"));
								startdate.add(jchildobj.getString("Date"));
								tbid.add(jchildobj.getString("Sess#"));
								unitprice.add(jchildobj.getString("Price"));
                                time.add(jchildobj.getString("Time"));
								sitename.add(ScoutBadgeActivityRegister2.SwimmingBadgeActivityEventList.get(i).get("sitename"));
                                remarks.add(ScoutBadgeActivityRegister2.SwimmingBadgeActivityEventList.get(i).get("Remark"));
							}
						}
					}
				}
				else{
				}
			}catch(JSONException e){
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
			
			if(dataload.toString().equalsIgnoreCase("True")){
				if(FullName.size()>0){
					loadList();
					tv_dt3_total_prc_sess.setText("$ "+ TotalSessionsPrice);
					tv_dt3_total_sess_ent.setText(TotalSessionsEntered);
					tv_dt3_total_ch_reg.setText(TotalChildrenRegistered);
				}else{
					System.out.println("Error : Empty Array!!!");
				}
			}
		}
	}

}
