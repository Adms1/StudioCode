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
public class WaterPoloRegister1 extends Activity {

    private static String TAG = "WaterPolo1";
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
    ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
//    ListView list;
    LinearLayout llListData;
    TextView tv_fsn_info, txtPriceinfo, txtSelChildText;
    String textInfo, textPriceInfo;
    ProgressDialog pd;
    ScrollView scrollviewpoloregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waterpolo_register1);

        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        Initialization();

        pd = new ProgressDialog(WaterPoloRegister1.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
        new Thread(new Runnable() {

            @Override
            public void run() {

                textInfo = Utility.getProgramsInstructionText("9");

                isInternetPresent = Utility.isNetworkConnected(WaterPoloRegister1.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new GetSite().execute();
                    new GetChild().execute();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        tv_fsn_info.setText(textInfo);
                    }
                });
            }
        }).start();


    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(WaterPoloRegister1.this);
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
                if (!btn_site.getText().toString().trim().equalsIgnoreCase("Select site")) {
                    if (sendingID.size() > 0) {
                        final ArrayList<String> studentNAME = new ArrayList<String>();
                        for (int i = 0; i < sendingID.size(); i++) {
                            for (HashMap<String, String> row : childList) {
                                if (sendingID.get(i).equalsIgnoreCase(row.get("StudentID"))) {
                                    studentNAME.add(row.get("StudentName"));
                                }
                            }
                        }

                        AppConfiguration.waterPoloStudentID = sendingID.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        AppConfiguration.waterPoloStudentName = studentNAME.toString().replaceAll("\\[", "").replaceAll("\\]", "");

                        Intent i = new Intent(getApplicationContext(), WaterPoloRegister2.class);
                        i.putExtra("siteid", siteID);
                        i.putExtra("stulist", sendingID.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                        i.putExtra("childno", "" + sendingID.size());
                        startActivity(i);
//					finish();
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
        // TODO Auto-generated method stub
        View include_layout_step_boxes = (View) findViewById(R.id.include_layout_step_boxes);
        TextView txtBox1 = (TextView) include_layout_step_boxes.findViewById(R.id.txtBox1);
        txtBox1.setTextColor(Color.WHITE);
        txtBox1.setBackgroundColor(getResources().getColor(R.color.design_change_d2));
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(WaterPoloRegister1.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        btn_site = (Button) findViewById(R.id.btn_dt_sites);
        txtPriceinfo = (TextView) findViewById(R.id.txtPriceinfo);
        txtSelChildText = (TextView) findViewById(R.id.txtSelChildText);
        relMenu = (Button) findViewById(R.id.relMenu);
        llListData = (LinearLayout) findViewById(R.id.llListData);
        tv_fsn_info = (TextView) findViewById(R.id.tv_fsn_info);

        lpw_site = new ListPopupWindow(getApplicationContext());
        StudentName = new ArrayList<String>();
        Studentid = new ArrayList<String>();
        btn_continue = (CardView) findViewById(R.id.btn_continue_dt1);
        scrollviewpoloregister=(ScrollView)findViewById(R.id.scrollviewpoloclinics);
        tv_fsn_info.setVerticalScrollBarEnabled(true);
        tv_fsn_info.setMovementMethod(new ScrollingMovementMethod());
        scrollviewpoloregister.setOnTouchListener(new View.OnTouchListener() {
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


    //////////////////////////////////GetSite////////////////////////

    private class GetSite extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.Get_WaterpoloSiteList, param);
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
            if (data_load_site.toString().equalsIgnoreCase("True")) {
                lpw_site.setAdapter(new ArrayAdapter<String>(
                        getApplicationContext(),
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
                                textPriceInfo = Utility.getProgramsPricingText("9", siteID);
                                if (textPriceInfo.equalsIgnoreCase("")) {
                                    txtPriceinfo.setVisibility(View.GONE);
                                } else {
                                    txtPriceinfo.setVisibility(View.VISIBLE);
                                    txtPriceinfo.setText(textPriceInfo);
                                }

                                lpw_site.dismiss();
                            }
                        });
            } else {
                Toast.makeText(WaterPoloRegister1.this, "Some internaml error,Please try after sometime.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /////////////////GetChild////////////////////

    private class GetChild extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //data = 1;
            Studentid.clear();
            StudentName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("ProgName", "Water Polo Conditioning");

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.GetChildDT, param);
            readAndParseJSON(responseString);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (data_load_child.toString().equals("True")) {
                sendingID = new ArrayList<String>();
                loadDataList(childList);
            } else {
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

                    String StudentID = jsonChildNode.getString("Studentid")
                            .trim();
                    String StudentName = jsonChildNode.getString("StudentName")
                            .trim();

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

    public void loadDataList(ArrayList<HashMap<String, String>> list){
        TextView textview;
        CheckBox checkbox;
        int id;
        try{
            for(int i = 0;i < list.size();i++) {
                View convertView = LayoutInflater.from(WaterPoloRegister1.this).inflate(R.layout.list_row_swim_camp_register1, null);
                textview = (TextView) convertView.findViewById(R.id.txtStudentName);
                checkbox = (CheckBox) convertView.findViewById(R.id.chb_students);

                checkbox.setId(Integer.parseInt(childList.get(i).get("StudentID")));
                textview.setId(i);
                checkbox.setOnCheckedChangeListener(onCheckedChangeListener);
                id = i;
                textview.setText(childList.get(i).get("StudentName"));
                llListData.addView(convertView);
            }
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        catch(Exception e){
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

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final ArrayList<HashMap<String, String>> data;

        public CustomList(Activity context,
                          ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_row_swim_camp_register1);
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

        class ViewHolder {
            TextView textview;
            CheckBox checkbox;
            int id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.list_row_swim_camp_register1, null);
                holder.textview = (TextView) convertView
                        .findViewById(R.id.txtStudentName);
                holder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.chb_students);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(Integer.parseInt(childList.get(position).get("StudentID")));
            holder.textview.setId(position);
            holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked) {
                        sendingID.add("" + holder.checkbox.getId());
                    } else {
                        sendingID.remove("" + holder.checkbox.getId());
                    }
                    Log.e(TAG, "" + sendingID);
                }
            });
            holder.textview.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                }
            });
            holder.textview.setText(childList.get(position).get("StudentName"));
            holder.id = position;
            return convertView;

        }
    }
}
