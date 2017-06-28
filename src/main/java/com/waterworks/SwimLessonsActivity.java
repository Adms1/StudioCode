package com.waterworks;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class SwimLessonsActivity extends Activity implements OnClickListener{
	private static String TAG = "Swim lessons";
	TextView tv_sl_lt,tv_cost,tv_subtotal,tv_package;
	Button btn_submit,btn_lt,btn_package;
	ImageButton ib_back;
	ArrayList<String> LessonName,LessonId,Quantity,PackageID;
	Boolean isInternetPresent = false;
	ListPopupWindow pw_lessontype,pw_package;
	String token,familyID;
	double PACKAGECOST;
	String lessonid,siteid,data_load="false",finalpackageID,data_load_basket="false",
			data_load_submit="false",Packageid,data_load_sheet="false",ViewSheetSiteName,ViewSheetSiteURL,pricesheetfilename;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_lessons);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

		isInternetPresent = Utility
				.isNetworkConnected(SwimLessonsActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			siteid = getIntent().getStringExtra("SiteId");
			AppConfiguration.basket_siteid = siteid;
			Initialization();
		}
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		tv_sl_lt = (TextView)findViewById(R.id.tv_sl_lt);
		tv_cost = (TextView)findViewById(R.id.tv_sl_cost_item);
		tv_subtotal = (TextView)findViewById(R.id.tv_sl_subtotal_item);
		btn_submit = (Button)findViewById(R.id.btn_sl_submit);
		btn_lt = (Button)findViewById(R.id.btn_sl_lt);
		btn_package = (Button)findViewById(R.id.btn_sl_package);
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		tv_package = (TextView)findViewById(R.id.tv_sl_package);
		btn_package.setEnabled(false);
		btn_lt.setOnClickListener(this);
		btn_package.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		btn_submit.setEnabled(false);
		SpannableString ss = new SpannableString("Lesson Type View Price Sheet");
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				if(isInternetPresent){
					new viewpricesheet().execute();
				}
				else{
					onDetectNetworkState().show();
				}
			}
		};
		ss.setSpan(clickableSpan, 12, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.link)), 13, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_sl_lt.setText(ss);
		tv_sl_lt.setMovementMethod(LinkMovementMethod.getInstance());

		LessonId = new ArrayList<String>();
		LessonName = new ArrayList<String>();
		LessonId.add("0");  LessonName.add("Please Choose");
		LessonId.add("1");  LessonName.add("Private");
		LessonId.add("2");  LessonName.add("Semi-Private");
		LessonId.add("4"); LessonName.add("Adult");
		if(!siteid.equals("19")){

			LessonId.add("13");  LessonName.add("Stroke Clinic");
			LessonId.add("6");  LessonName.add("P&M Adv");
			LessonId.add("9");  LessonName.add("P&M Beg / Int");
		}


		pw_lessontype = new ListPopupWindow(getApplicationContext());
		pw_lessontype.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				R.layout.edittextpopup,LessonName));
		pw_lessontype.setAnchorView(btn_lt);
		pw_lessontype.setHeight(LayoutParams.WRAP_CONTENT);
		pw_lessontype.setModal(true);
		pw_lessontype.setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int pos, long id) {
						// TODO Auto-generated method stub
						btn_lt.setText(LessonName.get(pos));
						lessonid = LessonId.get(pos);
						pw_lessontype.dismiss();
						if(isInternetPresent){
							new viewpricesheet().execute();
						}
						else {
							new GetPackage().execute();
						}
					}
				});

	}

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(SwimLessonsActivity.this);
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
				.isNetworkConnected(SwimLessonsActivity.this);
		if(!isInternetPresent){
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
		if(isInternetPresent){
			switch (v.getId()) {
			case R.id.ib_back:
				onBackPressed();
				break;
			case R.id.btn_sl_lt:
				pw_lessontype.show();
				btn_package.setText("");
				tv_cost.setText("");
				tv_subtotal.setText("");
				break;
			case R.id.btn_sl_package:
				pw_package.show();
				break;
			case R.id.btn_sl_submit:
				if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
					new GetBasketID().execute();
				}
				else{
					new swimlessonsubmit().execute();
				}
				break;
			default:
				break;
			}
		}
		else{
			onDetectNetworkState().show();
		}
	}

	private class GetPackage extends AsyncTask<Void, Void, Void>{

		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(SwimLessonsActivity.this);
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
			param.put("SiteID", siteid);
			param.put("LessonTypeID", lessonid);

			String responseString = WebServicesCall.RunScript(AppConfiguration.swimpackage, param);
			GetPackage(responseString);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			btn_package.setEnabled(true);
			if(data_load.toString().equalsIgnoreCase("True")){
				pw_package = new ListPopupWindow(getApplicationContext());
				pw_package.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup,Quantity));
				pw_package.setAnchorView(btn_package);
				pw_package.setHeight(LayoutParams.WRAP_CONTENT);
				pw_package.setModal(true);
				pw_package.setOnItemClickListener(
						new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int pos, long id) {
								// TODO Auto-generated method stub
								btn_package.setText(Quantity.get(pos));
								Packageid = PackageID.get(pos);
								String value[] = PackageID.get(pos).toString().split("\\|");
								finalpackageID = value[0];
								PACKAGECOST = Double.valueOf(value[1]);
								Log.i(TAG, "Package id = " + finalpackageID);
								DecimalFormat df = new DecimalFormat("#.00");
								PACKAGECOST = Double.valueOf(df.format(PACKAGECOST));
								String b[] = String.valueOf(PACKAGECOST).toString().split("\\.");
								if(b[1].length()==1){
									b[1] = b[1]+"0";
								}
								tv_cost.setText("$"+b[0]+"."+b[1]);
								tv_subtotal.setText("$"+b[0]+"."+b[1]);
								btn_submit.setEnabled(true);
								pw_package.dismiss();
							}
						});

			}
			else{
				Toast.makeText(SwimLessonsActivity.this, "No Package to purchase.", Toast.LENGTH_LONG).show();
				btn_package.setEnabled(false);
			}
		}
	}

	public void GetPackage(String response){
		try {
			JSONObject reader = new JSONObject(response);
			data_load = reader.getString("Success");
			if(data_load.toString().equals("True"))
			{
				PackageID = new ArrayList<String>();
				Quantity = new ArrayList<String>();
				JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
				if(jsonMainNode.toString().equalsIgnoreCase("")){

				}
				else{
					for(int i = 0 ;i < jsonMainNode.length();i++)
					{
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						PackageID.add(jsonChildNode.getString("PackageID"));
						Quantity.add(jsonChildNode.getString("Quantity"));
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

	private class swimlessonsubmit extends AsyncTask<Void, Void, Void>{

		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(SwimLessonsActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub


			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token", ""+token);
			param.put("FamilyID", ""+familyID);
			param.put("SiteID", siteid);
			param.put("BasketID", AppConfiguration.BasketID);
			param.put("LessonID", lessonid);
			param.put("PackageID",Packageid);
			String responseString = WebServicesCall.RunScript(AppConfiguration.swimsubmit, param);

			SubmitSwimLesson(responseString);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if(data_load_submit.toString().equalsIgnoreCase("True")){
				if(submited){
					Intent i = new Intent(getApplicationContext(), ViewCartActivity.class);
					startActivity(i);
					finish();
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "Not able to add in cart,  Please try after sometime.",Toast.LENGTH_LONG).show();
			}
		}
	}

	public void SubmitSwimLesson(String response){
		try {
			JSONObject reader = new JSONObject(response);
			data_load_submit = reader.getString("Success");
			if(data_load_submit.toString().equals("True"))
			{
				submited = true;
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	Boolean submited = false;


	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(SwimLessonsActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token", ""+token);
			param.put("siteid", siteid);

			String responseString = WebServicesCall.RunScript(AppConfiguration.Get_BasketID, param);

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
					Toast.makeText(SwimLessonsActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
				}
				else{
					if (!isInternetPresent) {
						onDetectNetworkState().show();
					} else {
						new swimlessonsubmit().execute();
					}
				}
			}
			else{
				Toast.makeText(SwimLessonsActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
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
	private class viewpricesheet extends AsyncTask<Void, Void, Void	>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(SwimLessonsActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token", ""+token);
			param.put("FamilyID", ""+familyID);
			param.put("SiteID", siteid);

			String responseString = WebServicesCall.RunScript(AppConfiguration.viewpricesheet, param);
			ViewPriceSheet(responseString);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if(data_load_sheet.toString().equalsIgnoreCase("True")){
				new DownloadFile().execute(ViewSheetSiteURL, pricesheetfilename); 
			}
			else{
				Toast.makeText(SwimLessonsActivity.this, "No price sheet found.", Toast.LENGTH_LONG).show();
			}
		}
	}
	private class DownloadFile extends AsyncTask<String, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(SwimLessonsActivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... strings) {
			String fileUrl = strings[0];  
			String fileName = strings[1]; 
			String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
			File folder = new File(extStorageDirectory, ".WaterWorksPriceSheet");
			folder.mkdir();

			File pdfFile = new File(folder, fileName);

			try{
				pdfFile.createNewFile();
			}catch (IOException e){
				e.printStackTrace();
			}
			FileDownloader.downloadFile(fileUrl, pdfFile);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			showfile();
		}
	}
	public void showfile(){
		File pdfFile = new File(Environment.getExternalStorageDirectory() + "/.WaterWorksPriceSheet/" + pricesheetfilename);  // -> filename = maven.pdf
		Uri path = Uri.fromFile(pdfFile);
		Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
		pdfIntent.setDataAndType(path, "application/pdf");
		pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try{
			startActivity(pdfIntent);
		}catch(ActivityNotFoundException e){
			Toast.makeText(SwimLessonsActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
		}
	}
	public void ViewPriceSheet(String response){
		try {
			JSONObject reader = new JSONObject(response);
			data_load_sheet = reader.getString("Success");
			if(data_load_sheet.toString().equals("True"))
			{
				JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
				if(jsonMainNode.toString().equalsIgnoreCase("")){
				}
				else{
					for(int i = 0 ;i < jsonMainNode.length();i++)
					{
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						ViewSheetSiteName = jsonChildNode.getString("SiteName");
						ViewSheetSiteURL = jsonChildNode.getString("SiteURL");
						pricesheetfilename = jsonChildNode.getString("FileName");
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
