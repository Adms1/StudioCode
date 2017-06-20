package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.SwimCompitition1Adapter;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class SwimCompititionRegisterAcitivity extends Activity {
    String TAG = "SwimCampsActivity1";
    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> siteName = new ArrayList<String>();
    ArrayList<String> siteId = new ArrayList<String>();
    String siteNamepass, DataValuestr, MeetDatestr, MeetStarttimestr, MeetDate_displaystr, Timestr;
    ArrayList<HashMap<String, String>> meetDatesMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Address1 = new ArrayList<String>();
    ArrayList<String> SiteName1 = new ArrayList<String>();
    ArrayList<String> State = new ArrayList<String>();
    ArrayList<String> ZipCode = new ArrayList<String>();
    ArrayList<String> MeetDate_Display = new ArrayList<String>();
    String value;
    ArrayList<String> meetDatesNames = new ArrayList<String>();
    ArrayList<String> DateValue = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    RadioGroup rg;
    RadioButton rb;
    private int selectedPosition = -1;

    ArrayList<String> meetStartTime = new ArrayList<String>();
    String site = "";

    ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    Boolean isInternetPresent = false;
    // Spinner spinner1;
    // Spinner spinner2_MeetDates;
    String siteID = null;
    //	ListView list;
    LinearLayout llListData, custom_linear;
    String successLoadChildList;
    String token, familyID;
    String messageMeetDate;
    String successMeetDate;
    String messageMeetDateLocation;
    String successMeetLocation;

    boolean isSelectedAgreement = false;
    String msg1_Hours, msg2_meet, Msg3_Meet;
    String successSwimCompittionCheck1;
    String messageNormal;
    //CheckBox chkAgree;
    TextView tv_sc_select_location, txtPriceinfo, tvListHeader, tv_sc_Fees;
    Button btn_sites, btnRegister;
    ListPopupWindow lpw_sitelist, lpw_meetDates;
    private boolean[] thumbnailsselection;
    private int count;
    //Button _continue;
    int display;
    TextView time_value;
    EditText tv_fsn_info;
    SwimCompitition1Adapter adp;
    RelativeLayout relLastview;
//    RadioGroup rg;

    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room;
    private TextView txt_1, txt_2, txt_3;
    View selected_1, selected_2, selected_3;
    String meetDatesNamesValue, meetStartTimeValue, MeetDate_DisplayValue, timeValue, Datevalue;
    Context mContext = this;
    ScrollView scrollView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition_register);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

        AppConfiguration.cartBackScreen = 2;
        siteID = "";
        AppConfiguration.SwimMeetID = "";
        AppConfiguration.SwimMeetText = "";
        AppConfiguration.SwimName = "";
        childList.clear();

        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        //DateValue= prefs.getString("DateValue", "");

        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        lpw_meetDates = new ListPopupWindow(getApplicationContext());
        tv_fsn_info = (EditText) findViewById(R.id.tv_fsn_info);
        txtPriceinfo = (TextView) findViewById(R.id.txtPriceinfo);
        tv_sc_Fees = (TextView) findViewById(R.id.tv_sc_Fees);
        tv_sc_select_location = (TextView) findViewById(R.id.tv_sc_select_location);
        btn_sites = (Button) findViewById(R.id.btn_sites);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvListHeader = (TextView) findViewById(R.id.tvListHeader);
        lpw_sitelist = new ListPopupWindow(getApplicationContext());
        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);
        relLastview = (RelativeLayout) findViewById(R.id.relLastview);
        scrollView3 = (ScrollView) findViewById(R.id.scrollView3);

        selected_1.setVisibility(View.GONE);
        selected_2.setVisibility(View.VISIBLE);
        selected_3.setVisibility(View.GONE);

        ((AnimationDrawable) selected_2.getBackground()).start();

        init();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

            }
        }, 1000);

        isInternetPresent = Utility
                .isNetworkConnected(SwimCompititionRegisterAcitivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            String fsn_info = Utility.getProgramsInstructionText("1");
            String[] infoSplit = fsn_info.split("\\!");
            tv_fsn_info.setText(Html.fromHtml("<strong>" + "<big>" + infoSplit[0] + "</strong>" + "</big>" + "<br>" + "\t\t\t" + infoSplit[1] + "</br>"));
            tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
            new SitesListAsyncTask().execute();
        }

        btn_sites.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                if (siteId.size() == 1) {
                } else {
                    lpw_sitelist.show();
                    AppConfiguration.SwimMeetID = "";
                    AppConfiguration.SwimMeetText = "";
                    selectedPosition = -1;
                }
            }
        });

        btnRegister.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                String btn_sitesstr = btn_sites.getText().toString();

                if (!btn_sitesstr.equalsIgnoreCase("Please Select a Location")) {

                    if (selectedPosition != -1) {

                        Intent i = new Intent(mContext, SwimcompititionRegisterStep2Activity.class);
                        i.putExtra("eventdates", meetDatesNamesValue);
                        i.putExtra("datevalue", meetStartTimeValue);
                        i.putExtra("time", MeetDate_DisplayValue);
                        i.putExtra("MeetDate_Display", timeValue);
                        i.putExtra("SiteName", siteNamepass);
                        i.putExtra("DateTime", Datevalue);

                        Log.d(TAG, "time:" + MeetDate_DisplayValue);
                        Log.d(TAG, "starttime:" + meetStartTimeValue);
                        Log.d(TAG, "MeetDate_Display:" + timeValue);
                        Log.d(TAG, "DateTime:" + Datevalue);
                        Log.d(TAG, "eventdates:" + meetDatesNamesValue);

                        startActivity(i);

                    } else {
                        Toast.makeText(mContext, "Please select event.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please select a location", Toast.LENGTH_LONG).show();
                }

            }

        });

        llListData = (LinearLayout) findViewById(R.id.llListData);
        custom_linear = (LinearLayout) findViewById(R.id.custom_linear);
        rg = (RadioGroup) findViewById(R.id.custom_radiogroup);
    }

    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void init() {
        // TODO Auto-generated method stub

        View view = findViewById(R.id.include_swim_comp_custom_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                SwimCompititionRegisterAcitivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                SwimCompititionRegisterAcitivity.this.overridePendingTransition(R.anim.fade_in, R.anim.zoom_in);
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimCompititionRegisterAcitivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        ll_upcoming_meet = (LinearLayout) view.findViewById(R.id.ll_upcoming_meet);
        ll_register = (LinearLayout) view.findViewById(R.id.ll_register);
        ll_trophy_room = (LinearLayout) view.findViewById(R.id.ll_trophy_room);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        if (AppConfiguration.CheckMeet.equalsIgnoreCase("1")) {
            txt_1.setText("Today's Meet");
        }
        tv_fsn_info.setVerticalScrollBarEnabled(true);
        tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
        scrollView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        tv_fsn_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        ll_upcoming_meet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionUpcomingEventsAcitivity.class);
                startActivity(i);
                SwimCompititionRegisterAcitivity.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });
        ll_register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionRegisterAcitivity.class);
                startActivity(i);
                SwimCompititionRegisterAcitivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });
        ll_trophy_room.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionTrophyRoomAcitivity.class);
                startActivity(i);
                SwimCompititionRegisterAcitivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

    }

    @SuppressWarnings("deprecation")
    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        isInternetPresent = Utility
                .isNetworkConnected(SwimCompititionRegisterAcitivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.swim_camps, menu);
        return true;
    }

    public void loadSitesList() {
        siteMainList.clear();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_SwimcompetitionsSiteURL, param);
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
                siteId.add("" + jsonChildNode.getString("SiteID"));
                Log.d("siteName###", "" + siteName.size());
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
            pd = new ProgressDialog(SwimCompititionRegisterAcitivity.this);
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
            if (siteId.size() == 1) {
                btn_sites.setText(siteName.get(0));
                siteID = siteId.get(0);
                String textPrice = Utility.getProgramsPricingText("1", siteID);
                if (textPrice.equalsIgnoreCase("")) {
                    txtPriceinfo.setVisibility(View.GONE);
                    tv_sc_Fees.setVisibility(View.GONE);
                } else {
                    Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                    slide_up.setDuration(1000);
                    txtPriceinfo.setVisibility(View.VISIBLE);
                    tv_sc_Fees.setVisibility(View.VISIBLE);
                    tv_sc_Fees.setAnimation(slide_up);
                    txtPriceinfo.setText(textPrice);
                    txtPriceinfo.setAnimation(slide_up);


                }

                AppConfiguration.siteID = siteID;
                Log.e("SiteID", siteID);
                isInternetPresent = Utility.isNetworkConnected(SwimCompititionRegisterAcitivity.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new MeetDatesAsyncTask().execute();
                    new MeetDatesLocationAsyncTask().execute();
                }
            } else {
                siteID = siteId.get(0);
                lpw_sitelist.setAdapter(new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.edittextpopup, siteName));
                lpw_sitelist.setAnchorView(btn_sites);
                lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_sitelist.setModal(true);
                lpw_sitelist.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        siteID = siteMainList.get(pos).get("SiteID");

                        btn_sites.setText(siteMainList.get(pos).get("SiteName"));
                        lpw_sitelist.dismiss();


                        String textPrice = Utility.getProgramsPricingText("1", siteID);
                        if (textPrice.equalsIgnoreCase("")) {
                            txtPriceinfo.setVisibility(View.GONE);
                            tv_sc_Fees.setVisibility(View.GONE);
                        } else {
                            Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                            slide_up.setDuration(1000);
                            txtPriceinfo.setVisibility(View.VISIBLE);
                            tv_sc_Fees.setVisibility(View.VISIBLE);
                            tv_sc_Fees.setAnimation(slide_up);
                            txtPriceinfo.setText(textPrice);
                            txtPriceinfo.setAnimation(slide_up);


                        }

                        AppConfiguration.siteID = siteID;
                        Log.e("SiteID", siteID);
                        isInternetPresent = Utility.isNetworkConnected(SwimCompititionRegisterAcitivity.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new MeetDatesAsyncTask().execute();
                            new MeetDatesLocationAsyncTask().execute();
                        }
                    }
                });
            }
        }
    }

    // ================================= Meet Dates
    // =======================================

    public void loadMeetDatesList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("SiteID", siteID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.swimCompititionMeetDatesURL, param);
        readAndParseJSONMeetDates(responseString);
    }

    public void readAndParseJSONMeetDates(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            successMeetDate = reader.getString("Success");
            if (successMeetDate.toString().equals("True")) {
                JSONArray jsonMainNode = reader
                        .optJSONArray("SwimMeetDateBySite");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("DateValue",
                            jsonChildNode.getString("DateValue"));
                    hashmap.put("DateText", jsonChildNode.getString("DateText"));

                    meetDatesNames.add("" + jsonChildNode.getString("MeetDate"));
                    meetStartTime.add("" + jsonChildNode.getString("StartTime"));
                    DateValue.add("" + jsonChildNode.getString("DateValue"));
                    MeetDate_Display.add("" + jsonChildNode.getString("MeetDate_Display"));
                    time.add("" + jsonChildNode.getString("DateText"));

                    String Meetid = jsonChildNode.getString("DateValue");

                    String[] Meetidsplit = Meetid.split("\\|");
                    AppConfiguration.SwimMeetIDACK = Meetidsplit[1];
                    Log.d("MeetID", AppConfiguration.SwimMeetIDACK);

                    Log.e("loadMeetDatesLocationList DateValue", DateValue.toString());
                    meetDatesMainList.add(hashmap);
                }
            } else {
                JSONArray jsonMainNode = reader
                        .optJSONArray("SwimMeetDateBySite");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    messageMeetDate = jsonChildNode.getString("Msg");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadMeetDatesLocationList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("SiteID", siteID);
        Log.e("loadMeetDatesLocationList SiteID", siteID);

        String responseString = WebServicesCall
                .RunScript(
                        AppConfiguration.swimCompititionMeetDatesLocationURL, param);
        readAndParseJSONMeetDatesLocation(responseString);
    }

    public void readAndParseJSONMeetDatesLocation(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            successMeetLocation = reader.getString("Success");
            if (successMeetLocation.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("Sites");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    Address1.add(" " + jsonChildNode.getString("Address1"));
                    SiteName1.add(" " + jsonChildNode.getString("SiteName"));
                    State.add(" " + jsonChildNode.getString("State"));
                    ZipCode.add(" " + jsonChildNode.getString("ZipCode"));
                    siteNamepass = jsonChildNode.getString("SiteName");
                    AppConfiguration.siteAddress = jsonChildNode.getString("Address1");
                    Log.e("Address1.toString()", Address1.toString());
                    Log.e("SiteName1.toString() ", SiteName1.toString());
                    Log.e(" State.toString()", State.toString());
                    Log.e("ZipCode.toString()", ZipCode.toString());

                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("Sites");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class MeetDatesLocationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCompititionRegisterAcitivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            Address1.clear();
            SiteName1.clear();
            State.clear();
            ZipCode.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadMeetDatesLocationList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successMeetDate.equalsIgnoreCase("True")) {
                String fstr = Address1.toString() + "-" + SiteName1.toString() + "," + "  " + State.toString() + ZipCode.toString();
                String str = fstr.replaceAll("\\[", "").replaceAll("\\]", "");

                String str1 = SiteName1.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                site = str1;
                tv_sc_select_location.setText(str);
//                27-12-2016 megha
                tv_sc_select_location.setVisibility(View.GONE);

            } else {
                site = "";
                tv_sc_select_location.setText("");
                Toast.makeText(getApplicationContext(), "" + messageMeetDateLocation,
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    class MeetDatesAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCompititionRegisterAcitivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            Address1.clear();
            SiteName1.clear();
            State.clear();
            ZipCode.clear();
            meetDatesMainList.clear();
            meetDatesNames.clear();
            meetStartTime.clear();
            MeetDate_Display.clear();
            DateValue.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadMeetDatesList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successMeetDate.equalsIgnoreCase("True")) {
                relLastview.setVisibility(View.VISIBLE);
                displaylistview();


            } else {
                relLastview.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "" + messageMeetDate,
                        Toast.LENGTH_LONG).show();

                AppConfiguration.SwimMeetID = "";
            }

        }
    }

    public void displaylistview() {
        RadioButton chkEvent;
        TextView tvDate, tvTime;
        if (llListData.getChildCount() > 0) {
            llListData.removeAllViews();
        }
        try {
            for (int i = 0; i < meetDatesNames.size(); i++) {
                View convertView = LayoutInflater.from(SwimCompititionRegisterAcitivity.this).inflate(R.layout.custom_radiobuttonview, null);
                chkEvent = (RadioButton) convertView.findViewById(R.id.custom_radio);
                tvDate = (TextView) convertView.findViewById(R.id.custom_textview1);   /*custom_buttonview_new_layout*/
                tvTime = (TextView) convertView.findViewById(R.id.custom_textview2);

                tvDate.setText(meetDatesNames.get(i));
                tvDate.setTextColor(Color.BLACK);
                tvTime.setText(meetStartTime.get(i));
                chkEvent.setTag(String.valueOf(i));


                if (i == selectedPosition) {
                    chkEvent.setChecked(true);
                    chkEvent.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    chkEvent.setHeight(35);
                } else
                    chkEvent.setChecked(false);

                chkEvent.setOnClickListener(onStateChangedListener(chkEvent, i));
                llListData.addView(convertView);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener onStateChangedListener(final RadioButton checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    selectedPosition = position;
                    meetDatesNamesValue = meetDatesNames.get(selectedPosition);
                    meetStartTimeValue = meetStartTime.get(selectedPosition);
                    timeValue = time.get(selectedPosition);
                    Datevalue = DateValue.get(selectedPosition);
                    MeetDate_DisplayValue = MeetDate_Display.get(selectedPosition);
                } else {
                    selectedPosition = -1;
                    checkBox.setChecked(false);

                }
                displaylistview();

            }
        };

    }
    // ================================== Load Child List ==========================================


    public void loadingChildList() {

        String[] Meetdate = Datevalue.split("\\s");
        String Meetdate1 = Meetdate[0];
        Log.d("mm/dd/yyyy", Meetdate1);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("FamilyID", familyID);
        param.put("meetdate", Meetdate1);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.swimCampRegister1_new, param);
        readAndParseJSONChildList(responseString);

    }

    public void readAndParseJSONChildList(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();


                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String StudentID = jsonChildNode.getString("StudentID")
                            .trim();
                    String StudentName = jsonChildNode.getString("StudentName")
                            .trim();

                    hashmap.put("StudentID", StudentID);
                    hashmap.put("StudentName", StudentName);

                    childList.add(hashmap);

                }

            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        finish();
    }

}
