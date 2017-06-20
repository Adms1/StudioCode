package com.waterworks.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.waterworks.CancelLessonFragment;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CancelLessonFragmentAdapter extends BaseAdapter {

    Context context;
    String data_load = "False";
    String token, familyID, cancelId;
    String message;
    SparseBooleanArray mChecked;
    ArrayList<String> Date, Day, Time, Student, Instructor, Attendance,
            LessonType, Count, Comment, NewDate, NewTime, HiddenFeildID, HiddenFeildValue, CancelID, CancelEnable, Abbr, Wu_Photo, MissingClass, temp, Wu_Lessoncount;
    public static ArrayList<String> RemoveClassList;
    String currentDate = "";
    ImageLoader imageLoader;
    boolean check=false;
    CancelLessonFragmentAdapter thadapter = null;
    View tempView;

    public CancelLessonFragmentAdapter(Context context, ArrayList<String> date,
                                       ArrayList<String> day, ArrayList<String> time,
                                       ArrayList<String> student, ArrayList<String> instructor,
                                       ArrayList<String> attendance, ArrayList<String> lessonType,
                                       ArrayList<String> count, ArrayList<String> comment,
                                       ArrayList<String> newDate, ArrayList<String> newTime,
                                       ArrayList<String> hiddenFeildID,
                                       ArrayList<String> hiddenFeildValue, ArrayList<String> cancelID,
                                       ArrayList<String> cancelEnable, ArrayList<String> abbr,
                                       ArrayList<String> removeClassList,
                                       ArrayList<String> Wu_photo,
                                       ArrayList<String> MissingClass,
                                       ArrayList<String> wu_lessoncount) {
        super();
        this.context = context;
        Date = date;
        Day = day;
        Time = time;
        Student = student;
        Instructor = instructor;
        Attendance = attendance;
        LessonType = lessonType;
        Count = count;
        Comment = comment;
        NewDate = newDate;
        NewTime = newTime;
        HiddenFeildID = hiddenFeildID;
        HiddenFeildValue = hiddenFeildValue;
        CancelID = cancelID;
        CancelEnable = cancelEnable;
        this.MissingClass = MissingClass;
        Abbr = abbr;
        Wu_Photo = Wu_photo;
        RemoveClassList = removeClassList;
        Wu_Lessoncount = wu_lessoncount;

        SharedPreferences prefs = AppConfiguration.getSharedPrefs(context.getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d("Cancel A lesson Adapter", "Token=" + token + "\nFamilyID=" + familyID);


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
                .tasksProcessingOrder(QueueProcessingType.FIFO)// .enableLogging()
                .build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
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
        TextView tv_student, tv_inst, tv_lt, tv_date_time, tv_comments, tv_att, tv_count, tv_day;
        TextView st_name, day, date_time, inst_name, ls_type;
        Button btn_cancel_a_lesson;
        CheckBox chb_rc_item_check;
        //Design change 2
        TextView date_d2, time_about_d2, lesson_type_d2, inst_name_d2, month_tv, day_tv, cancelled_txt;
        CircleImageView inst_DP;
        CheckBox check_d2, cancelled_d2;
        View firstView, lastview;//, vw_shadow;
        RelativeLayout main_back, disable_layout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
       final ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.cancelalesson_item_new_d2, null);

                //Design change 2
                holder.date_d2 = (TextView) convertView.findViewById(R.id.date_d2);
                holder.time_about_d2 = (TextView) convertView.findViewById(R.id.time_about_d2);
                holder.cancelled_txt = (TextView) convertView.findViewById(R.id.cancelled_txt);
                holder.lesson_type_d2 = (TextView) convertView.findViewById(R.id.lesson_type_d2);
                holder.inst_name_d2 = (TextView) convertView.findViewById(R.id.inst_name_d2);
                holder.inst_DP = (CircleImageView) convertView.findViewById(R.id.inst_DP);
                holder.check_d2 = (CheckBox) convertView.findViewById(R.id.check_d2);
                holder.cancelled_d2 = (CheckBox) convertView.findViewById(R.id.cancelled_d2);
                holder.firstView = (View) convertView.findViewById(R.id.firstView);
                holder.lastview = (View) convertView.findViewById(R.id.lastview);
                holder.main_back = (RelativeLayout) convertView.findViewById(R.id.main_back);
                holder.disable_layout = (RelativeLayout) convertView.findViewById(R.id.disable_layout);

                //New Design
                holder.st_name = (TextView) convertView.findViewById(R.id.st_name);
                holder.day = (TextView) convertView.findViewById(R.id.day);
                holder.date_time = (TextView) convertView.findViewById(R.id.date_time);
                holder.inst_name = (TextView) convertView.findViewById(R.id.inst_name);
                holder.ls_type = (TextView) convertView.findViewById(R.id.ls_type);
                holder.month_tv = (TextView) convertView.findViewById(R.id.month_tv);
                holder.day_tv = (TextView) convertView.findViewById(R.id.day_tv);

                holder.tv_student = (TextView) convertView.findViewById(R.id.tv_cln_student_item);
                holder.tv_inst = (TextView) convertView.findViewById(R.id.tv_cln_inst_item);
                holder.tv_lt = (TextView) convertView.findViewById(R.id.tv_cln_lt_item);
                holder.tv_date_time = (TextView) convertView.findViewById(R.id.tv_cln_date_time_item);
                holder.tv_comments = (TextView) convertView.findViewById(R.id.tv_cln_comments_item);
                holder.tv_att = (TextView) convertView.findViewById(R.id.tv_cln_att_item);
                holder.tv_count = (TextView) convertView.findViewById(R.id.tv_cln_count_item);
                holder.tv_day = (TextView) convertView.findViewById(R.id.tv_cln_day_item);
                String time[] = Time.get(position).toString().split("\\W");
                String date_split[] = NewDate.get(position).toString().split("\\/");

                //Design change 2

                final Typeface face = Typeface.createFromAsset(context.getAssets(),
                        "Roboto_Light.ttf");
                Typeface regular = Typeface.createFromAsset(context.getAssets(),
                        "Roboto_Black.ttf");

                String day = date2Day(NewDate.get(position).toString());
                String month = date2Month(Integer.parseInt(date_split[0]));

                if (currentDate.equals(NewDate.get(position))) {
                    holder.firstView.setVisibility(View.GONE);
                    holder.date_d2.setVisibility(View.GONE);
                    holder.month_tv.setVisibility(View.GONE);
                    holder.day_tv.setVisibility(View.GONE);
                } else {
                    currentDate = NewDate.get(position);
                    holder.firstView.setVisibility(View.VISIBLE);
                    holder.date_d2.setVisibility(View.VISIBLE);
                    holder.month_tv.setVisibility(View.VISIBLE);
                    holder.day_tv.setVisibility(View.VISIBLE);
                }

                if (position == (Student.size() - 1)) {
                    holder.lastview.setVisibility(View.INVISIBLE);
                } else {
                    holder.lastview.setVisibility(View.GONE);
                }
                holder.date_d2.setTypeface(face);
                holder.time_about_d2.setTypeface(regular);
                holder.inst_name_d2.setTypeface(face);
                holder.lesson_type_d2.setTypeface(face);
                holder.month_tv.setTypeface(face);
                holder.day_tv.setTypeface(face);

                if (day.contains("day")) {
                    day = day.substring(0, 3);
                }

                holder.month_tv.setText(month);

                holder.day_tv.setText(day);

                holder.date_d2.setText(date_split[1]);

                if (Attendance.get(position).toString() != null) {
                    if (Attendance.get(position).toString().equalsIgnoreCase("CWOP")
                            || Attendance.get(position).toString().equalsIgnoreCase("CBW")
                            || Attendance.get(position).toString().contains("CWP")
                            || Attendance.get(position).toString().contains("CIS")
                            || Attendance.get(position).toString().equalsIgnoreCase("FRZ")) {

                        holder.cancelled_d2.setVisibility(View.GONE);
                        holder.cancelled_d2.setEnabled(false);
                        holder.cancelled_txt.setVisibility(View.VISIBLE);

                        holder.disable_layout.setVisibility(View.VISIBLE);
                        holder.lesson_type_d2.setText(Wu_Lessoncount.get(position));

                        if (Wu_Lessoncount.get(position).equalsIgnoreCase("") && MissingClass.get(position).equalsIgnoreCase("1")) {
                            holder.lesson_type_d2.setTypeface(null, Typeface.BOLD);
                            holder.lesson_type_d2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            holder.lesson_type_d2.setText(Html.fromHtml("<font color=\"red\">No Lesson Today</font>"));
                            holder.check_d2.setVisibility(View.INVISIBLE);
                        }

                        if (MissingClass.get(position).equalsIgnoreCase("1")) {
                            holder.time_about_d2.setText(Student.get(position));
                        } else {
                            holder.time_about_d2.setText(time[0] + ":" + time[1] + " " + time[3] + " - " + Student.get(position));
                        }
                    } else {
//Unpaid Layout
                        if (Wu_Lessoncount.get(position).contains("Lesson 0 of 0")) {
                            if (Comment.get(position).contains("Makeup Used")) {
                                holder.lesson_type_d2.setText("Makeup Lesson - " + LessonType.get(position));
                            } else {
                                holder.lesson_type_d2.setText("UNPAID - " + LessonType.get(position));
                            }
                            holder.check_d2.setVisibility(View.INVISIBLE);

                        } else if (Wu_Lessoncount.get(position).equalsIgnoreCase("")) {
                            holder.lesson_type_d2.setTypeface(null, Typeface.BOLD);
                            holder.lesson_type_d2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            holder.lesson_type_d2.setText(Html.fromHtml("<font color=\"red\">No Lesson Today</font>"));
                            holder.check_d2.setVisibility(View.INVISIBLE);

                        } else {
                            holder.lesson_type_d2.setText(Wu_Lessoncount.get(position));

                        }
                        if (MissingClass.get(position).equalsIgnoreCase("1")) {
                            holder.time_about_d2.setText(Student.get(position));
                        } else {
                            holder.time_about_d2.setText(time[0] + ":" + time[1] + " " + time[3] + " - " + Student.get(position));
                        }
                    }
                } else {
                    if (Wu_Lessoncount.get(position).contains("Lesson 0 of 0")) {
                        if (Comment.get(position).contains("Makeup Used")) {
                            holder.lesson_type_d2.setText("Makeup Lesson - " + LessonType.get(position));
                        } else {
                            holder.lesson_type_d2.setText("UNPAID - " + LessonType.get(position));
                        }
                        holder.check_d2.setVisibility(View.INVISIBLE);

                    } else if (Wu_Lessoncount.get(position).equalsIgnoreCase("")) {
                        holder.lesson_type_d2.setTypeface(null, Typeface.BOLD);
                        holder.lesson_type_d2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        holder.lesson_type_d2.setText(Html.fromHtml("<font color=\"red\">No Lesson Today</font>"));
                        holder.check_d2.setVisibility(View.INVISIBLE);
                    } else {
                        holder.lesson_type_d2.setText(Wu_Lessoncount.get(position));

                    }
                    if (MissingClass.get(position).equalsIgnoreCase("1")) {
                        holder.time_about_d2.setText(Student.get(position));
                    } else {
                        holder.time_about_d2.setText(time[0] + ":" + time[1] + " " + time[3] + " - " + Student.get(position));
                    }

                }
                String instName = Instructor.get(position);

                instName = instName.toString().trim();

                holder.inst_name_d2.setText(instName);

                //New Design
                holder.st_name.setText(Student.get(position));
                String day_new = Day.get(position);
                day_new = day_new.replace("day", "");
                day_new = day_new.substring(0, 3);
                holder.day.setText(day_new);

                holder.date_time.setText(NewDate.get(position) + "," + time[0] + ":" + time[1] + " " + time[3]);

                holder.inst_name.setText(Instructor.get(position));
                holder.ls_type.setText(LessonType.get(position));
                if (Wu_Photo.get(position).toString().trim().length() > 0) {

                    imageLoader.displayImage(Wu_Photo.get(position), holder.inst_DP);
                }
                holder.tv_date_time.setText(Html.fromHtml("<b>Date:</b>" + NewDate.get(position) + "\n" + time[0] + ":" + time[1] + " " + time[3]));
                holder.tv_student.setText(Html.fromHtml("<b>Student:</b> " + Student.get(position)));
                holder.tv_inst.setText(Html.fromHtml("<b>Instructor:</b> " + Instructor.get(position)));
                holder.tv_lt.setText(Html.fromHtml("<b>Lesson Type:</b> " + LessonType.get(position)));
                holder.tv_comments.setText(Html.fromHtml("<b>Comments:</b> " + Comment.get(position)));
                holder.tv_att.setText(Html.fromHtml("<b>Attendance:</b> " + Attendance.get(position)));
                holder.tv_count.setText(Html.fromHtml("<b>Count:</b> " + Count.get(position)));
                holder.tv_day.setText(Html.fromHtml("<b>Day:</b> " + Day.get(position)));
                holder.btn_cancel_a_lesson = (Button) convertView.findViewById(R.id.btn_cancel_a_lesson);
                holder.chb_rc_item_check = (CheckBox) convertView.findViewById(R.id.chb_rc_item_check);

                //             ListView    animation
                try {

                    Animation animSlidup = AnimationUtils.loadAnimation(context, R.anim.slidup);
                    convertView.startAnimation(animSlidup);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mChecked = new SparseBooleanArray();
                if (CancelEnable.get(position).toString().equalsIgnoreCase("1")) {
                    holder.check_d2.setVisibility(View.VISIBLE);
                } else if (CancelEnable.get(position).toString().equalsIgnoreCase("0")) {
                    holder.check_d2.setVisibility(View.INVISIBLE);
                } else {
                    holder.check_d2.setVisibility(View.INVISIBLE);
                }
                holder.check_d2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (isChecked) {
                            mChecked.put(position, isChecked);
                            Log.i("Value", "" + mChecked);
                            RemoveClassList.set(position, "True");
                            Log.e("checkedDatammmmmm", RemoveClassList.toString());

                            TransitionDrawable transition = (TransitionDrawable) holder.main_back.getBackground();
                            transition.startTransition(500);

//                            new code 27/01/2017 megha
                            if (HiddenFeildValue.get(position).equalsIgnoreCase("2")) {
                                AlertDialog alertDialog = new AlertDialog.Builder(buttonView.getContext()).create();
                                alertDialog.setTitle("Cancel lesson");
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                alertDialog.setMessage("You must cancel your class before 2 hours of your class time in order to receive a Make-Up.  We are unable to award you a Make-Up for this cancellation.  Would you like to still cancel the class?");
                                alertDialog.setButton("Ok",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                alertDialog.setButton2("Cancel",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated method stub
                                                dialog.cancel();
                                                holder.check_d2.setChecked(false);
                                            }
                                        });
                                alertDialog.show();
                            } else if (HiddenFeildValue.get(position).equalsIgnoreCase("3")) {
                                AlertDialog alertDialog = new AlertDialog.Builder(buttonView.getContext()).create();
                                alertDialog.setTitle("Cancel lesson");
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                alertDialog.setMessage("You must cancel your class at least 2 hours before the start time in order to receive a Make-Up.  We are unable to award you a Make-Up for this cancellation.  Would you like to still cancel the class?");
                                alertDialog.setButton("Ok",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // close dialog
                                                dialog.cancel();
                                            }
                                        });
                                alertDialog.setButton2("Cancel",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated method stub
                                                dialog.cancel();
                                                holder.check_d2.setChecked(false);
                                            }
                                        });
                                alertDialog.show();
                            } else if (HiddenFeildValue.get(position).equalsIgnoreCase("4")) {
                                AlertDialog alertDialog = new AlertDialog.Builder(buttonView.getContext()).create();
                                alertDialog.setTitle("Cancel lesson(No Make-up)"); //Add Navin 02-02-2017
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                check=true;
                                alertDialog.setMessage(Html.fromHtml(" <font color='#FF0000'>We are unable to issue a makeup lesson </font> "+"for cancelling this class since it begins within 2 hrs from now."));
                                alertDialog.setButton("CANCEL LESSON",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // close dialog
                                                dialog.cancel();
                                                holder.btn_cancel_a_lesson.performClick();
                                                holder.btn_cancel_a_lesson.setPressed(true);
                                            }
                                        });
                                alertDialog.setButton2("DO NOT CANCEL",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated method stub
                                                dialog.cancel();
                                                holder.check_d2.setChecked(false);
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                        else {
                            mChecked.delete(position);
                            Log.i("Value1", "" + mChecked);
                            RemoveClassList.set(position, "False");
                            Log.e("checkedDataeeeeeee", RemoveClassList.toString());
                            TransitionDrawable transition = (TransitionDrawable) holder.main_back.getBackground();
                            transition.reverseTransition(500);
                        }
                        temp = new ArrayList<String>();
                        for (int i = 0; i < CancelLessonFragmentAdapter.RemoveClassList.size(); i++) {
                            if (CancelLessonFragmentAdapter.RemoveClassList.get(i).equals("True")) {
                                temp.add(CancelID.get(i).trim());
                            } else {
                            }
                        }
                        if (temp.size() > 0) {
                            if (temp.size() == 1) {
                                TransitionDrawable transition = (TransitionDrawable) CancelLessonFragment.btn_cancel_lsn.getBackground();
                                transition.startTransition(500);
                            }
                        } else {
                            TransitionDrawable transition = (TransitionDrawable) CancelLessonFragment.btn_cancel_lsn.getBackground();
                            transition.reverseTransition(500);
                        }
                    }
                });
                holder.chb_rc_item_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            mChecked.put(position, isChecked);
                            Log.i("Value", "" + mChecked);
                            RemoveClassList.set(position, "True");
                            Log.e("checkedDatarrrrrrrr", RemoveClassList.toString());
                        } else {
                            mChecked.delete(position);
                            Log.i("Value1", "" + mChecked);
                            RemoveClassList.set(position, "False");
                            Log.e("checkedData", RemoveClassList.toString());
                        }
                    }
                });
                holder.btn_cancel_a_lesson.setOnClickListener(new OnClickListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tempView = v;
                        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                        alertDialog.setTitle("WaterWorks");
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        if (check=true){
                            cancelId = CancelID.get(position)+"|"+"4";
                            new CancelClass().execute();
                            holder.cancelled_d2.setVisibility(View.GONE);
                            holder.cancelled_d2.setEnabled(false);
                            holder.cancelled_txt.setVisibility(View.VISIBLE);
                            holder.cancelled_txt.setVisibility(View.VISIBLE);
                            holder.disable_layout.setVisibility(View.VISIBLE);
                            holder.check_d2.setChecked(false);
                            holder.check_d2.setVisibility(View.INVISIBLE);
                        }
                        else{
                        alertDialog.setMessage("Are you sure you want to cancel the selected lesson?");
                        alertDialog.setButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // close dialog
                                        dialog.cancel();
                                        cancelId = CancelID.get(position);
                                        new CancelClass().execute();
                                    }
                                });
                        alertDialog.setButton2("No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();
                    }}
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
    private class CancelClass extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(tempView.getContext());
            pd.setMessage(tempView.getContext().getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", familyID);
            param.put("CancelClass", cancelId);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.cancelClass, param);
            readAndParseJSON(responseString);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (data_load.toString().equals("True")) {
                Toast.makeText(tempView.getContext(), "" + message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(tempView.getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            data_load = reader.getString("Success");
            if (data_load.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                message = jsonChildNode.getString("Msg");
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                message = jsonChildNode.getString("Msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String date2Day(String input) {
        String goal = "";
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("MM/dd/yy");
            Date date = inFormat.parse(input);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            goal = outFormat.format(date);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return goal;
    }
    public String date2Month(int number) {
        String month = "";

        if (number == 1) {
            month = "Jan";
        } else if (number == 2) {
            month = "Feb";
        } else if (number == 3) {
            month = "Mar";
        } else if (number == 4) {
            month = "Apr";
        } else if (number == 5) {
            month = "May";
        } else if (number == 6) {
            month = "Jun";
        } else if (number == 7) {
            month = "Jul";
        } else if (number == 8) {
            month = "Aug";
        } else if (number == 9) {
            month = "Sep";
        } else if (number == 10) {
            month = "Oct";
        } else if (number == 11) {
            month = "Nov";
        } else if (number == 12) {
            month = "Dec";
        }
        return month;
    }
}
