package com.waterworks;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.Get_MonthlyLapSwimPriceBySiteAsyncTask;
import com.waterworks.asyncTasks.Get_RegLapSwimPriceBySiteAsyncTask;
import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class LapSwimsDiscountActivity extends Activity {
	Button btn_select_months,btn_starting_month, relMenu;
	CardView btn_submit;
	private static String TAG = "LapSwimsDiscountActivity";
//	ImageButton ib_back;
	Boolean isInternetPresent = false;
	String data_load ="False",data_load_basket="False";
	String siteID;
	String token,familyID;
	Spinner spinSites;
	TextView tv_info;
	ListPopupWindow lpw_select_month,lpw_starting_month;
	ArrayList<String> select_month;//,starting_month;
	String m_number,s_month;
	TextView txtSecondDropDownTitle, txt1MonthReg, txt1MonthDis, txt1MonthSen, txt3MonthReg, txt3MonthDis, txt3MonthSen,
             txt6MonthReg, txt6MonthDis, txt6MonthSen, txt12MonthReg, txt12MonthDis, txt12MonthSen;

	String[] m_months ;

	List<String> monthsList = new ArrayList<String>();
	List<String> month_id = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> siteName = new ArrayList<String>();
	SitesListAsyncTask sitesListAsyncTask = null;
	Get_MonthlyLapSwimPriceBySiteAsyncTask get_monthlyLapSwimPriceBySiteAsyncTask = null;
    ProgressDialog pd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lap_swims_discount);

		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

		isInternetPresent = Utility
				.isNetworkConnected(LapSwimsDiscountActivity.this);
		if(isInternetPresent){
			Initialization();
			new MonthList().execute();
		try{
			sitesListAsyncTask = new SitesListAsyncTask(token);
			String responseString = sitesListAsyncTask.execute().get();
			readAndParseSiteJSON(responseString);
			ArrayAdapter<String> adapterSites = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, siteName);
			spinSites.setAdapter(adapterSites);
		}catch (Exception e){
			e.printStackTrace();
		}
			select_month.add("Please Choose");
			select_month.add("1 Month");
			select_month.add("3 Months");
			select_month.add("6 Months");
			select_month.add("12 Months");


			String[] months = new DateFormatSymbols().getMonths();
			int m = Calendar.MONTH;
			m = m-1;
			for (int i = m; i < months.length; i++) {
//				monthsList.add(months[i]);
			}

			//			Log.i(TAG, "month list = " + monthsList);
			//			m_months = new String[12];
			//			for (int j = 0; j < monthsList.size(); j++) {
			//				m_months[j] = monthsList.get(j)+" "+Calendar.getInstance().get(Calendar.YEAR);
			//				Log.i(TAG,""+ m_months[j]);
			//			}

//			int noofMonths = months.length - monthsList.size();
//
//			for(int k = 0 ; k < noofMonths ;k++)
//			{
//				m_months[monthsList.size()+k] = months[k]+" "+((Calendar.getInstance().get(Calendar.YEAR))+1);
//				Log.i(TAG,""+ m_months[monthsList.size()+k]);
//			}

//			siteid = getIntent().getStringExtra("siteid");
			if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
				new GetBasketID().execute();
			}

			lpw_select_month.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(),
					R.layout.edittextpopup,select_month));
			lpw_select_month.setAnchorView(btn_select_months);
			lpw_select_month.setHeight(LayoutParams.WRAP_CONTENT);
			lpw_select_month.setModal(true);
			lpw_select_month.setOnItemClickListener(
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int pos, long id) {

							btn_select_months.setText(select_month.get(pos));
							lpw_select_month.dismiss();

							if(pos == 1)
								txtSecondDropDownTitle.setText("Select Month");
							else {
								txtSecondDropDownTitle.setText("Select Starting Month");
							}
						}
					});
		}
		else{
			onDetectNetworkState().show();
		}
	}

	public class MonthList extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(LapSwimsDiscountActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );

			String responseString = WebServicesCall
					.RunScript(AppConfiguration.DOMAIN+AppConfiguration.MakePurchase_LapSwimMonthList, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load_basket = reader.getString("Success");
				if(data_load_basket.toString().equals("True"))
				{
					JSONArray jsonMainNode = reader.optJSONArray("PhoneList");
					if(jsonMainNode.toString().equalsIgnoreCase("")){

					}
					else{
						for(int i = 0 ;i < jsonMainNode.length();i++)
						{
							JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							monthsList.add(jsonChildNode.getString("MonthName"));
							month_id.add(jsonChildNode.getString("MonthId"));
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
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

			m_months = new String[12];
			for (int j = 0; j < monthsList.size(); j++) {
				m_months[j] = monthsList.get(j);
				Log.i(TAG,""+ m_months[j]);
			}

			lpw_starting_month.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(),
					R.layout.edittextpopup,m_months));
			lpw_starting_month.setAnchorView(btn_starting_month);
			lpw_starting_month.setHeight(LayoutParams.WRAP_CONTENT);
			lpw_starting_month.setModal(true);
			lpw_starting_month.setOnItemClickListener(
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int pos, long id) {
							// TODO Auto-generated method stub
							btn_starting_month.setText(m_months[pos]);
							s_month = month_id.get(pos);
							System.out.println("Selected : "+s_month);
							lpw_starting_month.dismiss();
						}
					});

			//			if(data_load_basket.toString().equalsIgnoreCase("True")){
			//				lv_list_month.setAdapter(new ListAdapterMonth(LapSwimsMonthlyActivity.this, month_name,btn_submit));
			//			}
			//			else{
			//			}
		}
	}

	private void Initialization() {
		txtSecondDropDownTitle = (TextView)findViewById(R.id.txtSecondDropDownTitle);
		spinSites = (Spinner) findViewById(R.id.spinSites);
		select_month = new ArrayList<String>();
		//	starting_month= new ArrayList<String>();
		relMenu = (Button)findViewById(R.id.relMenu);
		relMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCheckin = new Intent(LapSwimsDiscountActivity.this, DashBoardActivity.class);
				intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCheckin);
				finish();
			}
		});

        txt1MonthReg = (TextView) findViewById(R.id.txt1MonthReg);
        txt1MonthDis = (TextView) findViewById(R.id.txt1MonthDis);
        txt1MonthSen = (TextView) findViewById(R.id.txt1MonthSen);
        txt3MonthReg = (TextView) findViewById(R.id.txt3MonthReg);
        txt3MonthDis = (TextView) findViewById(R.id.txt3MonthDis);
        txt3MonthSen = (TextView) findViewById(R.id.txt3MonthSen);
        txt6MonthReg = (TextView) findViewById(R.id.txt6MonthReg);
        txt6MonthDis = (TextView) findViewById(R.id.txt6MonthDis);
        txt6MonthSen = (TextView) findViewById(R.id.txt6MonthSen);
        txt12MonthReg = (TextView) findViewById(R.id.txt12MonthReg);
        txt12MonthDis = (TextView) findViewById(R.id.txt12MonthDis);
        txt12MonthSen = (TextView) findViewById(R.id.txt12MonthSen);
		tv_info = (TextView)findViewById(R.id.tv_lsd_title);
		tv_info.setMovementMethod(new ScrollingMovementMethod());
		btn_submit = (CardView)findViewById(R.id.btn_ls3_submit);
		btn_select_months = (Button)findViewById(R.id.btn_lsd_month);
		btn_starting_month = (Button)findViewById(R.id.btn_lsd1_month);
		lpw_select_month = new ListPopupWindow(getApplicationContext());
		lpw_starting_month = new ListPopupWindow(getApplicationContext());
		btn_select_months.setText("Please Choose");
		btn_starting_month.setText("Please Choose");
		btn_select_months.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                lpw_select_month.show();
            }
        });
		btn_starting_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lpw_starting_month.show();
			}
		});
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_select_months.getText().toString().equalsIgnoreCase("Please Choose")){
					Toast.makeText(getApplicationContext(), "Please select number of months", Toast.LENGTH_LONG).show();
				}
				else if(btn_starting_month.getText().toString().equalsIgnoreCase("Please Choose")){
					Toast.makeText(getApplicationContext(), "Please select starting month", Toast.LENGTH_LONG).show();
				}else if(siteID == null){
                    Utility.ping(LapSwimsDiscountActivity.this, "Please select a location");
                }
				else{
					String monthlynumber[] = btn_select_months.getText().toString().split("\\s");
					m_number = monthlynumber[0];
					Log.i(TAG, m_number);
					Log.i(TAG, s_month);
					new LapSwimDiscountSubmit().execute();
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
        pd = new ProgressDialog(LapSwimsDiscountActivity.this);
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
                    get_monthlyLapSwimPriceBySiteAsyncTask = new Get_MonthlyLapSwimPriceBySiteAsyncTask(LapSwimsDiscountActivity.this, hashMap);
                    final HashMap<String, String> costDiscount = get_monthlyLapSwimPriceBySiteAsyncTask.execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt1MonthReg.setText(costDiscount.get("1MonthReg"));
                            txt1MonthDis.setText(costDiscount.get("1MonthDis"));
                            txt1MonthSen.setText(costDiscount.get("1MonthSen"));
                            txt3MonthReg.setText(costDiscount.get("3MonthReg"));
                            txt3MonthDis.setText(costDiscount.get("3MonthDis"));
                            txt3MonthSen.setText(costDiscount.get("3MonthSen"));
                            txt6MonthReg.setText(costDiscount.get("6MonthReg"));
                            txt6MonthDis.setText(costDiscount.get("6MonthDis"));
                            txt6MonthSen.setText(costDiscount.get("6MonthSen"));
                            txt12MonthReg.setText(costDiscount.get("12MonthReg"));
                            txt12MonthDis.setText(costDiscount.get("12MonthDis"));
                            txt12MonthSen.setText(costDiscount.get("12MonthSen"));
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
				.isNetworkConnected(LapSwimsDiscountActivity.this);
	}
	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(LapSwimsDiscountActivity.this);
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
			pd = new ProgressDialog(LapSwimsDiscountActivity.this);
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

	private class LapSwimDiscountSubmit extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(LapSwimsDiscountActivity.this);
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
			param.put("MonthlyNumber",m_number);
			param.put("StartMonth",s_month);

			String responseString = WebServicesCall
					.RunScript(AppConfiguration.lapswimbutton3submit, param);
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
