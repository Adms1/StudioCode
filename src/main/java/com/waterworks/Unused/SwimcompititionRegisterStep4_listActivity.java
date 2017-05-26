package com.waterworks.Unused;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.*;
import com.waterworks.SwimcompititionRegisterStep3Activity;
import com.waterworks.utils.AppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class SwimcompititionRegisterStep4_listActivity extends Activity {

    String message, DateValue, eventdates, MeetDate_Display, time;
    TextView txtCompititionVal;
    String TAG = "SwimcompititionRegisterStep4_listActivity";

    ArrayList<HashMap<String, String>> selectedChildDataMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<Boolean> selectedStudents = new ArrayList<Boolean>();

    Boolean isInternetPresent = false;
    // String siteID;
    // String successLoadChildList;
    String token, familyID;
    // String messageMeetDate;
    // String successMeetDate;
    Button btnContinueStep3;
    // boolean isSelectedAgreement = false;
    // String msg1_Hours,msg2_meet,Msg3_Meet;
    // String successSwimCompittionCheck1;
    // String messageNormal;
    TextView txtSelectedStudent, txtMeetDate;
    // String message;
    RadioButton radiobtnVolunteerYes, radiobtnVolunteerWall;
    boolean registerAsVolunteer = false;
    Spinner spinner1_volunteers;
    ArrayList<String> _StudentID = new ArrayList<String>();
    ArrayList<String> _StudentName = new ArrayList<String>();
    LinearLayout ll_body;
    String[] tempid, tempname;
    public static ArrayList<String> StudentId, StudentName, step3_rg1_2, step3_jumpingwall2, step3_rg2_2, step3_endlane_2;
    public static String _nameid = "";
    public static String _nameid1 = "";
    public static String _nameid2 = "";
    public static String _nameid3 = "";
    LinearLayout st_1, st_2, st_3,main_lay;
    public static String _nameid4 = "";
    public static String _nameid5 = "";
    String firstname, secondname, thirdname, firstid, thirdid, secondid, Finalid;
    View select_1, select_2, select_3;
    TextView name_1, name_2, name_3;
    int last_clicked_position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition4_new);
        SharedPreferences prefs = AppConfiguration
                .getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        txtCompititionVal = (TextView) findViewById(R.id.txtCompititionVal);
        Intent intent = getIntent();
        if (null != intent) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue = intent.getStringExtra("datevalue");
//            Toast.makeText(getApplicationContext(), "DateValue" + DateValue, Toast.LENGTH_LONG).show();
            time = intent.getStringExtra("time");
           MeetDate_Display = intent.getStringExtra("MeetDate_Display");
            firstname = intent.getStringExtra("firstname");
            secondname = intent.getStringExtra("secondname");
            thirdname = intent.getStringExtra("thirdname");

            // sitename = intent.getStringExtra("sitename");

            Log.d(TAG, "eventdates=" + eventdates);
            Log.d(TAG, "DateValue=" + DateValue);

            Log.d(TAG, "firstname=" + firstname);
            Log.d(TAG, "secondname=" + secondname);
            Log.d(TAG, "thirdname=" + thirdname);

            txtCompititionVal.setText(SwimcompititionRegisterStep2Activity.MeetDate_Display);
            View view = findViewById(R.id.layout_top);





            TextView title = (TextView) view.findViewById(R.id.action_title);
            title.setText("Step 3: Preferences");
            ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
            ib_menusad.setBackgroundResource(R.drawable.back_arrow);
           /* txtSelectedStudent = (TextView) findViewById(R.id.txtSelectedStudent);
            txtSelectedStudent.setText(
                     AppConfiguration.swimComptitionStudentName);*/

            Button relMenu = (Button) view.findViewById(R.id.relMenu);
            relMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  ClearArray();
                    Intent i = new Intent(SwimcompititionRegisterStep4_listActivity.this, com.waterworks.SwimcompititionRegisterStep3Activity.class);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                    //  finish();
                    //  ClearArray();
                    //     Intent i=new Intent(SwimcompititionRegisterStep3Activity.this,SwimcompititionRegisterStep2Activity.class);

                    //  ClearArray();
              /* Intent i=new Intent(SwimcompititionRegisterStep3Activity.this,SwimcompititionRegisterStep2Activity.class);
                i.putExtra("datevalue", DateValue);
                startActivity(i);*/
                    // finish();
                    //  ClearArray();
                }
            });
            tempid = AppConfiguration.swimComptitionStudentID.toString().split(
                    "\\,");
            tempname = AppConfiguration.swimComptitionStudentName.toString()
                    .split("\\,");

            _nameid = "";
            _nameid1 = "";
            _nameid2 = "";
            _nameid3 = "";
            _nameid4 = "";
            _nameid5 = "";

            for (int i = 0; i < tempid.length; i++) {
              /*  _nameid1=tempid[0].toString().trim();
                _nameid2=tempid[1].toString().trim();
                _nameid3=tempid[2].toString().trim();*/
             /*   _nameid4=tempid[3].toString().trim();
                _nameid5=tempid[4].toString().trim();*/

                _nameid = _nameid + tempid[i].toString().trim() + ":"
                        + tempname[i].toString().trim() + ",";
            }
            if (_nameid3 == "" || _nameid3 == null || _nameid3 == "NULL") {
                _nameid3 = "";
            }
            _StudentID.clear();
            _StudentName.clear();

            _StudentID = new ArrayList<String>(Arrays.asList(tempid));
            _StudentName = new ArrayList<String>(Arrays.asList(tempname));
            int temp_index = _nameid.lastIndexOf(",");
            Log.i(TAG, "nameid = " + _nameid.substring(0, temp_index));
          /*  Log.i(TAG, "_nameid1 = " + _nameid1);
            Log.i(TAG, "_nameid2 = " + _nameid2);
            Log.i(TAG, "_nameid3 = " + _nameid3);
            Log.i(TAG, "_nameid4 = " + _nameid4);
            Log.i(TAG, "_nameid5 = " + _nameid5);*/
            String s = AppConfiguration.swimComptitionStudentID.toString();
            StringTokenizer st = new StringTokenizer(s, ",");
            try {
                if (st.hasMoreTokens()) {
                    firstid = st.nextToken();

                    secondid = st.nextToken();
                    thirdid = st.nextToken();
                } else {
                    firstid = "";

                    secondid = "";
                    thirdid = "";
                }

            } catch (NoSuchElementException e) {
                e.toString();
                // firstname="";
                Log.i(TAG, "firstid exception = " + e.toString());
            }
            String s1 = AppConfiguration.swimComptitionStudentName.toString();
            StringTokenizer st1 = new StringTokenizer(s1, ",");
        }
        st_1 = (LinearLayout) findViewById(R.id.st_1);
        st_2 = (LinearLayout) findViewById(R.id.st_2);
        st_3 = (LinearLayout) findViewById(R.id.st_3);
        main_lay = (LinearLayout) findViewById(R.id.main_lay);
        select_1 = (View) findViewById(R.id.select_1);
        select_2 = (View) findViewById(R.id.select_2);
        select_3 = (View) findViewById(R.id.select_3);
        select_1.setTag(1);
        select_2.setTag(2);
        select_3.setTag(3);
        name_1 = (TextView) findViewById(R.id.name_1);
        name_2 = (TextView) findViewById(R.id.name_2);
        name_3 = (TextView) findViewById(R.id.name_3);
        name_1.setText(firstname);
        name_2.setText(secondname);
        name_3.setText(thirdname);
        main_lay.setVisibility(View.VISIBLE);


           st_1.setBackgroundColor((getResources().getColor(R.color.grey_days_selection)));
            name_2.setTextColor(Color.WHITE);
            name_1.setTextColor(Color.BLACK);
            select_2.setVisibility(View.VISIBLE);


        if(secondid==null) {
            Log.i(TAG, " st_2 null= " + secondid);
            name_2.setText("");
           // st_2.setVisibility(View.GONE);

        }
        else {
            Log.i(TAG, " st_2 not null= " + secondid);
           // st_2.setVisibility(View.VISIBLE);
        }
        if(thirdid==null) {
            Log.i(TAG, " st_3 null= " + thirdid);
            name_3.setText("");
          //  st_3.setVisibility(View.GONE);

        }
        else {
            Log.i(TAG, " st_3 not null= " + thirdid);
           // st_3.setVisibility(View.VISIBLE);
        }
        st_2.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Log.i(TAG, "first st_1 = " + firstid);

                select_1.setVisibility(View.VISIBLE);
                select_2.setVisibility(View.GONE);
                select_3.setVisibility(View.GONE);
             /*   select_4.setVisibility(View.GONE);
                select_5.setVisibility(View.GONE);*/

                //   name_1.setTextColor(getResources().getColor(R.color.orange));

          /*      name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));*/

                //  decide_layout(last_view(), select_1);
                // animation_slide( temp1,  temp3,  view_1, view_2);
                last_clicked_position=1;

                overridePendingTransition(0, 0);


            }
        });
        st_1.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(secondid!=null) {
                    //  firstid=secondid;
                    Log.i(TAG, "first st_2 = " + secondid);
                    // listview_fillup();
                    select_1.setVisibility(View.GONE);
                    select_2.setVisibility(View.VISIBLE);
                    select_3.setVisibility(View.GONE);


                    //   name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                    //   name_2.setTextColor(getResources().getColor(R.color.orange));
               /* name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));*/

                    Intent i = new Intent(getApplicationContext(), com.waterworks.SwimcompititionRegisterStep4Activity.class);
                    i.putExtra("firstname", firstname);
                    i.putExtra("secondname",secondname);
                    i.putExtra("thirdname",thirdname);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                    overridePendingTransition(0, 0);

                    //  decide_layout(last_view(), select_2);
                    // animation_slide(temp1, temp3, view_1, view_2);
                    last_clicked_position = 2;

                }else{
                    Log.i(TAG, "first st_2 not null= " + firstid);
                    Intent i = new Intent(getApplicationContext(), com.waterworks.SwimcompititionRegisterStep4Activity.class);
                    i.putExtra("firstname", firstname);
                    i.putExtra("secondname",secondname);
                    i.putExtra("thirdname", thirdname);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                    overridePendingTransition(0, 0);

                }
            }
        });
        st_3.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(thirdid!=null) {
                    //  firstid=secondid;
                    Log.i(TAG, "third st_3 = " + thirdid);
                    // listview_fillup();
                    select_1.setVisibility(View.GONE);
                    select_2.setVisibility(View.VISIBLE);
                    select_3.setVisibility(View.GONE);


                    //   name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                    //   name_2.setTextColor(getResources().getColor(R.color.orange));
               /* name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));*/

                    Intent i = new Intent(getApplicationContext(), SwimcompititionRegisterStep4_list4_Activity.class);
                    i.putExtra("firstname", firstname);
                    i.putExtra("secondname",secondname);
                    i.putExtra("thirdname",thirdname);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                    overridePendingTransition(0, 0);

                    //decide_layout(last_view(), select_2);
                    // animation_slide(temp1, temp3, view_1, view_2);
                    last_clicked_position = 2;

                }else{
                    Log.i(TAG, "first st_2 not null= " + firstid);
                    Intent i = new Intent(getApplicationContext(), com.waterworks.SwimcompititionRegisterStep4Activity.class);
                    i.putExtra("firstname", firstname);
                    i.putExtra("secondname",secondname);
                    i.putExtra("thirdname",thirdname);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                    overridePendingTransition(0, 0);

                }
            }
        });
        try {
            StudentName = new ArrayList<String>();
            StudentName = com.waterworks.SwimcompititionRegisterStep3Activity.t_sname;
            Log.d("StudentName"+TAG, StudentName.toString());
            StudentId = new ArrayList<String>();
            StudentId = SwimcompititionRegisterStep3Activity.t_sid;
            Log.d("StudentId"+TAG, StudentId.toString());
            Log.d("StudentName"+TAG, StudentName.toString());
            step3_rg1_2 = new ArrayList<String>();
            step3_jumpingwall2 = new ArrayList<String>();
            step3_rg2_2 = new ArrayList<String>();
            step3_endlane_2 = new ArrayList<String>();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < StudentId.size(); i++) {
            step3_rg1_2.add("0");
            step3_jumpingwall2.add("0");
            step3_rg2_2.add("0");
            step3_endlane_2.add("0");
        }
        //TextView tv_swim_comp_regi3_name = (TextView)findViewById(R.id.tv_swim_comp_regi3_name);

        for (int i = 0; i < StudentName.size(); i++) {
            final int pos = i;


            RadioButton radiobtnWater = (RadioButton) findViewById(R.id.radiobtnWater);
            RadioButton radiobtnWall = (RadioButton)findViewById(R.id.radiobtnWall);
            RadioButton radiobtndivingblock = (RadioButton) findViewById(R.id.radiobtndivingblock);
            RadioButton radiobtnYes = (RadioButton) findViewById(R.id.radiobtnYes);
            RadioButton radiobtnNoPreferences = (RadioButton) findViewById(R.id.radiobtnNoPreferences);
            step3_rg1_2.remove(pos);
            step3_rg1_2.add(pos, "0" + "|" + StudentId.get(pos));
            step3_jumpingwall2.remove(pos);
            step3_jumpingwall2.add(pos, "from in the water");
            radiobtnNoPreferences.setChecked(true);
            radiobtndivingblock.setChecked(true);
            step3_rg2_2.remove(pos);
            step3_rg2_2.add(pos, "1" + "|" + StudentId.get(pos));
            step3_endlane_2.remove(pos);
            step3_endlane_2.add(pos, "No Preference");
            radiobtnWater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked) {
                        step3_rg1_2.remove(pos);
                        step3_rg1_2.add(pos, "0" + "|" + StudentId.get(pos));
                        step3_jumpingwall2.remove(pos);
                        step3_jumpingwall2.add(pos, "from in the water");
                    }
                }
            });
            radiobtnWall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked) {
                        step3_rg1_2.remove(pos);
                        step3_rg1_2.add(pos, "1" + "|" + StudentId.get(pos));
                        step3_jumpingwall2.remove(pos);
                        step3_jumpingwall2.add(pos, "by jumping off the wall");
                    }
                }
            });

            radiobtndivingblock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    step3_rg1_2.remove(pos);
                    step3_rg1_2.add(pos, "2" + "|" + StudentId.get(pos));
                    step3_jumpingwall2.remove(pos);
                    step3_jumpingwall2.add(pos, "by jumping or diving off the tall diving block");
                }
            });

            radiobtnYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked) {
                        step3_rg2_2.remove(pos);
                        step3_rg2_2.add(pos, "0" + "|" + StudentId.get(pos));
                        step3_endlane_2.remove(pos);
                        step3_endlane_2.add(pos, "Yes");
                    }
                }
            });

            radiobtnNoPreferences.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked) {
                        step3_rg2_2.remove(pos);
                        step3_rg2_2.add(pos, "1" + "|" + StudentId.get(pos));
                        step3_endlane_2.remove(pos);
                        step3_endlane_2.add(pos, "No Preference");
                    }
                }
            });

            if (radiobtnWater.isChecked()) {

                //				AppConfiguration.step3_rg1 = "0" + "|"
                //						+ AppConfiguration.swimComptitionStudentID;
                //				AppConfiguration.step3_jumpingwall = "Would like to Start the race from in the water";
            } else if (radiobtnWall.isChecked()) {

                //				AppConfiguration.step3_rg1 = "1" + "|"
                //						+ AppConfiguration.swimComptitionStudentID;
                //				AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping off the wall";
            } else {

                //				AppConfiguration.step3_rg1 = "2" + "|"
                //						+ AppConfiguration.swimComptitionStudentID;
                //				AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping or diving off the tall diving block";
            }

            if (radiobtnYes.isChecked()) {

                //				AppConfiguration.step3_rg2 = "0" + "|"
                //						+ AppConfiguration.swimComptitionStudentID;
                //				AppConfiguration.step3_endlane = "Yes";
            } else {

                //				AppConfiguration.step3_rg2 = "1" + "|"
                //						+ AppConfiguration.swimComptitionStudentID;
                //				AppConfiguration.step3_endlane = "No Preference";
            }

            btnContinueStep3 = (Button) findViewById(R.id.btnContinueStep4);
            btnContinueStep3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //  checkRadioStringValue();

                    if (registerAsVolunteer == true) {
                        if (AppConfiguration.NoofVolunteersvalue == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Please select no.of volunteers ",
                                    Toast.LENGTH_LONG).show();
                        } else {

//                            Log.d("rg1", AppConfiguration.step3_rg1_2);
//                            Log.d("rg2", AppConfiguration.step3_rg1_2);
                            Log.d("no.of Volu", ""
                                    + AppConfiguration.NoofVolunteersvalue);
                            Log.d(TAG, "rg1 = " + step3_rg1_2);
                            Log.d(TAG, "rg2 = " + step3_rg2_2);
                            Log.d(TAG, "no.of Volu = "
                                    + AppConfiguration.NoofVolunteersvalue2);

                            Intent i = new Intent(SwimcompititionRegisterStep4_listActivity.this,
                                    SwimcompititionRegisterStep5Activity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    } else {
                        AppConfiguration.NoofVolunteersvalue2 = 0;

//                        Log.d("rg1", AppConfiguration.step3_rg1_2);
//                        Log.d("rg2", AppConfiguration.step3_rg2_2);
                        Log.d("no.of Volu", ""
                                + AppConfiguration.NoofVolunteersvalue2);
                        Log.d(TAG, "rg1 = " + step3_rg1_2);
                        Log.d(TAG, "rg2 = " + step3_rg2_2);
                        Log.d(TAG, "no.of Volu = "
                                + AppConfiguration.NoofVolunteersvalue);

                        Intent i = new Intent(SwimcompititionRegisterStep4_listActivity.this,
                                SwimcompititionRegisterStep5Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

                }
            });
        }
    }
    @SuppressWarnings("deprecation")
    public AlertDialog onDetectNetworkState () {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }


    /*public void checkRadioStringValue() {
        // Step 3 first radio group
        //		if (radiobtnWater.isChecked()) {
        //			AppConfiguration.step3_rg1 = "0" + "|"
        //					+ AppConfiguration.swimComptitionStudentID;
        //			AppConfiguration.step3_jumpingwall = "Would like to Start the race from in the water";
        //		} else if (radiobtnWall.isChecked()) {
        //			AppConfiguration.step3_rg1 = "1" + "|"
        //					+ AppConfiguration.swimComptitionStudentID;
        //			AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping off the wall";
        //		} else {
        //			AppConfiguration.step3_rg1 = "2" + "|"
        //					+ AppConfiguration.swimComptitionStudentID;
        //			AppConfiguration.step3_jumpingwall = "Would like to Start the race by jumping or diving off the tall diving block";
        //		}
        //
        //		// Step 3 second radio group
        //		if (radiobtnYes.isChecked()) {
        //			AppConfiguration.step3_rg2 = "0" + "|"
        //					+ AppConfiguration.swimComptitionStudentID;
        //			AppConfiguration.step3_endlane = "Yes";
        //		} else {
        //			AppConfiguration.step3_rg2 = "1" + "|"
        //					+ AppConfiguration.swimComptitionStudentID;
        //			AppConfiguration.step3_endlane = "No Preference";
        //		}

        // step 3 third radio group
        if (radiobtnVolunteerYes.isChecked())
            registerAsVolunteer = true;
        else
            registerAsVolunteer = false;
    }*/
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//		super.onBackPressed();
        Intent i=new Intent(SwimcompititionRegisterStep4_listActivity.this,SwimcompititionRegisterStep3_listActivity.class);
        i.putExtra("datevalue", DateValue);
        i.putExtra("time", time);
        i.putExtra("eventdates", eventdates);
        i.putExtra("MeetDate_Display",MeetDate_Display);
        startActivity(i);

    }
}









