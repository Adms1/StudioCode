/**
 *
 */
package com.waterworks.sheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

/**
 * @author Harsh Adms
 */
public class ScheduleLessonFragement5 extends Activity implements AnimationListener {

    View rootView;
    String token, familyID;

    //Student tab Layout
    LinearLayout st_1, st_2, st_3, st_4, st_5, main_lay, days_st_1, days_st_2, days_st_3, days_st_4, days_st_5;
    TextView name_1, name_2, name_3, name_4, name_5;
    public static ArrayList<HashMap<String, String>> Calendar_Event = new ArrayList<HashMap<String, String>>();
    View select_1, select_2, select_3, select_4, select_5;

    //Next Button
//	Button btn_next,begin_new;
    CardView begin_new_card, btn_next_card;
    /* (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

    String Cls_schedule1 = "", Cls_schedule2 = "", Cls_schedule3 = "",
            Cls_schedule4 = "", Cls_schedule5 = "", SelectStudListsch5 = "";
    LinearLayout selected_classes, selected_classes_1, selected_classes_2, selected_classes_3, selected_classes_4;
    Boolean isInternetPresent = false;

    Context mContext = this;

    View selected_1, selected_2, selected_3;
    LinearLayout scdl_lsn, scdl_mkp, waitlist;
    TextView txt_1, txt_2, txt_3, noti_count;
    ImageLoader imageLoader;
    View viewLine;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 120 * 1000;
    private final long interval = 1 * 1000;
    TextView tv_timeleft;
    ScaleAnimation animSlide;
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
//28-04-2017 megha
    String listconfirm1 = "", listconfirm2 = "", listconfirm3 = "", listconfirm4 = "", listconfirm5 = "",
            SelectStudList = "", getdataList = "false";

    ListView lv_schedule_confrimation;
    String ErrorMsgConfirmschedule;
    public static int last_clicked_position = 1;
    View view_1, view_2;

//    ==============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_schedule_lesson5);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));

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

        init();
        typeFace();
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

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
//        btn_next.setTypeface(regular);
//        begin_new.setTypeface(regular);
    }


    @SuppressWarnings("deprecation")
    public void init() {

//		btn_next = (Button)findViewById(R.id.btn_next);
//		begin_new = (Button)findViewById(R.id.begin_new);
        viewLine = (View) findViewById(R.id.viewLine);
        btn_next_card = (CardView) findViewById(R.id.btn_next_card);
        begin_new_card = (CardView) findViewById(R.id.begin_new_card);
        main_lay = (LinearLayout) findViewById(R.id.main_lay);
        Button btnHome = (Button) findViewById(R.id.btnHome);
//        28-04-2017 megha
        lv_schedule_confrimation = (ListView) findViewById(R.id.lv_schedule_confirmation);
//        ================
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCheckin = new Intent(ScheduleLessonFragement5.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        View view = findViewById(R.id.schdl_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);
        TextView page_title = (TextView) view.findViewById(R.id.page_title);
        page_title.setText("Confirm Schedule");
        RippleView ripple = (RippleView) view.findViewById(R.id.ripple);

        ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                finish();
            }
        });
//		BackButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});

        begin_new_card.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppConfiguration.custom_flow = true;
                        AppConfiguration.transform = 0;
//                        18-05-2017 megha
//
//						Intent i = new Intent(mContext, DashBoardActivity.class);
                        Intent i = new Intent(mContext, ScheduleLessonFragement.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }, 100);
//				AppConfiguration.custom_flow = true;
//				AppConfiguration.transform = 5;
//				Intent i = new Intent(mContext, DashBoardActivity.class);
//				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(i);
//				finish();

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

        btn_next_card.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBoxDtl.clear();
                CheckBoxDtl1.clear();
                CheckBoxDtl2.clear();
                CheckBoxDtl3.clear();
                CheckBoxDtl4.clear();

                for (int i = 0; i < 5; i++) {
                    if (i == 0) {
                        //						if(selected_classes.getVisibility()==View.VISIBLE){
                        getSelected(selected_classes);
                        //						}
                    } else if (i == 1) {
                        //						if(selected_classes_1.getVisibility()==View.VISIBLE){
                        getSelected(selected_classes_1);
                        //						}
                    } else if (i == 2) {
                        //						if(selected_classes_2.getVisibility()==View.VISIBLE){
                        getSelected(selected_classes_2);
                        //						}
                    } else if (i == 3) {
                        //						if(selected_classes_3.getVisibility()==View.VISIBLE){
                        getSelected(selected_classes_3);
                        //						}
                    } else if (i == 4) {
                        //						if(selected_classes_4.getVisibility()==View.VISIBLE){
                        getSelected(selected_classes_4);
                        //						}
                    }
                }

                if (CheckBoxDtl.size() > 0 ||
                        CheckBoxDtl1.size() > 0 ||
                        CheckBoxDtl2.size() > 0 ||
                        CheckBoxDtl3.size() > 0 ||
                        CheckBoxDtl4.size() > 0) {

                    AppConfiguration.list1 = CheckBoxDtl.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    AppConfiguration.list2 = CheckBoxDtl1.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    AppConfiguration.list3 = CheckBoxDtl2.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    AppConfiguration.list4 = CheckBoxDtl3.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    AppConfiguration.list5 = CheckBoxDtl4.toString().replaceAll("\\[", "").replaceAll("\\]", "");

                    System.out.println("List1 : " + AppConfiguration.list1);
                    System.out.println("List2 : " + AppConfiguration.list2);
//					Intent i = new Intent(mContext,ScheduleLessonFragement6.class);
//					startActivity(i);

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent i = new Intent(mContext, ScheduleLessonFragement6.class);
//                            startActivity(i);
//                        }
//                    }, 600);
                    listconfirm1 = AppConfiguration.list1;
                    listconfirm2 = AppConfiguration.list2;
                    listconfirm3 = AppConfiguration.list3;
                    listconfirm4 = AppConfiguration.list4;
                    listconfirm5 = AppConfiguration.list5;

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
                    new GetConfirmSchedule().execute();


                    countDownTimer.cancel();
                    timerHasStarted = false;
                    viewLine.clearAnimation();

                } else {
                    //  Rakesh  20112015
                    SelectInstructorDialog("Please select at least one schedule time.", "no");
                }
            }
        });

        select_1.setTag(1);
        select_2.setTag(2);
        select_3.setTag(3);
        select_4.setTag(4);
        select_5.setTag(5);

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

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.VISIBLE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);

                select_1.setVisibility(View.INVISIBLE);
                select_2.setVisibility(View.VISIBLE);
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

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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

        Cls_schedule1 = AppConfiguration.st_Student1;
        Cls_schedule2 = AppConfiguration.st_Student2;
        Cls_schedule3 = AppConfiguration.st_Student3;
        Cls_schedule4 = AppConfiguration.st_Student4;
        Cls_schedule5 = AppConfiguration.st_Student5;

        selected_classes = (LinearLayout) findViewById(R.id.days_st_1);
        selected_classes_1 = (LinearLayout) findViewById(R.id.days_st_2);
        selected_classes_2 = (LinearLayout) findViewById(R.id.days_st_3);
        selected_classes_3 = (LinearLayout) findViewById(R.id.days_st_4);
        selected_classes_4 = (LinearLayout) findViewById(R.id.days_st_5);

        tv_timeleft = (TextView) findViewById(R.id.tv_timeleft);
        tv_timeleft.setText("02:00");

		/*animSlide = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_in_right_line_timer);
        animSlide.setAnimationListener(this);*/
        animSlide = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animSlide.setDuration(1000);
        animSlide.setAnimationListener(this);


        String[] studentid = AppConfiguration.selectedStudentID.toString()
                .split(",");
        String[] studentname = AppConfiguration.selectedStudentsName.toString()
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .toString().split(",");

        System.out.println("Student Names");
        for (int i = 0; i < studentname.length; i++) {
            if (i == 0) {
                SelectStudListsch5 = studentid[i].toString().trim() + "|"
                        + studentname[i].toString().trim();
            } else {
                SelectStudListsch5 = SelectStudListsch5 + ","
                        + studentid[i].toString().trim() + "|"
                        + studentname[i].toString().trim();
            }
        }

        Calendar_Event.clear();
        isInternetPresent = Utility.isNetworkConnected(ScheduleLessonFragement5.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new GetConfirmClassList().execute();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
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

    public void ComboLastsch5() {

        //For combo 1
        if (AppConfiguration.comboID.size() > 0) {
            if (AppConfiguration.comboID.size() == 2) {
                Cls_schedule5 = Cls_schedule1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(1));
                System.out.println("Before : " + AppConfiguration.st_Student1);
                System.out.println("After : " + AppConfiguration.st_Student5);
            } else if (AppConfiguration.comboID.size() == 3) {
                Cls_schedule5 = Cls_schedule1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(1));
                Cls_schedule4 = Cls_schedule1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(2));
            } else if (AppConfiguration.comboID.size() == 4) {
                Cls_schedule5 = Cls_schedule1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(1));
                Cls_schedule4 = Cls_schedule1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(2));
                Cls_schedule3 = Cls_schedule1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(3));
            }
        }

        //For combo 2
        if (AppConfiguration.comboID2.size() > 0) {
            if (AppConfiguration.comboID.size() == 2) {
                if (AppConfiguration.comboID2.size() == 2) {//Combo 1 Size 2
                    Cls_schedule4 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                }
            } else if (AppConfiguration.comboID.size() == 3) {//Combo 1 Size 3
                if (AppConfiguration.comboID2.size() == 2) {
                    Cls_schedule3 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                }
            } else if (AppConfiguration.comboID.size() == 4) {//Combo 1 Size 3
                //Automatically become a combo of 4 and 1 so no need to re-assign.
            }


            if (AppConfiguration.comboID.size() == 2) {
                if (AppConfiguration.comboID2.size() == 3) {//Combo 1 Size 2
                    Cls_schedule3 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                    Cls_schedule4 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(2));
                }
            } else if (AppConfiguration.comboID.size() == 3) {//Combo 1 Size 3
                if (AppConfiguration.comboID2.size() == 3) {
                    Cls_schedule3 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                    Cls_schedule4 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(2));
                }
            } else if (AppConfiguration.comboID.size() == 4) {//Combo 1 Size 3
                if (AppConfiguration.comboID2.size() == 3) {
                    Cls_schedule3 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                    Cls_schedule4 = Cls_schedule2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(2));
                }
            }
        }
    }


    public void getSelected(LinearLayout ParentLay) {

        if (ParentLay.getChildCount() > 0) {
            for (int i = 0; i < ParentLay.getChildCount(); i++) {
                View view = ParentLay.getChildAt(i); //LinearLayout-Root
                if (view instanceof LinearLayout) {
                    LinearLayout ll1 = (LinearLayout) view;//InnerLinear - 1
                    for (int j = 0; j < ll1.getChildCount(); j++) {
                        View view1 = ll1.getChildAt(j);
                        if (view1 instanceof LinearLayout) {
                            LinearLayout ll2 = (LinearLayout) view1;//InnerLinear - 2
                            for (int k = 0; k < ll2.getChildCount(); k++) {
                                View view2 = ll2.getChildAt(k);//Checkbox - 3
                                if (view2 instanceof CheckBox) {
                                    System.out.println("Found : " + k);

                                    CheckBox check = (CheckBox) view2;
                                    String value = check.getTag().toString();
                                    if (check.isChecked()) {
                                        for (int l = 0; l < tempid.length; l++) {
                                            String id = tempid[l].trim();
                                            if (value.contains(id)) {
                                                if (l == 0) {
                                                    CheckBoxDtl.add(value);
                                                } else if (l == 1) {
                                                    CheckBoxDtl1.add(value);
                                                } else if (l == 2) {
                                                    CheckBoxDtl2.add(value);
                                                } else if (l == 3) {
                                                    CheckBoxDtl3.add(value);
                                                } else if (l == 4) {
                                                    CheckBoxDtl4.add(value);
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


    //Services

    String getdata = "false", Msg = "";
    ArrayList<String> Counter_list = new ArrayList<String>();
    ArrayList<String> InstructorName = new ArrayList<String>();
    ArrayList<String> ClassTime = new ArrayList<String>();
    ArrayList<String> Dates = new ArrayList<String>();
    ArrayList<String> Day = new ArrayList<String>();
    ArrayList<String> CheckBoxDtl = new ArrayList<String>();
    ArrayList<String> LessonName = new ArrayList<String>();
    ArrayList<String> SiteName = new ArrayList<String>();
    ArrayList<String> InstructorPhoto = new ArrayList<String>();

    ArrayList<String> InstructorName1 = new ArrayList<String>();
    ArrayList<String> ClassTime1 = new ArrayList<String>();
    ArrayList<String> Dates1 = new ArrayList<String>();
    ArrayList<String> Day1 = new ArrayList<String>();
    ArrayList<String> CheckBoxDtl1 = new ArrayList<String>();
    ArrayList<String> LessonName1 = new ArrayList<String>();
    ArrayList<String> SiteName1 = new ArrayList<String>();
    ArrayList<String> InstructorPhoto1 = new ArrayList<String>();

    ArrayList<String> InstructorName2 = new ArrayList<String>();
    ArrayList<String> ClassTime2 = new ArrayList<String>();
    ArrayList<String> Dates2 = new ArrayList<String>();
    ArrayList<String> Day2 = new ArrayList<String>();
    ArrayList<String> CheckBoxDtl2 = new ArrayList<String>();
    ArrayList<String> LessonName2 = new ArrayList<String>();
    ArrayList<String> SiteName2 = new ArrayList<String>();
    ArrayList<String> InstructorPhoto2 = new ArrayList<String>();

    ArrayList<String> InstructorName3 = new ArrayList<String>();
    ArrayList<String> ClassTime3 = new ArrayList<String>();
    ArrayList<String> Dates3 = new ArrayList<String>();
    ArrayList<String> Day3 = new ArrayList<String>();
    ArrayList<String> CheckBoxDtl3 = new ArrayList<String>();
    ArrayList<String> LessonName3 = new ArrayList<String>();
    ArrayList<String> SiteName3 = new ArrayList<String>();
    ArrayList<String> InstructorPhoto3 = new ArrayList<String>();

    ArrayList<String> InstructorName4 = new ArrayList<String>();
    ArrayList<String> ClassTime4 = new ArrayList<String>();
    ArrayList<String> Dates4 = new ArrayList<String>();
    ArrayList<String> Day4 = new ArrayList<String>();
    ArrayList<String> CheckBoxDtl4 = new ArrayList<String>();
    ArrayList<String> LessonName4 = new ArrayList<String>();
    ArrayList<String> SiteName4 = new ArrayList<String>();
    ArrayList<String> InstructorPhoto4 = new ArrayList<String>();

    SparseBooleanArray confirm_list = new SparseBooleanArray();

    String[] tempname = AppConfiguration.salStep1LessonText.toString()
            .split("\\,");
    int totalCheckboxSize = 0, GCounter = 0;
    MyArrayAdapter myArrayAdapter;

    String dtutl1 = "";

    private class GetConfirmClassList extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            String[] studentid = AppConfiguration.selectedStudentID.toString()
                    .split(",");
            String[] studentname = AppConfiguration.Array2String(AppConfiguration.selectedStudentsName)
                    .toString().split(",");
            String[] salStep1LID;

            salStep1LID = AppConfiguration.salStep1LessonID.toString().split(",");
            System.out.println("Selected Type : " + salStep1LID.toString());
            for (int i = 0; i < studentname.length; i++) {
                try {
                    if (i == 0) {
                        System.out.println("studentID : " + studentid[i].toString().trim());
                        System.out.println("studentName : " + studentname[i].toString().trim());

                        if (Cls_schedule1.trim().length() > 0) {
                            Cls_schedule1 = studentid[i].toString().trim() + "*"
                                    + studentname[i].toString().trim() + "*"
                                    + salStep1LID[i].toString().trim() + "*"
                                    + Cls_schedule1;

                            if (Cls_schedule1.length() > 0 && Cls_schedule1.endsWith(",")) {
                                Cls_schedule1 = Cls_schedule1.substring(0, Cls_schedule1.length() - 1);
                            }
                        }
                        //						if(Cls_schedule1.contains("1,")){
                        //							Cls_schedule1 = Cls_schedule1.replace("1,", "");
                        //						}

                        System.out.println("ClassList : " + Cls_schedule1);
                    } else if (i == 1) {
                        if (Cls_schedule2.trim().length() > 0) {
                            Cls_schedule2 = studentid[i].toString().trim() + "*"
                                    + studentname[i].toString().trim() + "*"
                                    + salStep1LID[i].toString().trim() + "*"
                                    + Cls_schedule2;

                            if (Cls_schedule2.length() > 0 && Cls_schedule2.endsWith(",")) {
                                Cls_schedule2 = Cls_schedule2.substring(0, Cls_schedule2.length() - 1);
                            }
                        }
                    } else if (i == 2) {
                        if (Cls_schedule3.trim().length() > 0) {
                            Cls_schedule3 = studentid[i].toString().trim() + "*"
                                    + studentname[i].toString().trim() + "*"
                                    + salStep1LID[i].toString().trim() + "*"
                                    + Cls_schedule3;

                            if (Cls_schedule3.length() > 0 && Cls_schedule3.endsWith(",")) {
                                Cls_schedule3 = Cls_schedule3.substring(0, Cls_schedule3.length() - 1);
                            }
                        }

                    } else if (i == 3) {
                        if (Cls_schedule4.trim().length() > 0) {
                            Cls_schedule4 = studentid[i].toString().trim() + "*"
                                    + studentname[i].toString().trim() + "*"
                                    + salStep1LID[i].toString().trim() + "*"
                                    + Cls_schedule4;

                            if (Cls_schedule4.length() > 0 && Cls_schedule4.endsWith(",")) {
                                Cls_schedule4 = Cls_schedule4.substring(0, Cls_schedule4.length() - 1);
                            }
                        }

                    } else if (i == 4) {
                        if (Cls_schedule5.trim().length() > 0) {
                            Cls_schedule5 = studentid[i].toString().trim() + "*"
                                    + studentname[i].toString().trim() + "*"
                                    + salStep1LID[i].toString().trim() + "*"
                                    + Cls_schedule5;

                            if (Cls_schedule5.length() > 0 && Cls_schedule5.endsWith(",")) {
                                Cls_schedule5 = Cls_schedule5.substring(0, Cls_schedule5.length() - 1);
                            }
                        }
                    } else {
                        // Cls_schedule1 =
                        // studentid[i].toString().trim()+"*"+studentname[i].toString().trim()+"*"+AppConfiguration.salStep1LessonID+"*"+Cls_schedule1;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(mContext, "Sorry! Something's went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
            ComboLastsch5();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();

            param.put("Token", token);
            param.put("schedulechoices", "7");
            param.put("scheduletype", String.valueOf(AppConfiguration.makeup_Clicked));
            param.put("SelectStudList",
                    SelectStudListsch5);
            param.put("Cls_schedule1",
                    Cls_schedule1);
            param.put("Cls_schedule2",
                    Cls_schedule2);
            param.put("Cls_schedule3",
                    Cls_schedule3);
            param.put("Cls_schedule4",
                    Cls_schedule4);
            param.put("Cls_schedule5",
                    Cls_schedule5);
            param.put("Makeupflg",
                    AppConfiguration.makeUpFlag);
            param.put("pair1_Cmbo1", AppConfiguration.pair1_comboValue1);
            param.put("pair1_Cmbo2", AppConfiguration.pair1_comboValue2);
            param.put("pair1_Cmbo3", AppConfiguration.pair1_comboValue3);
            param.put("pair1_Cmbo4", AppConfiguration.pair1_comboValue4);
            param.put("pair2_Cmbo1", AppConfiguration.pair1_comboValue1);
            param.put("pair2_Cmbo2", AppConfiguration.pair1_comboValue2);
            param.put("pair2_Cmbo3", AppConfiguration.pair1_comboValue3);
            param.put("pair2_Cmbo4", AppConfiguration.pair1_comboValue4);

            HashMap<String, String> params_sp = new HashMap<String, String>();
            params_sp = param;
            System.out.println("Step 5 : " + params_sp);
            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.Schl_Get_Confirm_classesList, params_sp);
            try {
                JSONObject reader = new JSONObject(responseString);

                getdata = reader.getString("Success");
                boolean added = false;
                HashMap<String, String> hashMap = null;
                if (getdata.toString().equals("True")) {
                    JSONArray jsonMainNode = reader
                            .optJSONArray("ConformList1");
                    Counter_list.add(String.valueOf(jsonMainNode.length()));

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        hashMap = new HashMap<>();
                        JSONObject jsonObject = jsonMainNode.getJSONObject(i);
                        InstructorName.add("(" + jsonObject.getString("InstructorName") + ")");
                        ClassTime.add(jsonObject.getString("ScheduleTime"));
                        Dates.add(jsonObject.getString("Dates"));
                        Day.add(jsonObject.getString("Day"));
                        CheckBoxDtl.add(jsonObject.getString("CheckBoxDtl"));
                        LessonName.add(jsonObject.getString("LessonType"));
                        SiteName.add(jsonObject.getString("SiteName"));
                        InstructorPhoto.add(jsonObject.getString("InstructorPhoto").replace("~", ""));

                        hashMap.put("date", jsonObject.getString("Dates"));
                        hashMap.put("time", jsonObject.getString("ScheduleTime"));
                        hashMap.put("sitename", jsonObject.getString("SiteName"));
                        hashMap.put("StudentName", jsonObject.getString("StudentName"));

                        confirm_list.put(i, true);
                        Calendar_Event.add(hashMap);
                    }

                    JSONArray jsonMainNode2 = reader
                            .optJSONArray("ConformList2");
                    Counter_list.add(String.valueOf(jsonMainNode2.length()));
                    for (int i = 0; i < jsonMainNode2.length(); i++) {
                        hashMap = new HashMap<>();
                        JSONObject jsonObject = jsonMainNode2.getJSONObject(i);
                        InstructorName1.add("(" + jsonObject.getString("InstructorName") + ")");
                        ClassTime1.add(jsonObject.getString("ScheduleTime"));
                        Dates1.add(jsonObject.getString("Dates"));
                        Day1.add(jsonObject.getString("Day"));
                        CheckBoxDtl1.add(jsonObject.getString("CheckBoxDtl"));
                        LessonName1.add(jsonObject.getString("LessonType"));
                        SiteName1.add(jsonObject.getString("SiteName"));
                        InstructorPhoto1.add(jsonObject.getString("InstructorPhoto").replace("~", ""));

                        hashMap.put("date", jsonObject.getString("Dates"));
                        hashMap.put("time", jsonObject.getString("ScheduleTime"));
                        hashMap.put("sitename", jsonObject.getString("SiteName"));
                        hashMap.put("StudentName", jsonObject.getString("StudentName"));

                        confirm_list.put(i, true);
                        Calendar_Event.add(hashMap);
                    }

                    JSONArray jsonMainNode3 = reader
                            .optJSONArray("ConformList3");
                    Counter_list.add(String.valueOf(jsonMainNode3.length()));
                    for (int i = 0; i < jsonMainNode3.length(); i++) {
                        hashMap = new HashMap<>();
                        JSONObject jsonObject = jsonMainNode3.getJSONObject(i);
                        InstructorName2.add("(" + jsonObject.getString("InstructorName") + ")");
                        ClassTime2.add(jsonObject.getString("ScheduleTime"));
                        Dates2.add(jsonObject.getString("Dates"));
                        Day2.add(jsonObject.getString("Day"));
                        CheckBoxDtl2.add(jsonObject.getString("CheckBoxDtl"));
                        LessonName2.add(jsonObject.getString("LessonType"));
                        SiteName2.add(jsonObject.getString("SiteName"));
                        InstructorPhoto2.add(jsonObject.getString("InstructorPhoto").replace("~", ""));

                        hashMap.put("date", jsonObject.getString("Dates"));
                        hashMap.put("time", jsonObject.getString("ScheduleTime"));
                        hashMap.put("sitename", jsonObject.getString("SiteName"));
                        hashMap.put("StudentName", jsonObject.getString("StudentName"));

                        confirm_list.put(i, true);
                        Calendar_Event.add(hashMap);
                    }

                    JSONArray jsonMainNode4 = reader
                            .optJSONArray("ConformList4");
                    Counter_list.add(String.valueOf(jsonMainNode4.length()));
                    for (int i = 0; i < jsonMainNode4.length(); i++) {
                        hashMap = new HashMap<>();
                        JSONObject jsonObject = jsonMainNode4.getJSONObject(i);
                        InstructorName3.add("(" + jsonObject.getString("InstructorName") + ")");
                        ClassTime3.add(jsonObject.getString("ScheduleTime"));
                        Dates3.add(jsonObject.getString("Dates"));
                        Day3.add(jsonObject.getString("Day"));
                        CheckBoxDtl3.add(jsonObject.getString("CheckBoxDtl"));
                        LessonName3.add(jsonObject.getString("LessonType"));
                        SiteName3.add(jsonObject.getString("SiteName"));
                        InstructorPhoto3.add(jsonObject.getString("InstructorPhoto").replace("~", ""));

                        hashMap.put("date", jsonObject.getString("Dates"));
                        hashMap.put("time", jsonObject.getString("ScheduleTime"));
                        hashMap.put("sitename", jsonObject.getString("SiteName"));
                        hashMap.put("StudentName", jsonObject.getString("StudentName"));

                        confirm_list.put(i, true);
                        Calendar_Event.add(hashMap);
                    }

                    JSONArray jsonMainNode5 = reader
                            .optJSONArray("ConformList5");

                    Counter_list.add(String.valueOf(jsonMainNode5.length()));
                    for (int i = 0; i < jsonMainNode5.length(); i++) {
                        hashMap = new HashMap<>();
                        JSONObject jsonObject = jsonMainNode5.getJSONObject(i);
                        InstructorName4.add("(" + jsonObject.getString("InstructorName") + ")");
                        ClassTime4.add(jsonObject.getString("ScheduleTime"));
                        Dates4.add(jsonObject.getString("Dates"));
                        Day4.add(jsonObject.getString("Day"));
                        CheckBoxDtl4.add(jsonObject.getString("CheckBoxDtl"));
                        LessonName4.add(jsonObject.getString("LessonType"));
                        SiteName4.add(jsonObject.getString("SiteName"));
                        InstructorPhoto4.add(jsonObject.getString("InstructorPhoto").replace("~", ""));

                        hashMap.put("date", jsonObject.getString("Dates"));
                        hashMap.put("time", jsonObject.getString("ScheduleTime"));
                        hashMap.put("sitename", jsonObject.getString("SiteName"));
                        hashMap.put("StudentName", jsonObject.getString("StudentName"));

                        confirm_list.put(i, true);
                        Calendar_Event.add(hashMap);
                    }

                    System.out.println("Sizes : " + InstructorName.size() + Dates.size() + CheckBoxDtl.size() + Day.size());
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
            if (pd != null) {
                pd.dismiss();
            }
            if (getdata.toString().equalsIgnoreCase("True")) {

                for (int i = 0; i < tempname.length; i++) {
                    if (i == 0) {
                        SetSelectedClasses(InstructorPhoto, SiteName, LessonName, InstructorName, ClassTime, Day, Dates, i, CheckBoxDtl, selected_classes);
                    } else if (i == 1) {
                        SetSelectedClasses(InstructorPhoto1, SiteName1, LessonName1, InstructorName1, ClassTime1, Day1, Dates1, i, CheckBoxDtl1, selected_classes_1);
                    } else if (i == 2) {
                        SetSelectedClasses(InstructorPhoto2, SiteName2, LessonName2, InstructorName2, ClassTime2, Day2, Dates2, i, CheckBoxDtl2, selected_classes_2);
                    } else if (i == 3) {
                        SetSelectedClasses(InstructorPhoto3, SiteName3, LessonName3, InstructorName3, ClassTime3, Day3, Dates3, i, CheckBoxDtl3, selected_classes_3);
                    } else if (i == 4) {
                        SetSelectedClasses(InstructorPhoto4, SiteName4, LessonName4, InstructorName4, ClassTime4, Day4, Dates4, i, CheckBoxDtl4, selected_classes_4);
                    }
                }

                totalCheckboxSize = InstructorName.size() + InstructorName1.size() +
                        InstructorName2.size() + InstructorName3.size() + InstructorName4.size();

                myArrayAdapter = new MyArrayAdapter(
                        mContext,
                        R.layout.d2_list_row_schedule_regi4_item,
                        android.R.id.text1,
                        InstructorName, ClassTime, Dates, Day, CheckBoxDtl
                );

                countDownTimer = new MyCountDownTimer(startTime, interval);
                if (!timerHasStarted) {
                    countDownTimer.start();
                    timerHasStarted = true;
                    viewLine.startAnimation(animSlide);
                }
            } else {
                Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    //    ================= 28-04-2017  megha ===================================


    ArrayList<String> InstructorNameList = new ArrayList<String>();
    ArrayList<String> StudentNameList = new ArrayList<String>();
    ArrayList<String> ClassTypeList = new ArrayList<String>();
    ArrayList<String> DatesList = new ArrayList<String>();
    ArrayList<String> WeekDayList = new ArrayList<String>();
    ArrayList<String> LessonTimeList = new ArrayList<String>();


    public void ComboLast() {
        if (AppConfiguration.comboID.size() > 0) {
            if (AppConfiguration.comboID.size() == 2) {
                listconfirm5 = listconfirm1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(1));
                System.out.println("Before : " + listconfirm1);
                System.out.println("After : " + listconfirm1);
            } else if (AppConfiguration.comboID.size() == 3) {
                listconfirm5 = listconfirm1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(1));
                listconfirm4 = listconfirm1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(2));
            } else if (AppConfiguration.comboID.size() == 4) {
                listconfirm5 = listconfirm1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(1));
                listconfirm4 = listconfirm1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(2));
                listconfirm3 = listconfirm1.
                        replaceAll(AppConfiguration.comboID.get(0), AppConfiguration.comboID.get(3));
            }
        }
        //For combo 2
        if (AppConfiguration.comboID2.size() > 0) {
            if (AppConfiguration.comboID.size() == 2) {
                if (AppConfiguration.comboID2.size() == 2) {//Combo 1 Size 2
                    listconfirm4 = listconfirm2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                }
            } else if (AppConfiguration.comboID.size() == 3) {//Combo 1 Size 3
                if (AppConfiguration.comboID2.size() == 2) {
                    listconfirm3 = listconfirm2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                }
            }


            if (AppConfiguration.comboID.size() == 2) {
                if (AppConfiguration.comboID2.size() == 3) {//Combo 1 Size 2
                    listconfirm3 = listconfirm2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                    listconfirm4 = listconfirm2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(2));
                }
            } else if (AppConfiguration.comboID.size() == 3) {//Combo 1 Size 3
                if (AppConfiguration.comboID2.size() == 3) {
                    listconfirm3 = listconfirm2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(1));
                    listconfirm4 = listconfirm2.
                            replaceAll(AppConfiguration.comboID2.get(0), AppConfiguration.comboID2.get(2));
                }
            }
        }
    }

    private class GetConfirmSchedule extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Scheduling...");
            pd.setCancelable(false);
            pd.show();

            String[] studentid = AppConfiguration.selectedStudentID.toString()
                    .split(",");
            String[] studentname = AppConfiguration.selectedStudentNameToSchedule
                    .toString().split(",");
            String[] templessonid = AppConfiguration.salStep1LessonID
                    .toString().split("\\,");

            for (int i = 0; i < studentname.length; i++) {
                if (i == 0) {
                    if (listconfirm1.trim().length() > 0) {
                        listconfirm1 = studentid[i].toString().trim() + "*"
                                + studentname[i].toString().trim() + "|"
                                + templessonid[i] + "_" + listconfirm1;
                    }

                } else if (i == 1) {
                    if (listconfirm2.trim().length() > 0) {
                        listconfirm2 = studentid[i].toString().trim() + "*"
                                + studentname[i].toString().trim() + "|"
                                + templessonid[i] + "_" + listconfirm2;
                    }

                } else if (i == 2) {
                    if (listconfirm3.trim().length() > 0) {
                        listconfirm3 = studentid[i].toString().trim() + "*"
                                + studentname[i].toString().trim() + "|"
                                + templessonid[i] + "_" + listconfirm3;
                    }

                } else if (i == 3) {
                    if (listconfirm4.trim().length() > 0) {
                        listconfirm4 = studentid[i].toString().trim() + "*"
                                + studentname[i].toString().trim() + "|"
                                + templessonid[i] + "_" + listconfirm4;
                    }

                } else if (i == 4) {
                    if (listconfirm5.trim().length() > 0) {
                        listconfirm5 = studentid[i].toString().trim() + "*"
                                + studentname[i].toString().trim() + "|"
                                + templessonid[i] + "_" + listconfirm5;
                    }

                }
            }
            ComboLast();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("schedulechoices", "7");
            param.put("scheduletype", String.valueOf(AppConfiguration.makeup_Clicked));
            param.put("SelectStudList",
                    SelectStudList);
            param.put("strconfirmschedule1",
                    listconfirm1);
            param.put("strconfirmschedule2",
                    listconfirm2);
            param.put("strconfirmschedule3",
                    listconfirm3);
            param.put("strconfirmschedule4",
                    listconfirm4);
            param.put("strconfirmschedule5",
                    listconfirm5);
            param.put("Makeupflg",
                    AppConfiguration.makeUpFlag);
            param.put("siteid",
                    AppConfiguration.salStep1SiteID);
            param.put("siteText",
                    AppConfiguration.salStep1SiteName);
            param.put("reserveforever",
                    AppConfiguration.reserverForever);
            param.put("calstartdate",
                    AppConfiguration.schdl_startDate);
            param.put("pair1_Cmbo1", AppConfiguration.pair1_comboValue1);
            param.put("pair1_Cmbo2", AppConfiguration.pair1_comboValue2);
            param.put("pair1_Cmbo3", AppConfiguration.pair1_comboValue3);
            param.put("pair1_Cmbo4", AppConfiguration.pair1_comboValue4);
            param.put("pair2_Cmbo1", AppConfiguration.pair1_comboValue1);
            param.put("pair2_Cmbo2", AppConfiguration.pair1_comboValue2);
            param.put("pair2_Cmbo3", AppConfiguration.pair1_comboValue3);
            param.put("pair2_Cmbo4", AppConfiguration.pair1_comboValue4);

            param.put("pair1_Cmbo1Text", AppConfiguration.pair1_Cmbo1);
            param.put("pair1_Cmbo2Text", AppConfiguration.pair2_Cmbo1);
            param.put("pair1_Cmbo3Text", AppConfiguration.pair3_Cmbo1);
            param.put("pair1_Cmbo4Text", AppConfiguration.pair4_Cmbo1);
            param.put("pair2_Cmbo1Text", AppConfiguration.pair1_Cmbo2);
            param.put("pair2_Cmbo2Text", AppConfiguration.pair2_Cmbo2);
            param.put("pair2_Cmbo3Text", AppConfiguration.pair3_Cmbo2);
            param.put("pair2_Cmbo4Text", AppConfiguration.pair4_Cmbo2);


            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.Schl_Add_Confirm_SchedulesLesson, param);
            try {
                JSONObject reader = new JSONObject(responseString);

                getdataList = reader.getString("Success");
                if (getdataList.toString().equals("True")) {
                    JSONArray jsonMainNode = reader
                            .optJSONArray("AddSchedule");

                    Boolean LaFitnessB = reader.getBoolean("LaFitness");

                    AppConfiguration.AddLaArray = LaFitnessB.toString();
                    AppConfiguration.AddScheduleArray = jsonMainNode.toString();

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonObject = jsonMainNode
                                .getJSONObject(i);
                        StudentNameList.add(jsonObject
                                .getString("StudentName"));
                        InstructorNameList.add(jsonObject
                                .getString("InstructorName"));
                        ClassTypeList
                                .add(jsonObject.getString("ClassType"));
                        WeekDayList.add(jsonObject.getString("WeekDay"));
                        LessonTimeList.add(jsonObject
                                .getString("LessonTime"));
                        DatesList.add(jsonObject.getString("Dates"));
                        ErrorMsgConfirmschedule = jsonObject.getString("Msg");
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
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (getdataList.toString().equalsIgnoreCase("True")) {
                if (!AppConfiguration.AddScheduleArray.contains("")) {
                    lv_schedule_confrimation
                            .setAdapter(new Confirm1Adapter(StudentNameList,
                                    InstructorNameList, ClassTypeList, DatesList, LessonTimeList,
                                    WeekDayList, mContext));
                } else if (AppConfiguration.AddLaArray.equalsIgnoreCase("true")) {
                    for (int i = 0; i < AppConfiguration.CountArray.size(); i++) {
                        Log.d("CountArray@@@", "" + AppConfiguration.CountArray);
                        if (AppConfiguration.CountArray.get(i).contains("Past due") || AppConfiguration.CountArray.get(i).equalsIgnoreCase("0")) {
                            Intent intentBuyMoreLessons = new Intent(mContext, BuyMoreSwimLession.class);
                            intentBuyMoreLessons.putExtra("value", "true");
                            startActivity(intentBuyMoreLessons);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            Intent ithankyou = new Intent(ScheduleLessonFragement5.this, ScheduleLessonFragement6.class);
                            startActivity(ithankyou);
                        }
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(mContext, ScheduleLessonFragement6.class);
                            startActivity(i);
                        }
                    }, 600);
                }
            } else {
                Toast.makeText(mContext, ErrorMsgConfirmschedule, Toast.LENGTH_SHORT)
                        .show();
            }
            if (pd != null) {
                pd.dismiss();
            }
        }
    }

    public class Confirm1Adapter extends BaseAdapter {
        ArrayList<String> Studentname, Instructorname, Classtype, _dates,
                Lessontime, Weekday;
        Context context;

        public Confirm1Adapter(ArrayList<String> studentname,
                               ArrayList<String> instructorname, ArrayList<String> classtype,
                               ArrayList<String> _dates, ArrayList<String> lessontime,
                               ArrayList<String> weekday, Context context) {
            super();
            Studentname = studentname;
            Instructorname = instructorname;
            Classtype = classtype;
            this._dates = _dates;
            Lessontime = lessontime;
            Weekday = weekday;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Studentname.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return Studentname.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder {
            TextView tv_schedule_instname, tv_schedule_classtype,
                    tv_schedule_studentname, tv_schedule_date,
                    tv_schedule_weekday, tv_schedule_lessontime;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();

                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_row_schedule_confirm_data,
                                    null);
                    holder.tv_schedule_instname = (TextView) convertView
                            .findViewById(R.id.tv_schedule_instructorname);
                    holder.tv_schedule_studentname = (TextView) convertView
                            .findViewById(R.id.tv_schedule_studentname);
                    holder.tv_schedule_classtype = (TextView) convertView
                            .findViewById(R.id.tv_schedule_classtype);
                    holder.tv_schedule_lessontime = (TextView) convertView
                            .findViewById(R.id.tv_schedule_lessontime);
                    holder.tv_schedule_weekday = (TextView) convertView
                            .findViewById(R.id.tv_schedule_day);
                    holder.tv_schedule_date = (TextView) convertView
                            .findViewById(R.id.tv_schedule_date);

                    holder.tv_schedule_instname.setText(Html.fromHtml("<b>Inst.Name:</b> " + Instructorname
                            .get(position)));
                    holder.tv_schedule_studentname.setText(Html.fromHtml("<b>StudentName:</b> " + Studentname
                            .get(position)));
                    holder.tv_schedule_classtype.setText(Html.fromHtml("<b>ClassType:</b> " + Classtype
                            .get(position)));
                    holder.tv_schedule_lessontime.setText(Html.fromHtml("<b>LessonTime:</b> " + Lessontime
                            .get(position)));
                    holder.tv_schedule_weekday.setText(Html.fromHtml("<b>WeekDay:</b> " + Weekday.get(position)));
                    holder.tv_schedule_date.setText(Html.fromHtml("<b>Date:</b> " + _dates.get(position)));
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                // TODO: handle exception
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

//    ======================================================

    @SuppressWarnings("unused")
    private class MyArrayAdapter extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

        public MyArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<String> instructorname, List<String> classTime, List<String> dates, List<String> day, List<String> checkBoxDtl) {
            super(context, resource, textViewResourceId, instructorname);

            for (int i = 0; i < instructorname.size(); i++) {
                myChecked.put(i, true);
            }
        }

        public void SetCheck(int position) {
            if (!myChecked.get(position)) {
                myChecked.put(position, true);
            }
            notifyDataSetChanged();
        }

        public void SetUncheck(int position) {
            myChecked.put(position, false);
            notifyDataSetChanged();
        }

        public void toggleChecked(int position) {
            if (myChecked.get(position)) {
                myChecked.put(position, false);
            } else {
                myChecked.put(position, true);
            }

            notifyDataSetChanged();
        }

        public List<Integer> getCheckedItemPositions() {
            List<Integer> checkedItemPositions = new ArrayList<Integer>();

            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    (checkedItemPositions).add(i);
                }
            }

            return checkedItemPositions;
        }

        public List<String> getCheckedItems() {
            List<String> checkedItems = new ArrayList<String>();

            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    (checkedItems).add(CheckBoxDtl.get(i));
                }
            }

            return checkedItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.d2_list_row_schedule_regi4_item, parent, false);
            }
            TextView tv_schedule_regi4_classtime = (TextView) row
                    .findViewById(R.id.tv_schedule_regi4_classtime);
            TextView tv_schedule_regi4_Site = (TextView) row
                    .findViewById(R.id.tv_schedule_regi4_Site);
            TextView tv_schedule_regi4_lessontypeinstname = (TextView) row
                    .findViewById(R.id.tv_schedule_regi4_lessontypeinstname);
            CircleImageView inst_DP = (CircleImageView) convertView.findViewById(R.id.inst_DP);

            CheckBox checkedTextView = (CheckBox) row.findViewById(R.id.chb_schedule_regi4_select);
            checkedTextView.setText("");
            Boolean checked = myChecked.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            if (InstructorName.get(position).contains("*")) {
            } else {
                tv_schedule_regi4_classtime.setText(ClassTime
                        .get(position));
                tv_schedule_regi4_lessontypeinstname.setText(LessonName.get(position) + "  " + InstructorName.get(position));
                tv_schedule_regi4_Site.setText(SiteName.get(position));
                if (InstructorPhoto.get(position).toString().trim().length() > 0) {
                    imageLoader.displayImage(AppConfiguration.PhotoDomain + InstructorPhoto.get(position).replace(" ", "%20"), inst_DP);
                }
            }

            return row;
        }
    }


    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            SelectInstructorDialog("Too much time has elapsed. Please enter your selection again.", "yes");
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
            tv_timeleft.setText(" " + hms);

        }
    }


    String[] tempid = AppConfiguration.selectedStudentID.toString()
            .split("\\,");

    String[] tempName = AppConfiguration.salStep1LessonText.toString()
            .split("\\,");

    public void SetSelectedClasses(ArrayList<String> InstructorPhoto, ArrayList<String> SiteName, ArrayList<String> LessonName,
                                   ArrayList<String> InstructorName,
                                   ArrayList<String> ClassTime, ArrayList<String> Day,
                                   ArrayList<String> Dates, int count, ArrayList<String> chckDtl, LinearLayout ParentLay) {
        GCounter = 0;

        for (int position = 0; position < InstructorName.size(); position++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.d2_list_row_schedule_regi4_item, null);

            TextView tv_schedule_regi4_classtime = (TextView) row
                    .findViewById(R.id.tv_schedule_regi4_classtime);
            TextView tv_schedule_regi4_lessontypeinstname = (TextView) row
                    .findViewById(R.id.tv_schedule_regi4_lessontypeinstname);
            TextView tv_schedule_regi4_Site = (TextView) row
                    .findViewById(R.id.tv_schedule_regi4_Site);
            CircleImageView inst_DP = (CircleImageView) row.findViewById(R.id.inst_DP);

            CheckBox checkedTextView = (CheckBox) row.findViewById(R.id.chb_schedule_regi4_select);
            checkedTextView.setTag(chckDtl.get(position));
            checkedTextView.setText("");
            checkedTextView.setChecked(true);
            checkedTextView.setId(GCounter);

            GCounter++;
            System.out.println("Global Counter Value = " + GCounter);

            checkedTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int ind = v.getId();
                    System.out.println("Called : " + ind);

                    for (int l = 0; l < tempid.length; l++) {
                        String id = tempid[l];
                        if (v.getTag().toString().contains(id)) {
                            if (l == 0) {
                                checkBoxLogic(ind, ind, selected_classes);
                                //								CheckBoxDtl.add(value);
                            } else if (l == 1) {
                                checkBoxLogic(ind, ind, selected_classes_1);
                                //								CheckBoxDtl1.add(value);
                            } else if (l == 2) {
                                checkBoxLogic(ind, ind, selected_classes_2);
                                //								CheckBoxDtl2.add(value);
                            } else if (l == 3) {
                                checkBoxLogic(ind, ind, selected_classes_3);
                                //								CheckBoxDtl3.add(value);
                            } else if (l == 4) {
                                checkBoxLogic(ind, ind, selected_classes_4);
                                //								CheckBoxDtl4.add(value);
                            }
                        }
                    }
                }
            });

            if (position == 0) {
                tv_schedule_regi4_classtime.setText(ClassTime.get(position));
                tv_schedule_regi4_lessontypeinstname.setText(LessonName.get(position) + "  " + InstructorName.get(position));
                tv_schedule_regi4_Site.setText(SiteName.get(position));
                if (InstructorPhoto.get(position).toString().trim().length() > 0) {
                    imageLoader.displayImage(AppConfiguration.PhotoDomain + InstructorPhoto.get(position).replace(" ", "%20"), inst_DP);
                }
            } else {
                tv_schedule_regi4_classtime.setText(ClassTime.get(position));
                tv_schedule_regi4_lessontypeinstname.setText(LessonName.get(position) + "  " + InstructorName.get(position));
                tv_schedule_regi4_Site.setText(SiteName.get(position));
                if (InstructorPhoto.get(position).toString().trim().length() > 0) {
                    imageLoader.displayImage(AppConfiguration.PhotoDomain + InstructorPhoto.get(position).replace(" ", "%20"), inst_DP);
                }
            }
            ParentLay.addView(row);
        }
    }

    public void checkBoxLogic(int checkCount, int uncheckCount, LinearLayout ParentLay) {

        if (ParentLay.getChildCount() > 0) {
            for (int i = 0; i <= checkCount; i++) {
                View view = ParentLay.getChildAt(i); //LinearLayout-Root
                if (view instanceof LinearLayout) {
                    LinearLayout ll1 = (LinearLayout) view;//InnerLinear - 1
                    for (int j = 0; j < ll1.getChildCount(); j++) {
                        View view1 = ll1.getChildAt(j);
                        if (view1 instanceof LinearLayout) {
                            LinearLayout ll2 = (LinearLayout) view1;//InnerLinear - 2
                            for (int k = 0; k < ll2.getChildCount(); k++) {
                                View view2 = ll2.getChildAt(k);//Checkbox - 3
                                if (view2 instanceof CheckBox) {
                                    CheckBox check = (CheckBox) view2;
                                    if (check.isChecked()) {
                                        check.setChecked(true);
                                    } else {
                                        check.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            uncheckCount++;

            for (int Babul = uncheckCount; Babul < ParentLay.getChildCount(); Babul++) {
                View view = ParentLay.getChildAt(Babul); //LinearLayout-Root
                if (view instanceof LinearLayout) {
                    LinearLayout ll1 = (LinearLayout) view;//InnerLinear - 1
                    for (int j = 0; j < ll1.getChildCount(); j++) {
                        View view1 = ll1.getChildAt(j);
                        if (view1 instanceof LinearLayout) {
                            LinearLayout ll2 = (LinearLayout) view1;//InnerLinear - 2
                            for (int k = 0; k < ll2.getChildCount(); k++) {
                                View view2 = ll2.getChildAt(k);//Checkbox - 3
                                if (view2 instanceof CheckBox) {
                                    CheckBox check = (CheckBox) view2;
                                    if (check.isChecked()) {
                                        check.setChecked(false);
                                    } else {
                                        check.setChecked(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /* (non-Javadoc)
     * @see android.view.animation.Animation.AnimationListener#onAnimationStart(android.view.animation.Animation)
     */
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

    //  Rakesh  20112015
    public void SelectInstructorDialog(String msg, final String close) {

        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
                    ScheduleLessonFragement5.this.finish();
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
                    ScheduleLessonFragement5.this.finish();
                } else {
                    if (popWindow.isShowing() == false) {
                        if (CheckBoxDtl.size() <= 0) {
                            st_1.setBackgroundResource(R.drawable.error_border);
                        }
                        if (CheckBoxDtl1.size() <= 0) {
                            st_2.setBackgroundResource(R.drawable.error_border);
                        }
                        if (CheckBoxDtl2.size() <= 0) {
                            st_3.setBackgroundResource(R.drawable.error_border);
                        }
                        if (CheckBoxDtl3.size() <= 0) {
                            st_4.setBackgroundResource(R.drawable.error_border);
                        }
                        if (CheckBoxDtl4.size() <= 0) {
                            st_5.setBackgroundResource(R.drawable.error_border);
                        }
                    }
                }


            }
        });

    }

}
