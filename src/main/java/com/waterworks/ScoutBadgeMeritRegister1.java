package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class ScoutBadgeMeritRegister1 extends Activity {

    private static String TAG = "ScoutMerit1";
    Button relMenu;
    String token, familyID, msg;
    Boolean isInternetPresent = false;
    ArrayList<String> sitename, siteid;
    ListPopupWindow lpw_site;
    String siteID, data_load_site = "False", data_load_basket = "False", data_load_child = "False", savebasket = "False";
    Button btn_site;
    CardView btn_continue;
    //	TableLayout table_dt_childs;
    ArrayList<String> StudentName, Studentid, sendingID;
    TextView txtSelChildText, txtPriceinfo;
    EditText tv_fsn_info;
    //	ListView list;
    LinearLayout llListData;
    ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
ScrollView scrollviewmeritbadge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoutmeritregister1);

        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        isInternetPresent = Utility.isNetworkConnected(ScoutBadgeMeritRegister1.this);
        if (isInternetPresent) {
            Initialization();
            if (AppConfiguration.BasketID.toString().equalsIgnoreCase("0")) {
                new GetBasketID().execute();
            }
            new SaveBasketId().execute();
            new GetSite().execute();
            new GetChild().execute();
        } else {
            onDetectNetworkState().show();
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ScoutBadgeMeritRegister1.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        onBackPressed();
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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
        btn_site.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lpw_site.show();
            }
        });
        btn_continue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!btn_site.getText().toString().equalsIgnoreCase("Select site")) {
                    if (sendingID.size() > 0) {
                        final ArrayList<String> studentNAME = new ArrayList<String>();
                        for (int i = 0; i < sendingID.size(); i++) {
                            for (HashMap<String, String> row : childList) {
                                if (sendingID.get(i).equalsIgnoreCase(row.get("StudentID"))) {
                                    studentNAME.add(row.get("StudentName"));
                                }
                            }
                        }

                        AppConfiguration.meritBadgesStudentID = sendingID.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        AppConfiguration.meritBadgesStudentName = studentNAME.toString().replaceAll("\\[", "").replaceAll("\\]", "");

                        Intent i = new Intent(getApplicationContext(), ScoutBadgeMeritRegister2.class);
                        i.putExtra("siteid", siteID);
                        i.putExtra("stulist", sendingID.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                        i.putExtra("childno", "" + sendingID.size());
                        startActivity(i);
//                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select at least one child.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select site.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void Initialization() {
        View include_layout_step_boxes = (View) findViewById(R.id.include_layout_step_boxes);
        TextView txtBox1 = (TextView) include_layout_step_boxes.findViewById(R.id.txtBox1);
        txtBox1.setTextColor(Color.WHITE);
        txtBox1.setBackgroundColor(getResources().getColor(R.color.design_change_d2));
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ScoutBadgeMeritRegister1.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        llListData = (LinearLayout) findViewById(R.id.llListData);
        btn_site = (Button) findViewById(R.id.btn_dt_sites);
        relMenu = (Button) findViewById(R.id.relMenu);
        lpw_site = new ListPopupWindow(getApplicationContext());
//		table_dt_childs = (TableLayout)findViewById(R.id.table_dt_childs);
        StudentName = new ArrayList<String>();
        Studentid = new ArrayList<String>();
        btn_continue = (CardView) findViewById(R.id.btn_continue_dt1);
        tv_fsn_info = (EditText) findViewById(R.id.tv_fsn_info);
        tv_fsn_info.setText(Utility.getProgramsInstructionText("7"));
        tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
        txtSelChildText = (TextView) findViewById(R.id.txtSelChildText);
        txtPriceinfo = (TextView) findViewById(R.id.txtPriceinfo);
        scrollviewmeritbadge=(ScrollView)findViewById(R.id.scrollviewmeritbadge);

        tv_fsn_info.setVerticalScrollBarEnabled(true);
        tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
        scrollviewmeritbadge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        tv_fsn_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv_fsn_info.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }
    //////////////////////Basket ID////////////
    public class GetBasketID extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ScoutBadgeMeritRegister1.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("siteid", AppConfiguration.basket_siteid);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.Get_BasketID, param);
            GetBasketID(responseString);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(isInternetPresent) {
                if (pd != null) {
                    pd.dismiss();
                }
                if (data_load_basket.toString().equalsIgnoreCase("True")) {
                    Log.i(TAG, "Get basket id");
                    new SaveBasketId().execute();
                } else {
                    Log.i(TAG, "Not Get basket id");
                }
            }else{
                onDetectNetworkState();
            }
        }
    }
    public void GetBasketID(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("BasketDtl");
                if (jsonMainNode.toString().equalsIgnoreCase("")) {
                } else {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        AppConfiguration.BasketID = jsonChildNode.getString("Basketid");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ////////////////////SaveBasketId//////////////
    private class SaveBasketId extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ScoutBadgeMeritRegister1.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("basketid", AppConfiguration.BasketID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_MeritBadgesSiteList, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                savebasket = reader.getString("Success");
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
            if (savebasket.toString().equalsIgnoreCase("True")) {
                Log.i(TAG, "Basket Saved");
            } else {
                Log.i(TAG, "Basket Not Saved");
            }
        }
    }
    /////////////////////GetSite///////////
    private class GetSite extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ScoutBadgeMeritRegister1.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_MeritBadgesSiteList, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_site = reader.getString("Success");
                if (data_load_site.toString().equals("True")) {
                    siteid = new ArrayList<String>();
                    sitename = new ArrayList<String>();


                    JSONArray jsonMainNode = reader.optJSONArray("Sites");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        siteid.add(jsonChildNode.getString("SiteID"));
                        sitename.add(jsonChildNode.getString("SiteName"));
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
            if (data_load_site.toString().equalsIgnoreCase("True")) {
                lpw_site.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.edittextpopup, sitename));
                lpw_site.setAnchorView(btn_site);
                lpw_site.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_site.setModal(true);
                lpw_site.setOnItemClickListener(
                        new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int pos, long id) {
                                // TODO Auto-generated method stub
                                btn_site.setText(sitename.get(pos));
                                siteID = siteid.get(pos);
                                String textPrice = Utility.getProgramsPricingText("7", siteID);
                                if (textPrice.equalsIgnoreCase("")) {
                                    txtPriceinfo.setVisibility(View.GONE);
                                } else {
                                    txtPriceinfo.setVisibility(View.VISIBLE);
                                    txtPriceinfo.setText(textPrice);
                                }
                                lpw_site.dismiss();
                            }
                        });
            } else {
                Toast.makeText(ScoutBadgeMeritRegister1.this, "Some internaml error,Please try after sometime.", Toast.LENGTH_LONG).show();
            }
        }
    }
    /////////////////GetChild////////////////////
    private class GetChild extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(ScoutBadgeMeritRegister1.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            //data = 1;
            Studentid.clear();
            StudentName.clear();
//			table_dt_childs.removeAllViews();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("ProgName", "Lifesaving/Swimming Merit Badges");

            String responseString = WebServicesCall.RunScript(AppConfiguration.GetChildDT, param);
            readAndParseJSON(responseString);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (pd != null) {
                pd.dismiss();
            }
            if (data_load_child.toString().equals("True")) {
                //	rl_rs_inside_body.setVisibility(View.VISIBLE);
                sendingID = new ArrayList<String>();
                loadDataList(childList);
            } else {
                //	rl_rs_inside_body.setVisibility(View.GONE);
                txtSelChildText.setText("You do not currently have a child that is eligible for this program.");
                txtSelChildText.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            data_load_child = reader.getString("Success");
            if (data_load_child.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("StudentList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String StudentID = jsonChildNode.getString("Studentid").trim();
                    String StudentName = jsonChildNode.getString("StudentName").trim();

                    hashmap.put("StudentID", StudentID);
                    hashmap.put("StudentName", StudentName);
                    childList.add(hashmap);
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("StudentList");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                msg = jsonChildNode.getString("Msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadDataList(ArrayList<HashMap<String, String>> list) {
        TextView textview;
        CheckBox checkbox;
        int id;
        try {
            for (int i = 0; i < list.size(); i++) {
                View convertView = LayoutInflater.from(ScoutBadgeMeritRegister1.this).inflate(R.layout.list_row_swim_camp_register1, null);
                textview = (TextView) convertView.findViewById(R.id.txtStudentName);
                checkbox = (CheckBox) convertView.findViewById(R.id.chb_students);

                checkbox.setId(Integer.parseInt(childList.get(i).get("StudentID")));
                textview.setId(i);
                checkbox.setOnCheckedChangeListener(onCheckedChangeListener);
                id = i;
                textview.setText(childList.get(i).get("StudentName"));
                llListData.addView(convertView);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            CheckBox checkBox = (CheckBox) buttonView;
            if (isChecked) {
                sendingID.add("" + checkBox.getId());
            } else {
                sendingID.remove("" + checkBox.getId());
            }
            Log.e(TAG, "" + sendingID);
        }
    };
}
