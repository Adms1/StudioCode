package com.waterworks.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.CancelRemoveLessonActivity;
import com.waterworks.R;

public class RemoveLessonAdapterInfo extends BaseAdapter{

	public ArrayList<String> StudentName,wu_sttimehr,wu_sttimemin,wu_Day,
	wu_lessonname,InstructorName,RemoveClass,RemoveClassList;
	public static ArrayList<String> FormateTime,RemoveFrom,ReleaseID;
	CheckBox checkBox_confirm;
	Button btn_release;
	Context context;
	SparseBooleanArray mChecked;
	ViewHolder holder;
	int DATE_DIALOG_ID = 0;
	public static int globalPosition;
	public static ArrayList<String> mfddateList = null;
	String startday,startmonth,startyear;
	RemoveLessonAdapter adapt;

	public static ArrayList<String> releaseClassList = new ArrayList<String>();

	//
	public RemoveLessonAdapterInfo(ArrayList<String> studentName,
			ArrayList<String> wu_sttimehr, ArrayList<String> wu_sttimemin,
			ArrayList<String> wu_Day, ArrayList<String> wu_lessonname,
			ArrayList<String> instructorName, ArrayList<String> removeFrom,
			ArrayList<String> releaseID, 
			ArrayList<String> removeClassList,
			CheckBox checkBox_confirm,
			Button btn_release, ArrayList<String> FormateTime,Context context) {
		super();
		StudentName = studentName;
		this.wu_sttimehr = wu_sttimehr;
		this.wu_sttimemin = wu_sttimemin;
		this.wu_Day = wu_Day;
		this.wu_lessonname = wu_lessonname;
		InstructorName = instructorName;
		RemoveFrom = removeFrom;
		ReleaseID = releaseID;
		RemoveClassList = removeClassList;
		this.checkBox_confirm = checkBox_confirm;
		this.btn_release = btn_release;
		RemoveLessonAdapterInfo.FormateTime = FormateTime;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return InstructorName.size();
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
		TextView tv_student,tv_lt,tv_inst,tv_st,tv_day;
		TextView tv_date,tv_date_new;
		CheckBox chb_check;
		int ref;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try{
			if(convertView==null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.removelesson_item_new_info, null);
				holder.tv_student = (TextView)convertView.findViewById(R.id.tv_rc_item_student);
				holder.tv_lt = (TextView)convertView.findViewById(R.id.tv_rc_item_lt);
				holder.tv_inst = (TextView)convertView.findViewById(R.id.tv_rc_item_instructor);

				holder.tv_st = (TextView)convertView.findViewById(R.id.tv_rc_item_st);
				holder.tv_day = (TextView)convertView.findViewById(R.id.tv_rc_item_day);
				holder.tv_date = (TextView)convertView.findViewById(R.id.tv_rc_item_date);
				holder.tv_date_new = (TextView)convertView.findViewById(R.id.tv_rc_item_date_new);
				holder.tv_date_new.setVisibility(View.VISIBLE);
				holder.chb_check = (CheckBox)convertView.findViewById(R.id.chb_rc_item_check);

				convertView.setTag(holder);

				mfddateList = new ArrayList<String>();
				for(int i=0;i<InstructorName.size();i++){
					mfddateList.add("");
				}
				mChecked = new SparseBooleanArray();
				RemoveClass = new ArrayList<String>();
				String hr,min;
				hr = wu_sttimehr.get(position);
				min = wu_sttimemin.get(position);

				if(hr.length()==1){
					hr = "0"+hr;
				}
				if(min.length()==1){
					min = min + "0";
				}


				holder.tv_date_new.setText(RemoveLessonAdapterInfo.RemoveFrom.get(position));
                holder.tv_student.setText(StudentName.get(position));
                holder.tv_lt.setText(wu_lessonname.get(position));
				holder.tv_day.setText(wu_Day.get(position));
                holder.tv_inst.setText(InstructorName.get(position));

				String strtime=FormateTime.get(position);
				String str=strtime.substring(0, 5) + "" + strtime.substring(5 + 3);

				holder.tv_st.setText(str);

				holder.ref = position;
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



}
