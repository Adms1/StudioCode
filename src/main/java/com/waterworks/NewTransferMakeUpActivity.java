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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class NewTransferMakeUpActivity extends Activity implements
		OnClickListener {
	ImageButton ib_back;
	Boolean isInternetPresent = false;
	private static final String TAG = "Transfer make up";
	String data_load = "False", data_load_site = "False",
			data_load_lesson = "False", transferdone = "False",
			data_load_basket = "False";
	public static ArrayList<String> tbid, date, time, sttimehr, sttimemin,
			lessontype, lessonId, attendancetype, attendanceId, Name,
			firstName, lastName, siteId, siteName;
	public static ArrayList<String> p_tbid, p_date, p_time, p_sttimehr,
			p_sttimemin, p_lessontype, p_lessonId, p_attendancetype,
			p_attendanceId, p_Name, p_firstName, p_lastName, p_siteId,
			p_siteName;
	public static ArrayList<String> all_sitename, all_siteid, all_lessonname,
			all_lessonid;
	Boolean data_load_sp = false;

	String token, familyID, msg;
	Button btn_lesson, btn_site, btn_semitoprvt;
	TextView tv_make_up_counts;
	LinearLayout ll_trnsfr_makeups_count;
TextView tv_tsm_error;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_transfer_make_up);

		// getting token
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

		isInternetPresent = Utility
				.isNetworkConnected(NewTransferMakeUpActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			Initialization();
			new GetSiteList().execute();
			new GetLessonList().execute();
			new TransferMakeUpLoad().execute();
			new MakeUpCounts().execute();
			if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
				new GetBasketID().execute();
			}
		}
	}

	private void Initialization() {
		// TODO Auto-generated method stub

		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ib_back.setOnClickListener(this);
		btn_lesson = (Button) findViewById(R.id.btn_trnsfr_to_lesson);
		btn_site = (Button) findViewById(R.id.btn_trnsfr_to_site);
		btn_semitoprvt = (Button) findViewById(R.id.btn_trnsfr_to_semi_to_private);
		btn_lesson.setOnClickListener(this);
		btn_semitoprvt.setOnClickListener(this);
		btn_site.setOnClickListener(this);
		tv_make_up_counts = (TextView)findViewById(R.id.tv_make_up_counts);
		ll_trnsfr_makeups_count = (LinearLayout)findViewById(R.id.ll_trnsfr_makeups_count);
		tv_tsm_error = (TextView)findViewById(R.id.tv_tsm_error);
		tv_tsm_error.setVisibility(View.GONE);
	}
	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				NewTransferMakeUpActivity.this);
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								onBackPressed();
							}
						})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
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
				.isNetworkConnected(NewTransferMakeUpActivity.this);
		if (!isInternetPresent) {
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
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.ib_back:
				onBackPressed();
				break;
			case R.id.btn_trnsfr_to_lesson:
				Intent lessonIntent = new Intent(NewTransferMakeUpActivity.this, TransferLessonActivity.class);
				startActivity(lessonIntent);
				
				break;
			case R.id.btn_trnsfr_to_semi_to_private:

				break;
			case R.id.btn_trnsfr_to_site:
				Intent siteIntent = new Intent(NewTransferMakeUpActivity.this, TransferSiteActivity.class);
				startActivity(siteIntent);
				break;
			default:
				break;
			}
		} else {
			onDetectNetworkState().show();
		}
	}

	public class TransferMakeUpLoad extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(NewTransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params1) {
			// TODO Auto-generated method stub

			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
			params.put("FamilyID",familyID );
					
			String responseString = WebServicesCall.RunScript(
					AppConfiguration.transfermakeupload, params);
			
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load = reader.getString("Success");
				if (data_load.toString().equals("True")) {
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
					JSONArray jsonMainNode = reader
							.optJSONArray("FinalArray");
					JSONArray jsonMainNode1 = reader
							.optJSONArray("PrivateArray");
					if (jsonMainNode.toString().equalsIgnoreCase("")) {

					} else {
						for (int i = 0; i < jsonMainNode.length(); i++) {
							JSONObject jsonChildNode = jsonMainNode
									.getJSONObject(i);
							tbid.add(jsonChildNode.getString("tbid"));
							date.add(jsonChildNode.getString("date"));
							time.add(jsonChildNode.getString("time"));
							sttimehr.add(jsonChildNode
									.getString("sttimehr"));
							sttimemin.add(jsonChildNode
									.getString("sttimemin"));
							lessontype.add(jsonChildNode
									.getString("lessontype"));
							lessonId.add(jsonChildNode
									.getString("lessonId"));
							attendancetype.add(jsonChildNode
									.getString("attendancetype"));
							attendanceId.add(jsonChildNode
									.getString("attendanceId"));
							Name.add(jsonChildNode.getString("name"));
							firstName.add(jsonChildNode
									.getString("firstName"));
							lastName.add(jsonChildNode
									.getString("lastName"));
							siteId.add(jsonChildNode
									.getString("siteId"));
							siteName.add(jsonChildNode
									.getString("siteName"));
						}
					}
					if (jsonMainNode1.toString().equalsIgnoreCase("[]")) {
						data_load_sp = false;
					} else {
						data_load_sp = true;
						for (int i = 0; i < jsonMainNode1.length(); i++) {
							JSONObject jsonChildNode = jsonMainNode1
									.getJSONObject(i);
							p_tbid.add(jsonChildNode.getString("tbid"));
							p_date.add(jsonChildNode.getString("date"));
							p_time.add(jsonChildNode.getString("time"));
							p_sttimehr.add(jsonChildNode
									.getString("sttimehr"));
							p_sttimemin.add(jsonChildNode
									.getString("sttimemin"));
							p_lessontype.add(jsonChildNode
									.getString("lessontype"));
							p_lessonId.add(jsonChildNode
									.getString("lessonId"));
							p_attendancetype.add(jsonChildNode
									.getString("attendancetype"));
							p_attendanceId.add(jsonChildNode
									.getString("attendanceId"));
							p_Name.add(jsonChildNode.getString("name"));
							p_firstName.add(jsonChildNode
									.getString("firstName"));
							p_lastName.add(jsonChildNode
									.getString("lastName"));
							p_siteId.add(jsonChildNode
									.getString("siteId"));
							p_siteName.add(jsonChildNode
									.getString("siteName"));
						}
					}
				} else {
					JSONArray jsonMainNode = reader
							.optJSONArray("FinalArray");

					JSONObject jsonObject = jsonMainNode
							.getJSONObject(0);
					message = jsonObject.getString("Msg");

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
			if (pd != null) {
				pd.dismiss();
				if(data_load.toString().equalsIgnoreCase("True")){
					btn_lesson.setVisibility(View.VISIBLE);
					btn_site.setVisibility(View.VISIBLE);
					tv_tsm_error.setVisibility(View.GONE);
					if(data_load_sp){
						data_load_sp=false;
						btn_semitoprvt.setVisibility(View.VISIBLE);
					}else{
						btn_semitoprvt.setVisibility(View.GONE);
					}
				}else{
					btn_lesson.setVisibility(View.GONE);
					btn_semitoprvt.setVisibility(View.GONE);
					btn_site.setVisibility(View.GONE);
					tv_tsm_error.setVisibility(View.VISIBLE);
					tv_tsm_error.setText(message);
				}
			}
		}
	}

	public static String message="";

	private class GetSiteList extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(NewTransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params1) {
			// TODO Auto-generated method stub

			HashMap<String, String > params = new HashMap<String, String>();
			
			String responseString = WebServicesCall.RunScript(AppConfiguration.getSiteListURL, params);
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load_site = reader.getString("Success");
				if (data_load_site.toString().equals("True")) {
					all_siteid = new ArrayList<String>();
					all_sitename = new ArrayList<String>();

					JSONArray jsonMainNode = reader
							.optJSONArray("Sites");

					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						all_siteid.add(jsonChildNode
								.getString("SiteID"));
						all_sitename.add(jsonChildNode
								.getString("SiteName"));
					}
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
			if (pd != null) {
				pd.dismiss();
			}
			if (data_load_site.toString().equalsIgnoreCase("True")) {

			} else {
				Toast.makeText(NewTransferMakeUpActivity.this,
						"Some internaml error,Please try after sometime.", 1)
						.show();
			}
		}
	}

	private class GetLessonList extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(NewTransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... param1s) {
			// TODO Auto-generated method stub
			HashMap<String, String > params = new HashMap<String, String>();
					
			String responseString = WebServicesCall.RunScript(
					AppConfiguration.getInstructorPreferences, params);
			
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load_lesson = reader.getString("Success");
				if (data_load_lesson.toString().equals("True")) {
					all_lessonid = new ArrayList<String>();
					all_lessonname = new ArrayList<String>();

					JSONArray jsonMainNode = reader
							.optJSONArray("Lesson");

					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						all_lessonid.add(jsonChildNode
								.getString("lessonid"));
						all_lessonname.add(jsonChildNode
								.getString("lessonname"));
					}
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
			if (pd != null) {
				pd.dismiss();
			}
			if (data_load_lesson.toString().equalsIgnoreCase("True")) {
			} else {
				Toast.makeText(NewTransferMakeUpActivity.this,
						"Some internaml error,Please try after sometime.", 1)
						.show();
			}
		}

	}

	@SuppressWarnings("unused")
	private class TransferSemiPrivateData extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(NewTransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... paramVarArgs) {
			// TODO Auto-generated method stub
			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
			params.put("FamilyID",familyID );
			params.put("BasketID",
					AppConfiguration.BasketID);
			params.put("Selected", p_tbid
					.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
			String responseString = WebServicesCall.RunScript(
					AppConfiguration.transfermakeup_privatelesson_transfer, params);
			
			try {
				JSONObject reader = new JSONObject(responseString);
				transferdone = reader.getString("Success");
				if (transferdone.toString().equalsIgnoreCase("true")) {
					JSONArray jsonMainNode = reader
							.optJSONArray("FinalArray");
					JSONObject jsonChildNode = jsonMainNode
							.getJSONObject(0);
					msg = jsonChildNode.getString("showCart");
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
			if (pd != null) {
				pd.dismiss();
			}
			if (transferdone.toString().equalsIgnoreCase("True")) {
				Toast.makeText(getApplicationContext(), "Lesson Transferred.",
						Toast.LENGTH_LONG).show();
				if (msg.toString().equalsIgnoreCase("True")) {
					Intent i = new Intent(getApplicationContext(),
							ViewCartActivity.class);
					startActivity(i);
				} else {
					finish();
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Not able to transfer lesson.", Toast.LENGTH_LONG).show();
			}
		}
	}

	public class GetBasketID extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(NewTransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... para1ms) {
			// TODO Auto-generated method stub

			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
			params.put("siteid", "0");
			
			String responseString = WebServicesCall.RunScript(AppConfiguration.Get_BasketID, params);
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
			if (data_load_basket.toString().equalsIgnoreCase("True")) {
				if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
					// Toast.makeText(TransferMakeUpActivity.this,
					// "Please try after sometime", Toast.LENGTH_LONG).show();
				} else {
					// new swimlessonsubmit().execute();
				}
			} else {
				// Toast.makeText(TransferMakeUpActivity.this,
				// "Please try after sometime", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void GetBasketID(String response) {
		try {
			JSONObject reader = new JSONObject(response);
			data_load_basket = reader.getString("Success");
			if (data_load_basket.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("BasketDtl");
				if (jsonMainNode.toString().equalsIgnoreCase("")) {

				} else {
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						AppConfiguration.BasketID = jsonChildNode
								.getString("Basketid");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MakeUpCounts extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		String getcounts ="";
		ArrayList<String> LessonID = new ArrayList<String>();
		ArrayList<String> LessonName = new ArrayList<String>();
		ArrayList<String> Qty = new ArrayList<String>();
		ArrayList<String> ExpiryDate = new ArrayList<String>();
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(NewTransferMakeUpActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... para1ms) {
			// TODO Auto-generated method stub
			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
					
			String responseString = WebServicesCall.RunScript(AppConfiguration.transfermakeupcounts, params);
			try {
				JSONObject reader = new JSONObject(responseString);
				getcounts = reader.getString("Success");
				if (getcounts.toString().equals("True")) {
					JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
					for (int i = 0; i < jsonMainNode.length(); i++) {

							JSONObject jsonChildNode = jsonMainNode
									.getJSONObject(i);
							LessonID.add(jsonChildNode
									.getString("LessonID"));
							LessonName.add(jsonChildNode
									.getString("LessonName"));
							Qty.add(jsonChildNode
									.getString("Qty"));
							ExpiryDate.add(jsonChildNode
									.getString("ExpiryDate"));
					}
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
			if(pd!=null){
				pd.dismiss();
			}
			if(getcounts.toString().equalsIgnoreCase("True")){
				tv_make_up_counts.setVisibility(View.VISIBLE);
				ll_trnsfr_makeups_count.setVisibility(View.VISIBLE);
				LessonID.add(0,"");
				LessonName.add(0,"Lesson Type");
				Qty.add(0,"Qty");
				ExpiryDate.add(0,"Exp. Date");
				LinearLayout v = ll_trnsfr_makeups_count;
				for (int i = 0; i < LessonID.size(); i++) {
					View childView = getLayoutInflater()
							.inflate(R.layout.transfermakeup_count,
									v, false);
					if(i==0){
						childView.setPadding(2, 2, 2, 1);
					}
					else if(i==(LessonID.size()-1)){
						childView.setPadding(2, 1, 2, 2);
					}else{
						childView.setPadding(2,1,2,1);
					}
					TextView tv_lesson = (TextView) childView
							.findViewById(R.id.tv_trnsf_cnt_lt);
					TextView tv_qty = (TextView) childView
							.findViewById(R.id.tv_trnsf_cnt_qty);
					TextView tv_exp_date = (TextView) childView
							.findViewById(R.id.tv_trnsf_cnt_exp_date);
					tv_lesson.setGravity(Gravity.LEFT
							| Gravity.CENTER_VERTICAL);
					if(i==0){
						tv_lesson.setText(Html.fromHtml("<b>"+LessonName.get(i)+"</b>"));
					}else{
						tv_lesson.setText(LessonName.get(i));
					}
					tv_lesson.setEllipsize(TextUtils.TruncateAt.END);
					tv_lesson.setMaxLines(1);
					tv_lesson.setMinLines(1);
					tv_lesson.setHorizontallyScrolling(true);
					tv_lesson.setPadding(2, 0, 0, 0);
					
					tv_qty.setGravity(Gravity.CENTER
							| Gravity.CENTER_VERTICAL);
					if(i==0){
						tv_qty.setText(Html.fromHtml("<b>"+Qty.get(i)+"</b>"));
					}else{
						tv_qty.setText(Qty.get(i));
					}
					tv_qty.setEllipsize(TextUtils.TruncateAt.END);
					tv_qty.setMaxLines(1);
					tv_qty.setMinLines(1);
					tv_qty.setHorizontallyScrolling(true);
					
					tv_exp_date.setGravity(Gravity.RIGHT
							| Gravity.CENTER_VERTICAL);
					if(i==0){
						tv_exp_date.setText(Html.fromHtml("<b>"+ExpiryDate.get(i)+"</b>"));
					}else{
						String _temp = ExpiryDate.get(i);
						if(!_temp.isEmpty()||!_temp.equalsIgnoreCase("")){
							if(_temp.contains(".")){
								String[] temp = _temp.toString().split("\\.");
								tv_exp_date.setText(temp[1]+"/"+temp[2]+"/"+temp[0]);
							}else{
								tv_exp_date.setText(_temp);
							}
						}else{
							tv_exp_date.setText(_temp);
						}
						
					}
					tv_exp_date.setEllipsize(TextUtils.TruncateAt.END);
					tv_exp_date.setMaxLines(1);
					tv_exp_date.setMinLines(1);
					tv_exp_date.setHorizontallyScrolling(true);
					tv_exp_date.setPadding(0, 0, 2, 0);
					
					v.addView(childView);
				}
			}else{
				tv_make_up_counts.setVisibility(View.GONE);
				ll_trnsfr_makeups_count.setVisibility(View.GONE);
			}
		}
	}
	
}
