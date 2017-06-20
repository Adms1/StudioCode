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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
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
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.model.Data;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.TinyDB;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;

/**
 * @author Harsh Adms
 */
public class ScheduleLessonFragement4 extends Activity {

    View rootView;
    String token, familyID;

    //Student tab Layout
    LinearLayout st_1, st_2, st_3, st_4, st_5, main_lay, days_st_1, days_st_2, days_st_3, days_st_4, days_st_5;
    TextView name_1, name_2, name_3, name_4, name_5, NoRecord_alert;
    View select_1, select_2, select_3, select_4, select_5;

    //Next Button
    CardView btn_next_card, btn_Prev_card, begin_new_card;
    Button filter_icon;
    String SelectedInstructor, SelectedInstructor1, SelectedInstructor2, SelectedInstructor3, SelectedInstructor4;
    LinearLayout ll_schedule3_body, ll_schedule3_body_2, ll_schedule3_body_3, ll_schedule3_body_4, ll_schedule3_body_5;

    Snackbar mSnackbar;
    Context mContext = this;

    private ArrayList<String> instructorList1 = null, instructorList2 = null, instructorList3 = null, instructorList4 = null, instructorList5 = null;

    View selected_1, selected_2, selected_3;
    LinearLayout scdl_lsn, scdl_mkp, waitlist;
    TextView txt_1, txt_2, txt_3, noti_count;
    ImageLoader imageLoader;
    ScrollView scrollView;
    public static int last_clicked_position = 1;
    View view_1, view_2;

    String strstudentarry1 = "";
    String gettimes = "false", Msg = "";
    ArrayList<ArrayList<ArrayList<String>>> ClassAvailList1 = new ArrayList<ArrayList<ArrayList<String>>>();
    ArrayList<ArrayList<ArrayList<String>>> ClassAvailList2 = new ArrayList<ArrayList<ArrayList<String>>>();
    ArrayList<ArrayList<ArrayList<String>>> ClassAvailList3 = new ArrayList<ArrayList<ArrayList<String>>>();
    ArrayList<ArrayList<ArrayList<String>>> ClassAvailList4 = new ArrayList<ArrayList<ArrayList<String>>>();
    ArrayList<ArrayList<ArrayList<String>>> ClassAvailList5 = new ArrayList<ArrayList<ArrayList<String>>>();
    TinyDB tinydb;
    Boolean isInternetPresent = false;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_schedule_lesson4);
        tinydb = new TinyDB(mContext);
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

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));

        init();
        ClearAll();
        isInternetPresent = Utility.isNetworkConnected(ScheduleLessonFragement4.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new PageLoadGetTime().execute();
        }
        typeFace();
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

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
    }

    public void init() {

        filter_icon = (Button) findViewById(R.id.filter_icon);
        filter_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Data> listData = new ArrayList<Data>();
                listData.add(new Data(ClassAvailList1));
                listData.add(new Data(ClassAvailList2));
                listData.add(new Data(ClassAvailList3));
                listData.add(new Data(ClassAvailList4));
                listData.add(new Data(ClassAvailList5));
                tinydb.putListObject("ClassAvailList", listData);

                instNamePicStatic = instNamePic;

                startActivity(new Intent(mContext, ShedulingFilter.class));
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAll();
                Intent intentCheckin = new Intent(ScheduleLessonFragement4.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        btn_next_card = (CardView) findViewById(R.id.btn_next_card);
        btn_Prev_card = (CardView) findViewById(R.id.btn_Prev_card);
        begin_new_card = (CardView) findViewById(R.id.begin_new_card);


        begin_new_card.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppConfiguration.custom_flow = true;
                        AppConfiguration.transform = 5;
                        Intent i = new Intent(mContext, ScheduleLessonFragement.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }, 100);
            }
        });

        btn_Prev_card.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        AppConfiguration.custom_flow = true;
//                        AppConfiguration.transform = 5;
                        finish();
                    }
                }, 100);
            }
        });

        main_lay = (LinearLayout) findViewById(R.id.main_lay);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        View view = findViewById(R.id.schdl_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);
        RippleView ripple = (RippleView) view.findViewById(R.id.ripple);

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

        st_1 = (LinearLayout) findViewById(R.id.st_1);
        st_2 = (LinearLayout) findViewById(R.id.st_2);
        st_3 = (LinearLayout) findViewById(R.id.st_3);
        st_4 = (LinearLayout) findViewById(R.id.st_4);
        st_5 = (LinearLayout) findViewById(R.id.st_5);

        days_st_1 = (LinearLayout) findViewById(R.id.days_st_1);
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

        NoRecord_alert = (TextView) findViewById(R.id.NoRecord_alert);

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
                scrollView.scrollTo(0, 0);
                days_st_1.setVisibility(View.VISIBLE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);

//                select_1.setVisibility(View.VISIBLE);
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

                animation_slide(temp1, temp3, view_1, view_2);
                last_clicked_position = 1;
            }
        });

        st_2.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scrollView.scrollTo(0, 0);
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.VISIBLE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);

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
                animation_slide(temp1, temp3, view_1, view_2);
                last_clicked_position = 2;
            }
        });

        st_3.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scrollView.scrollTo(0, 0);
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.VISIBLE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.INVISIBLE);
                select_3.setVisibility(View.VISIBLE);
                select_4.setVisibility(View.INVISIBLE);
                select_5.setVisibility(View.INVISIBLE);

                name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_3.setTextColor(getResources().getColor(R.color.orange));
                name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
                name_5.setTextColor(getResources().getColor(R.color.design_change_d2));


                decide_layout(last_view(), select_3);
                animation_slide(temp1, temp3, view_1, view_2);
                last_clicked_position = 3;
            }
        });

        st_4.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scrollView.scrollTo(0, 0);
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.VISIBLE);
                days_st_5.setVisibility(View.GONE);

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
                animation_slide(temp1, temp3, view_1, view_2);
                last_clicked_position = 4;
            }
        });

        st_5.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scrollView.scrollTo(0, 0);
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
                animation_slide(temp1, temp3, view_1, view_2);
                last_clicked_position = 5;
            }
        });

        distributeStudentsTop();

        SelectedInstructor = AppConfiguration.Array2String(AppConfiguration.builder);
        SelectedInstructor1 = AppConfiguration.Array2String(AppConfiguration.builder1);
        SelectedInstructor2 = AppConfiguration.Array2String(AppConfiguration.builder2);
        SelectedInstructor3 = AppConfiguration.Array2String(AppConfiguration.builder3);
        SelectedInstructor4 = AppConfiguration.Array2String(AppConfiguration.builder4);

        ll_schedule3_body = (LinearLayout) findViewById(R.id.days_st_1);
        ll_schedule3_body_2 = (LinearLayout) findViewById(R.id.days_st_2);
        ll_schedule3_body_3 = (LinearLayout) findViewById(R.id.days_st_3);
        ll_schedule3_body_4 = (LinearLayout) findViewById(R.id.days_st_4);
        ll_schedule3_body_5 = (LinearLayout) findViewById(R.id.days_st_5);

        instructorList1 = new ArrayList<>();
        instructorList2 = new ArrayList<>();
        instructorList3 = new ArrayList<>();
        instructorList4 = new ArrayList<>();
        instructorList5 = new ArrayList<>();

        btn_next_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFinalArray();
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
//            temp2=lay_1;
        } else if (pos_1 == 2) {
            temp1 = days_st_2;
//            temp2=lay_2;
        } else if (pos_1 == 3) {
            temp1 = days_st_3;
//            temp2=lay_3;
        } else if (pos_1 == 4) {
            temp1 = days_st_4;
//            temp2=lay_4;
        } else if (pos_1 == 5) {
            temp1 = days_st_5;
//            temp2=lay_5;
        }

        if (pos_2 == 1) {
            temp3 = days_st_1;
//            temp4=lay_1;
        } else if (pos_2 == 2) {
            temp3 = days_st_2;
//            temp4=lay_2;
        } else if (pos_2 == 3) {
            temp3 = days_st_3;
//            temp4=lay_3;
        } else if (pos_2 == 4) {
            temp3 = days_st_4;
//            temp4=lay_4;
        } else if (pos_2 == 5) {
            temp3 = days_st_5;
//            temp4=lay_5;
        }

    }

    public void animation_slide(
            final LinearLayout ll2,

            final LinearLayout ll5,
            final View v1,
            final View v2) {

        if (!slide_left) {
            Animation animSlidInLeft = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
            ll5.startAnimation(animSlidInLeft);

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


    //This will clear old values.
    public void ClearAll() {
        AppConfiguration.pair1_DayTime = "";
        AppConfiguration.pair2_DayTime = "";
        strstudentarry1 = "";
        ShedulingFilter.fromFilter = false;
//        ShedulingFilter.fromAdvancedFilter = false;
        ShedulingFilter.sendingID_std1.clear();
        ShedulingFilter.sendingID_std2.clear();
        ShedulingFilter.sendingID_std3.clear();
        ShedulingFilter.sendingID_std4.clear();
        ShedulingFilter.sendingID_std5.clear();
        ShedulingFilter.schedulingFilterInstArrayModels1.clear();
        ShedulingFilter.schedulingFilterInstArrayModels2.clear();
        ShedulingFilter.schedulingFilterInstArrayModels3.clear();
        ShedulingFilter.schedulingFilterInstArrayModels4.clear();
        ShedulingFilter.schedulingFilterInstArrayModels5.clear();
        last_clicked_position = 1;
        ClassAvailList1.clear();
        ClassAvailList2.clear();
        ClassAvailList3.clear();
        ClassAvailList4.clear();
        ClassAvailList5.clear();
        ShedulingFilter.daysAndTimes_std1.clear();
        ShedulingFilter.daysAndTimes_std2.clear();
        ShedulingFilter.daysAndTimes_std3.clear();
        ShedulingFilter.daysAndTimes_std4.clear();
        ShedulingFilter.daysAndTimes_std5.clear();
        ShedulingFilter.advancedFilter_std.clear();
        ShedulingFilter.ClassAvailListTempStud0.clear();
        ShedulingFilter.ClassAvailListTempStud2.clear();
        ShedulingFilter.ClassAvailListTempStud3.clear();
        ShedulingFilter.ClassAvailListTempStud4.clear();
        ShedulingFilter.ClassAvailListTempStud5.clear();
        instructorList1.clear();
        instructorList2.clear();
        instructorList3.clear();
        instructorList4.clear();
        instructorList5.clear();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        ClearAll();
    }

    @Override
    public void onPause() {
        super.onPause();
        popWindow.dismiss();
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

        if (displayDone == false) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    SearchingLessons();
                }
            }, 500);
        }

        if (ShedulingFilter.fromFilter) {
            if (AppConfiguration.selectedStudentsName.size() == 1) {

                ll_schedule3_body.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud0, ll_schedule3_body, 0);

            } else if (AppConfiguration.selectedStudentsName.size() == 2) {

                ll_schedule3_body.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud0, ll_schedule3_body, 0);
                ll_schedule3_body_2.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud2, ll_schedule3_body_2, 1);

            } else if (AppConfiguration.selectedStudentsName.size() == 3) {

                ll_schedule3_body.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud0, ll_schedule3_body, 0);
                ll_schedule3_body_2.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud2, ll_schedule3_body_2, 1);
                ll_schedule3_body_3.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud3, ll_schedule3_body_3, 2);

            } else if (AppConfiguration.selectedStudentsName.size() == 4) {

                ll_schedule3_body.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud0, ll_schedule3_body, 0);
                ll_schedule3_body_2.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud2, ll_schedule3_body_2, 1);
                ll_schedule3_body_3.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud3, ll_schedule3_body_3, 2);
                ll_schedule3_body_4.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud4, ll_schedule3_body_4, 3);

            } else if (AppConfiguration.selectedStudentsName.size() == 5) {

                ll_schedule3_body.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud0, ll_schedule3_body, 0);
                ll_schedule3_body_2.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud2, ll_schedule3_body_2, 1);
                ll_schedule3_body_3.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud3, ll_schedule3_body_3, 2);
                ll_schedule3_body_4.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud4, ll_schedule3_body_4, 3);
                ll_schedule3_body_5.removeAllViews();
                studentLaySet(ShedulingFilter.ClassAvailListTempStud5, ll_schedule3_body_5, 4);
            }
        }
    }

    public int count = 0, pr = 0, sp = 0, adult = 0;
    public boolean flag = true;

    @SuppressWarnings("unused")

    public void GetFinalArray() {
        //Reset the counters
        AppConfiguration.st_Student1 = "";
        AppConfiguration.st_Student2 = "";
        AppConfiguration.st_Student3 = "";
        AppConfiguration.st_Student4 = "";
        AppConfiguration.st_Student5 = "";

        String[] tempname = AppConfiguration.Array2String(AppConfiguration.selectedStudentsName)
                .toString().split("\\,");

        flag = true;
        pr = 0;
        sp = 0;
        adult = 0;
        for (int i = 0; i < tempname.length; i++) {
            if (i == 0) {
                AppConfiguration.st_Student1 = GetCheckedItems(ll_schedule3_body, i);
            } else if (i == 1) {
                AppConfiguration.st_Student2 = GetCheckedItems(ll_schedule3_body_2, i);
            } else if (i == 2) {
                AppConfiguration.st_Student3 = GetCheckedItems(ll_schedule3_body_3, i);
            } else if (i == 3) {
                AppConfiguration.st_Student4 = GetCheckedItems(ll_schedule3_body_4, i);
            } else if (i == 4) {
                AppConfiguration.st_Student5 = GetCheckedItems(ll_schedule3_body_5, i);
            }
        }

        if (AppConfiguration.makeUpFlag.contains("1")) {
            if (pr > Integer.valueOf(ScheduleMakeupFragment.limits.get(0).get("private_lessons"))) {
                flag = false;
            } else if (sp > Integer.valueOf(ScheduleMakeupFragment.limits.get(0).get("semiprivate"))) {
                flag = false;
            } else if (adult > Integer.valueOf(ScheduleMakeupFragment.limits.get(0).get("adult"))) {
                flag = false;
            }
        } else {
            flag = true;
        }

        System.out.println("Student 1: " + AppConfiguration.st_Student1);
        System.out.println("Student 2: " + AppConfiguration.st_Student2);
        System.out.println("Student 3: " + AppConfiguration.st_Student3);
        System.out.println("Student 4: " + AppConfiguration.st_Student4);
        System.out.println("Student 5: " + AppConfiguration.st_Student5);


        if (AppConfiguration.st_Student1.trim().length() > 0 ||
                AppConfiguration.st_Student2.trim().length() > 0 ||
                AppConfiguration.st_Student3.trim().length() > 0 ||
                AppConfiguration.st_Student4.trim().length() > 0 ||
                AppConfiguration.st_Student5.trim().length() > 0) {
            continue_disable = false;
            checkChekedStudents();
            if (doneForAll == false) {
                SelectInstructorDialog();
            } else {
                if (AppConfiguration.makeUpFlag.contains("1")) {
                    if (flag) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ShedulingFilter.fromFilter = false;
                                Intent i = new Intent(mContext, ScheduleLessonFragement5.class);
                                startActivity(i);
                            }
                        }, 600);

                    } else {
                        int bgColor = R.color.black;
                        int txtColor = R.color.red;
                        String message = "You have selected more classes then you have available on your account..!! ";
                        Utility.ping(ScheduleLessonFragement4.this, message);
                    }
                } else {
                    ShedulingFilter.fromFilter = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ShedulingFilter.fromFilter = false;
                            Intent i = new Intent(mContext, ScheduleLessonFragement5.class);
                            startActivity(i);
                        }
                    }, 600);
                }
            }
        } else {
            continue_disable = true;
            SelectInstructorDialog();
        }
    }


    public void checkChekedStudents() {
        int size = AppConfiguration.selectedStudentsName.size();
        if (size == 1) {
            if (AppConfiguration.st_Student1.trim().length() == 0
                    ) {
                doneForAll = false;
            } else {
                doneForAll = true;
            }
        }
        if (size == 2) {
            if (AppConfiguration.st_Student1.trim().length() == 0 ||
                    AppConfiguration.st_Student2.trim().length() == 0
                    ) {
                doneForAll = false;
            } else {
                doneForAll = true;
            }
        }
        if (size == 3) {
            if (AppConfiguration.st_Student1.trim().length() == 0 ||
                    AppConfiguration.st_Student2.trim().length() == 0 ||
                    AppConfiguration.st_Student3.trim().length() == 0
                    ) {
                doneForAll = false;
            } else {
                doneForAll = true;
            }
        }
        if (size == 4) {
            if (AppConfiguration.st_Student1.trim().length() == 0 ||
                    AppConfiguration.st_Student2.trim().length() == 0 ||
                    AppConfiguration.st_Student3.trim().length() == 0 ||
                    AppConfiguration.st_Student4.trim().length() == 0) {
                doneForAll = false;
            } else {
                doneForAll = true;
            }
        }
        if (size == 5) {
            if (AppConfiguration.st_Student1.trim().length() == 0 ||
                    AppConfiguration.st_Student2.trim().length() == 0 ||
                    AppConfiguration.st_Student3.trim().length() == 0 ||
                    AppConfiguration.st_Student4.trim().length() == 0 ||
                    AppConfiguration.st_Student5.trim().length() == 0) {
                doneForAll = false;
            } else {
                doneForAll = true;
            }
        }
    }

    boolean continue_disable = false;
    boolean doneForAll = true;
    ArrayList<String> sunDay1 = new ArrayList<String>();
    ArrayList<String> monDay1 = new ArrayList<String>();
    ArrayList<String> tueDay1 = new ArrayList<String>();
    ArrayList<String> wedDay1 = new ArrayList<String>();
    ArrayList<String> thuDay1 = new ArrayList<String>();
    ArrayList<String> friDay1 = new ArrayList<String>();
    ArrayList<String> satDay1 = new ArrayList<String>();

    public String GetCheckedItems(LinearLayout ParentLay, int whichSt) {
        count = 0;
        if (ParentLay.getChildCount() > 0) {
            for (int i = 0; i < ParentLay.getChildCount(); i++) {
                //				int count = ParentLay.getChildCount();
                View view = ParentLay.getChildAt(i);
                if (view instanceof LinearLayout) {
                    LinearLayout ll = (LinearLayout) view;
                    for (int j = 0; j < ll.getChildCount(); j++) {
                        View view1 = ll.getChildAt(j);
                        if (view1 instanceof LinearLayout) {
                            LinearLayout ll2 = (LinearLayout) view1;
                            for (int k = 0; k < ll2.getChildCount(); k++) {
                                View view2 = ll2.getChildAt(k);
                                if (view2 instanceof LinearLayout) {
                                    LinearLayout ll3 = (LinearLayout) view2;
                                    for (int l = 0; l < ll3.getChildCount(); l++) {
                                        View view3 = ll3.getChildAt(l);
                                        if (view3 instanceof LinearLayout) {
                                            LinearLayout sun = (LinearLayout) view3;
                                            for (int m = 0; m < sun.getChildCount(); m++) {
                                                View sunChildView = sun.getChildAt(m);
                                                if (sunChildView instanceof LinearLayout) {
                                                } else if (sunChildView instanceof CheckBox) {
                                                    CheckBox chBx = (CheckBox) sunChildView;
                                                    if (chBx instanceof CheckBox) {
                                                        if (chBx.isChecked()) {
                                                            if (templessonid[whichSt].equals("1")) {
                                                                pr++;
                                                            } else if (templessonid[whichSt].equals("2")) {
                                                                sp++;
                                                            } else if (templessonid[whichSt].equals("4")) {
                                                                adult++;
                                                            }
                                                            count++;
                                                            if (chBx.getTag().toString().contains("Sunday")) {
                                                                sunDay1.add(chBx.getTag().toString());
                                                            } else if (chBx.getTag().toString().contains("Monday")) {
                                                                monDay1.add(chBx.getTag().toString());
                                                            } else if (chBx.getTag().toString().contains("Tuesday")) {
                                                                tueDay1.add(chBx.getTag().toString());
                                                            } else if (chBx.getTag().toString().contains("Wednesday")) {
                                                                wedDay1.add(chBx.getTag().toString());
                                                            } else if (chBx.getTag().toString().contains("Thursday")) {
                                                                thuDay1.add(chBx.getTag().toString());
                                                            } else if (chBx.getTag().toString().contains("Friday")) {
                                                                friDay1.add(chBx.getTag().toString());
                                                            } else if (chBx.getTag().toString().contains("Saturday")) {
                                                                satDay1.add(chBx.getTag().toString());
                                                            }
                                                        } else {
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                CheckBox chBx = (CheckBox) view.findViewById(R.id.chkScheduleTime);
                if (chBx instanceof CheckBox) {
                    if (chBx.isChecked()) {
                        System.out.println("Check : " + chBx.getTag());
                    } else {
                        System.out.println("Check : " + chBx.getTag());
                    }
                }
            }
        }
        System.out.println("Counter Value : " + count);
        String studentArray = "";
        if (monDay1.size() > 0 ||
                tueDay1.size() > 0 ||
                wedDay1.size() > 0 ||
                thuDay1.size() > 0 ||
                friDay1.size() > 0 ||
                satDay1.size() > 0 ||
                sunDay1.size() > 0) {
            studentArray = GetArrayToString(monDay1, tueDay1, wedDay1, thuDay1, friDay1, satDay1, sunDay1);
        } else {
            studentArray = "";
        }
        System.out.println("Mon : " + monDay1);
        System.out.println("Tue : " + tueDay1);
        System.out.println("Wed : " + wedDay1);
        System.out.println("Thu : " + thuDay1);
        System.out.println("Fri : " + friDay1);
        System.out.println("Sat : " + satDay1);
        System.out.println("Sun : " + sunDay1);

        monDay1.clear();
        tueDay1.clear();
        wedDay1.clear();
        thuDay1.clear();
        friDay1.clear();
        satDay1.clear();
        sunDay1.clear();

        return studentArray;
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

    public String GetArrayToString(ArrayList<String> monDay1, ArrayList<String> tueDay1, ArrayList<String> wedDay1,
                                   ArrayList<String> thuDay1, ArrayList<String> friDay1, ArrayList<String> satDay1, ArrayList<String> sunDay1) {
        String student = "";

        for (String s : monDay1) {
            student += s + ",";
        }

        for (String s : tueDay1) {
            student += s + ",";
        }

        for (String s : wedDay1) {
            student += s + ",";
        }

        for (String s : thuDay1) {
            student += s + ",";
        }

        for (String s : friDay1) {
            student += s + ",";
        }

        for (String s : satDay1) {
            student += s + ",";
        }

        for (String s : sunDay1) {
            student += s + ",";
        }

        if (student.length() > 0 && student.equals(",")) {
            student = student.substring(0, student.length() - 1);
        }
        return student;

    }

    String _mon = "", _tue = "", _wed = "", _thu = "", _fri = "", _sat = "", _sun = "";
    String strstudentarry = "", instructorListBuilder = "", instructorListBuilder1 = "",
            instructorListBuilder2 = "", instructorListBuilder3 = "", instructorListBuilder4 = "";

    String instructorListBuilderNew = "";
    String[] templessonid = AppConfiguration.salStep1LessonID
            .toString().split("\\,");

    public String check(HashMap<String, Boolean> bool, int j) {

        String[] tempid = AppConfiguration.selectedStudentID.toString()
                .split("\\,");

        String[] tempname = AppConfiguration.Array2String(AppConfiguration.selectedStudentsName)
                .toString().split("\\,");

        if (AppConfiguration.instructorListBuilder.length() > 0 && AppConfiguration.instructorListBuilder
                .charAt(AppConfiguration.instructorListBuilder
                        .length() - 1) == ',') {
            instructorListBuilder = AppConfiguration.instructorListBuilder.toString().substring(0, AppConfiguration.instructorListBuilder.length() - 1);

            instructorListBuilderNew = AppConfiguration.instructorListBuilderForInstr.toString().substring(0, AppConfiguration.instructorListBuilderForInstr.length() - 1);
        }

        if (AppConfiguration.instructorListBuilder1.length() > 0
                && AppConfiguration.instructorListBuilder1
                .charAt(AppConfiguration.instructorListBuilder1
                        .length() - 1) == ',') {
            instructorListBuilder1 = AppConfiguration.instructorListBuilder1
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilder1.length() - 1);

            instructorListBuilderNew = AppConfiguration.instructorListBuilderForInstr1
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilderForInstr1.length() - 1);
        }

        if (AppConfiguration.instructorListBuilder2.length() > 0
                && AppConfiguration.instructorListBuilder2
                .charAt(AppConfiguration.instructorListBuilder2
                        .length() - 1) == ',') {
            instructorListBuilder2 = AppConfiguration.instructorListBuilder2
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilder2.length() - 1);

            instructorListBuilderNew = AppConfiguration.instructorListBuilderForInstr2
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilderForInstr2.length() - 1);
        }

        if (AppConfiguration.instructorListBuilder3.length() > 0
                && AppConfiguration.instructorListBuilder3
                .charAt(AppConfiguration.instructorListBuilder3
                        .length() - 1) == ',') {
            instructorListBuilder3 = AppConfiguration.instructorListBuilder3
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilder3.length() - 1);

            instructorListBuilderNew = AppConfiguration.instructorListBuilderForInstr3
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilderForInstr3.length() - 1);
        }

        if (AppConfiguration.instructorListBuilder4.length() > 0
                && AppConfiguration.instructorListBuilder4
                .charAt(AppConfiguration.instructorListBuilder4
                        .length() - 1) == ',') {
            instructorListBuilder4 = AppConfiguration.instructorListBuilder4
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilder4.length() - 1);

            instructorListBuilderNew = AppConfiguration.instructorListBuilderForInstr4
                    .toString()
                    .substring(
                            0,
                            AppConfiguration.instructorListBuilderForInstr4.length() - 1);
        }

        String instructorlist = "";
        if (j == 0) {
            instructorlist = instructorListBuilder.toString()
                    .replaceAll("--", "*").replaceAll(",", "|");
        } else if (j == 1) {
            instructorlist = instructorListBuilder1.toString()
                    .replaceAll("--", "*").replaceAll(",", "|");
        } else if (j == 2) {
            instructorlist = instructorListBuilder2.toString()
                    .replaceAll("--", "*").replaceAll(",", "|");
        } else if (j == 3) {
            instructorlist = instructorListBuilder3.toString()
                    .replaceAll("--", "*").replaceAll(",", "|");
        } else if (j == 4) {
            instructorlist = instructorListBuilder4.toString()
                    .replaceAll("--", "*").replaceAll(",", "|");
        }


        HashMap<String, Boolean> selected = new HashMap<String, Boolean>();
        selected = bool;

        //		for (int j= 0; j < AppConfiguration.studentsize; j++) {

        int i = 0;
        if (selected.get("Mon_" + i) ||
                selected.get("Mon_" + (i + 1))
                || selected.get("Mon_" + (i + 2))) {

            _mon = "Monday|True|" + String.valueOf(selected.get("Mon_" + i))
                    + "_" + String.valueOf(selected.get("Mon_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Mon_" + (i + 2)));

            AppConfiguration.MON_Check = true;
        } else {
            _mon = "Monday|True|" + String.valueOf(selected.get("Mon_" + i))
                    + "_" + String.valueOf(selected.get("Mon_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Mon_" + (i + 2)));
            //				_mon="null";
        }
        if (selected.get("Tue_" + i) ||
                selected.get("Tue_" + (i + 1))
                || selected.get("Tue_" + (i + 2))) {
            _tue = "Tuesday|True|" + String.valueOf(selected.get("Tue_" + i))
                    + "_" + String.valueOf(selected.get("Tue_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Tue_" + (i + 2)));

            AppConfiguration.TUE_Check = true;
        } else {
            _tue = "Tuesday|True|" + String.valueOf(selected.get("Tue_" + i))
                    + "_" + String.valueOf(selected.get("Tue_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Tue_" + (i + 2)));
            //				_tue="null";
        }
        if (selected.get("Wed_" + i) ||
                selected.get("Wed_" + (i + 1))
                || selected.get("Wed_" + (i + 2))) {
            _wed = "Wednesday|True|"
                    + String.valueOf(selected.get("Wed_" + i)) + "_"
                    + String.valueOf(selected.get("Wed_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Wed_" + (i + 2)));
            AppConfiguration.WED_Check = true;
        } else {
            _wed = "Wednesday|True|"
                    + String.valueOf(selected.get("Wed_" + i)) + "_"
                    + String.valueOf(selected.get("Wed_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Wed_" + (i + 2)));
            //				_wed = "null";
        }
        if (selected.get("Thu_" + i) ||
                selected.get("Thu_" + (i + 1))
                || selected.get("Thu_" + (i + 2))) {
            _thu = "Thursday|True|"
                    + String.valueOf(selected.get("Thu_" + i)) + "_"
                    + String.valueOf(selected.get("Thu_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Thu_" + (i + 2)));

            AppConfiguration.THU_Check = true;
        } else {
            _thu = "Thursday|True|"
                    + String.valueOf(selected.get("Thu_" + i)) + "_"
                    + String.valueOf(selected.get("Thu_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Thu_" + (i + 2)));
            //				_thu = "null";
        }
        if (selected.get("Fri_" + i) ||
                selected.get("Fri_" + (i + 1))
                || selected.get("Fri_" + (i + 2))) {
            _fri = "Friday|True|" + String.valueOf(selected.get("Fri_" + i))
                    + "_" + String.valueOf(selected.get("Fri_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Fri_" + (i + 2)));

            AppConfiguration.FRI_Check = true;
        } else {
            _fri = "Friday|True|" + String.valueOf(selected.get("Fri_" + i))
                    + "_" + String.valueOf(selected.get("Fri_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Fri_" + (i + 2)));
            //				_fri = "null";
        }
        if (selected.get("Sat_" + i) ||
                selected.get("Sat_" + (i + 1))
                || selected.get("Sat_" + (i + 2))) {
            AppConfiguration.SAT_Check = true;
            _sat = "Saturday|True|"
                    + String.valueOf(selected.get("Sat_" + i)) + "_"
                    + String.valueOf(selected.get("Sat_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Sat_" + (i + 2)));
        } else {
            _sat = "Saturday|True|"
                    + String.valueOf(selected.get("Sat_" + i)) + "_"
                    + String.valueOf(selected.get("Sat_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Sat_" + (i + 2)));
            //				_sat = "null";
        }

        if (selected.get("Sun_" + i) ||
                selected.get("Sun_" + (i + 1))
                || selected.get("Sun_" + (i + 2))) {
            AppConfiguration.SUN_Check = true;
            _sun = "Sunday|True|"
                    + String.valueOf(selected.get("Sun_" + i)) + "_"
                    + String.valueOf(selected.get("Sun_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Sun_" + (i + 2)));
        } else {
            _sun = "Sunday|True|"
                    + String.valueOf(selected.get("Sun_" + i)) + "_"
                    + String.valueOf(selected.get("Sun_" + (i + 1))) + "_"
                    + String.valueOf(selected.get("Sun_" + (i + 2)));
            //				_sun = "null";
        }

        if (j == 0) {

            AppConfiguration.pair1_DayTime = _mon + "-" + _tue + "-" + _wed + "-" + _thu + "-"
                    + _fri + "-" + _sat + "-" + _sun + ",";

            strstudentarry = strstudentarry + tempid[j] + "$" + tempname[j] + "$"
                    + templessonid[j] + "$" + instructorlist + "$"
                    + _mon + "$" + _tue + "$" + _wed + "$" + _thu + "$"
                    + _fri + "$" + _sat + "$" + _sun + ",";
        } else if (j == 1) {

            AppConfiguration.pair2_DayTime = _mon + "-" + _tue + "-" + _wed + "-" + _thu + "-"
                    + _fri + "-" + _sat + "-" + _sun + ",";

            strstudentarry = strstudentarry + tempid[j] + "$" + tempname[j] + "$"
                    + templessonid[j] + "$" + instructorlist + "$"
                    + _mon + "$" + _tue + "$" + _wed + "$" + _thu + "$"
                    + _fri + "$" + _sat + "$" + _sun + ",";
        } else if (j == 2) {
            strstudentarry = strstudentarry + tempid[j] + "$" + tempname[j] + "$"
                    + templessonid[j] + "$" + instructorlist + "$"
                    + _mon + "$" + _tue + "$" + _wed + "$" + _thu + "$"
                    + _fri + "$" + _sat + "$" + _sun + ",";
        } else if (j == 3) {
            strstudentarry = strstudentarry + tempid[j] + "$" + tempname[j] + "$"
                    + templessonid[j] + "$" + instructorlist + "$"
                    + _mon + "$" + _tue + "$" + _wed + "$" + _thu + "$"
                    + _fri + "$" + _sat + "$" + _sun + ",";
        } else if (j == 4) {
            strstudentarry = strstudentarry + tempid[j] + "$" + tempname[j] + "$"
                    + templessonid[j] + "$" + instructorlist + "$"
                    + _mon + "$" + _tue + "$" + _wed + "$" + _thu + "$"
                    + _fri + "$" + _sat + "$" + _sun + ",";
        }

        return strstudentarry;
    }


    ArrayList<String> instructors = new ArrayList<String>();
    ArrayList<String> instructors2 = new ArrayList<String>();
    ArrayList<String> instructors3 = new ArrayList<String>();
    ArrayList<String> instructors4 = new ArrayList<String>();
    ArrayList<String> instructors5 = new ArrayList<String>();

    int size_ofTIME = 0;
    HashMap<String, String> instNamePic = new HashMap<String, String>();
    public static HashMap<String, String> instNamePicStatic = new HashMap<String, String>();

    public ArrayList<ArrayList<ArrayList<String>>> getValues(JSONArray jsonMainNode, int count) {
        ArrayList<ArrayList<ArrayList<String>>> ClassAvailList = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> _ClassAvailList1 = new ArrayList<ArrayList<String>>();
        ArrayList<String> tcm_time = new ArrayList<String>();
        ArrayList<String> tcm_dt = new ArrayList<String>();
        ArrayList<String> tcm_cond = new ArrayList<String>();
        ArrayList<String> tcm_select = new ArrayList<String>();
        ArrayList<String> tct_time = new ArrayList<String>();
        ArrayList<String> tct_dt = new ArrayList<String>();
        ArrayList<String> tct_cond = new ArrayList<String>();
        ArrayList<String> tct_select = new ArrayList<String>();
        ArrayList<String> tcw_time = new ArrayList<String>();
        ArrayList<String> tcw_dt = new ArrayList<String>();
        ArrayList<String> tcw_cond = new ArrayList<String>();
        ArrayList<String> tcw_select = new ArrayList<String>();
        ArrayList<String> tcth_time = new ArrayList<String>();
        ArrayList<String> tcth_dt = new ArrayList<String>();
        ArrayList<String> tcth_cond = new ArrayList<String>();
        ArrayList<String> tcth_select = new ArrayList<String>();
        ArrayList<String> tcf_time = new ArrayList<String>();
        ArrayList<String> tcf_dt = new ArrayList<String>();
        ArrayList<String> tcf_cond = new ArrayList<String>();
        ArrayList<String> tcf_select = new ArrayList<String>();
        ArrayList<String> tcsa_time = new ArrayList<String>();
        ArrayList<String> tcsa_dt = new ArrayList<String>();
        ArrayList<String> tcsa_cond = new ArrayList<String>();
        ArrayList<String> tcsa_select = new ArrayList<String>();
        ArrayList<String> tcsu_time = new ArrayList<String>();
        ArrayList<String> tcsu_dt = new ArrayList<String>();
        ArrayList<String> tcsu_cond = new ArrayList<String>();
        ArrayList<String> tcsu_select = new ArrayList<String>();

        try {
            if (jsonMainNode.length() > 0) {
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonObject = jsonMainNode
                            .getJSONObject(i);


                    if (count == 0) {
                        instNamePic.put(jsonObject
                                .getString("instructors"), jsonObject
                                .getString("wu_Photo"));

                        instructors.add(jsonObject
                                .getString("instructors"));
                    } else if (count == 1) {
                        instNamePic.put(jsonObject
                                .getString("instructors"), jsonObject
                                .getString("wu_Photo"));
                        if (!jsonObject.getString("instructors").equals("")) {
                            instructors2.add(jsonObject
                                    .getString("instructors"));
                        }
                    } else if (count == 2) {
                        instNamePic.put(jsonObject
                                .getString("instructors"), jsonObject
                                .getString("wu_Photo"));
                        if (!jsonObject.getString("instructors").equals("")) {
                            instructors3.add(jsonObject
                                    .getString("instructors"));
                        }
                    } else if (count == 3) {
                        instNamePic.put(jsonObject
                                .getString("instructors"), jsonObject
                                .getString("wu_Photo"));
                        if (!jsonObject.getString("instructors").equals("")) {
                            instructors4.add(jsonObject
                                    .getString("instructors"));
                        }
                    } else if (count == 4) {
                        instNamePic.put(jsonObject
                                .getString("instructors"), jsonObject
                                .getString("wu_Photo"));
                        if (!jsonObject.getString("instructors").equals("")) {
                            instructors5.add(jsonObject
                                    .getString("instructors"));
                        }
                    }


                    JSONObject jObject = jsonObject;

                    tcm_time.add(jObject
                            .getString("tcm_time"));
                    tcm_dt.add(jObject.getString("tcm_dt"));
                    tcm_cond.add(jObject
                            .getString("tcm_cond"));
                    tcm_select.add(jObject
                            .getString("tcm_select"));
                    tct_time.add(jObject
                            .getString("tct_time"));
                    tct_dt.add(jObject.getString("tct_dt"));
                    tct_cond.add(jObject
                            .getString("tct_cond"));
                    tct_select.add(jObject
                            .getString("tct_select"));
                    tcw_time.add(jObject
                            .getString("tcw_time"));
                    tcw_dt.add(jObject.getString("tcw_dt"));
                    tcw_cond.add(jObject
                            .getString("tcw_cond"));
                    tcw_select.add(jObject
                            .getString("tcw_select"));
                    tcth_time.add(jObject
                            .getString("tcth_time"));
                    tcth_dt.add(jObject
                            .getString("tcth_dt"));
                    tcth_cond.add(jObject
                            .getString("tcth_cond"));
                    tcth_select.add(jObject
                            .getString("tcth_select"));
                    tcf_time.add(jObject
                            .getString("tcf_time"));
                    tcf_dt.add(jObject.getString("tcf_dt"));
                    tcf_cond.add(jObject
                            .getString("tcf_cond"));
                    tcf_select.add(jObject
                            .getString("tcf_select"));
                    tcsa_time.add(jObject
                            .getString("tcsa_time"));
                    tcsa_dt.add(jObject
                            .getString("tcsa_dt"));
                    tcsa_cond.add(jObject
                            .getString("tcsa_cond"));
                    tcsa_select.add(jObject
                            .getString("tcsa_select"));
                    tcsu_time.add(jObject
                            .getString("tcsu_time"));
                    tcsu_dt.add(jObject
                            .getString("tcsu_dt"));
                    tcsu_cond.add(jObject
                            .getString("tcsu_cond"));
                    tcsu_select.add(jObject
                            .getString("tcsu_select"));
                    size_ofTIME = tct_time.size();
                }

                if (count == 0) {
                    _ClassAvailList1.add(instructors);
                } else if (count == 1) {
                    _ClassAvailList1.add(instructors2);
                } else if (count == 2) {
                    _ClassAvailList1.add(instructors3);
                } else if (count == 3) {
                    _ClassAvailList1.add(instructors4);
                } else if (count == 4) {
                    _ClassAvailList1.add(instructors5);
                }

                _ClassAvailList1.add(tcm_time);
                _ClassAvailList1.add(tcm_dt);
                _ClassAvailList1.add(tcm_cond);
                _ClassAvailList1.add(tcm_select);
                _ClassAvailList1.add(tct_time);
                _ClassAvailList1.add(tct_dt);
                _ClassAvailList1.add(tct_cond);
                _ClassAvailList1.add(tct_select);
                _ClassAvailList1.add(tcw_time);
                _ClassAvailList1.add(tcw_dt);
                _ClassAvailList1.add(tcw_cond);
                _ClassAvailList1.add(tcw_select);
                _ClassAvailList1.add(tcth_time);
                _ClassAvailList1.add(tcth_dt);
                _ClassAvailList1.add(tcth_cond);
                _ClassAvailList1.add(tcth_select);
                _ClassAvailList1.add(tcf_time);
                _ClassAvailList1.add(tcf_dt);
                _ClassAvailList1.add(tcf_cond);
                _ClassAvailList1.add(tcf_select);
                _ClassAvailList1.add(tcsa_time);
                _ClassAvailList1.add(tcsa_dt);
                _ClassAvailList1.add(tcsa_cond);
                _ClassAvailList1.add(tcsa_select);
                _ClassAvailList1.add(tcsu_time);
                _ClassAvailList1.add(tcsu_dt);
                _ClassAvailList1.add(tcsu_cond);
                _ClassAvailList1.add(tcsu_select);
                ClassAvailList.add(_ClassAvailList1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return ClassAvailList;
    }

    //	ArrayList<String> gender = new ArrayList<String>();
//	ArrayList<String> name = new ArrayList<String>();
    String[] _gender;

    ProgressDialog pd;
    boolean settingDone = false;

    private class PageLoadGetTime extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AppConfiguration.studentsize = AppConfiguration.selectedStudentsName.size();

            for (int i = 0; i < AppConfiguration.studentsize; i++) {
                if (i == 0) {
                    strstudentarry1 = check(AppConfiguration.Checked_Bool, i);
                } else if (i == 1) {
                    strstudentarry1 = check(AppConfiguration.Checked_Bool_2, i);
                } else if (i == 2) {
                    strstudentarry1 = check(AppConfiguration.Checked_Bool_3, i);
                } else if (i == 3) {
                    strstudentarry1 = check(AppConfiguration.Checked_Bool_4, i);
                } else if (i == 4) {
                    strstudentarry1 = check(AppConfiguration.Checked_Bool_5, i);
                }
            }

            strstudentarry1 = strstudentarry1.trim();

            strstudentarry1 = strstudentarry1.substring(0, strstudentarry.length() - 1);
        }

        @Override
        protected Void doInBackground(Void... params1) {
            // TODO Auto-generated method stub


            LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("Token", token);
            params.put("Makeupflg", AppConfiguration.makeUpFlag);

            if (AppConfiguration.makeUpFlag == "1") {
                params.put("calstartdate", Utility.getTodaysDate());
                Log.d("@@@@@@@@@@@@@@@@@@@@@", Utility.getTodaysDate());
                params.put("calenddate", Utility.getScheduleMakeupEndDate());

            } else {
                params.put("calstartdate", AppConfiguration.d2_startDate);
                params.put("calenddate", AppConfiguration.d2_endDate);

            }

            params.put("siteid",
                    AppConfiguration.salStep1SiteID);
            params.put("strstudentarry",
                    strstudentarry1);
            params.put("intrlist",
                    instructorListBuilderNew);
            params.put("reserveforever",
                    AppConfiguration.reserverForever);
            params.put("Type", "2");
            params.put("schedulechoices", AppConfiguration.schedulechoices);//AppConfiguration.schedulechoices
            params.put("scheduletype", String.valueOf(AppConfiguration.makeup_Clicked));
            params.put("SelectStudList", "");
            params.put("pair1Check", AppConfiguration.pair1Check);
            params.put("pair2Check", AppConfiguration.pair2Check);
            params.put("pair1lessontype", AppConfiguration.pair1lessontype);
            params.put("pair2lessontype", AppConfiguration.pair2lessontype);

            if (!AppConfiguration.pair1_comboValue1.equalsIgnoreCase("0")) {
                params.put("pair1_Cmbo1", AppConfiguration.pair1_comboValue1 + ":" + AppConfiguration.pair1_Cmbo1.split(" ")[0]);
            } else {
                params.put("pair1_Cmbo1", AppConfiguration.pair1_comboValue1);
            }
            if (!AppConfiguration.pair1_comboValue2.equalsIgnoreCase("0")) {
                params.put("pair1_Cmbo2", AppConfiguration.pair1_comboValue2 + ":" + AppConfiguration.pair2_Cmbo1.split(" ")[0]);
            } else {
                params.put("pair1_Cmbo2", AppConfiguration.pair1_comboValue2);
            }
            if (!AppConfiguration.pair1_comboValue3.equalsIgnoreCase("0")) {
                params.put("pair1_Cmbo3", AppConfiguration.pair1_comboValue3 + ":" + AppConfiguration.pair3_Cmbo1.split(" ")[0]);
            } else {
                params.put("pair1_Cmbo3", AppConfiguration.pair1_comboValue3);
            }
            if (!AppConfiguration.pair1_comboValue4.equalsIgnoreCase("0")) {
                params.put("pair1_Cmbo4", AppConfiguration.pair1_comboValue4 + ":" + AppConfiguration.pair4_Cmbo1.split(" ")[0]);
            } else {
                params.put("pair1_Cmbo4", AppConfiguration.pair1_comboValue4);
            }
            if (!AppConfiguration.pair2_comboValue1.equalsIgnoreCase("0")) {
                params.put("pair2_Cmbo1", AppConfiguration.pair2_comboValue1 + ":" + AppConfiguration.pair1_Cmbo2.split(" ")[0]);
            } else {
                params.put("pair2_Cmbo1", AppConfiguration.pair2_comboValue1);
            }
            if (!AppConfiguration.pair2_comboValue2.equalsIgnoreCase("0")) {
                params.put("pair2_Cmbo2", AppConfiguration.pair2_comboValue2 + ":" + AppConfiguration.pair2_Cmbo2.split(" ")[0]);
            } else {
                params.put("pair2_Cmbo2", AppConfiguration.pair2_comboValue2);
            }
            if (!AppConfiguration.pair2_comboValue3.equalsIgnoreCase("0")) {
                params.put("pair2_Cmbo3", AppConfiguration.pair2_comboValue3 + ":" + AppConfiguration.pair3_Cmbo2.split(" ")[0]);
            } else {
                params.put("pair2_Cmbo3", AppConfiguration.pair2_comboValue3);
            }
            if (!AppConfiguration.pair2_comboValue4.equalsIgnoreCase("0")) {
                params.put("pair2_Cmbo4", AppConfiguration.pair2_comboValue4 + ":" + AppConfiguration.pair4_Cmbo2.split(" ")[0]);
            } else {
                params.put("pair2_Cmbo4", AppConfiguration.pair2_comboValue4);
            }

            params.put("pair1_InstrList", AppConfiguration.pair1_InstrList);
            params.put("pair2_InstrList", AppConfiguration.pair2_InstrList);

            if (AppConfiguration.pair1_DayTime.trim().length() > 0) {
                AppConfiguration.pair1_DayTime = AppConfiguration.pair1_DayTime.substring(0, AppConfiguration.pair1_DayTime.length() - 1);
                params.put("pair1_DayTime", AppConfiguration.pair1_DayTime);
            } else {
                params.put("pair1_DayTime", AppConfiguration.pair1_DayTime);
            }

            if (AppConfiguration.pair2_DayTime.trim().length() > 0) {
                AppConfiguration.pair2_DayTime = AppConfiguration.pair2_DayTime.substring(0, AppConfiguration.pair2_DayTime.length() - 1);
                params.put("pair2_DayTime", AppConfiguration.pair2_DayTime);

            } else {
                params.put("pair2_DayTime", AppConfiguration.pair2_DayTime);
            }

            HashMap<String, String> param_sp = new HashMap<String, String>();
            System.out.println("Step4 : " + params);
            param_sp = params;
            String responseString = WebServicesCall.RunScript(AppConfiguration.Schl_Get_Step3_AddSchedule, param_sp);
            try {
                JSONObject reader = new JSONObject(responseString);
                gettimes = reader.getString("Success");
                if (gettimes.toString().equals("True")) {
                    JSONArray jsonMainNode, jsonMainNode2, jsonMainNode3, jsonMainNode4, jsonMainNode5;

                    jsonMainNode = reader.optJSONArray("ClassAvailList1");
                    ClassAvailList1 = getValues(jsonMainNode, 0);

                    jsonMainNode2 = reader.optJSONArray("ClassAvailList2");
                    ClassAvailList2 = getValues(jsonMainNode2, 1);

                    jsonMainNode3 = reader.optJSONArray("ClassAvailList3");
                    ClassAvailList3 = getValues(jsonMainNode3, 2);

                    jsonMainNode4 = reader.optJSONArray("ClassAvailList4");
                    ClassAvailList4 = getValues(jsonMainNode4, 3);

                    jsonMainNode5 = reader.optJSONArray("ClassAvailList5");
                    ClassAvailList5 = getValues(jsonMainNode5, 4);

                } else {
                    JSONArray jsonMainNode = reader
                            .optJSONArray("ClassAvailList");
                    JSONObject jsonObject = jsonMainNode
                            .getJSONObject(0);
                    Msg = jsonObject.getString("Msg");
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
            if (gettimes.toString().equalsIgnoreCase("True")) {

                if (ClassAvailList1.size() > 0) {
                    studentLaySet(ClassAvailList1, ll_schedule3_body, 0);
                }
                if (ClassAvailList2.size() > 0) {
                    studentLaySet(ClassAvailList2, ll_schedule3_body_2, 1);
                }

                if (ClassAvailList3.size() > 0) {
                    studentLaySet(ClassAvailList3, ll_schedule3_body_3, 2);
                }

                if (ClassAvailList4.size() > 0) {
                    studentLaySet(ClassAvailList4, ll_schedule3_body_4, 3);
                }

                if (ClassAvailList5.size() > 0) {
                    studentLaySet(ClassAvailList5, ll_schedule3_body_5, 4);
                }

                settingDone = true;
                filter_icon.setVisibility(View.VISIBLE);
            } else {
                NoRecord_alert.setVisibility(View.VISIBLE);
                btn_Prev_card.setVisibility(View.VISIBLE);
                begin_new_card.setVisibility(View.VISIBLE);
                btn_next_card.setVisibility(View.GONE);
                filter_icon.setVisibility(View.GONE);

                //Toast.makeText(mContext, "Records not found...", Toast.LENGTH_SHORT).show();
                settingDone = true;
            }

            if (pd != null) {
                pd.dismiss();
            }
        }
    }

    ArrayList<String> sun_selection = new ArrayList<String>();
    ArrayList<String> mon_selection = new ArrayList<String>();
    ArrayList<String> tue_selection = new ArrayList<String>();
    ArrayList<String> wed_selection = new ArrayList<String>();
    ArrayList<String> thu_selection = new ArrayList<String>();
    ArrayList<String> fri_selection = new ArrayList<String>();
    ArrayList<String> sat_selection = new ArrayList<String>();
    String selectedInstructor = "";

    @SuppressWarnings("deprecation")
    public void studentLaySet(final ArrayList<ArrayList<ArrayList<String>>> ClassAvailList1Local, LinearLayout ll_body, int count) {
        try {

            ArrayList<String> tempArray = new ArrayList<String>();

            if (count == 0) {
                tempArray = instructors;
            } else if (count == 1) {
                tempArray = instructors2;
            } else if (count == 2) {
                tempArray = instructors3;
            } else if (count == 3) {
                tempArray = instructors4;
            } else if (count == 4) {
                tempArray = instructors5;
            }

            String currentInst = "";
            String lastValue = "";
            for (int k = 0; k < tempArray.size(); k++) {
                currentInst = tempArray.get(k);

                int original = k;
//                if (!tempArray.get(k).toString().equals(tempArray.get(k + 1))) {
                if (!currentInst.equalsIgnoreCase(lastValue) && ClassAvailList1Local.get(0).get(0).contains(currentInst)) {
                    System.err.println("Loop number : " + k);

                    if (count == 0) {
                        instructorList1.add(tempArray.get(k).toString());
//                        selectedStudInsList = instructorList1;
                    } else if (count == 1) {
                        instructorList2.add(tempArray.get(k).toString());
                    } else if (count == 2) {
                        instructorList3.add(tempArray.get(k).toString());
                    } else if (count == 3) {
                        instructorList4.add(tempArray.get(k).toString());
                    } else if (count == 4) {
                        instructorList5.add(tempArray.get(k).toString());
                    }

                    final int index = 0;
                    k = 0;

                    LinearLayout changing_lay, ll_sun = null, ll_mon = null, ll_tue = null, ll_wed = null, ll_thu = null, ll_fri = null, ll_sat = null;
                    TextView tv_sun, tv_mon, tv_tue, tv_wed, tv_thu, tv_fri, tv_sat, tv_instructorname;
                    View _childView = null;
                    final CircleImageView inst_DP;
                    final TextView view_bioo, view_shiftt;
                    final Button view_bio, view_shift;
                    _childView = getLayoutInflater().inflate(R.layout.d2_inflate_schedule3_body, ll_body, false);
                    //				tv_instructorname = (TextView) _childView
                    //						.findViewById(R.id.tv_schedule3_instructorname);
                    tv_instructorname = (TextView) _childView
                            .findViewById(R.id.inst_name);
                    changing_lay = (LinearLayout) _childView
                            .findViewById(R.id.changing_lay);
                    inst_DP = (CircleImageView) _childView
                            .findViewById(R.id.inst_DP);

//                view_bio = (TextView) _childView
//                        .findViewById(R.id.view_bio);
                    view_bio = (Button) _childView
                            .findViewById(R.id.view_bio);
                    view_shift = (Button) _childView
                            .findViewById(R.id.view_shift);
                    tv_sun = (TextView) _childView
                            .findViewById(R.id.tv_sundaydate);
                    tv_mon = (TextView) _childView
                            .findViewById(R.id.tv_mondaydate);
                    tv_tue = (TextView) _childView
                            .findViewById(R.id.tv_tuedaydate);
                    tv_wed = (TextView) _childView
                            .findViewById(R.id.tv_weddaydate);
                    tv_thu = (TextView) _childView
                            .findViewById(R.id.tv_thudaydate);
                    tv_fri = (TextView) _childView
                            .findViewById(R.id.tv_fridaydate);
                    tv_sat = (TextView) _childView
                            .findViewById(R.id.tv_satdaydate);
                    ll_sun = (LinearLayout) _childView
                            .findViewById(R.id.ll_sunday_body);
                    ll_mon = (LinearLayout) _childView
                            .findViewById(R.id.ll_monday_body);
                    ll_tue = (LinearLayout) _childView
                            .findViewById(R.id.ll_tueday_body);
                    ll_wed = (LinearLayout) _childView
                            .findViewById(R.id.ll_wedday_body);
                    ll_thu = (LinearLayout) _childView
                            .findViewById(R.id.ll_thuday_body);
                    ll_fri = (LinearLayout) _childView
                            .findViewById(R.id.ll_friday_body);
                    ll_sat = (LinearLayout) _childView
                            .findViewById(R.id.ll_satday_body);
                    tv_instructorname.setText(tempArray.get(original));


                    if (instNamePic.containsKey(tempArray.get(original))) {
                        if (instNamePic.get(tempArray.get(original)) != null
                                && instNamePic.get(tempArray.get(original)).trim().length() > 0) {
                            imageLoader.displayImage(AppConfiguration.getFullDpURL(instNamePic.get(tempArray.get(original)))
                                    , inst_DP);
                        }
                    }

                    view_shift.setTag(ScheduleLessonFragement2.instNameID.get(tempArray.get(original))
                            + ":" + tempArray.get(original));

                    view_bio.setTag(ScheduleLessonFragement2.instNameID.get(tempArray.get(original)));

                    System.out.println("Bio Tag: " + ScheduleLessonFragement2.instNameID.get(tempArray.get(original)));

                    if (AppConfiguration.gender_arry.get(tempArray.get(original)).equalsIgnoreCase("F")) {
                        changing_lay.setBackgroundResource(R.drawable.female_selector_bg);
                    } else {
                        changing_lay.setBackgroundResource(R.drawable.male_selector_bg);
                    }

                    view_bio.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            selectedInstructor = v.getTag().toString();
//                        new getBio().execute();
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    new getBio().execute();
                                }
                            }, 1000);

                        }
                    });
                    view_shift.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            selectedInstructor = v.getTag().toString();

//                        new InstrcutorAvailalibityAsyncTask().execute();
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    new InstrcutorAvailalibityAsyncTask().execute();
                                }
                            }, 500);

                        }
                    });


                    tv_sat.setText("Sat");
                    tv_sun.setText("Sun");
                    tv_mon.setText("Mon");
                    tv_tue.setText("Tue");
                    tv_wed.setText("Wed");
                    tv_thu.setText("Thu");
                    tv_fri.setText("Fri");
                    //						}

                    LinearLayout v = null;
                /*----------------------------sun--------------------------*/

                    v = ll_sun;
                    for (int i = 0; i < ClassAvailList1Local.get(index).get(25)
                            .size(); i++) {
                        // sun_selection.add("-1");
                        final int pos = i;
                        final View childView = getLayoutInflater().inflate(
                                R.layout.d2_schedule_a_lesson_body_item, v,
                                false);
                        if (!ClassAvailList1Local.get(k).get(25).get(i)
                                .isEmpty() && ClassAvailList1Local.get(k).get(28)
                                .get(i).toString().contains(currentInst)) {
                            System.out.println("Set : " + i);
                            final CheckBox ch_time = (CheckBox) childView
                                    .findViewById(R.id.chkScheduleTime);
                            ch_time.setId(i);
                            try {
                                String strDate = ClassAvailList1Local.get(k)
                                        .get(25).get(i);
                                SimpleDateFormat sdf1 = new SimpleDateFormat(
                                        "hh:mm:ss a", Locale.US);
                                SimpleDateFormat sdf2 = new SimpleDateFormat(
                                        "hh:mm a", Locale.US);
                                Date date = sdf1.parse(strDate.toLowerCase());
                                ch_time.setText(Html.fromHtml("" + sdf2.format(date).toLowerCase() + "\n" + "<br>" + "<b>" + ClassAvailList1Local.get(k).get(26).get(i) + "</b>"));
                                //									ch_time.setText("" + sdf2.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ch_time.setTag(ClassAvailList1Local.get(k).get(28)
                                    .get(i));
                            ch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    // TODO Auto-generated method stub

                                    if (isChecked) {
                                        //									Rakesh 20112015............
                                        Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                        childView.startAnimation(animFadeIn);
                                        if (select_1.getVisibility() == View.VISIBLE) {
                                            st_1.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_2.getVisibility() == View.VISIBLE) {
                                            st_2.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_3.getVisibility() == View.VISIBLE) {
                                            st_3.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_4.getVisibility() == View.VISIBLE) {
                                            st_4.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_5.getVisibility() == View.VISIBLE) {
                                            st_5.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        ch_time.setTextColor(getResources().getColor(R.color.white));

                                        if (sun_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(28)
                                                        .get(pos)
                                                        .toString())) {

                                        } else {
                                            sun_selection
                                                    .add(ClassAvailList1Local
                                                            .get(index)
                                                            .get(28)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    } else {
                                        ch_time.setTextColor(getResources().getColor(R.color.black));
                                        if (sun_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(28)
                                                        .get(pos)
                                                        .toString())) {
                                            sun_selection
                                                    .remove(ClassAvailList1Local
                                                            .get(index)
                                                            .get(28)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    }
                                }
                            });
                            v.addView(childView);
                        }
                    }

				/*----------------------------mon--------------------------*/
                    v = ll_mon;
                    for (int i = 0; i < ClassAvailList1Local.get(k).get(1)
                            .size(); i++) {
                        // mon_selection.add("-1");
                        final int pos = i;
                        final View childView = getLayoutInflater().inflate(
                                R.layout.d2_schedule_a_lesson_body_item, v,
                                false);
                        if (!ClassAvailList1Local.get(k).get(1).get(i).isEmpty() &&
                                ClassAvailList1Local.get(k).get(4)
                                        .get(i).toString().contains(currentInst)) {
                            final CheckBox ch_time = (CheckBox) childView
                                    .findViewById(R.id.chkScheduleTime);
                            ch_time.setId(i);
                            try {
                                String strDate = ClassAvailList1Local.get(k)
                                        .get(1).get(i);
                                SimpleDateFormat sdf1 = new SimpleDateFormat(
                                        "hh:mm:ss a", Locale.US);
                                SimpleDateFormat sdf2 = new SimpleDateFormat(
                                        "hh:mm a", Locale.US);
                                Date date = sdf1.parse(strDate.toLowerCase());
                                ch_time.setText(Html.fromHtml("" + sdf2.format(date).toLowerCase() + "\n" + "<b>" + ClassAvailList1Local.get(k).get(2).get(i) + "</b>"));
                                //									ch_time..setText("" + sdf2.format(date) + "\n" + "<b>"+ClassAvailList1Local.get(k).get(2).get(i));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ch_time.setTag(ClassAvailList1Local.get(k).get(4)
                                    .get(i));
                            ch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    // TODO Auto-generated method stub

                                    if (isChecked) {
                                        //									Rakesh 20112015............
                                        Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                        childView.startAnimation(animFadeIn);
                                        if (select_1.getVisibility() == View.VISIBLE) {
                                            st_1.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_2.getVisibility() == View.VISIBLE) {
                                            st_2.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_3.getVisibility() == View.VISIBLE) {
                                            st_3.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_4.getVisibility() == View.VISIBLE) {
                                            st_4.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_5.getVisibility() == View.VISIBLE) {
                                            st_5.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        ch_time.setTextColor(getResources().getColor(R.color.white));
                                        if (mon_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(4)
                                                        .get(pos)
                                                        .toString())) {

                                        } else {
                                            mon_selection
                                                    .add(ClassAvailList1Local
                                                            .get(index)
                                                            .get(4)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    } else {
                                        ch_time.setTextColor(getResources().getColor(R.color.black));
                                        if (mon_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(4)
                                                        .get(pos)
                                                        .toString())) {
                                            mon_selection
                                                    .remove(ClassAvailList1Local
                                                            .get(index)
                                                            .get(4)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    }
                                }
                            });
                            v.addView(childView);
                        }
                    }

				/*----------------------------tue--------------------------*/
                    v = ll_tue;
                    for (int i = 0; i < ClassAvailList1Local.get(k).get(5)
                            .size(); i++) {
                        // tue_selection.add("-1");
                        final int pos = i;
                        final View childView = getLayoutInflater().inflate(
                                R.layout.d2_schedule_a_lesson_body_item, v,
                                false);
                        if (!ClassAvailList1Local.get(k).get(5).get(i).isEmpty() &&
                                ClassAvailList1Local.get(k).get(8)
                                        .get(i).toString().contains(currentInst)) {
                            final CheckBox ch_time = (CheckBox) childView
                                    .findViewById(R.id.chkScheduleTime);
                            ch_time.setId(i);
                            try {
                                String strDate = ClassAvailList1Local.get(k)
                                        .get(5).get(i).toLowerCase();
                                SimpleDateFormat sdf1 = new SimpleDateFormat(
                                        "hh:mm:ss a", Locale.US);
                                SimpleDateFormat sdf2 = new SimpleDateFormat(
                                        "hh:mm a", Locale.US);
                                Date date = sdf1.parse(strDate.toLowerCase());
                                ch_time.setText(Html.fromHtml("" + sdf2.format(date).toLowerCase() + "\n" + "<b>" + ClassAvailList1Local.get(k).get(6).get(i) + "</b>"));
                                //									ch_time.setText("" + sdf2.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ch_time.setTag(ClassAvailList1Local.get(k).get(8)
                                    .get(i));
                            ch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    // TODO Auto-generated method stub

                                    if (isChecked) {
                                        //			Rakesh 20112015............
                                        Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                        childView.startAnimation(animFadeIn);

                                        if (select_1.getVisibility() == View.VISIBLE) {
                                            st_1.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_2.getVisibility() == View.VISIBLE) {
                                            st_2.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_3.getVisibility() == View.VISIBLE) {
                                            st_3.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_4.getVisibility() == View.VISIBLE) {
                                            st_4.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_5.getVisibility() == View.VISIBLE) {
                                            st_5.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        ch_time.setTextColor(getResources().getColor(R.color.white));
                                        if (tue_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(8)
                                                        .get(pos)
                                                        .toString())) {

                                        } else {
                                            tue_selection
                                                    .add(ClassAvailList1Local
                                                            .get(index)
                                                            .get(8)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    } else {
                                        ch_time.setTextColor(getResources().getColor(R.color.black));
                                        if (tue_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(8)
                                                        .get(pos)
                                                        .toString())) {
                                            tue_selection
                                                    .remove(ClassAvailList1Local
                                                            .get(index)
                                                            .get(8)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    }
                                }
                            });
                            v.addView(childView);
                        }
                    }

				/*----------------------------wed--------------------------*/
                    v = ll_wed;
                    for (int i = 0; i < ClassAvailList1Local.get(k).get(9)
                            .size(); i++) {
                        // wed_selection.add("-1");
                        final int pos = i;
                        final View childView = getLayoutInflater().inflate(
                                R.layout.d2_schedule_a_lesson_body_item, v,
                                false);
                        if (!ClassAvailList1Local.get(k).get(9).get(i).isEmpty() &&
                                ClassAvailList1Local.get(k).get(12)
                                        .get(i).toString().contains(currentInst)) {
                            final CheckBox ch_time = (CheckBox) childView
                                    .findViewById(R.id.chkScheduleTime);
                            ch_time.setId(i);
                            try {
                                String strDate = ClassAvailList1Local.get(k)
                                        .get(9).get(i).toLowerCase();
                                SimpleDateFormat sdf1 = new SimpleDateFormat(
                                        "hh:mm:ss a", Locale.US);
                                SimpleDateFormat sdf2 = new SimpleDateFormat(
                                        "hh:mm a", Locale.US);
                                Date date = sdf1.parse(strDate.toLowerCase());
                                ch_time.setText(Html.fromHtml("" + sdf2.format(date).toLowerCase() + "\n" + "<b>" + ClassAvailList1Local.get(k).get(10).get(i) + "</b>"));
                                //									ch_time.setText("" + sdf2.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ch_time.setTag(ClassAvailList1Local.get(k).get(12)
                                    .get(i));
                            ch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    // TODO Auto-generated method stub

                                    if (isChecked) {
                                        //			Rakesh 20112015............
                                        Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                        childView.startAnimation(animFadeIn);
                                        if (select_1.getVisibility() == View.VISIBLE) {
                                            st_1.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_2.getVisibility() == View.VISIBLE) {
                                            st_2.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_3.getVisibility() == View.VISIBLE) {
                                            st_3.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_4.getVisibility() == View.VISIBLE) {
                                            st_4.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_5.getVisibility() == View.VISIBLE) {
                                            st_5.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        ch_time.setTextColor(getResources().getColor(R.color.white));
                                        if (wed_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(12)
                                                        .get(pos)
                                                        .toString())) {

                                        } else {
                                            wed_selection
                                                    .add(ClassAvailList1Local
                                                            .get(index)
                                                            .get(12)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    } else {
                                        ch_time.setTextColor(getResources().getColor(R.color.black));
                                        if (wed_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(12)
                                                        .get(pos)
                                                        .toString())) {
                                            wed_selection
                                                    .remove(ClassAvailList1Local
                                                            .get(index)
                                                            .get(12)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    }
                                }
                            });
                            v.addView(childView);
                        }
                    }

				/*----------------------------thu--------------------------*/
                    v = ll_thu;
                    for (int i = 0; i < ClassAvailList1Local.get(k).get(13)
                            .size(); i++) {
                        // thu_selection.add("-1");
                        final int pos = i;
                        final View childView = getLayoutInflater().inflate(
                                R.layout.d2_schedule_a_lesson_body_item, v,
                                false);
                        if (!ClassAvailList1Local.get(k).get(13).get(i)
                                .isEmpty() && ClassAvailList1Local.get(k).get(16)
                                .get(i).toString().contains(currentInst)) {
                            final CheckBox ch_time = (CheckBox) childView
                                    .findViewById(R.id.chkScheduleTime);
                            ch_time.setId(i);
                            try {
                                String strDate = ClassAvailList1Local.get(k)
                                        .get(13).get(i).toLowerCase();
                                SimpleDateFormat sdf1 = new SimpleDateFormat(
                                        "hh:mm:ss a", Locale.US);
                                SimpleDateFormat sdf2 = new SimpleDateFormat(
                                        "hh:mm a", Locale.US);
                                Date date = sdf1.parse(strDate.toLowerCase());
                                ch_time.setText(Html.fromHtml("" + sdf2.format(date).toLowerCase() + "\n" + "<b>" + ClassAvailList1Local.get(k).get(14).get(i) + "</b>"));
                                //									ch_time.setText("" + sdf2.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ch_time.setTag(ClassAvailList1Local.get(k).get(16)
                                    .get(i));
                            ch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    // TODO Auto-generated method stub

                                    if (isChecked) {
                                        //				Rakesh 20112015............
                                        Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                        childView.startAnimation(animFadeIn);
                                        if (select_1.getVisibility() == View.VISIBLE) {
                                            st_1.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_2.getVisibility() == View.VISIBLE) {
                                            st_2.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_3.getVisibility() == View.VISIBLE) {
                                            st_3.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_4.getVisibility() == View.VISIBLE) {
                                            st_4.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_5.getVisibility() == View.VISIBLE) {
                                            st_5.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        ch_time.setTextColor(getResources().getColor(R.color.white));
                                        if (thu_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(16)
                                                        .get(pos)
                                                        .toString())) {

                                        } else {
                                            thu_selection
                                                    .add(ClassAvailList1Local
                                                            .get(index)
                                                            .get(16)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    } else {
                                        ch_time.setTextColor(getResources().getColor(R.color.black));
                                        if (thu_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(16)
                                                        .get(pos)
                                                        .toString())) {
                                            thu_selection
                                                    .remove(ClassAvailList1Local
                                                            .get(index)
                                                            .get(16)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    }
                                }
                            });
                            v.addView(childView);
                        }
                    }

				/*----------------------------fri--------------------------*/
                    v = ll_fri;
                    for (int i = 0; i < ClassAvailList1Local.get(k).get(17)
                            .size(); i++) {
                        // fri_selection.add("-1");
                        final int pos = i;
                        final View childView = getLayoutInflater().inflate(
                                R.layout.d2_schedule_a_lesson_body_item, v,
                                false);
                        if (!ClassAvailList1Local.get(k).get(17).get(i)
                                .isEmpty() && ClassAvailList1Local.get(k).get(20)
                                .get(i).toString().contains(currentInst)) {
                            final CheckBox ch_time = (CheckBox) childView
                                    .findViewById(R.id.chkScheduleTime);
                            ch_time.setId(i);
                            try {
                                String strDate = ClassAvailList1Local.get(k)
                                        .get(17).get(i);
                                SimpleDateFormat sdf1 = new SimpleDateFormat(
                                        "hh:mm:ss a", Locale.US);
                                SimpleDateFormat sdf2 = new SimpleDateFormat(
                                        "hh:mm a", Locale.US);
                                Date date = sdf1.parse(strDate.toLowerCase());
                                ch_time.setText(Html.fromHtml("" + sdf2.format(date).toLowerCase() + "\n" + "<b>" + ClassAvailList1Local.get(k).get(18).get(i) + "</b>"));
                                //									ch_time.setText("" + sdf2.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ch_time.setTag(ClassAvailList1Local.get(k).get(20)
                                    .get(i));
                            ch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    // TODO Auto-generated method stub

                                    if (isChecked) {
                                        //					Rakesh 20112015............
                                        Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                        childView.startAnimation(animFadeIn);
                                        if (select_1.getVisibility() == View.VISIBLE) {
                                            st_1.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_2.getVisibility() == View.VISIBLE) {
                                            st_2.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_3.getVisibility() == View.VISIBLE) {
                                            st_3.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_4.getVisibility() == View.VISIBLE) {
                                            st_4.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_5.getVisibility() == View.VISIBLE) {
                                            st_5.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        ch_time.setTextColor(getResources().getColor(R.color.white));
                                        if (fri_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(20)
                                                        .get(pos)
                                                        .toString())) {

                                        } else {
                                            fri_selection
                                                    .add(ClassAvailList1Local
                                                            .get(index)
                                                            .get(20)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    } else {
                                        ch_time.setTextColor(getResources().getColor(R.color.black));
                                        if (fri_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(20)
                                                        .get(pos)
                                                        .toString())) {
                                            fri_selection
                                                    .remove(ClassAvailList1Local
                                                            .get(index)
                                                            .get(20)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    }
                                }
                            });
                            v.addView(childView);
                        }
                    }

				/*----------------------------sat--------------------------*/
                    v = ll_sat;
                    for (int i = 0; i < ClassAvailList1Local.get(k).get(21)
                            .size(); i++) {
                        // sat_selection.add("-1");
                        final int pos = i;
                        final View childView = getLayoutInflater().inflate(
                                R.layout.d2_schedule_a_lesson_body_item, v,
                                false);
                        if (!ClassAvailList1Local.get(k).get(21).get(i)
                                .isEmpty() && ClassAvailList1Local.get(k).get(24)
                                .get(i).toString().contains(currentInst)) {
                            final CheckBox ch_time = (CheckBox) childView
                                    .findViewById(R.id.chkScheduleTime);
                            ch_time.setId(i);
                            try {
                                String strDate = ClassAvailList1Local.get(k)
                                        .get(21).get(i);
                                SimpleDateFormat sdf1 = new SimpleDateFormat(
                                        "hh:mm:ss a", Locale.US);
                                SimpleDateFormat sdf2 = new SimpleDateFormat(
                                        "hh:mm a", Locale.US);
                                Date date = sdf1.parse(strDate.toLowerCase());
                                ch_time.setText(Html.fromHtml("" + sdf2.format(date).toLowerCase() + "\n" + "<b>" + ClassAvailList1Local.get(k).get(22).get(i) + "</b>"));
                                //									ch_time.setText("" + sdf2.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ch_time.setTag(ClassAvailList1Local.get(k).get(24)
                                    .get(i));
                            ch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView,
                                        boolean isChecked) {
                                    // TODO Auto-generated method stub

                                    if (isChecked) {
                                        //			Rakesh 20112015............
                                        Animation animFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                                        childView.startAnimation(animFadeIn);
                                        if (select_1.getVisibility() == View.VISIBLE) {
                                            st_1.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_2.getVisibility() == View.VISIBLE) {
                                            st_2.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_3.getVisibility() == View.VISIBLE) {
                                            st_3.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_4.getVisibility() == View.VISIBLE) {
                                            st_4.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        if (select_5.getVisibility() == View.VISIBLE) {
                                            st_5.setBackgroundResource(R.drawable.error_border_white);
                                        }
                                        ch_time.setTextColor(getResources().getColor(R.color.white));
                                        if (sat_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(24)
                                                        .get(pos)
                                                        .toString())) {

                                        } else {
                                            sat_selection
                                                    .add(ClassAvailList1Local
                                                            .get(index)
                                                            .get(24)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    } else {
                                        ch_time.setTextColor(getResources().getColor(R.color.black));
                                        if (sat_selection
                                                .contains(ClassAvailList1Local
                                                        .get(index).get(24)
                                                        .get(pos)
                                                        .toString())) {
                                            sat_selection
                                                    .remove(ClassAvailList1Local
                                                            .get(index)
                                                            .get(24)
                                                            .get(pos)
                                                            .toString());
                                        }
                                    }
                                }
                            });
                            v.addView(childView);
                        }
                    }
                    ll_body.addView(_childView);
                }
                k = original;
                lastValue = currentInst;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean disableIT = false;

    //  Rakesh  19112015
    public void SelectInstructorDialog() {

        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        CardView button_back;

        button_back = (CardView) layout.findViewById(R.id.button_back);
        button_back.setVisibility(View.VISIBLE);

        button_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                if (AppConfiguration.st_Student1.trim().length() <= 0) {
                    st_1.setBackgroundResource(R.drawable.error_border);
                }
                if (AppConfiguration.st_Student2.trim().length() <= 0) {
                    st_2.setBackgroundResource(R.drawable.error_border);
                }
                if (AppConfiguration.st_Student3.trim().length() <= 0) {
                    st_3.setBackgroundResource(R.drawable.error_border);
                }
                if (AppConfiguration.st_Student4.trim().length() <= 0) {
                    st_4.setBackgroundResource(R.drawable.error_border);
                }
                if (AppConfiguration.st_Student5.trim().length() <= 0) {
                    st_5.setBackgroundResource(R.drawable.error_border);
                }


                if (AppConfiguration.st_Student1.trim().length() <= 0) {
                    //st_1.setBackgroundResource(R.drawable.error_border);
                    if (AppConfiguration.st_Student2.trim().length() > 0 || AppConfiguration.st_Student3.trim().length() > 0 || AppConfiguration.st_Student4.trim().length() > 0 || AppConfiguration.st_Student5.trim().length() > 0) {
                        //selectTimeEvent1Click(); navin 15-04-2016
                        st_1.performClick();
                    }
                } else if (AppConfiguration.st_Student2.trim().length() <= 0) {
                    // st_2.setBackgroundResource(R.drawable.error_border);
                    if (AppConfiguration.st_Student1.trim().length() > 0 || AppConfiguration.st_Student3.trim().length() > 0 || AppConfiguration.st_Student4.trim().length() > 0 || AppConfiguration.st_Student5.trim().length() > 0) {
                        //selectTimeEvent2Click();
                        st_2.performClick();
                    }
                } else if (AppConfiguration.st_Student3.trim().length() <= 0) {
                    // st_3.setBackgroundResource(R.drawable.error_border);
                    if (AppConfiguration.st_Student2.trim().length() <= 0 || AppConfiguration.st_Student1.trim().length() <= 0 || AppConfiguration.st_Student4.trim().length() <= 0 || AppConfiguration.st_Student5.trim().length() <= 0) {

                        //selectTimeEvent3Click();
                        st_3.performClick();
                    }
                } else if (AppConfiguration.st_Student4.trim().length() <= 0) {
                    // st_4.setBackgroundResource(R.drawable.error_border);
                    if (AppConfiguration.st_Student2.trim().length() <= 0 || AppConfiguration.st_Student3.trim().length() <= 0 || AppConfiguration.st_Student1.trim().length() <= 0 || AppConfiguration.st_Student5.trim().length() <= 0) {
                        //selectTimeEvent4Click();
                        st_4.performClick();
                    }
                } else if (AppConfiguration.st_Student5.trim().length() <= 0) {
                    // st_5.setBackgroundResource(R.drawable.error_border);
                    if (AppConfiguration.st_Student2.trim().length() <= 0 || AppConfiguration.st_Student3.trim().length() <= 0 || AppConfiguration.st_Student4.trim().length() <= 0 || AppConfiguration.st_Student1.trim().length() <= 0) {
                        //selectTimeEvent5Click();
                        st_5.performClick();
                    }
                }
            }
        });

        scrollView.smoothScrollTo(0, 0);

        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Alert");
        tv_appname.setTextColor(Color.RED);

        TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
        tv_description.setText(
                "Lesson times have not been selected for all students.\nAre you sure you want to continue? ");
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
        TextView tv_btntxt = (TextView) layout.findViewById(R.id.tv_btntxt);
//        button_ok.setText("Continue");
        tv_btntxt.setText("Go Back");
        if (continue_disable) {
            button_ok.setEnabled(false);
            button_ok.setBackgroundResource(R.color.gray_light);
        } else {
            button_ok.setEnabled(true);
        }
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                if (popWindow.isShowing() == false) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ShedulingFilter.fromFilter = false;
//                            ShedulingFilter.fromAdvancedFilter = false;
                            Intent i = new Intent(mContext, ScheduleLessonFragement5.class);
                            startActivity(i);
                        }
                    }, 600);
                }

            }
        });
    }

    ArrayList<HashMap<String, String>> aboutInst = new ArrayList<HashMap<String, String>>();
    String successLoadChildList;

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

//            pd = new ProgressDialog(ScheduleLessonFragement4.this);
//            pd.setMessage(getResources().getString(R.string.pleasewait));
//            pd.setCancelable(false);
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
                View layout = lInflater.inflate(R.layout.inst_bio_pop_up_layout, null);
                final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                popWindow.setAnimationStyle(R.style.Animation);
                Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
                slide_up.setDuration(1);
                popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

                LinearLayout pop_square;
                pop_square = (LinearLayout) layout.findViewById(R.id.pop_square);
                pop_square.startAnimation(slide_up);

                TextView tv_appname, inst_name, about, done1;
                CircleImageView inst_dp;
                Button done;
                done = (Button) layout.findViewById(R.id.done);
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

    //Inst. Avaibility
    ArrayList<HashMap<String, String>> instructorAvailableList = new ArrayList<HashMap<String, String>>();

    class InstrcutorAvailalibityAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(ScheduleLessonFragement4.this);
//            pd.setMessage(getResources().getString(R.string.pleasewait));
//            pd.setCancelable(false);
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
//                Animation animSlidup = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
//                layout.startAnimation(animSlidup);
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
                        done1;
                Button done;
                done = (Button) layout.findViewById(R.id.done);
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

    PopupWindow popWindow = null;
    boolean displayDone = false;

    public void SearchingLessons() {

        LayoutInflater lInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Searching for lessons...");

        TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
        if (AppConfiguration.makeUpFlag.contains("1")) {
            tv_description.setText(
                    "The start date for the available lesson is listed below the time.");
        } else {
            tv_description.setText(
                    "The start date for the available lesson is listed below the time.\n\nLessons will be scheduled from this date through the end date you selected.");
        }

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
                if (settingDone == false) {
                    pd = new ProgressDialog(mContext);
                    pd.setMessage("Searching for lessons...");
                    pd.setCancelable(false);
                    pd.show();
                }

                displayDone = true;
            }
        });

        CardView button_ok = (CardView) layout.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                if (popWindow.isShowing() == false) {
                    popWindow.dismiss();

                    if (settingDone == false) {
                        pd = new ProgressDialog(mContext);
                        pd.setMessage("Searching for lessons...");
                        pd.setCancelable(false);
                        pd.show();
                    }
                    displayDone = true;
                    //					Intent i = new Intent(mContext, ScheduleLessonFragement4.class);
                    //					startActivity(i);
                }
            }
        });
    }

    public void selectTimeEvent1Click() {
        days_st_1.setVisibility(View.VISIBLE);
        days_st_2.setVisibility(View.GONE);
        days_st_3.setVisibility(View.GONE);
        days_st_4.setVisibility(View.GONE);
        days_st_5.setVisibility(View.GONE);
        Log.e("selectTimeEvent1Click--", "" + "selectTimeEvent1Click");
//                select_1.setVisibility(View.VISIBLE);
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
        animation_slide(temp1, temp3, view_1, view_2);
        last_clicked_position = 1;

    }

    public void selectTimeEvent2Click() {
        days_st_1.setVisibility(View.GONE);
        days_st_2.setVisibility(View.VISIBLE);
        days_st_3.setVisibility(View.GONE);
        days_st_4.setVisibility(View.GONE);
        days_st_5.setVisibility(View.GONE);
        Log.e("selectTimeEvent2Click--", "" + "selectTimeEvent2Click");
        select_1.setVisibility(View.INVISIBLE);
//                select_2.setVisibility(View.VISIBLE);
        select_3.setVisibility(View.INVISIBLE);
        select_4.setVisibility(View.INVISIBLE);
        select_5.setVisibility(View.INVISIBLE);


        name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_2.setTextColor(getResources().getColor(R.color.orange));
        name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

        decide_layout(last_view(), select_2);
        animation_slide(temp1, temp3, view_1, view_2);
        last_clicked_position = 2;
    }

    public void selectTimeEvent3Click() {
        days_st_1.setVisibility(View.GONE);
        days_st_2.setVisibility(View.GONE);
        days_st_3.setVisibility(View.VISIBLE);
        days_st_4.setVisibility(View.GONE);
        days_st_5.setVisibility(View.GONE);

        select_1.setVisibility(View.INVISIBLE);
        select_2.setVisibility(View.INVISIBLE);
        select_3.setVisibility(View.VISIBLE);
        select_4.setVisibility(View.INVISIBLE);
        select_5.setVisibility(View.INVISIBLE);

        name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_3.setTextColor(getResources().getColor(R.color.orange));
        name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_5.setTextColor(getResources().getColor(R.color.design_change_d2));

        decide_layout(last_view(), select_3);
        animation_slide(temp1, temp3, view_1, view_2);
        last_clicked_position = 3;
    }

    public void selectTimeEvent4Click() {
        days_st_1.setVisibility(View.GONE);
        days_st_2.setVisibility(View.GONE);
        days_st_3.setVisibility(View.GONE);
        days_st_4.setVisibility(View.VISIBLE);
        days_st_5.setVisibility(View.GONE);

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
        animation_slide(temp1, temp3, view_1, view_2);
        last_clicked_position = 4;
    }

    public void selectTimeEvent5Click() {
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
        animation_slide(temp1, temp3, view_1, view_2);
        last_clicked_position = 5;
    }
}
