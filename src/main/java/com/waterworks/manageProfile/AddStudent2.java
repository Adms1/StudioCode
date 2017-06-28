package com.waterworks.manageProfile;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.DashBoardActivity;
import com.waterworks.HomeFragment;
import com.waterworks.LegalDoc_Check_FamilyDoc;
import com.waterworks.R;
import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.asyncTasks.cardDetailAsyncTask;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Harsh Adms on 07/01/2016.
 */
public class AddStudent2 extends Activity {

    Button btnHome;
    CardView btn_update;
    TextView level_number, level_no, btnLevelCalculator;
    String token, familyID;

    String questions = "1", responseString;
    LinearLayout complete_lay;
    LinearLayout llCheckbox, ques_lay;
    private ArrayList<Boolean> isLAFitnessSite = new ArrayList<>();
    CheckBox checkBox;
    private SitesListAsyncTask sitesListAsyncTask = null;
    private ProgressDialog progressDialog;
    String message;
    Context mContext = this;
    Boolean isInternetPresent = false;
    ScrollView scrollView;
    String fromWhere = "";
    boolean isLegalDoc = false;
    String DOMAIN = AppConfiguration.DOMAIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstudent2);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        fromWhere = getIntent().getStringExtra("fromWhere");
        Log.d("fromwhere",fromWhere);
        isInternetPresent = Utility.isNetworkConnected(AddStudent2.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            getSiteList();

            btn_update = (CardView) findViewById(R.id.btn_update);

            scrollView = (ScrollView) findViewById(R.id.scrollView);

            complete_lay = (LinearLayout) findViewById(R.id.complete_lay);
            level_number = (TextView) findViewById(R.id.level_number);
            level_no = (TextView) findViewById(R.id.level_no);
            llCheckbox = (LinearLayout) findViewById(R.id.llCheckbox);
            btnLevelCalculator = (TextView) findViewById(R.id.btnLevelCalculator);

            ques_lay = (LinearLayout) findViewById(R.id.ques_lay);

            View view = findViewById(R.id.header);
            TextView title = (TextView) view.findViewById(R.id.action_title);
            title.setText("Register New Student");

            ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
            ib_menusad.setBackgroundResource(R.drawable.back_arrow);

            Button relMenu = (Button) view.findViewById(R.id.relMenu);
            relMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnHome = (Button) findViewById(R.id.btnHome);
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCheckin = new Intent(AddStudent2.this, DashBoardActivity.class);
                    intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentCheckin);
                    finish();
                }
            });

            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double studentAge = Double.valueOf(AppConfiguration.studentAge);
                    if (studentAge < 3.00) {
                        if (isLAFitnessSite.contains(true)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddStudent2.this);
                            builder.setTitle("Waterworks");
                            builder.setMessage("This facility is currently accepting students 3 years old and older. Please contact our office staff with any questions, or to locate your nearest facility accepting students younger than 3 years old.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    collectValues();
                                    new RegisterChildAsyncTask().execute();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            collectValues();
                            new RegisterChildAsyncTask().execute();
                        }
                    } else {
                        collectValues();
                        new RegisterChildAsyncTask().execute();
                    }
                }
            });


            btnLevelCalculator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            GenerateQuestion("False");
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

    public void getSiteList() {
        progressDialog = new ProgressDialog(AddStudent2.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sitesListAsyncTask = new SitesListAsyncTask(token);
                    responseString = sitesListAsyncTask.execute().get();

                    JSONObject reader = new JSONObject(responseString);
                    JSONArray jsonMainNode = reader.optJSONArray("SiteList");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        isLAFitnessSite.add(jsonChildNode.getBoolean("Lafitness"));
                    }

                    runOnUiThread(new Runnable() {
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

    public void YesMethod(TextView txtQuestions) {
        if (questions.equals("1")) {
            question_string = getResources().getString(R.string.question_2a);
            questions = "2a";
        } else if (questions.equals("2a")) {
            question_string = getResources().getString(R.string.question_3a);
            questions = "3a";
        } else if (questions.equals("3a")) {
            question_string = getResources().getString(R.string.question_4yes);
            questions = "4yes";
        } else if (questions.equals("4yes")) {
            question_string = getResources().getString(R.string.question_5);
            questions = "5";
        } else if (questions.equals("5")) {
            question_string = getResources().getString(R.string.question_6);
            questions = "6";
        } else if (questions.equals("6")) {
            AppConfiguration.levelTypes = "11";
            CompleteIT();
        } else if (questions.equals("2b")) {
            questions = "2b";
            AppConfiguration.levelTypes = "4";
            CompleteIT();
        } else if (questions.equals("3b")) {
            questions = "3b";
            AppConfiguration.levelTypes = "3";
            CompleteIT();
        } else if (questions.equals("4no")) {
            AppConfiguration.levelTypes = "2";
            CompleteIT();
        }
    }
    public void NoMethod(TextView txtQuestions) {

        if (questions.equals("1")) {
            question_string = getResources().getString(R.string.question_2b);
            questions = "2b";
        } else if (questions.equals("2b")) {
            question_string = getResources().getString(R.string.question_3b);
            questions = "3b";
        } else if (questions.equals("3b")) {
            question_string = getResources().getString(R.string.question_4no);
            questions = "4no";
        } else if (questions.equals("4no")) {
            AppConfiguration.levelTypes = "1";
            CompleteIT();
        } else if (questions.equals("2a")) {
            questions = "2a";
            AppConfiguration.levelTypes = "5";
            CompleteIT();
        } else if (questions.equals("3a")) {
            questions = "3a";
            AppConfiguration.levelTypes = "7";
            CompleteIT();
        } else if (questions.equals("4yes")) {
            questions = "4yes";
            AppConfiguration.levelTypes = "8";
            CompleteIT();
        } else if (questions.equals("5")) {
            AppConfiguration.levelTypes = "9";
            CompleteIT();
        } else if (questions.equals("6")) {
            AppConfiguration.levelTypes = "10";
            CompleteIT();
        }
    }
    String question_string = "";
    boolean completed = false;

    public void CompleteIT() {
        if (!ManageStudents.edit) {
            level_number.setText("Level " + AppConfiguration.levelTypes + " - ");
            new getLessByAgeLevel().execute();
            complete_lay.setVisibility(View.VISIBLE);
            completed = true;

        } else {
            finish();
            completed = true;
        }
    }

    public void GenerateQuestion(String set) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_question_lay, null);

        final TextView txtQuestions;
        final RelativeLayout answered_lay;
        final TextView que_no, you_answer;

        final CardView btnYes, btnNoNotSure;
        btnYes = (CardView) view.findViewById(R.id.btnYes);
        btnNoNotSure = (CardView) view.findViewById(R.id.btnNoNotSure);
        txtQuestions = (TextView) view.findViewById(R.id.txtQuestions);
        answered_lay = (RelativeLayout) view.findViewById(R.id.answered_lay);
        que_no = (TextView) view.findViewById(R.id.que_no);
        you_answer = (TextView) view.findViewById(R.id.you_answer);

        if (set.equalsIgnoreCase("True")) {
            txtQuestions.setText(question_string);
        }

        if (txtQuestions.getText().toString().contains("Question #1")) {
            que_no.setText("Question #1");
        } else if (txtQuestions.getText().toString().contains("Question #2")) {
            que_no.setText("Question #2");
        } else if (txtQuestions.getText().toString().contains("Question #3")) {
            que_no.setText("Question #3");
        } else if (txtQuestions.getText().toString().contains("Question #4")) {
            que_no.setText("Question #4");
        } else if (txtQuestions.getText().toString().contains("Question #5")) {
            que_no.setText("Question #5");
        } else if (txtQuestions.getText().toString().contains("Question #6")) {
            que_no.setText("Question #6");
        }

        btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                YesMethod(txtQuestions);
                txtQuestions.setTextColor(R.color.gray_avaibility);
                txtQuestions.setText(txtQuestions.getText().toString().trim().
                                replaceAll(que_no.getText().toString().trim(), "")
                                .replaceAll("\n\n", "")
                );

                answered_lay.setVisibility(View.VISIBLE);
                you_answer.setText(Html.fromHtml("You answered : <b> Yes</b>"));
                btnYes.setVisibility(View.GONE);
                btnNoNotSure.setVisibility(View.GONE);

                if (!completed)
                    GenerateQuestion("True");
            }
        });


        btnNoNotSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NoMethod(txtQuestions);
                txtQuestions.setTextColor(R.color.gray_avaibility);
                txtQuestions.setText(txtQuestions.getText().toString().trim().replaceAll(que_no.getText().toString(), ""));

                answered_lay.setVisibility(View.VISIBLE);
                you_answer.setText(Html.fromHtml("You answered : <b> No</b>"));
                btnYes.setVisibility(View.GONE);
                btnNoNotSure.setVisibility(View.GONE);

                if (!completed)
                    GenerateQuestion("True");
            }
        });

        ques_lay.addView(view);

        AppConfiguration.AutoSmoothScroll(scrollView);
    }


    String successAgeCalculation;

    public class getLessByAgeLevel extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(AddStudent2.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            getAgeCalculationLoading();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            preparingLessionTypeList();
            pd.dismiss();
        }

    }

    public void getAgeCalculationLoading() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("age",
                AppConfiguration.studentAge);
        param.put("Level",
                AppConfiguration.levelTypes);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_Lession_by_LevelAge, param);
        readAndParseJSONLSN(responseString);

    }

    public void readAndParseJSONLSN(String in) {
        AppConfiguration.lessionTypeList_New.clear();
        try {
            JSONObject reader = new JSONObject(in);
            successAgeCalculation = reader.getString("Success");
            if (successAgeCalculation.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String lessonid = jsonChildNode.getString("LessionID")
                            .trim();
                    String lessonname = jsonChildNode.getString("LessionName")
                            .trim();
                    hashmap.put("lessonid", lessonid);
                    hashmap.put("lessonname", lessonname);

                    AppConfiguration.lessionTypeList_New.add(hashmap);
                }

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    message = jsonChildNode.getString("Msg").trim();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preparingLessionTypeList() {
        llCheckbox.removeAllViews();

        for (int i = 0; i < AppConfiguration.lessionTypeList_New.size(); i++) {
            checkBox = new CheckBox(AddStudent2.this);
            checkBox.setId(Integer.parseInt(AppConfiguration.lessionTypeList_New.get(i).get("lessonid")));
            checkBox.setText(AppConfiguration.lessionTypeList_New.get(i).get("lessonname"));
            checkBox.setPadding(5, 5, 5, 5);
            checkBox.setButtonDrawable(R.drawable.custom_check_orange);
            checkBox.setTextColor(getResources().getColor(R.color.black));

            llCheckbox.addView(checkBox);

        }

        AppConfiguration.AutoSmoothScroll(scrollView);
    }


    //Register a Child
    boolean isSaveAndProceedClick = false;

    class RegisterChildAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AddStudent2.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            registerChildProcess();
//            Legal_DOC_chk_method();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            ManageStudents.updated = true;
            if (successRegister.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                if (AppConfiguration.LAFitnessResult.equalsIgnoreCase("true")) {
//                 =====================26-04-2017 megha==========================
//                    Student_Doc_Update();
                    Intent imanage = new Intent(AddStudent2.this,ManageStudents.class);
                    startActivity(imanage);

                            overridePendingTransition(R.anim.slide_in_right,
                                    R.anim.slide_out_left);
                } else {
                    if (fromWhere != null) {
//                        finish();
                        Log.d("fromwhere",fromWhere);
                        Intent imanage = new Intent(AddStudent2.this,ManageStudents.class);
                        startActivity(imanage);
                    }
                }
 //      ==========================================================
                AppConfiguration.levelTypes = "";
            } else {
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean Student_list_check() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getStudentListURL, params);
        boolean student_available = false;

        try {
            JSONObject obj = new JSONObject(responseString);
            String success = obj.getString("Success");

            if (success.equals("True")) {
                JSONArray student_arr = obj.getJSONArray("StudentList");
                if (student_arr.length() > 0) {
                    student_available = true;
                } else {
                    student_available = false;
                }
            } else {
                student_available = false;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
        }

        return student_available;
    }
    public boolean Legal_DOC_chk_method() {

        boolean success_bul = false;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
//        params.put("password",AppConfiguration.LoginPass);

        String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.legal_doc_URL, params);

        JSONObject obj;
        String success = "";

        try {
            obj = new JSONObject(responseString);
            success = obj.getString("Success");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if (success.equals("True")) {
            success_bul = true;
            isLegalDoc = true;
        } else {
            success_bul = false;
            isLegalDoc = false;
        }
        return success_bul;
    }

    public void registerChildProcess() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("lessonlist", "" + AppConfiguration.lessionTypes);
        param.put("Description", "" + AppConfiguration.Description);
        param.put("ChildStrongWilledValue", AppConfiguration.ChildStrongWilledValue);
        param.put("ChildSensitiveValue", "" + AppConfiguration.ChildSensitiveValue);
        param.put("ChildOutGoingValue", "" + AppConfiguration.ChildOutgoingValue);
        param.put("ChildStrongWilled", "" + AppConfiguration.ChildStrongWilled);
        param.put("ChildSensitive", "" + AppConfiguration.ChildSensitive);
        param.put("ChildOutGoing", "" + AppConfiguration.ChildOutgoing);
        param.put("chk1", "" + AppConfiguration.childApplyCheck1);
        param.put("chk2", "" + AppConfiguration.childApplyCheck2);
        param.put("chk3", "" + AppConfiguration.childApplyCheck3);
        param.put("Swimgoals", "" + AppConfiguration.Swimgoals);
        param.put("levels", "" + AppConfiguration.levelTypes);
        param.put("FName", "" + AppConfiguration.studentFirstname);
        param.put("LName", "" + AppConfiguration.studentLastname);
        param.put("Dob", "" + AppConfiguration.studentDOB);
        param.put("Age", "" + AppConfiguration.studentAge);
        param.put("Gender", AppConfiguration.studentGender);
        param.put("InstructorNature", AppConfiguration.instructorNatureType);
        param.put("InstructorGender", AppConfiguration.instructorGender);
        param.put("strYesNo1", "" + AppConfiguration.strYesNo1);
        param.put("strYesNo2", "" + AppConfiguration.strYesNo2);
        param.put("strallergiesmedical", AppConfiguration.strallergiesmedical);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.registerChild, param);

        readAndParseJSON(responseString);
    }

    String successRegister;

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successRegister = reader.getString("Success");
            if (successRegister.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    StringBuilder builder;

    public void collectValues() {
        builder = new StringBuilder();

        for (int i = 0; i < llCheckbox.getChildCount(); i++) {
            View nextChild = llCheckbox.getChildAt(i);

            if (nextChild instanceof CheckBox) {
                CheckBox check = (CheckBox) nextChild;
                if (check.isChecked()) {
                    builder.append(check.getId() + ",");
                }
            }
        }
        AppConfiguration.lessionTypes = builder.toString();
    }
}
