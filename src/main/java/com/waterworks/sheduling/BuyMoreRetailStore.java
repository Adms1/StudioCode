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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class BuyMoreRetailStore extends Activity {
    Button BackButton, btn_viewCart, btn_sites, btn_sites_category;
    // LinearLayout scdl_lsn,scdl_mkp,waitlist;
    LinearLayout swimLsn, retailStore, otherPrograms, sites_lay, category_lay, lay_swim_caps, lay_goggles,
            sites_lay_dropdown, lay_thermalsuit;
    View selected_1, selected_2, selected_3, vw_dropdown;
    TextView txt_1, txt_2, txt_3, textView_goggles, textView_swimcaps, textView_swimsuit;
    Context mContext = this;
    String token, familyID, showcategroy1, showcategroy2, type;
    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> categoryList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> siteName = new ArrayList<String>();
    ArrayList<String> category = new ArrayList<String>();

    ListPopupWindow lpw_sitelist, lpw_categorylist;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more_retail_store);
        AppConfiguration.cartBackScreen = 1;
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        selected_1.setVisibility(View.GONE);
        selected_2.setVisibility(View.VISIBLE);
        selected_3.setVisibility(View.GONE);

        ((AnimationDrawable) selected_2.getBackground()).start();
        init();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                isInternetPresent = Utility.isNetworkConnected(BuyMoreRetailStore.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    InitialRequests();
                }
            }
        }, 1000);
        typeFace();
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
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void InitialRequests() {

        try {
            new SitesListAsyncTask().execute();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        BackButton.setTypeface(regular);
        btn_sites.setTypeface(regular);
        btn_sites_category.setTypeface(regular);
    }

    public void init() {

        View view = findViewById(R.id.schdl_top);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        lay_swim_caps = (LinearLayout) findViewById(R.id.lay_swim_caps);
        lay_goggles = (LinearLayout) findViewById(R.id.lay_goggles);
        lay_thermalsuit = (LinearLayout) findViewById(R.id.lay_thermalsuit);
        sites_lay_dropdown = (LinearLayout) findViewById(R.id.sites_lay_dropdown);
        lpw_sitelist = new ListPopupWindow(mContext);
        lpw_categorylist = new ListPopupWindow(mContext);
        sites_lay = (LinearLayout) findViewById(R.id.sites_lay);
        category_lay = (LinearLayout) findViewById(R.id.category_lay);
        btn_sites = (Button) findViewById(R.id.btn_sites);
        btn_sites_category = (Button) findViewById(R.id.btn_sites_category);
        textView_goggles = (TextView) findViewById(R.id.textView_goggles);
        textView_swimcaps = (TextView) findViewById(R.id.textView_swimcaps);
        textView_swimsuit = (TextView) findViewById(R.id.textView_swimsuit);
        vw_dropdown = (View) findViewById(R.id.vw_dropdown);

        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        category.add("Goggles");
        category.add("Swim Caps");
        Log.e("category-----**", "" + category);

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // BuyMoreRetailStore.this.finish();
//				Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession.class);
//				startActivity(i);

                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreRetailStore.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                startActivity(i);
            }
        });

        sites_lay_dropdown.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {

                lpw_sitelist.show();

            }
        });
        btn_sites.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {

                lpw_sitelist.show();
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
                BuyMoreRetailStore.this.overridePendingTransition(0, 0);
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
                BuyMoreRetailStore.this.overridePendingTransition(0, 0);
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
                BuyMoreRetailStore.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

        lay_thermalsuit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyMoreRetailStore.this, BuyMoreRetailStoreProductDetail.class);
                startActivity(intent);
                AppConfiguration.buyMoreRetailProductTypeID = "1";
            }
        });

        lay_swim_caps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyMoreRetailStore.this, BuyMoreRetailStoreProductList.class);
                startActivity(intent);
                AppConfiguration.buyMoreRetailProductTypeID = "3";
            }
        });

        lay_goggles.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyMoreRetailStore.this, BuyMoreRetailStoreProductList.class);
                startActivity(intent);
                AppConfiguration.buyMoreRetailProductTypeID = "2";
            }
        });

    }

    class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {
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
            loadSitesList();

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
            // new GetStartTimeRange().execute();

            if (siteName.size() == 1) {
                AppConfiguration.salStep1SiteID = siteMainList.get(0).get("SiteID");
                sites_lay.setVisibility(View.GONE);
                sites_lay_dropdown.setVisibility(View.GONE);
                // category_lay.setVisibility(View.GONE);
                lay_thermalsuit.setVisibility(View.GONE);
                textView_swimsuit.setVisibility(View.GONE);
                /*
				 * if(type=="0"){ type="0"; new
				 * productCategorytAsyncTask().execute(); } else if(type=="1"){
				 * type="1"; new productCategorytAsyncTask().execute(); }
				 */
                isInternetPresent = Utility.isNetworkConnected(BuyMoreRetailStore.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new productCategorytAsyncTask().execute();
                }
            } else {

                sites_lay.setVisibility(View.VISIBLE);
                sites_lay_dropdown.setVisibility(View.VISIBLE);
//				lay_thermalsuit.setVisibility(View.VISIBLE);
                lpw_sitelist.setAdapter(new ArrayAdapter<String>(mContext, R.layout.edittextpopup, siteName));
                lpw_sitelist.setAnchorView(sites_lay_dropdown);
                lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);

                // lpw_sitelist.setWidth(LayoutParams.WRAP_CONTENT);
                lpw_sitelist.setModal(true);
                lpw_sitelist.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                        AppConfiguration.salStep1SiteID = siteMainList.get(pos).get("SiteID");
                        btn_sites.setText(siteMainList.get(pos).get("SiteName"));
                        lpw_sitelist.dismiss();
                        isInternetPresent = Utility.isNetworkConnected(BuyMoreRetailStore.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new productCategorytAsyncTask().execute();
                        }
                    }
                });

				/*
				 * sites_lay.setVisibility(View.VISIBLE);
				 * lay_thermalsuit.setVisibility(View.VISIBLE);
				 * lpw_sitelist.setAdapter(new ArrayAdapter<String>(mContext,
				 * R.layout.edittextpopup, siteName));
				 * lpw_sitelist.setAnchorView(btn_sites);
				 * lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
				 * lpw_sitelist.setModal(true);
				 * lpw_sitelist.setOnItemClickListener(new OnItemClickListener()
				 * {
				 * 
				 * @Override public void onItemClick(AdapterView<?> parent, View
				 * view, int pos, long id) { AppConfiguration.salStep1SiteID =
				 * siteMainList.get(pos).get("SiteID");
				 * btn_sites.setText(siteMainList.get(pos).get("SiteName"));
				 * 
				 * lpw_sitelist.dismiss(); if (type == "0") { type = "0"; new
				 * productCategorytAsyncTask().execute(); } if (type == "1") {
				 * type = "1"; new productCategorytAsyncTask().execute(); } new
				 * productCategorytAsyncTask().execute(); //
				 * category_lay.setVisibility(View.GONE); } });
				 */
            }

        }
    }

    public void loadSitesList() {
        // siteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALesssionSiteListURL, param);

        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("SiteList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("siteid"));
                hashmap.put("SiteName", jsonChildNode.getString("sitename"));
                hashmap.put("LafitNess", jsonChildNode.getString("Lafitness"));

                siteName.add("" + jsonChildNode.getString("sitename"));
                siteMainList.add(hashmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Product category...

    class productCategorytAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
//			pd.show();

            // siteMainList.clear();
            // siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadCategoryList();

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

            if (showcategroy1 == "false") {
                Log.e("category false--**", showcategroy1);
                lay_goggles.setVisibility(View.GONE);
                textView_goggles.setVisibility(View.GONE);
            }
            // else if (showcategroy == "true")
            else {
                Log.e("category true--**", showcategroy1);
                lay_goggles.setVisibility(View.VISIBLE);
                textView_goggles.setVisibility(View.VISIBLE);
            }

            if (showcategroy2 == "false") {
                Log.e("category false--**", showcategroy2);
                lay_swim_caps.setVisibility(View.GONE);
                textView_swimcaps.setVisibility(View.GONE);
            }
            // else if (showcategroy == "true")
            else {
                Log.e("category true--**", showcategroy2);
                lay_swim_caps.setVisibility(View.VISIBLE);
                textView_swimcaps.setVisibility(View.VISIBLE);
            }
            //thermal should always be visible for anysite
            lay_thermalsuit.setVisibility(View.VISIBLE);
            textView_swimsuit.setVisibility(View.VISIBLE);
        }
    }

    public void loadCategoryList() {
        // siteMainList.clear();

        Log.e("AppConfiguration.salStep1SiteID---***", "" + AppConfiguration.salStep1SiteID);

        HashMap<String, String> param = new HashMap<String, String>();

        Log.e("category-type-id--***", "" + type);
        param.put("type", "0");

        param.put("Token", token);

        param.put("SiteID", AppConfiguration.salStep1SiteID);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.ShowHideProductCategory, param);

        readCategoryParseJSON(responseString, "0");

        param.put("type", "1");

        param.put("Token", token);

        param.put("SiteID", AppConfiguration.salStep1SiteID);

        String responseString1 = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.ShowHideProductCategory, param);

        readCategoryParseJSON(responseString1, "1");
    }

    public void readCategoryParseJSON(String in, String temp) {
        try {

            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");
            if (successStr.equalsIgnoreCase("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("ShowCategory", jsonChildNode.getString("ShowCategory"));
                    if (temp == "0") {
                        showcategroy1 = jsonChildNode.getString("ShowCategory");
                        Log.e("ShowCategory--***", "" + showcategroy1);
                    } else {
                        showcategroy2 = jsonChildNode.getString("ShowCategory");
                        Log.e("ShowCategory--***", "" + showcategroy2);
                    }

                    categoryList.add(hashmap);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession.class);
        startActivity(i);
        finish();
    }

}