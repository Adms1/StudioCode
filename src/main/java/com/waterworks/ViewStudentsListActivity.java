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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.waterworks.manageProfile.EditStudent;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewStudentsListActivity extends Activity {

    String TAG = "ViewStudentList";
    ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
    ListView list;
    TextView txtNoRecord;
    String successGetStudentList;

    String token, familyID;
    LinearLayout ll_ProgressReport, ll_ViewCertificate, ll_RibbonCount;
    Context mContext = this;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_studentlist_activity);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                Intent i = new Intent(ViewStudentsListActivity.this, EditStudent.class);
                startActivity(i);
            }
        });


        // fetching header view
        View headerLayout = findViewById(R.id.header);
        RippleView ripple = (RippleView) headerLayout.findViewById(R.id.ripple);

        ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                finish();
            }
        });

        TextView txt_1 = (TextView) headerLayout.findViewById(R.id.txt_1);
        TextView txt_2 = (TextView) headerLayout.findViewById(R.id.txt_2);
        TextView txt_3 = (TextView) headerLayout.findViewById(R.id.txt_3);
        TextView page_title = (TextView) headerLayout.findViewById(R.id.page_title);

        ll_ProgressReport = (LinearLayout) headerLayout.findViewById(R.id.scdl_lsn);
        ll_ViewCertificate = (LinearLayout) headerLayout.findViewById(R.id.scdl_mkp);
        ll_RibbonCount = (LinearLayout) headerLayout.findViewById(R.id.waitlist);


        txt_1.setText("Progress Report");
        txt_2.setText("View Certificate");
        txt_3.setText("Ribbon Count");
        page_title.setText("View Students");

        ll_ProgressReport.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewStudentsListActivity.this, ProgressReportActivity.class);
                startActivity(i);
            }
        });

        ll_ViewCertificate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ViewStudentsListActivity.this, ViewCertificateActivity.class);
                startActivity(i);
            }
        });

        ll_RibbonCount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewStudentsListActivity.this, RibbenCountActivity.class);
                startActivity(i);
            }
        });
        isInternetPresent = Utility.isNetworkConnected(ViewStudentsListActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new GetStudentListAsyncTask().execute();
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

    class GetStudentListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ViewStudentsListActivity.this);
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
                txtNoRecord.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            } else {
                txtNoRecord.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }

            if (successGetStudentList.toString().equals("True")) {

                CustomList adapter = new CustomList(ViewStudentsListActivity.this, studentList);
                list.setAdapter(adapter);


            } else {
            }
        }
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final ArrayList<HashMap<String, String>> data;

        public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_single);
            this.context = context;
            this.data = list;
        }

        @Override
        public int getCount() {
            return data.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_single, null, true);

            TextView txtStudentName = (TextView) rowView.findViewById(R.id.txtStudentName);
            txtStudentName.setText(data.get(position).get("ChildrenName"));

            TextView txtStudenDOBValue = (TextView) rowView.findViewById(R.id.txtStudenDOBValue);
            txtStudenDOBValue.setText(data.get(position).get("DateOfBirth"));


            TextView txtStudenAgeValue = (TextView) rowView.findViewById(R.id.txtStudenAgeValue);
            txtStudenAgeValue.setText(data.get(position).get("Age"));

            TextView txtLessionTypes = (TextView) rowView.findViewById(R.id.txtLessionTypes);
            txtLessionTypes.setText(data.get(position).get("ClassType"));

            return rowView;
        }
    }

}
