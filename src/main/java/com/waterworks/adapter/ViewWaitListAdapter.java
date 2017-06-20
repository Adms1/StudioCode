package com.waterworks.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.ViewWaitlistActivity;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

public class ViewWaitListAdapter extends BaseAdapter {
	Context context;
	private static final String TAG = "View waitlist Adater";
	ArrayList<String> student,startDate,startTime,day,lessonType,instructor,classType,tbid,SelectedTBID;
	SparseBooleanArray mChecked;
	Button btn_save;

String token,familyID; 
	
	public ViewWaitListAdapter(Context context, ArrayList<String> student,
			ArrayList<String> startDate, ArrayList<String> startTime,
			ArrayList<String> day, ArrayList<String> lessonType,
			ArrayList<String> instructor, ArrayList<String> classType,
			ArrayList<String> tbid,Button btn_save) {
		super();
		this.context = context;
		this.student = student;
		this.startDate = startDate;
		this.startTime = startTime;
		this.day = day;
		this.lessonType = lessonType;
		this.instructor = instructor;
		this.classType = classType;
		this.tbid = tbid;
		this.btn_save = btn_save;
		
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(context.getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tbid.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
		TextView tv_student,tv_lt,tv_inst,tv_sd,tv_st,tv_wd,tv_ct;
		CheckBox chb_check;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try{
			if(convertView==null){
				holder = new ViewHolder();
				
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.viewwaitlist_item, null);
				holder.tv_student = (TextView)convertView.findViewById(R.id.tv_vw_item_student);
				holder.tv_lt = (TextView)convertView.findViewById(R.id.tv_vw_item_lt);
				holder.tv_inst = (TextView)convertView.findViewById(R.id.tv_vw_item_instructor);
				holder.tv_sd = (TextView)convertView.findViewById(R.id.tv_vw_item_sd);
				holder.tv_st = (TextView)convertView.findViewById(R.id.tv_vw_item_st);
				holder.tv_wd = (TextView)convertView.findViewById(R.id.tv_vw_item_wd);
				holder.tv_ct = (TextView)convertView.findViewById(R.id.tv_vw_item_cl);
				holder.chb_check = (CheckBox)convertView.findViewById(R.id.chb_vw_item_check);
			
			mChecked = new SparseBooleanArray();
			SelectedTBID = new ArrayList<String>();
		    holder.tv_student.setText(Html.fromHtml("<b>Student: </b>"+student.get(position)));
		    holder.tv_lt.setText(Html.fromHtml("<b>Lesson Type: </b>"+lessonType.get(position)));
		    holder.tv_inst.setText(Html.fromHtml("<b>Instructor: </b>"+instructor.get(position)));
		    holder.tv_sd.setText(Html.fromHtml("<b>Satrt Date: </b>"+startDate.get(position)));
		    holder.tv_st.setText(Html.fromHtml("<b>Satrt Time: </b>"+startTime.get(position)));
		    holder.tv_wd.setText(Html.fromHtml("<b>Week Day: </b>"+day.get(position)));
			holder.tv_ct.setText(Html.fromHtml("<b>Class Type: </b>"+classType.get(position)));
			holder.chb_check.setTag(tbid.get(position));

			 holder.chb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){          
		        			mChecked.put(position,isChecked);
		        			Log.i("Value", ""+mChecked);
						
						} else{          
							mChecked.delete(position);
							Log.i("Value1", ""+mChecked);
						}
					}
				});

			
			btn_save.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for (int i = 0; i < mChecked.size(); i++) {
						int pos = mChecked.keyAt(i);
						SelectedTBID.add(tbid.get(pos));
					}
					new submitdata().execute();
					
				}
			});
			}
			else{
				holder = (ViewHolder) convertView.getTag();
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
			param.put("FamilyID",familyID );
			param.put("SelectedTBID",SelectedTBID.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.viewwaitlistsubmit, param);
			try {
            	JSONObject reader = new JSONObject(responseString);
				data_load_page = reader.getString("Success");
				
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
				Toast.makeText(context, "The selected lesson time has beed removed from the waitlist.", Toast.LENGTH_LONG).show();
				((ViewWaitlistActivity)context).new ViewWaitListLoad().execute();
			}
			else{
				Toast.makeText(context, "Not save.", Toast.LENGTH_LONG).show();
			}
		}
	}
	String data_load_page;
}
