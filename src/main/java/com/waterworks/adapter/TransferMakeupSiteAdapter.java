package com.waterworks.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.TransferMakeUpActivity;
import com.waterworks.ViewCartActivity;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

public class TransferMakeupSiteAdapter extends BaseAdapter {

	ArrayList<String> tbid,date,time,sttimehr,sttimemin,lessontype,lessonId,attendancetype,attendanceId,
	Name,firstName,lastName,siteId,siteName,all_siteid,all_sitename;
	Context context;

String token,familyID; 

	public TransferMakeupSiteAdapter(ArrayList<String> tbid, ArrayList<String> date,
			ArrayList<String> time, ArrayList<String> sttimehr,
			ArrayList<String> sttimemin, ArrayList<String> lessontype,
			ArrayList<String> lessonId, ArrayList<String> attendancetype,
			ArrayList<String> attendanceId, ArrayList<String> name,
			ArrayList<String> firstName, ArrayList<String> lastName,
			ArrayList<String> siteId, ArrayList<String> siteName,
			ArrayList<String> all_siteid, ArrayList<String> all_sitename,
			Context context) {
		super();
		this.tbid = tbid;
		this.date = date;
		this.time = time;
		this.sttimehr = sttimehr;
		this.sttimemin = sttimemin;
		this.lessontype = lessontype;
		this.lessonId = lessonId;
		this.attendancetype = attendancetype;
		this.attendanceId = attendanceId;
		Name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.siteId = siteId;
		this.siteName = siteName;
		this.all_siteid = all_siteid;
		this.all_sitename = all_sitename;
		this.context = context;
		
		
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(context.getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d("TransfterMakeUpSite","Token="+token+"\nFamilyID="+familyID);
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tbid.size();
	}
	@Override
	public Object getItem(int paramInt) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int paramInt) {
		// TODO Auto-generated method stub
		return paramInt;
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
		TextView tv_name,tv_lessonname,tv_date,tv_time,tv_sitename,tv_attendance;
		Button btn_sitetype,btn_select;
		ListPopupWindow lpw_sitelist;
		
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try{
			if(convertView==null){
				holder = new ViewHolder();
				
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.transfermakeup_item, null);
				holder.tv_name = (TextView)convertView.findViewById(R.id.tv_tm_item_name);
				holder.tv_lessonname = (TextView)convertView.findViewById(R.id.tv_tm_item_lesson);
				holder.tv_date = (TextView)convertView.findViewById(R.id.tv_tm_item_date);
				holder.tv_time = (TextView)convertView.findViewById(R.id.tv_tm_item_time);
				holder.tv_sitename = (TextView)convertView.findViewById(R.id.tv_tm_item_site);
				holder.tv_attendance = (TextView)convertView.findViewById(R.id.tv_tm_item_att);
				holder.btn_sitetype = (Button)convertView.findViewById(R.id.btn_tm_item_lt_os);
				holder.btn_select = (Button)convertView.findViewById(R.id.btn_tm_item_select);
				holder.lpw_sitelist = new ListPopupWindow(context.getApplicationContext());
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_name.setText(Html.fromHtml("<b>Name: </b>"+Name.get(position)));
			holder.tv_lessonname.setText(Html.fromHtml("<b>Lesson: </b>"+lessontype.get(position)));
			holder.tv_date.setText(Html.fromHtml("<b>Date: </b>"+date.get(position)));
			holder.tv_time.setText(Html.fromHtml("<b>Time: </b>"+time.get(position)));
			holder.tv_sitename.setText(Html.fromHtml("<b>Site: </b>"+siteName.get(position)));
			holder.tv_attendance.setText(Html.fromHtml("<b>Att: </b>"+attendancetype.get(position)));
			holder.btn_sitetype.setText("Other Site");
			holder.btn_sitetype.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View paramView) {
					// TODO Auto-generated method stub
					holder.lpw_sitelist.show();
				}
			});
			holder.lpw_sitelist.setAdapter(new ArrayAdapter<String>(
    	            context.getApplicationContext(),
    	            R.layout.edittextpopup,all_sitename ));
    		holder.lpw_sitelist.setAnchorView(holder.btn_sitetype);
    		holder.lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
    		holder.lpw_sitelist.setModal(true);
    		holder.lpw_sitelist.setOnItemClickListener(
                new OnItemClickListener() {

    				@Override
    				public void onItemClick(AdapterView<?> parent, View view,
    						int pos, long id) {
    					// TODO Auto-generated method stub
    					holder.btn_sitetype.setText(all_sitename.get(pos));
    					holder.lpw_sitelist.dismiss();
    				}
                });
    		
    		
    		holder.btn_select.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View paramView) {
					// TODO Auto-generated method stub
					Selected = tbid.get(position);
					siteid = all_siteid.get(all_sitename.indexOf(holder.btn_sitetype.getText().toString().trim()));
					sitename = holder.btn_sitetype.getText().toString().trim();
					new TransferSite().execute();
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
	
	private class TransferSite extends AsyncTask<Void, Void, Void>{
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
		protected Void doInBackground(Void... paramVarArgs) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("FamilyID",familyID );
			param.put("BasketID", AppConfiguration.BasketID);
			param.put("TBID",Selected);
			param.put("SiteID",siteid);
			param.put("SiteName",sitename);
			
			String responseString = WebServicesCall
			.RunScript(AppConfiguration.transfermakeup_site_transfer, param);
			
			try {
    			JSONObject reader = new JSONObject(responseString);
    			transferdone = reader.getString("Success");
    			if(transferdone.toString().equalsIgnoreCase("true")){
    				 JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
    				 JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
    				 msg = jsonChildNode.getString("showCart");
    			}
            }
            catch(JSONException e){
            	e.printStackTrace();
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
			if(transferdone.toString().equalsIgnoreCase("True")){
				Toast.makeText(context, "Lesson Transferred.", Toast.LENGTH_LONG).show();
				if(msg.toString().equalsIgnoreCase("True")){
					Intent i = new Intent(context,ViewCartActivity.class);
					context.startActivity(i);
				}
				else{
					((TransferMakeUpActivity)context).finish();
				}
				
			}
			else{
				Toast.makeText(context, "Not able to transfer lesson.", Toast.LENGTH_LONG).show();
			}
		}
	}
	String transferdone="False",Selected,sitename,siteid,msg;
}
