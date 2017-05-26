package com.waterworks.manageProfile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.ChangeEmailAddress2;
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
public class Locations extends Activity {

    String TAG = "Location";
    RelativeLayout title_black,rl_current_location;
    Button returnStack;
    TextView page_title,tv_privacy_policy,tv_add_new_loc;
    String token, familyID;
    ArrayList<HashMap<String, String>> getSitebyFamily = new ArrayList<HashMap<String, String>>();
    Context mContext = Locations.this;
//    ListView lv_location_add;
    LinearLayout l1_location_add;
    Button btnHome;
    Boolean isInternetPresent=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(Locations.this);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        View view = (View)findViewById(R.id.header);
        title_black = (RelativeLayout)view.findViewById(R.id.title_black);
        rl_current_location= (RelativeLayout)findViewById(R.id.rl_current_location);
        l1_location_add=(LinearLayout)findViewById(R.id.l1_location_add);
        title_black.setVisibility(View.GONE);
        returnStack=(Button)view.findViewById(R.id.returnStack);
        page_title=(TextView)view.findViewById(R.id.page_title);
        tv_add_new_loc = (TextView) findViewById(R.id.tv_add_new_loc);
        fn_permission_Location();

        page_title.setText("Locations");
        returnStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Locations.this,d2_MyProfile.class);
                startActivity(i);
                finish();
            }
        });

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(Locations.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        tv_add_new_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(Locations.this,AddLocation.class);
                startActivity(i);
            }
        });
        rl_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Locations.this,LocationMap.class);
                startActivity(i);
            }
        });
        isInternetPresent = Utility.isNetworkConnected(Locations.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new SiteAddressByFamilyAsyncTask().execute();
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

    boolean isBoolean_permission_Fine_Location = false;
    boolean isBoolean_permission_coarse_Location = false;
    public static final int REQUEST_PERMISSIONS_FINE_LOCATION = 1;
    public static final int REQUEST_PERMISSIONS_COARSE_LOCATION = 2;

    private void fn_permission_Location() {
        if ((ContextCompat.checkSelfPermission(Locations.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(Locations.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Locations.this, Manifest.permission.ACCESS_FINE_LOCATION))
                    ||(ActivityCompat.shouldShowRequestPermissionRationale(Locations.this, Manifest.permission.ACCESS_COARSE_LOCATION))) {
            } else {
                ActivityCompat.requestPermissions(Locations.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_FINE_LOCATION);
                ActivityCompat.requestPermissions(Locations.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_COARSE_LOCATION);

            }
        } else {
            isBoolean_permission_Fine_Location= true;
            isBoolean_permission_coarse_Location =true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isBoolean_permission_Fine_Location = true;
                    isBoolean_permission_coarse_Location =true;
                } else {
                    Toast.makeText(Locations.this, "Please allow the permission", Toast.LENGTH_LONG).show();
                }
                fn_permission_Location();
            }
        }
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

    // Get site address by family
    class SiteAddressByFamilyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Locations.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            getSitebyFamily.clear();
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
            inflateLocation(getSitebyFamily);
        }
    }

    public void loadRegionLoction() {
        HashMap<String, String> param = new HashMap<String, String>();
        Log.e("Token---", token);
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALesssionSiteListURL, param);
        readAndParseLOcJSON(responseString);
    }

    public void readAndParseLOcJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("SiteList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String>   hashmap = new HashMap<String, String>();

                    hashmap.put("siteid", jsonChildNode.getString("siteid"));
                hashmap.put("sitename", jsonChildNode.getString("sitename"));
                hashmap.put("Lafitness", jsonChildNode.getString("Lafitness"));
                hashmap.put("Address1", jsonChildNode.getString("Address1"));
                hashmap.put("Address2", jsonChildNode.getString("Address2"));
                hashmap.put("City", jsonChildNode.getString("City"));
                hashmap.put("State", jsonChildNode.getString("State"));
                hashmap.put("ZipCode", jsonChildNode.getString("ZipCode"));
                hashmap.put("Phone", jsonChildNode.getString("Phone"));
                Log.d("Address1--#-Loc", "" + jsonChildNode.getString("Address1"));

                getSitebyFamily.add(hashmap);
            }
            Log.d("siteListByRegion--#-Loc", "" + getSitebyFamily);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void inflateLocation(ArrayList<HashMap<String, String>> getSitebyFamily) {
       final ArrayList<HashMap<String, String>> getSitebyFamily1 ;
        getSitebyFamily1=getSitebyFamily;
        if(getSitebyFamily.size()>0){

            for(int i=0;i<getSitebyFamily1.size();i++){
               final int pos=i;
                LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View view= inflater.inflate(R.layout.region_bylist_item, null);

                TextView tv_current_location=(TextView)view.findViewById(R.id.tv_current_location);
                TextView tv_add=(TextView)view.findViewById(R.id.tv_add);
                ImageView imgv_arr=(ImageView)view.findViewById(R.id.imgv_arr);
                tv_add.setVisibility(View.GONE);
                imgv_arr.setVisibility(View.VISIBLE);
                tv_current_location.setText(Html.fromHtml("<b>"+getSitebyFamily1.get(i).get("sitename").toString().split("@")[0]+"</b>"+" • "+getSitebyFamily1.get(i).get("Address1")+" • "+getSitebyFamily1.get(i).get("City")+", "+getSitebyFamily1.get(i).get("State")));
                l1_location_add.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConfiguration.regionLocationAddress=getSitebyFamily1.get(pos).get("Address1")+"\n"+getSitebyFamily1.get(pos).get("City")+", "+getSitebyFamily1.get(pos).get("State")+" "+getSitebyFamily1.get(pos).get("ZipCode");
                        AppConfiguration.siteName=getSitebyFamily1.get(pos).get("sitename");
                        AppConfiguration.siteid=getSitebyFamily1.get(pos).get("siteid");
                        AppConfiguration.phone=getSitebyFamily1.get(pos).get("Phone");
                        AppConfiguration.Address1=getSitebyFamily1.get(pos).get("Address1");
                        Log.e("LocationAddress--1",""+AppConfiguration.regionLocationAddress);
                        Intent i=new Intent(mContext,LocationMap.class);
                        startActivity(i);
                        finish();
                    }
                });

            }
        }
    }

}
