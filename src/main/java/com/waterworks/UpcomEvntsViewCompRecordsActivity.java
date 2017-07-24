package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.asyncTasks.GetAllAgeAsyncTask;
import com.waterworks.asyncTasks.GetAllEventAsyncTask;
import com.waterworks.asyncTasks.GetAllLocationAsyncTask;
import com.waterworks.asyncTasks.GetPoolDataAsyncTask;
import com.waterworks.asyncTasks.SwimCmpt_AllMeetResultForEventAsyncTask;
import com.waterworks.model.PoolRecordsModel;
import com.waterworks.model.RecordsAgeGroup;
import com.waterworks.model.RecordsLocationGroups;
import com.waterworks.model.RecordsStrokeGroup;
import com.waterworks.model.UpcomingEventResultsDetailModel;
import com.waterworks.model.UpcomingEventResultsListModel;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.sheduling.ScheduleLessonFragement3;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class UpcomEvntsViewCompRecordsActivity extends Activity {

    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room, llEventRow, llPoolDataList;
    private TextView txt_1, txt_2, txt_3, txtEventName, txtDate, txtSwimmer, txtAge, txtTime, txtClearFilters;
    private Spinner spinAge, spinStroke,spinLocation;
    private View selected_1, selected_2, selected_3;
    private Context mContext = this;
    private GetAllEventAsyncTask getAllEventAsyncTask = null;
    private GetAllAgeAsyncTask getAllAgeAsyncTask = null;
    private GetAllLocationAsyncTask getAllLocationAsyncTask =null;
    private GetPoolDataAsyncTask getPoolDataAsyncTask = null;
    private ArrayList<RecordsAgeGroup> recordsAgeGroups = new ArrayList<>();
    private ArrayList<RecordsStrokeGroup> recordsStrokeGroups = new ArrayList<>();
    private ArrayList<RecordsLocationGroups>recordsLocationGroups=new ArrayList<>();
    private ArrayList<PoolRecordsModel> poolRecordsModels = new ArrayList<>();
    private ProgressDialog progressDialog = null;
    private String ageGroupID, strokeID,locationID;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_event_records);

        topTabsViewListnersAnimation();
        init();
        isInternetPresent = Utility.isNetworkConnected(UpcomEvntsViewCompRecordsActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            fetchSpinnerData();
//            fetchEventPoolData("0", "0","0");
        }
    }

    public void init() {
        spinAge = (Spinner) this.findViewById(R.id.spinAge);
        spinStroke = (Spinner) this.findViewById(R.id.spinStroke);
        spinLocation=(Spinner)this.findViewById(R.id.spinLocation);
        txtClearFilters = (TextView) this.findViewById(R.id.txtClearFilters);
        llPoolDataList = (LinearLayout) this.findViewById(R.id.llPoolDataList);


        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        &&spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")) {
                    poolRecordsModels.clear();
                    llPoolDataList.removeAllViews();
//                    fetchEventPoolData("0", "0","0");
                } else if (position > 0 && spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")) {
                    for (int i = 0; i < recordsLocationGroups.size(); i++) {
                        if (spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())) {
                            locationID = recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData("0","0",locationID);
                } else if (position > 0 && !spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        &&!spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")) {
                   for(int i=0;i<recordsLocationGroups.size();i++){
                       if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                           locationID=recordsLocationGroups.get(i).getLocationID();
                       }
                   }

                    for (int i = 0; i < recordsAgeGroups.size(); i++) {
                        if (spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())) {
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    fetchEventPoolData(ageGroupID, strokeID,locationID);
                }else if(position>0 &&spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        &&!spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")){
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }

                    for (int i = 0; i < recordsAgeGroups.size(); i++) {
                        if (spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())) {
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    fetchEventPoolData(ageGroupID, "0",locationID);
                }else if(position>0 &&!spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        &&spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")){
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }

                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    fetchEventPoolData("0",strokeID,locationID);
                }
                else if(position==0 && !spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && !spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")){
                    for (int i =0 ; i<recordsAgeGroups.size();i++){
                        if(spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())){
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    fetchEventPoolData(ageGroupID,strokeID,"0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        spinAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {
                    poolRecordsModels.clear();
                    llPoolDataList.removeAllViews();
//                    fetchEventPoolData("0", "0","0");
                } else if (position > 0 && spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {
                    for (int i = 0; i < recordsAgeGroups.size(); i++) {
                        if (spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())) {
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    fetchEventPoolData(ageGroupID, "0","0");
                } else if (position > 0 && !spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && !spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {
                    for (int i = 0; i < recordsAgeGroups.size(); i++) {
                        if (spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())) {
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData(ageGroupID, strokeID,locationID);
                }else if(position>0 && !spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {

                    for (int i = 0; i < recordsAgeGroups.size(); i++) {
                        if (spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())) {
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for (int i =0 ; i<recordsStrokeGroups.size();i++){
                        if(spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())){
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    fetchEventPoolData(ageGroupID,strokeID,"0");
                }else if (position>0 && spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && !spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-") ){

                    for (int i = 0; i < recordsAgeGroups.size(); i++) {
                        if (spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())) {
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData(ageGroupID,"0",locationID);
                }
                else if(position==0 && !spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && !spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")){
                    for (int i =0 ; i<recordsStrokeGroups.size();i++){
                        if(spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())){
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData("0",strokeID,locationID);
                }else if(position==0&& spinStroke.getSelectedItem().toString().equalsIgnoreCase("-Select Stroke to Filter-")
                        && !spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")){

                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData("0","0",locationID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinStroke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")
                        && spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {
                    poolRecordsModels.clear();
                    llPoolDataList.removeAllViews();
//                    fetchEventPoolData("0", "0","0");
                } else if (position > 0 && spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")
                        && spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {
                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    fetchEventPoolData("0", strokeID,"0");
                } else if (position > 0 && !spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")
                        && !spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {
                    for (int i = 0; i < recordsAgeGroups.size(); i++) {
                        if (spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())) {
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData(ageGroupID, strokeID,locationID);
                }else if(position>0 && !spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")
                        && spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")) {

                    for (int i =0 ; i<recordsAgeGroups.size();i++){
                        if(spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())){
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    fetchEventPoolData(ageGroupID,strokeID,"0");
                }else if (position>0 && spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")
                        && !spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-") ){

                    for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                        if (spinStroke.getSelectedItem().toString().equalsIgnoreCase(recordsStrokeGroups.get(i).getEventName())) {
                            strokeID = recordsStrokeGroups.get(i).getEventID();
                        }
                    }
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData("0",strokeID,locationID);
                }else if(position==0 && !spinAge.getSelectedItem().toString().equalsIgnoreCase("-Select Age to Filter-")
                        && !spinLocation.getSelectedItem().toString().equalsIgnoreCase("-Select Location to Filter-")){
                    for (int i =0 ; i<recordsAgeGroups.size();i++){
                        if(spinAge.getSelectedItem().toString().equalsIgnoreCase(recordsAgeGroups.get(i).getAgeGroup())){
                            ageGroupID = recordsAgeGroups.get(i).getAgeGroupID();
                        }
                    }
                    for(int i=0;i<recordsLocationGroups.size();i++){
                        if(spinLocation.getSelectedItem().toString().equalsIgnoreCase(recordsLocationGroups.get(i).getLocationName())){
                            locationID=recordsLocationGroups.get(i).getLocationID();
                        }
                    }
                    fetchEventPoolData(ageGroupID,"0",locationID);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinAge.setSelection(0);
                spinStroke.setSelection(0);
                spinLocation.setSelection(0);
                poolRecordsModels.clear();
                llPoolDataList.removeAllViews();
            }
        });
    }

    public void fetchEventPoolData(final String ageGroupid, final String strokeID,final String LocationID) {
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getPoolDataAsyncTask = new GetPoolDataAsyncTask(UpcomEvntsViewCompRecordsActivity.this, ageGroupid, strokeID,LocationID);
                    poolRecordsModels = getPoolDataAsyncTask.execute().get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        if (poolRecordsModels.size()==0){
                            llPoolDataList.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Record Found", Toast.LENGTH_SHORT).show();
                        }else{
                            setUI();}
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void fetchSpinnerData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getAllAgeAsyncTask = new GetAllAgeAsyncTask(UpcomEvntsViewCompRecordsActivity.this);
                    recordsAgeGroups = getAllAgeAsyncTask.execute().get();

                    getAllEventAsyncTask = new GetAllEventAsyncTask(UpcomEvntsViewCompRecordsActivity.this, "0");
                    recordsStrokeGroups = getAllEventAsyncTask.execute().get();

                    getAllLocationAsyncTask =new GetAllLocationAsyncTask(UpcomEvntsViewCompRecordsActivity.this);
                    recordsLocationGroups = getAllLocationAsyncTask.execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ArrayList<String> ageList = new ArrayList<String>();
                            ageList.add("-Select Age to Filter-");
                            for (int i = 0; i < recordsAgeGroups.size(); i++) {
                                ageList.add(recordsAgeGroups.get(i).getAgeGroup());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpcomEvntsViewCompRecordsActivity.this, android.R.layout.simple_dropdown_item_1line, ageList);
                            spinAge.setAdapter(adapter);

                            ArrayList<String> eventList = new ArrayList<String>();
                            eventList.add("-Select Stroke to Filter-");
                            for (int i = 0; i < recordsStrokeGroups.size(); i++) {
                                eventList.add(recordsStrokeGroups.get(i).getEventName());
                            }

                            ArrayAdapter<String> adapterEvent = new ArrayAdapter<String>(UpcomEvntsViewCompRecordsActivity.this, android.R.layout.simple_dropdown_item_1line, eventList);
                            spinStroke.setAdapter(adapterEvent);


                            ArrayList<String> locationList=new ArrayList<String>();
                            locationList.add("-Select Location to Filter-");
                            for(int i=0;i<recordsLocationGroups.size();i++) {
                                locationList.add(recordsLocationGroups.get(i).getLocationName());
                            }
                            ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(UpcomEvntsViewCompRecordsActivity.this, android.R.layout.simple_dropdown_item_1line, locationList);
                            spinLocation.setAdapter(adapterLocation);

                            progressDialog.dismiss();

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setUI() {
        llPoolDataList.setVisibility(View.VISIBLE);
        if (llPoolDataList.getChildCount() != 0) {
            llPoolDataList.removeAllViews();
        }

        for (int i = 0; i < poolRecordsModels.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_pool_data, null, false);
            txtEventName = (TextView) child.findViewById(R.id.txtEventName);
            llEventRow = (LinearLayout) child.findViewById(R.id.llEventRow);

            txtEventName.setText(poolRecordsModels.get(i).getEventDescription());

            for (HashMap<String, String> stringStringArrayList : poolRecordsModels.get(i).getEventDetails()) {
                View childll = getLayoutInflater().inflate(R.layout.row_event_pool_data, null, false);

                txtDate = (TextView) childll.findViewById(R.id.txtDate);
                txtSwimmer = (TextView) childll.findViewById(R.id.txtSwimmer);
                txtTime = (TextView) childll.findViewById(R.id.txtTime);
                txtAge = (TextView) childll.findViewById(R.id.txtAge);

                txtDate.setText(stringStringArrayList.get("EventDate"));
                String[] swimmername = stringStringArrayList.get("Swimmer").split("\\s+");
                String name = swimmername[1];
                char first = name.charAt(0);
                txtSwimmer.setText(swimmername[0] + " " + first + ".");
                txtTime.setText(stringStringArrayList.get("MeetTime"));
                txtAge.setText(stringStringArrayList.get("Age"));

                llEventRow.addView(childll);
            }

            llPoolDataList.addView(child);
        }
    }

    public void topTabsViewListnersAnimation() {
        View view = findViewById(R.id.include_swim_comp_custom_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);

        BackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(UpcomEvntsViewCompRecordsActivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        selected_1.setVisibility(View.VISIBLE);
        selected_2.setVisibility(View.GONE);
        selected_3.setVisibility(View.GONE);

        ((AnimationDrawable) selected_1.getBackground()).start();

        ll_upcoming_meet = (LinearLayout) view.findViewById(R.id.ll_upcoming_meet);
        ll_register = (LinearLayout) view.findViewById(R.id.ll_register);
        ll_trophy_room = (LinearLayout) view.findViewById(R.id.ll_trophy_room);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        if (AppConfiguration.CheckMeet.equalsIgnoreCase("1")) {
            txt_1.setText("Todays's Meet");
        }

        ll_upcoming_meet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionUpcomingEventsAcitivity.class);
                startActivity(i);
                UpcomEvntsViewCompRecordsActivity.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });
        ll_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionRegisterAcitivity.class);
                startActivity(i);
                UpcomEvntsViewCompRecordsActivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });
        ll_trophy_room.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, SwimCompititionTrophyRoomAcitivity.class);
                startActivity(i);
                UpcomEvntsViewCompRecordsActivity.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
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
    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
