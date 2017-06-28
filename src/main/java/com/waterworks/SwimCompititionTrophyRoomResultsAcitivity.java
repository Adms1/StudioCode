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
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.GetChildListAsyncTask;
import com.waterworks.asyncTasks.GetEventNameAsyncTask;
import com.waterworks.asyncTasks.GetStudentEventTimingAsyncTask;
import com.waterworks.asyncTasks.GetStudentRibbonCountAsyncTask;
import com.waterworks.asyncTasks.GetStudentTrophiesAsyncTask;
import com.waterworks.model.EventListModel;
import com.waterworks.model.TokenStudentID;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SwimCompititionTrophyRoomResultsAcitivity extends Activity {
    String TAG = "SwimCampsActivity1";

    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room, llEventList, llTabs;
    private TextView txt_1, txt_2, txt_3, txtEventDate, txtEventTime, txtEventTimeImprovement;
    private View selected_1, selected_2, selected_3;
    private Spinner spinSwimStyle;
    private TokenStudentID tokenStudentID = new TokenStudentID();
    private String token, studentID;
    private Context mContext = this;
    private GetChildListAsyncTask getChildListAsyncTask = null;
    private GetStudentEventTimingAsyncTask getStudentEventTimingAsyncTask = null;
    private GetEventNameAsyncTask getEventNameAsyncTask = null;
    private ArrayList<EventListModel> arryListEventList = null;
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> listEventName = null;
    private ProgressDialog progress;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_comp_trophy_room_results);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        setTabAnimation();

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        studentID = getIntent().getStringExtra("studentID");
        tokenStudentID.setToken(token);
        tokenStudentID.setStudentID(studentID);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimCompititionTrophyRoomResultsAcitivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        childList = getChildListAndParseJson(token);
        makeTabs(studentID);
        isInternetPresent = Utility.isNetworkConnected(SwimCompititionTrophyRoomResultsAcitivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            fetchdata(tokenStudentID);
        }
    }

    public void fetchdata(TokenStudentID tokenStudentID) {
        try {
            getStudentEventTimingAsyncTask = new GetStudentEventTimingAsyncTask(this, tokenStudentID);
            String responseString = getStudentEventTimingAsyncTask.execute().get();
            parseStudentEventJson(responseString);
            makeList();
            fillSpinner(tokenStudentID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeTabs(String studentID) {
        llTabs = (LinearLayout) findViewById(R.id.llTabs);


        for (int i = 0; i < childList.size(); i++) {
            View childTabs = getLayoutInflater().inflate(R.layout.layout_tabs, null, false);
            TextView txtStudentNameTabs = (TextView) childTabs.findViewById(R.id.txtStudentNameTabs);
            View viewOrangeBar = (View) childTabs.findViewById(R.id.viewOrangeBar);

            txtStudentNameTabs.setText(childList.get(i).get("SFirstName"));
            childTabs.setTag(childList.get(i).get("Studentid"));
            childTabs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            childTabs.setOnClickListener(myClickLIstener);

            if (!childTabs.getTag().equals(studentID)) {
                txtStudentNameTabs.setTextColor(Color.BLACK);
                childTabs.setBackgroundColor(getResources().getColor(R.color.student_back));
                viewOrangeBar.setBackgroundColor(getResources().getColor(R.color.student_back));
            }

            llTabs.addView(childTabs);
        }
    }

    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(final View v) {
            tokenStudentID.setToken(token);
            tokenStudentID.setStudentID(v.getTag().toString());
            llEventList.removeAllViews();
            listEventName.clear();
            llTabs.removeAllViews();
            makeTabs(v.getTag().toString());
            isInternetPresent = Utility.isNetworkConnected(SwimCompititionTrophyRoomResultsAcitivity.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                fetchdata(tokenStudentID);
            }
        }
    };

    private ArrayList<HashMap<String, String>> getChildListAndParseJson(String token) {
        ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> paramsStudentList = new HashMap<>();
        paramsStudentList.put("Token", token);

        try {
            isInternetPresent = Utility.isNetworkConnected(SwimCompititionTrophyRoomResultsAcitivity.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                getChildListAsyncTask = new GetChildListAsyncTask(this, paramsStudentList);
                String sJson = getChildListAsyncTask.execute().get();
                JSONObject reader = new JSONObject(sJson);
                String successViewCertificate = reader.getString("Success");
                if (successViewCertificate.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("ProgresRptList");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        String Studentid = jsonChildNode.getString("Studentid").trim();
                        String SFirstName = jsonChildNode.getString("SFirstName").trim();
                        String SLastName = jsonChildNode.getString("SLastName").trim();

                        hashmap.put("Studentid", Studentid);
                        hashmap.put("SFirstName", SFirstName);
                        hashmap.put("SLastName", SLastName);

                        childList.add(hashmap);
                    }
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childList;
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

    public void parseEventNameJson(String jsonString) {
        listEventName = new ArrayList<>();
        listEventName.add("-Select Event to Filter-");
        try {
            JSONObject reader = new JSONObject(jsonString);
            String successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray ArrayNodeEventList = reader.optJSONArray("EventList");

                for (int i = 0; i < ArrayNodeEventList.length(); i++) {
                    JSONObject jsonChildNodeEventList = ArrayNodeEventList.getJSONObject(i);
                    String eventName = jsonChildNodeEventList.getString("EventName").trim();
                    listEventName.add(eventName);
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillSpinner(TokenStudentID tokenStudentID) {
        try {
            getEventNameAsyncTask = new GetEventNameAsyncTask(this, tokenStudentID);
            String responseStr = getEventNameAsyncTask.execute().get();
            parseEventNameJson(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        spinSwimStyle = (Spinner) findViewById(R.id.spinSwimStyle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, listEventName);
        spinSwimStyle.setAdapter(adapter);

        spinSwimStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String style = spinSwimStyle.getItemAtPosition(position).toString();
                makeSpinnerSelectedList(style);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void makeSpinnerSelectedList(String style) {
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);
        llEventList.removeAllViews();
        for (EventListModel eventListModel : arryListEventList) {
            if (eventListModel.getEventName().startsWith(style)) {
                TextView txtEventName = new TextView(mContext);
                txtEventName.setText(eventListModel.getEventName());
                txtEventName.setTextColor(Color.BLACK);
                txtEventName.setBackgroundResource(R.drawable.gray_border);
                txtEventName.setPadding(padding, padding, padding, padding);
                txtEventName.setTypeface(null, Typeface.BOLD);
                llEventList.addView(txtEventName);

                for (int j = 0; j < eventListModel.getEventDetails().size(); j++) {
                    View childll = getLayoutInflater().inflate(R.layout.row_event_list, null, false);
                    txtEventDate = (TextView) childll.findViewById(R.id.txtEventDate);
                    txtEventTime = (TextView) childll.findViewById(R.id.txtEventTime);
                    txtEventTimeImprovement = (TextView) childll.findViewById(R.id.txtEventTimeImprovement);

                    txtEventDate.setText(eventListModel.getEventDetails().get(j).get("MeetDate"));
                    txtEventTime.setText(eventListModel.getEventDetails().get(j).get("MeetTime"));
                    txtEventTimeImprovement.setText("(" + eventListModel.getEventDetails().get(j).get("TimeImprovement") + ")");

                    if (txtEventTimeImprovement.getText().toString().contains("-")) {
                        txtEventTimeImprovement.setTextColor(getResources().getColor(R.color.green_trophy_room_details));
                        txtEventTimeImprovement.setTypeface(null, Typeface.BOLD);
                    } else if (txtEventTimeImprovement.getText().toString().contains("+")) {
                        txtEventTimeImprovement.setTextColor(Color.RED);
                    }
                    llEventList.addView(childll);
                }
            }
        }
    }

    public void makeList() {
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);
        llEventList = (LinearLayout) findViewById(R.id.llEventList);

        for (int i = 0; i < arryListEventList.size(); i++) {

            TextView txtEventName = new TextView(mContext);
            txtEventName.setText(arryListEventList.get(i).getEventName());
            txtEventName.setTextColor(Color.BLACK);
            txtEventName.setBackgroundResource(R.drawable.gray_border);
            txtEventName.setPadding(padding, padding, padding, padding);
            txtEventName.setTypeface(null, Typeface.BOLD);
            llEventList.addView(txtEventName);

            for (int j = 0; j < arryListEventList.get(i).getEventDetails().size(); j++) {
                View childll = getLayoutInflater().inflate(R.layout.row_event_list, null, false);
                txtEventDate = (TextView) childll.findViewById(R.id.txtEventDate);
                txtEventTime = (TextView) childll.findViewById(R.id.txtEventTime);
                txtEventTimeImprovement = (TextView) childll.findViewById(R.id.txtEventTimeImprovement);

                txtEventDate.setText(arryListEventList.get(i).getEventDetails().get(j).get("MeetDate"));
                txtEventTime.setText(arryListEventList.get(i).getEventDetails().get(j).get("MeetTime"));
                txtEventTimeImprovement.setText("(" + arryListEventList.get(i).getEventDetails().get(j).get("TimeImprovement") + ")");

                if (txtEventTimeImprovement.getText().toString().contains("-")) {
                    txtEventTimeImprovement.setTextColor(getResources().getColor(R.color.green_trophy_room_details));
                    txtEventTimeImprovement.setTypeface(null, Typeface.BOLD);
                } else if (txtEventTimeImprovement.getText().toString().contains("+")) {
                    txtEventTimeImprovement.setTextColor(Color.RED);
                }

                llEventList.addView(childll);
            }
        }
    }

    public void parseStudentEventJson(String responseString) {
        EventListModel eventListModel = null;
        arryListEventList = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(responseString);
            String successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray ArrayNodeEventList = reader.optJSONArray("EventList");

                for (int i = 0; i < ArrayNodeEventList.length(); i++) {
                    eventListModel = new EventListModel();
                    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
                    JSONObject jsonChildNodeEventList = ArrayNodeEventList.getJSONObject(i);

                    String eventName = jsonChildNodeEventList.getString("EventName").trim();
                    eventListModel.setEventName(eventName);
                    JSONArray ArrayNodeEvents = jsonChildNodeEventList.optJSONArray("Events");
                    for (int j = 0; j < ArrayNodeEvents.length(); j++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        JSONObject jsonChildNodeEvents = ArrayNodeEvents.getJSONObject(j);

                        String meetDate = jsonChildNodeEvents.getString("MeetDate").trim();
                        String meetTime = jsonChildNodeEvents.getString("MeetTime").trim();
                        String timeImprovement = jsonChildNodeEvents.getString("TimeImprovement").trim();
                        hashMap.put("MeetDate", meetDate);
                        hashMap.put("MeetTime", meetTime);
                        hashMap.put("TimeImprovement", timeImprovement);
                        hashMapArrayList.add(hashMap);
                    }
                    eventListModel.setEventDetails(hashMapArrayList);
                    arryListEventList.add(eventListModel);
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTabNavigation() {
        // TODO Auto-generated method stub

        View view = findViewById(R.id.include_swim_comp_custom_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        ll_upcoming_meet = (LinearLayout) view.findViewById(R.id.ll_upcoming_meet);
        ll_register = (LinearLayout) view.findViewById(R.id.ll_register);
        ll_trophy_room = (LinearLayout) view.findViewById(R.id.ll_trophy_room);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        if (AppConfiguration.CheckMeet.equalsIgnoreCase("1")) {
            txt_1.setText("Todays's Meet");
        }

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
                SwimCompititionTrophyRoomResultsAcitivity.this.overridePendingTransition(0, 0);
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
                SwimCompititionTrophyRoomResultsAcitivity.this.overridePendingTransition(0, 0);
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

                Intent i = new Intent(mContext, SwimCompititionTrophyRoomResultsAcitivity.class);
                startActivity(i);
                SwimCompititionTrophyRoomResultsAcitivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }

    private void setTabAnimation() {
        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        selected_1.setVisibility(View.GONE);
        selected_2.setVisibility(View.GONE);
        selected_3.setVisibility(View.VISIBLE);

        ((AnimationDrawable) selected_3.getBackground()).start();

        initTabNavigation();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
    }
}
