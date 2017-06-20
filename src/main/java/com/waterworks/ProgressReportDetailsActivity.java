package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.waterworks.adapter.StudentSkillExpListAdapter;
import com.waterworks.asyncTasks.RemainingSkillSetsAsyncTask;
import com.waterworks.asyncTasks.StudentPrevLevelAsyncTask;
import com.waterworks.asyncTasks.StudentProgressLevelAsyncTask;
import com.waterworks.asyncTasks.StudentProgressLevelSkillListAsyncTask;
import com.waterworks.model.ExpandableListChildModel;
import com.waterworks.model.StudentSkills;
import com.waterworks.model.TokenStudentID;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ProgressReportDetailsActivity extends Activity {

    ArrayList<StudentSkills> studentSkillsArrayList = new ArrayList<StudentSkills>();
    TokenStudentID tokenStudentID = null;
    ArrayList<String> studentRemainingSkillsArraylist = null;
    ArrayList<String> listDataHeader = null;
    HashMap<String, List<ExpandableListChildModel>> listDataChild = null;
    View includeExpList;
    ExpandableListView lvExp = null;

    LinearLayout lin_lay = null, relStudentRemainingSkill = null, relStudentSkill = null, llLevelBoxesLayout = null, llExpandableListView = null;

    Context mContext = null;

    //    ListView listStudentSkill;//listStudentRemainingSkill
    TextView title, txtNothingToDisplay, txtStudentName;
    String successViewCertificate;
    String TAG = "ViewCerfiticate";
    String studProgStr, skillLevelStr, remainingSkillSetsStr, studPrevLevel;

    String studentID = null, firstname = null, lastname = null;
    TextView txtLevel, txtCompletePercentage;
    ScrollView ScrollProgressRpt;
    StudentProgressLevelSkillListAsyncTask studentProgressLevelSkillListAsyncTask = null;
    StudentProgressLevelAsyncTask studentProgressLevelAsyncTask = null;
    RemainingSkillSetsAsyncTask remainingSkillSetsAsyncTask = null;
    StudentPrevLevelAsyncTask studentPrevLevelAsyncTask = null;
    ExpandableListChildModel expandableListChildModel = null;

    String token, familyID;
    ProgressDialog progressDialog;
    Boolean isInternetPresent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_report_details_activity);
        mContext = this.getApplicationContext();
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        // fetching header view
        View view = findViewById(R.id.header);

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ProgressReportDetailsActivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        studentID = getIntent().getStringExtra("studentID");
        firstname = getIntent().getStringExtra("Firstname");
        lastname = getIntent().getStringExtra("Lastname");

        txtLevel = (TextView) findViewById(R.id.txtLevel);
        txtCompletePercentage = (TextView) findViewById(R.id.txtCompletePercentage);


        title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Progress Report");//
        txtStudentName = (TextView) findViewById(R.id.txtStudentName);
        txtStudentName.setText(firstname + " " + lastname);
//        txtNothingToDisplay = (TextView)findViewById(R.id.txtNothingToDisplay);
//        llExpandableListView = (LinearLayout) findViewById(R.id.llExpandableListView);

        ScrollProgressRpt = (ScrollView) findViewById(R.id.ScrollProgressRpt);
        lin_lay = (LinearLayout) findViewById(R.id.lin_lay);
        relStudentRemainingSkill = (LinearLayout) findViewById(R.id.relStudentRemainingSkill);
        relStudentSkill = (LinearLayout) findViewById(R.id.relStudentSkill);
        llLevelBoxesLayout = (LinearLayout) findViewById(R.id.llLevelBoxesLayout);


//        includeExpList = (View) findViewById(R.id.includeExpList);
        lvExp = (ExpandableListView) findViewById(R.id.lvExp);
        lvExp.setDivider(null);
        lvExp.setDividerHeight(0);
        lvExp.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        tokenStudentID = new TokenStudentID();
        tokenStudentID.setToken(token);
        tokenStudentID.setStudentID(studentID);

        InitialRequests();
    }

    public void InitialRequests() {
        progressDialog = new ProgressDialog(ProgressReportDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isInternetPresent = Utility.isNetworkConnected(ProgressReportDetailsActivity.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        studentProgressLevelAsyncTask = new StudentProgressLevelAsyncTask(ProgressReportDetailsActivity.this, tokenStudentID);
                        studProgStr = studentProgressLevelAsyncTask.execute().get();

                        studentProgressLevelSkillListAsyncTask = new StudentProgressLevelSkillListAsyncTask(ProgressReportDetailsActivity.this, tokenStudentID);
                        skillLevelStr = studentProgressLevelSkillListAsyncTask.execute().get();

                        remainingSkillSetsAsyncTask = new RemainingSkillSetsAsyncTask(ProgressReportDetailsActivity.this, tokenStudentID);
                        remainingSkillSetsStr = remainingSkillSetsAsyncTask.execute().get();

                        studentPrevLevelAsyncTask = new StudentPrevLevelAsyncTask(ProgressReportDetailsActivity.this, tokenStudentID);
                        studPrevLevel = studentPrevLevelAsyncTask.execute().get();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                readAndParseStudentProgressLevelJSON(studProgStr);
                                readAndParseStudentProgressLevelSkillListJSON(skillLevelStr);
                                readAndParseStudentRemainingSkillJSON(remainingSkillSetsStr);
                                readAndParseStudentPreviousLevelJSON(studPrevLevel);
                                progressDialog.dismiss();
                                ScrollProgressRpt.post(new Runnable() {
                                    public void run() {
                                        ScrollProgressRpt.scrollTo(ScrollProgressRpt.getTop(), 0);
                                    }
                                });
                            }
                        });
                    }
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

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public void readAndParseStudentProgressLevelJSON(String in) {
        String levelName = null;
        try {
            JSONObject reader = new JSONObject(in);
            successViewCertificate = reader.getString("Msg");
            if (successViewCertificate.toString().equals("Success")) {

                for (int i = 0; i < reader.length(); i++) {

                    levelName = reader.getString("LevelName").trim();
                    String totalSkills = reader.getString("TotalSkills").trim();
                    String studentSkills = reader.getString("StudentSkills").trim();
                    String percentage = reader.getString("Percentage").trim();

                    txtLevel.setText(levelName);
                    txtCompletePercentage.setText(percentage + "%");
                    setBarLayout(Integer.parseInt(totalSkills), Integer.parseInt(studentSkills));

                }
                createLevelBoxesLayout(levelName);

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void readAndParseStudentProgressLevelSkillListJSON(String in) {
        StudentSkills studentSkills = null;
        try {
            JSONObject reader = new JSONObject(in);
            successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("Skills");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    studentSkills = new StudentSkills();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    studentSkills.setDate(jsonChildNode.getString("Date").trim());
                    studentSkills.setSkills(jsonChildNode.getString("Skills").trim());
                    studentSkills.setSkillNo(jsonChildNode.getString("Skill No").trim());

                    studentSkillsArrayList.add(studentSkills);
                }

            } else {

            }
            createStudentSkillListLayout(studentSkillsArrayList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAndParseStudentRemainingSkillJSON(String in) {
        studentRemainingSkillsArraylist = new ArrayList<String>();
        try {
            JSONObject reader = new JSONObject(in);
            successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("Skills");

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String skillsRemaining = jsonChildNode.getString("Skills").trim().replace("\\r", "").replace("\\n", "");
                    studentRemainingSkillsArraylist.add(skillsRemaining);
                }

            } else {

            }
            createRemainingSkillLayout(studentRemainingSkillsArraylist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAndParseStudentPreviousLevelJSON(String in) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ExpandableListChildModel>>();
        List<ExpandableListChildModel> skillList = null;

        List<LinearLayout> skillList1 = null;


        try {
            JSONObject reader = new JSONObject(in);
            successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("StudentSkills");

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    listDataHeader.add(jsonChildNode.getString("Level").trim());

                    JSONArray skillArray = jsonChildNode.optJSONArray("Skills");
                    skillList = new ArrayList<ExpandableListChildModel>();
                    String lastDate = "";
                    for (int j = 0; j < skillArray.length(); j++) {
                        JSONObject skillArrObj = skillArray.getJSONObject(j);
                        expandableListChildModel = new ExpandableListChildModel();
                        String newDate = skillArrObj.getString("SkillDate").trim();
                        String s = "";
                        if (!newDate.equals(lastDate)) {
                            expandableListChildModel.setDate(skillArrObj.getString("SkillDate").trim());
                            expandableListChildModel.setItem(skillArrObj.getString("SkillsData").trim());
                        } else {
                            expandableListChildModel.setDate("");
                            expandableListChildModel.setItem(skillArrObj.getString("SkillsData").trim());
                        }

                        lastDate = skillArrObj.getString("SkillDate").trim();
                        skillList.add(expandableListChildModel);
                    }

                    listDataChild.put(listDataHeader.get(i), skillList);
                }

            } else {

            }

            if (listDataChild.size() == 0) {
                txtNothingToDisplay.setVisibility(View.VISIBLE);

                float scale = getResources().getDisplayMetrics().density;
                int width = (int) (300 * scale + 0.5f);
                LinearLayout.LayoutParams llExpandableListParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                llExpandableListParams.width = width;
                llExpandableListView.setLayoutParams(llExpandableListParams);
            } else {
                StudentSkillExpListAdapter studentSkillExpListAdapter = new StudentSkillExpListAdapter(this, listDataHeader, listDataChild);
                lvExp.setAdapter(studentSkillExpListAdapter);
            }
            lvExp.setFocusable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setBarLayout(int totalSkills, int obtainedSkills) {
        {

            if (lin_lay != null)
                lin_lay.removeAllViews();

            for (int i = 0; i < obtainedSkills; i++) {
                View view = new View(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                view.setLayoutParams(params);
                view.setBackgroundResource(R.drawable.filled_box);

                lin_lay.addView(view);

                View viewLine = new View(mContext);
                LinearLayout.LayoutParams paramsLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                paramsLine.width = 2;
                viewLine.setLayoutParams(paramsLine);
                viewLine.setBackgroundColor(Color.parseColor("#FFFFFF"));

                lin_lay.addView(viewLine);
            }

            int final_total = totalSkills - obtainedSkills;

            for (int i = 0; i < final_total; i++) {
                View view = new View(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                view.setLayoutParams(params);
                view.setBackgroundResource(R.drawable.blank_box);

                lin_lay.addView(view);

                View viewLine = new View(mContext);
                LinearLayout.LayoutParams paramsLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                paramsLine.width = 2;
                viewLine.setLayoutParams(paramsLine);
                viewLine.setBackgroundColor(Color.parseColor("#FF8D31"));

                lin_lay.addView(viewLine);
            }
        }
    }

    public void createRemainingSkillLayout(ArrayList<String> studentRemainingSkillsArraylist) {
        float scale = getResources().getDisplayMetrics().density;
        int paddingBottom = (int) (1 * scale + 0.5f);

        if (studentRemainingSkillsArraylist.size() > 0) {
            for (int i = 0; i < studentRemainingSkillsArraylist.size(); i++) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(5 * paddingBottom, 2 * paddingBottom, paddingBottom, 0);

                TextView view = new TextView(mContext);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                view.setPadding(0, 0, 0, paddingBottom);
                view.setText(studentRemainingSkillsArraylist.get(i).toString());
                view.setTextColor(Color.GRAY);
                view.setLayoutParams(params);

                relStudentRemainingSkill.addView(view);
            }
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5 * paddingBottom, 2 * paddingBottom, paddingBottom, 0);

            TextView view = new TextView(mContext);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            view.setTypeface(null, Typeface.BOLD);
            view.setPadding(0, 0, 0, paddingBottom);
            view.setText("No Records to Display");
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.parseColor("#000000"));
            view.setLayoutParams(params);

            relStudentRemainingSkill.addView(view);
        }
    }

    public void createStudentSkillListLayout(ArrayList<StudentSkills> studentSkillsArrayList) {
        float scale = getResources().getDisplayMetrics().density;
        int paddingBottom = (int) (5 * scale + 0.5f);
        int dateTextViewWidth = (int) (80 * scale + 0.5f);

        if (studentSkillsArrayList.size() > 0) {
            for (int i = 0; i < studentSkillsArrayList.size(); i++) {
                LinearLayout.LayoutParams LinearLayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(LinearLayoutparams);

                LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                dateParams.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);
                dateParams.width = dateTextViewWidth;

                TextView textViewDate = new TextView(mContext);
                textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                textViewDate.setText(studentSkillsArrayList.get(i).getDate().toString());
                textViewDate.setTextColor(Color.parseColor("#014378"));
                textViewDate.setTypeface(null, Typeface.BOLD);
                textViewDate.setLayoutParams(dateParams);
                linearLayout.addView(textViewDate);


                LinearLayout.LayoutParams paramsskillno = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //               paramsskillno.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);

                TextView textViewSkillNo = new TextView(mContext);
                textViewSkillNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                textViewSkillNo.setText(studentSkillsArrayList.get(i).getSkillNo().toString());
                textViewSkillNo.setTextColor(Color.parseColor("#000000"));
                textViewSkillNo.setLayoutParams(paramsskillno);
                linearLayout.addView(textViewSkillNo);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //params.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);
                params.setMargins(paddingBottom, 0, paddingBottom, 0);

                TextView textViewSkills = new TextView(mContext);
                textViewSkills.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                textViewSkills.setText(studentSkillsArrayList.get(i).getSkills().toString());
                textViewSkills.setTextColor(Color.parseColor("#000000"));
                textViewSkills.setLayoutParams(params);
                linearLayout.addView(textViewSkills);

                relStudentSkill.addView(linearLayout);
            }
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);

            TextView view = new TextView(mContext);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            view.setTypeface(null, Typeface.BOLD);
            view.setPadding(0, 0, 0, paddingBottom);
            view.setGravity(Gravity.CENTER);
            view.setText("No Records to Display");
            view.setTextColor(Color.parseColor("#000000"));
            view.setLayoutParams(params);

            relStudentSkill.addView(view);
        }
    }

    public void createLevelBoxesLayout(String level) {

        String actuallevel = level;
        if (level.equals("P1") || level.equals("P2") || level.equals("P3")) {
            level = "1";
        }

        float scale = getResources().getDisplayMetrics().density;
        int paddingBottom = (int) (3 * scale + 0.5f);
        int count = 13;

        int obtainedLevel = Integer.parseInt(level);
        int blankLevels = 12 - obtainedLevel;
        for (int i = 0; i < blankLevels + 1; i++) {
            count--;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);

            TextView view = new TextView(mContext);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            view.setPadding(0, 0, 0, paddingBottom);
            view.setText(String.valueOf(count));
            view.setGravity(Gravity.CENTER);
            view.setBackgroundResource(R.drawable.gray_border);
            view.setTextColor(Color.parseColor("#000000"));
            view.setLayoutParams(params);

            llLevelBoxesLayout.addView(view);
        }

        for (int i = 0; i < obtainedLevel - 1; i++) {
            count--;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);

            TextView view = new TextView(mContext);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            view.setPadding(0, 0, 0, paddingBottom);
            view.setText(String.valueOf(count));
            view.setGravity(Gravity.CENTER);
            view.setBackgroundResource(R.drawable.filled_box);
            view.setTextColor(Color.parseColor("#000000"));
            view.setLayoutParams(params);

            llLevelBoxesLayout.addView(view);
        }

        //coding for P1, P2, P3 logic
        int pCount = 4;
        for (int i = 0; i < 3; i++) {
            pCount--;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);

            TextView view = new TextView(mContext);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            view.setPadding(0, 0, 0, paddingBottom);
            view.setText("P" + String.valueOf(pCount));
            view.setGravity(Gravity.CENTER);

            if (actuallevel.equals("P1")) {
                if (i == 2)
                    view.setBackgroundResource(R.drawable.filled_box);
                else
                    view.setBackgroundResource(R.drawable.gray_border);
            } else if (actuallevel.equals("P2")) {
                if (i == 1 || i == 2)
                    view.setBackgroundResource(R.drawable.filled_box);
                else
                    view.setBackgroundResource(R.drawable.gray_border);
            } else if (actuallevel.equals("P3")) {
                if (i == 0 || i == 1 || i == 2)
                    view.setBackgroundResource(R.drawable.filled_box);
                else
                    view.setBackgroundResource(R.drawable.gray_border);
            } else {
                view.setBackgroundResource(R.drawable.filled_box);
            }

            view.setTextColor(Color.parseColor("#000000"));
            view.setLayoutParams(params);
            if (actuallevel.equals("P1") || actuallevel.equals("P2") || actuallevel.equals("P3")) {
                llLevelBoxesLayout.addView(view);
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(paddingBottom, paddingBottom, paddingBottom, 0);

        TextView view = new TextView(mContext);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        view.setPadding(0, 0, 0, paddingBottom);
        view.setText("View All Levels");
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.BLUE);
        view.setLayoutParams(params);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivityViewAllLevels = new Intent(ProgressReportDetailsActivity.this, ActivityViewAllLevels.class);
                startActivity(intentActivityViewAllLevels);
            }
        });

        llLevelBoxesLayout.addView(view);
    }
}
