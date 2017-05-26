package com.waterworks.Unused;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.ConstantData;
import com.waterworks.DashBoardActivity;
import com.waterworks.FeedbackActivity;
import com.waterworks.HelpCenterActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class SwimcompititionStep3RegiFragment extends Fragment implements OnClickListener{

	public SwimcompititionStep3RegiFragment() {}
	private static String TAG = "ListFragment Register";
	View rootView;
	Boolean isInternetPresent = false;
	String message;
	Button btnFeedback,btnHelpCenter;
	ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	String successContact;
	RelativeLayout title_black;
	ListView list;
	Context mContext=getActivity();

	String token,familyID;
	String DateValue,eventdates,MeetDate_Display,time;
	public static HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
	public static ArrayList<String> t_sname = new ArrayList<String>();
	public static ArrayList<String> t_sid = new ArrayList<String>();
	ArrayList<ArrayList<String>> Event = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> AgeGroup = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> Distance = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> StrokeType = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> StudentID = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> tdid = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> ControlType = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> Text = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> Value = new ArrayList<ArrayList<String>>();
	ArrayList<String> temp_value = new ArrayList<String>();
	ArrayList<String> _StudentID = new ArrayList<String>();
	ArrayList<String> _StudentName = new ArrayList<String>();
	String[] tempid, tempname;
	public static List<SwimCompRegi2Item1> itemData1;
	String successLoadChildList;
	String firstname,secondname,thirdname,firstid,thirdid,secondid,Finalid;
	public static String _nameid = "";

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		ConstantData.destroyed=true;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_swim_register_step3_list, container, false);
		
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity().getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
		Intent intent = getActivity().getIntent();
		if (null != intent) {
			eventdates = intent.getStringExtra("eventdates");
			DateValue= intent.getStringExtra("datevalue");
			Toast.makeText(getActivity(), "DateValue" + DateValue,
					Toast.LENGTH_LONG).show();
			time = intent.getStringExtra("time");
			MeetDate_Display = intent.getStringExtra("MeetDate_Display");

			// sitename = intent.getStringExtra("sitename");

			Log.d(TAG, "eventdates=" + eventdates);
			Log.d(TAG, "DateValue=" + DateValue);
		//	txtCompititionVal.setText(MeetDate_Display );

		}
		initialization();
		_StudentID = new ArrayList<String>(Arrays.asList(tempid));
		_StudentName = new ArrayList<String>(Arrays.asList(tempname));
		itemData1= new ArrayList<SwimCompRegi2Item1>();
		int temp_index = _nameid.lastIndexOf(",");
		Log.i(TAG, "nameid = " + _nameid.substring(0, temp_index));
          /*  Log.i(TAG, "_nameid1 = " + _nameid1);
            Log.i(TAG, "_nameid2 = " + _nameid2);
            Log.i(TAG, "_nameid3 = " + _nameid3);
            Log.i(TAG, "_nameid4 = " + _nameid4);
            Log.i(TAG, "_nameid5 = " + _nameid5);*/
		String s = AppConfiguration.swimComptitionStudentID.toString();
		StringTokenizer st = new StringTokenizer(s, ",");
		try {
			if (st.hasMoreTokens()) {
				firstid = st.nextToken();

				secondid = st.nextToken();
				thirdid = st.nextToken();
			} else {
				firstid = "";

				secondid = "";
				thirdid = "";
			}

		}catch (NoSuchElementException e)
		{
			e.toString();
			// firstname="";
			Log.i(TAG, "firstid exception = " + e.toString());
		}
		String s1 = AppConfiguration.swimComptitionStudentName.toString();
		StringTokenizer st1 = new StringTokenizer(s1, ",");

		try {

			if(st1.hasMoreTokens()) {
				firstname = st1.nextToken();
				secondname = st1.nextToken();
				thirdname = st1.nextToken();
			}
			else{
				firstname = " ";
				secondname = " ";
				thirdname = " ";

			}
			if (firstname.equals("") || firstname.equalsIgnoreCase(null)) {
				firstname = " ";
			}
			if (secondname.equals("") || secondname.equalsIgnoreCase(null)) {
				secondname = " ";
			}
			if (thirdname.equals("") || thirdname.equalsIgnoreCase(null)) {
				thirdname = " ";
			}
		}catch (NoSuchElementException e){
			e.toString();
			// firstname="";
			Log.i(TAG, "first exception = " + e.toString());

		}

		Log.i(TAG, "first = " + firstid);
		Log.i(TAG, "second = " + secondid);
		Log.i(TAG, "third = " + thirdid);
		Log.i(TAG, "firstname = " + firstname);
		Log.i(TAG, "secondname = " + secondname);
		Log.i(TAG, "thirdname = " + thirdname);
		Log.i(TAG, "swimComptitionStudentName = " + AppConfiguration.swimComptitionStudentName.toString());
		Log.i(TAG, " swimComptitionStudentid = " + AppConfiguration.swimComptitionStudentID.toString());


		_nameid = _nameid.substring(0, temp_index);
		isInternetPresent = Utility.isNetworkConnected(getActivity());
		if(!isInternetPresent){
//			AppConfiguration.showAlertDialog(getActivity(), null, null);
			onDetectNetworkState().show();
		}
		else{
		
			new LoadChildData1().execute();
			
			btnFeedback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

				}
			});
			
			btnHelpCenter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

				}
			});
			
		}
		
		list = (ListView) rootView.findViewById(R.id.list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
		title_black=(RelativeLayout)rootView.findViewById(R.id.title_black);
//		title_black.setVisibility(View.GONE);
        View view = rootView.findViewById(R.id.header);

        TextView title = (TextView)view.findViewById(R.id.page_title);
        title.setText("List");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.menu_icon_new);
        LinearLayout ll_ProgressReport,ll_ViewCertificate,ll_RibbonCount;
        ll_ProgressReport = (LinearLayout) view.findViewById(R.id.scdl_lsn);
        ll_ViewCertificate = (LinearLayout) view.findViewById(R.id.scdl_mkp);
        ll_RibbonCount = (LinearLayout) view.findViewById(R.id.waitlist);

        View vert_1 = (View) view.findViewById(R.id.vert_1);
        vert_1.setVisibility(View.GONE);
        ll_ProgressReport.setVisibility(View.GONE);

        TextView txt_2 = (TextView)view.findViewById(R.id.txt_2);
        TextView txt_3 = (TextView)view.findViewById(R.id.txt_3);

        txt_2.setText("Feedback");
        txt_3.setText("Help Center");

        Button relMenu = (Button)view.findViewById(R.id.returnStack);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity.onLeft();
            }
        });

        ll_ViewCertificate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),FeedbackActivity.class);
                startActivity(i);
            }
        });

        ll_RibbonCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),HelpCenterActivity.class);
                startActivity(i);
            }
        });

		
		return rootView;
	}
	
	public void initialization()
	{
		btnFeedback = (Button)rootView.findViewById(R.id.btnFeedback);
		btnHelpCenter = (Button)rootView.findViewById(R.id.btnHelpCenter);

	}
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
		builder1.setIcon(R.drawable.logo);
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub

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

	/*public void getContactList() {
		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		
		String responseString = WebServicesCall.RunScript(AppConfiguration.contactUsURL, param);
		readAndParseJSON(responseString);
	}

	public void readAndParseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successContact = reader.getString("Success");
			if (successContact.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("SiteDetail");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					String Sitename = jsonChildNode.getString("Sitename").trim();
					String SiteDetails = jsonChildNode.getString("SiteDetails").trim();
					String Pnoneno = jsonChildNode.getString("Pnoneno").trim();
					String Details2 = jsonChildNode.getString("Details2").trim();
					
					hashmap.put("Sitename", Sitename);
					hashmap.put("SiteDetails", SiteDetails);
					hashmap.put("Pnoneno", Pnoneno);
					hashmap.put("Details2", Details2);

					contactList.add(hashmap);

				}

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class LoadContactListAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			getContactList();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			
			if (successContact.toString().equals("True")) {

				CustomList adapter = new CustomList(getActivity(),contactList);
				list.setAdapter(adapter);
				

			} else {
				//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
			}
			
		}
	}
	public class CustomList extends ArrayAdapter<String> {
		private_lessons final Activity context;
		private_lessons final ArrayList<HashMap<String, String>> data;

		public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
			super(context, R.layout.list_row_contact_us);
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
		public View getView(final int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.list_row_contact_us, null, true);
			
			TextView txtSiteName = (TextView) rowView.findViewById(R.id.txtSiteName);
			txtSiteName.setText(data.get(position).get("Sitename"));
			
			TextView txtVenue = (TextView) rowView.findViewById(R.id.txtVenue);
			txtVenue.setText(Html.fromHtml(data.get(position).get("SiteDetails")));
			
			TextView txtPhone = (TextView) rowView.findViewById(R.id.txtPhone);
			txtPhone.setText(Html.fromHtml(data.get(position).get("Pnoneno")));
			
			TextView txtHrsOfOperation = (TextView) rowView.findViewById(R.id.txtHrsOfOperation);
			txtHrsOfOperation.setText(Html.fromHtml(data.get(position).get("Details2")));
			
			txtPhone.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:"+data.get(position).get("Pnoneno")));
					startActivity(callIntent);
				}
			});

			return rowView;
		}
	}*/
	private class LoadChildData1 extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			// AppConfiguration.selectedChildDataMainList.clear();
			// AppConfiguration.selectedStudents.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("MeetdatetimeValue",
					DateValue);
			param.put("MeetdatetimeText",
					time);
			//  param.put("ChildListArry", _nameid);
			param.put("ChildListArry", secondid);
			String responseString = WebServicesCall
					.RunScript(
							AppConfiguration.swimCompititionStep2URL, param);

			try {
				JSONObject reader = new JSONObject(responseString);
				successLoadChildList = reader.getString("Success");
				if (successLoadChildList.toString().equals("True")) {
					JSONArray jsonMainNode = reader
							.optJSONArray("SwimMeetCheck2");

					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						JSONArray jsonArray = jsonChildNode
								.getJSONArray("EvantDetails");
						ArrayList<String> _Event = new ArrayList<String>();
						ArrayList<String> _AgeGroup = new ArrayList<String>();
						ArrayList<String> _Distance = new ArrayList<String>();
						ArrayList<String> _StrokeType = new ArrayList<String>();
						ArrayList<String> _StudentID = new ArrayList<String>();
						ArrayList<String> _tdid = new ArrayList<String>();
						ArrayList<String> _ControlType = new ArrayList<String>();
						ArrayList<String> _Text = new ArrayList<String>();
						ArrayList<String> _Value = new ArrayList<String>();


						for (int j = 0; j < jsonArray.length(); j++) {
							JSONObject jObject = jsonArray
									.getJSONObject(j);
							_Event.add(jObject.getString("Event"));
							_AgeGroup
									.add(jObject.getString("AgeGroup"));
							_Distance
									.add(jObject.getString("Distance"));
							_StrokeType.add(jObject
									.getString("StrokeType"));
							_StudentID.add(jObject
									.getString("StudentID"));
							_tdid.add(jObject.getString("tdid"));
							_ControlType.add(jObject
									.getString("ControlType"));
							_Text.add(jObject.getString("Text"));
							_Value.add(jObject.getString("Value"));
						}
						Event.add(_Event);
						AgeGroup.add(_AgeGroup);
						Distance.add(_Distance);
						StrokeType.add(_StrokeType);
						StudentID.add(_StudentID);
						tdid.add(_tdid);
						ControlType.add(_ControlType);
						Text.add(_Text);
						Value.add(_Value);
					}

				} else {
					JSONArray jsonMainNode = reader
							.optJSONArray("SwimMeetCheck2");

					for (int i = 0; i < jsonMainNode.length(); i++) {

						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						message = jsonChildNode.getString("Msg").trim();

					}
				}

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

			if (successLoadChildList.toString().equals("True")) {

				for (int i = 0; i < StudentID.size(); i++) {
					System.out.println("St : "+StudentID.size());
					System.out.println("st : "+_StudentID.size());
					itemData1.add(new SwimCompRegi2Item1(_StudentID.get(i),
							_StudentName.get(i), Event.get(i), AgeGroup.get(i),
							Distance.get(i), StrokeType.get(i), StudentID
							.get(i), tdid.get(i), ControlType.get(i),
							Text.get(i), Value.get(i)));
				}
				//list2.setAdapter(_adpter1);

			} else {
				Toast.makeText(getActivity(), "" + message,
						Toast.LENGTH_LONG).show();
			}
		}
	}
	public class SwimCompRegi2Item1 {
		String _StudentId, _StudentName;
		ArrayList<String> Event, AgeGroup, Distance, StrokeType, StudentID,
				tdid, ControlType, Text, Value;

		public SwimCompRegi2Item1(String _StudentId, String _StudentName,
								  ArrayList<String> event, ArrayList<String> ageGroup,
								  ArrayList<String> distance, ArrayList<String> strokeType,
								  ArrayList<String> studentID, ArrayList<String> tdid,
								  ArrayList<String> controlType, ArrayList<String> text,
								  ArrayList<String> value) {
			super();
			this._StudentId = _StudentId;
			this._StudentName = _StudentName;
			Event = event;
			AgeGroup = ageGroup;
			Distance = distance;
			StrokeType = strokeType;
			StudentID = studentID;
			this.tdid = tdid;
			ControlType = controlType;
			Text = text;
			Value = value;
		}

		public String get_StudentId() {
			return _StudentId;
		}

		public void set_StudentName(String _StudentName) {
			this._StudentName = _StudentName;
		}

		public String get_StudentName() {
			return _StudentName;
		}

		public void set_StudentId(String _StudentId) {
			this._StudentId = _StudentId;
		}

		public ArrayList<String> getEvent() {
			return Event;
		}

		public void setEvent(ArrayList<String> event) {
			Event = event;
		}

		public ArrayList<String> getAgeGroup() {
			return AgeGroup;
		}

		public void setAgeGroup(ArrayList<String> ageGroup) {
			AgeGroup = ageGroup;
		}

		public ArrayList<String> getDistance() {
			return Distance;
		}

		public void setDistance(ArrayList<String> distance) {
			Distance = distance;
		}

		public ArrayList<String> getStrokeType() {
			return StrokeType;
		}

		public void setStrokeType(ArrayList<String> strokeType) {
			StrokeType = strokeType;
		}

		public ArrayList<String> getStudentID() {
			return StudentID;
		}

		public void setStudentID(ArrayList<String> studentID) {
			StudentID = studentID;
		}

		public ArrayList<String> getTdid() {
			return tdid;
		}

		public void setTdid(ArrayList<String> tdid) {
			this.tdid = tdid;
		}

		public ArrayList<String> getControlType() {
			return ControlType;
		}

		public void setControlType(ArrayList<String> controlType) {
			ControlType = controlType;
		}

		public ArrayList<String> getText() {
			return Text;
		}

		public void setText(ArrayList<String> text) {
			Text = text;
		}

		public ArrayList<String> getValue() {
			return Value;
		}

		public void setValue(ArrayList<String> value) {
			Value = value;
		}
	}

	public class SwimCompRegi2Adapter1 extends ArrayAdapter<SwimCompRegi2Item1> {
		List<SwimCompRegi2Item1> data;
		Context context;
		int layoutResID;
		LayoutInflater inflater;

		public SwimCompRegi2Adapter1(Context context, int resource,
									 List<SwimCompRegi2Item1> data) {
			super(context, resource, data);
			this.data = data;
			this.context = context;
			this.layoutResID = resource;
			inflater = LayoutInflater.from(this.context);
		}

		private class NewViewHolder {
			LinearLayout ll_students;
			TextView tv_swim_comp_regi2_name;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public SwimCompRegi2Item1 getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {

			return 1;
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final NewViewHolder holder;
			try {
				if (convertView == null) {
					convertView = LayoutInflater.from(parent.getContext())
							.inflate(layoutResID, null);
					holder = new NewViewHolder();
					holder.tv_swim_comp_regi2_name = (TextView) convertView
							.findViewById(R.id.tv_swim_comp_regi2_name);
					holder.ll_students = (LinearLayout) convertView
							.findViewById(R.id.ll_swim_comp_regi2_item);
					convertView.setTag(holder);

					final SwimCompRegi2Item1 _itemData = data.get(position);
					holder.tv_swim_comp_regi2_name.setText(_itemData
							.get_StudentName());
                  /*  name_1.setText(_itemData
                            .get_StudentName());
                    name_2.setText(_itemData
                            .get_StudentName());
                    name_3.setText(_itemData
                            .get_StudentName());*/
             /*       selstudentname.setText(_itemData
                            .get_StudentName());*/
					holder.tv_swim_comp_regi2_name.setVisibility(ViewGroup.GONE);
					for (int i = 0; i < _itemData.getStudentID().size(); i++) {
						final int pos = i;
						LayoutInflater minflater = LayoutInflater
								.from(this.context);
						View childView = minflater.inflate(
								R.layout.list_row_swim_compitition2_body, null);
						TextView txtStrokeType = (TextView) childView
								.findViewById(R.id.txtStrokeType);
						TextView txtEvent = (TextView) childView
								.findViewById(R.id.txtEvent);
						TextView txtAgeGroup = (TextView) childView
								.findViewById(R.id.txtAgeGroup);
						TextView txtControlType = (TextView) childView
								.findViewById(R.id.txtControlType);
						TextView txttDid = (TextView) childView
								.findViewById(R.id.txttDid);
						TextView txtDistance = (TextView) childView
								.findViewById(R.id.txtDistance);
						final CheckBox chk = (CheckBox) childView
								.findViewById(R.id.chk);
						txtAgeGroup.setVisibility(View.GONE);
						txttDid.setVisibility(View.GONE);
						txtDistance.setVisibility(View.GONE);

                       /* txtStrokeType.setText(Html
                                .fromHtml("<b>StrokeType: </b>"
                                        + _itemData.getStrokeType().get(i)));*/

						txtStrokeType.setText(Html
								.fromHtml("<b>" + _itemData.getStrokeType().get(i) + " - " + _itemData.getDistance().get(i) + "</b>"));

                    /*    txtDistance.setText(Html.fromHtml("<b>Distance: </b>"
                                + _itemData.getDistance().get(i)));*/
						txtEvent.setText( _itemData.getAgeGroup().get(i)+" "+" - "+"Event"+" # "+
								_itemData.getEvent().get(i));
                    /*    txtEvent.setText(Html.fromHtml("<b>Event#: </b>"
                                + _itemData.getEvent().get(i)));*/

						String controlType = _itemData.getControlType().get(i);
						if (controlType.contains("CheckBox")) {
							chk.setVisibility(View.VISIBLE);
							chk.setTag(_itemData.getValue().get(i));
							txtControlType.setVisibility(View.GONE);

							chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									if (isChecked) {
										// AppConfiguration.selectedStudents.set(
										// position, true);
										if(!hashmap.containsKey(position+"-"+pos)){
											ArrayList<String> abc = new ArrayList<String>();
											abc.add(_itemData.getEvent().get(pos));
											abc.add(_itemData.getAgeGroup().get(pos));
											abc.add(_itemData.getDistance().get(pos));
											abc.add(_itemData.getStrokeType().get(pos));
											abc.add(_itemData.getStudentID().get(pos));
											abc.add(_itemData.getTdid().get(pos));
											abc.add(_itemData.getControlType().get(pos));
											abc.add(_itemData.getText().get(pos));
											abc.add(_itemData.getValue().get(pos));
											hashmap.put(position+"-"+pos, abc);
										}
										if(!temp_value.contains(chk.getTag())){
											temp_value.add(chk.getTag().toString().trim());
										}
										if(!t_sname.contains(_itemData.get_StudentName().toString().trim())){
											t_sname.add(_itemData.get_StudentName().toString().trim());
										}
										if(!t_sid.contains(_itemData.get_StudentId().toString().trim())){
											t_sid.add(_itemData.get_StudentId().toString().trim());
										}
										Log.e("temp_value",
												temp_value
														.toString());
									} else {
										ArrayList<String> abc = new ArrayList<String>();
										abc.add(_itemData.getEvent().get(pos));
										abc.add(_itemData.getAgeGroup().get(pos));
										abc.add(_itemData.getDistance().get(pos));
										abc.add(_itemData.getStrokeType().get(pos));
										abc.add(_itemData.getStudentID().get(pos));
										abc.add(_itemData.getTdid().get(pos));
										abc.add(_itemData.getControlType().get(pos));
										abc.add(_itemData.getText().get(pos));
										abc.add(_itemData.getValue().get(pos));
										if(hashmap.containsKey(position+"-"+pos)){
											hashmap.remove(position+"-"+pos);
										}
										if(temp_value.contains(chk.getTag())){
											temp_value.remove(chk.getTag().toString().trim());
										}
										if(t_sname.contains(_itemData.get_StudentName())){
											t_sname.remove(_itemData.get_StudentName());
										}
										if(t_sid.contains(_itemData.get_StudentId())){
											t_sid.remove(_itemData.get_StudentId());
										}
										Log.e("temp_value",
												temp_value
														.toString());
									}
								}
							});

						} else {
							chk.setVisibility(View.GONE);
							txtControlType.setVisibility(View.VISIBLE);
							txtControlType.setText("Registered");

							Log.e("checkedData",
									AppConfiguration.selectedStudents
											.toString());

						}

						holder.ll_students.addView(childView);
					}
				} else {
					holder = (NewViewHolder) convertView.getTag();
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relMenu:
				DashBoardActivity.onLeft();
			break;
		
		default:
			break;
		}
	}		
		

}
