package com.waterworks.adapter;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.R;

public class ViewSchdeduleFragmentAdapter extends BaseAdapter {

	Context context;
	ValueAnimator mAnimator;
	LinearLayout ll_animator_layout;
	ArrayList<String> Date, Day, Time, Student, Age, Level, Instructor,
	Attendance, LessonType, Location, Count, Comment, ABBR, NewDate,
	NewTime;

	public ViewSchdeduleFragmentAdapter(Context context,
			ArrayList<String> date, ArrayList<String> day,
			ArrayList<String> time, ArrayList<String> student,
			ArrayList<String> age, ArrayList<String> level,
			ArrayList<String> instructor, ArrayList<String> attendance,
			ArrayList<String> lessonType, ArrayList<String> location,
			ArrayList<String> count, ArrayList<String> comment,
			ArrayList<String> abbr, ArrayList<String> newdate,
			ArrayList<String> newtime) {
		super();
		this.context = context;
		Date = date;
		Day = day;
		Time = time;
		Student = student;
		Age = age;
		Level = level;
		Instructor = instructor;
		Attendance = attendance;
		LessonType = lessonType;
		Location = location;
		Count = count;
		Comment = comment;
		ABBR = abbr;
		NewDate = newdate;
		NewTime = newtime;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Student.size();
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

	public class ViewHolder {
		TextView tv_student, tv_age, tv_inst, tv_lt, tv_comments,
		tv_level, tv_att, tv_location, tv_count, tv_day,tv_vs_att_for_cancel_item,comment;
		ImageButton show_detail, hide_detail;
		LinearLayout ll_details;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try {
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.view_schedule_item, null);
				holder.comment = (TextView) convertView
						.findViewById(R.id.comment);
				holder.tv_student = (TextView) convertView
						.findViewById(R.id.tv_vs_student_item);
				holder.tv_age = (TextView) convertView
						.findViewById(R.id.tv_vs_age_item);
				holder.tv_inst = (TextView) convertView
						.findViewById(R.id.tv_vs_inst_item);
				holder.tv_lt = (TextView) convertView
						.findViewById(R.id.tv_vs_lt_item);
				holder.tv_comments = (TextView) convertView
						.findViewById(R.id.tv_vs_comments_item);
				holder.tv_level = (TextView) convertView
						.findViewById(R.id.tv_vs_level_item);
				holder.tv_att = (TextView) convertView
						.findViewById(R.id.tv_vs_att_item);
				holder.tv_location = (TextView) convertView
						.findViewById(R.id.tv_vs_location_item);
				holder.tv_count = (TextView) convertView
						.findViewById(R.id.tv_vs_count_item);
				holder.tv_day = (TextView) convertView
						.findViewById(R.id.tv_vs_day_item);
				holder.show_detail = (ImageButton) convertView
						.findViewById(R.id.show_detail);
				holder.hide_detail = (ImageButton) convertView
						.findViewById(R.id.hide_detail);
				holder.ll_details = (LinearLayout) convertView
						.findViewById(R.id.ll_details);
				holder.ll_details.setVisibility(View.GONE);
				holder.tv_vs_att_for_cancel_item=(TextView)convertView
						.findViewById(R.id.tv_vs_att_for_cancel_item);
				holder.tv_vs_att_for_cancel_item.setVisibility(View.GONE);


				String student = Student.get(position).toString();
				if (student.length() >= 8) {
					holder.tv_student.setText(student.substring(0, 6));
				} else {
					holder.tv_student.setText(student);
				}

				if(Comment.get(position).toString().trim().length()>0){
					holder.comment.setText(Html.fromHtml(Comment.get(position).toString()));
				}
				holder.tv_inst.setText(Instructor.get(position));
				holder.tv_lt.setText(LessonType.get(position));
				holder.tv_age.setText(Age.get(position));
				String comments = Comment.get(position).toString();
				if(comments.contains("<br>")){
					comments.replace("<br>", "");
				}
				holder.tv_comments.setText(comments);
				holder.tv_level.setText(Level.get(position));
				String attendance = Attendance.get(position).toString();
				if (attendance.toString().equalsIgnoreCase("CWOP")
						|| attendance.toString().equalsIgnoreCase("CIS")
						|| attendance.toString().equalsIgnoreCase("CWP")
						|| attendance.toString().equalsIgnoreCase("CBW")
						|| attendance.toString().equalsIgnoreCase("FRZ")
						|| attendance.toString().equalsIgnoreCase("MU-CBW")
						|| attendance.toString().equalsIgnoreCase("MU-CWP")) {
					holder.tv_att.setTextColor(Color.RED);
					holder.tv_att.setGravity(Gravity.CENTER);
					holder.tv_att.setText(Html.fromHtml("<b>CANCELLED</b>"));
					holder.tv_vs_att_for_cancel_item.setVisibility(View.VISIBLE);
					holder.tv_vs_att_for_cancel_item.setText(Attendance.get(position));
				} else {
					holder.tv_att.setText(Attendance.get(position));
				}

				holder.tv_location.setText(Location.get(position));
				String count = Count.get(position).toString();
				if (count.toString().equalsIgnoreCase("Unpaid")) {
					holder.tv_count.setText(Html
							.fromHtml("<b>Unpaid</b>"));
				} else {
					holder.tv_count.setText(Count.get(position));
				}
				String day = Day.get(position).toString();
				if(day.equalsIgnoreCase("Monday")){
					holder.tv_day.setText("Mo "+NewDate.get(position)+" "+NewTime.get(position));
				}else if(day.equalsIgnoreCase("Tuesday")){
					holder.tv_day.setText("Tu "+NewDate.get(position)+" "+NewTime.get(position));
				}else if(day.equalsIgnoreCase("Wednesday")){
					holder.tv_day.setText("We "+NewDate.get(position)+" "+NewTime.get(position));
				}else if(day.equalsIgnoreCase("Thursday")){
					holder.tv_day.setText("Th "+NewDate.get(position)+" "+NewTime.get(position));
				}else if(day.equalsIgnoreCase("Friday")){
					holder.tv_day.setText("Fr "+NewDate.get(position)+" "+NewTime.get(position));
				}else if(day.equalsIgnoreCase("Saturday")){
					holder.tv_day.setText("Sa "+NewDate.get(position)+" "+NewTime.get(position));
				}else if(day.equalsIgnoreCase("Sunday")){
					holder.tv_day.setText("Su "+NewDate.get(position)+" "+NewTime.get(position));
				}
				holder.ll_details.getViewTreeObserver().addOnPreDrawListener(
						new ViewTreeObserver.OnPreDrawListener() {

							@Override
							public boolean onPreDraw() {
								holder.ll_details.getViewTreeObserver()
								.removeOnPreDrawListener(this);
								holder.ll_details.setVisibility(View.GONE);

								final int widthSpec = View.MeasureSpec
										.makeMeasureSpec(0,
												View.MeasureSpec.UNSPECIFIED);
								final int heightSpec = View.MeasureSpec
										.makeMeasureSpec(0,
												View.MeasureSpec.UNSPECIFIED);
								holder.ll_details
								.measure(widthSpec, heightSpec);

								mAnimator = slideAnimator(0,
										holder.ll_details.getMeasuredHeight());
								return true;
							}
						});
				holder.show_detail.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ll_animator_layout = holder.ll_details;
						holder.show_detail.setVisibility(View.GONE);
						holder.hide_detail.setVisibility(View.VISIBLE);
						if (holder.ll_details.getVisibility() == View.GONE) {
							expand();
						}
					}
				});
				holder.hide_detail.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ll_animator_layout = holder.ll_details;
						holder.hide_detail.setVisibility(View.GONE);
						holder.show_detail.setVisibility(View.VISIBLE);
						if (holder.ll_details.getVisibility() == View.VISIBLE) {
							collapse();
						}
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	protected void collapse() {
		// TODO Auto-generated method stub
		int finalHeight = ll_animator_layout.getHeight();

		ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

		mAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animator) {
				// Height=0, but it set visibility to GONE
				ll_animator_layout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		mAnimator.start();
	}

	protected void expand() {
		// TODO Auto-generated method stub
		ll_animator_layout.setVisibility(View.VISIBLE);

		mAnimator.start();
	}

	protected ValueAnimator slideAnimator(int start, int end) {

		ValueAnimator animator = ValueAnimator.ofInt(start, end);

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// Update Height
				int value = (Integer) valueAnimator.getAnimatedValue();

				ViewGroup.LayoutParams layoutParams = ll_animator_layout
						.getLayoutParams();
				layoutParams.height = value;
				ll_animator_layout.setLayoutParams(layoutParams);
			}
		});
		return animator;
	}
}
