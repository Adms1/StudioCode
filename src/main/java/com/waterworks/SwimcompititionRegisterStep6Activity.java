package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.GetMemberAsyncTask;
import com.waterworks.asyncTasks.SwimCompetitionAddToCartAsyncTask;
import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwimcompititionRegisterStep6Activity extends Activity {

    private TextView txtCompititionVal, txtChildName, txtStrokeType, txtEventType,
            txtCompititionVal2, txtCompititionVal3, txtCompititionVal4, confirm_step6checkTxt, confirm_step6chk, confirm_step6check;
    private CardView btn_swim_confirm;
    private LinearLayout llEventList, llTabs;
    private TextView EventListTxt;
    private ListView list;
    private Context mContext = this;
    public static ArrayList<String> xyz = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> tempid = new ArrayList<>();
    ArrayList<String> tempname = new ArrayList<>();
    private String token, familyID, basketID, siteID, DateValue, eventdates, time, MeetDate_Display, eventData, memberString, wwmember;
    private ArrayList<String> eventDataDisplayRow1, eventDataDisplayRow2;
    private SharedPreferences prefs;
    private String TAG = "SwimcompititionRegisterStep6Activity";
    private GetMemberAsyncTask getMemberAsyncTask = null;
    private SwimCompetitionAddToCartAsyncTask swimCompetitionAddToCartAsyncTask = null;
    private ProgressDialog progressDialog = null;
    private String str, currentStudentID, selectedStudent, selectedStudentName;
    String[] eventStroke;
    public int previousTab, CurrentTab;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition6_new);

        prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        siteID = AppConfiguration.siteID;
        basketID = Utility.getBasketID(this, siteID);

        Intent intent = getIntent();
        if (null != intent) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue = intent.getStringExtra("datevalue");
            time = intent.getStringExtra("time");
            MeetDate_Display = intent.getStringExtra("MeetDate_Display");
            Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID + "\nDateValue=" + DateValue + "\nTime=" + time);
        }
        previousTab = 1;
        CurrentTab = 1;
        initViews();
        Animation animSlidInLeft = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
        llEventList.startAnimation(animSlidInLeft);
        setListners();
        String[] tempEventList = AppConfiguration.SelectedEventDataStep2.split(",");
        List<String> newTempEventList = new ArrayList<String>(Arrays.asList(tempEventList));

        String[] tempnew = AppConfiguration.swimComptitionStudentIDName.split("\\,");
        for (int i = 0; i < tempnew.length; i++) {
            String temp = tempnew[i];
            String[] tempagain = temp.split("\\|");
            for (int j = 0; j < newTempEventList.size(); j++) {
                if (newTempEventList.get(j).toString().contains(tempagain[0].toString())) {
                    if (!tempid.contains(tempagain[0]))
                        tempid.add(tempagain[0]);
                    if (!tempname.contains(tempagain[1]))
                        tempname.add(tempagain[1]);
                }
            }
        }
        makeTabs(tempid.get(0).toString());
        currentStudentID = tempid.get(0).toString();

//        06-06-2017 megha
        String eventdisplay = AppConfiguration.SelectedEventDataStep2.toString().trim().replaceFirst(",", "");
        eventData = eventdisplay;

        eventDataDisplayRow1 = AppConfiguration.selectedStudent1;
        makeEventList();
        Log.d("meghaevent", "" + eventDataDisplayRow1);
        Log.e("SelectedEventDataStep2", "" + AppConfiguration.SelectedEventDataStep2);
        /*---------------------------------- 05/01/2016 megha------------------------ */
        String[] meetday = MeetDate_Display.split("\\-");
        String[] infotime = time.split("\\@");
        txtCompititionVal.setText(meetday[0]);
        txtCompititionVal3.setText(infotime[1] + "\t" + "Facility");
        String[] infoSplit = DateValue.split("\\s+");
        String[] infoid = infoSplit[2].split("\\|");
        String[] displaytime = infoSplit[1].split(":");

        txtCompititionVal2.setText(displaytime[0] + ":" + displaytime[1] + "\t" + infoid[0]);

        txtCompititionVal4.setText(AppConfiguration.siteAddress + "," + infotime[1]);
        isInternetPresent = Utility.isNetworkConnected(SwimcompititionRegisterStep6Activity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            setGetMemberAsyncTask();
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

    public void makeEventList() {

        for (String event : eventDataDisplayRow1) {
            String[] separetevent = event.split(",");

            String[] eventArray = separetevent[0].split("\\|");

            if (eventArray[0].equalsIgnoreCase(currentStudentID)) {
                View viewEvent = getLayoutInflater().inflate(R.layout.swim_comp_summary_event_list_layout, null, false);
                txtStrokeType = (TextView) viewEvent.findViewById(R.id.txtStrokeType);
                txtEventType = (TextView) viewEvent.findViewById(R.id.txtEventType);

                String[] eventStroke = eventArray[1].split("\\n");
                txtStrokeType.setText(eventStroke[0]);
                txtEventType.setText(eventStroke[1]);
                llEventList.addView(viewEvent);
            }
        }
        if (previousTab == CurrentTab) {
            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
            llEventList.startAnimation(animSlidInRight);
        }
        if (CurrentTab < previousTab) {
            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
            llEventList.startAnimation(animSlidInRight);
            previousTab = CurrentTab;
        } else if (CurrentTab > previousTab) {
            previousTab = CurrentTab;
            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
            llEventList.startAnimation(animSlidInRight);
        }
    }

    public void initViews() {
        setTitleBar();
        txtCompititionVal = (TextView) findViewById(R.id.txtCompititionVaL);
        btn_swim_confirm = (CardView) findViewById(R.id.btn_swim_camps_regi1_students);
        llEventList = (LinearLayout) findViewById(R.id.llEventList);
        txtCompititionVal2 = (TextView) findViewById(R.id.txtCompititionVaL2);
        txtCompititionVal3 = (TextView) findViewById(R.id.txtCompititionVaL3);
        txtCompititionVal4 = (TextView) findViewById(R.id.txtCompititionVaL4);
        // =====================================       05/01/2017 megha
        confirm_step6checkTxt = (TextView) findViewById(R.id.confirm_step6checkTxt);
        confirm_step6check = (TextView) findViewById(R.id.confirm_step6check);

        confirm_step6chk = (CheckBox) findViewById(R.id.confirm_step6chk);
        list = (ListView) findViewById(R.id.lv_dt2_list);
        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
        slide_up.setDuration(400);
        confirm_step6checkTxt.setAnimation(slide_up);
        confirm_step6chk.setAnimation(slide_up);
        confirm_step6checkTxt.setText(Html.fromHtml("Please remember to check-in online or by phone 1<br> hour prior to the competition start time"));
        //===========================================
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait!!!");

        Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
        llEventList.startAnimation(animSlidInRight);
    }

    //    06/01/2017 megha
    public void makeTabs(String studentID) {
        llTabs = (LinearLayout) findViewById(R.id.llTabs);
        for (int i = 0; i < tempid.size(); i++) {
            View childTabs = getLayoutInflater().inflate(R.layout.layout_tabs, null, false);
            TextView txtStudentNameTabs = (TextView) childTabs.findViewById(R.id.txtStudentNameTabs);
            View viewOrangeBar = (View) childTabs.findViewById(R.id.viewOrangeBar);

            if (previousTab == CurrentTab) {
                Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
                viewOrangeBar.startAnimation(animSlidInRight);
            }
            if (CurrentTab < previousTab) {
                Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
                viewOrangeBar.startAnimation(animSlidInRight);
            } else if (CurrentTab > previousTab) {
                Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
                viewOrangeBar.startAnimation(animSlidInRight);
            }
            int temp = i + 1;
            String Tabno = Integer.toString(temp);
            String[] tempnamesplit = tempname.get(i).split("\\s+");

            txtStudentNameTabs.setText(tempnamesplit[0]);
            childTabs.setTag(tempid.get(i).toString() + "|" + Tabno.toString());
            childTabs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            childTabs.setOnClickListener(myClickLIstener);

            String studentID1[] = childTabs.getTag().toString().trim().split("\\|");

            if (!studentID1[0].equals(studentID)) {
                txtStudentNameTabs.setTextColor(getResources().getColor(R.color.design_change_d2));
                childTabs.setBackgroundColor(getResources().getColor(R.color.student_back));
                viewOrangeBar.setBackgroundColor(getResources().getColor(R.color.student_back));
            } else {
                txtStudentNameTabs.setTextColor(getResources().getColor(R.color.orange));
                childTabs.setBackgroundColor(getResources().getColor(R.color.student_back));
                viewOrangeBar.setBackgroundColor(getResources().getColor(R.color.orange));
            }
            llTabs.addView(childTabs);
        }
    }

    //06/01/2017 megha
    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(final View v) {
            llTabs.removeAllViews();
            String studnetTab[] = v.getTag().toString().trim().split("\\|");
            currentStudentID = studnetTab[0];
            int SelectedTabno = Integer.parseInt(studnetTab[1]);
            CurrentTab = SelectedTabno;

            makeTabs(currentStudentID);

            llEventList.removeAllViews();
            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
            llEventList.startAnimation(animSlidInRight);
            makeEventList();
        }
    };

    //05/01/2017 megha
    public void setListners() {
        confirm_step6chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {

                    confirm_step6chk.setBackgroundResource(R.drawable.custom_check_orange);

                    btn_swim_confirm.postDelayed(new Runnable() {
                        public void run() {
                            Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                            slide_up.setDuration(300);
                            btn_swim_confirm.setAnimation(slide_up);
                            btn_swim_confirm.setVisibility(View.VISIBLE);
                            confirm_step6checkTxt.setVisibility(View.GONE);
                            confirm_step6chk.setVisibility(View.GONE);
                            confirm_step6check.setVisibility(View.GONE);
                        }
                    }, 300);

                } else {
                    confirm_step6chk.setBackgroundResource(R.color.design_change_d2);
                }
            }
        });
        btn_swim_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HashMap<String, String> param = new HashMap<String, String>();

                            param.put("Token", token);
                            param.put("MeetdatetimeValue", DateValue);
                            param.put("NoofVolunteersvalue", "0");
                            param.put("flag", "0");
                            param.put("swimmeetid", "0");
                            param.put("basketid", basketID);
                            param.put("ChkChildListStp1", getChkChildListStp1Value());
                            param.put("RdbChildLstStp3_1", AppConfiguration.step3_jumpingwall);
                            param.put("RdbChildLstStp3_2", AppConfiguration.step3_endlane);
                            param.put("member", memberString);
                            param.put("SelectedEventDataStep2", eventData);

                            param.put("FirstName1", "");
                            param.put("LastName1", "");
                            param.put("RdbComp1", "0");
                            param.put("RdbMeet1", "0");

                            param.put("FirstName2", "");
                            param.put("LastName2", "");
                            param.put("RdbComp2", "0");
                            param.put("RdbMeet2", "0");

                            param.put("FirstName3", "");
                            param.put("LastName3", "");
                            param.put("RdbComp3", "0");
                            param.put("RdbMeet3", "0");

                            param.put("FirstName4", "");
                            param.put("LastName4", "");
                            param.put("RdbComp4", "0");
                            param.put("RdbMeet4", "0");

                            param.put("FirstName5", "");
                            param.put("LastName5", "");
                            param.put("RdbComp5", "0");
                            param.put("RdbMeet5", "0");

                            param.put("FirstName6", "");
                            param.put("LastName6", "");
                            param.put("RdbComp6", "0");
                            param.put("RdbMeet6", "0");

                            param.put("FirstName7", "");
                            param.put("LastName7", "");
                            param.put("RdbComp7", "0");
                            param.put("RdbMeet7", "0");

                            param.put("FirstName8", "");
                            param.put("LastName8", "");
                            param.put("RdbComp8", "0");
                            param.put("RdbMeet8", "0");

                            param.put("FirstName9", "");
                            param.put("LastName9", "");
                            param.put("RdbComp9", "0");
                            param.put("RdbMeet9", "0");

                            param.put("FirstName10", "");
                            param.put("LastName10", "");
                            param.put("RdbComp10", "0");
                            param.put("RdbMeet10", "0");

                            swimCompetitionAddToCartAsyncTask = new SwimCompetitionAddToCartAsyncTask(param);
                            String resposeString = swimCompetitionAddToCartAsyncTask.execute().get();
                            try {
                                JSONObject reader = new JSONObject(resposeString);
                                String successAddToCart = reader.getString("Success");
                                if (successAddToCart.toString().equals("True")) {

                                    AppConfiguration.fromSwimCompetition = "true";
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Intent viewcart = new Intent(getApplicationContext(), ByMoreMyCart.class);
//                                            viewcart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(viewcart);
//                                            finish();
                                        }
                                    });

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Some internal error, Please try after sometime.", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    public void setTitleBar() {
        View view = findViewById(R.id.layout_top);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Review");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);

        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ClearArray();
                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfiguration.global_StudIDChecked.clear();
                Intent intentCheckin = new Intent(SwimcompititionRegisterStep6Activity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();

            }
        });
    }

    public void setGetMemberAsyncTask() {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("Token", token);
                    param.put("MeetdatetimeValue", DateValue);
                    param.put("ChkChildListStp1", getChkChildListStp1Value());
                    param.put("ChkChilsWiseDtlStp2", "" + eventData);
                    param.put("EventCounter", "" + eventData.split(",").length);
                    getMemberAsyncTask = new GetMemberAsyncTask(param);
                    String responseString = getMemberAsyncTask.execute().get();
                    readAndParseJSONReminderText(responseString);
                    memberString = prepareMemberString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void readAndParseJSONReminderText(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            String successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck5_EventCalc");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    wwmember = jsonChildNode.getString("wwmember").trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getChkChildListStp1Value() {
        String ChkChildListStp1 = "";
        for (int i = 0; i < tempname.size(); i++) {
            if (i == 0) {
                ChkChildListStp1 = tempid.get(i) + ":" + tempname.get(i);
            } else {
                ChkChildListStp1 = ChkChildListStp1 + "," + tempid.get(i) + ":" + tempname.get(i);
            }
        }
        return ChkChildListStp1;
    }

    public String prepareMemberString() {
        String member = "";
        for (int i = 0; i < tempid.size(); i++) {
            if (i == 0) {
                member = wwmember + "|" + tempid.get(i);
            } else {
                member = member + "," + wwmember + "|" + tempid.get(i);
            }
        }
        return member;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        AppConfiguration.global_StudIDChecked.clear();
        finish();
    }
}
