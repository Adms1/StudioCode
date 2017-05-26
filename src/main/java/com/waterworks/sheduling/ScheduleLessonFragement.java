/**
 *
 */
package com.waterworks.sheduling;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import com.waterworks.CancelLessonFragment;
import com.waterworks.CancelSurvey;
import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.LoginActivity;
import com.waterworks.R;
import com.waterworks.adapter.ExpandCollapseAnimation;
import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.TimeZone;


/**
 * @author Harsh Adms
 */
public class ScheduleLessonFragement extends Activity {
    // View rootView;
    View note_upper;
    //    ListPopupWindow lpw_sitelist;
    Button start_date, end_date;//btn_sites
    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
    //        public static ArrayList<HashMap<String, String>> Calendar_Event = new ArrayList<HashMap<String, String>>();
    public static HashMap<String, String> Calendar_Event = new HashMap<String, String>();
    public static int c_yr, c_month, c_day, c_hour, c_min;

    ArrayList<String> siteName = new ArrayList<String>();
    String token, familyID;
    String successGetStudentList;
    RadioGroup enddate_vis;
    RelativeLayout rel_enddate, loadingScreens;
    LinearLayout students_lay, que1, que2, sites_lay, students_check,
            start_redBorder, endDateredBorder, llNote;//, site_Anchor;
    TextView note, tv_instruction_msg, tv_btnsite_cardview;
    String tempLessionId, fromWhere;
    // Calendar
    int startmonth, startyear, startday, endmonth, endyear, endday;
    int mYEAR;
    int mMONTH;
    int mDAY;
    String getstarttime = "false", _StartDate = "", _EndDate = "";
    Boolean isInternetPresent = false;

    ArrayList<HashMap<String, String>> lessonTypesMainListForStudent1 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> lessonTypesMainListForStudent2 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> lessonTypesMainListForStudent3 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> lessonTypesMainListForStudent4 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> lessonTypesMainListForStudent5 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> Temp_Lessons = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> Temp_Lessons_Sigle_Student = new ArrayList<HashMap<String, String>>();
    String successLessonTypes = "";
    ValueAnimator mAnimator;
    // Questions Lay
    RadioGroup que1_grp, que2_grp;
    RadioButton yes1, no_prf1, yes2, no_prf2;

    // Next Button
//    Button btn_next;
    CardView btn_next_card, btn_site;
    Bundle data;

    // Combo Students
    CheckBox st_chk_1, st_chk_2, st_chk_3, st_chk_4, st_chk_5, st_chk_1_2,
            st_chk_2_2, st_chk_3_2, st_chk_4_2, st_chk_5_2;

    Context mContext = this;

    View selected_1, selected_2, selected_3;
    LinearLayout scdl_lsn, scdl_mkp, waitlist;
    TextView txt_1, txt_2, txt_3, noti_count, student_title;
    Bundle fromFilterBundle = null;

    public static final ArrayList<String> sp_lesson = new ArrayList<String>();
    public static final ArrayList<String> grp_lesson = new ArrayList<String>();
    public static final ArrayList<String> strkINT = new ArrayList<String>();
    public static final ArrayList<String> PMadv = new ArrayList<String>();
    public static final ArrayList<String> PMbeg = new ArrayList<String>();
    public static final ArrayList<String> PMint = new ArrayList<String>();
    public static final ArrayList<String> PMbi = new ArrayList<String>();
    public static final ArrayList<String> strkadv = new ArrayList<String>();
    public static final ArrayList<String> strk = new ArrayList<String>();
    public static final ArrayList<String> strkBeg = new ArrayList<String>();
    public static final ArrayList<String> adltBeg = new ArrayList<String>();
    public static final ArrayList<String> adltInt = new ArrayList<String>();
    LinearLayout combo1_lay, combo2_lay;

    ScrollView scrollview;
    boolean running = false;
    Fade mFade, Fadeout;
    Animation animationFadeIn, animationSlideDown;

    Spinner sites_spinner;
    ProgressDialog pd;

    /**
     * RefreshAll the global variables.
     */
    private void RefreshAll() {
        // TODO Auto-generated method stub

        AppConfiguration.pair1_Cmbo1 = "";
        AppConfiguration.pair2_Cmbo1 = "";
        AppConfiguration.pair3_Cmbo1 = "";
        AppConfiguration.pair4_Cmbo1 = "";
        AppConfiguration.pair1_Cmbo2 = "";
        AppConfiguration.pair2_Cmbo2 = "";
        AppConfiguration.pair3_Cmbo2 = "";
        AppConfiguration.pair4_Cmbo2 = "";

        AppConfiguration.pair1_InstrList = "";
        AppConfiguration.pair2_InstrList = "";

        AppConfiguration.pair1_comboValue1 = "0";
        AppConfiguration.pair1_comboValue2 = "0";
        AppConfiguration.pair1_comboValue3 = "0";
        AppConfiguration.pair1_comboValue4 = "0";

        AppConfiguration.pair2_comboValue1 = "0";
        AppConfiguration.pair2_comboValue2 = "0";
        AppConfiguration.pair2_comboValue3 = "0";
        AppConfiguration.pair2_comboValue4 = "0";

        AppConfiguration.comboID.clear();
        AppConfiguration.comboID2.clear();

        AppConfiguration.pair1_DayTime = "";
        AppConfiguration.pair2_DayTime = "";

        AppConfiguration.salStep1LessonID = "";
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    Animation animSlidup, animSlidup_slow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_schedule_lesson);

        fromWhere = getIntent().getStringExtra("fromWhere");
        if (fromWhere == null) {
            clearArray();
            RefreshAll();
        } else {
            fromFilterBundle = new Bundle();
            fromFilterBundle = AppConfiguration.page1_schdl;
//            bundle.putString("Site", btn_sites.getText().toString());
//            bundle.putString("startdate", start_date.getText().toString());
//            bundle.putString("enddate", end_date.getText().toString());

        }
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        if (AppConfiguration.animation) {
            mFade = new Fade(Fade.IN);
            Fadeout = new Fade(Fade.OUT);
        }
        animSlidup = AnimationUtils.loadAnimation(mContext, R.anim.slidup);
        animSlidup_slow = AnimationUtils.loadAnimation(mContext, R.anim.slidup_slow);
        animationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        animationSlideDown = AnimationUtils.loadAnimation(mContext, R.anim.slid_in_down);
//        RefreshAll();
        AppConfiguration.addclass();
        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        AppConfiguration.makeUpFlag = "0";
        AppConfiguration.makeup_Clicked = 0;

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        ((AnimationDrawable) selected_1.getBackground()).start();
        running = true;
        init();
        typeFace();
        InitialRequests();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
        running = false;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    int spin_pos = 0;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//        spinner_selection(spin_pos);
//        RefreshAll();
        if (originalRestore.size() > 0) {
            AppConfiguration.selectedStudentsName.clear();
            AppConfiguration.selectedStudentsName.addAll(originalRestore);
        }
        running = true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        clearArray();
        AppConfiguration.custom_flow = false;
        Intent i = new Intent(mContext, DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        ScheduleLessonFragement.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        finish();

//        ScheduleLessonFragement.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void clearArray() {
        AppConfiguration.selectedStudentsName.clear();
        AppConfiguration.comboID.clear();
        AppConfiguration.comboID2.clear();
        checkedValue.clear();
        checkedValue_2.clear();
        originalRestore.clear();
        temp_st_ID.clear();
        combo1.clear();
        combo2.clear();
        selectedID.clear();
        selectedText.clear();
        sp_lesson.clear();
        grp_lesson.clear();
        strkINT.clear();
        PMadv.clear();
        PMbeg.clear();
        PMbi.clear();
        PMint.clear();
        strkadv.clear();
        strk.clear();
        strkBeg.clear();
        adltBeg.clear();
        adltInt.clear();
        AppConfiguration.reserverForever = "1";
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        System.err.println("Method called : ");
        outState.putString("Site", sites_spinner.getSelectedItem().toString());//btn_sites.getText().toString());
    }

    public void InitialRequests() {
        new SitesListAsyncTask().execute();
    }

    public void spinner_selection(final int pos) {
        Log.d("Spin POS : ", String.valueOf(spin_pos));
        sites_spinner.post(new Runnable() {
            @Override
            public void run() {
                sites_spinner.setSelection(pos, true);
            }
        });
    }

    public void init() {
        final View view = findViewById(R.id.schdl_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);
        RippleView ripple = (RippleView) view.findViewById(R.id.ripple);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfiguration.custom_flow = false;
                Intent intentCheckin = new Intent(mContext, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                onBackPressed();
                /*clearArray();
                finish();
                ScheduleLessonFragement.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ScheduleLessonFragement.this.overridePendingTransition(R.anim.fade_in, R.anim.zoom_in);*/
            }
        });
//        BackButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                clearArray();
//                finish();
//            }
//        });

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        student_title = (TextView) findViewById(R.id.student_title);
        scdl_lsn = (LinearLayout) view.findViewById(R.id.scdl_lsn);
        scdl_mkp = (LinearLayout) view.findViewById(R.id.scdl_mkp);
        waitlist = (LinearLayout) view.findViewById(R.id.waitlist);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);
        combo1_lay = (LinearLayout) findViewById(R.id.combo1_lay);
        combo2_lay = (LinearLayout) findViewById(R.id.combo2_lay);
        loadingScreens = (RelativeLayout) findViewById(R.id.loadingScreens);
        noti_count = (TextView) findViewById(R.id.noti_count);
//        tv_btnsite_cardview= (TextView) findViewById(R.id.tv_btnsite_cardview);
        sites_spinner = (Spinner) findViewById(R.id.sites_spinner);
        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in_menu);
        fadeIn.setDuration(0);
        //sites_spinner.startAnimation(fadeIn);
        sites_spinner.setVisibility(View.VISIBLE);
        sites_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Animation animBounce = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
//                animBounce.setDuration(600);
                //sites_spinner.startAnimation(animBounce);
//                    btn_sites.setText(siteName.get(position));
                if (position > 0) {
                    sites_spinner.setVisibility(View.VISIBLE);
                    spinner_selection(position);
                    spin_pos = position;

                    sites_lay.setBackgroundResource(R.drawable.pure_error_border_white);

                    Log.d("position--", "" + position);
                    AppConfiguration.salStep1SiteID = siteMainList.get(position)
                            .get("SiteID");
                    AppConfiguration.salStep1SiteName = siteMainList.get(position).get(
                            "SiteName");
                    Log.d("siteMainList---id", "" + siteMainList.get(position).get(
                            "SiteID"));
                    Log.d("siteMainList---id2", "" + siteMainList);
                    Log.d("siteMainList---id-po", "" + siteMainList.get(position));
                    Log.d("siteMainList---name", "" + siteMainList.get(position).get(
                            "SiteName"));
                } else {
                    if (sites_lay.getVisibility() == View.VISIBLE) {
                        AppConfiguration.salStep1SiteID = "";
                        AppConfiguration.salStep1SiteName = "";
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        scdl_lsn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                scdl_lsn.setBackgroundResource(R.color.design_change_d2);
                scdl_mkp.setBackgroundResource(R.color.design_change_d2);
                waitlist.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, ScheduleLessonFragement.class);
                startActivity(i);
                ScheduleLessonFragement.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });
        scdl_mkp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scdl_lsn.setBackgroundResource(R.color.design_change_d2);
                scdl_mkp.setBackgroundResource(R.color.design_change_d2);
                waitlist.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, ScheduleMakeupFragment.class);
                startActivity(i);
                ScheduleLessonFragement.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });
        waitlist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scdl_lsn.setBackgroundResource(R.color.design_change_d2);
                scdl_mkp.setBackgroundResource(R.color.design_change_d2);
                waitlist.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, WaitListFragment.class);
                startActivity(i);
                ScheduleLessonFragement.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

//        lpw_sitelist = new ListPopupWindow(mContext);
//        btn_sites = (Button) findViewById(R.id.btn_sites);
        start_date = (Button) findViewById(R.id.start_date);
        end_date = (Button) findViewById(R.id.end_date);
        enddate_vis = (RadioGroup) findViewById(R.id.enddate_vis);
        rel_enddate = (RelativeLayout) findViewById(R.id.rel_enddate);
        students_lay = (LinearLayout) findViewById(R.id.students_lay);
        // Rakesh 20112015............
        start_redBorder = (LinearLayout) findViewById(R.id.start_redBorder);
        endDateredBorder = (LinearLayout) findViewById(R.id.endDateredBorder);
        sites_lay = (LinearLayout) findViewById(R.id.sites_lay);
        que1 = (LinearLayout) findViewById(R.id.que1);
        que2 = (LinearLayout) findViewById(R.id.que2);
        note_upper = (View) findViewById(R.id.note_upper);
        yes1 = (RadioButton) findViewById(R.id.yes1);
        no_prf1 = (RadioButton) findViewById(R.id.no_prf1);
        yes2 = (RadioButton) findViewById(R.id.yes2);
        no_prf2 = (RadioButton) findViewById(R.id.no_prf2);
        students_check = (LinearLayout) findViewById(R.id.students_check);
//        site_Anchor = (LinearLayout) findViewById(R.id.site_Anchor);

        st_chk_1 = (CheckBox) findViewById(R.id.st_chk_1);
        st_chk_2 = (CheckBox) findViewById(R.id.st_chk_2);
        st_chk_3 = (CheckBox) findViewById(R.id.st_chk_3);
        st_chk_4 = (CheckBox) findViewById(R.id.st_chk_4);
        st_chk_5 = (CheckBox) findViewById(R.id.st_chk_5);

        st_chk_1_2 = (CheckBox) findViewById(R.id.st_chk_1_2);
        st_chk_2_2 = (CheckBox) findViewById(R.id.st_chk_2_2);
        st_chk_3_2 = (CheckBox) findViewById(R.id.st_chk_3_2);
        st_chk_4_2 = (CheckBox) findViewById(R.id.st_chk_4_2);
        st_chk_5_2 = (CheckBox) findViewById(R.id.st_chk_5_2);

        CommonCheckboxClicks(st_chk_1_2);
        CommonCheckboxClicks(st_chk_2_2);
        CommonCheckboxClicks(st_chk_3_2);
        CommonCheckboxClicks(st_chk_4_2);
        CommonCheckboxClicks(st_chk_5_2);

        que1_grp = (RadioGroup) findViewById(R.id.que1_grp);
        que2_grp = (RadioGroup) findViewById(R.id.que2_grp);

//        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next_card = (CardView) findViewById(R.id.btn_next_card);
        note = (TextView) findViewById(R.id.note);
        llNote = (LinearLayout) findViewById(R.id.llNote);
        tv_instruction_msg = (TextView) findViewById(R.id.tv_instruction_msg);
        /*note.setText(Html
                .fromHtml(" <font color='red'>Note :</font> If you want to continue past your end date, <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;you must extend your schedule 2 weeks prior to " +
                        "<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;your end date."));*/

        // Rakesh 16112015

        students_check.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                students_check
                        .setBackgroundResource(R.drawable.pure_error_border_white);
            }
        });

        commonCheckboxEvent(st_chk_1);
        commonCheckboxEvent(st_chk_2);
        commonCheckboxEvent(st_chk_3);
        commonCheckboxEvent(st_chk_4);
        commonCheckboxEvent(st_chk_5);

//        st_chk_1.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                que1.setBackgroundResource(R.drawable.pure_error_border_white);
//                que2.setBackgroundResource(R.drawable.pure_error_border_white);
//                students_check
//                        .setBackgroundResource(R.drawable.pure_error_border_white);
//
//                if (checkCHEKCSvalue(Integer.parseInt(combo1_limit), Integer.parseInt(combo2_limit)) == false) {
//
//                } else {
//                    st_chk_1.setChecked(false);
//                    Toast.makeText(mContext, "Student Combo limit Exceeded",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        st_chk_2.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (checkCHEKCSvalue(Integer.parseInt(combo1_limit), Integer.parseInt(combo2_limit)) == false) {
//
//                } else {
//                    st_chk_2.setChecked(false);
//                    Toast.makeText(mContext, "Student Combo limit Exceeded",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        st_chk_3.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (checkCHEKCSvalue(Integer.parseInt(combo1_limit), Integer.parseInt(combo2_limit)) == false) {
//
//                } else {
//                    st_chk_3.setChecked(false);
//                    Toast.makeText(mContext, "Student Combo limit Exceeded",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        st_chk_4.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (checkCHEKCSvalue(Integer.parseInt(combo1_limit), Integer.parseInt(combo2_limit)) == false) {
//
//                } else {
//                    st_chk_4.setChecked(false);
//                    Toast.makeText(mContext, "Student Combo limit Exceeded",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        st_chk_5.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (checkCHEKCSvalue(Integer.parseInt(combo1_limit), Integer.parseInt(combo2_limit)) == false) {
//
//                } else {
//                    st_chk_5.setChecked(false);
//                    Toast.makeText(mContext, "Student Combo limit Exceeded",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        enddate_vis.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.rb1) {
                    AppConfiguration.reserverForever = "1";
                    endDateredBorder.setBackgroundResource(R.drawable.pure_error_border_white);
                    rel_enddate.startAnimation(new ExpandCollapseAnimation(rel_enddate, 500, 1, ScheduleLessonFragement.this));
                    llNote.setVisibility(View.GONE);
                    note.startAnimation(new ExpandCollapseAnimation(note, 500, 1, ScheduleLessonFragement.this));
                    note_upper.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb2) {
                    AppConfiguration.reserverForever = "0";
                    rel_enddate.startAnimation(new ExpandCollapseAnimation(rel_enddate, 500, 0, ScheduleLessonFragement.this));
                    llNote.setVisibility(View.VISIBLE);
                    note.startAnimation(new ExpandCollapseAnimation(note, 500, 0, ScheduleLessonFragement.this));
                    note_upper.setVisibility(View.VISIBLE);
                }
            }
        });
        end_date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Rakesh 20112015............
                endDateredBorder
                        .setBackgroundResource(R.drawable.pure_error_border_white);
                Calendar minDate = Calendar.getInstance();
                // minDate.set(mYEAR,
                // mMONTH - 1,
                // mDAY, 00, 00, 01);
                DatePickerDialog mDialog1 = new DatePickerDialog(mContext,
                        mDateSetListener1, mDAY, mMONTH, mYEAR);
                mDialog1.getDatePicker().setCalendarViewShown(false);
                mDialog1.getDatePicker().setMinDate(minDate.getTimeInMillis());
                mDialog1.show();
            }
        });
        start_date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                start_redBorder
                        .setBackgroundResource(R.drawable.pure_error_border_white);
                Calendar minDate = Calendar.getInstance();
                minDate.set(startyear, startmonth - 1, startday, 00, 00, 01);
                DatePickerDialog mDialog = new DatePickerDialog(mContext,
                        mDateSetListener, startday, startmonth, startyear);
                mDialog.getDatePicker().setCalendarViewShown(false);
                Calendar maxdate = Calendar.getInstance();
                maxdate.set(Calendar.HOUR, 11);
                maxdate.set(endyear, endmonth - 1, endday, 23, 00, 01);

                mDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
                if (System.currentTimeMillis() != minDate.getTimeInMillis()) {
                    mDialog.getDatePicker().setMaxDate(
                            maxdate.getTimeInMillis());
                }
                mDialog.show();
            }
        });
        /*site_Anchor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                lpw_sitelist.show();
            }
        });*/
        que1_grp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                que1.setBackgroundResource(R.drawable.pure_error_border_white);
                clearCheckboxes();
                if (checkedId == R.id.yes1) {
                    makeAcombo();
                    chckStudents();
//                    que2.setVisibility(View.GONE);

//                    scrollview.scrollTo(0,scrollview.getBottom());

                    scrollview.canScrollVertically(scrollview.getBottom());

                    tv_instruction_msg.setText(Html
                            .fromHtml("Please select the students you would like to schedule in the same class."));
                    AppConfiguration.schedulechoices = "1";
                    que2.startAnimation(new ExpandCollapseAnimation(que2, 500, 1, ScheduleLessonFragement.this));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            students_check.setVisibility(View.VISIBLE);
                            students_check.startAnimation(new ExpandCollapseAnimation(students_check, 500, 0, ScheduleLessonFragement.this));
//                            scrollview.scrollTo(0, scrollview.getBottom());
                            scrollview.canScrollVertically(scrollview.getBottom());
                        }
                    }, 500);
                } else {
//                    scrollview.fullScroll(scrollview.FOCUS_DOWN);
//                    scrollview.arrowScroll(scrollview.getBottom());
//                    students_check.setVisibility(View.GONE);
                    AppConfiguration.schedulechoices = "7";
                    students_check.startAnimation(new ExpandCollapseAnimation(students_check, 500, 1, ScheduleLessonFragement.this));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            que2.setVisibility(View.VISIBLE);
                            que2.startAnimation(new ExpandCollapseAnimation(que2, 500, 0, ScheduleLessonFragement.this));
//                            scrollview.arrowScroll(scrollview.getBottom());
                            scrollview.canScrollVertically(scrollview.getBottom());
                        }
                    }, 500);
                }
            }
        });
        que2_grp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                clearCheckboxes();
                if (checkedId == R.id.yes2) {
                    makeAcombo();
                    chckStudents();
//                    scrollview.fullScroll(scrollview.FOCUS_DOWN);
                    scrollview.smoothScrollBy(500, 0);
                    students_check.requestFocus();
                    tv_instruction_msg.setText(Html
                            .fromHtml("Please select the students you would like to schedule with the same instructor."));
//                    students_check.setVisibility(View.VISIBLE);
                    students_check.startAnimation(new ExpandCollapseAnimation(students_check, 500, 0, ScheduleLessonFragement.this));

                } else {
//                    scrollview.fullScroll(scrollview.FOCUS_DOWN);

                    students_check.startAnimation(new ExpandCollapseAnimation(students_check, 500, 1, ScheduleLessonFragement.this));
                    scrollview.smoothScrollBy(500, 0);
//                    students_check.setVisibility(View.GONE);
                }
            }
        });


//        cardView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("CLciked","true");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(mContext,
//                            R.anim.lift_on_touch);
//                    btn_next.setStateListAnimator(stateListAnimator);
//                }
//                clickEvent();
//            }
//        });
        btn_next_card.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scrollview.smoothScrollBy(500, 0);
//                scrollview.fullScroll(scrollview.FOCUS_DOWN);
                isInternetPresent = Utility.isNetworkConnected(ScheduleLessonFragement.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    clickEvent();
                }
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

    public class ViewDialog {

        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_cancel_survey);

            TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
            txtTitle.setText("Alert!");

            EditText txtBodyText = (EditText) dialog.findViewById(R.id.txtBodyText);
//            txtBodyText.setText("When scheduling two students together in the same class, we will only show search results for these two students. " +
//                    "After you have scheduled these students, please schedule separately for any additional students.");
            txtBodyText.setText("Because you are scheduling students together in the same class, we will only offer classes with enough available spots to accommodate these students. " +
                    "After you have scheduled these students, please schedule separately for any additional students.");

            TextView txtClose = (TextView) dialog.findViewById(R.id.txtClose);
            txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            CardView btnYes = (CardView) dialog.findViewById(R.id.btnYes);
//            btnYes.setText("OK");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(mContext, ScheduleLessonFragement2.class);
                            startActivity(i);

                        }
                    }, 100);
//                    clickEvent();
                }
            });

            dialog.show();
        }
    }

    public void clickEvent() {

       /* Calendar_Event.put("Title", "WaterWorks");
        Calendar_Event.put("Desc", "Swim Lesson in " + sites_spinner.getSelectedItem().toString());
        Calendar_Event.put("Location", sites_spinner.getSelectedItem().toString());
        Calendar_Event.put("Email", "info@waterworksswim.com");
        Calendar_Event.put("St1", "");
        Calendar_Event.put("St2", "");
        Calendar_Event.put("St3", "");
        Calendar_Event.put("St4", "");
        Calendar_Event.put("St5", "");
        Calendar_Event.put("St1_time", "");*/

//        importToCalendar();
        removeIT = false;
        flag = false;
        checkInstructor();
        selectedStudents();
        /*if (AppConfiguration.selectedStudentsName.size() > 0) {
            for (int i = 0; i < AppConfiguration.selectedStudentsName.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Title", "WaterWorks");
                hashMap.put("Desc", "Swim Lesson in " + sites_spinner.getSelectedItem().toString());
                hashMap.put("Location", sites_spinner.getSelectedItem().toString());
                hashMap.put("Email", "info@waterworksswim.com");
                hashMap.put("St1", "");
                hashMap.put("St2", "");
                hashMap.put("St3", "");
                hashMap.put("St4", "");
                hashMap.put("St5", "");
                hashMap.put("St1_time", "");
                hashMap.put("St1_name", AppConfiguration.selectedStudentsName.get(0));
                Calendar_Event.putAll(hashMap);
            }*/
//        }
    }

    public void commonCheckboxEvent(final CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkCHEKCSvalue(Integer.parseInt(combo1_limit), Integer.parseInt(combo2_limit)) == false) {

                } else {
                    checkBox.setChecked(false);
                    if (isChecked)
                        Toast.makeText(mContext, "Student Combo limit Exceeded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean sameInstrctr = false;

    public void checkInstructor() {
        if (que2.getVisibility() == View.VISIBLE) {
            if (que2_grp.getCheckedRadioButtonId() == R.id.yes2) {
                sameInstrctr = true;
            } else {
                sameInstrctr = false;
            }
        } else {
            sameInstrctr = false;
        }
    }

    public void selectedStudents() {

        AppConfiguration.selectedStudentsName.clear();
        if (students_lay.getChildCount() > 0) {
            for (int i = 0; i < students_lay.getChildCount(); i++) {
                View view1 = students_lay.getChildAt(i);
                LinearLayout topLay = (LinearLayout) view1;
                View view2 = topLay.getChildAt(0);
                LinearLayout superLay = (LinearLayout) view2;
                View view3 = superLay.getChildAt(0);
                LinearLayout mainLay = (LinearLayout) view3;
                View view4 = mainLay.getChildAt(1);
                if (view4 instanceof TextView) {
                    TextView studentName = (TextView) view4;
                    if (!studentName.getTag().equals("") ||
                            studentName.getTag().toString().equalsIgnoreCase("checked")) {
                        AppConfiguration.selectedStudentsName.add(studentName.getText().toString().trim());
                    }
                    System.out.println("StName : " + studentName.getText().toString()
                            + "   \nStTag : " + studentName.getTag());
                }

                View view5 = mainLay.getChildAt(2);
                HorizontalScrollView temp = (HorizontalScrollView) view5;
                RadioGroup lsnTypes = (RadioGroup) temp.getChildAt(0);
                if (lsnTypes.getChildCount() > 0) {
                    for (int j = 0; j < lsnTypes.getChildCount(); j++) {
                        RadioButton rr = (RadioButton) lsnTypes.getChildAt(j);
                        radioButtonLogic("next", rr);
                    }
                }
            }
        }
        addToarray();
        combineIT();
    }

    Animation.AnimationListener animationSlideInListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
//            students_lay.startAnimation(animationSlideDown);
            rel_enddate.setVisibility(View.INVISIBLE);
            note.setVisibility(View.INVISIBLE);
            note_upper.setVisibility(View.INVISIBLE);
            que1.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            rel_enddate.setVisibility(View.VISIBLE);
            note.setVisibility(View.VISIBLE);
            note_upper.setVisibility(View.VISIBLE);
            que1.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void radioButtonLogic(String forWhich, RadioButton rr) {
        System.out.println(forWhich);
        if (forWhich.equalsIgnoreCase("next")) {
            if (rr.isChecked()) {
                executeIT(rr);
            }
        } else {
            executeIT(rr);
        }
    }

    public void executeIT(RadioButton rr) {

        String tagValue = rr.getTag().toString();
        if (tagValue.contains("|")) {
            String id[] = tagValue.split("\\|");
            for (int l = 0; l < studentList
                    .size(); l++) {
                if (id[0].equals(studentList
                        .get(l)
                        .get("StudentID"))) {
                    System.out.println("ID : "
                            + rr.getId()
                            + " Lsn ID : "
                            + id[1]
                            + " St Name : "
                            + rr.getText()
                            .toString());
                    if (l == 0) {
                        clsID = id[1];
                        clsTxt = rr.getText()
                                .toString();
                        addToCorrectArray(
                                clsID, id[2]);
                    } else if (l == 1) {
                        clsID_2 = id[1];
                        clsTxt_2 = rr.getText()
                                .toString();
                        addToCorrectArray(
                                clsID_2, id[2]);

                    } else if (l == 2) {
                        clsID_3 = id[1];
                        clsTxt_3 = rr.getText()
                                .toString();
                        addToCorrectArray(
                                clsID_3, id[2]);

                    } else if (l == 3) {
                        clsID_4 = id[1];
                        clsTxt_4 = rr.getText()
                                .toString();
                        addToCorrectArray(
                                clsID_4, id[2]);

                    } else if (l == 4) {
                        clsID_5 = id[1];
                        clsTxt_5 = rr.getText()
                                .toString();
                        addToCorrectArray(
                                clsID_5, id[2]);
                    }

                    //Name of the student
                    if (removeIT) {
                        if (selected_st_name
                                .contains(id[2])) {
                            selected_st_name
                                    .remove(id[2]);
                        }

                        //ID of the student
                        selectedStudents.remove(id[0]);

                        if (filterd.containsKey(id[2])) {
                            filterd.remove(id[2]);
                        }

                    } else {
                        if (!selected_st_name
                                .contains(id[2])) {
                            selected_st_name
                                    .add(id[2]);
                        }

                        //ID of the student
                        selectedStudents.add(id[0]);

                        if (!filterd.containsKey(id[2])) {
                            filterd.put(id[2], id[0]);
                        }
                    }


                    HashSet<String> duplicates = new HashSet<String>();
                    duplicates.addAll(selectedStudents);
                    selectedStudents.clear();
                    selectedStudents.addAll(duplicates);
                    for (int k1 = 0; k1 < selectedStudents.size(); k1++) {
                        for (int k2 = k1 + 1; k2 < selectedStudents.size(); k2++) {
                            if (Integer.parseInt(selectedStudents.get(k2)) < Integer.parseInt(selectedStudents.get(k1))) {
                                String tmpvar = selectedStudents.get(k2);
                                selectedStudents.set(k2, selectedStudents.get(k1));
                                selectedStudents.set(k1, tmpvar);
                            }
                        }
                    }

                    duplicates.clear();
                    duplicates.addAll(selected_st_name);
                    selected_st_name.clear();
                    selected_st_name.addAll(duplicates);

                    System.out
                            .println("STName : " + selected_st_name);
                    System.out
                            .println("StID : " + selectedStudents);
                    System.out
                            .println("Filtered : " + filterd);
                }
            }
        }
    }

    public void nextMethod() {

        AppConfiguration.studentsize = AppConfiguration.selectedStudentsName.size();
        AppConfiguration.selectedStudentNameToSchedule = AppConfiguration.Array2String
                (AppConfiguration.selectedStudentsName);
        if (students_check.getVisibility() == View.VISIBLE) {
            if (checkCHEKCSvalue(Integer.parseInt(combo1_limit), Integer.parseInt(combo2_limit)) == false) {
                AppConfiguration.pair1Check = "1";
                if (validation()) {
                    jumpingToOther();
                } else {
                    SelectInstructorDialog();
                }

            } else {
                AppConfiguration.pair1Check = "0";
                SelectInstructorDialog();
                students_check
                        .setBackgroundResource(R.drawable.error_border);
            }
        } else {
            if (validation()) {
                jumpingToOther();
            } else {
                SelectInstructorDialog();
            }
        }
    }

    /**
     * Common Checkbox click method for all checkboxes.
     * Just go through this method and you'll get desire output
     * for all common checkboxes.
     *
     * @param check
     */
    public void CommonCheckboxClicks(final CheckBox check) {
        check.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (checkCHEKCSvalue(Integer.parseInt(combo2_limit)) == false) {
                    AppConfiguration.pair2Check = "1";
                } else {
                    AppConfiguration.pair2Check = "0";
                    check.setChecked(false);
                    Toast.makeText(mContext, "Student Combo limit Exceeded",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Common validation like : startdate
     * EndDate
     * SiteID
     * QuestionsValidation
     *
     * @return
     */
    public boolean validation() {
        boolean validate = false;
        if (questionsValidation()) {
            if (AppConfiguration.salStep1SiteID.toString().trim().length() > 0) {
                if (start_date.getText().toString().trim().length() > 0) {
                    if (enddate_vis.getCheckedRadioButtonId() == R.id.rb2) {
                        if (end_date.getText().toString().trim().length() > 0) {
                            validate = true;
                        } else {
                            // End date
                            validate = false;
                        }
                    } else {
                        // End date
                        validate = true;
                    }
                } else {
                    // startDate
                    validate = false;
                }
            } else {
                // site selection
                validate = false;
            }
        } else {
            validate = false;
        }

        return validate;
    }

    /**
     * Questions validation
     *
     * @return
     */
    public boolean questionsValidation() {
        boolean validate = false;
        if (que1.getVisibility() == View.VISIBLE) {
            if (que1_grp.getCheckedRadioButtonId() != -1) {
                if (que2.getVisibility() == View.VISIBLE) {
                    if (que2_grp.getCheckedRadioButtonId() != -1) {
                        validate = true;
                    } else {
                        validate = true;
                    }
                } else {
                    validate = true;
                }
            } else {
                validate = true;
            }
        } else {
            validate = true;
        }
        return validate;
    }

    /**
     * After the all filters jump from this activity to Another.
     */
    public void jumpingToOther() {

        AppConfiguration.selectedStudentID = "";
        AppConfiguration.salStep1LessonID = "";
        AppConfiguration.salStep1LessonText = "";

        AppConfiguration.selectedStudentID = selectedStudents.toString()
                .replaceAll("\\[", "").replaceAll("\\]", "");
        AppConfiguration.salStep1LessonID = selectedID.toString()
                .replaceAll("\\[", "").replaceAll("\\]", "");
        AppConfiguration.salStep1LessonText = selectedText.toString()
                .replaceAll("\\[", "").replaceAll("\\]", "");
//        AppConfiguration.selectedStudentsName = selected_st_name;

        AppConfiguration.d2_startDate = start_date.getText().toString();
        AppConfiguration.d2_endDate = end_date.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("Site", sites_spinner.getSelectedItem().toString());//btn_sites.getText().toString());
        bundle.putString("startdate", start_date.getText().toString());
        bundle.putString("enddate", end_date.getText().toString());

        AppConfiguration.page1_schdl = bundle;

        if (AppConfiguration.selectedStudentID.trim().length() > 0) {
            if (AppConfiguration.salStep1SiteID.trim().length() > 0) {
                if (yes1.isChecked()) {// || yes2.isChecked()){
                    if (originalRestore.size() > 2) {
                        ViewDialog alert = new ViewDialog();
                        alert.showDialog(ScheduleLessonFragement.this);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(mContext, ScheduleLessonFragement2.class);
                                startActivity(i);

                            }
                        }, 100);
                    }

                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(mContext, ScheduleLessonFragement2.class);
                            startActivity(i);

                        }
                    }, 100);
                }

            } else {
                Toast.makeText(mContext, "Please select Site first.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            SelectInstructorDialog();
            Toast.makeText(mContext, "Please select atleast one student.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Custom Methods
     */
    final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {


        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYEAR = year;
            mMONTH = monthOfYear + 1;
            mDAY = dayOfMonth;
            String d, m, y;
            d = Integer.toString(mDAY);
            m = Integer.toString(mMONTH);
            y = Integer.toString(mYEAR);
            if (mDAY < 10) {
                d = "0" + d;
            }
            if (mMONTH < 10) {
                m = "0" + m;
            }

            c_yr = year;
            c_month = monthOfYear;
            c_day = dayOfMonth;
            c_hour = 8;
            c_min = 00;

            start_date.setText(m + "/" + d + "/" + y);

            Log.v("daySelected2", "" + d);
            Log.v("monthSelected2", "" + m);
            Log.v("yearSelected2", "" + y);
        }
    };

    /**
     * WebServices
     * SiteList
     *
     * @author Harsh Adms
     */
    class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading...");
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
//            if (pd != null) {
//                pd.dismiss();
//            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, siteName);

            sites_spinner.setAdapter(adapter);
            sites_spinner.setSelection(spin_pos, true);
            if (siteName.size() == 1) {
                AppConfiguration.salStep1SiteID = siteMainList.get(0).get("SiteID");
                sites_spinner.setSelection(siteName.indexOf(siteName.get(0)));//.setText(siteMainList.get(0).get("SiteName"));

                Log.d("siteMainList---id", "" + siteMainList.get(0).get("SiteID"));
                Log.d("siteMainList---name", "" + siteMainList.get(0).get("SiteName"));

                sites_lay.setVisibility(View.GONE);
            } else {
                sites_lay.setVisibility(View.VISIBLE);
                //btn_sites.setText("Please select a Location");
//                lpw_sitelist.setAdapter(new ArrayAdapter<String>(mContext, R.layout.edittextpopup, siteName));
//                lpw_sitelist.setAnchorView(site_Anchor);
//                lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
//                lpw_sitelist.setModal(true);
                if (fromWhere != null) {
//                    btn_sites.setText(siteName.get()fromFilterBundle.getString("Site"));
                    sites_spinner.setSelection(siteName.indexOf(fromFilterBundle.getString("Site")));
                    start_date.setText(fromFilterBundle.getString("startdate"));
                    if (AppConfiguration.reserverForever.equalsIgnoreCase("1")) {
                        enddate_vis.check(R.id.rb1);
                    } else {
                        enddate_vis.check(R.id.rb2);
                        end_date.setText(fromFilterBundle.getString("enddate"));
                    }

                }
                /*lpw_sitelist.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        sites_lay.setBackgroundResource(R.drawable.pure_error_border_white);
                        AppConfiguration.salStep1SiteID = siteMainList.get(pos).get("SiteID");
                        AppConfiguration.salStep1SiteName = siteMainList.get(pos).get("SiteName");
                        btn_sites.setText(siteMainList.get(pos).get("SiteName"));

                        lpw_sitelist.dismiss();
                    }
                });*/
            }

            new GetStartTimeRange().execute();
        }
    }

    public void loadSitesList() {
        siteMainList.clear();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(
                AppConfiguration.scheduleALesssionSiteListURL, param);

        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            HashMap<String, String> hashmap;
            hashmap = new HashMap<String, String>();

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("SiteList");
//            siteName.add(0, "Please select location");

            if (jsonMainNode.length() > 1) {
                hashmap.put("SiteID", "0");
                hashmap.put("SiteName", "Please Select a Location");
                siteMainList.add(hashmap);
            }
            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("siteid"));
                hashmap.put("SiteName", jsonChildNode.getString("sitename"));

                siteName.add("" + jsonChildNode.getString("sitename"));

                siteMainList.add(hashmap);
//
            }
//            hashmap = new HashMap<String, String>();
//            hashmap.put("SiteID", "0");
//            hashmap.put("SiteName", "Please Select a Location");
            if (jsonMainNode.length() > 1) {
                siteName.add(0, "Please Select a Location");
            }
            Log.d("siteName---", "" + siteName);
            Log.d("siteName---1", "" + siteName.size());
            Log.d("siteMainList---", "" + siteMainList);
            Log.d("siteMainList---1", "" + siteMainList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Date Range
     *
     * @author Harsh Adms
     */
    private class GetStartTimeRange extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("SiteID", "1");

            String responseString = WebServicesCall.RunScript(
                    AppConfiguration.Schl_Get_ActiveSiteStartEndDate, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                getstarttime = reader.getString("Success");
                if (getstarttime.toString().equals("True")) {
                    JSONArray jsonMainNode = reader
                            .optJSONArray("InstructorList");
                    JSONObject jsonObject = jsonMainNode.getJSONObject(0);
                    _StartDate = jsonObject.getString("StartDate");
                    _EndDate = jsonObject.getString("EndDate");
                } else {

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

            if (getstarttime.toString().equalsIgnoreCase("True")) {
                String[] temp_date = _StartDate.toString().split("T");
                String[] temp = temp_date[0].toString().split("\\-");
                startyear = Integer.parseInt(temp[0].toString());
                startmonth = Integer.parseInt(temp[1].toString());
                startday = Integer.parseInt(temp[2].toString());
                String[] temp_date1 = _EndDate.toString().split("T");
                String[] temp1 = temp_date1[0].toString().split("\\-");
                endyear = Integer.parseInt(temp1[0].toString());
                endmonth = Integer.parseInt(temp1[1].toString());
                endday = Integer.parseInt(temp1[2].toString());
            }
            new GetStudentListAsyncTask().execute();
        }
    }

    final DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYEAR = year;
            mMONTH = monthOfYear + 1;
            mDAY = dayOfMonth;
            String d, m, y;
            d = Integer.toString(mDAY);
            m = Integer.toString(mMONTH);
            y = Integer.toString(mYEAR);
            if (mDAY < 10) {
                d = "0" + d;
            }
            if (mMONTH < 10) {
                m = "0" + m;
            }

            end_date.setText(m + "/" + d + "/" + y);
            Log.v("daySelected2", "" + d);
            Log.v("monthSelected2", "" + m);
            Log.v("yearSelected2", "" + y);

            checkDate();
        }
    };

    public void checkDate() {
        String mStartDate = start_date.getText().toString();
        String mEndDate = end_date.getText().toString();

        if (mStartDate.trim().length() > 0 && mEndDate.trim().length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            Date startDate;
            try {
                Log.v("Start Date--1---", "" + mStartDate);
                Log.v("End Date--1---", "" + mEndDate);

                startDate = sdf.parse(mStartDate);
                Date EndDate = sdf.parse(mEndDate);

                if (startDate.after(EndDate)) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                            mContext);
                    builder1.setMessage("Please select after date from start date.");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    // Rakesh 20112015............
                    end_date.setText("");
                } else {
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "Please select start Date.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<String> temp_st_ID = new ArrayList<String>();

    /**
     * Student list
     *
     * @author Harsh Adms
     */

    private class GetStudentListAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            studentList.clear();
        }

        @Override
        protected Void doInBackground(Void... params1) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);

            String responseString = WebServicesCall.RunScript(
                    AppConfiguration.scheduleALessionStudentListURL, params);
            try {
                JSONObject reader = new JSONObject(responseString);
                successGetStudentList = reader.getString("Success");
                if (successGetStudentList.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("StudentList");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        String StudentID = jsonChildNode.getString("StudentID")
                                .trim();
                        String SFirstName = jsonChildNode.getString(
                                "SFirstName").trim();

                        //						stNamestID.put("SFirstName", );
                        stNamestID.put(jsonChildNode.getString("SFirstName"), jsonChildNode.getString("StudentID"));

                        temp_st_ID.add(StudentID);

                        hashmap.put("StudentID", StudentID);
                        hashmap.put("SFirstName", SFirstName);

                        studentList.add(hashmap);
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

            if (successGetStudentList.toString().equals("True")) {

                new LessonTypesAsyncTask().execute();

            } else {

            }
        }
    }

    boolean removeIT = false;

    /**
     * Lessons types per student
     *
     * @author Harsh Adms
     */
    class LessonTypesAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lessonTypesMainListForStudent1.clear();
            lessonTypesMainListForStudent2.clear();
            lessonTypesMainListForStudent3.clear();
            lessonTypesMainListForStudent4.clear();
            lessonTypesMainListForStudent5.clear();

            AppConfiguration.selectedStudentsName.clear();

            selectedID.clear();
            selectedStudents.clear();
            selectedText.clear();
        }

        @Override
        protected Void doInBackground(Void... params1) {

            String selectedID = temp_st_ID.toString().replaceAll("\\[", "")
                    .replaceAll("\\]", "");

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("studentarray", selectedID);
            params.put("siteid", "1");
            params.put("mup", String.valueOf(AppConfiguration.makeup_Clicked));
            params.put("familyid", familyID);
            String responseString = WebServicesCall.RunScript(
                    AppConfiguration.Schl_Get_LessonListByStudent, params);

            try {
                JSONObject reader = new JSONObject(responseString);
                successLessonTypes = reader.getString("Success");
                if (successLessonTypes.toString().equals("True")) {
                    JSONArray jsonLessonLst_1 = reader
                            .optJSONArray("LessonLst_1");
                    for (int i = 0; i < jsonLessonLst_1.length(); i++) {
                        JSONObject jsonChildNode = jsonLessonLst_1
                                .getJSONObject(i);

                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        hashmap.put("LessonID",
                                jsonChildNode.getString("LessionValue"));
                        hashmap.put("LessonName",
                                jsonChildNode.getString("LessionType"));
                        hashmap.put("Enable", jsonChildNode.getString("Enable"));
                        lessonTypesMainListForStudent1.add(hashmap);
                    }
                    JSONArray jsonLessonLst_2 = reader
                            .optJSONArray("LessonLst_2");
                    if (jsonLessonLst_2.length() > 0) {
                        for (int i = 0; i < jsonLessonLst_2.length(); i++) {
                            JSONObject jsonChildNode = jsonLessonLst_2
                                    .getJSONObject(i);

                            HashMap<String, String> hashmap = new HashMap<String, String>();

                            hashmap.put("LessonID",
                                    jsonChildNode.getString("LessionValue"));
                            hashmap.put("LessonName",
                                    jsonChildNode.getString("LessionType"));
                            hashmap.put("Enable",
                                    jsonChildNode.getString("Enable"));
                            lessonTypesMainListForStudent2.add(hashmap);
                        }
                    }
                    JSONArray jsonLessonLst_3 = reader
                            .optJSONArray("LessonLst_3");
                    if (jsonLessonLst_3.length() > 0) {
                        for (int i = 0; i < jsonLessonLst_3.length(); i++) {
                            JSONObject jsonChildNode = jsonLessonLst_3
                                    .getJSONObject(i);

                            HashMap<String, String> hashmap = new HashMap<String, String>();

                            hashmap.put("LessonID",
                                    jsonChildNode.getString("LessionValue"));
                            hashmap.put("LessonName",
                                    jsonChildNode.getString("LessionType"));
                            hashmap.put("Enable",
                                    jsonChildNode.getString("Enable"));
                            lessonTypesMainListForStudent3.add(hashmap);
                        }
                    }
                    JSONArray jsonLessonLst_4 = reader
                            .optJSONArray("LessonLst_4");
                    if (jsonLessonLst_4.length() > 0) {
                        for (int i = 0; i < jsonLessonLst_4.length(); i++) {
                            JSONObject jsonChildNode = jsonLessonLst_4
                                    .getJSONObject(i);

                            HashMap<String, String> hashmap = new HashMap<String, String>();

                            hashmap.put("LessonID",
                                    jsonChildNode.getString("LessionValue"));
                            hashmap.put("LessonName",
                                    jsonChildNode.getString("LessionType"));
                            hashmap.put("Enable",
                                    jsonChildNode.getString("Enable"));

                            lessonTypesMainListForStudent4.add(hashmap);
                        }
                    }
                    JSONArray jsonLessonLst_5 = reader
                            .optJSONArray("LessonLst_5");
                    if (jsonLessonLst_5.length() > 0) {
                        for (int i = 0; i < jsonLessonLst_5.length(); i++) {
                            JSONObject jsonChildNode = jsonLessonLst_5
                                    .getJSONObject(i);

                            HashMap<String, String> hashmap = new HashMap<String, String>();

                            hashmap.put("LessonID",
                                    jsonChildNode.getString("LessionValue"));
                            hashmap.put("LessonName",
                                    jsonChildNode.getString("LessionType"));
                            hashmap.put("Enable",
                                    jsonChildNode.getString("Enable"));

                            lessonTypesMainListForStudent5.add(hashmap);
                        }
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
//            if (pd != null) {
//                pd.dismiss();
//            }


            if (successLessonTypes.equalsIgnoreCase("True")) {
                for (int i = 0; i < studentList.size(); i++) {
                    // Temp_Lessons_Sigle_Student.clear();
                    Temp_Lessons.clear();
                    LayoutInflater inflater = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(
                            R.layout.d2_custom_studentslay, null);

                    final TextView studentName = (TextView) view
                            .findViewById(R.id.student_name);
                    final RadioGroup lsnTypes = (RadioGroup) view
                            .findViewById(R.id.lsnTypes);
                    final LinearLayout MainLay = (LinearLayout) view
                            .findViewById(R.id.MainLay);
                    final LinearLayout superlay = (LinearLayout) view
                            .findViewById(R.id.super_lay);
                    final TextView track = (TextView) view.findViewById(R.id.track);

                    MainLay.setTag(studentList.get(i).get("SFirstName"));

                    if (i == 0) {
                        Temp_Lessons = lessonTypesMainListForStudent1;
                    } else if (i == 1) {
                        Temp_Lessons = lessonTypesMainListForStudent2;
                    } else if (i == 2) {
                        Temp_Lessons = lessonTypesMainListForStudent3;
                    } else if (i == 3) {
                        Temp_Lessons = lessonTypesMainListForStudent4;
                    } else if (i == 4) {
                        Temp_Lessons = lessonTypesMainListForStudent5;
                    } else {
                        Temp_Lessons = lessonTypesMainListForStudent1;
                    }

                    track.setText("0");
                    if (Temp_Lessons.size() == 0) {
                        superlay.setVisibility(View.GONE);
                    }
                    for (int j = 0; j < Temp_Lessons.size(); j++) {
                        LayoutInflater inflater1 = (LayoutInflater) mContext
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View RadioView = inflater1.inflate(
                                R.layout.d2_custom_lsntypes, null);
                        final RadioButton radioBtn = (RadioButton) RadioView
                                .findViewById(R.id.radio_btn);
                        radioBtn.setText(Temp_Lessons.get(j).get("LessonName"));
                        radioBtn.setId(Integer.parseInt(Temp_Lessons.get(j)
                                .get("LessonID")));
                        //
                        radioBtn.setTag(studentList.get(i).get("StudentID")
                                + "|" + Temp_Lessons.get(j).get("LessonID")
                                + "|" + studentList.get(i).get("SFirstName"));


                        radioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // TODO Auto-generated method stub


                            }
                        });

                        radioBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub

                                if (track.getText().toString().equals(radioBtn.getText().toString())) {
                                    Animation animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                    animFadein.setDuration(200);
                                    radioBtn.startAnimation(animFadein);
                                    studentName.setTag("");
                                    removeIT = true;
                                    clearCheckboxes();
                                    lsnTypes.clearCheck();
                                    track.setText("");

                                    TransitionDrawable transition = (TransitionDrawable) superlay.getBackground();
                                    transition.reverseTransition(200);

//                                    superlay.setBackgroundResource(R.color.student_back);

//                                    MainLay.setBackgroundResource(R.color.student_back);
                                } else {

                                    studentName.setTag("checked");
                                    removeIT = false;
                                    track.setText(radioBtn.getText().toString());

                                    TransitionDrawable transition = (TransitionDrawable) superlay.getBackground();
                                    transition.startTransition(200);

//                                    superlay.setBackgroundResource(R.color.orange);
//                                    MainLay.setBackgroundResource(R.color.orange);
                                }

                                flag = true;

                                students_lay
                                        .setBackgroundResource(R.drawable.pure_error_border_white);
                                View view = (View) v.getParent();// RadioGroup
                                if (v instanceof RadioButton) {
                                    RadioButton rr = (RadioButton) v;
                                    radioButtonLogic("Yo", rr);
                                }

                                if (view instanceof RadioGroup) {
                                    RadioGroup radio = (RadioGroup) view; // HZ
                                    // ScrollView
                                    View HZ = (View) radio.getParent();
                                    if (HZ instanceof HorizontalScrollView) {
                                        HorizontalScrollView HZView = (HorizontalScrollView) HZ;
                                        View Parent = (View) HZView.getParent();
                                        if (Parent instanceof LinearLayout) { // LinearLayout
                                            LinearLayout MainLay = (LinearLayout) Parent;

                                            View top_Lay = (View) MainLay
                                                    .getParent();
                                            if (top_Lay instanceof LinearLayout) {// LinearLayout

                                                if (removeIT) {
                                                    if (AppConfiguration.selectedStudentsName.contains(MainLay.getTag())) {
                                                        AppConfiguration.selectedStudentsName.remove(MainLay.getTag().toString());
                                                    }
                                                } else {
                                                    if (!AppConfiguration.selectedStudentsName.contains(MainLay.getTag())) {
                                                        AppConfiguration.selectedStudentsName.add(MainLay.getTag().toString());
                                                    }
                                                }

                                                HashSet<String> duplicates = new HashSet<String>();
                                                duplicates.addAll(AppConfiguration.selectedStudentsName);
                                                AppConfiguration.selectedStudentsName.clear();
                                                AppConfiguration.selectedStudentsName
                                                        .addAll(duplicates);

                                                // MainLay.setBackgroundResource(getResources().getColor(R.color.orange));
                                                System.out.println("Clicked Student : "
                                                        + MainLay.getTag());
                                            }
                                        }
                                    }
                                }
                            }
                        });

                        lsnTypes.addView(radioBtn);
                    }
                    studentName.setText(studentList.get(i).get("SFirstName"));
                    studentName.setTag("");
                    if (AppConfiguration.animation) {
                        TransitionManager.beginDelayedTransition(students_lay, mFade);
                    }
                    view.startAnimation(animSlidup);
                    students_lay.addView(view);
                }


                if (fromWhere != null) {

                    if (students_lay.getChildCount() > 0) {
                        for (int i = 0; i < students_lay.getChildCount(); i++) {
                            View view1 = students_lay.getChildAt(i);
                            LinearLayout topLay = (LinearLayout) view1;
                            View view2 = topLay.getChildAt(0);
                            LinearLayout superLay = (LinearLayout) view2;
                            View view3 = superLay.getChildAt(0);
                            LinearLayout mainLay = (LinearLayout) view3;
                            View view5 = mainLay.getChildAt(2);
                            HorizontalScrollView temp = (HorizontalScrollView) view5;
                            RadioGroup lsnTypes = (RadioGroup) temp.getChildAt(0);
                            if (lsnTypes.getChildCount() > 0) {
                                for (int j = 0; j < lsnTypes.getChildCount(); j++) {
                                    RadioButton radioBtn = (RadioButton) lsnTypes.getChildAt(j);
                                    String rdbTag = radioBtn.getTag().toString();
                                    String tempNewData[] = rdbTag.split("\\|");
                                    String tempOldData[] = AppConfiguration.selectedStudentID.split(",");
                                    String tempOldLessonID[] = AppConfiguration.salStep1LessonID.split(",");
                                    for (int a = 0; a < tempOldData.length; a++) {
                                        if (tempOldData[a].trim().equalsIgnoreCase(tempNewData[0].trim())) {
                                            if (tempOldLessonID[a].trim().equalsIgnoreCase(tempNewData[1].trim())) {
                                                radioBtn.performClick();
                                            }
                                        }
                                    }
                                    if (que1.getVisibility() == View.VISIBLE) {
                                        if (AppConfiguration.schedulechoices.equalsIgnoreCase("1")) {
                                            que1_grp.check(R.id.yes1);
                                        } else {
                                            que1_grp.check(R.id.no_prf1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (AppConfiguration.animation) {
//                final Animation animationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fadeout);
//                loadingScreens.startAnimation(animationFadeOut);
                loadingScreens.startAnimation(animSlidup);
                loadingScreens.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                loadingScreens.setVisibility(View.GONE);
                                if (AppConfiguration.animation) {
                                    TransitionManager.beginDelayedTransition(scrollview, mFade);
                                }
//                                scrollview.animate()
//                                        .setDuration(300)
//                                        .setListener(null)
//                                        .alpha(1f);

//                                sites_lay.startAnimation(animSlidup);
                                start_redBorder.startAnimation(animSlidup);
                                endDateredBorder.startAnimation(animSlidup);
                                student_title.startAnimation(animSlidup);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

//                TransitionManager.beginDelayedTransition(loadingScreens, Fadeout);
                Log.d("FadingOut", "Done");
//                loadingScreens.setVisibility(View.GONE);
            }


            if (data != null) {
                // RestoreState(data);
            }
            new GetMakeUpCount().execute();
        }
    }

    LinkedHashMap<String, String> filterd = new LinkedHashMap<String, String>();

    String clsID = "", clsTxt = "", clsID_2 = "", clsTxt_2 = "", clsID_3 = "",
            clsTxt_3 = "", clsID_4 = "", clsTxt_4 = "", clsID_5 = "",
            clsTxt_5 = "";

    public void addToarray() {
        selectedID.clear();
        selectedText.clear();

        selectedID.add(clsID);
        selectedID.add(clsID_2);
        selectedID.add(clsID_3);
        selectedID.add(clsID_4);
        selectedID.add(clsID_5);
        selectedText.add(clsTxt);
        selectedText.add(clsTxt_2);
        selectedText.add(clsTxt_3);
        selectedText.add(clsTxt_4);
        selectedText.add(clsTxt_5);

        //Remove null value from the array.
        selectedID.removeAll(Collections.singleton(""));
        selectedText.removeAll(Collections.singleton(""));
    }

    /**
     * Add clicked lessons to its type. And make queGroup Visible.
     *
     * @param clsID
     * @param stName
     */
    boolean flag = true;

    public void addToCorrectArray(String clsID, String stName) {
        if (flag) {
            doSomeStuff();
        }
        if (removeIT) {
            if (sp_lesson.contains(stName)) {
                sp_lesson.remove(stName);
            }

            if (grp_lesson.contains(stName)) {
                grp_lesson.remove(stName);
            }

            if (strkINT.contains(stName)) {
                strkINT.remove(stName);
            }

            if (PMadv.contains(stName)) {
                PMadv.remove(stName);
            }
            if (PMbeg.contains(stName)) {
                PMbeg.remove(stName);
            }
            if (PMbi.contains(stName)) {
                PMbi.remove(stName);
            }
            if (PMint.contains(stName)) {
                PMint.remove(stName);
            }
            if (strkadv.contains(stName)) {
                strkadv.remove(stName);
            }
            if (strk.contains(stName)) {
                strk.remove(stName);
            }
            if (strkBeg.contains(stName)) {
                strkBeg.remove(stName);
            }
            if (adltBeg.contains(stName)) {
                adltBeg.remove(stName);
            }
            if (adltInt.contains(stName)) {
                adltInt.remove(stName);
            }

        } else {
            if (clsID.equals("2")) {//SemiPrivate
                if (!sp_lesson.contains(stName)) {
                    sp_lesson.add(stName);
                    //RemoveFromOther
                    removeFromOther("2", stName);
                }
            } else if (clsID.equals("3")) {//Group
                if (!grp_lesson.contains(stName)) {
                    grp_lesson.add(stName);
                    //RemoveFromOther
                    removeFromOther("3", stName);
                }
            } else if (clsID.equals("5")) {//Stroke Clinic int
                if (!strkINT.contains(stName)) {
                    strkINT.add(stName);
                    //RemoveFromOther
                    removeFromOther("5", stName);
                }
            } else if (clsID.equals("6")) {//PM adv
                if (!PMadv.contains(stName)) {
                    PMadv.add(stName);
                    //RemoveFromOther
                    removeFromOther("6", stName);
                }
            } else if (clsID.equals("7")) {//PM Beg
                if (!PMbeg.contains(stName)) {
                    PMbeg.add(stName);
                    //RemoveFromOther
                    removeFromOther("7", stName);
                }
            } else if (clsID.equals("8")) {//PM Int
                if (!PMint.contains(stName)) {
                    PMint.add(stName);
                    //RemoveFromOther
                    removeFromOther("8", stName);
                }
            } else if (clsID.equals("9")) {//PM BI
                if (!PMbi.contains(stName)) {
                    PMbi.add(stName);
                    //RemoveFromOther
                    removeFromOther("9", stName);
                }
            } else if (clsID.equals("10")) {//Stroke Clinic adv
                if (!strkadv.contains(stName)) {
                    strkadv.add(stName);
                    //RemoveFromOther
                    removeFromOther("10", stName);
                }
            } else if (clsID.equals("13")) {//Stroke Clinic
                if (!strk.contains(stName)) {
                    strk.add(stName);
                    //RemoveFromOther
                    removeFromOther("13", stName);
                }
            } else if (clsID.equals("15")) {//Stroke Clinic Beg
                if (!strkBeg.contains(stName)) {
                    strkBeg.add(stName);
                    //RemoveFromOther
                    removeFromOther("15", stName);
                }
            } else if (clsID.equals("21")) {//Adult Stroke Clinic Beg
                if (!adltBeg.contains(stName)) {
                    adltBeg.add(stName);
                    //RemoveFromOther
                    removeFromOther("21", stName);
                }
            } else if (clsID.equals("22")) {//Adult Stroke Clinic int
                if (!adltInt.contains(stName)) {
                    adltInt.add(stName);
                    //RemoveFromOther
                    removeFromOther("22", stName);
                }
            } else {
                if (sp_lesson.contains(stName)) {
                    sp_lesson.remove(stName);
                }
                if (grp_lesson.contains(stName)) {
                    grp_lesson.remove(stName);
                }

                if (strkINT.contains(stName)) {
                    strkINT.remove(stName);
                }
                if (PMadv.contains(stName)) {
                    PMadv.remove(stName);
                }
                if (PMbeg.contains(stName)) {
                    PMbeg.remove(stName);
                }
                if (PMbi.contains(stName)) {
                    PMbi.remove(stName);
                }
                if (PMint.contains(stName)) {
                    PMint.remove(stName);
                }
                if (strkadv.contains(stName)) {
                    strkadv.remove(stName);
                }
                if (strk.contains(stName)) {
                    strk.remove(stName);
                }
                if (strkBeg.contains(stName)) {
                    strkBeg.remove(stName);
                }
                if (adltBeg.contains(stName)) {
                    adltBeg.remove(stName);
                }
                if (adltInt.contains(stName)) {
                    adltInt.remove(stName);
                }

            }
        }

        if (sp_lesson.size() > 1
                || grp_lesson.size() > 1
                || strkINT.size() > 1
                || PMadv.size() > 1
                || PMbeg.size() > 1
                || PMbi.size() > 1
                || PMint.size() > 1
                || strk.size() > 1
                || strkadv.size() > 1
                || strkBeg.size() > 1
                || adltBeg.size() > 1
                || adltInt.size() > 1) {
            AppConfiguration.reserverForever = "0";


            Log.d("Expand---", "Expand");
            que1.startAnimation(new ExpandCollapseAnimation(que1, 500, 0, ScheduleLessonFragement.this));
//            que2.startAnimation(new ExpandCollapseAnimation(que1, 500, 0, ScheduleLessonFragement.this));
//            que1_grp.startAnimation(new ExpandCollapseAnimation(que1, 500, 0, ScheduleLessonFragement.this));


        } else {
            try {
//                que1.startAnimation(new ExpandCollapseAnimation(que1, 500, 1, ScheduleLessonFragement.this));
                doSomeStuff();
                Log.d("collapse---", "collapse");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println("SpLessons:" + sp_lesson);
        System.out.println("GrpLessons:" + grp_lesson);
    }

    public void removeFromOther(String clsID, String stName) {
        if (sp_lesson.contains(stName)
                && !clsID.equals("2")) {
            sp_lesson.remove(stName);
        }

        if (grp_lesson.contains(stName)
                && !clsID.equals("3")) {
            grp_lesson.remove(stName);
        }

        if (strkINT.contains(stName)
                && !clsID.equals("5")) {
            strkINT.remove(stName);
        }
        if (PMadv.contains(stName)
                && !clsID.equals("6")) {
            PMadv.remove(stName);
        }
        if (PMbeg.contains(stName)
                && !clsID.equals("7")) {
            PMbeg.remove(stName);
        }
        if (PMbi.contains(stName)
                && !clsID.equals("9")) {
            PMbi.remove(stName);
        }
        if (PMint.contains(stName)
                && !clsID.equals("8")) {
            PMint.remove(stName);
        }
        if (strkadv.contains(stName)
                && !clsID.equals("10")) {
            strkadv.remove(stName);
        }
        if (strk.contains(stName)
                && !clsID.equals("13")) {
            strk.remove(stName);
        }
        if (strkBeg.contains(stName)
                && !clsID.equals("15")) {
            strkBeg.remove(stName);
        }
        if (adltBeg.contains(stName)
                && !clsID.equals("21")) {
            adltBeg.remove(stName);
        }
        if (adltInt.contains(stName)
                && !clsID.equals("22")) {
            adltInt.remove(stName);
        }
    }

    /**
     * As per name suggest it'll generate the combo.
     */
    ArrayList<String> combo1 = new ArrayList<String>();
    ArrayList<String> combo2 = new ArrayList<String>();
    String combo1_limit = "0";
    String combo2_limit = "0";

    private void makeAcombo() {
        // TODO Auto-generated method stub
        combo1.clear();
        combo2.clear();
        String current = "0", current2 = "0";

        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> temp1 = new ArrayList<String>();
        if (sp_lesson.size() > 1) {
            current = "2";
            temp.addAll(sp_lesson);
            AppConfiguration.pair1lessontype = "2";
            AppConfiguration.pair1lessontype_txt = "Semi-Private";
        }
        if (grp_lesson.size() > 1) {
            if (temp.size() > 0) {
                current2 = "3";
                AppConfiguration.pair2lessontype = "3";
                AppConfiguration.pair2lessontype_txt = "Group";
                temp1.addAll(grp_lesson);
            } else {
                current = "3";
                AppConfiguration.pair1lessontype = "3";
                AppConfiguration.pair1lessontype_txt = "Group";
                temp.addAll(grp_lesson);
            }
        }
        if (strkINT.size() > 1) {
            if (temp.size() > 0) {
                current2 = "5";
                AppConfiguration.pair2lessontype = "5";
                AppConfiguration.pair2lessontype_txt = "Stroke Clinic Int";
                temp1.addAll(strkINT);
            } else {
                current = "5";
                AppConfiguration.pair1lessontype = "5";
                AppConfiguration.pair1lessontype_txt = "Stroke Clinic Int";
                temp.addAll(strkINT);
            }
        }
        if (PMadv.size() > 1) {
            if (temp.size() > 0) {
                current2 = "6";
                AppConfiguration.pair2lessontype = "6";
                AppConfiguration.pair2lessontype_txt = "P&M Adv";
                temp1.addAll(PMadv);
            } else {
                current = "6";
                AppConfiguration.pair1lessontype = "6";
                AppConfiguration.pair1lessontype_txt = "P&M Adv";
                temp.addAll(PMadv);
            }
        }
        if (PMbeg.size() > 1) {
            if (temp.size() > 0) {
                current2 = "7";
                AppConfiguration.pair2lessontype = "7";
                AppConfiguration.pair2lessontype_txt = "P&M Beg";
                temp1.addAll(PMbeg);
            } else {
                current = "7";
                AppConfiguration.pair1lessontype = "7";
                AppConfiguration.pair1lessontype_txt = "P&M Beg";
                temp.addAll(PMbeg);
            }
        }
        if (PMint.size() > 1) {
            if (temp.size() > 0) {
                current2 = "8";
                AppConfiguration.pair2lessontype = "8";
                AppConfiguration.pair2lessontype_txt = "P&M Int";
                temp1.addAll(PMint);
            } else {
                current = "8";
                AppConfiguration.pair1lessontype = "8";
                AppConfiguration.pair1lessontype_txt = "P&M Int";
                temp.addAll(PMint);
            }
        }
        if (PMbi.size() > 1) {
            if (temp.size() > 0) {
                current2 = "9";
                AppConfiguration.pair2lessontype = "9";
                AppConfiguration.pair2lessontype_txt = "P&M B/I";
                temp1.addAll(PMbi);
            } else {
                current = "9";
                AppConfiguration.pair1lessontype = "9";
                AppConfiguration.pair1lessontype_txt = "P&M B/I";
                temp.addAll(PMbi);
            }
        }
        if (strkadv.size() > 1) {
            if (temp.size() > 0) {
                current2 = "10";
                AppConfiguration.pair2lessontype = "10";
                AppConfiguration.pair2lessontype_txt = "Stroke Clinic Adv";
                temp1.addAll(strkadv);
            } else {
                current = "10";
                AppConfiguration.pair1lessontype = "10";
                AppConfiguration.pair1lessontype_txt = "Stroke Clinic Adv";
                temp.addAll(strkadv);
            }
        }
        if (strk.size() > 1) {
            if (temp.size() > 0) {
                current2 = "13";
                AppConfiguration.pair2lessontype = "13";
                AppConfiguration.pair2lessontype_txt = "Stroke Clinic";
                temp1.addAll(strk);
            } else {
                current = "13";
                AppConfiguration.pair1lessontype = "13";
                AppConfiguration.pair1lessontype_txt = "Stroke Clinic";
                temp.addAll(strk);
            }
        }
        if (strkBeg.size() > 1) {
            if (temp.size() > 0) {
                current2 = "15";
                AppConfiguration.pair2lessontype = "15";
                AppConfiguration.pair2lessontype_txt = "Stroke Clinic Beg";
                temp1.addAll(strkBeg);
            } else {
                current = "15";
                AppConfiguration.pair1lessontype = "15";
                AppConfiguration.pair1lessontype_txt = "Stroke Clinic Beg";
                temp.addAll(strkBeg);
            }
        }
        if (adltBeg.size() > 1) {
            if (temp.size() > 0) {
                current2 = "21";
                AppConfiguration.pair2lessontype = "21";
                AppConfiguration.pair2lessontype_txt = "Adult Stroke Clinic Beg";
                temp1.addAll(adltBeg);
            } else {
                current = "21";
                AppConfiguration.pair1lessontype = "21";
                AppConfiguration.pair1lessontype_txt = "Adult Stroke Clinic Beg";
                temp.addAll(adltBeg);
            }
        }
        if (adltInt.size() > 1) {
            if (temp.size() > 0) {
                current2 = "22";
                AppConfiguration.pair2lessontype = "22";
                AppConfiguration.pair2lessontype_txt = "Adult Stroke Clinic Int";
                temp1.addAll(adltInt);
            } else {
                current = "22";
                AppConfiguration.pair1lessontype = "22";
                AppConfiguration.pair1lessontype_txt = "Adult Stroke Clinic Int";
                temp.addAll(adltInt);
            }
        }


        if (temp.size() > 1) {
            combo1 = temp;

            setComboLimit(current, "1");
        }

        if (temp1.size() > 1) {
            if (combo1.size() == 0) {
                combo1 = temp1;
                setComboLimit(current, "1");
            } else {
                setComboLimit(current2, "2");
                combo2 = temp1;
            }
        }

    }

    /**
     * This method is use to reassign some values when
     * user click on lesson types.
     */
    public void doSomeStuff() {
        clearCheckboxes();
//        que1_grp.clearCheck();
//        que2_grp.clearCheck();
//        AppConfiguration.reserverForever = "1";
//        que1.startAnimation(new ExpandCollapseAnimation(que1, 500, 1, ScheduleLessonFragement.this));//collapse
//        que2.startAnimation(new ExpandCollapseAnimation(que1, 500, 1, ScheduleLessonFragement.this));
//        que1_grp.startAnimation(new ExpandCollapseAnimation(que1, 500, 1, ScheduleLessonFragement.this));

        if (que2.getVisibility() == View.VISIBLE) {
            que2.startAnimation(new ExpandCollapseAnimation(que2, 500, 1, ScheduleLessonFragement.this));
        } else {
            que2.setVisibility(View.GONE);
            yes2.setChecked(false);
            no_prf2.setChecked(false);
        }
        if (que1.getVisibility() == View.VISIBLE) {
            que1.startAnimation(new ExpandCollapseAnimation(que1, 500, 1, ScheduleLessonFragement.this));
        } else {
            que1.setVisibility(View.GONE);
            yes1.setChecked(false);
            no_prf1.setChecked(false);
        }
        if (students_check.getVisibility() == View.VISIBLE) {
            students_check.startAnimation(new ExpandCollapseAnimation(students_check, 500, 1, ScheduleLessonFragement.this));
        } else {
            students_check.setVisibility(View.GONE);
        }

    }

    String limit = "";

    /**
     * ComboLimit can be set using this method.
     */
    private void setComboLimit(String current, String which) {
        // TODO Auto-generated method stub

        //For combo1
        if (which.equals("1")) {
            if (current.equals("2")) {//SemiPrivate
                combo1_limit = "2";
            } else if (current.equals("3")) {//Group
                combo1_limit = "4";
            } else if (current.equals("5")) {//Stroke Clinic Int
                combo1_limit = "12";
            } else if (current.equals("6")) {//P&M adv
                combo1_limit = "3";
            } else if (current.equals("7")
                    || current.equals("8")
                    || current.equals("9")) {//P&M Int,B/I,Beg
                combo1_limit = "6";
            } else if (current.equals("10")) {//Stroke Clinic adv
                combo1_limit = "12";
            } else if (current.equals("13")) {//Stroke Clinic
                combo1_limit = "12";
            } else if (current.equals("15")) {//Stroke Clinic Beg
                combo1_limit = "6";
            } else if (current.equals("21")) {//Adult Stroke Clinic Beg
                combo1_limit = "6";
            } else if (current.equals("22")) {//Adult Stroke Clinic Int
                combo1_limit = "6";
            }
        }

        //For combo2
        if (which.equals("2")) {
            if (current.equals("2")) {//SemiPrivate
                combo2_limit = "2";
            } else if (current.equals("3")) {//Group
                combo2_limit = "4";
            } else if (current.equals("5")) {//Stroke Clinic Int
                combo2_limit = "12";
            } else if (current.equals("6")) {//P&M adv
                combo2_limit = "3";
            } else if (current.equals("7")
                    || current.equals("8")
                    || current.equals("9")) {//P&M Int,B/I,Beg
                combo2_limit = "6";
            } else if (current.equals("10")) {//Stroke Clinic adv
                combo2_limit = "12";
            } else if (current.equals("13")) {//Stroke Clinic
                combo2_limit = "12";
            } else if (current.equals("15")) {//Stroke Clinic Beg
                combo2_limit = "6";
            } else if (current.equals("21")) {//Adult Stroke Clinic Beg
                combo2_limit = "6";
            } else if (current.equals("22")) {//Adult Stroke Clinic Int
                combo2_limit = "6";
            }
        }
    }

    /**
     * Clear the checks and reassign the null value to
     * CheckBox.
     */
    public void clearCheckboxes() {
        for (int i = 0; i < combo1_lay.getChildCount(); i++) {
            View view = combo1_lay.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox check = (CheckBox) view;
                check.setChecked(false);
                check.setVisibility(View.GONE);
                check.setText("");
            }
        }

        for (int i = 0; i < combo2_lay.getChildCount(); i++) {
            View view = combo2_lay.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox check = (CheckBox) view;
                check.setChecked(false);
                check.setVisibility(View.GONE);
                check.setText("");
            }
        }
    }

    public final static ArrayList<String> checkedValue = new ArrayList<String>();
    public final static ArrayList<String> checkedValue_2 = new ArrayList<String>();
    public final static ArrayList<String> originalRestore = new ArrayList<String>();

    /**
     * Get the checked CheckBox value from Layout
     *
     * @param limit
     * @return
     */
    public boolean checkCHEKCSvalue(int limit, int limit2) {
        int count = 0;
        //		int count_2=0;
        checkedValue.clear();
        checkedValue_2.clear();
        //		checkedValue_2.clear();

        for (int i = 0; i < combo1_lay.getChildCount(); i++) {
            View view = combo1_lay.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox check = (CheckBox) view;
                if (check.isChecked()) {
                    checkedValue.add(check.getTag().toString());
                    count++;
                }
            }
        }

        //1st Validation
        boolean wrong = false;
        if (limit < count) {
            wrong = true;
        }

        if (count == 0) {
            wrong = true;
        }

        if (wrong) {
            checkedValue.clear();
            // students_check.setBackground(getResources().getDrawable(R.drawable.error_border));
        } else {

        }

        if (checkCHEKCSvalue(limit2)) ;
        System.out.println("Checked Value = " + checkedValue);
        return wrong;
    }

    public boolean checkCHEKCSvalue(int limit2) {
        int count_2 = 0;
        checkedValue_2.clear();

        boolean wrong = false;
        if (combo2_lay.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < combo2_lay.getChildCount(); i++) {
                View view = combo2_lay.getChildAt(i);
                if (view instanceof CheckBox) {
                    CheckBox check = (CheckBox) view;
                    if (check.isChecked()) {
                        checkedValue_2.add(check.getTag().toString());
                        count_2++;
                    }
                }
            }

            //2nd validation
            if (limit2 < count_2) {
                wrong = true;
            }

            if (count_2 == 0) {
                wrong = true;
            }

            if (wrong) {
                checkedValue_2.clear();
                // students_check.setBackground(getResources().getDrawable(R.drawable.error_border));
            } else {
            }
        }
        return wrong;
    }

    public void checkQueGroup() {
        if (que1.getVisibility() == View.VISIBLE ||
                que2.getVisibility() == View.VISIBLE) {
            if (que1_grp.getCheckedRadioButtonId() == R.id.no_prf1
                    && que2_grp.getCheckedRadioButtonId() == R.id.no_prf2) {
                checkedValue.clear();
                checkedValue_2.clear();
            } else if (que1_grp.getCheckedRadioButtonId() == -1) {
                checkedValue.clear();
                checkedValue_2.clear();
            }
        } else {
            checkedValue.clear();
            checkedValue_2.clear();
        }
    }

    /**
     * This method generate the combo users name
     * and seprate them with "&" and add them to
     * first position of array.
     * <p/>
     * And assigning values to some useful Variables.
     */
    public void


    combineIT() {
        AppConfiguration.comboID.clear();
        AppConfiguration.comboID2.clear();

        checkQueGroup();
        ArrayList<String> temp_lessonsID = new ArrayList<String>();
        ArrayList<String> temp_lessonstxt = new ArrayList<String>();

        checkedValue.clear();
        for (int i = 0; i < combo1_lay.getChildCount(); i++) {
            View view = combo1_lay.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox check = (CheckBox) view;
                if (check.isChecked()) {
                    checkedValue.add(check.getTag().toString());
                }
            }
        }

        if (checkedValue.size() > 0) {
            ArrayList<String> temp_array = new ArrayList<String>();

            temp_lessonsID.addAll(selectedID);
            temp_lessonstxt.addAll(selectedText);

            temp_array.addAll(selected_st_name);
            String combinedSt = "";

            for (int i = 0; i < selected_st_name.size(); i++) {
                if (selected_st_name.get(i).toString().contains("&")) {
                    temp_array.remove(i);
                }
            }

            for (int i = 0; i < checkedValue.size(); i++) {
                String temp = "";
                if (i == 0) {
                    if (checkedValue.get(i).contains(" ")) {
                        temp = checkedValue.get(i).split("\\s+")[0];
                    } else {
                        temp = checkedValue.get(i).split("")[0];
                    }
                    combinedSt = temp;
                    temp_array.remove(checkedValue.get(i));
                    if (!AppConfiguration.comboID.contains(filterd.get(checkedValue.get(i)))) {
                        AppConfiguration.comboID.add(filterd.get(checkedValue.get(i)));
                    }
                } else {
                    if (!sameInstrctr) {
                        temp_lessonsID.remove(AppConfiguration.pair1lessontype);
                        temp_lessonstxt.remove(AppConfiguration.pair1lessontype_txt);
                    }

                    if (checkedValue.get(i).contains(" ")) {
                        temp = checkedValue.get(i).split("\\s+")[0];
                    } else {
                        temp = checkedValue.get(i).split("")[0];
                    }
                    temp_array.remove(checkedValue.get(i));
                    combinedSt = combinedSt + "&" + temp;
                    if (!AppConfiguration.comboID.contains(filterd.get(checkedValue.get(i)))) {
                        AppConfiguration.comboID.add(filterd.get(checkedValue.get(i)));
                    }
                }

                if (!sameInstrctr) {
                    if (i == 0) {
                        AppConfiguration.pair1_Cmbo1 = checkedValue.get(i);
                        AppConfiguration.pair1_comboValue1 = stNamestID.get(checkedValue.get(i));
                    } else if (i == 1) {
                        clsID_2 = "";
                        clsTxt_2 = "";
                        AppConfiguration.pair2_Cmbo1 = checkedValue.get(i);
                        AppConfiguration.pair1_comboValue2 = stNamestID.get(checkedValue.get(i));
                    } else if (i == 2) {
                        clsID_3 = "";
                        clsTxt_3 = "";
                        AppConfiguration.pair3_Cmbo1 = checkedValue.get(i);
                        AppConfiguration.pair1_comboValue3 = stNamestID.get(checkedValue.get(i));
                    } else if (i == 3) {
                        clsID_4 = "";
                        clsTxt_4 = "";
                        AppConfiguration.pair4_Cmbo1 = checkedValue.get(i);
                        AppConfiguration.pair1_comboValue4 = stNamestID.get(checkedValue.get(i));
                    }
                }
            }

            System.out.println("ComboID: " + AppConfiguration.comboID);

            if (combinedSt.trim().length() > 0 && !temp_array.contains(combinedSt)) {
                temp_array.clear();
                temp_array.add(0, combinedSt);
            }
            System.out.println("After array : " + temp_array);

            originalRestore.clear();
            originalRestore.addAll(AppConfiguration.selectedStudentsName);

            AppConfiguration.selectedStudentsName.clear();
            AppConfiguration.selectedStudentsName.addAll(temp_array);
            System.out.println("Combined List:" + temp_array.toString());
        }

        if (checkedValue_2.size() > 0) {
            ArrayList<String> temp_array = new ArrayList<String>();

            temp_array.addAll(AppConfiguration.selectedStudentsName);
            String combinedSt = "";

            for (int i = 0; i < checkedValue_2.size(); i++) {
                String temp = "";
                if (i == 0) {
                    if (checkedValue_2.get(i).contains(" ")) {
                        temp = checkedValue_2.get(i).split("\\s+")[0];
                    } else {
                        temp = checkedValue_2.get(i).split("")[0];
                    }
                    combinedSt = temp;
                    temp_array.remove(checkedValue_2.get(i));
                    if (!AppConfiguration.comboID2.contains(filterd.get(checkedValue_2.get(i)))) {
                        AppConfiguration.comboID2.add(filterd.get(checkedValue_2.get(i)));
                    }
                } else {
                    if (!sameInstrctr) {
                        temp_lessonsID.remove(AppConfiguration.pair2lessontype);
                        temp_lessonstxt.remove(AppConfiguration.pair2lessontype_txt);
                    }
                    if (checkedValue_2.get(i).contains(" ")) {
                        temp = checkedValue_2.get(i).split("\\s+")[0];
                    } else {
                        temp = checkedValue_2.get(i).split("")[0];
                    }
                    temp_array.remove(checkedValue_2.get(i));
                    combinedSt = combinedSt + "&" + temp;
                    if (!AppConfiguration.comboID2.contains(filterd.get(checkedValue_2.get(i)))) {
                        AppConfiguration.comboID2.add(filterd.get(checkedValue_2.get(i)));
                    }
                }

                if (!sameInstrctr) {
                    if (i == 0) {
                        AppConfiguration.pair1_Cmbo2 = checkedValue_2.get(i);
                        AppConfiguration.pair2_comboValue1 = stNamestID.get(checkedValue_2.get(i));
                    } else if (i == 1) {
                        AppConfiguration.pair2_Cmbo2 = checkedValue_2.get(i);
                        AppConfiguration.pair2_comboValue2 = stNamestID.get(checkedValue_2.get(i));
                    } else if (i == 2) {
                        AppConfiguration.pair3_Cmbo2 = checkedValue_2.get(i);
                        AppConfiguration.pair2_comboValue3 = stNamestID.get(checkedValue_2.get(i));
                    } else if (i == 3) {
                        AppConfiguration.pair4_Cmbo2 = checkedValue_2.get(i);
                        AppConfiguration.pair2_comboValue4 = stNamestID.get(checkedValue_2.get(i));
                    }
                }
            }

            System.out.println("ComboID: " + AppConfiguration.comboID2);

            if (combinedSt.trim().length() > 0
                    && !temp_array.contains(combinedSt)) {
                temp_array.add(1, combinedSt);
            }
            System.out.println("After array : " + temp_array);

            AppConfiguration.selectedStudentsName.clear();
            AppConfiguration.selectedStudentsName.addAll(temp_array);
            System.out.println("Combined List:" + temp_array.toString());
        }

        if (checkedValue.size() > 0
                || checkedValue_2.size() > 0) {

            if (!sameInstrctr) {
                selectedID.clear();
                selectedText.clear();

                selectedID.addAll(temp_lessonsID);
                selectedText.addAll(temp_lessonstxt);

                RearrangeClsID();
            }
        }

        if (!sameInstrctr) {
            Log.d("Refiltering", "True");
            ReFilterIDS();
        } else {
            AppConfiguration.comboID.clear();
            AppConfiguration.comboID2.clear();
            Log.d("Direct Next", "True");
            nextMethod();
        }
    }

    /**
     * Rearrange the LsnID's
     */
    private void RearrangeClsID() {
        // TODO Auto-generated method stub
        ArrayList<String> temp_ID = new ArrayList<String>();
        ArrayList<String> temp_TXT = new ArrayList<String>();
        temp_ID.addAll(selectedID);
        temp_TXT.addAll(selectedText);

        for (int i = 0; i < Integer.parseInt(combo1_limit); i++) {
            if (i != 0) {
                temp_ID.remove(AppConfiguration.pair1lessontype);
                temp_TXT.remove(AppConfiguration.pair1lessontype_txt);
            }
        }

        for (int i = 0; i < Integer.parseInt(combo2_limit); i++) {
            if (i != 0) {
                temp_ID.remove(AppConfiguration.pair2lessontype);
                temp_TXT.remove(AppConfiguration.pair2lessontype_txt);
            }
        }

        temp_ID.add(0, AppConfiguration.pair1lessontype);
        temp_TXT.add(0, AppConfiguration.pair1lessontype_txt);

        temp_ID.add(1, AppConfiguration.pair2lessontype);
        temp_TXT.add(1, AppConfiguration.pair2lessontype_txt);

        temp_ID.removeAll(Collections.singleton(""));
        temp_TXT.removeAll(Collections.singleton(""));

        selectedID.clear();
        selectedText.clear();

        selectedID.addAll(temp_ID);
        selectedText.addAll(temp_TXT);

    }

    /**
     * This method use to filter the Combo ID's
     * it'll remove the combo users ID from main ID array
     * except 1st user ID.
     */
    public void ReFilterIDS() {
        boolean found = false;
        ArrayList<String> tempID = new ArrayList<String>();
        tempID.addAll(selectedStudents);
        for (int i = 0; i < AppConfiguration.comboID.size(); i++) {
            if (new ArrayList<String>(filterd.values()).contains(AppConfiguration.comboID.get(i))) {
                if (found) {
                    tempID.remove(AppConfiguration.comboID.get(i));
                }
                found = true;
            }
        }

        found = false;
        for (int i = 0; i < AppConfiguration.comboID2.size(); i++) {
            if (new ArrayList<String>(filterd.values()).contains(AppConfiguration.comboID2.get(i))) {
                if (found) {
                    tempID.remove(AppConfiguration.comboID2.get(i));
                }
                found = true;
            }
        }


        selectedStudents.clear();
        selectedStudents.addAll(tempID);
        System.out.println("Filterd IDs " + tempID);

        nextMethod();
    }

    ArrayList<String> selectedStudents = new ArrayList<String>();
    ArrayList<String> selectedID = new ArrayList<String>();
    ArrayList<String> selectedText = new ArrayList<String>();


    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
//        btn_next.setTypeface(regular);

    }

    /**
     * This method generate MODAL view
     * And also make border around the error views.
     */
    public void SelectInstructorDialog() {


        LayoutInflater lInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        @SuppressWarnings("deprecation")
        final PopupWindow popWindow = new PopupWindow(layout,
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        popWindow.showAtLocation(layout, Gravity.TOP, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Warning");

        TextView tv_description = (TextView) layout
                .findViewById(R.id.tv_description);
        tv_description.setText("Please complete the missing information");
        tv_description.setTextColor(Color.BLACK);
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "Roboto_Light.ttf");
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        tv_description.setTypeface(face);
        tv_appname.setTypeface(regular);

        TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
        imgv_icon.setOnClickListener(new OnClickListener() {
            // Rakesh 16112015
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
                if (popWindow.isShowing() == false) {
                    if (sites_spinner.getSelectedItem().toString().equalsIgnoreCase("Please Select a Location")) {
                        sites_lay.setBackgroundResource(R.drawable.error_border);
                        scrollview.smoothScrollTo(0, 0);
                    }

                    if (start_date.getText().toString().trim().length() == 0) {
                        start_redBorder
                                .setBackgroundResource(R.drawable.error_border);
                        scrollview.smoothScrollTo(0, 0);
                    }
                    // Rakesh 20112015............

                    if (enddate_vis.getCheckedRadioButtonId() == R.id.rb2) {
                        if (end_date.getText().toString().trim().length() == 0) {
                            endDateredBorder
                                    .setBackgroundResource(R.drawable.error_border);
                        } else {
                            endDateredBorder
                                    .setBackgroundResource(R.drawable.pure_error_border_white);
                        }
                        scrollview.smoothScrollTo(0, 0);
                    }

                    if (selectedStudents.size() == 0) {
                        students_lay
                                .setBackgroundResource(R.drawable.error_border);
                        scrollview.smoothScrollTo(0, 0);
                    }
                    // que1.setBackgroundResource(R.drawable.error_border);
                    // que2.setBackgroundResource(R.drawable.error_border);
//                    Log.e("btn_sites.getText().toString()", btn_sites.getText()
//                            .toString());
//                    Log.e("start_date.getText().toString().trim().length()",
//                            String.valueOf(start_date.getText().toString()
//                                    .trim().length()));
//                    Log.e("selectedStudents.size()",
//                            "" + selectedStudents.size());
                }

            }
        });
    }


    /**
     * Generates the CheckBoxes from the combo array.
     */
    public void chckStudents() {

        if (combo1.size() > 0) {
            combo1_lay.setVisibility(View.VISIBLE);
            for (int i = 0; i < combo1.size(); i++) {
                String tempst[];
                if (combo1.get(i).contains(" ")) {
                    tempst = combo1.get(i).split(" ");
                } else {
                    tempst = combo1.get(i).toString().split("");
                }

                if (i == 0) {
                    st_chk_1.setText(tempst[0]);
                    st_chk_1.setTag(combo1.get(i));
                    st_chk_1.setVisibility(View.VISIBLE);
                    if (combo1.size() == 2) {
                        st_chk_1.setChecked(true);
                    }
                } else if (i == 1) {
                    st_chk_2.setText(tempst[0]);
                    st_chk_2.setTag(combo1.get(i));
                    st_chk_2.setVisibility(View.VISIBLE);
                    if (combo1.size() == 2) {
                        st_chk_2.setChecked(true);
                    }
                } else if (i == 2) {
                    st_chk_3.setText(tempst[0]);
                    st_chk_3.setTag(combo1.get(i));
                    st_chk_3.setVisibility(View.VISIBLE);
                } else if (i == 3) {
                    st_chk_4.setText(tempst[0]);
                    st_chk_4.setTag(combo1.get(i));
                    st_chk_4.setVisibility(View.VISIBLE);
                } else if (i == 4) {
                    st_chk_5.setText(tempst[0]);
                    st_chk_5.setTag(combo1.get(i));
                    st_chk_5.setVisibility(View.VISIBLE);
                }

            }
        } else {
            combo1_lay.setVisibility(View.GONE);
        }
        if (combo2.size() > 0) {
            combo2_lay.setVisibility(View.VISIBLE);
            for (int i = 0; i < combo2.size(); i++) {
                String tempst[];
                if (combo2.get(i).contains(" ")) {
                    tempst = combo2.get(i).split(" ");
                } else {
                    tempst = combo2.get(i).toString().split("");
                }

                if (i == 0) {
                    st_chk_1_2.setText(tempst[0]);
                    st_chk_1_2.setTag(combo2.get(i));
                    st_chk_1_2.setVisibility(View.VISIBLE);
                } else if (i == 1) {
                    st_chk_2_2.setText(tempst[0]);
                    st_chk_2_2.setTag(combo2.get(i));
                    st_chk_2_2.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    st_chk_3_2.setText(tempst[0]);
                    st_chk_3_2.setTag(combo2.get(i));
                    st_chk_3_2.setVisibility(View.VISIBLE);
                } else if (i == 3) {
                    st_chk_4_2.setText(tempst[0]);
                    st_chk_4_2.setTag(combo2.get(i));
                    st_chk_4_2.setVisibility(View.VISIBLE);
                } else if (i == 4) {
                    st_chk_5_2.setText(tempst[0]);
                    st_chk_5_2.setTag(combo2.get(i));
                    st_chk_5_2.setVisibility(View.VISIBLE);
                }

            }
        } else {
            combo2_lay.setVisibility(View.GONE);
        }

    }

    ArrayList<String> selected_st_name = new ArrayList<String>();

    /**
     * WebServices
     *
     * @author Harsh Adms
     *         Student Fname Service
     */
    class StudentFname extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(mContext);
//            pd.setMessage("Please wait...");
//            pd.setCancelable(true);
            //			pd.show();

            siteMainList.clear();
            siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadFnameList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if (pd != null) {
//                pd.dismiss();
//            }
        }
    }

    public void loadFnameList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(
                AppConfiguration.scheduleALesssionSiteListURL, param);

        readAndParseJSONFname(responseString);
    }

    HashMap<String, String> stNamestID = new HashMap<String, String>();

    public void readAndParseJSONFname(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("StudentList");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("SFirstName", jsonChildNode.getString("SFirstName"));
                hashmap.put("StudentID", jsonChildNode.getString("StudentID"));

                siteName.add("" + jsonChildNode.getString("sitename"));
                siteMainList.add(hashmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String data_load = "False";

    /**
     * Remaining lessons detail Service
     *
     * @author Harsh Adms
     */
    private class GetMakeUpCount extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("familyid", familyID);

            String responseString = WebServicesCall.RunScript(
                    AppConfiguration.DOMAIN
                            + AppConfiguration.Schl_Get_MakeupCountByFamily,
                    param);

            try {
                data_load = "False";

                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equals("True")) {
                    AppConfiguration.Mup_cnt = reader.getString("Mup_cnt");
                } else {
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
            if (data_load.toString().equals("True")) {
                if (!AppConfiguration.Mup_cnt.equals("0")) {
                    // Rakesh 20112015............
                    noti_count.setVisibility(View.VISIBLE);
                    noti_count.setText(AppConfiguration.Mup_cnt);
                } else {
                    noti_count.setVisibility(View.GONE);
                }

            } else {

            }
        }
    }
/*// Expand Colapse

    private_lessons void expand() {
        //set Visible
        que1.setVisibility(View.VISIBLE);

//		Remove and used in preDrawListener
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        que1.measure(widthSpec, heightSpec);
		mAnimator = slideAnimator(0, que1.getMeasuredHeight());


        mAnimator.start();
    }

    private_lessons void collapse() {
        int finalHeight = que1.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                que1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private_lessons ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = que1.getLayoutParams();
                layoutParams.height = value;
                que1.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }*/
}
