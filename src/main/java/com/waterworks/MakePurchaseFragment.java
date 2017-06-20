package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class MakePurchaseFragment extends Fragment implements OnClickListener {

	public MakePurchaseFragment(){}

	ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();


	View rootView;
	Button relMenu;
	Boolean isInternetPresent = false;
	private static String TAG = "Make Purchase";
	ArrayList<String> sitename,siteid;
	//,productname,productid;
	String message;
	String data_load= "False",data_load1="True";
	String siteID,productID;
	LinearLayout rl_mp_site,rl_mp_product;
	RelativeLayout rl_mp_product_;
	Button btn_view_cart,btn_payment,btn_pastduebal;
	RadioButton[] rb1,rb2;
	RadioGroup rg1,rg2;
	String token,familyID; 
	TextView tv_mp_product;


	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		ConstantData.destroyed=true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_make_purchase, container, false);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity().getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);


		isInternetPresent = Utility
				.isNetworkConnected(getActivity());
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			new GetSiteByFID().execute();

		}
		return rootView;
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		relMenu = (Button)rootView.findViewById(R.id.relMenu);
		relMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DashBoardActivity.onLeft();
			}
		});

		tv_mp_product = (TextView)rootView.findViewById(R.id.tv_mp_product);
		rl_mp_site = (LinearLayout)rootView.findViewById(R.id.rl_mp_site_selection);
		rl_mp_product = (LinearLayout)rootView.findViewById(R.id.rl_mp_products);
		rg1 = new RadioGroup(getActivity().getApplicationContext());
		rg2 = new RadioGroup(getActivity().getApplicationContext());
		rl_mp_product_ = (RelativeLayout)rootView.findViewById(R.id.rl_mp_product_);
		rl_mp_product_.setVisibility(View.GONE);
		btn_pastduebal = (Button)rootView.findViewById(R.id.btn_mp_past_due_balance);
		btn_view_cart = (Button)rootView.findViewById(R.id.btn_mp_view_cart);
		btn_payment = (Button)rootView.findViewById(R.id.btn_mp_view_payments);
		btn_pastduebal.setOnClickListener(this);
		btn_payment.setOnClickListener(this);
		btn_view_cart.setOnClickListener(this);
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
				.isNetworkConnected(getActivity());
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}

	}

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				getActivity().finish();
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

	private class GetSiteByFID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
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
			
			String responseString = WebServicesCall.RunScript(AppConfiguration.sitebyFid, params);

			try{
				JSONObject reader = new JSONObject(responseString);
				data_load = reader.getString("Success");
				if(data_load.toString().equals("True"))
				{
					sitename = new ArrayList<String>();
					siteid = new ArrayList<String>();
					JSONArray jsonMainNode = reader.optJSONArray("PhoneList");
					if(jsonMainNode.toString().equalsIgnoreCase("")){

					}
					else{
						for(int i = 0 ;i < jsonMainNode.length();i++)
						{
							JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							siteid.add(jsonChildNode.getString("siteid"));
							sitename.add(jsonChildNode.getString("sitename"));
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
				//new GetProducts().execute();

				rl_mp_site = (LinearLayout)rootView.findViewById(R.id.rl_mp_site_selection);
				rl_mp_site.removeAllViews();
				rg1 = new RadioGroup(getActivity());
				rg1.removeAllViews();

				rb1 = new RadioButton[siteid.size()];

				rg1.setOrientation(RadioGroup.VERTICAL);
				for (int i = 0; i < siteid.size(); i++) {
					rb1[i] = new RadioButton(getActivity().getApplicationContext());
					rb1[i].setGravity(Gravity.CENTER_VERTICAL);
					rb1[i].setPadding(20, 0, 0, 0);
					rg1.addView(rb1[i]);
					rb1[i].setText(sitename.get(i));
					rb1[i].setId(i);
					rb1[i].setButtonDrawable(R.drawable.customdrawableradionbuttons);
					rb1[i].setTextColor(getResources().getColor(R.color.app_text));
				}
				rl_mp_site.addView(rg1);
				if(siteid.size()==1){
					// rb1[0].setChecked(true);
					siteID = siteid.get(0);
					AppConfiguration.basket_siteid = siteID;
					rl_mp_product_.setVisibility(View.VISIBLE);
				}
				rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						try{
							int a = sitename.indexOf(sitename.get(checkedId));
							Log.i("Site index", ""+a);
							siteID = siteid.get(a);
							AppConfiguration.basket_siteid = siteID;
							Log.i("Site id", ""+siteID);
							rl_mp_product_.setVisibility(View.VISIBLE);
							tv_mp_product.setVisibility(View.VISIBLE);
							isInternetPresent = Utility
									.isNetworkConnected(getActivity());
							if(!isInternetPresent){
								onDetectNetworkState().show();
							}
							else {
								new GetProducts().execute();
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				});


			}
			else{
				Toast.makeText(getActivity(), "You are not eligible for purchase.", Toast.LENGTH_LONG).show();
				rl_mp_site.setEnabled(false);
				rl_mp_site.setEnabled(false);
			}
		}
	}
	private class GetProducts extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();

			productList.clear();
		}
		@Override
		protected Void doInBackground(Void... params1) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
			params.put("FamilyID",familyID );
					
			String responseString = WebServicesCall.RunScript(AppConfiguration.productbyFamily, params);
			
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load1 = reader.getString("Success");
				if(data_load1.toString().equals("True"))
				{

					JSONArray jsonMainNode = reader.optJSONArray("PhoneList");

					for (int i = 0; i < jsonMainNode.length(); i++) {
						HashMap<String, String> hashmap = new HashMap<String, String>();

						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

						String siteId = jsonChildNode.getString("siteId").trim();
						String sitename = jsonChildNode.getString("sitename").trim();

						hashmap.put("siteId", siteId);
						hashmap.put("sitename", sitename);

						productList.add(hashmap);

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
			if(data_load1.toString().equalsIgnoreCase("True")){

				rl_mp_product = (LinearLayout)rootView.findViewById(R.id.rl_mp_products);
				rl_mp_product.removeAllViews();
				rg2 = new RadioGroup(getActivity().getApplicationContext());
				rg2.removeAllViews();
				RadioGroup.LayoutParams params_soiled = new RadioGroup.LayoutParams(getActivity().getBaseContext(), null);
				params_soiled.setMargins(0, 10, 10, 0);
				rb2 = new RadioButton[productList.size()];

				rg2.setOrientation(RadioGroup.VERTICAL);
				for (int i = 0; i < productList.size(); i++) {
					rb2[i] = new RadioButton(getActivity().getApplicationContext());
					rg2.addView(rb2[i]);
					rb2[i].setText(productList.get(i).get("sitename"));
					rb2[i].setPadding(20, 0, 0, 0);
					rb2[i].setLayoutParams(params_soiled);
					rb2[i].setId(i);
					rb2[i].setButtonDrawable(R.drawable.customdrawableradionbuttons);
					rb2[i].setTextColor(getResources().getColor(R.color.app_text));
				}
				rl_mp_product.addView(rg2);
				rg2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						try{
							//int a = productname.indexOf(productname.get(checkedId));
							productID = productList.get(checkedId).get("siteId");//productid.get(a);
							Log.i("Product id", ""+productID);
							if(productID.toString().equalsIgnoreCase("1")){
								Intent swimlessonsIntent = new Intent(getActivity(), SwimLessonsActivity.class);
								swimlessonsIntent.putExtra("SiteId", siteID);
								startActivity(swimlessonsIntent);
								rb2[checkedId].setChecked(false);
							}
							else if(productID.toString().equalsIgnoreCase("2")){
								Intent swimcompetitionIntent = new Intent(getActivity(), SwimCompititionRegisterAcitivity.class);
								swimcompetitionIntent.putExtra("SiteId", siteID);
								startActivity(swimcompetitionIntent);
								rb2[checkedId].setChecked(false);
							}
							else if(productID.toString().equalsIgnoreCase("3")){
								Intent swimcampsIntent = new Intent(getActivity(), SwimCampsRegister1Acitivity.class);
								swimcampsIntent.putExtra("SiteId", siteID);
								startActivity(swimcampsIntent);
								rb2[checkedId].setChecked(false);
							}
							else if(productID.toString().equalsIgnoreCase("4")){
								Intent swimcampsIntent = new Intent(getActivity(), SwimTeamActivity.class);
								swimcampsIntent.putExtra("SiteId", siteID);
								startActivity(swimcampsIntent);
								rb2[checkedId].setChecked(false);
							}
							else if(productID.toString().equalsIgnoreCase("5")){
								Intent lapswimIntent = new Intent(getActivity(), LapSwimsActivity.class);
								lapswimIntent.putExtra("SiteId", siteID);
								startActivity(lapswimIntent);
								rb2[checkedId].setChecked(false);
							}
							else if(productID.toString().equalsIgnoreCase("6")){

								Intent waterAerobicsIntent = new Intent(getActivity(), WaterAerobicsActivity.class);
								waterAerobicsIntent.putExtra("SiteId", siteID);
								startActivity(waterAerobicsIntent);
								rb2[checkedId].setChecked(false);

							}
							else if(productID.toString().equalsIgnoreCase("7")){
								Intent fsnIntent = new Intent(getActivity(), FamilySwimNightActivity.class);
								fsnIntent.putExtra("SiteId", siteID);
								startActivity(fsnIntent);
								rb2[checkedId].setChecked(false);
							}
							else if(productID.toString().equalsIgnoreCase("8")){
								Intent transfermakeupIntent = new Intent(getActivity(), NewTransferMakeUpActivity.class);
								transfermakeupIntent.putExtra("SiteId", siteID);
								startActivity(transfermakeupIntent);
								rb2[checkedId].setChecked(false);
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				});

			}
			else{
				Toast.makeText(getActivity(), "You are not eligible for purchase.", Toast.LENGTH_LONG).show();
				rl_mp_site.setEnabled(false);
				rl_mp_site.setEnabled(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isInternetPresent){
			switch (v.getId()) {
			case R.id.btn_mp_past_due_balance:
				Intent pastduebalIntent = new Intent(getActivity(), PastDueBalActivity.class);
				startActivity(pastduebalIntent);
				break;
			case R.id.btn_mp_view_cart:
				Intent viewcartIntent = new Intent(getActivity(), ViewCartActivity.class);
				startActivity(viewcartIntent);
				break;
			case R.id.btn_mp_view_payments:
				Intent viewpaymentIntent = new Intent(getActivity(), ViewPaymentHistoryListActivity.class);
				startActivity(viewpaymentIntent);
				break;

			default:
				break;
			}
		}
		else{
			onDetectNetworkState().show();
		}
	}
}
