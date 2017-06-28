package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class FamilySwimNightActivity_Register extends Activity {
    Button btn_adult, btn_kids;
    CardView btn_submit;
    private static String TAG = "Family Swim Night regtister";
    Button ib_back;
    Boolean isInternetPresent = false;
    ListView lv_cr_list;
    String data_load = "False", data_load_basket = "False", data_load_site = "False",
            getdate = "False", getcost = "False", subminfamily = "False";
    String siteid;
    String token, familyID;
    //TextView tv_info;
    ListPopupWindow lpw_date_time, lpw_adult, lpw_kids;
    ArrayList<String> select_month, starting_month, all_siteid, all_sitename, SwimDateSiteID, Adult, Kids,
            AdultCost, AdultPrice, KidCost, KidPrice, FamSwimCost, FamilySwimid, Warning;
    TextView tv_adult, tv_kids, tv_total, tv_warning, tv_familyswmnightval, tv_familyswmnight;
    ArrayAdapter<String> adapter;
    String SelectedDateval, SelectedDatevalId, siteidval, sitename;
    ProgressDialog pd;
    String adult, kids;
    String warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_swim_night_register);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        siteid = prefs.getString("siteid", "");
        Intent intent = getIntent();
        if (null != intent) {
            SelectedDateval = intent.getStringExtra("selectdate");
            SelectedDatevalId = intent.getStringExtra("selectdateid");
            siteidval = intent.getStringExtra("siteid");
            sitename = intent.getStringExtra("sitename");
        }
        isInternetPresent = Utility
                .isNetworkConnected(FamilySwimNightActivity_Register.this);
        if (isInternetPresent) {
            Initialization();
            siteid = getIntent().getStringExtra("siteid");
            if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
                new GetBasketID().execute();

            }
            Adult.add("1");
            Adult.add("2");
            Kids.add("0");
            for (int i = 1; i <=4; i++) {
                Kids.add("" + i);
            }
            lpw_adult.setAdapter(new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.edittextpopup, Adult));
            lpw_adult.setAnchorView(btn_adult);
            lpw_adult.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_adult.setModal(true);
            lpw_adult.setOnItemClickListener(
                    new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int pos, long id) {
                            // TODO Auto-generated method stub
                            btn_adult.setText(Adult.get(pos));
                            lpw_adult.dismiss();
                            adult = Adult.get(pos);
//                            09/01/2016 megha
//                            check adult & kids value and call webservice
                            try {
                                if (adult.length() > 0 && kids.length() > 0) {
                                    new GetCost().execute();
                                    Log.d("adult", adult);
                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(FamilySwimNightActivity_Register.this, "Please Select Kids value", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            lpw_kids.setAdapter(new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.edittextpopup, Kids));
            lpw_kids.setAnchorView(btn_kids);
            lpw_kids.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_kids.setModal(true);
            lpw_kids.setOnItemClickListener(
                    new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int pos, long id) {
                            // TODO Auto-generated method stub
                            btn_kids.setText(Kids.get(pos));
                            kids = Kids.get(pos);
                            //call web service
                            try {
                                if (adult.length() > 0 && kids.length() > 0) {
                                    new GetCost().execute();
                                    Log.d("kids", kids);
                                    lpw_kids.dismiss();
                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(FamilySwimNightActivity_Register.this, "Please Select Adult value", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            onDetectNetworkState().show();
        }
    }
    private void Initialization() {
        // TODO Auto-generated method stub
        ib_back = (Button) findViewById(R.id.relMenu);
        ib_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(FamilySwimNightActivity_Register.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        tv_familyswmnight = (TextView) findViewById(R.id.txtfamilyswmnight);
        tv_familyswmnightval = (TextView) findViewById(R.id.txtfamilyswmnightval);
        tv_familyswmnightval.setText(sitename);

        lpw_date_time = new ListPopupWindow(getApplicationContext());

        Adult = new ArrayList<String>();
        Kids = new ArrayList<String>();
        lpw_adult = new ListPopupWindow(getApplicationContext());
        lpw_kids = new ListPopupWindow(getApplicationContext());
        btn_adult = (Button) findViewById(R.id.btn_fsn_adults);
        btn_kids = (Button) findViewById(R.id.btn_fsn_kids);
        btn_adult.setText("Select");
        btn_kids.setText("Select");

        btn_adult.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isInternetPresent) {
                    if (tv_familyswmnightval.getText().toString().equalsIgnoreCase("Please Choose")) {
                        Toast.makeText(FamilySwimNightActivity_Register.this, "Please select date first.", Toast.LENGTH_LONG).show();
                    } else {
                        lpw_adult.show();
                    }
                } else {
                    onDetectNetworkState().show();
                }
            }


        });
        btn_kids.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isInternetPresent) {
                    if (tv_familyswmnightval.getText().toString().equalsIgnoreCase("Please Choose")) {
                        Toast.makeText(FamilySwimNightActivity_Register.this, "Please select date first.", Toast.LENGTH_LONG).show();
                    } else {
                        lpw_kids.show();
                    }
                } else {
                    onDetectNetworkState().show();
                }
            }
        });
        tv_adult = (TextView) findViewById(R.id.tv_fsn_adult);
        tv_kids = (TextView) findViewById(R.id.tv_fsn_kids);
        tv_total = (TextView) findViewById(R.id.tv_fsn_total);
        tv_warning = (TextView) findViewById(R.id.tv_fsn_warning);
        btn_submit = (CardView) findViewById(R.id.btn_fsn_submit);
        btn_submit.setEnabled(false);
        //added by Alka
        tv_adult.setVisibility(View.GONE);
        tv_kids.setVisibility(View.GONE);

        lv_cr_list = (ListView) findViewById(R.id.lv_cr_list);

        btn_submit.setOnClickListener(new OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              // TODO Auto-generated method stub
                                              if (isInternetPresent) {
                                                  new SubmitFamilyNight().execute();
                                              } else {
                                                  onDetectNetworkState().show();
                                              }
                                         }
                                      });
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        isInternetPresent = Utility
                .isNetworkConnected(FamilySwimNightActivity_Register.this);

    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(FamilySwimNightActivity_Register.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        onBackPressed();
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

    public class GetBasketID extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("siteid", "0");

            String responseString = WebServicesCall.RunScript(AppConfiguration.Get_BasketID, param);
            GetBasketID(responseString);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(FamilySwimNightActivity_Register.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            if (data_load_basket.toString().equalsIgnoreCase("True")) {
                if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {} else {}
            } else {}
        }
    }

    public void GetBasketID(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("BasketDtl");
                if (jsonMainNode.toString().equalsIgnoreCase("")) {

                } else {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        AppConfiguration.BasketID = jsonChildNode.getString("Basketid");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String date2Day(String input) {
        String goal = "";
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("MM/dd/yy");
            Date date = inFormat.parse(input);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            goal = outFormat.format(date);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return goal;
    }

    public String date2Month(int number) {
        String month = "";

        if (number == 1) {
            month = "Jan";
        } else if (number == 2) {
            month = "Feb";
        } else if (number == 3) {
            month = "Mar";
        } else if (number == 4) {
            month = "Apr";
        } else if (number == 5) {
            month = "May";
        } else if (number == 6) {
            month = "Jun";
        } else if (number == 7) {
            month = "Jul";
        } else if (number == 8) {
            month = "Aug";
        } else if (number == 9) {
            month = "Sep";
        } else if (number == 10) {
            month = "Oct";
        } else if (number == 11) {
            month = "Nov";
        } else if (number == 12) {
            month = "Dec";
        }
        return month;
    }
    private class GetCost extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(FamilySwimNightActivity_Register.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if (SelectedDateval.toString().equalsIgnoreCase("")) {

            } else {

                HashMap<String, String> param = new HashMap<String, String>();
                param.put("Token", token);
                param.put("FamilyID", familyID);
                param.put("SiteID", siteidval);

                param.put("SwimDate", SelectedDatevalId);
                param.put("Adults", adult);/*btn_adult.getText().toString().trim()*/
                param.put("Kids", kids);/*btn_kids.getText().toString().trim()*/


                String responseString = WebServicesCall.RunScript(AppConfiguration.fsn_getcost, param);
                try {
                    JSONObject reader = new JSONObject(responseString);
                    getcost = reader.getString("Success");
                    if (getcost.toString().equals("True")) {

                        AdultCost = new ArrayList<String>();
                        AdultPrice = new ArrayList<String>();
                        KidCost = new ArrayList<String>();
                        KidPrice = new ArrayList<String>();
                        FamSwimCost = new ArrayList<String>();
                        FamilySwimid = new ArrayList<String>();
                        Warning = new ArrayList<String>();
                        JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                            AdultCost.add(jsonChildNode.getString("AdultCost"));
                            AdultPrice.add(jsonChildNode.getString("AdultPrice"));
                            KidCost.add(jsonChildNode.getString("KidCost"));
                            KidPrice.add(jsonChildNode.getString("KidPrice"));
                            FamSwimCost.add(jsonChildNode.getString("FamSwimCost"));
                            FamilySwimid.add(jsonChildNode.getString("FamilySwimid"));
                            Warning.add(jsonChildNode.getString("Warning"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
            if (getcost.toString().equalsIgnoreCase("True")) {
                tv_adult.setText(Html.fromHtml("<b>Adults</b> " + AdultPrice.get(0)));
                tv_kids.setText(Html.fromHtml("<b>Kids</b> " + KidPrice.get(0)));
                tv_total.setText(Html.fromHtml(FamSwimCost.get(0)));
                warning = Warning.get(0).toString();
                if (warning.toString().equalsIgnoreCase("")) {
                    tv_warning.setVisibility(View.GONE);
                    btn_submit.setEnabled(true);
                } else {
                    tv_warning.setVisibility(View.VISIBLE);
                    tv_warning.setText(warning);
                    btn_submit.setEnabled(false);
                }
            } else {
                Toast.makeText(FamilySwimNightActivity_Register.this, "Some internaml error,Please try after sometime.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class SubmitFamilyNight extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(FamilySwimNightActivity_Register.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", familyID);
            param.put("BasketID", AppConfiguration.BasketID);

            param.put("SiteID", siteidval);
            param.put("SwimDate", SelectedDatevalId);
            param.put("Adults", adult);/*btn_adult.getText().toString().trim()*/
            param.put("Kids", kids);/*btn_kids.getText().toString().trim()*/


            String responseString = WebServicesCall.RunScript(AppConfiguration.fsn_submit, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                subminfamily = reader.getString("Success");
                if (subminfamily.toString().equals("True")) {
                }
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
            if (subminfamily.toString().equalsIgnoreCase("True")) {
                Intent viewcart = new Intent(getApplicationContext(), ByMoreMyCart.class);
                viewcart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(viewcart);
            } else {
                Toast.makeText(FamilySwimNightActivity_Register.this, "Some internaml error,Please try after sometime.", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onDestroy() {
        try {
            if (pd != null && pd.isShowing()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
