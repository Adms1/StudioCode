package com.waterworks.manageProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.SwimCompititionTrophyRoomResultsAcitivity;
import com.waterworks.asyncTasks.CardAsyncTask;
import com.waterworks.asyncTasks.CheckingAcAsyncTask;
import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.asyncTasks.StateListAsyncTask;
import com.waterworks.asyncTasks.cardDetailAsyncTask;
import com.waterworks.sheduling.BuyMoreOrderSummary;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.CustomDialogClass;
import com.waterworks.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ManagePaymentAccount extends Activity {

    private LinearLayout llPaymentAccounts;
    private cardDetailAsyncTask cardDetailAsyncTask = null;
    private SitesListAsyncTask sitesListAsyncTask = null;
    private HashMap<String, String> paramBasketIDToken = new HashMap<String, String>();
    private ArrayList<String> clientName = new ArrayList<>();
    private ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> siteIDList = new ArrayList<String>();
    ArrayList<HashMap<String, String>> cardDetailArray = new ArrayList<HashMap<String, String>>();
    private String token, familyID, responseString;
    private ProgressDialog progressDialog;
    private Context mContext;
    Boolean isInternetPresent =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accnts);
        mContext = this;
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(this);

        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        addHeader();
        initViews();
        addListners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void initViews() {
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ManagePaymentAccount.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        llPaymentAccounts = (LinearLayout) findViewById(R.id.llPaymentAccounts);
        isInternetPresent = Utility.isNetworkConnected(ManagePaymentAccount.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sitesListAsyncTask = new SitesListAsyncTask(token);
                        responseString = sitesListAsyncTask.execute().get();
                        readAndParseSiteJSON(responseString);
                        StringBuilder result = new StringBuilder();
                        for (String val : siteIDList) {
                            result.append(val);
                            result.append(",");
                        }
                        paramBasketIDToken.put("strSiteID", result.substring(0, result.length() - 1));
                        paramBasketIDToken.put("Token", token);

                        cardDetailAsyncTask = new cardDetailAsyncTask(mContext, paramBasketIDToken);
                        responseString = cardDetailAsyncTask.execute().get();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                readAndParseJSON(responseString);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public void addListners() {

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

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");

            if (successStr.equalsIgnoreCase("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("ACHList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    //commented as per new development by aanal : 28/Jun/2016
                    hashmap.put("wu_CardAccNumber", jsonChildNode.getString("wu_CardAccNumber"));
                    hashmap.put("wu_PayType", jsonChildNode.getString("wu_PayType"));
                    hashmap.put("wu_SendId", jsonChildNode.getString("wu_SendId"));
                    cardDetailArray.add(hashmap);
                    Log.e("cardDetailArray size---", "" + cardDetailArray.size());
                    AppConfiguration.cardDetailArray = cardDetailArray;
                    Log.e("AppConfig.cardDetailAr", "" + AppConfiguration.cardDetailArray);
                    Log.e("cardDetailArray size--1", "" + cardDetailArray.size());
                }
            } else if (successStr.equalsIgnoreCase("False")) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        inflateSelectPaymentMethod(cardDetailArray);

    }

    public void inflateSelectPaymentMethod(final ArrayList<HashMap<String, String>> cardDetailArray) {

        Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());

        for (int i = 0; i < cardDetailArray.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.row_manage_account, null, false);
            TextView txtClientName = (TextView) view.findViewById(R.id.txtClientName);
            TextView txtLblRemove = (TextView) view.findViewById(R.id.txtLblRemove);
            txtLblRemove.setOnClickListener(myClickLIstener);

            if (cardDetailArray.get(i).get("wu_PayType").equalsIgnoreCase("Card")) {
                //  card detail..............
                txtClientName.setText(cardDetailArray.get(i).get("wu_PayType") + " " + cardDetailArray.get(i).get("wu_CardAccNumber").toString().trim().substring(Math.max(0, cardDetailArray.get(i).get("wu_CardAccNumber").toString().trim().length() - 9)));
                txtLblRemove.setTag(cardDetailArray.get(i).get("wu_SendId"));
            } else {
                //  check details........
                txtClientName.setText("Checking - " + cardDetailArray.get(i).get("wu_CardAccNumber").toString().trim().replace("*****", ""));
                txtLblRemove.setTag(cardDetailArray.get(i).get("wu_SendId"));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, px, 0, 0);
            view.setLayoutParams(params);

            llPaymentAccounts.addView(view);
        }
    }

    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(View v) {
            HashMap<String, String> hashMapFinal = new HashMap<>();
            for (HashMap<String, String> hashMap : cardDetailArray) {
                if (hashMap.get("wu_SendId").equals(v.getTag().toString())) {
                    hashMapFinal.put("Token", token);
                    hashMapFinal.put("familyid", familyID);
                    hashMapFinal.put("sendid", v.getTag().toString());
                }
            }

            CustomDialogClass cdd = new CustomDialogClass(ManagePaymentAccount.this, hashMapFinal);
            cdd.show();
        }
    };

    public void readAndParseSiteJSON(String in) {
        try {
            HashMap<String, String> hashmap;
            hashmap = new HashMap<String, String>();

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("SiteList");

            if (jsonMainNode.length() > 1) {
                hashmap.put("SiteID", "0");
                hashmap.put("SiteName", "Please Select a Location");
                siteMainList.add(hashmap);
            }
            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("siteid"));
                hashmap.put("SiteName", jsonChildNode.getString("sitename"));

                siteIDList.add("" + jsonChildNode.getString("siteid"));
                siteMainList.add(hashmap);
            }

            Log.d("siteName---", "" + siteIDList);
            Log.d("siteName---1", "" + siteIDList.size());
            Log.d("siteMainList---", "" + siteMainList);
            Log.d("siteMainList---1", "" + siteMainList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addHeader() {
        //Custom Header
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Manage Payment Accounts");

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
