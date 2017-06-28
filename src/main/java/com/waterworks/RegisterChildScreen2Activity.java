package com.waterworks;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class RegisterChildScreen2Activity extends Activity {

    ImageView imgNext;
    String successSaveEmailPreferences;
    CheckBox checkBox;
    ImageView imgBack;
    StringBuilder builder;
    String message;
    String successAgeCalculation;

    EditText edtStudentBdate;
    LinearLayout llCheckbox;
    Button btnLevelCalculator;
    public static EditText edtLevel;
    String token, familyID;
    Boolean isInternetPresent = false;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerchild_screen2);

        AppConfiguration.levelTypes = "";

        SharedPreferences prefs = AppConfiguration
                .getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        System.out.println("" + token);
        imgNext = (ImageView) findViewById(R.id.imgNext);

        llCheckbox = (LinearLayout) findViewById(R.id.llCheckbox);
        edtStudentBdate = (EditText) findViewById(R.id.edtStudentBdate);
        edtLevel = (EditText) findViewById(R.id.spinner1Level);
        edtLevel.setText("");
        btnLevelCalculator = (Button) findViewById(R.id.btnLevelCalculator);

        // fetching header view

        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Register a Child");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfiguration.levelTypes = "";
                edtLevel.setText("");
                finish();
            }
        });

        btnLevelCalculator.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AppConfiguration.lessionTypeList_New.clear();
                if (llCheckbox.getChildCount() > 0) {
                    llCheckbox.removeAllViews();
                }
                Intent i = new Intent(RegisterChildScreen2Activity.this, LevelCalculatorActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        imgNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

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

                Log.d("data", builder.toString());

                AppConfiguration.levelTypes = edtLevel.getText().toString();
                if (AppConfiguration.levelTypes.equals("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.levelCal), Toast.LENGTH_LONG).show();
                } else {
                    AppConfiguration.lessionTypes = builder.toString();

                    if (!AppConfiguration.lessionTypes.equals("")) {
                        Intent i = new Intent(RegisterChildScreen2Activity.this, RegisterChildScreen3Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.selectLessonType), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        edtLevel.setText(AppConfiguration.levelTypes);
        if (edtLevel.getText().toString().trim().length() > 0) {
            isInternetPresent = Utility.isNetworkConnected(RegisterChildScreen2Activity.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new getLessByAgeLevel().execute();
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

    @SuppressWarnings("deprecation")
    public void preparingLessionTypeList() {
        llCheckbox.removeAllViews();

        for (int i = 0; i < AppConfiguration.lessionTypeList_New.size(); i++) {
            checkBox = new CheckBox(RegisterChildScreen2Activity.this);
            checkBox.setId(Integer.parseInt(AppConfiguration.lessionTypeList_New.get(i).get("lessonid")));
            checkBox.setText(AppConfiguration.lessionTypeList_New.get(i).get("lessonname"));
            checkBox.setPadding(5, 5, 5, 5);
            checkBox.setButtonDrawable(R.drawable.customdrawablecheckbox);
            checkBox.setTextColor(getResources().getColor(R.color.black));
            llCheckbox.addView(checkBox);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppConfiguration.levelTypes = "";
        edtLevel.setText("");
        finish();
    }


    public class getLessByAgeLevel extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(RegisterChildScreen2Activity.this);
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
                edtLevel.getText().toString());

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_Lession_by_LevelAge, param);
        readAndParseJSON(responseString);

    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successAgeCalculation = reader.getString("Success");
            if (successAgeCalculation.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                AppConfiguration.lessionTypeList_New.clear();

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String lessonid = jsonChildNode.getString("LessionID").trim();
                    String lessonname = jsonChildNode.getString("LessionName").trim();
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

}
