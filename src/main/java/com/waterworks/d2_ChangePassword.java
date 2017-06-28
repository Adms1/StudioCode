/**
 *
 */
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
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

/**
 * @author Harsh Adms
 */
public class d2_ChangePassword extends Activity {

    TextView err_1, err_2, err_3;
    Button relMenu;
    RelativeLayout currPassLay, newPassLay, newPassCnfrmLay;
    EditText newPassCnfrm, newPass, currPass;
    CardView btn_update;
    String successChangePass, message;
    String token, familyID;
    Boolean isInternetPresent = false;
    Context mContext = this;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_change_pass);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(d2_ChangePassword.this);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        init();
    }

    private void init() {
        // TODO Auto-generated method stub

        err_1 = (TextView) findViewById(R.id.err_1);
        err_2 = (TextView) findViewById(R.id.err_2);
        err_3 = (TextView) findViewById(R.id.err_3);

        newPassCnfrmLay = (RelativeLayout) findViewById(R.id.newPassCnfrmLay);
        currPassLay = (RelativeLayout) findViewById(R.id.currPassLay);
        newPassLay = (RelativeLayout) findViewById(R.id.newPassLay);

        newPassCnfrm = (EditText) findViewById(R.id.newPassCnfrm);
        newPass = (EditText) findViewById(R.id.newPass);
        currPass = (EditText) findViewById(R.id.currPass);

        relMenu = (Button) findViewById(R.id.relMenu);
        btn_update = (CardView) findViewById(R.id.btn_update);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(d2_ChangePassword.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        btn_update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (currPass.getText().toString().trim().equals(AppConfiguration.currentPass)) {
                    err_1.setVisibility(View.GONE);
                    currPassLay.setBackgroundResource(R.drawable.bordergray);

                    if (newPass.getText().toString().trim().length() > 0) {
                        err_2.setVisibility(View.GONE);
                        newPassLay.setBackgroundResource(R.drawable.bordergray);
                        if (newPass.getText().toString().trim().equals(newPassCnfrm.getText().toString().trim())) {
                            err_3.setVisibility(View.GONE);
                            newPassCnfrmLay.setBackgroundResource(R.drawable.bordergray);
                            isInternetPresent = Utility.isNetworkConnected(d2_ChangePassword.this);
                            if (!isInternetPresent) {
                                onDetectNetworkState().show();
                            } else {
                                new ChangePassAsyncTask().execute();
                            }
                        } else {
                            err_3.setVisibility(View.VISIBLE);
                            newPassCnfrmLay.setBackgroundResource(R.drawable.error_border);
                        }
                    } else {
                        err_2.setVisibility(View.VISIBLE);
                        newPassLay.setBackgroundResource(R.drawable.error_border);
                    }

                } else {
                    err_1.setVisibility(View.VISIBLE);
                    currPassLay.setBackgroundResource(R.drawable.error_border);
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
        param.put("OldPassword", AppConfiguration.currentPass);
        param.put("NewPassword", newPass.getText().toString().trim());

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
            pd = new ProgressDialog(d2_ChangePassword.this);
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
                AppConfiguration.currentPass = newPass.getText().toString();
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
