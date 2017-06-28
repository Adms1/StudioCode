package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class UpcomingDivesAndTurns extends Activity {

    LinearLayout list_registration, get_upcoming;
    ImageButton ib_back;
    String SwimCampData = "False", Title, Sc_title;
    String token, familyID;
    Context mContext = this;
    TextView title, txt_register, txt_checkin, upcoming_title, already_title;
    ArrayList<String> _Name, _StartDate, _EndDate, _Capacity, Get_Name, Get_StartDate, Get_EndDate, Get_Capacity;
    Button register_btn;
    View sep1, sep2;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_competition);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        init();
    }

    public void init() {
        View view = findViewById(R.id.layout_top);
        ib_back = (ImageButton) view.findViewById(R.id.ib_back);
        list_registration = (LinearLayout) findViewById(R.id.list_registration);
        title = (TextView) findViewById(R.id.title);
        txt_checkin = (TextView) findViewById(R.id.txt_checkin);
        txt_register = (TextView) findViewById(R.id.txt_register);
        get_upcoming = (LinearLayout) findViewById(R.id.get_upcoming);
        register_btn = (Button) findViewById(R.id.register_btn);
        sep1 = (View) findViewById(R.id.sep1);
        sep2 = (View) findViewById(R.id.sep2);
        already_title = (TextView) findViewById(R.id.already_title);
        upcoming_title = (TextView) findViewById(R.id.upcoming_title);

        sep1.setVisibility(View.GONE);
        sep2.setVisibility(View.VISIBLE);

        txt_checkin.setVisibility(View.GONE);
        txt_register.setVisibility(View.VISIBLE);
        isInternetPresent = Utility.isNetworkConnected(UpcomingDivesAndTurns.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new GetUpcomingDivesAndTurns().execute();
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
        // TODO Auto-generated method stub
        super.onResume();

        ib_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        txt_register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, DivesAndTurnsRegi1Activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    class GetUpcomingDivesAndTurns extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(UpcomingDivesAndTurns.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("siteid", AppConfiguration.basket_siteid);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_UpcomingDivesandTurns, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                SwimCampData = reader.getString("Success");
                Title = reader.getString("Title");

                if (SwimCampData.toString().equals("True")) {

                    _Name = new ArrayList<String>();
                    _Capacity = new ArrayList<String>();
                    _EndDate = new ArrayList<String>();
                    _StartDate = new ArrayList<String>();

                    JSONArray jsonMainNode = reader
                            .optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        _Name.add(jsonChildNode.getString("Name"));
                        _Capacity.add(jsonChildNode.getString("Capacity"));
                        _StartDate.add(jsonChildNode.getString("Start Date"));
                        _EndDate.add(jsonChildNode.getString("Time"));
                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (SwimCampData.equals("True")) {
                if (_Name.size() > 0) {
                    setLayout_Upcoming();
                }
            } else {
            }
            upcoming_title.setText(Title);
            isInternetPresent = Utility.isNetworkConnected(UpcomingDivesAndTurns.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new Get_Divesandturnsregistrations().execute();
            }
            pd.dismiss();
        }
    }

    class Get_Divesandturnsregistrations extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(UpcomingDivesAndTurns.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("siteid", AppConfiguration.basket_siteid);
            param.put("FamilyID", familyID);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_Divesandturnsregistrations, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                SwimCampData = reader.getString("Success");
                Sc_title = reader.getString("Title");

                if (SwimCampData.toString().equals("True")) {
                    Get_Capacity = new ArrayList<String>();
                    Get_EndDate = new ArrayList<String>();
                    Get_Name = new ArrayList<String>();
                    Get_StartDate = new ArrayList<String>();

                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        Get_Name.add(jsonChildNode.getString("Name"));
                        Get_Capacity.add(jsonChildNode.getString("Capacity"));
                        Get_StartDate.add(jsonChildNode.getString("Start Date"));
                        Get_EndDate.add(jsonChildNode.getString("Time"));
                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (SwimCampData.equals("True")) {
                if (Get_Name.size() > 0) {
                    SetLayout();
                }
            } else {
            }
            already_title.setText(Sc_title);
            pd.dismiss();
        }
    }

    public void setLayout_Upcoming() {

        for (int position = 0; position < _Name.size(); position++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_swim_static_info, null);
            TextView Name = (TextView) view.findViewById(R.id.participant);
            TextView capacity = (TextView) view.findViewById(R.id.date);
            TextView start_date = (TextView) view.findViewById(R.id.location);
            TextView end_date = (TextView) view.findViewById(R.id.event);
            TextView age_group = (TextView) view.findViewById(R.id.age_group);
            TextView stroke = (TextView) view.findViewById(R.id.stroke);
            TextView distance = (TextView) view.findViewById(R.id.distance);

            age_group.setVisibility(View.GONE);
            stroke.setVisibility(View.GONE);
            distance.setVisibility(View.GONE);

            Name.setText(Html.fromHtml("<b>Name: </b>" + _Name.get(position)));
            capacity.setText(Html.fromHtml("<b>Capacity: </b>" + _Capacity.get(position)));
            start_date.setText(Html.fromHtml("<b>Start Date: </b>" + _StartDate.get(position)));
            end_date.setText(Html.fromHtml("<b>Time: </b>" + _EndDate.get(position)));

            get_upcoming.addView(view);
        }
    }

    public void SetLayout() {
        for (int position = 0; position < Get_Name.size(); position++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_swim_static_info, null);
            TextView Name = (TextView) view.findViewById(R.id.participant);
            TextView capacity = (TextView) view.findViewById(R.id.date);
            TextView start_date = (TextView) view.findViewById(R.id.location);
            TextView end_date = (TextView) view.findViewById(R.id.event);
            TextView age_group = (TextView) view.findViewById(R.id.age_group);
            TextView stroke = (TextView) view.findViewById(R.id.stroke);
            TextView distance = (TextView) view.findViewById(R.id.distance);

            age_group.setVisibility(View.GONE);
            stroke.setVisibility(View.GONE);
            distance.setVisibility(View.GONE);

            Name.setText(Html.fromHtml("<b>Name: </b>" + Get_Name.get(position)));
            capacity.setText(Html.fromHtml("<b>Capacity: </b>" + Get_Capacity.get(position)));
            start_date.setText(Html.fromHtml("<b>Start Date: </b>" + Get_StartDate.get(position)));
            end_date.setText(Html.fromHtml("<b>Time: </b>" + Get_EndDate.get(position)));

            list_registration.addView(view);
        }
    }

}

