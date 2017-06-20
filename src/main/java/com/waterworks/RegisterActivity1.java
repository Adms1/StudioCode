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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class RegisterActivity1 extends Activity {

    CheckBox chk_reg1;
    LinearLayout ll_secondary_parent;
    ImageView imgNext;

    EditText edtEmailAddress, edtConfirmEmailAddress, edtPassword, edtConfirmPassword, edtPrimaryFirstname, edtPrimaryLastname, edtSecondaryFirstName, edtSecondaryLastname;
    boolean checkSingleParent = false;
    RelativeLayout rel_back;
    String password, confirmpass;
    String email, confirmEmail;
    String successIsEmailExists;
    String messageEmailExists;
    String DOMAIN;
    Boolean isInternetPresent = false;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1_activity);
        ll_secondary_parent = (LinearLayout) findViewById(R.id.ll_secondary_parent);
        imgNext = (ImageView) findViewById(R.id.imgNext);
        chk_reg1 = (CheckBox) findViewById(R.id.chk_reg1);
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        edtConfirmEmailAddress = (EditText) findViewById(R.id.edtConfirmEmailAddress);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        edtPrimaryFirstname = (EditText) findViewById(R.id.edtPrimaryFirstname);
        edtPrimaryLastname = (EditText) findViewById(R.id.edtPrimaryLastname);
        edtSecondaryFirstName = (EditText) findViewById(R.id.edtSecondaryFirstName);
        edtSecondaryLastname = (EditText) findViewById(R.id.edtSecondaryLastname);
        DOMAIN = getIntent().getStringExtra("DOMAIN");
        chk_reg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    View view = RegisterActivity1.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    ll_secondary_parent.setVisibility(View.GONE);
                    checkSingleParent = true;
                } else {
                    ll_secondary_parent.setVisibility(View.VISIBLE);
                    checkSingleParent = false;
                }
            }
        });

        imgNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                email = edtEmailAddress.getText().toString();
                confirmEmail = edtConfirmEmailAddress.getText().toString();
                password = edtPassword.getText().toString();
                confirmpass = edtConfirmPassword.getText().toString();
                String pFirstname = edtPrimaryFirstname.getText().toString();
                String pLastname = edtPrimaryLastname.getText().toString();
                String sFirstname = edtSecondaryFirstName.getText().toString();
                String sLastname = edtSecondaryLastname.getText().toString();

                AppConfiguration.EmailAdd = email;
                AppConfiguration.ConfirmEmail = confirmEmail;
                AppConfiguration.Password = password;
                AppConfiguration.ConfrmPassword = confirmpass;
                AppConfiguration.PFirstName = pFirstname;
                AppConfiguration.PLastName = pLastname;
                AppConfiguration.SFirstName = sFirstname;
                AppConfiguration.SLastName = sLastname;


                if (chk_reg1.isChecked() == true) {
                    if (email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() || confirmpass.isEmpty() || pFirstname.isEmpty() || pLastname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                    } else {
                        if (!AppConfiguration.isValidEmail(email) && !AppConfiguration.isValidEmail(confirmEmail)) {
                            Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                        } else if (!email.equalsIgnoreCase(confirmEmail)) {
                            Toast.makeText(getApplicationContext(), R.string.not_match_email, Toast.LENGTH_LONG).show();
                        }
                        //						else if(!password.equals(confirmpass))
                        //						{
                        //							Toast.makeText(getApplicationContext(), R.string.not_match_pass, Toast.LENGTH_LONG).show();
                        //						}
                        else {
                            Intent i = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("DOMAIN", DOMAIN);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                } else {
                    if (email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() || confirmpass.isEmpty() || pFirstname.isEmpty() || pLastname.isEmpty() || sFirstname.isEmpty() || sLastname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                    } else {
                        if (!AppConfiguration.isValidEmail(email) && !AppConfiguration.isValidEmail(confirmEmail)) {
                            Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                        } else if (!email.equalsIgnoreCase(confirmEmail)) {
                            Toast.makeText(getApplicationContext(), R.string.not_match_email, Toast.LENGTH_LONG).show();
                        }
                        //						else if(!password.equals(confirmpass))
                        //						{
                        //							Toast.makeText(getApplicationContext(), R.string.not_match_pass, Toast.LENGTH_LONG).show();
                        //						}
                        else {
                            Intent i = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                            startActivityForResult(i, RESULT_OK);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                }
            }
        });


        // fetching header view
        Button relMenu = (Button) findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        edtPrimaryFirstname.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                password = edtPassword.getText().toString();
                confirmpass = edtConfirmPassword.getText().toString();

                if (hasFocus) {
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();

                    if (!password.equals(confirmpass)) {
                        //Toast.makeText(getApplicationContext(), R.string.not_match_pass, Toast.LENGTH_LONG).show();
                        //edtConfirmPassword.setError(getResources().getString(R.string.not_match_pass));

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity1.this);
                        builder.setCancelable(true);
                        builder.setTitle("WaterWorks");
                        builder.setCancelable(false);
                        builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
                        builder.setMessage(getResources().getString(R.string.not_match_pass));
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        edtConfirmPassword.setText("");

                                        edtConfirmPassword.requestFocus();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(edtConfirmPassword, InputMethodManager.SHOW_IMPLICIT);
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    //Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();


                }
            }
        });

        edtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                email = edtEmailAddress.getText().toString();
                confirmEmail = edtConfirmEmailAddress.getText().toString();

                if (hasFocus) {
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                    if (!email.equalsIgnoreCase(confirmEmail)) {
                        //Toast.makeText(getApplicationContext(), R.string.not_match_pass, Toast.LENGTH_LONG).show();
                        //edtConfirmEmailAddress.setError(""+getResources().getString(R.string.not_match_email));

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity1.this);
                        builder.setCancelable(true);
                        builder.setTitle("WaterWorks");
                        builder.setCancelable(false);
                        builder.setIcon(R.drawable.alerticon);
                        builder.setMessage(getResources().getString(R.string.not_match_email));
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    //Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();


                }
            }
        });


        edtEmailAddress.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                email = edtEmailAddress.getText().toString();

                if (hasFocus) {
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    if (!AppConfiguration.isValidEmail(email)) {
                        //Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                        //edtEmailAddress.setError(getResources().getString(R.string.invalid_email));

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity1.this);
                        builder.setCancelable(true);
                        builder.setTitle("WaterWorks");
                        builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
                        builder.setMessage(getResources().getString(R.string.invalid_email));
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        edtEmailAddress.requestFocus();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        //Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                        isInternetPresent = Utility.isNetworkConnected(RegisterActivity1.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new isEmailExistsAsyncTask().execute();
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void isEmailExistsChcek() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("EmailID", email);

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
            pd = new ProgressDialog(RegisterActivity1.this);
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
                //Toast.makeText(getApplicationContext(), ""+messageEmailExists,  Toast.LENGTH_LONG).show();
                //	edtEmailAddress.setError(getResources().getString(R.string.existsEmail));


                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity1.this);
                builder.setCancelable(true);
                builder.setTitle("WaterWorks");
                builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
                builder.setMessage(getResources().getString(R.string.existsEmail));
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                edtEmailAddress.setText("");
                                edtEmailAddress.requestFocus();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            } else {

            }


        }
    }


}
