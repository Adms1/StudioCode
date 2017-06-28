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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ChangePasswordActivity extends Activity {
    String TAG = "ChangePassword";
    Button btnSubmit;
    EditText edtOldPass, edtNewPass, edtConfirmNewPass;

    String oldPass, newPass;
    String successChangePass, message;
    String token, familyID;
    Context mContext = this;
    Boolean isInternetPresent = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(ChangePasswordActivity.this);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edtOldPass = (EditText) findViewById(R.id.edtOldPass);
        edtNewPass = (EditText) findViewById(R.id.edtNewPass);
        edtConfirmNewPass = (EditText) findViewById(R.id.edtConfirmNewPass);

        // fetching header view
        View headerLayout = findViewById(R.id.header);
        ImageView imgBack = (ImageView) headerLayout.findViewById(R.id.ib_back);
        imgBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                oldPass = edtOldPass.getText().toString();
                newPass = edtNewPass.getText().toString();
                String confirmPass = edtConfirmNewPass.getText().toString();

                if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, R.string.empty_fields, Toast.LENGTH_LONG).show();
                } else {
                    if (!newPass.equals(confirmPass)) {
                        Toast.makeText(ChangePasswordActivity.this, R.string.not_match_pass, Toast.LENGTH_LONG).show();
                    } else {
                        isInternetPresent = Utility.isNetworkConnected(ChangePasswordActivity.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new ChangePassAsyncTask().execute();
                        }
                    }
                }


            }

        });

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

    public void changePasswordProcess() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("OldPassword", oldPass);
        param.put("NewPassword", newPass);

        String responseString = WebServicesCall.RunScript(AppConfiguration.changePasswordURL, param);
        readAndParseJSON(responseString);

    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successChangePass = reader.getString("Success");
            if (successChangePass.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("ChangePass");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("ChangePass");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    class ChangePassAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ChangePasswordActivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            changePasswordProcess();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successChangePass.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
            }


        }
    }

}
