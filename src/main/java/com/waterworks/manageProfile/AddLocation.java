package com.waterworks.manageProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.DashBoardActivity;
import com.waterworks.HelpAndSupportFragment;
import com.waterworks.R;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rakesh Tiwari ADMS on 2/1/2016.
 */

public class AddLocation extends Activity {
    RelativeLayout title_black, rl_current_location;
    Button returnStack, btnHome;
    ArrayAdapter<String> dataAdapter1;
    TextView page_title, tv_privacy_policy, tv_add_new_loc, current_location_title;
    //    ListView lv_location_add;
    Spinner spinner_region;
    Context mContext = this;
    LinearLayout ll_region, l1_location_add;
    ;
    ArrayList<HashMap<String, String>> regionArrList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> siteListByRegion = new ArrayList<HashMap<String, String>>();
    ArrayList<String> regionList = new ArrayList<String>();
    String RegionID;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        View view = (View) findViewById(R.id.header);
        title_black = (RelativeLayout) view.findViewById(R.id.title_black);
        title_black.setVisibility(View.GONE);
        returnStack = (Button) view.findViewById(R.id.returnStack);
        page_title = (TextView) view.findViewById(R.id.page_title);
        tv_privacy_policy = (TextView) findViewById(R.id.tv_privacy_policy);
        tv_add_new_loc = (TextView) findViewById(R.id.tv_add_new_loc);
        current_location_title = (TextView) findViewById(R.id.current_location_title);
        rl_current_location = (RelativeLayout) findViewById(R.id.rl_current_location);
        ll_region = (LinearLayout) findViewById(R.id.ll_region);
        l1_location_add = (LinearLayout) findViewById(R.id.l1_location_add);
        spinner_region = (Spinner) findViewById(R.id.spinner_region);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(AddLocation.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        current_location_title.setText("Choose Region");
        page_title.setText("Add Location");
        ll_region.setVisibility(View.VISIBLE);
        tv_add_new_loc.setVisibility(View.GONE);
        rl_current_location.setVisibility(View.GONE);

        isInternetPresent = Utility.isNetworkConnected(AddLocation.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new RegionListAsyncTask().execute();
        }
        returnStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
// Get region list

    class RegionListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AddLocation.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadRegionList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            dataAdapter1 = new ArrayAdapter<String>(AddLocation.this, android.R.layout.simple_spinner_item, regionList);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_region.setAdapter(dataAdapter1);

            spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                    Log.d("regionArrList--$-1", "" + regionArrList.toString());
                    Log.d("regionId--#-1", "" + regionArrList.get(0).get("RegionID"));
                    Log.d("regionList--#-1", "" + regionList);
                    spinner_region.setPrompt(regionList.get(position));
                    RegionID = regionArrList.get(position).get("RegionID");
                    Log.e("regionArrList---$", "" + regionArrList);
                    Log.e("regionArrList--size-$", "" + regionArrList.size());
                    Log.e("regionList--size-$", "" + regionList.size());
                    Log.e("RegionID---$", RegionID);
                    new RegionLocationAsyncTask().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }

    public void loadRegionList() {
        regionArrList.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", "0");

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetRegionList, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            HashMap<String, String> hashmap;
            hashmap = new HashMap<String, String>();
            hashmap.put("RegionID", "0");
            hashmap.put("RegionName", "Please Select");
            regionArrList.add(hashmap);

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("Sites");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                hashmap = new HashMap<String, String>();

                hashmap.put("RegionID", jsonChildNode.getString("RegionID"));
                hashmap.put("RegionName", jsonChildNode.getString("RegionName"));
                regionList.add("" + jsonChildNode.getString("RegionName"));
                regionArrList.add(hashmap);

            }
            regionList.add(0, "Please Select");
            Log.d("regionArrList--#-2", "" + regionArrList.toString());
            Log.d("regionId--#-2", "" + regionArrList.get(0).get("RegionID"));
            Log.d("regionList--#-2", "" + regionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Get Location according to region id

    class RegionLocationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("regionArrList-clear", "" + regionArrList);
            pd = new ProgressDialog(AddLocation.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadRegionLoction();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            inflateLocation(siteListByRegion);
        }
    }

    public void loadRegionLoction() {

        HashMap<String, String> param = new HashMap<String, String>();
        Log.e("RegionID---5", RegionID);
        param.put("RegionID", RegionID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetSiteListByRegion, param);
        readAndParseLOcJSON(responseString);
    }

    public void readAndParseLOcJSON(String in) {
        try {
            siteListByRegion.clear();
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("Sites");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("SiteID"));
                hashmap.put("SiteName", jsonChildNode.getString("SiteName"));
                hashmap.put("Address1", jsonChildNode.getString("Address1"));
                hashmap.put("Address2", jsonChildNode.getString("Address2"));
                hashmap.put("City", jsonChildNode.getString("City"));
                hashmap.put("State", jsonChildNode.getString("State"));
                hashmap.put("ZipCode", jsonChildNode.getString("ZipCode"));
                hashmap.put("Phone", jsonChildNode.getString("Phone"));
                Log.d("Address1--#-1", "" + jsonChildNode.getString("Address1"));
                siteListByRegion.add(hashmap);
            }
            Log.d("siteListByRegion--#-1", "" + siteListByRegion);
            Log.d("regionList--#-1", "" + regionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void inflateLocation(ArrayList<HashMap<String, String>> siteListByRegion) {
        final ArrayList<HashMap<String, String>> siteListByRegion1;
        siteListByRegion1 = siteListByRegion;
        l1_location_add.removeAllViews();
        if (siteListByRegion1.size() > 0) {

            for (int i = 0; i < siteListByRegion1.size(); i++) {
                final int pos = i;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.region_bylist_item, null);

                TextView tv_current_location = (TextView) view.findViewById(R.id.tv_current_location);
                TextView tv_add = (TextView) view.findViewById(R.id.tv_add);
                ImageView imgv_arr = (ImageView) view.findViewById(R.id.imgv_arr);
                tv_add.setVisibility(View.VISIBLE);
                imgv_arr.setVisibility(View.GONE);
                tv_current_location.setText(Html.fromHtml("<b>" + siteListByRegion1.get(i).get("SiteName").toString().split("@")[0] + "</b>" + " • " + siteListByRegion1.get(i).get("Address1")));
                l1_location_add.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConfiguration.regionLocationAddress = siteListByRegion1.get(pos).get("Address1") + "" + "\n" + siteListByRegion1.get(pos).get("City") + ", " + siteListByRegion1.get(pos).get("State") + " " + siteListByRegion1.get(pos).get("ZipCode");
                        AppConfiguration.siteName = siteListByRegion1.get(pos).get("SiteName");
                        AppConfiguration.siteidAdd = siteListByRegion1.get(pos).get("SiteID");
                        AppConfiguration.phoneAdd = siteListByRegion1.get(pos).get("Phone");
                        AppConfiguration.Address1 = siteListByRegion1.get(pos).get("Address1");
                        Log.e("LocationAddress--1", "" + AppConfiguration.regionLocationAddress);
                        Intent i = new Intent(mContext, LocationName.class);
                        startActivity(i);
                    }
                });

            }
        }

    }


}
