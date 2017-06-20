package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class JuniorLifeGuardRegister2 extends Activity{

	private static String TAG = "Dive and Truns Regi 2";
	Boolean isInternetPresent = false;
	Button relMenu;
	String token,familyID,stulist,dataload="False",msg,childno, currentStudentID;
	int siteid;
    LinearLayout llTabs;
	CardView btn_dt2_continue;
	ListView lv_body;
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> JuniorLifeGuardEventList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<String> finalstulist;
//	public static ArrayList<String> FullName,Studentid,tbid,sessionname,startdate,enddate,unitprice,sitename,finalstulist,remark;
	public static SparseBooleanArray mChecked = new SparseBooleanArray();
	ArrayList<String> checked_value= new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juniorlifeguardregister2);

		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

        siteid = Integer.parseInt(getIntent().getStringExtra("siteid"));
        stulist = getIntent().getStringExtra("stulist");
        childno = getIntent().getStringExtra("childno");
        Log.i(TAG, "Child no = "+childno);
        Log.i(TAG, "stulist = " + stulist);
        Initialization();
		setListners();

        String[] temp_name = AppConfiguration.jrLifeGuardStudentName.toString().split(",");
        String[] temp_id = AppConfiguration.jrLifeGuardStudentID.toString().split(",");

        HashMap<String, String> hashMap = null;
        for (int i = 0; i < temp_id.length; i++) {
            hashMap = new HashMap<>();
            hashMap.put("studentID", temp_id[i].toString());
            hashMap.put("studentName", temp_name[i].toString());
            childList.add(hashMap);
        }

        makeTabs(childList.get(0).get("studentID"));
        currentStudentID = childList.get(0).get("studentID").trim();
//        Utility.ping(this, currentStudentID);
        new GetData().execute();
	}

    public void makeTabs(String studentID) {
        llTabs = (LinearLayout) findViewById(R.id.llTabs);

        for (int i = 0; i < childList.size(); i++) {
            View childTabs = getLayoutInflater().inflate(R.layout.layout_tabs, null, false);
            TextView txtStudentNameTabs = (TextView) childTabs.findViewById(R.id.txtStudentNameTabs);
            View viewOrangeBar = (View) childTabs.findViewById(R.id.viewOrangeBar);

            txtStudentNameTabs.setText(childList.get(i).get("studentName"));
            childTabs.setTag(childList.get(i).get("studentID"));
            childTabs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            childTabs.setOnClickListener(myClickLIstener);

            if (!childTabs.getTag().equals(studentID)) {
                txtStudentNameTabs.setTextColor(Color.BLACK);
                childTabs.setBackgroundColor(getResources().getColor(R.color.student_back));
                viewOrangeBar.setBackgroundColor(getResources().getColor(R.color.student_back));
            }

            llTabs.addView(childTabs);
        }
    }

    OnClickListener myClickLIstener = new OnClickListener() {
        public void onClick(final View v) {
            llTabs.removeAllViews();
            makeTabs(v.getTag().toString());
            currentStudentID = v.getTag().toString().trim();
//            Utility.ping(JuniorLifeGuardRegister2.this, currentStudentID);
			if(JuniorLifeGuardEventList.size() > 0){
				Dt2BodyAdapter dt2BodyAdapter = new Dt2BodyAdapter(JuniorLifeGuardEventList);
				lv_body.setAdapter(dt2BodyAdapter);
			}else {
				Utility.ping(JuniorLifeGuardRegister2.this, "No Events in this Session");
			}
        }
    };

	public void setListners(){
		isInternetPresent = Utility
				.isNetworkConnected(JuniorLifeGuardRegister2.this);

		if(isInternetPresent){

			relMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			});
			Button btnHome = (Button) findViewById(R.id.btnHome);
			btnHome.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intentCheckin = new Intent(JuniorLifeGuardRegister2.this, DashBoardActivity.class);
					intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intentCheckin);
					finish();
				}
			});
			btn_dt2_continue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StringBuilder str = new StringBuilder();
                    StringBuilder strStudentID = new StringBuilder();
                    if(checked_value.size() > 0) {
                        for (int i = 0; i < checked_value.size(); i++) {
                            String value = checked_value.get(i);
                            String[] valueArray = value.replaceAll("\\s","").split("\\|");
                            if (!strStudentID.toString().contains(valueArray[0])) {
                                strStudentID = strStudentID.append(valueArray[0] + ", ");
                            }

                            if(str.toString().contains(valueArray[0])){
                                str = str.append("*"+valueArray[1]);
                            }else{
                                if(i>0){
                                    str = str.append(","+valueArray[0]+"|"+valueArray[1]);
                                }else{
                                    str = str.append(valueArray[0]+"|"+valueArray[1]);
                                }

                            }
                        }

                        String strStudid = strStudentID.substring(0, strStudentID.length() - 2);
                        if (strStudid.trim().length() == AppConfiguration.jrLifeGuardStudentID.length()) {
                            Intent i = new Intent(getApplicationContext(),JuniorLifeGuardRegister3.class);
                            i.putExtra("siteid", ""+siteid);
                            i.putExtra("mCheck", ""+mChecked);
                            i.putExtra("childno",""+childno);
                            i.putExtra("strStuList", str.toString());
                            startActivity(i);
//					finish();
                        }else {
                            Utility.ping(JuniorLifeGuardRegister2.this, "Please select at least one session for each student.");
                        }
                    }else {
                        Utility.ping(JuniorLifeGuardRegister2.this, "Please select at least one session before going to next step.");
                    }
				}
			});
		}
		else{
			onDetectNetworkState().show();
		}
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		View include_layout_step_boxes = (View) findViewById(R.id.include_layout_step_boxes);
		TextView txtBox2 = (TextView) include_layout_step_boxes.findViewById(R.id.txtBox2);
		txtBox2.setTextColor(Color.WHITE);
		txtBox2.setBackgroundColor(getResources().getColor(R.color.design_change_d2));

		relMenu = (Button)findViewById(R.id.relMenu);
		btn_dt2_continue = (CardView)findViewById(R.id.btn_continue_dt2);
		lv_body = (ListView)findViewById(R.id.lv_dt2_list);
		/*FullName = new ArrayList<String>();
		Studentid = new ArrayList<String>();
		tbid = new ArrayList<String>();
		sessionname = new ArrayList<String>();
		startdate = new ArrayList<String>();
		enddate = new ArrayList<String>();
		unitprice = new ArrayList<String>();
		sitename = new ArrayList<String>();
		remark=new ArrayList<String>();*/
		finalstulist = new ArrayList<String>();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		isInternetPresent = Utility
				.isNetworkConnected(JuniorLifeGuardRegister2.this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//		super.onBackPressed();
		finish();
	}

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(JuniorLifeGuardRegister2.this);
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

	/////////////////////GetDate///////////////

	private class GetData extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(JuniorLifeGuardRegister2.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
//			String basketID = (AppConfiguration.BasketID.equals("BasketID")) ? "0" :  AppConfiguration.BasketID;

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("strStuList", stulist);
			param.put("Siteid",""+siteid);
			param.put("ShowFlg", ""+true);
			param.put("BasketID", AppConfiguration.BasketID);

			String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN+AppConfiguration.Prg_Get_GuardPrep_Step2, param);
			try {
                JuniorLifeGuardEventList.clear();
				JSONObject reader = new JSONObject(responseString);
				dataload= reader.getString("Success");
				if(dataload.toString().equalsIgnoreCase("True")){

					JSONArray jsonMainNode = reader.optJSONArray("GuardPrep");
					for(int i = 0 ;i < jsonMainNode.length();i++)
					{
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        HashMap<String, String> hashmap = new HashMap<String, String>();
						hashmap.put("Fullname", jsonChildNode.getString("Fullname"));
                        hashmap.put("Studentid", jsonChildNode.getString("Studentid"));
                        hashmap.put("tbid", jsonChildNode.getString("tbid"));
                        hashmap.put("sessionname", jsonChildNode.getString("sessionname"));
                        hashmap.put("startdate", jsonChildNode.getString("startdate"));
                        hashmap.put("enddate", jsonChildNode.getString("enddate"));
                        hashmap.put("unitprice", jsonChildNode.getString("unitprice"));
                        hashmap.put("sitename", jsonChildNode.getString("sitename"));
						hashmap.put("Time", jsonChildNode.getString("Time"));
                        hashmap.put("Remark", jsonChildNode.getString("Remark"));
						hashmap.put("alreadyshopped", jsonChildNode.getString("alreadyshopped"));

                        JuniorLifeGuardEventList.add(hashmap);
					}
				}
				else{
					JSONArray jsonMainNode = reader.optJSONArray("GuardPrep");
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
					msg = jsonChildNode.getString("Msg");
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			catch (Exception e) {
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
			if(dataload.toString().equalsIgnoreCase("True")){
				if(JuniorLifeGuardEventList.size() > 0){
					Dt2BodyAdapter dt2BodyAdapter = new Dt2BodyAdapter(JuniorLifeGuardEventList);
					lv_body.setAdapter(dt2BodyAdapter);
				}else {
					Utility.ping(JuniorLifeGuardRegister2.this, "No Events in this Session");
				}
//				lv_body.setAdapter(new Dt2BodyAdapter(JuniorLifeGuardRegister2.this,
//						FullName, Studentid, tbid, sessionname, startdate, enddate, unitprice, sitename,remark));
			}
			else{
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	///////////////Dt2BodyAdapter//////////////////

	public class Dt2BodyAdapter extends BaseAdapter{

//		Context context;
        private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
        //		ArrayList<String> FullName,Studentid,tbid,sessionname,startdate,enddate,unitprice,sitename,remark;
        public Dt2BodyAdapter(ArrayList<HashMap<String, String>> list) {
            super();
//            this.context = context;
//            FullName = fullName;
//            Studentid = studentid;
//            this.tbid = tbid;
//            this.sessionname = sessionname;
//            this.startdate = startdate;
//            this.enddate = enddate;
//            this.unitprice = unitprice;
//            this.sitename = sitename;
//            this.remark=remark;
            for(int i = 0;i < list.size();i++){
                if(currentStudentID.equalsIgnoreCase(list.get(i).get("Studentid"))){
                    this.data.add(list.get(i));
                }
            }
        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
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
			TextView tv_name,tv_sess,tv_desc,tv_stdate,tv_enddate,tv_site,tv_price,tv_remark;
			CheckBox chb_add;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if(convertView==null){
					holder = new ViewHolder();

					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.dive_turns_2_item, null);
					holder.tv_name = (TextView)convertView.findViewById(R.id.dt2_name);
					holder.tv_sess = (TextView)convertView.findViewById(R.id.dt2_sess);
					holder.tv_desc = (TextView)convertView.findViewById(R.id.dt2_desc);
					holder.tv_stdate = (TextView)convertView.findViewById(R.id.dt2_stdate);
					holder.tv_enddate = (TextView)convertView.findViewById(R.id.dt2_eddate);
					holder.tv_site = (TextView)convertView.findViewById(R.id.dt2_site);
					holder.tv_price = (TextView)convertView.findViewById(R.id.dt2_untprice);
					holder.tv_remark = (TextView)convertView.findViewById(R.id.dt2_remark);
					holder.chb_add = (CheckBox)convertView.findViewById(R.id.dt2_add);

                    holder.tv_name.setText(Html.fromHtml("<b>Participant:</b> " + data.get(position).get("Fullname")));
                    holder.tv_sess.setText(Html.fromHtml("<b>Session #:</b> "+data.get(position).get("tbid")));
                    holder.tv_desc.setText(Html.fromHtml("<b>Description:</b> "+data.get(position).get("sessionname")));
                    holder.tv_stdate.setText(Html.fromHtml("<b>Start Date:</b> "+data.get(position).get("startdate")));
                    holder.tv_enddate.setText(Html.fromHtml("<b>Start Time:</b> "+data.get(position).get("Time")));
                    holder.tv_site.setText(Html.fromHtml("<b>Site:</b> "+data.get(position).get("sitename")));
                    holder.tv_price.setText(Html.fromHtml("<b>Unit Price:</b> "+data.get(position).get("unitprice")));
                    holder.tv_remark.setText(Html.fromHtml("<b>Remark:</b> " + data.get(position).get("Remark")));

					if(data.get(position).get("alreadyshopped")=="1")
					{
						holder.chb_add.setVisibility(View.GONE);
					}

                    holder.chb_add.setTag(data.get(position).get("Studentid") + "|" + data.get(position).get("tbid"));

					holder.chb_add.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked){
                                if(!checked_value.contains(buttonView.getTag().toString())){
                                    checked_value.add(buttonView.getTag().toString());
                                    mChecked.put(position, isChecked);
                                }
								Log.i(TAG, mChecked.toString());
							}
							else{
                                if(checked_value.contains(buttonView.getTag().toString())){
                                    checked_value.remove(buttonView.getTag().toString());
                                    mChecked.delete(position);
                                }
								Log.i(TAG, mChecked.toString());
							}
						}
					});
				}
				else{
					holder = (ViewHolder) convertView.getTag();
				}
                for (String row : checked_value) {
                    String[] spitString = row.split("\\|");
                    if (currentStudentID.trim().equalsIgnoreCase(spitString[0].trim()) && spitString[1].trim().equalsIgnoreCase(data.get(position).get("tbid"))) {
                        holder.chb_add.setChecked(true);
                    }
                }
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
			catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return convertView;
		}
	}
}