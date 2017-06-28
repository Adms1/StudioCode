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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SwimcompititionRegisterStep2Activity extends Activity {
    TextView txtCompititionVal;
    String token, familyID, eventdates, sitename, DateValue, time, starttime;
    String TAG = "Swimcompitition2New";
    String successLoadChildList;
    Boolean isInternetPresent = false;
    private int count;
    //    ListView list;
    LinearLayout llListData;
    CheckBox chkAgree;
    String successSwimCompittionCheck1;
    String msg1_Hours, msg2_meet, Msg3_Meet;
    boolean isSelectedAgreement = false;
    private boolean[] thumbnailsselection;
    CardView _continue;
    public static String MeetDate_Display, SiteName;
    String messageNormal;
    Context mContext = this;
    ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition2_new);
// getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        txtCompititionVal = (TextView) findViewById(R.id.txtCompititionVal);
        isInternetPresent = Utility.isNetworkConnected(SwimcompititionRegisterStep2Activity.this);
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        Intent intent = getIntent();
        if (null != intent) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue = intent.getStringExtra("DateTime");
            time = intent.getStringExtra("time");
            MeetDate_Display = intent.getStringExtra("MeetDate_Display");
            SiteName = intent.getStringExtra("SiteName");
            starttime = intent.getStringExtra("meetStartTime");

            txtCompititionVal.setText(MeetDate_Display);
        }
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new LoadChildListAsyncTask().execute();
            childList.clear();
            View view = findViewById(R.id.layout_top);
            TextView title = (TextView) view.findViewById(R.id.action_title);
//        title.setText("Step 1:Select Participants");
            title.setText("Participants");

            ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
            ib_menusad.setBackgroundResource(R.drawable.back_arrow);
            Button btnHome = (Button) findViewById(R.id.btnHome);
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCheckin = new Intent(SwimcompititionRegisterStep2Activity.this, DashBoardActivity.class);
                    intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentCheckin);
                    finish();
                }
            });
            Button relMenu = (Button) view.findViewById(R.id.relMenu);
            relMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            _continue = (CardView) findViewById(R.id.btn_swim_camps_regi1_students);
            _continue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final ArrayList<String> studentID = new ArrayList<String>();
                    final ArrayList<String> studentNAME = new ArrayList<String>();
                    studentID.clear();
                    studentNAME.clear();
                    boolean noSelect = false;
                    for (int i = 0; i < thumbnailsselection.length; i++) {
                        if (thumbnailsselection[i] == true) {
                            noSelect = true;
                            studentID.add(childList.get(i).get("StudentID").toString().trim());
                            studentNAME.add(childList.get(i).get("StudentName").toString().trim());
                        }
                    }
                    AppConfiguration.totalSelectedStudent = studentNAME.size();
                    if (!noSelect) {
                        Toast.makeText(SwimcompititionRegisterStep2Activity.this, "Please select at lease one student.", Toast.LENGTH_SHORT).show();
                    } else {
                        AppConfiguration.swimComptitionStudentID = studentID
                                .toString().replaceAll("\\[", "")
                                .replaceAll("\\]", "").trim();
                        AppConfiguration.swimComptitionStudentName = studentNAME
                                .toString().replaceAll("\\[", "")
                                .replaceAll("\\]", "").trim();

                        StringBuffer tempString = new StringBuffer();
                        for (int i = 0; i < studentID.size(); i++) {
                            tempString.append(studentID.get(i) + "|" + studentNAME.get(i));
                            if (i < studentID.size() - 1) {
                                tempString.append(",");
                            }
                        }

                        AppConfiguration.swimComptitionStudentIDName = tempString.toString();
//                    if (chkAgree.isChecked())
//                        isSelectedAgreement = true;
//                    else
//                        isSelectedAgreement = false;
//                    if(isSelectedAgreement){
                        try {
                            if (DateValue.equalsIgnoreCase("") || DateValue.equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(), "Please select swim meet date", Toast.LENGTH_LONG).show();
                            } else {
                                new SwimMeetDateCheckAsyncTask().execute();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    27-12-2016 megha
//                    }else{
//                    new AlertDialog.Builder(SwimcompititionRegisterStep2Activity.this)
//                            .setTitle("Alert")
//                            .setMessage("Please agree to the conditions.")
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //do nothing
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
                    }
                }

            });
            llListData = (LinearLayout) findViewById(R.id.llListData);
        }
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        isInternetPresent = Utility
                .isNetworkConnected(SwimcompititionRegisterStep2Activity.this);
        AppConfiguration.SelectedEventDataStep2="";
        AppConfiguration.selectedStudent1.clear();
    }

    public void readAndParseJSONSwimMeetDateCheck(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successSwimCompittionCheck1 = reader.getString("Success");
            if (successSwimCompittionCheck1.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SwimMeetDateStep1check");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    msg1_Hours = jsonChildNode.getString("Msg1_Hours");
                    msg2_meet = jsonChildNode.getString("Msg2_Meet");
                    Msg3_Meet = jsonChildNode.getString("Msg3_Meet");
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("SwimMeetDateStep1check");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageNormal = jsonChildNode.getString("Msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void swimMeetDateCheck() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("MeetdatetimeValue", DateValue);

        String responseString = WebServicesCall.RunScript(AppConfiguration.swimCompititionMeetDateCheckURL, param);
        readAndParseJSONSwimMeetDateCheck(responseString);
    }
    class SwimMeetDateCheckAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimcompititionRegisterStep2Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            swimMeetDateCheck();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successSwimCompittionCheck1.equals("True")) {
                if (msg1_Hours.equals("null") && msg2_meet.equals("null")
                        && Msg3_Meet.equals("null")) {
                    Intent i = new Intent(SwimcompititionRegisterStep2Activity.this, SwimcompititionRegisterStep3Activity.class);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                } else {
                    if (!msg1_Hours.equals("null")) {
                        Toast.makeText(getApplicationContext(),
                                "" + msg1_Hours, Toast.LENGTH_LONG).show();
                    } else if (!msg2_meet.equals("null")) {
                        Toast.makeText(getApplicationContext(), "" + msg2_meet,
                                Toast.LENGTH_LONG).show();
                    } else if ((!Msg3_Meet.equals("null"))) {
                        Toast.makeText(getApplicationContext(), "" + Msg3_Meet,
                                Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "" + messageNormal,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    @SuppressWarnings("deprecation")
    public AlertDialog onDetectNetworkState() {
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
    public void loadingChildList() {
        String [] Meetdate = DateValue.split("\\s");
        String Meetdate1= Meetdate[0];
        Log.d("mm/dd/yyyy",Meetdate1);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("FamilyID", familyID);
        param.put("meetdate",Meetdate1);

        String responseString = WebServicesCall.RunScript(AppConfiguration.swimCampRegister1_new, param);
        readAndParseJSONChildList(responseString);
    }
    public void readAndParseJSONChildList(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                for (int i = 0; i < jsonMainNode.length(); i++) {

                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String StudentID = jsonChildNode.getString("StudentID").trim();
                    String StudentName = jsonChildNode.getString("StudentName").trim();

                    hashmap.put("StudentID", StudentID);
                    hashmap.put("StudentName", StudentName);
                    childList.add(hashmap);
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class LoadChildListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimcompititionRegisterStep2Activity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
            childList.clear();
        }
        @Override
        protected Void doInBackground(Void... params) {
            loadingChildList();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successLoadChildList.toString().equals("True")) {
                count = childList.size();
                thumbnailsselection = new boolean[count];
                loadDataList(childList);
            } else {
            }
        }
    }
    public void loadDataList(ArrayList<HashMap<String, String>> list) {
        TextView txtStudentName;
        CheckBox checkbox;
        int id;
        try {
            for (int i = 0; i < list.size(); i++) {
                View convertView = LayoutInflater.from(SwimcompititionRegisterStep2Activity.this).inflate(R.layout.list_row_swim_camp_register1, null);
                txtStudentName = (TextView) convertView.findViewById(R.id.txtStudentName);
                checkbox = (CheckBox) convertView.findViewById(R.id.chb_students);
                checkbox.setId(i);
                checkbox.setButtonDrawable(R.drawable.custom_check_orange);
                txtStudentName.setId(i);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        CheckBox cb = (CheckBox) v;
                        int id = cb.getId();
                        if (thumbnailsselection[id]) {
                            cb.setChecked(false);
                            cb.setButtonDrawable(R.drawable.custom_check_orange);
                            thumbnailsselection[id] = false;
                        } else {
                            cb.setChecked(true);
                            thumbnailsselection[id] = true;
                            cb.setButtonDrawable(R.drawable.custom_check_orange);
                        }
                    }
                });
                checkbox.setChecked(thumbnailsselection[i]);
                id = i;
                txtStudentName.setText(childList.get(i).get("StudentName"));
                Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                llListData.startAnimation(animSlidInRight);
                llListData.addView(convertView);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            CheckBox cb = (CheckBox) v;
            int id = cb.getId();
            if (thumbnailsselection[id]) {
                cb.setChecked(false);
                thumbnailsselection[id] = false;
            } else {
                cb.setChecked(true);
                thumbnailsselection[id] = true;
            }
        }
    };
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
