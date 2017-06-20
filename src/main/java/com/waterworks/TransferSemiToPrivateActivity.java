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
import android.text.Html;
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
public class TransferSemiToPrivateActivity extends Activity {
	ImageButton ib_back;
	TextView tv_tm_sp_name1, tv_tm_sp_date1, tv_tm_sp_lesson1, tv_tm_sp_time1,
			tv_tm_sp_att1, tv_tm_sp_site1, tv_tm_sp_name2, tv_tm_sp_date2,
			tv_tm_sp_lesson2, tv_tm_sp_time2, tv_tm_sp_att2, tv_tm_sp_site2;
	Button btn_tm_sp_transfer;
	String token, familyID, msg,transferdone="False";
	Boolean isInternetPresent = false;
	private static final String TAG = "Transfer semi-private_lessons to private_lessons ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_semi_to_private);
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

		isInternetPresent = Utility
				.isNetworkConnected(TransferSemiToPrivateActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			Initialization();
		}
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_tm_sp_name1 = (TextView) findViewById(R.id.tv_tm_sp_name1);
		tv_tm_sp_date1 = (TextView) findViewById(R.id.tv_tm_sp_date1);
		tv_tm_sp_lesson1 = (TextView) findViewById(R.id.tv_tm_sp_lesson1);
		tv_tm_sp_time1 = (TextView) findViewById(R.id.tv_tm_sp_time1);
		tv_tm_sp_att1 = (TextView) findViewById(R.id.tv_tm_sp_att1);
		tv_tm_sp_site1 = (TextView) findViewById(R.id.tv_tm_sp_site1);
		tv_tm_sp_name2 = (TextView) findViewById(R.id.tv_tm_sp_name2);
		tv_tm_sp_date2 = (TextView) findViewById(R.id.tv_tm_sp_date2);
		tv_tm_sp_lesson2 = (TextView) findViewById(R.id.tv_tm_sp_lesson2);
		tv_tm_sp_time2 = (TextView) findViewById(R.id.tv_tm_sp_time2);
		tv_tm_sp_att2 = (TextView) findViewById(R.id.tv_tm_sp_att2);
		tv_tm_sp_site2 = (TextView) findViewById(R.id.tv_tm_sp_site2);
		btn_tm_sp_transfer = (Button) findViewById(R.id.btn_tm_sp_transfer);

		tv_tm_sp_name1.setText(Html.fromHtml("<b>Name:</b>" + NewTransferMakeUpActivity.p_Name.get(0)));
		tv_tm_sp_name2.setText(Html.fromHtml("<b>Name:</b>" + NewTransferMakeUpActivity.p_Name.get(1)));
		tv_tm_sp_lesson1.setText(Html.fromHtml("<b>Lesson:</b>"
				+ NewTransferMakeUpActivity.p_lessontype.get(0)));
		tv_tm_sp_lesson2.setText(Html.fromHtml("<b>Lesson:</b>"
				+ NewTransferMakeUpActivity.p_lessontype.get(1)));
		tv_tm_sp_att1.setText(Html.fromHtml("<b>Att:</b>"
				+ NewTransferMakeUpActivity.p_attendancetype.get(0)));
		tv_tm_sp_att2.setText(Html.fromHtml("<b>Att:</b>"
				+ NewTransferMakeUpActivity.p_attendancetype.get(1)));
		tv_tm_sp_date1.setText(Html.fromHtml("<b>Date:</b>" + NewTransferMakeUpActivity.p_date.get(0)));
		tv_tm_sp_date2.setText(Html.fromHtml("<b>Date:</b>" + NewTransferMakeUpActivity.p_date.get(1)));
		tv_tm_sp_time1.setText(Html.fromHtml("<b>Time:</b>" + NewTransferMakeUpActivity.p_time.get(0)));
		tv_tm_sp_time2.setText(Html.fromHtml("<b>Time:</b>" + NewTransferMakeUpActivity.p_time.get(1)));
		tv_tm_sp_site1
				.setText(Html.fromHtml("<b>Site:</b>" + NewTransferMakeUpActivity.p_siteName.get(0)));
		tv_tm_sp_site2
				.setText(Html.fromHtml("<b>Site:</b>" + NewTransferMakeUpActivity.p_siteName.get(1)));
		
		btn_tm_sp_transfer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				new TransferSemiPrivateData().execute();
			}
		});

	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				TransferSemiToPrivateActivity.this);
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
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(TransferSemiToPrivateActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	private class TransferSemiPrivateData extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(TransferSemiToPrivateActivity.this);
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
			param.put("Selected",NewTransferMakeUpActivity.p_tbid.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

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
}
