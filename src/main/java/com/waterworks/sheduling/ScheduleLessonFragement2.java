/**
 *
 */
package com.waterworks.sheduling;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.waterworks.DashBoardActivity;
import com.waterworks.LoginActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Harsh Adms
 */
public class ScheduleLessonFragement2 extends Activity {

    View rootView;
    String token, familyID;
    String successLoadChildList;
    int selectedViewId;
    // Filteration
    RadioGroup inst_types_grp, inst_types_grp_2, inst_types_grp_3, inst_types_grp_4, inst_types_grp_5;

    CheckBox male, female, male_2, female_2, male_3, female_3, male_4, female_4, male_5, female_5;

    RadioButton rbchk_1, rbchk_2, rbchk_3, rbchk_4,
            rbchk_1_2, rbchk_2_2, rbchk_3_2, rbchk_4_2,
            rbchk_1_3, rbchk_2_3, rbchk_3_3, rbchk_4_3,
            rbchk_1_4, rbchk_2_4, rbchk_3_4, rbchk_4_4,
            rbchk_1_5, rbchk_2_5, rbchk_3_5, rbchk_4_5;

    // Next Button
//    Button btn_next;
    CardView btn_next_card;
    // Instructor List
    LinearLayout inst_inflate;
    ImageLoader imageLoader;
    Boolean isInternetPresent = false;
    // Student tab Layout
    LinearLayout st_1, st_2, st_3, st_4, st_5, days_st_1, days_st_2, days_st_3, days_st_4, days_st_5, filter_1,
            filter_2, filter_3, filter_4, filter_5, spec_search_lay, spec_search_lay_2, spec_search_lay_3,
            spec_search_lay_4, spec_search_lay_5, lay_1, lay_2, lay_3, lay_4, lay_5, days1, days2, filter, lay1, lay2;
    TextView name_1, name_2, name_3, name_4, name_5;
    View select_1, select_2, select_3, select_4, select_5;

    // SelectedInstructors
    ArrayList<String> builder, builder1, builder2, builder3, builder4, combo1, combo2;

    public static ArrayList<HashMap<String, String>> schedulePageLoadList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> limits = new ArrayList<HashMap<String, String>>();
    String successGetStudentList, fromWhere;

    Context mContext = this;

    View selected_1, selected_2, selected_3, select1, select2;
    LinearLayout scdl_lsn, scdl_mkp, waitlist;
    TextView txt_1, txt_2, txt_3, noti_count;
    ScrollView scrollview;
    int last_clicked_position = 1;
    View view_1, view_2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_schedule_lesson2);

        isInternetPresent = Utility.isNetworkConnected(ScheduleLessonFragement2.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            System.out.println("Pair 1 : " + AppConfiguration.pair1lessontype);
            System.out.println("Pair 2 : " + AppConfiguration.pair2lessontype);
            // getting token
            SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
            token = prefs.getString("Token", "");
            familyID = prefs.getString("FamilyID", "");

            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));

            selected_1 = (View) findViewById(R.id.selected_1);
            selected_2 = (View) findViewById(R.id.selected_2);
            selected_3 = (View) findViewById(R.id.selected_3);

            scrollview = (ScrollView) findViewById(R.id.scrollview);
            scrollview.setSmoothScrollingEnabled(true);
            ((AnimationDrawable) selected_1.getBackground()).start();

            if (AppConfiguration.makeup_Clicked == 1) {
                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.GONE);
                ((AnimationDrawable) selected_2.getBackground()).start();
            }

            init();

            fromWhere = getIntent().getStringExtra("fromWhere");
            if (fromWhere != null) {
                if (AppConfiguration.addCheckedOptions1 > 0) {
                    if (AppConfiguration.addCheckedOptions1 == 1) {
//                    rbchk_1.setChecked(true);
                        inst_types_grp.check(R.id.rbchk_1);
                    } else if (AppConfiguration.addCheckedOptions1 == 2) {
//                    rbchk_2.setChecked(true);
                        inst_types_grp.check(R.id.rbchk_2);
                    } else if (AppConfiguration.addCheckedOptions1 == 3) {
//                    rbchk_3.setChecked(true);
                        inst_types_grp.check(R.id.rbchk_3);
                    } else if (AppConfiguration.addCheckedOptions1 == 4) {
//                    rbchk_4.setChecked(true);
                        inst_types_grp.check(R.id.rbchk_4);
                    }
                }
                if (AppConfiguration.addCheckedOptions2 > 0) {
                    if (AppConfiguration.addCheckedOptions2 == 1) {
//                    rbchk_1_2.setChecked(true);
                        inst_types_grp_2.check(R.id.rbchk_1_2);
                    } else if (AppConfiguration.addCheckedOptions2 == 2) {
//                    rbchk_2_2.setChecked(true);
                        inst_types_grp_2.check(R.id.rbchk_2_2);
                    } else if (AppConfiguration.addCheckedOptions2 == 3) {
//                    rbchk_3_2.setChecked(true);
                        inst_types_grp_2.check(R.id.rbchk_3_2);
                    } else if (AppConfiguration.addCheckedOptions2 == 4) {
//                    rbchk_4_2.setChecked(true);
                        inst_types_grp_2.check(R.id.rbchk_4_2);
                    }
                }
                if (AppConfiguration.addCheckedOptions3 > 0) {
                    if (AppConfiguration.addCheckedOptions3 == 1) {
//                    rbchk_1_3.setChecked(true);
                        inst_types_grp_3.check(R.id.rbchk_1_3);
                    } else if (AppConfiguration.addCheckedOptions3 == 2) {
//                    rbchk_2_3.setChecked(true);
                        inst_types_grp_3.check(R.id.rbchk_2_3);
                    } else if (AppConfiguration.addCheckedOptions3 == 3) {
//                    rbchk_3_3.setChecked(true);
                        inst_types_grp_3.check(R.id.rbchk_3_3);
                    } else if (AppConfiguration.addCheckedOptions3 == 4) {
//                    rbchk_4_3.setChecked(true);
                        inst_types_grp_3.check(R.id.rbchk_4_3);
                    }
                }
                if (AppConfiguration.addCheckedOptions4 > 0) {
                    if (AppConfiguration.addCheckedOptions4 == 1) {
//                    rbchk_1_4.setChecked(true);
                        inst_types_grp_4.check(R.id.rbchk_1_4);
                    } else if (AppConfiguration.addCheckedOptions4 == 2) {
//                    rbchk_2_4.setChecked(true);
                        inst_types_grp_4.check(R.id.rbchk_2_4);
                    } else if (AppConfiguration.addCheckedOptions4 == 3) {
//                    rbchk_3_4.setChecked(true);
                        inst_types_grp_4.check(R.id.rbchk_3_4);
                    } else if (AppConfiguration.addCheckedOptions4 == 4) {
//                    rbchk_4_4.setChecked(true);
                        inst_types_grp_4.check(R.id.rbchk_4_4);
                    }
                }
                if (AppConfiguration.addCheckedOptions5 > 0) {
                    if (AppConfiguration.addCheckedOptions5 == 1) {
//                    rbchk_1_5.setChecked(true);
                        inst_types_grp_5.check(R.id.rbchk_1_5);
                    } else if (AppConfiguration.addCheckedOptions5 == 2) {
//                    rbchk_2_5.setChecked(true);
                        inst_types_grp_5.check(R.id.rbchk_2_5);
                    } else if (AppConfiguration.addCheckedOptions5 == 3) {
//                    rbchk_3_5.setChecked(true);
                        inst_types_grp_5.check(R.id.rbchk_3_5);
                    } else if (AppConfiguration.addCheckedOptions5 == 4) {
//                    rbchk_4_5.setChecked(true);
                        inst_types_grp_5.check(R.id.rbchk_4_5);
                    }
                }
            } else {
                AppConfiguration.addCheckedOptions1 = 0;
                AppConfiguration.addCheckedOptions2 = 0;
                AppConfiguration.addCheckedOptions3 = 0;
                AppConfiguration.addCheckedOptions4 = 0;
                AppConfiguration.addCheckedOptions5 = 0;
            }

            InitialRequests();
            typeFace();
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);

    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void InitialRequests() {
        new LoadInstructorListAsyncTask().execute();
        new SchedulePageLoad().execute();
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
    public void init() {
        builder = new ArrayList<String>();
        builder1 = new ArrayList<String>();
        builder2 = new ArrayList<String>();
        builder3 = new ArrayList<String>();
        builder4 = new ArrayList<String>();

        combo1 = new ArrayList<String>();
        combo2 = new ArrayList<String>();

        AppConfiguration.SelectedInstName = new ArrayList<String>();

        View view = findViewById(R.id.schdl_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);
        RippleView ripple = (RippleView) view.findViewById(R.id.ripple);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ScheduleLessonFragement2.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                onBackPressed();
            }
        });

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

        btn_next_card = (CardView) findViewById(R.id.btn_next_card);
        spec_search_lay = (LinearLayout) findViewById(R.id.spec_search_lay);
        spec_search_lay_2 = (LinearLayout) findViewById(R.id.spec_search_lay_2);
        spec_search_lay_3 = (LinearLayout) findViewById(R.id.spec_search_lay_3);
        spec_search_lay_4 = (LinearLayout) findViewById(R.id.spec_search_lay_4);
        spec_search_lay_5 = (LinearLayout) findViewById(R.id.spec_search_lay_5);

        inst_types_grp = (RadioGroup) findViewById(R.id.inst_types_grp);
        male = (CheckBox) findViewById(R.id.male);
        female = (CheckBox) findViewById(R.id.female);
        rbchk_4 = (RadioButton) findViewById(R.id.rbchk_4);

        lay_1 = (LinearLayout) findViewById(R.id.lay_1);
        lay_2 = (LinearLayout) findViewById(R.id.lay_2);
        lay_3 = (LinearLayout) findViewById(R.id.lay_3);
        lay_4 = (LinearLayout) findViewById(R.id.lay_4);
        lay_5 = (LinearLayout) findViewById(R.id.lay_5);

        CommonMethods(inst_types_grp, male, female, rbchk_4, spec_search_lay);

        inst_types_grp_2 = (RadioGroup) findViewById(R.id.inst_types_grp_2);
        male_2 = (CheckBox) findViewById(R.id.male_2);
        female_2 = (CheckBox) findViewById(R.id.female_2);
        rbchk_4_2 = (RadioButton) findViewById(R.id.rbchk_4_2);

        CommonMethods(inst_types_grp_2, male_2, female_2, rbchk_4_2, spec_search_lay_2);

        inst_types_grp_3 = (RadioGroup) findViewById(R.id.inst_types_grp_3);
        male_3 = (CheckBox) findViewById(R.id.male_3);
        female_3 = (CheckBox) findViewById(R.id.female_3);
        rbchk_4_3 = (RadioButton) findViewById(R.id.rbchk_4_3);

        CommonMethods(inst_types_grp_3, male_3, female_3, rbchk_4_3, spec_search_lay_3);

        inst_types_grp_4 = (RadioGroup) findViewById(R.id.inst_types_grp_4);
        male_4 = (CheckBox) findViewById(R.id.male_4);
        female_4 = (CheckBox) findViewById(R.id.female_4);
        rbchk_4_4 = (RadioButton) findViewById(R.id.rbchk_4_4);

        CommonMethods(inst_types_grp_4, male_4, female_4, rbchk_4_4, spec_search_lay_4);

        inst_types_grp_5 = (RadioGroup) findViewById(R.id.inst_types_grp_5);
        male_5 = (CheckBox) findViewById(R.id.male_5);
        female_5 = (CheckBox) findViewById(R.id.female_5);
        rbchk_4_5 = (RadioButton) findViewById(R.id.rbchk_4_5);

        CommonMethods(inst_types_grp_5, male_5, female_5, rbchk_4_5, spec_search_lay_5);

        rbchk_1 = (RadioButton) findViewById(R.id.rbchk_1);
        rbchk_2 = (RadioButton) findViewById(R.id.rbchk_2);
        rbchk_3 = (RadioButton) findViewById(R.id.rbchk_3);
        rbchk_1_2 = (RadioButton) findViewById(R.id.rbchk_1_2);
        rbchk_2_2 = (RadioButton) findViewById(R.id.rbchk_2_2);
        rbchk_3_2 = (RadioButton) findViewById(R.id.rbchk_3_2);
        rbchk_1_3 = (RadioButton) findViewById(R.id.rbchk_1_3);
        rbchk_2_3 = (RadioButton) findViewById(R.id.rbchk_2_3);
        rbchk_3_3 = (RadioButton) findViewById(R.id.rbchk_3_3);
        rbchk_1_4 = (RadioButton) findViewById(R.id.rbchk_1_4);
        rbchk_2_4 = (RadioButton) findViewById(R.id.rbchk_2_4);
        rbchk_3_4 = (RadioButton) findViewById(R.id.rbchk_3_4);
        rbchk_1_5 = (RadioButton) findViewById(R.id.rbchk_1_5);
        rbchk_2_5 = (RadioButton) findViewById(R.id.rbchk_2_5);
        rbchk_3_5 = (RadioButton) findViewById(R.id.rbchk_3_5);

        // Studetnts Tab Lay
        st_1 = (LinearLayout) findViewById(R.id.st_1);
        st_2 = (LinearLayout) findViewById(R.id.st_2);
        st_3 = (LinearLayout) findViewById(R.id.st_3);
        st_4 = (LinearLayout) findViewById(R.id.st_4);
        st_5 = (LinearLayout) findViewById(R.id.st_5);

        filter_1 = (LinearLayout) findViewById(R.id.filter_1);
        filter_2 = (LinearLayout) findViewById(R.id.filter_2);
        filter_3 = (LinearLayout) findViewById(R.id.filter_3);
        filter_4 = (LinearLayout) findViewById(R.id.filter_4);
        filter_5 = (LinearLayout) findViewById(R.id.filter_5);

        days_st_1 = (LinearLayout) findViewById(R.id.inst_inflate_1);
        days_st_2 = (LinearLayout) findViewById(R.id.inst_inflate_2);
        days_st_3 = (LinearLayout) findViewById(R.id.inst_inflate_3);
        days_st_4 = (LinearLayout) findViewById(R.id.inst_inflate_4);
        days_st_5 = (LinearLayout) findViewById(R.id.inst_inflate_5);

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

        select_1.setTag(1);
        select_2.setTag(2);
        select_3.setTag(3);
        select_4.setTag(4);
        select_5.setTag(5);

        st_1.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (rbchk_4.isChecked()) {
                    days_st_1.setVisibility(View.VISIBLE);
                    days_st_2.setVisibility(View.GONE);
                    days_st_3.setVisibility(View.GONE);
                    days_st_4.setVisibility(View.GONE);
                    days_st_5.setVisibility(View.GONE);
                }
                lay_1.setVisibility(View.VISIBLE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.VISIBLE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.GONE);

                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.orange));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

                decide_layout(last_view(), select_1);
                animation_slide(filter_1, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 1;
            }
        });

        st_2.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (rbchk_4_2.isChecked()) {
                    days_st_1.setVisibility(View.GONE);
                    days_st_2.setVisibility(View.VISIBLE);
                    days_st_3.setVisibility(View.GONE);
                    days_st_4.setVisibility(View.GONE);
                    days_st_5.setVisibility(View.GONE);
                }
                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.VISIBLE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.VISIBLE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.orange));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

                decide_layout(last_view(), select_2);
                animation_slide(filter_2, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 2;


            }
        });

        st_3.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (rbchk_4_3.isChecked()) {
                    days_st_1.setVisibility(View.GONE);
                    days_st_2.setVisibility(View.GONE);
                    days_st_3.setVisibility(View.VISIBLE);
                    days_st_4.setVisibility(View.GONE);
                    days_st_5.setVisibility(View.GONE);
                }

                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.VISIBLE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.VISIBLE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.orange));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

                decide_layout(last_view(), select_3);
                animation_slide(filter_3, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 3;


            }
        });

        st_4.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (rbchk_4_4.isChecked()) {
                    days_st_1.setVisibility(View.GONE);
                    days_st_2.setVisibility(View.GONE);
                    days_st_3.setVisibility(View.GONE);
                    days_st_4.setVisibility(View.VISIBLE);
                    days_st_5.setVisibility(View.GONE);
                }

                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.VISIBLE);
                lay_5.setVisibility(View.GONE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.VISIBLE);
                filter_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.orange));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));
                decide_layout(last_view(), select_4);
                animation_slide(filter_4, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 4;

            }
        });

        st_5.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (rbchk_4_5.isChecked()) {
                    days_st_1.setVisibility(View.GONE);
                    days_st_2.setVisibility(View.GONE);
                    days_st_3.setVisibility(View.GONE);
                    days_st_4.setVisibility(View.GONE);
                    days_st_5.setVisibility(View.VISIBLE);
                }
                lay_1.setVisibility(View.GONE);
                lay_2.setVisibility(View.GONE);
                lay_3.setVisibility(View.GONE);
                lay_4.setVisibility(View.GONE);
                lay_5.setVisibility(View.VISIBLE);

                filter_1.setVisibility(View.GONE);
                filter_2.setVisibility(View.GONE);
                filter_3.setVisibility(View.GONE);
                filter_4.setVisibility(View.GONE);
                filter_5.setVisibility(View.VISIBLE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.INVISIBLE);
                select_4.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.orange));

                decide_layout(last_view(), select_5);
                animation_slide(filter_5, temp1, temp2, temp3, temp4, view_1, view_2);
                last_clicked_position = 5;
            }
        });

        distributeStudentsTop();

        btn_next_card.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppConfiguration.builder = new ArrayList<String>();
                AppConfiguration.builder1 = new ArrayList<String>();
                AppConfiguration.builder2 = new ArrayList<String>();
                AppConfiguration.builder3 = new ArrayList<String>();
                AppConfiguration.builder4 = new ArrayList<String>();

                if (ScheduleLessonFragement.sameInstrctr) {
                    if (ScheduleLessonFragement.originalRestore.size() == 2) {
                        builder1.clear();
                        builder1.addAll(builder);
                    } else if (ScheduleLessonFragement.originalRestore.size() == 3) {
                        builder1.clear();
                        builder2.clear();
                        builder1.addAll(builder);
                        builder2.addAll(builder);
                    } else if (ScheduleLessonFragement.originalRestore.size() == 4) {
                        builder1.clear();
                        builder2.clear();
                        builder3.clear();
                        builder1.addAll(builder);
                        builder2.addAll(builder);
                        builder3.addAll(builder);
                    } else if (ScheduleLessonFragement.originalRestore.size() == 5) {
                        builder1.clear();
                        builder2.clear();
                        builder3.clear();
                        builder4.clear();
                        builder1.addAll(builder);
                        builder2.addAll(builder);
                        builder3.addAll(builder);
                        builder4.addAll(builder);
                    }

                }

                AppConfiguration.builder = builder;
                AppConfiguration.builder1 = builder1;
                AppConfiguration.builder2 = builder2;
                AppConfiguration.builder3 = builder3;
                AppConfiguration.builder4 = builder4;


                AppConfiguration.pair1_InstrList = AppConfiguration.Array2String(combo1);
                AppConfiguration.pair2_InstrList = AppConfiguration.Array2String(combo2);

                System.out.println("Slctd1 : " + builder);
                System.out.println("Slctd2 : " + builder1);
                System.out.println("Slctd3 : " + builder2);
                System.out.println("Slctd4 : " + builder3);
                System.out.println("Slctd5 : " + builder4);

                if (AppConfiguration.comboID.size() > 0) {
                    comboCombine();
                }

                if (AppConfiguration.selectedStudentsName.size() == 1) {
                    if (AppConfiguration.builder.size() == 0) {
                        SelectInstructorDialog();
                    } else {
                        jumpToOther();
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 2) {
                    if (AppConfiguration.builder1.size() == 0
                            || AppConfiguration.builder.size() == 0) {
                        SelectInstructorDialog();
                    } else {
                        jumpToOther();
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 3) {
                    if (AppConfiguration.builder1.size() == 0
                            || AppConfiguration.builder.size() == 0
                            || AppConfiguration.builder2.size() == 0) {
                        SelectInstructorDialog();
                    } else {
                        jumpToOther();
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 4) {
                    if (AppConfiguration.builder1.size() == 0
                            || AppConfiguration.builder.size() == 0
                            || AppConfiguration.builder2.size() == 0
                            || AppConfiguration.builder3.size() == 0) {
                        SelectInstructorDialog();
                    } else {
                        jumpToOther();
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 5) {
                    if (AppConfiguration.builder1.size() == 0
                            || AppConfiguration.builder.size() == 0
                            || AppConfiguration.builder2.size() == 0
                            || AppConfiguration.builder3.size() == 0
                            || AppConfiguration.builder4.size() == 0) {
                        SelectInstructorDialog();
                    } else {
                        jumpToOther();
                    }
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(mContext, ScheduleLessonFragement3.class);
                            startActivity(i);
                        }
                    }, 100);


                    // ((SchedulingMain)mContext).schedulingStep(3);
                }
            }
        });
    }

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

    boolean slide_left = false;

    public void decide_layout(final View v_1, final View v_2) {

        int pos_1 = Integer.parseInt(v_1.getTag().toString());
        int pos_2 = Integer.parseInt(v_2.getTag().toString());

        Log.d("Pos1", String.valueOf(pos_1));
        Log.d("Pos2", String.valueOf(pos_2));


        select_lay(pos_1, pos_2);
        if (pos_1 < pos_2) {
            view_1 = v_1;
            view_2 = v_2;
            slide_left = true;
        } else {
            view_1 = v_1;
            view_2 = v_2;
            slide_left = false;
        }
    }

    LinearLayout temp1, temp2, temp3, temp4;

    public void select_lay(int pos_1, int pos_2) {

        if (pos_1 == 1) {
            temp1 = days_st_1;
            temp2 = lay_1;
        } else if (pos_1 == 2) {
            temp1 = days_st_2;
            temp2 = lay_2;
        } else if (pos_1 == 3) {
            temp1 = days_st_3;
            temp2 = lay_3;
        } else if (pos_1 == 4) {
            temp1 = days_st_4;
            temp2 = lay_4;
        } else if (pos_1 == 5) {
            temp1 = days_st_5;
            temp2 = lay_5;
        }

        if (pos_2 == 1) {
            temp3 = days_st_1;
            temp4 = lay_1;
        } else if (pos_2 == 2) {
            temp3 = days_st_2;
            temp4 = lay_2;
        } else if (pos_2 == 3) {
            temp3 = days_st_3;
            temp4 = lay_3;
        } else if (pos_2 == 4) {
            temp3 = days_st_4;
            temp4 = lay_4;
        } else if (pos_2 == 5) {
            temp3 = days_st_5;
            temp4 = lay_5;
        }

    }

    public void animation_slide(final LinearLayout ll1,
                                final LinearLayout ll2,
                                final LinearLayout ll3,
                                final LinearLayout ll4,
                                final LinearLayout ll5,
                                final View v1,
                                final View v2) {

        if (!slide_left) {
            Animation animSlidInLeft = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);

            ll5.startAnimation(animSlidInLeft);

            if (ll1.getVisibility() == View.VISIBLE) {
            }

            Animation animSlidInLeftLine = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left_line_rev);
            v1.startAnimation(animSlidInLeftLine);

            animSlidInLeftLine.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v2.setVisibility(View.VISIBLE);

                    Animation animSlidInLeftLineRev = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left_line);
                    v2.startAnimation(animSlidInLeftLineRev);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {

            Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);

            ll5.startAnimation(animSlidInRight);

            if (ll1.getVisibility() == View.VISIBLE) {
                Animation animFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_right);
            }

            Animation animSlidInRightLineRev = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right_line_rev);
            v1.startAnimation(animSlidInRightLineRev);

            animSlidInRightLineRev.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v2.setVisibility(View.VISIBLE);

                    Animation animSlidInRightLine = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right_line);
                    v2.startAnimation(animSlidInRightLine);


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void comboCombine() {
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(builder);
        temp.toString().replace(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(0));
    }

    ArrayList<String> sameInstArray = new ArrayList<String>();

    /**
     *
     */
    private void jumpToOther() {
        // TODO Auto-generated method stub
        //		sameInstArray.addAll(AppConfiguration.selectedStudentsName);
        if (ScheduleLessonFragement.sameInstrctr) {
            AppConfiguration.selectedStudentsName.clear();
            AppConfiguration.selectedStudentsName.addAll(ScheduleLessonFragement.originalRestore);
            AppConfiguration.selectedStudentNameToSchedule = AppConfiguration.Array2String
                    (AppConfiguration.selectedStudentsName);
        }
        Intent i = new Intent(mContext, ScheduleLessonFragement3.class);
        startActivity(i);
    }

    public void CommonMethods(RadioGroup rbGroup, final CheckBox check1, final CheckBox check2, final RadioButton rb1,
                              final LinearLayout checkLay) {//CommonMethods(inst_types_grp, male, female, rbchk_4, spec_search_lay);
        check1.setEnabled(false);
        check2.setEnabled(false);

        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (rb1.isChecked()) {
                    visiBILITY();
                    if (isChecked) {
                        if (check2.isChecked()) {
                            specificSearch("both", whichLay());
                        } else {
                            specificSearch("M", whichLay());
                        }
                    } else {
                        if (!check2.isChecked()) {
                            specificSearch("both", whichLay());
                        } else {
                            specificSearch("F", whichLay());
                        }
                    }
                }
            }
        });

        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (rb1.isChecked()) {
                    visiBILITY();
                    if (isChecked) {
                        if (check1.isChecked()) {
                            specificSearch("both", whichLay());
                        } else {
                            specificSearch("F", whichLay());
                        }

                        animate_IT = false;
                    } else {
                        if (!check1.isChecked()) {
                            specificSearch("both", whichLay());
                        } else {
                            specificSearch("M", whichLay());
//                             specificSearch("removef",whichLay());
                        }

                        animate_IT = false;
                    }

                }

            }
        });

        rbGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if (checkedId == R.id.rbchk_1 || checkedId == R.id.rbchk_1_2 || checkedId == R.id.rbchk_1_3
                        || checkedId == R.id.rbchk_1_4 || checkedId == R.id.rbchk_1_5) {
                    // selectAllMales("",whichLay());

                    if (last_clicked_position == 1) {
                        AppConfiguration.addCheckedOptions1 = 1;
                    } else if (last_clicked_position == 2) {
                        AppConfiguration.addCheckedOptions2 = 1;
                    } else if (last_clicked_position == 3) {
                        AppConfiguration.addCheckedOptions3 = 1;
                    } else if (last_clicked_position == 4) {
                        AppConfiguration.addCheckedOptions4 = 1;
                    } else if (last_clicked_position == 5) {
                        AppConfiguration.addCheckedOptions5 = 1;
                    }

                    days_st_1.setVisibility(View.GONE);
                    days_st_2.setVisibility(View.GONE);
                    days_st_3.setVisibility(View.GONE);
                    days_st_4.setVisibility(View.GONE);
                    days_st_5.setVisibility(View.GONE);
//
                    selectAllMales("both", whichLay());
//
                    checkLay.setVisibility(View.GONE);
//                    check1.setChecked(false);
//                    check2.setChecked(false);
                    check1.setEnabled(false);
                    check2.setEnabled(false);
//                    specificSearch("both", whichLay());
                } else if (checkedId == R.id.rbchk_2 || checkedId == R.id.rbchk_2_2 || checkedId == R.id.rbchk_2_3
                        || checkedId == R.id.rbchk_2_4 || checkedId == R.id.rbchk_2_5) {

                    if (last_clicked_position == 1) {
                        AppConfiguration.addCheckedOptions1 = 2;
                    } else if (last_clicked_position == 2) {
                        AppConfiguration.addCheckedOptions2 = 2;
                    } else if (last_clicked_position == 3) {
                        AppConfiguration.addCheckedOptions3 = 2;
                    } else if (last_clicked_position == 4) {
                        AppConfiguration.addCheckedOptions4 = 2;
                    } else if (last_clicked_position == 5) {
                        AppConfiguration.addCheckedOptions5 = 2;
                    }

                    days_st_1.setVisibility(View.GONE);
                    days_st_2.setVisibility(View.GONE);
                    days_st_3.setVisibility(View.GONE);
                    days_st_4.setVisibility(View.GONE);
                    days_st_5.setVisibility(View.GONE);
                    selectAllMales("M", whichLay());
//
                    checkLay.setVisibility(View.GONE);
//                    check1.setChecked(false);
//                    check2.setChecked(false);
                    check1.setEnabled(false);
                    check2.setEnabled(false);
//                    specificSearch("M", whichLay());
                } else if (checkedId == R.id.rbchk_3 || checkedId == R.id.rbchk_3_2 || checkedId == R.id.rbchk_3_3
                        || checkedId == R.id.rbchk_3_4 || checkedId == R.id.rbchk_3_5) {

                    if (last_clicked_position == 1) {
                        AppConfiguration.addCheckedOptions1 = 3;
                    } else if (last_clicked_position == 2) {
                        AppConfiguration.addCheckedOptions2 = 3;
                    } else if (last_clicked_position == 3) {
                        AppConfiguration.addCheckedOptions3 = 3;
                    } else if (last_clicked_position == 4) {
                        AppConfiguration.addCheckedOptions4 = 3;
                    } else if (last_clicked_position == 5) {
                        AppConfiguration.addCheckedOptions5 = 3;
                    }

                    days_st_1.setVisibility(View.GONE);
                    days_st_2.setVisibility(View.GONE);
                    days_st_3.setVisibility(View.GONE);
                    days_st_4.setVisibility(View.GONE);
                    days_st_5.setVisibility(View.GONE);
                    selectAllMales("F", whichLay());
                    checkLay.setVisibility(View.GONE);
//                    check1.setChecked(false);
//                    check2.setChecked(false);
                    check1.setEnabled(false);
                    check2.setEnabled(false);

//                    specificSearch("F", whichLay());
                } else if (checkedId == R.id.rbchk_4 || checkedId == R.id.rbchk_4_2 || checkedId == R.id.rbchk_4_3
                        || checkedId == R.id.rbchk_4_4 || checkedId == R.id.rbchk_4_5) {

                    if (last_clicked_position == 1) {
                        AppConfiguration.addCheckedOptions1 = 4;
                    } else if (last_clicked_position == 2) {
                        AppConfiguration.addCheckedOptions2 = 4;
                    } else if (last_clicked_position == 3) {
                        AppConfiguration.addCheckedOptions3 = 4;
                    } else if (last_clicked_position == 4) {
                        AppConfiguration.addCheckedOptions4 = 4;
                    } else if (last_clicked_position == 5) {
                        AppConfiguration.addCheckedOptions5 = 4;
                    }

                    selectAllMales("bothVisible", whichLay());
//                    specificSearch("both", whichLay());
//                    specificSearch("bothVisible", whichLay());
                    checkLay.setVisibility(View.VISIBLE);

                    check1.setEnabled(true);
                    check2.setEnabled(true);
//                    check1.setChecked(false);
//                    check2.setChecked(false);
                    Animation animSlidDown = AnimationUtils.loadAnimation(mContext, R.anim.slid_in_down);
                    checkLay.startAnimation(animSlidDown);
                    Animation animSlidUp = AnimationUtils.loadAnimation(mContext, R.anim.slidup_medium);
                    days_st_1.startAnimation(animSlidUp);
                    days_st_2.startAnimation(animSlidUp);
                    days_st_3.startAnimation(animSlidUp);
                    days_st_4.startAnimation(animSlidUp);
                    days_st_5.startAnimation(animSlidUp);
//                    Animation animSlidDownpopup = AnimationUtils.loadAnimation(mContext, R.anim.slid_down_slow);
//                    btn_next_card.startAnimation(animSlidDown);

                }

//                Animation animSlidUp = AnimationUtils.loadAnimation(mContext, R.anim.slidup_medium);
//                days_st_1.startAnimation(animSlidUp);
//                days_st_2.startAnimation(animSlidUp);
//                days_st_3.startAnimation(animSlidUp);
//                days_st_4.startAnimation(animSlidUp);
//                days_st_5.startAnimation(animSlidUp);

            }
        });

    }

    public void visiBILITY() {
        if (select_1.getVisibility() == View.VISIBLE) {
            days_st_1.setVisibility(View.VISIBLE);
            days_st_2.setVisibility(View.GONE);
            days_st_3.setVisibility(View.GONE);
            days_st_4.setVisibility(View.GONE);
            days_st_5.setVisibility(View.GONE);


        } else if (select_2.getVisibility() == View.VISIBLE) {
            days_st_2.setVisibility(View.VISIBLE);
            days_st_1.setVisibility(View.GONE);
            days_st_3.setVisibility(View.GONE);
            days_st_4.setVisibility(View.GONE);
            days_st_5.setVisibility(View.GONE);


        } else if (select_3.getVisibility() == View.VISIBLE) {
            days_st_3.setVisibility(View.VISIBLE);
            days_st_1.setVisibility(View.GONE);
            days_st_2.setVisibility(View.GONE);
            days_st_4.setVisibility(View.GONE);
            days_st_5.setVisibility(View.GONE);


        } else if (select_4.getVisibility() == View.VISIBLE) {
            days_st_4.setVisibility(View.VISIBLE);
            days_st_1.setVisibility(View.GONE);
            days_st_2.setVisibility(View.GONE);
            days_st_3.setVisibility(View.GONE);
            days_st_5.setVisibility(View.GONE);


        } else if (select_5.getVisibility() == View.VISIBLE) {
            days_st_5.setVisibility(View.VISIBLE);
            days_st_1.setVisibility(View.GONE);
            days_st_2.setVisibility(View.GONE);
            days_st_3.setVisibility(View.GONE);
            days_st_4.setVisibility(View.GONE);

        }
    }

    public LinearLayout whichLay() {
        LinearLayout lay = null;
        if (select_1.getVisibility() == View.VISIBLE) {
            lay = days_st_1;
        } else if (select_2.getVisibility() == View.VISIBLE) {
            lay = days_st_2;
        } else if (select_3.getVisibility() == View.VISIBLE) {
            lay = days_st_3;
        } else if (select_4.getVisibility() == View.VISIBLE) {
            lay = days_st_4;
        } else if (select_5.getVisibility() == View.VISIBLE) {
            lay = days_st_5;
        }
        return lay;
    }

    public void selectAllMales(String value, LinearLayout inst_inflate) {

        Log.d("value", "" + value);
        Log.d("inst_inflate", "" + inst_inflate);

        for (int i = 0; i < inst_inflate.getChildCount(); i++) {
            View view = inst_inflate.getChildAt(i);
            System.out.println("Child : " + inst_inflate.getChildCount());
            if (view instanceof CardView) {
                CardView ll = (CardView) view;
                View view2 = ll.getChildAt(0);
                if (view2 instanceof LinearLayout) {
                    LinearLayout ll2 = (LinearLayout) view2;
                    View view3 = ll2.getChildAt(0);
                    if (view3 instanceof LinearLayout) {
                        LinearLayout ll3 = (LinearLayout) view3;
                        for (int j = 0; j < ll3.getChildCount(); j++) {
                            View view4 = ll3.getChildAt(2);
                            if (view4 instanceof LinearLayout) {
                                LinearLayout ll4 = (LinearLayout) view4;
                                View view5 = ll4.getChildAt(j);
                                if (view5 instanceof CheckBox) {
                                    CheckBox check = (CheckBox) view5;

                                    if (value.equalsIgnoreCase("bothVisible")) {
                                        inst_inflate.setVisibility(View.VISIBLE);

                                        check.setChecked(false);
                                    } else if (!value.equalsIgnoreCase("both")) {
                                        if (ll3.getTag().toString().equalsIgnoreCase(value)) {

                                            if (ll3.getTag().toString().equalsIgnoreCase("M")) {
                                                /*Animation animSlidUp = AnimationUtils.loadAnimation(mContext, R.anim.combine_fade_out_slide_up);
                                                inst_inflate.startAnimation(animSlidUp);*/
                                                check.setChecked(true);
//                                                inst_inflate.setVisibility(View.GONE);

                                            }
                                            if (ll3.getTag().toString().equalsIgnoreCase("F")) {
                                                /*Animation animSlidUp = AnimationUtils.loadAnimation(mContext, R.anim.combine_fade_out_slide_up);
                                                inst_inflate.startAnimation(animSlidUp);*/
                                                check.setChecked(true);
//                                                inst_inflate.setVisibility(View.GONE);

                                            }

                                        } else {
//                                            if(!ll3.getTag().toString().equalsIgnoreCase(value)) {
                                            check.setChecked(false);
//                                            inst_inflate.setVisibility(View.VISIBLE);
//                                            }
                                        }
                                    } else {

                                        check.setChecked(true);
                                    }

                                }
                            }
                        }

//                        System.out.println("Tag : " + ll3.getTag().toString());
                    }
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
        }

        for (int i = 0; i < AppConfiguration.selectedStudentsName.size(); i++) {
            if (i == 0) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
                    //					name_1.setText(temp[0] + "\n" + temp[1]);
                    name_1.setText(temp[0]);
                } else {
                    name_1.setText(AppConfiguration.selectedStudentsName.get(i));
                }
            } else if (i == 1) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
                    //					name_2.setText(temp[0] + "\n" + temp[1]);
                    name_2.setText(temp[0]);
                } else {
                    name_2.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            } else if (i == 2) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
                    //					name_3.setText(temp[0] + "\n" + temp[1]);
                    name_3.setText(temp[0]);
                } else {
                    name_3.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            } else if (i == 3) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
                    //					name_4.setText(temp[0] + "\n" + temp[1]);
                    name_4.setText(temp[0]);
                } else {
                    name_4.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            } else if (i == 4) {
                if (AppConfiguration.selectedStudentsName.get(i).contains(" ")) {
                    String temp[] = AppConfiguration.selectedStudentsName.get(i).split("\\s+");
                    //					name_5.setText(temp[0] + "\n" + temp[1]);
                    name_5.setText(temp[0]);
                } else {
                    name_5.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            }
        }
    }

    boolean animate_IT = false;

    // shrenik was here 2/22/16 11:16 PM

    public void specificSearch4(String value, LinearLayout inst_inflate) {
        if (value == "both") {
            for (int i = 0; i <= inst_inflate.getChildCount(); i++) {
                inst_inflate.getChildAt(i).setVisibility(View.VISIBLE);
            }
        } else {
            for (int i = 0; i <= inst_inflate.getChildCount(); i++) {
                if (inst_inflate.getChildAt(i).getTag().toString().equalsIgnoreCase(value)) {
                    inst_inflate.getChildAt(i).setVisibility(View.VISIBLE);
                } else {
                    inst_inflate.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }
    }


    public void specificSearch(String value, LinearLayout inst_inflate) {
        Log.d("Value : ", value);
        for (int i = 0; i < inst_inflate.getChildCount(); i++) {
            View view = inst_inflate.getChildAt(i);
            System.out.println("Child : " + inst_inflate.getChildCount());
            if (view instanceof CardView) {
                final CardView ll = (CardView) view;
                View view2 = ll.getChildAt(0);
                if (view2 instanceof LinearLayout) {
                    LinearLayout ll2 = (LinearLayout) view2;
                    View view3 = ll2.getChildAt(0);
                    if (view3 instanceof LinearLayout) {
                        final LinearLayout ll3 = (LinearLayout) view3;
                        if (!value.equalsIgnoreCase("both")) {
                            if (ll3.getTag().toString().equalsIgnoreCase(value)) {

                                Animation animSlidUp = AnimationUtils.loadAnimation(mContext, R.anim.slidup_medium_instructor);
                                ll.startAnimation(animSlidUp);
                                ll.setVisibility(View.VISIBLE);
                                Log.e("visibility--8", "" + ll3.isShown());
                            } else {
                                animate_IT = true;
                                ll.setVisibility(View.GONE);
                                Log.e("visibility--7", "" + ll3.isShown());
                            }
                        } else if (value.equalsIgnoreCase("removef")) {

                            if (ll3.getTag().toString().equalsIgnoreCase("F")) {
                                Log.e("visibility--6", "" + ll3.isShown());
                                ll.setVisibility(View.GONE);
//

                            } else {
                                Log.e("visibility--5", "" + ll3.isShown());
                                ll.setVisibility(View.VISIBLE);
//
                            }

                        } else if (value.equalsIgnoreCase("removem")) {

                            if (ll3.getTag().toString().equalsIgnoreCase("M")) {
                                ll.setVisibility(View.GONE);
                                Log.e("visibility--4", "" + ll3.isShown());

                            } else {

                                Log.e("visibility--3", "" + ll3.isShown());
                                ll.setVisibility(View.VISIBLE);

                            }

                        } else {

                            if (ll.getVisibility() == View.GONE) {
                                if (ll3.getTag().toString().equalsIgnoreCase("M")) {
                                    Animation animSlidUp = AnimationUtils.loadAnimation(mContext, R.anim.slid_in_down);
                                    ll.startAnimation(animSlidUp);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll.setVisibility(View.VISIBLE);
                                            Log.e("visibility--2", "" + ll3.isShown());
                                        }
                                    }, 100);
                                } else if (ll3.getTag().toString().equalsIgnoreCase("F")) {
                                    Animation animSlidUp = AnimationUtils.loadAnimation(mContext, R.anim.slid_in_down);
                                    ll.startAnimation(animSlidUp);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll.setVisibility(View.VISIBLE);
                                            Log.e("visibility--1", "" + ll3.isShown());
                                        }
                                    }, 100);
                                } else {

                                }

                            }


                        }
//                        ll3.setVisibility(View.VISIBLE);
                        System.out.println("Tag : " + ll3.getTag().toString());
                    }
                }
            }
        }
    }


    // Vars
    int studentsize;
    ArrayList<Boolean> instructorSelectedList = new ArrayList<Boolean>();
    String Les6Msg;


    class LoadInstructorListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            AppConfiguration.instructorList.clear();
            AppConfiguration.instructorList_2.clear();
            AppConfiguration.instructorList_3.clear();
            AppConfiguration.instructorList_4.clear();
            AppConfiguration.instructorList_5.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadingInstructorList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            try {
                if (successLoadChildList.toString().equals("True")) {
                    for (int i = 0; i < AppConfiguration.selectedStudentsName.size(); i++) {
                        if (i == 0) {
                            inflateInstructor(AppConfiguration.instructorList, i);
                        } else if (i == 1) {
                            inflateInstructor(AppConfiguration.instructorList_2, i);
                        } else if (i == 2) {
                            inflateInstructor(AppConfiguration.instructorList_3, i);
                        } else if (i == 3) {
                            inflateInstructor(AppConfiguration.instructorList_4, i);
                        } else if (i == 4) {
                            inflateInstructor(AppConfiguration.instructorList_5, i);
                        }
                    }

                } else {
                }
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public void inflateInstructor(ArrayList<HashMap<String, String>> instructorList, int pos) {
        for (int i = 0; i < instructorList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.d2_custom_inst_inflate, null);
            final TextView inst_name = (TextView) view.findViewById(R.id.inst_name);
            CircleImageView inst_dp = (CircleImageView) view.findViewById(R.id.inst_DP);
            final CheckBox check_selected = (CheckBox) view.findViewById(R.id.check_selected);
            final LinearLayout changing_lay = (LinearLayout) view.findViewById(R.id.changing_lay);
//            final View vw_shadow = (View) view.findViewById(R.id.vw_shadow);
//            TextView view_bio = (TextView) view.findViewById(R.id.view_bio);
//            TextView view_shift = (TextView) view.findViewById(R.id.view_shift);

            Button view_bio = (Button) view.findViewById(R.id.view_bio);
            Button view_shift = (Button) view.findViewById(R.id.view_shift);
            view_shift.setTag(instructorList.get(i).get("UserID")
                    + ":" + instructorList.get(i).get("Username"));

            view_bio.setTag(instructorList.get(i).get("UserID"));

            view_bio.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    selectedInstructor = v.getTag().toString();
                    new getBio().execute();
                }
            });
            view_shift.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    selectedInstructor = v.getTag().toString();

                    new InstrcutorAvailalibityAsyncTask().execute();
                }
            });

            changing_lay.setTag(instructorList.get(i).get("gender"));

            if (instructorList.get(i).get("gender").toString().equalsIgnoreCase("F")) {
                changing_lay.setBackgroundResource(R.drawable.female_selector_transition);
            }

            check_selected.setId(Integer.parseInt(instructorList.get(i).get("UserID")));
            check_selected.setTag(instructorList.get(i).get("UserID") + ":"
                    + instructorList.get(i).get("Username"));

            inst_name.setText(instructorList.get(i).get("Username"));

            if (fromWhere != null) {
                if (pos == 0) {
                    if (AppConfiguration.builder.contains(String.valueOf(check_selected.getId()))) {
                        check_selected.setChecked(true);
                    } else {
                        check_selected.setChecked(false);
                    }
                } else if (pos == 1) {
                    if (AppConfiguration.builder1.contains(String.valueOf(check_selected.getId()))) {
                        check_selected.setChecked(true);
                    } else {
                        check_selected.setChecked(false);
                    }
                } else if (pos == 2) {
                    if (AppConfiguration.builder2.contains(String.valueOf(check_selected.getId()))) {
                        check_selected.setChecked(true);
                    } else {
                        check_selected.setChecked(false);
                    }
                } else if (pos == 3) {
                    if (AppConfiguration.builder3.contains(String.valueOf(check_selected.getId()))) {
                        check_selected.setChecked(true);
                    } else {
                        check_selected.setChecked(false);
                    }
                } else if (pos == 4) {
                    if (AppConfiguration.builder4.contains(String.valueOf(check_selected.getId()))) {
                        check_selected.setChecked(true);
                    } else {
                        check_selected.setChecked(false);
                    }
                }

                View viewNew = (View) check_selected.getParent();

                if (check_selected.isChecked()) {
//                    vw_shadow.setVisibility(View.VISIBLE);
                    check_selected.setButtonTintList(getResources().getColorStateList(R.color.white));
                } else {
//                    vw_shadow.setVisibility(View.INVISIBLE);
                    check_selected.setButtonTintList(getResources().getColorStateList(R.color.design_change_d2));
                }

//                    check_selected.startAnimation(animFadein);
                if (viewNew instanceof LinearLayout) {
                    LinearLayout ll = (LinearLayout) viewNew;
                    View view1 = (View) ll.getParent();
                    if (view1 instanceof LinearLayout) {
                        LinearLayout ll2 = (LinearLayout) view1;
                        System.out.println("Tag : " + ll2.getTag());
                        if (ll2.getTag().toString().equalsIgnoreCase("F")) {
                            if (check_selected.isChecked()) {

                                TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                transition.startTransition(200);

                                if (pos == 0) {
                                    st_1.setBackgroundResource(R.drawable.error_border_white);
                                    combo1.add(check_selected.getTag().toString());
                                    builder.add(String.valueOf(check_selected.getId()));
                                } else if (pos == 1) {
                                    st_2.setBackgroundResource(R.drawable.error_border_white);
                                    builder1.add(String.valueOf(check_selected.getId()));
                                    combo2.add(check_selected.getTag().toString());
                                } else if (pos == 2) {
                                    st_3.setBackgroundResource(R.drawable.error_border_white);
                                    builder2.add(String.valueOf(check_selected.getId()));
                                    //										combo3.add(check_selected.getTag().toString());
                                } else if (pos == 3) {
                                    st_4.setBackgroundResource(R.drawable.error_border_white);
                                    builder3.add(String.valueOf(check_selected.getId()));
                                    //										combo4.add(check_selected.getTag().toString());
                                } else if (pos == 4) {
                                    st_5.setBackgroundResource(R.drawable.error_border_white);
                                    builder4.add(String.valueOf(check_selected.getId()));
                                    //										combo5.add(check_selected.getTag().toString());
                                }

                                AppConfiguration.gender_arry.put(inst_name.getText().toString(),
                                        ll2.getTag().toString());
//                                    ll2.setBackgroundResource(R.drawable.box1);
                            } else {
                                TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                transition.reverseTransition(500);

                                if (pos == 0) {
                                    combo1.remove(check_selected.getTag().toString());
                                    builder.remove(String.valueOf(check_selected.getId()));
                                } else if (pos == 1) {
                                    builder1.remove(String.valueOf(check_selected.getId()));
                                    combo2.remove(check_selected.getTag().toString());
                                } else if (pos == 2) {
                                    builder2.remove(String.valueOf(check_selected.getId()));
                                    //										combo3.remove(check_selected.getTag().toString());
                                } else if (pos == 3) {
                                    builder3.remove(String.valueOf(check_selected.getId()));
                                    //										combo4.remove(check_selected.getTag().toString());
                                } else if (pos == 4) {
                                    builder4.remove(String.valueOf(check_selected.getId()));
                                    //										combo5.remove(check_selected.getTag().toString());
                                }
                                AppConfiguration.SelectedInstName.remove(inst_name.getText().toString());
//                                    ll2.setBackgroundResource(R.drawable.female_selector_transition);
                            }
                        } else {
                            if (check_selected.isChecked()) {
                                TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                transition.startTransition(200);

                                if (pos == 0) {
                                    builder.add(String.valueOf(check_selected.getId()));
                                    st_1.setBackgroundResource(R.drawable.error_border_white);
                                    combo1.add(check_selected.getTag().toString());
                                } else if (pos == 1) {
                                    builder1.add(String.valueOf(check_selected.getId()));
                                    st_2.setBackgroundResource(R.drawable.error_border_white);
                                    combo2.add(check_selected.getTag().toString());
                                } else if (pos == 2) {
                                    builder2.add(String.valueOf(check_selected.getId()));
                                    st_3.setBackgroundResource(R.drawable.error_border_white);
                                    //										combo3.add(check_selected.getTag().toString());
                                } else if (pos == 3) {
                                    builder3.add(String.valueOf(check_selected.getId()));
                                    st_4.setBackgroundResource(R.drawable.error_border_white);
                                    //										combo4.add(check_selected.getTag().toString());
                                } else if (pos == 4) {
                                    builder4.add(String.valueOf(check_selected.getId()));
                                    st_5.setBackgroundResource(R.drawable.error_border_white);
                                    //										combo5.add(check_selected.getTag().toString());
                                }
                                AppConfiguration.SelectedInstName.add(inst_name.getText().toString());

                                AppConfiguration.gender_arry.put(inst_name.getText().toString(),
                                        ll2.getTag().toString());
//                                    ll2.setBackgroundResource(R.drawable.box1);
                            } else {
                                TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                transition.reverseTransition(200);

                                if (pos == 0) {
                                    builder.remove(String.valueOf(check_selected.getId()));
                                    combo1.remove(check_selected.getTag().toString());
                                } else if (pos == 1) {
                                    builder1.remove(String.valueOf(check_selected.getId()));
                                    combo2.remove(check_selected.getTag().toString());
                                } else if (pos == 2) {
                                    builder2.remove(String.valueOf(check_selected.getId()));
                                    //										combo3.remove(check_selected.getTag().toString());
                                } else if (pos == 3) {
                                    builder3.remove(String.valueOf(check_selected.getId()));
                                    //										combo4.remove(check_selected.getTag().toString());
                                } else if (pos == 4) {
                                    builder4.remove(String.valueOf(check_selected.getId()));
                                    //										combo5.remove(check_selected.getTag().toString());
                                }
                                AppConfiguration.SelectedInstName.remove(inst_name.getText().toString());
//                                    ll2.setBackgroundResource(R.drawable.male_selector_transition);
                            }
                        }
                    }
                }
                System.out.println("Checked : " + check_selected.isChecked());
            }

            check_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    View view = (View) buttonView.getParent();
//                    Animation animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
//                    animFadein.setDuration(1000);

                    if (isChecked) {
//                        vw_shadow.setVisibility(View.VISIBLE);
                        buttonView.setButtonTintList(getResources().getColorStateList(R.color.white));
                    } else {
//                        vw_shadow.setVisibility(View.INVISIBLE);
                        buttonView.setButtonTintList(getResources().getColorStateList(R.color.design_change_d2));
                    }

//                    check_selected.startAnimation(animFadein);
                    if (view instanceof LinearLayout) {
                        LinearLayout ll = (LinearLayout) view;
                        View view1 = (View) ll.getParent();
                        if (view1 instanceof LinearLayout) {
                            LinearLayout ll2 = (LinearLayout) view1;
                            System.out.println("Tag : " + ll2.getTag());
                            if (ll2.getTag().toString().equalsIgnoreCase("F")) {
                                if (isChecked) {

                                    TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                    transition.startTransition(200);

                                    if (select_1.getVisibility() == View.VISIBLE) {
                                        st_1.setBackgroundResource(R.drawable.error_border_white);
                                        combo1.add(check_selected.getTag().toString());
                                        builder.add(String.valueOf(check_selected.getId()));
                                    } else if (select_2.getVisibility() == View.VISIBLE) {
                                        st_2.setBackgroundResource(R.drawable.error_border_white);
                                        builder1.add(String.valueOf(check_selected.getId()));
                                        combo2.add(check_selected.getTag().toString());
                                    } else if (select_3.getVisibility() == View.VISIBLE) {
                                        st_3.setBackgroundResource(R.drawable.error_border_white);
                                        builder2.add(String.valueOf(check_selected.getId()));
                                        //										combo3.add(check_selected.getTag().toString());
                                    } else if (select_4.getVisibility() == View.VISIBLE) {
                                        st_4.setBackgroundResource(R.drawable.error_border_white);
                                        builder3.add(String.valueOf(check_selected.getId()));
                                        //										combo4.add(check_selected.getTag().toString());
                                    } else if (select_5.getVisibility() == View.VISIBLE) {
                                        st_5.setBackgroundResource(R.drawable.error_border_white);
                                        builder4.add(String.valueOf(check_selected.getId()));
                                        //										combo5.add(check_selected.getTag().toString());
                                    }

                                    AppConfiguration.gender_arry.put(inst_name.getText().toString(),
                                            ll2.getTag().toString());
//                                    ll2.setBackgroundResource(R.drawable.box1);
                                } else {
                                    TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                    transition.reverseTransition(500);

                                    if (select_1.getVisibility() == View.VISIBLE) {
                                        combo1.remove(check_selected.getTag().toString());
                                        builder.remove(String.valueOf(check_selected.getId()));
                                    } else if (select_2.getVisibility() == View.VISIBLE) {
                                        builder1.remove(String.valueOf(check_selected.getId()));
                                        combo2.remove(check_selected.getTag().toString());
                                    } else if (select_3.getVisibility() == View.VISIBLE) {
                                        builder2.remove(String.valueOf(check_selected.getId()));
                                        //										combo3.remove(check_selected.getTag().toString());
                                    } else if (select_4.getVisibility() == View.VISIBLE) {
                                        builder3.remove(String.valueOf(check_selected.getId()));
                                        //										combo4.remove(check_selected.getTag().toString());
                                    } else if (select_5.getVisibility() == View.VISIBLE) {
                                        builder4.remove(String.valueOf(check_selected.getId()));
                                        //										combo5.remove(check_selected.getTag().toString());
                                    }
                                    AppConfiguration.SelectedInstName.remove(inst_name.getText().toString());
//                                    ll2.setBackgroundResource(R.drawable.female_selector_transition);
                                }
                            } else {
                                if (isChecked) {
                                    TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                    transition.startTransition(200);

                                    if (select_1.getVisibility() == View.VISIBLE) {
                                        builder.add(String.valueOf(check_selected.getId()));
                                        st_1.setBackgroundResource(R.drawable.error_border_white);
                                        combo1.add(check_selected.getTag().toString());
                                    } else if (select_2.getVisibility() == View.VISIBLE) {
                                        builder1.add(String.valueOf(check_selected.getId()));
                                        st_2.setBackgroundResource(R.drawable.error_border_white);
                                        combo2.add(check_selected.getTag().toString());
                                    } else if (select_3.getVisibility() == View.VISIBLE) {
                                        builder2.add(String.valueOf(check_selected.getId()));
                                        st_3.setBackgroundResource(R.drawable.error_border_white);
                                        //										combo3.add(check_selected.getTag().toString());
                                    } else if (select_4.getVisibility() == View.VISIBLE) {
                                        builder3.add(String.valueOf(check_selected.getId()));
                                        st_4.setBackgroundResource(R.drawable.error_border_white);
                                        //										combo4.add(check_selected.getTag().toString());
                                    } else if (select_5.getVisibility() == View.VISIBLE) {
                                        builder4.add(String.valueOf(check_selected.getId()));
                                        st_5.setBackgroundResource(R.drawable.error_border_white);
                                        //										combo5.add(check_selected.getTag().toString());
                                    }
                                    AppConfiguration.SelectedInstName.add(inst_name.getText().toString());

                                    AppConfiguration.gender_arry.put(inst_name.getText().toString(),
                                            ll2.getTag().toString());
//                                    ll2.setBackgroundResource(R.drawable.box1);
                                } else {
                                    TransitionDrawable transition = (TransitionDrawable) changing_lay.getBackground();
                                    transition.reverseTransition(200);

                                    if (select_1.getVisibility() == View.VISIBLE) {
                                        builder.remove(String.valueOf(check_selected.getId()));
                                        combo1.remove(check_selected.getTag().toString());
                                    } else if (select_2.getVisibility() == View.VISIBLE) {
                                        builder1.remove(String.valueOf(check_selected.getId()));
                                        combo2.remove(check_selected.getTag().toString());
                                    } else if (select_3.getVisibility() == View.VISIBLE) {
                                        builder2.remove(String.valueOf(check_selected.getId()));
                                        //										combo3.remove(check_selected.getTag().toString());
                                    } else if (select_4.getVisibility() == View.VISIBLE) {
                                        builder3.remove(String.valueOf(check_selected.getId()));
                                        //										combo4.remove(check_selected.getTag().toString());
                                    } else if (select_5.getVisibility() == View.VISIBLE) {
                                        builder4.remove(String.valueOf(check_selected.getId()));
                                        //										combo5.remove(check_selected.getTag().toString());
                                    }
                                    AppConfiguration.SelectedInstName.remove(inst_name.getText().toString());
//                                    ll2.setBackgroundResource(R.drawable.male_selector_transition);
                                }
                            }
                        }
                    }
                    System.out.println("Checked : " + isChecked);
                }
            });

            if (instructorList.get(i).get("wu_photo").toString().trim().length() > 0) {
                imageLoader.displayImage(instructorList.get(i).get("wu_photo").toString(), inst_dp);
            }

            if (pos == 0) {
                days_st_1.addView(view);
            } else if (pos == 1) {
                days_st_2.addView(view);
            } else if (pos == 2) {
                days_st_3.addView(view);
            } else if (pos == 3) {
                days_st_4.addView(view);
            } else if (pos == 4) {
                days_st_5.addView(view);
            }
        }

    }

    public int whichStudentSelected() {
        int which = 0;
        if (select_1.getVisibility() == View.VISIBLE) {
            which = 1;
        } else if (select_2.getVisibility() == View.VISIBLE) {
            which = 2;
        } else if (select_3.getVisibility() == View.VISIBLE) {
            which = 3;
        } else if (select_4.getVisibility() == View.VISIBLE) {
            which = 4;
        } else if (select_5.getVisibility() == View.VISIBLE) {
            which = 5;
        }
        return which;
    }

    public void loadingInstructorList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("LevelType", "1");
//        16-06-2017 megha
        param.put("schedulechoices",AppConfiguration.schedulechoices);/*AppConfiguration.schedulechoices or  "0"*/
        param.put("StudentId", AppConfiguration.selectedStudentID);
        param.put("siteid", AppConfiguration.salStep1SiteID);
        param.put("lessontypeValue", AppConfiguration.salStep1LessonID);
        param.put("lessontypeText", AppConfiguration.salStep1LessonText);
        param.put("Pair1_CmbValue1", AppConfiguration.pair1_comboValue1);
        param.put("Pair1_CmbValue2", AppConfiguration.pair1_comboValue2);
        param.put("Pair1_CmbValue3", AppConfiguration.pair1_comboValue3);
        param.put("Pair1_CmbValue4", AppConfiguration.pair1_comboValue4);
        param.put("StudentId_New",AppConfiguration.selectedStudentIDforStep2);
        Log.d("siteMainList---id1", "" + AppConfiguration.salStep1SiteID);
        String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALessionInstructorListURL, param);
        readAndParseJSONChildList(responseString);

    }

    public void readAndParseJSONChildList(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("InstructorList");
                JSONArray jsonArray = reader.optJSONArray("Less6Msg");
                    JSONArray jsonArray_1 = jsonArray.getJSONArray(0);
                JSONObject jsonObject = jsonArray_1.getJSONObject(0);

                Les6Msg = jsonObject.getString("Les6Msg").trim();

                studentsize = jsonMainNode.length();
                //				AppConfiguration.studentsize = studentsize;

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONArray jsonMainNode_1 = jsonMainNode.getJSONArray(i);
                    for (int j = 0; j < jsonMainNode_1.length(); j++) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        JSONObject jsonChildNode = jsonMainNode_1.getJSONObject(j);

                        String Username = jsonChildNode.getString("Username").trim();
                        String UserID = jsonChildNode.getString("UserID").trim();
                        String gender = jsonChildNode.getString("gender").trim();
                        String nextDate = jsonChildNode.getString("NextDate").trim();
                        // String wu_photo =
                        // jsonChildNode.getString("wu_photo").trim();

                        String temp_photo = "";
                        temp_photo = jsonChildNode.getString("wu_photo").trim();

                        if (temp_photo.trim().contains("~")) {
                            temp_photo = temp_photo.replace("~", "");
                            if (temp_photo.trim().contains(" ")) {
                                temp_photo = AppConfiguration.PhotoDomain + temp_photo.replace(" ", "%20");
                                System.out.println("Pic URL" + temp_photo);
                            } else {
                                temp_photo = AppConfiguration.PhotoDomain + temp_photo;
                                System.out.println("Pic URL" + temp_photo);
                            }
                        }

                        hashmap.put("Username", Username);
                        hashmap.put("UserID", UserID);
                        hashmap.put("gender", gender);
                        hashmap.put("nextDate", nextDate);
                        hashmap.put("wu_photo", temp_photo);


                        if (i == 0) {
                            AppConfiguration.instructorList.add(hashmap);
                            instNameID.put(Username, UserID);
                        } else if (i == 1) {
                            AppConfiguration.instructorList_2.add(hashmap);
                        } else if (i == 2) {
                            AppConfiguration.instructorList_3.add(hashmap);
                        } else if (i == 3) {
                            AppConfiguration.instructorList_4.add(hashmap);
                        } else if (i == 4) {
                            AppConfiguration.instructorList_5.add(hashmap);
                        }
                        instructorSelectedList.add(false);
                    }
                }

                System.out.println("InstructorList : " + AppConfiguration.instructorList);
                System.out.println("InstructorList2 : " + AppConfiguration.instructorList_2);

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("InstructorList");

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    //					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> instNameID = new HashMap<String, String>();

    private class SchedulePageLoad extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            schedulePageLoadList.clear();
            limits.clear();
        }

        @Override
        protected Void doInBackground(Void... params1) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("ScheduleType", String.valueOf(AppConfiguration.makeup_Clicked));

            String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALessionPageLoad, params);
            try {
                JSONObject reader = new JSONObject(responseString);
                successGetStudentList = reader.getString("Success");
                if (successGetStudentList.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("LessonList");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        String pastdue = jsonChildNode.getString("pastdue").trim();
                        String UnusedMsg = jsonChildNode.getString("UnusedMsg").trim();
                        String pastduemessage = jsonChildNode.getString("pastduemessage").trim();

                        String mupravailable = jsonChildNode.getString("mupravailable").trim();
                        String muspavailable = jsonChildNode.getString("muspavailable").trim();
                        String muadultavailable = jsonChildNode.getString("muadultavailable").trim();
                        String mupmbegavailable = jsonChildNode.getString("mupmbegavailable").trim();
                        String mupmintavaiable = jsonChildNode.getString("mupmintavaiable").trim();
                        String mupmadvavailable = jsonChildNode.getString("mupmadvavailable").trim();
                        String muscintavailable = jsonChildNode.getString("muscintavailable").trim();
                        String muscadvavailable = jsonChildNode.getString("muscadvavailable").trim();
                        String muscbegavailable = jsonChildNode.getString("muscbegavailable").trim();
                        String Makeupflg = jsonChildNode.getString("Makeupflg").trim();
                        String pavailable = jsonChildNode.getString("pavailable").trim();
                        String spavailable = jsonChildNode.getString("spavailable").trim();
                        String aavailable = jsonChildNode.getString("aavailable").trim();
                        String pmbavailable = jsonChildNode.getString("pmbavailable").trim();
                        String pmiavailable = jsonChildNode.getString("pmiavailable").trim();
                        String pmaavailable = jsonChildNode.getString("pmaavailable").trim();
                        String sciavailable = jsonChildNode.getString("sciavailable").trim();
                        String scavailable = jsonChildNode.getString("scavailable").trim();
                        String Date = jsonChildNode.getString("Date").trim();

                        hashmap.put("pastduemessage", pastduemessage);
                        hashmap.put("pastdue", pastdue);
                        hashmap.put("UnusedMsg", UnusedMsg);
                        hashmap.put("mupravailable", mupravailable);
                        hashmap.put("muspavailable", muspavailable);
                        hashmap.put("muadultavailable", muadultavailable);
                        hashmap.put("mupmbegavailable", mupmbegavailable);
                        hashmap.put("mupmintavaiable", mupmintavaiable);
                        hashmap.put("mupmadvavailable", mupmadvavailable);
                        hashmap.put("muscintavailable", muscintavailable);
                        hashmap.put("muscadvavailable", muscadvavailable);
                        hashmap.put("muscbegavailable", muscbegavailable);
                        hashmap.put("Makeupflg", Makeupflg);
                        hashmap.put("pavailable", pavailable);
                        hashmap.put("spavailable", spavailable);
                        hashmap.put("aavailable", aavailable);
                        hashmap.put("pmbavailable", pmbavailable);
                        hashmap.put("pmiavailable", pmiavailable);
                        hashmap.put("pmaavailable", pmaavailable);
                        hashmap.put("sciavailable", sciavailable);
                        hashmap.put("scavailable", scavailable);
                        hashmap.put("Date", Date);

                        AppConfiguration.makeUpFlag = Makeupflg;
                        AppConfiguration.pastDueBalance = pastdue;

                        schedulePageLoadList.add(hashmap);
                        System.out.println("Schedule : " + schedulePageLoadList);
                    }

                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (schedulePageLoadList.get(0).get("Makeupflg").toString().equalsIgnoreCase("1")) {
                HashMap<String, String> hashmap = new HashMap<String, String>();
                hashmap.put("private_lessons", schedulePageLoadList.get(0).get("mupravailable").toString());
                hashmap.put("semiprivate", schedulePageLoadList.get(0).get("muspavailable").toString());
                hashmap.put("adult", schedulePageLoadList.get(0).get("muadultavailable").toString());
                hashmap.put("pmbeg", schedulePageLoadList.get(0).get("mupmbegavailable").toString());
                hashmap.put("pmint", schedulePageLoadList.get(0).get("mupmintavaiable").toString());
                hashmap.put("pmad", schedulePageLoadList.get(0).get("mupmadvavailable").toString());
                hashmap.put("scint", schedulePageLoadList.get(0).get("muscintavailable").toString());
                hashmap.put("scad", schedulePageLoadList.get(0).get("muscadvavailable").toString());
                hashmap.put("scbeg", schedulePageLoadList.get(0).get("muscbegavailable").toString());

                limits.add(hashmap);
            } else {
                HashMap<String, String> hashmap = new HashMap<String, String>();
                hashmap.put("private_lessons", schedulePageLoadList.get(0).get("pavailable").toString());
                hashmap.put("semiprivate", schedulePageLoadList.get(0).get("spavailable").toString());
                hashmap.put("adult", schedulePageLoadList.get(0).get("aavailable").toString());
                hashmap.put("pmbeg", schedulePageLoadList.get(0).get("pmbavailable").toString());
                hashmap.put("pmint", schedulePageLoadList.get(0).get("pmiavailable").toString());
                hashmap.put("pmad", schedulePageLoadList.get(0).get("pmaavailable").toString());
                hashmap.put("scint", schedulePageLoadList.get(0).get("sciavailable").toString());
                hashmap.put("scad", schedulePageLoadList.get(0).get("scavailable").toString());
                hashmap.put("scbeg", "");

                limits.add(hashmap);
            }
        }
    }


    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
//        btn_next.setTypeface(regular);
    }

    public void SelectInstructorDialog() {

        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Warning");

        TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
        tv_description.setText(
                "Instructors have not been selected for all students. Please complete selection before continuing.");
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

            }
        });
        CardView button_ok = (CardView) layout.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                scrollview.smoothScrollTo(0, 0);
                if (popWindow.isShowing() == false) {


                    if (AppConfiguration.builder1.size() == 0) {
                        st_2.setBackgroundResource(R.drawable.error_border);
                    }
                    if (AppConfiguration.builder.size() == 0) {
                        st_1.setBackgroundResource(R.drawable.error_border);
                    }
                    if (AppConfiguration.builder2.size() == 0) {
                        st_3.setBackgroundResource(R.drawable.error_border);
                    }
                    if (AppConfiguration.builder3.size() == 0) {
                        st_4.setBackgroundResource(R.drawable.error_border);
                    }
                    if (AppConfiguration.builder4.size() == 0) {
                        st_5.setBackgroundResource(R.drawable.error_border);
                    }

                    if (AppConfiguration.builder1.size() == 0) {
                        // st_2.setBackgroundResource(R.drawable.error_border);
                        if (AppConfiguration.builder.size() != 0 || AppConfiguration.builder2.size() != 0 || AppConfiguration.builder3.size() != 0 || AppConfiguration.builder4.size() != 0) {

//                           student2ClickEvent();
                            st_2.performClick();
                        }
                        /*else{
                            student1ClickEvent();
                            student3ClickEvent();
                            student4ClickEvent();
                            student5ClickEvent();
                        }*/
                    } else if (AppConfiguration.builder.size() == 0) {
                        // st_1.setBackgroundResource(R.drawable.error_border);
                        if (AppConfiguration.builder1.size() != 0 || AppConfiguration.builder2.size() != 0 || AppConfiguration.builder3.size() != 0 || AppConfiguration.builder4.size() != 0) {

//                            student1ClickEvent();
                            st_1.performClick();
                        }
                       /* else{
                            student2ClickEvent();
                            student3ClickEvent();
                            student4ClickEvent();
                            student5ClickEvent();
                        }*/

                    } else if (AppConfiguration.builder2.size() == 0) {
                        //  st_3.setBackgroundResource(R.drawable.error_border);

                        if (AppConfiguration.builder.size() != 0 || AppConfiguration.builder1.size() != 0 || AppConfiguration.builder3.size() != 0 || AppConfiguration.builder4.size() != 0) {

//                            student3ClickEvent();
                            st_3.performClick();
                        }
                    } else if (AppConfiguration.builder3.size() == 0) {
                        //st_4.setBackgroundResource(R.drawable.error_border);

                        if (AppConfiguration.builder.size() != 0 || AppConfiguration.builder1.size() != 0 || AppConfiguration.builder2.size() != 0 || AppConfiguration.builder4.size() != 0) {

//                            student4ClickEvent();
                            st_4.performClick();
                        }
                    } else if (AppConfiguration.builder4.size() == 0) {
                        // st_5.setBackgroundResource(R.drawable.error_border);

                        if (AppConfiguration.builder.size() != 0 || AppConfiguration.builder1.size() != 0 || AppConfiguration.builder2.size() != 0 || AppConfiguration.builder3.size() != 0) {

//                            student5ClickEvent();
                            st_5.performClick();
                        }
                    }
                } else {
                }
            }
        });
    }

    //Inst. Avaibility
    ArrayList<HashMap<String, String>> instructorAvailableList = new ArrayList<HashMap<String, String>>();
    String selectedInstructor = "";

    class InstrcutorAvailalibityAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ScheduleLessonFragement2.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
//            pd.show();

            instructorAvailableList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadingChildList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if (pd != null) {
//                pd.dismiss();
//            }


            if (successLoadChildList.toString().equals("True")) {

                String strMonday = instructorAvailableList.get(0).get("mstr").trim();
                String strTuesday = instructorAvailableList.get(0).get("tstr").trim();
                String strWendesday = instructorAvailableList.get(0).get("wstr").trim();
                String strThursday = instructorAvailableList.get(0).get("thstr").trim();
                String strFridayday = instructorAvailableList.get(0).get("fstr").trim();
                String strSaturday = instructorAvailableList.get(0).get("sstr").trim();
                String strSunday = instructorAvailableList.get(0).get("sustr").trim();

                LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = lInflater.inflate(R.layout.inst_avai_pop_up_layout, null);
                final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                popWindow.setAnimationStyle(R.style.Animation);

                Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                slide_up.setDuration(1);
                popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

                LinearLayout pop_square;
                pop_square = (LinearLayout) layout.findViewById(R.id.pop_square);
                pop_square.startAnimation(slide_up);

                TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
                tv_appname.setText("Shifts");

                TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
//                Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
//                layout.startAnimation(animFadeIn);
                imgv_icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();

                    }
                });
                Button button_ok = (Button) layout.findViewById(R.id.button_ok);
                button_ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();
                    }
                });
                TextView txtMonday, txtTuesday, txtWednesday, txtThursday, txtFriday, txtSaturday, txtSunday,
                        done;
                done = (TextView) layout.findViewById(R.id.done);
                txtMonday = (TextView) layout.findViewById(R.id.txtMonday);
                txtTuesday = (TextView) layout.findViewById(R.id.txtTuesday);
                txtWednesday = (TextView) layout.findViewById(R.id.txtWednesday);
                txtThursday = (TextView) layout.findViewById(R.id.txtThursday);
                txtFriday = (TextView) layout.findViewById(R.id.txtFriday);
                txtSaturday = (TextView) layout.findViewById(R.id.txtSaturday);
                txtSunday = (TextView) layout.findViewById(R.id.txtSunday);

                done.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();
                    }
                });
                if (strMonday.equals("")) {
                    txtMonday.setPadding(0, 10, 0, 0);
                    txtMonday.setText("Not available");
                } else
                    txtMonday.setText(Html.fromHtml(strMonday));

                if (strTuesday.equals("")) {
                    txtTuesday.setPadding(0, 10, 0, 0);
                    txtTuesday.setText("Not available");
                } else {
                    txtTuesday.setText(Html.fromHtml(strTuesday));
                }
                if (strWendesday.equals("")) {
                    txtWednesday.setPadding(0, 10, 0, 0);
                    txtWednesday.setText("Not available");
                } else
                    txtWednesday.setText(Html.fromHtml(strWendesday));

                if (strThursday.equals("")) {
                    txtThursday.setPadding(0, 10, 0, 0);
                    txtThursday.setText("Not available");
                } else
                    txtThursday.setText(Html.fromHtml(strThursday));

                if (strFridayday.equals("")) {
                    txtFriday.setPadding(0, 10, 0, 0);
                    txtFriday.setText("Not available");
                } else
                    txtFriday.setText(Html.fromHtml(strFridayday));

                if (strSaturday.equals("")) {
                    txtSaturday.setPadding(0, 10, 0, 0);
                    txtSaturday.setText("Not available");
                } else
                    txtSaturday.setText(Html.fromHtml(strSaturday));

                if (strSunday.equals("")) {
                    txtSunday.setPadding(0, 10, 0, 0);
                    txtSunday.setText("Not available");
                } else
                    txtSunday.setText(Html.fromHtml(strSunday));
            } else {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();

                //				finish();
            }
        }
    }


    public void loadingChildList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("Makeupflg", AppConfiguration.makeUpFlag);
        param.put("sselected", AppConfiguration.selectedStudentID);
        param.put("intrlist", selectedInstructor);
        param.put("calstartdate", "");

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.scheduleALessionStep2InstructorAvailabilityURL, param);
        readAndParseJSONChildList_(responseString);

    }

    public void readAndParseJSONChildList_(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successLoadChildList = reader.getString("Success");
            if (successLoadChildList.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("InstructorList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String mstr = jsonChildNode.getString("mstr").trim();
                    String tstr = jsonChildNode.getString("tstr").trim();
                    String wstr = jsonChildNode.getString("wstr").trim();
                    String thstr = jsonChildNode.getString("thstr").trim();
                    String fstr = jsonChildNode.getString("fstr").trim();
                    String sstr = jsonChildNode.getString("sstr").trim();
                    String sustr = jsonChildNode.getString("sustr").trim();

                    hashmap.put("mstr", mstr);
                    hashmap.put("tstr", tstr);
                    hashmap.put("wstr", wstr);
                    hashmap.put("thstr", thstr);
                    hashmap.put("fstr", fstr);
                    hashmap.put("sstr", sstr);
                    hashmap.put("sustr", sustr);

                    instructorAvailableList.add(hashmap);


                }

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("InstructorList");

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    //					message = jsonChildNode.getString("Msg").trim();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<HashMap<String, String>> aboutInst = new ArrayList<HashMap<String, String>>();

    public class getBio extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = new ProgressDialog(ScheduleLessonFragement2.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
//            pd.show();
            aboutInst.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("InstructorID", selectedInstructor);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetInstructorBio, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                successLoadChildList = reader.getString("Success");
                if (successLoadChildList.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        String wu_InsName = jsonChildNode.getString("wu_InsName").trim();
                        //						String wu_InsNature = jsonChildNode.getString("wu_InsNature").trim();
                        //						String wu_InsFeatures = jsonChildNode.getString("wu_InsFeatures").trim();
                        //						String wu_FirstDate = jsonChildNode.getString("wu_FirstDate").trim();
                        String wu_Photo = jsonChildNode.getString("wu_Photo").trim();
                        String wu_Bio = jsonChildNode.getString("wu_Bio").trim();

                        hashmap.put("wu_InsName", wu_InsName);
                        //						hashmap.put("wu_InsNature", wu_InsNature);
                        //						hashmap.put("wu_InsFeatures", wu_InsFeatures);
                        //						hashmap.put("wu_FirstDate", wu_FirstDate);
                        hashmap.put("wu_Photo", wu_Photo);
                        hashmap.put("wu_Bio", wu_Bio);
                        aboutInst.add(hashmap);
                    }

                } else {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

//            if (pd != null && pd.isShowing()) {
//                pd.dismiss();
//            }

            if (successLoadChildList.toString().equals("True")) {
                LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = lInflater.inflate(R.layout.inst_bio_pop_up_layout, null);
                final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                popWindow.setAnimationStyle(R.style.Animation);

                Animation slide_up = AnimationUtils.loadAnimation(mContext,
                        R.anim.slid_up_popup);
                slide_up.setDuration(1);
                popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                TextView tv_appname, inst_name, done, about;
                CircleImageView inst_dp;
                LinearLayout pop_square;

                pop_square = (LinearLayout) layout.findViewById(R.id.pop_square);
                pop_square.startAnimation(slide_up);

                done = (TextView) layout.findViewById(R.id.done);
                tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
                inst_name = (TextView) layout.findViewById(R.id.inst_name);
                about = (TextView) layout.findViewById(R.id.about);
                inst_dp = (CircleImageView) layout.findViewById(R.id.inst_DP);

                done.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();
                    }
                });
                TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
                imgv_icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();
                    }
                });
                Button button_ok = (Button) layout.findViewById(R.id.button_ok);
                button_ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();
                    }
                });

                String dp_half = aboutInst.get(0).get("wu_Photo").toString();

                if (dp_half.trim().length() > 0) {
                    if (dp_half.trim().contains("~")) {
                        dp_half = dp_half.replace("~", "");
                        if (dp_half.trim().contains(" ")) {
                            dp_half = AppConfiguration.PhotoDomain + dp_half.replace(" ", "%20");
                            System.out.println("Pic URL" + dp_half);
                        } else {
                            dp_half = AppConfiguration.PhotoDomain + dp_half;
                            System.out.println("Pic URL" + dp_half);
                        }
                    }
                    imageLoader.displayImage(dp_half, inst_dp);
                }


                tv_appname.setText("Instructor Bio");
                inst_name.setText(aboutInst.get(0).get("wu_InsName").toString());

                about.setText(Html.fromHtml(aboutInst.get(0).get("wu_Bio")));
                about.setMovementMethod(new ScrollingMovementMethod());

            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        AppConfiguration.addCheckedOptions1 = 0;
        AppConfiguration.addCheckedOptions2 = 0;
        AppConfiguration.addCheckedOptions3 = 0;
        AppConfiguration.addCheckedOptions4 = 0;
        AppConfiguration.addCheckedOptions5 = 0;

        if (AppConfiguration.makeUpFlag.equalsIgnoreCase("1") &&
                AppConfiguration.makeup_Clicked == 1) {
            if (fromWhere != null) {
                Intent intent = new Intent(ScheduleLessonFragement2.this, ScheduleMakeupFragment.class);
                startActivity(intent);
                finish();

            }
        } else if (AppConfiguration.makeUpFlag.equalsIgnoreCase("0") &&
                AppConfiguration.makeup_Clicked == 0) {
            if (fromWhere != null) {
                Intent intent = new Intent(ScheduleLessonFragement2.this, ScheduleLessonFragement.class);
                intent.putExtra("fromWhere", "filter");
                startActivity(intent);
                finish();

            }
        } else {
            finish();
            ScheduleLessonFragement2.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

        }
    }



}
