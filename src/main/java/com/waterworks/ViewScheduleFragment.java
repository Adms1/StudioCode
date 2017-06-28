package com.waterworks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.ViewPastSchdeduleDialogAdapter;
import com.waterworks.adapter.ViewSchdeduleFragmentAdapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class ViewScheduleFragment extends Fragment implements OnClickListener {
    private static String TAG = "View schdeule";
    View rootView;
    Boolean isInternetPresent = false;
    Button relMenu;
    Button btn_attendance_codes, btn_past_schedule, btn_change_schedule, btn_pricesheet;
    TextView tv_note1, tv_vs_invoice_value;
    ListView lv_vs_schedule_data;
    RelativeLayout rl_schedule_detail;
    String message;
    String data_load = "False";
    String invoice_value = "";
    String msg;
    int mYEAR;
    int mMONTH;
    int mDAY;
    String startday, startmonth, startyear, endday, endmonth, endyear;
    ArrayList<String> Date, Day, Time, Student, Age, Level, Instructor, Attendance, LessonType,
            Location, Count, Comment, ABBR, NewDate, NewTime, Header, Footer;

    TextView tv_vs_name, tv_noschedule;

    String token, familyID;

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        ConstantData.destroyed = true;
    }

    public ViewScheduleFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_schedule, container, false);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity().getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        isInternetPresent = Utility
                .isNetworkConnected(getActivity().getApplicationContext());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            Initialization();
            Calendar c = Calendar.getInstance();
            //get current date
            mYEAR = c.get(Calendar.YEAR);
            mMONTH = c.get(Calendar.MONTH);
            mDAY = c.get(Calendar.DAY_OF_MONTH);

            new ScheduleData().execute();
        }
        return rootView;
    }

    private void Initialization() {

        tv_vs_name = (TextView) rootView.findViewById(R.id.tv_vs_name);
        tv_vs_name.setText("Current Schedule");

        tv_noschedule = (TextView) rootView.findViewById(R.id.tv_noschedule);
        relMenu = (Button) rootView.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(this);
        tv_note1 = (TextView) rootView.findViewById(R.id.tv_vs_note1);
        String note = getResources().getString(R.string.vs_note1) + " ";
        String clickhere = getResources().getString(R.string.clickhere);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(note);
        stringBuilder.append(clickhere);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent requestshadowingIntent = new Intent(getActivity().getApplicationContext(), RequestShadowingActivity.class);
                startActivity(requestshadowingIntent);
            }
        };
        stringBuilder.setSpan(clickableSpan, 69, 80, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        tv_note1.setText(stringBuilder);
        tv_note1.setMovementMethod(LinkMovementMethod.getInstance());
        tv_note1.setLinkTextColor(getResources().getColor(R.color.link));
        lv_vs_schedule_data = (ListView) rootView.findViewById(R.id.lv_vs_schedule_data);
        btn_attendance_codes = (Button) rootView.findViewById(R.id.btn_vs_attendance_code);
        btn_past_schedule = (Button) rootView.findViewById(R.id.btn_vs_past_schedule);
        btn_change_schedule = (Button) rootView.findViewById(R.id.btn_vs_change_schedule);
        btn_pricesheet = (Button) rootView.findViewById(R.id.btn_vs_view_price_sheet);
        btn_pricesheet.setOnClickListener(this);
        btn_attendance_codes.setOnClickListener(this);
        btn_change_schedule.setOnClickListener(this);
        btn_past_schedule.setOnClickListener(this);
        rl_schedule_detail = (RelativeLayout) rootView.findViewById(R.id.rl_schedule_detail);
        rl_schedule_detail.setVisibility(View.GONE);
        tv_vs_invoice_value = (TextView) rootView.findViewById(R.id.tv_vs_invoice_value);

    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        getActivity().finish();
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

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isInternetPresent = Utility
                .isNetworkConnected(getActivity().getApplicationContext());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.relMenu:
                DashBoardActivity.onLeft();
                break;
            case R.id.btn_vs_attendance_code:
                Intent attendancecodeIntent = new Intent(getActivity().getApplicationContext(), AttenadnceCodesActivity.class);
                startActivity(attendancecodeIntent);
                break;
            case R.id.btn_vs_past_schedule:
                PastScheduleDialog();
                break;
            case R.id.btn_vs_change_schedule:

                break;
            case R.id.btn_vs_view_price_sheet:
                Intent priceSheetIntent = new Intent(getActivity().getApplicationContext(), ViewPriceSheetActivity.class);
                startActivity(priceSheetIntent);
                break;
            default:
                break;
        }
    }

    private class ScheduleData extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Date = new ArrayList<String>();
            Day = new ArrayList<String>();
            Time = new ArrayList<String>();
            Student = new ArrayList<String>();
            Age = new ArrayList<String>();
            Level = new ArrayList<String>();
            Instructor = new ArrayList<String>();
            Attendance = new ArrayList<String>();
            LessonType = new ArrayList<String>();
            Location = new ArrayList<String>();
            Count = new ArrayList<String>();
            Comment = new ArrayList<String>();
            ABBR = new ArrayList<String>();
            NewDate = new ArrayList<String>();
            NewTime = new ArrayList<String>();

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", familyID);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.viewschedule, param);
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
                rl_schedule_detail.setVisibility(View.VISIBLE);
                tv_vs_invoice_value.setVisibility(View.GONE);
                tv_noschedule.setVisibility(View.GONE);
                tv_vs_invoice_value.setText(invoice_value);
                lv_vs_schedule_data.setAdapter(
                        new ViewSchdeduleFragmentAdapter(getActivity().getApplicationContext(), Date, Day,
                                Time, Student, Age, Level, Instructor, Attendance,
                                LessonType, Location, Count, Comment, ABBR, NewDate, NewTime));
            } else {
                rl_schedule_detail.setVisibility(View.GONE);
                tv_noschedule.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

        }
    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            data_load = reader.getString("Success");
            if (data_load.toString().equals("True")) {
                invoice_value = reader.getString("Invoice");
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    Date.add(jsonChildNode.getString("Date"));
                    Day.add(jsonChildNode.getString("Day"));
                    Time.add(jsonChildNode.getString("Time"));
                    Student.add(jsonChildNode.getString("Student"));
                    Age.add(jsonChildNode.getString("Age"));
                    Level.add(jsonChildNode.getString("Level"));
                    Instructor.add(jsonChildNode.getString("Instructor"));
                    Attendance.add(jsonChildNode.getString("Attendance"));
                    LessonType.add(jsonChildNode.getString("LessonType"));
                    Location.add(jsonChildNode.getString("Location"));
                    Count.add(jsonChildNode.getString("Count"));
                    Comment.add(jsonChildNode.getString("Comment"));
                    ABBR.add(jsonChildNode.getString("ABBR"));
                    NewDate.add(jsonChildNode.getString("NewDate"));
                    NewTime.add(jsonChildNode.getString("NewTime"));

                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                msg = jsonChildNode.getString("Msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PastScheduleDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_past_schedule_date_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        ImageButton ib_close = (ImageButton) dialog.findViewById(R.id.ib_dialog_close);
        final Button btn_start_date = (Button) dialog.findViewById(R.id.btn_dialog_start_date);
        final Button btn_end_date = (Button) dialog.findViewById(R.id.btn_dialog_end_date);
        Button btn_view_schedule = (Button) dialog.findViewById(R.id.btn_view_schedule);
        ib_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mYEAR = year;
                mMONTH = monthOfYear + 1;
                mDAY = dayOfMonth;
                String d, m, y;
                d = Integer.toString(mDAY);
                m = Integer.toString(mMONTH);
                y = Integer.toString(mYEAR);
                if (mDAY < 10) {
                    d = "0" + d;
                }
                if (mMONTH < 10) {
                    m = "0" + m;
                }

                startday = d;
                startmonth = m;
                startyear = y;
                btn_start_date.setText(m + "/" + d + "/" + y);
            }
        };
        final DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mYEAR = year;
                mMONTH = monthOfYear + 1;
                mDAY = dayOfMonth;
                String d, m, y;
                d = Integer.toString(mDAY);
                m = Integer.toString(mMONTH);
                y = Integer.toString(mYEAR);
                if (mDAY < 10) {
                    d = "0" + d;
                }
                if (mMONTH < 10) {
                    m = "0" + m;
                }

                endday = d;
                endmonth = m;
                endyear = y;
                btn_end_date.setText(m + "/" + d + "/" + y);
            }
        };
        btn_start_date.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog mDialog = new DatePickerDialog(getActivity(),
                        mDateSetListener, mYEAR, mMONTH, mDAY);
                mDialog.getDatePicker().setCalendarViewShown(false);
                mDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDialog.show();
            }
        });
        btn_end_date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (btn_start_date.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please select start date first", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Calendar minDate = Calendar.getInstance();
                        minDate.set(Integer.parseInt(startyear), (Integer.parseInt(startmonth) - 1), Integer.parseInt(startday), 00, 00, 01);
                        DatePickerDialog mDialog1 = new DatePickerDialog(getActivity(),
                                mDateSetListener1, Integer.parseInt(startday), Integer.parseInt(startmonth), Integer.parseInt(startyear));
                        mDialog1.getDatePicker().setCalendarViewShown(false);
                        mDialog1.getDatePicker().setMinDate(minDate.getTimeInMillis());
                        if (System.currentTimeMillis() != minDate.getTimeInMillis()) {
                            mDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                        }


                        mDialog1.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btn_view_schedule.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConfiguration.isConnectingToInternet(getActivity().getApplicationContext())) {
                    if (btn_start_date.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please select start date", Toast.LENGTH_LONG).show();
                    } else if (btn_end_date.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please select end date", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.cancel();
                        new PastSchedule().execute();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    private class PastSchedule extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            rl_schedule_detail.setVisibility(View.GONE);

            tv_vs_name.setText("Past Schedule");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Header = new ArrayList<String>();
            Footer = new ArrayList<String>();
            Date = new ArrayList<String>();
            Day = new ArrayList<String>();
            Time = new ArrayList<String>();
            Student = new ArrayList<String>();
            Age = new ArrayList<String>();
            Level = new ArrayList<String>();
            Instructor = new ArrayList<String>();
            Attendance = new ArrayList<String>();
            LessonType = new ArrayList<String>();
            Location = new ArrayList<String>();
            Count = new ArrayList<String>();
            Comment = new ArrayList<String>();
            ABBR = new ArrayList<String>();
            NewDate = new ArrayList<String>();
            NewTime = new ArrayList<String>();

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("FamilyID", "" + familyID);
            param.put("StartMonth", String.valueOf(startmonth));
            param.put("StartDay", String.valueOf(startday));
            param.put("StartYear", String.valueOf(startyear));
            param.put("EndMonth", String.valueOf(endmonth));
            param.put("EndDay", String.valueOf(endday));
            param.put("EndYear", String.valueOf(endyear));

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.viewpastschedule, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("PhoneList");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        Date.add(jsonChildNode.getString("Date"));
                        Day.add(jsonChildNode.getString("Day"));
                        Time.add(jsonChildNode.getString("Time"));
                        Student.add(jsonChildNode.getString("Student"));
                        Age.add(jsonChildNode.getString("Age"));
                        Level.add(jsonChildNode.getString("Level"));
                        Instructor.add(jsonChildNode.getString("Instructor"));
                        Attendance.add(jsonChildNode.getString("Attendance"));
                        LessonType.add(jsonChildNode.getString("LessonType"));
                        Location.add(jsonChildNode.getString("Location"));
                        Count.add(jsonChildNode.getString("Count"));
                        Comment.add(jsonChildNode.getString("Comment"));
                        ABBR.add(jsonChildNode.getString("ABBR"));
                        NewDate.add(jsonChildNode.getString("NewDate"));
                        NewTime.add(jsonChildNode.getString("NewTime"));
                        Header.add(jsonChildNode.getString("Header"));
                        Footer.add(jsonChildNode.getString("Footer"));
                    }
                } else {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                    msg = jsonChildNode.getString("Msg");
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
            }
            if (data_load.toString().equals("True")) {
                rl_schedule_detail.setVisibility(View.VISIBLE);
                tv_noschedule.setVisibility(View.GONE);
                tv_vs_invoice_value.setVisibility(View.GONE);
                lv_vs_schedule_data.setAdapter(
                        new ViewPastSchdeduleDialogAdapter(getActivity().getApplicationContext(), Date, Day,
                                Time, Student, Age, Level, Instructor, Attendance,
                                LessonType, Location, Count, Comment, ABBR, NewDate, NewTime, Header, Footer));
            } else {
                rl_schedule_detail.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                tv_noschedule.setVisibility(View.VISIBLE);
            }

        }
    }
}
