package com.waterworks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwimcompititionRegisterStep4Activity extends Activity {

    String message, DateValue, eventdates, time, currentStudentID;
    TextView txtCompititionVal, swimerstartTxt1, swimerendTxt1, txtStudentNameTabs;
    LinearLayout llTabs, lay_1;
    String TAG = "SwimcompititionRegisterStep4Activity";
    String token, familyID, studentname1, studentname2;
    CardView btnContinueStep3;
    TextView txtSelectedStudent, txtMeetDate;
    RadioButton radiobtnWater, radiobtnWall, radiobtndivingblock, radiobtnYes, radiobtnNoPreferences;
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> tempid = new ArrayList<>();
    ArrayList<String> tempname = new ArrayList<>();
    public static ArrayList<String> StudentName;
    private ArrayList<HashMap<String, String>> step3_jumpingwall, step3_endlane;
    String MeetDate_Display,
            selectedStudent, selectedStudentName;
    String[] Displayname;
    RadioGroup rg1, rg2;
    Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition4_new);

        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        Intent intent = getIntent();
        if (null != intent) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue = intent.getStringExtra("datevalue");
            time = intent.getStringExtra("time");
            MeetDate_Display = intent.getStringExtra("MeetDate_Display");
            Log.d(TAG, "eventdates=" + eventdates);
            Log.d(TAG, "DateValue=" + DateValue);
        }

        initViews();
        Animation animSlidInLeft = AnimationUtils.loadAnimation(mcontext, R.anim.slide_in_right);
        lay_1.startAnimation(animSlidInLeft);
        setListners();

//        tempid = AppConfiguration.swimComptitionStudentID.toString().trim().split("\\,");
//        tempname = AppConfiguration.swimComptitionStudentName.toString().split("\\,");

        String[] tempEventList = AppConfiguration.SelectedEventDataStep2.split(",");
        List<String> newTempEventList = new ArrayList<String>(Arrays.asList(tempEventList));

        /*List<String> TempID = new ArrayList<String>(Arrays.asList(tempid));

        for (int i = 0; i < TempID.size(); i++) {
            for (int j = 0; j < newTempEventList.size(); j++) {
                if (newTempEventList.get(j).toString().contains(TempID.get(i).toString().trim())) {
                    newTempID.add(TempID.get(i).toString().trim());
                }
            }
        }*/

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


        swimerstartTxt1.setText("Where should" + " " + " " + tempname.get(0) + " " + " " + "start the race?");
        swimerendTxt1.setText("Should " + " " + " " + tempname.get(0) + " " + " " + "swim next to the wall in an end lane?");
//        selectedStudent = tempid.get(0).toString();
//        selectedStudentName = tempname.get(0).toString();


        Log.d("megha name", selectedStudentName);
//        filling step3_jumpingwall default radio button values
        HashMap<String, String> hashMapStep3_jumpingwall = new HashMap<>();
        for (int i = 0; i < tempid.size(); i++) {
            hashMapStep3_jumpingwall.put(tempid.get(i).toString().trim(), "1");
        }
        step3_jumpingwall.add(hashMapStep3_jumpingwall);

        //filling step3_endlane default radio button values
        HashMap<String, String> hashMapStep3_endlane = new HashMap<>();
        for (int i = 0; i < tempid.size(); i++) {
            hashMapStep3_endlane.put(tempid.get(i).toString().trim(), "1");

        }
        step3_endlane.add(hashMapStep3_endlane);
    }

    public void fillJumpingWallValues(String selectedValue) {
        step3_jumpingwall.get(0).put(selectedStudent, selectedValue);
    }

    public void fillEndLanePrefValues(String selectedValue) {
        step3_endlane.get(0).put(selectedStudent, selectedValue);
    }

    public void initViews() {
        setTitleBar();
        lay_1 = (LinearLayout) findViewById(R.id.lay_1);
        radiobtnWater = (RadioButton) findViewById(R.id.radiobtnWater);
        radiobtnWall = (RadioButton) findViewById(R.id.radiobtnWall);
        radiobtndivingblock = (RadioButton) findViewById(R.id.radiobtndivingblock);
        radiobtnYes = (RadioButton) findViewById(R.id.radiobtnYes);
        radiobtnNoPreferences = (RadioButton) findViewById(R.id.radiobtnNoPreferences);
        rg1 = (RadioGroup) findViewById(R.id.inst_types_grp1);
        rg2 = (RadioGroup) findViewById(R.id.inst_types_grp);
        btnContinueStep3 = (CardView) findViewById(R.id.btnContinueStep4);
        txtCompititionVal = (TextView) findViewById(R.id.txtCompititionVal);
        txtCompititionVal.setText(SwimcompititionRegisterStep2Activity.MeetDate_Display);
        swimerstartTxt1 = (TextView) findViewById(R.id.swimerstartnametxt1);
        swimerendTxt1 = (TextView) findViewById(R.id.swimerendtxt1);
        step3_jumpingwall = new ArrayList<>();
        step3_endlane = new ArrayList<>();
        Animation animSlidInRight = AnimationUtils.loadAnimation(mcontext, R.anim.slide_in_right);
        lay_1.startAnimation(animSlidInRight);
    }

    public void setListners() {
        radiobtnWater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fillJumpingWallValues("0");
            }
        });
        radiobtnWall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fillJumpingWallValues("1");
            }
        });
        radiobtndivingblock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fillJumpingWallValues("2");
            }
        });
        radiobtnYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fillEndLanePrefValues("0");
            }
        });
        radiobtnNoPreferences.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fillEndLanePrefValues("1");
            }
        });
        btnContinueStep3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuilder step3_jumpingwallString = new StringBuilder();
                for (int j = 0; j < tempid.size(); j++) {
                    step3_jumpingwallString.append(step3_jumpingwall.get(0).get(tempid.get(j).trim()) + "|" + tempid.get(j).toString().trim());
                    if (j != tempid.size() - 1)
                        step3_jumpingwallString.append(",");
                }
                StringBuilder step3_endLaneString = new StringBuilder();
                for (int j = 0; j < tempid.size(); j++) {
                    step3_endLaneString.append(step3_endlane.get(0).get(tempid.get(j).trim()) + "|" + tempid.get(j).toString().trim());
                    if (j != tempid.size() - 1)
                        step3_endLaneString.append(",");
                }
                AppConfiguration.step3_jumpingwall = step3_jumpingwallString.toString();
                AppConfiguration.step3_endlane = step3_endLaneString.toString();
                Log.e(TAG, AppConfiguration.step3_jumpingwall);
                Log.e(TAG, AppConfiguration.step3_endlane);

                int selectedId = rg1.getCheckedRadioButtonId();
                int selectedIdyes = rg2.getCheckedRadioButtonId();

                if (selectedId >= 0 && selectedIdyes >= 0) {

                    Intent i = new Intent(SwimcompititionRegisterStep4Activity.this, SwimcompititionRegisterStep5Activity.class);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);

                    startActivity(i);
                } else {
                    Toast.makeText(mcontext, "Please select at least one event for each child.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void makeTabs(String studentID) {
        llTabs = (LinearLayout) findViewById(R.id.llTabs);
        for (int i = 0; i < tempid.size(); i++) {
            View childTabs = getLayoutInflater().inflate(R.layout.layout_tabs, null, false);
            txtStudentNameTabs = (TextView) childTabs.findViewById(R.id.txtStudentNameTabs);
            View viewOrangeBar = (View) childTabs.findViewById(R.id.viewOrangeBar);
            txtStudentNameTabs.setText(tempname.get(i).toString());

            Animation animSlidInRight = AnimationUtils.loadAnimation(mcontext, R.anim.slide_in_right);
            viewOrangeBar.startAnimation(animSlidInRight);

            childTabs.setTag(tempid.get(i).toString() + "," + tempname.get(i).toString());
            selectedStudentName = tempid.get(i).toString() + "," + tempname.get(i).toString();
//            currentStudentID = tempid.get(i).toString();
            childTabs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            childTabs.setOnClickListener(myClickLIstener);

            currentStudentID = tempid.get(0).toString();
            if (!childTabs.getTag().equals(studentID)) {
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

    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(final View v) {
            llTabs.removeAllViews();
            makeTabs(v.getTag().toString());
            selectedStudent = v.getTag().toString().trim();
            currentStudentID = v.getTag().toString().trim();

            radiobtnWater.setChecked((step3_jumpingwall.get(0).get(selectedStudent).equals("0")) ? true : false);
            radiobtnWall.setChecked((step3_jumpingwall.get(0).get(selectedStudent).equals("1")) ? true : false);
            radiobtndivingblock.setChecked((step3_jumpingwall.get(0).get(selectedStudent).equals("2")) ? true : false);
            radiobtnYes.setChecked((step3_endlane.get(0).get(selectedStudent).equals("0")) ? true : false);
            radiobtnNoPreferences.setChecked((step3_endlane.get(0).get(selectedStudent).equals("1")) ? true : false);

//           05/01/2016 megha
            selectedStudentName = v.getTag().toString().trim();
            Displayname = selectedStudentName.split("\\,");
            swimerstartTxt1.setText("Where should" + " " + " " + Displayname[1] + " " + " " + "start the race?");
            swimerendTxt1.setText("Should " + " " + " " + Displayname[1] + " " + " " + "swim next to the wall in an end lane?");

            Animation animSlidInRight = AnimationUtils.loadAnimation(mcontext, R.anim.slide_in_left);
            lay_1.startAnimation(animSlidInRight);

//            radiobtnWater.setChecked((step3_jumpingwall.get(0).get(selectedStudent).equals("0")) ? true : false);
//            radiobtnWall.setChecked((step3_jumpingwall.get(0).get(selectedStudent).equals("1")) ? true : false);
//            radiobtndivingblock.setChecked((step3_jumpingwall.get(0).get(selectedStudent).equals("2")) ? true : false);
//            radiobtnYes.setChecked((step3_endlane.get(0).get(selectedStudent).equals("0")) ? true : false);
//            radiobtnNoPreferences.setChecked((step3_endlane.get(0).get(selectedStudent).equals("1")) ? true : false);


        }
    };

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
                Intent intentCheckin = new Intent(SwimcompititionRegisterStep4Activity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        super.onBackPressed();
        Intent i3 = new Intent(SwimcompititionRegisterStep4Activity.this, SwimcompititionRegisterStep3Activity.class);
        startActivity(i3);
    }
}
