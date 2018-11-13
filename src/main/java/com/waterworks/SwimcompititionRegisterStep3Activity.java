package com.waterworks;

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
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.model.SwimCompRegi2Item;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@SuppressWarnings("deprecation")
public class SwimcompititionRegisterStep3Activity extends Activity {
    String TAG = "SwimcompititionRegisterStep3Activity";

    String siteID;
    ListView list;
    String successLoadChildList;
    String token, familyID;
    String time, currentStudentID;
    CardView btnContinue;
    boolean isSelectedAgreement = false;
    String successSwimCompittionCheck1;
    TextView txtSelectedStudent;
    String message, DateValue, eventdates, MeetDate_Display;
    TextView txtMeetDate;
    Context mContext = this;
    TextView txtCompititionVal;
    LinearLayout llTabs, lastraw;
    int getposition = -1;
    TextView abc;
    boolean chkvalue = false;
    private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<String> t_sname = new ArrayList<String>();
    public static ArrayList<String> t_sid = new ArrayList<String>();
    ArrayList<ArrayList<String>> Event = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> AgeGroup = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> Distance = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> StrokeType = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> StudentID = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> tdid = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> ControlType = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> Text = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> Value = new ArrayList<ArrayList<String>>();
    ArrayList<String> temp_value = new ArrayList<String>();
    HashSet<String> studentlist = new HashSet<String>();

    String[] tempid, tempname;
    SwimCompRegi2Adapter _adpter;
    public List<SwimCompRegi2Item> itemData;

    public static HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
     SharedPreferences.Editor editor;
    SharedPreferences prefs;
    public static SparseBooleanArray mChecked = new SparseBooleanArray();
    private LoadChildData loadChildData = null;
    boolean[] itemChecked;
    public int previousTab, CurrentTab;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition3_new);

        // getting token
        prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        //getting intent values from previous screen
        Intent intent = getIntent();
        if (intent != null) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue = intent.getStringExtra("datevalue");
            time = intent.getStringExtra("time");
            MeetDate_Display = intent.getStringExtra("MeetDate_Display");
        }
        tempid = AppConfiguration.swimComptitionStudentID.toString().split("\\,");
        tempname = AppConfiguration.swimComptitionStudentName.toString().split("\\,");
//        30-05-2017 megha
//        AppConfiguration.global_StudIDChecked.clear();//clear the student ids which were checked in last session

        HashMap<String, String> hashMap = null;
        for (int i = 0; i < tempid.length; i++) {
            hashMap = new HashMap<>();
            hashMap.put("studentID", tempid[i].toString());
            hashMap.put("studentName", tempname[i].toString());
            int temp = i + 1;
            hashMap.put("Tabno", Integer.toString(temp));
            childList.add(hashMap);
        }
//        29-05-2017 megha
//        AppConfiguration.selectedStudent1.clear();
//        AppConfiguration.selectedStudent2.clear();
        previousTab = 1;
        CurrentTab = 1;
        initViews();
        setListners();

        makeTabs(childList.get(0).get("studentID"));
        currentStudentID = childList.get(0).get("studentID").trim();
        isInternetPresent = Utility.isNetworkConnected(SwimcompititionRegisterStep3Activity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            loadChildData = new LoadChildData();
            loadChildData.execute();
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

    public void setTitleBar() {
        View view = findViewById(R.id.layout_top);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Events");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfiguration.global_StudIDChecked.clear();
                Intent intentCheckin = new Intent(SwimcompititionRegisterStep3Activity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
    }

    public void makeTabs(String studentID) {
        llTabs = (LinearLayout) findViewById(R.id.llTabs);
        for (int i = 0; i < childList.size(); i++) {
            View childTabs = getLayoutInflater().inflate(R.layout.layout_tabs, null, false);
            TextView txtStudentNameTabs = (TextView) childTabs.findViewById(R.id.txtStudentNameTabs);
            View viewOrangeBar = (View) childTabs.findViewById(R.id.viewOrangeBar);

            if (previousTab == CurrentTab) {
                Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
                viewOrangeBar.startAnimation(animSlidInRight);
            }
            if (CurrentTab < previousTab) {
                Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
                viewOrangeBar.startAnimation(animSlidInRight);

            } else if (CurrentTab > previousTab) {
                Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
                viewOrangeBar.startAnimation(animSlidInRight);
            }
            String studentfirstname[] = childList.get(i).get("studentName").trim().split("\\s+");

            txtStudentNameTabs.setText(studentfirstname[0]);
            childTabs.setTag(childList.get(i).get("studentID") + "|" + childList.get(i).get("Tabno"));

            childTabs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            childTabs.setOnClickListener(myClickLIstener);
            //navin add tabno instudent
            String studentID1[] = childTabs.getTag().toString().trim().split("\\|");
            if (!studentID1[0].equals(studentID)) {
                txtStudentNameTabs.setTextColor(getResources().getColor(R.color.design_change_d2));
                childTabs.setBackgroundColor(getResources().getColor(R.color.student_back));
                viewOrangeBar.setBackgroundColor(getResources().getColor(R.color.student_back));
            } else {
                txtStudentNameTabs.setTextColor(getResources().getColor(R.color.orange));
                childTabs.setBackgroundColor(getResources().getColor(R.color.student_back));
            }
            llTabs.addView(childTabs);
        }
    }
    View.OnClickListener myClickLIstener = new View.OnClickListener() {
        public void onClick(final View v) {
            llTabs.removeAllViews();
            String studnetTab[] = v.getTag().toString().trim().split("\\|");
            currentStudentID = studnetTab[0];
            int SelectedTabno = Integer.parseInt(studnetTab[1]);
            CurrentTab = SelectedTabno;
            makeTabs(currentStudentID);
            ClearArray();
            isInternetPresent = Utility.isNetworkConnected(SwimcompititionRegisterStep3Activity.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                loadChildData = new LoadChildData();
                loadChildData.execute();
            }
        }
    };
    LinearLayout temp1;
    public void initViews() {
        setTitleBar();
        txtCompititionVal = (TextView) findViewById(R.id.txtCompititionVal);
        txtCompititionVal.setText(MeetDate_Display);
        btnContinue = (CardView) findViewById(R.id.btnContinue);
        itemData = new ArrayList<SwimCompRegi2Item>();
        list = (ListView) findViewById(R.id.list);
        lastraw = (LinearLayout) findViewById(R.id.lastraw);
    }
    public void setListners() {
        btnContinue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp_value.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select at least one event for each child.", Toast.LENGTH_LONG).show();
                } else {
                    if (checkedForAll()) {
                        AppConfiguration.SelectedEventDataStep2 = temp_value.toString().trim();
                        Log.d("selectvalue", AppConfiguration.SelectedEventDataStep2);
                        Intent i = new Intent(SwimcompititionRegisterStep3Activity.this, Swim4Test.class);
                        i.putExtra("datevalue", DateValue);
                        i.putExtra("time", time);
                        i.putExtra("eventdates", eventdates);
                        i.putExtra("MeetDate_Display", MeetDate_Display);
                        startActivity(i);
                        finish();
                    } else {
                        new AlertDialog.Builder(SwimcompititionRegisterStep3Activity.this)
                                .setTitle("Alert")
                                .setMessage("Please select at least one event for each child.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        });
    }
//26-06-2017 megha  use for user come back that time add value in temp_value
    @Override
    protected void onResume() {
        super.onResume();
        AppConfiguration.SelectedEventDataStep2.toString();
        if (!AppConfiguration.SelectedEventDataStep2.equals("")) {
            if (temp_value.size() == 0) {
                String[] tempEventList = AppConfiguration.SelectedEventDataStep2.split(",");
                List<String> newTempEventList = new ArrayList<String>(Arrays.asList(tempEventList));
                for (int i = 0; i < newTempEventList.size(); i++) {
                    temp_value.add(newTempEventList.get(i).replaceFirst(",", "").replaceAll("\\[","").replaceAll("\\]","").trim());
                    Log.d("backtemp_value",temp_value.toString());
                    AppConfiguration.selectedStudent1.add(newTempEventList.get(i).replaceFirst(",", "").replaceAll("\\[","").replaceAll("\\]","").trim());
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppConfiguration.SelectedEventDataStep2 = temp_value.toString().trim();
    }
//====================
    public boolean checkedForAll() {
        boolean selectedAll = false;
        AppConfiguration.temp_id = AppConfiguration.swimComptitionStudentID.toString().split(",");
        for (int i = 0; i < AppConfiguration.temp_id.length; i++) {
            System.out.println("TempID : " + AppConfiguration.temp_id[i]);
            for (int j = 0; j < temp_value.size(); j++) {
                String tempsss = temp_value.get(j);
                if (tempsss.contains(AppConfiguration.temp_id[i].toString().trim())) {
                    studentlist.add(AppConfiguration.temp_id[i].toString().trim());
                    System.out.println("TempID added: " + AppConfiguration.temp_id[i]);
                }
            }
        }
        System.out.println("List : " + studentlist.size());
        System.out.println("Array : " + AppConfiguration.temp_id.length);
        if (studentlist.size() == AppConfiguration.temp_id.length) {
            selectedAll = true;
        } else {
            selectedAll = false;
        }
        return selectedAll;
    }

    /*-------------------------------------------new code-------------------------------------*/
    private class LoadChildData extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimcompititionRegisterStep3Activity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("MeetdatetimeValue", DateValue);
            param.put("MeetdatetimeText", time);
            param.put("ChildListArry", currentStudentID);
            String responseString = WebServicesCall.RunScript(AppConfiguration.swimCompititionStep2URL, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                if (reader != null) {
                    successLoadChildList = reader.getString("Success");
                    if (successLoadChildList.toString().equals("True")) {
                        JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck2");

                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                            JSONArray jsonArray = jsonChildNode.getJSONArray("EvantDetails");
                            ArrayList<String> _Event = new ArrayList<String>();
                            ArrayList<String> _AgeGroup = new ArrayList<String>();
                            ArrayList<String> _Distance = new ArrayList<String>();
                            ArrayList<String> _StrokeType = new ArrayList<String>();
                            ArrayList<String> _StudentID = new ArrayList<String>();
                            ArrayList<String> _tdid = new ArrayList<String>();
                            ArrayList<String> _ControlType = new ArrayList<String>();
                            ArrayList<String> _Text = new ArrayList<String>();
                            ArrayList<String> _Value = new ArrayList<String>();

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jObject = jsonArray.getJSONObject(j);
                                _Event.add(jObject.getString("Event"));
                                _AgeGroup.add(jObject.getString("AgeGroup"));
                                _Distance.add(jObject.getString("Distance"));
                                _StrokeType.add(jObject.getString("StrokeType"));
                                _StudentID.add(jObject.getString("StudentID"));
                                _tdid.add(jObject.getString("tdid"));
                                _ControlType.add(jObject.getString("ControlType"));
                                _Text.add(jObject.getString("Text"));
                                _Value.add(jObject.getString("Value"));
                            }
                            Event.add(_Event);
                            AgeGroup.add(_AgeGroup);
                            Distance.add(_Distance);
                            StrokeType.add(_StrokeType);
                            StudentID.add(_StudentID);
                            tdid.add(_tdid);
                            ControlType.add(_ControlType);
                            Text.add(_Text);
                            Value.add(_Value);
                        }
                    } else {
                        JSONArray jsonMainNode = reader
                                .optJSONArray("SwimMeetCheck2");
                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                            message = jsonChildNode.getString("Msg").trim();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Null data" + message, Toast.LENGTH_LONG).show();
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
            if (successLoadChildList.toString().equals("True")) {
                for (int i = 0; i < StudentID.size(); i++) {
                    System.out.println("St : " + StudentID.size());
                    itemData.add(new SwimCompRegi2Item(childList.get(i).get("studentID"),
                            childList.get(i).get("studentName"), Event.get(i), AgeGroup.get(i),
                            Distance.get(i), StrokeType.get(i), StudentID
                            .get(i), tdid.get(i), ControlType.get(i),
                            Text.get(i), Value.get(i)));
                }
                if (itemData.size() != 0) {
                    _adpter = new SwimCompRegi2Adapter(SwimcompititionRegisterStep3Activity.this,
                            R.layout.list_row_swim_compitition2_new, itemData);
                    if (_adpter.getCount() > 0) {
                        list.setAdapter(_adpter);

                        chkvalue = true;
                        list.deferNotifyDataSetChanged();
                        if (previousTab == CurrentTab) {
                            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
                            list.startAnimation(animSlidInRight);
                        }
                        if (CurrentTab < previousTab) {
                            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
                            list.startAnimation(animSlidInRight);
                            previousTab = CurrentTab;
                        } else if (CurrentTab > previousTab) {
                            previousTab = CurrentTab;
                            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
                            list.startAnimation(animSlidInRight);
                        }
                    }
                } else {
                    list.setAdapter(null);
                }
            } else {
                Toast.makeText(getApplicationContext(), "" + message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
    }

    public class SwimCompRegi2Adapter extends BaseAdapter {
        List<SwimCompRegi2Item> data;
        Context context;
        int layoutResID;
        LayoutInflater inflater;

        public SwimCompRegi2Adapter(Context context, int resource,List<SwimCompRegi2Item> data) {
            super();
            this.data = data;
            this.context = context;
            this.layoutResID = resource;
            inflater = LayoutInflater.from(this.context);
            itemChecked = new boolean[data.get(0).getEvent().size()];
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.get(0).getStudentID().size();
        }

        @Override
        public SwimCompRegi2Item getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        //        04-01-2016 megha
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = SwimcompititionRegisterStep3Activity.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.list_row_swim_compitition2_body, null);
            final RelativeLayout rel = (RelativeLayout) view.findViewById(R.id.relativecolor);
            final TextView txtStrokeType = (TextView) view.findViewById(R.id.txtStrokeType);
            final TextView txtEvent = (TextView) view.findViewById(R.id.txtEvent);
            final TextView txtAgeGroup = (TextView) view.findViewById(R.id.txtAgeGroup);
            final TextView txtControlType = (TextView) view.findViewById(R.id.txtControlType);
            final TextView txttDid = (TextView) view.findViewById(R.id.txttDid);
            final TextView txtDistance = (TextView) view.findViewById(R.id.txtDistance);
            final CheckBox chk1 = (CheckBox) view.findViewById(R.id.chk);

            txtStrokeType.setText(Html.fromHtml("<b>" + data.get(0).getStrokeType().get(position) + " - " + data.get(0).getDistance().get(position) + "</b>"));
            txtEvent.setText("Age:" + " " + data.get(0).getAgeGroup().get(position) + "  " + "  " + "Event" + " #" + data.get(0).getEvent().get(position));
            chk1.setChecked(false);
            chkvalue = true;
            for (String row : AppConfiguration.global_StudIDChecked) {
                String[] spitString = row.split("\\|");
                if (currentStudentID.trim().equalsIgnoreCase(spitString[0].trim()) && spitString[1].trim().equalsIgnoreCase(String.valueOf(position).trim())) {
                    chk1.setChecked(true);
                }
            }
            chk1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (chk1.isChecked()) {
                        chkvalue = true;
                        chk1.setButtonDrawable(R.drawable.custom_check);
                        rel.setBackgroundColor(getResources().getColor(R.color.orange));

                        AppConfiguration.global_StudIDChecked.remove(data.get(0).getStudentID().get(0).trim() + "|" + String.valueOf(position));
                        AppConfiguration.global_StudIDChecked.add(data.get(0).getStudentID().get(0).trim() + "|" + String.valueOf(position));
                        AppConfiguration.selectedStudent1.add(data.get(0).getStudentID().get(0).trim() + "|" + data.get(0).getStrokeType().get(position) + " - " + data.get(0).getDistance().get(position)
                                + "\n" + "Age: " + "\t" + data.get(0).getAgeGroup().get(position) + " \t\t " + "Event" + " #" + data.get(0).getEvent().get(position));
                        if (!temp_value.contains(data.get(0).getValue().get(position).trim())) {
                            temp_value.add(data.get(0).getValue().get(position).replaceFirst(",", "").trim());
                            Log.d("add value", "" + temp_value);
                            chk1.setButtonDrawable(R.drawable.custom_check);
                            rel.setBackgroundColor(getResources().getColor(R.color.orange));
                        }
                    } else {
                        itemChecked[position] = false;
                        rel.setBackgroundColor(getResources().getColor(R.color.student_back));
                        AppConfiguration.global_StudIDChecked.remove(data.get(0).getStudentID().get(0).trim() + "|" + String.valueOf(position));
                        AppConfiguration.selectedStudent1.remove(data.get(0).getStudentID().get(0).trim() + "|" + data.get(0).getStrokeType().get(position) + " - " + data.get(0).getDistance().get(position)
                                + "\n" + "Age: " + "\t" + data.get(0).getAgeGroup().get(position) + " \t\t " + "Event" + " #" + data.get(0).getEvent().get(position));
                        if (temp_value.contains(data.get(0).getValue().get(position).trim())) {
                            temp_value.remove(data.get(0).getValue().get(position).trim());
                            Log.d("clear value", "" + temp_value);
                        }
                    }
                }

            });
            String controlType = data.get(0).getControlType().get(position);
            if (controlType.contains("CheckBox")) {
                chk1.setVisibility(View.VISIBLE);
                chk1.setTag(data.get(0).getValue().get(position));
                txtControlType.setVisibility(View.GONE);

                if (chk1.isChecked()) {
                    chk1.setChecked(true);
                    chk1.setButtonDrawable(R.drawable.custom_check);
                    rel.setBackgroundColor(getResources().getColor(R.color.orange));
                } else {
                    chk1.setChecked(false);
                    chk1.setButtonDrawable(R.drawable.custom_check);
                }
            } else {
                chk1.setVisibility(View.GONE);
                txtControlType.setVisibility(View.VISIBLE);
                txtControlType.setText("Registered");
                Log.e("checkedData", AppConfiguration.selectedStudents.toString());
            }
            return view;
        }
    }
    protected void ClearArray() {
        Event.clear();
        AgeGroup.clear();
        Distance.clear();
        StrokeType.clear();
        StudentID.clear();
        tdid.clear();
        ControlType.clear();
        Text.clear();
        Value.clear();
        itemData.clear();
        hashmap.clear();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        super.onBackPressed();
        AppConfiguration.global_StudIDChecked.clear();
        AppConfiguration.SelectedEventDataStep2 = "";
        finish();
        ClearArray();
    }
    /*-------------------------------------------new code-------------------------------------*/
}
