package com.waterworks.manageProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.asyncTasks.CardAsyncTask;
import com.waterworks.asyncTasks.CheckingAcAsyncTask;
import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.asyncTasks.StateListAsyncTask;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPaymentAccount extends Activity {

    private Spinner spinPaymentAcc, spinCardType, spinAccontType, spinMonth, spinYear, spinState;
    private EditText edtNameonCard, edtCardNumber, edtCVV, edtAddressLine1, edtAddressLine2, edtCity, edtZipCode, edtBankName, edtNameonAccount, edtAccountNumber, edtRoutingNumber;
    private LinearLayout llAddress;
    private View includeLayoutCard, includeLayoutBank;
    private TextView btnAddCardorBank;
    private CardView cardBtnAddCardorBank;
    private StateListAsyncTask stateListAsyncTask = null;
    private CardAsyncTask cardAsyncTask = null;
    private CheckingAcAsyncTask checkingAcAsyncTask = null;

    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> siteName = new ArrayList<String>();
    private String token, basketID, siteID;
    private Context mContext = this;
    Boolean isInternetPresent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_account);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(this);
        token = prefs.getString("Token", "");
        basketID = AppConfiguration.BasketID;

        isInternetPresent = Utility.isNetworkConnected(AddPaymentAccount.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            addHeader();
            initViews();
            addListners();
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
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void initViews() {
        llAddress = (LinearLayout) findViewById(R.id.llAddress);

        spinPaymentAcc = (Spinner) findViewById(R.id.spinPaymentAcc);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.payment_type));
        spinPaymentAcc.setAdapter(adapter);

        spinState = (Spinner) findViewById(R.id.spinState);
        stateListAsyncTask = new StateListAsyncTask(this);
        try {
            String responseString = stateListAsyncTask.execute().get();
            stateList = readStateAndParseJSON(responseString);
            ArrayAdapter<String> adapterState = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stateList);
            spinState.setAdapter(adapterState);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(AddPaymentAccount.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        edtAddressLine1 = (EditText) findViewById(R.id.edtAddressLine1);
        edtAddressLine2 = (EditText) findViewById(R.id.edtAddressLine2);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtZipCode = (EditText) findViewById(R.id.edtZipCode);
        btnAddCardorBank = (TextView) findViewById(R.id.btnAddCardorBank);
        cardBtnAddCardorBank = (CardView) findViewById(R.id.cardBtnAddCardorBank);

        //card layout
        includeLayoutCard = (View) findViewById(R.id.includeLayoutCard);
        edtNameonCard = (EditText) includeLayoutCard.findViewById(R.id.edtNameonCard);
        edtCardNumber = (EditText) includeLayoutCard.findViewById(R.id.edtCardNumber);
        edtCVV = (EditText) includeLayoutCard.findViewById(R.id.edtCVV);

        spinCardType = (Spinner) includeLayoutCard.findViewById(R.id.spinCardType);
        ArrayAdapter<String> adapterCardType = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.card_type));
        spinCardType.setAdapter(adapterCardType);

        spinMonth = (Spinner) includeLayoutCard.findViewById(R.id.spinMonth);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.month));
        spinMonth.setAdapter(adapterMonth);

        spinYear = (Spinner) includeLayoutCard.findViewById(R.id.spinYear);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.year));
        spinYear.setAdapter(adapterYear);

        //bank layout
        includeLayoutBank = (View) findViewById(R.id.includeLayoutBank);
        edtBankName = (EditText) includeLayoutBank.findViewById(R.id.edtBankName);
        edtNameonAccount = (EditText) includeLayoutBank.findViewById(R.id.edtNameonAccount);
        edtAccountNumber = (EditText) includeLayoutBank.findViewById(R.id.edtAccountNumber);
        edtRoutingNumber = (EditText) includeLayoutBank.findViewById(R.id.edtRoutingNumber);

        spinAccontType = (Spinner) includeLayoutBank.findViewById(R.id.spinAccontType);
        ArrayAdapter<String> adapterAccountType = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.acc_type));
        spinAccontType.setAdapter(adapterAccountType);
    }

    public void addListners() {
        spinState.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtCity.getWindowToken(), 0);
                return false;
            }
        });

        edtCVV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (edtCardNumber.getText().toString().length() > 0) {
                    if (edtCardNumber.getText().toString().length() < 15) {
                        edtCardNumber.setError("Card Number is not valid.");
                        edtCardNumber.requestFocus();
                    }
                } else {
                    edtCardNumber.setError("Card Number is missing.");
                    edtCardNumber.requestFocus();
                }
            }
        });

        spinPaymentAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    llAddress.setVisibility(View.GONE);
                    includeLayoutCard.setVisibility(View.GONE);
                    includeLayoutBank.setVisibility(View.GONE);
                } else if (position == 1) {
                    llAddress.setVisibility(View.VISIBLE);
                    includeLayoutCard.setVisibility(View.VISIBLE);
                    includeLayoutBank.setVisibility(View.GONE);
                    btnAddCardorBank.setText("Add Credit Card");
                    cardBtnAddCardorBank.setTag("card");
                } else {
                    llAddress.setVisibility(View.VISIBLE);
                    includeLayoutCard.setVisibility(View.GONE);
                    includeLayoutBank.setVisibility(View.VISIBLE);
                    btnAddCardorBank.setText("Add Bank Account");
                    cardBtnAddCardorBank.setTag("bank");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cardBtnAddCardorBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (v.getTag().equals("card")) {

                        if (validateCard()) {
                            HashMap<String, String> param = new HashMap<String, String>();
                            param.put("Token", token);
                            param.put("SiteID", "0");//site id to be sent static '0': by Aanal
                            param.put("ddlctype", spinCardType.getSelectedItem().toString());

                            String nameOnCard = edtNameonCard.getText().toString();
                            String[] names = nameOnCard.split(" ", 2);

                            param.put("txtfname", names[0].toString());
                            param.put("txtlname", names[1].toString());
                            param.put("CardNo", edtCardNumber.getText().toString());
                            param.put("ddlexp1", spinMonth.getSelectedItem().toString());
                            param.put("ddlexp2", spinYear.getSelectedItem().toString());
                            param.put("txtcvv", edtCVV.getText().toString());

                            param.put("txtaddress1", edtAddressLine1.getText().toString());
                            param.put("txtaddress2", edtAddressLine2.getText().toString());
                            param.put("txtcity", edtCity.getText().toString());
                            param.put("txtstate", spinState.getSelectedItem().toString());
                            param.put("txtzipcode", edtZipCode.getText().toString());

                            cardAsyncTask = new CardAsyncTask(AddPaymentAccount.this, param);
                            String responseString = cardAsyncTask.execute().get();
                            readAndParseCardAndCheckingJSON(responseString);
                        }

                    } else {
                        if (validateBank()) {
                            HashMap<String, String> param = new HashMap<String, String>();
                            param.put("Token", token);
                            param.put("SiteID", "0");//site id to be sent static '0': by Aanal
                            param.put("txtnameofcheck", edtNameonAccount.getText().toString().trim());
                            param.put("txtrouting", edtRoutingNumber.getText().toString().trim());
                            param.put("txtaccno", edtAccountNumber.getText().toString().trim());
                            param.put("txtbankname", edtBankName.getText().toString().trim());
                            param.put("ddlAccountType", spinAccontType.getSelectedItem().toString().trim());

                            param.put("chkaddressline1", edtAddressLine1.getText().toString());
                            param.put("chkaddressline2", edtAddressLine2.getText().toString());
                            param.put("chkcity", edtCity.getText().toString());
                            param.put("chkstate", spinState.getSelectedItem().toString());
                            param.put("chkzip", edtZipCode.getText().toString());

                            checkingAcAsyncTask = new CheckingAcAsyncTask(AddPaymentAccount.this, param);
                            String responseString = checkingAcAsyncTask.execute().get();
                            readAndParseCardAndCheckingJSON(responseString);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean validateCard() {
        boolean all_done = false;

        if (edtNameonCard.getText().toString().length() > 0) {
            if (edtCardNumber.getText().toString().length() > 0) {
                if (edtCardNumber.getText().toString().length() <= 16) {
                    if (spinCardType.getSelectedItemPosition() > 0) {
                        if (edtCVV.getText().toString().length() > 0) {
                            if (edtAddressLine1.getText().toString().length() > 0) {
                                if (edtCity.getText().toString().length() > 0) {
                                    if (edtZipCode.getText().toString().length() > 0) {
                                        all_done = true;
                                    } else {
                                        edtZipCode.setError("Zipcode is missing.");
                                        edtZipCode.requestFocus();
                                    }
                                } else {
                                    edtCity.setError("City name is missing.");
                                    edtCity.requestFocus();
                                }
                            } else {
                                edtAddressLine1.setError("Address line 1is missing.");
                                edtAddressLine1.requestFocus();
                            }
                        } else {
                            edtCVV.setError("CVV is missing.");
                            edtCVV.requestFocus();
                        }
                    } else {
                        Utility.ping(mContext, "Card type is missing.");
                        spinCardType.requestFocus();
                    }
                } else {
                    edtCardNumber.setError("Card Number is not valid.");
                    edtCardNumber.requestFocus();
                }
            } else {
                edtCardNumber.setError("Card Number is missing.");
                edtCardNumber.requestFocus();
            }
        } else {
            edtNameonCard.setError("Name is missing.");
            edtNameonCard.requestFocus();
        }
        return all_done;
    }

    public boolean validateBank() {
        boolean all_done = false;
        if (edtNameonAccount.getText().toString().trim().length() > 0) {
            if (edtRoutingNumber.getText().toString().trim().length() > 0) {
                if (edtAccountNumber.getText().toString().trim().length() > 0) {
                    if (edtBankName.getText().toString().trim().length() > 0) {
                        if (edtAddressLine1.getText().toString().trim().length() > 0) {
                            if (edtCity.getText().toString().trim().length() > 0) {
                                if (edtZipCode.getText().toString().trim().length() > 0) {
                                    all_done = true;
                                } else {
                                    edtZipCode.setError("Zipcode is missing.");
                                    edtZipCode.requestFocus();
                                }
                            } else {
                                edtCity.setError("City is missing.");
                                edtCity.requestFocus();
                            }
                        } else {
                            edtAddressLine1.setError("Address line 1 is missing.");
                            edtAddressLine1.requestFocus();
                        }
                    } else {
                        edtBankName.setError("Bank name is missing.");
                        edtBankName.requestFocus();
                    }
                } else {
                    edtAccountNumber.setError("Account number is missing.");
                    edtAccountNumber.requestFocus();
                }
            } else {
                edtRoutingNumber.setError("Routing number is missing.");
                edtRoutingNumber.requestFocus();
            }
        } else {
            edtNameonAccount.setError("Name is missing.");
            edtNameonAccount.requestFocus();
        }
        return all_done;
    }
    public ArrayList<String> readStateAndParseJSON(String in) {
        ArrayList<String> stateList = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("StateList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String state = jsonChildNode.getString("State");
                stateList.add(state);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stateList;
    }

    public void readAndParseCardAndCheckingJSON(String in) {
        String msg = null;
        try {
            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if (success.equalsIgnoreCase("True")) {
                msg = reader.getString("Msg");
                Utility.ping(AddPaymentAccount.this, msg);
                finish();

            } else {
                Utility.ping(AddPaymentAccount.this, msg);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAndParseSiteJSON(String in) {
        try {
            HashMap<String, String> hashmap;
            hashmap = new HashMap<String, String>();

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("SiteList");

            if (jsonMainNode.length() > 1) {
                hashmap.put("SiteID", "0");
                hashmap.put("SiteName", "Please Select a Location");
                siteMainList.add(hashmap);
            }
            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("siteid"));
                hashmap.put("SiteName", jsonChildNode.getString("sitename"));

                siteName.add("" + jsonChildNode.getString("sitename"));

                siteMainList.add(hashmap);
            }

            Log.d("siteName---", "" + siteName);
            Log.d("siteName---1", "" + siteName.size());
            Log.d("siteMainList---", "" + siteMainList);
            Log.d("siteMainList---1", "" + siteMainList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addHeader() {
        //Custom Header
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Add Payment Account");

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
