package com.waterworks.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
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

import com.waterworks.DashBoardActivity;
import com.waterworks.FamilySwimNightActivity;
import com.waterworks.FamilySwimNightActivity_Register;
import com.waterworks.PastDueBalActivity;
import com.waterworks.R;
import com.waterworks.SwimTeamActivity;
import com.waterworks.ViewCartActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class ListadapterFamilySwimNightAdapter extends BaseAdapter{

	public static ArrayList<String>  date_list,date_list_id,all_siteid,all_sitename,theme;
	Activity context;
	boolean[] itemChecked;
	FamilySwimNightActivity fm;

	public ListadapterFamilySwimNightAdapter(Activity context, ArrayList<String> days_list, ArrayList<String> days_list_id, ArrayList<String> all_siteid, ArrayList<String> all_sitename,ArrayList<String> theme) {
		super();
		this.context = context;
		this.date_list = days_list;
		this.date_list_id=days_list_id;
		this.all_siteid=all_siteid;
		this.all_sitename=all_sitename;
		this.theme=theme;
	}
	private class ViewHolder {
		//	  TextView apkName;
		CardView ck1;
		TextView t1,t2;
	}
	public int getCount() {
		return date_list.size();
	}

	public Object getItem(int position) {
		return date_list.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View v;
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_buttonview, null);
			holder = new ViewHolder();

			holder.ck1 = (CardView) convertView
					.findViewById(R.id.text);
			holder.t1=(TextView)convertView
					.findViewById(R.id.textView1);
			holder.t2=(TextView)convertView
					.findViewById(R.id.textView2);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.t1.setText(date_list.get(position));
		holder.t2.setText(theme.get(position));

		holder.ck1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context,FamilySwimNightActivity_Register.class);
				i.putExtra("selectdateid",date_list_id.get(position));
				i.putExtra("selectdate",holder.t1.getText());
				i.putExtra("siteid",date_list_id.get(position).split("|")[1]);
				i.putExtra("sitename",all_sitename.get(position));
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}
		});
		return convertView;
	}
}
