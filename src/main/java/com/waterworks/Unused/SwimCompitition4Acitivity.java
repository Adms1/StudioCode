package com.waterworks.Unused;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.NewSwimCompitition5Activity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class SwimCompitition4Acitivity extends Activity {
	String TAG = "SwimCampsActivity4";

	ArrayList<HashMap<String, String>> selectedChildDataMainList = new ArrayList<HashMap<String, String>>();
	ArrayList<Boolean> selectedStudents  = new ArrayList<Boolean>();


	Boolean isInternetPresent = false;
	String siteID;
	String successLoadChildList;
	String token,familyID;
	Button btnContinueStep4;
	String successSwimCompittionCheck1;
	String messageNormal;
	String message;
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_compitition4);

		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

		isInternetPresent = Utility.isNetworkConnected(SwimCompitition4Acitivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			new LoadSelectedChildDataListAsyncTask().execute();
		}



		// fetching header view


        View view = findViewById(R.id.layout_top);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Swim Competition Registration:Step 4 of 5");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			}
		});

		btnContinueStep4 = (Button)findViewById(R.id.btnContinueStep4);
		btnContinueStep4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(successLoadChildList.toString().equals("True"))
				{
					if(selectedStudents.toString().contains("false"))
					{
						Toast.makeText(getApplicationContext(), "Please check all the conditions to continue", Toast.LENGTH_LONG).show();
					}
					else
					{
						Intent i = new Intent(SwimCompitition4Acitivity.this,NewSwimCompitition5Activity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					}
				}
				else
				{
					Intent i = new Intent(SwimCompitition4Acitivity.this,NewSwimCompitition5Activity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			}
		});
	}
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				finish();
			}
		})
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		return builder1.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.swim_camps, menu);
		return true;
	}

	public void loadingChildList() {
		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("MeetdatetimeValue", AppConfiguration.SwimMeetID);
		param.put("MeetdatetimeText", AppConfiguration.SwimMeetText);
		param.put("forstep4", "0");
		param.put("count", "0");

		String responseString = WebServicesCall
				.RunScript(AppConfiguration.swimCompititionStep4URL, param);
		readAndParseJSONChildList(responseString);
	}

	public void readAndParseJSONChildList(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successLoadChildList = reader.getString("Success");
			if (successLoadChildList.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck4");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					String messageText = jsonChildNode.getString("messageText").trim();
					String messageValue = jsonChildNode.getString("messageValue").trim();

					hashmap.put("messageText", messageText);
					hashmap.put("messageValue", messageValue);

					selectedChildDataMainList.add(hashmap);

					selectedStudents.add(false);

				}

			} else {
				JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck4");

				for (int i = 0; i < jsonMainNode.length(); i++) {

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					message = jsonChildNode.getString("Msg").trim();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	class LoadSelectedChildDataListAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(SwimCompitition4Acitivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			selectedChildDataMainList.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			loadingChildList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}


			if (successLoadChildList.toString().equals("True")) {

				CustomList adapter = new CustomList(SwimCompitition4Acitivity.this,selectedChildDataMainList);
				list.setAdapter(adapter);


			} else {
				Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_LONG)
				.show();
			}
		}
	}
	public static class ViewHolder{
		TextView txtMessageText;
		CheckBox chk;
	}

	public class CustomList extends BaseAdapter {
		private final ArrayList<HashMap<String, String>> data;
		ViewHolder holder;
		public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
			super();
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
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {

			try{
				if(view==null){
					holder = new ViewHolder();

					view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_swim_compitition4, null);

					holder.txtMessageText = (TextView) view.findViewById(R.id.txtMessageText);
					holder.chk = (CheckBox)view.findViewById(R.id.chk);

					view.setTag(holder);
				}
				else
				{
					holder = (ViewHolder) view.getTag();
				}

				holder.txtMessageText.setText(Html.fromHtml(selectedChildDataMainList.get(position).get("messageText")));

				holder.chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){          
							selectedStudents.set(position, true);
							Log.e("checkedData",selectedStudents.toString());
						} else{          
							selectedStudents.set(position, false);
							Log.e("checkedData",selectedStudents.toString());
						}
					}
				});
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
			catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return view;
		}
	}
}
