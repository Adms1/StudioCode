package com.waterworks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class SwimCampsRegister2Activity extends Activity {
    String TAG = "SwimCampsRegister2";
    ArrayList<HashMap<String, String>> swimCampList = new ArrayList<HashMap<String, String>>();
    ArrayList<Boolean> AddSessionValue = new ArrayList<Boolean>();
    ArrayList<Boolean> DropEarlyOffValue = new ArrayList<Boolean>();

    Boolean isInternetPresent = false;
    String siteID;
    ListView list;
    String successLoadChildList;
    String token, familyID;
    //	TextView txtSelectedStudent;
    String AddSession, currentStudentID;
    String DropOffEarly;
    LinearLayout llTabs;

    CardView btnContinue;
    DecimalFormat df;

    ArrayList<String> Add = new ArrayList<String>();
    ArrayList<String> Drop = new ArrayList<String>();
    ArrayList<String> DropOff = new ArrayList<String>();
    ArrayList<String> added = new ArrayList<String>();
    ArrayList<String> final_drop = new ArrayList<String>();
    ArrayList<String> final_sesson = new ArrayList<String>();
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> participantid = new ArrayList<String>();
    ArrayList<String> participantname = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();

    ArrayList<String> temp_Add = new ArrayList<String>();
    ArrayList<String> temp_drop = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_camps_register2);
        df = new DecimalFormat("####0.00");

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        Log.d(TAG, "Name = " + AppConfiguration.swimCampsRegister1StudentName + "\nId = " + AppConfiguration.swimCampsRegister1StudentID);

        setListners();

        String[] temp_name = AppConfiguration.swimCampsRegister1StudentName.toString().split(",");
        String[] temp_id = AppConfiguration.swimCampsRegister1StudentID.toString().split(",");

        for (int j = 0; j < temp_id.length; j++) {
            participantname.add(temp_name[j].toString().trim());
            participantid.add(temp_id[j].toString().trim());
        }

        HashMap<String, String> hashMap = null;
        for (int i = 0; i < temp_id.length; i++) {
            hashMap = new HashMap<>();
            hashMap.put("studentID", temp_id[i].toString());
            hashMap.put("studentName", temp_name[i].toString());
            childList.add(hashMap);
        }
        makeTabs(childList.get(0).get("studentID"));
        currentStudentID = childList.get(0).get("studentID").trim();

        isInternetPresent = Utility.isNetworkConnected(SwimCampsRegister2Activity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();

        } else {
            new SwimCampListAsyncTask().execute();
        }
    }
    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    public void makeTabs(String studentID) {
        llTabs = (LinearLayout) findViewById(R.id.llTabs);

        for (int i = 0; i < childList.size(); i++) {
            View childTabs = getLayoutInflater().inflate(R.layout.layout_tabs, null, false);
            TextView txtStudentNameTabs = (TextView) childTabs.findViewById(R.id.txtStudentNameTabs);
            View viewOrangeBar = (View) childTabs.findViewById(R.id.viewOrangeBar);

            txtStudentNameTabs.setText(childList.get(i).get("studentName"));
            childTabs.setTag(childList.get(i).get("studentID"));
            childTabs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            childTabs.setOnClickListener(myClickLIstener);

            if (!childTabs.getTag().equals(studentID)) {
                txtStudentNameTabs.setTextColor(Color.BLACK);
                childTabs.setBackgroundColor(getResources().getColor(R.color.student_back));
                viewOrangeBar.setBackgroundColor(getResources().getColor(R.color.student_back));
            }

            llTabs.addView(childTabs);
        }
    }

    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(final View v) {
            llTabs.removeAllViews();
            makeTabs(v.getTag().toString());
            currentStudentID = v.getTag().toString().trim();
            if(swimCampList.size() > 0){
                CustomList adapter = new CustomList(SwimCampsRegister2Activity.this, swimCampList);
                list.setAdapter(adapter);
            }else {
                Utility.ping(SwimCampsRegister2Activity.this, "No Events in this Session");
            }
        }
    };

    public void setListners() {
        View include_layout_step_boxes = (View) findViewById(R.id.include_layout_step_boxes);
        TextView txtBox2 = (TextView) include_layout_step_boxes.findViewById(R.id.txtBox2);
        txtBox2.setTextColor(Color.WHITE);
        txtBox2.setBackgroundColor(getResources().getColor(R.color.design_change_d2));

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimCampsRegister2Activity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        /*txtSelectedStudent = (TextView) findViewById(R.id.txtSelectedStudent);
		txtSelectedStudent.setText("Participant:"+AppConfiguration.swimCampsRegister1StudentName);*/
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//			   Intent i = new Intent(ProgressReportActivity.this,ProgressReportDetailsActivity.class);
//			   i.putExtra("studentID", studentID);
//			   i.putExtra("Firstname", firstname);
//			   i.putExtra("Lastname", lastname);

//			   startActivity(i);
            }
        });


        btnContinue = (CardView) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (added.containsAll(Add)) {
                    StringBuilder addSessionName = new StringBuilder();
                    StringBuilder DropOffEarlyBuilder = new StringBuilder();
                    StringBuilder nameid = new StringBuilder();
                    StringBuilder sessionname = new StringBuilder();
                    StringBuilder dropoffvalue = new StringBuilder();

                    String final_nameid, final_addsession, final_dropoff;
                    for (int i = 0; i < AddSessionValue.size(); i++) {
                        if (AddSessionValue.get(i) == true) {
                            addSessionName.append(swimCampList.get(i).get("AddName") + ",");
                        } else {
                            //addSessionName.append("0"+"*");
                        }
                    }

                    for (int j = 0; j < participantid.size(); j++) {
                        nameid.append(participantid.get(j).toString().trim() + ":" + participantname.get(j).toString().toString() + "|");
                    }
                    for (int i = 0; i < final_sesson.size(); i++) {
                        sessionname.append(final_sesson.get(i).toString().trim() + "|");
                    }
                    for (int j = 0; j < final_drop.size(); j++) {
                        dropoffvalue.append(final_drop.get(j).toString().trim() + "|");
                    }


                    for (int i = 0; i < DropEarlyOffValue.size(); i++) {
                        if (DropEarlyOffValue.get(i) == true) {
                            DropOffEarlyBuilder.append(swimCampList.get(i).get("EarlyDropOffID") + ",");
                        } else {
                            //DropOffEarlyBuilder.append("0"+"*");
                        }
                    }

                    AddSession = addSessionName.toString();
                    if (!AddSession.equals(""))
                        AddSession = AddSession.substring(0, AddSession.lastIndexOf(","));

                    DropOffEarly = DropOffEarlyBuilder.toString();
                    if (!DropOffEarly.equals(""))
                        DropOffEarly = DropOffEarly.substring(0, DropOffEarly.lastIndexOf(","));

                    final_nameid = nameid.toString();
                    if (!final_nameid.equals(""))
                        final_nameid = final_nameid.substring(0, final_nameid.lastIndexOf("|"));

                    final_addsession = sessionname.toString();
                    if (!final_addsession.equals(""))
                        final_addsession = final_addsession.substring(0, final_addsession.lastIndexOf("|"));

                    final_dropoff = dropoffvalue.toString();
                    if (!final_dropoff.equals(""))
                        final_dropoff = final_dropoff.substring(0, final_dropoff.lastIndexOf("|"));

                    //
                    Log.d("Added Session", "" + AddSession);
                    Log.d("Added DropOffEarly", "" + DropOffEarly);

//				AppConfiguration.selectedStudentSwimCampRegister2 = AppConfiguration.swimCampsRegister1StudentName +" | " + AppConfiguration.swimCampsRegister1StudentID + " | " + AddSession + " | " + DropOffEarly;
                    Intent i = new Intent(SwimCampsRegister2Activity.this, SwimCampRegister3Activity.class);
                    i.putExtra("Nameid", final_nameid);
                    i.putExtra("Session", final_addsession);
                    i.putExtra("DropOff", final_dropoff);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
//                    finish();

                } else {
                    Toast.makeText(SwimCampsRegister2Activity.this, "Please select at least one session before going to next step.", Toast.LENGTH_LONG).show();
                }

            }
        });

        // fetching header view
//		View headerLayout = findViewById(R.id.layout_top);
        Button relMenu = (Button) findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    public void loadSwimCampList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("FamilyID", familyID);
        param.put("SiteID", AppConfiguration.swimCampsRegister1SiteID);
        param.put("SelectedStudent", AppConfiguration.swimCampsRegister1StudentID);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.swimCampRegister2, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    temp_Add.add(jsonChildNode.getString("Add"));
                    temp_drop.add(jsonChildNode.getString("EarlyDropOff"));

                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put("StudentID", jsonChildNode.getString("StudentID"));
                    hashmap.put("Description", jsonChildNode.getString("Description"));
                    hashmap.put("Start Date", jsonChildNode.getString("Start Date"));
                    hashmap.put("End Date", jsonChildNode.getString("End Date"));
                    hashmap.put("Unit Price", jsonChildNode.getString("Unit Price"));
                    hashmap.put("Add", jsonChildNode.getString("Add"));
                    hashmap.put("AddName", jsonChildNode.getString("AddName"));
                    hashmap.put("AddEnable", jsonChildNode.getString("AddEnable"));
                    hashmap.put("EarlyDropOff", jsonChildNode.getString("EarlyDropOff"));
                    hashmap.put("EarlyDropOffID", jsonChildNode.getString("EarlyDropOffID"));

                    swimCampList.add(hashmap);
                    if (!Add.contains(jsonChildNode.getString("Add"))) {
                        Add.add(jsonChildNode.getString("Add"));
                    }
                    if (!Drop.contains(jsonChildNode.getString("EarlyDropOff"))) {
                        Drop.add(jsonChildNode.getString("EarlyDropOff"));
                    }
                    mylist.add("-1");
                    added.add("0");
                    DropOff.add("0");

                    AddSessionValue.add(false);
                    DropEarlyOffValue.add(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SwimCampListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCampsRegister2Activity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            swimCampList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadSwimCampList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successLoadChildList.equals("True")) {

                if(swimCampList.size() > 0){
                    CustomList adapter = new CustomList(SwimCampsRegister2Activity.this, swimCampList);
                    list.setAdapter(adapter);
                }else {
                    Utility.ping(SwimCampsRegister2Activity.this, "No Events in this Session");
                }

                btnContinue.setEnabled(true);

            } else {
                Toast.makeText(getApplicationContext(), "Data not found.", Toast.LENGTH_LONG).show();

                btnContinue.setEnabled(false);
            }

        }
    }

    public class CustomList extends ArrayAdapter<String> {
        private final ArrayList<HashMap<String, String>> data = new ArrayList<>();
        ViewHolder holder;
        int rowsToDisplay = 0;

        public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_row_swim_camp_register2);
//            this.data = list;
            for(int i = 0;i < list.size();i++){
                if(currentStudentID.equalsIgnoreCase(list.get(i).get("StudentID"))){
                    this.data.add(list.get(i));
                }
            }
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
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }


        @Override
        public View getView(final int position, View view, ViewGroup parent) {
                if (view == null) {
                    holder = new ViewHolder();
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_swim_camp_register2, null);

                    holder.txtDescription = (TextView) view.findViewById(R.id.txtDescription);
                    holder.txtUnitPrice = (TextView) view.findViewById(R.id.txtUnitPrice);
                    holder.txtStartDate = (TextView) view.findViewById(R.id.txtStartDate);
                    holder.txtEndDate = (TextView) view.findViewById(R.id.txtEndDate);
                    holder.chkAdd = (CheckBox) view.findViewById(R.id.chkAdd);
                    holder.chkEndDropOff = (CheckBox) view.findViewById(R.id.chkEndDropOff);

                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                Log.i(TAG, "index = " + participantid.indexOf(data.get(position).get("StudentID")));

                holder.txtDescription.setText(data.get(position).get("Description"));
                holder.txtUnitPrice.setText("UnitPrice:" + AppConfiguration.currency + df.format(Double.parseDouble(data.get(position).get("Unit Price"))));
                holder.txtStartDate.setText("Start Date:" + data.get(position).get("Start Date"));
                holder.txtEndDate.setText("End Date:" + data.get(position).get("End Date"));

            /*holder.chkAdd.setChecked(false);
            holder.chkEndDropOff.setChecked(false);*/

                holder.chkAdd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            /*holder.chkAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {*/

                        if (isChecked) {

                            AddSessionValue.set(position, true);
//                            added.remove(data.get(position).get("Add"));
//                            added.add(position, temp_Add.get(position).toString().trim());
                            if (!final_sesson.contains(data.get(position).get("StudentID") + ":" + data.get(position).get("AddName"))) {
                                final_sesson.add(data.get(position).get("StudentID") + ":" + data.get(position).get("AddName"));
                                added.add(data.get(position).get("Add"));
                            }
                            Log.e("AddSessionValue", AddSessionValue.toString());

                        } else {

                            AddSessionValue.set(position, false);

//                            added.add("0");
                            if (final_sesson.contains(data.get(position).get("StudentID") + ":" + data.get(position).get("AddName"))) {
                                final_sesson.remove(data.get(position).get("StudentID") + ":" + data.get(position).get("AddName"));
                                added.remove(data.get(position).get("Add"));
                            }
                            Log.e("AddSessionValue", AddSessionValue.toString());
                        }
                    }
                });

                holder.chkEndDropOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            /*holder.chkEndDropOff.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {*/

                        if (isChecked) {

                            DropEarlyOffValue.set(position, true);
//                            DropOff.remove(data.get(position).get("EarlyDropOff"));
//                            DropOff.add(data.get(position).get("EarlyDropOff"));
                            if (!final_drop.contains(data.get(position).get("StudentID") + ":" + data.get(position).get("EarlyDropOffID"))) {
                                final_drop.add(data.get(position).get("StudentID") + ":" + data.get(position).get("EarlyDropOffID"));
                                DropOff.add(data.get(position).get("EarlyDropOff"));
                            }
                            Log.e("DropEarlyOffValue", DropEarlyOffValue.toString());

                        } else {

                            DropEarlyOffValue.set(position, false);

//                            DropOff.add("0");
                            if (final_drop.contains(data.get(position).get("StudentID") + ":" + data.get(position).get("EarlyDropOffID"))) {
                                final_drop.remove(data.get(position).get("StudentID") + ":" + data.get(position).get("EarlyDropOffID"));
                                DropOff.remove(data.get(position).get("EarlyDropOff"));
                            }
                            Log.e("DropEarlyOffValue", DropEarlyOffValue.toString());
                        }
                    }
                });

            for (String row : final_sesson) {
                String[] spitString = row.split("\\:");
                if (currentStudentID.trim().equalsIgnoreCase(spitString[0].trim()) && spitString[1].trim().equalsIgnoreCase(data.get(position).get("AddName"))) {
                    holder.chkAdd.setChecked(true);
                }
                /*else {
                    holder.chkAdd.setChecked(false);
                }*/
            }

            for (String row : final_drop) {
                String[] spitString = row.split("\\:");
                if (currentStudentID.trim().equalsIgnoreCase(spitString[0].trim()) && spitString[1].trim().equalsIgnoreCase(data.get(position).get("EarlyDropOffID"))) {
                    holder.chkEndDropOff.setChecked(true);
                }
                /*else {
                    holder.chkEndDropOff.setChecked(false);
                }*/
            }

            return view;
        }
    }
        public class ViewHolder {
            TextView txtDescription, txtUnitPrice, txtStartDate, txtEndDate;
            CheckBox chkAdd, chkEndDropOff;
//            TextView swim_camp_regi2_item_studentname;
        }

        @Override
        protected void onResume() {
            super.onResume();
            this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        }

        @Override
        public void onBackPressed() {
//		super.onBackPressed();
            finish();
        }
    }