package com.waterworks.Unused;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.SwimCompititionRegisterAcitivity;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
@SuppressWarnings("deprecation")
public class SwimCompitition2Acitivity extends Activity {
    String TAG = "SwimCampsActivity2";

    @SuppressWarnings("unused")
    private Snackbar mSnackBar;
    String snackMsg="";
    Boolean isInternetPresent = false;
    String siteID;
    ListView list;
    String successLoadChildList;
    String token, familyID;
    String messageMeetDate;
    String successMeetDate;
    Button btnContinue;
    boolean isSelectedAgreement = false;
    String msg1_Hours, msg2_meet, Msg3_Meet;
    String successSwimCompittionCheck1;
    String messageNormal;
    TextView txtSelectedStudent;
    String message;
    TextView txtMeetDate;
    public static String _nameid = "";

    ArrayList<String> _StudentID = new ArrayList<String>();
    ArrayList<String> _StudentName = new ArrayList<String>();
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
    String[] tempid, tempname;
    SwimCompRegi2Adapter _adpter;
    public static List<SwimCompRegi2Item> itemData;
    public static HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
    RelativeLayout rl_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_compitition2);

        // getting token
        SharedPreferences prefs = AppConfiguration
                .getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        isInternetPresent = Utility
                .isNetworkConnected(SwimCompitition2Acitivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            // AppConfiguration.swimComptitionStudentID+":"+AppConfiguration.swimComptitionStudentName
            tempid = AppConfiguration.swimComptitionStudentID.toString().split(
                    "\\,");
            tempname = AppConfiguration.swimComptitionStudentName.toString()
                    .split("\\,");
            _nameid="";
            for (int i = 0; i < tempid.length; i++) {
                _nameid = _nameid + tempid[i].toString().trim() + ":"
                        + tempname[i].toString().trim() + ",";
            }
            _StudentID.clear();
            _StudentName.clear();

            _StudentID = new ArrayList<String>(Arrays.asList(tempid));
            _StudentName = new ArrayList<String>(Arrays.asList(tempname));
            int temp_index = _nameid.lastIndexOf(",");
            Log.i(TAG, "nameid = " + _nameid.substring(0, temp_index));
            _nameid = _nameid.substring(0, temp_index);
            // new LoadSelectedChildDataListAsyncTask().execute();
            itemData = new ArrayList<SwimCompRegi2Item>();
            _adpter = new SwimCompRegi2Adapter(this,
                    R.layout.list_row_swim_compitition2, itemData);
            new LoadChildData().execute();
        }

        txtSelectedStudent = (TextView) findViewById(R.id.txtSelectedStudent);
        txtSelectedStudent.setText("Participant:"
                + AppConfiguration.swimComptitionStudentName);
        txtSelectedStudent.setVisibility(View.GONE);
        txtMeetDate = (TextView) findViewById(R.id.txtMeetDate);
        txtMeetDate.setText(Html.fromHtml("<b>Meet Date: </b>"
                + AppConfiguration.SwimMeetText));

        list = (ListView) findViewById(R.id.list);
        rl_data = (RelativeLayout)findViewById(R.id.rl_data);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

        // fetching header view

        View view = findViewById(R.id.layout_top);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Step 1: Select Participants");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwimCompitition2Acitivity.this,
                        SwimCompititionRegisterAcitivity.class);
                ClearArray();
            }
        });


        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (temp_value.size()==0) {
                    Toast.makeText(getApplicationContext(),
                            "Please select participants", Toast.LENGTH_LONG)
                            .show();
                }
                else {

                    if(checkedForAll()){
                        Log.i(TAG, "name = " + t_sname);
                        AppConfiguration.SelectedEventDataStep2 = temp_value.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        Log.e("SelectedEventDataStep2", ""
                                + AppConfiguration.SelectedEventDataStep2);

                        Intent i = new Intent(SwimCompitition2Acitivity.this,
                                SwimCompitition3Acitivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }else{
                        int bgColor = R.color.black;
                        int txtColor = R.color.red;
                        snackMsg = "Please make atleast single student for each child.";
                        mSnackBar = Snackbar.make(rl_data,
                                snackMsg,Snackbar.LENGTH_INDEFINITE)
                                .setAction("Done", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mSnackBar.dismiss();
                                    }
                                })
                                .setActionTextColor(Color.RED);
                    }
                }
            }
        });
    }

    public void clearArray(){
        StudentID.clear();
        _StudentID.clear();
        _StudentName.clear();
    }

    public boolean checkedForAll(){
        boolean selectedAll = false;
        HashSet<String> list = new HashSet<String>();
        String[] temp_id = AppConfiguration.swimComptitionStudentID.toString().split(",");
        for (int i = 0; i < temp_id.length; i++) {
            System.out.println("TempID : "+temp_id[i]);
            for (int j = 0; j < temp_value.size(); j++) {
                String tempsss = temp_value.get(j);
                if(tempsss.contains(temp_id[i].toString().trim())){
                    list.add(temp_id[i]);
                    System.out.println("TempID added: "+temp_id[i]);
                }
            }
        }

        System.out.println("List : "+list.size());
        System.out.println("Array : "+temp_id.length);
        if(list.size()== temp_id.length){
            selectedAll=true;
        }else{
            selectedAll=false;
        }
        return selectedAll;
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.swim_camps, menu);
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // AppConfiguration.SelectedEventDataStep2 = "";
    }

    // ================================== Load Child List
    // ==========================================

    public void loadingChildList() {

        HashMap<String, String > param = new HashMap<String, String>();
        param.put("Token",token );
        param.put("MeetdatetimeValue",
                AppConfiguration.SwimMeetID);
        param.put("MeetdatetimeText",
                AppConfiguration.SwimMeetText);
        param.put("ChildListArry", _nameid);

        String responseString = WebServicesCall
                .RunScript(
                        AppConfiguration.swimCompititionStep2URL, param);

        readAndParseJSONChildList(responseString);


    }

    public void readAndParseJSONChildList(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck2");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String Event = jsonChildNode.getString("Event").trim();
                    String AgeGroup = jsonChildNode.getString("AgeGroup")
                            .trim();
                    String Distance = jsonChildNode.getString("Distance")
                            .trim();
                    String StrokeType = jsonChildNode.getString("StrokeType")
                            .trim();
                    String tdid = jsonChildNode.getString("tdid").trim();
                    String ControlType = jsonChildNode.getString("ControlType")
                            .trim();
                    String Text = jsonChildNode.getString("Text").trim();
                    String Value = jsonChildNode.getString("Value").trim();

                    hashmap.put("Event", Event);
                    hashmap.put("AgeGroup", AgeGroup);
                    hashmap.put("Distance", Distance);
                    hashmap.put("StrokeType", StrokeType);
                    hashmap.put("tdid", tdid);
                    hashmap.put("ControlType", ControlType);
                    hashmap.put("Text", Text);
                    hashmap.put("Value", Value);

                    AppConfiguration.selectedChildDataMainList.add(hashmap);

                    AppConfiguration.selectedStudents.add(false);

                }

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck2");

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    message = jsonChildNode.getString("Msg").trim();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoadSelectedChildDataListAsyncTask extends
            AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCompitition2Acitivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            AppConfiguration.selectedChildDataMainList.clear();
            AppConfiguration.selectedStudents.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadingChildList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successLoadChildList.toString().equals("True")) {

                CustomList adapter = new CustomList(
                        SwimCompitition2Acitivity.this,
                        AppConfiguration.selectedChildDataMainList);
                list.setAdapter(adapter);

            } else {
                Toast.makeText(getApplicationContext(), "" + message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public static class ViewHolder {
        TextView txtStrokeType, txtDistance, txtEvent, txtAgeGroup,
                txtControlType;
        CheckBox chk;
    }

    public class CustomList extends BaseAdapter {
        private final ArrayList<HashMap<String, String>> data;
        ViewHolder holder;

        public CustomList(Activity context,
                          ArrayList<HashMap<String, String>> list) {
            super();
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
        public Object getItem(int position) {
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

            try {
                if (view == null) {
                    holder = new ViewHolder();

                    view = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.list_row_swim_compitition2, null);

                    holder.txtStrokeType = (TextView) view
                            .findViewById(R.id.txtStrokeType);
                    holder.txtDistance = (TextView) view
                            .findViewById(R.id.txtDistance);
                    holder.txtEvent = (TextView) view
                            .findViewById(R.id.txtEvent);
                    holder.txtAgeGroup = (TextView) view
                            .findViewById(R.id.txtAgeGroup);
                    holder.chk = (CheckBox) view.findViewById(R.id.chk);
                    holder.txtControlType = (TextView) view
                            .findViewById(R.id.txtControlType);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }

                holder.txtStrokeType.setText(Html
                        .fromHtml("<b>StrokeType: </b>"
                                + AppConfiguration.selectedChildDataMainList
                                .get(position).get("StrokeType")));
                holder.txtDistance.setText(Html.fromHtml("<b>Distance: </b>"
                        + AppConfiguration.selectedChildDataMainList.get(
                        position).get("Distance")));
                holder.txtEvent.setText(Html.fromHtml("<b>Event#: </b>"
                        + AppConfiguration.selectedChildDataMainList.get(
                        position).get("Event")));
                holder.txtAgeGroup.setText(Html.fromHtml("<b>AgeGroup: </b>"
                        + AppConfiguration.selectedChildDataMainList.get(
                        position).get("AgeGroup")));

                String controlType = AppConfiguration.selectedChildDataMainList
                        .get(position).get("ControlType");
                if (controlType.contains("CheckBox")) {
                    holder.chk.setVisibility(View.VISIBLE);
                    holder.txtControlType.setVisibility(View.GONE);

                    holder.chk
                            .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    if (isChecked) {
                                        AppConfiguration.selectedStudents.set(
                                                position, true);
                                        Log.e("checkedData",
                                                AppConfiguration.selectedStudents
                                                        .toString());
                                    } else {
                                        AppConfiguration.selectedStudents.set(
                                                position, false);
                                        Log.e("checkedData",
                                                AppConfiguration.selectedStudents
                                                        .toString());
                                    }
                                }
                            });

                } else {
                    holder.chk.setVisibility(View.GONE);
                    holder.txtControlType.setVisibility(View.VISIBLE);
                    holder.txtControlType.setText("Registered");

                    AppConfiguration.selectedStudents.set(position, true);
                    Log.e("checkedData",
                            AppConfiguration.selectedStudents.toString());

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

    }

	/*-------------------------------------------new code-------------------------------------*/

    private class LoadChildData extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimCompitition2Acitivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            // AppConfiguration.selectedChildDataMainList.clear();
            // AppConfiguration.selectedStudents.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String > param = new HashMap<String, String>();
            param.put("Token",token );
            param.put("MeetdatetimeValue",
                    AppConfiguration.SwimMeetID);
            param.put("MeetdatetimeText",
                    AppConfiguration.SwimMeetText);
            param.put("ChildListArry", _nameid);

            String responseString = WebServicesCall
                    .RunScript(
                            AppConfiguration.swimCompititionStep2URL, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                successLoadChildList = reader.getString("Success");
                if (successLoadChildList.toString().equals("True")) {
                    JSONArray jsonMainNode = reader
                            .optJSONArray("SwimMeetCheck2");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        JSONArray jsonArray = jsonChildNode
                                .getJSONArray("EvantDetails");
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
                            JSONObject jObject = jsonArray
                                    .getJSONObject(j);
                            _Event.add(jObject.getString("Event"));
                            _AgeGroup
                                    .add(jObject.getString("AgeGroup"));
                            _Distance
                                    .add(jObject.getString("Distance"));
                            _StrokeType.add(jObject
                                    .getString("StrokeType"));
                            _StudentID.add(jObject
                                    .getString("StudentID"));
                            _tdid.add(jObject.getString("tdid"));
                            _ControlType.add(jObject
                                    .getString("ControlType"));
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

                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        message = jsonChildNode.getString("Msg").trim();

                    }
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
                list.setAdapter(_adpter);
                for (int i = 0; i < StudentID.size(); i++) {
                    System.out.println("St : "+StudentID.size());
                    System.out.println("st : "+_StudentID.size());
                    itemData.add(new SwimCompRegi2Item(_StudentID.get(i),
                            _StudentName.get(i), Event.get(i), AgeGroup.get(i),
                            Distance.get(i), StrokeType.get(i), StudentID
                            .get(i), tdid.get(i), ControlType.get(i),
                            Text.get(i), Value.get(i)));
                }


            } else {
                Toast.makeText(getApplicationContext(), "" + message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public class SwimCompRegi2Item {
        String _StudentId, _StudentName;
        ArrayList<String> Event, AgeGroup, Distance, StrokeType, StudentID,
                tdid, ControlType, Text, Value;

        public SwimCompRegi2Item(String _StudentId, String _StudentName,
                                 ArrayList<String> event, ArrayList<String> ageGroup,
                                 ArrayList<String> distance, ArrayList<String> strokeType,
                                 ArrayList<String> studentID, ArrayList<String> tdid,
                                 ArrayList<String> controlType, ArrayList<String> text,
                                 ArrayList<String> value) {
            super();
            this._StudentId = _StudentId;
            this._StudentName = _StudentName;
            Event = event;
            AgeGroup = ageGroup;
            Distance = distance;
            StrokeType = strokeType;
            StudentID = studentID;
            this.tdid = tdid;
            ControlType = controlType;
            Text = text;
            Value = value;
        }

        public String get_StudentId() {
            return _StudentId;
        }

        public void set_StudentName(String _StudentName) {
            this._StudentName = _StudentName;
        }

        public String get_StudentName() {
            return _StudentName;
        }

        public void set_StudentId(String _StudentId) {
            this._StudentId = _StudentId;
        }

        public ArrayList<String> getEvent() {
            return Event;
        }

        public void setEvent(ArrayList<String> event) {
            Event = event;
        }

        public ArrayList<String> getAgeGroup() {
            return AgeGroup;
        }

        public void setAgeGroup(ArrayList<String> ageGroup) {
            AgeGroup = ageGroup;
        }

        public ArrayList<String> getDistance() {
            return Distance;
        }

        public void setDistance(ArrayList<String> distance) {
            Distance = distance;
        }

        public ArrayList<String> getStrokeType() {
            return StrokeType;
        }

        public void setStrokeType(ArrayList<String> strokeType) {
            StrokeType = strokeType;
        }

        public ArrayList<String> getStudentID() {
            return StudentID;
        }

        public void setStudentID(ArrayList<String> studentID) {
            StudentID = studentID;
        }

        public ArrayList<String> getTdid() {
            return tdid;
        }

        public void setTdid(ArrayList<String> tdid) {
            this.tdid = tdid;
        }

        public ArrayList<String> getControlType() {
            return ControlType;
        }

        public void setControlType(ArrayList<String> controlType) {
            ControlType = controlType;
        }

        public ArrayList<String> getText() {
            return Text;
        }

        public void setText(ArrayList<String> text) {
            Text = text;
        }

        public ArrayList<String> getValue() {
            return Value;
        }

        public void setValue(ArrayList<String> value) {
            Value = value;
        }
    }

    public class SwimCompRegi2Adapter extends ArrayAdapter<SwimCompRegi2Item> {
        List<SwimCompRegi2Item> data;
        Context context;
        int layoutResID;
        LayoutInflater inflater;

        public SwimCompRegi2Adapter(Context context, int resource,
                                    List<SwimCompRegi2Item> data) {
            super(context, resource, data);
            this.data = data;
            this.context = context;
            this.layoutResID = resource;
            inflater = LayoutInflater.from(this.context);
        }

        private class NewViewHolder {
            LinearLayout ll_students;
            TextView tv_swim_comp_regi2_name;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
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

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final NewViewHolder holder;
            try {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(layoutResID, null);
                    holder = new NewViewHolder();
                    holder.tv_swim_comp_regi2_name = (TextView) convertView
                            .findViewById(R.id.tv_swim_comp_regi2_name);
                    holder.ll_students = (LinearLayout) convertView
                            .findViewById(R.id.ll_swim_comp_regi2_item);
                    convertView.setTag(holder);

                    final SwimCompRegi2Item _itemData = data.get(position);
                    holder.tv_swim_comp_regi2_name.setText(_itemData
                            .get_StudentName());
                    for (int i = 0; i < _itemData.getStudentID().size(); i++) {
                        final int pos = i;
                        LayoutInflater minflater = LayoutInflater
                                .from(this.context);
                        View childView = minflater.inflate(
                                R.layout.list_row_swim_compitition2_body, null);
                        TextView txtStrokeType = (TextView) childView
                                .findViewById(R.id.txtStrokeType);
                        TextView txtEvent = (TextView) childView
                                .findViewById(R.id.txtEvent);
                        TextView txtAgeGroup = (TextView) childView
                                .findViewById(R.id.txtAgeGroup);
                        TextView txtControlType = (TextView) childView
                                .findViewById(R.id.txtControlType);
                        TextView txttDid = (TextView) childView
                                .findViewById(R.id.txttDid);
                        TextView txtDistance = (TextView) childView
                                .findViewById(R.id.txtDistance);
                        final CheckBox chk = (CheckBox) childView
                                .findViewById(R.id.chk);


                        txtStrokeType.setText(Html
                                .fromHtml("<b>StrokeType: </b>"
                                        + _itemData.getStrokeType().get(i)));
                        txtDistance.setText(Html.fromHtml("<b>Distance: </b>"
                                + _itemData.getDistance().get(i)));
                        txtEvent.setText(Html.fromHtml("<b>Event#: </b>"
                                + _itemData.getEvent().get(i)));
                        txtAgeGroup.setText(Html.fromHtml("<b>AgeGroup: </b>"
                                + _itemData.getAgeGroup().get(i)));
                        txttDid.setText(Html.fromHtml("<b>Session #: </b>"
                                + _itemData.getTdid().get(i)));

                        String controlType = _itemData.getControlType().get(i);
                        if (controlType.contains("CheckBox")) {
                            chk.setVisibility(View.VISIBLE);
                            chk.setTag(_itemData.getValue().get(i));
                            txtControlType.setVisibility(View.GONE);

                            chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    if (isChecked) {
                                        // AppConfiguration.selectedStudents.set(
                                        // position, true);
                                        if(!hashmap.containsKey(position+"-"+pos)){
                                            ArrayList<String> abc = new ArrayList<String>();
                                            abc.add(_itemData.getEvent().get(pos));
                                            abc.add(_itemData.getAgeGroup().get(pos));
                                            abc.add(_itemData.getDistance().get(pos));
                                            abc.add(_itemData.getStrokeType().get(pos));
                                            abc.add(_itemData.getStudentID().get(pos));
                                            abc.add(_itemData.getTdid().get(pos));
                                            abc.add(_itemData.getControlType().get(pos));
                                            abc.add(_itemData.getText().get(pos));
                                            abc.add(_itemData.getValue().get(pos));
                                            hashmap.put(position+"-"+pos, abc);
                                        }
                                        if(!temp_value.contains(chk.getTag())){
                                            temp_value.add(chk.getTag().toString().trim());
                                        }
                                        if(!t_sname.contains(_itemData.get_StudentName().toString().trim())){
                                            t_sname.add(_itemData.get_StudentName().toString().trim());
                                        }
                                        if(!t_sid.contains(_itemData.get_StudentId().toString().trim())){
                                            t_sid.add(_itemData.get_StudentId().toString().trim());
                                        }
                                        Log.e("temp_value",
                                                temp_value
                                                        .toString());
                                    } else {
                                        ArrayList<String> abc = new ArrayList<String>();
                                        abc.add(_itemData.getEvent().get(pos));
                                        abc.add(_itemData.getAgeGroup().get(pos));
                                        abc.add(_itemData.getDistance().get(pos));
                                        abc.add(_itemData.getStrokeType().get(pos));
                                        abc.add(_itemData.getStudentID().get(pos));
                                        abc.add(_itemData.getTdid().get(pos));
                                        abc.add(_itemData.getControlType().get(pos));
                                        abc.add(_itemData.getText().get(pos));
                                        abc.add(_itemData.getValue().get(pos));
                                        if(hashmap.containsKey(position+"-"+pos)){
                                            hashmap.remove(position+"-"+pos);
                                        }
                                        if(temp_value.contains(chk.getTag())){
                                            temp_value.remove(chk.getTag().toString().trim());
                                        }
                                        if(t_sname.contains(_itemData.get_StudentName())){
                                            t_sname.remove(_itemData.get_StudentName());
                                        }
                                        if(t_sid.contains(_itemData.get_StudentId())){
                                            t_sid.remove(_itemData.get_StudentId());
                                        }
                                        Log.e("temp_value",
                                                temp_value
                                                        .toString());
                                    }
                                }
                            });

                        } else {
                            chk.setVisibility(View.GONE);
                            txtControlType.setVisibility(View.VISIBLE);
                            txtControlType.setText("Registered");

                            Log.e("checkedData",
                                    AppConfiguration.selectedStudents
                                            .toString());

                        }

                        holder.ll_students.addView(childView);
                    }
                } else {
                    holder = (NewViewHolder) convertView.getTag();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    protected void ClearArray(){
        _nameid = "";
        _StudentID.clear();
        _StudentName.clear();
        t_sname.clear();
        t_sid.clear();
        Event.clear();
        AgeGroup.clear();
        Distance.clear();
        StrokeType.clear();
        StudentID.clear();
        tdid.clear();
        ControlType.clear();
        Text.clear();
        Value.clear();
        temp_value.clear();
        itemData.clear();
        hashmap.clear();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//		super.onBackPressed();
        finish();
        ClearArray();
    }
	/*-------------------------------------------new code-------------------------------------*/

}
