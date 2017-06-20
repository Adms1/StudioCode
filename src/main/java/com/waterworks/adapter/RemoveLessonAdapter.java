package com.waterworks.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;

public class RemoveLessonAdapter extends BaseAdapter{

	public ArrayList<String> StudentName,wu_sttimehr,wu_sttimemin,wu_Day,
	wu_lessonname,InstructorName,RemoveClass, Wu_photo;
	public static ArrayList<String> RemoveClassList;
	public static ArrayList<String> ReleaseID,RemoveFrom,FormateTime;
	CheckBox checkBox_confirm;
	CardView btn_release;
	Context context;
	public static SparseBooleanArray mChecked;
	Boolean mChecked1;
	public static String time="";
	int DATE_DIALOG_ID = 0;

	public static int globalPosition,globalPosition1;
	public static ArrayList<String> mfddateList = null;
	String startday,startmonth,startyear;
    ImageLoader imageLoader;

	public static ArrayList<String> releaseClassList = new ArrayList<String>();
	public RemoveLessonAdapter(ArrayList<String> studentName,
							   ArrayList<String> wu_sttimehr, ArrayList<String> wu_sttimemin,
							   ArrayList<String> wu_Day, ArrayList<String> wu_lessonname,
							   ArrayList<String> instructorName, ArrayList<String> removeFrom,
							   ArrayList<String> releaseID,
							   ArrayList<String> removeClassList,
							   CheckBox checkBox_confirm,
							   CardView btn_release, ArrayList<String> FormateTime, Context context, ArrayList<String> Wu_photo) {
		super();
		StudentName = studentName;
		this.wu_sttimehr = wu_sttimehr;
		this.wu_sttimemin = wu_sttimemin;
		this.wu_Day = wu_Day;
		this.wu_lessonname = wu_lessonname;
		this.Wu_photo = Wu_photo;
		InstructorName = instructorName;
		RemoveFrom = removeFrom;
		ReleaseID = releaseID;
		RemoveClassList = removeClassList;
		this.checkBox_confirm = checkBox_confirm;
		this.btn_release = btn_release;
		RemoveLessonAdapter.FormateTime = FormateTime;
		this.context = context;

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.MAX_PRIORITY)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)// .enableLogging()
                .build();
        imageLoader.init(config.createDefault(context));
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ReleaseID.size();
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
		TextView tv_student,tv_lt,tv_inst,tv_st,tv_day, noteTitle;
		//TextView tv_date;
		LinearLayout llBelowLayout, llBelowLayoutInner;
		TextView st_name,day,date_time,inst_name,ls_type;
		CheckBox chb_check;
		Button end_date;
		ImageView date_selection;
		CircleImageView inst_DP;
		RelativeLayout rel_enddate;
        View remove_sc_ts,note_upper;
        TextView note,remove_sc;
        RelativeLayout main_back;


		int ref;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		try{
			if(convertView==null){
				holder = new ViewHolder();

				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.removelesson_item1, null);
                holder.main_back=(RelativeLayout)convertView.findViewById(R.id.main_back);
                holder.remove_sc=(TextView)convertView.findViewById(R.id.remove_sc);
                holder.remove_sc_ts= (View) convertView.findViewById(R.id.remove_sc_ts);

				//New Design
				holder.st_name = (TextView)convertView.findViewById(R.id.st_name);
				holder.day = (TextView)convertView.findViewById(R.id.day);
				holder.date_time = (TextView)convertView.findViewById(R.id.date_time);
				holder.inst_name = (TextView)convertView.findViewById(R.id.inst_name);
				holder.ls_type = (TextView)convertView.findViewById(R.id.ls_type);
				holder.date_selection = (ImageView)convertView.findViewById(R.id.date_selection);
				
				holder.tv_student = (TextView)convertView.findViewById(R.id.tv_rc_item_student);
				holder.tv_lt = (TextView)convertView.findViewById(R.id.tv_rc_item_lt);
				holder.tv_inst = (TextView)convertView.findViewById(R.id.tv_rc_item_instructor);
				holder.tv_st = (TextView)convertView.findViewById(R.id.tv_rc_item_st);
				holder.tv_day = (TextView)convertView.findViewById(R.id.tv_rc_item_day);
				holder.chb_check = (CheckBox)convertView.findViewById(R.id.chb_rc_item_check);
                holder.inst_DP = (CircleImageView)convertView.findViewById(R.id.inst_DP);
				holder.rel_enddate=(RelativeLayout)convertView.findViewById(R.id.rel_enddate);
				holder.end_date=(Button)convertView.findViewById(R.id.end_date);
                holder.note_upper = (View) convertView.findViewById(R.id.note_upper);
				holder.llBelowLayout = (LinearLayout) convertView.findViewById(R.id.llBelowLayout);
				holder.noteTitle = (TextView) convertView.findViewById(R.id.noteTitle);
                holder.llBelowLayoutInner = (LinearLayout) convertView.findViewById(R.id.llBelowLayoutInner);
                holder.note = (TextView)convertView. findViewById(R.id.note);
				convertView.setTag(holder);

				if (Wu_photo.get(position).toString().trim().length() > 0) {

					imageLoader.displayImage(Wu_photo.get(position), holder.inst_DP);
				}

				mfddateList = new ArrayList<String>();
				for(int i=0;i<ReleaseID.size();i++){
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

				holder.end_date.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						globalPosition = position;
						((Activity)RemoveLessonAdapter.this.context).showDialog(DATE_DIALOG_ID);
					}
				});
				//New Design
				holder.st_name.setText(StudentName.get(position));
				String day_new = wu_Day.get(position);

				holder.day.setText(day_new);
				holder.inst_name.setText(InstructorName.get(position));
				holder.ls_type.setText(wu_lessonname.get(position));
				holder.end_date.setText(RemoveLessonAdapter.RemoveFrom.get(position));

				holder.tv_student.setText(Html.fromHtml("<b>Student: </b>" + StudentName.get(position)));
				holder.tv_lt.setText(Html.fromHtml("<b>Lesson Type: </b>" + wu_lessonname.get(position)));
				holder.tv_inst.setText(Html.fromHtml("<b>Instructor: </b>" + InstructorName.get(position)));
				holder.tv_day.setText(Html.fromHtml("<b>Day: </b>" + wu_Day.get(position)));
								holder.tv_st.setText(FormateTime.get(position));
				time=FormateTime.get(position).toString();

				holder.chb_check.setTag(RemoveLessonAdapter.ReleaseID.get(position));
                holder.ref = position;
				convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                    }
                });
				holder.chb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						globalPosition1 = position;
						if (isChecked) {
							mChecked.put(position, isChecked);
							mChecked1 = true;

							Log.i("Value if", "" + mChecked);
							System.out.println("Position Clicked else : " + position);
							holder.chb_check.setTag(RemoveLessonAdapter.ReleaseID.get(position));
                            holder.chb_check.setChecked(true);
							holder.chb_check.setButtonDrawable(R.drawable.custom_check);

							System.out.println("Position Clicked : " + position);

							RemoveClassList.set(position, "True");
							Log.e("checkedData", RemoveClassList.toString());

							Log.i("if Value", "" + mChecked1);
							Log.i("if Value", "" + mChecked1);
							Log.e("checkedData", RemoveClassList.toString());
							TransitionDrawable transition = (TransitionDrawable) holder.main_back.getBackground();
							transition.startTransition(500);

                            final Animation sliddown = AnimationUtils.loadAnimation(context, android.support.v7.appcompat.R.anim.abc_slide_in_top);
							holder.llBelowLayout.startAnimation(new ExpandCollapseAnimation(holder.llBelowLayout, 500, 0, (Activity) RemoveLessonAdapter.this.context));
							Handler hand = new Handler();
							hand.postDelayed(new Runnable() {
								@Override
								public void run() {
                                    holder.llBelowLayoutInner.setVisibility(View.VISIBLE);
                                    holder.llBelowLayoutInner.startAnimation(sliddown);

								}
							}, 500);

						} else {
                            mChecked.delete(position);
                            System.out.println("Position Clicked else : " + position);
							holder.chb_check.setChecked(false);

                            Log.i("else Value1", "" + mChecked1);
                            RemoveClassList.set(position, "False");
                            Log.e("checkedData", RemoveClassList.toString());
							TransitionDrawable transition = (TransitionDrawable) holder.main_back.getBackground();
							transition.reverseTransition(500);

                            holder.llBelowLayout.startAnimation(new ExpandCollapseAnimation(holder.llBelowLayout, 250, 1, (Activity) RemoveLessonAdapter.this.context));
                            holder.llBelowLayoutInner.setVisibility(View.INVISIBLE);
                       						}

					}
				});
				holder.date_selection.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						globalPosition = position;
						((Activity)RemoveLessonAdapter.this.context).showDialog(DATE_DIALOG_ID);
						notifyDataSetChanged();
						holder.chb_check.setText(RemoveLessonAdapter.ReleaseID.get(position));

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



}
