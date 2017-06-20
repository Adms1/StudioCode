/**
 *
 */
package com.waterworks.sheduling;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.waterworks.CancelLessonFragment;
import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Harsh Adms
 */
public class ScheduleLessonFragement6 extends Activity {
    View rootView;
    String token, familyID;

    //Student tab Layout
    LinearLayout st_1, st_2, st_3, st_4, st_5, main_lay, days_st_2, days_st_3, days_st_4, days_st_5, button_lay;
    RelativeLayout days_st_1;
    TextView name_1, name_2, name_3, name_4, name_5;
    View select_1, select_2, select_3, select_4, select_5;

    //Next Button
    Button btn_next;//, import_calender, purchase_lsn, view_my_schedule, main_menu;
    CardView import_calender, purchase_lsn, view_my_schedule, main_menu;
    ListView lv_schedule_confrimation;
    String list1 = "", list2 = "", list3 = "", list4 = "", list5 = "",
            SelectStudList = "", getdata = "false";


    Context mContext = this;
  String LaFitness ="";
    View selected_1, selected_2, selected_3;
    LinearLayout scdl_lsn, scdl_mkp, waitlist;
    TextView txt_1, txt_2, txt_3, noti_count;

    //ThankYou Layout
    TextView tv_thankyou, success, email_short;
    Animation animationFadeIn, animationSlideUp;

    private static final int PERMISSIONS_REQUEST_WRITE_CALENDAR = 100;
    Boolean isInternetPresent = false;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_schedule_lesson6);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        ((AnimationDrawable) selected_1.getBackground()).start();

        if (AppConfiguration.makeup_Clicked == 1) {
            selected_1.setVisibility(View.GONE);
            selected_2.setVisibility(View.VISIBLE);
            selected_3.setVisibility(View.GONE);
            ((AnimationDrawable) selected_2.getBackground()).start();
        }

        animationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        animationSlideUp = AnimationUtils.loadAnimation(mContext, R.anim.slidup_slow);

        init();
        isInternetPresent = Utility.isNetworkConnected(ScheduleLessonFragement6.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {

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

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);

    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    //typeFace
    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        success.setTypeface(regular);
        tv_thankyou.setTypeface(regular);
        email_short.setTypeface(regular);
        btn_next.setTypeface(regular);
//        import_calender.setTypeface(regular);
//        view_my_schedule.setTypeface(regular);
//        main_menu.setTypeface(regular);
//        purchase_lsn.setTypeface(regular);

    }

    @SuppressWarnings("deprecation")
    public void init() {

        View view = findViewById(R.id.schdl_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);
        RippleView ripple = (RippleView) view.findViewById(R.id.ripple);
        ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                onBackPressed();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ScheduleLessonFragement6.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
//		BackButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(mContext,DashBoardActivity.class);
//				startActivity(i);
//				finish();
//			}
//		});

        success = (TextView) findViewById(R.id.success);
        tv_thankyou = (TextView) findViewById(R.id.tv_thankyou);
        button_lay = (LinearLayout) findViewById(R.id.button_lay);

        email_short = (TextView) findViewById(R.id.email_short);

//        button_lay.startAnimation(animationSlideUp);
        tv_thankyou.startAnimation(animationFadeIn);
        success.startAnimation(animationSlideUp);
        email_short.startAnimation(animationSlideUp);

        scdl_lsn = (LinearLayout) view.findViewById(R.id.scdl_lsn);
        scdl_mkp = (LinearLayout) view.findViewById(R.id.scdl_mkp);
        waitlist = (LinearLayout) view.findViewById(R.id.waitlist);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        noti_count = (TextView) findViewById(R.id.noti_count);
        if (!AppConfiguration.Mup_cnt.equals("0")) {
            //			Rakesh  20112015............
            noti_count.setVisibility(View.VISIBLE);
            noti_count.setText(AppConfiguration.Mup_cnt);
        } else {
            noti_count.setVisibility(View.GONE);
        }
        scdl_lsn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppConfiguration.ErrorPopup(mContext, ScheduleLessonFragement.class)) {
                    scdl_lsn.setBackgroundResource(R.color.design_change_d2);
                    scdl_mkp.setBackgroundResource(R.color.design_change_d2);
                    waitlist.setBackgroundResource(R.color.design_change_d2);

                    selected_1.setVisibility(View.VISIBLE);
                    selected_2.setVisibility(View.GONE);
                    selected_3.setVisibility(View.GONE);

                    txt_1.setTextColor(Color.WHITE);
                    txt_2.setTextColor(Color.WHITE);
                    txt_3.setTextColor(Color.WHITE);

                    Intent i = new Intent(mContext, ScheduleLessonFragement.class);
                    startActivity(i);
                    finish();
                    ((AnimationDrawable) selected_1.getBackground()).start();
                }
            }
        });
        scdl_mkp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppConfiguration.ErrorPopup(mContext, ScheduleMakeupFragment.class)) {
                    scdl_lsn.setBackgroundResource(R.color.design_change_d2);
                    scdl_mkp.setBackgroundResource(R.color.design_change_d2);
                    waitlist.setBackgroundResource(R.color.design_change_d2);

                    selected_1.setVisibility(View.GONE);
                    selected_2.setVisibility(View.VISIBLE);
                    selected_3.setVisibility(View.GONE);

                    txt_1.setTextColor(Color.WHITE);
                    txt_2.setTextColor(Color.WHITE);
                    txt_3.setTextColor(Color.WHITE);

                    Intent i = new Intent(mContext, ScheduleMakeupFragment.class);
                    startActivity(i);
                    finish();

                    ((AnimationDrawable) selected_2.getBackground()).start();
                }
            }
        });
        waitlist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppConfiguration.ErrorPopup(mContext, WaitListFragment.class)) {
                    scdl_lsn.setBackgroundResource(R.color.design_change_d2);
                    scdl_mkp.setBackgroundResource(R.color.design_change_d2);
                    waitlist.setBackgroundResource(R.color.design_change_d2);

                    selected_1.setVisibility(View.GONE);
                    selected_2.setVisibility(View.GONE);
                    selected_3.setVisibility(View.VISIBLE);

                    txt_1.setTextColor(Color.WHITE);
                    txt_2.setTextColor(Color.WHITE);
                    txt_3.setTextColor(Color.WHITE);

                    Intent i = new Intent(mContext, WaitListFragment.class);
                    startActivity(i);
                    finish();

                    ((AnimationDrawable) selected_3.getBackground()).start();
                }
            }
        });

        list1 = AppConfiguration.list1;
        list2 = AppConfiguration.list2;
        list3 = AppConfiguration.list3;
        list4 = AppConfiguration.list4;
        list5 = AppConfiguration.list5;


        String[] studentid = AppConfiguration.selectedStudentID.toString()
                .split(",");
        String[] studentname = AppConfiguration.selectedStudentsName.toString()
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .toString().split(",");

        System.out.println("Student Names");
        for (int i = 0; i < studentname.length; i++) {
            if (i == 0) {
                SelectStudList = studentid[i].toString().trim() + "|"
                        + studentname[i].toString().trim();
            } else {
                SelectStudList = SelectStudList + ","
                        + studentid[i].toString().trim() + "|"
                        + studentname[i].toString().trim();
            }
        }


        btn_next = (Button) findViewById(R.id.btn_next);
        import_calender = (CardView) findViewById(R.id.import_calender);
        purchase_lsn = (CardView) findViewById(R.id.purchase_lsn);
        main_menu = (CardView) findViewById(R.id.main_menu);
        view_my_schedule = (CardView) findViewById(R.id.view_my_schedule);

        import_calender.startAnimation(animationFadeIn);
        purchase_lsn.startAnimation(animationFadeIn);
        main_menu.startAnimation(animationFadeIn);
        view_my_schedule.startAnimation(animationFadeIn);

        main_lay = (LinearLayout) findViewById(R.id.main_lay);

        st_1 = (LinearLayout) findViewById(R.id.st_1);
        st_2 = (LinearLayout) findViewById(R.id.st_2);
        st_3 = (LinearLayout) findViewById(R.id.st_3);
        st_4 = (LinearLayout) findViewById(R.id.st_4);
        st_5 = (LinearLayout) findViewById(R.id.st_5);

        days_st_1 = (RelativeLayout) findViewById(R.id.days_st_1);
        days_st_2 = (LinearLayout) findViewById(R.id.days_st_2);
        days_st_3 = (LinearLayout) findViewById(R.id.days_st_3);
        days_st_4 = (LinearLayout) findViewById(R.id.days_st_4);
        days_st_5 = (LinearLayout) findViewById(R.id.days_st_5);

        select_1 = (View) findViewById(R.id.select_1);
        select_2 = (View) findViewById(R.id.select_2);
        select_3 = (View) findViewById(R.id.select_3);
        select_4 = (View) findViewById(R.id.select_4);
        select_5 = (View) findViewById(R.id.select_5);

        name_1 = (TextView) findViewById(R.id.name_1);
        name_2 = (TextView) findViewById(R.id.name_2);
        name_3 = (TextView) findViewById(R.id.name_3);
        name_4 = (TextView) findViewById(R.id.name_4);
        name_5 = (TextView) findViewById(R.id.name_5);


        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        st_1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                days_st_1.setVisibility(View.VISIBLE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);

                select_1.setVisibility(View.VISIBLE);
                select_2.setVisibility(View.GONE);
                select_3.setVisibility(View.GONE);
                select_4.setVisibility(View.GONE);
                select_5.setVisibility(View.GONE);

                name_1.setTextColor(getResources().getColor(R.color.orange));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

            }
        });

        st_2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.VISIBLE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);

                select_1.setVisibility(View.GONE);
                select_2.setVisibility(View.VISIBLE);
                select_3.setVisibility(View.GONE);
                select_4.setVisibility(View.GONE);
                select_5.setVisibility(View.GONE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.orange));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));
            }
        });

        st_3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.VISIBLE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);

                select_1.setVisibility(View.GONE);
                select_2.setVisibility(View.GONE);
                select_3.setVisibility(View.VISIBLE);
                select_4.setVisibility(View.GONE);
                select_5.setVisibility(View.GONE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.orange));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));
            }
        });

        st_4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.VISIBLE);
                days_st_5.setVisibility(View.GONE);

                select_1.setVisibility(View.GONE);
                select_2.setVisibility(View.GONE);
                select_3.setVisibility(View.GONE);
                select_4.setVisibility(View.VISIBLE);
                select_5.setVisibility(View.GONE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.orange));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));
            }
        });

        st_5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.VISIBLE);

                select_1.setVisibility(View.GONE);
                select_2.setVisibility(View.GONE);
                select_3.setVisibility(View.GONE);
                select_4.setVisibility(View.GONE);
                select_5.setVisibility(View.VISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.orange));
            }
        });

        import_calender.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the SDK version and whether the permission is already granted or not.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        ContextCompat.checkSelfPermission(ScheduleLessonFragement6.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSIONS_REQUEST_WRITE_CALENDAR);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    // Android version is lesser than 6.0 or the permission is already granted.
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(ScheduleLessonFragement6.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    importToCalendar();
                    progressDialog.dismiss();
                }
            }
        });

        purchase_lsn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMULIntent = new Intent(mContext, BuyMoreSwimLession.class);
                startActivity(seeMULIntent);
                finish();
            }
        });

        view_my_schedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConfiguration.custom_flow = true;
//                AppConfiguration.transform = 7;
                //Intent i = new Intent(mContext, DashBoardActivity.class);
                Intent i = new Intent(mContext, CancelLessonFragment.class);
                startActivity(i);
                finish();
            }
        });

        main_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConfiguration.custom_flow = true;
//                AppConfiguration.transform = 0;
                Intent i = new Intent(mContext, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        typeFace();
        //		distributeStudentsTop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_CALENDAR) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                importToCalendar();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot add events in calendar", Toast.LENGTH_SHORT).show();
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
                    //					name_5.setText(temp[0]+"\n"+temp[1]);
                    name_5.setText(temp[0]);
                } else {
                    name_5.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            }
        }
        main_lay.setVisibility(View.VISIBLE);
    }

    public ArrayList<String> eventids = new ArrayList<>();

    public void importToCalendar() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ScheduleLessonFragement6.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        long startMillis = 0, endMillis = 0;
        String generate_title = "";
        eventids.clear();
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        for (int i = 0; i < ScheduleLessonFragement5.Calendar_Event.size(); i++) {

            if (ScheduleLessonFragement5.Calendar_Event.get(i).get("date").contains("/")) {
                String dates[] = ScheduleLessonFragement5.Calendar_Event.get(i).get("date").split("/");
                String timetemp[] = ScheduleLessonFragement5.Calendar_Event.get(i).get("time").split("@");
                String timing[] = new String[2];
                try {
                    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
                    Date date = parseFormat.parse(timetemp[1]);
                    System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
                    timing = displayFormat.format(date).split(":");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                beginTime.set(Integer.parseInt(dates[2]), Integer.parseInt(dates[0]) - 1, Integer.parseInt(dates[1]),
                        Integer.parseInt(timing[0]), Integer.parseInt(timing[1]), 30);
                endTime.set(Integer.parseInt(dates[2]), Integer.parseInt(dates[0]) - 1, Integer.parseInt(dates[1]),
                        Integer.parseInt(timing[0]), Integer.parseInt(timing[1]));

                generate_title = String.valueOf(ScheduleLessonFragement5.Calendar_Event.get(i).get("StudentName") + " " +
                        ScheduleLessonFragement5.Calendar_Event.get(i).get("time") + " - " + " Waterworks");
                Log.d("generate_title", generate_title);
            } else {
                beginTime.set(ScheduleLessonFragement.c_yr, ScheduleLessonFragement.c_month, ScheduleLessonFragement.c_day,
                        ScheduleLessonFragement.c_hour, ScheduleLessonFragement.c_min);
            }


            startMillis = beginTime.getTimeInMillis();
            endMillis = endTime.getTimeInMillis() + (20 * 60 * 1000);

            System.out.println(ScheduleLessonFragement5.Calendar_Event.get(i).get("date") + " - " + ScheduleLessonFragement5.Calendar_Event.get(i).get("time"));
//            Uri eventsUri = ;
            ContentValues event = new ContentValues();
            /*event.put("calendar_id", 1);
            event.put("title", generate_title);
            event.put("description", "Swim Lesson in " + ScheduleLessonFragement5.Calendar_Event.get(i).get("sitename"));
            event.put("eventLocation", ScheduleLessonFragement5.Calendar_Event.get(i).get("sitename"));
            event.put("dtstart", startMillis);
            event.put("dtend", endMillis);
            event.put("allDay", 0);   // 0 for false, 1 for true
            event.put("eventStatus", 1);
            event.put("hasAlarm", 1); // 0 for false, 1 for true
//            event.put("eventTimezone", TimeZone.getDefault().toString());
            event.put("eventTimezone", "GMT");//TimeZone.getTimeZone("GMT+0").toString()*/

            event.put(CalendarContract.Events.CALENDAR_ID, 1);
            event.put(CalendarContract.Events.TITLE, generate_title);
            event.put(CalendarContract.Events.DESCRIPTION, "Swim Lesson in " + ScheduleLessonFragement5.Calendar_Event.get(i).get("sitename"));
//            event.put(CalendarContract.Events.EVENT_LOCATION, ScheduleLessonFragement5.Calendar_Event.get(i).get("sitename"));
            event.put(CalendarContract.Events.EVENT_LOCATION, "India");
            event.put(CalendarContract.Events.DTSTART, startMillis);
            event.put(CalendarContract.Events.DTEND, endMillis);
            event.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
            event.put(CalendarContract.Events.STATUS, 1);
            event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true
//            event.put("eventTimezone", TimeZone.getDefault().toString());
            TimeZone tz = TimeZone.getDefault();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getDisplayName(false, TimeZone.SHORT).toString());//TimeZone.getTimeZone("GMT+0").toString()
            Log.v("Current TimeZone", tz.getDisplayName(false, TimeZone.SHORT).toString());


            try {
//                Uri eventUri = getApplicationContext().getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), event);
//                eventids.add(eventUri.getLastPathSegment());
//                content://com.android.calendar/events


                Intent intent = new Intent(Intent.ACTION_VIEW, getApplicationContext().getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), event));
//                intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(eventids.get(0))));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ContentValues reminders = new ContentValues();
                Uri eventUri = getApplicationContext().getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), event);

                reminders.put(CalendarContract.Reminders.EVENT_ID, eventUri.getLastPathSegment());
                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(CalendarContract.Reminders.MINUTES, 30);

                Uri uri2 = getApplicationContext().getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminders);
                progressDialog.dismiss();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Your Phone not support calender", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        System.out.println("" + eventids);
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(eventids.get(0))));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
//        Utility.ping(this, "Events added successfully in Calendar.");
//        Intent i = new Intent(mContext, CancelLessonFragment.class);
//        startActivity(i);
//        finish();
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    String convertDate(String inputDate) {

        DateFormat theDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch (Exception exception) {
            // Generic catch. Do what you want.
        }
        theDateFormat = new SimpleDateFormat("EEE, MMM yyyy");
        return theDateFormat.format(date);
    }


    String convertendDate(String inputDate) {

        DateFormat theDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch (Exception exception) {
            // Generic catch. Do what you want.
        }
        theDateFormat = new SimpleDateFormat("yyyyMMdd");
        return theDateFormat.format(date);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(mContext, DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
