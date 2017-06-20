package com.waterworks.sheduling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.SwimLessonsActivity;
import com.waterworks.ViewCartActivity;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BuyMoreSwimLession2 extends Activity {
    LinearLayout swimLsn, retailStore, otherPrograms, lay_non_lafitness, lay_lafitness, sites_lay, ll_pakcage,
            ll_monthly_plan, lay_inflatepackage, lay_inflatemothlyplan;
    TextView tv_lession_type, tv_planstype;
    Button btn_monthly_plan, btn_packages, BackButton, btn_viewCart;
    CardView btn_continue_shopping, btn_add_tocart;
    ScrollView scrollview;
    ListView lv_swimlession;
    String lessionType, token, familyID, siteID, LessinTypeId, mQuantity, mPrice, basketId, tempBasketId;
    TextView txt_1, txt_2, txt_3, tv_package, tv_monthly_plan, tv_mostpopular;
    View selected_1, selected_2, selected_3;
    Context mContext = this;
    String msg, data_load_submit = "false";
    View select1, select2;

    ArrayList<HashMap<String, String>> packageArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> MonthlyArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tempPackageArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tempMonthlyArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> selectedPackageArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> selectedMonthlyPlanArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tempFinalPackageArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tempFinalMonthlyArray = new ArrayList<HashMap<String, String>>();
    Boolean isInternetPresent = false;

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
//        btn_monthly_plan.setTypeface(regular);
//        btn_packages.setTypeface(regular);
        BackButton.setTypeface(regular);
//        btn_continue_shopping.setTypeface(regular);
//        btn_add_tocart.setTypeface(regular);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more_lessions2);
        BuyMoreSwimLession2.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        Intent lessionIntent = getIntent();
        lessionType = lessionIntent.getStringExtra("LessionType");


//        siteID = lessionIntent.getStringExtra("SiteID");
//		LessinTypeId = lessionIntent.getStringExtra("LessinTypeId");
        siteID = AppConfiguration.siteID;
        LessinTypeId = AppConfiguration.LessinTypeId;
//        basketId = AppConfiguration.Basketid;
//        tempBasketId = AppConfiguration.Basketid;
        basketId = AppConfiguration.BasketID;
        tempBasketId = AppConfiguration.BasketID;
        packageArray = AppConfiguration.finalArrayPackage;
        MonthlyArray = AppConfiguration.finalArrayMonthlyPlan;
        tempFinalPackageArray = AppConfiguration.finalArrayPackage;
        tempFinalMonthlyArray = AppConfiguration.finalArrayMonthlyPlan;

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);
        BuyMoreSwimLession2.this.overridePendingTransition(0, 0);
        ((AnimationDrawable) selected_1.getBackground()).start();

        init();
        typeFace();
        Log.e("tempBasketId-----", tempBasketId);
        isInternetPresent = Utility.isNetworkConnected(BuyMoreSwimLession2.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
        try {
            if (basketId.equalsIgnoreCase("0")) {
                new BasketIdAsyncTask().execute();
            } else {

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }}
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

            }
        }, 1000);

    }

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }


    public void init() {
        View view = findViewById(R.id.schdl_top);
        ll_monthly_plan = (LinearLayout) findViewById(R.id.ll_monthly_plan);
        ll_pakcage = (LinearLayout) findViewById(R.id.ll_package);
        tv_lession_type = (TextView) findViewById(R.id.tv_lession_type);
        tv_planstype = (TextView) findViewById(R.id.tv_planstype);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        btn_continue_shopping = (CardView) findViewById(R.id.btn_continue_shopping);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        lay_inflatepackage = (LinearLayout) findViewById(R.id.lay_inflatepackage);
        lay_inflatemothlyplan = (LinearLayout) findViewById(R.id.lay_inflatemothlyplan);

        btn_add_tocart = (CardView) findViewById(R.id.btn_add_tocart);
        select1 = (View) findViewById(R.id.select_1);
        select2 = (View) findViewById(R.id.select_2);
        tv_package = (TextView) findViewById(R.id.tv_package);
        tv_monthly_plan = (TextView) findViewById(R.id.tv_monthly_plan);
        tv_mostpopular = (TextView) findViewById(R.id.tv_mostpopular);
        scrollview = (ScrollView) findViewById(R.id.scrollview);

        tv_mostpopular.setText("Most popular for\nfamilies with one student!");

        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);
        tv_lession_type.setText(lessionType);

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreSwimLession2.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        try {
            if (basketId.equalsIgnoreCase("0")) {
                inflatePackage(packageArray);
                inflateMonthlyPlans(MonthlyArray);
            } else if (!basketId.equalsIgnoreCase("0")) {
//                basketId = AppConfiguration.Basketid;
                basketId = AppConfiguration.BasketID;
                packageArray = AppConfiguration.finalArrayPackage;
                inflatePackage(packageArray);
                inflateMonthlyPlans(MonthlyArray);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                startActivity(i);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
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
                BuyMoreSwimLession2.this.overridePendingTransition(0, 0);
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
                BuyMoreSwimLession2.this.overridePendingTransition(0, 0);
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
                BuyMoreSwimLession2.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

        btn_continue_shopping.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession.class);
                startActivity(i);

            }
        });

        btn_add_tocart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*Log.e("tempBasketId-----", tempBasketId);
                try {
					if (basketId.equalsIgnoreCase("Basketid")) {
						new BasketIdAsyncTask().execute();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}*/
                isInternetPresent = Utility.isNetworkConnected(BuyMoreSwimLession2.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    if (selectedPackageArray.size() != 0) {
                        new swimlessonPackageSubmit().execute();
                        AppConfiguration.myCartPackageArray = selectedPackageArray;
                    }
                    if (selectedMonthlyPlanArray.size() != 0) {
                        AppConfiguration.myCartMonthlyPlanArray = selectedMonthlyPlanArray;
                    }
//                AppConfiguration.Basketid = basketId;
                    AppConfiguration.BasketID = basketId;
                    if (selectedPackageArray.size() == 0 && selectedMonthlyPlanArray.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Please select the atleast one package", Toast.LENGTH_SHORT)
                                .show();

                    } else {

                        Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                        i.putExtra("LessionType", lessionType);
                        startActivity(i);
                        BuyMoreSwimLession2.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                    finish();
                    }
                }
            }
        });

        ll_monthly_plan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tv_package.setTextColor(getResources().getColor(R.color.design_change_d2));
                tv_monthly_plan.setTextColor(getResources().getColor(R.color.orange));
                select2.setVisibility(View.VISIBLE);
                select1.setVisibility(View.INVISIBLE);
                tv_planstype.setText("Lesson Monthly Plans");
                lay_inflatemothlyplan.setVisibility(View.VISIBLE);
                lay_inflatepackage.setVisibility(View.GONE);
                try {
                    if (MonthlyArray.size() == 0) {
                        inflateMonthlyPlans(MonthlyArray);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        });

        ll_pakcage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tv_monthly_plan.setTextColor(getResources().getColor(R.color.design_change_d2));
                tv_package.setTextColor(getResources().getColor(R.color.orange));
                select1.setVisibility(View.VISIBLE);
                select2.setVisibility(View.INVISIBLE);
//                tv_planstype.setText("Lesson Packages");
                lay_inflatemothlyplan.setVisibility(View.GONE);
                lay_inflatepackage.setVisibility(View.VISIBLE);

                try {
                    if (packageArray.size() == 0) {
                        inflatePackage(packageArray);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
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
    // get basket id

    class BasketIdAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            loadBasketIdList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        }
    }

    public void loadBasketIdList() {

        HashMap<String, String> param = new HashMap<String, String>();

        Log.i("siteID--12-", siteID);
        Log.i("Token--12-", token);
        //
        param.put("Token", token);
        param.put("SiteID", siteID);
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.getBasketID,
                param);
        Log.i("responseString", responseString);
        getBasketIdAndParseJSON(responseString);
    }

    public void getBasketIdAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");
            if (successStr.equalsIgnoreCase("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("BasketDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
//                    Log.i("jsonMainNode.length()", "" + jsonMainNode.length());
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    basketId = jsonChildNode.getString("Basketid");
//                    AppConfiguration.Basketid = basketId;
                    AppConfiguration.BasketID = basketId;
                    tempBasketId = basketId;
                    Log.i("Basketid.......****", jsonChildNode.getString("Basketid"));
                }
            } else if (successStr.equalsIgnoreCase("False")) {
                msg = reader.getString("Msg");

                Toast.makeText(getApplicationContext(), "msg", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inflatePackage(ArrayList<HashMap<String, String>> packageArray) {

        if (packageArray.size() == 0) {
        } else {
            for (int i = 0; i < packageArray.size(); i++) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.buymoreswimlession_list_item, null);
                final TextView tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
                final TextView tv_your_cost = (TextView) view.findViewById(R.id.tv_your_cost);
                TextView tv_reg_cost = (TextView) view.findViewById(R.id.tv_reg_cost);
                TextView tv_saving = (TextView) view.findViewById(R.id.tv_saving);
                final TextView tv_packageId = (TextView) view.findViewById(R.id.tv_packageId);
                final RadioButton rb_item = (RadioButton) view.findViewById(R.id.rb_item);
                rb_item.setTag(packageArray.get(i).get("Quantity"));
                tv_quantity.setText(packageArray.get(i).get("Quantity"));
//                tv_your_cost.setText(formatedCurrency("$" + packageArray.get(i).get("Price").replaceAll("\\.0000", "") + ".00"));
                /*tv_your_cost.setText(formatedCurrency("$" + packageArray.get(i).get("Price").replaceAll("\\.0000", "") + ".00"));
                tv_reg_cost.setText(formatedCurrency("$" + packageArray.get(i).get("OrgPrice").replaceAll("\\.0", "") + ".00"));
                tv_saving.setText(formatedCurrency("$" + packageArray.get(i).get("Saving").replaceAll("\\.0", "") + ".00"));*/
                tv_your_cost.setText(formatedCurrency("$" + packageArray.get(i).get("Price").replaceAll("\\.0000", "")));
                tv_reg_cost.setText(formatedCurrency("$" + packageArray.get(i).get("OrgPrice").replaceAll("\\.0", "")));
                tv_saving.setText(formatedCurrency("$" + packageArray.get(i).get("Saving").replaceAll("\\.0", "")));
                tv_packageId.setText(packageArray.get(i).get("PackageID"));
                lay_inflatepackage.addView(view);

                if (packageArray.get(i).get("Popular").equalsIgnoreCase("Y")) {
                    tv_quantity.setTextColor(getResources().getColor(R.color.orange));
                    tv_your_cost.setTextColor(getResources().getColor(R.color.orange));
                    tv_reg_cost.setTextColor(getResources().getColor(R.color.orange));
                    tv_saving.setTextColor(getResources().getColor(R.color.orange));
                }

                rb_item.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (rb_item.isChecked()) {

                            Log.d("packageArray---TestPrev", "" + selectedPackageArray);
                            selectedPackageArray.clear();
                            HashMap<String, String> hashmap = new HashMap<String, String>();

                            if (!selectedPackageArray.contains(hashmap)) {
                                hashmap.put("Quantity", tv_quantity.getText().toString());
                                hashmap.put("Price", tv_your_cost.getText().toString());
                                hashmap.put("LessonType", tv_lession_type.getText().toString());
                                hashmap.put("PackageID", tv_packageId.getText().toString());
                                tempPackageArray = AppConfiguration.myCartPackageArray;
                                selectedPackageArray.add(hashmap);
                                selectedPackageArray.addAll(tempPackageArray);
                                Log.i("packageArray--##", "" + selectedPackageArray);
//                                new swimlessonPackageSubmit().execute();
                            }
                            mQuantity = tv_quantity.getText().toString();
                            mPrice = tv_quantity.getText().toString();

                            checkedValue = tv_quantity.getText().toString();
                            Log.d("packageArray---Test", "" + selectedPackageArray);
                            getChild();
                        } else {
//                            rb_item.setBackgroundResource(R.drawable.r0);
                        }
                    }
                });

            }
        }
    }

    String checkedValue = "";

    public void getChild() {
        if (lay_inflatepackage.getChildCount() > 0) {
            for (int i = 0; i < lay_inflatepackage.getChildCount(); i++) {
                View view = lay_inflatepackage.getChildAt(i);
                LinearLayout superLay = (LinearLayout) view;
                View view2 = superLay.getChildAt(0);
                LinearLayout lay_list_item = (LinearLayout) view2;
                View view3 = lay_list_item.getChildAt(0);
                LinearLayout rgLay = (LinearLayout) view3;
                View view4 = rgLay.getChildAt(0);
                if (view4 instanceof RadioButton) {
                    RadioButton rr = (RadioButton) view4;
                    if (!rr.getTag().toString().equalsIgnoreCase(checkedValue)) {
                        rr.setChecked(false);
                    }
                }
            }
        }
    }

    public void inflateMonthlyPlans(ArrayList<HashMap<String, String>> MonthlyArray) {
        if (MonthlyArray.size() == 0) {
        } else {
            for (int i = 0; i < MonthlyArray.size(); i++) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.buymoreswimlession_list_item, null);
                final TextView tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
                final TextView tv_your_cost = (TextView) view.findViewById(R.id.tv_your_cost);
                TextView tv_reg_cost = (TextView) view.findViewById(R.id.tv_reg_cost);
                TextView tv_saving = (TextView) view.findViewById(R.id.tv_saving);
                final RadioButton rb_item = (RadioButton) view.findViewById(R.id.rb_item);

                rb_item.setTag(MonthlyArray.get(i).get("Quantity"));

                tv_quantity.setText(MonthlyArray.get(i).get("Quantity"));
                /*tv_your_cost.setText(formatedCurrency("$" + MonthlyArray.get(i).get("Price").replaceAll("\\.0000", ".00")));
                tv_reg_cost.setText(formatedCurrency("$" + MonthlyArray.get(i).get("OrgPrice").replaceAll("\\.0", ".00")));
                tv_saving.setText(formatedCurrency("$" + MonthlyArray.get(i).get("Saving").replaceAll("\\.0", ".00")));*/
                tv_your_cost.setText(formatedCurrency("$" + MonthlyArray.get(i).get("Price").replaceAll("\\.0000", "")));
                tv_reg_cost.setText(formatedCurrency("$" + MonthlyArray.get(i).get("OrgPrice").replaceAll("\\.0", "")));
                tv_saving.setText(formatedCurrency("$" + MonthlyArray.get(i).get("Saving").replaceAll("\\.0", "")));

                rb_item.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (rb_item.isChecked()) {

                            selectedMonthlyPlanArray.clear();
                            HashMap<String, String> hashmap = new HashMap<String, String>();

                            if (selectedMonthlyPlanArray.size() == 0) {
                                hashmap.put("Quantity", tv_quantity.getText().toString());
                                hashmap.put("Price", tv_your_cost.getText().toString());
                                hashmap.put("LessonType", tv_lession_type.getText().toString());
                                tempMonthlyArray = AppConfiguration.myCartMonthlyPlanArray;
                                selectedMonthlyPlanArray.add(hashmap);
                                selectedMonthlyPlanArray.addAll(tempMonthlyArray);
                                Log.i("MonthlyArray--##", "" + selectedMonthlyPlanArray);
                            }
                            mQuantity = tv_quantity.getText().toString();
                            mPrice = tv_quantity.getText().toString();
                            checkedMonthlyValue = tv_quantity.getText().toString();
                            getChildMonthly();
                        }
                    }
                });

                lay_inflatemothlyplan.addView(view);

            }
        }
    }

    String checkedMonthlyValue = "";

    public void getChildMonthly() {
        if (lay_inflatemothlyplan.getChildCount() > 0) {
            for (int i = 0; i < lay_inflatemothlyplan.getChildCount(); i++) {
                View view = lay_inflatemothlyplan.getChildAt(i);
                LinearLayout superLay = (LinearLayout) view;
                View view2 = superLay.getChildAt(0);
                LinearLayout lay_list_item = (LinearLayout) view2;
                View view3 = lay_list_item.getChildAt(0);
                LinearLayout rgLay = (LinearLayout) view3;
                View view4 = rgLay.getChildAt(0);
                if (view4 instanceof RadioButton) {
                    RadioButton rr = (RadioButton) view4;
                    if (!rr.getTag().toString().equalsIgnoreCase(checkedMonthlyValue)) {
                        rr.setChecked(false);
                    }
                }
            }
        }
    }

//	Submit Package

    private class swimlessonPackageSubmit extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(BuyMoreSwimLession2.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
//			pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub


            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", "" + token);
            param.put("FamilyID", "" + familyID);
            param.put("SiteID", siteID);
//            param.put("BasketID", AppConfiguration.Basketid);
            param.put("BasketID", AppConfiguration.BasketID);
            param.put("LessonID", LessinTypeId);
            param.put("PackageID", selectedPackageArray.get(0).get("PackageID"));
            Log.e("param---", "" + param);
            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.swimsubmit, param);

            SubmitSwimLesson(responseString);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            /*if(data_load_submit.toString().equalsIgnoreCase("True")){
                if(submited){
					//					Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), ViewCartActivity.class);
					startActivity(i);
					finish();
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "Not able to add in cart,  Please try after sometime.",Toast.LENGTH_LONG).show();
			}*/
        }
    }

    public void SubmitSwimLesson(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            data_load_submit = reader.getString("Success");
            if (data_load_submit.toString().equals("True")) {
                submited = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Boolean submited = false;

    public static String formatedCurrency(String amountData) {

        double amount = Double.parseDouble(amountData.replace("$", ""));
//        DecimalFormat formatter2 = new DecimalFormat("$#,###,###.00");
        DecimalFormat formatter2 = new DecimalFormat("$#,###,###");
        String formatedAmount = formatter2.format(amount);

        return formatedAmount;
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//        Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession.class);
//        startActivity(i);
        finish();
    }

}
