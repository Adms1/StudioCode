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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.waterworks.DashBoardActivity;
import com.waterworks.HelpAndSupportFragment;
import com.waterworks.R;
import com.waterworks.SwimcompititionRegisterStep5Activity;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rakesh Tiwari ADMS on 2/1/2016.
 */
public class LocationName extends FragmentActivity implements OnMapReadyCallback {

    RelativeLayout title_black;
    Button returnStack;
    String TAG = "Location Name";
    TextView page_title,tv_privacy_policy,tv_remove_location,tv_phone,tv_loc;
//    final Geocoder geocoder = new Geocoder(LocationName.this);
    CardView btn_remove_location;
    Address address;
    double longitude;
    Marker customMarker;
    double latitude;
    SupportMapFragment mapFragment;
    LatLng position;
    String regionLocationAddress,siteName,msg,siteid,success,phoneAdd,Address1;
    private GoogleMap mMap;
    String token, familyID;
    Boolean isInternetPresent=false;
    Context mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(LocationName.this);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        siteName= AppConfiguration.siteName;
        siteid= AppConfiguration.siteidAdd;
        phoneAdd= AppConfiguration.phoneAdd;
        Address1=AppConfiguration.Address1;
        Log.d(TAG, "siteid=" + siteid + "\n siteName=" + siteName+"\n phoneAdd=" + phoneAdd);
        regionLocationAddress= AppConfiguration.regionLocationAddress;
        siteName= AppConfiguration.siteName;
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        View view = (View)findViewById(R.id.header);
        title_black = (RelativeLayout)view.findViewById(R.id.title_black);
        title_black.setVisibility(View.GONE);
        returnStack=(Button)view.findViewById(R.id.returnStack);
        page_title=(TextView)view.findViewById(R.id.page_title);
        tv_privacy_policy=(TextView)findViewById(R.id.tv_privacy_policy);
        btn_remove_location=(CardView)findViewById(R.id.btn_remove_location);
        tv_remove_location=(TextView)findViewById(R.id.tv_remove_location);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_loc= (TextView) findViewById(R.id.tv_loc);

        tv_loc.setText(regionLocationAddress);
        fn_permission_Location();
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(LocationName.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        isInternetPresent = Utility.isNetworkConnected(LocationName.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new fetchLatLongFromService(regionLocationAddress).execute();
        }
        tv_remove_location.setText("Add Location ");
        page_title.setText(siteName);
        tv_phone.setText(phoneAdd);
        returnStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edited by alka
                Intent i=new Intent(LocationName.this,Locations.class);
                startActivity(i);
                Log.d("Location call--", "" + "Location data back call");
            }
        });


        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = tv_phone.getText().toString().replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
                startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+1" + phoneNumber)));
            }
        });
        btn_remove_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = Utility.isNetworkConnected(LocationName.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new InsertLocationAsyncTask().execute();
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

    boolean isBoolean_permission_Fine_Location = false;
    boolean isBoolean_permission_coarse_Location = false;
    public static final int REQUEST_PERMISSIONS_FINE_LOCATION = 1;
    public static final int REQUEST_PERMISSIONS_COARSE_LOCATION = 2;

    private void fn_permission_Location() {
        if ((ContextCompat.checkSelfPermission(LocationName.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(LocationName.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(LocationName.this, Manifest.permission.ACCESS_FINE_LOCATION))
                    ||(ActivityCompat.shouldShowRequestPermissionRationale(LocationName.this, Manifest.permission.ACCESS_COARSE_LOCATION))) {
            } else {
                ActivityCompat.requestPermissions(LocationName.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_FINE_LOCATION);
                ActivityCompat.requestPermissions(LocationName.this,
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
                    Toast.makeText(LocationName.this, "Please allow the permission", Toast.LENGTH_LONG).show();
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

    // Insert Location
    class InsertLocationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LocationName.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            InserteLoction();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if(success.equalsIgnoreCase("True")){
                Toast.makeText(LocationName.this,"Location Added Successfully!",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LocationName.this,Locations.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(LocationName.this,msg,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void InserteLoction() {
        HashMap<String, String> param = new HashMap<String, String>();
        Log.e("Token--&%-", token);
        Log.e("siteid--&%-", siteid);
        param.put("Token", token);
        param.put("siteid", siteid);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN+AppConfiguration.GetInsertSiteFromFamily, param);
        readAndParseLOcJSON(responseString);

    }

    public void readAndParseLOcJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            success=reader.getString("Success");
            if(success.equalsIgnoreCase("True")){
                msg=reader.getString("Msg");
            }
            else {
                msg=reader.getString("Msg");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void setUpMap() {

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
        numTxt.setText(Address1);

        customMarker = mMap.addMarker(new MarkerOptions()
                .position(position)

                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    public class fetchLatLongFromService extends
            AsyncTask<Void, Void, StringBuilder> {
        String place;



        public fetchLatLongFromService(String place) {
            super();
            this.place = place;

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="
                        + this.place.replaceAll(" ", "%20") + "&sensor=false";

                Log.e("googleMapUrl--",""+googleMapUrl);
                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                String a = "";
                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                JSONObject before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0);

                JSONObject geometry_jsonObj = before_geometry_jsonObj
                        .getJSONObject("geometry");

                JSONObject location_jsonObj = geometry_jsonObj
                        .getJSONObject("location");

                String lat_helper = location_jsonObj.getString("lat");
                latitude = Double.valueOf(lat_helper);


                String lng_helper = location_jsonObj.getString("lng");
                longitude = Double.valueOf(lng_helper);


                position = new LatLng(latitude, longitude);
                Log.e("Positon-2-latLong",""+position);
                setUpMap();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            try {
                position = new LatLng(latitude,longitude);
                setUpMap();
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(regionLocationAddress));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }
}
