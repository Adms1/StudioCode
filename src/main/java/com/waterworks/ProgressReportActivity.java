package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ProgressReportActivity extends Activity {
	
	ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
	
	ListView list;
	TextView txtNoRecord,txtLabel;
	String successViewCertificate;
	RelativeLayout rel_list;
	String TAG = "ViewCerfiticate";
	String filename;
	Context mContext=this;
	String token,familyID;
	Boolean isInternetPresent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_report_activity);
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

        txtLabel = (TextView)findViewById(R.id.txtLabel);

        View view = findViewById(R.id.actionbar);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Progress Report");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCheckin = new Intent(ProgressReportActivity.this, DashBoardActivity.class);
				intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCheckin);
				finish();
			}
		});

        rel_list = (RelativeLayout) findViewById(R.id.rel_list);
		
		txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
			   String studentID = childList.get(position).get("Studentid");
			   String firstname = childList.get(position).get("SFirstName");
			   String lastname = childList.get(position).get("SLastName");
			   
			   Intent i = new Intent(ProgressReportActivity.this,ProgressReportDetailsActivity.class);
			   i.putExtra("studentID", studentID);
			   i.putExtra("Firstname", firstname);
			   i.putExtra("Lastname", lastname);
			   
			   startActivity(i);
			}
		});

		isInternetPresent = Utility.isNetworkConnected(ProgressReportActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new ProgressReportAsyncTask().execute();
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
	@Override
	protected void onResume() {
		super.onResume();
		this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	}

	public void getProgressReportList() {

		HashMap<String, String > params = new HashMap<String, String>();
		params.put("Token",token );
		
		String responseString = WebServicesCall.RunScript(AppConfiguration.progressReportURL, params);
		readAndParseJSON(responseString);
	}

	public void readAndParseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successViewCertificate = reader.getString("Success");
			if (successViewCertificate.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("ProgresRptList");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					
					String Studentid = jsonChildNode.getString("Studentid").trim();
					String SFirstName = jsonChildNode.getString("SFirstName").trim();
					String SLastName = jsonChildNode.getString("SLastName").trim();
									
					hashmap.put("Studentid", Studentid);
					hashmap.put("SFirstName", SFirstName);
					hashmap.put("SLastName", SLastName);

					childList.add(hashmap);

				}

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ProgressReportAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(ProgressReportActivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			childList.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			getProgressReportList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (childList.size() > 0) {
				txtNoRecord.setVisibility(View.GONE);
				rel_list.setVisibility(View.VISIBLE);
			} else {
				txtNoRecord.setVisibility(View.VISIBLE);
				rel_list.setVisibility(View.GONE);
			}

			if (successViewCertificate.toString().equals("True")) {

				CustomList adapter = new CustomList(ProgressReportActivity.this,childList);
				list.setAdapter(adapter);
				

			} else {
				//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
			}
		}
	}

	public class CustomList extends ArrayAdapter<String> {
		private final Activity context;
		private final ArrayList<HashMap<String, String>> data;

		public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
			super(context, R.layout.list_row_progress_report);
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
			View rowView = inflater.inflate(R.layout.list_row_progress_report, null, true);
			
			TextView txtChildName = (TextView) rowView.findViewById(R.id.txtChildName);
			txtChildName.setText(childList.get(position).get("SFirstName") +" "+ childList.get(position).get("SLastName"));
			
//			TextView txtChildLastName = (TextView) rowView.findViewById(R.id.txtChildLastName);
//			txtChildLastName.setText(childList.get(position).get("SLastName"));
			
			return rowView;
		}
	}

	public void SelectInstructorDialog() {

		LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = lInflater.inflate(R.layout.level_req_popup, null);
		final PopupWindow popWindow = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
		TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
		tv_appname.setText("Warning");

		TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
		tv_description.setText(Html.fromHtml("<div id=\"sitenewsletter\" style=\"display: none; text-align: center;\" class=\"General\">\n" +
				"        <p style=\"padding: 0px 22px;\">\n" +
				"            NOTE: There are additional skill requirements for each level, however those listed\n" +
				"            below are the most emphasized requirements to achieve level advancement.</p>\n" +
				"        <ul>\n" +
				"            <li>Level 2<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Back-float with an instructor’s assistance for 10 seconds\n" +
				"                    without hesitation.</li><li style=\"list-style: none;\">2. Swim from the wall unassisted\n" +
				"                        for 5 feet with their face in the water using flutter kicking. </li>\n" +
				"            </ul>\n" +
				"            </li>\n" +
				"            <li>Level 3<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Back-float unassisted for 10 seconds.</li><li style=\"list-style: none;\">\n" +
				"                    2. Swim Freestyle with 2 unassisted 5 second long roll-over breaths.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 4<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Swim unassisted half-way (12.5 yards) across the pool\n" +
				"                    with roll-over breathing or side breaths.</li><li style=\"list-style: none;\">2. Jump\n" +
				"                        into the pool and swim back to the wall unassisted.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 5<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Swim Freestyle unassisted across the pool with roll-over\n" +
				"                    breathing or side breaths.</li><li style=\"list-style: none;\">2. Introduced to swimming\n" +
				"                        Freestyle with a ‘high elbow’ arm recovery.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 6<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Swim Freestyle across the pool taking roll-over breaths\n" +
				"                    of less than 5 seconds.</li><li style=\"list-style: none;\">2. Instructor must be at least\n" +
				"                        10 feet from the student while they swim across the pool.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 7<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Freestyle across the pool with the Instructor behind\n" +
				"                    the student.</li><li style=\"list-style: none;\">2. Unassisted Backstroke across the pool\n" +
				"                        (25 yards).</li><li style=\"list-style: none;\">3. Student is introduced to track-start\n" +
				"                            diving.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 8<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Introduced to the Breaststroke “frog kick”.</li><li\n" +
				"                    style=\"list-style: none;\">2. Breaststroke half-way across the pool. Glide following\n" +
				"                    kick and competition “legal” form not required.</li><li style=\"list-style: none;\">3.\n" +
				"                        Side breath for Freestyle required for swimmers over the age of 6.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 9<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Student has mastered a competition legal Breaststroke\n" +
				"                    kick.</li><li style=\"list-style: none;\">2. Breaststroke across the pool. Glide following\n" +
				"                        the kick not required.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 10<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Butterfly kick with knees and ankles together.</li><li\n" +
				"                    style=\"list-style: none;\">2. Butterfly across the pool (25 yards).</li><li style=\"list-style: none;\">\n" +
				"                        3. Head first dive from the side of pool.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 11<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Track start dive from the racing block.</li><li\n" +
				"                    style=\"list-style: none;\">2. Swim 50 continuous yards of each of the 4 competitive\n" +
				"                    strokes.</li><li style=\"list-style: none;\">3. Open turns for Breaststroke and Butterfly.</li></ul>\n" +
				"            </li>\n" +
				"            <li>Level 12<ul style=\"padding:1px 15px;\">\n" +
				"                <li style=\"list-style: none;\">1. Freestyle & Backstroke flip-turns.</li><li style=\"list-style: none;\">\n" +
				"                    2. Sprinting and speed work on all 4 competitive strokes.</li><li style=\"list-style: none;\">\n" +
				"                        3. Technical improvements to form and efficiency on all 4 competitive strokes.</li></ul>\n" +
				"            </li>\n" +
				"        </ul>\n" +
				"        <br />\n" +
				"        <div style=\"text-align: center;\">\n" +
				"            <input type=\"button\" value=\"Ok\" name=\"B1\" onclick=\"divbox.hide()\" class=\"newbutton\" /></div>\n" +
				"    </div>"));
		tv_description.setTextColor(Color.BLACK);

		tv_description.setMovementMethod(new ScrollingMovementMethod());

		Typeface face = Typeface.createFromAsset(mContext.getAssets(),
				"Roboto_Light.ttf");
		Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
				"RobotoRegular.ttf");
		tv_description.setTypeface(face);
		tv_appname.setTypeface(regular);

		TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
		imgv_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popWindow.dismiss();
			}
		});
		Button button_ok = (Button) layout.findViewById(R.id.button_ok);
		button_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}
		});
	}
		
}
