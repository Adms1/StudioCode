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

public class ScoutBadgeRegistration extends Activity {

    String token, familyID;
    ImageButton ib_back;
    LinearLayout list_registration, get_upcoming;
    String SwimCampData = "False", upcm_false = "", register_false = "";
    ArrayList<String> _session, _startDate, _endDate, _time, _capacity,
            _upName, _upCapacity, _upStartDate, _upEndDate;
    Context mContext = this;
    TextView empty_upcoming, empty_registration;
    Button swim_activity, swim_merit, girl_scout;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scoutbadgeregister);
        // getting token
        SharedPreferences prefs = AppConfiguration
                .getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        init();

    }

    public void init() {
        View view = findViewById(R.id.layout_top);
        ib_back = (ImageButton) view.findViewById(R.id.ib_back);
        list_registration = (LinearLayout) findViewById(R.id.list_registration);
        get_upcoming = (LinearLayout) findViewById(R.id.get_upcoming);
        empty_upcoming = (TextView) findViewById(R.id.empty_upcoming);
        empty_registration = (TextView) findViewById(R.id.empty_registration);
        swim_activity = (Button) findViewById(R.id.swim_activity);
        swim_merit = (Button) findViewById(R.id.swim_merit);
        girl_scout = (Button) findViewById(R.id.girl_scout);

//		new GetUpcomingSB().execute();
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

        swim_activity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, ScoutBadgeActivityRegister.class);
                startActivity(i);
            }
        });

        swim_merit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, ScoutBadgeMeritRegister.class);
                startActivity(i);
            }
        });

        girl_scout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, ScoutBadgeGirlsRegister.class);
                startActivity(i);
            }
        });
    }

    class GetUpcomingSB extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ScoutBadgeRegistration.this);
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
                    .RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_UpcomingScoutPrograms, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                SwimCampData = reader.getString("Success");
                if (SwimCampData.toString().equals("True")) {

                    _upCapacity = new ArrayList<String>();
                    _upEndDate = new ArrayList<String>();
                    _upName = new ArrayList<String>();
                    _upStartDate = new ArrayList<String>();


                    JSONArray jsonMainNode = reader
                            .optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);
                        _upCapacity.add(jsonChildNode.getString("Capacity"));
                        _upEndDate.add(jsonChildNode.getString("End Date"));
                        _upName.add(jsonChildNode.getString("Name"));
                        _upStartDate.add(jsonChildNode.getString("Start Date"));

                    }
                } else {
                    upcm_false = reader.getString("Msg");
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
            isInternetPresent = Utility.isNetworkConnected(ScoutBadgeRegistration.this);
            if (isInternetPresent) {
                if (SwimCampData.equals("True")) {
                    if (_upName.size() > 0) {
                        setLayout_Upcoming();
                    }
                } else {
                    empty_upcoming.setVisibility(View.VISIBLE);
                    empty_upcoming.setText(upcm_false);
                }
                pd.dismiss();
                new GetSBRegistration().execute();
            } else {
                onDetectNetworkState();
            }
        }
    }

    public void setLayout_Upcoming() {

        for (int position = 0; position < _upName.size(); position++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_swim_static_info, null);
            TextView participant = (TextView) view.findViewById(R.id.participant);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView location = (TextView) view.findViewById(R.id.location);
            TextView event = (TextView) view.findViewById(R.id.event);
            TextView age_group = (TextView) view.findViewById(R.id.age_group);
            TextView stroke = (TextView) view.findViewById(R.id.stroke);
            TextView distance = (TextView) view.findViewById(R.id.distance);

            age_group.setVisibility(View.GONE);
            stroke.setVisibility(View.GONE);
            distance.setVisibility(View.GONE);

            participant.setText(Html.fromHtml("<b>Name: </b>" + _upName.get(position)));
            date.setText(Html.fromHtml("<b>Capacity: </b>" + _upCapacity.get(position)));
            location.setText(Html.fromHtml("<b>Start Date: </b>" + _upStartDate.get(position)));
            event.setText(Html.fromHtml("<b>End Date: </b>" + _upEndDate.get(position)));

            get_upcoming.addView(view);
        }
    }


    private class GetSBRegistration extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = new ProgressDialog(ScoutBadgeRegistration.this);
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
            param.put("FamilyID", familyID);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_ScoutBadgeRegistrations, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                SwimCampData = reader.getString("Success");
                if (SwimCampData.toString().equals("True")) {

                    _session = new ArrayList<String>();
                    _capacity = new ArrayList<String>();
                    _endDate = new ArrayList<String>();
                    _startDate = new ArrayList<String>();
                    _time = new ArrayList<String>();

                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        _session.add(jsonChildNode.getString("Session"));
                        _capacity.add(jsonChildNode.getString("Capacity"));
                        _endDate.add(jsonChildNode.getString("End Date"));
                        _startDate.add(jsonChildNode.getString("Start Date"));
                        _time.add(jsonChildNode.getString("Time"));

                    }
                } else {
                    register_false = reader.getString("Msg");
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
                if (_session.size() > 0) {
                    SetLayout();
                }
            } else {
                empty_registration.setVisibility(View.VISIBLE);
                empty_registration.setText(register_false);
            }
            pd.dismiss();
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

    public void SetLayout() {

        for (int position = 0; position < _session.size(); position++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_swim_static_info, null);
            TextView participant = (TextView) view.findViewById(R.id.participant);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView location = (TextView) view.findViewById(R.id.location);
            TextView event = (TextView) view.findViewById(R.id.event);
            TextView age_group = (TextView) view.findViewById(R.id.age_group);
            TextView stroke = (TextView) view.findViewById(R.id.stroke);
            TextView distance = (TextView) view.findViewById(R.id.distance);

            stroke.setVisibility(View.GONE);
            distance.setVisibility(View.GONE);

            participant.setText(Html.fromHtml("<b>Session: </b>" + _session.get(position)));
            date.setText(Html.fromHtml("<b>Start Date: </b>" + _startDate.get(position)));
            location.setText(Html.fromHtml("<b>End Date: </b>" + _endDate.get(position)));
            event.setText(Html.fromHtml("<b>Time: </b>" + _time.get(position)));
            age_group.setText(Html.fromHtml("<b>Capacity: </b>" + _capacity.get(position)));

            list_registration.addView(view);
        }
    }
}
