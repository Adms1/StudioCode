package com.waterworks;

import java.text.DecimalFormat;
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
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class WaterAerobicsActivity extends Activity {
	Button btn_sessions,relMenu;
	CardView btn_submit;
	private static String TAG = "WaterAerobicsActivity";
	Boolean isInternetPresent = false;
	ListPopupWindow lpw_sessions;
	ArrayList<String> sessions;
	String data_load ="False",data_load_basket="False", siteID;
	Spinner spinSites;
	private ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> siteName = new ArrayList<String>();
	SitesListAsyncTask sitesListAsyncTask = null;
	TextView tv_error,tv_total, txtPriceinfo;
	String token,familyID;
	ScrollView scrollviewwateraerobics;
	EditText tv_fsn_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.water_aerobics_activity);
        spinSites = (Spinner) findViewById(R.id.spinSites);
		
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		
		isInternetPresent = Utility.isNetworkConnected(WaterAerobicsActivity.this);
		if(isInternetPresent){
			Initialization();
			if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
				new GetBasketID().execute();
			}

            try{
                sitesListAsyncTask = new SitesListAsyncTask(token);
                String responseString = sitesListAsyncTask.execute().get();
                readAndParseSiteJSON(responseString);
                ArrayAdapter<String> adapterSites = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, siteName);
                spinSites.setAdapter(adapterSites);
            }catch (Exception e){
                e.printStackTrace();
            }

			lpw_sessions.setAdapter(new ArrayAdapter<String>(
    	            getApplicationContext(),
    	            R.layout.edittextpopup,sessions));
			lpw_sessions.setAnchorView(btn_sessions);
			lpw_sessions.setHeight(LayoutParams.WRAP_CONTENT);
			lpw_sessions.setModal(true);
			lpw_sessions.setOnItemClickListener(
                new OnItemClickListener() {

    				@Override
    				public void onItemClick(AdapterView<?> parent, View view,
    						int pos, long id) {
    					// TODO Auto-generated method stub
    					btn_sessions.setText(sessions.get(pos));
    					if(sessions.get(pos).toString().equalsIgnoreCase("0")){
    						tv_error.setVisibility(View.VISIBLE);
    						tv_error.setText("Please select the number of water aerobics classes you would like to purchase.");
    					}
    					else{
    						tv_error.setVisibility(View.GONE);
    					}
    					if(Integer.parseInt(sessions.get(pos))>=1&&Integer.parseInt(sessions.get(pos))<=9){
    						double a = Integer.parseInt(sessions.get(pos))*12;
    						DecimalFormat df = new DecimalFormat("#.00");
    						a = Double.valueOf(df.format(a));
    						String b[] = String.valueOf(a).toString().split("\\.");
    						if(b[1].length()==1){
    							b[1] = b[1]+"0";
    						}
    						
    						tv_total.setText(Html.fromHtml("<b>Total:</b> $"+b[0]+"."+b[1]));
    					}
    					else if(Integer.parseInt(sessions.get(pos))>=10&&Integer.parseInt(sessions.get(pos))<=14){
    						double a = Integer.parseInt(sessions.get(pos))*10.80;
    						DecimalFormat df = new DecimalFormat("#.00");
    						a = Double.valueOf(df.format(a));
    						String b[] = String.valueOf(a).toString().split("\\.");
    						if(b[1].length()==1){
    							b[1] = b[1]+"0";
    						}
    						
    						tv_total.setText(Html.fromHtml("<b>Total:</b> $"+b[0]+"."+b[1]));
    					}
    					else if(Integer.parseInt(sessions.get(pos))>=15&&Integer.parseInt(sessions.get(pos))<=17){
    						double a = Integer.parseInt(sessions.get(pos))*10;
    						DecimalFormat df = new DecimalFormat("#.00");
    						a = Double.valueOf(df.format(a));
    						String b[] = String.valueOf(a).toString().split("\\.");
    						if(b[1].length()==1){
    							b[1] = b[1]+"0";
    						}
    						
    						tv_total.setText(Html.fromHtml("<b>Total:</b> $"+b[0]+"."+b[1]));
    					}
    					else if(Integer.parseInt(sessions.get(pos))==18){
    						    						
    						tv_total.setText(Html.fromHtml("<b>Total:</b> $"+"172.00"));
    					}
    					else if(Integer.parseInt(sessions.get(pos))==19){
    						tv_total.setText(Html.fromHtml("<b>Total:</b> $"+"175.00"));
    					}
    					else if(Integer.parseInt(sessions.get(pos))>=20){
    						double a = Integer.parseInt(sessions.get(pos))*9;
    						DecimalFormat df = new DecimalFormat("#.00");
    						a = Double.valueOf(df.format(a));
    						String b[] = String.valueOf(a).toString().split("\\.");
    						if(b[1].length()==1){
    							b[1] = b[1]+"0";
    						}
    						
    						tv_total.setText(Html.fromHtml("<b>Total:</b> $"+b[0]+"."+b[1]));
    					}
    					lpw_sessions.dismiss();
    				}
                });
			
		}
		else{
			onDetectNetworkState().show();
		}
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		 tv_fsn_info = (EditText) findViewById(R.id.tv_fsn_info);
		tv_fsn_info.setText(Utility.getProgramsInstructionText("12"));

		txtPriceinfo = (TextView) findViewById(R.id.txtPriceinfo);
		tv_error = (TextView)findViewById(R.id.tv_wa_error);
		tv_total = (TextView)findViewById(R.id.tv_wa_total);
		relMenu = (Button)findViewById(R.id.relMenu);
		scrollviewwateraerobics=(ScrollView)findViewById(R.id.scrollviewwateraerobics);
		tv_fsn_info.setVerticalScrollBarEnabled(true);
		tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
		scrollviewwateraerobics.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});
		tv_fsn_info.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		relMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCheckin = new Intent(WaterAerobicsActivity.this, DashBoardActivity.class);
				intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCheckin);
				finish();
			}
		});
		btn_sessions = (Button)findViewById(R.id.btn_wa_sessions);
		btn_submit = (CardView)findViewById(R.id.btn_wa_submit);
		btn_sessions.setTextColor(getResources().getColor(R.color.app_text));
		btn_sessions.setText("0");
		sessions = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			sessions.add(""+i);
		}
		lpw_sessions = new ListPopupWindow(getApplicationContext());
		btn_sessions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				lpw_sessions.show();
			}
		});
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				if(isInternetPresent){
					if(btn_sessions.getText().toString().trim().equalsIgnoreCase("0")){
						tv_error.setVisibility(View.VISIBLE);
						tv_error.setText("Please select the number of water aerobics classes you would like to purchase.");
					}else if(siteID == null){
                        Utility.ping(WaterAerobicsActivity.this, "Please select a site");
                    }else{
						tv_error.setVisibility(View.GONE);
						new WaterAerobicsSubmit().execute();
					}
				}
				else{
					onDetectNetworkState().show();
				}
			}
		});
		spinSites.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0 && siteMainList.size()>1) {
					siteID = null;
				} else {
					siteID = siteMainList.get(position).get("SiteID");
                    String textPrice = Utility.getProgramsPricingText("12", siteID);
                    if (textPrice.equalsIgnoreCase("")) {
                        txtPriceinfo.setVisibility(View.GONE);
                    } else {
                        txtPriceinfo.setVisibility(View.VISIBLE);
                        txtPriceinfo.setText(textPrice);
                    }
				}
				Log.v(TAG, ""+siteID);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
        finish();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		isInternetPresent = Utility
				.isNetworkConnected(WaterAerobicsActivity.this);
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(WaterAerobicsActivity.this);
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
	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(WaterAerobicsActivity.this);
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
				}
				else{
				}
			}
			else{
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
	
	private class WaterAerobicsSubmit extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(WaterAerobicsActivity.this);
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
			param.put("BasketID", AppConfiguration.BasketID);
			param.put("SiteID", siteID);
			param.put("AerobicsQnt",btn_sessions.getText().toString().trim());
			
			String responseString = WebServicesCall
			.RunScript(AppConfiguration.WaterArobics, param);
			
			try {
    			JSONObject reader = new JSONObject(responseString);
    			data_load = reader.getString("Success");
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
			if(data_load.toString().equals("True"))
			{
                Intent viewcart = new Intent(getApplicationContext(), ByMoreMyCart.class);
				startActivity(viewcart);
			}
			else{
				Toast.makeText(getApplicationContext(), "Some internal error, Please try after sometime.", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void readAndParseSiteJSON(String in) {
		try {
			HashMap<String, String> hashmap;
			hashmap = new HashMap<String, String>();

			JSONObject reader = new JSONObject(in);
			JSONArray jsonMainNode = reader.optJSONArray("SiteList");

			if (jsonMainNode.length() > 1) {
				hashmap.put("SiteID", "0");
				hashmap.put("SiteName", "Please Select a Location");
				siteName.add("Please Select a Location");
				siteMainList.add(hashmap);
			}
			for (int i = 0; i < jsonMainNode.length(); i++) {

				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

				hashmap = new HashMap<String, String>();

				hashmap.put("SiteID", jsonChildNode.getString("siteid"));
				hashmap.put("SiteName", jsonChildNode.getString("sitename"));

				siteName.add("" + jsonChildNode.getString("sitename"));
				siteMainList.add(hashmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}