package com.waterworks.sheduling;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.waterworks.DashBoardActivity;
import com.waterworks.R;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ByMoreMyCart extends Activity {
    LinearLayout swimLsn, retailStore, otherPrograms, lay_non_lafitness, lay_lafitness, sites_lay,
            ll_view_cart_promocode, ll_inflate_mycart_package, ll_inflate_mycart_monthlyplan;
    RelativeLayout rl_promo_code, rl_total, ll_sales_tax, ll_promo, ll_subtotal, rl_tax;
    Button BackButton, btn_viewCart;
    Context context;
    CardView btnEmptyBasket, btn_checkout, btn_continue_shopping, btn_view_cart_promocode;
    EditText et_view_cart_promocode;
    TextView txt_1, txt_2, txt_3, tv_total, tv_salestax, tv_promo, tv_subtotal, tv_empty_cart, txtTax;
    View selected_1, selected_2, selected_3;
    String lessionType, token, familyID, siteID, LessinTypeId, mQuantity, mPrice, msg, total, successStr1;
    Context mContext = this;
    ListView lv_view_cart;
    String DeleteID = "DeleteID", data_load_apply = "False", PromoCode, PromoCodeInternal, promotext;
    // myCartArrayAdapter myCartAdp;
    ArrayList<HashMap<String, String>> myCartArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> MonthlyArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tempMyCartArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tempMonthlyArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> packageArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> baketIdArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> cardDetailArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> totalPromoArray = new ArrayList<HashMap<String, String>>();
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more_my_cart);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);
        ByMoreMyCart.this.overridePendingTransition(0, 0);
        ((AnimationDrawable) selected_1.getBackground()).start();

        init();
        typeFace();
    }

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(), "RobotoRegular.ttf");
        BackButton.setTypeface(regular);
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

    @SuppressWarnings("unchecked")
    public void init() {
        View view = findViewById(R.id.schdl_top);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        ll_view_cart_promocode = (LinearLayout) findViewById(R.id.ll_view_cart_promocode);

        ll_inflate_mycart_package = (LinearLayout) findViewById(R.id.ll_inflate_mycart_package);
        ll_inflate_mycart_monthlyplan = (LinearLayout) findViewById(R.id.ll_inflate_mycart_monthlyplan);

        rl_total = (RelativeLayout) findViewById(R.id.rl_total);
        ll_sales_tax = (RelativeLayout) findViewById(R.id.ll_sales_tax);
        ll_promo = (RelativeLayout) findViewById(R.id.ll_promo);
        ll_subtotal = (RelativeLayout) findViewById(R.id.ll_subtotal);
        rl_tax = (RelativeLayout) findViewById(R.id.rl_tax);
        rl_promo_code = (RelativeLayout) findViewById(R.id.rl_promo_code);
        tv_empty_cart = (TextView) findViewById(R.id.tv_empty_cart);
        tv_total = (TextView) findViewById(R.id.tv_total);
        txtTax = (TextView) findViewById(R.id.txtTax);
        tv_salestax = (TextView) findViewById(R.id.tv_salestax);
        tv_promo = (TextView) findViewById(R.id.tv_promo);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        et_view_cart_promocode = (EditText) findViewById(R.id.et_view_cart_promocode);
        btn_view_cart_promocode = (CardView) findViewById(R.id.btn_view_cart_promocode);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);
        lv_view_cart = (ListView) findViewById(R.id.lv_view_cart);
        btn_checkout = (CardView) findViewById(R.id.btn_checkout);
        btnEmptyBasket = (CardView) findViewById(R.id.btnEmptyBasket);
        btn_continue_shopping = (CardView) findViewById(R.id.btn_continue_shopping);
        Intent lessionIntent = getIntent();
        lessionType = lessionIntent.getStringExtra("LessionType");
        AppConfiguration.lessionType = lessionType;
//		myCartArray = AppConfiguration.myCartPackageArray;
        MonthlyArray = AppConfiguration.myCartMonthlyPlanArray;
        if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
            tv_empty_cart.setVisibility(View.VISIBLE);
            rl_total.setVisibility(View.INVISIBLE);
        } else {
            isInternetPresent = Utility.isNetworkConnected(ByMoreMyCart.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new totalPromoAsyncTask().execute();
            }
        }
        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ByMoreMyCart.this, DashBoardActivity.class);
                AppConfiguration.global_StudIDChecked.clear();
                AppConfiguration.selectedStudent1.clear();
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        btn_view_cart_promocode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (et_view_cart_promocode.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter promo code before redeem it.", Toast.LENGTH_SHORT).show();
                } else {
                    PromoCode = et_view_cart_promocode.getText().toString();
                    new ApplyPromocode().execute();
                }
            }
        });
        btn_checkout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                if(cardDetailArray.size()==0){
//                    new cardDetailAsyncTask().execute();
//                }
//                new cardDetailAsyncTask().execute();\
                if (totalPromoArray.size() > 0) {
                    Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
                    startActivity(i);
                    finish();
                } else {
                    Utility.ping(ByMoreMyCart.this, "Your cart is empty.");
                }
            }
        });
        btnEmptyBasket.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalPromoArray.size() > 0) {
                    new EmptyBasket().execute();
                } else {
                    Utility.ping(ByMoreMyCart.this, "Cart is already empty. Continue Shopping.");
                }
            }
        });
        btn_continue_shopping.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
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
                ByMoreMyCart.this.overridePendingTransition(0, 0);
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
                ByMoreMyCart.this.overridePendingTransition(0, 0);
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
                AppConfiguration.global_StudIDChecked.clear();
                AppConfiguration.selectedStudent1.clear();
                startActivity(i);
                ByMoreMyCart.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }

    // get payment method
    class cardDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            cardDetailArray.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadCardDetailList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            AppConfiguration.myCartPackageArray = myCartArray;
            AppConfiguration.myCartMonthlyPlanArray = MonthlyArray;
            Log.e("myCartArray---##", "" + AppConfiguration.myCartPackageArray);
            if (cardDetailArray.size() == 0 && myCartArray.size() == 0 && MonthlyArray.size() == 0) {
                Toast.makeText(getApplicationContext(), "Your cart is empty cotinue shopping", Toast.LENGTH_SHORT).show();
                // finish();
            } else {
                Intent i = new Intent(getApplicationContext(), BuyMoreSelectPaymentMethod.class);
                startActivity(i);
            }
        }
    }

    public void loadCardDetailList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Basketid", AppConfiguration.BasketID);
        param.put("Token", token);
        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.payDGetACHCardDetail, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");

            if (successStr.equalsIgnoreCase("True")) {
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


                    cardDetailArray.add(hashmap);

                    Log.e("cardDetailArray size---", "" + cardDetailArray.size());
                    AppConfiguration.cardDetailArray = cardDetailArray;
                    Log.e("AppConfig.cardDetailAr", "" + AppConfiguration.cardDetailArray);
                    Log.e("cardDetailArray size--1", "" + cardDetailArray.size());
                }
            } else if (successStr.equalsIgnoreCase("False")) {
                msg = reader.getString("Msg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get Total and promo data
    class totalPromoAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            cardDetailArray.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadtotalPromoList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (successStr1.equalsIgnoreCase("False")) {
                Log.i("successStr--21-", successStr1);
                tv_empty_cart.setVisibility(View.VISIBLE);
                totalPromoVisibility();

            } else {
                try {
                    AppConfiguration.totalPromoArray = totalPromoArray;
                    Log.e("myCartArray-1", "" + myCartArray);
                    Log.e("AppConfiguration-1", "" + AppConfiguration.totalPromoArray);
                    myCartArray = totalPromoArray;
                    if (DeleteID.equalsIgnoreCase("DeleteID")) {
                        inflatePackage(myCartArray);
                        inflateMonthlyPlans(MonthlyArray);
                    } else {

                    }
                    totalPromoVisibility();

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

        }
    }

    public void loadtotalPromoList() {

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("Basketid", AppConfiguration.BasketID);
        param.put("Token", token);
        param.put("FamilyID", familyID);
        String responseString = WebServicesCall.RunScript(AppConfiguration.viewcart__pasduebal_basketid, param);
        readtotalPromoAndParseJSON(responseString);
    }

    public void readtotalPromoAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            successStr1 = reader.getString("Success");

            if (successStr1.equalsIgnoreCase("True")) {
                Log.i("successStr--1-", successStr1);
                total = reader.getString("Total");
                String finalSubTotal = reader.getString("SubTotalSum");
                String finalTaxTotal = reader.getString("TaxSum");
                PromoCodeInternal = reader.getString("promocode");
                promotext = reader.getString("promotext");
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    Log.i("jsonMainNode.length()--", "" + jsonMainNode.length());
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("Subtotal", jsonChildNode.getString("Subtotal"));
                    hashmap.put("Promocode", jsonChildNode.getString("Promocode"));
                    hashmap.put("promotext", promotext);
                    hashmap.put("Tax", jsonChildNode.getString("Tax"));
                    hashmap.put("Package", jsonChildNode.getString("Package"));
                    hashmap.put("ItemTypeID", jsonChildNode.getString("ItemTypeID"));
                    hashmap.put("Total", "$" + total);
                    hashmap.put("SubTotalSum", "$" + finalSubTotal);
                    hashmap.put("TaxSum", "$" + finalTaxTotal);
                    hashmap.put("PromoCodeInternal", PromoCodeInternal);
                    hashmap.put("DeleteID", jsonChildNode.getString("Delete").toString());
                    hashmap.put("Type", jsonChildNode.getString("Type").toString());
                    hashmap.put("Item", jsonChildNode.getString("Item").toString());
                    hashmap.put("Package", jsonChildNode.getString("Package").toString());
                    hashmap.put("Price", jsonChildNode.getString("Price").toString());
                    hashmap.put("Qty", jsonChildNode.getString("Qty").toString());
                    hashmap.put("Description", jsonChildNode.getString("Description").toString());
                    hashmap.put("DeleteEblDble", jsonChildNode.getString("DeleteEblDble").toString());
                    hashmap.put("Location", jsonChildNode.getString("Location").toString());
                    hashmap.put("StudentName", jsonChildNode.getString("StudentName").toString());
                    totalPromoArray.add(hashmap);
                }
            } else if (successStr1.equalsIgnoreCase("False")) {
                Log.i("successStr--2-", successStr1);
                totalPromoArray.clear();
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("Msg", jsonChildNode.getString("Msg"));

                    totalPromoArray.add(hashmap);
                    AppConfiguration.totalPromoArray = totalPromoArray;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inflatePackage(final ArrayList<HashMap<String, String>> myCartArray) {
        Log.e("myCartArray-", "" + myCartArray);
        for (int i = 0; i < myCartArray.size(); i++) {
            final int pos = i;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.buymore_mycart_items, null);
            final TextView tv_quantity = (TextView) view.findViewById(R.id.tv_view_cart_item_qty);
            final TextView tv_view_cart_item_package = (TextView) view.findViewById(R.id.tv_view_cart_item_package);
            final TextView tv_your_cost = (TextView) view.findViewById(R.id.tv_view_cart_item_price);
            final TextView tv_view_cart_item_delete = (TextView) view.findViewById(R.id.tv_view_cart_item_delete);
            final TextView tv_view_cart_item_type = (TextView) view.findViewById(R.id.tv_view_cart_item_type);
            final ImageView img_view_cart_item_delete = (ImageView) view.findViewById(R.id.img_view_cart_item_delete);

            if (myCartArray.get(i).get("Type").equalsIgnoreCase("Lessons")) {
                if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("8") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("9")) {
                    tv_quantity.setVisibility(View.VISIBLE);
                    tv_quantity.setText("Qty: " + myCartArray.get(i).get("Qty"));
                } else {
                    tv_quantity.setVisibility(View.GONE);
                }
            } else if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("37") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("36")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("35") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("12")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("10") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("34")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("33") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("14")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("15") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("38")) {
                tv_quantity.setText("Location: " + myCartArray.get(i).get("Location"));
            } else if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("6")) {
                tv_quantity.setText("Student: " + myCartArray.get(i).get("StudentName"));
            } else {
                tv_quantity.setText("Qty: " + myCartArray.get(i).get("Qty"));

            }
            if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("40") && !myCartArray.get(i).get("Description").equalsIgnoreCase("")) {
                String[] firstString = myCartArray.get(i).get("Description").split(",");
                StringBuilder builder = new StringBuilder();
                String[] colorString = firstString[1].split(":");
                builder.append("Color: " + colorString[0]);
                String[] sizeString = firstString[2].split(":");
                builder.append("\nSize: " + sizeString[0]);
                tv_view_cart_item_package.setText(builder.toString());

            } else if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("37") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("36")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("35") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("12")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("10") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("34")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("33") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("14")
                    || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("15")|| myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("38")) {
                tv_view_cart_item_package.setText("Student: " + myCartArray.get(i).get("StudentName"));

            } else if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("3") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("4")) {
                tv_view_cart_item_package.setText("Location: " + myCartArray.get(i).get("Location"));

            } else {
                //tv_view_cart_item_package.setText("Pkg: "+ myCartArray.get(i).get("Package"));
                if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("8") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("9")
                        || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("7")) {
                    tv_view_cart_item_package.setText(myCartArray.get(i).get("Package"));
                } else if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("13") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("11")) {
                    tv_view_cart_item_package.setVisibility(View.GONE);
                } else {
//                    24-05-2017 megha
                    if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("1")) {
                        int packagestr = Integer.parseInt(myCartArray.get(i).get("Package"));
                        int qtystr = Integer.parseInt(myCartArray.get(i).get("Qty"));
                        if (qtystr >= packagestr) {
                            tv_view_cart_item_package.setText(myCartArray.get(i).get("Qty") + " Lesson Package");
                        }else {
                            tv_view_cart_item_package.setText(myCartArray.get(i).get("Package") + " Lesson Package");
                        }
                    } else {
                        tv_view_cart_item_package.setText(myCartArray.get(i).get("Package") + " Lesson Package");
                    }
                }
            }
            if (myCartArray.get(i).get("DeleteEblDble").equalsIgnoreCase("false")) {
                img_view_cart_item_delete.setVisibility(View.INVISIBLE);
            } else {
                img_view_cart_item_delete.setVisibility(View.VISIBLE);
            }
            tv_your_cost.setText(myCartArray.get(i).get("Subtotal"));

            if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("8") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("9")) {
                tv_view_cart_item_type.setText(myCartArray.get(i).get("Item") + " Night");
            } else {
                if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("4") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("7")
                        || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("37") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("36")
                        || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("35") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("34")
                        || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("33") || myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("3")||
                        myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("38")) {
                    tv_view_cart_item_type.setText(myCartArray.get(i).get("Item"));

                } else if (myCartArray.get(i).get("ItemTypeID").equalsIgnoreCase("10")) {
                    tv_view_cart_item_type.setText(myCartArray.get(i).get("Item").substring(myCartArray.get(i).get("Item").indexOf("Date"), myCartArray.get(i).get("Item").length())
                            + " " + myCartArray.get(i).get("Type"));
                } else {
                    tv_view_cart_item_type.setText(myCartArray.get(i).get("Item") + " " + myCartArray.get(i).get("Type"));
                }
            }
            tv_view_cart_item_delete.setText(myCartArray.get(i).get("DeleteID"));

            final ProgressDialog pd;
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            img_view_cart_item_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        pd.show();
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                ll_inflate_mycart_package.removeView(view);
                                myCartArray.clear();
                                isInternetPresent = Utility.isNetworkConnected(ByMoreMyCart.this);
                                if (!isInternetPresent) {
                                    onDetectNetworkState().show();
                                } else {
                                    new deleteinvoice().execute();
                                }
                                pd.dismiss();
                            }
                        }, 1000);
                        DeleteID = tv_view_cart_item_delete.getText().toString();
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            ll_inflate_mycart_package.addView(view);
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

    public void inflateMonthlyPlans(final ArrayList<HashMap<String, String>> MonthlyArray) {
        for (int i = 0; i < MonthlyArray.size(); i++) {
            final int pos = i;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.buymore_mycart_items, null);
            final TextView tv_quantity = (TextView) view.findViewById(R.id.tv_view_cart_item_qty);
            final TextView tv_your_cost = (TextView) view.findViewById(R.id.tv_view_cart_item_price);
            TextView tv_view_cart_item_type = (TextView) view.findViewById(R.id.tv_view_cart_item_type);
            ImageView img_view_cart_item_delete = (ImageView) view.findViewById(R.id.img_view_cart_item_delete);
            tv_view_cart_item_type.setText(MonthlyArray.get(i).get("LessonType"));
            tv_quantity.setText("Monthly Pass" + "(" + MonthlyArray.get(i).get("Quantity") + " Lessons/Month" + ")");
            tv_your_cost.setText(MonthlyArray.get(i).get("Price"));
            img_view_cart_item_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        ll_inflate_mycart_monthlyplan.removeView(view);
                        MonthlyArray.remove(pos);
                        Toast.makeText(getApplicationContext(), "Monthly plan deleted", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            ll_inflate_mycart_monthlyplan.addView(view);
        }
    }

    public void totalPromoVisibility() {
        if (successStr1.equalsIgnoreCase("False")) {
            if (AppConfiguration.totalPromoArray.get(0).get("Msg").equals("No balace due.. ")) {
                ll_subtotal.setVisibility(View.INVISIBLE);
                ll_promo.setVisibility(View.INVISIBLE);
                ll_sales_tax.setVisibility(View.INVISIBLE);
                rl_tax.setVisibility(View.INVISIBLE);
                rl_total.setVisibility(View.INVISIBLE);
                rl_promo_code.setVisibility(View.INVISIBLE);
            }
        } else {
            if (!AppConfiguration.totalPromoArray.get(0).get("Tax").equalsIgnoreCase("$0.00")) {
                ll_sales_tax.setVisibility(View.VISIBLE);
                tv_salestax.setText(AppConfiguration.totalPromoArray.get(0).get("Tax"));
            } else {
                txtTax.setText(AppConfiguration.totalPromoArray.get(0).get("TaxSum"));
            }
            if (!AppConfiguration.totalPromoArray.get(0).get("promotext").equalsIgnoreCase("")) {
                rl_promo_code.setVisibility(View.VISIBLE);
                ll_promo.setVisibility(View.VISIBLE);
                tv_promo.setText(AppConfiguration.totalPromoArray.get(0).get("promotext"));
                ll_subtotal.setVisibility(View.VISIBLE);
                tv_subtotal.setText(AppConfiguration.totalPromoArray.get(0).get("SubTotalSum"));
                rl_total.setVisibility(View.VISIBLE);
                tv_total.setText(AppConfiguration.totalPromoArray.get(0).get("Total"));
                rl_tax.setVisibility(View.VISIBLE);
                txtTax.setText(AppConfiguration.totalPromoArray.get(0).get("TaxSum"));
            } else {
                ll_subtotal.setVisibility(View.VISIBLE);
                tv_subtotal.setText(AppConfiguration.totalPromoArray.get(0).get("SubTotalSum"));
                rl_total.setVisibility(View.VISIBLE);
                tv_total.setText(AppConfiguration.totalPromoArray.get(0).get("Total"));
            }
        }
    }

    // Delete invoice
    private class deleteinvoice extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.e("Basketid--$$", AppConfiguration.BasketID);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("DetailID", DeleteID);
            param.put("Token", token);
            param.put("FamilyID", familyID);
            param.put("BasketID", AppConfiguration.BasketID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.deleteinvoice, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_page = reader.getString("Success");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (data_load_page.toString().equals("True")) {
                try {
                    isInternetPresent = Utility.isNetworkConnected(ByMoreMyCart.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new totalPromoAsyncTask().execute();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                Toast.makeText(mContext, "Deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Not deleted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    String data_load_page;

    private class ApplyPromocode extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ByMoreMyCart.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.e("Basketid-test", "" + AppConfiguration.BasketID);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", familyID);
            param.put("BasketID", AppConfiguration.BasketID);
            param.put("PromoCode", PromoCode);

            String responseString = WebServicesCall.RunScript(AppConfiguration.applypromocode, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_apply = reader.getString("Success");
                JSONArray jsonArray = reader.getJSONArray("FinalArray");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    msg = obj.getString("Msg");
                }
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
            if (data_load_apply.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
//				new totalPromoAsyncTask().execute();
            } else {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class EmptyBasket extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        String data_load_empty;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ByMoreMyCart.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", familyID);
            param.put("BasketID", AppConfiguration.BasketID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.emptybasket_byid, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_empty = reader.getString("Success");
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
            if (data_load_empty.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), "Cart is already empty. Continue Shopping.", Toast.LENGTH_LONG).show();
                AppConfiguration.BasketID = "0";
                tv_total.setText("Total:");
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(getApplicationContext(), "Basket not refreshed.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession.class);
        AppConfiguration.global_StudIDChecked.clear();
        AppConfiguration.selectedStudent1.clear();
        startActivity(i);
        finish();
//        megha================
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

//        super.onBackPressed();
//        Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession2.class);
//        startActivity(i);
//        if(AppConfiguration.cartBackScreen == 0){
//            Intent i = new Intent(mContext, BuyMoreSwimLession.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        }else if(AppConfiguration.cartBackScreen == 1){
//            Intent i = new Intent(mContext, BuyMoreRetailStore.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        }else if(AppConfiguration.cartBackScreen == 2){
//            Intent i = new Intent(mContext, ByMoreOtherPrograms.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        }
    }
}






