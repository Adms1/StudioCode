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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.PastDueBalanceAdapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class PastDueBalActivity extends Activity {

	ImageButton ib_back;
	Boolean isInternetPresent = false;
	private static final String TAG = "Past due balance";
	String data_load="False";
	ArrayList<String> index,date,invoiceId,itemType,expDate,amountDue,amountPaid,amountTotal;
	String main_Total,main_PaidAmount,main_DueAmount;
	ListView lv_pb_list;
	Button btn_pay_bal;
	TextView tv_main_total,tv_main_amnt_paid,tv_main_amnt_due;
	String token,familyID; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_past_due_bal);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);


		isInternetPresent = Utility
				.isNetworkConnected(PastDueBalActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			new PastBalDueLoad().execute();
		}
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		lv_pb_list = (ListView)findViewById(R.id.lv_pastbal_list);
		btn_pay_bal = (Button)findViewById(R.id.btn_pastbal_pastbal);
		tv_main_total = (TextView)findViewById(R.id.tv_pastbal_total);
		tv_main_amnt_paid = (TextView)findViewById(R.id.tv_pastbal_amntpaid);
		tv_main_amnt_due = (TextView)findViewById(R.id.tv_pastbal_amntdue);

	}
	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(PastDueBalActivity.this);
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
				.isNetworkConnected(PastDueBalActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	public class PastBalDueLoad extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(PastDueBalActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... pa1rams) {
			// TODO Auto-generated method stub

			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
			params.put("FamilyID",familyID );

			String responseString = WebServicesCall.RunScript(AppConfiguration.pastduebalload, params);
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load = reader.getString("Success");
				if(data_load.toString().equals("True"))
				{
					main_Total = reader.getString("Total");
					main_PaidAmount = reader.getString("PaidAmount");
					main_DueAmount = reader.getString("DueAmount");
					index = new ArrayList<String>();
					date = new ArrayList<String>();
					invoiceId = new ArrayList<String>();
					itemType = new ArrayList<String>();
					expDate = new ArrayList<String>();
					amountDue = new ArrayList<String>();
					amountPaid = new ArrayList<String>();
					amountTotal = new ArrayList<String>();
					JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
					if(jsonMainNode.toString().equalsIgnoreCase("")){

					}
					else{
						for(int i = 0 ;i < jsonMainNode.length();i++)
						{
							JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							index.add(jsonChildNode.getString("index"));
							date.add(jsonChildNode.getString("date"));
							invoiceId.add(jsonChildNode.getString("invoiceId"));
							itemType.add(jsonChildNode.getString("itemType"));
							expDate.add(jsonChildNode.getString("expDate"));
							amountDue.add(jsonChildNode.getString("amountDue"));
							amountPaid.add(jsonChildNode.getString("amountPaid"));
							amountTotal.add(jsonChildNode.getString("amountTotal"));


						}
					}
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
				tv_main_total.setText(Html.fromHtml("<br>"+main_Total+"</br>"));
				tv_main_amnt_paid.setText(Html.fromHtml("<br>"+main_PaidAmount+"</br>"));
				tv_main_amnt_due.setText(Html.fromHtml("<br>"+main_DueAmount+"</br>"));
				lv_pb_list.setAdapter(new PastDueBalanceAdapter(PastDueBalActivity.this,
						index, date, invoiceId, itemType, expDate, amountDue, amountPaid, amountTotal,btn_pay_bal));
			}else{
				Toast.makeText(getApplicationContext(), "No past amount due.", Toast.LENGTH_LONG).show();
			}
		}

	}

}
