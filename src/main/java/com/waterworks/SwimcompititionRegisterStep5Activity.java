package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.LoadCheckboxListAsyncTask;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

public class SwimcompititionRegisterStep5Activity extends Activity {
    String token, familyID, DateValue, eventdates, time, MeetDate_Display, suceessACK, messageACK, sucessmsgACK, MeetID;
    SharedPreferences.Editor editor;
    ArrayList<HashMap<String, String>> ACKmessage = new ArrayList<HashMap<String, String>>();
    ArrayList<String> MessageText = new ArrayList<String>();
    ArrayList<String> MessageValue = new ArrayList<String>();
    SharedPreferences prefs;
    String TAG = "SwimcompititionRegisterStep5Activity";

    CheckBox acknowlegement_chk1, acknowlegement_chk2, acknowlegement_chk3, acknowlegement_chk4, acknowlegement_chk5,
            acknowlegement_chk6, acknowlegement_chk7, acknowlegement_chk8, acknowlegement_chk9, acknowlegement_chk10;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6,
            linearLayout7, linearLayout8, linearLayout9, linearLayout10;
    TextView acknowlegements_txt1, acknowlegements_txt2, acknowlegements_txt3, acknowlegements_txt4,
            acknowlegements_txt5, acknowlegements_txt6, acknowlegements_txt7, acknowlegements_txt8, acknowlegements_txt9, acknowlegements_txt10, nextbtn, displaytxt;
    CardView nextcv;
    Context mContext = this;
    Boolean isInternetPresent = false;
    ScrollView scrollview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition5_newchange);
        prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        Intent intent = getIntent();
        if (null != intent) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue = intent.getStringExtra("datevalue");
            time = intent.getStringExtra("time");
            MeetDate_Display = intent.getStringExtra("MeetDate_Display");
        }
        String[] Meetidsplit = DateValue.split("\\|");
        MeetID = Meetidsplit[1];
        isInternetPresent = Utility.isNetworkConnected(SwimcompititionRegisterStep5Activity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
// call webservice for Acknowlegement
            new GetAcknowlegements().execute();
            findViewById();

            View view = findViewById(R.id.layout_top);
            TextView title = (TextView) view.findViewById(R.id.action_title);
            title.setText("Acknowledgements");

            ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
            ib_menusad.setBackgroundResource(R.drawable.back_arrow);

            Button relMenu = (Button) view.findViewById(R.id.relMenu);

            Button btnHome = (Button) findViewById(R.id.btnHome);
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConfiguration.global_StudIDChecked.clear();
                    Intent intentCheckin = new Intent(SwimcompititionRegisterStep5Activity.this, DashBoardActivity.class);
                    intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentCheckin);
                    finish();
                }
            });
            relMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    //initilize control
    public void findViewById() {
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        linearLayout1 = (LinearLayout) findViewById(R.id.linear_1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linear_2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linear_3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linear_4);
        linearLayout5 = (LinearLayout) findViewById(R.id.linear_5);
        linearLayout6 = (LinearLayout) findViewById(R.id.linear_6);
        linearLayout7 = (LinearLayout) findViewById(R.id.linear_7);
        linearLayout8 = (LinearLayout) findViewById(R.id.linear_8);
        linearLayout9 = (LinearLayout) findViewById(R.id.linear_9);
        linearLayout10 = (LinearLayout) findViewById(R.id.linear_10);

        acknowlegements_txt1 = (TextView) findViewById(R.id.acknowlegement_txt1);
        acknowlegements_txt2 = (TextView) findViewById(R.id.acknowlegement_txt2);
        acknowlegements_txt3 = (TextView) findViewById(R.id.acknowlegement_txt3);
        acknowlegements_txt4 = (TextView) findViewById(R.id.acknowlegement_txt4);
        acknowlegements_txt5 = (TextView) findViewById(R.id.acknowlegement_txt5);
        acknowlegements_txt6 = (TextView) findViewById(R.id.acknowlegement_txt6);
        acknowlegements_txt7 = (TextView) findViewById(R.id.acknowlegement_txt7);
        acknowlegements_txt8 = (TextView) findViewById(R.id.acknowlegement_txt8);
        acknowlegements_txt9 = (TextView) findViewById(R.id.acknowlegement_txt9);
        acknowlegements_txt10 = (TextView) findViewById(R.id.acknowlegement_txt10);

        displaytxt = (TextView) findViewById(R.id.textviewinfo);

        acknowlegement_chk1 = (CheckBox) findViewById(R.id.acknowlegement_chk1);
        acknowlegement_chk2 = (CheckBox) findViewById(R.id.acknowlegement_chk2);
        acknowlegement_chk3 = (CheckBox) findViewById(R.id.acknowlegement_chk3);
        acknowlegement_chk4 = (CheckBox) findViewById(R.id.acknowlegement_chk4);
        acknowlegement_chk5 = (CheckBox) findViewById(R.id.acknowlegement_chk5);
        acknowlegement_chk6 = (CheckBox) findViewById(R.id.acknowlegement_chk6);
        acknowlegement_chk7 = (CheckBox) findViewById(R.id.acknowlegement_chk7);
        acknowlegement_chk8 = (CheckBox) findViewById(R.id.acknowlegement_chk8);
        acknowlegement_chk9 = (CheckBox) findViewById(R.id.acknowlegement_chk9);
        acknowlegement_chk10 = (CheckBox) findViewById(R.id.acknowlegement_chk10);

        nextcv = (CardView) findViewById(R.id.btn_swim_next);

        scrollview3=(ScrollView)findViewById(R.id.scrollView3);

        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
        slide_up.setDuration(1000);
        acknowlegement_chk1.setAnimation(slide_up);
        acknowlegements_txt1.setAnimation(slide_up);
        acknowlegements_txt1.setVisibility(View.VISIBLE);
        acknowlegement_chk1.setVisibility(View.VISIBLE);

        acknowlegement_chk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk1.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(1);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk2.setAnimation(slide_up);
                        acknowlegements_txt2.setAnimation(slide_up);
                        acknowlegements_txt2.setVisibility(View.VISIBLE);
                        acknowlegement_chk2.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk2.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(1);
                }
            }
        });
        acknowlegement_chk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk2.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(2);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk3.setAnimation(slide_up);
                        acknowlegements_txt3.setAnimation(slide_up);
                        acknowlegements_txt3.setVisibility(View.VISIBLE);
                        acknowlegement_chk3.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk3.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(2);
                }
            }
        });
        acknowlegement_chk3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk3.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(3);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk4.setAnimation(slide_up);
                        acknowlegements_txt4.setAnimation(slide_up);
                        acknowlegements_txt4.setVisibility(View.VISIBLE);
                        acknowlegement_chk4.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk4.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(3);
                }
            }
        });
        acknowlegement_chk4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk4.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(4);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk5.setAnimation(slide_up);
                        acknowlegements_txt5.setAnimation(slide_up);
                        acknowlegements_txt5.setVisibility(View.VISIBLE);
                        acknowlegement_chk5.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk5.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(4);
                }
            }
        });
        acknowlegement_chk5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk5.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(5);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk6.setAnimation(slide_up);
                        acknowlegements_txt6.setAnimation(slide_up);
                        acknowlegements_txt6.setVisibility(View.VISIBLE);
                        acknowlegement_chk6.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk6.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(5);
                }
            }
        });
        acknowlegement_chk6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk6.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(6);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk7.setAnimation(slide_up);
                        acknowlegements_txt7.setAnimation(slide_up);
                        acknowlegements_txt7.setVisibility(View.VISIBLE);
                        acknowlegement_chk7.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk7.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(6);
                }
            }
        });
        acknowlegement_chk7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk7.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(7);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk8.setAnimation(slide_up);
                        acknowlegements_txt8.setAnimation(slide_up);
                        acknowlegements_txt8.setVisibility(View.VISIBLE);
                        acknowlegement_chk8.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk8.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(7);
                }
            }
        });
        acknowlegement_chk8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk8.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(8);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk9.setAnimation(slide_up);
                        acknowlegements_txt9.setAnimation(slide_up);
                        acknowlegements_txt9.setVisibility(View.VISIBLE);
                        acknowlegement_chk9.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk9.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(8);
                }
            }
        });
        acknowlegement_chk9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk9.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(9);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        acknowlegement_chk10.setAnimation(slide_up);
                        acknowlegements_txt10.setAnimation(slide_up);
                        acknowlegements_txt10.setVisibility(View.VISIBLE);
                        acknowlegement_chk10.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk10.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(9);
                }
            }
        });
        acknowlegement_chk10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acknowlegement_chk10.setButtonDrawable(R.drawable.custom_check_orange);
                int flag1 = displayNextButton(10);
                if (isChecked) {
                    if (flag1 == 0) {
                        Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                        slide_up.setDuration(400);
                        nextcv.setAnimation(slide_up);
                        nextcv.setVisibility(View.VISIBLE);
                        scrollview3.scrollTo(0,acknowlegement_chk2.getBottom());
                    }
                } else {
                    hideUncheckChekboxAckMessage(10);
                }
            }
        });
        nextcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwimcompititionRegisterStep5Activity.this, SwimCompRegisterSplash.class);
                overridePendingTransition(0, 0);
                i.putExtra("datevalue", DateValue);
                i.putExtra("time", time);
                i.putExtra("eventdates", eventdates);
                i.putExtra("MeetDate_Display", MeetDate_Display);
                startActivity(i);
            }
        });
    }
    public void hideUncheckChekboxAckMessage(int number) {
        switch (number) {
            case 1:
                acknowlegement_chk1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down1 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down1.setDuration(300);
                        acknowlegement_chk2.startAnimation(slide_down1);
                        acknowlegement_chk3.startAnimation(slide_down1);
                        acknowlegement_chk4.startAnimation(slide_down1);
                        acknowlegement_chk5.startAnimation(slide_down1);
                        acknowlegement_chk6.startAnimation(slide_down1);
                        acknowlegement_chk7.startAnimation(slide_down1);
                        acknowlegement_chk8.startAnimation(slide_down1);
                        acknowlegement_chk9.startAnimation(slide_down1);
                        acknowlegement_chk10.startAnimation(slide_down1);

                        acknowlegements_txt2.startAnimation(slide_down1);
                        acknowlegements_txt3.startAnimation(slide_down1);
                        acknowlegements_txt4.startAnimation(slide_down1);
                        acknowlegements_txt5.startAnimation(slide_down1);
                        acknowlegements_txt6.startAnimation(slide_down1);
                        acknowlegements_txt7.startAnimation(slide_down1);
                        acknowlegements_txt8.startAnimation(slide_down1);
                        acknowlegements_txt9.startAnimation(slide_down1);
                        acknowlegements_txt10.startAnimation(slide_down1);

                        nextcv.startAnimation(slide_down1);

                        acknowlegement_chk2.setVisibility(View.GONE);
                        acknowlegement_chk3.setVisibility(View.GONE);
                        acknowlegement_chk4.setVisibility(View.GONE);
                        acknowlegement_chk5.setVisibility(View.GONE);
                        acknowlegement_chk6.setVisibility(View.GONE);
                        acknowlegement_chk7.setVisibility(View.GONE);
                        acknowlegement_chk8.setVisibility(View.GONE);
                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk2.setChecked(false);
                        acknowlegement_chk3.setChecked(false);
                        acknowlegement_chk4.setChecked(false);
                        acknowlegement_chk5.setChecked(false);
                        acknowlegement_chk6.setChecked(false);
                        acknowlegement_chk7.setChecked(false);
                        acknowlegement_chk8.setChecked(false);
                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt2.setVisibility(View.GONE);
                        acknowlegements_txt3.setVisibility(View.GONE);
                        acknowlegements_txt4.setVisibility(View.GONE);
                        acknowlegements_txt5.setVisibility(View.GONE);
                        acknowlegements_txt6.setVisibility(View.GONE);
                        acknowlegements_txt7.setVisibility(View.GONE);
                        acknowlegements_txt8.setVisibility(View.GONE);
                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 2:
                acknowlegement_chk2.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Animation slide_down2 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down2.setDuration(300);

                        acknowlegement_chk3.startAnimation(slide_down2);
                        acknowlegement_chk4.startAnimation(slide_down2);
                        acknowlegement_chk5.startAnimation(slide_down2);
                        acknowlegement_chk6.startAnimation(slide_down2);
                        acknowlegement_chk7.startAnimation(slide_down2);
                        acknowlegement_chk8.startAnimation(slide_down2);
                        acknowlegement_chk9.startAnimation(slide_down2);
                        acknowlegement_chk10.startAnimation(slide_down2);

                        acknowlegements_txt3.startAnimation(slide_down2);
                        acknowlegements_txt4.startAnimation(slide_down2);
                        acknowlegements_txt5.startAnimation(slide_down2);
                        acknowlegements_txt6.startAnimation(slide_down2);
                        acknowlegements_txt7.startAnimation(slide_down2);
                        acknowlegements_txt8.startAnimation(slide_down2);
                        acknowlegements_txt9.startAnimation(slide_down2);
                        acknowlegements_txt10.startAnimation(slide_down2);

                        nextcv.startAnimation(slide_down2);

                        acknowlegement_chk3.setVisibility(View.GONE);
                        acknowlegement_chk4.setVisibility(View.GONE);
                        acknowlegement_chk5.setVisibility(View.GONE);
                        acknowlegement_chk6.setVisibility(View.GONE);
                        acknowlegement_chk7.setVisibility(View.GONE);
                        acknowlegement_chk8.setVisibility(View.GONE);
                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk3.setChecked(false);
                        acknowlegement_chk4.setChecked(false);
                        acknowlegement_chk5.setChecked(false);
                        acknowlegement_chk6.setChecked(false);
                        acknowlegement_chk7.setChecked(false);
                        acknowlegement_chk8.setChecked(false);
                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt3.setVisibility(View.GONE);
                        acknowlegements_txt4.setVisibility(View.GONE);
                        acknowlegements_txt5.setVisibility(View.GONE);
                        acknowlegements_txt6.setVisibility(View.GONE);
                        acknowlegements_txt7.setVisibility(View.GONE);
                        acknowlegements_txt8.setVisibility(View.GONE);
                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 3:
                acknowlegement_chk3.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Animation slide_down3 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down3.setDuration(300);
                        acknowlegement_chk4.startAnimation(slide_down3);
                        acknowlegement_chk5.startAnimation(slide_down3);
                        acknowlegement_chk6.startAnimation(slide_down3);
                        acknowlegement_chk7.startAnimation(slide_down3);
                        acknowlegement_chk8.startAnimation(slide_down3);
                        acknowlegement_chk9.startAnimation(slide_down3);
                        acknowlegement_chk10.startAnimation(slide_down3);

                        acknowlegements_txt4.startAnimation(slide_down3);
                        acknowlegements_txt5.startAnimation(slide_down3);
                        acknowlegements_txt6.startAnimation(slide_down3);
                        acknowlegements_txt7.startAnimation(slide_down3);
                        acknowlegements_txt8.startAnimation(slide_down3);
                        acknowlegements_txt9.startAnimation(slide_down3);
                        acknowlegements_txt10.startAnimation(slide_down3);
                        nextcv.startAnimation(slide_down3);

                        acknowlegement_chk4.setVisibility(View.GONE);
                        acknowlegement_chk5.setVisibility(View.GONE);
                        acknowlegement_chk6.setVisibility(View.GONE);
                        acknowlegement_chk7.setVisibility(View.GONE);
                        acknowlegement_chk8.setVisibility(View.GONE);
                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk4.setChecked(false);
                        acknowlegement_chk5.setChecked(false);
                        acknowlegement_chk6.setChecked(false);
                        acknowlegement_chk7.setChecked(false);
                        acknowlegement_chk8.setChecked(false);
                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt4.setVisibility(View.GONE);
                        acknowlegements_txt5.setVisibility(View.GONE);
                        acknowlegements_txt6.setVisibility(View.GONE);
                        acknowlegements_txt7.setVisibility(View.GONE);
                        acknowlegements_txt8.setVisibility(View.GONE);
                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 4:
                acknowlegement_chk4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down4 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down4.setDuration(300);

                        acknowlegement_chk5.startAnimation(slide_down4);
                        acknowlegement_chk6.startAnimation(slide_down4);
                        acknowlegement_chk7.startAnimation(slide_down4);
                        acknowlegement_chk8.startAnimation(slide_down4);
                        acknowlegement_chk9.startAnimation(slide_down4);
                        acknowlegement_chk10.startAnimation(slide_down4);

                        acknowlegements_txt5.startAnimation(slide_down4);
                        acknowlegements_txt6.startAnimation(slide_down4);
                        acknowlegements_txt7.startAnimation(slide_down4);
                        acknowlegements_txt8.startAnimation(slide_down4);
                        acknowlegements_txt9.startAnimation(slide_down4);
                        acknowlegements_txt10.startAnimation(slide_down4);

                        nextcv.startAnimation(slide_down4);

                        acknowlegement_chk5.setVisibility(View.GONE);
                        acknowlegement_chk6.setVisibility(View.GONE);
                        acknowlegement_chk7.setVisibility(View.GONE);
                        acknowlegement_chk8.setVisibility(View.GONE);
                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk5.setChecked(false);
                        acknowlegement_chk6.setChecked(false);
                        acknowlegement_chk7.setChecked(false);
                        acknowlegement_chk8.setChecked(false);
                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt5.setVisibility(View.GONE);
                        acknowlegements_txt6.setVisibility(View.GONE);
                        acknowlegements_txt7.setVisibility(View.GONE);
                        acknowlegements_txt8.setVisibility(View.GONE);
                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 5:
                acknowlegement_chk5.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down5 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down5.setDuration(300);

                        acknowlegement_chk6.startAnimation(slide_down5);
                        acknowlegement_chk7.startAnimation(slide_down5);
                        acknowlegement_chk8.startAnimation(slide_down5);
                        acknowlegement_chk9.startAnimation(slide_down5);
                        acknowlegement_chk10.startAnimation(slide_down5);

                        acknowlegements_txt6.startAnimation(slide_down5);
                        acknowlegements_txt7.startAnimation(slide_down5);
                        acknowlegements_txt8.startAnimation(slide_down5);
                        acknowlegements_txt9.startAnimation(slide_down5);
                        acknowlegements_txt10.startAnimation(slide_down5);

                        nextcv.startAnimation(slide_down5);

                        acknowlegement_chk6.setVisibility(View.GONE);
                        acknowlegement_chk7.setVisibility(View.GONE);
                        acknowlegement_chk8.setVisibility(View.GONE);
                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk6.setChecked(false);
                        acknowlegement_chk7.setChecked(false);
                        acknowlegement_chk8.setChecked(false);
                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt6.setVisibility(View.GONE);
                        acknowlegements_txt7.setVisibility(View.GONE);
                        acknowlegements_txt8.setVisibility(View.GONE);
                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 6:
                acknowlegement_chk6.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down6 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down6.setDuration(300);

                        acknowlegement_chk7.startAnimation(slide_down6);
                        acknowlegement_chk8.startAnimation(slide_down6);
                        acknowlegement_chk9.startAnimation(slide_down6);
                        acknowlegement_chk10.startAnimation(slide_down6);

                        acknowlegements_txt7.startAnimation(slide_down6);
                        acknowlegements_txt8.startAnimation(slide_down6);
                        acknowlegements_txt9.startAnimation(slide_down6);
                        acknowlegements_txt10.startAnimation(slide_down6);

                        nextcv.startAnimation(slide_down6);

                        acknowlegement_chk7.setVisibility(View.GONE);
                        acknowlegement_chk8.setVisibility(View.GONE);
                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk7.setChecked(false);
                        acknowlegement_chk8.setChecked(false);
                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt7.setVisibility(View.GONE);
                        acknowlegements_txt8.setVisibility(View.GONE);
                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 7:
                acknowlegement_chk7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down7 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down7.setDuration(300);

                        acknowlegement_chk8.startAnimation(slide_down7);
                        acknowlegement_chk9.startAnimation(slide_down7);
                        acknowlegement_chk10.startAnimation(slide_down7);

                        acknowlegements_txt8.startAnimation(slide_down7);
                        acknowlegements_txt9.startAnimation(slide_down7);
                        acknowlegements_txt10.startAnimation(slide_down7);

                        nextcv.startAnimation(slide_down7);
                        acknowlegement_chk8.setVisibility(View.GONE);
                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk8.setChecked(false);
                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt8.setVisibility(View.GONE);
                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 8:
                acknowlegement_chk8.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down8 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down8.setDuration(300);

                        acknowlegement_chk9.startAnimation(slide_down8);
                        acknowlegement_chk10.startAnimation(slide_down8);

                        acknowlegements_txt9.startAnimation(slide_down8);
                        acknowlegements_txt10.startAnimation(slide_down8);

                        nextcv.startAnimation(slide_down8);

                        acknowlegement_chk9.setVisibility(View.GONE);
                        acknowlegement_chk10.setVisibility(View.GONE);

                        acknowlegement_chk9.setChecked(false);
                        acknowlegement_chk10.setChecked(false);

                        acknowlegements_txt9.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 9:
                acknowlegement_chk9.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down9 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down9.setDuration(300);

                        acknowlegement_chk10.startAnimation(slide_down9);
                        acknowlegements_txt10.startAnimation(slide_down9);

                        nextcv.startAnimation(slide_down9);

                        acknowlegement_chk10.setVisibility(View.GONE);
                        acknowlegements_txt10.setVisibility(View.GONE);
                        acknowlegement_chk10.setChecked(false);

                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            case 10:
                acknowlegement_chk10.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down10 = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_popup);
                        slide_down10.setDuration(300);
                        nextcv.startAnimation(slide_down10);
                        nextcv.setVisibility(View.GONE);
                    }
                }, 100);
                break;
            default:
                break;
        }
    }
    public int displayNextButton(int number) {
        int flag = 0;
        if (MessageText.size() == number) {
            Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
            slide_up.setDuration(400);
            nextcv.setAnimation(slide_up);
            nextcv.setVisibility(View.VISIBLE);
            flag = 1;
        }
        return flag;
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
    public void load_ACKmessge() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("meetid", MeetID);
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.AcknowlegementsStep4URL, param);
        readAndParseJSONACK(responseString);
    }
    public void readAndParseJSONACK(String ack) {
        try {
            JSONObject reader = new JSONObject(ack);
            suceessACK = reader.getString("Success");
            if (suceessACK.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck4");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put("messageText", jsonChildNode.getString("messageText"));
                    hashmap.put("messageValue", jsonChildNode.getString("messageValue"));
                    MessageText.add("" + jsonChildNode.getString("messageText"));
                    MessageValue.add("" + jsonChildNode.getString("messageValue"));
                    ACKmessage.add(hashmap);
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck4");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageACK = jsonChildNode.getString("Msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*-------------------- 10/01/2017 new code megha --------------------------------- */
    public class GetAcknowlegements extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimcompititionRegisterStep5Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd != null) {
                pd.dismiss();
            }
            if (suceessACK.equalsIgnoreCase("True")) {
                for (int i = 0; i < MessageText.size(); i++) {
                    if (i == 0) {
                        acknowlegements_txt1.setText(MessageText.get(0));
                    }
                    if (i == 1) {
                        acknowlegements_txt2.setText(MessageText.get(1));
                                            }
                    if (i == 2) {
                        acknowlegements_txt3.setText(MessageText.get(2));
                    }
                    if (i == 3) {
                        acknowlegements_txt4.setText(MessageText.get(3));
                    }
                    if (i == 4) {
                        acknowlegements_txt5.setText(MessageText.get(4));
                    }
                    if (i == 5) {
                        acknowlegements_txt6.setText(MessageText.get(5));
                    }
                    if (i == 6) {
                        acknowlegements_txt7.setText(MessageText.get(6));
                    }
                    if (i == 7) {
                        acknowlegements_txt8.setText(MessageText.get(7));
                    }
                    if (i == 8) {
                        acknowlegements_txt9.setText(MessageText.get(8));
                    }
                    if (i == 9) {
                        acknowlegements_txt10.setText(MessageText.get(9));
                    }
                }
            } else {
                Toast.makeText(SwimcompititionRegisterStep5Activity.this, "" + messageACK, Toast.LENGTH_LONG).show();
                acknowlegement_chk1.setVisibility(View.GONE);
                acknowlegements_txt1.setText(messageACK);
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            load_ACKmessge();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();

    }

}
