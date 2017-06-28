package com.waterworks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ViewCertificateActivity extends Activity {
	
	ArrayList<HashMap<String, String>> ViewCertificateList = new ArrayList<HashMap<String, String>>();
	
	ListView list;
	TextView txtNoRecord;
	String successViewCertificate;
	RelativeLayout rel_list;
	String TAG = "ViewCerfiticate";
	String filename;
	Context mContext =this;
	Boolean isInternetPresent =false;

	String token,familyID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewcertificate_activity);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		
		// fetching header view
        View view = findViewById(R.id.actionbar);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("View Certificate");
        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
		
		rel_list = (RelativeLayout) findViewById(R.id.rel_list);
		
		txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				isInternetPresent = Utility.isNetworkConnected(ViewCertificateActivity.this);
				if (!isInternetPresent) {
					onDetectNetworkState().show();
				} else {
					String pdfURL = ViewCertificateList.get(position).get("Link") + ViewCertificateList.get(position).get("certname");
					filename = ViewCertificateList.get(position).get("certname");


					new DownloadFile().execute(pdfURL, filename);
				}
			}
		});
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {

			new GetViewCertificateAsyncTask().execute();
		}
	}
	
	public void getViewCertificateList() {
		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );

		String responseString = WebServicesCall
		.RunScript(AppConfiguration.viewCertificateURL, param);
		readAndParseJSON(responseString);
		
	}
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
		builder1.setIcon(R.drawable.logo);
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		return builder1.create();
	}
	public void readAndParseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successViewCertificate = reader.getString("Success");
			if (successViewCertificate.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("Certificate");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					
					String Sfirstname = jsonChildNode.getString("Sfirstname").trim();
					String Slastname = jsonChildNode.getString("Slastname").trim();
					String levelname = jsonChildNode.getString("levelname").trim();
					String studentid = jsonChildNode.getString("studentid").trim();
					String certname = jsonChildNode.getString("certname").trim();
					String level = jsonChildNode.getString("level").trim();
					String Link = jsonChildNode.getString("Link").trim();
					String createdate = jsonChildNode.getString("createdate").trim();
					
									
					hashmap.put("Sfirstname", Sfirstname);
					hashmap.put("Slastname", Slastname);
					hashmap.put("levelname", levelname);
					hashmap.put("studentid", studentid);
					hashmap.put("certname", certname);
					hashmap.put("level", level);
					hashmap.put("Link", Link);
					hashmap.put("createdate", createdate);
					
				

					ViewCertificateList.add(hashmap);

				}

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class GetViewCertificateAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(ViewCertificateActivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			ViewCertificateList.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			getViewCertificateList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (ViewCertificateList.size() > 0) {
				txtNoRecord.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
			} else {
				txtNoRecord.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}

			if (successViewCertificate.toString().equals("True")) {

				CustomList adapter = new CustomList(ViewCertificateActivity.this,ViewCertificateList);
				list.setAdapter(adapter);
				

			} else {
			}
		}
	}

	public class CustomList extends ArrayAdapter<String> {
		private final Activity context;
		private final ArrayList<HashMap<String, String>> data;

		public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
			super(context, R.layout.list_row_viewcertificate);
			this.context = context;
			this.data = list;
		}

		@Override
	    public int getCount() {
	        return data.size();
	    }
	 
	 
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	    
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.list_row_viewcertificate, null, true);
			
			TextView txtFirstName = (TextView) rowView.findViewById(R.id.txtFirstName);
			txtFirstName.setText(ViewCertificateList.get(position).get("Sfirstname"));
			
			TextView txtLastname = (TextView) rowView.findViewById(R.id.txtLastname);
			txtLastname.setText(ViewCertificateList.get(position).get("Slastname"));
			
			TextView txtLevels = (TextView) rowView.findViewById(R.id.txtLevels);
			txtLevels.setText("Level: "+ViewCertificateList.get(position).get("levelname"));
			
			TextView txtDate = (TextView) rowView.findViewById(R.id.txtDate);
			txtDate.setText("Date: "+ViewCertificateList.get(position).get("createdate"));
			
			TextView txtLink = (TextView) rowView.findViewById(R.id.txtLink);
			txtLink.setText("View");
			
			
		
			
			return rowView;
		}
	}

	
	private class DownloadFile extends AsyncTask<String, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ViewCertificateActivity.this);
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
		File pdfFile = new File(Environment.getExternalStorageDirectory() + "/.WaterWorksPriceSheet/" + filename);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(ViewCertificateActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
	}
	
}
