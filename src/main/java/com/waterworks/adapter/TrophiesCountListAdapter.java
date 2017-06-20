package com.waterworks.adapter;

import android.app.Activity;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.HashMap;

public class TrophiesCountListAdapter extends BaseAdapter{

	private ArrayList<HashMap<String, String>> trophiesCount;
	Activity context;

	public TrophiesCountListAdapter(Activity context, ArrayList<HashMap<String, String>> trophiesCount) {
		super();
		this.context = context;
		this.trophiesCount = trophiesCount;
	}

	private class ViewHolder {
		TextView txtTrophyCount, txtTrophyStatus;
	}

	public int getCount() {
		return trophiesCount.size();
	}

	public Object getItem(int position) {
		return trophiesCount.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_lst_trophy_count_swimcomp_trophies, null);
			holder = new ViewHolder();
			holder.txtTrophyCount = (TextView) convertView.findViewById(R.id.txtTrophyCount);
			holder.txtTrophyStatus = (TextView) convertView.findViewById(R.id.txtTrophyStatus);

			holder.txtTrophyCount.setText(trophiesCount.get(position).get("TrophySize") +" Ribbon Trophy");
			if(trophiesCount.get(position).get("TrophyStatus").equals("Earned")){
				holder.txtTrophyStatus.setText(trophiesCount.get(position).get("TrophyStatus")+"!");
				holder.txtTrophyStatus.setTextColor(Color.GREEN);
			}else {
				holder.txtTrophyStatus.setText(trophiesCount.get(position).get("TrophyStatus")+" to go!");
				holder.txtTrophyStatus.setTextColor(Color.LTGRAY);
			}

		}
		return convertView;
	}
}
