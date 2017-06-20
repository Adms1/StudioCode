package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.ViewCartAdapter;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ReceivePaymentConfirm extends Activity {

    String token = "", FamilyID, InvoiceID, pastdueAmount;
    private static final String TAG = "Summary cart";
    TableLayout card_lay, check_lay;
    String DOMAIN = AppConfiguration.DOMAIN;
    //Check Lay
    TextView name_check, r_number, acc_number, bank_name, acc_type, tv_view_cart_name;
    TextView f_name, l_name, credit_num, cre_type, cvv, c_add_1, c_add_2, city, state, zipcode, site_name;

    String data_load_page = "False", data_load_delete = "False", data_load_apply = "False",
            data_load_empty = "False", data_load_basket = "False";
    String _Total, _PaidAmount, _DueAmount;
    ArrayList<String> _index, _ItemTypeID, _Type, _Item, _Package, _Price, _Qty, _Tax, _Subtotal, _Delete, DeleteEblDble;
    public TextView tv_total;
    ListView lv_view_cart;
    Button make_change, make_payment;
    LinearLayout paybutton_layout, invoice_view;

    //Order Confirmation
    boolean bool_invoice_view = false;
    String billname, bill_add, billcsc, billdetails, Subtotal, Tax, Total, SiteName;
    TextView invoice_total, tax, grand_total, bill_Add, bill_csc, bill_detail, bill_name;
    ImageButton ib_back;
    TextView view_my_schedule;
    Context mContext = this;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receivepay_confirm);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        FamilyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + FamilyID);

        init();

        if (AppConfiguration.BasketID.toString().equalsIgnoreCase("0")) {
            Toast.makeText(getApplicationContext(), "No balance due..", Toast.LENGTH_LONG).show();
            //			new GetBasketID().execute();
        } else {
            isInternetPresent = Utility.isNetworkConnected(ReceivePaymentConfirm.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new GetCart().execute();
            }
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        make_payment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isInternetPresent = Utility.isNetworkConnected(ReceivePaymentConfirm.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new SubmitPayment().execute();
                }
            }
        });
        view_my_schedule.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(ReceivePaymentConfirm.this, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("POS", 3);
                startActivity(i);
                finish();
            }
        });
        make_change.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        ib_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (bool_invoice_view) {
                    Intent i = new Intent(ReceivePaymentConfirm.this, DashBoardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppConfiguration.BasketID = "BasketID";
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }
    public void init() {
        View view = findViewById(R.id.layout_top);
        ib_back = (ImageButton) view.findViewById(R.id.ib_back);

        view_my_schedule = (TextView) findViewById(R.id.view_my_schedule);
        site_name = (TextView) findViewById(R.id.site_name);
        invoice_total = (TextView) findViewById(R.id.invoice_total);
        tax = (TextView) findViewById(R.id.tax);
        grand_total = (TextView) findViewById(R.id.grand_total);
        bill_Add = (TextView) findViewById(R.id.bill_Add);
        bill_csc = (TextView) findViewById(R.id.bill_csc);
        bill_detail = (TextView) findViewById(R.id.bill_detail);
        bill_name = (TextView) findViewById(R.id.bill_name);
        tv_view_cart_name = (TextView) findViewById(R.id.tv_view_cart_name);
        tv_total = (TextView) findViewById(R.id.tv_view_cart_total);

        lv_view_cart = (ListView) findViewById(R.id.lv_view_cart);

        card_lay = (TableLayout) findViewById(R.id.card_lay);
        check_lay = (TableLayout) findViewById(R.id.check_lay);

        make_payment = (Button) findViewById(R.id.make_payment);
        make_change = (Button) findViewById(R.id.make_change);

        paybutton_layout = (LinearLayout) findViewById(R.id.paybutton_layout);
        invoice_view = (LinearLayout) findViewById(R.id.invoice_view);

        //Check vars
        name_check = (TextView) findViewById(R.id.name_check);
        r_number = (TextView) findViewById(R.id.r_number);
        acc_number = (TextView) findViewById(R.id.acc_number);
        bank_name = (TextView) findViewById(R.id.bank_name);
        acc_type = (TextView) findViewById(R.id.acc_type);

        //Card vars
        f_name = (TextView) findViewById(R.id.f_name);
        l_name = (TextView) findViewById(R.id.l_name);
        credit_num = (TextView) findViewById(R.id.credit_num);
        cre_type = (TextView) findViewById(R.id.cre_type);
        cvv = (TextView) findViewById(R.id.cvv);
        c_add_1 = (TextView) findViewById(R.id.c_add_1);
        c_add_2 = (TextView) findViewById(R.id.c_add_2);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        zipcode = (TextView) findViewById(R.id.zipcode);

        if (ReceivePayment.pay_type_card == false) {
            check_lay.setVisibility(View.VISIBLE);
            card_lay.setVisibility(View.GONE);

            name_check.setText(ReceivePayment.netxt_page_value.get("check_name"));
            r_number.setText(ReceivePayment.netxt_page_value.get("r_number"));
            acc_number.setText(ReceivePayment.netxt_page_value.get("acc_number"));
            bank_name.setText(ReceivePayment.netxt_page_value.get("bank_name"));
            acc_type.setText(ReceivePayment.netxt_page_value.get("acc_type"));

        } else {
            check_lay.setVisibility(View.GONE);
            card_lay.setVisibility(View.VISIBLE);

            f_name.setText(ReceivePayment.netxt_page_value.get("fname"));
            l_name.setText(ReceivePayment.netxt_page_value.get("lname"));
            credit_num.setText(ReceivePayment.netxt_page_value.get("cnum"));
            cre_type.setText(ReceivePayment.netxt_page_value.get("card_type"));
            cvv.setText(ReceivePayment.netxt_page_value.get("cvv"));
            c_add_1.setText(ReceivePayment.netxt_page_value.get("c_address_1"));
            c_add_2.setText(ReceivePayment.netxt_page_value.get("c_address_2"));
            city.setText(ReceivePayment.netxt_page_value.get("c_city"));
            state.setText(ReceivePayment.netxt_page_value.get("cstate"));
            zipcode.setText(ReceivePayment.netxt_page_value.get("c_zip"));
        }
    }
    String msg;
    public class GetCart extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ReceivePaymentConfirm.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", FamilyID);
            param.put("BasketID", AppConfiguration.BasketID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.viewcart__pasduebal_basketid, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_page = reader.getString("Success");
                if (data_load_page.toString().equals("True")) {
                    _Total = reader.getString("Total");
                    _index = new ArrayList<String>();
                    _ItemTypeID = new ArrayList<String>();
                    _Type = new ArrayList<String>();
                    _Item = new ArrayList<String>();
                    _Package = new ArrayList<String>();
                    _Price = new ArrayList<String>();
                    _Qty = new ArrayList<String>();
                    _Tax = new ArrayList<String>();
                    _Subtotal = new ArrayList<String>();
                    _Delete = new ArrayList<String>();
                    DeleteEblDble = new ArrayList<String>();
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        _index.add(jsonChildNode.getString("index"));
                        _ItemTypeID.add(jsonChildNode.getString("ItemTypeID"));
                        _Type.add(jsonChildNode.getString("Type"));
                        _Item.add(jsonChildNode.getString("Item"));
                        _Package.add(jsonChildNode.getString("Package"));
                        _Price.add(jsonChildNode.getString("Price"));
                        _Qty.add(jsonChildNode.getString("Qty"));
                        _Tax.add(jsonChildNode.getString("Tax"));
                        _Subtotal.add(jsonChildNode.getString("Subtotal"));
                        _Delete.add(jsonChildNode.getString("Delete"));
                        //								DeleteEblDble.add(jsonChildNode.getString("DeleteEblDble"));
                        DeleteEblDble.add("False");
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
            if (data_load_page.toString().equals("True")) {
                tv_total.setText("Total: $" + _Total);
                lv_view_cart.setVisibility(View.VISIBLE);
                lv_view_cart.setAdapter(new ViewCartAdapter(ReceivePaymentConfirm.this, _index, _ItemTypeID, _Item,
                        _Type, _Package, _Price, _Qty, _Tax, _Subtotal, _Delete, DeleteEblDble));
            } else {
                Toast.makeText(ReceivePaymentConfirm.this, msg, Toast.LENGTH_LONG).show();
                lv_view_cart.setVisibility(View.GONE);
            }
        }
    }
    public String et_valu(TextView et) {
        String value = "";
        value = et.getText().toString().trim();
        return value;
    }
    public String spinner_value(Spinner sp) {
        String value = "";
        value = sp.getSelectedItem().toString();
        return value;
    }
    public class SubmitPayment extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        String errormsg = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ReceivePaymentConfirm.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("BasketID", AppConfiguration.BasketID);
            param.put("pastduepaymentFlag", "false");
            param.put("pastdueInvoiceId", "");
            param.put("creditexp", ReceivePayment.cred_exp);
            param.put("strPaymentCredit", ReceivePayment.card_detail);
            param.put("strPaymentCheck", ReceivePayment.check_detail);
            param.put("Total", _Total);
            param.put("strPaymentBillingAddress", ReceivePayment.address);
            param.put("strPaymentShippingAddress", "");
            param.put("Userid1", "");

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.Pay_Conform_SubmitPayment, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_page = reader.getString("Success");

                if (data_load_page.contains("True")) {
                    JSONArray jArr = reader.getJSONArray("ConformMsgDisplay");

                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject jObj = jArr.getJSONObject(i);
                        if (jObj.toString().contains("InvoiceID")) {
                            InvoiceID = jObj.getString("InvoiceID");
                            errormsg = jObj.getString("Msg");
                        }
                    }
                } else {
                    errormsg = reader.getString("Msg");
                }
            } catch (Exception e) {}
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (data_load_page.toString().equalsIgnoreCase("True")) {
                //				new viewPastDueBalance().execute();
                if (errormsg.contains("Not")) {
                    Toast.makeText(mContext, errormsg, Toast.LENGTH_SHORT).show();
                } else {
                    isInternetPresent = Utility.isNetworkConnected(ReceivePaymentConfirm.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new viewPastDueBalance().execute();
                    }
                }
            } else {
                Toast.makeText(mContext, errormsg, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class viewPastDueBalance extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ReceivePaymentConfirm.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", FamilyID);
            param.put("InvoiceID", InvoiceID);
            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.ViewPasDueBal_InvoiceData, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                String read = reader.getString("Success");
                if (read.contains("True")) {
                    AppConfiguration.BasketID = reader.getString("0");
                    pastdueAmount = reader.getString("pastdueAmount");
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pd.dismiss();
            bool_invoice_view = true;
            isInternetPresent = Utility.isNetworkConnected(ReceivePaymentConfirm.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new PayInvoiceLoad().execute();
            }
        }
    }
    public class PayInvoiceLoad extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        String read;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ReceivePaymentConfirm.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("BasketID", AppConfiguration.BasketID);
            param.put("InvoiceId", InvoiceID);
            param.put("pastdueAmount", pastdueAmount);

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.Pay_Invoice_Load, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                read = reader.getString("Success");
                if (read.contains("True")) {
                    JSONArray jarr = reader.getJSONArray("InvoiceDtl");
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jobj = jarr.getJSONObject(i);
                        SiteName = jobj.getString("Sitename");
                        billname = jobj.getString("billname");
                        bill_add = jobj.getString("billaddress1") + jobj.getString("billaddress2");
                        billcsc = jobj.getString("billcsc");
                        billdetails = jobj.getString("billdetails");
                    }
                    JSONArray invoice_data = reader.getJSONArray("InvoiceDtldata");
                    for (int i = 0; i < invoice_data.length(); i++) {
                        JSONObject jobj = invoice_data.getJSONObject(i);
                        Subtotal = jobj.getString("Subtotal");
                        Tax = jobj.getString("Tax");
                    }
                    JSONArray invoice_total = reader.getJSONArray("InvoiceDtlTotal");
                    for (int i = 0; i < invoice_total.length(); i++) {
                        JSONObject jobj = invoice_total.getJSONObject(i);
                        Total = jobj.getString("Total");
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pd.dismiss();
            if (read.contains("True")) {
                view_my_schedule.setClickable(true);
                tv_view_cart_name.setText("Order Confirmation");
                paybutton_layout.setVisibility(View.GONE);
                invoice_view.setVisibility(View.VISIBLE);

                site_name.setText(SiteName);
                invoice_total.setText("Invoice Total: " + Subtotal);
                tax.setText("Tax : " + Tax);
                grand_total.setText("Total : " + Total);
                grand_total.setTypeface(Typeface.DEFAULT_BOLD);
                bill_Add.setText(bill_add);
                bill_csc.setText(billcsc);
                bill_detail.setText(billdetails);
                bill_name.setText(billname);
            } else {
                Toast.makeText(getApplicationContext(), "Something's went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (bool_invoice_view) {
            Intent i = new Intent(ReceivePaymentConfirm.this, DashBoardActivity.class);
            AppConfiguration.BasketID = "BasketID";
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //			i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();
        } else {
            finish();
        }
    }
}
