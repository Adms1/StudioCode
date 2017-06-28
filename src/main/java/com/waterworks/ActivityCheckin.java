package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.waterworks.R;
import com.waterworks.asyncTasks.GetScanCardsProgramsStatusAsyncTask;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityCheckin extends Activity {

    private Button btnSwimLesson, btnSwimTeam, btnLapSwim, btnWaterAerobics, btnPhysicalTherapy,btnMonthlyLapSwim;
    private HashMap<String, String> hashMapShowHideButtonValues = new HashMap<>();
    private String token, currentProgramID;
    private ProgressDialog progressDialog;
    Boolean isInternetPresent = false;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");

        addHeader();
        setViews();
        setListners();
    }

    public void setViews() {
        btnSwimLesson = (Button) findViewById(R.id.btnSwimLesson);
        btnSwimTeam = (Button) findViewById(R.id.btnSwimTeam);
        btnLapSwim = (Button) findViewById(R.id.btnLapSwim);
        btnWaterAerobics = (Button) findViewById(R.id.btnWaterAerobics);
        btnPhysicalTherapy = (Button) findViewById(R.id.btnPhysicalTherapy);
        btnMonthlyLapSwim=(Button)findViewById(R.id.btnMonthlyLapSwim);

        hashMapShowHideButtonValues = AppConfiguration.ShowHideScanProgramsList;
        if (hashMapShowHideButtonValues.size() > 0) {
            if (hashMapShowHideButtonValues.get("SwimLessons").equalsIgnoreCase("1")) {
                btnSwimLesson.setVisibility(View.VISIBLE);
            }
            if (hashMapShowHideButtonValues.get("SwimTeam").equalsIgnoreCase("1")) {
                btnSwimTeam.setVisibility(View.VISIBLE);
            }
            if (hashMapShowHideButtonValues.get("LapSwim").equalsIgnoreCase("1")) {
                btnLapSwim.setVisibility(View.VISIBLE);
                            }
            if (hashMapShowHideButtonValues.get("MonthlyLapSwim").equalsIgnoreCase("1")) {
                btnMonthlyLapSwim.setVisibility(View.VISIBLE);
                       }
            if (hashMapShowHideButtonValues.get("Aerobics").equalsIgnoreCase("1")) {
                btnWaterAerobics.setVisibility(View.VISIBLE);
            }
            if (hashMapShowHideButtonValues.get("PhysicalTherapy").equalsIgnoreCase("1")) {
                btnPhysicalTherapy.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getScanCardStatus(final String programID, final String flg1) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Token", token);
                    params.put("programid", programID);
                    params.put("flg1", flg1);
                    isInternetPresent = Utility.isNetworkConnected(ActivityCheckin.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        GetScanCardsProgramsStatusAsyncTask getScanCardsProgramsStatusAsyncTask = new GetScanCardsProgramsStatusAsyncTask(ActivityCheckin.this, params);
                        String responseString = getScanCardsProgramsStatusAsyncTask.execute().get();

                        parseJSON(responseString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    public void parseJSON(String responseString) {
        try {
            JSONObject reader = new JSONObject(responseString);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                if (jsonMainNode.toString().equalsIgnoreCase("")) {
                } else {
                    String successflg = "";
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        successflg = jsonChildNode.getString("successflg");
                    }
                    if (successflg.equalsIgnoreCase("1")) {
                        //You have checked in successfully
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Utility.ping(ActivityCheckin.this, "You have checked in successfully");
                                onBackPressed();
                            }
                        });
                    } else if (successflg.equalsIgnoreCase("-1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                ViewDialog alert = new ViewDialog();
                                alert.showDialog(ActivityCheckin.this, "You have already checked in for this activity. Do you want to use another session?");
                            }
                        });
                    }
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListners() {
        btnSwimLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgramID = "1";
                getScanCardStatus(currentProgramID, "false");
            }
        });
        btnSwimTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgramID = "2";
                getScanCardStatus(currentProgramID, "false");
            }
        });
        btnLapSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgramID = "3";
                getScanCardStatus(currentProgramID, "false");
            }
        });
        btnMonthlyLapSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgramID = "3";
                getScanCardStatus(currentProgramID,"false");
            }
        });
        btnWaterAerobics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgramID = "4";
                getScanCardStatus(currentProgramID, "false");
            }
        });
        btnPhysicalTherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgramID = "5";
                getScanCardStatus(currentProgramID, "false");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentCheckin = new Intent(ActivityCheckin.this, DashBoardActivity.class);
        intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentCheckin);
        finish();
    }

    public class ViewDialog {
        public void showDialog(Activity activity, String message) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_checkin);

            TextView txtBodyText = (TextView) dialog.findViewById(R.id.txtBodyText);
            txtBodyText.setText(message);

            TextView txtClose = (TextView) dialog.findViewById(R.id.txtClose);
            txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getScanCardStatus(currentProgramID, "true");
                }
            });
            Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
    public void addHeader() {
        //Custom Header
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Check In");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnHome = (Button) view.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ActivityCheckin.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
    }
}
