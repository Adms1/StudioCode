package com.waterworks.sheduling;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.ReceivePayment;
import com.waterworks.SwimCompititionTrophyRoomResultsAcitivity;
import com.waterworks.asyncTasks.InvoiceDataDueBalanceAsyncTask;
import com.waterworks.asyncTasks.PayInvoiceLoadAsyncTask;
import com.waterworks.asyncTasks.cardDetailAsyncTask;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BuyMoreSelectPaymentMethod extends Activity {

    //	ButtonRectangle btn_continue;
    LinearLayout swimLsn, retailStore, otherPrograms, ll_select_payment;
    TextView txt_1, txt_2, txt_3, tv_add_new_payment_method2;
    View selected_1, selected_2, selected_3;
    Context mContext = this;
    CardView tv_add_new_payment_method, btn_continue;
    String token, familyID, pastdueAmount = "", msg, expireDate, CustomerId, pmtId;
    Button BackButton, btn_viewCart, btn_continue2;//tv_add_new_payment_method2
    ListView lv_card_detail;
    CardDetailAsyncTask cardDetailAsyncTask = null;

    // String DOMAIN = AppConfiguration.DOMAIN;
    String cred_exp = "", card_detail = "", check_detail = "", address = "";
    ArrayList<HashMap<String, String>> myCartArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> MonthlyArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> editCardArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> selectedCardDetailArray = new ArrayList<HashMap<String, String>>();
    //    ArrayList<HashMap<String, String>> finalSelectedPaymentArray = new ArrayList<HashMap<String, String>>();
//    public static ArrayList<HashMap<String, String>> insertedCheckDetailArray = new ArrayList<HashMap<String, String>>();
//    public static ArrayList<HashMap<String, String>> insertedCardDetailArray = new ArrayList<HashMap<String, String>>();
    // CardDetailArrayAdapter cardDetailArrAdp;
    String insertedCardDetails, insertedCheckDetails, checkShippAdd, cardShippAdd,
            SelectedCheckDetail, InvoiceID = "", errormsg = "", PaymenPaymentBillingAddresstCheck;
    Boolean isInternetPresent = false;

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
//        btn_continue.setTypeface(regular);
        BackButton.setTypeface(regular);
        btn_continue2.setTypeface(regular);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buymore_select_payment);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        isInternetPresent = Utility.isNetworkConnected(BuyMoreSelectPaymentMethod.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            cardDetailAsyncTask = new CardDetailAsyncTask();
            cardDetailAsyncTask.execute();
        }
//        cardDetailArray = AppConfiguration.cardDetailArray;
        /*Log.e("insertedCheckDetailArray--25-", "" + cardDetailArrayTemp);
        insertedCardDetailArray = AppConfiguration.insertedCardDetailArray;
        insertedCheckDetailArray = AppConfiguration.insertedCheckDetailArray;
        Log.e("insertedCheckDetailArray--12#-", "" + insertedCheckDetailArray);

        try {
            if (insertedCardDetailArray.size() != 0) {
                Log.d("insertedCardDetailArray--12#-", "" + insertedCardDetailArray);
                insertedCardDetails = insertedCardDetailArray.get(0).get("txtfname") + "|"
                        + insertedCardDetailArray.get(0).get("txtlname") + "|"
                        + insertedCardDetailArray.get(0).get("CardNo") + "|"
                        + insertedCardDetailArray.get(0).get("ddlctype") + "|"
                        + insertedCardDetailArray.get(0).get("txtcvv") + "|"
                        + insertedCardDetailArray.get(0).get("ddlexp1") + "|"
                        + insertedCardDetailArray.get(0).get("ddlexp2") + "|"
                        + insertedCardDetailArray.get(0).get("txtaddress1") + "|"
                        + insertedCardDetailArray.get(0).get("txtaddress2") + "|"
                        + insertedCardDetailArray.get(0).get("txtcity") + "|"
                        + insertedCardDetailArray.get(0).get("txtstate") + "|"
                        + insertedCardDetailArray.get(0).get("chkzip");
                card_detail = insertedCardDetails;

                address = "";
                cred_exp = insertedCardDetailArray.get(0).get("ddlexp1")
                        + insertedCardDetailArray.get(0).get("ddlexp2");

                cardShippAdd = insertedCardDetailArray.get(0).get("txtaddress1") + "|"
                        + insertedCardDetailArray.get(0).get("txtaddress2") + "|"
                        + insertedCardDetailArray.get(0).get("txtcity") + "|"
                        + insertedCardDetailArray.get(0).get("txtstate") + "|"
                        + insertedCardDetailArray.get(0).get("chkzip");

                address = cardShippAdd;

                try {
                    new paymentConfirmtAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }*/

        /*try {
            if (insertedCheckDetailArray.size() != 0) {
                Log.d("insertedCheckDetailArray--12#-", "" + insertedCheckDetailArray);
                insertedCheckDetails = insertedCheckDetailArray.get(0).get("txtnameofcheck").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("txtrouting").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("txtaccno").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("txtbankname").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("ddlAccountType").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("chkaddressline1").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("chkaddressline2").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("chkcity").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("chkstate").toString().trim() + "|"
                        + insertedCheckDetailArray.get(0).get("chkzip").toString().trim();
                Log.d("check_detail--12#-", "" + check_detail);
                checkShippAdd = insertedCheckDetailArray.get(0).get("chkaddressline1") + "|"
                        + insertedCheckDetailArray.get(0).get("chkaddressline2") + "|"
                        + insertedCheckDetailArray.get(0).get("chkcity") + "|"
                        + insertedCheckDetailArray.get(0).get("chkstate") + "|"
                        + insertedCheckDetailArray.get(0).get("chkzip");
                address = checkShippAdd;

                try {
                    new paymentConfirmtAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }*/

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        BuyMoreSelectPaymentMethod.this.overridePendingTransition(0, 0);
        ((AnimationDrawable) selected_1.getBackground()).start();
        init();
        typeFace();
    }

    //    public void InitialRequests() {
//    }
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
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
//        inflateSelectPaymentMethod(cardDetailArray);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//        inflateSelectPaymentMethod(cardDetailArray);
    }

    @SuppressWarnings("unchecked")
    private void init() {
        // TODO Auto-generated method stub
        View view = findViewById(R.id.schdl_top);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        ll_select_payment = (LinearLayout) findViewById(R.id.ll_select_payment);
        tv_add_new_payment_method = (CardView) findViewById(R.id.tv_add_new_payment_method);
        tv_add_new_payment_method2 = (TextView) findViewById(R.id.tv_add_new_payment_method2);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        btn_continue = (CardView) findViewById(R.id.btn_continue);
        btn_continue2 = (Button) findViewById(R.id.btn_continue2);

        btn_continue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppConfiguration.selectedCardDetailArray = selectedCardDetailArray;

                if (AppConfiguration.selectedCardDetailArray.size() != 0) {

                    try {

//                        || errormsg.toString().trim().equalsIgnoreCase("Not Submited SuccessFully....")
                        if (AppConfiguration.selectedCardDetailArray.get(0).get("wu_PmtID").equals("")) {
                            Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_SHORT).show();
                        } else {

                            Intent i = new Intent(getApplicationContext(), BuyMoreOrderSummary.class);
                            startActivity(i);
                            finish();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please select payment method", Toast.LENGTH_SHORT)
                            .show();


                }
            }
        });

        btn_continue2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppConfiguration.selectedCardDetailArray = selectedCardDetailArray;

                if (AppConfiguration.selectedCardDetailArray.size() != 0) {

                    try {

//                        || errormsg.toString().trim().equalsIgnoreCase("Not Submited SuccessFully....")
                        if (AppConfiguration.selectedCardDetailArray.get(0).get("wu_PmtID").equals("")) {
                            Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_SHORT).show();
                        } else {

                            Intent i = new Intent(getApplicationContext(), BuyMoreOrderSummary.class);
                            startActivity(i);
                            finish();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please select payment method", Toast.LENGTH_SHORT)
                            .show();


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
        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreSelectPaymentMethod.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
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
                BuyMoreSelectPaymentMethod.this.overridePendingTransition(0, 0);
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
                BuyMoreSelectPaymentMethod.this.overridePendingTransition(0, 0);
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
                BuyMoreSelectPaymentMethod.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

        tv_add_new_payment_method.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BuyMoreAddNewPaymentMethod.class);
                startActivity(i);
            }
        });

        tv_add_new_payment_method2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BuyMoreAddNewPaymentMethod.class);
                startActivity(i);
            }
        });
        /*if (insertedCardDetailArray.size() == 0 && insertedCheckDetailArray.size() == 0) {
            cardDetailArray = AppConfiguration.cardDetailArray;
            inflateSelectPaymentMethod(cardDetailArray);


        } else {
            if (insertedCardDetailArray.size() != 0) {
                cardDetailArray = AppConfiguration.cardDetailArray;
                Log.e("cardDetailArray--01", "" + cardDetailArray);

                cardDetailArray.addAll(insertedCardDetailArray);
                Log.e("cardDetailArray--00", "" + cardDetailArray);
                inflateSelectPaymentMethod(cardDetailArray);
            }

            if (insertedCheckDetailArray.size() != 0) {
                cardDetailArray = AppConfiguration.cardDetailArray;
                Log.e("cardDetailArray--1", "" + cardDetailArray);
                cardDetailArray.addAll(insertedCheckDetailArray);
                Log.e("cardDetailArray--0", "" + cardDetailArray);
                inflateSelectPaymentMethod(cardDetailArray);
            }
        }*/

//        inflateSelectPaymentMethod(cardDetailArray);
    }

    public void inflateSelectPaymentMethod(final ArrayList<HashMap<String, String>> cardDetailArray) {
        Log.e("cardDetailArray-tost", "" + cardDetailArray);
        ArrayList<String> tempPaymentId = new ArrayList<String>();
        //
        for (int i = 0; i < cardDetailArray.size(); i++) {
            final int pos = i;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.buymore_card_details_item, null);
            final TextView tv_cardType = (TextView) view.findViewById(R.id.tv_cardType);
            final TextView tv_card_holder_name = (TextView) view.findViewById(R.id.tv_card_holder_name);
            final TextView tv_expire_date = (TextView) view.findViewById(R.id.tv_expire_date);
            final TextView tv_edit_card = (TextView) view.findViewById(R.id.tv_edit_card);
            final TextView textView_wu_PayType = (TextView) view.findViewById(R.id.textView_wu_PayType);
            final TextView textView_wu_CardAccNumber = (TextView) view.findViewById(R.id.textView_wu_CardAccNumber);
            View v = (View) view.findViewById(R.id.divider);
            final TextView textView_cardNumber = (TextView) view.findViewById(R.id.textView_cardNumber);
            final TextView textView_bankName = (TextView) view.findViewById(R.id.textView_bankName);
            final TextView textView_bankRoutingNo = (TextView) view.findViewById(R.id.textView_bankRoutingNo);
            final TextView textViewPayTypeId = (TextView) view.findViewById(R.id.textViewPayTypeId);
            final TextView textView_paymentId = (TextView) view.findViewById(R.id.textView_paymentId);
            final CheckBox chkbx = (CheckBox) view.findViewById(R.id.chkbx);
            chkbx.setTag(cardDetailArray.get(i).get("wu_PmtID"));
            /*if(!cardDetailArray.get(i).get("CardNo").equalsIgnoreCase("")){

			}*/
// card detail..............
            if (cardDetailArray.get(i).containsKey("wu_PayTypeID")) {
                if (cardDetailArray.get(i).get("wu_PayTypeID").equalsIgnoreCase("0") && cardDetailArray.get(i).get("wu_PayType").equalsIgnoreCase("Card")) {

                    tv_cardType.setVisibility(View.INVISIBLE);
                    tv_card_holder_name.setVisibility(View.VISIBLE);
                    textView_bankName.setVisibility(View.GONE);
                    tv_expire_date.setVisibility(View.VISIBLE);
                    tv_cardType.setVisibility(View.VISIBLE);
                    textView_bankRoutingNo.setVisibility(View.GONE);

                    if (!cardDetailArray.get(i).get("wu_CardType").equalsIgnoreCase("")) {
                        Log.e("WU_CARD_TYPE_2", "" + cardDetailArray.get(i).get("wu_CardType"));
                        tv_cardType.setText(cardDetailArray.get(i).get("wu_CardType") + " " + cardDetailArray.get(i).get("wu_CardAccNumber").toString().trim().substring(Math.max(0, cardDetailArray.get(i).get("wu_CardAccNumber").toString().trim().length() - 9)));


                    } else {
                        Log.e("wu_CardType-1", "" + cardDetailArray.get(i).get("wu_CardType"));
                        tv_cardType.setText(" " + cardDetailArray.get(i).get("wu_CardAccNumber"));
                    }


                    tv_card_holder_name.setText(cardDetailArray.get(i).get("wu_ClientName"));
                    //Compare date...
                    try {
                        expireDate = cardDetailArray.get(i).get("wu_ExpDate").toString().trim().replace("Exp Date :", "");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
                        StringBuilder srb = new StringBuilder(expireDate);

                        srb.insert(3, "20");
                        expireDate = srb.toString();
                        Log.e("expireDate--", "" + expireDate);
                        Date date1 = dateFormat.parse(expireDate);

                        if (date1.compareTo(new Date()) < 0) {
                            tv_expire_date.setText("Expires" + " " + cardDetailArray.get(i).get("wu_ExpDate").toString().trim().replace("Exp Date :", ""));
                            tv_expire_date.setTextColor(Color.RED);
                            tv_edit_card.setVisibility(View.VISIBLE);
                        } else {
                            tv_expire_date.setText("Expires" + " " + cardDetailArray.get(i).get("wu_ExpDate").toString().trim().replace("Exp Date :", ""));
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


//                    tv_expire_date.setText("Expires" + " " + cardDetailArray.get(i).get("wu_ExpDate"));
                    tempPaymentId.add(cardDetailArray.get(i).get("wu_PmtID"));
                    Log.e("tempPaymentId-", "" + tempPaymentId);
                    if (AppConfiguration.addNewCardPayment == true) {
                        for (int j = 0; j < tempPaymentId.size(); j++) {
                            if (tempPaymentId.get(j).contains(cardDetailArray.get(i).get("wu_PmtID"))) {
                                chkbx.setButtonDrawable(R.drawable.custom_checkbox_check_orange);
                            }
                        }
                    }
                } else {
//                    check details........

                    Log.e("cardDetailArray-test", "" + cardDetailArray.get(i));
                    Log.e("cardDetailArray-tes1", "" + cardDetailArray);
                    tv_card_holder_name.setVisibility(View.GONE);
                    tv_expire_date.setVisibility(View.GONE);
                    tv_cardType.setVisibility(View.VISIBLE);
                    textView_bankRoutingNo.setVisibility(View.VISIBLE);
                    textView_bankName.setVisibility(View.VISIBLE);
                    textView_bankRoutingNo.setVisibility(View.INVISIBLE);
                    tv_cardType.setText("Checking - " + cardDetailArray.get(i).get("wu_CardAccNumber").toString().trim().replace("*****", ""));
//                    textView_bankRoutingNo.setText(cardDetailArray.get(i).get("wu_CardAccNumber").toString().trim().replace("*****",""));
                    textView_bankName.setText(cardDetailArray.get(i).get("wu_BankName"));
                    Log.e("tempPaymentId-", "" + tempPaymentId);
                    if (AppConfiguration.addNewCheckPayment == true) {
                        for (int j = 0; j < tempPaymentId.size(); j++) {
                            if (tempPaymentId.get(j).contains(cardDetailArray.get(i).get("wu_PmtID"))) {
                                chkbx.setButtonDrawable(R.drawable.custom_checkbox_check_orange);
                                chkbx.setChecked(true);
                            }
                        }
                    }
                }
//                cardDetailArray.clear();
            }

            textViewPayTypeId.setText(cardDetailArray.get(i).get("wu_PayTypeID"));
            textView_paymentId.setText(cardDetailArray.get(i).get("wu_PmtID"));
            textView_wu_CardAccNumber.setText(cardDetailArray.get(i).get("wu_CardAccNumber"));
            textView_wu_PayType.setText(cardDetailArray.get(i).get("wu_PayType"));
            tv_edit_card.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        CustomerId = cardDetailArray.get(pos).get("wu_CustomerID");
                        pmtId = cardDetailArray.get(pos).get("wu_PmtID");
                        Log.e("CustomerId--", "" + CustomerId);
                        Log.e("pmtId--", "" + pmtId);

                        new CardDetailsByPmtIdAsyncTask().execute();
//                        Intent i=new Intent(BuyMoreSelectPaymentMethod.this,BuyMoreAddNewPaymentMethod.class);
//                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
            chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            /*chkbx.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {*/
                    if (chkbx.isChecked()) {

                        chkbx.setButtonDrawable(R.drawable.custom_checkbox_check_orange);
                        AppConfiguration.cardType = tv_cardType.getText().toString();
                        selectedCardDetailArray.clear();
                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        hashmap.put("wu_PayType", textView_wu_PayType.getText().toString());
                        hashmap.put("wu_CardAccNumber", textView_wu_CardAccNumber.getText().toString());
                        hashmap.put("wu_PayTypeID", textViewPayTypeId.getText().toString());
                        hashmap.put("wu_PmtID", textView_paymentId.getText().toString());
                        if (!selectedCardDetailArray.contains(hashmap)) {
                            selectedCardDetailArray.add(hashmap);
                            AppConfiguration.selectedCardDetailArray = selectedCardDetailArray;
                            Log.d("AppConfiguration.selectedCardDetailArray---&", "" + AppConfiguration.selectedCardDetailArray);

                            try {
                                new paymentConfirmtAsyncTask().execute();
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                        checkedValue = textView_paymentId.getText().toString();

                        getChild();

                    } else {
//                        selectedCardDetailArray.clear();
//                        Log.d("AppConfiguration.selectedCardDetailArray---&", "Clear");
                        chkbx.setButtonDrawable(R.drawable.custom_checkbox_uncheck);
                    }
                }
            });

            ll_select_payment.addView(view);

        }
    }

    /*View

    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SwimCompititionTrophyRoomResultsAcitivity.class);
            intent.putExtra("studentID", v.getTag().toString());
            startActivity(intent);
        }
    }*/;

    String checkedValue = "";

    public void getChild() {
        if (ll_select_payment.getChildCount() > 0) {
            for (int i = 0; i < ll_select_payment.getChildCount(); i++) {
                View view = ll_select_payment.getChildAt(i);
                LinearLayout superLay = (LinearLayout) view;
                View view2 = superLay.getChildAt(0);
//                LinearLayout lay_card_type = (LinearLayout) view2;
//                View view3 = lay_card_type.getChildAt(0);
                LinearLayout lay_check = (LinearLayout) view2;
                LinearLayout view4 = (LinearLayout) lay_check.getChildAt(1);
                View view5 = view4.getChildAt(0);
                if (view5 instanceof CheckBox) {
                    CheckBox rr = (CheckBox) view5;
                    if (!rr.getTag().toString().equalsIgnoreCase(checkedValue)) {
                        rr.setChecked(false);
                    }
                }
            }
        }
    }

    // get payment method

    class CardDetailAsyncTask extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

//            cardDetailArray.clear();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = loadCardDetailListReal();

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            readAndParseJSON(result);
            if (AppConfiguration.cardDetailArray.size() == 0) {
                tv_add_new_payment_method.setVisibility(View.VISIBLE);
                btn_continue.setVisibility(View.VISIBLE);
                btn_continue2.setVisibility(View.GONE);
                tv_add_new_payment_method2.setVisibility(View.GONE);

            } else {
                tv_add_new_payment_method.setVisibility(View.VISIBLE);
                btn_continue.setVisibility(View.VISIBLE);
                btn_continue2.setVisibility(View.GONE);
                tv_add_new_payment_method2.setVisibility(View.GONE);
//                contitional coding to put continue button above add new payment button : removed by client
                /*tv_add_new_payment_method.setVisibility(View.GONE);
                btn_continue.setVisibility(View.GONE);
                btn_continue2.setVisibility(View.VISIBLE);
                tv_add_new_payment_method2.setVisibility(View.VISIBLE);*/
            }
            inflateSelectPaymentMethod(AppConfiguration.cardDetailArray);
            if (pd != null && pd.isShowing())
                pd.dismiss();
            AppConfiguration.myCartPackageArray = myCartArray;
            AppConfiguration.myCartMonthlyPlanArray = MonthlyArray;
            Log.e("myCartArray---##", "" + AppConfiguration.myCartPackageArray);
            /*if (cardDetailArray.size() == 0 && myCartArray.size() == 0 && MonthlyArray.size() == 0) {
                Toast.makeText(getApplicationContext(), "Your cart is empty cotinue shopping", Toast.LENGTH_SHORT).show();
                // finish();
            }*/

//            }

        }
    }

    public String loadCardDetailListReal() {

        HashMap<String, String> param = new HashMap<String, String>();

//        param.put("Basketid", AppConfiguration.Basketid);
        param.put("Basketid", AppConfiguration.BasketID);
        param.put("Token", token);
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.payDGetACHCardDetail, param);
        // Log.i("responseString", responseString);
        return responseString;
    }

    public void readAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");
            AppConfiguration.cardDetailArray.clear();
            if (successStr.equalsIgnoreCase("True")) {
                // Log.i("successStr--1-", successStr);
                JSONArray jsonMainNode = reader.optJSONArray("ACHList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("wu_ClientName", jsonChildNode.getString("wu_ClientName"));
                    hashmap.put("wu_CardAccNumber", jsonChildNode.getString("wu_CardAccNumber"));
                    hashmap.put("wu_PayType", jsonChildNode.getString("wu_PayType"));
                    hashmap.put("wu_ExpDate", jsonChildNode.getString("wu_ExpDate"));
                    hashmap.put("wu_BankName", jsonChildNode.getString("wu_BankName"));
                    hashmap.put("wu_BankRtngNum", jsonChildNode.getString("wu_BankRtngNum"));
                    hashmap.put("wu_PayTypeID", jsonChildNode.getString("wu_PayTypeID"));
                    hashmap.put("wu_PmtID", jsonChildNode.getString("wu_PmtID"));
                    hashmap.put("wu_CustomerID", jsonChildNode.getString("wu_CustomerID"));
                    hashmap.put("wu_CardType", jsonChildNode.getString("wu_CardType"));

                    AppConfiguration.cardDetailArray.add(hashmap);

                }
            } else if (successStr.equalsIgnoreCase("False")) {
                msg = reader.getString("Msg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Payment confirmation-----------

    class paymentConfirmtAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
//            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            loadpaymentConfirm();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if (pd != null && pd.isShowing()) {
            try {

                pd.dismiss();

            } catch (Exception e) {
                // TODO: handle exception
            }
            //moved to order summary
                /*try {
                    new ConfirmSubmitPaymentAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }*/

//            }

        }
    }

    public void loadpaymentConfirm() {
        try {

            HashMap<String, String> param = new HashMap<String, String>();
            HashMap<String, String> param1 = new HashMap<String, String>();

//            param1.put("Token", token);
//            param1.put("BasketID", Basketid);
//            param1.put("wu_recurring", "0");
//            param1.put("pmtid", "0");
//            param1.put("strType", "");
//            param1.put("strCard", "");
//            param1.put("ShippingAddress", "");
//            param1.put("chkagree", "True");
//            param1.put("ChkAddShippingAddress", "False");
//            param1.put("RBPaymentType", "0");
//            param1.put("chkSaveACH", "True");
//            param1.put("chkSaveCard", "False");
//            param1.put("strCheckDtl", "ankit|45885|5558336|sycyc|dgfgg|dgfgg. shfc|fhggyy|duhgh|AS|556685");
//            param1.put("strCreditDtl", "");

            param.put("Token", token);
            param.put("BasketID", AppConfiguration.BasketID);
            param.put("wu_recurring", "0");

            param.put("ShippingAddress", "");
            param.put("chkagree", "True");

            /*if (insertedCardDetailArray.size() != 0) {
                param.put("RBPaymentType", "0");
                param.put("pmtid", "");
                param.put("strType", "");
                param.put("strCard", "");
                param.put("chkSaveACH", "False");
                param.put("chkSaveCard", "True");
                param.put("strCheckDtl", "");
                param.put("strCreditDtl", insertedCardDetails);
                param.put("ChkAddShippingAddress", "");
            }
            if (insertedCheckDetailArray.size() != 0) {
                Log.i("insertedCheckDetails--$$", "" + insertedCheckDetails);
                param.put("RBPaymentType", "0");
                param.put("pmtid", "");
                param.put("strType", "");
                param.put("strCard", "");
                param.put("chkSaveACH", "True");
                param.put("chkSaveCard", "False");
                param.put("strCheckDtl", insertedCheckDetails);
                param.put("strCreditDtl", "");
                param.put("ChkAddShippingAddress", "");
            }*/
            // else {
            selectedCardDetailArray = AppConfiguration.selectedCardDetailArray;
            if (selectedCardDetailArray.size() != 0) {
                Log.i("selectedCardDetailArray--$$", "" + selectedCardDetailArray);
                SelectedCheckDetail = selectedCardDetailArray.get(0).get("wu_ClientName") + "|"
                        + selectedCardDetailArray.get(0).get("wu_CardAccNumber") + "|"
                        + selectedCardDetailArray.get(0).get("wu_ExpDate") + "|"
                        + selectedCardDetailArray.get(0).get("wu_BankName") + "|"
                        + selectedCardDetailArray.get(0).get("wu_BankRtngNum");

                card_detail = SelectedCheckDetail;
                if (selectedCardDetailArray.get(0).get("wu_PayType").toString().equalsIgnoreCase("ACH")) {
                    param.put("RBPaymentType", "");
                    param.put("pmtid", selectedCardDetailArray.get(0).get("wu_PmtID").toString().trim());
                    param.put("strType", selectedCardDetailArray.get(0).get("wu_PayTypeID").toString().trim());
                    param.put("strCard", selectedCardDetailArray.get(0).get("wu_CardAccNumber").toString().trim());
                    param.put("chkSaveACH", "");
                    param.put("chkSaveCard", "");
                    param.put("strCheckDtl", "");
                    param.put("strCreditDtl", "");
                    param.put("ChkAddShippingAddress", "False");
                } else {
                    param.put("RBPaymentType", "");
                    param.put("pmtid", selectedCardDetailArray.get(0).get("wu_PmtID").toString().trim());
                    param.put("strType", selectedCardDetailArray.get(0).get("wu_PayTypeID").toString().trim());
                    param.put("strCard", selectedCardDetailArray.get(0).get("wu_CardAccNumber").toString().trim());
                    param.put("chkSaveACH", "");
                    param.put("chkSaveCard", "");
                    param.put("strCheckDtl", "");
                    param.put("strCreditDtl", "");
                    param.put("ChkAddShippingAddress", "False");
                }
            }

//            finalSelectedPaymentArray.add(param);

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.confirm_pay, param);
//            AppConfiguration.finalSelectedPaymentArray = finalSelectedPaymentArray;
//            Log.i("AppConfiguration.finalSelectedPaymentArray--", "" + AppConfiguration.finalSelectedPaymentArray);
            readpaymentConfirmAndParseJSON(responseString);
            Log.i("responseString--", responseString);
            CreateConfirmSubmitPaymentArray();//will be used in order summary page
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void readpaymentConfirmAndParseJSON(String in) {

        try {

            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if (success.equalsIgnoreCase("True")) {
                JSONArray jArr = reader.getJSONArray("ConformPayment");
                Log.d("jArr-", "" + jArr);
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject jObj = jArr.getJSONObject(i);
                    check_detail = jObj.getString("PaymentCheck");
                    card_detail = jObj.getString("PaymentCredit");
                    String PaymentShippingAddress = jObj.getString("PaymentShippingAddress");
                    PaymenPaymentBillingAddresstCheck = jObj.getString("PaymentBillingAddress");
                    Log.d("Response--data--", check_detail + "\n" + card_detail + "\n" + PaymentShippingAddress + "\n"
                            + PaymenPaymentBillingAddresstCheck);
                }
            } else {
                errormsg = reader.getString("Msg");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void CreateConfirmSubmitPaymentArray() {
        AppConfiguration.finalSelectedPaymentArray.clear();
        HashMap<String, String> param1 = new HashMap<String, String>();
        param1.put("Token", token);
        param1.put("BasketID", AppConfiguration.BasketID);
        param1.put("pastduepaymentFlag", "false");
        param1.put("pastdueInvoiceId", "");
//        param1.put("creditexp", cred_exp);
        param1.put("creditexp", "");
        param1.put("strPaymentCredit", card_detail);
        param1.put("strPaymentCheck", check_detail);
        param1.put("Total", AppConfiguration.totalPromoArray.get(0).get("Total"));
        param1.put("strPaymentBillingAddress", PaymenPaymentBillingAddresstCheck);
        param1.put("strPaymentShippingAddress", PaymenPaymentBillingAddresstCheck);
        param1.put("Userid1", "");
        AppConfiguration.finalSelectedPaymentArray.add(param1);
        Log.e("AppConfiguration.finalSelectedPaymentArray -- final", "" + AppConfiguration.finalSelectedPaymentArray);
//        insertedCheckDetailArray.add(param1);
//        AppConfiguration.insertedCheckDetailArray = insertedCheckDetailArray;
    }

    String success;

    // Get card details buy payment id
    class CardDetailsByPmtIdAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
//            pd.show();
//            insertedCardDetailArray.clear();
            // siteMainList.clear();
            // siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CardDetailsByPmtId();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if (pd != null) {
//
//                pd.dismiss();
//
//            }
            if (success.equalsIgnoreCase("True")) {
                Intent i = new Intent(BuyMoreSelectPaymentMethod.this, BuyMoreAddNewPaymentMethod.class);
                startActivity(i);
                AppConfiguration.pmtId = pmtId;
                Log.d("AppConfiguration.pmtId-", AppConfiguration.pmtId);
            } else {
                Toast.makeText(getApplicationContext(), "Something wrong,please try again...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void CardDetailsByPmtId() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("Basketid", AppConfiguration.BasketID);
        param.put("CustomerId", CustomerId);
        param.put("pmtId", pmtId);

//        insertedCardDetailArray.add(param);
//        AppConfiguration.insertedCardDetailArray = insertedCardDetailArray;
//        AppConfiguration.addNewCardPayment=true;
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetCardDetailByPmtID,
                param);
        Log.d("card buy pmt id-", responseString);

        readAndParseCardDetailsByPmtIdJSON(responseString);
    }

    public void readAndParseCardDetailsByPmtIdJSON(String in) {
        try {
            AppConfiguration.editCardArray.clear();
            JSONObject reader = new JSONObject(in);
            success = reader.getString("Success");
            if (success.equalsIgnoreCase("True")) {
                JSONArray jarray = reader.getJSONArray("ACHList");
                Log.d("jarray-ACHList", "" + jarray);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject cardObj = jarray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    if (pmtId.equalsIgnoreCase(cardObj.getString("wu_PmtID").toString().trim())) {
                        map.put("wu_FirstName", cardObj.getString("wu_FirstName"));
                        map.put("wu_LastName", cardObj.getString("wu_LastName"));
                        map.put("wu_CardAccNumber", cardObj.getString("wu_CardAccNumber"));
                        map.put("wu_CardType", cardObj.getString("wu_CardType"));
                        map.put("wu_ExpDate", cardObj.getString("wu_ExpDate"));
//                    map.put("wu_Cvv",cardObj.getString("wu_Cvv"));
//                    map.put("wu_PmtID",cardObj.getString("wu_PmtID"));
                        map.put("wu_Address1", cardObj.getString("wu_Address1"));
                        map.put("wu_Address2", cardObj.getString("wu_Address2"));
                        map.put("wu_City", cardObj.getString("wu_City"));
                        map.put("wu_State", cardObj.getString("wu_State"));
                        map.put("wu_ZipCode", cardObj.getString("wu_ZipCode"));
                        editCardArray.add(map);

                        AppConfiguration.editCardArray = editCardArray;
                    }
                }
            } else {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
        startActivity(i);
        finish();
    }
}
