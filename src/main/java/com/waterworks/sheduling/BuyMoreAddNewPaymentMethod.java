package com.waterworks.sheduling;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.LoginActivity;
import com.waterworks.MainActivity;
import com.waterworks.R;
import com.waterworks.asyncTasks.CardAsyncTask;
import com.waterworks.asyncTasks.CheckingAcAsyncTask;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BuyMoreAddNewPaymentMethod extends Activity implements OnClickListener {
    //	ButtonRectangle btn_rectangle, btn_add_checkingaccount, btn_add_creditdebit;
    LinearLayout swimLsn, retailStore, otherPrograms, ll_add_credit_debit, ll_add_checking_accountt,
            ll_debit_credit_card, ll_add_checking_account, ll_checking_account, start_redBorder;
    ImageView imgv_creditdebit_down, imgv_checkingAccount_down;
    TextView txt_1, txt_2, txt_3, txt_btn_add_creditdebit;
    CheckBox checkBox_save_forFutureUse, checkBox_save_forFutureUseBank;
    EditText editText_fName, editText_lName, editText_cardNumber, editText_cvv, editText_addressC1, editText_addressC2, editText_city, editText_state, editText_zipCode,
            editText_cardType;//editText_expireMonth, editText_expireYear
    EditText editText_nameOnAccount, editText_bankingRoutingNumber, editText_checkingAcNumber,
            editText_re_enter_checkingAcNumber, editText_stateName, editText_bank_name, editText_accountType,
            editText_address1, editText_address2, editText_chkCity, editText_chZipCode;

    View selected_1, selected_2, selected_3, vw_checkinAc, vw_hrline;
    Button BackButton, btn_viewCart;
    CardView btn_add_creditdebit, btn_add_checkingaccount;
    String token, Basketid, msg, success, check_detail, card_detail, PaymenPaymentBillingAddresstCheck;
    Context mContext = this;
    Animation animSlidBottom, animSlidTop;
    ListPopupWindow lpw_sitelist, lpw_sitelistState, lpw_cardType;//, lpw_cardExpireMonth, lpw_cardExpireYear;
    Spinner spinMonth, spinYear;
    ArrayList<HashMap<String, String>> insertedCardDetailArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> insertedCheckDetailArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> StateList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> stateName = new ArrayList<String>();
    ArrayList<HashMap<String, String>> editCardArray = new ArrayList<HashMap<String, String>>();
    //	String paymentId;
    int maxLength;
    Boolean paymentTypeBank = false, forUpdate = false;
    BuyMoreSelectPaymentMethod buyMoreSelectPaymentMethod;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buymore_addnew_payment_method);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
//        Basketid = AppConfiguration.Basketid;
        Basketid = AppConfiguration.BasketID;
        editCardArray = AppConfiguration.editCardArray;
        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        if (!AppConfiguration.pmtId.equalsIgnoreCase("pmtIdEdt")) {
            //nothing happens
        } else {
            AppConfiguration.pmtId = "pmtIdEdt";
        }

        buyMoreSelectPaymentMethod = new BuyMoreSelectPaymentMethod();

        ((AnimationDrawable) selected_1.getBackground()).start();
        init();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isInternetPresent = Utility.isNetworkConnected(BuyMoreAddNewPaymentMethod.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new StateListAsyncTask().execute();
                }
            }
        }, 1000);
        typeFace();
    }


    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        BackButton.setTypeface(regular);

    }

    private void init() {
        // TODO Auto-generated method stub
        View view = findViewById(R.id.schdl_top);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        ll_add_credit_debit = (LinearLayout) findViewById(R.id.ll_add_credit_debit);
        ll_add_checking_accountt = (LinearLayout) findViewById(R.id.ll_add_checking_accountt);
        ll_debit_credit_card = (LinearLayout) findViewById(R.id.ll_debit_credit_card);
        ll_checking_account = (LinearLayout) findViewById(R.id.ll_checking_account);
        imgv_creditdebit_down = (ImageView) findViewById(R.id.imgv_creditdebit_down);
        imgv_checkingAccount_down = (ImageView) findViewById(R.id.imgv_checkingAccount_down);
        btn_add_checkingaccount = (CardView) findViewById(R.id.btn_add_checkingaccount);
        btn_add_creditdebit = (CardView) findViewById(R.id.btn_add_creditdebit);
        txt_btn_add_creditdebit = (TextView) findViewById(R.id.txt_btn_add_creditdebit);
        vw_checkinAc = (View) findViewById(R.id.vw_checkinAc);
        vw_hrline = (View) findViewById(R.id.vw_hrline);
        lpw_sitelist = new ListPopupWindow(mContext);
        lpw_sitelistState = new ListPopupWindow(mContext);
        lpw_cardType = new ListPopupWindow(getApplicationContext());
//        lpw_cardExpireMonth = new ListPopupWindow (getApplicationContext());
//        lpw_cardExpireYear = new ListPopupWindow(getApplicationContext());
        checkBox_save_forFutureUse = (CheckBox) findViewById(R.id.checkBox_save_forFutureUse);
        checkBox_save_forFutureUseBank = (CheckBox) findViewById(R.id.checkBox_save_forFutureUseBank);
        start_redBorder = (LinearLayout) findViewById(R.id.start_redBorder);

        // Card detail variables

        editText_fName = (EditText) findViewById(R.id.editText_fName);

        editText_lName = (EditText) findViewById(R.id.editText_lName);
        editText_cardNumber = (EditText) findViewById(R.id.editText_cardNumber);
        editText_cardType = (EditText) findViewById(R.id.editText_cardType);
//        editText_expireMonth = (EditText) findViewById(R.id.editText_expireMonth);
//        editText_expireYear = (EditText) findViewById(R.id.editText_expireYear);


        editText_cvv = (EditText) findViewById(R.id.editText_cvv);
        editText_addressC1 = (EditText) findViewById(R.id.editText_addressC1);
        editText_addressC2 = (EditText) findViewById(R.id.editText_addressC2);

        editText_city = (EditText) findViewById(R.id.editText_city);
        editText_state = (EditText) findViewById(R.id.editText_state);
        editText_zipCode = (EditText) findViewById(R.id.editText_zipCode);

        // Checking account variables

        editText_nameOnAccount = (EditText) findViewById(R.id.editText_nameOnAccount);
        editText_bankingRoutingNumber = (EditText) findViewById(R.id.editText_bankingRoutingNumber);
        editText_checkingAcNumber = (EditText) findViewById(R.id.editText_checkingAcNumber);
        editText_bank_name = (EditText) findViewById(R.id.editText_bank_name);
        editText_accountType = (EditText) findViewById(R.id.editText_accountType);
        editText_address1 = (EditText) findViewById(R.id.editText_address1);
        editText_address2 = (EditText) findViewById(R.id.editText_address2);
        editText_chkCity = (EditText) findViewById(R.id.editText_chkCity);
        editText_chZipCode = (EditText) findViewById(R.id.editText_chZipCode);
        editText_stateName = (EditText) findViewById(R.id.editText_stateName);

        ll_add_credit_debit.setOnClickListener(this);
        ll_add_checking_accountt.setOnClickListener(this);
        btn_add_checkingaccount.setOnClickListener(this);
        btn_add_creditdebit.setOnClickListener(this);
        animSlidBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_up);
        animSlidTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slid_in_down);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

//		int maxLength = 16;
        editText_cardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cardTypeValidation(maxLength))});
        Log.e("cardTypeValidation(maxLength)", "" + cardTypeValidation(maxLength));
        // Card type dropdown
        editText_cardType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lpw_cardType.show();

            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreAddNewPaymentMethod.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        lpw_cardType.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup,
                getResources().getStringArray(R.array.card_type)));
        lpw_cardType.setAnchorView(editText_cardType);
        lpw_cardType.setModal(true);
        lpw_cardType.setHeight(LayoutParams.WRAP_CONTENT);

        lpw_cardType.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editText_cardType.setText(getResources().getStringArray(R.array.card_type)[position]);
                lpw_cardType.dismiss();
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
        editText_cvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                start_redBorder
                        .setBackgroundResource(R.drawable.pure_error_border_white);
                if (editText_cardNumber.getText().toString().length() > 0) {
                    if (editText_cardNumber.getText().toString().length() < 15) {
//                        editText_cardNumber.setError("Card Number is not valid.");
//                        editText_cardNumber.requestFocus();
                        start_redBorder
                                .setBackgroundResource(R.drawable.error_border);
                    }
                } else {
//                    editText_cardNumber.setError("Card Number is missing.");
//                    editText_cardNumber.requestFocus();
                    start_redBorder
                            .setBackgroundResource(R.drawable.error_border);
                }
            }
        });
        spinMonth = (Spinner) findViewById(R.id.spinMonth);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.month));
        spinMonth.setAdapter(adapterMonth);

        spinYear = (Spinner) findViewById(R.id.spinYear);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.year));
        spinYear.setAdapter(adapterYear);
        /*// Card expire month drop down
        editText_expireMonth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lpw_cardExpireMonth.show();
            }
        });
        lpw_cardExpireMonth.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup,
                getResources().getStringArray(R.array.month)));
        lpw_cardExpireMonth.setAnchorView(editText_expireMonth);
        lpw_cardExpireMonth.setModal(true);
        lpw_cardExpireMonth.setHeight(LayoutParams.WRAP_CONTENT);
        lpw_cardExpireMonth.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editText_expireMonth.setText(getResources().getStringArray(R.array.month)[position]);
                lpw_cardExpireMonth.dismiss();
            }
        });
        // Card expire year drop down
        editText_expireYear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lpw_cardExpireYear.show();
            }
        });

        lpw_cardExpireYear.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.edittextpopup,
                getResources().getStringArray(R.array.year)));
        lpw_cardExpireYear.setAnchorView(editText_expireYear);
        lpw_cardExpireYear.setModal(true);
        lpw_cardExpireYear.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        lpw_cardExpireYear.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editText_expireYear.setText(getResources().getStringArray(R.array.year)[position]);
                lpw_cardExpireYear.dismiss();
            }
        });*/

        // back button function

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
//				startActivity(i);
                BuyMoreAddNewPaymentMethod.this.finish();
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
                BuyMoreAddNewPaymentMethod.this.overridePendingTransition(0, 0);
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
                BuyMoreAddNewPaymentMethod.this.overridePendingTransition(0, 0);
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
                BuyMoreAddNewPaymentMethod.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

        editText_state.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_city.getWindowToken(), 0);
                lpw_sitelistState.show();
            }
        });

        editText_stateName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_chkCity.getWindowToken(), 0);
                lpw_sitelist.show();
            }
        });

        try {

            if (!AppConfiguration.pmtId.equalsIgnoreCase("pmtIdEdt")) {
                ll_debit_credit_card.setVisibility(View.VISIBLE);
                ll_debit_credit_card.startAnimation(animSlidTop);
                imgv_creditdebit_down.setImageResource(R.drawable.down_arr);
                ll_checking_account.setVisibility(View.GONE);
                ll_add_checking_accountt.setVisibility(View.GONE);
                ll_add_credit_debit.setVisibility(View.GONE);
                imgv_checkingAccount_down.setImageResource(R.drawable.horizontal_arr);

                txt_btn_add_creditdebit.setText("Update Your Card");
                forUpdate = true;
                vw_checkinAc.setVisibility(View.GONE);
                vw_hrline.setVisibility(View.GONE);

                // Edit Card detail data set
                Log.e("editCardArray--te", "" + editCardArray);
                editText_fName.setText(editCardArray.get(0).get("wu_FirstName"));
                editText_lName.setText(editCardArray.get(0).get("wu_LastName"));
                editText_cardNumber.setText(editCardArray.get(0).get("wu_CardAccNumber"));
                editText_cardType.setText(editCardArray.get(0).get("wu_CardType"));
                editText_cvv.setVisibility(View.GONE);
                editText_addressC1.setText(editCardArray.get(0).get("wu_Address1"));
                editText_addressC2.setText(editCardArray.get(0).get("wu_Address2"));
                editText_city.setText(editCardArray.get(0).get("wu_City"));
                editText_state.setText(editCardArray.get(0).get("wu_State"));
                editText_zipCode.setText(editCardArray.get(0).get("wu_ZipCode"));
                int spinnerPosition = adapterMonth.getPosition(editCardArray.get(0).get("wu_ExpDate").toString().trim().substring(10, 12));
                spinMonth.setSelection(spinnerPosition);
                StringBuilder sb = new StringBuilder(editCardArray.get(0).get("wu_ExpDate").toString().trim().substring(13, 15));
                sb.insert(0, "20");
                int spinnerPositionYear = adapterYear.getPosition(sb.toString());
                spinYear.setSelection(spinnerPositionYear);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkBox_save_forFutureUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox_save_forFutureUse.setButtonDrawable(R.drawable.custom_checkbox_check_orange);
                } else {
                    checkBox_save_forFutureUse.setButtonDrawable(R.drawable.custom_checkbox_uncheck);
                }
            }
        });
        checkBox_save_forFutureUseBank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox_save_forFutureUseBank.setButtonDrawable(R.drawable.custom_checkbox_check_orange);
                } else {
                    checkBox_save_forFutureUseBank.setButtonDrawable(R.drawable.custom_checkbox_uncheck);
                }
            }
        });
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
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
    }
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        editText_cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 5) {
                    switch (Integer.parseInt(editText_cardNumber.getText().toString().trim().substring(0, 2))) {
                        case 34:
                            editText_cardType.setText("American Express");
                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.american_express, 0);
                            break;
                        case 37:
                            editText_cardType.setText("American Express");
                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.american_express, 0);
                            break;
                        case 51:
                            editText_cardType.setText("Master Card");
                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mastercard, 0);
//                        CardType = "Master Card";
                            break;
                        case 52:
                            editText_cardType.setText("Master Card");
                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mastercard, 0);
//                        CardType = "Master Card";
                            break;
                        case 53:
                            editText_cardType.setText("Master Card");
                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mastercard, 0);
//                        CardType = "Master Card";
                            break;
                        case 54:
                            editText_cardType.setText("Master Card");
                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mastercard, 0);
//                        CardType = "Master Card";
                            break;
                        case 55:
                            editText_cardType.setText("Master Card");
                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mastercard, 0);
//                        CardType = "Master Card";
                            break;
                        default:
                            switch (Integer.parseInt(editText_cardNumber.getText().toString().trim().substring(0, 4)))
//                                (Convert.ToInt32(CardNo.Substring(0, 4)))
                            {
                                case 6011:
                                    editText_cardType.setText("Discover");
                                    editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.discover, 0);
//                                CardType = "Discover";
                                    break;
                                default:
                                    switch (Integer.parseInt(editText_cardNumber.getText().toString().trim().substring(0, 1)))
//                                        (Convert.ToInt32(CardNo.Substring(0, 1)))
                                    {
                                        case 4:
                                            editText_cardType.setText("Visa");
                                            editText_cardNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visa, 0);
//                                        CardType = "Visa";
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }
                }
//            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_cardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
        editText_cardNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (editText_cardNumber.getText().toString().trim().length() > 0) {
                    if (editText_cardNumber.getText().toString().trim().length() < 15) {
                        if (editText_cardType.getText().toString().trim().equalsIgnoreCase("American Express")) {
                            if (editText_cardNumber.getText().toString().trim().length() != 15) {
                                Toast.makeText(mContext, "Card number is not Valid.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (!editText_cardType.getText().toString().trim().equalsIgnoreCase("American Express")) {
                            if (editText_cardNumber.getText().toString().trim().length() != 16) {
                                Toast.makeText(mContext, "Card number is not Valid.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == ll_add_credit_debit) {

//            isInternetPresent = Utility.isNetworkConnected(LoginActivity.this);
//            if(!isInternetPresent){
//                onDetectNetworkState().show();
//            }
            if (!ll_debit_credit_card.isShown()) {

                ll_debit_credit_card.setVisibility(View.VISIBLE);
                ll_debit_credit_card.startAnimation(animSlidTop);
                imgv_creditdebit_down.setImageResource(R.drawable.down_arr);
                ll_checking_account.setVisibility(View.GONE);
                imgv_checkingAccount_down.setImageResource(R.drawable.horizontal_arr);
                vw_checkinAc.setVisibility(View.VISIBLE);
                if (checkBox_save_forFutureUseBank.isChecked())
                    checkBox_save_forFutureUseBank.setChecked(false);
                // Toast.makeText(getApplicationContext(), "slid top",
                // Toast.LENGTH_SHORT).show();

            } else {

                ll_debit_credit_card.startAnimation(animSlidBottom);
                // Toast.makeText(getApplicationContext(), "slid bottom",
                // Toast.LENGTH_SHORT).show();
                checkBox_save_forFutureUse.setChecked(false);
                imgv_creditdebit_down.setImageResource(R.drawable.horizontal_arr);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        ll_debit_credit_card.setVisibility(View.GONE);
                        ll_add_checking_accountt.setVisibility(View.VISIBLE);
                        vw_checkinAc.setVisibility(View.VISIBLE);
                    }
                }, 300);

            }
        }

        if (v == ll_add_checking_accountt) {

            if (!ll_checking_account.isShown()) {
                ll_add_checking_accountt.setVisibility(View.VISIBLE);
                ll_checking_account.setVisibility(View.VISIBLE);
                ll_debit_credit_card.setVisibility(View.GONE);
                imgv_creditdebit_down.setImageResource(R.drawable.horizontal_arr);
                ll_checking_account.startAnimation(animSlidTop);
                imgv_checkingAccount_down.setImageResource(R.drawable.down_arr);
                if (checkBox_save_forFutureUse.isChecked())
                    checkBox_save_forFutureUse.setChecked(false);
                // Toast.makeText(getApplicationContext(), "slid top 1",
                // Toast.LENGTH_SHORT).show();
            } else {
                ll_checking_account.startAnimation(animSlidBottom);

                // Toast.makeText(getApplicationContext(), "slid bottom 1",
                // Toast.LENGTH_SHORT).show();
                checkBox_save_forFutureUseBank.setChecked(false);
                imgv_checkingAccount_down.setImageResource(R.drawable.horizontal_arr);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ll_checking_account.setVisibility(View.GONE);
                    }
                }, 200);
            }
        }

        if (v == btn_add_checkingaccount) {
            if (pay_check_chk() == true) {
                try {
                    paymentTypeBank = true;

                    AppConfiguration.cardType = "Checking " + editText_checkingAcNumber.getText().toString().substring(0, 12).replaceAll(".", "*") + editText_checkingAcNumber.getText().toString().substring(Math.max(0, editText_checkingAcNumber.getText().toString().trim().length() - 4));
                    PaymentConfirmtAsyncTask paymentConfirmtAsyncTask = new PaymentConfirmtAsyncTask();
                    paymentConfirmtAsyncTask.execute();
//                    new CheckingAcAsyncTask().execute();
                    //add confirm payment ws
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

//                buyMoreSelectPaymentMethod.inflateSelectPaymentMethod(insertedCheckDetailArray);

//                Intent i = new Intent(getApplicationContext(), BuyMoreOrderSummary.class);
//                i.putExtra("payTypeCheck", "payTypeCheck");
                // Toast.makeText(getApplicationContext(), "Your details have
                // been updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                pay_check_chk();
            }
        }

        if (v == btn_add_creditdebit) {

            if (forUpdate) {
                if (pay_credit_update_chk()) {
                    Pay_EditPaymentDetailsAsyncTask pay_editPaymentDetailsAsyncTask = new Pay_EditPaymentDetailsAsyncTask();
                    pay_editPaymentDetailsAsyncTask.execute();
                }
            } else {
                if (pay_credit_chk() == true) {
                    try {
                        paymentTypeBank = false;
                        AppConfiguration.cardType = editText_cardType.getText().toString() + " " + editText_cardNumber.getText().toString().substring(0, 12).replaceAll(".", "*") + editText_cardNumber.getText().toString().substring(Math.max(0, editText_cardNumber.getText().toString().trim().length() - 4));
                        PaymentConfirmtAsyncTask paymentConfirmtAsyncTask = new PaymentConfirmtAsyncTask();
                        paymentConfirmtAsyncTask.execute();
                        //add confirm payment ws
                    /*if (AppConfiguration.pmtId.equalsIgnoreCase("pmtIdEdt")) {
//                        new CardAsyncTask().execute();
                    } else {
//                        new EditCardAsyncTask().execute();
                    }*/
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                } else {
                    pay_credit_chk();
                }
            }
//                buyMoreSelectPaymentMethod.inflateSelectPaymentMethod(insertedCardDetailArray);
//                Intent i = new Intent(getApplicationContext(), BuyMoreOrderSummary.class);
//				i.putExtra("payTypeCard", "payTypeCard");
                /*Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
                startActivity(i);
                finish();*/
            // Toast.makeText(getApplicationContext(), "Your details have
            // been updated successfully!", Toast.LENGTH_SHORT).show();

        }
    }

    class Pay_EditPaymentDetailsAsyncTask extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        String strCreditDtl = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            strCreditDtl = editText_fName.getText().toString()
                    + "|" + editText_lName.getText().toString()
                    + "|" + getCardNumber(editText_cardNumber.getText().toString())[0]
                    + "|" + getCardNumber(editText_cardNumber.getText().toString())[1]
                    + "|" + getCardNumber(editText_cardNumber.getText().toString())[2]
                    + "|" + getCardNumber(editText_cardNumber.getText().toString())[3]
                    + "|" + editText_cardType.getText().toString()
                    + "|" + ""//CVV will go blank
                    + "|" + spinMonth.getSelectedItem().toString()
                    + "|" + spinYear.getSelectedItem().toString()
                    + "|" + editText_addressC1.getText().toString()
                    + "|" + editText_addressC2.getText().toString()
                    + "|" + editText_city.getText().toString()
                    + "|" + editText_state.getText().toString()
                    + "|" + editText_zipCode.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = "";

            try {
                HashMap<String, String> param1 = new HashMap<String, String>();
                param1.put("Token", token);
                param1.put("BasketID", Basketid);
                param1.put("pmtid", AppConfiguration.pmtId);
                param1.put("strCreditDtl", strCreditDtl);
                responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Pay_EditPaymentDetails, param1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
//            {"Success":"True","Msg":"Update Paymen t Detail Successful..."}
            pd.dismiss();
            try {
                JSONObject reader = new JSONObject(response);
                String success = reader.getString("Success");
                String msg = reader.get("Msg").toString();
                if (success.equalsIgnoreCase("True")) {
                    Utility.ping(mContext, msg);
                    finish();
                } else {
                    Utility.ping(mContext, msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Payment confirmation-----------

    class PaymentConfirmtAsyncTask extends AsyncTask<Void, Void, Void> {
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
                CreateConfirmSubmitPaymentArray();

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

            HashMap<String, String> param1 = new HashMap<String, String>();

            param1.put("Token", token);
            param1.put("BasketID", AppConfiguration.BasketID);
            param1.put("wu_recurring", "0");
            param1.put("pmtid", "0");
            param1.put("strType", paymentTypeBank ? "1" : "0");//bank = 1 and card = 0
            param1.put("strCard", paymentTypeBank ? editText_checkingAcNumber.getText().toString() : editText_cardNumber.getText().toString());//bank acc no or card no
            param1.put("ShippingAddress", "");
            param1.put("chkagree", "True");
            param1.put("ChkAddShippingAddress", "True");
            param1.put("RBPaymentType", paymentTypeBank ? "0" : "1");//bank = 0 and card = 1
            if (checkBox_save_forFutureUse.isChecked()) {
                param1.put("chkSaveCard", "True");
                param1.put("chkSaveACH", "False");
            } else if (checkBox_save_forFutureUseBank.isChecked()) {
                param1.put("chkSaveCard", "False");
                param1.put("chkSaveACH", "True");
            } else {
                param1.put("chkSaveCard", "False");
                param1.put("chkSaveACH", "False");
            }
            if (paymentTypeBank) {

                String strCheckDtl = editText_nameOnAccount.getText().toString()
                        + "|" + editText_bankingRoutingNumber.getText().toString()
                        + "|" + editText_checkingAcNumber.getText().toString()
                        + "|" + editText_bank_name.getText().toString()
                        + "|" + editText_accountType.getText().toString()
                        + "|" + editText_address1.getText().toString()
                        + "|" + editText_address2.getText().toString()
                        + "|" + editText_chkCity.getText().toString()
                        + "|" + editText_stateName.getText().toString()
                        + "|" + editText_chZipCode.getText().toString();

                param1.put("strCheckDtl", strCheckDtl);
                param1.put("strCreditDtl", "");

            } else {
                String strCreditDtl = editText_fName.getText().toString()
                        + "|" + editText_lName.getText().toString()
                        + "|" + getCardNumber(editText_cardNumber.getText().toString())[0]
                        + "|" + getCardNumber(editText_cardNumber.getText().toString())[1]
                        + "|" + getCardNumber(editText_cardNumber.getText().toString())[2]
                        + "|" + getCardNumber(editText_cardNumber.getText().toString())[3]
                        + "|" + editText_cardType.getText().toString()
                        + "|" + editText_cvv.getText().toString()
                        + "|" + spinMonth.getSelectedItem().toString()
                        + "|" + spinYear.getSelectedItem().toString()
                        + "|" + editText_addressC1.getText().toString()
                        + "|" + editText_addressC2.getText().toString()
                        + "|" + editText_city.getText().toString()
                        + "|" + editText_state.getText().toString()
                        + "|" + editText_zipCode.getText().toString();

                param1.put("strCheckDtl", "");
                param1.put("strCreditDtl", strCreditDtl);
            }

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.confirm_pay, param1);
            readpaymentConfirmAndParseJSON(responseString);
            Log.i("responseString--", responseString);
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
                Utility.ping(mContext, "Something went wrong here");
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
//        param1.put("creditexp", editText_expireMonth.getText().toString() +""+ editText_expireYear.getText().toString());
        param1.put("creditexp", "");
        param1.put("strPaymentCredit", card_detail);
        param1.put("strPaymentCheck", check_detail);
        param1.put("Total", AppConfiguration.totalPromoArray.get(0).get("Total"));
        param1.put("strPaymentBillingAddress", PaymenPaymentBillingAddresstCheck);
        param1.put("strPaymentShippingAddress", "");
        param1.put("Userid1", "");
        AppConfiguration.finalSelectedPaymentArray.add(param1);

        Log.e("AppConfiguration.finalSelectedPaymentArray -- final", "" + AppConfiguration.finalSelectedPaymentArray);

        //take user directly to order summary as per new flow
        Intent intent = new Intent(BuyMoreAddNewPaymentMethod.this, BuyMoreOrderSummary.class);
        startActivity(intent);
        finish();
    }


    public String[] getCardNumber(String cardNumber) {
        String strfinal = "";
        String[] cardNumberSplitArray;
        while (!cardNumber.trim().equalsIgnoreCase("")) {
            int length = cardNumber.length() < 4 ? cardNumber.length() : 4;
            if (strfinal.trim().equalsIgnoreCase("")) {
                strfinal = cardNumber.substring(0, length);
            } else {
                strfinal += "-" + cardNumber.substring(0, length);
            }
            cardNumber = cardNumber.substring(length, cardNumber.length());
        }
        cardNumberSplitArray = strfinal.split("-");
        return cardNumberSplitArray;
    }

    // Card details insert
    /*class CardAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            insertedCardDetailArray.clear();
            // siteMainList.clear();
            // siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                insertCardDetails();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {

                pd.dismiss();

            }

        }
    }

    public void insertCardDetails() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("Basketid", Basketid);
        param.put("txtfname", editText_fName.getText().toString().trim());
        param.put("txtlname", editText_lName.getText().toString().trim());
        param.put("CardNo", editText_cardNumber.getText().toString().trim());
        param.put("ddlctype", editText_cardType.getText().toString().trim());
        param.put("txtcvv", editText_cvv.getText().toString().trim());
        param.put("ddlexp1", editText_expireMonth.getText().toString().trim());
        param.put("ddlexp2", editText_expireYear.getText().toString().trim());
        param.put("txtaddress1", editText_addressC1.getText().toString().trim());
        param.put("txtaddress2", editText_addressC2.getText().toString().trim());
        param.put("txtcity", editText_city.getText().toString().trim());
        param.put("txtstate", editText_state.getText().toString().trim());
        param.put("txtzipcode", editText_zipCode.getText().toString().trim());

        insertedCardDetailArray.add(param);
//        AppConfiguration.insertedCardDetailArray = insertedCardDetailArray;
        AppConfiguration.addNewCardPayment = true;
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.AddCardDetails,
                param);
        Log.d("card responseString-", responseString);

        readAndParseCardJSON(responseString);
    }

    public void readAndParseCardJSON(final String in) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject reader = new JSONObject(in);
                    success = reader.getString("Success");
                    if (success.equalsIgnoreCase("True")) {
                        msg = reader.getString("Msg");

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Checking Account details insert
    class CheckingAcAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            insertedCheckDetailArray.clear();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            insertCheckingAc();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                    Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

        }
    }

    public void insertCheckingAc() {
//		Log.d("bankingRoutingNumber-",  editText_bankingRoutingNumber.getText().toString());
        insertedCheckDetailArray.clear();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("Basketid", Basketid);
        param.put("txtnameofcheck", editText_nameOnAccount.getText().toString().trim());
        param.put("txtrouting", editText_bankingRoutingNumber.getText().toString().trim());
        param.put("txtaccno", editText_checkingAcNumber.getText().toString().trim());
        param.put("txtbankname", editText_bank_name.getText().toString().trim());
        param.put("ddlAccountType", editText_accountType.getText().toString().trim());
        param.put("chkaddressline1", editText_address1.getText().toString().trim());
        param.put("chkaddressline2", editText_address2.getText().toString().trim());
        param.put("chkcity", editText_chkCity.getText().toString().trim());
        param.put("chkstate", editText_stateName.getText().toString().trim());
        param.put("chkzip", editText_chZipCode.getText().toString().trim());

        insertedCheckDetailArray.add(param);
//        AppConfiguration.insertedCheckDetailArray = insertedCheckDetailArray;
        AppConfiguration.addNewCheckPayment = true;
        Log.d("insertedCheckDetailArray-", "" + insertedCheckDetailArray);
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.AddACHDetails,
                param);
        Log.d("responseString-", responseString);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            success = reader.getString("Success");
            if (success.equalsIgnoreCase("True")) {
                msg = reader.getString("Msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Edit Card details
    class EditCardAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            insertedCardDetailArray.clear();
            // siteMainList.clear();
            // siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                EditCardDetails();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {

                pd.dismiss();

            }

        }
    }

    public void EditCardDetails() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("Basketid", Basketid);
        param.put("pmtID", AppConfiguration.pmtId);
        param.put("txtfname", editText_fName.getText().toString().trim());
        param.put("txtlname", editText_lName.getText().toString().trim());
        param.put("CardNo", editText_cardNumber.getText().toString().trim());
        param.put("ddlctype", editText_cardType.getText().toString().trim());
        param.put("txtcvv", editText_cvv.getText().toString().trim());
        param.put("ddlexp1", editText_expireMonth.getText().toString().trim());
        param.put("ddlexp2", editText_expireYear.getText().toString().trim());
        param.put("txtaddress1", editText_addressC1.getText().toString().trim());
        param.put("txtaddress2", editText_addressC2.getText().toString().trim());
        param.put("txtcity", editText_city.getText().toString().trim());
        param.put("txtstate", editText_state.getText().toString().trim());
        param.put("chkzip", editText_zipCode.getText().toString().trim());

        insertedCardDetailArray.add(param);
//        AppConfiguration.insertedCardDetailArray = insertedCardDetailArray;
        AppConfiguration.addNewCardPayment = true;
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.EditCardDetails,
                param);
        Log.d("card responseString-", responseString);

        readAndParseEditCardCardJSON(responseString);
    }

    public void readAndParseEditCardCardJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            success = reader.getString("Success");
            if (success.equalsIgnoreCase("True")) {
                msg = reader.getString("Msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    // Get state list

    class StateListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            // siteMainList.clear();
            // siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadStateList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {

                pd.dismiss();

            }

            lpw_sitelistState.setAdapter(new ArrayAdapter<String>(mContext, R.layout.edittextpopup, stateName));
            lpw_sitelistState.setAnchorView(editText_state);
            lpw_sitelistState.setHeight(LayoutParams.WRAP_CONTENT);
            // lpw_sitelist.setWidth(LayoutParams.WRAP_CONTENT);
            lpw_sitelistState.setModal(true);
            lpw_sitelistState.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    // AppConfiguration.salStep1SiteID =
                    // StateList.get(pos).get("SiteID");
                    // siteId = StateList.get(pos).get("SiteID");
                    // Log.d("siteId----", siteId);
                    editText_state.setText(StateList.get(pos).get("State"));
                    //
                    lpw_sitelistState.dismiss();
                }
            });

            lpw_sitelist.setAdapter(new ArrayAdapter<String>(mContext, R.layout.edittextpopup, stateName));
            lpw_sitelist.setAnchorView(editText_stateName);
            lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_sitelist.setModal(true);
            lpw_sitelist.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    editText_stateName.setText(StateList.get(pos).get("State"));
                    lpw_sitelist.dismiss();
                }
            });

        }
    }

    public void loadStateList() {
        // siteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("state", " ");

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_StateList,
                param);

        readStateAndParseJSON(responseString);
        // Log.i("responseString--", responseString);
    }

    public void readStateAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("StateList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("State", jsonChildNode.getString("State"));
                // Log.i("States--", "" + jsonChildNode.getString("State"));

                stateName.add("" + jsonChildNode.getString("State"));
                StateList.add(hashmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // card and checking account validation

    public void ping(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public boolean pay_credit_update_chk() {
        boolean all_done = false;

        if (editText_fName.getText().toString().trim().length() > 0) {
            if (editText_lName.getText().toString().length() > 0) {
                if (editText_cardNumber.getText().toString().length() <= 16) {
                    if (editText_cardType.getText().toString().length() > 0) {
                        if (spinMonth.getSelectedItem().toString().length() > 0) {
                            if (spinYear.getSelectedItem().toString().length() > 0) {
                                if (editText_addressC1.getText().toString().length() > 0) {
//                                        if (editText_addressC2.getText().toString().length() > 0) {
                                    if (editText_city.getText().toString().length() > 0) {
                                        if (editText_state.getText().toString().length() > 0) {
                                            if (editText_zipCode.getText().toString().length() > 0) {
                                                all_done = true;
                                            } else {
                                                ping(mContext, "Zipcode is missing.");
                                            }
                                        } else {
                                            ping(mContext, "State is missing.");
                                        }
                                    } else {
                                        ping(mContext, "City name is missing.");
                                    }
//                                        } else {
//                                            ping(mContext, "Address line 2 is missing.");
//                                        }
                                } else {
                                    ping(mContext, "Address line 1is missing.");
                                }
                            } else {
                                ping(mContext, "Year is missing.");
                            }
                        } else {
                            ping(mContext, "Month is missing.");
                        }
                    } else {
                        ping(mContext, "Card type is missing.");
                    }
                } else {
//                    ping(mContext, "Card Number is not valid.");
                    editText_cardNumber.setError("Card Number is not valid.");
                    editText_cardNumber.requestFocus();
                }
            } else {
                ping(mContext, "Last name is missing.");
            }
        } else {
            ping(mContext, "First name is missing.");
        }
        return all_done;
    }

    public boolean pay_credit_chk() {
        boolean all_done = false;

        if (editText_fName.getText().toString().trim().length() > 0) {
            if (editText_lName.getText().toString().length() > 0) {
                if (editText_cardNumber.getText().toString().length() <= 16) {
                    if (editText_cardType.getText().toString().length() > 0) {
                        if (editText_cvv.getText().toString().length() > 0) {
                            if (spinMonth.getSelectedItem().toString().length() > 0) {
                                if (spinYear.getSelectedItem().toString().length() > 0) {
                                    if (editText_addressC1.getText().toString().length() > 0) {
//                                        if (editText_addressC2.getText().toString().length() > 0) {
                                        if (editText_city.getText().toString().length() > 0) {
                                            if (editText_state.getText().toString().length() > 0) {
                                                if (editText_zipCode.getText().toString().length() > 0) {
                                                    all_done = true;
                                                } else {
                                                    ping(mContext, "Zipcode is missing.");
                                                }
                                            } else {
                                                ping(mContext, "State is missing.");
                                            }
                                        } else {
                                            ping(mContext, "City name is missing.");
                                        }
//                                        } else {
//                                            ping(mContext, "Address line 2 is missing.");
//                                        }
                                    } else {
                                        ping(mContext, "Address line 1is missing.");
                                    }
                                } else {
                                    ping(mContext, "Year is missing.");
                                }
                            } else {
                                ping(mContext, "Month is missing.");
                            }
                        } else {
                            ping(mContext, "CVV is missing.");
                        }
                    } else {
                        ping(mContext, "Card type is missing.");
                    }
                } else {
//                    ping(mContext, "Card Number is not valid.");
                    editText_cardNumber.setError("Card Number is not valid.");
                    editText_cardNumber.requestFocus();
                }
            } else {
                ping(mContext, "Last name is missing.");
            }
        } else {
            ping(mContext, "First name is missing.");
        }
        return all_done;
    }

    public boolean pay_check_chk() {
        boolean all_done = false;
        if (editText_nameOnAccount.getText().toString().trim().length() > 0) {
            if (editText_bankingRoutingNumber.getText().toString().trim().length() > 0) {
                if (editText_checkingAcNumber.getText().toString().trim().length() > 0) {
                    if (editText_bank_name.getText().toString().trim().length() > 0) {
                        if (editText_accountType.getText().toString().trim().length() > 0) {
                            if (editText_address1.getText().toString().trim().length() > 0) {
//                                if (editText_address2.getText().toString().trim().length() > 0) {
                                if (editText_chkCity.getText().toString().trim().length() > 0) {
                                    if (editText_stateName.getText().toString().trim().length() > 0) {
                                        if (editText_chZipCode.getText().toString().trim().length() > 0) {
                                            all_done = true;
                                        } else {
                                            ping(mContext, "Zipcode is missing.");
                                        }
                                    } else {
                                        ping(mContext, "State is missing.");
                                    }
                                } else {
                                    ping(mContext, "City is missing.");
                                }
                            } else {
                                ping(mContext, "Address line 1 is missing.");
                            }
//                            } else {
//                                ping(mContext, "Address line 2 is missing.");
//                            }
                        } else {
                            ping(mContext, "Bank account type is missing.");
                        }
                    } else {
                        ping(mContext, "Bank name is missing.");
                    }
                } else {
                    ping(mContext, "Account number is missing.");
                }
            } else {
                ping(mContext, "Routing number is missing.");
            }
        } else {
            ping(mContext, "Name is missing.");
        }

        return all_done;
    }

    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        private static final char space = '-';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a
                // space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }

    public int cardTypeValidation(int maxLength) {
        if (editText_cardType.getText().toString().trim().equalsIgnoreCase("American Express")) {
            maxLength = 15;
        } else {
            maxLength = 16;
        }
        return maxLength;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//		Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
//		startActivity(i);
        finish();
    }
}
