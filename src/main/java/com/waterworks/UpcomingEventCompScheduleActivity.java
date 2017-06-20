package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.AllEventListOfStudentByMeetAsyncTask;
import com.waterworks.asyncTasks.EventListOfStudentByMeetAsyncTask;
import com.waterworks.model.UpcomingAllEventStudListModel;
import com.waterworks.model.UpcomingEventStudListModel;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.sheduling.ScheduleLessonFragement3;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpcomingEventCompScheduleActivity extends Activity {

    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room;
    private TextView txt_1, txt_2, txt_3, txtStudentName, txtEventId, txtEventDescription, txtStrokeDescription,
            txtEventTimeAndName, txtGender, txtAge, txtEvent, txtEventSymbol, txtEstDuration;
    private LinearLayout llEventRow, llStudentList, llAllEventsList, llAllEventListRow, llAllEventsRow;
    private View selected_1, selected_2, selected_3;
    private EventListOfStudentByMeetAsyncTask eventListOfStudentByMeetAsyncTask = null;
    private AllEventListOfStudentByMeetAsyncTask allEventListOfStudentByMeetAsyncTask = null;
    private Context mContext = this;
    private String swimmeetid = "";
    String successTime, messageTime, Displaytime, token, IsUpcomingEventsflg;
    private ProgressDialog progressDialog = null;
    private ArrayList<UpcomingEventStudListModel> upcomingEventStudListModels = new ArrayList<>();
    private ArrayList<UpcomingAllEventStudListModel> upcomingAllEventStudListModels = new ArrayList<>();
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_event_comp_schedule);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        swimmeetid = getIntent().getStringExtra("swimmeetid");

        topTabsViewListnersAnimation();
        init();
        isInternetPresent = Utility.isNetworkConnected(UpcomingEventCompScheduleActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new SwimCmptSchedule_EstimatedDurationForEventAsyncTask().execute();
            fetchAndShowEventData();
        }
    }

    public void init() {
        llStudentList = (LinearLayout) findViewById(R.id.llStudentList);
        llAllEventsList = (LinearLayout) findViewById(R.id.llAllEventsList);
        txtEstDuration = (TextView) findViewById(R.id.txtEstDuration);
    }

    public void fetchAndShowEventData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListOfStudentByMeetAsyncTask = new EventListOfStudentByMeetAsyncTask(UpcomingEventCompScheduleActivity.this, swimmeetid);
                    upcomingEventStudListModels = eventListOfStudentByMeetAsyncTask.execute().get();
                    allEventListOfStudentByMeetAsyncTask = new AllEventListOfStudentByMeetAsyncTask(UpcomingEventCompScheduleActivity.this, swimmeetid);
                    upcomingAllEventStudListModels = allEventListOfStudentByMeetAsyncTask.execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUI();
                            setUIAllEvents();
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setUIAllEvents() {
        for (int i = 0; i < upcomingAllEventStudListModels.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_comp_all_events, null, false);
            txtEventTimeAndName = (TextView) child.findViewById(R.id.txtEventTimeAndName);
            llAllEventListRow = (LinearLayout) child.findViewById(R.id.llAllEventListRow);

            txtEventTimeAndName.setText(upcomingAllEventStudListModels.get(i).getStartTime() + " " + upcomingAllEventStudListModels.get(i).getEventDescription());

            for (UpcomingAllEventStudListModel.Events eventList : upcomingAllEventStudListModels.get(i).getEventsList()) {
                View childll = getLayoutInflater().inflate(R.layout.list_row_all_events_comp_schedule, null, false);
                llAllEventsRow = (LinearLayout) childll.findViewById(R.id.llAllEventsRow);

                txtEventSymbol = (TextView) childll.findViewById(R.id.txtEventSymbol);
                txtGender = (TextView) childll.findViewById(R.id.txtGender);
                txtAge = (TextView) childll.findViewById(R.id.txtAge);
                txtEvent = (TextView) childll.findViewById(R.id.txtEvent);

                txtEventSymbol.setText(eventList.getEventNumber());
                txtGender.setText(eventList.getGender());
                txtAge.setText(eventList.getAge());
                txtEvent.setText(eventList.getStrokeDesc());

                for (int k = 0; k < upcomingEventStudListModels.size(); k++) {
                    for (UpcomingEventStudListModel.Events eventListTop : upcomingEventStudListModels.get(k).getEventsList()) {
                        if (txtEventSymbol.getText().toString().equalsIgnoreCase(eventListTop.getEventNumber())) {
                            llAllEventsRow.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
                        }/*else {
                            llAllEventListRow.setBackgroundResource(R.drawable.gray_border);
                        }*/
                    }
                }
                llAllEventListRow.addView(childll);
            }

            llAllEventsList.addView(child);
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
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

    public void setUI() {
        for (int i = 0; i < upcomingEventStudListModels.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_comp_sched, null, false);
            txtStudentName = (TextView) child.findViewById(R.id.txtStudentName);

            txtStudentName.setText(upcomingEventStudListModels.get(i).getStudentName());
            txtStudentName.setBackgroundColor(getResources().getColor(R.color.student_back));

            for (UpcomingEventStudListModel.Events eventList : upcomingEventStudListModels.get(i).getEventsList()) {
                View childll = getLayoutInflater().inflate(R.layout.layout_event_row, null, false);
                llEventRow = (LinearLayout) child.findViewById(R.id.llEventRow);

                txtEventId = (TextView) childll.findViewById(R.id.txtEventId);
                txtEventDescription = (TextView) childll.findViewById(R.id.txtEventDescription);
                txtStrokeDescription = (TextView) childll.findViewById(R.id.txtStrokeDescription);

                txtEventId.setText(eventList.getEventNumber());
                txtEventDescription.setText(eventList.getDescription());
                txtStrokeDescription.setText(eventList.getStrockDescription());

                llEventRow.addView(childll);
            }

            llStudentList.addView(child);
        }
    }
    public void topTabsViewListnersAnimation() {
        View view = findViewById(R.id.include_swim_comp_custom_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);

        BackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(UpcomingEventCompScheduleActivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        selected_1.setVisibility(View.VISIBLE);
        selected_2.setVisibility(View.GONE);
        selected_3.setVisibility(View.GONE);

        ((AnimationDrawable) selected_1.getBackground()).start();

        ll_upcoming_meet = (LinearLayout) view.findViewById(R.id.ll_upcoming_meet);
        ll_register = (LinearLayout) view.findViewById(R.id.ll_register);
        ll_trophy_room = (LinearLayout) view.findViewById(R.id.ll_trophy_room);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        if (AppConfiguration.CheckMeet.equalsIgnoreCase("1")) {
            txt_1.setText("Todays's Meet");
        }

        ll_upcoming_meet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionUpcomingEventsAcitivity.class);
                startActivity(i);
                UpcomingEventCompScheduleActivity.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });
        ll_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionRegisterAcitivity.class);
                startActivity(i);
                UpcomingEventCompScheduleActivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });
        ll_trophy_room.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionTrophyRoomAcitivity.class);
                startActivity(i);
                UpcomingEventCompScheduleActivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }

    public void load_estimateTime() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("swimmeetid", swimmeetid);
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmptSchedule_EstimatedDurationForEvent, param);
        readAndParseJSONEstimationtime(responseString);
    }

    public void readAndParseJSONEstimationtime(String time) {
        try {
            JSONObject reader = new JSONObject(time);
            successTime = reader.getString("Success");
            if (successTime.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    Displaytime = jsonChildNode.getString("EstimatedTime");
                    IsUpcomingEventsflg = jsonChildNode.getString("IsUpcomingEvents");
                }
            } else {
                JSONArray jsonMainNode = reader
                        .optJSONArray("EmailPref");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageTime = jsonChildNode.getString("Msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SwimCmptSchedule_EstimatedDurationForEventAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(UpcomingEventCompScheduleActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            load_estimateTime();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (!IsUpcomingEventsflg.equalsIgnoreCase("0")) {
                txtEstDuration.setText(Displaytime);
            } else {
                txtEstDuration.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
