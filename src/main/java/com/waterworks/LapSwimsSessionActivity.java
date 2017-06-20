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
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.Get_RegLapSwimPriceBySiteAsyncTask;
import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class LapSwimsSessionActivity extends Activity {
	Button btn_sessions,relMenu;
	CardView btn_submit;
	private static String TAG = "LapSwimsSessionActivity";
//	ImageButton ib_back;
	Boolean isInternetPresent = false;
	ListPopupWindow lpw_sessions;
	ArrayList<String> sessions;
	String data_load ="False",data_load_basket="False";
	String siteID;
	TextView tv_error, txtCostDiscount;
	String token,familyID;
    Spinner spinSites;
    private ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> siteName = new ArrayList<String>();
    SitesListAsyncTask sitesListAsyncTask = null;
	private Get_RegLapSwimPriceBySiteAsyncTask get_regLapSwimPriceBySiteAsyncTask;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lap_swims_session);

		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		
		isInternetPresent = Utility
				.isNetworkConnected(LapSwimsSessionActivity.this);
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
    					if(sessions.get(pos).toString()=="0"){
    						tv_error.setVisibility(View.VISIBLE);
    						tv_error.setText("Please select the number of sessions.");
        					btn_sessions.setText(sessions.get(pos));
        					lpw_sessions.dismiss();
    					}
    					else{
    					btn_sessions.setText(sessions.get(pos));
    					lpw_sessions.dismiss();
    					}
    				}
                });
			
		}
		else{
			onDetectNetworkState().show();
			
		}
		
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		EditText tv_fsn_info = (EditText) findViewById(R.id.tv_fsn_info);
		tv_fsn_info.setText(Utility.getProgramsInstructionText("11"));
		tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
		tv_error = (TextView)findViewById(R.id.tv_ls_error);
		relMenu = (Button)findViewById(R.id.relMenu);
		txtCostDiscount = (TextView) findViewById(R.id.txtCostDiscount);

		spinSites = (Spinner) findViewById(R.id.spinSites);
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
				Intent intentCheckin = new Intent(LapSwimsSessionActivity.this, DashBoardActivity.class);
				intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCheckin);
				finish();
			}
		});

		btn_sessions = (Button)findViewById(R.id.btn_ls1_sessions);
		btn_submit = (CardView)findViewById(R.id.btn_ls1_submit);
		btn_sessions.setTextColor(getResources().getColor(R.color.app_text));
		btn_sessions.setText("0");
		sessions = new ArrayList<String>();
		for (int i = 0; i < 101; i++) {
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
					if(btn_sessions.getText().toString().equalsIgnoreCase("0")){
    					tv_error.setVisibility(View.VISIBLE);
    					tv_error.setText("Please select the number of sessions.");
					}else if(siteID == null){
                        Utility.ping(LapSwimsSessionActivity.this, "Please select a location");
                    }
					else{
						new SubmitLapSwim().execute();
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
                if(position == 0 && siteMainList.size()>1){
                    siteID = null;
                }else{
                    siteID = siteMainList.get(position).get("SiteID");
					getLapSwimPrice();

                }
                Log.v(TAG, ""+siteID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
	}

	public void getLapSwimPrice(){
		pd = new ProgressDialog(LapSwimsSessionActivity.this);
		pd.setMessage("Please Wait...");
		pd.setCancelable(false);
		pd.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("Token", token);
					hashMap.put("siteid", siteID);
					get_regLapSwimPriceBySiteAsyncTask = new Get_RegLapSwimPriceBySiteAsyncTask(LapSwimsSessionActivity.this, hashMap);
					final ArrayList<String> costDiscount = get_regLapSwimPriceBySiteAsyncTask.execute().get();

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String costDisc = costDiscount.get(0).toString() +"\nOR\n"+ costDiscount.get(1).toString();
							txtCostDiscount.setText(costDisc);
							pd.dismiss();
						}
					});

				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
//		Intent i = new Intent(getApplicationContext(),LapSwimsActivity.class);
//		i.putExtra("SiteId", siteID);
//		startActivity(i);
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
				.isNetworkConnected(LapSwimsSessionActivity.this);
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(LapSwimsSessionActivity.this);
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
	
	private class SubmitLapSwim extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(LapSwimsSessionActivity.this);
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
			param.put("LapSwimQnt", btn_sessions.getText().toString().trim());
			
			String responseString = WebServicesCall
			.RunScript(AppConfiguration.lapswimbutton1submit, param);
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
//				Toast.makeText(getApplicationContext(), "Added to cart.", Toast.LENGTH_LONG).show();
//				Intent viewcart = new Intent(getApplicationContext(), ViewCartActivity.class);
                Intent viewcart = new Intent(getApplicationContext(), ByMoreMyCart.class);
				startActivity(viewcart);
//				finish();
			}
			else{
				Toast.makeText(getApplicationContext(), "Some internal error, Please try after sometime.", Toast.LENGTH_LONG).show();
			}
		}
	}
	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(LapSwimsSessionActivity.this);
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
			//else {
			//	JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			//	siteID=jsonChildNode.getString("siteid");
			//}
            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("siteid"));
                hashmap.put("SiteName", jsonChildNode.getString("sitename"));

                siteName.add("" + jsonChildNode.getString("sitename"));

                siteMainList.add(hashmap);
//
            }

            Log.d("siteName---", "" + siteName);
            Log.d("siteName---1", "" + siteName.size());
            Log.d("siteMainList---", "" + siteMainList);
            Log.d("siteMainList---1", "" + siteMainList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
