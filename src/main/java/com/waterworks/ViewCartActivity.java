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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.ViewCartAdapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class ViewCartActivity extends Activity implements OnClickListener{
	Boolean isInternetPresent = false;
	private static final String TAG = "View cart";
	String message,PromoCode;
	String data_load_page= "False",data_load_delete= "False",data_load_apply= "False",
			data_load_empty= "False",data_load_basket="False";
	
	ListView lv_view_cart;
	Button btn_continue,btn_empty_basket,btn_checkout,btn_promocode;
	EditText et_promocode;
	public TextView tv_total;
	ImageButton ib_back;
	
	String _Total,_PaidAmount,_DueAmount;
	ArrayList<String> _index,_ItemTypeID,_Type,_Item,_Package,_Price,_Qty,_Tax,_Subtotal,_Delete,DeleteEblDble;
	String token,familyID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_cart);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
		
		isInternetPresent = Utility
				.isNetworkConnected(ViewCartActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			if(AppConfiguration.BasketID.toString().equalsIgnoreCase("0")){
				Toast.makeText(getApplicationContext(), "No balance due..", Toast.LENGTH_LONG).show();
			}
			else{
				new GetCart().execute();
			}
		}
	}

	
	private void Initialization() {
		// TODO Auto-generated method stub
		tv_total = (TextView)findViewById(R.id.tv_view_cart_total);
		btn_checkout = (Button)findViewById(R.id.btn_view_cart_checkout);
		btn_continue = (Button)findViewById(R.id.btn_view_cart_continue_shopping);
		btn_empty_basket = (Button)findViewById(R.id.btn_view_cart_empty_basket);
		btn_promocode = (Button)findViewById(R.id.btn_view_cart_promocode);
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		ib_back.setOnClickListener(this);
		btn_checkout.setOnClickListener(this);
		btn_continue.setOnClickListener(this);
		btn_empty_basket.setOnClickListener(this);
		btn_promocode.setOnClickListener(this);
		lv_view_cart = (ListView)findViewById(R.id.lv_view_cart);
		et_promocode = (EditText)findViewById(R.id.et_view_cart_promocode);
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(ViewCartActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
	}
	
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewCartActivity.this);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isInternetPresent){
			switch (v.getId()) {
			case R.id.ib_back:
				Intent i1 = new Intent(this,FamilySwimNightActivity.class);
				startActivity(i1);
				break;
			case R.id.btn_view_cart_promocode:
				if(et_promocode.getText().toString().isEmpty()){
					Toast.makeText(getApplicationContext(), "Please enter promo code before redeem it.", Toast.LENGTH_LONG).show();
				}
				else{
					PromoCode = et_promocode.getText().toString();
					new ApplyPromocode().execute();
				}
				break;
			case R.id.btn_view_cart_continue_shopping:
					Intent continueIntent = new Intent(getApplicationContext(), DashBoardActivity.class);
					continueIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					continueIntent.putExtra("POS", 4);
					startActivity(continueIntent);
					finish();
				break;
			case R.id.btn_view_cart_checkout:
				if(AppConfiguration.BasketID.toString().equalsIgnoreCase("0")){
					Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent i = new Intent(this,ReceivePayment.class);
					i.putExtra("TOKEN", token);
					i.putExtra("FamilyID", familyID);
					startActivity(i);
				}
				break;
			case R.id.btn_view_cart_empty_basket:
					new EmptyBasket().execute();
					
				break;
				

			default:
				break;
			}
		}
		else{
			onDetectNetworkState().show();
		}
	}
	
	
	public class GetCart extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ViewCartActivity.this);
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

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.viewcart__pasduebal_basketid, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load_page = reader.getString("Success");
				if(data_load_page.toString().equals("True"))
				{
					_Total =  reader.getString("Total");
					_index = new ArrayList<String>();
					_ItemTypeID = new ArrayList<String>();
					_Type = new ArrayList<String>();
					_Item = new ArrayList<String>();
					_Package = new ArrayList<String>();
					_Price = new ArrayList<String>();
					_Qty = new ArrayList<String>();
					_Tax = new ArrayList<String>();
					_Subtotal = new ArrayList<String>();
					_Delete = new ArrayList<String>();
					DeleteEblDble = new ArrayList<String>();
					 JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
			         for(int i = 0 ;i < jsonMainNode.length();i++)
			         {
			        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	    					_index.add(jsonChildNode.getString("index"));
	    					_ItemTypeID.add(jsonChildNode.getString("ItemTypeID"));
	    					_Type.add(jsonChildNode.getString("Type"));
	    					_Item.add(jsonChildNode.getString("Item"));
	    					_Package.add(jsonChildNode.getString("Package"));
	    					_Price.add(jsonChildNode.getString("Price"));
	    					_Qty.add(jsonChildNode.getString("Qty"));
	    					_Tax.add(jsonChildNode.getString("Tax"));
	    					_Subtotal.add(jsonChildNode.getString("Subtotal"));
	    					_Delete.add(jsonChildNode.getString("Delete"));
	    					DeleteEblDble.add(jsonChildNode.getString("DeleteEblDble"));
			         }
			         }
				else{
					 JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
		        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
		        	 msg= jsonChildNode.getString("Msg");
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
			if(data_load_page.toString().equals("True"))
			{
				tv_total.setText("Total: $" + _Total);
				lv_view_cart.setVisibility(View.VISIBLE);
				lv_view_cart.setAdapter(new ViewCartAdapter(ViewCartActivity.this,_index,_ItemTypeID,_Item,
						_Type,_Package,_Price,_Qty,_Tax,_Subtotal,_Delete,DeleteEblDble));
			}
			else
			{
				Toast.makeText(ViewCartActivity.this, msg, Toast.LENGTH_LONG).show();
				lv_view_cart.setVisibility(View.GONE);
			}
			
		}
	}
	
	String msg;
	
	
	private class ApplyPromocode extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ViewCartActivity.this);
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
			param.put("PromoCode", PromoCode);

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.applypromocode, param);
			try {
            	JSONObject reader = new JSONObject(responseString);
            	data_load_apply = reader.getString("Success");
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
			if(data_load_apply.toString().equals("True"))
			{
				Toast.makeText(getApplicationContext(), "Promo code applyed successfully.", Toast.LENGTH_LONG).show();
				new GetCart().execute();
			}
			else{
				Toast.makeText(getApplicationContext(), "Promo code not applyed.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private class EmptyBasket extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ViewCartActivity.this);
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

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.emptybasket_byid, param);
			
			 try {
             	JSONObject reader = new JSONObject(responseString);
             	data_load_empty = reader.getString("Success");
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
			if(data_load_empty.toString().equals("True"))
			{
				Toast.makeText(getApplicationContext(), "Basket refreshed.", Toast.LENGTH_LONG).show();
				AppConfiguration.BasketID ="BasketID";
				tv_total.setText("Total:");
				//finish();
				
				Intent i = new Intent(ViewCartActivity.this,DashBoardActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("POS", 4);
				startActivity(i);
//				new GetBasketID().execute();
			}
			else{
				Toast.makeText(getApplicationContext(), "Basket not refreshed.", Toast.LENGTH_LONG).show();
			}
		}
	}
	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ViewCartActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("siteid", AppConfiguration.basket_siteid);

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
					Toast.makeText(ViewCartActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
				}
				else{
					new GetCart().execute();
				}
			}
			else{
				Toast.makeText(ViewCartActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
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
}