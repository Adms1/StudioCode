package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.Listadapter;
import com.waterworks.model.Days;
import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import ab.custom.Helper;

@SuppressWarnings("deprecation")
public class SwimTeamActivity extends Activity {
    TextView card_notice;
    public EditText tv_price;//, card_num;
    Button btn_site, btn_child, btn_group, btn_months, btn_no_days, btn_date, btn_days;
    CardView btn_add;
    public TableRow tblRowSite, tblRowChild, tblRowGroup, tblRowMonths, tblRowStartDate, tblRowNoDays, tblRowChooseDays, tblRowPrice;
    private static String TAG = "Swim Team";
    String DOMAIN = AppConfiguration.DOMAIN;
    String message, price, ChildID, GroupID = "", final_date;
    public static int hwMnySelct = 0, globalInc = 0;
    String data_load_group = "False", data_load_site = "False", child_load = "False", desc_load = "False",
            desc_load_add = "False";
    String token, familyID, msg;
    Boolean isInternetPresent = false, day_ready = false;
    String data_load_basket = "False";
    ListView check_inflate;
    ArrayList<Days> dayslist = new ArrayList<>();
    CharSequence[] m_months;

    protected ArrayList<CharSequence> selectedmonth = new ArrayList<CharSequence>();
    protected ArrayList<CharSequence> selectedmonthForString = new ArrayList<CharSequence>();
    ArrayList<CharSequence> selectedDays = new ArrayList<CharSequence>();
    ArrayList<CharSequence> DayNo = new ArrayList<CharSequence>();
    ArrayList<String> month_no = new ArrayList<String>();
    ArrayList<String> days = new ArrayList<String>();
    ArrayList<String> sitename, siteid, GroupName, GroupIDSW, Student, StudentID, SwimTeamName, SwimTeamDescription,
            NoOfDayName_Arr = new ArrayList<String>(), NoOfDayID_Arr, MonthID_Arr;//DayID_Arr, DayName_Arr
    ListPopupWindow lpw_site, lpw_group, lpw_child, lpw_days;
    String siteID = "", NoOfDay = "";
    Context mContext = this;
    HashMap<String, String> Split_NoOfID_Array = new HashMap<String, String>();
    String cardnumber = "";
    TextView txtPriceinfo, tv_fsn_info;
    ScrollView scrollteam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_team_new);

        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        View view = findViewById(R.id.layout_top);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Youth Swim Team");
        scrollteam = (ScrollView) findViewById(R.id.scrollteam);
        tv_fsn_info = (TextView) findViewById(R.id.tv_fsn_info);

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimTeamActivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        isInternetPresent = Utility.isNetworkConnected(SwimTeamActivity.this);
        if (isInternetPresent) {
            Initialization();
            new CardCheck().execute();
            new GetSiteByFID().execute();
            new GetChild().execute();
            new NoOFDays().execute();
            new GetMonths().execute();

            btn_site.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    tblRowSite.setBackgroundResource(R.drawable.pure_error_border_white);
                    lpw_site.show();
                }
            });
            btn_group.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    tblRowGroup.setBackgroundResource(R.drawable.pure_error_border_white);
                    if (GroupName.size() > 0) {
                        lpw_group.show();
                    }
                }
            });
            btn_child.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    tblRowChild.setBackgroundResource(R.drawable.pure_error_border_white);
                    lpw_child.show();
                }
            });

            btn_months.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    selectedmonthForString.clear();
                    tblRowMonths.setBackgroundResource(R.drawable.pure_error_border_white);
                    boolean[] checkedMOnths = new boolean[m_months.length];
                    int count = m_months.length;

                    for (int i = 0; i < count; i++)
                        checkedMOnths[i] = selectedmonth.contains(m_months[i]);

                    DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                selectedmonth.add(m_months[which]);
                                month_no.add(MonthID_Arr.get(which));
                            } else {
                                selectedmonth.remove(m_months[which]);
                                month_no.remove(MonthID_Arr.get(which));
                                btn_months.setText("");
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Select Month");
                    builder.setMultiChoiceItems(m_months, checkedMOnths, coloursDialogListener);
                    builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedmonthForString.clear();
                            for (int i = 0; i < m_months.length; i++) {
                                for (int j = 0; j < selectedmonth.size(); j++) {
                                    if (m_months[i].equals(selectedmonth.get(j))) {
                                        selectedmonthForString.add(m_months[i]);
                                        onChangeSelectedMonth(selectedmonthForString);
                                    }
                                }
                            }
                            if (month_no.size() > 0) {
                                new NoOFDays().execute();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            tv_fsn_info.setVerticalScrollBarEnabled(true);
            tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
            scrollteam.setOnTouchListener(new View.OnTouchListener() {
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
            btn_add.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (AppConfiguration.BasketID.toString().equalsIgnoreCase("0")) {
                        new GetBasketID().execute();
                    }

                    if (!btn_site.getText().toString().equalsIgnoreCase("select site")) {
                        if (!ChildID.equalsIgnoreCase("0")) {
                            if (!btn_group.getText().toString().equalsIgnoreCase("Please Choose")) {
                                if (month_no.size() > 0) {
                                    if (!btn_date.getText().toString().equals("")) {
                                        if (SwimTeamActivity.globalInc > 0) {
                                            days.clear();
                                            for (int i = 0; i < check_inflate.getChildCount(); i++) {
                                                View view = check_inflate.getChildAt(i);
                                                CheckBox chk = (CheckBox) view.findViewById(R.id.day);
                                                if (chk.isChecked()) {
                                                    days.add(dayslist.get(i).getDayID());
                                                }
                                            }
                                            if (btn_no_days.getText().toString().trim().equalsIgnoreCase(String.valueOf(days.size()))) {
                                                new AddSwimTeam().execute();
                                            } else {
                                                tblRowChooseDays.setBackgroundResource(R.drawable.error_border);
                                                Utility.ping(mContext, "You must select at least " + btn_no_days.getText().toString() + " days");
                                            }
                                        } else {
                                            tblRowChooseDays.setBackgroundResource(R.drawable.error_border);
                                            Utility.ping(mContext, "Please select day");
                                        }
                                    } else {
                                        tblRowStartDate.setBackgroundResource(R.drawable.error_border);
                                        btn_date.requestFocus();
                                        Utility.ping(mContext, "Please select a start date");
                                    }
                                } else {
                                    tblRowMonths.setBackgroundResource(R.drawable.error_border);
                                    btn_months.requestFocus();
                                    Utility.ping(mContext, "Please select a month");
                                }
                            } else {
                                tblRowGroup.setBackgroundResource(R.drawable.error_border);
                                btn_group.requestFocus();
                                Utility.ping(mContext, "Please select a group");
                            }
                        } else {
                            tblRowChild.setBackgroundResource(R.drawable.error_border);
                            Utility.ping(mContext, "Please select a child");
                            btn_child.requestFocus();
                        }
                    } else {
                        tblRowSite.setBackgroundResource(R.drawable.error_border);
                        Utility.ping(mContext, "Please select a site");
                        btn_site.requestFocus();
                    }
                }
            });
            btn_no_days.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    tblRowNoDays.setBackgroundResource(R.drawable.pure_error_border_white);
                    if (day_ready) {
                        lpw_days.show();
                    } else {
                    }
                }
            });
            check_inflate.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });

            btn_date.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    tblRowStartDate.setBackgroundResource(R.drawable.pure_error_border_white);
                    int mYear, mMonth, newMonth, mDay, newYear;

                    Calendar mcurrentDate = Calendar.getInstance();
                    mYear = mcurrentDate.get(Calendar.YEAR);
                    mMonth = mcurrentDate.get(Calendar.MONTH);
                    mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                    try {
                        Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(selectedmonthForString.get(0).toString().substring(0, 4));
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        newMonth = cal.get(Calendar.MONTH);
                        if (mMonth == newMonth) {

                        } else {
                            mMonth = newMonth;
                            mDay = 1;
                        }

                        Date date1 = new SimpleDateFormat("yyyy", Locale.ENGLISH).parse(selectedmonthForString.get(0).toString().substring(4, 8));
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(date1);
                        newYear = cal1.get(Calendar.YEAR);

                        if (mYear == newYear) {

                        } else {
                            mYear = newYear;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatePickerDialog mDatePicker = new DatePickerDialog(SwimTeamActivity.this, new OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                            System.out.println(selectedmonth + "/" + selectedday + "/" + selectedyear);
                            final_date = String.valueOf(selectedmonth + 1) + "/" + String.valueOf(selectedday) + "/" + String.valueOf(selectedyear);
                            btn_date.setText(String.valueOf(selectedmonth + 1) + "/" + String.valueOf(selectedday) + "/" + String.valueOf(selectedyear));

                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select date");
                    mDatePicker.show();
                }
            });
        } else {
            onDetectNetworkState().show();
        }
    }
    public String array_spliter(ArrayList<String> list1) {
        String listString = "";
        for (String s : list1) {
            listString += s + ",";
        }
        return listString;
    }
    public String array_spliter_2(ArrayList<CharSequence> list1) {
        String listString = "";
        for (CharSequence s : list1) {
            listString += s + ",";
        }
        return listString;
    }
    protected void onChangeSelectedMonth(ArrayList<CharSequence> selectedmonth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CharSequence month : selectedmonth) {
            Log.i(TAG, "size = " + selectedmonth.size());
            if (selectedmonth.size() <= 1) {
                stringBuilder.append(month);
            } else {
                stringBuilder.append(month + ",");
            }
        }
        if (selectedmonth.size()<=1) {
            btn_months.setText(stringBuilder.toString().trim());
        } else {
            btn_months.setText(stringBuilder.toString().trim().substring(0, stringBuilder.length() - 1));
        }
    }
    private void Initialization() {
        // TODO Auto-generated method stub
        EditText tv_fsn_info = (EditText) findViewById(R.id.tv_fsn_info);
        tv_fsn_info.setText(Utility.getProgramsInstructionText("3"));
        tv_fsn_info.setScroller(new Scroller(mContext));
        tv_fsn_info.scrollTo(0, 0);
        card_notice = (TextView) findViewById(R.id.card_notice);
        txtPriceinfo = (TextView) findViewById(R.id.txtPriceinfo);
        tblRowSite = (TableRow) findViewById(R.id.tblRowSite);
        tblRowChild = (TableRow) findViewById(R.id.tblRowChild);
        tblRowGroup = (TableRow) findViewById(R.id.tblRowGroup);
        tblRowMonths = (TableRow) findViewById(R.id.tblRowMonths);
        tblRowStartDate = (TableRow) findViewById(R.id.tblRowStartDate);
        tblRowNoDays = (TableRow) findViewById(R.id.tblRowNoDays);
        tblRowChooseDays = (TableRow) findViewById(R.id.tblRowChooseDays);
        tblRowPrice = (TableRow) findViewById(R.id.tblRowPrice);

        tv_price = (EditText) findViewById(R.id.tv_swmtm_price);
        btn_add = (CardView) findViewById(R.id.btn_swmtm_add);
        btn_site = (Button) findViewById(R.id.btn_swmtm_site);
        btn_child = (Button) findViewById(R.id.btn_swmtm_child);
        btn_group = (Button) findViewById(R.id.btn_swmtm_group);
        btn_months = (Button) findViewById(R.id.btn_swmtm_months);
        btn_date = (Button) findViewById(R.id.btn_swmtm_date);
        btn_days = (Button) findViewById(R.id.btn_swmtm_days);
        btn_no_days = (Button) findViewById(R.id.btn_swmtm_no_days);
        check_inflate = (ListView) findViewById(R.id.check_inflate);
        lpw_group = new ListPopupWindow(getApplicationContext());
        GroupName = new ArrayList<String>();
        GroupIDSW = new ArrayList<String>();
    }
    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SwimTeamActivity.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.").setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        onBackPressed();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        return builder1.create();
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
        isInternetPresent = Utility.isNetworkConnected(SwimTeamActivity.this);
    }
    ////////////////////// Basket ID////////////
    public class GetBasketID extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("siteid", siteID);

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
            if (data_load_basket.toString().equalsIgnoreCase("True")) {
                if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
                } else {
                }
            } else {
            }
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
    private class CardCheck extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(true);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("FamilyID", "" + familyID);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.DOMAIN + AppConfiguration.MakePurchase_SwmTm_CardCheck, param);
            try {

                JSONObject reader = new JSONObject(responseString);
                data_load_site = reader.getString("Success");
                if (data_load_site.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("MonthList");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        cardnumber = jsonChildNode.getString("CardNum");
                    }
                } else {

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
            if (data_load_site.toString().equalsIgnoreCase("True")) {
//                card_num.setText(cardnumber);
            } else {
//                16-01 megha
                if (!cardnumber.equalsIgnoreCase("")) {
                    card_notice.setVisibility(View.VISIBLE);
//                card_num.setEnabled(false);
                    btn_add.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Please add cardnumber", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    ///////////////////// Site by Fid///////////

    private class GetSiteByFID extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("FamilyID", "" + familyID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.sitebyFid, param);
            try {

                JSONObject reader = new JSONObject(responseString);
                data_load_site = reader.getString("Success");
                if (data_load_site.toString().equals("True")) {
                    sitename = new ArrayList<String>();
                    siteid = new ArrayList<String>();
                    JSONArray jsonMainNode = reader.optJSONArray("PhoneList");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        siteid.add(jsonChildNode.getString("siteid"));
                        sitename.add(jsonChildNode.getString("sitename"));
                    }
                } else {

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
            if (data_load_site.toString().equalsIgnoreCase("True")) {
                if (siteid.size() == 1) {
                    btn_site.setText(sitename.get(0));
                    siteID = siteid.get(0);
                    String textPrice = Utility.getProgramsPricingText("3", siteID);
                    if (textPrice.equalsIgnoreCase("")) {
                        txtPriceinfo.setVisibility(View.GONE);
                    } else {
                        txtPriceinfo.setVisibility(View.VISIBLE);
                        txtPriceinfo.setText(textPrice);
                    }
                    btn_site.setEnabled(false);
                    btn_site.setBackground(mContext.getResources().getDrawable(R.drawable.gray_border));

                    GroupIDSW.clear();
                    GroupName.clear();
//                    new GetGroup().execute();
                } else {
                    btn_site.setText("Select Site");
                    siteID = siteid.get(0);
                    lpw_site = new ListPopupWindow(getApplicationContext());
                    lpw_site.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, sitename));
                    lpw_site.setAnchorView(btn_site);
                    lpw_site.setHeight(LayoutParams.WRAP_CONTENT);
                    lpw_site.setModal(true);
                    lpw_site.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            // TODO Auto-generated method stub
                            btn_site.setText(sitename.get(pos));
                            siteID = siteid.get(pos);
                            String textPrice = Utility.getProgramsPricingText("3", siteID);
                            if (textPrice.equalsIgnoreCase("")) {
                                txtPriceinfo.setVisibility(View.GONE);
                            } else {
                                txtPriceinfo.setVisibility(View.VISIBLE);
                                txtPriceinfo.setText(textPrice);
                            }
                            lpw_site.dismiss();

                            GroupIDSW.clear();
                            GroupName.clear();
//                            new GetGroup().execute();
                        }
                    });
                }

            } else {
                Toast.makeText(getApplicationContext(), "You are not eligible for purchase.", Toast.LENGTH_LONG).show();
            }
        }
    }

    ////////////////////// GetGroup /////////////////////
    private class GetGroup extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("SiteID", siteID);
            param.put("studentid",ChildID);

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.MakePurchase_SwmTm_BindGroupByStudent/*MakePurchase_SwmTm_BindGroup*/,
                    param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_group = reader.getString("Success");
                if (data_load_group.toString().equals("True")) {

                    JSONArray jsonMainNode = reader.optJSONArray("MonthList");
                    GroupName.clear();
                    GroupIDSW.clear();
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        GroupName.add(jsonChildNode.getString("GroupName"));
                        GroupIDSW.add(jsonChildNode.getString("GroupID"));
                    }
                } else {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                    msg = jsonChildNode.getString("Msg");
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
            if (data_load_group.toString().equals("True")) {
                btn_group.setEnabled(true);
                Log.i(TAG, GroupIDSW.get(0));
                GroupID = GroupIDSW.get(0);
                btn_group.setText(GroupName.get(0));

                lpw_group.setAdapter(
                        new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, GroupName));
                lpw_group.setAnchorView(btn_group);
                lpw_group.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_group.setModal(true);
                lpw_group.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        // TODO Auto-generated method stub
                        btn_group.setText(GroupName.get(pos));
                        Log.i(TAG, GroupName.get(pos));
                        GroupID = GroupIDSW.get(pos);
                        lpw_group.dismiss();
                    }
                });
            } else {
                btn_group.setEnabled(false);
            }
        }
    }
    //////////////// GetChild ////////////////////////
    private class GetChild extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();

            param.put("Token", "" + token);
            param.put("FamilyID", "" + familyID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.swimteamChild, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                child_load = reader.getString("Success");
                if (child_load.toString().equals("True")) {

                    Student = new ArrayList<String>();
                    StudentID = new ArrayList<String>();
                    Student.add("Please select child");
                    StudentID.add("0");
                    JSONArray jsonMainNode = reader.optJSONArray("PhoneList");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        Student.add(jsonChildNode.getString("Student"));
                        StudentID.add(jsonChildNode.getString("StudentID"));
                    }
                } else {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                    msg = jsonChildNode.getString("Msg");
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
            if (child_load.toString().equals("True")) {
                btn_child.setText(Student.get(0));
                ChildID = StudentID.get(0);
                lpw_child = new ListPopupWindow(getApplicationContext());
                lpw_child
                        .setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, Student));
                lpw_child.setAnchorView(btn_child);
                lpw_child.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_child.setModal(true);
                lpw_child.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        // TODO Auto-generated method stub
                        btn_child.setText(Student.get(pos));
                        ChildID = StudentID.get(pos);
                        lpw_child.dismiss();

                        new GetGroup().execute();
                    }
                });
            } else {
            }
        }
    }
    /////////////////////// GetDescription ////////////////

    private class GetDescription extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("FamilyID", "" + familyID);
            param.put("SiteID", siteID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.swimteamDescription, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                desc_load = reader.getString("Success");
                if (desc_load.toString().equals("True")) {

                    SwimTeamName = new ArrayList<String>();
                    SwimTeamDescription = new ArrayList<String>();
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        SwimTeamName.add(jsonChildNode.getString("SwimTeamName"));
                        SwimTeamDescription.add(jsonChildNode.getString("SwimTeamDescription"));
                    }
                } else {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                    msg = jsonChildNode.getString("Msg");
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
            if (desc_load.toString().equals("True")) {
            } else {
            }
        }
    }
    public class DescAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> Name, Desc;

        public DescAdapter(Context context, ArrayList<String> name, ArrayList<String> desc) {
            super();
            this.context = context;
            Name = name;
            Desc = desc;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Name.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        public class ViewHolder {
            TextView tv_name, tv_desc;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();

                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.swimteamdescription_item,
                            null);
                    holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_swmtm_item_desc);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_swmtm_item_name);

                    holder.tv_name.setText(Name.get(position));
                    holder.tv_desc.setText(Desc.get(position));
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                // TODO: handle exception
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

    }

    public static String DaysValue = "";

    private class NoOFDays extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("GroupID", GroupID);

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.MakePurchase_SwmTm_BindNoOfDay,
                    param);
            try {
                JSONObject reader = new JSONObject(responseString);
                desc_load_add = reader.getString("Success");
                if (desc_load_add.toString().equals("True")) {
                    NoOfDayName_Arr = new ArrayList<String>();
                    NoOfDayID_Arr = new ArrayList<String>();
                    JSONArray jsonMainNode = reader.optJSONArray("MonthList");
                    if (jsonMainNode.length() > 0) {
                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                            NoOfDayName_Arr.add(jsonChildNode.getString("NoOfDayName"));
                            NoOfDayID_Arr.add(jsonChildNode.getString("NoOfDayID"));
                        }
                        Split_NoOfID_Array = ArraySplitter(NoOfDayID_Arr);
                    } else {
                        // desc_load_add="False";
                        Toast.makeText(getApplicationContext(), "No data available for this Group", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    desc_load_add = "False";
                    msg = reader.getString("Msg");
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
            if (desc_load_add.toString().equals("True")) {
                if (NoOfDayName_Arr.size() > 0) {
                    day_ready = true;
                    btn_no_days.setText(NoOfDayName_Arr.get(0));
                    DaysValue = Split_NoOfID_Array.get(NoOfDayName_Arr.get(0));
                    tv_price.setText(SwimTeamActivity.DaysValue);
                    lpw_days = new ListPopupWindow(getApplicationContext());
                    lpw_days.setAdapter(
                            new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup, NoOfDayName_Arr));

                    lpw_days.setAnchorView(btn_no_days);
                    lpw_days.setHeight(LayoutParams.WRAP_CONTENT);
                    lpw_days.setModal(true);
                    hwMnySelct = Integer.parseInt(NoOfDayName_Arr.get(0));
                    DaysValue = Split_NoOfID_Array.get(NoOfDayName_Arr.get(0));
                    NoOfDay = NoOfDayID_Arr.get(0);

                    lpw_days.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            // TODO Auto-generated method stub

                            btn_no_days.setText(NoOfDayName_Arr.get(pos));
                            NoOfDay = NoOfDayID_Arr.get(pos);
                            Log.e("NoOfDay", "  " + NoOfDay);
                            hwMnySelct = Integer.parseInt(NoOfDayName_Arr.get(pos));

                            System.out.println("Default selected value : " + hwMnySelct);

                            Log.e("split  no of  days----", "  " + Split_NoOfID_Array.get(NoOfDayName_Arr.get(pos)));
                            // changed by Rakesh 28102015..............
                            DaysValue = Split_NoOfID_Array.get(NoOfDayName_Arr.get(pos));
                            tv_price.setText(SwimTeamActivity.DaysValue);

                            lpw_days.dismiss();
                            new FillDays().execute();
                        }
                    });
                    new FillDays().execute();
                } else {
                    day_ready = false;
                    btn_no_days.setText("");
                    lpw_days = new ListPopupWindow(getApplicationContext());
                    hwMnySelct = 0;
                    tv_price.setText("");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SwimTeamActivity.this,
                            android.R.layout.simple_list_item_1, NoOfDayName_Arr);
                    check_inflate.setAdapter(adapter);
                    Helper.getListViewSize(check_inflate);
                }
            } else {
            }
        }
    }

    public void selectAll() {
        for (int i = 0; i < check_inflate.getChildCount(); i++) {
            View view = check_inflate.getChildAt(i);
            CheckBox cb = (CheckBox) view.findViewById(R.id.day);
            cb.setChecked(true);
        }
    }

    public HashMap<String, String> ArraySplitter(ArrayList<String> Spilt) {
        HashMap<String, String> temp_handler = new HashMap<String, String>();
        for (int i = 0; i < Spilt.size(); i++) {
            StringTokenizer tokens = new StringTokenizer(Spilt.get(i), "|");

            temp_handler.put(tokens.nextToken(), tokens.nextToken());
        }
        return temp_handler;
    }

    private class FillDays extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String listString = "";
            for (String s : month_no) {
                listString += s + ",";
            }
            listString = listString.substring(0, listString.length() - 1);

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("GroupID", GroupID);
            param.put("NoOfDay", NoOfDay);
            param.put("selMonths", listString);

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.MakePurchase_SwmTm_BindFillDay,
                    param);
            try {
                JSONObject reader = new JSONObject(responseString);
                desc_load_add = reader.getString("Success");
                if (desc_load_add.toString().equals("True")) {
                    dayslist.clear();
                    Days days = null;
                    JSONArray jsonMainNode = reader.optJSONArray("MonthList");
                    if (jsonMainNode.length() > 0) {
                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                            days = new Days();
                            days.setDayID(jsonChildNode.getString("DayID"));
                            days.setDayName(jsonChildNode.getString("DayName"));
                            dayslist.add(days);
                        }
                    } else { }
                } else {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                    msg = jsonChildNode.getString("Msg");
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
            if (desc_load_add.toString().equals("True")) {
                globalInc = 0;
                Collections.sort(dayslist, new Comparator<Days>() {
                    public int compare(Days o1, Days o2) {
                        return o1.getDayID().compareTo(o2.getDayID());
                    }
                });
                Listadapter adapter = new Listadapter(SwimTeamActivity.this, dayslist);
                check_inflate.setAdapter(adapter);
                Helper.getListViewSize(check_inflate);

                check_inflate.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub
                        CheckBox cb = (CheckBox) view.findViewById(R.id.day);

                        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }

                });
            } else {
            }
        }
    }

    private class AddSwimTeam extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();

            param.put("Token", "" + token);
            param.put("GroupID", GroupID);
            param.put("NoOfDay", NoOfDay);
            param.put("BasketID", AppConfiguration.BasketID);
            param.put("SiteID", siteID);
            param.put("CardNum", cardnumber);
            param.put("Price1", tv_price.getText().toString());
            param.put("StartDate", final_date);
            param.put("chkDaysAry", array_spliter(days));
            param.put("ddlStudent1", ChildID);
            param.put("chkMonthListAry", array_spliter(month_no));
            param.put("rdbClassToday2", "off");
            String responseString = WebServicesCall
                    .RunScript(DOMAIN + AppConfiguration.MakePurchase_SwmTm_InsertSwimTeamData, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                desc_load_add = reader.getString("Success");
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
            if (desc_load_add.toString().equals("True")) {
                Intent viewCard = new Intent(mContext, ByMoreMyCart.class);
                startActivity(viewCard);
            } else {}
        }
    }

    private class GetMonths extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(SwimTeamActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("showall", "0"); /*change 1 to 0  01-06-2017 megha*/

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.MakePurchase_SwmTm_BindMonthAll,
                    param);
            try {
                JSONObject reader = new JSONObject(responseString);
                desc_load_add = reader.getString("Success");
                if (desc_load_add.toString().equals("True")) {
                    MonthID_Arr = new ArrayList<String>();
                    JSONArray jsonMainNode = reader.optJSONArray("MonthList");
                    if (jsonMainNode.length() > 0) {
                        m_months = new String[jsonMainNode.length()];

                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                            m_months[i] = jsonChildNode.getString("MonthName");
                            Log.d("months", m_months[i].toString());
                            MonthID_Arr.add(jsonChildNode.getString("MonthID"));
                        }
                    } else {}
                } else {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                    msg = jsonChildNode.getString("Msg");
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
            if (desc_load_add.toString().equals("True")) {
            } else {
            }
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
