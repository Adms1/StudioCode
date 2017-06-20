package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Swim4Test extends Activity {
    private SharedPreferences prefs;
    private String token, familyID, basketID, siteID, eventdates, time, MeetDate_Display, eventData, memberString, DateValue, wwmember;
    Context mContext = this;
    private String TAG = "Swim4Test";

    RadioGroup inst_types_grp, inst_types_grp2, inst_types_grp_2, inst_types_grp2_2,
            inst_types_grp_3, inst_types_grp3_2, inst_types_grp_4, inst_types_grp4_2, inst_types_grp_5, inst_types_grp5_2;
    RadioButton rbchk_1, rbchk_2, rbchk_3, rbchk_grp2_1, rbchk_grp2_2, rbchk_2_1, rbchk_2_2, rbchk_2_3,
            rbchk_grp2_2_1, rbchk_grp2_2_2, rbchk_3_1, rbchk_3_2, rbchk_3_3, rbchk_grp3_2_1, rbchk_grp3_2_2, rbchk_4_1,
            rbchk_4_2, rbchk_4_3, rbchk_grp4_2_1, rbchk_grp4_2_2, rbchk_5_1, rbchk_5_2, rbchk_5_3, rbchk_grp5_2_1, rbchk_grp5_2_2;
    CardView btn_next_card;
    LinearLayout st_1, st_2, st_3, st_4, st_5, lay_1, lay_2, lay_3, lay_4, lay_5, filter_1,
            filter_2, filter_3, filter_4, filter_5;
    View select_1, select_2, select_3, select_4, select_5;
    TextView name_1, name_2, name_3, name_4, name_5, student1Txt, student1_2Txt, student2Txt, student2_2Txt, student3Txt, student3_2Txt, student4Txt, student4_2Txt,
            student5Txt, student5_2Txt;
    int last_clicked_position = 1;
    View view_1, view_2;
    ArrayList<String> tempid = new ArrayList<>();
    ArrayList<String> tempname = new ArrayList<>();
    int selectedIdgrp1, selectedIdgrp1_2, selectedIdgrp2, selectedIdgrp2_2, selectedIdgrp3, selectedIdgrp3_2,
            selectedIdgrp4, selectedIdgrp4_2, selectedIdgrp5, selectedIdgrp5_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim4_test);
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
        findViewById();

        setListener();
    }

    public void findViewById() {
        setTitleBar();
        lay_1 = (LinearLayout) findViewById(R.id.lay_1);
        lay_2 = (LinearLayout) findViewById(R.id.lay_2);
        lay_3 = (LinearLayout) findViewById(R.id.lay_3);
        lay_4 = (LinearLayout) findViewById(R.id.lay_4);
        lay_5 = (LinearLayout) findViewById(R.id.lay_5);
        btn_next_card = (CardView) findViewById(R.id.btnContinueStep4);


        rbchk_1 = (RadioButton) findViewById(R.id.rbchk_1);
        rbchk_2 = (RadioButton) findViewById(R.id.rbchk_2);
        rbchk_3 = (RadioButton) findViewById(R.id.rbchk_3);
        rbchk_grp2_1 = (RadioButton) findViewById(R.id.rbchk_grp2_1);
        rbchk_grp2_2 = (RadioButton) findViewById(R.id.rbchk_grp2_2);
        rbchk_2_1 = (RadioButton) findViewById(R.id.rbchk_2_1);
        rbchk_2_2 = (RadioButton) findViewById(R.id.rbchk_2_2);
        rbchk_2_3 = (RadioButton) findViewById(R.id.rbchk_2_3);
        rbchk_grp2_2_1 = (RadioButton) findViewById(R.id.rbchk_grp2_2_1);
        rbchk_grp2_2_2 = (RadioButton) findViewById(R.id.rbchk_grp2_2_2);
        rbchk_3_1 = (RadioButton) findViewById(R.id.rbchk_3_1);
        rbchk_3_2 = (RadioButton) findViewById(R.id.rbchk_3_2);
        rbchk_3_3 = (RadioButton) findViewById(R.id.rbchk_3_3);
        rbchk_grp3_2_1 = (RadioButton) findViewById(R.id.rbchk_grp3_2_1);
        rbchk_grp3_2_2 = (RadioButton) findViewById(R.id.rbchk_grp3_2_2);
        rbchk_4_1 = (RadioButton) findViewById(R.id.rbchk_4_1);
        rbchk_4_2 = (RadioButton) findViewById(R.id.rbchk_4_2);
        rbchk_4_3 = (RadioButton) findViewById(R.id.rbchk_4_3);
        rbchk_grp4_2_1 = (RadioButton) findViewById(R.id.rbchk_grp4_2_1);
        rbchk_grp4_2_2 = (RadioButton) findViewById(R.id.rbchk_grp4_2_2);
        rbchk_5_1 = (RadioButton) findViewById(R.id.rbchk_5_1);
        rbchk_5_2 = (RadioButton) findViewById(R.id.rbchk_5_2);
        rbchk_5_3 = (RadioButton) findViewById(R.id.rbchk_5_3);
        rbchk_grp5_2_1 = (RadioButton) findViewById(R.id.rbchk_grp5_2_1);
        rbchk_grp5_2_2 = (RadioButton) findViewById(R.id.rbchk_grp5_2_1);

        inst_types_grp = (RadioGroup) findViewById(R.id.inst_types_grp);
        inst_types_grp2 = (RadioGroup) findViewById(R.id.inst_types_grp2);
        inst_types_grp_2 = (RadioGroup) findViewById(R.id.inst_types_grp_2);
        inst_types_grp2_2 = (RadioGroup) findViewById(R.id.inst_types_grp2_2);
        inst_types_grp_3 = (RadioGroup) findViewById(R.id.inst_types_grp_3);
        inst_types_grp3_2 = (RadioGroup) findViewById(R.id.inst_types_grp3_2);
        inst_types_grp_4 = (RadioGroup) findViewById(R.id.inst_types_grp_4);
        inst_types_grp4_2 = (RadioGroup) findViewById(R.id.inst_types_grp4_2);
        inst_types_grp_5 = (RadioGroup) findViewById(R.id.inst_types_grp_5);
        inst_types_grp5_2 = (RadioGroup) findViewById(R.id.inst_types_grp5_2);


        // Studetnts Tab Lay
        st_1 = (LinearLayout) findViewById(R.id.st_1);
        st_2 = (LinearLayout) findViewById(R.id.st_2);
        st_3 = (LinearLayout) findViewById(R.id.st_3);
        st_4 = (LinearLayout) findViewById(R.id.st_4);
        st_5 = (LinearLayout) findViewById(R.id.st_5);

        filter_1 = (LinearLayout) findViewById(R.id.filter_1);
        filter_2 = (LinearLayout) findViewById(R.id.filter_2);
        filter_3 = (LinearLayout) findViewById(R.id.filter_3);
        filter_4 = (LinearLayout) findViewById(R.id.filter_4);
        filter_5 = (LinearLayout) findViewById(R.id.filter_5);

        select_1 = (View) findViewById(R.id.select_1);
        select_2 = (View) findViewById(R.id.select_2);
        select_3 = (View) findViewById(R.id.select_3);
        select_4 = (View) findViewById(R.id.select_4);
        select_5 = (View) findViewById(R.id.select_5);


        name_1 = (TextView) findViewById(R.id.name_1);
        name_2 = (TextView) findViewById(R.id.name_2);
        name_3 = (TextView) findViewById(R.id.name_3);
        name_4 = (TextView) findViewById(R.id.name_4);
        name_5 = (TextView) findViewById(R.id.name_5);


        student1Txt = (TextView) findViewById(R.id.student1Txt);
        student1_2Txt = (TextView) findViewById(R.id.student1_2Txt);
        student2Txt = (TextView) findViewById(R.id.student2Txt);
        student2_2Txt = (TextView) findViewById(R.id.student2_2Txt);
        student3Txt = (TextView) findViewById(R.id.student3Txt);
        student3_2Txt = (TextView) findViewById(R.id.student3_2Txt);
        student4Txt = (TextView) findViewById(R.id.student4Txt);
        student4_2Txt = (TextView) findViewById(R.id.student4_2Txt);
        student5Txt = (TextView) findViewById(R.id.student5Txt);
        student5_2Txt = (TextView) findViewById(R.id.student5_2Txt);


        select_1.setTag(1);
        select_2.setTag(2);
        select_3.setTag(3);
        select_4.setTag(4);
        select_5.setTag(5);

        st_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lay_1.setVisibility(View.VISIBLE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.VISIBLE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.GONE);

//                select_1.setVisibility(View.VISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.orange));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));


                decide_layout(last_view(), select_1);
                animation_slide(filter_1, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 1;


            }
        });

        st_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.VISIBLE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.VISIBLE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
//                select_2.setVisibility(View.VISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.orange));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

                decide_layout(last_view(), select_2);
                animation_slide(filter_2, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 2;
            }
        });
        st_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.VISIBLE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.VISIBLE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
//                select_3.setVisibility(View.VISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.orange));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

                decide_layout(last_view(), select_3);
                animation_slide(filter_3, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 3;


            }
        });
        st_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.VISIBLE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.VISIBLE);
                filter_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
//                select_4.setVisibility(View.VISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.orange));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));
                decide_layout(last_view(), select_4);
                animation_slide(filter_4, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 4;
            }
        });
        st_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.VISIBLE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.VISIBLE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);
//                select_5.setVisibility(View.VISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.orange));

                decide_layout(last_view(), select_5);
                animation_slide(filter_5, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 5;
            }
        });


        distributeStudentsTop();
    }

    public void distributeStudentsTop() {

        int size = tempname.size();

        if (size == 1) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.GONE);
            st_3.setVisibility(View.GONE);
            st_4.setVisibility(View.GONE);
            st_5.setVisibility(View.GONE);
        } else if (size == 2) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.GONE);
            st_4.setVisibility(View.GONE);
            st_5.setVisibility(View.GONE);
        } else if (size == 3) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.VISIBLE);
            st_4.setVisibility(View.GONE);
            st_5.setVisibility(View.GONE);
        } else if (size == 4) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.VISIBLE);
            st_4.setVisibility(View.VISIBLE);
            st_5.setVisibility(View.GONE);
        } else if (size == 5) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.VISIBLE);
            st_4.setVisibility(View.VISIBLE);
            st_5.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < tempname.size(); i++) {
            if (i == 0) {
                if (tempname.get(i).contains(" ")) {
                    String temp[] = tempname.get(i).split("\\s+");
                    //					name_1.setText(temp[0] + "\n" + temp[1]);
                    name_1.setText(temp[0]);
                    student1Txt.setText("Where should" + " " + temp[0] + " " + "start the race?");
                    student1_2Txt.setText("Should"  + " " + temp[0] + " " + "swim next to the wall in an end lane?");
                } else {
                    name_1.setText(tempname.get(i));
                    student1Txt.setText("Where should" + " " + tempname.get(i) + " " + "start the race?");
                    student1_2Txt.setText("Should" + " " + tempname.get(i) + " " + "swim next to the wall in an end lane?");
                }
            } else if (i == 1) {
                if (tempname.get(i).contains(" ")) {
                    String temp[] = tempname.get(i).split("\\s+");
                    //					name_2.setText(temp[0] + "\n" + temp[1]);
                    name_2.setText(temp[0]);
                    student2Txt.setText("Where should" + " " + temp[0] + " " + "start the race?");
                    student2_2Txt.setText("Should" +  " " + temp[0] + " " + "swim next to the wall in an end lane?");
                } else {
                    name_2.setText(tempname.get(i));
                    student2Txt.setText("Where should" + " "+ tempname.get(i) + " " + "start the race?");
                    student2_2Txt.setText("Should" + " " + tempname.get(i) + " " + "swim next to the wall in an end lane?");
                }

            } else if (i == 2) {
                if (tempname.get(i).contains(" ")) {
                    String temp[] = tempname.get(i).split("\\s+");
                    //					name_3.setText(temp[0] + "\n" + temp[1]);
                    name_3.setText(temp[0]);
                    student3Txt.setText("Where should" + " " + temp[0] + " " +  "start the race?");
                    student3_2Txt.setText("Should" + " "  + temp[0] + " "  + "swim next to the wall in an end lane?");
                } else {
                    name_3.setText(tempname.get(i));
                    student3Txt.setText("Where should" + " "  + tempname.get(i) + " " +  "start the race?");
                    student3_2Txt.setText("Should" + " " + tempname.get(i) + " " + "swim next to the wall in an end lane?");
                }

            } else if (i == 3) {
                if (tempname.get(i).contains(" ")) {
                    String temp[] = tempname.get(i).split("\\s+");
                    //					name_4.setText(temp[0] + "\n" + temp[1]);
                    name_4.setText(temp[0]);
                    student4Txt.setText("Where should" + " " + temp[0] + " " +  "start the race?");
                    student4_2Txt.setText("Should" + " " + temp[0] + " " + "swim next to the wall in an end lane?");
                } else {
                    name_4.setText(tempname.get(i));
                    student4Txt.setText("Where should" + " " + tempname.get(i) + " " + "start the race?");
                    student4_2Txt.setText("Should" + " " + tempname.get(i) + " " + "swim next to the wall in an end lane?");
                }

            } else if (i == 4) {
                if (tempname.get(i).contains(" ")) {
                    String temp[] = tempname.get(i).split("\\s+");
                    //					name_5.setText(temp[0] + "\n" + temp[1]);
                    name_5.setText(temp[0]);
                    student5Txt.setText("Where should"+ " " + temp[0] + " " + "start the race?");
                    student5_2Txt.setText("Should" + " " + temp[0] + " " + "swim next to the wall in an end lane?");
                } else {
                    name_5.setText(tempname.get(i));
                    student5Txt.setText("Where should" + " " + tempname.get(i) + " " + "start the race?");
                    student5_2Txt.setText("Should" + " " + tempname.get(i) + " " + "swim next to the wall in an end lane?");
                }

            }
        }
    }

    public void setTitleBar() {
        View view = findViewById(R.id.layout_top);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Preferences");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfiguration.global_StudIDChecked.clear();
                Intent intentCheckin = new Intent(Swim4Test.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
    }


    public View last_view() {
        View temp = null;
        if (last_clicked_position == 1) {
            temp = select_1;
            Log.d("Pos1", "select 1");
        } else if (last_clicked_position == 2) {
            temp = select_2;
            Log.d("Pos1", "select 2");
        } else if (last_clicked_position == 3) {
            temp = select_3;
            Log.d("Pos1", "select 3");
        } else if (last_clicked_position == 4) {
            temp = select_4;
            Log.d("Pos1", "select 4");
        } else if (last_clicked_position == 5) {
            temp = select_5;
            Log.d("Pos1", "select 5");
        }
        return temp;
    }

    LinearLayout temp1, temp2, temp3, temp4;

    public void select_lay(int pos_1, int pos_2) {

        if (pos_1 == 1) {

            temp2 = lay_1;
        } else if (pos_1 == 2) {
            temp2 = lay_2;
        } else if (pos_1 == 3) {

            temp2 = lay_3;
        } else if (pos_1 == 4) {

            temp2 = lay_4;
        } else if (pos_1 == 5) {

            temp2 = lay_5;
        }

        if (pos_2 == 1) {

            temp4 = lay_1;
        } else if (pos_2 == 2) {

            temp4 = lay_2;
        } else if (pos_2 == 3) {

            temp4 = lay_3;
        } else if (pos_2 == 4) {

            temp4 = lay_4;
        } else if (pos_2 == 5) {

            temp4 = lay_5;
        }

    }

    boolean slide_left = false;

    public void decide_layout(final View v_1, final View v_2) {

        int pos_1 = Integer.parseInt(v_1.getTag().toString());
        int pos_2 = Integer.parseInt(v_2.getTag().toString());

        Log.d("Pos1", String.valueOf(pos_1));
        Log.d("Pos2", String.valueOf(pos_2));

        select_lay(pos_1, pos_2);
        if (pos_1 < pos_2) {
            view_1 = v_1;
            view_2 = v_2;
            slide_left = true;
        } else {
            view_1 = v_1;
            view_2 = v_2;
            slide_left = false;
        }
    }

    public void setListener() {

        rbchk_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_2_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp2_2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp2_2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_3_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_3_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_3_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp3_2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp3_2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_4_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_4_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_4_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp4_2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp4_2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_5_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_5_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_5_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp5_2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        rbchk_grp5_2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                }
            }
        });
        btn_next_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int counter = 0;

                selectedIdgrp1 = inst_types_grp.getCheckedRadioButtonId();
                selectedIdgrp1_2 = inst_types_grp2.getCheckedRadioButtonId();
                selectedIdgrp2 = inst_types_grp_2.getCheckedRadioButtonId();
                selectedIdgrp2_2 = inst_types_grp2_2.getCheckedRadioButtonId();
                selectedIdgrp3 = inst_types_grp_3.getCheckedRadioButtonId();
                selectedIdgrp3_2 = inst_types_grp3_2.getCheckedRadioButtonId();
                selectedIdgrp4 = inst_types_grp_4.getCheckedRadioButtonId();
                selectedIdgrp4_2 = inst_types_grp4_2.getCheckedRadioButtonId();
                selectedIdgrp5 = inst_types_grp_5.getCheckedRadioButtonId();
                selectedIdgrp5_2 = inst_types_grp5_2.getCheckedRadioButtonId();

                if (selectedIdgrp1 > 0 && selectedIdgrp1_2 > 0) {
                    counter = counter + 1;
                }
                if (selectedIdgrp2 > 0 && selectedIdgrp2_2 > 0) {
                    counter = counter + 1;
                }
                if (selectedIdgrp3 > 0 && selectedIdgrp3_2 > 0) {
                    counter = counter + 1;
                }
                if (selectedIdgrp4 > 0 && selectedIdgrp4_2 > 0) {
                    counter = counter + 1;
                }
                if (selectedIdgrp5 > 0 && selectedIdgrp5_2 > 0) {
                    counter = counter + 1;
                }
                if (tempname.size() == counter) {

                        Intent i = new Intent(Swim4Test.this, SwimcompititionRegisterStep5Activity.class);
                        i.putExtra("datevalue", DateValue);
                        i.putExtra("time", time);
                        i.putExtra("eventdates", eventdates);
                        i.putExtra("MeetDate_Display", MeetDate_Display);

                        startActivity(i);
                    } else {
                        Toast.makeText(mContext, "Please select how the swimmer would like to begin the race.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    public void animation_slide(final LinearLayout ll1,
                                final LinearLayout ll2,
                                final LinearLayout ll3,
                                final LinearLayout ll4,
                                final LinearLayout ll5,
                                final View v1,
                                final View v2) {

        if (!slide_left) {
            Animation animSlidInLeft = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
//            ll1.startAnimation(animSlidInLeft);
//            ll2.startAnimation(animSlidInLeft);
//            ll3.startAnimation(animSlidInLeft);
//            ll4.startAnimation(animSlidInLeft);
            ll5.startAnimation(animSlidInLeft);

            if (ll1.getVisibility() == View.VISIBLE) {
//                Animation animFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_left);
//                ll1.startAnimation(animFadeOut);
//                ll2.startAnimation(animFadeOut);
//                ll3.startAnimation(animFadeOut);
            }
            Animation animSlidInLeftLine = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left_line_rev);
            v1.startAnimation(animSlidInLeftLine);
            animSlidInLeftLine.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    v2.setVisibility(View.VISIBLE);
                    Animation animSlidInLeftLineRev = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left_line);
                    v2.startAnimation(animSlidInLeftLineRev);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
////            days_st_1.startAnimation(animSlidInRight);
////            ll2.startAnimation(animSlidInRight);
//            ll3.startAnimation(animSlidInRight);
//            ll4.startAnimation(animSlidInRight);
            ll5.startAnimation(animSlidInRight);
            if (ll1.getVisibility() == View.VISIBLE) {
                Animation animFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_right);
//                ll5.startAnimation(animFadeOut);
//                ll3.startAnimation(animFadeOut);
//                ll4.startAnimation(animFadeOut);
            }
            Animation animSlidInRightLineRev = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right_line_rev);
            v1.startAnimation(animSlidInRightLineRev);
            animSlidInRightLineRev.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    v2.setVisibility(View.VISIBLE);
                    Animation animSlidInRightLine = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right_line);
                    v2.startAnimation(animSlidInRightLine);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(Swim4Test.this, SwimcompititionRegisterStep3Activity.class);
        i.putExtra("datevalue", DateValue);
        i.putExtra("time", time);
        i.putExtra("eventdates", eventdates);
        i.putExtra("MeetDate_Display", MeetDate_Display);
        startActivity(i);
//        finish();
    }
}