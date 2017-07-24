package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.asyncTasks.SwimCmpt_AllGetEventResultForStudentAsyncTask;
import com.waterworks.asyncTasks.SwimCmpt_AllMeetResultForEventAsyncTask;
import com.waterworks.model.UpcomingEventResultsDetailModel;
import com.waterworks.model.UpcomingEventResultsListModel;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import java.util.ArrayList;

public class UpcomingEventCompResultDetailActivity extends Activity {

    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room;
    private TextView txt_1, txt_2, txt_3, txtEventTitle, txtStudentName, txtPlace, txtSwimmer, txtAge, txtTime, txtTimeImprovement;
    private LinearLayout llStudentEventList, llRowLayout;
    private View selected_1, selected_2, selected_3;
    private SwimCmpt_AllMeetResultForEventAsyncTask swimCmpt_allMeetResultForEventAsyncTask = null;
    private Context mContext = this;
    private String StudIDEventIDSwimmeetID = "", studID, swimMeetID, eventID, selectedEventTitle, selectedEventName;
    private ProgressDialog progressDialog = null;
    private ArrayList<UpcomingEventResultsDetailModel> upcomingEventResultsListModels = new ArrayList<>();
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_event_comp_results_detail);

        StudIDEventIDSwimmeetID = getIntent().getStringExtra("studentID");
        Log.d("change", StudIDEventIDSwimmeetID);
        String[] data = StudIDEventIDSwimmeetID.split(":");
        studID = data[0];
        eventID = data[1];
        swimMeetID = data[2];
        selectedEventTitle = data[3];
        selectedEventName = data[4];

        topTabsViewListnersAnimation();
        init();
        isInternetPresent = Utility.isNetworkConnected(UpcomingEventCompResultDetailActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            fetchAndShowStudentWiseEventData();
        }
    }

    public void init() {
        llStudentEventList = (LinearLayout) findViewById(R.id.llStudentEventList);
        txtEventTitle = (TextView) findViewById(R.id.txtEventTitle);
        llRowLayout = (LinearLayout) findViewById(R.id.llRowLayout);

        Log.d("event", selectedEventTitle);
        txtEventTitle.setText(selectedEventTitle + ":" + " " + selectedEventName);
    }

    public void fetchAndShowStudentWiseEventData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    swimCmpt_allMeetResultForEventAsyncTask = new SwimCmpt_AllMeetResultForEventAsyncTask(UpcomingEventCompResultDetailActivity.this, swimMeetID, eventID);
                    upcomingEventResultsListModels = swimCmpt_allMeetResultForEventAsyncTask.execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            setUI(studID);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setUI(String studID) {
        for (UpcomingEventResultsDetailModel upcomingEventResultsListModel : upcomingEventResultsListModels) {
            View childll = getLayoutInflater().inflate(R.layout.layout_row_event_detail_results, null, false);

            llRowLayout = (LinearLayout) childll.findViewById(R.id.llRowLayout);
            txtTime = (TextView) childll.findViewById(R.id.txtTime);
            txtTimeImprovement = (TextView) childll.findViewById(R.id.txtTimeImprovement);
            txtPlace = (TextView) childll.findViewById(R.id.txtPlace);
            txtSwimmer = (TextView) childll.findViewById(R.id.txtSwimmer);
            txtAge = (TextView) childll.findViewById(R.id.txtAge);

            txtTime.setText(upcomingEventResultsListModel.getMeetTime());
            if (upcomingEventResultsListModel.getTimeImprovement().contains("-")) {
                txtTimeImprovement.setText(upcomingEventResultsListModel.getTimeImprovement());
                txtTimeImprovement.setTextColor(getResources().getColor(R.color.green_trophy_room));
            } else if (upcomingEventResultsListModel.getTimeImprovement().contains("+")) {
                txtTimeImprovement.setText(upcomingEventResultsListModel.getTimeImprovement());
                txtTimeImprovement.setTextColor(getResources().getColor(R.color.red));
            } else {
                txtTimeImprovement.setText(upcomingEventResultsListModel.getTimeImprovement());
            }
            txtPlace.setText(upcomingEventResultsListModel.getPlaceno());
            txtSwimmer.setText(upcomingEventResultsListModel.getSwimmer());
            txtAge.setText(upcomingEventResultsListModel.getAge());

            if (studID.equalsIgnoreCase(upcomingEventResultsListModel.getStudentID())) {
                llRowLayout.setBackgroundColor(getResources().getColor(R.color.orange));
            }

            llStudentEventList.addView(childll);
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

    public void topTabsViewListnersAnimation() {
        View view = findViewById(R.id.include_swim_comp_custom_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);

        BackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
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
                UpcomingEventCompResultDetailActivity.this.overridePendingTransition(0, 0);
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
                UpcomingEventCompResultDetailActivity.this.overridePendingTransition(0, 0);
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
                UpcomingEventCompResultDetailActivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
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
