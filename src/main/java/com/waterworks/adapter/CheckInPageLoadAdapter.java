package com.waterworks.adapter;

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
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.CheckIn2Activity;
import com.waterworks.CheckInFragment;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class CheckInPageLoadAdapter extends BaseAdapter {
	Boolean isInternetPresent = false;
	ArrayList<String> fullname,wu_preference1,wu_studentid,wu_swimmeetid,wu_preference2,wu_eventnumber,
	wu_groupdescription,wu_description,wu_strokedescription,Time1,wu_checkin,listtbid,YES_NO,
	PREF1,PREF2,CheckInListArray;
	Activity context;
	Button btn_proceed;
	String token,familyID;
	private static final String TAG = "Checkin Adater";
	ArrayList<String> temp_stid = new ArrayList<String>();
	ArrayList<String> temp_pref1 = new ArrayList<String>();
	public CheckInPageLoadAdapter(ArrayList<String> fullname,
			ArrayList<String> wu_preference1, ArrayList<String> wu_studentid,
			ArrayList<String> wu_swimmeetid, ArrayList<String> wu_preference2,
			ArrayList<String> wu_eventnumber,
			ArrayList<String> wu_groupdescription,
			ArrayList<String> wu_description,
			ArrayList<String> wu_strokedescription, ArrayList<String> time1,
			ArrayList<String> wu_checkin, ArrayList<String> listtbid,
			Activity context) {
		super();
		this.fullname = fullname;
		this.wu_preference1 = wu_preference1;
		this.wu_studentid = wu_studentid;
		this.wu_swimmeetid = wu_swimmeetid;
		this.wu_preference2 = wu_preference2;
		this.wu_eventnumber = wu_eventnumber;
		this.wu_groupdescription = wu_groupdescription;
		this.wu_description = wu_description;
		this.wu_strokedescription = wu_strokedescription;
		Time1 = time1;
		this.wu_checkin = wu_checkin;
		this.listtbid = listtbid;
        this.btn_proceed = CheckInFragment.btn_proceed;
		this.context = context;

		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(context.getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wu_swimmeetid.size();
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
		TextView tv_name,tv_event,tv_age,tv_distance,tv_stroke,tv_besttime,tv_dive,tv_end;
		CheckBox chk_check;
		Button btn_diving,btn_endlan;
		ListPopupWindow lpw_divingin,lpw_endlan;
		View view_temp;
		LinearLayout selection_lay;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try{
			if(convertView==null){
				holder = new ViewHolder();

				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.checkin_load_item_body_new, null);
				holder.tv_name = (TextView)convertView.findViewById(R.id.tv_chkld_item_name);
				holder.tv_event = (TextView)convertView.findViewById(R.id.tv_chkld_event);
				holder.tv_age = (TextView)convertView.findViewById(R.id.tv_chkld_age);
				holder.tv_distance = (TextView)convertView.findViewById(R.id.tv_chkld_distance);
				holder.tv_stroke = (TextView)convertView.findViewById(R.id.tv_chkld_stroke);
				holder.btn_diving = (Button)convertView.findViewById(R.id.btn_chkld_item_divingblock);
				holder.btn_endlan = (Button)convertView.findViewById(R.id.btn_chkld_item_endlane);
				holder.tv_dive = (TextView)convertView.findViewById(R.id.tv_chkin_item_diving);
				holder.tv_end = (TextView)convertView.findViewById(R.id.tv_chkin_item_end);
				holder.view_temp = (View)convertView.findViewById(R.id.view_temp);
				holder.selection_lay = (LinearLayout)convertView.findViewById(R.id.selection_lay);
				
				isInternetPresent = Utility
						.isNetworkConnected(context);
				CheckInListArray = new ArrayList<String>();
				for (int i = 0; i < wu_studentid.size(); i++) {
					if(!temp_stid.contains(wu_studentid.get(i))){
						temp_stid.add(wu_studentid.get(i));
						temp_pref1.add(wu_preference1.get(i));
					}
				}
				PREF1 = new ArrayList<String>();
				PREF2= new ArrayList<String>();
				PREF1.add("2");
				PREF1.add("0");
				PREF2.add("0");
				PREF2.add("1");
				YES_NO = new ArrayList<String>();
				YES_NO.add("Yes");
				YES_NO.add("No");
				holder.chk_check = (CheckBox)convertView.findViewById(R.id.chb_chkld_check);
				holder.lpw_divingin = new ListPopupWindow(context);
				holder.lpw_endlan = new ListPopupWindow(context);
				String fname = fullname.get(position);
				if(fname.toString().equalsIgnoreCase("")){
					holder.tv_name.setVisibility(View.GONE);
					holder.btn_diving.setVisibility(View.GONE);
					holder.btn_endlan.setVisibility(View.GONE);
					holder.tv_dive.setVisibility(View.GONE);
					holder.tv_end.setVisibility(View.GONE);
					holder.view_temp.setVisibility(View.GONE);
				}
				else{
					holder.tv_name.setText(Html.fromHtml("Child: "+"<font color='#0000FF'>"+fname+"</font>"));
					holder.tv_name.setVisibility(View.VISIBLE);
					holder.btn_diving.setVisibility(View.VISIBLE);
					holder.btn_endlan.setVisibility(View.VISIBLE);
					holder.tv_dive.setVisibility(View.VISIBLE);
					holder.tv_end.setVisibility(View.VISIBLE);
					holder.view_temp.setVisibility(View.VISIBLE);
				}

				holder.tv_name.setText(Html.fromHtml("Child: "+fname+"</font>"));
				holder.tv_event.setText(Html.fromHtml("Event: "+wu_eventnumber.get(position)));
				holder.tv_age.setText(Html.fromHtml("Age Group: "+wu_groupdescription.get(position)));
				holder.tv_distance.setText(Html.fromHtml("Distance: "+wu_description.get(position)));
				holder.tv_stroke.setText(Html.fromHtml("Stroke: "+wu_strokedescription.get(position)));

				holder.btn_diving.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						holder.lpw_divingin.show();
					}
				});
				holder.lpw_divingin.setAdapter(new ArrayAdapter<String>(
						context,
						R.layout.edittextpopup,YES_NO));
				holder.lpw_divingin.setAnchorView(holder.btn_diving);
				holder.lpw_divingin.setWidth(LayoutParams.WRAP_CONTENT);
				holder.lpw_divingin.setHeight(LayoutParams.WRAP_CONTENT);
				holder.lpw_divingin.setModal(true);
				holder.lpw_divingin.setOnItemClickListener(
						new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int pos, long id) {
								// TODO Auto-generated method stub
								holder.btn_diving.setText(Html.fromHtml(YES_NO.get(pos)));

								String a = wu_studentid.get(position);
								Log.i(TAG, "First index = " + wu_studentid.indexOf(a));
								Log.i(TAG, "Last index = " + wu_studentid.lastIndexOf(a));
								for (int i = wu_studentid.indexOf(a); i <= wu_studentid.lastIndexOf(a); i++) {
									wu_preference1.remove(i);
									wu_preference1.add(i,PREF1.get(pos));
								}
								Log.i(TAG, wu_preference1.toString());
								holder.lpw_divingin.dismiss();
							}
						});
				holder.btn_endlan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						holder.lpw_endlan.show();
					}
				});
				holder.lpw_endlan.setAdapter(new ArrayAdapter<String>(
						context,
						R.layout.edittextpopup,YES_NO));
				holder.lpw_endlan.setAnchorView(holder.btn_endlan);
				holder.lpw_divingin.setWidth(LayoutParams.WRAP_CONTENT);
				holder.lpw_endlan.setHeight(LayoutParams.WRAP_CONTENT);
				holder.lpw_endlan.setModal(true);
				holder.lpw_endlan.setOnItemClickListener(
						new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int pos, long id) {
								// TODO Auto-generated method stub
								holder.btn_endlan.setText(Html.fromHtml(YES_NO.get(pos)));
								String a = wu_studentid.get(position);
								Log.i(TAG, "First index = " + wu_studentid.indexOf(a));
								Log.i(TAG, "Last index = " + wu_studentid.lastIndexOf(a));
								for (int i = wu_studentid.indexOf(a); i <= wu_studentid.lastIndexOf(a); i++) {
									wu_preference2.remove(i);
									wu_preference2.add(i,PREF2.get(pos));
								}
								Log.i(TAG, wu_preference2.toString());
								holder.lpw_endlan.dismiss();
							}
						});
				if(wu_preference1.get(position).equalsIgnoreCase("2")){
					holder.btn_diving.setText(Html.fromHtml("Yes"));
				}
				else{
					holder.btn_diving.setText(Html.fromHtml("No"));
				}
				if(wu_preference2.get(position).equalsIgnoreCase("0")){
					holder.btn_endlan.setText(Html.fromHtml("Yes"));
				}
				else{
					holder.btn_endlan.setText(Html.fromHtml("No"));
				}
				if(wu_checkin.get(position).equalsIgnoreCase("1")){
					holder.chk_check.setChecked(true);
				}
				else{
					holder.chk_check.setChecked(false);
				}
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
            holder.chk_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if(isChecked){
                        wu_checkin.remove(position);
                        wu_checkin.add(position,"1");
                        Log.i("Value", "real array = "+wu_checkin.toString());

                    } else{
                        wu_checkin.remove(position);
                        wu_checkin.add(position,"0");
                        Log.i("Value", "real array = "+wu_checkin.toString());
                    }
                }
            });
            btn_proceed.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    for (int i = 0; i < listtbid.size(); i++) {
                        String pref1, pref2;
                        pref1 = wu_preference1.get(i);
                        pref2 = wu_preference2.get(i);
                        if (pref1.toString().equalsIgnoreCase("2")) {
                            pref1 = "Yes";
                        } else {
                            pref1 = "No";
                        }
                        if (pref2.toString().equalsIgnoreCase("1")) {
                            pref2 = "No";
                        } else {
                            pref2 = "Yes";
                        }
                        String true_false = wu_checkin.get(i);
                        if (true_false.toString().equalsIgnoreCase("1")) {
                            true_false = "True";
                        } else {
                            true_false = "False";
                        }

                        String final_id_list = true_false + ":" + listtbid.get(i) + ":" + fullname.get(i) + ":" +
                                pref1 + ":" + pref2 + ":" + wu_studentid.get(i) + ":" + wu_swimmeetid.get(i);
                        CheckInListArray.add(final_id_list);
                    }
                    Log.i(TAG, CheckInListArray.toString().replaceAll("\\[", "").
                            replaceAll("\\]", ""));
                    if (isInternetPresent) {
                        new submitdata().execute();
                    } else {
                        onDetectNetworkState().show();
                    }
                }
            });
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
	private class submitdata extends AsyncTask<Void, Void, Void>{
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
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("CheckInListArray", CheckInListArray.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

			String responseString = WebServicesCall
					.RunScript(AppConfiguration.checkinproceed, param);
			try {
				JSONObject reader = new JSONObject(responseString);
				data_load_page = reader.getString("Success");
				if(data_load_page.toString().equalsIgnoreCase("True")){
					JSONArray jsonArray = reader.optJSONArray("SwimComp_CheckINProceed");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonChildNode = jsonArray.getJSONObject(i);
						msg = jsonChildNode.getString("MeetID");
					}
				}
				else{
					JSONArray jsonArray = reader.optJSONArray("SwimComp_CheckINProceed");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonChildNode = jsonArray.getJSONObject(i);
						msg = jsonChildNode.getString("Msg");
					}
				}

			}
			catch(Exception e){
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
			if(data_load_page.toString().equals("True"))
			{
                context.finish();
			}
			else{
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		}
	}
	String data_load_page;
	String msg;

	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setIcon(context.getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				((Activity) context).finish();
			}
		})       
		.setPositiveButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		return builder1.create();
	}
}
