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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rakesh Tiwari ADMS on 2/1/2016.
 */
public class SendUsMessage extends Activity implements View.OnClickListener, View.OnTouchListener {
    String TAG = "Send Us Message";
    LinearLayout ll_secondary_parent, ll_callBack;
    CardView btnSend;
    Spinner spinner1, spinner2_reference;
    ArrayList<HashMap<String, String>> phoneTypeList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> typeList = new ArrayList<String>();
    RelativeLayout title_black;
    Button returnStack;
    TextView page_title;
    String primaryPhoneType;
    String secondaryPhoneType;
    CheckBox chk_callBack;
    EditText edtFirstName, edtLastName, edtEmail, edtSubject, edtBestCall, edtPrimarytele, edtAlternate, edtDetailedMessage;

    RelativeLayout rel_back;
    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    LinearLayout sites_lay, ll_location;
    ArrayList<String> siteName = new ArrayList<String>();
    String DeptName, DeptValue;
    String selectedSiteID, selectedSiteName;
    String firstname, lastname, email, bestcall, pTelephone, sTelephone, subject, detailMessage;
    String successFeedback;
    String messageFeedback;

    String token, familyID;
    Context mContext = this;
    Boolean isInternetPresent = false;

    //    megha 13-04-2017
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_us_message);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);


        chk_callBack = (CheckBox) findViewById(R.id.chk_callBack);
        spinner1 = (Spinner) findViewById(R.id.spinner1_sites);
        spinner1.setPrompt("Please Select");
        sites_lay = (LinearLayout) findViewById(R.id.sites_lay);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        spinner2_reference = (Spinner) findViewById(R.id.spinner2_reference);
        ll_secondary_parent = (LinearLayout) findViewById(R.id.ll_secondary_parent);
        btnSend = (CardView) findViewById(R.id.btnSend);
//        edtName = (EditText)findViewById(R.id.edtName);
        edtFirstName = (EditText) findViewById(R.id.edtName);
//        edtLastName = (EditText)findViewById(R.id.edtLastName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtBestCall = (EditText) findViewById(R.id.edtCallbackTime);
        edtPrimarytele = (EditText) findViewById(R.id.edtPhone);
//        edtAlternate = (EditText)findViewById(R.id.edtAlternate);
        edtSubject = (EditText) findViewById(R.id.edtSubject);
        edtDetailedMessage = (EditText) findViewById(R.id.edtMessage);
        ll_callBack = (LinearLayout) findViewById(R.id.ll_callBack);
        View view = (View) findViewById(R.id.header);
        title_black = (RelativeLayout) view.findViewById(R.id.title_black);
        title_black.setVisibility(View.GONE);
        returnStack = (Button) view.findViewById(R.id.returnStack);
        page_title = (TextView) view.findViewById(R.id.page_title);
        page_title.setText("Send Us A Message");

        sv = (ScrollView) findViewById(R.id.sv);

        edtFirstName.setOnClickListener(this);
        edtEmail.setOnClickListener(this);
        edtSubject.setOnClickListener(this);
        edtDetailedMessage.setOnClickListener(this);
        sites_lay.setOnClickListener(this);
        returnStack.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        edtFirstName.setOnTouchListener(this);
        edtEmail.setOnTouchListener(this);
        edtSubject.setOnTouchListener(this);
        edtDetailedMessage.setOnTouchListener(this);
        sites_lay.setOnTouchListener(this);

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SendUsMessage.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
//        returnStack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        isInternetPresent = Utility.isNetworkConnected(SendUsMessage.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new SitesListAsyncTask().execute();
            typeList.add("Front Desk");
            typeList.add("Maintenance");
            typeList.add("Management");
            typeList.add("Accounting");
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(SendUsMessage.this, android.R.layout.simple_spinner_item, typeList);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2_reference.setAdapter(dataAdapter1);
            // Spinner1 on item click listener
            spinner2_reference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    sites_lay.setBackgroundResource(R.drawable.gray_border);
                    DeptName = arg0.getItemAtPosition(position).toString();
                    DeptValue = "" + (position + 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


           /*=============== megha 13-04-2017=================*/
           edtDetailedMessage.setVerticalScrollBarEnabled(true);
            edtDetailedMessage.setMovementMethod(new ScrollingMovementMethod());
                sv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    edtDetailedMessage.getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            });
            edtDetailedMessage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    edtDetailedMessage.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            /*==========================================================*/
            chk_callBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
//                    chk_callBack.setBackgroundResource(R.drawable.custom_checkbox_check_orange);
                        chk_callBack.setButtonDrawable(R.drawable.custom_checkbox_check_orange);
                        ll_callBack.setVisibility(View.VISIBLE);
//                    if( bestcall.isEmpty() || pTelephone.isEmpty() ){
//                        Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
//                    }
                    } else {
//                    chk_callBack.setBackgroundResource(R.drawable.custom_check_orange);
                        chk_callBack.setButtonDrawable(R.drawable.custom_checkbox_uncheck);
                        ll_callBack.setVisibility(View.GONE);

                    }
                }
            });


            ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
            ib_menusad.setBackgroundResource(R.drawable.back_arrow);
//        edtPrimarytele.setText(formatPhoneNumber( number));
            edtPrimarytele.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        edtPrimarytele.setText(edtPrimarytele.getText().toString().replaceAll("\\(", "")
                                .replaceAll("\\) ", "")
                                .replaceAll("x", "")
                                .replaceAll("-", "")
                                .replaceAll(" ", ""));
                    } else {
                        if (edtPrimarytele.getText().toString().trim().length() > 9) {
                            edtPrimarytele.setText(formatPhoneNumber(edtPrimarytele.getText().toString().trim()));
                            edtPrimarytele.setBackgroundResource(R.drawable.bordergray);
                        } else {
                            edtPrimarytele.setBackgroundResource(R.drawable.error_border);
                        }
                    }
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

    public static String formatPhoneNumber(String number) {
        if (number.trim().length() >= 10) {
            if (number.trim().length() == 10) {
                number = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, number.length());
            } else {
                number = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10)
                        + "x" + number.substring(10, number.length());
            }
        }
        return number;
    }

    //==========================================Start of Site List ===================================================
    public void loadSitesList() {
        siteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
//        String responseString = WebServicesCall.RunScript(AppConfiguration.getSiteListURL, param);
        String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALesssionSiteListURL, param);
        readAndParseJSON(responseString);


    }

    public void readAndParseJSON(String in) {
        try {
            HashMap<String, String> hashmap;
            hashmap = new HashMap<String, String>();
            hashmap.put("SiteID", "0");
            hashmap.put("SiteName", "Please Select");
            siteMainList.add(hashmap);
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("SiteList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("siteid"));
                hashmap.put("SiteName", jsonChildNode.getString("sitename"));

                siteName.add("" + jsonChildNode.getString("sitename"));
                siteMainList.add(hashmap);
            }
            siteName.add(0, "Please Select");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
//        if (v == edtFirstName) {
//            edtFirstName.setBackgroundResource(R.drawable.gray_border);
//        }
//        if (v == edtEmail) {
//            edtEmail.setBackgroundResource(R.drawable.gray_border);
//        }
//        if (v == edtSubject) {
//            edtSubject.setBackgroundResource(R.drawable.gray_border);
//        }
//        if (v == edtDetailedMessage) {
//            edtDetailedMessage.setBackgroundResource(R.drawable.gray_border);
//        }
//        if (v == sites_lay) {
//            sites_lay.setBackgroundResource(R.drawable.gray_border);
//        }
        if (v == returnStack) {
            finish();
        }
        if (v == btnSend) {
            firstname = edtFirstName.getText().toString();
//                lastname = edtLastName.getText().toString();
            email = edtEmail.getText().toString();
            bestcall = edtBestCall.getText().toString();
            pTelephone = edtPrimarytele.getText().toString();
//                sTelephone = edtAlternate.getText().toString();
            subject = edtSubject.getText().toString();
            detailMessage = edtDetailedMessage.getText().toString();
            Log.d("spinner1.getSelectedItem()...", "" + spinner1.getSelectedItem().toString());
            if (firstname.isEmpty() || email.isEmpty() || subject.isEmpty() || detailMessage.isEmpty() || spinner1.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                validation();
                Toast.makeText(getApplicationContext(), "Please complete all required fields.", Toast.LENGTH_LONG).show();
            } else {
                if (!AppConfiguration.isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid Email", Toast.LENGTH_SHORT).show();
                } else {
                    isInternetPresent = Utility.isNetworkConnected(SendUsMessage.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new FeedBackSendingAsyncTask().execute();
                    }
                }
            }
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == edtFirstName) {
            edtFirstName.setBackgroundResource(R.drawable.gray_border);
        }
        if (v == edtEmail) {
            edtEmail.setBackgroundResource(R.drawable.gray_border);
        }
        if (v == edtSubject) {
            edtSubject.setBackgroundResource(R.drawable.gray_border);
        }
        if (v == edtDetailedMessage) {
            edtDetailedMessage.setBackgroundResource(R.drawable.gray_border);
        }
        if (v == sites_lay) {
            sites_lay.setBackgroundResource(R.drawable.gray_border);
        }
        return false;
    }
    class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SendUsMessage.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            siteMainList.clear();
            siteName.clear();
        }
        @Override
        protected Void doInBackground(Void... params) {
            loadSitesList();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            Log.d("siteMainList-#--", "" + siteMainList);
            Log.d("siteMainList-#--1", "" + siteMainList.size());
            Log.d("siteName--#-", "" + siteName);
            Log.d("siteName--#-1", "" + siteName.size());

            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(SendUsMessage.this, android.R.layout.simple_spinner_item, siteName);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(dataAdapter1);
            if (siteMainList.size() == 2) {
                selectedSiteName = siteMainList.get(1).get("SiteName");
                selectedSiteID = siteMainList.get(1).get("SiteID");
                spinner1.setSelection(1);
                spinner1.setPrompt(siteName.get(1));

                Log.d("selectedSiteName--#-1", "" + siteMainList.get(1).get("SiteName"));
                Log.d("selectedSiteID--#-1", "" + siteMainList.get(1).get("SiteID"));
                Log.d("selectedSiteName--#-0", "" + siteMainList.get(0).get("SiteName"));
                Log.d("selectedSiteID--#-0", "" + siteMainList.get(0).get("SiteID"));
                Log.d("siteName--#-1", "" + siteName.get(1));
                Log.d("siteName--#-0", "" + siteName.get(0));
                Log.d("selectedItem--#-0", "" + spinner1.getSelectedItem());
            } else {
                ll_location.setVisibility(View.VISIBLE);
                // Spinner1 on item click listener
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                        sites_lay.setBackgroundResource(R.drawable.gray_border);
                        selectedSiteName = siteMainList.get(position).get("SiteName");
                        selectedSiteID = siteMainList.get(position).get("SiteID");
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }
        }
    }
    //===================================== feedback =======================================
    public void sendingFeedback() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("firstname", "" + firstname);
        param.put("lastname", "" + firstname);
        param.put("phone1", "" + pTelephone);
        param.put("phone2", "" + pTelephone);
        param.put("Email", "" + email);
        param.put("callback", "" + bestcall);
        param.put("subject", "" + subject);
        param.put("problemDetails", detailMessage);
        param.put("DeptName", DeptName);
        param.put("DeptValue", DeptValue);
        param.put("Sitename", selectedSiteName);
        param.put("sitevalue", selectedSiteID);
        Log.d("selectedSiteName.....", " " + "selectedSiteName");
        Log.d("selectedSiteID.....", "" + selectedSiteID);
        String responseString = WebServicesCall.RunScript(AppConfiguration.feedbackURL, param);
        readAndParseJSONFeedback(responseString);
        Log.d("param.....", "" + param);
    }
    public void readAndParseJSONFeedback(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successFeedback = reader.getString("Success");
            if (successFeedback.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("ContactMail");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageFeedback = jsonChildNode.getString("Msg");
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("ContactMail");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageFeedback = jsonChildNode.getString("Msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class FeedBackSendingAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SendUsMessage.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            sendingFeedback();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successFeedback.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), "" + "Your message has been sent successfully.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + messageFeedback, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void validation() {
        if (edtFirstName.getText().toString().trim().length() == 0) {
            edtFirstName.setBackgroundResource(R.drawable.error_border);
        }
        if (edtEmail.getText().toString().trim().length() == 0) {
            edtEmail.setBackgroundResource(R.drawable.error_border);
        }
        if (edtSubject.getText().toString().trim().length() == 0) {
            edtSubject.setBackgroundResource(R.drawable.error_border);
        }
        if (edtDetailedMessage.getText().toString().trim().length() == 0) {
            edtDetailedMessage.setBackgroundResource(R.drawable.error_border);
        }
        if (spinner1.getSelectedItem().equals("Please Select")) {
            sites_lay.setBackgroundResource(R.drawable.error_border);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
    }
    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
}



