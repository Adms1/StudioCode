package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class SwimCampsRegister1Acitivity extends Activity {
	String TAG = "SwimCampsActivity1";
	ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> siteName = new ArrayList<String>();
	ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();

	Boolean isInternetPresent = false;
	String siteID;
    View include_layout_step_boxes;
	LinearLayout llListData;
	String successLoadChildList;
	String token, familyID;
	Button btn_sites;
	ListPopupWindow lpw_sitelist;
	TextView tv_lebel, txtHeader, txtPriceinfo;
    EditText tv_fsn_info;
	CardView _continue;
	private boolean[] thumbnailsselection;
	private int count;
    ProgressDialog pd;
	ScrollView ScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_camps_register1);

		AppConfiguration.swimCampsRegister1SiteID = "";

		// getting token
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        txtPriceinfo = (TextView) findViewById(R.id.txtPriceinfo);
        tv_lebel = (TextView) findViewById(R.id.tv_lebel);
        tv_fsn_info = (EditText) findViewById(R.id.tv_fsn_info);
        tv_fsn_info.setText(Utility.getProgramsInstructionText("4"));
		tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Swim Camps");
        btn_sites = (Button) findViewById(R.id.btn_sites);
        lpw_sitelist = new ListPopupWindow(getApplicationContext());
		llListData = (LinearLayout) findViewById(R.id.llListData);
		ScrollView =(ScrollView)findViewById(R.id.ScrollView);

		isInternetPresent = Utility
                .isNetworkConnected(SwimCampsRegister1Acitivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {

			new SitesListAsyncTask().execute();
			new LoadChildListAsyncTask().execute();
		}

        include_layout_step_boxes = (View) findViewById(R.id.include_layout_step_boxes);
        TextView txtBox1 = (TextView) include_layout_step_boxes.findViewById(R.id.txtBox1);
        txtBox1.setTextColor(Color.WHITE);
        txtBox1.setBackgroundColor(getResources().getColor(R.color.design_change_d2));

		btn_sites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {

				lpw_sitelist.show();
			}
		});

		tv_fsn_info.setVerticalScrollBarEnabled(true);
		tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
		ScrollView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});
		tv_fsn_info.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCheckin = new Intent(SwimCampsRegister1Acitivity.this, DashBoardActivity.class);
				intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCheckin);
				finish();
			}
		});
		_continue = (CardView) findViewById(R.id.btn_swim_camps_regi1_students);
		_continue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ArrayList<String> studentID = new ArrayList<String>();
				final ArrayList<String> studentNAME = new ArrayList<String>();
				studentID.clear();
				boolean noSelect = false;
				for (int i = 0; i < thumbnailsselection.length; i++) {
					if (thumbnailsselection[i] == true) {
						noSelect = true;
						Log.e("sel pos thu-->", "" + i);
						studentID.add(childList.get(i).get("StudentID"));
						studentNAME.add(childList.get(i).get("StudentName"));
					}
				}
//				30-05-2017 megha
//				if (!noSelect) {
//					Toast.makeText(SwimCampsRegister1Acitivity.this,
//							"Please select at lease one student.",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					AppConfiguration.swimCampsRegister1StudentID = studentID.toString().replaceAll("\\[", "").replaceAll("\\]", "");
//					AppConfiguration.swimCampsRegister1StudentName = studentNAME.toString().replaceAll("\\[", "").replaceAll("\\]", "");
//
//					if (AppConfiguration.swimCampsRegister1SiteID.equals("")) {
//						Toast.makeText(getApplicationContext(),
//								"Please select a site",
//								Toast.LENGTH_LONG).show();
//					} else {
//						Intent i = new Intent(SwimCampsRegister1Acitivity.this,
//								SwimCampsRegister2Activity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(i);
////						finish();
//					}
//				}
                AppConfiguration.swimCampsRegister1StudentID = studentID.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                if (AppConfiguration.swimCampsRegister1SiteID.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please select a site",
                            Toast.LENGTH_LONG).show();
                } else if (!noSelect) {
                    Toast.makeText(SwimCampsRegister1Acitivity.this,
                            "Please select at lease one student.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    AppConfiguration.swimCampsRegister1StudentName = studentNAME.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    Intent i = new Intent(SwimCampsRegister1Acitivity.this, SwimCampsRegister2Activity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
			}
		});
		// fetching header view
		Button relMenu = (Button) findViewById(R.id.relMenu);
		relMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onBackPressed();
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

	public void loadSitesList() {
		siteMainList.clear();

		HashMap<String, String > param = new HashMap<String, String>();

		String responseString = WebServicesCall
		.RunScript(AppConfiguration.getSiteWiseSwimLessonURL, param);
		readAndParseJSON(responseString);


	}

	public void readAndParseJSON(String in) {
		try {
			JSONObject reader = new JSONObject(in);
			JSONArray jsonMainNode = reader.optJSONArray("SitesFlgList");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

				HashMap<String, String> hashmap = new HashMap<String, String>();

				hashmap.put("SiteID", jsonChildNode.getString("SiteID"));
				hashmap.put("SiteName", jsonChildNode.getString("SiteName"));
				hashmap.put("FlgCnt", jsonChildNode.getString("FlgCnt"));

				siteName.add("" + jsonChildNode.getString("SiteName"));
				siteMainList.add(hashmap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(SwimCampsRegister1Acitivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();

			siteMainList.clear();
			siteName.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			loadSitesList();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			lpw_sitelist.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(), R.layout.edittextpopup, siteName));
			lpw_sitelist.setAnchorView(btn_sites);
			lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
			lpw_sitelist.setModal(true);
			lpw_sitelist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int pos, long id) {

					btn_sites.setText(siteMainList.get(pos).get("SiteName"));
					lpw_sitelist.dismiss();

					if (siteMainList.get(pos).get("FlgCnt").equals("0")) {
						String message = "We're sorry. There are no future dates set for this program at your selected location. Please select another location or contact the office staff for assistance.";

						AlertDialog.Builder builder = new AlertDialog.Builder(
								SwimCampsRegister1Acitivity.this);
						builder.setCancelable(false);
						builder.setTitle("Swim Camps");
						builder.setMessage(Html.fromHtml("" + message));
						builder.setInverseBackgroundForced(true);
						builder.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
														int which) {
										dialog.dismiss();

									}
								});

						AlertDialog alert = builder.create();
						alert.show();
					} else {
						siteID = siteMainList.get(pos).get("SiteID");
						String textPrice = Utility.getProgramsPricingText("4", siteID);
						if (textPrice.equalsIgnoreCase("")) {
							txtPriceinfo.setVisibility(View.GONE);
						} else {
							txtPriceinfo.setVisibility(View.VISIBLE);
							txtPriceinfo.setText(textPrice);
						}

						AppConfiguration.swimCampsRegister1SiteID = siteID;
					}

				}
			});

		}
	}

	// ================================== Load Child List
	// ==========================================

	public void loadingChildList() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("FamilyID",familyID );

		String responseString = WebServicesCall
		.RunScript(AppConfiguration.swimCampRegister1, param);
		readAndParseJSONChildList(responseString);

	}

	public void readAndParseJSONChildList(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successLoadChildList = reader.getString("Success");
			if (successLoadChildList.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					String StudentID = jsonChildNode.getString("StudentID").trim();
					String StudentName = jsonChildNode.getString("StudentName").trim();

					hashmap.put("StudentID", StudentID);
					hashmap.put("StudentName", StudentName);

					childList.add(hashmap);

				}

			} else {
				JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
				Msg = jsonMainNode.getJSONObject(0).getString("Msg");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String Msg = "";

	class LoadChildListAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			childList.clear();
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
				tv_lebel.setText("Please select the participant(s) you would like to register.");
				tv_lebel.setTextColor(Color.BLACK);
				_continue.setVisibility(View.VISIBLE);
				count = childList.size();
				thumbnailsselection = new boolean[count];
				loadDataList(childList);
			} else {
				tv_lebel.setText("You do not currently have a child that is eligible for this program.");
				tv_lebel.setTextColor(Color.RED);
				_continue.setVisibility(View.GONE);
			}
		}
	}
	public void loadDataList(ArrayList<HashMap<String, String>> list){
		TextView txtStudentName;
		CheckBox checkbox;
		int id;
		try{
			for(int i = 0;i < list.size();i++) {
				View convertView = LayoutInflater.from(SwimCampsRegister1Acitivity.this).inflate(R.layout.list_row_swim_camp_register1, null);
				txtStudentName = (TextView) convertView.findViewById(R.id.txtStudentName);
				checkbox = (CheckBox) convertView.findViewById(R.id.chb_students);

				checkbox.setId(i);
				txtStudentName.setId(i);
				checkbox.setOnClickListener(onClickListener);
				checkbox.setChecked(thumbnailsselection[i]);
				id = i;
				txtStudentName.setText(childList.get(i).get("StudentName"));
				llListData.addView(convertView);
			}
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
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CheckBox cb = (CheckBox) v;
			int id = cb.getId();
			if (thumbnailsselection[id]) {
				cb.setChecked(false);
				thumbnailsselection[id] = false;
				Log.e("checkedData", thumbnailsselection.toString());
			} else {
				cb.setChecked(true);
				thumbnailsselection[id] = true;
				Log.e("checkedData", thumbnailsselection.toString());
			}
		}
	};
	@Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
    @Override
	public void onBackPressed() {
//		super.onBackPressed();
		finish();
	}
}
