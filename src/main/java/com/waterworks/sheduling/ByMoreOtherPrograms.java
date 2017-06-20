package com.waterworks.sheduling;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.waterworks.DashBoardActivity;
import com.waterworks.DivesAndTurnsRegi1Activity;
import com.waterworks.FamilySwimNightActivity;
import com.waterworks.JuniorLifeGuardRegister1;
import com.waterworks.LapSwimsActivity;
import com.waterworks.R;
import com.waterworks.ScoutBadgeActivityRegister1;
import com.waterworks.ScoutBadgeGirlsRegister1;
import com.waterworks.ScoutBadgeMeritRegister1;
import com.waterworks.SwimCampsRegister1Acitivity;
import com.waterworks.SwimCompititionRegisterAcitivity;
import com.waterworks.SwimCompititionUpcomingEventsAcitivity;
import com.waterworks.SwimTeamActivity;
import com.waterworks.WaterAerobicsActivity;
import com.waterworks.WaterPoloRegister1;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

    public class ByMoreOtherPrograms extends Activity implements OnClickListener {
    Button BackButton, btn_viewCart, btn_swim_competition, btn_family_swim_night, btn_swim_teams, btn_swim_camps, btn_scout_Programs,
            btn_junior_lifg_prep, btn_dive_turns, btn_water_polo_clinics, btn_lap_swim, btn_water_aerobics, btn_merit_badge, btn_activity_badge;
    // LinearLayout scdl_lsn,scdl_mkp,waitlist;
    LinearLayout swimLsn, retailStore, otherPrograms;
    EditText tv_fsn_info;
    View selected_1, selected_2, selected_3;
    TextView txt_1, txt_2, txt_3, tv_adult;
    boolean running = false;
    Context mContext = this;

    String token, familyID;
    String SwimCompFlag, FamilySwimFlag, SwimTeamFlag, SwimCampFlag, GirlScoutFlag, ActivityBadgeFlag, MeritBadgeFlag,
            LifeGuardFlag, DivesTurnsFlag, WaterPoloFlag;
    ArrayList<HashMap<String, String>> programArrayList = new ArrayList<HashMap<String, String>>();

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
    }

    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more_lessions_otherprograms);
        AppConfiguration.cartBackScreen = 2;
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        selected_1.setVisibility(View.GONE);
        selected_2.setVisibility(View.GONE);
        selected_3.setVisibility(View.VISIBLE);

        ((AnimationDrawable) selected_3.getBackground()).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isInternetPresent = Utility.isNetworkConnected(ByMoreOtherPrograms.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    InitialRequests();
                }
                init();
            }
        }, 1000);
        typeFace();
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

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void InitialRequests() {
        try {
            new ProgramListAsyncTask().execute();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


    }

    public void init() {

        View view = findViewById(R.id.schdl_top);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);

        btn_swim_competition = (Button) findViewById(R.id.btn_swim_competition);
        btn_family_swim_night = (Button) findViewById(R.id.btn_family_swim_night);
        btn_swim_teams = (Button) findViewById(R.id.btn_swim_teams);
        btn_swim_camps = (Button) findViewById(R.id.btn_swim_camps);
        btn_scout_Programs = (Button) findViewById(R.id.btn_scout_Programs);
        btn_junior_lifg_prep = (Button) findViewById(R.id.btn_junior_lifg_prep);
        btn_dive_turns = (Button) findViewById(R.id.btn_dive_turns);
        btn_water_polo_clinics = (Button) findViewById(R.id.btn_water_polo_clinics);
        btn_lap_swim = (Button) findViewById(R.id.btn_lap_swim);
        btn_water_aerobics = (Button) findViewById(R.id.btn_water_aerobics);
        tv_adult = (TextView) findViewById(R.id.tv_adult);
        btn_merit_badge = (Button) findViewById(R.id.btn_merit_badge);
        btn_activity_badge = (Button) findViewById(R.id.btn_activity_badge);

        btn_swim_competition.setOnClickListener(this);
        btn_family_swim_night.setOnClickListener(this);
        btn_swim_teams.setOnClickListener(this);
        btn_swim_camps.setOnClickListener(this);
        btn_scout_Programs.setOnClickListener(this);
        btn_junior_lifg_prep.setOnClickListener(this);
        btn_dive_turns.setOnClickListener(this);
        btn_water_polo_clinics.setOnClickListener(this);
        btn_lap_swim.setOnClickListener(this);
        btn_water_aerobics.setOnClickListener(this);
        btn_merit_badge.setOnClickListener(this);
        btn_activity_badge.setOnClickListener(this);

        tv_fsn_info = (EditText) findViewById(R.id.tv_fsn_info);
        String text = getResources().getString(R.string.otherPrograms_header_text);
        tv_fsn_info.setText(Html.fromHtml("<body style=\"text-align:justify;color:gray;background-color:black;\">" + text
                + "</body>"));

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ByMoreOtherPrograms.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);
        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                startActivity(i);
            }
        });
        swimLsn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, BuyMoreSwimLession.class);
                startActivity(i);
                ByMoreOtherPrograms.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });

        retailStore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, BuyMoreRetailStore.class);
                startActivity(i);
                ByMoreOtherPrograms.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });

        otherPrograms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, ByMoreOtherPrograms.class);
                startActivity(i);
                ByMoreOtherPrograms.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == btn_swim_competition) {
            AppConfiguration.CheckMeet = Utility.getCheckMeet(token);
            Intent i = null;
            if (AppConfiguration.CheckMeet.equalsIgnoreCase("0")) {
                i = new Intent(ByMoreOtherPrograms.this, SwimCompititionRegisterAcitivity.class);

            } else if (AppConfiguration.CheckMeet.equalsIgnoreCase("1")) {
                i = new Intent(ByMoreOtherPrograms.this, SwimCompititionUpcomingEventsAcitivity.class);

            } else if (AppConfiguration.CheckMeet.equalsIgnoreCase("2")) {
                i = new Intent(ByMoreOtherPrograms.this, SwimCompititionUpcomingEventsAcitivity.class);

            }
            startActivity(i);
        }

        if (v == btn_family_swim_night) {
            Intent i = new Intent(ByMoreOtherPrograms.this, FamilySwimNightActivity.class);
            startActivity(i);
        }

        if (v == btn_swim_teams) {
            Intent i = new Intent(ByMoreOtherPrograms.this, SwimTeamActivity.class);
            startActivity(i);
        }

        if (v == btn_swim_camps) {
            Intent i = new Intent(ByMoreOtherPrograms.this, SwimCampsRegister1Acitivity.class);
            startActivity(i);
        }

        if (v == btn_scout_Programs) {
            Intent i = new Intent(ByMoreOtherPrograms.this, ScoutBadgeGirlsRegister1.class);
            startActivity(i);
        }
        if (v == btn_junior_lifg_prep) {
            Intent i = new Intent(ByMoreOtherPrograms.this, JuniorLifeGuardRegister1.class);
            startActivity(i);
        }

        if (v == btn_merit_badge) {
            Intent i = new Intent(ByMoreOtherPrograms.this, ScoutBadgeMeritRegister1.class);
            startActivity(i);
        }

        if (v == btn_activity_badge) {
            Intent i = new Intent(ByMoreOtherPrograms.this, ScoutBadgeActivityRegister1.class);
            startActivity(i);
        }

        if (v == btn_dive_turns) {
            Intent i = new Intent(ByMoreOtherPrograms.this, DivesAndTurnsRegi1Activity.class);
            startActivity(i);
        }

        if (v == btn_water_polo_clinics) {
            Intent i = new Intent(ByMoreOtherPrograms.this, WaterPoloRegister1.class);
            startActivity(i);
        }

        if (v == btn_lap_swim) {
            Intent i = new Intent(ByMoreOtherPrograms.this, LapSwimsActivity.class);
            startActivity(i);
        }

        if (v == btn_water_aerobics) {
            Intent i = new Intent(ByMoreOtherPrograms.this, WaterAerobicsActivity.class);
            startActivity(i);
        }
    }


    class ProgramListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            programArrayList.clear();

        }

        @Override
        protected Void doInBackground(Void... params) {
            loadProgramList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }

            if (SwimCompFlag.equalsIgnoreCase("1")) {

                btn_swim_competition.setVisibility(View.VISIBLE);
            } else {
                btn_swim_competition.setVisibility(View.GONE);
            }

            if (FamilySwimFlag.equalsIgnoreCase("1")) {

                btn_family_swim_night.setVisibility(View.VISIBLE);
            } else {
                btn_family_swim_night.setVisibility(View.GONE);
            }

            if (SwimTeamFlag.equalsIgnoreCase("1")) {

                btn_swim_teams.setVisibility(View.VISIBLE);
            } else {
                btn_swim_teams.setVisibility(View.GONE);
            }

            if (SwimCampFlag.equalsIgnoreCase("1")) {

                btn_swim_camps.setVisibility(View.VISIBLE);
            } else {
                btn_swim_camps.setVisibility(View.GONE);
            }

            if (GirlScoutFlag.equalsIgnoreCase("1")) {

                btn_scout_Programs.setVisibility(View.VISIBLE);
//                btn_scout_Programs.setVisibility(View.GONE);//removed as per suggestion by client : 30Jun2016
            } else {
                btn_scout_Programs.setVisibility(View.GONE);
            }

            if (MeritBadgeFlag.equalsIgnoreCase("1")) {
                btn_merit_badge.setVisibility(View.VISIBLE);
            } else {
                btn_merit_badge.setVisibility(View.GONE);
            }

            if (ActivityBadgeFlag.equalsIgnoreCase("1")) {
                btn_activity_badge.setVisibility(View.VISIBLE);
            } else {
                btn_activity_badge.setVisibility(View.GONE);
            }

            if (LifeGuardFlag.equalsIgnoreCase("1")) {

                btn_junior_lifg_prep.setVisibility(View.VISIBLE);
            } else {
                btn_junior_lifg_prep.setVisibility(View.GONE);
            }

            if (DivesTurnsFlag.equalsIgnoreCase("1")) {

                btn_dive_turns.setVisibility(View.VISIBLE);
                tv_adult.setVisibility(View.VISIBLE);
            } else {
                btn_dive_turns.setVisibility(View.GONE);
                tv_adult.setVisibility(View.GONE);
            }
            if (WaterPoloFlag.equalsIgnoreCase("1")) {

                btn_water_polo_clinics.setVisibility(View.VISIBLE);
            } else {
                btn_water_polo_clinics.setVisibility(View.GONE);
            }
            btn_lap_swim.setVisibility(View.VISIBLE);
            btn_water_aerobics.setVisibility(View.VISIBLE);

			/*
             * if(ActivityBadgeFlag.equalsIgnoreCase("1")){
			 * 
			 * btn_lap_swim.setVisibility(View.VISIBLE); } else{
			 * btn_lap_swim.setVisibility(View.GONE); }
			 * 
			 * if(MeritBadgeFlag.equalsIgnoreCase("1")){
			 * 
			 * btn_water_aerobics.setVisibility(View.VISIBLE);
			 * tv_adult.setVisibility(View.VISIBLE);
			 * 
			 * } else{ btn_water_aerobics.setVisibility(View.GONE);
			 * tv_adult.setVisibility(View.GONE); }
			 */

        }
    }

    public void loadProgramList() {
        programArrayList.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.ShowHidePrograms,
                param);
        Log.e("Program---responseString", responseString);

        readProgramAndParseJSON(responseString);
    }

    public void readProgramAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("SwimCompFlag", jsonChildNode.getString("SwimCompFlag"));
                hashmap.put("FamilySwimFlag", jsonChildNode.getString("FamilySwimFlag"));
                hashmap.put("SwimTeamFlag", jsonChildNode.getString("SwimTeamFlag"));
                hashmap.put("SwimCampFlag", jsonChildNode.getString("SwimCampFlag"));
                hashmap.put("GirlScoutFlag", jsonChildNode.getString("GirlScoutFlag"));
                hashmap.put("ActivityBadgeFlag", jsonChildNode.getString("ActivityBadgeFlag"));
                hashmap.put("MeritBadgeFlag", jsonChildNode.getString("MeritBadgeFlag"));
                hashmap.put("LifeGuardFlag", jsonChildNode.getString("LifeGuardFlag"));
                hashmap.put("DivesTurnsFlag", jsonChildNode.getString("DivesTurnsFlag"));
                hashmap.put("WaterPoloFlag", jsonChildNode.getString("WaterPoloFlag"));

                SwimCompFlag = jsonChildNode.getString("SwimCompFlag");
                FamilySwimFlag = jsonChildNode.getString("FamilySwimFlag");
                SwimTeamFlag = jsonChildNode.getString("SwimTeamFlag");
                SwimCampFlag = jsonChildNode.getString("SwimCampFlag");
                GirlScoutFlag = jsonChildNode.getString("GirlScoutFlag");
                ActivityBadgeFlag = jsonChildNode.getString("ActivityBadgeFlag");
                MeritBadgeFlag = jsonChildNode.getString("MeritBadgeFlag");
                LifeGuardFlag = jsonChildNode.getString("LifeGuardFlag");
                DivesTurnsFlag = jsonChildNode.getString("DivesTurnsFlag");
                WaterPoloFlag = jsonChildNode.getString("WaterPoloFlag");

                Log.e("SwimCompFlag----", "" + SwimCompFlag);
                Log.e("FamilySwimFlag----", "" + FamilySwimFlag);
                Log.e("SwimTeamFlag----", "" + SwimTeamFlag);
                programArrayList.add(hashmap);
                Log.e("programArrayList----", "" + programArrayList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intentCheckin = new Intent(ByMoreOtherPrograms.this, DashBoardActivity.class);
        intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentCheckin);
        finish();
        /*// ByMoreOtherPrograms.this.finish();
        Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession.class);
        startActivity(i);
        finish();*/
    }
}