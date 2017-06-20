package com.waterworks.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.waterworks.R;
import com.waterworks.model.menuoptionItem;

public class menuoptionItemAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<menuoptionItem> menuOptionItems;
	
	public menuoptionItemAdapter(Context context, ArrayList<menuoptionItem> menuOptionItems){
		this.context = context;
		this.menuOptionItems = menuOptionItems;
	}
	@Override
	public int getCount() {
		return menuOptionItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return menuOptionItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_drawer_item, null);
        }
         
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(menuOptionItems.get(position).getName());
        return convertView;
	}

}
