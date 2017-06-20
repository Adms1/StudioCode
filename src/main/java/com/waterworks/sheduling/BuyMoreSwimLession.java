package com.waterworks.sheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BuyMoreSwimLession extends Activity implements Animation.AnimationListener, OnClickListener {
    Button BackButton, btn_sites, btn_viewCart;
    // LinearLayout scdl_lsn,scdl_mkp,waitlist;
    LinearLayout swimLsn, retailStore, otherPrograms, lay_non_lafitness, lay_lafitness, sites_lay, sites_lay_dropdown;
    CardView imageView_private, imageView_semiprivate, imageView1__beginner_intermediate, imageView_advanced,
            imageView_strock_clinic, imageView_adult, imageView_lafitness_private, imageView_lafitness_semi_private,
            imageView_lafitness_adult;
    View selected_1, selected_2, selected_3;
    TextView txt_1, txt_2, txt_3, tv_select_lession_type;
    Context mContext = this;
    String token, familyID, siteId, globalLessinTypeId, globalLessinType;
    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> packageArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> monthlyPlanArray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> lessonArray = new ArrayList<HashMap<String, String>>();
    ArrayList<String> siteName = new ArrayList<String>();
    ListPopupWindow lpw_sitelist;
    View vw_dropdown, viewLineGray;
    ProgressDialog pd;
    Boolean isInternetPresent = false;

    TextView timeLeft_txt, timemsg_txt;
    View viewLine;
    ScaleAnimation animSlide;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 60 * 10 * 1000;
    private final long interval = 1 * 1000;
    String checkFlag = "";
    public boolean checkfromWhere = false;

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more_lessions);
        AppConfiguration.cartBackScreen = 0;
//		BuyMoreSwimLession.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
//		Log.i(" Basketid..@#^", "" + AppConfiguration.Basketid);
        Log.i(" Basketid..@#^", "" + AppConfiguration.BasketID);
        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

//		BuyMoreSwimLession.this.overridePendingTransition(0, 0);
        ((AnimationDrawable) selected_1.getBackground()).start();
        init();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                isInternetPresent = Utility.isNetworkConnected(BuyMoreSwimLession.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    InitialRequests();
                }

            }
        }, 1000);
        typeFace();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

//    23-05-2017 megha
//    @Override
//    public void onResume() {
//        super.onResume();
//        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
//        viewLine.setVisibility(View.VISIBLE);
    }

    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation)
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        /*if (animation == animSlide) {
        }*/
//        viewLine.setVisibility(View.INVISIBLE);
        viewLine.startAnimation(animSlide);

    }

    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationRepeat(android.view.animation.Animation)
     */
    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void InitialRequests() {
        try {
            new SitesListAsyncTask().execute();
            Intent intent = getIntent();
            checkFlag = intent.getStringExtra("value");
            if (checkFlag.equalsIgnoreCase("")) {
                checkfromWhere = false;
//                timeLeft_txt.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                timemsg_txt.setVisibility(View.GONE);
                viewLineGray.setVisibility(View.GONE);
            }
            else {
                checkfromWhere = true;
                timeLeft_txt.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
                timemsg_txt.setVisibility(View.VISIBLE);
                countDownTimer = new BuyMoreSwimLession.MyCountDownTimer(startTime, interval);
                if (!timerHasStarted) {
                    countDownTimer.start();
                    timerHasStarted = true;
                    viewLine.startAnimation(animSlide);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public void init() {
        View view = findViewById(R.id.schdl_top);
        View lessionView = findViewById(R.id.lession_type);
        BackButton = (Button) findViewById(R.id.returnStack);
        btn_viewCart = (Button) findViewById(R.id.btn_viewCart);
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        lay_non_lafitness = (LinearLayout) lessionView.findViewById(R.id.lay_non_lafitness);
        lay_lafitness = (LinearLayout) lessionView.findViewById(R.id.lay_lafitness);
        sites_lay_dropdown = (LinearLayout) findViewById(R.id.sites_lay_dropdown);
        vw_dropdown = (View) findViewById(R.id.vw_dropdown);
        imageView_private = (CardView) lessionView.findViewById(R.id.imageView_private);
        imageView_semiprivate = (CardView) lessionView.findViewById(R.id.imageView_semiprivate);
        imageView1__beginner_intermediate = (CardView) lessionView.findViewById(R.id.imageView1__beginner_intermediate);
        imageView_advanced = (CardView) lessionView.findViewById(R.id.imageView_advanced);
        imageView_strock_clinic = (CardView) lessionView.findViewById(R.id.imageView_strock_clinic);
        imageView_adult = (CardView) lessionView.findViewById(R.id.imageView_adult);
        imageView_lafitness_private = (CardView) lessionView.findViewById(R.id.imageView_lafitness_private);
        imageView_lafitness_semi_private = (CardView) lessionView.findViewById(R.id.imageView_lafitness_semi_private);
        imageView_lafitness_adult = (CardView) lessionView.findViewById(R.id.imageView_lafitness_adult);
        tv_select_lession_type = (TextView) findViewById(R.id.tv_select_lession_type);
        timeLeft_txt = (TextView) findViewById(R.id.tv_timeleft);
        viewLine = (View) findViewById(R.id.viewLine);
        viewLineGray = (View) findViewById(R.id.viewLineGray);
        timemsg_txt = (TextView) findViewById(R.id.timemsg_txt);

        timeLeft_txt.setText("10:00");

        imageView_private.setOnClickListener(this);
        imageView_semiprivate.setOnClickListener(this);
        imageView1__beginner_intermediate.setOnClickListener(this);
        imageView_advanced.setOnClickListener(this);
        imageView_strock_clinic.setOnClickListener(this);
        imageView_adult.setOnClickListener(this);
        imageView_lafitness_private.setOnClickListener(this);
        imageView_lafitness_semi_private.setOnClickListener(this);
        imageView_lafitness_adult.setOnClickListener(this);

        lpw_sitelist = new ListPopupWindow(mContext);
        sites_lay = (LinearLayout) findViewById(R.id.sites_lay);
        btn_sites = (Button) findViewById(R.id.btn_sites);

        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        animSlide = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animSlide.setDuration(1000);
        animSlide.setAnimationListener(this);

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreSwimLession.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuyMoreSwimLession.this, ByMoreMyCart.class);
                startActivity(i);
            }
        });
        btn_sites.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                try {
                    if (siteMainList.size() == 0) {
//						Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                    } else {
                        lpw_sitelist.show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        });
        sites_lay_dropdown.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                if (siteMainList.size() == 0) {
//					Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    lpw_sitelist.show();
                }
            }
        });
        vw_dropdown.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                if (siteMainList.size() == 0) {
//					Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    lpw_sitelist.show();
                }
            }
        });
        swimLsn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(BuyMoreSwimLession.this, BuyMoreSwimLession.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                BuyMoreSwimLession.this.overridePendingTransition(0, 0);
//                finish();

                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });

        retailStore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, BuyMoreRetailStore.class);
                startActivity(i);
                BuyMoreSwimLession.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });

        otherPrograms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, ByMoreOtherPrograms.class);
                startActivity(i);
                BuyMoreSwimLession.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
//                BuyMoreSwimLession .this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            SelectInstructorDialogLAFitness("It looks like you may need more time. Please go to Schedule a Lesson when you are ready to set up a schedule and make payment for lessons.", "yes");
            //			Intent i = new Intent(mContext,
            //					DashBoardActivity.class);
            //			i.putExtra("POS", 5);
            //			startActivity(i);
            //			finish();

            countDownTimer.cancel();
            timerHasStarted = false;
            viewLine.clearAnimation();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(millis)));
            timeLeft_txt.setText(" " + hms);
//            Toast.makeText(mContext, hms, Toast.LENGTH_LONG).show();

        }
    }

    public void SelectInstructorDialogLAFitness(String msg, final String close) {

        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout_lafitness, null);
        final PopupWindow popWindow = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Warning");

        TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
        tv_description.setText(msg);
        tv_description.setTextColor(Color.BLACK);
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "Roboto_Light.ttf");
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        tv_description.setTypeface(face);
        tv_appname.setTypeface(regular);
        TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
        imgv_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                if (close.equalsIgnoreCase("yes")) {
                    BuyMoreSwimLession.this.finish();
                    Intent can = new Intent(BuyMoreSwimLession.this, ScheduleLessonFragement.class);
                    startActivity(can);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        CardView button_ok = (CardView) layout.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                if (close.equalsIgnoreCase("yes")) {
                    BuyMoreSwimLession.this.finish();
                    Intent can = new Intent(BuyMoreSwimLession.this, ScheduleLessonFragement.class);
                    startActivity(can);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
//                    if (popWindow.isShowing() == false) {
//                        Intent can = new Intent(BuyMoreSwimLession.this, ScheduleLessonFragement.class);
//                        startActivity(can);
//                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                    }
                }


            }
        });

    }

    class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            siteMainList.clear();
            siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadSitesList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
            if (siteName.size() == 1) {
                AppConfiguration.salStep1SiteID = siteMainList.get(0).get("SiteID");
                siteId = siteMainList.get(0).get("SiteID");
                sites_lay.setVisibility(View.GONE);
                sites_lay_dropdown.setVisibility(View.GONE);
                if (siteMainList.get(0).get("LafitNess") == "false") {
                    Log.e("Non Lafitens", siteMainList.get(0).get("LafitNess"));
                    lay_non_lafitness.setVisibility(View.VISIBLE);
                    lay_lafitness.setVisibility(View.GONE);
                    tv_select_lession_type.setVisibility(View.VISIBLE);

                } else if (siteMainList.get(0).get("LafitNess") == "true") {
                    Log.e("Lafitens", siteMainList.get(0).get("LafitNess"));
                    lay_lafitness.setVisibility(View.VISIBLE);
                    lay_non_lafitness.setVisibility(View.GONE);
                    tv_select_lession_type.setVisibility(View.VISIBLE);
                }

            } else {
                sites_lay.setVisibility(View.VISIBLE);
                sites_lay_dropdown.setVisibility(View.VISIBLE);
                lpw_sitelist.setAdapter(new ArrayAdapter<String>(mContext, R.layout.edittextpopup, siteName));
                lpw_sitelist.setAnchorView(sites_lay_dropdown);
                lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
                // lpw_sitelist.setWidth(LayoutParams.WRAP_CONTENT);
                lpw_sitelist.setModal(true);
                lpw_sitelist.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        AppConfiguration.salStep1SiteID = siteMainList.get(pos).get("SiteID");
                        siteId = siteMainList.get(pos).get("SiteID");
                        // Log.d("siteId----", siteId);
                        btn_sites.setText(siteMainList.get(pos).get("SiteName"));
                        // Log.d("siteName----",
                        // siteMainList.get(pos).get("SiteName"));
                        if (siteMainList.get(pos).get("LafitNess") == "false") {
                            // Log.e("Non Lafitens",
                            // siteMainList.get(pos).get("LafitNess"));
                            lay_non_lafitness.setVisibility(View.VISIBLE);
                            lay_lafitness.setVisibility(View.GONE);
                            tv_select_lession_type.setVisibility(View.VISIBLE);

                        } else if (siteMainList.get(pos).get("LafitNess") == "true") {
                            // Log.e("Lafitens",
                            // siteMainList.get(pos).get("LafitNess"));
                            lay_lafitness.setVisibility(View.VISIBLE);
                            lay_non_lafitness.setVisibility(View.GONE);
                            tv_select_lession_type.setVisibility(View.VISIBLE);
                        }
                        lpw_sitelist.dismiss();
                    }
                });
            }

        }
    }

    public void loadSitesList() {
        siteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALesssionSiteListURL, param);

        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("SiteList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("siteid"));
                hashmap.put("SiteName", jsonChildNode.getString("sitename"));
                hashmap.put("LafitNess", jsonChildNode.getString("Lafitness"));

                siteName.add("" + jsonChildNode.getString("sitename"));
                siteMainList.add(hashmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lesson Type

    class getLessonAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            packageArray.clear();
            // siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadLessonList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }
    }

    public void loadLessonList() {
        // iteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("Token", token);
        param.put("FamilyID", familyID);
        param.put("SiteID", siteId);
        param.put("LessonTypeID", globalLessinTypeId);
        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.getInstructorPreferences, param);
        Log.i("responseString", responseString);
        readLessonAndParseJSON(responseString);
    }

    public void readLessonAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");

            if (successStr.equalsIgnoreCase("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("Lesson");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("Quantity", jsonChildNode.getString("lessonid"));
                    hashmap.put("PackageID", jsonChildNode.getString("lessonname"));

                    lessonArray.add(hashmap);
                    AppConfiguration.lessonArray = lessonArray;
                }
            } else if (successStr.equalsIgnoreCase("False")) {
                Log.d("False array--**", successStr);

                Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        isInternetPresent = Utility.isNetworkConnected(BuyMoreSwimLession.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            if (v == imageView_private) {
                globalLessinType = "Private Lessons";
                globalLessinTypeId = "1";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
            if (v == imageView_semiprivate) {
                globalLessinType = "Semi Private Lessons";
                globalLessinTypeId = "2";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (v == imageView1__beginner_intermediate) {
                globalLessinType = "Beginner/Intermediate Lessons";
                globalLessinTypeId = "9";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (v == imageView_advanced) {
                globalLessinType = "Advanced Lessons";
                globalLessinTypeId = "6";//previously 10
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (v == imageView_strock_clinic) {
                globalLessinType = "Stroke Clinics Lessons";
                globalLessinTypeId = "5";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (v == imageView_adult) {
                globalLessinType = "Adult Lessons";
                globalLessinTypeId = "4";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (v == imageView_lafitness_private) {
                globalLessinType = "Private Lessons";
                globalLessinTypeId = "1";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (v == imageView_lafitness_semi_private) {
                globalLessinTypeId = "2";
                globalLessinType = "Semi Private Lessons";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (v == imageView_lafitness_adult) {
                globalLessinType = "Adult Lessons";
                globalLessinTypeId = "4";
                try {
                    new PackageAsyncTask().execute();
//				new MonthlyPlanAsyncTask().execute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
    }

    // get package
    class PackageAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            packageArray.clear();
            // siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadPackageList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            isInternetPresent = Utility.isNetworkConnected(BuyMoreSwimLession.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new MonthlyPlanAsyncTask().execute();
            }
        }
    }

    public void loadPackageList() {

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("Token", token);
        param.put("FamilyID", familyID);
        param.put("SiteID", siteId);
        param.put("LessonTypeID", globalLessinTypeId);
        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.makePurchasePkgListLessonWise, param);
        Log.i("responseString", responseString);
        readPackageAndParseJSON(responseString);
    }

    public void readPackageAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");

            if (successStr.equalsIgnoreCase("True")) {
                Log.d("True array---**", successStr);
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("Quantity", jsonChildNode.getString("Quantity"));
                    hashmap.put("PackageID", jsonChildNode.getString("PackageID"));
                    hashmap.put("Price", jsonChildNode.getString("Price"));
                    hashmap.put("OrgPrice", jsonChildNode.getString("OrgPrice"));
                    hashmap.put("Saving", jsonChildNode.getString("Saving"));
                    hashmap.put("Popular", jsonChildNode.getString("Popular"));

                    packageArray.add(hashmap);
                    AppConfiguration.finalArrayPackage = packageArray;
                    Log.d("AppConfiguration.finalArrayPackage--", "" + AppConfiguration.finalArrayPackage);
                }
            } else if (successStr.equalsIgnoreCase("False")) {

                Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get Monthly Plan

    class MonthlyPlanAsyncTask extends AsyncTask<Void, Void, Void> {
//		ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//			pd = new ProgressDialog(mContext);
//			pd.setMessage("Please wait...");
//			pd.setCancelable(false);
//			pd.show();

            monthlyPlanArray.clear();

        }

        @Override
        protected Void doInBackground(Void... params) {
            loadMonthlyPlanList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {

                try {
                    pd.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
            if (packageArray.size() == 0 && monthlyPlanArray.size() == 0) {
                Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();

            } else {
                AppConfiguration.siteID = siteId;
                AppConfiguration.LessinTypeId = globalLessinTypeId;
                Intent i = new Intent(getApplicationContext(), BuyMoreSwimLession2.class);
                i.putExtra("LessionType", globalLessinType);
                i.putExtra("SiteID", siteId);
                startActivity(i);
                BuyMoreSwimLession.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }

        }
    }

    public void loadMonthlyPlanList() {
        // iteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("Token", token);
        param.put("FamilyID", familyID);
        param.put("SiteID", siteId);
        param.put("LessonTypeID", globalLessinTypeId);
        String responseString = WebServicesCall
                .RunScript(AppConfiguration.DOMAIN + AppConfiguration.monthlyPackageList_LessonWise, param);
        Log.i("responseString", responseString);
        readMonthlyPlanAndParseJSON(responseString);
    }

    public void readMonthlyPlanAndParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");

            if (successStr.equalsIgnoreCase("True")) {
                Log.d("True array---**", successStr);
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    // Log.i("jsonMainNode.length()---", "" +
                    // jsonMainNode.length());
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("Quantity", jsonChildNode.getString("Quantity"));
                    hashmap.put("PackageID", jsonChildNode.getString("PackageID"));
                    hashmap.put("Price", jsonChildNode.getString("Price"));
                    hashmap.put("OrgPrice", jsonChildNode.getString("OrgPrice"));
                    hashmap.put("Saving", jsonChildNode.getString("Saving"));

                    monthlyPlanArray.add(hashmap);
                    AppConfiguration.finalArrayMonthlyPlan = monthlyPlanArray;
                }
            } else if (successStr.equalsIgnoreCase("False")) {
                Log.d("False array--**", successStr);

                Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        BuyMoreSwimLession.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}