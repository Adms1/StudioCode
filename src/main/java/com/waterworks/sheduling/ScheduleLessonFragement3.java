/**
 *
 */
package com.waterworks.sheduling;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Harsh Adms
 */
public class ScheduleLessonFragement3 extends Activity {

    View rootView;
    String token, familyID;

    //Student tab Layout
    LinearLayout st_1, st_2, st_3, st_4, st_5, main_lay, days_st_1, days_st_2, days_st_3, days_st_4, days_st_5;
    TextView name_1, name_2, name_3, name_4, name_5;
    View select_1, select_2, select_3, select_4, select_5;

    //Next Button
//    Button btn_next;
    CardView btn_next_card;

    String selectedInstructor = "", selectedInstructor1 = "", selectedInstructor2 = "", selectedInstructor3 = "", selectedInstructor4 = "";
    String fromWhere;

    Context mContext = this;

    View selected_1, selected_2, selected_3;
    LinearLayout scdl_lsn, scdl_mkp, waitlist;
    TextView txt_1, txt_2, txt_3, noti_count;

    int last_clicked_position = 1;
    View view_1, view_2;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_schedule_lesson3);
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
        fromWhere = getIntent().getStringExtra("fromWhere");
        init();
        setSelectedInstructor();
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
    }


    public static ArrayList<HashMap<String, String>> newinstructorList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> newinstructorList1 = new ArrayList<HashMap<String, String>>();


    public void setSelectedInstructor() {


        selectedInstructor = AppConfiguration.Array2String(AppConfiguration.builder);
        selectedInstructor1 = AppConfiguration.Array2String(AppConfiguration.builder1);
        selectedInstructor2 = AppConfiguration.Array2String(AppConfiguration.builder2);
        selectedInstructor3 = AppConfiguration.Array2String(AppConfiguration.builder3);
        selectedInstructor4 = AppConfiguration.Array2String(AppConfiguration.builder4);

        AppConfiguration.instructorListBuilder = new StringBuilder();
        AppConfiguration.instructorListBuilderForInstr = new StringBuilder();

        AppConfiguration.instructorListBuilder1 = new StringBuilder();
        AppConfiguration.instructorListBuilder2 = new StringBuilder();
        AppConfiguration.instructorListBuilder3 = new StringBuilder();
        AppConfiguration.instructorListBuilder4 = new StringBuilder();
        AppConfiguration.instructorListBuilderForInstr1 = new StringBuilder();
        AppConfiguration.instructorListBuilderForInstr2 = new StringBuilder();
        AppConfiguration.instructorListBuilderForInstr3 = new StringBuilder();
        AppConfiguration.instructorListBuilderForInstr4 = new StringBuilder();

        for (int i = 0; i < AppConfiguration.instructorList.size(); i++) {
            if (selectedInstructor.contains(AppConfiguration.instructorList.get(i).get("UserID"))) {
                //TestComment
                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("Username", AppConfiguration.instructorList.get(i).get("Username"));
                hashmap.put("UserID", AppConfiguration.instructorList.get(i).get("UserID"));
                hashmap.put("gender", AppConfiguration.instructorList.get(i).get("gender"));
                hashmap.put("nextDate", AppConfiguration.instructorList.get(i).get("nextDate"));


                if (!newinstructorList.contains(hashmap)) {
                    newinstructorList.add(hashmap);
                }

                //making instructor list for step 3
                String Date = AppConfiguration.instructorList.get(i).get("nextDate").replaceAll("-", "/");
                Date = Date.replaceAll("T", " ");
                String selectedInstructorInfo = AppConfiguration.instructorList.get(i).get("UserID").toString().trim()
                        + "--" + AppConfiguration.instructorList.get(i).get("gender").toString().trim()
                        + "--" + Date
                        + "--" + AppConfiguration.instructorList.get(i).get("Username").toString().trim();

                String selectedInstructorInfoForInstr = AppConfiguration.instructorList.get(i).get("UserID").toString().trim() + ":" + AppConfiguration.instructorList.get(i).get("Username").toString().trim();
                AppConfiguration.instructorListBuilder.append(selectedInstructorInfo + ",");
                AppConfiguration.instructorListBuilderForInstr.append(selectedInstructorInfoForInstr + ",");
                AppConfiguration.ForSchedule3instructorList.add(AppConfiguration.instructorList.get(i).get("UserID").toString().trim() + "*" + AppConfiguration.instructorList.get(i).get("Username").toString().trim());

                Log.d("instrlist", AppConfiguration.instructorListBuilder.toString());
                Log.d("instrlist", AppConfiguration.instructorListBuilderForInstr.toString());
            }

        }

        for (int i = 0; i < AppConfiguration.instructorList_2.size(); i++) {
            if (selectedInstructor1.contains(AppConfiguration.instructorList_2.get(i).get("UserID"))) {
                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("Username", AppConfiguration.instructorList_2.get(i).get("Username"));
                hashmap.put("UserID", AppConfiguration.instructorList_2.get(i).get("UserID"));
                hashmap.put("gender", AppConfiguration.instructorList_2.get(i).get("gender"));
                hashmap.put("nextDate", AppConfiguration.instructorList_2.get(i).get("nextDate"));

                //				if(CheckIt(newinstructorList, hashmap.get("Username"))){
                //					newinstructorList.add(hashmap);
                //				}

                if (!newinstructorList.contains(hashmap)) {
                    newinstructorList.add(hashmap);
                }
                String Date = AppConfiguration.instructorList_2.get(i).get("nextDate").replaceAll("-", "/");
                Date = Date.replaceAll("T", " ");
                String selectedInstructorInfo = AppConfiguration.instructorList_2.get(i).get("UserID").toString().trim()
                        + "--" + AppConfiguration.instructorList_2.get(i).get("gender").toString().trim()
                        + "--" + Date
                        + "--" + AppConfiguration.instructorList_2.get(i).get("Username").toString().trim();

                String selectedInstructorInfoForInstr = AppConfiguration.instructorList_2.get(i).get("UserID").toString().trim() + ":" + AppConfiguration.instructorList_2.get(i).get("Username").toString().trim();
                AppConfiguration.instructorListBuilder1.append(selectedInstructorInfo + ",");
                AppConfiguration.instructorListBuilderForInstr1.append(selectedInstructorInfoForInstr + ",");
                AppConfiguration.ForSchedule3instructorList.add(AppConfiguration.instructorList_2.get(i).get("UserID").toString().trim() + "*" + AppConfiguration.instructorList_2.get(i).get("Username").toString().trim());
            }

        }

        for (int i = 0; i < AppConfiguration.instructorList_3.size(); i++) {
            if (selectedInstructor2.contains(AppConfiguration.instructorList_3.get(i).get("UserID"))) {
                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("Username", AppConfiguration.instructorList_3.get(i).get("Username"));
                hashmap.put("UserID", AppConfiguration.instructorList_3.get(i).get("UserID"));
                hashmap.put("gender", AppConfiguration.instructorList_3.get(i).get("gender"));
                hashmap.put("nextDate", AppConfiguration.instructorList_3.get(i).get("nextDate"));

                if (!newinstructorList.contains(hashmap)) {
                    newinstructorList.add(hashmap);
                }

                String Date = AppConfiguration.instructorList_3.get(i).get("nextDate").replaceAll("-", "/");
                Date = Date.replaceAll("T", " ");
                String selectedInstructorInfo = AppConfiguration.instructorList_3.get(i).get("UserID").toString().trim()
                        + "--" + AppConfiguration.instructorList_3.get(i).get("gender").toString().trim()
                        + "--" + Date
                        + "--" + AppConfiguration.instructorList_3.get(i).get("Username").toString().trim();

                String selectedInstructorInfoForInstr = AppConfiguration.instructorList_3.get(i).get("UserID").toString().trim() + ":" + AppConfiguration.instructorList_3.get(i).get("Username").toString().trim();
                AppConfiguration.instructorListBuilder2.append(selectedInstructorInfo + ",");
                AppConfiguration.instructorListBuilderForInstr2.append(selectedInstructorInfoForInstr + ",");
                AppConfiguration.ForSchedule3instructorList.add(AppConfiguration.instructorList_3.get(i).get("UserID").toString().trim() + "*" + AppConfiguration.instructorList_3.get(i).get("Username").toString().trim());
            }

        }

        for (int i = 0; i < AppConfiguration.instructorList_4.size(); i++) {
            if (selectedInstructor3.contains(AppConfiguration.instructorList_4.get(i).get("UserID"))) {
                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("Username", AppConfiguration.instructorList_4.get(i).get("Username"));
                hashmap.put("UserID", AppConfiguration.instructorList_4.get(i).get("UserID"));
                hashmap.put("gender", AppConfiguration.instructorList_4.get(i).get("gender"));
                hashmap.put("nextDate", AppConfiguration.instructorList_4.get(i).get("nextDate"));

                if (!newinstructorList.contains(hashmap)) {
                    newinstructorList.add(hashmap);
                }
                String Date = AppConfiguration.instructorList_4.get(i).get("nextDate").replaceAll("-", "/");
                Date = Date.replaceAll("T", " ");
                String selectedInstructorInfo = AppConfiguration.instructorList_4.get(i).get("UserID").toString().trim()
                        + "--" + AppConfiguration.instructorList_4.get(i).get("gender").toString().trim()
                        + "--" + Date
                        + "--" + AppConfiguration.instructorList_4.get(i).get("Username").toString().trim();

                String selectedInstructorInfoForInstr = AppConfiguration.instructorList_4.get(i).get("UserID").toString().trim() + ":" + AppConfiguration.instructorList_4.get(i).get("Username").toString().trim();
                AppConfiguration.instructorListBuilder3.append(selectedInstructorInfo + ",");
                AppConfiguration.instructorListBuilderForInstr3.append(selectedInstructorInfoForInstr + ",");
                AppConfiguration.ForSchedule3instructorList.add(AppConfiguration.instructorList_4.get(i).get("UserID").toString().trim() + "*" + AppConfiguration.instructorList_4.get(i).get("Username").toString().trim());
            }

        }

        for (int i = 0; i < AppConfiguration.instructorList_5.size(); i++) {
            if (selectedInstructor4.contains(AppConfiguration.instructorList_5.get(i).get("UserID"))) {
                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("Username", AppConfiguration.instructorList_5.get(i).get("Username"));
                hashmap.put("UserID", AppConfiguration.instructorList_5.get(i).get("UserID"));
                hashmap.put("gender", AppConfiguration.instructorList_5.get(i).get("gender"));
                hashmap.put("nextDate", AppConfiguration.instructorList_5.get(i).get("nextDate"));

                if (!newinstructorList.contains(hashmap)) {
                    newinstructorList.add(hashmap);
                }

                String Date = AppConfiguration.instructorList_5.get(i).get("nextDate").replaceAll("-", "/");
                Date = Date.replaceAll("T", " ");
                String selectedInstructorInfo = AppConfiguration.instructorList_5.get(i).get("UserID").toString().trim()
                        + "--" + AppConfiguration.instructorList_5.get(i).get("gender").toString().trim()
                        + "--" + Date
                        + "--" + AppConfiguration.instructorList_5.get(i).get("Username").toString().trim();

                String selectedInstructorInfoForInstr = AppConfiguration.instructorList_5.get(i).get("UserID").toString().trim() + ":" + AppConfiguration.instructorList_5.get(i).get("Username").toString().trim();
                AppConfiguration.instructorListBuilder4.append(selectedInstructorInfo + ",");
                AppConfiguration.instructorListBuilderForInstr4.append(selectedInstructorInfoForInstr + ",");
                AppConfiguration.ForSchedule3instructorList.add(AppConfiguration.instructorList_5.get(i).get("UserID").toString().trim() + "*" + AppConfiguration.instructorList_5.get(i).get("Username").toString().trim());
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(fromWhere != null){
            Intent intent= new Intent(ScheduleLessonFragement3.this, ScheduleLessonFragement2.class);
            intent.putExtra("fromWhere", "filter");
            startActivity(intent);
            finish();
        }else {
            finish();
        }
    }

    public void init() {
//        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next_card = (CardView) findViewById(R.id.btn_next_card);
        main_lay = (LinearLayout) findViewById(R.id.main_lay);

        final View view = findViewById(R.id.schdl_top);
        Button BackButton = (Button) view.findViewById(R.id.returnStack);
        RippleView ripple = (RippleView) view.findViewById(R.id.ripple);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ScheduleLessonFragement3.this, DashBoardActivity.class);
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
        /*BackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		*/
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

        select_1.setTag(1);
        select_2.setTag(2);
        select_3.setTag(3);
        select_4.setTag(4);
        select_5.setTag(5);

        btn_next_card.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //				getFragmentManager().popBackStack();

                get_values();

                if (AppConfiguration.selectedStudentsName.size() == 1) {
                    if (!AppConfiguration.Checked_Bool.containsValue(true)) {

                        SelectInstructorDialog();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(mContext, ScheduleLessonFragement4.class);
                                startActivity(i);
                            }
                        }, 100);
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 2) {
                    if (!AppConfiguration.Checked_Bool.containsValue(true)
                            || !AppConfiguration.Checked_Bool_2.containsValue(true)) {
                        SelectInstructorDialog();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(mContext, ScheduleLessonFragement4.class);
                                startActivity(i);
                            }
                        }, 100);
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 3) {
                    if (!AppConfiguration.Checked_Bool.containsValue(true)
                            || !AppConfiguration.Checked_Bool_2.containsValue(true)
                            || !AppConfiguration.Checked_Bool_3.containsValue(true)) {
                        SelectInstructorDialog();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(mContext, ScheduleLessonFragement4.class);
                                startActivity(i);
                            }
                        }, 100);
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 4) {
                    if (!AppConfiguration.Checked_Bool.containsValue(true)
                            || !AppConfiguration.Checked_Bool_2.containsValue(true)
                            || !AppConfiguration.Checked_Bool_3.containsValue(true)
                            || !AppConfiguration.Checked_Bool_4.containsValue(true)) {
                        SelectInstructorDialog();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(mContext, ScheduleLessonFragement4.class);
                                startActivity(i);
                            }
                        }, 100);
                    }
                } else if (AppConfiguration.selectedStudentsName.size() == 5) {
                    if (!AppConfiguration.Checked_Bool.containsValue(true)
                            || !AppConfiguration.Checked_Bool_2.containsValue(true)
                            || !AppConfiguration.Checked_Bool_3.containsValue(true)
                            || !AppConfiguration.Checked_Bool_4.containsValue(true)
                            || !AppConfiguration.Checked_Bool_5.containsValue(true)) {
                        SelectInstructorDialog();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(mContext, ScheduleLessonFragement4.class);
                                startActivity(i);
                            }
                        }, 100);
                    }
                }

            }
        });

        st_1.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Log.e("Std_1", "Call");
                // TODO Auto-generated method stub

                days_st_2.setVisibility(View.GONE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);
                days_st_1.setVisibility(View.VISIBLE);
                Log.e("selectTime2Event-vi@-", "" + days_st_1.isShown());
                //select_1.setVisibility(View.VISIBLE);
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
                Log.e("Std_2", "Call");
                // TODO Auto-generated method stub
                days_st_1.setVisibility(View.GONE);
                days_st_2.setVisibility(View.VISIBLE);
                days_st_3.setVisibility(View.GONE);
                days_st_4.setVisibility(View.GONE);
                days_st_5.setVisibility(View.GONE);
                Log.e("selectTime2Event-vi@-", "" + days_st_2.isShown());
                select_1.setVisibility(View.INVISIBLE);
                //select_2.setVisibility(View.VISIBLE);
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
    }

    public void selectTime1Event() {
        days_st_1.setVisibility(View.VISIBLE);
        days_st_2.setVisibility(View.GONE);
        days_st_3.setVisibility(View.GONE);
        days_st_4.setVisibility(View.GONE);
        days_st_5.setVisibility(View.GONE);
        Log.e("selectTime1Event--", "" + "selectTime1Event");
        Log.e("selectTime2Event-vi1-", "" + days_st_1.isShown());
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

    public void selectTime2Event() {
        days_st_1.setVisibility(View.GONE);
        days_st_2.setVisibility(View.VISIBLE);
        days_st_3.setVisibility(View.GONE);
        days_st_4.setVisibility(View.GONE);
        days_st_5.setVisibility(View.GONE);
        Log.e("selectTime2Event--", "" + "selectTime2Event");
        Log.e("selectTime2Event-vi-", "" + days_st_2.isShown());
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

    public void selectTime3Event() {
        days_st_1.setVisibility(View.GONE);
        days_st_2.setVisibility(View.GONE);
        days_st_3.setVisibility(View.VISIBLE);
        days_st_4.setVisibility(View.GONE);
        days_st_5.setVisibility(View.GONE);

        select_1.setVisibility(View.INVISIBLE);
        select_2.setVisibility(View.INVISIBLE);
//        select_3.setVisibility(View.VISIBLE);
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

    public void selectTime4Event() {
        days_st_1.setVisibility(View.GONE);
        days_st_2.setVisibility(View.GONE);
        days_st_3.setVisibility(View.GONE);
        days_st_4.setVisibility(View.VISIBLE);
        days_st_5.setVisibility(View.GONE);

        select_1.setVisibility(View.INVISIBLE);
        select_2.setVisibility(View.INVISIBLE);
        select_3.setVisibility(View.INVISIBLE);
//        select_4.setVisibility(View.VISIBLE);
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

    public void selectTime5Event() {
        days_st_1.setVisibility(View.GONE);
        days_st_2.setVisibility(View.GONE);
        days_st_3.setVisibility(View.GONE);
        days_st_4.setVisibility(View.GONE);
        days_st_5.setVisibility(View.VISIBLE);

        select_1.setVisibility(View.INVISIBLE);
        select_2.setVisibility(View.INVISIBLE);
        select_3.setVisibility(View.INVISIBLE);
        select_4.setVisibility(View.INVISIBLE);
//        select_5.setVisibility(View.VISIBLE);

        name_1.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_2.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_3.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_4.setTextColor(getResources().getColor(R.color.design_change_d2));
        name_5.setTextColor(getResources().getColor(R.color.orange));

        decide_layout(last_view(), select_5);
        animation_slide(temp1, temp3, view_1, view_2);
        last_clicked_position = 5;
    }

    public View last_view() {
        Log.e("Last_View", String.valueOf(last_clicked_position));
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

            //ll1.startAnimation(animSlidInLeft);
            //ll2.startAnimation(animSlidInLeft);
//            ll3.startAnimation(animSlidInLeft);
//            ll4.startAnimation(animSlidInLeft);
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
            //days_st_1.startAnimation(animSlidInRight);
//            ll2.startAnimation(animSlidInRight);
//            ll3.startAnimation(animSlidInRight);
//            ll4.startAnimation(animSlidInRight);
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
                    name_5.setText(temp[0] + "\n" + temp[1]);
                    name_5.setText(temp[0]);
                } else {
                    name_5.setText(AppConfiguration.selectedStudentsName.get(i));
                }

            }
        }
        main_lay.setVisibility(View.VISIBLE);
        inflateDays();
    }

    public void inflateDays() {

        for (int i = 0; i < AppConfiguration.selectedStudentsName.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.d2_custom_days_selection, null);

            checkBoxes(view, i);

            if (i == 0) {
                days_st_1.addView(view);
            } else if (i == 1) {
                days_st_2.addView(view);
            } else if (i == 2) {
                days_st_3.addView(view);
            } else if (i == 3) {
                days_st_4.addView(view);
            } else if (i == 4) {
                days_st_5.addView(view);
            }
        }

    }

    public void checkBoxes(View view, int count) {
        final CheckBox chkSundayTime1, chkSundayTime2, chkSundayTime3;
        final CheckBox chkMondayTime1, chkMondayTime2, chkMondayTime3;
        final CheckBox chkTuesDayTime1, chkTuesDayTime2, chkTuesDayTime3;
        final CheckBox chkWednesdayTime1, chkWednesdayTime2, chkWednesdayTime3;
        final CheckBox chkThursdayTime1, chkThursdayTime2, chkThursdayTime3;
        final CheckBox chkFridayTime1, chkFridayTime2, chkFridayTime3;
        final CheckBox chkSaturdayTime1, chkSaturdayTime2, chkSaturdayTime3;

        final CheckBox chkSearchAllDaysAndTime;

        chkSearchAllDaysAndTime = (CheckBox) view.findViewById(R.id.chkSearchAllDaysAndTime);

        chkSundayTime1 = (CheckBox) view.findViewById(R.id.chkSundayTime1);
        chkSundayTime2 = (CheckBox) view.findViewById(R.id.chkSundayTime2);
        chkSundayTime3 = (CheckBox) view.findViewById(R.id.chkSundayTime3);

        changeCheckColor(view, chkSundayTime1, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkSundayTime2, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkSundayTime3, chkSearchAllDaysAndTime);

        chkMondayTime1 = (CheckBox) view.findViewById(R.id.chkMondayTime1);
        chkMondayTime2 = (CheckBox) view.findViewById(R.id.chkMondayTime2);
        chkMondayTime3 = (CheckBox) view.findViewById(R.id.chkMondayTime3);

        changeCheckColor(view, chkMondayTime1, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkMondayTime2, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkMondayTime3, chkSearchAllDaysAndTime);

        chkTuesDayTime1 = (CheckBox) view.findViewById(R.id.chkTuesDayTime1);
        chkTuesDayTime2 = (CheckBox) view.findViewById(R.id.chkTuesDayTime2);
        chkTuesDayTime3 = (CheckBox) view.findViewById(R.id.chkTuesDayTime3);

        changeCheckColor(view, chkTuesDayTime1, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkTuesDayTime2, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkTuesDayTime3, chkSearchAllDaysAndTime);

        chkWednesdayTime1 = (CheckBox) view.findViewById(R.id.chkWednesdayTime1);
        chkWednesdayTime2 = (CheckBox) view.findViewById(R.id.chkWednesdayTime2);
        chkWednesdayTime3 = (CheckBox) view.findViewById(R.id.chkWednesdayTime3);

        changeCheckColor(view, chkWednesdayTime1, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkWednesdayTime2, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkWednesdayTime3, chkSearchAllDaysAndTime);

        chkThursdayTime1 = (CheckBox) view.findViewById(R.id.chkThursdayTime1);
        chkThursdayTime2 = (CheckBox) view.findViewById(R.id.chkThursdayTime2);
        chkThursdayTime3 = (CheckBox) view.findViewById(R.id.chkThursdayTime3);

        changeCheckColor(view, chkThursdayTime1, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkThursdayTime2, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkThursdayTime3, chkSearchAllDaysAndTime);

        chkFridayTime1 = (CheckBox) view.findViewById(R.id.chkFridayTime1);
        chkFridayTime2 = (CheckBox) view.findViewById(R.id.chkFridayTime2);
        chkFridayTime3 = (CheckBox) view.findViewById(R.id.chkFridayTime3);

        changeCheckColor(view, chkFridayTime1, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkFridayTime2, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkFridayTime3, chkSearchAllDaysAndTime);

        chkSaturdayTime1 = (CheckBox) view.findViewById(R.id.chkSaturdayTime1);
        chkSaturdayTime2 = (CheckBox) view.findViewById(R.id.chkSaturdayTime2);
        chkSaturdayTime3 = (CheckBox) view.findViewById(R.id.chkSaturdayTime3);

        changeCheckColor(view, chkSaturdayTime1, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkSaturdayTime2, chkSearchAllDaysAndTime);
        changeCheckColor(view, chkSaturdayTime3, chkSearchAllDaysAndTime);

        if (fromWhere != null) {
            setValues(view, count);
        }

        chkSearchAllDaysAndTime.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                prgmChange = false;

                if (prgmChange == false) {
                    if (chkSearchAllDaysAndTime.isChecked()) {
                        TransitionDrawable transition1 = (TransitionDrawable) chkSearchAllDaysAndTime.getBackground();
                        transition1.startTransition(500);
                        chkSearchAllDaysAndTime.setChecked(true);
                        chkSearchAllDaysAndTime.setText("Reset");
                        chkSearchAllDaysAndTime.setTextColor(getResources().getColor(R.color.white));

                        chkSundayTime1.setChecked(true);
                        chkSundayTime2.setChecked(true);
                        chkSundayTime3.setChecked(true);

                        chkMondayTime1.setChecked(true);
                        chkMondayTime2.setChecked(true);
                        chkMondayTime3.setChecked(true);

                        chkTuesDayTime1.setChecked(true);
                        chkTuesDayTime2.setChecked(true);
                        chkTuesDayTime3.setChecked(true);

                        chkWednesdayTime1.setChecked(true);
                        chkWednesdayTime2.setChecked(true);
                        chkWednesdayTime3.setChecked(true);

                        chkThursdayTime1.setChecked(true);
                        chkThursdayTime2.setChecked(true);
                        chkThursdayTime3.setChecked(true);

                        chkFridayTime1.setChecked(true);
                        chkFridayTime2.setChecked(true);
                        chkFridayTime3.setChecked(true);

                        chkSaturdayTime1.setChecked(true);
                        chkSaturdayTime2.setChecked(true);
                        chkSaturdayTime3.setChecked(true);

                    } else {
                        TransitionDrawable transition1 = (TransitionDrawable) chkSearchAllDaysAndTime.getBackground();
                        transition1.reverseTransition(500);
                        chkSearchAllDaysAndTime.setText("Select All");
                        chkSearchAllDaysAndTime.setTextColor(getResources().getColor(R.color.gray));
                        chkSearchAllDaysAndTime.setChecked(false);

                        chkSundayTime1.setChecked(false);
                        chkSundayTime2.setChecked(false);
                        chkSundayTime3.setChecked(false);

                        chkMondayTime1.setChecked(false);
                        chkMondayTime2.setChecked(false);
                        chkMondayTime3.setChecked(false);

                        chkTuesDayTime1.setChecked(false);
                        chkTuesDayTime2.setChecked(false);
                        chkTuesDayTime3.setChecked(false);

                        chkWednesdayTime1.setChecked(false);
                        chkWednesdayTime2.setChecked(false);
                        chkWednesdayTime3.setChecked(false);

                        chkThursdayTime1.setChecked(false);
                        chkThursdayTime2.setChecked(false);
                        chkThursdayTime3.setChecked(false);

                        chkFridayTime1.setChecked(false);
                        chkFridayTime2.setChecked(false);
                        chkFridayTime3.setChecked(false);

                        chkSaturdayTime1.setChecked(false);
                        chkSaturdayTime2.setChecked(false);
                        chkSaturdayTime3.setChecked(false);
                    }
                }
            }
        });
        chkSearchAllDaysAndTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                System.out.println("Prgmchange = " + prgmChange);
            }
        });
    }

    public boolean prgmChange = false;

    public void changeCheckColor(final View view, CheckBox check, final CheckBox chkSearchAllDaysAndTime) {

        check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                CheckBox ChkSu1 = (CheckBox) view.findViewById(R.id.chkSundayTime1);
                CheckBox ChkSu2 = (CheckBox) view.findViewById(R.id.chkSundayTime2);
                CheckBox ChkSu3 = (CheckBox) view.findViewById(R.id.chkSundayTime3);

                CheckBox ChkMon1 = (CheckBox) view.findViewById(R.id.chkMondayTime1);
                CheckBox ChkMon2 = (CheckBox) view.findViewById(R.id.chkMondayTime2);
                CheckBox ChkMon3 = (CheckBox) view.findViewById(R.id.chkMondayTime3);

                CheckBox ChkTue1 = (CheckBox) view.findViewById(R.id.chkTuesDayTime1);
                CheckBox ChkTue2 = (CheckBox) view.findViewById(R.id.chkTuesDayTime2);
                CheckBox ChkTue3 = (CheckBox) view.findViewById(R.id.chkTuesDayTime3);

                CheckBox ChkWed1 = (CheckBox) view.findViewById(R.id.chkWednesdayTime1);
                CheckBox ChkWed2 = (CheckBox) view.findViewById(R.id.chkWednesdayTime2);
                CheckBox ChkWed3 = (CheckBox) view.findViewById(R.id.chkWednesdayTime3);

                CheckBox ChkThu1 = (CheckBox) view.findViewById(R.id.chkThursdayTime1);
                CheckBox ChkThu2 = (CheckBox) view.findViewById(R.id.chkThursdayTime2);
                CheckBox ChkThu3 = (CheckBox) view.findViewById(R.id.chkThursdayTime3);

                CheckBox ChkFri1 = (CheckBox) view.findViewById(R.id.chkFridayTime1);
                CheckBox ChkFri2 = (CheckBox) view.findViewById(R.id.chkFridayTime2);
                CheckBox ChkFri3 = (CheckBox) view.findViewById(R.id.chkFridayTime3);

                CheckBox ChkSat1 = (CheckBox) view.findViewById(R.id.chkSaturdayTime1);
                CheckBox ChkSat2 = (CheckBox) view.findViewById(R.id.chkSaturdayTime2);
                CheckBox ChkSat3 = (CheckBox) view.findViewById(R.id.chkSaturdayTime3);
                int i = 0;
                if (ChkMon1.isChecked() || ChkMon2.isChecked() || ChkMon3.isChecked()
                        || ChkTue1.isChecked() || ChkTue2.isChecked() || ChkTue3.isChecked()
                        || ChkWed1.isChecked() || ChkWed2.isChecked() || ChkWed3.isChecked()
                        || ChkThu1.isChecked() || ChkThu2.isChecked() || ChkThu3.isChecked()
                        || ChkFri1.isChecked() || ChkFri2.isChecked() || ChkFri3.isChecked()
                        || ChkSat1.isChecked() || ChkSat2.isChecked() || ChkSat3.isChecked()
                        || ChkSu1.isChecked() || ChkSu2.isChecked() || ChkSu3.isChecked())
                    i = 1;

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


                if (isChecked) {

                    TransitionDrawable transition = (TransitionDrawable) buttonView.getBackground();
                    transition.startTransition(500);
                    prgmChange = true;
                    if (i == 1 && !chkSearchAllDaysAndTime.getText().toString().equalsIgnoreCase("Reset")) {
                        TransitionDrawable transition1 = (TransitionDrawable) chkSearchAllDaysAndTime.getBackground();
                        transition1.startTransition(500);

                        chkSearchAllDaysAndTime.setChecked(true);
                        chkSearchAllDaysAndTime.setText("Reset");
                        chkSearchAllDaysAndTime.setTextColor(getResources().getColor(R.color.white));
                    }
                    buttonView.setTextColor(getResources().getColor(R.color.white));

                } else {
                    TransitionDrawable transition = (TransitionDrawable) buttonView.getBackground();
                    transition.reverseTransition(500);
//                    prgmChange = false;
                    if (i == 0 && !chkSearchAllDaysAndTime.getText().toString().equalsIgnoreCase("Select All")) {
                        TransitionDrawable transition1 = (TransitionDrawable) chkSearchAllDaysAndTime.getBackground();
                        transition1.reverseTransition(500);

                        chkSearchAllDaysAndTime.setChecked(false);
                        chkSearchAllDaysAndTime.setText("Select All");
                        chkSearchAllDaysAndTime.setTextColor(getResources().getColor(R.color.gray));
                    }
                    buttonView.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
    }

    //Values of Selected TimeSlots
    public static ArrayList<String> timeslot = new ArrayList<String>();

    public void get_values() {
        CheckBox chkSundayTime1, chkSundayTime2, chkSundayTime3;
        CheckBox chkMondayTime1, chkMondayTime2, chkMondayTime3;
        CheckBox chkTuesDayTime1, chkTuesDayTime2, chkTuesDayTime3;
        CheckBox chkWednesdayTime1, chkWednesdayTime2, chkWednesdayTime3;
        CheckBox chkThursdayTime1, chkThursdayTime2, chkThursdayTime3;
        CheckBox chkFridayTime1, chkFridayTime2, chkFridayTime3;
        CheckBox chkSaturdayTime1, chkSaturdayTime2, chkSaturdayTime3;

        timeslot.clear();
        AppConfiguration.Checked_Bool.clear();

        for (int j = 0; j < AppConfiguration.selectedStudentsName.size(); j++) {

            View view = null;

            if (j == 0) {
                view = days_st_1;
            } else if (j == 1) {
                view = days_st_2;
            } else if (j == 2) {
                view = days_st_3;
            } else if (j == 3) {
                view = days_st_4;
            } else if (j == 4) {
                view = days_st_5;
            }

            int i = 0;

            chkSundayTime1 = (CheckBox) view.findViewById(R.id.chkSundayTime1);
            chkSundayTime2 = (CheckBox) view.findViewById(R.id.chkSundayTime2);
            chkSundayTime3 = (CheckBox) view.findViewById(R.id.chkSundayTime3);

            chkMondayTime1 = (CheckBox) view.findViewById(R.id.chkMondayTime1);
            chkMondayTime2 = (CheckBox) view.findViewById(R.id.chkMondayTime2);
            chkMondayTime3 = (CheckBox) view.findViewById(R.id.chkMondayTime3);

            chkTuesDayTime1 = (CheckBox) view.findViewById(R.id.chkTuesDayTime1);
            chkTuesDayTime2 = (CheckBox) view.findViewById(R.id.chkTuesDayTime2);
            chkTuesDayTime3 = (CheckBox) view.findViewById(R.id.chkTuesDayTime3);

            chkWednesdayTime1 = (CheckBox) view.findViewById(R.id.chkWednesdayTime1);
            chkWednesdayTime2 = (CheckBox) view.findViewById(R.id.chkWednesdayTime2);
            chkWednesdayTime3 = (CheckBox) view.findViewById(R.id.chkWednesdayTime3);

            chkThursdayTime1 = (CheckBox) view.findViewById(R.id.chkThursdayTime1);
            chkThursdayTime2 = (CheckBox) view.findViewById(R.id.chkThursdayTime2);
            chkThursdayTime3 = (CheckBox) view.findViewById(R.id.chkThursdayTime3);

            chkFridayTime1 = (CheckBox) view.findViewById(R.id.chkFridayTime1);
            chkFridayTime2 = (CheckBox) view.findViewById(R.id.chkFridayTime2);
            chkFridayTime3 = (CheckBox) view.findViewById(R.id.chkFridayTime3);

            chkSaturdayTime1 = (CheckBox) view.findViewById(R.id.chkSaturdayTime1);
            chkSaturdayTime2 = (CheckBox) view.findViewById(R.id.chkSaturdayTime2);
            chkSaturdayTime3 = (CheckBox) view.findViewById(R.id.chkSaturdayTime3);

            //===========================SUNDAY======================
            if (chkSundayTime1.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sun_" + i, true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sun_" + i, true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sun_" + i, true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sun_" + i, true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sun_" + i, true);
                }
                AppConfiguration.sun_1 = true;
                timeslot.add("Sun 8am-12pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sun_" + i, false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sun_" + i, false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sun_" + i, false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sun_" + i, false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sun_" + i, false);
                }

                AppConfiguration.sun_1 = false;
            }

            if (chkSundayTime2.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sun_" + (i + 1), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sun_" + (i + 1), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sun_" + (i + 1), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sun_" + (i + 1), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sun_" + (i + 1), true);
                }

                AppConfiguration.sun_2 = true;
                timeslot.add("Sun 12pm-4pm");
            } else {

                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sun_" + (i + 1), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sun_" + (i + 1), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sun_" + (i + 1), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sun_" + (i + 1), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sun_" + (i + 1), false);
                }

                AppConfiguration.sun_2 = false;
            }

            if (chkSundayTime3.isChecked()) {

                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sun_" + (i + 2), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sun_" + (i + 2), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sun_" + (i + 2), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sun_" + (i + 2), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sun_" + (i + 2), true);
                }

                AppConfiguration.sun_3 = true;
                timeslot.add("Sun 4pm-8pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sun_" + (i + 2), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sun_" + (i + 2), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sun_" + (i + 2), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sun_" + (i + 2), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sun_" + (i + 2), false);
                }

                AppConfiguration.sun_3 = false;
            }

            //===========================MONDAY======================
            if (chkMondayTime1.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Mon_" + i, true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Mon_" + i, true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Mon_" + i, true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Mon_" + i, true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Mon_" + i, true);
                }

                AppConfiguration.mon_1 = true;
                timeslot.add("Mon 8am-12pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Mon_" + i, false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Mon_" + i, false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Mon_" + i, false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Mon_" + i, false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Mon_" + i, false);
                }


                AppConfiguration.mon_1 = false;
            }

            if (chkMondayTime2.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Mon_" + (i + 1), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Mon_" + (i + 1), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Mon_" + (i + 1), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Mon_" + (i + 1), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Mon_" + (i + 1), true);
                }

                AppConfiguration.mon_2 = true;
                timeslot.add("Mon 12pm-4pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Mon_" + (i + 1), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Mon_" + (i + 1), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Mon_" + (i + 1), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Mon_" + (i + 1), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Mon_" + (i + 1), false);
                }

                AppConfiguration.mon_2 = false;
            }

            if (chkMondayTime3.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Mon_" + (i + 2), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Mon_" + (i + 2), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Mon_" + (i + 2), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Mon_" + (i + 2), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Mon_" + (i + 2), true);
                }


                AppConfiguration.mon_3 = true;
                timeslot.add("Mon 4pm-8pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Mon_" + (i + 2), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Mon_" + (i + 2), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Mon_" + (i + 2), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Mon_" + (i + 2), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Mon_" + (i + 2), false);
                }

                AppConfiguration.mon_3 = false;
            }


            //===========================TUESDAY======================
            if (chkTuesDayTime1.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Tue_" + i, true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Tue_" + i, true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Tue_" + i, true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Tue_" + i, true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Tue_" + i, true);
                }

                AppConfiguration.tue_1 = true;
                timeslot.add("Tue 8am-12pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Tue_" + i, false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Tue_" + i, false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Tue_" + i, false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Tue_" + i, false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Tue_" + i, false);
                }

                AppConfiguration.tue_1 = false;
            }

            if (chkTuesDayTime2.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Tue_" + (i + 1), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Tue_" + (i + 1), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Tue_" + (i + 1), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Tue_" + (i + 1), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Tue_" + (i + 1), true);
                }

                AppConfiguration.tue_2 = true;
                timeslot.add("Tue 12pm-4pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Tue_" + (i + 1), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Tue_" + (i + 1), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Tue_" + (i + 1), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Tue_" + (i + 1), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Tue_" + (i + 1), false);
                }

                AppConfiguration.tue_2 = false;
            }

            if (chkTuesDayTime3.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Tue_" + (i + 2), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Tue_" + (i + 2), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Tue_" + (i + 2), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Tue_" + (i + 2), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Tue_" + (i + 2), true);
                }

                AppConfiguration.tue_3 = true;
                timeslot.add("Tue 4pm-8pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Tue_" + (i + 2), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Tue_" + (i + 2), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Tue_" + (i + 2), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Tue_" + (i + 2), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Tue_" + (i + 2), false);
                }

                AppConfiguration.tue_3 = false;
            }

            //===========================WENDESDAY======================
            if (chkWednesdayTime1.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Wed_" + i, true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Wed_" + i, true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Wed_" + i, true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Wed_" + i, true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Wed_" + i, true);
                }

                AppConfiguration.wed_1 = true;
                timeslot.add("Wed 8am-12pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Wed_" + i, false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Wed_" + i, false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Wed_" + i, false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Wed_" + i, false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Wed_" + i, false);
                }

                AppConfiguration.wed_1 = false;
            }

            if (chkWednesdayTime2.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Wed_" + (i + 1), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Wed_" + (i + 1), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Wed_" + (i + 1), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Wed_" + (i + 1), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Wed_" + (i + 1), true);
                }

                AppConfiguration.wed_2 = true;
                timeslot.add("Wed 12pm-4pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Wed_" + (i + 1), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Wed_" + (i + 1), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Wed_" + (i + 1), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Wed_" + (i + 1), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Wed_" + (i + 1), false);
                }

                AppConfiguration.wed_2 = false;
            }

            if (chkWednesdayTime3.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Wed_" + (i + 2), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Wed_" + (i + 2), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Wed_" + (i + 2), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Wed_" + (i + 2), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Wed_" + (i + 2), true);
                }

                AppConfiguration.wed_3 = true;
                timeslot.add("Wed 4pm-8pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Wed_" + (i + 2), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Wed_" + (i + 2), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Wed_" + (i + 2), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Wed_" + (i + 2), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Wed_" + (i + 2), false);
                }

                AppConfiguration.wed_3 = false;
            }

            //===========================THURSDAY======================
            if (chkThursdayTime1.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Thu_" + i, true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Thu_" + i, true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Thu_" + i, true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Thu_" + i, true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Thu_" + i, true);
                }

                AppConfiguration.thu_1 = true;
                timeslot.add("Thu 8am-12pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Thu_" + i, false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Thu_" + i, false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Thu_" + i, false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Thu_" + i, false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Thu_" + i, false);
                }

                AppConfiguration.thu_1 = false;
            }

            if (chkThursdayTime2.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Thu_" + (i + 1), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Thu_" + (i + 1), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Thu_" + (i + 1), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Thu_" + (i + 1), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Thu_" + (i + 1), true);
                }

                AppConfiguration.thu_2 = true;
                timeslot.add("Thu 12pm-4pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Thu_" + (i + 1), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Thu_" + (i + 1), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Thu_" + (i + 1), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Thu_" + (i + 1), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Thu_" + (i + 1), false);
                }

                AppConfiguration.thu_2 = false;
            }

            if (chkThursdayTime3.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Thu_" + (i + 2), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Thu_" + (i + 2), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Thu_" + (i + 2), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Thu_" + (i + 2), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Thu_" + (i + 2), true);
                }

                AppConfiguration.thu_3 = true;
                timeslot.add("Thu 4pm-8pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Thu_" + (i + 2), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Thu_" + (i + 2), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Thu_" + (i + 2), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Thu_" + (i + 2), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Thu_" + (i + 2), false);
                }

                AppConfiguration.thu_3 = false;
            }


            //===========================FRIDAY======================
            if (chkFridayTime1.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Fri_" + (i + 0), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Fri_" + (i + 0), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Fri_" + (i + 0), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Fri_" + (i + 0), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Fri_" + (i + 0), true);
                }

                AppConfiguration.fri_1 = true;
                timeslot.add("Fri 8am-12pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Fri_" + (i + 0), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Fri_" + (i + 0), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Fri_" + (i + 0), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Fri_" + (i + 0), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Fri_" + (i + 0), false);
                }

                AppConfiguration.fri_1 = false;
            }

            if (chkFridayTime2.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Fri_" + (i + 1), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Fri_" + (i + 1), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Fri_" + (i + 1), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Fri_" + (i + 1), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Fri_" + (i + 1), true);
                }

                AppConfiguration.fri_2 = true;
                timeslot.add("Fri 12pm-4pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Fri_" + (i + 1), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Fri_" + (i + 1), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Fri_" + (i + 1), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Fri_" + (i + 1), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Fri_" + (i + 1), false);
                }

                AppConfiguration.fri_2 = false;
            }

            if (chkFridayTime3.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Fri_" + (i + 2), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Fri_" + (i + 2), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Fri_" + (i + 2), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Fri_" + (i + 2), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Fri_" + (i + 2), true);
                }

                AppConfiguration.fri_3 = true;
                timeslot.add("Fri 4pm-8pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Fri_" + (i + 2), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Fri_" + (i + 2), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Fri_" + (i + 2), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Fri_" + (i + 2), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Fri_" + (i + 2), false);
                }

                AppConfiguration.fri_3 = false;
            }


            //===========================SATURDAY======================
            if (chkSaturdayTime1.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sat_" + (i + 0), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sat_" + (i + 0), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sat_" + (i + 0), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sat_" + (i + 0), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sat_" + (i + 0), true);
                }

                AppConfiguration.sat_1 = true;
                timeslot.add("Sat 8am-12pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sat_" + (i + 0), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sat_" + (i + 0), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sat_" + (i + 0), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sat_" + (i + 0), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sat_" + (i + 0), false);
                }

                AppConfiguration.sat_1 = false;
            }

            if (chkSaturdayTime2.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sat_" + (i + 1), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sat_" + (i + 1), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sat_" + (i + 1), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sat_" + (i + 1), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sat_" + (i + 1), true);
                }

                AppConfiguration.sat_2 = true;
                timeslot.add("Sat 12pm-4pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sat_" + (i + 1), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sat_" + (i + 1), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sat_" + (i + 1), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sat_" + (i + 1), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sat_" + (i + 1), false);
                }

                AppConfiguration.sat_2 = false;
            }

            if (chkSaturdayTime3.isChecked()) {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sat_" + (i + 2), true);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sat_" + (i + 2), true);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sat_" + (i + 2), true);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sat_" + (i + 2), true);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sat_" + (i + 2), true);
                }

                AppConfiguration.sat_3 = true;
                timeslot.add("Sat 4pm-8pm");
            } else {
                if (j == 0) {
                    AppConfiguration.Checked_Bool.put("Sat_" + (i + 2), false);
                } else if (j == 1) {
                    AppConfiguration.Checked_Bool_2.put("Sat_" + (i + 2), false);
                } else if (j == 2) {
                    AppConfiguration.Checked_Bool_3.put("Sat_" + (i + 2), false);
                } else if (j == 3) {
                    AppConfiguration.Checked_Bool_4.put("Sat_" + (i + 2), false);
                } else if (j == 4) {
                    AppConfiguration.Checked_Bool_5.put("Sat_" + (i + 2), false);
                }

                AppConfiguration.sat_3 = false;
            }
        }
        System.out.println("Hashmap 1 : " + AppConfiguration.Checked_Bool);
        System.out.println("Hashmap 2 : " + AppConfiguration.Checked_Bool_2);
        System.out.println("Hashmap 3 : " + AppConfiguration.Checked_Bool_3);
        System.out.println("Hashmap 4 : " + AppConfiguration.Checked_Bool_4);
        System.out.println("Hashmap 5 : " + AppConfiguration.Checked_Bool_5);

    }


    public static void SearchingLessons(Context mContext) {

        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Searching for lessons...");

        TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
        tv_description.setText(
                "The start date for the available lesson is listed below the time.\n\nLessons will be scheduled from this date through the end date you selected.");
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

        Button button_ok = (Button) layout.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                if (popWindow.isShowing() == false) {
                    popWindow.dismiss();
//					Intent i = new Intent(mContext, ScheduleLessonFragement4.class);
//					startActivity(i);
                }
            }
        });
    }


    public void SelectInstructorDialog() {

        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        final PopupWindow popWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Warning");

        TextView tv_description = (TextView) layout.findViewById(R.id.tv_description);
        tv_description.setText("Times have not been selected for all students. Please complete selection before continuing.");
        tv_description.setGravity(Gravity.CENTER);
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
//        Button button_ok = (Button) layout.findViewById(R.id.button_ok);
        CardView button_ok = (CardView) layout.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
                if (popWindow.isShowing() == false) {
                    if (!AppConfiguration.Checked_Bool.containsValue(true)) {
                        st_1.setBackgroundResource(R.drawable.error_border);
                    }
                    if (!AppConfiguration.Checked_Bool_2.containsValue(true)) {
                        st_2.setBackgroundResource(R.drawable.error_border);
                    }
                    if (!AppConfiguration.Checked_Bool_3.containsValue(true)) {
                        st_3.setBackgroundResource(R.drawable.error_border);
                    }
                    if (!AppConfiguration.Checked_Bool_4.containsValue(true)) {
                        st_4.setBackgroundResource(R.drawable.error_border);
                    }
                    if (!AppConfiguration.Checked_Bool_5.containsValue(true)) {
                        st_5.setBackgroundResource(R.drawable.error_border);
                    }


                    if (!AppConfiguration.Checked_Bool.containsValue(true)) {
                        //st_1.setBackgroundResource(R.drawable.error_border);
//                        selectTime1Event();
                        if (AppConfiguration.Checked_Bool_2.containsValue(true) || AppConfiguration.Checked_Bool_3.containsValue(true) || AppConfiguration.Checked_Bool_4.containsValue(true) || AppConfiguration.Checked_Bool_5.containsValue(true)) {

                            try {
//                               selectTime1Event();
                                st_1.performClick();
                                Log.e("Enter--1", "Enter--1");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (!AppConfiguration.Checked_Bool_2.containsValue(true)) {
                        // st_2.setBackgroundResource(R.drawable.error_border);
//                        selectTime2Event();
                        if (AppConfiguration.Checked_Bool.containsValue(true) || AppConfiguration.Checked_Bool_3.containsValue(true) || AppConfiguration.Checked_Bool_4.containsValue(true) || AppConfiguration.Checked_Bool_5.containsValue(true)) {

                            try {
//                                selectTime2Event();
                                st_2.performClick();
                                Log.e("Enter--2", "Enter--2");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (!AppConfiguration.Checked_Bool_3.containsValue(true)) {
                        // st_3.setBackgroundResource(R.drawable.error_border);
                        if (AppConfiguration.Checked_Bool_2.containsValue(true) || AppConfiguration.Checked_Bool.containsValue(true) || AppConfiguration.Checked_Bool_4.containsValue(true) || AppConfiguration.Checked_Bool_5.containsValue(true)) {
                            try {
//                                selectTime2Event();
                                st_3.performClick();
                                Log.e("Enter--3", "Enter--3");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (!AppConfiguration.Checked_Bool_4.containsValue(true)) {
                        // st_4.setBackgroundResource(R.drawable.error_border);
                        if (AppConfiguration.Checked_Bool_2.containsValue(true) || AppConfiguration.Checked_Bool_3.containsValue(true) || AppConfiguration.Checked_Bool.containsValue(true) || AppConfiguration.Checked_Bool_5.containsValue(true)) {
                            try {
//                                selectTime2Event();
                                st_4.performClick();
                                Log.e("Enter--4", "Enter--4");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (!AppConfiguration.Checked_Bool_5.containsValue(true)) {
                        // st_5.setBackgroundResource(R.drawable.error_border);
                        if (AppConfiguration.Checked_Bool_2.containsValue(true) || AppConfiguration.Checked_Bool_3.containsValue(true) || AppConfiguration.Checked_Bool_4.containsValue(true) || AppConfiguration.Checked_Bool.containsValue(true)) {
                            try {
//                                selectTime2Event();
                                st_5.performClick();
                                Log.e("Enter--5", "Enter--5");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setValues(View view, int count) {
        timeslot.clear();

//        for (int j = 0; j < AppConfiguration.selectedStudentsName.size(); j++) {

            /*View view = null;

            if (j == 0) {
                view = days_st_1;
            } else if (j == 1) {
                view = days_st_2;
            } else if (j == 2) {
                view = days_st_3;
            } else if (j == 3) {
                view = days_st_4;
            } else if (j == 4) {
                view = days_st_5;
            }*/

        String chk = "chk";// SundayTime1"TextView" + position;

        if (count == 0) {
            for (int a = 0; a <= 2; a++) {
                if (AppConfiguration.Checked_Bool.get("Sun_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SundayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool.get("Mon_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "MondayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool.get("Tue_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "TuesDayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool.get("Wed_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "WednesdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool.get("Thu_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "ThursdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool.get("Fri_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "FridayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool.get("Sat_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SaturdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
            }
        } else if (count == 1) {
            for (int a = 0; a <= 2; a++) {
                if (AppConfiguration.Checked_Bool_2.get("Sun_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SundayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_2.get("Mon_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "MondayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_2.get("Tue_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "TuesDayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_2.get("Wed_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "WednesdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_2.get("Thu_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "ThursdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_2.get("Fri_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "FridayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_2.get("Sat_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SaturdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
            }
        } else if (count == 2) {
            for (int a = 0; a <= 2; a++) {
                if (AppConfiguration.Checked_Bool_3.get("Sun_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SundayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_3.get("Mon_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "MondayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_3.get("Tue_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "TuesDayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_3.get("Wed_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "WednesdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_3.get("Thu_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "ThursdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_3.get("Fri_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "FridayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_3.get("Sat_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SaturdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
            }
        } else if (count == 3) {
            for (int a = 0; a <= 2; a++) {
                if (AppConfiguration.Checked_Bool_4.get("Sun_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SundayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_4.get("Mon_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "MondayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_4.get("Tue_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "TuesDayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_4.get("Wed_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "WednesdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_4.get("Thu_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "ThursdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_4.get("Fri_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "FridayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_4.get("Sat_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SaturdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
            }
        } else if (count == 4) {
            for (int a = 0; a <= 2; a++) {
                if (AppConfiguration.Checked_Bool_5.get("Sun_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SundayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_5.get("Mon_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "MondayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_5.get("Tue_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "TuesDayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_5.get("Wed_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "WednesdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_5.get("Thu_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "ThursdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_5.get("Fri_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "FridayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
                if (AppConfiguration.Checked_Bool_5.get("Sat_" + a).equals(true)) {
                    int resourceID = getResources().getIdentifier(chk + "SaturdayTime" + (a + 1), "id", this.getPackageName());
                    CheckBox checkBox = (CheckBox) view.findViewById(resourceID);
                    checkBox.setChecked(true);
                    setCheckedHere(checkBox, view);
                }
            }
        }
    }

    public void setCheckedHere(CheckBox checkBox, View view) {/*
        CheckBox chkSearchAllDaysAndTime = (CheckBox) view.findViewById(R.id.chkSearchAllDaysAndTime);
        if (checkBox.isChecked()) {

            TransitionDrawable transition = (TransitionDrawable) checkBox.getBackground();
            transition.startTransition(500);
            prgmChange = true;

            TransitionDrawable transition1 = (TransitionDrawable) chkSearchAllDaysAndTime.getBackground();
            transition1.startTransition(500);

            chkSearchAllDaysAndTime.setChecked(true);
            chkSearchAllDaysAndTime.setText("Reset");
            chkSearchAllDaysAndTime.setTextColor(getResources().getColor(R.color.white));
            checkBox.setTextColor(getResources().getColor(R.color.white));
        } else {
            TransitionDrawable transition = (TransitionDrawable) checkBox.getBackground();
            transition.reverseTransition(500);
//                    prgmChange = false;
            TransitionDrawable transition1 = (TransitionDrawable) chkSearchAllDaysAndTime.getBackground();
            transition1.reverseTransition(500);

            chkSearchAllDaysAndTime.setChecked(false);
            chkSearchAllDaysAndTime.setText("Select All");
            chkSearchAllDaysAndTime.setTextColor(getResources().getColor(R.color.gray));
            checkBox.setTextColor(getResources().getColor(R.color.gray));
        }
    */
    }
}
