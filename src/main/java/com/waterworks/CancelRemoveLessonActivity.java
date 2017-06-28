package com.waterworks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.CancelLessonFragmentAdapter;
import com.waterworks.adapter.ExpandCollapseAnimation;
import com.waterworks.adapter.RemoveLessonAdapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.NonUnderlinedClickableSpan;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class CancelRemoveLessonActivity extends Activity implements OnClickListener {
    Boolean isInternetPresent = false;
    TextView tv_info;
    CheckBox chb_confirm;
    ListView lv_cr_list;
    static final int DATE_DIALOG_ID = 0;
    private int mYear;
    private int mMonth;
    private int mDay;
    Button end_date;
    CardView btn_release;
    TextView note1;
    RelativeLayout rel_enddate;
    private static final String TAG = "View wait list";
    String data_load = "False";
    ArrayList<String> StudentName, wu_sttimehr, wu_sttimemin, wu_Day, wu_lessonname, InstructorName, FormateTime, temp;
    RemoveLessonAdapter adapter;
    private ProgressDialog pd;

    String successRelease;
    String messageRelease;
    String formatedString;

    String token, familyID;
    Context mContext = this;
    public static String EndDt, RequestedID, AllTrue, format_class, ScheduleIDs;//deleted = "false",

    public static ArrayList<String> r_Date, r_Day, r_StartTime, r_Student, r_Instructor, r_Attendence, r_LessonType, r_Location, r_Counts, r_ScheduleID, r_Comments, ReleaseID, Wu_photo;
    private boolean waitlistEmpty = false;

    /**
     * Release a Class
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_remove_lesson_new_1);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(CancelRemoveLessonActivity.this);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        View view = findViewById(R.id.actionBar);
        TextView title = (TextView) view.findViewById(R.id.txtTitleText);
        Button bttnMenu = (Button) view.findViewById(R.id.relMenu);
        title.setText("Remove Class Time");
        title.setVisibility(View.VISIBLE);
        bttnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(CancelRemoveLessonActivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        isInternetPresent = Utility
                .isNetworkConnected(CancelRemoveLessonActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            Initialization();

            Intent i = getIntent();
            String from = i.getStringExtra("from");

            if (from.equals("single")) {
                new Cancel_Request().execute();
            }else {
                new ViewRemoveLessonLoad().execute();
            }

        }
    }

    private void Initialization() {
        // TODO Auto-generated method stub
        tv_info = (TextView) findViewById(R.id.tv_cr_info);
        note1 = (TextView) findViewById(R.id.note);
        final String note = getResources().getString(R.string.removelesson_info) + " ";
        String clickhere = getResources().getString(R.string.clickhere);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        stringBuilder.append(note);
        stringBuilder.append(clickhere);
        stringBuilder.setSpan(
                new NonUnderlinedClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("POS", 7);
                        startActivity(i);
                    }
                },
                note.length(), note.length() + clickhere.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_info.setText(stringBuilder);
        tv_info.setTextColor(Color.RED);
        tv_info.setMovementMethod(LinkMovementMethod.getInstance());
        tv_info.setLinkTextColor(Color.BLUE);
        chb_confirm = (CheckBox) findViewById(R.id.chb_cr_confirm);
        lv_cr_list = (ListView) findViewById(R.id.lv_cr_list);
        btn_release = (CardView) findViewById(R.id.btn_cr_save);

        end_date = (Button) findViewById(R.id.end_date);

        rel_enddate = (RelativeLayout) findViewById(R.id.rel_enddate);

        chb_confirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub
                if (isChecked == true) {
                    chb_confirm.setChecked(true);
                } else if (isChecked == false) {
                    chb_confirm.setChecked(false);
                }
            }
        });

        btn_release.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                checkSize();
                if (temp_array.size() > 0) {
                    AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(mContext);
                    alertdialogbuilder2.setTitle("Release a class");
                    alertdialogbuilder2.setIcon(getResources().getDrawable(R.drawable.alerticon));
                    try {
                        if (temp_array.size() == 1) {
                            if (waitlistEmpty) {
                                Toast.makeText(mContext, "You have nothing in waitlist.", Toast.LENGTH_SHORT).show();
                            } else {
                                alertdialogbuilder2
                                        .setMessage(Html.fromHtml("<p><font size=2>You will be PERMANENTLY removed from the day and time you selected."
                                                + " If you wish to cancel lessons only for a specific date, press CANCEL, then the back button.<br>Once you remove a lesson schedule, then this time slot will be unavailable for you to schedule in the next 30 days."
                                                + " <br> <br></br></br></font></p>"
                                                + "Are you sure you want to release <font color=red size=4>" + temp_array.size() + " </font>selected lesson time?"))
                                        .setCancelable(false)
                                        .setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int id) {
                                                        dialog.cancel();
                                                        new ReleaseAsyncTask().execute();
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        // TODO Auto-generated method stub
                                                        dialog.cancel();
                                                    }
                                                });
                            }
                        } else {
                            alertdialogbuilder2
                                    .setMessage(Html.fromHtml("<p><font size=2>You will be PERMANENTLY removed from the day and time you selected."
                                            + " If you wish to cancel lessons only for a specific date, press CANCEL, then the back button.<br>Once you remove a lesson schedule, then this time slot will be unavailable for you to schedule in the next 30 days."
                                            + " <br> <br></br></br></font></p>"
                                            + "Are you sure you want to release <font color=red size=4>" + temp_array.size() + " </font>selected lesson times?"))
                                    .setCancelable(false)
                                    .setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int id) {
                                                    dialog.cancel();
                                                    new ReleaseAsyncTask().execute();
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    // TODO Auto-generated method stub
                                                    dialog.cancel();
                                                }
                                            });
                        }
                        AlertDialog alertDialog2 = alertdialogbuilder2.create();
                        alertDialog2.show();
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        CancelRemoveLessonActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        if ((pd != null) && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CancelRemoveLessonActivity.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        onBackPressed();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isInternetPresent = Utility
                .isNetworkConnected(CancelRemoveLessonActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        }
        lv_cr_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv_cr_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                view.setSelected(true);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        CancelLessonFragmentAdapter.RemoveClassList.clear();

        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.relMenu:
                    onBackPressed();
                    break;

                default:
                    break;
            }
        } else {
            onDetectNetworkState().show();
        }
    }

    public class ViewRemoveLessonLoad extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(CancelRemoveLessonActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params1) {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("FamilyID", familyID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.removelesson, params);

            try {
                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equals("True")) {
                    StudentName = new ArrayList<String>();
                    wu_sttimehr = new ArrayList<String>();
                    Wu_photo = new ArrayList<String>();
                    wu_sttimemin = new ArrayList<String>();
                    wu_Day = new ArrayList<String>();
                    wu_lessonname = new ArrayList<String>();
                    InstructorName = new ArrayList<String>();
                    FormateTime = new ArrayList<String>();
                    RemoveLessonAdapter.RemoveFrom = new ArrayList<String>();
                    RemoveLessonAdapter.releaseClassList = new ArrayList<String>();
                    RemoveLessonAdapter.RemoveClassList = new ArrayList<String>();
                    ReleaseID = new ArrayList<String>();
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        StudentName.add(jsonChildNode.getString("StudentName"));
                        wu_sttimehr.add(jsonChildNode.getString("wu_sttimehr"));
                        Wu_photo.add(jsonChildNode.getString("wu_photo"));
                        wu_sttimemin.add(jsonChildNode.getString("wu_sttimemin"));
                        wu_Day.add(jsonChildNode.getString("wu_Day"));
                        wu_lessonname.add(jsonChildNode.getString("wu_lessonname"));
                        InstructorName.add(jsonChildNode.getString("InstructorName"));
                        RemoveLessonAdapter.RemoveFrom.add(jsonChildNode.getString("RemoveFrom"));
                        ReleaseID.add(jsonChildNode.getString("ReleaseID"));
                        FormateTime.add(jsonChildNode.getString("FormateTime"));
                        RemoveLessonAdapter.RemoveClassList.add("False");
                    }
                } else {
//                    07-06-2017 megha
                    RemoveLessonAdapter.ReleaseID.clear();
                    RemoveLessonAdapter.RemoveFrom.clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
                pd = null;
            }
            if (data_load.toString().equalsIgnoreCase("True")) {
                adapter = new RemoveLessonAdapter(StudentName, wu_sttimehr, wu_sttimemin,
                        wu_Day, wu_lessonname, InstructorName, RemoveLessonAdapter.RemoveFrom, ReleaseID, RemoveLessonAdapter.RemoveClassList, chb_confirm,
                        btn_release, FormateTime, CancelRemoveLessonActivity.this, Wu_photo);
                lv_cr_list.setAdapter(adapter);
                lv_cr_list.deferNotifyDataSetChanged();
                waitlistEmpty = false;
            } else {
                Toast.makeText(getApplicationContext(), "You do not currently have any classes to  remove.", Toast.LENGTH_SHORT).show();
                waitlistEmpty = true;
            }
        }

    }

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int yr, int monthOfYear,
                              int dayOfMonth) {

            mDay = dayOfMonth;
            mYear = yr;
            mMonth = monthOfYear;
            String m, d;
            m = "" + (mMonth + 1);
            if (m.length() == 1) {
                m = "0" + m;
            }
            d = "" + mDay;
            if (d.length() == 1) {
                d = "0" + d;
            }
            String date = m + "/" + d + "/" + mYear;
            Log.i("Remove Lesson Adapter", "Position=" + RemoveLessonAdapter.globalPosition + " , Date = " + date);

            View v = lv_cr_list.getChildAt(RemoveLessonAdapter.globalPosition - lv_cr_list.getFirstVisiblePosition());

            if (v == null)
                return;

            TextView someText = (TextView) v.findViewById(R.id.end_date);

            someText.setText(date);

            RemoveLessonAdapter.RemoveFrom.set(RemoveLessonAdapter.globalPosition, date);
            Log.e("Array Value", RemoveLessonAdapter.RemoveFrom.toString());
        }
    };

    public void upDateList() {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                adapter.notifyDataSetChanged(); //for updating the date change
                lv_cr_list.setAdapter(adapter);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                c.set(mYear, mMonth, mDay);
                Log.d("Date==", mYear + "=" + mMonth + "=" + mDay);

                DatePickerDialog dialog = new DatePickerDialog(this, dateListener, mYear, mMonth, mDay);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return dialog;


        }
        return null;
    }

    ArrayList<String> temp_array;

    public void checkSize() {
        try {
            AppConfiguration.ReleaseIDRemoveLesson = RemoveLessonAdapter.ReleaseID;//to send release ids to release screen

            temp_array = new ArrayList<String>();
            temp_array.clear();
            if (RemoveLessonAdapter.RemoveClassList != null) {
                for (int i = 0; i < RemoveLessonAdapter.RemoveClassList.size(); i++) {
                    if (RemoveLessonAdapter.RemoveClassList.get(i).equals("True")) {
                        if (RemoveLessonAdapter.ReleaseID.size() > 0) {
                            String removeClass = RemoveLessonAdapter.ReleaseID.get(i) + "|" +
                                    RemoveLessonAdapter.RemoveFrom.get(i);
                            temp_array.add(removeClass);
                            Log.d("temp_array", temp_array.toString());
                        }
                    } else {
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    //================================== Release Class=============================
    class ReleaseAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CancelRemoveLessonActivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            releasingClass();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            formatedString = null;
            RemoveLessonAdapter.releaseClassList.clear();
            if (successRelease.toString().equals("True")) {
                if (AllTrue.contains("true")) {
                    Intent i = new Intent(CancelRemoveLessonActivity.this, CancelSurvey.class);
                    i.putExtra("formatedString", CancelRemoveLessonActivity.format_class);
                    startActivity(i);
                    finish();
                } else if (AllTrue.contains("false")) {
                    ParmanentReleaseAsyncTask parmanentReleaseAsyncTask = new ParmanentReleaseAsyncTask();
                    parmanentReleaseAsyncTask.execute();
                }
            } else {
                Toast.makeText(CancelRemoveLessonActivity.this, "There is no any schedule available from this date. please select valid date for release schedule.", Toast.LENGTH_SHORT).show();
                if (pd != null) {
                    pd.dismiss();
                    pd = null;
                }
            }
        }
    }

    public void releasingClass() {

        for (int i = 0; i < RemoveLessonAdapter.RemoveClassList.size(); i++) {
            if (RemoveLessonAdapter.RemoveClassList.get(i).equals("True")) {
                String removeClass = RemoveLessonAdapter.ReleaseID.get(i) + "|" +
                        RemoveLessonAdapter.RemoveFrom.get(i);

                RemoveLessonAdapter.releaseClassList.add(removeClass);
            } else {
            }
        }
        formatedString = RemoveLessonAdapter.releaseClassList.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();

        format_class = formatedString;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
        params.put("FamilyID", familyID);
        params.put("RemoveClass", formatedString);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.releaseRequestClassURL, params);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successRelease = reader.getString("Success");

            if (reader.getString("AllSelect").contains("true") || reader.getString("AllSelect").contains("True")) {
                AllTrue = reader.getString("AllSelect");
                EndDt = reader.getString("EndDt");
                RequestedID = reader.getString("ID");
            } else {
                AllTrue = reader.getString("AllSelect");
                ScheduleIDs = reader.getString("ScheduleIDs");
            }
            if (successRelease.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                System.out.println("Final array List : " + jsonMainNode);
                if (jsonMainNode.length() > 0) {
                    r_Date = new ArrayList<String>();
                    r_Day = new ArrayList<String>();
                    r_StartTime = new ArrayList<String>();
                    r_Student = new ArrayList<String>();
                    r_Instructor = new ArrayList<String>();
                    r_Attendence = new ArrayList<String>();
                    r_LessonType = new ArrayList<String>();
                    r_Location = new ArrayList<String>();
                    r_Counts = new ArrayList<String>();
                    r_Comments = new ArrayList<String>();
                    r_ScheduleID = new ArrayList<String>();

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        r_Date.add(jsonChildNode.getString("Date"));
                        r_Day.add(jsonChildNode.getString("Day"));
                        r_StartTime.add(jsonChildNode.getString("StartTime"));
                        r_Student.add(jsonChildNode.getString("Student"));
                        r_Instructor.add(jsonChildNode.getString("Instructor"));
                        r_Attendence.add(jsonChildNode.getString("Attendence"));
                        r_LessonType.add(jsonChildNode.getString("LessonType"));
                        r_Location.add(jsonChildNode.getString("Location"));
                        r_Counts.add(jsonChildNode.getString("Counts"));
                        r_Comments.add(jsonChildNode.getString("Comments"));
                        r_ScheduleID.add(jsonChildNode.getString("ScheduleID"));
                    }
                } else {
                    Toast.makeText(mContext, "FinalArray is empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                JSONObject jobj = jsonMainNode.getJSONObject(0);
                String msg = jobj.getString("Msg");
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Cancel_Request extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(CancelRemoveLessonActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params1) {
            // TODO Auto-generated method stub
            get_final_Array();

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("FamilyID", familyID);
            params.put("strCancelID", formatedString);

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN +
                    AppConfiguration.RemoveLesson_PageLoad_FillData_SelectedID, params);
            System.out.println("Final array List responseString Cancel remove Lesson Activity : " + responseString);
            System.out.println("Final array List responseString Cancel remove Lesson Activity : " + params);

            try {
                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equals("True")) {
                    StudentName = new ArrayList<String>();
                    wu_sttimehr = new ArrayList<String>();
                    wu_sttimemin = new ArrayList<String>();
                    wu_Day = new ArrayList<String>();
                    wu_lessonname = new ArrayList<String>();
                    InstructorName = new ArrayList<String>();
                    FormateTime = new ArrayList<String>();
                    RemoveLessonAdapter.RemoveFrom = new ArrayList<String>();
                    RemoveLessonAdapter.releaseClassList = new ArrayList<String>();
                    RemoveLessonAdapter.RemoveClassList = new ArrayList<String>();
                    ReleaseID = new ArrayList<String>();
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        StudentName.add(jsonChildNode.getString("StudentName"));
                        wu_sttimehr.add(jsonChildNode.getString("wu_sttimehr"));
                        wu_sttimemin.add(jsonChildNode.getString("wu_sttimemin"));
                        wu_Day.add(jsonChildNode.getString("wu_Day"));
                        wu_lessonname.add(jsonChildNode.getString("wu_lessonname"));
                        InstructorName.add(jsonChildNode.getString("InstructorName"));
                        RemoveLessonAdapter.RemoveFrom.add(jsonChildNode.getString("RemoveFrom"));
                        ReleaseID.add(jsonChildNode.getString("ReleaseID"));
                        FormateTime.add(jsonChildNode.getString("FormateTime"));
                        RemoveLessonAdapter.RemoveClassList.add("False");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
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
                pd = null;
            }
            if (data_load.toString().equalsIgnoreCase("True")) {
                adapter = new RemoveLessonAdapter(StudentName, wu_sttimehr, wu_sttimemin,
                        wu_Day, wu_lessonname, InstructorName, RemoveLessonAdapter.RemoveFrom, ReleaseID, RemoveLessonAdapter.RemoveClassList, chb_confirm,
                        btn_release, FormateTime, CancelRemoveLessonActivity.this, Wu_photo);
                lv_cr_list.setAdapter(adapter);

            } else {
                Toast.makeText(getApplicationContext(), "You have nothing in waitlist.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //==================================Parmanent Release Class=============================

    class ParmanentReleaseAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ParmanentreleasingClass();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
                pd = null;
            }

            if (successRelease.toString().equals("True")) {
                Intent intent = new Intent(CancelRemoveLessonActivity.this, CancelLessonFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "The selected classes have been removed successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ParmanentreleasingClass() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("FamilyID", familyID);
        param.put("RemoveClass", CancelRemoveLessonActivity.format_class);
        param.put("ScheduleIDs", CancelRemoveLessonActivity.ScheduleIDs);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.releaseClassURL_new, param);
        ParmanentReleasereadAndParseJSON(responseString);

        System.out.println("Final array List responseString Cancel remove Lesson Activity Info: " + responseString);
        System.out.println("Final array List responseString Cancel remove Lesson Activity Info param: " + param);
    }


    public void ParmanentReleasereadAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successRelease = reader.getString("Success");
            if (successRelease.toString().equals("True")) {
            } else {
                Toast.makeText(CancelRemoveLessonActivity.this, "Something's went wrong.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void get_final_Array() {
        temp = new ArrayList<String>();

        for (int i = 0; i < CancelLessonFragmentAdapter.RemoveClassList.size(); i++) {
            if (CancelLessonFragmentAdapter.RemoveClassList.get(i).equals("True")) {
                temp.add(CancelLessonFragment.CancelID.get(i).trim());
            } else {
            }
        }

        formatedString = temp.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();
        Log.e("final String", formatedString);
    }
}
