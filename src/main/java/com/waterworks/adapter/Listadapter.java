package com.waterworks.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.SwimTeamActivity;
import com.waterworks.model.Days;

public class Listadapter extends BaseAdapter{

	ArrayList<Days>  days_list;
	Activity context;
	boolean[] itemChecked;

	public Listadapter(Activity context, ArrayList <Days> days_list) {
		super();
		this.context = context;
		this.days_list = days_list;
		itemChecked = new boolean[days_list.size()];
	}

	private class ViewHolder {
		TextView txtText;
		CheckBox ck1;
	}

	public int getCount() {
		return days_list.size();
	}

	public Object getItem(int position) {
		return days_list.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_checklist, null);
			holder = new ViewHolder();

			//	   holder.apkName = (TextView) convertView
			//	     .findViewById(R.id.textView1);
			holder.ck1 = (CheckBox) convertView.findViewById(R.id.day);
			holder.txtText = (TextView) convertView.findViewById(R.id.txtText);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtText.setText(days_list.get(position).getDayName());
		holder.ck1.setChecked(false);

		if (itemChecked[position])
			holder.ck1.setChecked(true);
		else
			holder.ck1.setChecked(false);

		holder.ck1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (holder.ck1.isChecked())
					itemChecked[position] = true;
				else
					itemChecked[position] = false;
			}
		});

		holder.ck1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				((SwimTeamActivity) context).tblRowChooseDays.setBackgroundResource(R.drawable.pure_error_border_white);
				if (isChecked) {
					SwimTeamActivity.globalInc++;
					System.out.println("Incr" + SwimTeamActivity.globalInc + SwimTeamActivity.hwMnySelct);
				} else {
					SwimTeamActivity.globalInc--;
                    System.out.println("Decr" + SwimTeamActivity.globalInc + SwimTeamActivity.hwMnySelct);
				}
				if (SwimTeamActivity.globalInc <= SwimTeamActivity.hwMnySelct) {
					System.out.println(SwimTeamActivity.globalInc + SwimTeamActivity.hwMnySelct);
				} else {
					SwimTeamActivity.globalInc--;
					buttonView.setChecked(false);
					Toast.makeText(context, "Limit Exceeded", Toast.LENGTH_SHORT).show();
				}

			}
		});
		return convertView;

	}

}
