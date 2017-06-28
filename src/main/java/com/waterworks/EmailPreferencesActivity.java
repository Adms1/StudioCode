package com.waterworks;

import java.util.ArrayList;
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

public class EmailPreferencesActivity extends Activity {
    String TAG = "EmailPreferences";
    ArrayList<String> emailPreferencesList = new ArrayList<String>();
    ArrayList<HashMap<String, String>> emailList = new ArrayList<HashMap<String, String>>();

    Button btnSubmit;
    EditText edtOldPass, edtNewPass, edtConfirmNewPass;

    String oldPass, newPass;
    String successGetEmailPreferences;
    String successSaveEmailPreferences;
    CheckBox checkBox;
    StringBuilder builder;
    String message;
    ImageView imgBack;
    LinearLayout llCheckbox;
    Context mContext = this;
    String token, familyID;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_preferences);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edtOldPass = (EditText) findViewById(R.id.edtOldPass);
        edtNewPass = (EditText) findViewById(R.id.edtNewPass);
        edtConfirmNewPass = (EditText) findViewById(R.id.edtConfirmNewPass);

        llCheckbox = (LinearLayout) findViewById(R.id.llCheckbox);


        // fetching header view

        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Email Preferences");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new OnClickListener() {

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
                isInternetPresent = Utility.isNetworkConnected(EmailPreferencesActivity.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new SaveEmailPreferencesAsyncTask().execute();
                }
            }
        });
        isInternetPresent = Utility.isNetworkConnected(EmailPreferencesActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            //getting already set email preferences
            new GetEmailPreferencesAsyncTask().execute();
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

    public void getEmailPreferences() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getEmailPreferences, param);
        readAndParseJSON(responseString);

    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successGetEmailPreferences = reader.getString("Success");
            if (successGetEmailPreferences.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String TypeText = jsonChildNode.getString("TypeText").trim();
                    String TypeValue = jsonChildNode.getString("TypeValue").trim();
                    String Description = jsonChildNode.getString("Description").trim();
                    String TypeCheck = jsonChildNode.getString("TypeCheck").trim();
                    if (!TypeCheck.equals(""))
                        hashmap.put("TypeCheck", TypeCheck);
                    else
                        hashmap.put("TypeCheck", "~");

                    hashmap.put("TypeText", TypeText);
                    hashmap.put("TypeValue", TypeValue);
                    hashmap.put("Description", Description);

                    emailList.add(hashmap);


                }

            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    class GetEmailPreferencesAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(EmailPreferencesActivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            emailList.clear();
            emailPreferencesList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getEmailPreferences();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successGetEmailPreferences.toString().equals("True")) {

                for (int i = 0; i < emailList.size(); i++) {
                    checkBox = new CheckBox(EmailPreferencesActivity.this);
                    checkBox.setId(Integer.parseInt(emailList.get(i).get("TypeValue")));
                    checkBox.setText(emailList.get(i).get("TypeText"));
                    checkBox.setTextColor(getResources().getColor(R.color.black));
                    String emailPrefChecked = emailList.get(i).get("TypeCheck");
                    checkBox.setPadding(5, 5, 5, 5);
                    checkBox.setButtonDrawable(R.drawable.customdrawablecheckbox);

                    if (emailPrefChecked.equalsIgnoreCase("checked"))
                        checkBox.setChecked(true);
                    else
                        checkBox.setChecked(false);

                    llCheckbox.addView(checkBox);

                }


            } else {
                Toast.makeText(getApplicationContext(), "Email preferences not getting.", Toast.LENGTH_LONG).show();
            }
        }
    }


// ======================================= Save Email Preferences ================================


    public void SaveEmailPreferences() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("TypeValue", builder.toString());

        String responseString = WebServicesCall.RunScript(AppConfiguration.saveEmailPreferences, param);
        readAndParseJSONSave(responseString);

    }

    public void readAndParseJSONSave(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successSaveEmailPreferences = reader.getString("Success");
            if (successSaveEmailPreferences.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    message = jsonChildNode.getString("Msg");
                }

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    message = jsonChildNode.getString("Msg");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SaveEmailPreferencesAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(EmailPreferencesActivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SaveEmailPreferences();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successSaveEmailPreferences.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
