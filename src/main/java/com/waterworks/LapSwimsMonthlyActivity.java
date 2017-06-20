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
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class LapSwimsMonthlyActivity extends Activity {
	Button btn_submit;
	private static String TAG = "Lap Swim Session by month";
	ImageButton ib_back;
	Boolean isInternetPresent = false;
	String data_load ="False",data_load_basket="False";
	String siteid;
	ListView lv_list_month;
	ArrayList<String> month_name,month_id;
	ArrayList<String> finalmonths ;
	String token,familyID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lap_swims_monthly);

		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

		month_name = new ArrayList<String>();
		month_id = new ArrayList<String>();
		
		
		isInternetPresent = Utility
				.isNetworkConnected(LapSwimsMonthlyActivity.this);
		if(isInternetPresent){
			siteid = getIntent().getStringExtra("siteid");
			Initialization();
			new MonthList().execute();
			if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
				new GetBasketID().execute();
			}
		}
		else{
			onDetectNetworkState().show();
		}
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		lv_list_month = (ListView)findViewById(R.id.lv_ls2_list);
		btn_submit = (Button)findViewById(R.id.btn_ls2_submit);
		
//		month_name.add("JANUARY "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("FEBRUARY "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("MARCH "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("APRIL "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("MAY "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("JUNE "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("JULY "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("AUGUST "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("SEPTEMBER "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("OCTOBER "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("NOVEMBER "+Calendar.getInstance().get(Calendar.YEAR));
//		month_name.add("DECEMBER "+Calendar.getInstance().get(Calendar.YEAR));
		
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//		super.onBackPressed();
		Intent i = new Intent(getApplicationContext(),LapSwimsActivity.class);
		i.putExtra("SiteId", siteid);
		startActivity(i);
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
		isInternetPresent = Utility
				.isNetworkConnected(LapSwimsMonthlyActivity.this);
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(LapSwimsMonthlyActivity.this);
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
	
	
	public class MonthList extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(LapSwimsMonthlyActivity.this);
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
							month_name.add(jsonChildNode.getString("MonthName"));
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
			if(data_load_basket.toString().equalsIgnoreCase("True")){
				lv_list_month.setAdapter(new ListAdapterMonth(LapSwimsMonthlyActivity.this, month_name,btn_submit));
			}
			else{
			}
		}
	}
	
	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(LapSwimsMonthlyActivity.this);
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



	public class ListAdapterMonth extends BaseAdapter{
		Context context;
		ArrayList<String> month_selection;
		ArrayList<Integer> month_no;
		Button btn_submit;
		SparseBooleanArray mChecked;
		public ListAdapterMonth(Context context,
				ArrayList<String> month_selection,
				Button btn_submit) {
			super();
			this.context = context;
			this.month_selection = month_selection;
			this.btn_submit = btn_submit;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return month_selection.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		@Override
		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}

		public class ViewHolder{
			TextView tv_name;
			CheckBox chb_check;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if(convertView==null){
					holder = new ViewHolder();

					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.checkbox_right_side, null);
					holder.tv_name = (TextView)convertView.findViewById(R.id.tv_textview);
					holder.chb_check = (CheckBox)convertView.findViewById(R.id.chb_checkbox);
					mChecked = new SparseBooleanArray();
					month_no = new ArrayList<Integer>();
					holder.tv_name.setText(month_selection.get(position));
					holder.chb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked){     
								mChecked.put(Integer.parseInt(month_id.get(position)),isChecked);
								Log.i(TAG, ""+mChecked);
							} else{          
								mChecked.delete(position);
								Log.i(TAG, ""+mChecked);
							}
						}
					});
					finalmonths = new ArrayList<String>();
					btn_submit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int newpos;
							for(int i=0;i<mChecked.size();i++){
								newpos = mChecked.keyAt(i);
								finalmonths.add(""+month_no.get(newpos));
							}
							Log.i(TAG, ""+finalmonths.toString());
							if(finalmonths.toString().replaceAll("\\[", "").replaceAll("\\]", "").equalsIgnoreCase("")){
								Toast.makeText(context, "Please select at least a month.", Toast.LENGTH_LONG).show();
							}
							else{
								new LapSwimMonthSubmit().execute();
							}

						}
					});
				}
				else{
					holder = (ViewHolder) convertView.getTag();
				}

				for (int i = 0; i < 12; i++) {
					month_no.add(i);
				}


			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
			catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return convertView;
		}

		private class LapSwimMonthSubmit extends AsyncTask<Void, Void, Void>{
			ProgressDialog pd;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd = new ProgressDialog(context);
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
				param.put("SiteID", siteid);
				param.put("SelectLapSwimMonth",finalmonths.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

				String responseString = WebServicesCall
						.RunScript(AppConfiguration.lapswimbutton2submit, param);

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
					Intent viewcart = new Intent(getApplicationContext(), ViewCartActivity.class);
					startActivity(viewcart);

					finish();
				}
				else{
					Toast.makeText(getApplicationContext(), "Some internal error, Please try after sometime.", Toast.LENGTH_LONG).show();
				}
			}

		}

	}
}
