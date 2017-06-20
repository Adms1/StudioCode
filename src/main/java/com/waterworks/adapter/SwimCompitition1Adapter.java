package com.waterworks.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.waterworks.FamilySwimNightActivity;
import com.waterworks.R;
import com.waterworks.SwimcompititionRegisterStep2Activity;

import java.util.ArrayList;
import java.util.HashMap;

public class SwimCompitition1Adapter extends BaseAdapter {

    public static ArrayList<String> date_list, date_list_id, all_siteid, datevalue, time, MeetDate_Display;
    ArrayList<HashMap<String, String>> meetDatesMainList;
    Activity context;
    String sitename;
    boolean[] itemChecked;
    FamilySwimNightActivity fm;


    public SwimCompitition1Adapter(Activity context, ArrayList<String> days_list, ArrayList<String> days_list_id, ArrayList<String> datevalue, ArrayList<String> time, ArrayList<String> MeetDate_Display) {
        super();
        this.context = context;
        this.date_list = days_list;
        this.date_list_id = days_list_id;
        this.sitename = sitename;
        this.datevalue = datevalue;
        this.time = time;
        this.MeetDate_Display = MeetDate_Display;

    }

    private class ViewHolder {
        //	  TextView apkName;
        CardView ck1;
        TextView t1, t2;
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
            holder.t1 = (TextView) convertView
                    .findViewById(R.id.textView1);
            holder.t2 = (TextView) convertView
                    .findViewById(R.id.textView2);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        holder.t1.setText(date_list.get(position) + "       " + date_list_id.get(position));
        holder.t1.setTextColor(Color.BLACK);
        holder.t2.setVisibility(View.INVISIBLE);
        holder.ck1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, SwimcompititionRegisterStep2Activity.class);
                i.putExtra("eventdates", date_list.get(position));
                i.putExtra("datevalue", datevalue.get(position));
                i.putExtra("time", time.get(position));
                i.putExtra("MeetDate_Display", MeetDate_Display.get(position));
                context.startActivity(i);
            }
        });

        return convertView;

    }

}
