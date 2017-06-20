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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.DashBoardActivity;

import com.waterworks.LegalDoc_Check_FamilyDoc;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Harsh Adms on 06/01/2016.
 */
public class ManageStudents extends Activity {

    LinearLayout students_lay;
    Context mContext = this;
    String token, familyID;

    ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
    String successGetStudentList;
    TextView txtNoRecord, register, currentstudentTitle;

    public static boolean updated = false;
    public static boolean edit = false;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managestudents);

        init();

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        isInternetPresent = Utility.isNetworkConnected(ManageStudents.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new GetStudentListAsyncTask().execute();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        if (updated) {
            updated = false;
            isInternetPresent = Utility.isNetworkConnected(ManageStudents.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new GetStudentListAsyncTask().execute();
            }
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

    public void init() {
        //Custom Header
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Students");

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//26-04-2017 megha
                if (AppConfiguration.LAFitnessResult.equalsIgnoreCase("true")) {
                    Student_Doc_Update();
                } else {
                    finish();
                }
//                ===========
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                27-04-2017 megha==========================
                if (AppConfiguration.LAFitnessResult.equalsIgnoreCase("true")) {
                    Student_Doc_Update();
                } else {
                    Intent intentCheckin = new Intent(ManageStudents.this, DashBoardActivity.class);
                    intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentCheckin);
                    finish();
                }
//                =====================================
            }
        });

        students_lay = (LinearLayout) findViewById(R.id.students_lay);
        txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
        register = (TextView) findViewById(R.id.register);
        currentstudentTitle = (TextView) findViewById(R.id.currentstudentTitle);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = Utility.isNetworkConnected(ManageStudents.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new checkStudentCountAsyncTask().execute();
                }
            }
        });
    }

    public void getStudentList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("FamilyID", familyID);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.getStudentListURL, param);

        readAndParseJSON(responseString);
    }


    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successGetStudentList = reader.getString("Success");
            if (successGetStudentList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("StudentList");
                studentList.clear();
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String SstudentId = jsonChildNode.getString("SstudentId").trim();
                    String ChildrenName = jsonChildNode.getString("ChildrenName").trim();
                    String DateOfBirth = jsonChildNode.getString("DateOfBirth").trim();
                    String Age = jsonChildNode.getString("Age").trim();
                    String ClassType = jsonChildNode.getString("ClassType").trim();



                    String SFirstName = jsonChildNode.getString("SFirstName").trim();
                    String SLastName = jsonChildNode.getString("SLastName").trim();
                    String WU_SGender = jsonChildNode.getString("WU_SGender").trim();
                    String SInstrGender = jsonChildNode.getString("SInstrGender").trim();
                    String SInstrNature = jsonChildNode.getString("SInstrNature").trim();
                    String SLevel = jsonChildNode.getString("SLevel").trim();
                    String SLevelID = jsonChildNode.getString("SLevelID").trim();
                    String Sreschedule = jsonChildNode.getString("Sreschedule").trim();
                    String SAllergy = jsonChildNode.getString("SAllergy").trim();
                    String SAllergyDesc = jsonChildNode.getString("SAllergyDesc").trim();
                    String Sactive = jsonChildNode.getString("Sactive").trim();

                    String Description = jsonChildNode.getString("Description").trim();
                    String ChildStrongWilledValue = jsonChildNode.getString("ChildStrongWilledValue").trim();
                    String ChildSensitiveValue = jsonChildNode.getString("ChildSensitiveValue").trim();
                    String ChildOutGoingValue = jsonChildNode.getString("ChildOutGoingValue").trim();
                    String competitiveswimming = jsonChildNode.getString("competitiveswimming").trim();
                    String distance = jsonChildNode.getString("distance").trim();
                    String techniqueandshorter = jsonChildNode.getString("techniqueandshorter").trim();

                    String Swimgoals = jsonChildNode.getString("Swimgoals").trim();


                    hashmap.put("SstudentId", SstudentId);
                    hashmap.put("ChildrenName", ChildrenName);
                    hashmap.put("DateOfBirth", DateOfBirth);
                    hashmap.put("Age", Age);
                    hashmap.put("ClassType", ClassType);

                    hashmap.put("SFirstName", SFirstName);
                    hashmap.put("SLastName", SLastName);
                    hashmap.put("WU_SGender", WU_SGender);
                    hashmap.put("SInstrGender", SInstrGender);
                    hashmap.put("SInstrNature", SInstrNature);
                    hashmap.put("SLevel", SLevel);
                    hashmap.put("SLevelID", SLevelID);

                    hashmap.put("Sreschedule", Sreschedule);
                    hashmap.put("SAllergyDesc", SAllergyDesc);
                    hashmap.put("SAllergy", SAllergy);
                    hashmap.put("Sactive", Sactive);

                    hashmap.put("Description", Description);
                    hashmap.put("ChildStrongWilledValue", ChildStrongWilledValue);
                    hashmap.put("ChildSensitiveValue", ChildSensitiveValue);
                    hashmap.put("ChildOutGoingValue", ChildOutGoingValue);
                    hashmap.put("competitiveswimming", competitiveswimming);
                    hashmap.put("distance", distance);
                    hashmap.put("techniqueandshorter", techniqueandshorter);
                    hashmap.put("Swimgoals", Swimgoals);

                    studentList.add(hashmap);


                }

            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //======================27-04-2017 megha===================
    public boolean Student_Doc_Update() {

        boolean student_available = false;

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.legal_doc_StudentDoc_Status, param);

        try {
            JSONObject obj = new JSONObject(responseString);
            String success = obj.getString("Success");

            if (success.equals("True")) {
                Intent i = new Intent(ManageStudents.this, LegalDoc_Check_FamilyDoc.class);
                i.putExtra("Token", token);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Something Wrong.", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
        }

        return student_available;
    }

    //==================================================
    class GetStudentListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            studentList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getStudentList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (studentList.size() > 0) {
                currentstudentTitle.setVisibility(View.VISIBLE);
                txtNoRecord.setVisibility(View.GONE);
            } else {
                currentstudentTitle.setVisibility(View.INVISIBLE);
                txtNoRecord.setVisibility(View.GONE);
            }

            if (successGetStudentList.toString().equals("True")) {
                students_lay.removeAllViews();
                System.out.println("Size : " + studentList.size());
                for (int i = 0; i < studentList.size(); i++) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View child_view = inflater.inflate(R.layout.custom_students, null);
                    TextView student_name = (TextView) child_view.findViewById(R.id.student_name);
                    student_name.setText(studentList.get(i).get("SFirstName") + " " + studentList.get(i).get("SLastName"));
                    final RelativeLayout student_number = (RelativeLayout) child_view.findViewById(R.id.student_number);
                    student_number.setTag(i);

                    student_number.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) student_number.getTag();
                            selectedStudents(position);
                        }
                    });
                    students_lay.addView(child_view, i);
                }

            } else {
            }
        }
    }

    public void selectedStudents(int position) {
        AppConfiguration.studentID = "" + studentList.get(position).get("SstudentId");
        AppConfiguration.studentFirstname = "" + studentList.get(position).get("SFirstName");
        AppConfiguration.studentLastname = "" + studentList.get(position).get("SLastName");
        AppConfiguration.studentDOB = "" + studentList.get(position).get("DateOfBirth");
        AppConfiguration.studentAge = "" + studentList.get(position).get("Age");
        AppConfiguration.studentGender = "" + studentList.get(position).get("WU_SGender");
        //Instructor
        AppConfiguration.instructorGender = "" + studentList.get(position).get("SInstrGender");
        AppConfiguration.instructorNatureType = "" + studentList.get(position).get("SInstrNature");
        AppConfiguration.classTypes = "" + studentList.get(position).get("ClassType");

        AppConfiguration.levelTypes = "" + studentList.get(position).get("SLevel");
        AppConfiguration.levelTypesID = "" + studentList.get(position).get("SLevelID");

        AppConfiguration.strYesNo2 = "" + studentList.get(position).get("SAllergy");
        AppConfiguration.strYesNo1 = "" + studentList.get(position).get("Sactive");
        AppConfiguration.strallergiesmedical = "" + studentList.get(position).get("SAllergyDesc");

        AppConfiguration.ChildStrongWilledValue = "" + studentList.get(position).get("ChildStrongWilledValue");
        AppConfiguration.ChildSensitiveValue = "" + studentList.get(position).get("ChildSensitiveValue");
        AppConfiguration.ChildOutgoingValue = "" + studentList.get(position).get("ChildOutGoingValue");

        AppConfiguration.childApplyCheck1 = false;
        AppConfiguration.childApplyCheck2 = false;
        AppConfiguration.childApplyCheck3 = false;

        if (studentList.get(position).get("distance").equals("")) {
            AppConfiguration.childApplyCheck2 = false;
        } else
            AppConfiguration.childApplyCheck2 = true;

        if (studentList.get(position).get("competitiveswimming").equals("")) {
            AppConfiguration.childApplyCheck1 = false;
        } else {
            AppConfiguration.childApplyCheck1 = true;
        }

        if (studentList.get(position).get("techniqueandshorter").equals("")) {
            AppConfiguration.childApplyCheck3 = false;
        } else {
            AppConfiguration.childApplyCheck3 = true;
        }


        AppConfiguration.Description = "" + studentList.get(position).get("Description");
        AppConfiguration.Swimgoals = "" + studentList.get(position).get("Swimgoals");

        Intent i = new Intent(mContext, EditStudent.class);

        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//        //    27-04-2017 megha =======================
//        if (AppConfiguration.LAFitnessResult.equalsIgnoreCase("true")) {
//            Student_Doc_Update();
//        } else {
//
//        }
    }


    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        //    27-04-2017 megha =======================
        if (AppConfiguration.LAFitnessResult.equalsIgnoreCase("true")) {
            Student_Doc_Update();
        } else {

        }
    }

    //CheckStudentCount
    String Message;
    String successMyAccount, successStudentCount;

    class checkStudentCountAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(mContext);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            studentCountInfo();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successStudentCount.equalsIgnoreCase("True")) {
                Intent i = new Intent(mContext, AddStudent.class);
                i.putExtra("fromWhere", "register");
//                Intent i = new Intent(mContext, RegisterChildScreen1Activity.class);
                startActivity(i);
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
                alertDialogBuilder.setMessage(Html.fromHtml("" + Message));
                // set positive button: Yes message
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                // show alert
                alertDialog.show();
            }
        }
    }

    public void studentCountInfo() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
        params.put("FamilyID", familyID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getChildCountURL, params);
        readAndParseJSONStudntCount(responseString);
    }

    public void readAndParseJSONStudntCount(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successStudentCount = reader.getString("Success");
            if (successStudentCount.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
                for (int i = 0; i < jsonMainNode.length(); i++) {

                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    Message = jsonChildNode.getString("Msg").trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
