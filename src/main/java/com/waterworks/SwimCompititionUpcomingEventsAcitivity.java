package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.asyncTasks.SwimCmpt_AllGetEventResultForStudentAsyncTask;
import com.waterworks.asyncTasks.SwimCompUpcomingEventsAsyncTask;
import com.waterworks.model.UpcomingEventResultsListModel;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.sheduling.ScheduleLessonFragement3;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SwimCompititionUpcomingEventsAcitivity extends Activity {
    private String TAG = "SwimCampsActivity1";
    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room;
    private TextView txt_1, txt_2, txt_3, txtNextSwimComp, txtDayDate, txtSite, txtAddress, txtSite2nd, txtState, txtZipCode, txtStudentName, txtNumberOfEvents, txtMiddleMessage;
    private Button btnChecking, btnCompetitionResult, btnCompetitionSchedule, btnCompetitionRecords;
    private LinearLayout llStudentList;
    private View selected_1, selected_2, selected_3;
    private Context mContext = this;
    private String token, flgMeet, sitename, Address, State, ZipCode, MeetDate, CheckinFlg;
    public static ArrayList<HashMap<String, String>> studentsList = new ArrayList<>();
    private SwimCompUpcomingEventsAsyncTask swimCompUpcomingEventsAsyncTask = null;
    private ProgressDialog progressDialog = null;
    private String swimmeetid = "";
    Boolean isInternetPresent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swim_compitition_upcoming_events);

        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        topTabsViewListnersAnimation();
        init();
        setListners();
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        isInternetPresent = Utility.isNetworkConnected(SwimCompititionUpcomingEventsAcitivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            getUpcomingEventData();
        }
    }
    private void init() {
        txtNextSwimComp = (TextView) findViewById(R.id.txtNextSwimComp);
        txtDayDate = (TextView) findViewById(R.id.txtDayDate);
        txtSite = (TextView) findViewById(R.id.txtSite);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtSite2nd = (TextView) findViewById(R.id.txtSite2nd);
        txtState = (TextView) findViewById(R.id.txtState);
        txtZipCode = (TextView) findViewById(R.id.txtZipCode);
        txtStudentName = (TextView) findViewById(R.id.txtStudentName);
        txtNumberOfEvents = (TextView) findViewById(R.id.txtNumberOfEvents);
        llStudentList = (LinearLayout) findViewById(R.id.llStudentList);
        btnChecking = (Button) findViewById(R.id.btnChecking);
        btnCompetitionResult = (Button) findViewById(R.id.btnCompetitionResult);
        btnCompetitionSchedule = (Button) findViewById(R.id.btnCompetitionSchedule);
        btnCompetitionRecords = (Button) findViewById(R.id.btnCompetitionRecords);
        txtMiddleMessage = (TextView) findViewById(R.id.txtMiddleMessage);

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
    public void setListners(){
        btnChecking.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimCompititionUpcomingEventsAcitivity.this, CheckInFragment.class);
                intentCheckin.putExtra("swimmeetid", swimmeetid);
                startActivity(intentCheckin);
            }
        });

        btnCompetitionResult.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCompResults = new Intent(SwimCompititionUpcomingEventsAcitivity.this, UpcomingEventCompResultActivity.class);
                intentCompResults.putExtra("swimmeetid", swimmeetid);
                startActivity(intentCompResults);
            }
        });

        btnCompetitionSchedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCompSchedule = new Intent(SwimCompititionUpcomingEventsAcitivity.this, UpcomingEventCompScheduleActivity.class);
                intentCompSchedule.putExtra("swimmeetid", swimmeetid);
                startActivity(intentCompSchedule);
            }
        });

        btnCompetitionRecords.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCompSchedule = new Intent(SwimCompititionUpcomingEventsAcitivity.this, UpcomEvntsViewCompRecordsActivity.class);
                intentCompSchedule.putExtra("swimmeetid", swimmeetid);
                startActivity(intentCompSchedule);

            }
        });
    }

    public void getUpcomingEventData(){
        progressDialog = new ProgressDialog(SwimCompititionUpcomingEventsAcitivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    swimCompUpcomingEventsAsyncTask = new SwimCompUpcomingEventsAsyncTask(token);
                    String responseString = swimCompUpcomingEventsAsyncTask.execute().get();
                    parseCheckinData(responseString);
                    SwimCompititionUpcomingEventsAcitivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUI();
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setUI() {

        if (CheckinFlg.equalsIgnoreCase("1"))
        {
            btnChecking.setEnabled(true);
            btnChecking.setBackgroundColor(getResources().getColor(R.color.orange));
            btnChecking.setText("Check In Now");
            txtMiddleMessage.setText("Please Check In now.\n We look forward to seeing you there!");

        }
        else if (CheckinFlg.equalsIgnoreCase("0"))
        {
            btnChecking.setEnabled(false);
            btnChecking.setBackgroundColor(Color.parseColor("#fee0bf"));
            btnChecking.setText("You are Checked In");
            txtMiddleMessage.setText("");
        }
        String schedule_result[]=flgMeet.toString().trim().split("\\|");

        if (schedule_result[0].equalsIgnoreCase("0"))
        {
            txtNextSwimComp.setText("You are not currently registered for any future swim competitions.");
            btnChecking.setEnabled(false);
            btnChecking.setBackgroundColor(Color.parseColor("#fee0bf"));
            txtMiddleMessage.setText("You will be able to Check In on the day of the meet\n We look forward to seeing you there!");
            btnCompetitionSchedule.setEnabled(false);
            btnCompetitionSchedule.setTextColor(mContext.getResources().getColor(R.color.black_prefer_client_blur));

        }
        else if (schedule_result[0].equalsIgnoreCase("1"))
        {
            txtNextSwimComp.setText("You have a Swim Competition today!");
            txt_1.setText("Today's Meet");
            btnCompetitionSchedule.setEnabled(true);
            btnCompetitionSchedule.setTextColor(mContext.getResources().getColor(R.color.black_prefer_client));


        }
        else if (schedule_result[0].equalsIgnoreCase("2"))
        {
            txtNextSwimComp.setText("Your next Swim Competition is:");
            btnChecking.setEnabled(false);
            if (CheckinFlg.equalsIgnoreCase("1")) {
                btnChecking.setEnabled(true);
                btnChecking.setBackgroundColor(getResources().getColor(R.color.orange));
                btnChecking.setText("Check In Now");
                txtMiddleMessage.setText("Please Check In now.\n We look forward to seeing you there!");

            } else if (CheckinFlg.equalsIgnoreCase("0")) {
                btnChecking.setEnabled(false);
                btnChecking.setBackgroundColor(Color.parseColor("#fee0bf"));
                btnChecking.setText("Check In");
                txtMiddleMessage.setText(Html.fromHtml("You will be able to Check In on the day of the meet <br> We look forward to seeing you there!"));


            }
            btnCompetitionSchedule.setEnabled(true);
            btnCompetitionSchedule.setTextColor(mContext.getResources().getColor(R.color.black_prefer_client));

        }
        if (schedule_result[1].equalsIgnoreCase("1"))
        {
            btnCompetitionResult.setEnabled(true);
            btnCompetitionResult.setTextColor(mContext.getResources().getColor(R.color.black_prefer_client));
        }
        else
        {
            btnCompetitionResult.setEnabled(false);
            btnCompetitionResult.setTextColor(mContext.getResources().getColor(R.color.black_prefer_client_blur));
        }


        txtDayDate.setText(MeetDate);
        txtSite.setText(sitename);
        txtAddress.setText(Address);
        txtSite2nd.setText(sitename);
        txtState.setText(State);
        txtZipCode.setText(ZipCode);

        if(llStudentList.getChildCount() > 0){
            llStudentList.removeAllViews();
        }

        for (int i = 0; i < studentsList.size(); i++) {
            View childll = getLayoutInflater().inflate(R.layout.layout_upcoming_meet_stud_list, null, false);
            txtStudentName = (TextView) childll.findViewById(R.id.txtStudentName);
            txtNumberOfEvents = (TextView) childll.findViewById(R.id.txtNumberOfEvents);

            txtStudentName.setText(studentsList.get(i).get("StudentName"));
            txtNumberOfEvents.setText(studentsList.get(i).get("MeetCount"));

            llStudentList.addView(childll);
        }
    }

    public void parseCheckinData(String respString) {
        studentsList.clear();
        try {
            JSONObject reader = new JSONObject(respString);
            String data_load = reader.getString("Success");
            if (data_load.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    flgMeet = jsonChildNode.getString("flgMeet");
                    sitename = jsonChildNode.getString("sitename");
                    Address = jsonChildNode.getString("Address");
                    State = jsonChildNode.getString("State");
                    ZipCode = jsonChildNode.getString("ZipCode");
                    MeetDate = jsonChildNode.getString("MeetDate");
                    CheckinFlg = jsonChildNode.getString("CheckinFlg");
                    swimmeetid = jsonChildNode.getString("swimmeetid");

                    AppConfiguration.flagmeet=flgMeet;
                    JSONArray jsonArrayChild = jsonChildNode.optJSONArray("Students");
                    HashMap<String, String> hashMap;

                    for (int j = 0; j < jsonArrayChild.length(); j++) {
                        JSONObject jsonChild = jsonArrayChild.getJSONObject(j);
                        hashMap = new HashMap<>();
                        hashMap.put("StudentName", jsonChild.getString("StudentName"));
                        hashMap.put("MeetCount", jsonChild.getString("MeetCount"));
                        hashMap.put("StudentID", jsonChild.getString("StudentID"));
                        studentsList.add(hashMap);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void topTabsViewListnersAnimation() {
        View view = findViewById(R.id.include_swim_comp_custom_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimCompititionUpcomingEventsAcitivity.this, DashBoardActivity.class);
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

        ll_upcoming_meet.setOnClickListener(new OnClickListener() {

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
                SwimCompititionUpcomingEventsAcitivity.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });
        ll_register.setOnClickListener(new OnClickListener() {

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
                SwimCompititionUpcomingEventsAcitivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });
        ll_trophy_room.setOnClickListener(new OnClickListener() {

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
                SwimCompititionUpcomingEventsAcitivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }

}
