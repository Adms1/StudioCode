package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class RibbenCountDetailsActivity extends Activity {

    ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();

    ListView list;
    TextView txtNoRecord;
    String successViewCertificate;
    RelativeLayout rel_list;
    String TAG = "ViewCerfiticate";
    String filename;

    String studentID = null;
    String studentName = null;
    TextView txtLabel;
    Boolean isInternetPresent = false;
    Context mContext = this;
    String token, familyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ribben_count_details_activity);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        // fetching header view
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Ribbon Count");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        studentID = getIntent().getStringExtra("studentID");
        studentName = getIntent().getStringExtra("studentName");

        rel_list = (RelativeLayout) findViewById(R.id.rel_list);
        txtLabel = (TextView) findViewById(R.id.txtLabel);
        txtLabel.setText("Student: " + studentName);

        txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        isInternetPresent = Utility.isNetworkConnected(RibbenCountDetailsActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new RibbenCountDetailsAsyncTask().execute();
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

    public void getRibbenCountDetailsList() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("StudentID", studentID);
        String responseString = WebServicesCall
                .RunScript(AppConfiguration.ribbenCountDetailsURL, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successViewCertificate = reader.getString("Success");
            if (successViewCertificate.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SwimComp_RibbonCount");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String strokedescription = jsonChildNode.getString("strokedescription").trim();
                    String meetdate = jsonChildNode.getString("meetdate").trim();
                    String time = jsonChildNode.getString("time").trim();
                    String Desc = jsonChildNode.getString("Desc").trim();

                    hashmap.put("strokedescription", strokedescription);
                    hashmap.put("meetdate", meetdate);
                    hashmap.put("time", time);
                    hashmap.put("Desc", Desc);
                    childList.add(hashmap);
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RibbenCountDetailsAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RibbenCountDetailsActivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
            childList.clear();
        }
        @Override
        protected Void doInBackground(Void... params) {
            getRibbenCountDetailsList();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (childList.size() > 0) {
                txtNoRecord.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            } else {
                txtNoRecord.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }
            if (successViewCertificate.toString().equals("True")) {
                CustomList adapter = new CustomList(RibbenCountDetailsActivity.this, childList);
                list.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final ArrayList<HashMap<String, String>> data;

        public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_row_ribben_count_details);
            this.context = context;
            this.data = list;
        }
        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_row_ribben_count_details, null, true);

            TextView txtEventName = (TextView) rowView.findViewById(R.id.txtEventName);
            txtEventName.setText("Event: " + childList.get(position).get("strokedescription"));

            TextView txtMeetDate = (TextView) rowView.findViewById(R.id.txtMeetDate);
            txtMeetDate.setText("Date: " + childList.get(position).get("meetdate"));

            TextView txtTime = (TextView) rowView.findViewById(R.id.txtTime);
            txtTime.setText("Time: " + childList.get(position).get("time"));

            TextView txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            txtDesc.setText("" + childList.get(position).get("Desc"));

            return rowView;
        }
    }


}
