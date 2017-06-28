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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class CheckIn2Activity extends Activity {
    private static String TAG = "Check in 2";
    Button returnStack;
    Boolean isInternetPresent = false;
    boolean getdata = false;
    TextView tv_info;
    String data_load = "False", title_load = "False";
    ListView listView;
    String token, familyID, meetid, msg1, msg2;
    ArrayList<String> t_fullname, fullname, wu_preference1, wu_studentid, wu_swimmeetid, wu_preference2, wu_eventnumber,
            wu_groupdescription, wu_description, wu_strokedescription, Time1, wu_checkin, listtbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in2);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        isInternetPresent = Utility.isNetworkConnected(getApplicationContext());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            Initialization();
            meetid = getIntent().getStringExtra("swimmeetid");
            new GetTitle().execute();
            new GetCheckInData().execute();
        }
    }

    private void Initialization() {
        // TODO Auto-generated method stub
        listView = (ListView) findViewById(R.id.lv_checkin_list2);
        tv_info = (TextView) findViewById(R.id.tv_checkin_title2);
        returnStack = (Button) findViewById(R.id.returnStack);
        returnStack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
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
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        isInternetPresent = Utility.isNetworkConnected(getApplicationContext());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        }
    }

    private class GetTitle extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(CheckIn2Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("SwimmeetID", "" + meetid);

            String responseString = WebServicesCall.RunScript(AppConfiguration.checkin2title, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equalsIgnoreCase("True")) {
                    JSONArray jsonArray = reader.optJSONArray("SwimComp_CheckIN2Msg");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                        msg1 = jsonChildNode.getString("Msg");
                    }
                } else {
                    msg1 = reader.getString("Msg");
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
            tv_info.setText(Html.fromHtml(msg1));
        }
    }

    private class GetCheckInData extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(CheckIn2Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);

            String responseString = WebServicesCall.RunScript(AppConfiguration.checkin2data, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equals("True")) {

                    t_fullname = new ArrayList<String>();
                    wu_preference1 = new ArrayList<String>();
                    wu_studentid = new ArrayList<String>();
                    wu_swimmeetid = new ArrayList<String>();
                    wu_preference2 = new ArrayList<String>();
                    wu_eventnumber = new ArrayList<String>();
                    wu_groupdescription = new ArrayList<String>();
                    wu_description = new ArrayList<String>();
                    wu_strokedescription = new ArrayList<String>();
                    Time1 = new ArrayList<String>();
                    wu_checkin = new ArrayList<String>();
                    listtbid = new ArrayList<String>();

                    JSONArray jsonMainNode = reader.optJSONArray("SwimComp_CheckIN2");
                    if (jsonMainNode.toString().equalsIgnoreCase("[{}]") || jsonMainNode.toString().equalsIgnoreCase("[]")) {
                        getdata = false;
                    } else {
                        getdata = true;
                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                            t_fullname.add(jsonChildNode.getString("fullname"));
                            wu_preference1.add(jsonChildNode.getString("wu_preference1"));
                            wu_studentid.add(jsonChildNode.getString("wu_studentid"));
                            wu_swimmeetid.add(jsonChildNode.getString("wu_swimmeetid"));
                            wu_preference2.add(jsonChildNode.getString("wu_preference2"));
                            wu_eventnumber.add(jsonChildNode.getString("wu_eventnumber"));
                            wu_groupdescription.add(jsonChildNode.getString("wu_groupdescription"));
                            wu_description.add(jsonChildNode.getString("wu_description"));
                            wu_strokedescription.add(jsonChildNode.getString("wu_strokedescription"));
                            Time1.add(jsonChildNode.getString("Time1"));
                            wu_checkin.add(jsonChildNode.getString("wu_checkin"));
                            listtbid.add(jsonChildNode.getString("listtbid"));
                        }
                    }
                } else {
                    JSONArray jsonArray = reader.optJSONArray("SwimComp_CheckIN2");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                        msg2 = jsonChildNode.getString("Msg");
                    }
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
            if (pd != null) {
                pd.dismiss();
            }
            if (data_load.toString().equals("True")) {
                if (getdata) {
                    listView.setAdapter(new Checkin2Adapter(t_fullname, wu_preference1, wu_studentid,
                            wu_swimmeetid, wu_preference2, wu_eventnumber, wu_groupdescription, wu_description,
                            wu_strokedescription, Time1, wu_checkin, listtbid, CheckIn2Activity.this));
                } else {
                }
            } else {
                Toast.makeText(getApplicationContext(), msg2, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Checkin2Adapter extends BaseAdapter {
        ArrayList<String> fullname, wu_preference1, wu_swimmeetid, wu_preference2, wu_eventnumber,
                wu_groupdescription, wu_description, wu_strokedescription, Time1, wu_checkin;
        Context context;


        public Checkin2Adapter(ArrayList<String> fullname, ArrayList<String> wu_preference1,
                               ArrayList<String> wu_studentid,
                               ArrayList<String> wu_swimmeetid,
                               ArrayList<String> wu_preference2,
                               ArrayList<String> wu_eventnumber,
                               ArrayList<String> wu_groupdescription,
                               ArrayList<String> wu_description,
                               ArrayList<String> wu_strokedescription,
                               ArrayList<String> time1, ArrayList<String> wu_checkin,
                               ArrayList<String> listtbid, Context context) {
            super();
            this.fullname = fullname;
            this.wu_preference1 = wu_preference1;
            this.wu_swimmeetid = wu_swimmeetid;
            this.wu_preference2 = wu_preference2;
            this.wu_eventnumber = wu_eventnumber;
            this.wu_groupdescription = wu_groupdescription;
            this.wu_description = wu_description;
            this.wu_strokedescription = wu_strokedescription;
            Time1 = time1;
            this.wu_checkin = wu_checkin;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return wu_swimmeetid.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        public class ViewHolder {
            TextView tv_name, tv_event, tv_age, tv_distance, tv_stroke, tv_besttime;
            TextView tv_check;
            TextView tv_diving, tv_endlan;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();

                    convertView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.checkin2_item, null);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_chkld_item_name2);
                    holder.tv_event = (TextView) convertView.findViewById(R.id.tv_chkld_event2);
                    holder.tv_age = (TextView) convertView.findViewById(R.id.tv_chkld_age2);
                    holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_chkld_distance2);
                    holder.tv_stroke = (TextView) convertView.findViewById(R.id.tv_chkld_stroke2);
                    holder.tv_besttime = (TextView) convertView.findViewById(R.id.tv_chkld_besttime2);
                    holder.tv_diving = (TextView) convertView.findViewById(R.id.tv_chkld_item_divingblock2);
                    holder.tv_endlan = (TextView) convertView.findViewById(R.id.tv_chkld_item_endlane2);
                    isInternetPresent = Utility
                            .isNetworkConnected(context);
                    holder.tv_check = (TextView) convertView.findViewById(R.id.tv_chkld_check2);
                    holder.tv_name.setText(Html.fromHtml("<b>Name:</b> " + fullname.get(position)));
                    holder.tv_event.setText(Html.fromHtml("<b>Event:</b> " + wu_eventnumber.get(position)));
                    holder.tv_age.setText(Html.fromHtml("<b>Age Group:</b> " + wu_groupdescription.get(position)));
                    holder.tv_distance.setText(Html.fromHtml("<b>Distance:</b> " + wu_description.get(position)));
                    holder.tv_stroke.setText(Html.fromHtml("<b>Stroke:</b> " + wu_strokedescription.get(position)));
                    String check = wu_checkin.get(position);
                    if (check.toString().equalsIgnoreCase("1")) {
                        check = "Checked in";
                    } else {
                        check = "Check in";
                    }
                    holder.tv_check.setText(Html.fromHtml("<b>Check-in Status:</b> " + check));

                    String diving = wu_preference1.get(position);
                    if (diving.toString().equalsIgnoreCase("2")) {
                        diving = "Yes";
                    } else {
                        diving = "No";
                    }
                    holder.tv_diving.setText(Html.fromHtml("<b>Diving Block:</b> " + diving));

                    String end = wu_preference2.get(position);
                    if (end.toString().equalsIgnoreCase("1")) {
                        end = "No";
                    } else {
                        end = "Yes";
                    }
                    holder.tv_endlan.setText(Html.fromHtml("<b>End Lane:</b> " + end));

                    String besttime = Time1.get(position);
                    if (besttime.toString().equalsIgnoreCase("n/a")) {
                        holder.tv_besttime.setText(Html.fromHtml("<b>Best Time:</b> " + "<font color='#0000FF'>" + besttime + "</font"));
                    } else {
                        String temp[] = besttime.toString().split("\\W");
                        if (temp[0].toString().length() == 1) {
                            temp[0] = "0" + temp[0];
                        }
                        if (temp[1].toString().length() == 1) {
                            temp[1] = "0" + temp[1];
                        }
                        if (temp[2].toString().length() == 1) {
                            temp[2] = "0" + temp[2];
                        }
                        besttime = temp[0] + ":" + temp[1] + "." + temp[2];
                        holder.tv_besttime.setText(Html.fromHtml("<b>Best Time:</b> " + besttime));
                        holder.tv_besttime.setTextColor(context.getResources().getColor(R.color.app_text));
                    }


                } else {
                    holder = (ViewHolder) convertView.getTag();
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                // TODO: handle exception
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

    }
}
