package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.AllEventListOfStudentByMeetAsyncTask;
import com.waterworks.asyncTasks.EventListOfStudentByMeetAsyncTask;
import com.waterworks.asyncTasks.SwimCmpt_AllGetEventResultForStudentAsyncTask;
import com.waterworks.model.UpcomingAllEventStudListModel;
import com.waterworks.model.UpcomingEventResultsListModel;
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

public class UpcomingEventCompResultActivity extends Activity {

    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room;
    private TextView txt_1, txt_2, txt_3, txtStudentName, txtEventTitle, txtPlacement, txtTime, txtEventNumber, txtTimeImprovement, txtResultmsg;
    private LinearLayout llEventRow, llStudentList, llRowLayout;
    private View selected_1, selected_2, selected_3;
    private SwimCmpt_AllGetEventResultForStudentAsyncTask swimCmpt_allGetEventResultForStudentAsyncTask = null;
    private Context mContext = this;
    private String swimmeetid = "";
    private ProgressDialog progressDialog = null;
    private ArrayList<String> studIDs = new ArrayList<>(), studNames = new ArrayList<>();
    private ArrayList<UpcomingEventResultsListModel> upcomingEventResultsListModels = new ArrayList<>();
    Boolean isInternetPresent = false;

    String suceessResult, HeaderText;
    ArrayList<HashMap<String, String>> Resultmessage = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_event_comp_results);

        swimmeetid = getIntent().getStringExtra("swimmeetid");
        topTabsViewListnersAnimation();
        init();

        for (int i = 0; i < SwimCompititionUpcomingEventsAcitivity.studentsList.size(); i++) {
            studIDs.add(SwimCompititionUpcomingEventsAcitivity.studentsList.get(i).get("StudentID"));
            studNames.add(SwimCompititionUpcomingEventsAcitivity.studentsList.get(i).get("StudentName"));
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
//        for (int j = 0; j < studIDs.size(); j++) {
        isInternetPresent = Utility.isNetworkConnected(UpcomingEventCompResultActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new GetResultMsg().execute();
        }
//        }
        progressDialog.dismiss();
    }

    public void init() {
        llStudentList = (LinearLayout) findViewById(R.id.llStudentList);
        txtResultmsg = (TextView) findViewById(R.id.txtResultmsg);
    }

    public void fetchAndShowStudentWiseEventData(final String studID, final String studName) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    swimCmpt_allGetEventResultForStudentAsyncTask = new SwimCmpt_AllGetEventResultForStudentAsyncTask(UpcomingEventCompResultActivity.this, swimmeetid, studID);
                    upcomingEventResultsListModels = swimCmpt_allGetEventResultForStudentAsyncTask.execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUI(studID, studName);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    public void setUI(String studID, String studName) {
        View child = getLayoutInflater().inflate(R.layout.layout_comp_results, null, false);
        txtStudentName = (TextView) child.findViewById(R.id.txtStudentName);
        llEventRow = (LinearLayout) child.findViewById(R.id.llEventRow);

        txtStudentName.setText(studName);
        txtStudentName.setBackgroundColor(getResources().getColor(R.color.student_back));

        for (UpcomingEventResultsListModel upcomingEventResultsListModel : upcomingEventResultsListModels) {
            View childll = getLayoutInflater().inflate(R.layout.row_event_result, null, false);

            llRowLayout = (LinearLayout) childll.findViewById(R.id.llRowLayout);
            txtEventTitle = (TextView) childll.findViewById(R.id.txtEventTitle);
            txtPlacement = (TextView) childll.findViewById(R.id.txtPlacement);
            txtTime = (TextView) childll.findViewById(R.id.txtTime);
            txtEventNumber = (TextView) childll.findViewById(R.id.txtEventNumber);
            txtTimeImprovement = (TextView) childll.findViewById(R.id.txtTimeImprovement);

            txtEventNumber.setText("Event #" + upcomingEventResultsListModel.getEventNumber());
            txtEventTitle.setText(upcomingEventResultsListModel.getDistance()+" " + "-" +" "+upcomingEventResultsListModel.getStrokedescription());
            txtPlacement.setText(upcomingEventResultsListModel.getPlaceno());
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
            txtTimeImprovement.setText(upcomingEventResultsListModel.getTimeImprovement());
            llRowLayout.setTag(studID + ":" + upcomingEventResultsListModel.getEventNumber()
                    + ":" + swimmeetid + ":" + txtEventNumber.getText().toString() + ":" + txtEventTitle.getText().toString());
            llRowLayout.setOnClickListener(myClickLIstener);

            llEventRow.addView(childll);
        }

        llStudentList.addView(child);
    }

    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(mContext, UpcomingEventCompResultDetailActivity.class);
            intent.putExtra("studentID", v.getTag().toString());
            startActivity(intent);
        }
    };

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
                Intent intentCheckin = new Intent(UpcomingEventCompResultActivity.this, DashBoardActivity.class);
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
                UpcomingEventCompResultActivity.this.overridePendingTransition(0, 0);
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
                UpcomingEventCompResultActivity.this.overridePendingTransition(0, 0);
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
                UpcomingEventCompResultActivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }

    /*-------------------- 30/05/2017 new code megha --------------------------------- */
    public void load_Result() {
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(UpcomingEventCompResultActivity.this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("swimmeetid", swimmeetid);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmpt_AllEventList_MeetOverTime, param);
        readAndParseJSONRESULT(responseString);
    }

    public void readAndParseJSONRESULT(String result) {
        try {
            JSONObject reader = new JSONObject(result);
            suceessResult = reader.getString("Success");
            if (suceessResult.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EventTime");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put("header", jsonChildNode.getString("header"));
                    hashmap.put("RemainingTimeFlag", jsonChildNode.getString("RemainingTimeFlag"));
                    HeaderText = jsonChildNode.getString("header");
                    Resultmessage.add(hashmap);
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class GetResultMsg extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(UpcomingEventCompResultActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd != null) {
                pd.dismiss();
                if (suceessResult.equalsIgnoreCase("True")) {
                    if (Resultmessage.equals("")) {
                        txtResultmsg.setText("");
                        txtResultmsg.setVisibility(View.GONE);
                    } else
                        txtResultmsg.setText(HeaderText);
                    for (int i = 0; i < Resultmessage.size(); i++) {
                        if (Resultmessage.get(i).get("RemainingTimeFlag").equalsIgnoreCase("true")) {
                            for (int j = 0; j < studIDs.size(); j++) {
                                fetchAndShowStudentWiseEventData(studIDs.get(j), studNames.get(j));
                            }
                        } else {
                            Toast.makeText(mContext, "Currently, results are not updated, Please check after some time.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            load_Result();
            return null;
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
