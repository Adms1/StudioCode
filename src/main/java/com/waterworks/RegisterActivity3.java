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
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class RegisterActivity3 extends Activity {

    LinearLayout ll_secondary_parent, llerrorBorderChildLevel1, llerrorBorderChildLevel2;
    ArrayList<HashMap<String, String>> FirstList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> thirdList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> secondaryList = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> siteName = new ArrayList<String>();

    ArrayList<String> firstListName = new ArrayList<String>();
    ArrayList<String> thirdListName = new ArrayList<String>();
    ArrayList<String> secondaryListName = new ArrayList<String>();

    String siteID = "0";

    String firstSiteSelected = "0", thirdSiteSelected = "0", secondSiteSelected = "0";
    int isMasterDropDown = 0;
    int isChildDropDown = 0;
    EditText edtThirdLevel, edtSecondaryLevel;
    CardView btnSubmit;

    String successMaster;
    RelativeLayout rel_back;
    String registerSuccess = "";
    String message;
    //Spinner spinner1;
    Button btn_sites, btn_master, btn_secondary, btn_third_level;
    ListPopupWindow lpw_sitelist, lpw_master, lpw_secondary, lpw_thirdlevel;
    String DOMAIN;
    Boolean isInternetPresent = false;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register3_activity);
        DOMAIN = getIntent().getStringExtra("DOMAIN");
        btnSubmit = (CardView) findViewById(R.id.btnSubmit);
        ll_secondary_parent = (LinearLayout) findViewById(R.id.ll_secondary_parent);
        llerrorBorderChildLevel1 = (LinearLayout) findViewById(R.id.llerrorBorderChildLevel1);
        llerrorBorderChildLevel2 = (LinearLayout) findViewById(R.id.llerrorBorderChildLevel2);
        btn_master = (Button) findViewById(R.id.btn_master);
        btn_third_level = (Button) findViewById(R.id.btn_third_level);
        edtThirdLevel = (EditText) findViewById(R.id.edtChildLevel1);
        btn_secondary = (Button) findViewById(R.id.btn_secondary);
        edtSecondaryLevel = (EditText) findViewById(R.id.edtChildLevel2);

        btn_sites = (Button) findViewById(R.id.btn_sites);
        lpw_sitelist = new ListPopupWindow(getApplicationContext());
        lpw_master = new ListPopupWindow(getApplicationContext());
        lpw_secondary = new ListPopupWindow(getApplicationContext());
        lpw_thirdlevel = new ListPopupWindow(getApplicationContext());
        isInternetPresent = Utility.isNetworkConnected(RegisterActivity3.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new SitesListAsyncTask().execute();

            edtThirdLevel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    llerrorBorderChildLevel1.setBackgroundResource(R.drawable.pure_error_border_white);
                }
            });

            edtSecondaryLevel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    llerrorBorderChildLevel2.setBackgroundResource(R.drawable.pure_error_border_white);
                }
            });

            btn_sites.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View paramView) {

                    llerrorBorderChildLevel1.setBackgroundResource(R.drawable.pure_error_border_white);
                    llerrorBorderChildLevel2.setBackgroundResource(R.drawable.pure_error_border_white);

                    AppConfiguration.SiteID = "";
                    AppConfiguration.strMaster = "0";
                    AppConfiguration.strSecondary = "0";
                    AppConfiguration.strChild = "0";

                    lpw_sitelist.show();
                    btn_master.setText("Select");
                }
            });


            btn_master.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    llerrorBorderChildLevel1.setBackgroundResource(R.drawable.pure_error_border_white);
                    llerrorBorderChildLevel2.setBackgroundResource(R.drawable.pure_error_border_white);

                    AppConfiguration.strMaster = "0";
                    AppConfiguration.strSecondary = "0";
                    AppConfiguration.strChild = "0";
                    firstSiteSelected = "0";
                    secondSiteSelected = "0";
                    thirdSiteSelected = "0";

                    lpw_master.show();
                    btn_secondary.setText("Select");
                }
            });

            btn_secondary.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    llerrorBorderChildLevel1.setBackgroundResource(R.drawable.pure_error_border_white);
                    llerrorBorderChildLevel2.setBackgroundResource(R.drawable.pure_error_border_white);

                    AppConfiguration.strSecondary = "0";
                    AppConfiguration.strChild = "0";
                    secondSiteSelected = "0";
                    thirdSiteSelected = "0";

                    lpw_secondary.show();
                }
            });

            btn_third_level.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    llerrorBorderChildLevel1.setBackgroundResource(R.drawable.pure_error_border_white);
                    llerrorBorderChildLevel2.setBackgroundResource(R.drawable.pure_error_border_white);
                    AppConfiguration.strChild = "0";
                    thirdSiteSelected = "0";

                    lpw_thirdlevel.show();
                }
            });


            btnSubmit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AppConfiguration.strOther = "" + edtThirdLevel.getText().toString();
                    if (AppConfiguration.strOther.equals(""))
                        AppConfiguration.strOther = "" + edtSecondaryLevel.getText().toString();

                    if (siteID == "0") {
                        Toast.makeText(getApplicationContext(), "Please select location.", Toast.LENGTH_LONG).show();
                    } else if (btn_master.getVisibility() == View.VISIBLE && firstSiteSelected == "0") {
                        Toast.makeText(getApplicationContext(), "Please select first hear about.", Toast.LENGTH_LONG).show();
                    } else if (btn_secondary.getVisibility() == View.VISIBLE && secondSiteSelected == "0") {
                        Toast.makeText(getApplicationContext(), "Please select second hear about.", Toast.LENGTH_LONG).show();
                    } else if (btn_third_level.getVisibility() == View.VISIBLE && thirdSiteSelected == "0") {
                        Toast.makeText(getApplicationContext(), "Please select third hear about.", Toast.LENGTH_LONG).show();
                    } else if (edtThirdLevel.getVisibility() == View.VISIBLE && AppConfiguration.strOther == "") {
                        Toast.makeText(getApplicationContext(), "Please enter hear about text.", Toast.LENGTH_LONG).show();
                        llerrorBorderChildLevel1.setBackgroundResource(R.drawable.error_border);
                    } else if (edtSecondaryLevel.getVisibility() == View.VISIBLE && AppConfiguration.strOther == "") {
                        Toast.makeText(getApplicationContext(), "Please enter hear about text.", Toast.LENGTH_LONG).show();
                        llerrorBorderChildLevel2.setBackgroundResource(R.drawable.error_border);
                    } else {
                        isInternetPresent = Utility.isNetworkConnected(RegisterActivity3.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new SubmitAsyncTask().execute();
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void loadMasterData() {

        FirstList.clear();
        firstListName.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Siteid", "" + siteID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getDataSpinner1URL, param);
        readAndParseJSONMaster(responseString);
    }

    public void readAndParseJSONMaster(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successMaster = reader.getString("Success");

            if (successMaster.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("MasterList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("master", jsonChildNode.getString("master"));
                    hashmap.put("hearaboutlabel", jsonChildNode.getString("hearaboutlabel"));

                    firstListName.add(jsonChildNode.getString("hearaboutlabel"));
                    FirstList.add(hashmap);
                }
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class MasterAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity3.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadMasterData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successMaster.toString().equals("True")) {

                btn_master.setVisibility(View.VISIBLE);
            } else {
                btn_master.setVisibility(View.GONE);

            }

            lpw_master.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, firstListName));
            lpw_master.setAnchorView(btn_master);
            lpw_master.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_master.setModal(true);
            lpw_master.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            btn_master.setText(firstListName.get(position));
                            lpw_master.dismiss();
                            btn_third_level.setVisibility(View.GONE);
                            edtThirdLevel.setVisibility(View.GONE);
                            btn_secondary.setVisibility(View.GONE);
                            edtSecondaryLevel.setVisibility(View.GONE);


                            firstSiteSelected = FirstList.get(position).get("master");
                            AppConfiguration.strMaster = firstSiteSelected;

                            //dropdown or inputbox
                            String[] separated = firstSiteSelected.split("/");
                            separated[0] = separated[0];
                            separated[1] = separated[1];

                            isMasterDropDown = Integer.parseInt(separated[1]);

                            //next child is dropdown or input box
                            if (isMasterDropDown == 0) {

                                btn_secondary.setVisibility(View.VISIBLE);
                                edtSecondaryLevel.setVisibility(View.GONE);
                            } else {
                                btn_secondary.setVisibility(View.GONE);
                                edtSecondaryLevel.setVisibility(View.VISIBLE);
                            }
                            isInternetPresent = Utility.isNetworkConnected(RegisterActivity3.this);
                            if (!isInternetPresent) {
                                onDetectNetworkState().show();
                            } else {
                                new SecondaryAsyncTask().execute();

                            }
                        }
                    });
        }
    }


    //==========================================Start of Child Level 1 ===================================================

    public void loadThirdLevelData() {

        thirdList.clear();
        thirdListName.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("strMaster", "" + secondSiteSelected);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getDataChildLevel1URL, param);
        readAndParseJSONChildLevel1(responseString);
    }

    public void readAndParseJSONChildLevel1(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if (success.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("MasterList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("child", jsonChildNode.getString("child"));
                    hashmap.put("hearaboutlabel", jsonChildNode.getString("hearaboutlabel"));

                    thirdListName.add(jsonChildNode.getString("hearaboutlabel"));
                    thirdList.add(hashmap);
                }
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ThirdLevelAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity3.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadThirdLevelData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (isChildDropDown == 0) {

                if (thirdListName.size() > 0)
                    btn_third_level.setVisibility(View.VISIBLE);
                else
                    btn_third_level.setVisibility(View.GONE);

                edtThirdLevel.setVisibility(View.GONE);
            } else {
                edtThirdLevel.setVisibility(View.VISIBLE);
                btn_third_level.setVisibility(View.GONE);

            }

            lpw_thirdlevel.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, thirdListName));
            lpw_thirdlevel.setAnchorView(btn_third_level);
            lpw_thirdlevel.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_thirdlevel.setModal(true);
            lpw_thirdlevel.setOnItemClickListener(
                    new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {


                            btn_third_level.setText(thirdListName.get(position));
                            lpw_thirdlevel.dismiss();


                            thirdSiteSelected = thirdList.get(position).get("child");
                            if (isChildDropDown == 0) {
                                AppConfiguration.strChild = thirdSiteSelected;
                            } else {
                                AppConfiguration.strOther = "" + edtThirdLevel.getText().toString();
                                Log.e("childLevelOne", "" + AppConfiguration.strOther);
                            }
                        }
                    });
        }
    }


    //====================================== child level 2 start ===============================


    public void loadSecondaryLevelData() {

        secondaryList.clear();
        secondaryListName.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("strChild", "" + firstSiteSelected);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getDataChildLevel2URL, param);
        readAndParseJSONChildLevel2(responseString);

    }

    public void readAndParseJSONChildLevel2(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if (success.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("MasterList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("secondary", jsonChildNode.getString("secondary"));
                    hashmap.put("hearaboutlabel", jsonChildNode.getString("hearaboutlabel"));

                    secondaryListName.add(jsonChildNode.getString("hearaboutlabel"));
                    secondaryList.add(hashmap);
                }
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SecondaryAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity3.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadSecondaryLevelData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (secondaryListName.size() > 0) {

                lpw_secondary.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, secondaryListName));
                lpw_secondary.setAnchorView(btn_secondary);
                lpw_secondary.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_secondary.setModal(true);
                lpw_secondary.setOnItemClickListener(
                        new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {


                                btn_secondary.setText(secondaryListName.get(position));
                                lpw_secondary.dismiss();


                                secondSiteSelected = secondaryList.get(position).get("secondary");
                                secondSiteSelected = secondSiteSelected.replace(':', '/');

                                //dropdown or inputbox
                                String[] separated = secondSiteSelected.split("/");
                                separated[0] = separated[0];
                                separated[1] = separated[1];

                                isChildDropDown = Integer.parseInt(separated[1]);
                                if (isChildDropDown == 0) {
                                    AppConfiguration.strSecondary = "" + secondSiteSelected;
                                    btn_third_level.setVisibility(View.VISIBLE);
                                    btn_third_level.setText("Select");
                                    edtThirdLevel.setVisibility(View.GONE);

                                } else {
                                    btn_third_level.setVisibility(View.GONE);
                                    edtThirdLevel.setVisibility(View.VISIBLE);

                                    AppConfiguration.strOther = "" + edtSecondaryLevel.getText().toString();
                                    Log.e("Secondary strOther", "" + AppConfiguration.strOther);
                                }
                                isInternetPresent = Utility.isNetworkConnected(RegisterActivity3.this);
                                if (!isInternetPresent) {
                                    onDetectNetworkState().show();
                                } else {
                                    new ThirdLevelAsyncTask().execute();
                                }

                            }
                        });

            } else {

                //next child is dropdown or input box
                if (isMasterDropDown == 0) {
                    btn_secondary.setVisibility(View.GONE);
                    edtSecondaryLevel.setVisibility(View.GONE);
                } else {
                    btn_secondary.setVisibility(View.GONE);
                    edtSecondaryLevel.setVisibility(View.VISIBLE);
                }
                btn_third_level.setVisibility(View.GONE);
                edtThirdLevel.setVisibility(View.GONE);
            }

        }
    }

    //====================================== child level 2 end ===============================


    //==========================================Start of Site List ===================================================
    public void loadSitesList() {
        siteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();

        String responseString = WebServicesCall.RunScript(AppConfiguration.getSiteListURL, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("Sites");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("SiteID"));
                hashmap.put("SiteName", jsonChildNode.getString("SiteName"));

                siteName.add("" + jsonChildNode.getString("SiteName"));
                siteMainList.add(hashmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity3.this);
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

            lpw_sitelist.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, siteName));
            lpw_sitelist.setAnchorView(btn_sites);
            lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_sitelist.setModal(true);
            lpw_sitelist.setOnItemClickListener(
                    new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                            btn_sites.setText(siteMainList.get(pos).get("SiteName"));
                            lpw_sitelist.dismiss();

                            siteID = siteMainList.get(pos).get("SiteID");
                            AppConfiguration.SiteID = siteID;

                            btn_master.setVisibility(View.VISIBLE);

                            btn_third_level.setVisibility(View.GONE);
                            edtThirdLevel.setVisibility(View.GONE);
                            btn_secondary.setVisibility(View.GONE);
                            edtSecondaryLevel.setVisibility(View.GONE);
                            isInternetPresent = Utility.isNetworkConnected(RegisterActivity3.this);
                            if (!isInternetPresent) {
                                onDetectNetworkState().show();
                            } else {
                                new MasterAsyncTask().execute();
                            }
                        }
                    });
        }
    }


    //==========================================End of Site List ===================================================


    //======================================== Submit button event =======================

    public void submittingData() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("EmailAdd", AppConfiguration.EmailAdd);
        param.put("ConfirmEmail", AppConfiguration.ConfirmEmail);
        param.put("Password", AppConfiguration.Password);
        param.put("ConfrmPassword", AppConfiguration.ConfrmPassword);
        param.put("PFirstName", AppConfiguration.PFirstName);
        param.put("PLastName", AppConfiguration.PLastName);
        param.put("SFirstName", AppConfiguration.SFirstName);
        param.put("SLastName", AppConfiguration.SLastName);
        param.put("Address", AppConfiguration.Address);
        param.put("Apt", AppConfiguration.Apt);
        param.put("Zipcode", AppConfiguration.Zipcode);
        param.put("State", AppConfiguration.State);

        param.put("City", AppConfiguration.City);
        param.put("SiteID", AppConfiguration.SiteID);
        param.put("strMaster", AppConfiguration.strMaster);
        param.put("strSecondary", secondSiteSelected);
        param.put("strChild", thirdSiteSelected);
        param.put("strOther", AppConfiguration.strOther);
        param.put("status", "0");
        param.put("PLeavePrivate", AppConfiguration.PLeavePrivate);

        param.put("SLeavePrivate", AppConfiguration.SLeavePrivate);
        param.put("Phone1", AppConfiguration.Phone1);
        param.put("Phone2", AppConfiguration.Phone2);
        param.put("PhoneType1", AppConfiguration.PhoneType1);

        param.put("PhoneType2", AppConfiguration.PhoneType2);

        String responseString = WebServicesCall.RunScript(AppConfiguration.registration, param);
        readAndParseJSONSubmit(responseString);
    }

    public void readAndParseJSONSubmit(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            registerSuccess = reader.getString("Success");
            if (registerSuccess.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("CreateAccount");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");


                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("CreateAccount");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SubmitAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity3.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            submittingData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (registerSuccess.toString().equals("True")) {
                AppConfiguration.strOther = "";

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity3.this);
                builder.setCancelable(true);
                builder.setTitle("Registration - Success");
                builder.setMessage(getResources().getString(R.string.createaccount_success));
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(RegisterActivity3.this, SplashScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("DOMAIN", DOMAIN);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            } else {
                AppConfiguration.strOther = "";
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
