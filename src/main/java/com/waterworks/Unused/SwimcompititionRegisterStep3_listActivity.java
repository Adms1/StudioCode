package com.waterworks.Unused;

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
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.waterworks.*;
import com.waterworks.SwimcompititionRegisterStep4Activity;
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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

@SuppressWarnings("deprecation")
public class SwimcompititionRegisterStep3_listActivity extends Activity {
    String TAG = "SwimcompititionRegisterStep3Activity";

    @SuppressWarnings("unused")
    private Snackbar mSnackBar;
    String snackMsg="";
    Boolean isInternetPresent = false;
    String siteID;
    ListView list;
    String successLoadChildList;
    String token, familyID;
    String messageMeetDate,time;
    String successMeetDate;
    Button btnContinue;
    boolean isSelectedAgreement = false;
    String msg1_Hours, msg2_meet, Msg3_Meet;
    String successSwimCompittionCheck1;
    String messageNormal;
    TextView txtSelectedStudent;
    String message,DateValue,eventdates,MeetDate_Display;
    TextView txtMeetDate;
    public static String _nameid = "";
    public static String _nameid1 = "";
    public static String _nameid2 = "";
    public static String _nameid3 = "";

    public static String _nameid4 = "";
    public static String _nameid5 = "";

    TextView txtCompititionVal;

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
    HashSet<String> studentlist = new HashSet<String>();
    String[] tempid, tempname;
    SwimCompRegi2Adapter _adpter,adp;
    SwimCompRegi2Adapter1 _adpter1;
    SwimCompRegi2Adapter2 adpter2;
    public static List<SwimCompRegi2Item> itemData;
    public static List<SwimCompRegi2Item1> itemData1;
    public static List<SwimCompRegi2Item2> itemData2;
    public static String firstname,secondname,thirdname;
    String firstid,thirdid,secondid,Finalid;
    public static HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
    RelativeLayout rl_data,lastview;
    LinearLayout st_1, st_2, st_3, st_4, st_5, main_lay, days_st_1, days_st_2, days_st_3, days_st_4, days_st_5;
    TextView name_1, name_2, name_3, name_4, name_5;
    View select_1, select_2, select_3, select_4, select_5;
    CardView btn_next_card;
    View view_1, view_2;
    int last_clicked_position = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_swim_compitition2);
        setContentView(R.layout.activity_swim_compitition3_new);

        // getting token
        SharedPreferences prefs = AppConfiguration
                .getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID+"\nDateValue=" +DateValue);
        txtCompititionVal = (TextView) findViewById(R.id.txtCompititionVal);

        System.out.println("Array : "+ com.waterworks.SwimcompititionRegisterStep3Activity.mChecked);
        Intent intent = getIntent();
        if (null != intent) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue= intent.getStringExtra("datevalue");
//            Toast.makeText(getApplicationContext(), "DateValue" + DateValue, Toast.LENGTH_LONG).show();
            time = intent.getStringExtra("time");
            MeetDate_Display = intent.getStringExtra("MeetDate_Display");

            Log.d(TAG, "eventdates=" + eventdates);
            Log.d(TAG, "DateValue=" + DateValue);
            txtCompititionVal.setText(MeetDate_Display );

        }
       // temp_value=SwimcompititionRegisterStep3Activity.temp_value;
       /* else{
            DateValue="";
            Log.d(TAG, "DateValue=" + DateValue);
        }*/
        isInternetPresent = Utility
                .isNetworkConnected(SwimcompititionRegisterStep3_listActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            // AppConfiguration.swimComptitionStudentID+":"+AppConfiguration.swimComptitionStudentName
            tempid = AppConfiguration.swimComptitionStudentID.toString().split(
                    "\\,");
            tempname = AppConfiguration.swimComptitionStudentName.toString()
                    .split("\\,");

            _nameid="";
            _nameid1="";
            _nameid2="";
            _nameid3="";
            _nameid4="";
            _nameid5="";

            for (int i = 0; i < tempid.length; i++) {
              /*  _nameid1=tempid[0].toString().trim();
                _nameid2=tempid[1].toString().trim();
                _nameid3=tempid[2].toString().trim();*/
             /*   _nameid4=tempid[3].toString().trim();
                _nameid5=tempid[4].toString().trim();*/

                _nameid = _nameid + tempid[i].toString().trim() + ":"
                        + tempname[i].toString().trim() + ",";
            }
            if(_nameid3==""||_nameid3==null||_nameid3=="NULL")
            {
                _nameid3="";
            }
            _StudentID.clear();
            _StudentName.clear();

            _StudentID = new ArrayList<String>(Arrays.asList(tempid));
            _StudentName = new ArrayList<String>(Arrays.asList(tempname));
            int temp_index = _nameid.lastIndexOf(",");
            Log.i(TAG, "nameid = " + _nameid.substring(0, temp_index));
          /*  Log.i(TAG, "_nameid1 = " + _nameid1);
            Log.i(TAG, "_nameid2 = " + _nameid2);
            Log.i(TAG, "_nameid3 = " + _nameid3);
            Log.i(TAG, "_nameid4 = " + _nameid4);
            Log.i(TAG, "_nameid5 = " + _nameid5);*/
            String s = AppConfiguration.swimComptitionStudentID.toString();
            StringTokenizer st = new StringTokenizer(s, ",");
            try {
                if (st.hasMoreTokens()) {
                    firstid = st.nextToken();

                    secondid = st.nextToken();
                    thirdid = st.nextToken();
                } else {
                    firstid = "";

                    secondid = "";
                    thirdid = "";
                }

            }catch (NoSuchElementException e)
            {
                e.toString();
                // firstname="";
                Log.i(TAG, "firstid exception = " + e.toString());
            }
            String s1 = AppConfiguration.swimComptitionStudentName.toString();
            StringTokenizer st1 = new StringTokenizer(s1, ",");

            try {

                if(st1.hasMoreTokens()) {
                    firstname = st1.nextToken();
                    secondname = st1.nextToken();
                    thirdname = st1.nextToken();
                }
                else{
                    firstname = " ";
                    secondname = " ";
                    thirdname = " ";

                }
                if (firstname.equals("") || firstname.equalsIgnoreCase(null)) {
                    firstname = " ";
                }
                if (secondname.equals("") || secondname.equalsIgnoreCase(null)) {
                    secondname = " ";
                }
                if (thirdname.equals("") || thirdname.equalsIgnoreCase(null)) {
                    thirdname = " ";
                }
            }catch (NoSuchElementException e){
                e.toString();
                // firstname="";
                Log.i(TAG, "first exception = " + e.toString());

            }

            Log.i(TAG, "first = " + firstid);
            Log.i(TAG, "second = " + secondid);
            Log.i(TAG, "third = " + thirdid);
            Log.i(TAG, "firstname = " + firstname);
            Log.i(TAG, "secondname = " + secondname);
            Log.i(TAG, "thirdname = " + thirdname);
            Log.i(TAG, "swimComptitionStudentName = " + AppConfiguration.swimComptitionStudentName.toString());
            Log.i(TAG, " swimComptitionStudentid = " + AppConfiguration.swimComptitionStudentID.toString());


            _nameid = _nameid.substring(0, temp_index);
            //  new LoadSelectedChildDataListAsyncTask().execute();
            itemData = new ArrayList<SwimCompRegi2Item>();
            itemData1= new ArrayList<SwimCompRegi2Item1>();
            itemData2= new ArrayList<SwimCompRegi2Item2>();

   /*     txtSelectedStudent = (TextView) findViewById(R.id.txtSelectedStudent);
        txtSelectedStudent.setText("Participant:"
                + AppConfiguration.swimComptitionStudentName);
        txtSelectedStudent.setVisibility(View.GONE);
        txtMeetDate = (TextView) findViewById(R.id.txtMeetDate);
        txtMeetDate.setText(Html.fromHtml("<b>Meet Date: </b>"
                + AppConfiguration.SwimMeetText));*/
            //  selstudentname=(TextView) findViewById(R.id.textView1);
            list = (ListView) findViewById(R.id.list);
            //list2 = (ListView) findViewById(R.id.list2);
            // list3 = (ListView) findViewById(R.id.list3);

            // list4 = (ListView) findViewById(R.id.list5);
            lastview=(RelativeLayout) findViewById(R.id.lastview);

            //  lastview1=(RelativeLayout) findViewById(R.id.lastview2);
            // lastview2=(RelativeLayout) findViewById(R.id.lastview2);
            // lastview3=(RelativeLayout) findViewById(R.id.lastview4);
            rl_data = (RelativeLayout)findViewById(R.id.rl_data);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                }
            });

            st_1 = (LinearLayout) findViewById(R.id.st_1);
            st_2 = (LinearLayout) findViewById(R.id.st_2);

            st_3 = (LinearLayout) findViewById(R.id.st_3);
            st_4 = (LinearLayout) findViewById(R.id.st_4);
            st_5 = (LinearLayout) findViewById(R.id.st_5);





            /*name_1.setTextColor(R.color.white);
            name_2.setTextColor(R.color.black);
            name_3.setTextColor(R.color.black);
*/


      /*  days_st_1 = (LinearLayout) findViewById(R.id.days_st_1);
        days_st_2 = (LinearLayout) findViewById(R.id.days_st_2);
        days_st_3 = (LinearLayout) findViewById(R.id.days_st_3);
        days_st_4 = (LinearLayout) findViewById(R.id.days_st_4);
        days_st_5 = (LinearLayout) findViewById(R.id.days_st_5);*/


            select_1 = (View) findViewById(R.id.select_1);
            select_2 = (View) findViewById(R.id.select_2);
            select_3 = (View) findViewById(R.id.select_3);
            select_4 = (View) findViewById(R.id.select_4);
            select_5 = (View) findViewById(R.id.select_5);

            main_lay = (LinearLayout) findViewById(R.id.main_lay);
            // fetching header view
            select_1.setTag(1);
            select_2.setTag(2);
            select_3.setTag(3);
            select_4.setTag(4);
            select_5.setTag(5);
            name_1 = (TextView) findViewById(R.id.name_1);
            name_2 = (TextView) findViewById(R.id.name_2);
            name_3 = (TextView) findViewById(R.id.name_3);
            name_4 = (TextView) findViewById(R.id.name_4);
            name_5 = (TextView) findViewById(R.id.name_5);

            name_1.setText(firstname);
            name_2.setText(secondname);
            name_3.setText(thirdname);
            main_lay.setVisibility(View.VISIBLE);
            View view = findViewById(R.id.layout_top);

            st_1.setBackgroundColor((getResources().getColor(R.color.grey_days_selection)));
            st_3.setBackgroundColor(getResources().getColor(R.color.grey_days_selection));
            name_2.setTextColor(Color.WHITE);
            name_1.setTextColor(Color.BLACK);
            name_3.setTextColor(Color.BLACK);
            select_2.setVisibility(View.VISIBLE);
            select_1.setVisibility(View.GONE);
            select_3.setVisibility(View.GONE);

           // select_1.setVisibility(View.GONE);
          //  st_1.setBackgroundColor(Color.parseColor("#003d71"));



            TextView title = (TextView)view.findViewById(R.id.action_title);
            title.setText("Step 2: Select Events");

            ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
            ib_menusad.setBackgroundResource(R.drawable.back_arrow);

            Button relMenu = (Button)view.findViewById(R.id.relMenu);
            relMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // finish();
                     // ClearArray();
                    //     Intent i=new Intent(SwimcompititionRegisterStep3Activity.this,SwimcompititionRegisterStep2Activity.class);

                    //  ClearArray();
               Intent i=new Intent(SwimcompititionRegisterStep3_listActivity.this,SwimcompititionRegisterStep2Activity.class);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    ClearArray();
                startActivity(i);
                   // finish();
                   // ClearArray();
                }
            });

            if(firstid==null) {
                Log.i(TAG, " st_1 null= " + firstid);
                name_1.setText("");
             //  st_1.setVisibility(View.GONE);

            }
            else {
                Log.i(TAG, " st_1 not null= " + firstid);
               // st_1.setVisibility(View.VISIBLE);
            }
            if(thirdid==null) {
                Log.i(TAG, " st_3 null= " + thirdid);
                name_3.setText("");
               /// st_3.setVisibility(View.GONE);

            }
            else {
                Log.i(TAG, " st_3 not null= " + thirdid);
                //st_3.setVisibility(View.VISIBLE);
            }
           /* if(thirdid==null) {
                Log.i(TAG, " st_3 null= " + thirdid);
                st_3.setVisibility(View.GONE);

            }
            else {
                Log.i(TAG, " st_3 not null= " + thirdid);
                st_3.setVisibility(View.VISIBLE);
            }*/

            try {
                if(secondid==null){
                        secondid=firstid;

                    new LoadChildData().execute();
                   /* Intent i=new Intent(SwimcompititionRegisterStep3Activity.this,SwimcompititionRegisterStep3Activity.class);
                    i.putExtra("datevalue", DateValue);
                    startActivity(i);*/

                }else if (secondid != null){
                    new LoadChildData().execute();
                    Log.i(TAG, "secondid exception not null1 step1" + secondid.toString());}
                else if (thirdid == null){
                    thirdid=firstid;
                    new LoadChildData().execute();
                    Log.i(TAG, "thirdid exception not null1 step1" + thirdid.toString());}
                else if (thirdid != null){
                    new LoadChildData().execute();
                    Log.i(TAG, "thirdid exception not null1 step1" + thirdid.toString());}

            } catch (NullPointerException e1)
            {
                e1.toString();
                // firstname="";
                Log.i(TAG, "secondid exception =null1 " + e1.toString());
                //  secondid=firstid;
                new LoadChildData().execute();
           /*     Intent i=new Intent(SwimcompititionRegisterStep3Activity.this,SwimcompititionRegisterStep2Activity.class);
                i.putExtra("datevalue", DateValue);
                startActivity(i);*/

            } catch (Exception e){
                e.toString();
                // secondid=firstid;
                // firstname="";
                Log.i(TAG, "secondid exception =null " + e.toString());
                new LoadChildData().execute();
             /*   Intent i=new Intent(SwimcompititionRegisterStep3Activity.this,SwimcompititionRegisterStep3Activity.class);
                i.putExtra("datevalue", DateValue);
                startActivity(i);*/
            }

            st_2.setOnClickListener(new OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Log.i(TAG, "first st_1 = " + firstid);

                    select_1.setVisibility(View.GONE);
                    select_2.setVisibility(View.VISIBLE);
                    select_3.setVisibility(View.GONE);
                    select_4.setVisibility(View.GONE);
                    select_5.setVisibility(View.GONE);
                  /*  st_1.setBackgroundColor((getResources().getColor(R.color.grey_days_selection)));
                    name_1.setTextColor(Color.WHITE);
                    name_2.setTextColor(Color.BLACK);*/

                    //   name_1.setTextColor(getResources().getColor(R.color.orange));

          /*      name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));*/

                    decide_layout(last_view(), select_1);
                    // animation_slide( temp1,  temp3,  view_1, view_2);
                    last_clicked_position=1;

                    overridePendingTransition(0, 0);


                }
            });
            st_1.setOnClickListener(new OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(secondid!=null) {
                        //  firstid=secondid;
                        Log.i(TAG, "first st_2 = " + secondid);
                        // listview_fillup();
                        select_2.setVisibility(View.VISIBLE);
                        select_1.setVisibility(View.GONE);
                        select_3.setVisibility(View.GONE);
                        select_4.setVisibility(View.GONE);
                        select_5.setVisibility(View.GONE);

                        //   name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                        //   name_2.setTextColor(getResources().getColor(R.color.orange));
               /* name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));*/

                        Intent i = new Intent(getApplicationContext(), com.waterworks.SwimcompititionRegisterStep3Activity.class);

                        i.putExtra("datevalue", DateValue);
                        i.putExtra("time", time);
                        i.putExtra("eventdates", eventdates);
                        i.putExtra("MeetDate_Display", MeetDate_Display);
                        startActivity(i);
                        overridePendingTransition(0, 0);

                        decide_layout(last_view(), select_2);
                        // animation_slide(temp1, temp3, view_1, view_2);
                        last_clicked_position = 2;

                    }else{
                        Log.i(TAG, "first st_2 not null= " + firstid);
                        Intent i = new Intent(getApplicationContext(), SwimcompititionRegisterStep3_listActivity.class);
                        i.putExtra("datevalue", DateValue);
                        i.putExtra("time", time);
                        i.putExtra("eventdates", eventdates);
                        i.putExtra("MeetDate_Display", MeetDate_Display);
                        startActivity(i);
                        overridePendingTransition(0, 0);

                    }
                }
            });
            st_3.setOnClickListener(new OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(thirdid!=null) {
                        //  firstid=secondid;
                        Log.i(TAG, "third st_3 = " + thirdid);
                        // listview_fillup();
                        select_1.setVisibility(View.GONE);
                        select_2.setVisibility(View.GONE);
                        select_3.setVisibility(View.VISIBLE);
                        select_4.setVisibility(View.GONE);
                        select_5.setVisibility(View.GONE);

                        //   name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                        //   name_2.setTextColor(getResources().getColor(R.color.orange));
               /* name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));*/

                        Intent i = new Intent(getApplicationContext(), SwimcompititionRegisterStep3_list3_Activity.class);
                        i.putExtra("datevalue", DateValue);
                        i.putExtra("time", time);
                        i.putExtra("eventdates", eventdates);
                        i.putExtra("MeetDate_Display", MeetDate_Display);
                        startActivity(i);
                        overridePendingTransition(0, 0);

                        decide_layout(last_view(), select_2);
                        // animation_slide(temp1, temp3, view_1, view_2);
                        last_clicked_position = 2;

                    }else{
                        Log.i(TAG, "first st_2 not null= " + firstid);
                        Intent i = new Intent(getApplicationContext(), com.waterworks.SwimcompititionRegisterStep3Activity.class);
                        i.putExtra("datevalue", DateValue);
                        i.putExtra("time", time);
                        i.putExtra("eventdates", eventdates);
                        i.putExtra("MeetDate_Display", MeetDate_Display);
                        startActivity(i);
                        overridePendingTransition(0, 0);

                    }
                }
            });
      /*  st_3.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(thirdid!=null) {
                    //  firstid=secondid;
                    Log.i(TAG, "first st_3 = " + thirdid);
                    // listview_fillup();
                    select_1.setVisibility(View.GONE);
                    select_2.setVisibility(View.VISIBLE);
                    select_3.setVisibility(View.GONE);
                    select_4.setVisibility(View.GONE);
                    select_5.setVisibility(View.GONE);

                    //   name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                    //   name_2.setTextColor(getResources().getColor(R.color.orange));
               *//* name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));*//*

                    Intent i = new Intent(getApplicationContext(), SwimcompititionRegisterStep3_list3_Activity.class);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                    overridePendingTransition(0, 0);

                    decide_layout(last_view(), select_2);
                    // animation_slide(temp1, temp3, view_1, view_2);
                    last_clicked_position = 2;

                }else{
                    Log.i(TAG, "first st_3 not null= " + thirdid);
                    Intent i = new Intent(getApplicationContext(), SwimcompititionRegisterStep3Activity.class);
                    i.putExtra("datevalue", DateValue);
                    i.putExtra("time", time);
                    i.putExtra("eventdates", eventdates);
                    i.putExtra("MeetDate_Display", MeetDate_Display);
                    startActivity(i);
                    overridePendingTransition(0, 0);

                }
            }
        });*/

    /*    st_4.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                firstid= thirdid;
                Log.i(TAG, "first st_4 = " + firstid);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.VISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.orange));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));
                decide_layout(last_view(), select_4);
                //animation_slide( temp1, temp3,  view_1, view_2);
                last_clicked_position=4;
            }
        });

        st_5.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                firstid=thirdid;
                Log.i(TAG, "first st_5 = " + firstid);
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.VISIBLE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.VISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.orange));

                decide_layout(last_view(), select_5);
                //animation_slide(temp1, temp3, view_1, view_2);
                last_clicked_position=5;
            }
        });*/
            btnContinue = (Button) findViewById(R.id.btnContinue);
            btnContinue.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    if (temp_value.size()==0) {
                        Toast.makeText(getApplicationContext(),
                                "Please select Events", Toast.LENGTH_LONG)
                                .show();
                    }
                    else {

                        if(checkedForAll()){
                            Log.i(TAG, "name = " + t_sname);

                            Intent i = new Intent(SwimcompititionRegisterStep3_listActivity.this,
                                    SwimcompititionRegisterStep4Activity.class);
                            AppConfiguration.SelectedEventDataStep2 = temp_value.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                            Log.e("SelectedEventDataStep2_2", ""+ AppConfiguration.SelectedEventDataStep2);
                            i.putExtra("firstname", firstname);
                            i.putExtra("secondname",secondname);
                            i.putExtra("thirdname",thirdname);
                            i.putExtra("datevalue", DateValue);
                            i.putExtra("time", time);
                            i.putExtra("eventdates", eventdates);
                            i.putExtra("MeetDate_Display", MeetDate_Display);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), "Please make atleast single student for each child." ,
                                    Toast.LENGTH_LONG).show();
                      /*
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
                                    .setActionTextColor(Color.RED);*/
                        }
                    }
                }
            });
            distributeStudentsTop();}}




    public View last_view() {
        View temp = null;
        if (last_clicked_position == 1) {
            temp = select_1;
            Log.d("Pos1", "select 1");
        } else if (last_clicked_position == 2) {
            temp = select_2;
            Log.d("Pos1", "select 2");
        } else if (last_clicked_position == 3) {
            temp = select_3;
            Log.d("Pos1", "select 3");
        } else if (last_clicked_position == 4) {
            temp = select_4;
            Log.d("Pos1", "select 4");
        } else if (last_clicked_position == 5) {
            temp = select_5;
            Log.d("Pos1", "select 5");
        }
        return temp;
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    boolean slide_left = false;

    public void decide_layout(final View v_1, final View v_2) {

        int pos_1 = Integer.parseInt(v_1.getTag().toString());
        int pos_2 = Integer.parseInt(v_2.getTag().toString());

        Log.d("Pos1", String.valueOf(pos_1));
        Log.d("Pos2", String.valueOf(pos_2));

        select_lay(pos_1,pos_2);
        if (pos_1 < pos_2) {
            view_1 = v_1;
            view_2 = v_2;
            slide_left=true;
        } else {
            view_1 = v_1;
            view_2 = v_2;
            slide_left=false;
        }
    }
    LinearLayout temp1,temp2,temp3,temp4;
    public void select_lay(int pos_1,int pos_2){

        if(pos_1==1){
            temp1=days_st_1;
//            temp2=lay_1;
        }else if(pos_1==2){
            temp1=days_st_2;
//            temp2=lay_2;
        }else if(pos_1==3){
            temp1=days_st_3;
//            temp2=lay_3;
        }else if(pos_1==4){
            temp1=days_st_4;
//            temp2=lay_4;
        }else if(pos_1==5){
            temp1=days_st_5;
//            temp2=lay_5;
        }

        if(pos_2==1){
            temp3=days_st_1;
//            temp4=lay_1;
        }else if(pos_2==2){
            temp3=days_st_2;
//            temp4=lay_2;
        }else if(pos_2==3){
            temp3=days_st_3;
//            temp4=lay_3;
        }else if(pos_2==4){
            temp3=days_st_4;
//            temp4=lay_4;
        }else if(pos_2==5){
            temp3=days_st_5;
//            temp4=lay_5;
        }



        for (int i = 0; i < AppConfiguration.selectedStudentsName.size(); i++) {
            if (i == 0) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
//					name_1.setText(temp[0]+"\n"+temp[1]);
                    name_1.setText(temp[0]);
                } else {
                    name_1.setText(AppConfiguration.selectedStudentsName.get(i));
                }
            } else if (i == 1) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
//					name_2.setText(temp[0]+"\n"+temp[1]);
                    name_2.setText(temp[0]);
                } else {
                    name_2.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            } else if (i == 2) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
//					name_3.setText(temp[0]+"\n"+temp[1]);
                    name_3.setText(temp[0]);
                } else {
                    name_3.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            } else if (i == 3) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
//					name_4.setText(temp[0]+"\n"+temp[1]);
                    name_4.setText(temp[0]);
                } else {
                    name_4.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            } else if (i == 4) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
                    name_5.setText(temp[0] + "\n" + temp[1]);
                    name_5.setText(temp[0]);
                } else {
                    name_5.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            }
        }
    }



    public void distributeStudentsTop() {
        int size = AppConfiguration.selectedStudentsName.size();

        if (size == 1) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.GONE);
            st_3.setVisibility(View.GONE);
            st_4.setVisibility(View.GONE);
            st_5.setVisibility(View.GONE);
        } else if (size == 2) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.GONE);
            st_4.setVisibility(View.GONE);
            st_5.setVisibility(View.GONE);
        } else if (size == 3) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.VISIBLE);
            st_4.setVisibility(View.GONE);
            st_5.setVisibility(View.GONE);
        } else if (size == 4) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.VISIBLE);
            st_4.setVisibility(View.VISIBLE);
            st_5.setVisibility(View.GONE);
        } else if (size == 5) {
            st_1.setVisibility(View.VISIBLE);
            st_2.setVisibility(View.VISIBLE);
            st_3.setVisibility(View.VISIBLE);
            st_4.setVisibility(View.VISIBLE);
            st_5.setVisibility(View.VISIBLE);
        }}
    public void clearArray(){
        StudentID.clear();
        _StudentID.clear();
        _StudentName.clear();
    }
    public boolean checkedForAll(){
        boolean selectedAll = false;
        //HashSet<String> list = new HashSet<String>();
        AppConfiguration.temp_id = AppConfiguration.swimComptitionStudentID.toString().split(",");
        for (int i = 0; i < AppConfiguration.temp_id.length; i++) {
            System.out.println("TempID : "+AppConfiguration.temp_id[i]);
            for (int j = 0; j < temp_value.size(); j++) {
                String tempsss = temp_value.get(j);
                if(tempsss.contains(AppConfiguration.temp_id[i].toString().trim())){
                    studentlist.add(AppConfiguration.temp_id[i]);
                    System.out.println("TempID added: "+AppConfiguration.temp_id[i]);
                }
            }
        }

        System.out.println("List 3_list : " + studentlist.size());
        System.out.println("Array 3_list: "+AppConfiguration.temp_id.length);
        if( studentlist.size()== AppConfiguration.temp_id.length){
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
        param.put("MeetdatetimeValue",DateValue);
        param.put("MeetdatetimeText",time);
        param.put("ChildListArry", secondid);

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
                    JSONArray jsonArray = jsonChildNode
                            .getJSONArray("EvantDetails");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jObject = jsonArray
                                .getJSONObject(j);
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
                    }
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

  /*  class LoadSelectedChildDataListAsyncTask extends
            AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimcompititionRegisterStep3Activity.this);
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
                        SwimcompititionRegisterStep3Activity.this,
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
        private_lessons final ArrayList<HashMap<String, String>> data;
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

            return 1;
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
*/
	/*-------------------------------------------new code-------------------------------------*/

    private class LoadChildData extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimcompititionRegisterStep3_listActivity.this);
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
                    DateValue);
            param.put("MeetdatetimeText",
                    time);
            //  param.put("ChildListArry", _nameid);
            param.put("ChildListArry", secondid);
            String responseString = WebServicesCall
                    .RunScript(
                            AppConfiguration.swimCompititionStep2URL, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                if(reader!=null){
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
                    }}else
                {
                    Toast.makeText(getApplicationContext(), "Null data" + message,
                            Toast.LENGTH_LONG).show();
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
                    System.out.println("St : "+StudentID.size());
                    System.out.println("st : "+_StudentID.size());
                    itemData.add(new SwimCompRegi2Item(_StudentID.get(i),
                            _StudentName.get(i), Event.get(i), AgeGroup.get(i),
                            Distance.get(i), StrokeType.get(i), StudentID
                            .get(i), tdid.get(i), ControlType.get(i),
                            Text.get(i), Value.get(i)));
                }
                _adpter = new SwimCompRegi2Adapter(SwimcompititionRegisterStep3_listActivity.this,
                        R.layout.list_row_swim_compitition2, itemData);
                if(_adpter.getCount()>0){
                    list.setAdapter(_adpter);
                }else{
                    list.setAdapter(null);
                }



            } else {
                Toast.makeText(getApplicationContext(), "" + message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

   /* Fragment fragment = null;
    int myid;
    boolean first_time_trans = true;

    private_lessons void displayView(int position) {
        // update the main content by replacing fragments
        //		Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0:
               // AppConfiguration.makeup_Clicked = 0;
                fragment = new HomeFragment();
                myid = fragment.getId();
              //  mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case 1:
                fragment = new CheckInFragment();
                break;
           *//**//* case 2:
                Intent acc = new Intent(DashBoardActivity.this, d2_MyProfile.class);
                startActivity(acc);*//**//*
//                fragment = new MyAccountw1Fragment();
                break;
        }

        if (fragment != null) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            if (fragment instanceof HomeFragment) {
                if (first_time_trans) {
                    first_time_trans = false;
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0, 0)
                            .replace(R.id.frame_container, fragment).commit();

//                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                } else {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0, 0)
                            .replace(R.id.frame_container, fragment).commit();
//                    R.anim.slide_in_left, R.anim.slide_out_right
//                    fragmentTransaction.setCustomAnimations(R.animator.slide_out_right, R.animator.slide_in_left);
                }
            } else {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(0,0)
                        .replace(R.id.frame_container, fragment).commit();
            }}
    }*/

    private class LoadChildData2 extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SwimcompititionRegisterStep3_listActivity.this);
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
                    DateValue);
            param.put("MeetdatetimeText",
                    time);
            //  param.put("ChildListArry", _nameid);
            param.put("ChildListArry", thirdid);
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

                for (int i = 0; i < StudentID.size(); i++) {
                    System.out.println("St : "+StudentID.size());
                    System.out.println("st : "+_StudentID.size());
                    itemData2.add(new SwimCompRegi2Item2(_StudentID.get(i),
                            _StudentName.get(i), Event.get(i), AgeGroup.get(i),
                            Distance.get(i), StrokeType.get(i), StudentID
                            .get(i), tdid.get(i), ControlType.get(i),
                            Text.get(i), Value.get(i)));
                }
                //list3.setAdapter(adpter2);

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

            return 1;
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
                  /*  name_1.setText(_itemData
                            .get_StudentName());
                    name_2.setText(_itemData
                            .get_StudentName());
                    name_3.setText(_itemData
                            .get_StudentName());*/
             /*       selstudentname.setText(_itemData
                            .get_StudentName());*/
                    holder.tv_swim_comp_regi2_name.setVisibility(ViewGroup.GONE);
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
                        txtAgeGroup.setVisibility(View.GONE);
                        txttDid.setVisibility(View.GONE);
                        txtDistance.setVisibility(View.GONE);

                       /* txtStrokeType.setText(Html
                                .fromHtml("<b>StrokeType: </b>"
                                        + _itemData.getStrokeType().get(i)));*/

                        txtStrokeType.setText(Html
                                .fromHtml("<b>" + _itemData.getStrokeType().get(i) + " - " + _itemData.getDistance().get(i) + "</b>"));

                    /*    txtDistance.setText(Html.fromHtml("<b>Distance: </b>"
                                + _itemData.getDistance().get(i)));*/
                        txtEvent.setText( _itemData.getAgeGroup().get(i)+" "+" - "+"Event"+" # "+
                                _itemData.getEvent().get(i));
                    /*    txtEvent.setText(Html.fromHtml("<b>Event#: </b>"
                                + _itemData.getEvent().get(i)));*/

                        String controlType = _itemData.getControlType().get(i);
                        if (controlType.contains("CheckBox")) {
                            chk.setVisibility(View.VISIBLE);
                            chk.setTag(_itemData.getValue().get(i));
                            txtControlType.setVisibility(View.GONE);
                            for (int j = 0; j < data.size(); j++) {
                                AppConfiguration.itemChecked2.add(j, false); // initializes all items value with false
                            }

                            chk.setChecked(AppConfiguration.itemChecked2.get(position));
                            chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    if (isChecked) {
                                        // AppConfiguration.selectedStudents.set(
                                        // position, true);
                                        Log.e("chk value false",
                                                "itemChecked2" + AppConfiguration.itemChecked2 + position);
                                        AppConfiguration.itemChecked2.set(position, true);
                                        AppConfiguration.selectedStudent3.add(_itemData.getStrokeType().get(pos) + " - " + _itemData.getDistance().get(pos));
                                        AppConfiguration.selectedStudent4.add(_itemData.getAgeGroup().get(pos) +" - " + "Event" + " # "+  _itemData.getEvent().get(pos) );
                                        Log.e("AppConfiguration.selectedStudent3 list",
                                                "AppConfiguration.selectedStudent3" + AppConfiguration.selectedStudent3);
                                        Log.e("AppConfiguration.selectedStudent4 list",
                                                "AppConfiguration.selectedStudent4" + AppConfiguration.selectedStudent4);
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
                                        AppConfiguration.itemChecked2.set(position, false);
                                        AppConfiguration.selectedStudent3.remove(_itemData.getStrokeType().get(pos) + " - " + _itemData.getDistance().get(pos));
                                        AppConfiguration.selectedStudent4.remove(_itemData.getAgeGroup().get(pos) + " - " + "Event" + " # "+  _itemData.getEvent().get(pos));
                                        Log.e("AppConfiguration.selectedStudent3 list", "AppConfiguration.selectedStudent1 remove" + AppConfiguration.selectedStudent3);
                                        Log.e("AppConfiguration.selectedStudent4",
                                                "AppConfiguration.selectedStudent4 list remove" + AppConfiguration.selectedStudent4);
                                        Log.e("chk value false",
                                                "itemChecked2" + AppConfiguration.itemChecked2 + position);


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
    /* public void listview_fillup(){
         _adpter1 = new SwimCompRegi2Adapter1(SwimcompititionRegisterStep3Activity.this,
                 R.layout.list_row_swim_compitition2, itemData1);
         new LoadChildData1().execute();
     }*/
    public class SwimCompRegi2Item1 {
        String _StudentId, _StudentName;
        ArrayList<String> Event, AgeGroup, Distance, StrokeType, StudentID,
                tdid, ControlType, Text, Value;

        public SwimCompRegi2Item1(String _StudentId, String _StudentName,
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

    public class SwimCompRegi2Adapter1 extends ArrayAdapter<SwimCompRegi2Item1> {
        List<SwimCompRegi2Item1> data;
        Context context;
        int layoutResID;
        LayoutInflater inflater;

        public SwimCompRegi2Adapter1(Context context, int resource,
                                     List<SwimCompRegi2Item1> data) {
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
        public SwimCompRegi2Item1 getItem(int position) {
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

                    final SwimCompRegi2Item1 _itemData = data.get(position);
                    holder.tv_swim_comp_regi2_name.setText(_itemData
                            .get_StudentName());
                  /*  name_1.setText(_itemData
                            .get_StudentName());
                    name_2.setText(_itemData
                            .get_StudentName());
                    name_3.setText(_itemData
                            .get_StudentName());*/
             /*       selstudentname.setText(_itemData
                            .get_StudentName());*/
                    holder.tv_swim_comp_regi2_name.setVisibility(ViewGroup.GONE);
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
                        txtAgeGroup.setVisibility(View.GONE);
                        txttDid.setVisibility(View.GONE);
                        txtDistance.setVisibility(View.GONE);

                       /* txtStrokeType.setText(Html
                                .fromHtml("<b>StrokeType: </b>"
                                        + _itemData.getStrokeType().get(i)));*/

                        txtStrokeType.setText(Html
                                .fromHtml("<b>" + _itemData.getStrokeType().get(i) + " - " + _itemData.getDistance().get(i) + "</b>"));

                    /*    txtDistance.setText(Html.fromHtml("<b>Distance: </b>"
                                + _itemData.getDistance().get(i)));*/
                        txtEvent.setText( _itemData.getAgeGroup().get(i)+" "+" - "+"Event"+" # "+
                                _itemData.getEvent().get(i));
                    /*    txtEvent.setText(Html.fromHtml("<b>Event#: </b>"
                                + _itemData.getEvent().get(i)));*/

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

    public class SwimCompRegi2Item2 {
        String _StudentId, _StudentName;
        ArrayList<String> Event, AgeGroup, Distance, StrokeType, StudentID,
                tdid, ControlType, Text, Value;

        public SwimCompRegi2Item2(String _StudentId, String _StudentName,
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

    public class SwimCompRegi2Adapter2 extends ArrayAdapter<SwimCompRegi2Item2> {
        List<SwimCompRegi2Item2> data;
        Context context;
        int layoutResID;
        LayoutInflater inflater;

        public SwimCompRegi2Adapter2(Context context, int resource,
                                     List<SwimCompRegi2Item2> data) {
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
        public SwimCompRegi2Item2 getItem(int position) {
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

                    final SwimCompRegi2Item2 _itemData = data.get(position);
                    holder.tv_swim_comp_regi2_name.setText(_itemData
                            .get_StudentName());
                  /*  name_1.setText(_itemData
                            .get_StudentName());
                    name_2.setText(_itemData
                            .get_StudentName());
                    name_3.setText(_itemData
                            .get_StudentName());*/
             /*       selstudentname.setText(_itemData
                            .get_StudentName());*/
                    holder.tv_swim_comp_regi2_name.setVisibility(ViewGroup.GONE);
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
                        txtAgeGroup.setVisibility(View.GONE);
                        txttDid.setVisibility(View.GONE);
                        txtDistance.setVisibility(View.GONE);

                       /* txtStrokeType.setText(Html
                                .fromHtml("<b>StrokeType: </b>"
                                        + _itemData.getStrokeType().get(i)));*/

                        txtStrokeType.setText(Html
                                .fromHtml("<b>" + _itemData.getStrokeType().get(i) + " - " + _itemData.getDistance().get(i) + "</b>"));

                    /*    txtDistance.setText(Html.fromHtml("<b>Distance: </b>"
                                + _itemData.getDistance().get(i)));*/
                        txtEvent.setText( _itemData.getAgeGroup().get(i)+" "+" - "+"Event"+" # "+
                                _itemData.getEvent().get(i));
                    /*    txtEvent.setText(Html.fromHtml("<b>Event#: </b>"
                                + _itemData.getEvent().get(i)));*/

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
        _nameid1 = "";
        _nameid2 = "";
        _nameid3= "";
        _nameid4 = "";
        _nameid5 = "";
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
		super.onBackPressed();
       /* Intent i=new Intent(SwimcompititionRegisterStep3_listActivity.this,SwimcompititionRegisterStep2Activity.class);
        i.putExtra("datevalue", DateValue);
        i.putExtra("time", time);
        i.putExtra("eventdates", eventdates);
        i.putExtra("MeetDate_Display", MeetDate_Display);
        startActivity(i);*/
        ClearArray();
    }
	/*-------------------------------------------new code-------------------------------------*/

}
