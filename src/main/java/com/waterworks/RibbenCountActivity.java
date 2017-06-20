package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
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

public class RibbenCountActivity extends Activity {
	
	ArrayList<HashMap<String, String>> ribbenList = new ArrayList<HashMap<String, String>>();
	
	ListView list;
	TextView txtNoRecord;
	String successRibbenCount;
	RelativeLayout rel_list;
	String TAG = "ViewCerfiticate";
	String filename;

	String token,familyID;
	Context mContext=this;
	Boolean isInternetPresent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ribben_count_activity);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
		
		// fetching header view

        View view = findViewById(R.id.actionbar);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Ribbon Count");
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
				
			   String studentID = ribbenList.get(position).get("StudentID");
			   String studentName = ribbenList.get(position).get("StudentName");
			   
			   Intent i = new Intent(RibbenCountActivity.this,RibbenCountDetailsActivity.class);
			   i.putExtra("studentName", studentName);
			   i.putExtra("studentID", studentID);
			   
			   startActivity(i);
			}
		});

		isInternetPresent = Utility.isNetworkConnected(RibbenCountActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new RibbenCountAsyncTask().execute();
		}
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

	public void getRibbenCounttList() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		
		String responseString = WebServicesCall
		.RunScript(AppConfiguration.ribbenCountURL, param);
		readAndParseJSON(responseString);
		
	}

	public void readAndParseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successRibbenCount = reader.getString("Success");
			if (successRibbenCount.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("SwimComp_RibbonCount");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					
					String StudentID = jsonChildNode.getString("StudentID").trim();
					String StudentCount = jsonChildNode.getString("StudentCount").trim();
					String totalstudents = jsonChildNode.getString("totalstudents").trim();
					String StudentName = jsonChildNode.getString("StudentName").trim();
					String meetcount = jsonChildNode.getString("meetcount").trim();
					String participation = jsonChildNode.getString("participation").trim();
					String timeimprovement = jsonChildNode.getString("timeimprovement").trim();
					String totalribbons = jsonChildNode.getString("totalribbons").trim();
									
					hashmap.put("StudentID", StudentID);
					hashmap.put("StudentCount", StudentCount);
					hashmap.put("totalstudents", totalstudents);
					hashmap.put("StudentName", StudentName);
					hashmap.put("meetcount", meetcount);
					hashmap.put("participation", participation);
					hashmap.put("timeimprovement", timeimprovement);
					hashmap.put("totalribbons", totalribbons);
					

					ribbenList.add(hashmap);

				}

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class RibbenCountAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(RibbenCountActivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			ribbenList.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			getRibbenCounttList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (ribbenList.size() > 0) {
				txtNoRecord.setVisibility(View.GONE);
				rel_list.setVisibility(View.VISIBLE);
			} else {
				txtNoRecord.setVisibility(View.VISIBLE);
				rel_list.setVisibility(View.GONE);
			}

			if (successRibbenCount.toString().equals("True")) {

				CustomList adapter = new CustomList(RibbenCountActivity.this,ribbenList);
				list.setAdapter(adapter);
				

			} else {
				Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	public class CustomList extends ArrayAdapter<String> {
		private final Activity context;
		private final ArrayList<HashMap<String, String>> data;

		public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
			super(context, R.layout.list_row_ribben_count);
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
			View rowView = inflater.inflate(R.layout.list_row_ribben_count, null, true);
			
			
			TextView txtStudentName = (TextView) rowView.findViewById(R.id.txtStudentName);
			txtStudentName.setText(ribbenList.get(position).get("StudentName"));
			
			TextView txtMeetsAttendedValue = (TextView) rowView.findViewById(R.id.txtMeetsAttendedValue);
			txtMeetsAttendedValue.setText(ribbenList.get(position).get("meetcount"));
			
			TextView txtParticipationRibbonsValue = (TextView) rowView.findViewById(R.id.txtParticipationRibbonsValue);
			txtParticipationRibbonsValue.setText(ribbenList.get(position).get("participation"));
			
			TextView txtTimeImprovementRibbonsValue = (TextView) rowView.findViewById(R.id.txtTimeImprovementRibbonsValue);
			txtTimeImprovementRibbonsValue.setText(ribbenList.get(position).get("timeimprovement"));
			
			TextView txtTotalRibbonsValue = (TextView) rowView.findViewById(R.id.txtTotalRibbonsValue);
			txtTotalRibbonsValue.setText(ribbenList.get(position).get("totalribbons"));
			
			
			return rowView;
		}
	}

	
		
}
