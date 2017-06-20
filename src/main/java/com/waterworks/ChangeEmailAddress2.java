/**
 *
 */
package com.waterworks;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.manageProfile.d2_MyProfile;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

/**
 * @author Harsh Adms
 */
public class ChangeEmailAddress2 extends Activity {

	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

    RelativeLayout newEmailLay, newEmailLayCnfrm;
    TextView currentEmail, err_1, err_2;
    CardView update;
    EditText newEmail, cnfrmEmail;
    Button relMenu;
    Context mContext = this;
    String token, familyID, NewEmailID;
    Button btnHome;
    Boolean isInternetPresent = false;
    String nemail, confirmEmail;
    String successIsEmailExists, messageEmailExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_change_email2);

        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        init();
    }
    private void init() {
        // TODO Auto-generated method stub
        newEmailLay = (RelativeLayout) findViewById(R.id.newEmailLay);
        newEmailLayCnfrm = (RelativeLayout) findViewById(R.id.newEmailLayCnfrm);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ChangeEmailAddress2.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        currentEmail = (TextView) findViewById(R.id.currentEmail);
        update = (CardView) findViewById(R.id.btn_update);
        err_1 = (TextView) findViewById(R.id.err_1);
        err_2 = (TextView) findViewById(R.id.err_2);
        newEmail = (EditText) findViewById(R.id.newEmail);
        cnfrmEmail = (EditText) findViewById(R.id.cnfrmnewEmail);
        relMenu = (Button) findViewById(R.id.relMenu);
        currentEmail.setText(AppConfiguration.EmailAdd);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
           //09-05-2017   megha
        newEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nemail = newEmail.getText().toString();
                if (hasFocus) {
                } else {
                    if (!AppConfiguration.isValidEmail(nemail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeEmailAddress2.this);
                        builder.setCancelable(true);
                        builder.setTitle("WaterWorks");
                        builder.setCancelable(false);
                        builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
                        builder.setMessage(getResources().getString(R.string.invalid_email));
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        newEmail.requestFocus();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        isInternetPresent = Utility.isNetworkConnected(ChangeEmailAddress2.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new isEmailExistsAsyncTask().execute();
                        }
                    }
                }
            }
        });
        cnfrmEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newEmailLayCnfrm.setBackgroundResource(R.drawable.bordergray);
                err_2.setVisibility(View.GONE);
            }
        });
//================
        update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (newEmail.getText().toString().trim().length() > 0) {
                    newEmailLay.setBackgroundResource(R.drawable.bordergray);
                    err_1.setVisibility(View.GONE);
                     if (newEmail.getText().toString().trim().equals(
                            cnfrmEmail.getText().toString().trim())) {
                        newEmailLayCnfrm.setBackgroundResource(R.drawable.bordergray);
                        err_2.setVisibility(View.GONE);

                        NewEmailID = newEmail.getText().toString().trim();
                        isInternetPresent = Utility.isNetworkConnected(ChangeEmailAddress2.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new ChangeEmail().execute();
                        }
                        Toast.makeText(getApplicationContext(), "Email Address Updated", Toast.LENGTH_LONG).show();
                    } else {
                        newEmailLayCnfrm.setBackgroundResource(R.drawable.error_border);
                        err_2.setVisibility(View.VISIBLE);
                    }
                } else {
                    newEmailLay.setBackgroundResource(R.drawable.error_border);
                    err_1.setVisibility(View.VISIBLE);
                }
            }
        });
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
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


    /**
     * Remaining lessons detail Service
     *
     * @author Harsh Adms
     */
    String data_load = "False";
    private class ChangeEmail extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("NewEmailID", NewEmailID);

            String responseString = WebServicesCall.RunScript(
                    AppConfiguration.DOMAIN + AppConfiguration.UpdateFamilyEmail,param);
            try {
                data_load = "False";
                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equals("True")) {
                } else {}
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (data_load.toString().equals("True")) {
                d2_MyProfile.ChangedEmail = newEmail.getText().toString();
                finish();
            } else {
                finish();
            }
        }
    }

    //12-05-2017 megha newcode
    public void isEmailExistsChcek() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("EmailID", nemail);

        String responseString = WebServicesCall.RunScript(AppConfiguration.isEmailExistSURL, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successIsEmailExists = reader.getString("Success");
            if (successIsEmailExists.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("CheckEmail");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageEmailExists = jsonChildNode.getString("Msg");
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class isEmailExistsAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ChangeEmailAddress2.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            isEmailExistsChcek();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successIsEmailExists.toString().equals("True")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeEmailAddress2.this);
                builder.setCancelable(true);
                builder.setTitle("WaterWorks");
                builder.setCancelable(false);
                builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
                builder.setMessage(getResources().getString(R.string.existsEmail));
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                newEmail.setText("");
                                newEmail.requestFocus();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            } else {}
        }
    }
}
