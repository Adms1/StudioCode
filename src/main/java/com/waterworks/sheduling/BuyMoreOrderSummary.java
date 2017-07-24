package com.waterworks.sheduling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.asyncTasks.ConfirmSubmitPaymentAsyncTask;
import com.waterworks.asyncTasks.InvoiceDataDueBalanceAsyncTask;
import com.waterworks.asyncTasks.PayInvoiceLoadAsyncTask;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class BuyMoreOrderSummary extends Activity {
    LinearLayout swimLsn, retailStore, otherPrograms, ll_inflate_orderSummary_package,
            ll_inflate_orderSummary_monthlyplan, llProductSummary;
    RelativeLayout rlQuantity;
    TextView txt_1, txt_2, txt_3, tv_payment_type, txtProductName, txtPrice, txtTax, txtQuantity, txtSubTotal,
            txtLblPromoCode, txtPromoCode, txtTotalTax, txtTotal, txtPackage, txtLblQuantity;
    View selected_1, selected_2, selected_3, includeAddBillAddress;
    Context mContext = this;
    Button BackButton, btn_place_order, btn_viewCart;
    RadioButton rbShipBillAddress, rbShipDiffAddress;
    EditText edtAddressLine1, edtAddressLine2, edtCity, edtState, edtZipCode;
    ArrayList<HashMap<String, String>> packageOrderSumaryArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> MonthlyOrderSumaryArray = new ArrayList<HashMap<String, String>>();
    String msg, token, familyID;
    ProgressDialog progressDialog;
    String errormsg = "", InvoiceID = "", pastdueAmount = "", success = "";
    String ErrorMsgDisplay;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buymore_ordersummry);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);
        packageOrderSumaryArray = AppConfiguration.myCartPackageArray;
        MonthlyOrderSumaryArray = AppConfiguration.myCartMonthlyPlanArray;
        Log.e("packageOrderSumaryAr-", "" + packageOrderSumaryArray);
        Log.e("MonthlyOrderSumaryAr-", "" + MonthlyOrderSumaryArray);
        msg = AppConfiguration.totalPromoArray.get(0).get("Msg");
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreOrderSummary.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        BuyMoreOrderSummary.this.overridePendingTransition(0, 0);
        ((AnimationDrawable) selected_1.getBackground()).start();
        init();
        try {
            totalPromoVisibility(AppConfiguration.totalPromoArray);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        typeFace();
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

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        BackButton.setTypeface(regular);
        btn_place_order.setTypeface(regular);
    }

    private void init() {
        // TODO Auto-generated method stub
        View view = findViewById(R.id.schdl_top);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        includeAddBillAddress = (View) findViewById(R.id.includeAddBillAddress);

        txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);
        txtLblPromoCode = (TextView) findViewById(R.id.txtLblPromoCode);
        txtPromoCode = (TextView) findViewById(R.id.txtPromoCode);
        txtTotalTax = (TextView) findViewById(R.id.txtTotalTax);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        ll_inflate_orderSummary_package = (LinearLayout) findViewById(R.id.ll_inflate_orderSummary_package);
        ll_inflate_orderSummary_monthlyplan = (LinearLayout) findViewById(R.id.ll_inflate_orderSummary_monthlyplan);
        tv_payment_type = (TextView) findViewById(R.id.tv_payment_type);

        try {
            if (AppConfiguration.finalSelectedPaymentArray.size() != 0) {
                tv_payment_type.setText(AppConfiguration.cardType);
            } else {
                // select payment by add new payment
                // By Check
                if (AppConfiguration.finalSelectedPaymentArray.get(0).get("chkSaveACH").equalsIgnoreCase("True")) {
                    tv_payment_type.setText(AppConfiguration.finalSelectedPaymentArray.get(0).get("txtaccno") + " " + AppConfiguration.finalSelectedPaymentArray.get(0).get("txtaccno"));
                }
                // By Card
                if (AppConfiguration.finalSelectedPaymentArray.get(0).get("chkSaveACH").equalsIgnoreCase("False")) {
                    tv_payment_type.setText(AppConfiguration.finalSelectedPaymentArray.get(0).get("strCard"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_place_order = (Button) findViewById(R.id.btn_place_order);

        inflateMonthlyPlans(MonthlyOrderSumaryArray);
        inflatePackage(packageOrderSumaryArray);

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
                startActivity(i);
                finish();
            }
        });

        swimLsn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);
                Intent i = new Intent(mContext, BuyMoreSwimLession.class);
                startActivity(i);
                BuyMoreOrderSummary.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });

        retailStore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, BuyMoreRetailStore.class);
                startActivity(i);
                BuyMoreOrderSummary.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });

        otherPrograms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, ByMoreOtherPrograms.class);
                startActivity(i);
                BuyMoreOrderSummary.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

        btn_place_order.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isInternetPresent = Utility.isNetworkConnected(BuyMoreOrderSummary.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    if (includeAddBillAddress.getVisibility() == View.VISIBLE) {
                        if (rbShipDiffAddress.isChecked()) {
                            if (!edtAddressLine1.getText().toString().equals("")) {
                                if (!edtCity.getText().toString().equals("")) {
                                    if (!edtState.getText().toString().equals("")) {
                                        if (!edtZipCode.getText().toString().equals("")) {
                                            String strPaymentShippingAddress = edtAddressLine1.getText().toString()
                                                    + "," + edtAddressLine2.getText().toString()
                                                    + "," + edtCity.getText().toString()
                                                    + "," + edtState.getText().toString()
                                                    + "," + edtZipCode.getText().toString();
                                            AppConfiguration.finalSelectedPaymentArray.get(0).put("strPaymentShippingAddress", strPaymentShippingAddress);
                                            callConfirmPayment();
                                        } else {
                                            edtZipCode.setError("ZipCode cant be empty");
                                            edtZipCode.requestFocus();
                                        }
                                    } else {
                                        edtState.setError("State cant be empty");
                                        edtState.requestFocus();
                                    }
                                } else {
                                    edtCity.setError("City cant be empty");
                                    edtCity.requestFocus();
                                }
                            } else {
                                edtAddressLine1.setError("Address Line 1 cant be empty");
                                edtAddressLine1.requestFocus();
                            }
                        } else {
                            String strPaymentShippingAddress = edtAddressLine1.getText().toString()
                                    + "," + edtAddressLine2.getText().toString()
                                    + "," + edtCity.getText().toString()
                                    + "," + edtState.getText().toString()
                                    + "," + edtZipCode.getText().toString();
                            AppConfiguration.finalSelectedPaymentArray.get(0).put("strPaymentShippingAddress", strPaymentShippingAddress);
                            callConfirmPayment();
                        }
                    } else {
                        callConfirmPayment();
                    }
                }
            }
        });

        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void callConfirmPayment() {
        progressDialog = new ProgressDialog(BuyMoreOrderSummary.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConfirmSubmitPaymentAsyncTask confirmSubmitPaymentAsyncTask = new ConfirmSubmitPaymentAsyncTask(BuyMoreOrderSummary.this, AppConfiguration.finalSelectedPaymentArray.get(0));
                    String responseString = confirmSubmitPaymentAsyncTask.execute().get();
                    readAndParseConfirmSubmitPaymentJSON(responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    public void readAndParseConfirmSubmitPaymentJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            success = reader.getString("Success");
            if (success.equalsIgnoreCase("True")) {
                JSONArray jArr = reader.getJSONArray("ConformMsgDisplay");
                Log.d("jArr-", "" + jArr);
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject jObj = jArr.getJSONObject(i);
                    if (jObj.toString().contains("InvoiceID")) {
                        InvoiceID = jObj.getString("InvoiceID");
                        errormsg = jObj.getString("Msg");
                        String successMsg1 = jObj.getString("Msg1");
                        String successMsg2 = jObj.getString("Msg2");
                        ErrorMsgDisplay = jObj.getString("ErrorMsg");

                        AppConfiguration.successMsg1 = successMsg1;
                        AppConfiguration.successMsg2 = successMsg2;

                        Log.e("errormsg-01", errormsg);
                    }
                }
            } else {
                errormsg = reader.getString("Msg");
                Log.e("errormsg-01", errormsg);
            }
            try {
                Log.e("errormsg-0", errormsg);
                if (errormsg.toString().trim().equalsIgnoreCase("Not Submitted Successfully....")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            SelectInstructorDialog();
                        }
                    });
                } else {
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("Token", token);
                    param.put("InvoiceID", InvoiceID);
                    param.put("FamilyID", familyID); // insertedCheckDetailArray.add(param);

                    InvoiceDataDueBalanceAsyncTask invoiceDataDueBalanceAsyncTask = new InvoiceDataDueBalanceAsyncTask(BuyMoreOrderSummary.this, param);
                    String responseString = invoiceDataDueBalanceAsyncTask.execute().get();

                    try {
                        JSONObject reader1 = new JSONObject(responseString);
                        String read = reader1.getString("Success");
                        if (read.contains("True")) {
                            AppConfiguration.BasketID = reader1.getString("BasketID");
                            pastdueAmount = reader1.getString("pastdueAmount");
                        }
                        HashMap<String, String> param1 = new HashMap<String, String>();
                        param1.put("Token", token);
                        param1.put("BasketID", AppConfiguration.BasketID);
                        param1.put("InvoiceId", InvoiceID);
                        param1.put("pastdueAmount", pastdueAmount);

                        PayInvoiceLoadAsyncTask payInvoiceLoadAsyncTask = new PayInvoiceLoadAsyncTask(BuyMoreOrderSummary.this, param1);
                        String respnseStirng = payInvoiceLoadAsyncTask.execute().get();
                        try {
                            JSONObject reader2 = new JSONObject(respnseStirng);
                            success = reader2.getString("Success");
                            if (success.equalsIgnoreCase("True")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), AppConfiguration.successMsg1, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(BuyMoreOrderSummary.this, BuyMoreThankYou.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                errormsg = reader.getString("Msg");
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        SelectInstructorDialog();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    SelectInstructorDialog();
                                }
                            });
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                SelectInstructorDialog();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void totalPromoVisibility(ArrayList<HashMap<String, String>> promoArray) {
        if (msg != null) {
            if (msg.equals("No balace due.. ")) {
            }
        } else {
            for (int i = 0; i < promoArray.size(); i++) {
                View child = getLayoutInflater().inflate(R.layout.layout_products_order_summary, null, false);
                txtProductName = (TextView) child.findViewById(R.id.txtProductName);
                txtPrice = (TextView) child.findViewById(R.id.txtPrice);
                txtTax = (TextView) child.findViewById(R.id.txtTax);
                txtQuantity = (TextView) child.findViewById(R.id.txtQuantity);
                txtLblQuantity = (TextView) child.findViewById(R.id.txtLblQuantity);
                txtPackage = (TextView) child.findViewById(R.id.txtPackage);
                llProductSummary = (LinearLayout) findViewById(R.id.llProductSummary);
                rlQuantity = (RelativeLayout) child.findViewById(R.id.rlQuantity);
//06-07-2017 megha
                if (promoArray.get(i).get("Type").equalsIgnoreCase("lessons")) {
                    if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("8") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("9")) {
                        rlQuantity.setVisibility(View.VISIBLE);
                        txtQuantity.setText(promoArray.get(i).get("Qty"));
                    } else {
                        rlQuantity.setVisibility(View.GONE);
                    }

                } else if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("34") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("33")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("38") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("37")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("35") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("10")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("15") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("36")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("12") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("14")) {
                    rlQuantity.setVisibility(View.VISIBLE);
                    txtQuantity.setText(promoArray.get(i).get("Location"));
                    txtLblQuantity.setText("Location: ");
                } else if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("6")) {
                    rlQuantity.setVisibility(View.VISIBLE);
                    txtQuantity.setText(promoArray.get(i).get("StudentName"));
                    txtLblQuantity.setText("Student: ");
                } else {
                    rlQuantity.setVisibility(View.VISIBLE);
                    txtQuantity.setText(promoArray.get(i).get("Qty"));
                }

                //this condition is to hide and show the billing address layout
                if (promoArray.get(i).get("ItemTypeID").equals("40")) {
                    includeAddBillAddress.setVisibility(View.VISIBLE);
                }
                if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("8") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("9")) {
                    txtProductName.setText(promoArray.get(i).get("Item") + " Night");
                } else {
                    if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("4") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("7")
                            || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("37") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("36")
                            || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("35") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("34")
                            || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("33") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("3")) {
                        txtProductName.setText(promoArray.get(i).get("Item"));
                    } else {
                        txtProductName.setText(promoArray.get(i).get("Item") + " " + promoArray.get(i).get("Type"));
                    }
                }
                if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("40") && !promoArray.get(i).get("Description").equalsIgnoreCase("")) {
                    String[] firstString = promoArray.get(i).get("Description").split(",");
                    StringBuilder builder = new StringBuilder();
                    String[] colorString = firstString[1].split(":");
                    builder.append("Color: " + colorString[0]);
                    String[] sizeString = firstString[2].split(":");
                    builder.append("\nSize: " + sizeString[0]);
                    txtPackage.setText(builder.toString());

                } else if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("37") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("36")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("35") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("12")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("10") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("34")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("33") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("14")
                        || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("15")) {
                    txtPackage.setText("Student: " + promoArray.get(i).get("StudentName"));

                } else if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("3") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("4")) {
                    txtPackage.setText("Location: " + promoArray.get(i).get("Location"));

                } else {
                    if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("8") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("9")
                            || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("7")) {
                        txtPackage.setText(promoArray.get(i).get("Package"));
                    } else if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("13") || promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("11")) {
                        txtPackage.setVisibility(View.GONE);
                    } else {
//                    24-05-2017 megha
                        if (promoArray.get(i).get("ItemTypeID").equalsIgnoreCase("1")) {
                            int packagestr = Integer.parseInt(promoArray.get(i).get("Package"));
                            int qtystr = Integer.parseInt(promoArray.get(i).get("Qty"));
                            if (qtystr >= packagestr) {
                                txtPackage.setText(promoArray.get(i).get("Qty") + " Lesson Package");
                            } else {
                                txtPackage.setText(promoArray.get(i).get("Package") + " Lesson Package");
                            }
                        } else {
                            txtPackage.setText(promoArray.get(i).get("Package") + " Lesson Package");
                        }
                    }
                }
                txtPrice.setText(promoArray.get(i).get("Subtotal"));
                txtTax.setText(promoArray.get(i).get("Tax"));
                llProductSummary.addView(child);
            }
            txtSubTotal.setText(promoArray.get(0).get("SubTotalSum"));
            txtTotalTax.setText(promoArray.get(0).get("TaxSum"));
            txtTotal.setText(promoArray.get(0).get("Total"));

            if (promoArray.get(0).get("promotext").toString().equals("")) {
                txtLblPromoCode.setVisibility(View.GONE);
                txtPromoCode.setVisibility(View.GONE);
            } else {
                txtLblPromoCode.setVisibility(View.VISIBLE);
                txtPromoCode.setVisibility(View.VISIBLE);
                txtPromoCode.setText(promoArray.get(0).get("promotext").toString());
            }
            if (includeAddBillAddress.getVisibility() == View.VISIBLE) {
                rbShipBillAddress = (RadioButton) includeAddBillAddress.findViewById(R.id.rbShipBillAddress);
                rbShipDiffAddress = (RadioButton) includeAddBillAddress.findViewById(R.id.rbShipDiffAddress);
                edtAddressLine1 = (EditText) includeAddBillAddress.findViewById(R.id.edtAddressLine1);
                edtAddressLine2 = (EditText) includeAddBillAddress.findViewById(R.id.edtAddressLine2);
                edtCity = (EditText) includeAddBillAddress.findViewById(R.id.edtCity);
                edtState = (EditText) includeAddBillAddress.findViewById(R.id.edtState);
                edtZipCode = (EditText) includeAddBillAddress.findViewById(R.id.edtZipCode);

                rbShipDiffAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            edtAddressLine1.setText("");
                            edtAddressLine2.setText("");
                            edtCity.setText("");
                            edtState.setText("");
                            edtZipCode.setText("");

                            edtAddressLine1.setEnabled(true);
                            edtAddressLine2.setEnabled(true);
                            edtCity.setEnabled(true);
                            edtState.setEnabled(true);
                            edtZipCode.setEnabled(true);
                        }
                    }
                });
                rbShipBillAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (AppConfiguration.finalSelectedPaymentArray.size() > 0) {
                                String billingAddress = AppConfiguration.finalSelectedPaymentArray.get(0).get("strPaymentShippingAddress");
                                String[] address = billingAddress.split("\\s*,\\s*");
                                edtAddressLine1.setText(address[0]);
                                edtAddressLine2.setText(address[1]);
                                edtCity.setText(address[2]);
                                edtState.setText(address[3]);
                                edtZipCode.setText(address[4]);

                                edtAddressLine1.setEnabled(false);
                                edtAddressLine2.setEnabled(false);
                                edtCity.setEnabled(false);
                                edtState.setEnabled(false);
                                edtZipCode.setEnabled(false);
                            }
                        }
                    }
                });
                rbShipBillAddress.setChecked(true);
            }
        }
    }

    public void inflatePackage(final ArrayList<HashMap<String, String>> packageOrderSumaryArray) {
        int totalQuantity = 0;
        for (int i = 0; i < packageOrderSumaryArray.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.buymore_oredersummary_item, null);
            final TextView tv_view_cart_item_type = (TextView) view.findViewById(R.id.tv_view_cart_item_type);
            final TextView tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
            final TextView tv_view_cart_item_price = (TextView) view.findViewById(R.id.tv_view_cart_item_price);
            final TextView tv_view_cart_item_qty = (TextView) view.findViewById(R.id.tv_view_cart_item_qty);
            final TextView tv_package_lesson = (TextView) view.findViewById(R.id.tv_package_lesson);
            tv_package_lesson.setText(packageOrderSumaryArray.get(i).get("Item") + " " + packageOrderSumaryArray.get(i).get("Type"));
            tv_view_cart_item_type.setVisibility(View.INVISIBLE);
//            tv_quantity.setText("Qty:" + "1");
            tv_view_cart_item_price.setText(packageOrderSumaryArray.get(i).get("Price"));
            tv_view_cart_item_qty.setVisibility(View.INVISIBLE);
            totalQuantity = totalQuantity + Integer.parseInt(packageOrderSumaryArray.get(i).get("Package"));
            Log.e("totalQty-0", "" + totalQuantity);
            String totalQty = String.valueOf(totalQuantity);

            AppConfiguration.totalQty = totalQty;
            Log.e("totalQty-S", "" + AppConfiguration.totalQty);
            ll_inflate_orderSummary_package.addView(view);
            ll_inflate_orderSummary_package.setPadding(0, 2, 0, 2);
        }
    }

    public void inflateMonthlyPlans(final ArrayList<HashMap<String, String>> MonthlyOrderSumaryArray) {
        for (int i = 0; i < MonthlyOrderSumaryArray.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.buymore_oredersummary_item, null);
            final TextView tv_view_cart_item_type = (TextView) view.findViewById(R.id.tv_view_cart_item_type);
            final TextView tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
            final TextView tv_view_cart_item_price = (TextView) view.findViewById(R.id.tv_view_cart_item_price);
            final TextView tv_view_cart_item_qty = (TextView) view.findViewById(R.id.tv_view_cart_item_qty);
            final TextView tv_package_lesson = (TextView) view.findViewById(R.id.tv_package_lesson);
            tv_package_lesson.setVisibility(View.INVISIBLE);
            tv_view_cart_item_type.setText(AppConfiguration.lessionType);
//            tv_quantity.setText("Qty:" + "1");
            tv_view_cart_item_price.setText(MonthlyOrderSumaryArray.get(i).get("Price"));
//            tv_view_cart_item_qty.setText(MonthlyOrderSumaryArray.get(i).get("Quantity") + " Lesson Package");

            ll_inflate_orderSummary_monthlyplan.addView(view);
            ll_inflate_orderSummary_monthlyplan.setPadding(0, 2, 0, 2);

        }
    }

    public void SelectInstructorDialog() {
        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout_buymore, null);
        final PopupWindow popWindow = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Alert");

        TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
        String[] ErrorMsgDisplaysplit = ErrorMsgDisplay.split("\\<");
        tv_description.setText(ErrorMsgDisplaysplit[0]);
        tv_description.setTextColor(Color.BLACK);
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "Roboto_Light.ttf");
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        tv_description.setTypeface(face);
        tv_appname.setTypeface(regular);
        TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
        imgv_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
            }
        });
        CardView button_ok = (CardView) layout.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                Intent i = new Intent(BuyMoreOrderSummary.this, BuyMoreSelectPaymentMethod.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
        startActivity(i);
        finish();
    }
}





