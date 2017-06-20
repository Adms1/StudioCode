package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.waterworks.asyncTasks.PaymentHistoryAsyncTask;
import com.waterworks.asyncTasks.SiteListForFamilyPaymentsAsyncTask;
import com.waterworks.model.PaymentHistoryModel;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewPaymentHistoryListNewActivity extends Activity {

    String TAG = "ViewPaymentHistoryNew";
    ArrayList<HashMap<String, String>> viewPaymentHistoryList = new ArrayList<HashMap<String, String>>();

    Spinner spinLocation, spinProgramType;
    TextView txtStartDate, txtEndDate, txtDate, txtSite, txtInvoiceTotal, txtAmmountPaid, txtPaymentType, txtDescription, txtShowLineItems;
    CardView btnFilterResults;
    LinearLayout llPaymentList;
    TextView txtNoRecord;
    boolean isStartDateClicked = false;
    String siteID;
    String typeID;
    String successViewPaymentHistory;
    SiteListForFamilyPaymentsAsyncTask siteListForFamilyPaymentsAsyncTask;
    PaymentHistoryAsyncTask paymentHistoryAsyncTask;
    ArrayList<PaymentHistoryModel> paymentHistoryModels = new ArrayList<>();
    Calendar myCalendar = Calendar.getInstance();
    private ArrayList<String> siteName = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> allTypesList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> typeid = new ArrayList<String>();
    private ArrayList<String> typename = new ArrayList<String>();
    String token, familyID;
    DecimalFormat df;
    Context mContext = this;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_payment_history_new_activity);
        df = new DecimalFormat("####0.00");

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
        // fetching header view
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("View Payment History");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ViewPaymentHistoryListNewActivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        spinLocation = (Spinner) findViewById(R.id.spinLocation);
        spinProgramType = (Spinner) findViewById(R.id.spinProgramType);
        txtStartDate = (TextView) findViewById(R.id.txtStartDate);
        txtEndDate = (TextView) findViewById(R.id.txtEndDate);
        btnFilterResults = (CardView) findViewById(R.id.btnFilterResults);
        llPaymentList = (LinearLayout) findViewById(R.id.llPaymentList);

        btnFilterResults.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siteID == null)
                    siteID = "0";
                if (typeID == null)
                    typeID = "0";
                filterResults(siteID, typeID);
            }
        });

        txtStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isStartDateClicked = true;
                new DatePickerDialog(ViewPaymentHistoryListNewActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtEndDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isStartDateClicked = false;
                new DatePickerDialog(ViewPaymentHistoryListNewActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                siteID = siteMainList.get(position).get("SiteID");
                Log.v(TAG, "" + siteID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinProgramType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeID = typeid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        isInternetPresent = Utility.isNetworkConnected(ViewPaymentHistoryListNewActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            fillSiteList();
            filterResults("0", "0");
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

    public void filterResults(final String siteID, final String typeID) {
        final ProgressDialog pd = new ProgressDialog(ViewPaymentHistoryListNewActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    paymentHistoryAsyncTask = new PaymentHistoryAsyncTask(token, txtStartDate.getText().toString(), txtEndDate.getText().toString(), siteID, typeID);
                    paymentHistoryModels = paymentHistoryAsyncTask.execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (paymentHistoryModels.size() > 0) {
                                inflateLayout(paymentHistoryModels);
                                llPaymentList.setVisibility(View.VISIBLE);
                                txtNoRecord.setVisibility(View.GONE);
                            } else {
                                txtNoRecord.setVisibility(View.VISIBLE);
                                llPaymentList.setVisibility(View.GONE);
                            }

                            pd.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void inflateLayout(ArrayList<PaymentHistoryModel> paymentHistoryModels) {
        boolean isMultipleLines = false;
        if (llPaymentList.getChildCount() > 0) {
            llPaymentList.removeAllViews();
        }

        for (int i = 0; i < paymentHistoryModels.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_row_payment_history, null, false);

            txtDate = (TextView) child.findViewById(R.id.txtDate);
            txtSite = (TextView) child.findViewById(R.id.txtSite);
            txtInvoiceTotal = (TextView) child.findViewById(R.id.txtInvoiceTotal);
            txtAmmountPaid = (TextView) child.findViewById(R.id.txtAmmountPaid);
            txtPaymentType = (TextView) child.findViewById(R.id.txtPaymentType);
            txtDescription = (TextView) child.findViewById(R.id.txtDescription);
            txtShowLineItems = (TextView) child.findViewById(R.id.txtShowLineItems);

            txtDate.setText(paymentHistoryModels.get(i).getInvoicedate());
            txtSite.setText(paymentHistoryModels.get(i).getSitename());
            if(paymentHistoryModels.get(i).getAmountPaid().equals("0.00")){
                String strview ="CREDITED";
                txtPaymentType.setText(strview);
            }else{txtPaymentType.setText(paymentHistoryModels.get(i).getPaymentType());}
            txtInvoiceTotal.setText(paymentHistoryModels.get(i).getTotalAmount());
            txtAmmountPaid.setText(paymentHistoryModels.get(i).getAmountPaid());


            StringBuilder description = new StringBuilder();
            for (int j = 0; j < paymentHistoryModels.get(i).getDescription().size(); j++) {

                if (paymentHistoryModels.get(i).getDescription().size() == 1) {

                    description.append(paymentHistoryModels.get(i).getDescription().get(j).get("ItemType")
                            + ":" + paymentHistoryModels.get(i).getDescription().get(j).get("ItemName"));
                    isMultipleLines = false;
                } else {
                    description.append(paymentHistoryModels.get(i).getDescription().get(j).get("ItemType")
                            + ":" + paymentHistoryModels.get(i).getDescription().get(j).get("ItemName"));

                    if (j < paymentHistoryModels.get(i).getDescription().size() - 1) {
                        description.append("<div style=\"height:1px;\"></div>");
                    }
                    isMultipleLines = true;
                }

            }

            if (isMultipleLines) {
                txtShowLineItems.setVisibility(View.VISIBLE);
                txtShowLineItems.setTag(description);
                txtDescription.setVisibility(View.GONE);
            } else {
                txtDescription.setVisibility(View.VISIBLE);
                txtShowLineItems.setVisibility(View.GONE);
                txtDescription.setText(description);
            }

            txtShowLineItems.setOnClickListener(listner);
            llPaymentList.addView(child);
        }
    }

    OnClickListener listner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            textView.setText(Html.fromHtml(textView.getTag().toString()));
            textView.setTextColor(ViewPaymentHistoryListNewActivity.this.getResources().getColor(R.color.black));
            textView.setTypeface(null, Typeface.NORMAL);
        }
    };

    public void fillSiteList() {
        final ProgressDialog pd = new ProgressDialog(ViewPaymentHistoryListNewActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    siteListForFamilyPaymentsAsyncTask = new SiteListForFamilyPaymentsAsyncTask(token);
                    String responseString = siteListForFamilyPaymentsAsyncTask.execute().get();
                    readAndParseSiteJSON(responseString);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapterSites = new ArrayAdapter<String>(ViewPaymentHistoryListNewActivity.this, android.R.layout.simple_spinner_dropdown_item, siteName);
                            spinLocation.setAdapter(adapterSites);
                            spinLocation.setSelection(0);
                            fillProgramType();
                            pd.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void fillProgramType() {

        String[] typeArray = this.getResources().getStringArray(R.array.alltypes);
        List<String> stringList = new ArrayList<String>(Arrays.asList(typeArray));

        for (int i = 0; i < stringList.size(); i++) {
            String[] temp = stringList.get(i).split("\\|");
            typename.add(temp[0].toString());
            typeid.add(temp[1].toString());
        }

        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(ViewPaymentHistoryListNewActivity.this, android.R.layout.simple_spinner_dropdown_item, typename);
        spinProgramType.setAdapter(adapterTypes);
        spinProgramType.setSelection(0);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MM/dd/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            if (isStartDateClicked) {
                txtStartDate.setText(sdf.format(myCalendar.getTime()));
            } else {
                txtEndDate.setText(sdf.format(myCalendar.getTime()));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void readAndParseSiteJSON(String in) {
        try {
            HashMap<String, String> hashmap;
            hashmap = new HashMap<String, String>();

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("EmailPref");

            if (jsonMainNode.length() > 1) {
                hashmap.put("SiteID", "0");
                hashmap.put("SiteName", "-All Locations-");
                siteName.add("-All Locations-");
                siteMainList.add(hashmap);
            }
            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("SiteID"));
                hashmap.put("SiteName", jsonChildNode.getString("SiteName"));

                siteName.add("" + jsonChildNode.getString("SiteName"));

                siteMainList.add(hashmap);
//
            }

            Log.d("siteName---", "" + siteName);
            Log.d("siteName---1", "" + siteName.size());
            Log.d("siteMainList---", "" + siteMainList);
            Log.d("siteMainList---1", "" + siteMainList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
