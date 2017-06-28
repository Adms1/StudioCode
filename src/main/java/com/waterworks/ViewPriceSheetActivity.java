package com.waterworks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class ViewPriceSheetActivity extends Activity {
	ImageButton ib_back;
	ListView lv_pricesheet;
	Boolean isInternetPresent = false;
	private static final String TAG = "View Price Sheet";
	String data_load_sheet = "False";
	ArrayList<String> ViewSheetSiteName,ViewSheetSiteURL,pricesheetfilename;
	String token,familyID; 

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_price_sheet);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

		isInternetPresent = Utility
				.isNetworkConnected(ViewPriceSheetActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			new viewpricesheet().execute();
		}
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		lv_pricesheet = (ListView)findViewById(R.id.lv_pricesheet_list);
		ib_back = (ImageButton)findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewPriceSheetActivity.this);
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
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private class viewpricesheet extends AsyncTask<Void, Void, Void	>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ViewPriceSheetActivity.this);
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
			param.put("SiteID", "-1");

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.viewpricesheet, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load_sheet = reader.getString("Success");
				if(data_load_sheet.toString().equals("True"))
				{
					pricesheetfilename = new ArrayList<String>();
					ViewSheetSiteName = new ArrayList<String>();
					ViewSheetSiteURL = new ArrayList<String>();
					JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
					if(jsonMainNode.toString().equalsIgnoreCase("")){

					}
					else{
						for(int i = 0 ;i < jsonMainNode.length();i++)
						{
							JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							pricesheetfilename.add(jsonChildNode.getString("FileName"));
							ViewSheetSiteName.add(jsonChildNode.getString("SiteName"));

							String temp_url = jsonChildNode.getString("SiteURL");
							if(temp_url.contains(" ")){
								temp_url = temp_url.substring(0, temp_url.indexOf(" ")) + "%20" + temp_url.substring(temp_url.indexOf(" ")+1, temp_url.length());
							}
							ViewSheetSiteURL.add(temp_url);

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
			if(data_load_sheet.toString().equalsIgnoreCase("True")){
				if(pricesheetfilename.size()>0 && ViewSheetSiteName.size()>0){
					lv_pricesheet.setAdapter(new ViewPriceSheetAdapter(ViewPriceSheetActivity.this,ViewSheetSiteURL,pricesheetfilename,ViewSheetSiteName));
				}
				else{
					Toast.makeText(ViewPriceSheetActivity.this, "No price sheet found.", Toast.LENGTH_LONG).show();
				}
			}
			else{
				Toast.makeText(ViewPriceSheetActivity.this, "No price sheet found.", Toast.LENGTH_LONG).show();
			}
		}
	}


	private class ViewPriceSheetAdapter extends BaseAdapter{
		ArrayList<String> ViewSheetSiteName,ViewSheetSiteURL,pricesheetfilename;
		Context context;
		public ViewPriceSheetAdapter(Context context,
				ArrayList<String> viewSheetSiteURL,
				ArrayList<String> pricesheetfilename, ArrayList<String> viewSheetSiteName) {
			super();
			ViewSheetSiteName = viewSheetSiteName;
			ViewSheetSiteURL = viewSheetSiteURL;
			this.pricesheetfilename = pricesheetfilename;
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ViewSheetSiteName.size();
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
			TextView tv_pricesheet_name;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if(convertView==null){
					holder = new ViewHolder();

					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.edittextpopup, null);
					holder.tv_pricesheet_name = (TextView)convertView.findViewById(R.id.abc);
				}
				else{
					holder = (ViewHolder) convertView.getTag();
				}
				String name = ViewSheetSiteName.get(position);

				holder.tv_pricesheet_name.setBackgroundColor(Color.TRANSPARENT);
				SpannableString content = new SpannableString(name);
				content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
				holder.tv_pricesheet_name.setText(content);
				holder.tv_pricesheet_name.setTextColor(getResources().getColor(R.color.link));

				holder.tv_pricesheet_name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new DownloadFile().execute(ViewSheetSiteURL.get(position), pricesheetfilename.get(position));
					}
				});
			}
			catch(Exception e){
				e.printStackTrace();
			}

			return convertView;
		}

		String show_name;

		private	class DownloadFile extends AsyncTask<String, Void, Void>{
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
			protected Void doInBackground(String... strings) {
				String fileUrl = strings[0];  
				String fileName = strings[1]; 
				String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
				File folder = new File(extStorageDirectory, ".WaterWorksPriceSheet");
				folder.mkdir();
				show_name = fileName;
				File pdfFile = new File(folder, fileName);
				delete_exist(pdfFile);
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
			File pdfFile = new File(Environment.getExternalStorageDirectory() + "/.WaterWorksPriceSheet/" +show_name);  // -> filename = maven.pdf
			Uri path = Uri.fromFile(pdfFile);
			Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
			pdfIntent.setDataAndType(path, "application/pdf");
			pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			try{
				startActivity(pdfIntent);
			}catch(ActivityNotFoundException e){
				Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void delete_exist(File fdelete){
		if (fdelete.exists()) {
			if (fdelete.delete()) {
				System.out.println("file Deleted :");
			} else {
				System.out.println("file not Deleted :");
			}
		}
	}
}
