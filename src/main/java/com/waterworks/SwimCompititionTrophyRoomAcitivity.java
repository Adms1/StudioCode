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
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.SwimCompitition1Adapter;
import com.waterworks.adapter.TrophiesCountListAdapter;
import com.waterworks.asyncTasks.GetChildListAsyncTask;
import com.waterworks.asyncTasks.GetStudentRibbonCountAsyncTask;
import com.waterworks.asyncTasks.GetStudentTrophiesAsyncTask;
import com.waterworks.asyncTasks.StudentProgressLevelAsyncTask;
import com.waterworks.model.TokenStudentID;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwimCompititionTrophyRoomAcitivity extends Activity {
    String TAG = "SwimCampsActivity1";

    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room, llTrophyRoomRibonCountLayout, llTrophyList;
    private TextView txt_1, txt_2, txt_3, txtStudentName, txtParticipant, txtTimeImprovement, txtTotal, txtTrophyCount, txtTrophyStatus, txtLabelViewResults;
    private View selected_1, selected_2, selected_3;
    private GetStudentRibbonCountAsyncTask getStudentRibbonCountAsyncTask = null;
    private GetChildListAsyncTask getChildListAsyncTask = null;
    private GetStudentTrophiesAsyncTask getStudentTrophiesAsyncTask = null;
    private TokenStudentID tokenStudentID = null;
    private String token;
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> ribonCountList = new ArrayList<HashMap<String, String>>();
    private ArrayList<ArrayList<HashMap<String, String>>> trophyCountList = new ArrayList<ArrayList<HashMap<String, String>>>();
    private ArrayList<TokenStudentID> tokenStudentIDs = new ArrayList<>();
    private Context mContext = this;
    Boolean isInternetPresent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition_trophy_room);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        setTabAnimation();

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        childList = getChildListAndParseJson(token);
        isInternetPresent = Utility.isNetworkConnected(SwimCompititionTrophyRoomAcitivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            ribonCountList = getRibbonCountAsyncTask();
            trophyCountList = getStudentTrophiesAsyncTask();

            llTrophyRoomRibonCountLayout = (LinearLayout) findViewById(R.id.llTrophyRoomRibonCountLayout);
            Button btnHome = (Button) findViewById(R.id.btnHome);
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCheckin = new Intent(SwimCompititionTrophyRoomAcitivity.this, DashBoardActivity.class);
                    intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentCheckin);
                    finish();
                }
            });
            for (int i = 0; i < childList.size(); i++) {
                View child = getLayoutInflater().inflate(R.layout.layout_swim_comp_trophy_room, null, false);
                txtStudentName = (TextView) child.findViewById(R.id.txtStudentName);
                txtParticipant = (TextView) child.findViewById(R.id.txtParticipant);
                txtTimeImprovement = (TextView) child.findViewById(R.id.txtTimeImprovement);
                txtTotal = (TextView) child.findViewById(R.id.txtTotal);
                txtLabelViewResults = (TextView) child.findViewById(R.id.txtLabelViewResults);
                llTrophyList = (LinearLayout) child.findViewById(R.id.llTrophyList);

                for (HashMap<String, String> listMap : trophyCountList.get(i)) {
                    View childll = getLayoutInflater().inflate(R.layout.row_lst_trophy_count_swimcomp_trophies, null, false);
                    txtTrophyCount = (TextView) childll.findViewById(R.id.txtTrophyCount);
                    txtTrophyStatus = (TextView) childll.findViewById(R.id.txtTrophyStatus);
                    txtTrophyCount.setText(listMap.get("TrophySize") + " Ribbon Trophy");
                    if (listMap.get("TrophyStatus").equals("Earned")) {
                        txtTrophyStatus.setText(listMap.get("TrophyStatus") + "!");
                        txtTrophyStatus.setTypeface(null, Typeface.BOLD);
                        txtTrophyStatus.setTextColor(getResources().getColor(R.color.green_trophy_room));
                    } else {
                        txtTrophyStatus.setText(listMap.get("TrophyStatus") + " to go!");
                        txtTrophyStatus.setTextColor(Color.LTGRAY);
                    }
                    llTrophyList.addView(childll);
                }

                txtStudentName.setText(childList.get(i).get("SFirstName") + " " + childList.get(i).get("SLastName"));
                txtParticipant.setText(ribonCountList.get(i).get("participation"));
                txtTimeImprovement.setText(ribonCountList.get(i).get("timeimprovement"));
                txtTotal.setText(ribonCountList.get(i).get("totalribbons"));
                txtLabelViewResults.setTag(childList.get(i).get("Studentid"));
                txtLabelViewResults.setOnClickListener(myClickLIstener);

                llTrophyRoomRibonCountLayout.addView(child);
            }
        }
    }

    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SwimCompititionTrophyRoomResultsAcitivity.class);
            intent.putExtra("studentID", v.getTag().toString());
            startActivity(intent);
        }
    };

    private ArrayList<ArrayList<HashMap<String, String>>> getStudentTrophiesAsyncTask(){
        String s = null;
        ArrayList<ArrayList<HashMap<String, String>>> trophyCountList = new ArrayList<ArrayList<HashMap<String, String>>>();
        try {
            for(int i = 0;i < childList.size();i++){
                tokenStudentID = new TokenStudentID();
                tokenStudentID.setToken(token);
                tokenStudentID.setStudentID(childList.get(i).get("Studentid"));
                tokenStudentIDs.add(tokenStudentID);

                getStudentTrophiesAsyncTask = new GetStudentTrophiesAsyncTask(this, tokenStudentID);
                s = getStudentTrophiesAsyncTask.execute().get();
                trophyCountList.add(readAndParseGetStudentTrophyCountJSON(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trophyCountList;
    }

    private ArrayList<HashMap<String, String>> readAndParseGetStudentTrophyCountJSON(String resposeString){
        ArrayList<HashMap<String, String>> trophyList = new ArrayList<HashMap<String, String>>();

        try {
            JSONObject reader = new JSONObject(resposeString);
            String successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("TrophyList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<>();
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String Studentid = jsonChildNode.getString("TrophySize").trim();
                    String SFirstName = jsonChildNode.getString("TrophyStatus").trim();

                    hashmap.put("TrophySize", Studentid);
                    hashmap.put("TrophyStatus", SFirstName);
                    trophyList.add(hashmap);
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trophyList;
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
    private ArrayList<HashMap<String, String>> getRibbonCountAsyncTask() {
        String s = null;
        ArrayList<HashMap<String, String>> ribonCountList = new ArrayList<HashMap<String, String>>();
        try {
            for(int i = 0;i < childList.size();i++){
                tokenStudentID = new TokenStudentID();
                tokenStudentID.setToken(token);
                tokenStudentID.setStudentID(childList.get(i).get("Studentid"));
                tokenStudentIDs.add(tokenStudentID);

                getStudentRibbonCountAsyncTask = new GetStudentRibbonCountAsyncTask(this, tokenStudentID);
                s = getStudentRibbonCountAsyncTask.execute().get();
                ribonCountList.add(readAndParseGetStudentRibbonCountJSON(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ribonCountList;
    }

    private HashMap<String, String> readAndParseGetStudentRibbonCountJSON(String resposeString){
        HashMap<String, String> hashmap = new HashMap<String, String>();

        try {
            JSONObject reader = new JSONObject(resposeString);
            String successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("RibbonCount");

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String Studentid = jsonChildNode.getString("participation").trim();
                    String SFirstName = jsonChildNode.getString("timeimprovement").trim();
                    String SLastName = jsonChildNode.getString("totalribbons").trim();

                    hashmap.put("participation", Studentid);
                    hashmap.put("timeimprovement", SFirstName);
                    hashmap.put("totalribbons", SLastName);

                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashmap;
    }

    private ArrayList<HashMap<String, String>> getChildListAndParseJson(String token) {
        ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> paramsStudentList = new HashMap<>();
        paramsStudentList.put("Token", token);

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childList;
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
                SwimCompititionTrophyRoomAcitivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                SwimCompititionTrophyRoomAcitivity.this.overridePendingTransition(R.anim.fade_in, R.anim.zoom_in);
            }
        });

        ll_upcoming_meet = (LinearLayout) view.findViewById(R.id.ll_upcoming_meet);
        ll_register = (LinearLayout) view.findViewById(R.id.ll_register);
        ll_trophy_room = (LinearLayout) view.findViewById(R.id.ll_trophy_room);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        if(AppConfiguration.CheckMeet.equalsIgnoreCase("1")){
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
                SwimCompititionTrophyRoomAcitivity.this.overridePendingTransition(0, 0);
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
                SwimCompititionTrophyRoomAcitivity.this.overridePendingTransition(0, 0);
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
                SwimCompititionTrophyRoomAcitivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }

    private void setTabAnimation(){
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
