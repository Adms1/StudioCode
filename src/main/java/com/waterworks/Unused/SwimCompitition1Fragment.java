package com.waterworks.Unused;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.adapter.SwimCompitition1Adapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwimCompitition1Fragment extends Fragment {
	String TAG = "SwimCampsActivity1";
	ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> siteName = new ArrayList<String>();

	ArrayList<HashMap<String, String>> meetDatesMainList = new ArrayList<HashMap<String, String>>();
	ArrayList<String> Address1 = new ArrayList<String>();
	ArrayList<String> SiteName1 = new ArrayList<String>();
	ArrayList<String> State = new ArrayList<String>();
	ArrayList<String> ZipCode = new ArrayList<String>();
	ArrayList<String> MeetDate_Display = new ArrayList<String>();

	ArrayList<String> meetDatesNames = new ArrayList<String>();
	ArrayList<String> DateValue = new ArrayList<String>();
	ArrayList<String> time = new ArrayList<String>();

	ArrayList<String> meetStartTime = new ArrayList<String>();
	String site="";

	ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
	Boolean isInternetPresent = false;
	// Spinner spinner1;
	// Spinner spinner2_MeetDates;
	String siteID = null;
	ListView list;
	String successLoadChildList;
	String token, familyID;
	String messageMeetDate;
	String successMeetDate;
	String messageMeetDateLocation;
	String successMeetLocation;

	boolean isSelectedAgreement = false;
	String msg1_Hours, msg2_meet, Msg3_Meet;
	String successSwimCompittionCheck1;
	String messageNormal;
	//CheckBox chkAgree;
	TextView tv_sc_select_location;
	Button btn_sites;
	ListPopupWindow lpw_sitelist, lpw_meetDates;
	private boolean[] thumbnailsselection;
	private int count;
	//Button _continue;

	TextView time_value,txt_fsn_info1;
	SwimCompitition1Adapter adp;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_swim_compitition_register, container, false);
		getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			siteID = "";
			AppConfiguration.SwimMeetID = "";
			AppConfiguration.SwimMeetText = "";
			AppConfiguration.SwimName= "";
			childList.clear();

			// getting token
			SharedPreferences prefs = AppConfiguration
					.getSharedPrefs(getContext());
			token = prefs.getString("Token", "");
			familyID = prefs.getString("FamilyID", "");
			//DateValue= prefs.getString("DateValue", "");

			Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

			//btn_MeetDate = (Button) findViewById(R.id.btn_MeetDate);
			lpw_meetDates = new ListPopupWindow(getContext());
			//time_value = (TextView)findViewById(R.id.time_value);
			txt_fsn_info1= (TextView)rootView.findViewById(R.id.tv_fsn_info1);
			tv_sc_select_location= (TextView)rootView.findViewById(R.id.tv_sc_select_location);
			//txt_fsn_info1.setText(Html.fromHtml("<b>" + "Members:"+"          " + "</b>"+"                            "+"$5Entry Fee + $3 per  event"+"<br>"+"<b>"+"Non-Members:"+"</b>"+"</br>"+"\u0020 \u0020 $10 Entry Fee + $3 per event"));
			btn_sites = (Button) rootView.findViewById(R.id.btn_sites);
			lpw_sitelist = new ListPopupWindow(getContext());

			isInternetPresent = Utility
					.isNetworkConnected(getContext());
			if (!isInternetPresent) {
				onDetectNetworkState().show();
			} else {
				new SitesListAsyncTask().execute();
				new LoadChildListAsyncTask().execute();
			}

			btn_sites.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View paramView) {

					lpw_sitelist.show();
					//btn_MeetDate.setText("Choose a Meet Date");
					AppConfiguration.SwimMeetID = "";
					AppConfiguration.SwimMeetText = "";
				}
			});
			list = (ListView) rootView.findViewById(R.id.lv_cr_list);
		/*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


			}
		});*/
		/*btn_MeetDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {

				if (siteID.isEmpty() || siteID.equals("")) {
					Toast.makeText(SwimCompititionRegisterAcitivity.this,
							"Please select location.", Toast.LENGTH_LONG)
							.show();
				} else {
					if (meetDatesMainList.size() < 1) {
						Toast.makeText(SwimCompititionRegisterAcitivity.this,
								"Please select another location.",
								Toast.LENGTH_LONG).show();
					} else {
						lpw_meetDates.show();
					}
				}
			}
		});*/


			//	_continue = (Button) findViewById(R.id.btn_swim_camps_regi1_students);
		/*_continue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ArrayList<String> studentID = new ArrayList<String>();
				final ArrayList<String> studentNAME = new ArrayList<String>();
				studentID.clear();
				studentNAME.clear();

				boolean noSelect = false;
				for (int i = 0; i < thumbnailsselection.length; i++) {
					if (thumbnailsselection[i] == true) {
						noSelect = true;
						Log.e("sel pos thu-->", "" + i);
						studentID.add(childList.get(i).get("StudentID").toString().trim());
						studentNAME.add(childList.get(i).get("StudentName").toString().trim());
					}
				}
				AppConfiguration.totalSelectedStudent = studentNAME.size();
				if (!noSelect) {
					Toast.makeText(SwimCompititionRegisterAcitivity.this,
							"Please select at lease one student.",
							Toast.LENGTH_SHORT).show();
				} else {
					AppConfiguration.swimComptitionStudentID = studentID
							.toString().replaceAll("\\[", "")
							.replaceAll("\\]", "");
					AppConfiguration.swimComptitionStudentName = studentNAME
							.toString().replaceAll("\\[", "")
							.replaceAll("\\]", "");

				*//*	if (chkAgree.isChecked())
						isSelectedAgreement = true;
					else
						isSelectedAgreement = false;*//*

					if (AppConfiguration.SwimMeetID.equals("")) {
						Toast.makeText(getApplicationContext(),
								"Please select swim meet date",
								Toast.LENGTH_LONG).show();
					}
					else {
						new SwimMeetDateCheckAsyncTask().execute();
					}
				}
			}
		});*/
			// fetching header view



			//chkAgree = (CheckBox) findViewById(R.id.chkAgree);

		return rootView;
	}

	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_compitition_register);

		siteID = "";
		AppConfiguration.SwimMeetID = "";
		AppConfiguration.SwimMeetText = "";
		AppConfiguration.SwimName= "";
		childList.clear();
		
		// getting token
		SharedPreferences prefs = AppConfiguration
				.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		//DateValue= prefs.getString("DateValue", "");

		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

		//btn_MeetDate = (Button) findViewById(R.id.btn_MeetDate);
		lpw_meetDates = new ListPopupWindow(getApplicationContext());
		//time_value = (TextView)findViewById(R.id.time_value);
		txt_fsn_info1= (TextView)findViewById(R.id.tv_fsn_info1);
		tv_sc_select_location= (TextView)findViewById(R.id.tv_sc_select_location);
		//txt_fsn_info1.setText(Html.fromHtml("<b>" + "Members:"+"          " + "</b>"+"                            "+"$5Entry Fee + $3 per  event"+"<br>"+"<b>"+"Non-Members:"+"</b>"+"</br>"+"\u0020 \u0020 $10 Entry Fee + $3 per event"));
		btn_sites = (Button) findViewById(R.id.btn_sites);
		lpw_sitelist = new ListPopupWindow(getApplicationContext());

		isInternetPresent = Utility
				.isNetworkConnected(SwimCompitition1Fragment.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new SitesListAsyncTask().execute();
			new LoadChildListAsyncTask().execute();
		}

		btn_sites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {

				lpw_sitelist.show();
				//btn_MeetDate.setText("Choose a Meet Date");
				AppConfiguration.SwimMeetID = "";
				AppConfiguration.SwimMeetText = "";
			}
		});
		list = (ListView) findViewById(R.id.lv_cr_list);
		*//*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


			}
		});*//*
		*//*btn_MeetDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {

				if (siteID.isEmpty() || siteID.equals("")) {
					Toast.makeText(SwimCompititionRegisterAcitivity.this,
							"Please select location.", Toast.LENGTH_LONG)
							.show();
				} else {
					if (meetDatesMainList.size() < 1) {
						Toast.makeText(SwimCompititionRegisterAcitivity.this,
								"Please select another location.",
								Toast.LENGTH_LONG).show();
					} else {
						lpw_meetDates.show();
					}
				}
			}
		});*//*


	//	_continue = (Button) findViewById(R.id.btn_swim_camps_regi1_students);
		*//*_continue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ArrayList<String> studentID = new ArrayList<String>();
				final ArrayList<String> studentNAME = new ArrayList<String>();
				studentID.clear();
				studentNAME.clear();
				
				boolean noSelect = false;
				for (int i = 0; i < thumbnailsselection.length; i++) {
					if (thumbnailsselection[i] == true) {
						noSelect = true;
						Log.e("sel pos thu-->", "" + i);
						studentID.add(childList.get(i).get("StudentID").toString().trim());
						studentNAME.add(childList.get(i).get("StudentName").toString().trim());
					}
				}
				AppConfiguration.totalSelectedStudent = studentNAME.size();
				if (!noSelect) {
					Toast.makeText(SwimCompititionRegisterAcitivity.this,
							"Please select at lease one student.",
							Toast.LENGTH_SHORT).show();
				} else {
					AppConfiguration.swimComptitionStudentID = studentID
							.toString().replaceAll("\\[", "")
							.replaceAll("\\]", "");
					AppConfiguration.swimComptitionStudentName = studentNAME
							.toString().replaceAll("\\[", "")
							.replaceAll("\\]", "");

				*//**//*	if (chkAgree.isChecked())
						isSelectedAgreement = true;
					else
						isSelectedAgreement = false;*//**//*

					if (AppConfiguration.SwimMeetID.equals("")) {
						Toast.makeText(getApplicationContext(),
								"Please select swim meet date",
								Toast.LENGTH_LONG).show();
					}
					else {
						new SwimMeetDateCheckAsyncTask().execute();
					}
				}
			}
		});*//*
		// fetching header view

        View view = findViewById(R.id.layout_top);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Swim Competitions");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

		//chkAgree = (CheckBox) findViewById(R.id.chkAgree);

	}*/
	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				getActivity().finish();
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
	/*@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(getContext());
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				view.setSelected(true);
				//SelectedVal = parent.getItemAtPosition(position).toString();
			}
		});

	}*/
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.swim_camps, menu);
		return true;
	}*/

	public void loadSitesList() {
		siteMainList.clear();
		HashMap<String, String > param = new HashMap<String, String>();

		String responseString = WebServicesCall
				.RunScript(AppConfiguration.getSiteListURL, param);
		readAndParseJSON(responseString);

	}

	public void readAndParseJSON(String in) {
		try {
			JSONObject reader = new JSONObject(in);
			JSONArray jsonMainNode = reader.optJSONArray("Sites");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

				HashMap<String, String> hashmap = new HashMap<String, String>();

				hashmap.put("SiteID", jsonChildNode.getString("SiteID"));
				hashmap.put("SiteName", jsonChildNode.getString("SiteName"));

				siteName.add("" + jsonChildNode.getString("SiteName"));
				siteMainList.add(hashmap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
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
			if (pd != null) {
				pd.dismiss();
			}

			lpw_sitelist.setAdapter(new ArrayAdapter<String>(
					getContext(), R.layout.edittextpopup, siteName));
			lpw_sitelist.setAnchorView(btn_sites);
			lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
			lpw_sitelist.setModal(true);
			lpw_sitelist.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int pos, long id) {

					btn_sites.setText(siteMainList.get(pos).get("SiteName"));
					lpw_sitelist.dismiss();

					siteID = siteMainList.get(pos).get("SiteID");
					Log.e("SiteID", siteID);
					// AppConfiguration.swimCampsRegister1SiteID = siteID;

					new MeetDatesAsyncTask().execute();
					new MeetDatesLocationAsyncTask().execute();




				}
			});

			// ArrayAdapter<String> dataAdapter1 = new
			// ArrayAdapter<String>(SwimCompititionRegisterAcitivity.this,android.R.layout.simple_spinner_item,
			// siteName);
			// dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// spinner1.setAdapter(dataAdapter1);
			// // Spinner1 on item click listener
			// spinner1.setOnItemSelectedListener(new
			// AdapterView.OnItemSelectedListener() {
			//
			// @Override
			// public void onItemSelected(AdapterView<?> arg0, View arg1,
			// int position, long arg3) {
			//
			// siteID = siteMainList.get(position).get("SiteID");
			// //AppConfiguration.swimCampsRegister1SiteID = siteID;
			//
			//
			// new MeetDatesAsyncTask().execute();
			// }
			//
			// @Override
			// public void onNothingSelected(AdapterView<?> arg0) {
			// }
			// });

		}
	}

	// ================================= Meet Dates
	// =======================================

	public void loadMeetDatesList() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("SiteID", siteID);

		String responseString = WebServicesCall
				.RunScript(
						AppConfiguration.swimCompititionMeetDatesURL, param);
		readAndParseJSONMeetDates(responseString);


	}

	public void loadMeetDatesLocationList() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("SiteID", siteID);
		Log.e("loadMeetDatesLocationList SiteID", siteID);

		String responseString = WebServicesCall
				.RunScript(
						AppConfiguration.swimCompititionMeetDatesLocationURL, param);
		readAndParseJSONMeetDatesLocation(responseString);
		/*Toast.makeText(getApplicationContext(), "innerlocation true"+siteID ,
				Toast.LENGTH_LONG).show();*/
	}

	public void readAndParseJSONMeetDates(String in) {
		try {
			JSONObject reader = new JSONObject(in);

			successMeetDate = reader.getString("Success");
			if (successMeetDate.toString().equals("True")) {
				JSONArray jsonMainNode = reader
						.optJSONArray("SwimMeetDateBySite");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					HashMap<String, String> hashmap = new HashMap<String, String>();

					hashmap.put("DateValue",
							jsonChildNode.getString("DateValue"));
					hashmap.put("DateText", jsonChildNode.getString("DateText"));

					meetDatesNames
							.add("" + jsonChildNode.getString("MeetDate"));
					meetStartTime
							.add("" + jsonChildNode.getString("StartTime"));
					DateValue.add("" + jsonChildNode.getString("DateValue"));
					MeetDate_Display.add("" + jsonChildNode.getString("MeetDate_Display"));
					time.add("" + jsonChildNode.getString("DateText"));
					Log.e("loadMeetDatesLocationList DateValue", DateValue.toString());
					meetDatesMainList.add(hashmap);



				}
			} else {
				JSONArray jsonMainNode = reader
						.optJSONArray("SwimMeetDateBySite");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					messageMeetDate = jsonChildNode.getString("Msg");

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void readAndParseJSONMeetDatesLocation(String in) {
		try {
			JSONObject reader = new JSONObject(in);

			successMeetLocation = reader.getString("Success");
			if (successMeetLocation.toString().equals("True")) {
				JSONArray jsonMainNode = reader
						.optJSONArray("Sites");

				for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					/*HashMap<String, String> hashmap = new HashMap<String, String>();

					hashmap.put("DateValue",
							jsonChildNode.getString("DateValue"));
					hashmap.put("DateText", jsonChildNode.getString("DateText"));*/

					Address1
							.add(" "+ jsonChildNode.getString("Address1"));
					SiteName1
							.add(" "+jsonChildNode.getString("SiteName"));
					State.add(" "+jsonChildNode.getString("State"));
					ZipCode.add(" " + jsonChildNode.getString("ZipCode"));

					Log.e("Address1.toString()", Address1.toString());
					Log.e("SiteName1.toString() ", SiteName1.toString());
					Log.e(" State.toString()", State.toString());
					Log.e("ZipCode.toString()", ZipCode.toString());

					//tv_sc_select_location.setText(Address1.toString() + SiteName1.toString() + State.toString()+ZipCode.toString());
					//meetDatesMainList.add(hashmap);



				}
			} else {
				JSONArray jsonMainNode = reader
						.optJSONArray("Sites");

				/*for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					messageMeetDateLocation = jsonChildNode.getString("Msg");

				}*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	class MeetDatesLocationAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();

			meetDatesMainList.clear();
			meetDatesNames.clear();
			meetStartTime.clear();
			Address1.clear();
			State.clear();
			ZipCode.clear();
			DateValue.clear();
			time.clear();
			SiteName1.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			loadMeetDatesLocationList();


			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successMeetDate.equalsIgnoreCase("True")) {
				/*Toast.makeText(getApplicationContext(), "innerlocation true" ,
						Toast.LENGTH_LONG).show();*/
				String fstr=Address1.toString() +"-"+ SiteName1.toString() +","+"  "+ State.toString() + ZipCode.toString();
				String str = fstr.replaceAll("\\[", "").replaceAll("\\]","");

				String str1 = SiteName1.toString().replaceAll("\\[", "").replaceAll("\\]","");
				site=str1;
				tv_sc_select_location.setText(str);

			/*	Toast.makeText(getApplicationContext(), "site" + site,
						Toast.LENGTH_LONG).show();
*/
				//adp=new SwimCompitition1Adapter(SwimCompititionRegisterAcitivity.this,meetDatesNames,meetStartTime);
				//ist.setVisibility(View.VISIBLE);
				//list.setAdapter(adp);
				//lpw_meetDates.setAdapter(new ArrayAdapter<String>(
				//		getApplicationContext(), R.layout.edittextpopup,
				//		meetDatesNames));
				//lpw_meetDates.setAnchorView(btn_MeetDate);
			//	lpw_meetDates.setHeight(LayoutParams.WRAP_CONTENT);
				//lpw_meetDates.setModal(true);
				/*lpw_meetDates.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {

					*//*	btn_MeetDate.setText(meetDatesMainList.get(position)
								.get("DateText"));*//*
						lpw_meetDates.dismiss();

						AppConfiguration.SwimMeetID = meetDatesMainList.get(
								position).get("DateValue");
						AppConfiguration.SwimMeetText = meetDatesMainList.get(
								position).get("DateText");

						String pattern1 = " ";
						String pattern2 = "|";
						String text = AppConfiguration.SwimMeetID;
						String time = null;

						Pattern p = Pattern.compile(Pattern.quote(pattern1)
								+ "(.*?)" + Pattern.quote(pattern2));
						Matcher m = p.matcher(text);
						while (m.find()) {
							System.out.println(m.group(1));
							time = m.group(1);
						}
						if(time.trim().length()>0){
							time_value.setText(time);
						}else{
							time_value.setVisibility(View.GONE);
						}

						String message = "*check-in online or by phone by 1 hour before meet time";

						AlertDialog.Builder builder1 = new AlertDialog.Builder(
								SwimCompititionRegisterAcitivity.this);
						builder1.setTitle("Swim Competition");
						builder1.setMessage(message);
						builder1.setCancelable(false);
						builder1.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										dialog.cancel();
									}
								});

						AlertDialog alert11 = builder1.create();
						alert11.show();

					}
				});*/


			} else {
				site="";
			//	list.setVisibility(View.GONE);
				tv_sc_select_location.setText("");
				Toast.makeText(getContext(), "" + messageMeetDateLocation,
						Toast.LENGTH_LONG).show();
				// spinner2_MeetDates.setAdapter(null);
			//	AppConfiguration.SwimMeetID = "";
			}

		}
	}

	class MeetDatesAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
Address1.clear();
			SiteName1.clear();
			State.clear();
			ZipCode.clear();
			meetDatesMainList.clear();
			meetDatesNames.clear();
			meetStartTime.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			loadMeetDatesList();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successMeetDate.equalsIgnoreCase("True")) {

				adp=new SwimCompitition1Adapter(getActivity(),meetDatesNames,meetStartTime,DateValue,time,MeetDate_Display);
				list.setVisibility(View.VISIBLE);
				list.setAdapter(adp);
				/*Toast.makeText(getApplicationContext(), "meetStartTime in adapter" + meetDatesMainList,
						Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), "DateValue in adapter" + DateValue,
						Toast.LENGTH_LONG).show();*/
				lpw_meetDates.setAdapter(new ArrayAdapter<String>(
						getContext(), R.layout.edittextpopup,
						meetDatesNames));

				//lpw_meetDates.setAnchorView(btn_MeetDate);
				lpw_meetDates.setHeight(LayoutParams.WRAP_CONTENT);
				lpw_meetDates.setModal(true);
				lpw_meetDates.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					/*	btn_MeetDate.setText(meetDatesMainList.get(position)
								.get("DateText"));*/
						lpw_meetDates.dismiss();

						AppConfiguration.SwimMeetID = meetDatesMainList.get(
								position).get("DateValue");
						AppConfiguration.SwimMeetText = meetDatesMainList.get(
								position).get("DateText");

						String pattern1 = " ";
						String pattern2 = "|";
						String text = AppConfiguration.SwimMeetID;
						String time = null;

						Pattern p = Pattern.compile(Pattern.quote(pattern1)
								+ "(.*?)" + Pattern.quote(pattern2));
						Matcher m = p.matcher(text);
						while (m.find()) {
							System.out.println(m.group(1));
							time = m.group(1);
						}
						if(time.trim().length()>0){
							time_value.setText(time);
						}else{
							time_value.setVisibility(View.GONE);
						}
						
						String message = "*check-in online or by phone by 1 hour before meet time";

						AlertDialog.Builder builder1 = new AlertDialog.Builder(
								getActivity());
						builder1.setTitle("Swim Competition");
						builder1.setMessage(message);
						builder1.setCancelable(false);
						builder1.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});

						AlertDialog alert11 = builder1.create();
						alert11.show();

					}
				});


			} else {
				list.setVisibility(View.GONE);
				Toast.makeText(getContext(), "" + messageMeetDate,
						Toast.LENGTH_LONG).show();
				// spinner2_MeetDates.setAdapter(null);
				AppConfiguration.SwimMeetID = "";
			}

		}
	}

	// ===================================== Swim Meet Date
	// Check==================================

	public void swimMeetDateCheck() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("MeetdatetimeValue",
				AppConfiguration.SwimMeetID);

		String responseString = WebServicesCall
				.RunScript(
						AppConfiguration.swimCompititionMeetDateCheckURL, param);

		readAndParseJSONSwimMeetDateCheck(responseString);


	}

	public void readAndParseJSONSwimMeetDateCheck(String in) {
		try {
			JSONObject reader = new JSONObject(in);
			successSwimCompittionCheck1 = reader.getString("Success");
			if (successSwimCompittionCheck1.toString().equals("True")) {
				JSONArray jsonMainNode = reader
						.optJSONArray("SwimMeetDateStep1check");
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					msg1_Hours = jsonChildNode.getString("Msg1_Hours");
					msg2_meet = jsonChildNode.getString("Msg2_Meet");
					Msg3_Meet = jsonChildNode.getString("Msg3_Meet");
				}
			} else {
				JSONArray jsonMainNode = reader
						.optJSONArray("SwimMeetDateStep1check");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					messageNormal = jsonChildNode.getString("Msg");

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class SwimMeetDateCheckAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			swimMeetDateCheck();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successSwimCompittionCheck1.equals("True")) {
				if (msg1_Hours.equals("null") && msg2_meet.equals("null")
						&& Msg3_Meet.equals("null")) {
					Intent i = new Intent(getActivity(),
							SwimCompitition2Acitivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				} else {
					if (!msg1_Hours.equals("null")) {
						Toast.makeText(getContext(),
								"" + msg1_Hours, Toast.LENGTH_LONG).show();
					} else if (!msg2_meet.equals("null")) {
						Toast.makeText(getContext(), "" + msg2_meet,
								Toast.LENGTH_LONG).show();
					} else if ((!Msg3_Meet.equals("null"))) {
						Toast.makeText(getContext(), "" + Msg3_Meet,
								Toast.LENGTH_LONG).show();
					}

				}
			} else {
				Toast.makeText(getContext(), "" + messageNormal,
						Toast.LENGTH_LONG).show();
			}

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

					String StudentID = jsonChildNode.getString("StudentID")
							.trim();
					String StudentName = jsonChildNode.getString("StudentName")
							.trim();

					hashmap.put("StudentID", StudentID);
					hashmap.put("StudentName", StudentName);

					childList.add(hashmap);

				}

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class LoadChildListAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

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

				CustomList adapter = new CustomList(
						getActivity(), childList);
				//list.setAdapter(adapter);
				count = childList.size();
				thumbnailsselection = new boolean[count];

			} else {
				// Toast.makeText(getApplicationContext(), "",
				// Toast.LENGTH_LONG).show();
			}
		}
	}

	public class CustomList extends ArrayAdapter<String> {
		private final Activity context;
		private final ArrayList<HashMap<String, String>> data;
		ViewHolder holder;

		public CustomList(Activity context,
				ArrayList<HashMap<String, String>> list) {
			super(context, R.layout.list_row_swim_compitition1);
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

		public class ViewHolder {
			TextView txtStudentName;
			CheckBox checkbox;
			int id;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			ViewHolder holder;
			try {
				if (view == null) {
					holder = new ViewHolder();

					LayoutInflater inflater = context.getLayoutInflater();
					view = inflater.inflate(
							R.layout.list_row_swim_compitition1, null, true);

					holder.txtStudentName = (TextView) view
							.findViewById(R.id.txtStudentName);
					holder.checkbox = (CheckBox) view
							.findViewById(R.id.chb_students);
					view.setTag(holder);
				} else {
					holder = (ViewHolder) view.getTag();
				}
				holder.checkbox.setId(position);
				holder.txtStudentName.setId(position);
				holder.checkbox.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CheckBox cb = (CheckBox) v;
						int id = cb.getId();
						if (thumbnailsselection[id]) {
							cb.setChecked(false);
							thumbnailsselection[id] = false;
						} else {
							cb.setChecked(true);
							thumbnailsselection[id] = true;
						}
					}
				});
				holder.checkbox.setChecked(thumbnailsselection[position]);
				holder.id = position;
				holder.txtStudentName.setText(childList.get(position).get(
						"StudentName"));

			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}
	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.
		getActivity().finish();
		//  ClearArray();
	}*/

}
