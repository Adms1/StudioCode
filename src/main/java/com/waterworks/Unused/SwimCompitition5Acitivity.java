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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class SwimCompitition5Acitivity extends Activity {
	String TAG = "SwimCampsActivity5";

	ArrayList<HashMap<String, String>> selectedListFromStep2= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> reminderTextList = new ArrayList<HashMap<String, String>>();
	String reminderText;

	Boolean isInternetPresent = false;
	String siteID;
	String successLoadChildList;
	String token,familyID;
	String messageMeetDate;
	String successMeetDate;
	Button btnContinue;
	boolean isSelectedAgreement = false;
	String msg1_Hours,msg2_meet,Msg3_Meet;
	String successSwimCompittionCheck1;
	String messageNormal;
	TextView txtSelectedStudent,txtMeetDate;
	String message;
	boolean registerAsVolunteer = false;
	Spinner spinner1_volunteers;
	ListView list;
	TextView txtJumpingWall,txtSwimNext;
	String successReminderText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_compitition5);

		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {


			}
		});

		isInternetPresent = Utility.isNetworkConnected(SwimCompitition5Acitivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{

			int count = 0 ;
			for(int i = 0 ; i < AppConfiguration.selectedStudents.size();i++)
			{
				if(AppConfiguration.selectedStudents.get(i) == true)
				{
					selectedListFromStep2.add(AppConfiguration.selectedChildDataMainList.get(i));
					count++;
				}
			}

			AppConfiguration.totalSelectedEvents = count;


			CustomList adapter = new CustomList(SwimCompitition5Acitivity.this,selectedListFromStep2);
			list.setAdapter(adapter);
		}

		txtSelectedStudent = (TextView)findViewById(R.id.txtSelectedStudent);
		txtSelectedStudent.setText("Participant:"+AppConfiguration.swimComptitionStudentName);

		txtMeetDate = (TextView)findViewById(R.id.txtMeetDate);
		txtMeetDate.setText(Html.fromHtml("<b>Meet Date: </b>"+AppConfiguration.SwimMeetText));

		txtJumpingWall = (TextView)findViewById(R.id.txtJumpingWall);
		txtSwimNext = (TextView)findViewById(R.id.txtSwimNext);

		txtJumpingWall.setText(AppConfiguration.step3_jumpingwall);
		txtSwimNext.setText(Html.fromHtml("<b>Would like to swim next to the wall in an end lane ?</b>\n"+AppConfiguration.step3_endlane));


		// fetching header view
		View headerLayout = findViewById(R.id.layout_top);
		ImageView imgBack = (ImageView) headerLayout.findViewById(R.id.ib_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		btnContinue = (Button)findViewById(R.id.btnContinue);
		btnContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new LoadRemiderTextAsyncTask().execute();




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

	public static class ViewHolder{
		TextView txtStrokeType,txtDistance,txtEvent,txtAgeGroup;
		//CheckBox chk;
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

					view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_swim_compitition5, null);

					holder.txtStrokeType = (TextView) view.findViewById(R.id.txtStrokeType);
					holder.txtDistance = (TextView)view.findViewById(R.id.txtDistance);
					holder.txtEvent = (TextView)view.findViewById(R.id.txtEvent);
					holder.txtAgeGroup = (TextView)view.findViewById(R.id.txtAgeGroup);
					//holder.chk = (CheckBox)view.findViewById(R.id.chk);

					view.setTag(holder);

				}
				else
				{
					holder = (ViewHolder) view.getTag();
				}



				holder.txtStrokeType.setText(Html.fromHtml("<b>StrokeType: </b>"+selectedListFromStep2.get(position).get("StrokeType")));
				holder.txtDistance.setText(Html.fromHtml("<b>Distance: </b>"+selectedListFromStep2.get(position).get("Distance")));
				holder.txtEvent.setText(Html.fromHtml("<b>Event#: </b>"+selectedListFromStep2.get(position).get("Event")));
				holder.txtAgeGroup.setText(Html.fromHtml("<b>AgeGroup: </b>"+selectedListFromStep2.get(position).get("AgeGroup")));


				//					holder.chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				//						@Override
				//						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//							if(isChecked){          
				//								AppConfiguration.selectedStudents.set(position, true);
				//			        			Log.e("checkedData",AppConfiguration.selectedStudents.toString());
				//							} else{          
				//								AppConfiguration.selectedStudents.set(position, false);
				//								Log.e("checkedData",AppConfiguration.selectedStudents.toString());
				//							}
				//						}
				//					});



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


	//================================ Remider Text

	public void loadingReminderText() {
		String[] value_split = AppConfiguration.SwimMeetID.split("\\|");

		int posLast = value_split.length -1;
		String swimMeetid = value_split[posLast];
		Log.e("SwimMeetIDforREminderText",""+swimMeetid);

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("MeetdatetimeValue", AppConfiguration.SwimMeetID);
		param.put("Swimmeetid",""+swimMeetid );

		String responseString = WebServicesCall
				.RunScript(AppConfiguration.swimCompititionConfirmationReminderText, param);
		readAndParseJSONChildList(responseString);
	}

	public void readAndParseJSONChildList(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successReminderText = reader.getString("Success");
			if (successReminderText.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck5_Reminderlist");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					String Line = jsonChildNode.getString("Line").trim();

					hashmap.put("Line", Line);

					reminderTextList.add(hashmap);


				}

			} else {
				JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck5_Reminderlist");

				for (int i = 0; i < jsonMainNode.length(); i++) {

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					message = jsonChildNode.getString("Msg").trim();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	class LoadRemiderTextAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(SwimCompitition5Acitivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			reminderTextList.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			loadingReminderText();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successReminderText.toString().equals("True")) {

				reminderText = reminderTextList.get(0).get("Line");


				AlertDialog.Builder builder = new AlertDialog.Builder(SwimCompitition5Acitivity.this);
				builder.setCancelable(false);
				builder.setTitle("Swim Meet Reminders");
				builder.setMessage(Html.fromHtml(""+reminderText));
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Close",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();

						Intent i  = new Intent(SwimCompitition5Acitivity.this,SwimCompititionConfirmationPageAcitivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);

					}
				});

				AlertDialog alert = builder.create();
				alert.show();


			} else {

				AlertDialog.Builder builder = new AlertDialog.Builder(SwimCompitition5Acitivity.this);
				builder.setCancelable(false);
				builder.setTitle("Swim Meet Reminders");
				builder.setMessage(Html.fromHtml("No Reminder Founds."));
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Close",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();

						Intent i  = new Intent(SwimCompitition5Acitivity.this,SwimCompititionConfirmationPageAcitivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);

					}
				});

				AlertDialog alert = builder.create();
				alert.show();

				//Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_LONG).show();
			}
		}
	}
}
