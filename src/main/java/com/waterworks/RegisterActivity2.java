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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class RegisterActivity2 extends Activity {

    LinearLayout ll_secondary_parent;
    ImageView imgNext;
    Spinner spinner1, spinner2;
    ArrayList<HashMap<String, String>> phoneTypeList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> typeList = new ArrayList<String>();
    String primaryPhoneType;
    String secondaryPhoneType;
    EditText edtStreetAddress, edtApt, edtCity, edtState, edtZipCode, edtPrimarytele, edtSecondarytele;
    ImageView btnValidZipCode;
    String zipcode;
    String getState = null, getCity = null;
    String successZipCode = "";
    String messageZipCode = "";
    String DOMAIN;
    Boolean isInternetPresent = false;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2_activity);
        DOMAIN = getIntent().getStringExtra("DOMAIN");
        spinner1 = (Spinner) findViewById(R.id.spinner1_primary_tel);
        spinner2 = (Spinner) findViewById(R.id.spinner2_secondary_tel);
        ll_secondary_parent = (LinearLayout) findViewById(R.id.ll_secondary_parent);
        imgNext = (ImageView) findViewById(R.id.imgNext);
        edtStreetAddress = (EditText) findViewById(R.id.edtStreetAddress);
        edtApt = (EditText) findViewById(R.id.edtApt);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtState = (EditText) findViewById(R.id.edtState);
        edtZipCode = (EditText) findViewById(R.id.edtZipCode);
        edtPrimarytele = (EditText) findViewById(R.id.edtPrimarytele);
        edtSecondarytele = (EditText) findViewById(R.id.edtSecondarytele);
        btnValidZipCode = (ImageView) findViewById(R.id.btnValidZipCode);

        isInternetPresent = Utility.isNetworkConnected(RegisterActivity2.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new PhoneTypeAsyncTask().execute();

            btnValidZipCode.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });

            edtZipCode.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtZipCode.getWindowToken(), 0);

                        zipcode = edtZipCode.getText().toString();
                        isInternetPresent = Utility.isNetworkConnected(RegisterActivity2.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new ValidatingZipCodeAsyncTask().execute();
                        }
                        return true;

                    } else {
                        return false;
                    }
                }
            });

            edtZipCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edtZipCode.getText().toString().length() == 5) {
                        isInternetPresent = Utility.isNetworkConnected(RegisterActivity2.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new ValidatingZipCodeAsyncTask().execute();
                        }
                    } else {
                        edtState.setText("");
                        edtCity.setText("");
                        btnValidZipCode.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            imgNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    String address = edtStreetAddress.getText().toString();
                    String apt = edtApt.getText().toString();
                    String city = edtCity.getText().toString();
                    String state = edtState.getText().toString();
                    zipcode = edtZipCode.getText().toString();
                    String pTelephone = edtPrimarytele.getText().toString();
                    String sTelephone = edtSecondarytele.getText().toString();
//09-05-2017 megha
                    if (pTelephone.isEmpty() || sTelephone.isEmpty()) {
                    }else{
                        if (pTelephone.trim().length() > 9) {
                            if (!pTelephone.equals("") && (!pTelephone.contains("(") || !pTelephone.contains("-"))) {
                                String part1 = pTelephone.substring(0, 3);
                                String part2 = pTelephone.substring(3, 6);
                                String part3 = pTelephone.substring(6, pTelephone.length());

                                pTelephone = "(" + part1 + ")" + part2 + "-" + part3;
                                Log.e("Formatttttt", pTelephone);
                            }
                            if (sTelephone.trim().length() > 9) {
                                if (!sTelephone.equals("") && (!sTelephone.contains("(") || !sTelephone.contains("-"))) {
                                    String part4 = sTelephone.substring(0, 3);
                                    String part5 = sTelephone.substring(3, 6);
                                    String part6 = sTelephone.substring(6, sTelephone.length());

                                    sTelephone = "(" + part4 + ")" + part5 + "-" + part6;
                                    Log.e("Formatttttt", sTelephone);
                                }
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_LONG).show();
                        }
                    }
                    if (address.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty() || pTelephone.isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                    } else {
                        if (!AppConfiguration.isValidMobile(pTelephone)) {
                            Toast.makeText(getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                        } else if (!sTelephone.equals("")) {
                            if (!AppConfiguration.isValidMobile("" + sTelephone)) {
                                Toast.makeText(getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                            } else {

                                AppConfiguration.Address = address;
                                AppConfiguration.City = city;
                                AppConfiguration.Apt = "" + apt;
                                AppConfiguration.State = state;
                                AppConfiguration.Zipcode = zipcode;
                                AppConfiguration.Phone1 = pTelephone;
                                AppConfiguration.PhoneType1 = primaryPhoneType;
                                AppConfiguration.Phone2 = sTelephone;
                                AppConfiguration.PhoneType2 = secondaryPhoneType;

                                Intent i = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("DOMAIN", DOMAIN);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        } else {
                            AppConfiguration.Address = address;
                            AppConfiguration.City = city;
                            AppConfiguration.Apt = "" + apt;
                            AppConfiguration.State = state;
                            AppConfiguration.Zipcode = zipcode;
                            AppConfiguration.Phone1 = pTelephone;
                            AppConfiguration.PhoneType1 = primaryPhoneType;
                            AppConfiguration.Phone2 = sTelephone;
                            AppConfiguration.PhoneType2 = secondaryPhoneType;

                            Intent i = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                            startActivityForResult(i, RESULT_OK);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
//                    if (address.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty() || pTelephone.isEmpty() || sTelephone.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
//                    } else if (!AppConfiguration.isValidMobile(pTelephone)) {
//                        Toast.makeText(getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_SHORT).show();
//                    } else if (!AppConfiguration.isValidMobile("" + sTelephone)) {
//                        Toast.makeText(getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_SHORT).show();
////                            } else {
////                                AppConfiguration.Address = address;
////                                AppConfiguration.City = city;
////                                AppConfiguration.Apt = "" + apt;
////                                AppConfiguration.State = state;
////                                AppConfiguration.Zipcode = zipcode;
////                                AppConfiguration.Phone1 = pTelephone;
////                                AppConfiguration.PhoneType1 = primaryPhoneType;
////                                AppConfiguration.Phone2 = sTelephone;
////                                AppConfiguration.PhoneType2 = secondaryPhoneType;
////
////                                Intent i = new Intent(RegisterActivity2.this, RegisterActivity3.class);
////                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                i.putExtra("DOMAIN", DOMAIN);
////                                startActivity(i);
////                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                            }
//                    } else {
//                        AppConfiguration.Address = address;
//                        AppConfiguration.City = city;
//                        AppConfiguration.Apt = "" + apt;
//                        AppConfiguration.State = state;
//                        AppConfiguration.Zipcode = zipcode;
//                        AppConfiguration.Phone1 = pTelephone;
//                        AppConfiguration.PhoneType1 = primaryPhoneType;
//                        AppConfiguration.Phone2 = sTelephone;
//                        AppConfiguration.PhoneType2 = secondaryPhoneType;
//
//                        Intent i = new Intent(RegisterActivity2.this, RegisterActivity3.class);
//                        startActivityForResult(i, RESULT_OK);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    }


                }

            });
//=================

            // fetching header view
            Button relMenu = (Button) findViewById(R.id.relMenu);
            relMenu.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
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

    public void loadPhoneTypes() {
        HashMap<String, String> param = new HashMap<String, String>();
        String responseString = WebServicesCall.RunScript(AppConfiguration.phoneTypesURL, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("PhoneList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("PhoneType", jsonChildNode.getString("PhoneType"));
                hashmap.put("Pk_PhoneTypeId",
                        jsonChildNode.getString("Pk_PhoneTypeId"));

                typeList.add(jsonChildNode.getString("PhoneType"));
                phoneTypeList.add(hashmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class PhoneTypeAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity2.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadPhoneTypes();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegisterActivity2.this, android.R.layout.simple_spinner_item, typeList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(dataAdapter);

            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(RegisterActivity2.this, android.R.layout.simple_spinner_item, typeList);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(dataAdapter);

            // Spinner1 on item click listener
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                    primaryPhoneType = phoneTypeList.get(position).get("Pk_PhoneTypeId");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            // Spinner2 on item click listener
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                    secondaryPhoneType = phoneTypeList.get(position).get("Pk_PhoneTypeId");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        }
    }


    //========================================== Validating Zipcode =================================

    public void validatingZipcode() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("zipcode", edtZipCode.getText().toString());

        String responseString = WebServicesCall.RunScript(AppConfiguration.validateZipCodeURL, param);
        readAndParseJSONZipCode(responseString);
    }

    public void readAndParseJSONZipCode(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successZipCode = reader.getString("Success");
            if (successZipCode.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("LocationDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    getState = jsonChildNode.getString("State");
                    getCity = jsonChildNode.getString("City");

                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("LocationDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageZipCode = jsonChildNode.getString("Msg");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ValidatingZipCodeAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity2.this);
            pd.setMessage("Validating...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            validatingZipcode();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            edtPrimarytele.requestFocus();
            if (successZipCode.toString().equals("True")) {
                edtState.setText(getState);
                edtCity.setText(getCity);
                btnValidZipCode.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "" + messageZipCode, Toast.LENGTH_LONG).show();
                edtState.setText("");
                edtCity.setText("");

            }
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }


}
