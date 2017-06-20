package com.waterworks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ByMoreMyCart;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class SwimCampRegister3Activity extends Activity {
    String TAG = "SwimCampsRegister3";
    ArrayList<HashMap<String, String>> swimCampList = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> swimCampTotalList = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> subSwimCampList = new ArrayList<HashMap<String, String>>();

    String arrsession;
    String arrsession1;
    String arrsibsc;

    ArrayList<Boolean> AddSessionValue = new ArrayList<Boolean>();
    ArrayList<Boolean> DropEarlyOffValue = new ArrayList<Boolean>();
    ArrayList<String> LineCount = new ArrayList<String>();
    ArrayList<String> SessionID = new ArrayList<String>();
    ArrayList<String> Description = new ArrayList<String>();
    ArrayList<String> StartDate = new ArrayList<String>();
    ArrayList<String> EndDate = new ArrayList<String>();
    ArrayList<String> Cost = new ArrayList<String>();
    ArrayList<String> Type = new ArrayList<String>();

    Boolean isInternetPresent = false;
    String siteID;
    ListView list;
    String successLoadChildList = "";
    String token, familyID;
    //	TextView txtSelectedStudent;
    String AddSession;
    String DropOffEarly;

    CardView btnAddToCart;
    TextView txtTotalChildValue, txtTotalSessionValue;
    TextView txtTotalPriceValue, txtTotalSibDisctLabel, txtTotalSibDisctValue, txtFinalCapErrorMsg,
            txtTotalDOFeeValue, txtOutStandBalValue, txtInvoiceAmountValue, txtMultipleSSDiscountValue,
            txtAmountBilledValue;
    String data_load_basket;
    String data_load = "", FinalCapacityMsg = "";
    RelativeLayout relAmountBilled, relInvoiceAmount, rlSessionDiscount;
    LinearLayout ll_list;

    String message;
    DecimalFormat df;

    String _nameid, _sessions, _dropoff;//, dropoffNumber = ""/*, final_name_id*/;
    ArrayList<String> StudentName = new ArrayList<String>();
    ArrayList<String> StudentID = new ArrayList<String>();
    ArrayList<String> SUM = new ArrayList<String>();
    ArrayList<String> DropOffValue = new ArrayList<String>();
    ArrayList<String> DiscountValue = new ArrayList<String>();
    ArrayList<String> SubTotal = new ArrayList<String>();

    ArrayList<ArrayList<String>> _LineCount = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _SessionID = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _Description = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _StartDate = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _EndDate = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _Cost = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> _Type = new ArrayList<ArrayList<String>>();

    List<SwimCampRegi3Items> itemData;
	/* new code */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_camps_register3);

        View include_layout_step_boxes = (View) findViewById(R.id.include_layout_step_boxes);
        TextView txtBox3 = (TextView) include_layout_step_boxes.findViewById(R.id.txtBox3);
        txtBox3.setTextColor(Color.WHITE);
        txtBox3.setBackgroundColor(getResources().getColor(R.color.design_change_d2));

        df = new DecimalFormat("####0.00");

        _nameid = getIntent().getStringExtra("Nameid");
        _sessions = getIntent().getStringExtra("Session");
        _dropoff = getIntent().getStringExtra("DropOff");//96685:45|96685:46|96685:54

        initialize();

        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        isInternetPresent = Utility.isNetworkConnected(SwimCampRegister3Activity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
                new GetBasketID().execute();
            }
            new SwimCampListNew().execute();

            btnAddToCart.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    isInternetPresent = Utility
                            .isNetworkConnected(SwimCampRegister3Activity.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new AddToCartAsyncTask().execute();
                    }
                }
            });
            Button relMenu = (Button) findViewById(R.id.relMenu);
            relMenu.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
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
    public void initialize() {
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimCampRegister3Activity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        ll_list = (LinearLayout) findViewById(R.id.ll_list);
        btnAddToCart = (CardView) findViewById(R.id.btnAddToCart);
        rlSessionDiscount = (RelativeLayout) findViewById(R.id.rlSessionDiscount);
        txtMultipleSSDiscountValue = (TextView) findViewById(R.id.txtMultipleSSDiscountValue);
        txtTotalChildValue = (TextView) findViewById(R.id.txtTotalChildValue);
        txtTotalSessionValue = (TextView) findViewById(R.id.txtTotalSessionValue);
        txtTotalPriceValue = (TextView) findViewById(R.id.txtTotalPriceValue);
        txtTotalSibDisctLabel = (TextView) findViewById(R.id.txtTotalSibDisctLabel);
        txtTotalSibDisctValue = (TextView) findViewById(R.id.txtTotalSibDisctValue);
        txtTotalDOFeeValue = (TextView) findViewById(R.id.txtTotalDOFeeValue);
        txtOutStandBalValue = (TextView) findViewById(R.id.txtOutStandBalValue);
        txtAmountBilledValue = (TextView) findViewById(R.id.txtAmountBilledValue);
        txtInvoiceAmountValue = (TextView) findViewById(R.id.txtInvoiceAmountValue);
        txtFinalCapErrorMsg = (TextView) findViewById(R.id.txtFinalCapErrorMsg);

        relAmountBilled = (RelativeLayout) findViewById(R.id.relAmountBilled);
        relInvoiceAmount = (RelativeLayout) findViewById(R.id.relInvoiceAmount);

        itemData = new ArrayList<SwimCampRegi3Items>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.swim_camps, menu);
        return true;
    }

    public void loadSwimCampList() {
        arrsession = null;
        arrsession1 = null;
        arrsibsc = null;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
        params.put("FamilyID", familyID);
        params.put("SiteID", AppConfiguration.swimCampsRegister1SiteID);
        params.put("SelectedStudentList", AppConfiguration.selectedStudentSwimCampRegister2);
        String responseString = WebServicesCall.RunScript(AppConfiguration.swimCampRegister3, params);
        readAndParseJSON(responseString);
    }
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put("StudentID",jsonChildNode.getString("StudentID"));
                    hashmap.put("StudentName",jsonChildNode.getString("StudentName"));
                    hashmap.put("SUM", jsonChildNode.getString("SUM"));
                    hashmap.put("DropOffValue",jsonChildNode.getString("DropOffValue"));
                    hashmap.put("DiscountValue",jsonChildNode.getString("DiscountValue"));
                    hashmap.put("SubTotal", jsonChildNode.getString("SubTotal"));

                    JSONArray jsonStudentNode = jsonChildNode.getJSONArray("StudentDetail");
                    for (int m = 0; m < jsonStudentNode.length(); m++) {
                        JSONObject jsonStudNode = jsonStudentNode.getJSONObject(m);

                        HashMap<String, String> map = new HashMap<String, String>();
                        String lineCount = jsonStudNode.getString("LineCount").toString().trim();
                        String sessionID = jsonStudNode.getString("SessionID").toString().trim();
                        String description = jsonStudNode.getString("Description").toString().trim();
                        String startDate = jsonStudNode.getString("StartDate").toString().trim();
                        String endDate = jsonStudNode.getString("EndDate").toString().trim();
                        String cost = jsonStudNode.getString("Cost").toString().trim();
                        String type = jsonStudNode.getString("Type").toString().trim();

                        map.put("LineCount", lineCount);
                        map.put("SessionID", sessionID);
                        map.put("Description", description);
                        map.put("StartDate", startDate);
                        map.put("EndDate", endDate);
                        map.put("Cost", cost);
                        map.put("Type", type);
                        subSwimCampList.add(map);
                    }
                    AddSessionValue.add(false);
                    DropEarlyOffValue.add(false);
                    swimCampList.add(hashmap);
                }
                JSONArray jsonMainTotal = reader.optJSONArray("FinalTotal");
                for (int i = 0; i < jsonMainTotal.length(); i++) {
                    JSONObject jsonChildNode = jsonMainTotal.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("TotalChild", jsonChildNode.getString("TotalChild"));
                    hashmap.put("Totalsession", jsonChildNode.getString("Totalsession"));
                    hashmap.put("TotalsessionPrice", jsonChildNode.getString("TotalsessionPrice"));
                    hashmap.put("SublingAccounthdr", jsonChildNode.getString("SublingAccounthdr"));
                    hashmap.put("SublingAccount", jsonChildNode.getString("SublingAccount"));
                    hashmap.put("EarlyDropOff", jsonChildNode.getString("EarlyDropOff"));
                    hashmap.put("OutStandingBalance", jsonChildNode.getString("OutStandingBalance"));
                    hashmap.put("AmountBiled", jsonChildNode.getString("AmountBiled"));
                    hashmap.put("InvoiceAmount", jsonChildNode.getString("InvoiceAmount"));
                    hashmap.put("arrsession", jsonChildNode.getString("arrsession"));
                    hashmap.put("arrsession1", jsonChildNode.getString("arrsession1"));
                    hashmap.put("arrsibdisc", jsonChildNode.getString("arrsibdisc"));

                    swimCampTotalList.add(hashmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class SwimCampListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCampRegister3Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            swimCampList.clear();
            subSwimCampList.clear();
            swimCampTotalList.clear();
        }
        @Override
        protected Void doInBackground(Void... params) {
            loadSwimCampList();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successLoadChildList.toString().equals("True")) {

                CustomList adapter = new CustomList(SwimCampRegister3Activity.this, subSwimCampList);
                list.setAdapter(adapter);
                System.out.println("Sum Value : " + swimCampList.get(0).get("SUM"));

                txtTotalChildValue.setText("" + swimCampTotalList.get(0).get("TotalChild"));
                txtTotalSessionValue.setText("" + swimCampTotalList.get(0).get("Totalsession"));
                txtTotalPriceValue.setText("" + swimCampTotalList.get(0).get("TotalsessionPrice"));

                if (!swimCampTotalList.get(0).get("SublingAccount").contains("0child")
                        || !swimCampTotalList.get(0).get("SublingAccount").contains("0 child")) {
                    txtTotalSibDisctLabel.setText("" + swimCampTotalList.get(0).get("SublingAccounthdr"));
                }
                System.out.println("Set -- " + swimCampTotalList.get(0).get("SublingAccounthdr"));
                txtTotalSibDisctValue.setText("" + swimCampTotalList.get(0).get("SublingAccount"));
                txtTotalDOFeeValue.setText("" + swimCampTotalList.get(0).get("EarlyDropOff"));
                txtOutStandBalValue.setText("" + swimCampTotalList.get(0).get("OutStandingBalance"));

                String amountBilled = swimCampTotalList.get(0).get("AmountBiled");
                String invoiceAmount = swimCampTotalList.get(0).get("InvoiceAmount");

                if (!amountBilled.equals("")) {
                    relAmountBilled.setVisibility(View.VISIBLE);
                    txtAmountBilledValue.setText("" + amountBilled);
                }

                if (!invoiceAmount.equals("")) {
                    relInvoiceAmount.setVisibility(View.VISIBLE);
                    txtInvoiceAmountValue.setText("$" + invoiceAmount);
                }

                arrsession = swimCampTotalList.get(0).get("arrsession");
                arrsession1 = swimCampTotalList.get(0).get("arrsession1");
                arrsibsc = swimCampTotalList.get(0).get("arrsibdisc");

            } else {}
        }
    }
    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final ArrayList<HashMap<String, String>> data;
        ViewHolder holder;

        public CustomList(Activity context,ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_row_swim_camp_register3);
            this.context = context;
            this.data = list;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            if (view == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = context.getLayoutInflater();
                view = inflater.inflate(R.layout.list_row_swim_camp_register3,null, true);

                holder.txtDescription = (TextView) view.findViewById(R.id.txtDescription);
                holder.txtUnitPrice = (TextView) view.findViewById(R.id.txtUnitPrice);
                holder.txtStartDate = (TextView) view.findViewById(R.id.txtStartDate);
                holder.txtEndDate = (TextView) view.findViewById(R.id.txtEndDate);
                holder.txtEndDropOff = (TextView) view.findViewById(R.id.txtEndDropOff);
                holder.txtLineCount = (TextView) view.findViewById(R.id.txtLineCount);
                holder.txtSessionID = (TextView) view.findViewById(R.id.txtSessionID);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.txtDescription.setText(subSwimCampList.get(position).get("Description"));
            holder.txtUnitPrice.setText("Price:" + AppConfiguration.currency + df.format(Double.parseDouble(subSwimCampList.get(position).get("Cost"))));
            holder.txtStartDate.setText("StartDate:" + subSwimCampList.get(position).get("StartDate"));
            holder.txtEndDate.setText("EndDate:" + subSwimCampList.get(position).get("EndDate"));
            if (subSwimCampList.get(position).get("Type").toString().equalsIgnoreCase("Yes")) {
                holder.txtEndDropOff.setText("Early DropOff:" + subSwimCampList.get(position).get("Type"));
                holder.txtEndDropOff.setTextColor(context.getResources().getColor(R.color.green_trophy_room));
            } else {
                holder.txtEndDropOff.setText("Early DropOff:" + subSwimCampList.get(position).get("Type"));
            }
            holder.txtLineCount.setText("Line#:" + subSwimCampList.get(position).get("LineCount"));
            holder.txtSessionID.setText("Session#:" + subSwimCampList.get(position).get("SessionID"));
            return view;
        }
    }
    public class ViewHolder {
        TextView txtDescription, txtUnitPrice, txtStartDate, txtEndDate,
                txtEndDropOff, txtLineCount, txtSessionID;
    }
    public class GetBasketID extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCampRegister3Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params1) {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("siteid", AppConfiguration.swimCampsRegister1SiteID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.Get_BasketID, params);
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
                } else {}
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
    // ///==========================

    public void addToCartProcess() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
        params.put("FamilyID", familyID);
        params.put("SiteID", AppConfiguration.swimCampsRegister1SiteID);
        params.put("BasketID", AppConfiguration.BasketID);
        params.put("SelectedStudentList", _nameid);
        params.put("ArraySession", arrsession);
        params.put("ArraySession1", arrsession1);
        params.put("ArraySidDisc", arrsibsc);

        String responseString = WebServicesCall.RunScript(AppConfiguration.makePurchaseAddToCart, params);
        readAndParseJSONAdd(responseString);
    }
    public void readAndParseJSONAdd(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            data_load = reader.getString("Success");
            if (data_load.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class AddToCartAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCampRegister3Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            swimCampList.clear();
        }
        @Override
        protected Void doInBackground(Void... params) {
            addToCartProcess();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (data_load.toString().equals("True")) {
                Intent viewcart = new Intent(getApplicationContext(), ByMoreMyCart.class);
                viewcart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(viewcart);
            } else {
                Toast.makeText(getApplicationContext(), "Some internal error, Please try after sometime.", 1).show();
            }
        }
    }
    /*---------------------------------------new code---------------------------------------*/
    private class SwimCampListNew extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCampRegister3Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params1) {
            // TODO Auto-generated method stub
            arrsession = null;
            arrsession1 = null;
            arrsibsc = null;

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("FamilyID", familyID);
            params.put("SiteID", AppConfiguration.swimCampsRegister1SiteID);
            params.put("StudentList", _nameid);
            params.put("StudentSession", _sessions);
            params.put("StudentDropOff", _dropoff);
            String responseString = WebServicesCall.RunScript(AppConfiguration.swimCampRegister3New, params);

            try {
                JSONObject reader = new JSONObject(responseString);
                successLoadChildList = reader.getString("Success");
                if (successLoadChildList.toString().equals("True")) {
                    if (!reader.getString("FinalCapacityMsg").equalsIgnoreCase("")) {
                        FinalCapacityMsg = reader.getString("FinalCapacityMsg");
                    } else {
                        JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                            StudentID.add(jsonChildNode.getString("StudentID"));
                            StudentName.add(jsonChildNode.getString("StudentName"));
                            SUM.add(jsonChildNode.getString("SUM"));
                            DropOffValue.add(jsonChildNode.getString("DropOffValue"));
                            DiscountValue.add(jsonChildNode.getString("DiscountValue"));
                            SubTotal.add(jsonChildNode.getString("SubTotal"));

                            JSONArray jsonStudentNode = jsonChildNode.getJSONArray("StudentDetail");
                            ArrayList<String> temp_LineCount = new ArrayList<String>();
                            ArrayList<String> temp_SessionID = new ArrayList<String>();
                            ArrayList<String> temp_Description = new ArrayList<String>();
                            ArrayList<String> temp_StartDate = new ArrayList<String>();
                            ArrayList<String> temp_EndDate = new ArrayList<String>();
                            ArrayList<String> temp_Cost = new ArrayList<String>();
                            ArrayList<String> temp_Type = new ArrayList<String>();
                            for (int m = 0; m < jsonStudentNode.length(); m++) {
                                JSONObject jsonStudNode = jsonStudentNode.getJSONObject(m);

                                temp_LineCount.add(jsonStudNode.getString("LineCount").toString().trim());
                                temp_SessionID.add(jsonStudNode.getString("SessionID").toString().trim());
                                temp_Description.add(jsonStudNode.getString("Description").toString().trim());
                                temp_StartDate.add(jsonStudNode.getString("StartDate").toString().trim());
                                temp_EndDate.add(jsonStudNode.getString("EndDate").toString().trim());
                                temp_Cost.add(jsonStudNode.getString("Cost").toString().trim());
                                temp_Type.add(jsonStudNode.getString("Type").toString().trim());
                            }
                            _LineCount.add(temp_LineCount);
                            _SessionID.add(temp_SessionID);
                            _Description.add(temp_Description);
                            _StartDate.add(temp_StartDate);
                            _EndDate.add(temp_EndDate);
                            _Cost.add(temp_Cost);
                            _Type.add(temp_Type);

                        }

                        JSONArray jsonMainTotal = reader
                                .optJSONArray("FinalTotal");
                        for (int i = 0; i < jsonMainTotal.length(); i++) {
                            JSONObject jsonChildNode = jsonMainTotal
                                    .getJSONObject(i);

                            HashMap<String, String> hashmap = new HashMap<String, String>();
                            hashmap.put("TotalChild", jsonChildNode.getString("TotalChild"));
                            hashmap.put("Totalsession", jsonChildNode.getString("Totalsession"));
                            hashmap.put("TotalsessionPrice", jsonChildNode.getString("TotalsessionPrice"));
                            hashmap.put("SessionDiscount", jsonChildNode.getString("SessionDiscount"));
                            hashmap.put("SublingAccounthdr", jsonChildNode.getString("SublingAccounthdr"));
                            hashmap.put("SublingAccount", jsonChildNode.getString("SublingAccount"));
                            hashmap.put("EarlyDropOff", jsonChildNode.getString("EarlyDropOff"));
                            hashmap.put("OutStandingBalance", jsonChildNode.getString("OutStandingBalance"));
                            hashmap.put("AmountBiled", jsonChildNode.getString("AmountBiled"));
                            hashmap.put("InvoiceAmount", jsonChildNode.getString("InvoiceAmount"));
                            hashmap.put("arrsession", jsonChildNode.getString("arrsession"));
                            hashmap.put("arrsession1", jsonChildNode.getString("arrsession1"));
                            hashmap.put("arrsibdisc", jsonChildNode.getString("arrsibdisc"));

                            swimCampTotalList.add(hashmap);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (!FinalCapacityMsg.equalsIgnoreCase("")) {
                txtFinalCapErrorMsg.setVisibility(View.VISIBLE);
                txtFinalCapErrorMsg.setText(FinalCapacityMsg);
            } else {
                if (successLoadChildList.toString().equals("True")) {
                    Log.e(TAG, "Student size = " + StudentID.size());
                    for (int i = 0; i < StudentID.size(); i++) {
                        Log.e(TAG, "Session size = " + _SessionID.get(i).size());
                        itemData.add(new SwimCampRegi3Items(StudentName.get(i),
                                StudentID.get(i), SUM.get(i), DropOffValue.get(i),
                                DiscountValue.get(i), SubTotal.get(i), _LineCount
                                .get(i), _SessionID.get(i), _Description
                                .get(i), _StartDate.get(i),
                                _EndDate.get(i), _Cost.get(i), _Type.get(i)));
                    }
                    loadList(itemData);
                    if (swimCampTotalList.get(0).get("SessionDiscount").toString().equalsIgnoreCase("N/A")) {
                        rlSessionDiscount.setVisibility(View.GONE);
                    } else {
                        rlSessionDiscount.setVisibility(View.VISIBLE);
                        txtMultipleSSDiscountValue.setText("" + swimCampTotalList.get(0).get("SessionDiscount"));
                    }
                    txtTotalChildValue.setText("" + swimCampTotalList.get(0).get("TotalChild"));
                    txtTotalSessionValue.setText("" + swimCampTotalList.get(0).get("Totalsession"));
                    txtTotalPriceValue.setText("" + swimCampTotalList.get(0).get("TotalsessionPrice"));
                    if (swimCampTotalList.get(0).get("SublingAccounthdr").toString().contains("0child")
                            || swimCampTotalList.get(0).get("SublingAccounthdr").toString().contains("0 child")) {
                        txtTotalSibDisctLabel.setText("Total Amount of Sibling Discount");
                    } else {
                        txtTotalSibDisctLabel.setText("" + swimCampTotalList.get(0).get("SublingAccounthdr"));
                    }
                    System.out.println("Here ++ :" + swimCampTotalList.get(0).get("SublingAccounthdr"));
                    txtTotalSibDisctValue.setText("" + swimCampTotalList.get(0).get("SublingAccount"));
                    txtTotalDOFeeValue.setText("" + swimCampTotalList.get(0).get("EarlyDropOff"));
                    txtOutStandBalValue.setText("" + swimCampTotalList.get(0).get("OutStandingBalance"));

                    String amountBilled = swimCampTotalList.get(0).get("AmountBiled");
                    String invoiceAmount = swimCampTotalList.get(0).get("InvoiceAmount");

                    if (!amountBilled.equals("")) {
                        relAmountBilled.setVisibility(View.VISIBLE);
                        txtAmountBilledValue.setText("" + amountBilled);
                    }
                    if (!invoiceAmount.equals("")) {
                        relInvoiceAmount.setVisibility(View.VISIBLE);
                        txtInvoiceAmountValue.setText("$" + invoiceAmount);
                    }
                    arrsession = swimCampTotalList.get(0).get("arrsession");//+dropoffNumber;
                    arrsession1 = swimCampTotalList.get(0).get("arrsession1");
                    arrsibsc = swimCampTotalList.get(0).get("arrsibdisc");

                } else {
                }
            }
        }
    }
    public void loadList(List<SwimCampRegi3Items> itemData) {
        // TODO Auto-generated method stub
        for (int i = 0; i < itemData.size(); i++) {
            try {
                View convertView = LayoutInflater.from(SwimCampRegister3Activity.this).inflate(R.layout.new_list_row_swim_camp_register3, null);
                TextView tv_studentname, tv_sum, tv_dropoff, tv_discount, tv_subtotal;
                LinearLayout ll_students;
                tv_studentname = (TextView) convertView.findViewById(R.id.tv_swm_cmp_regi3_student_name);
                tv_discount = (TextView) convertView.findViewById(R.id.tv_swm_cmp_regi3_discount);
                tv_sum = (TextView) convertView.findViewById(R.id.tv_swm_cmp_regi3_sum);
                tv_dropoff = (TextView) convertView.findViewById(R.id.tv_swm_cmp_regi3_drop_off_fee);
                tv_subtotal = (TextView) convertView.findViewById(R.id.tv_swm_cmp_regi3_sub_total);
                ll_students = (LinearLayout) convertView.findViewById(R.id.ll_swm_cmp_regi3_child);

                SwimCampRegi3Items _itemData = itemData.get(i);
                tv_studentname.setText(_itemData.getStudentName());
                tv_sum.setText(AppConfiguration.currency + _itemData.getSUM());
                tv_dropoff.setText(AppConfiguration.currency + _itemData.getDropOffValue());
                tv_discount.setText(AppConfiguration.currency + _itemData.getDiscountValue());
                tv_subtotal.setText(AppConfiguration.currency + _itemData.getSubTotal());

                for (int j = 0; j < _itemData.get_SessionID().size(); j++) {
                    LayoutInflater minflater = LayoutInflater.from(SwimCampRegister3Activity.this);
                    View childView = minflater.inflate(R.layout.new_list_row_swim_camp_register3_child, null);

                    TextView tv_desc = (TextView) childView.findViewById(R.id.txtDescription);
                    TextView tv_unitprice = (TextView) childView.findViewById(R.id.txtUnitPrice);
                    TextView tv_linecount = (TextView) childView.findViewById(R.id.txtLineCount);
                    TextView tv_sessionid = (TextView) childView.findViewById(R.id.txtSessionID);
                    TextView tv_startdate = (TextView) childView.findViewById(R.id.txtStartDate);
                    TextView tv_enddate = (TextView) childView.findViewById(R.id.txtEndDate);
                    TextView tv_dropoff1 = (TextView) childView.findViewById(R.id.txtEndDropOff1);
                    tv_desc.setText("Description:"+ _itemData.get_Description().get(j));

                    tv_unitprice.setText("Price:" + AppConfiguration.currency + df.format(Double.parseDouble(_itemData.get_Cost().get(j))));
                    tv_linecount.setText("Line#:" + _itemData.get_LineCount().get(j));
                    tv_sessionid.setText("Session #:" + _itemData.get_SessionID().get(j));
                    tv_startdate.setText("StartDate:" + _itemData.get_StartDate().get(j));
                    tv_enddate.setText("EndDate:" + _itemData.get_EndDate().get(j));
                    if (_itemData.get_Type().get(j).toString().equalsIgnoreCase("Yes")) {
                        tv_dropoff1.setText("Early DropOff:" + _itemData.get_Type().get(j));
                        tv_dropoff1.setTextColor(SwimCampRegister3Activity.this.getResources().getColor(R.color.green_trophy_room));
                    } else {
                        tv_dropoff1.setText("Early DropOff:" + _itemData.get_Type().get(j));
                    }
                    ll_students.addView(childView);
                }
                ll_list.addView(convertView);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class SwimCampRegi3Items {
        String StudentName, StudentID, SUM, DropOffValue, DiscountValue,
                SubTotal;
        ArrayList<String> _LineCount, _SessionID, _Description, _StartDate,
                _EndDate, _Cost, _Type;

        public SwimCampRegi3Items(String studentName, String studentID,
                                  String sUM, String dropOffValue, String discountValue,
                                  String subTotal, ArrayList<String> _LineCount,
                                  ArrayList<String> _SessionID, ArrayList<String> _Description,
                                  ArrayList<String> _StartDate, ArrayList<String> _EndDate,
                                  ArrayList<String> _Cost, ArrayList<String> _Type) {
            super();
            StudentName = studentName;
            StudentID = studentID;
            SUM = sUM;
            DropOffValue = dropOffValue;
            DiscountValue = discountValue;
            SubTotal = subTotal;
            this._LineCount = _LineCount;
            this._SessionID = _SessionID;
            this._Description = _Description;
            this._StartDate = _StartDate;
            this._EndDate = _EndDate;
            this._Cost = _Cost;
            this._Type = _Type;
        }

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String studentName) {
            StudentName = studentName;
        }

        public String getStudentID() {
            return StudentID;
        }

        public void setStudentID(String studentID) {
            StudentID = studentID;
        }

        public String getSUM() {
            System.out.println("Sum value :" + SUM);
            return SUM;

        }
        public void setSUM(String sUM) {
            SUM = sUM;
        }

        public String getDropOffValue() {
            return DropOffValue;
        }

        public void setDropOffValue(String dropOffValue) {
            DropOffValue = dropOffValue;
        }

        public void setDiscountValue(String discountValue) {
            DiscountValue = discountValue;
        }

        public String getDiscountValue() {
            return DiscountValue;
        }

        public String getSubTotal() {
            return SubTotal;
        }

        public void setSubTotal(String subTotal) {
            SubTotal = subTotal;
        }

        public ArrayList<String> get_LineCount() {
            return _LineCount;
        }

        public void set_LineCount(ArrayList<String> _LineCount) {
            this._LineCount = _LineCount;
        }

        public ArrayList<String> get_SessionID() {
            return _SessionID;
        }

        public void set_SessionID(ArrayList<String> _SessionID) {
            this._SessionID = _SessionID;
        }

        public ArrayList<String> get_Description() {
            return _Description;
        }

        public void set_Description(ArrayList<String> _Description) {
            this._Description = _Description;
        }

        public ArrayList<String> get_StartDate() {
            return _StartDate;
        }

        public void set_StartDate(ArrayList<String> _StartDate) {
            this._StartDate = _StartDate;
        }

        public ArrayList<String> get_EndDate() {
            return _EndDate;
        }

        public void set_EndDate(ArrayList<String> _EndDate) {
            this._EndDate = _EndDate;
        }

        public ArrayList<String> get_Cost() {
            return _Cost;
        }

        public void set_Cost(ArrayList<String> _Cost) {
            this._Cost = _Cost;
        }

        public ArrayList<String> get_Type() {
            return _Type;
        }

        public void set_Type(ArrayList<String> _Type) {
            this._Type = _Type;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void onBackPressed() {
//		super.onBackPressed();
        finish();
    }
}




