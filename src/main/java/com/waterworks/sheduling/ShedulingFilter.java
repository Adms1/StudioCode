package com.waterworks.sheduling;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.model.SchedulingFilterInstArrayModel;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.TinyDB;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Harsh Adms on 30/12/2015.
 */
public class ShedulingFilter extends Activity {
    //    AppCompatActivity

    //for student layout
    private LinearLayout st_1, st_2, st_3, st_4, st_5, inst_inflate_1;
    private TextView name_1, name_2, name_3, name_4, name_5, NoRecord_alert, txtStartTimeMonday, txtEndTimeMonday, txtStartTimeTuesday, txtEndTimeTuesday, txtStartTimeWednesday, txtEndTimeWednesday, txtStartTimeThursday, txtEndTimeThursday,
            txtStartTimeFriday, txtEndTimeFriday, txtStartTimeSaturday, txtEndTimeSaturday, txtStartTimeSunday, txtEndTimeSunday;
    private View select_1, select_2, select_3, select_4, select_5;

    private Button btnFilterDaysAndTimes, btnFilterInstructors, btnAdvancedOptions;
    private CardView btnViewFilteredResults, btnBeginNewSearch, btnApplyDaysTimeFilter, btnChangeDaysAndTime, btnApplyAdvancedFilter, btnApplyInstructorFilter, btnChangeInstructor;
    private View include_all_button_layout, include_days_and_times_filter, include_instructor_filter, include_advanced_filter;
    private String[] inst1 = {}, inst2 = {}, inst3 = {}, inst4 = {}, inst5 = {};

    private RelativeLayout tab1, tab2, tab3;
    private LinearLayout tab1_lay, tab2_lay, tab3_lay, llSaturday, llFriday, llThursday, llWednesday, llTuesday, llMonday, llSunday, llTimeLayout;
    private View select_1_tab, select_2_tab, select_3_tab;
    private ListView listInstructor, listInstructornew;
    private TextView tab1_txt, tab2_txt, tab3_txt, ad_title, txtSunMinRangeValue, txtSunMaxRangeValue, txtMonMinRangeValue, txtMonMaxRangeValue, txtTueMinRangeValue, txtTueMaxRangeValue,
            txtWedMinRangeValue, txtWedMaxRangeValue, txtThuMinRangeValue, txtThuMaxRangeValue, txtFriMinRangeValue, txtFriMaxRangeValue, txtSatMinRangeValue, txtSatMaxRangeValue;
    private CheckBox chkSunday, chkMonday, chkTuesday, chkWednesday, chkThursday, chkFriday, chkSaturday;
    private RangeSeekBar rangeSunday, rangeMonday, rangeTuesday, rangeWednesday, rangeThursday, rangeFriday, rangeSaturday;
    private RadioButton rbNoPref, rb20Mins, rb40Mins, rbNoPrefNew, rb20MinsNew, rb40MinsNew;
    private RadioGroup advance_grp;
    private Button returnStack, returnStackTimesAndDays, returnStackInstructor, returnStackAdvanced, bttnApplyFilter;
    int current_clicked_position = 1, last_clicked_position = 1;
    private ArrayList<String> tempIns1;
    private ArrayList<String> tempIns2;
    private ArrayList<String> tempIns3;
    private ArrayList<String> tempIns4;
    private ArrayList<String> tempIns5;
    public static ArrayList<String> sendingID_std1 = new ArrayList<>();
    public static ArrayList<String> sendingID_std2 = new ArrayList<>();
    public static ArrayList<String> sendingID_std3 = new ArrayList<>();
    public static ArrayList<String> sendingID_std4 = new ArrayList<>();
    public static ArrayList<String> sendingID_std5 = new ArrayList<>();
    public static ArrayList<SchedulingFilterInstArrayModel> schedulingFilterInstArrayModels1 = new ArrayList<>();
    public static ArrayList<SchedulingFilterInstArrayModel> schedulingFilterInstArrayModels2 = new ArrayList<>();
    public static ArrayList<SchedulingFilterInstArrayModel> schedulingFilterInstArrayModels3 = new ArrayList<>();
    public static ArrayList<SchedulingFilterInstArrayModel> schedulingFilterInstArrayModels4 = new ArrayList<>();
    public static ArrayList<SchedulingFilterInstArrayModel> schedulingFilterInstArrayModels5 = new ArrayList<>();
    public static ArrayList<String> sendingID_current;
    public static ArrayList<String> daysAndTimes_std1 = new ArrayList<>();
    public static ArrayList<String> daysAndTimes_std2 = new ArrayList<>();
    public static ArrayList<String> daysAndTimes_std3 = new ArrayList<>();
    public static ArrayList<String> daysAndTimes_std4 = new ArrayList<>();
    public static ArrayList<String> daysAndTimes_std5 = new ArrayList<>();
    ArrayList<String> daysAndTimes_std1Temp = new ArrayList<>();
    ArrayList<String> daysAndTimes_std2Temp = new ArrayList<>();
    ArrayList<String> daysAndTimes_std3Temp = new ArrayList<>();
    ArrayList<String> daysAndTimes_std4Temp = new ArrayList<>();
    ArrayList<String> daysAndTimes_std5Temp = new ArrayList<>();
    static ArrayList<String> daysAndTimes_std1TempOri = new ArrayList<>();
    static ArrayList<String> daysAndTimes_std2TempOri = new ArrayList<>();
    static ArrayList<String> daysAndTimes_std3TempOri = new ArrayList<>();
    static ArrayList<String> daysAndTimes_std4TempOri = new ArrayList<>();
    static ArrayList<String> daysAndTimes_std5TempOri = new ArrayList<>();
    public static ArrayList<String> advancedFilter_std = new ArrayList<>();
    public static boolean fromFilter = false;
    //    public static boolean fromAdvancedFilter = false;
    private String whichClass, token;
    private boolean filterOn = false;
    private Context mContext;
    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    ArrayList<String> selectedStudInsList = new ArrayList<>();
    private ArrayList<String> listTimesOriginal = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTemp1 = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTemp0 = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTemp2 = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTemp3 = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTemp4 = new ArrayList<ArrayList<ArrayList<String>>>();
    private ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTemp5 = new ArrayList<ArrayList<ArrayList<String>>>();
    public static ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempStud0 = new ArrayList<ArrayList<ArrayList<String>>>();
    public static ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempStud2 = new ArrayList<ArrayList<ArrayList<String>>>();
    public static ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempStud3 = new ArrayList<ArrayList<ArrayList<String>>>();
    public static ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempStud4 = new ArrayList<ArrayList<ArrayList<String>>>();
    public static ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempStud5 = new ArrayList<ArrayList<ArrayList<String>>>();
    private HashMap<String, String> instNamePic = new HashMap<String, String>();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling_filters_new);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        mContext = this;
        tinydb = new TinyDB(mContext);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.logo_change)
                .showImageOnFail(R.drawable.logo_change)
                .showImageOnLoading(R.drawable.logo_change).build();

        init();
        setListners();
        inittypeface();
    }

    public void init() {

        String[] temp = getResources().getStringArray(R.array.markArray);
        for (int i = 0; i < temp.length; i++) {
            listTimesOriginal.add(temp[i]);
        }

        //new layout
        btnFilterDaysAndTimes = (Button) findViewById(R.id.btnFilterDaysAndTimes);
        btnFilterInstructors = (Button) findViewById(R.id.btnFilterInstructors);
        btnAdvancedOptions = (Button) findViewById(R.id.btnAdvancedOptions);
        btnApplyDaysTimeFilter = (CardView) findViewById(R.id.btnApplyDaysTimeFilter);
        btnApplyInstructorFilter = (CardView) findViewById(R.id.btnApplyInstructorFilter);
        btnViewFilteredResults = (CardView) findViewById(R.id.btnViewFilteredResults);
        btnApplyAdvancedFilter = (CardView) findViewById(R.id.btnApplyAdvancedFilter);
        btnBeginNewSearch = (CardView) findViewById(R.id.btnBeginNewSearch);
        btnChangeDaysAndTime = (CardView) findViewById(R.id.btnChangeDaysAndTime);
        btnChangeInstructor = (CardView) findViewById(R.id.btnChangeInstructor);

        include_all_button_layout = (View) findViewById(R.id.include_all_button_layout);
        include_days_and_times_filter = (View) findViewById(R.id.include_days_and_times_filter);
        include_instructor_filter = (View) findViewById(R.id.include_instructor_filter);
        include_advanced_filter = (View) findViewById(R.id.include_advanced_filter);

        fromFilter = false;
        tab1 = (RelativeLayout) findViewById(R.id.tab1);
        tab2 = (RelativeLayout) findViewById(R.id.tab2);
        tab3 = (RelativeLayout) findViewById(R.id.tab3);

        tab1_lay = (LinearLayout) findViewById(R.id.tab1_lay);
        tab2_lay = (LinearLayout) findViewById(R.id.tab2_lay);
        tab3_lay = (LinearLayout) findViewById(R.id.tab3_lay);

        select_1_tab = (View) findViewById(R.id.select_1_tab);
        select_2_tab = (View) findViewById(R.id.select_2_tab);
        select_3_tab = (View) findViewById(R.id.select_3_tab);

        tab1_txt = (TextView) findViewById(R.id.tab1_txt);
        tab2_txt = (TextView) findViewById(R.id.tab2_txt);
        tab3_txt = (TextView) findViewById(R.id.tab3_txt);

        ad_title = (TextView) findViewById(R.id.ad_title);

        rbNoPref = (RadioButton) findViewById(R.id.rbNoPref);
        rb20Mins = (RadioButton) findViewById(R.id.rb20Mins);
        rb40Mins = (RadioButton) findViewById(R.id.rb40Mins);
        rbNoPrefNew = (RadioButton) findViewById(R.id.rbNoPrefNew);
        rb20MinsNew = (RadioButton) findViewById(R.id.rb20MinsNew);
        rb40MinsNew = (RadioButton) findViewById(R.id.rb40MinsNew);

        returnStack = (Button) findViewById(R.id.returnStack);
        returnStackTimesAndDays = (Button) findViewById(R.id.returnStackTimesAndDays);
        returnStackInstructor = (Button) findViewById(R.id.returnStackInstructor);
        returnStackAdvanced = (Button) findViewById(R.id.returnStackAdvanced);

        bttnApplyFilter = (Button) findViewById(R.id.bttnApplyFilter);
        listInstructor = (ListView) findViewById(R.id.listInstructor);
        listInstructornew = (ListView) findViewById(R.id.listInstructornew);

        llSaturday = (LinearLayout) findViewById(R.id.llSaturday);
        llFriday = (LinearLayout) findViewById(R.id.llFriday);
        llThursday = (LinearLayout) findViewById(R.id.llThursday);
        llWednesday = (LinearLayout) findViewById(R.id.llWednesday);
        llTuesday = (LinearLayout) findViewById(R.id.llTuesday);
        llMonday = (LinearLayout) findViewById(R.id.llMonday);
        llSunday = (LinearLayout) findViewById(R.id.llSunday);
        llTimeLayout = (LinearLayout) findViewById(R.id.llTimeLayout);

        txtStartTimeMonday = (TextView) findViewById(R.id.txtStartTimeMonday);
        txtEndTimeMonday = (TextView) findViewById(R.id.txtEndTimeMonday);
        txtStartTimeTuesday = (TextView) findViewById(R.id.txtStartTimeTuesday);
        txtEndTimeTuesday = (TextView) findViewById(R.id.txtEndTimeTuesday);
        txtStartTimeWednesday = (TextView) findViewById(R.id.txtStartTimeWednesday);
        txtEndTimeWednesday = (TextView) findViewById(R.id.txtEndTimeWednesday);
        txtStartTimeThursday = (TextView) findViewById(R.id.txtStartTimeThursday);
        txtEndTimeThursday = (TextView) findViewById(R.id.txtEndTimeThursday);
        txtStartTimeFriday = (TextView) findViewById(R.id.txtStartTimeFriday);
        txtEndTimeFriday = (TextView) findViewById(R.id.txtEndTimeFriday);
        txtStartTimeSaturday = (TextView) findViewById(R.id.txtStartTimeSaturday);
        txtEndTimeSaturday = (TextView) findViewById(R.id.txtEndTimeSaturday);
        txtStartTimeSunday = (TextView) findViewById(R.id.txtStartTimeSunday);
        txtEndTimeSunday = (TextView) findViewById(R.id.txtEndTimeSunday);

        chkSunday = (CheckBox) findViewById(R.id.chkSunday);
        chkMonday = (CheckBox) findViewById(R.id.chkMonday);
        chkTuesday = (CheckBox) findViewById(R.id.chkTuesDay);
        chkWednesday = (CheckBox) findViewById(R.id.chkWednesday);
        chkThursday = (CheckBox) findViewById(R.id.chkThursday);
        chkFriday = (CheckBox) findViewById(R.id.chkFriday);
        chkSaturday = (CheckBox) findViewById(R.id.chkSaturday);

        rangeSunday = (RangeSeekBar) findViewById(R.id.rangeSunday);
        rangeSunday.setRangeValues(0, 35);
        rangeMonday = (RangeSeekBar) findViewById(R.id.rangeMonday);
        rangeMonday.setRangeValues(0, 35);
        rangeTuesday = (RangeSeekBar) findViewById(R.id.rangeTuesday);
        rangeTuesday.setRangeValues(0, 35);
        rangeWednesday = (RangeSeekBar) findViewById(R.id.rangeWednesday);
        rangeWednesday.setRangeValues(0, 35);
        rangeThursday = (RangeSeekBar) findViewById(R.id.rangeThursday);
        rangeThursday.setRangeValues(0, 35);
        rangeFriday = (RangeSeekBar) findViewById(R.id.rangeFriday);
        rangeFriday.setRangeValues(0, 35);
        rangeSaturday = (RangeSeekBar) findViewById(R.id.rangeSaturday);
        rangeSaturday.setRangeValues(0, 35);

        txtSunMinRangeValue = (TextView) findViewById(R.id.txtSunMinRangeValue);
        txtSunMaxRangeValue = (TextView) findViewById(R.id.txtSunMaxRangeValue);
        txtMonMinRangeValue = (TextView) findViewById(R.id.txtMonMinRangeValue);
        txtMonMaxRangeValue = (TextView) findViewById(R.id.txtMonMaxRangeValue);
        txtTueMinRangeValue = (TextView) findViewById(R.id.txtTueMinRangeValue);
        txtTueMaxRangeValue = (TextView) findViewById(R.id.txtTueMaxRangeValue);
        txtWedMinRangeValue = (TextView) findViewById(R.id.txtWedMinRangeValue);
        txtWedMaxRangeValue = (TextView) findViewById(R.id.txtWedMaxRangeValue);
        txtThuMinRangeValue = (TextView) findViewById(R.id.txtThuMinRangeValue);
        txtThuMaxRangeValue = (TextView) findViewById(R.id.txtThuMaxRangeValue);
        txtFriMinRangeValue = (TextView) findViewById(R.id.txtFriMinRangeValue);
        txtFriMaxRangeValue = (TextView) findViewById(R.id.txtFriMaxRangeValue);
        txtSatMinRangeValue = (TextView) findViewById(R.id.txtSatMinRangeValue);
        txtSatMaxRangeValue = (TextView) findViewById(R.id.txtSatMaxRangeValue);

        inst_inflate_1 = (LinearLayout) findViewById(R.id.inst_inflate_1);

        advance_grp = (RadioGroup) findViewById(R.id.advance_grp);

        if (AppConfiguration.selectedStudentsName.size() == 1) {
            ClassAvailListTemp0 = tinydb.getListObject("ClassAvailList").get(0).getArrayLists();

        } else if (AppConfiguration.selectedStudentsName.size() == 2) {
            ClassAvailListTemp0 = tinydb.getListObject("ClassAvailList").get(0).getArrayLists();
            ClassAvailListTemp2 = tinydb.getListObject("ClassAvailList").get(1).getArrayLists();

        } else if (AppConfiguration.selectedStudentsName.size() == 3) {
            ClassAvailListTemp0 = tinydb.getListObject("ClassAvailList").get(0).getArrayLists();
            ClassAvailListTemp2 = tinydb.getListObject("ClassAvailList").get(1).getArrayLists();
            ClassAvailListTemp3 = tinydb.getListObject("ClassAvailList").get(2).getArrayLists();

        } else if (AppConfiguration.selectedStudentsName.size() == 4) {
            ClassAvailListTemp0 = tinydb.getListObject("ClassAvailList").get(0).getArrayLists();
            ClassAvailListTemp2 = tinydb.getListObject("ClassAvailList").get(1).getArrayLists();
            ClassAvailListTemp3 = tinydb.getListObject("ClassAvailList").get(2).getArrayLists();
            ClassAvailListTemp4 = tinydb.getListObject("ClassAvailList").get(3).getArrayLists();

        } else if (AppConfiguration.selectedStudentsName.size() == 5) {
            ClassAvailListTemp0 = tinydb.getListObject("ClassAvailList").get(0).getArrayLists();
            ClassAvailListTemp2 = tinydb.getListObject("ClassAvailList").get(1).getArrayLists();
            ClassAvailListTemp3 = tinydb.getListObject("ClassAvailList").get(2).getArrayLists();
            ClassAvailListTemp4 = tinydb.getListObject("ClassAvailList").get(3).getArrayLists();
            ClassAvailListTemp5 = tinydb.getListObject("ClassAvailList").get(4).getArrayLists();
        }

        daysAndTimes_std1Temp = daysAndTimes_std1;
        daysAndTimes_std2Temp = daysAndTimes_std2;
        daysAndTimes_std3Temp = daysAndTimes_std3;
        daysAndTimes_std4Temp = daysAndTimes_std4;
        daysAndTimes_std5Temp = daysAndTimes_std5;

        instNamePic = ScheduleLessonFragement4.instNamePicStatic;

        if (AppConfiguration.selectedStudentsName.size() > 1) {
            btnAdvancedOptions.setEnabled(true);
        }
    }

    public void setListners() {

        //new layout
        btnFilterDaysAndTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                include_all_button_layout.setVisibility(View.GONE);
                include_days_and_times_filter.setVisibility(View.VISIBLE);
                include_instructor_filter.setVisibility(View.GONE);
                include_advanced_filter.setVisibility(View.GONE);

                distributeStudentsTop(include_days_and_times_filter);

                setDaysandTimesUINew();
                filterOn = true;
            }
        });
        btnFilterInstructors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                include_all_button_layout.setVisibility(View.GONE);
                include_days_and_times_filter.setVisibility(View.GONE);
                include_instructor_filter.setVisibility(View.VISIBLE);
                include_advanced_filter.setVisibility(View.GONE);

                distributeStudentsTop(include_instructor_filter);

                if (!AppConfiguration.instructorListBuilder.toString().equalsIgnoreCase(""))
                    inst1 = AppConfiguration.instructorListBuilder.toString().substring(0, AppConfiguration.instructorListBuilder.toString().length() - 1).split("\\,");

                tempIns1 = new ArrayList<String>();
                ArrayList<String> helperArray = new ArrayList<String>();
                for (int a = 0; a < inst1.length; a++) {
                    if (ClassAvailListTemp0.get(0).get(0).contains(inst1[a].substring(inst1[a].lastIndexOf("--") + 2, inst1[a].length()))) {
                        helperArray.add(inst1[a].substring(0, inst1[a].lastIndexOf("--") - 2));
                        tempIns1.add(inst1[a].substring(inst1[a].lastIndexOf("--") + 2, inst1[a].length()));

                    }
                }

                sendingID_current = new ArrayList<String>();

                SchedulingFilterInstArrayModel tempModel;
                if (schedulingFilterInstArrayModels1.size() < 1) {
                    for (int i = 0; i < tempIns1.size(); i++) {
                        tempModel = new SchedulingFilterInstArrayModel();
                        tempModel.setInstructorID(helperArray.get(i).substring(0, 4).replace("-", ""));
                        tempModel.setInstructorName(tempIns1.get(i));
                        tempModel.setInstructorGender(helperArray.get(i).contains("F") ? "F" : "M");
                        tempModel.setInstructorPhoto(instNamePic.get(tempIns1.get(i)));
                        tempModel.setIsSelected(true);
                        schedulingFilterInstArrayModels1.add(tempModel);
                    }
                }

//                inflateInstructorLayout(tempIns1, helperArray);
                inflateInstructorLayout(schedulingFilterInstArrayModels1);
                filterOn = true;
            }
        });
        btnAdvancedOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                include_all_button_layout.setVisibility(View.GONE);
                include_days_and_times_filter.setVisibility(View.GONE);
                include_instructor_filter.setVisibility(View.GONE);
                include_advanced_filter.setVisibility(View.VISIBLE);

//                distributeStudentsTop(include_advanced_filter);

                if (advancedFilter_std.size() > 0) {
                    if (advancedFilter_std.get(0).equalsIgnoreCase("nopref")) {
                        rbNoPrefNew.setChecked(true);
                    } else if (advancedFilter_std.get(0).equalsIgnoreCase("20")) {
                        rb20MinsNew.setChecked(true);
                    } else if (advancedFilter_std.get(0).equalsIgnoreCase("40")) {
                        rb40MinsNew.setChecked(true);
                    }
                } else {
                    rbNoPrefNew.setChecked(true);
                }
                filterOn = true;
            }
        });
        btnApplyDaysTimeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                include_all_button_layout.setVisibility(View.VISIBLE);
                include_days_and_times_filter.setVisibility(View.GONE);

                daysAndTimes_std1 = daysAndTimes_std1Temp;
                daysAndTimes_std2 = daysAndTimes_std2Temp;
                daysAndTimes_std3 = daysAndTimes_std3Temp;
                daysAndTimes_std4 = daysAndTimes_std4Temp;
                daysAndTimes_std5 = daysAndTimes_std5Temp;

                filterOn = false;
            }
        });
        btnApplyInstructorFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                include_all_button_layout.setVisibility(View.VISIBLE);
                include_instructor_filter.setVisibility(View.GONE);
                filterOn = false;
            }
        });
        btnApplyAdvancedFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                include_all_button_layout.setVisibility(View.VISIBLE);
                include_advanced_filter.setVisibility(View.GONE);
                filterOn = false;
            }
        });

        btnViewFilteredResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBeginNewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfiguration.makeUpFlag.equalsIgnoreCase("1") &&
                        AppConfiguration.makeup_Clicked == 1) {
                    Intent intent = new Intent(ShedulingFilter.this, ScheduleMakeupFragment.class);
                    intent.putExtra("fromWhere", "filter");
                    startActivity(intent);
                    finish();

                } else if (AppConfiguration.makeUpFlag.equalsIgnoreCase("0") &&
                        AppConfiguration.makeup_Clicked == 0) {
                    Intent intent = new Intent(ShedulingFilter.this, ScheduleLessonFragement.class);
                    intent.putExtra("fromWhere", "filter");
                    startActivity(intent);
                    finish();

                }

            }
        });

        bttnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnChangeDaysAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShedulingFilter.this, ScheduleLessonFragement3.class);
                intent.putExtra("fromWhere", "filter");
                startActivity(intent);
                finish();
            }
        });

        btnChangeInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShedulingFilter.this, ScheduleLessonFragement2.class);
                intent.putExtra("fromWhere", "filter");
                startActivity(intent);
                finish();
            }
        });

        rangeSunday.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtSunMinRangeValue.setText(listTimesOriginal.get((int) minValue));
                txtSunMaxRangeValue.setText(listTimesOriginal.get((int) maxValue));
            }
        });

        rangeMonday.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtMonMinRangeValue.setText(listTimesOriginal.get((int) minValue));
                txtMonMaxRangeValue.setText(listTimesOriginal.get((int) maxValue));
            }
        });

        rangeTuesday.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtTueMinRangeValue.setText(listTimesOriginal.get((int) minValue));
                txtTueMaxRangeValue.setText(listTimesOriginal.get((int) maxValue));
            }
        });

        rangeWednesday.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtWedMinRangeValue.setText(listTimesOriginal.get((int) minValue));
                txtWedMaxRangeValue.setText(listTimesOriginal.get((int) maxValue));
            }
        });

        rangeThursday.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtThuMinRangeValue.setText(listTimesOriginal.get((int) minValue));
                txtThuMaxRangeValue.setText(listTimesOriginal.get((int) maxValue));
            }
        });

        rangeFriday.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtFriMinRangeValue.setText(listTimesOriginal.get((int) minValue));
                txtFriMaxRangeValue.setText(listTimesOriginal.get((int) maxValue));
            }
        });

        rangeSaturday.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtSatMinRangeValue.setText(listTimesOriginal.get((int) minValue));
                txtSatMaxRangeValue.setText(listTimesOriginal.get((int) maxValue));
            }
        });

        returnStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        returnStackTimesAndDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        returnStackInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        returnStackAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab1_lay.setVisibility(View.VISIBLE);
                tab2_lay.setVisibility(View.GONE);
                tab3_lay.setVisibility(View.GONE);

                select_1_tab.setVisibility(View.VISIBLE);
                select_2_tab.setVisibility(View.GONE);
                select_3_tab.setVisibility(View.GONE);
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab1_lay.setVisibility(View.GONE);
                tab2_lay.setVisibility(View.VISIBLE);
                tab3_lay.setVisibility(View.GONE);

                select_1_tab.setVisibility(View.GONE);
                select_2_tab.setVisibility(View.VISIBLE);
                select_3_tab.setVisibility(View.GONE);
            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab1_lay.setVisibility(View.GONE);
                tab2_lay.setVisibility(View.GONE);
                tab3_lay.setVisibility(View.VISIBLE);

                select_1_tab.setVisibility(View.GONE);
                select_2_tab.setVisibility(View.GONE);
                select_3_tab.setVisibility(View.VISIBLE);

            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ShedulingFilter.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        listInstructor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Utility.ping(ShedulingFilter.this, ScheduleLessonFragement4.selectedStudInsList.get(position).toString());
            }
        });

        rbNoPref.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAdvancedFilterChecks("nopref");
                }
            }
        });

        rb20Mins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAdvancedFilterChecks("20");
                }
            }
        });

        rb40Mins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAdvancedFilterChecks("40");
                }
            }
        });

        rbNoPrefNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAdvancedFilterChecks("nopref");
                }
            }
        });

        rb20MinsNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAdvancedFilterChecks("20");
                }
            }
        });

        rb40MinsNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAdvancedFilterChecks("40");
                }
            }
        });

        timeDateFilterListners();

    }

    public void setAdvancedFilterChecks(String filterName) {
        advancedFilter_std.clear();
        advancedFilter_std.add(filterName);
    }

    public void setDaysandTimesUINew() {
        if (current_clicked_position == 1) {
            if (daysAndTimes_std1Temp.size() > 0) {
                setStud_Values_From_Previous(daysAndTimes_std1Temp);
            } else {
                setStud_Values(ClassAvailListTemp0);
                daysAndTimes_std1Temp = setTimesAndDaysValues();
                daysAndTimes_std1TempOri = setTimesAndDaysValues();
            }
        } else if (current_clicked_position == 2) {
            if (daysAndTimes_std2Temp.size() > 0) {
                setStud_Values_From_Previous(daysAndTimes_std2Temp);
            } else {
                setStud_Values(ClassAvailListTemp2);
                daysAndTimes_std2Temp = setTimesAndDaysValues();
                daysAndTimes_std2TempOri = setTimesAndDaysValues();
            }
        } else if (current_clicked_position == 3) {
            if (daysAndTimes_std3Temp.size() > 0) {
                setStud_Values_From_Previous(daysAndTimes_std3Temp);
            } else {
                setStud_Values(ClassAvailListTemp3);
                daysAndTimes_std3Temp = setTimesAndDaysValues();
                daysAndTimes_std3TempOri = setTimesAndDaysValues();
            }
        } else if (current_clicked_position == 4) {
            if (daysAndTimes_std4Temp.size() > 0) {
                setStud_Values_From_Previous(daysAndTimes_std4Temp);
            } else {
                setStud_Values(ClassAvailListTemp4);
                daysAndTimes_std4Temp = setTimesAndDaysValues();
                daysAndTimes_std4TempOri = setTimesAndDaysValues();
            }
        } else if (current_clicked_position == 5) {
            if (daysAndTimes_std5Temp.size() > 0) {
                setStud_Values_From_Previous(daysAndTimes_std5Temp);
            } else {
                setStud_Values(ClassAvailListTemp5);
                daysAndTimes_std5Temp = setTimesAndDaysValues();
                daysAndTimes_std5TempOri = setTimesAndDaysValues();
            }
        }
    }

    public void setStud_Values_From_Previous(ArrayList<String> daystime) {

        llMonday.setVisibility(View.GONE);
        llTuesday.setVisibility(View.GONE);
        llWednesday.setVisibility(View.GONE);
        llThursday.setVisibility(View.GONE);
        llFriday.setVisibility(View.GONE);
        llSaturday.setVisibility(View.GONE);
        llSunday.setVisibility(View.GONE);


        for (int i = 0; i < daystime.size(); i++) {
            if (daystime.get(i).contains("Monday")) {
                llMonday.setVisibility(View.VISIBLE);
                String[] temp = daystime.get(i).split("\\|");
                txtStartTimeMonday.setText(formattedString(temp[1]));
                txtEndTimeMonday.setText(formattedString(temp[2]));
            }
        }
        for (int i = 0; i < daystime.size(); i++) {
            if (daystime.get(i).contains("Tuesday")) {
                llTuesday.setVisibility(View.VISIBLE);
                String[] temp = daystime.get(i).split("\\|");
                txtStartTimeTuesday.setText(formattedString(temp[1]));
                txtEndTimeTuesday.setText(formattedString(temp[2]));
            }
        }
        for (int i = 0; i < daystime.size(); i++) {
            if (daystime.get(i).contains("Wednesday")) {
                llWednesday.setVisibility(View.VISIBLE);
                String[] temp = daystime.get(i).split("\\|");
                txtStartTimeWednesday.setText(formattedString(temp[1]));
                txtEndTimeWednesday.setText(formattedString(temp[2]));
            }
        }
        for (int i = 0; i < daystime.size(); i++) {
            if (daystime.get(i).contains("Thursday")) {
                llThursday.setVisibility(View.VISIBLE);
                String[] temp = daystime.get(i).split("\\|");
                txtStartTimeThursday.setText(formattedString(temp[1]));
                txtEndTimeThursday.setText(formattedString(temp[2]));
            }
        }
        for (int i = 0; i < daystime.size(); i++) {
            if (daystime.get(i).contains("Friday")) {
                llFriday.setVisibility(View.VISIBLE);
                String[] temp = daystime.get(i).split("\\|");
                txtStartTimeFriday.setText(formattedString(temp[1]));
                txtEndTimeFriday.setText(formattedString(temp[2]));
            }
        }
        for (int i = 0; i < daystime.size(); i++) {
            if (daystime.get(i).contains("Saturday")) {
                llSaturday.setVisibility(View.VISIBLE);
                String[] temp = daystime.get(i).split("\\|");
                txtStartTimeSaturday.setText(formattedString(temp[1]));
                txtEndTimeSaturday.setText(formattedString(temp[2]));
            }
        }
        for (int i = 0; i < daystime.size(); i++) {
            if (daystime.get(i).contains("Sunday")) {
                llSunday.setVisibility(View.VISIBLE);
                String[] temp = daystime.get(i).split("\\|");
                txtStartTimeSunday.setText(formattedString(temp[1]));
                txtEndTimeSunday.setText(formattedString(temp[2]));
            }
        }
    }

    public String formattedString(String time) {
        String[] timeDay = time.split(" ");
        final SpannableStringBuilder sb = new SpannableStringBuilder(timeDay[0]);

        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(bss, 0, timeDay[0].length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return sb + " " + timeDay[1];
    }

    public int getCount(ArrayList<ArrayList<ArrayList<String>>> tempTimeDay, int row) {
        int count = 0;
        for (int i = 0; i < tempTimeDay.get(0).get(row).size(); i++) {
            if (tempTimeDay.get(0).get(row).get(i).equalsIgnoreCase("")) {
                count++;
            }
        }
        return count;
    }

    public void setStud_Values(ArrayList<ArrayList<ArrayList<String>>> tempTimeDayList) {
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        Date dateTime;

        if (getCount(tempTimeDayList, 4) < tempTimeDayList.get(0).get(4).size()) {
            llMonday.setVisibility(View.VISIBLE);

            ArrayList<Date> allDates = new ArrayList<>();
            for (int i = 0; i < tempTimeDayList.get(0).get(1).size(); i++) {
                try {
                    if (!tempTimeDayList.get(0).get(1).get(i).toString().equalsIgnoreCase("")) {
                        dateTime = sdf.parse(tempTimeDayList.get(0).get(1).get(i).toString());
                        allDates.add(dateTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(allDates);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");

            txtStartTimeMonday.setText(formattedString(displayFormat.format(allDates.get(0)).toString().toUpperCase()));
            txtEndTimeMonday.setText(formattedString(displayFormat.format(allDates.get(allDates.size() - 1)).toString().toUpperCase()));

        } else {
            llMonday.setVisibility(View.GONE);
        }

        count = 0;
        if (getCount(tempTimeDayList, 8) < tempTimeDayList.get(0).get(8).size()) {
            llTuesday.setVisibility(View.VISIBLE);

            ArrayList<Date> allDates = new ArrayList<>();
            for (int i = 0; i < tempTimeDayList.get(0).get(5).size(); i++) {
                try {
                    if (!tempTimeDayList.get(0).get(5).get(i).toString().equalsIgnoreCase("")) {
                        dateTime = sdf.parse(tempTimeDayList.get(0).get(5).get(i).toString());
                        allDates.add(dateTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(allDates);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            txtStartTimeTuesday.setText(formattedString(displayFormat.format(allDates.get(0)).toString().toUpperCase()));
            txtEndTimeTuesday.setText(formattedString(displayFormat.format(allDates.get(allDates.size() - 1)).toString().toUpperCase()));

        } else {
            llTuesday.setVisibility(View.GONE);
        }

        count = 0;
        if (getCount(tempTimeDayList, 12) < tempTimeDayList.get(0).get(12).size()) {
            llWednesday.setVisibility(View.VISIBLE);

            ArrayList<Date> allDates = new ArrayList<>();
            for (int i = 0; i < tempTimeDayList.get(0).get(9).size(); i++) {
                try {
                    if (!tempTimeDayList.get(0).get(9).get(i).toString().equalsIgnoreCase("")) {
                        dateTime = sdf.parse(tempTimeDayList.get(0).get(9).get(i).toString());
                        allDates.add(dateTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(allDates);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            txtStartTimeWednesday.setText(formattedString(displayFormat.format(allDates.get(0)).toString().toUpperCase()));
            txtEndTimeWednesday.setText(formattedString(displayFormat.format(allDates.get(allDates.size() - 1)).toString().toUpperCase()));

        } else {
            llWednesday.setVisibility(View.GONE);
        }

        count = 0;
        if (getCount(tempTimeDayList, 16) < tempTimeDayList.get(0).get(16).size()) {
            llThursday.setVisibility(View.VISIBLE);

            ArrayList<Date> allDates = new ArrayList<>();
            for (int i = 0; i < tempTimeDayList.get(0).get(13).size(); i++) {
                try {
                    if (!tempTimeDayList.get(0).get(13).get(i).toString().equalsIgnoreCase("")) {
                        dateTime = sdf.parse(tempTimeDayList.get(0).get(13).get(i).toString());
                        allDates.add(dateTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(allDates);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            txtStartTimeThursday.setText(formattedString(displayFormat.format(allDates.get(0)).toString().toUpperCase()));
            txtEndTimeThursday.setText(formattedString(displayFormat.format(allDates.get(allDates.size() - 1)).toString().toUpperCase()));

        } else {
            llThursday.setVisibility(View.GONE);
        }

        count = 0;
        if (getCount(tempTimeDayList, 20) < tempTimeDayList.get(0).get(20).size()) {
            llFriday.setVisibility(View.VISIBLE);

            ArrayList<Date> allDates = new ArrayList<>();
            for (int i = 0; i < tempTimeDayList.get(0).get(17).size(); i++) {
                try {
                    if (!tempTimeDayList.get(0).get(17).get(i).toString().equalsIgnoreCase("")) {
                        dateTime = sdf.parse(tempTimeDayList.get(0).get(17).get(i).toString());
                        allDates.add(dateTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(allDates);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            txtStartTimeFriday.setText(formattedString(displayFormat.format(allDates.get(0)).toString().toUpperCase()));
            txtEndTimeFriday.setText(formattedString(displayFormat.format(allDates.get(allDates.size() - 1)).toString().toUpperCase()));

        } else {
            llFriday.setVisibility(View.GONE);
        }

        count = 0;
        if (getCount(tempTimeDayList, 24) < tempTimeDayList.get(0).get(24).size()) {
            llSaturday.setVisibility(View.VISIBLE);

            ArrayList<Date> allDates = new ArrayList<>();
            for (int i = 0; i < tempTimeDayList.get(0).get(21).size(); i++) {
                try {
                    if (!tempTimeDayList.get(0).get(21).get(i).toString().equalsIgnoreCase("")) {
                        dateTime = sdf.parse(tempTimeDayList.get(0).get(21).get(i).toString());
                        allDates.add(dateTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(allDates);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            txtStartTimeSaturday.setText(formattedString(displayFormat.format(allDates.get(0)).toString().toUpperCase()));
            txtEndTimeSaturday.setText(formattedString(displayFormat.format(allDates.get(allDates.size() - 1)).toString().toUpperCase()));

        } else {
            llSaturday.setVisibility(View.GONE);
        }

        count = 0;
        if (getCount(tempTimeDayList, 28) < tempTimeDayList.get(0).get(28).size()) {
            llSunday.setVisibility(View.VISIBLE);

            ArrayList<Date> allDates = new ArrayList<>();
            for (int i = 0; i < tempTimeDayList.get(0).get(25).size(); i++) {
                try {
                    if (!tempTimeDayList.get(0).get(25).get(i).toString().equalsIgnoreCase("")) {
                        dateTime = sdf.parse(tempTimeDayList.get(0).get(25).get(i).toString());
                        allDates.add(dateTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(allDates);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            txtStartTimeSunday.setText(formattedString(displayFormat.format(allDates.get(0)).toString().toUpperCase()));
            txtEndTimeSunday.setText(formattedString(displayFormat.format(allDates.get(allDates.size() - 1)).toString().toUpperCase()));

        } else {
            llSunday.setVisibility(View.GONE);
        }
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


    public ArrayList<String> setTimesAndDaysValues() {
        ArrayList<String> dayandtimestemp = new ArrayList<>();
        if (llMonday.getVisibility() == View.VISIBLE) {
            dayandtimestemp.add("Monday|" + txtStartTimeMonday.getText().toString()
                    + "|" + txtEndTimeMonday.getText().toString());
        }
        if (llTuesday.getVisibility() == View.VISIBLE) {
            dayandtimestemp.add("Tuesday|" + txtStartTimeTuesday.getText().toString()
                    + "|" + txtEndTimeTuesday.getText().toString());
        }
        if (llWednesday.getVisibility() == View.VISIBLE) {
            dayandtimestemp.add("Wednesday|" + txtStartTimeWednesday.getText().toString()
                    + "|" + txtEndTimeWednesday.getText().toString());
        }
        if (llThursday.getVisibility() == View.VISIBLE) {
            dayandtimestemp.add("Thursday|" + txtStartTimeThursday.getText().toString()
                    + "|" + txtEndTimeThursday.getText().toString());
        }
        if (llFriday.getVisibility() == View.VISIBLE) {
            dayandtimestemp.add("Friday|" + txtStartTimeFriday.getText().toString()
                    + "|" + txtEndTimeFriday.getText().toString());
        }
        if (llSaturday.getVisibility() == View.VISIBLE) {
            dayandtimestemp.add("Saturday|" + txtStartTimeSaturday.getText().toString()
                    + "|" + txtEndTimeSaturday.getText().toString());
        }
        if (llSunday.getVisibility() == View.VISIBLE) {
            dayandtimestemp.add("Sunday|" + txtStartTimeSunday.getText().toString()
                    + "|" + txtEndTimeSunday.getText().toString());
        }

        Log.d("dayandtimestemp", "" + dayandtimestemp.toString());
        return dayandtimestemp;
    }

    @Override
    public void onBackPressed() {

        if (filterOn) {
            if (include_days_and_times_filter.getVisibility() == View.VISIBLE)
                include_days_and_times_filter.setVisibility(View.GONE);

            else if (include_instructor_filter.getVisibility() == View.VISIBLE)
                include_instructor_filter.setVisibility(View.GONE);

            else if (include_advanced_filter.getVisibility() == View.VISIBLE)
                include_advanced_filter.setVisibility(View.GONE);

            include_all_button_layout.setVisibility(View.VISIBLE);
            filterOn = false;
        } else {
            if (AppConfiguration.selectedStudentsName.size() == 1) {
                if (sendingID_std1.size() > 0) {
                    ClassAvailListTemp0 = filterInstructors(sendingID_std1, ClassAvailListTemp0);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 2) {
                if (sendingID_std1.size() > 0) {
                    ClassAvailListTemp0 = filterInstructors(sendingID_std1, ClassAvailListTemp0);
                }
                if (sendingID_std2.size() > 0) {
                    ClassAvailListTemp2 = filterInstructors(sendingID_std2, ClassAvailListTemp2);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 3) {
                if (sendingID_std1.size() > 0) {
                    ClassAvailListTemp0 = filterInstructors(sendingID_std1, ClassAvailListTemp0);
                }
                if (sendingID_std2.size() > 0) {
                    ClassAvailListTemp2 = filterInstructors(sendingID_std2, ClassAvailListTemp2);
                }
                if (sendingID_std3.size() > 0) {
                    ClassAvailListTemp3 = filterInstructors(sendingID_std3, ClassAvailListTemp3);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 4) {
                if (sendingID_std1.size() > 0) {
                    ClassAvailListTemp0 = filterInstructors(sendingID_std1, ClassAvailListTemp0);
                }
                if (sendingID_std2.size() > 0) {
                    ClassAvailListTemp2 = filterInstructors(sendingID_std2, ClassAvailListTemp2);
                }
                if (sendingID_std3.size() > 0) {
                    ClassAvailListTemp3 = filterInstructors(sendingID_std3, ClassAvailListTemp3);
                }
                if (sendingID_std4.size() > 0) {
                    ClassAvailListTemp4 = filterInstructors(sendingID_std4, ClassAvailListTemp4);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 5) {
                if (sendingID_std1.size() > 0) {
                    ClassAvailListTemp0 = filterInstructors(sendingID_std1, ClassAvailListTemp0);
                }
                if (sendingID_std2.size() > 0) {
                    ClassAvailListTemp2 = filterInstructors(sendingID_std2, ClassAvailListTemp2);
                }
                if (sendingID_std3.size() > 0) {
                    ClassAvailListTemp3 = filterInstructors(sendingID_std3, ClassAvailListTemp3);
                }
                if (sendingID_std4.size() > 0) {
                    ClassAvailListTemp4 = filterInstructors(sendingID_std4, ClassAvailListTemp4);
                }
                if (sendingID_std5.size() > 0) {
                    ClassAvailListTemp5 = filterInstructors(sendingID_std5, ClassAvailListTemp5);
                }
            }

            if (AppConfiguration.selectedStudentsName.size() == 1) {
                if (daysAndTimes_std1.size() > 0) {
                    ClassAvailListTemp0 = filterDays(daysAndTimes_std1, ClassAvailListTemp0);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 2) {
                if (daysAndTimes_std1.size() > 0) {
                    ClassAvailListTemp0 = filterDays(daysAndTimes_std1, ClassAvailListTemp0);
                }
                if (daysAndTimes_std2.size() > 0) {
                    ClassAvailListTemp2 = filterDays(daysAndTimes_std2, ClassAvailListTemp2);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 3) {
                if (daysAndTimes_std1.size() > 0) {
                    ClassAvailListTemp0 = filterDays(daysAndTimes_std1, ClassAvailListTemp0);
                }
                if (daysAndTimes_std2.size() > 0) {
                    ClassAvailListTemp2 = filterDays(daysAndTimes_std2, ClassAvailListTemp2);
                }
                if (daysAndTimes_std3.size() > 0) {
                    ClassAvailListTemp3 = filterDays(daysAndTimes_std3, ClassAvailListTemp3);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 4) {
                if (daysAndTimes_std1.size() > 0) {
                    ClassAvailListTemp0 = filterDays(daysAndTimes_std1, ClassAvailListTemp0);
                }
                if (daysAndTimes_std2.size() > 0) {
                    ClassAvailListTemp2 = filterDays(daysAndTimes_std2, ClassAvailListTemp2);
                }
                if (daysAndTimes_std3.size() > 0) {
                    ClassAvailListTemp3 = filterDays(daysAndTimes_std3, ClassAvailListTemp3);
                }
                if (daysAndTimes_std4.size() > 0) {
                    ClassAvailListTemp4 = filterDays(daysAndTimes_std4, ClassAvailListTemp4);
                }

            } else if (AppConfiguration.selectedStudentsName.size() == 5) {
                if (daysAndTimes_std1.size() > 0) {
                    ClassAvailListTemp0 = filterDays(daysAndTimes_std1, ClassAvailListTemp0);
                }
                if (daysAndTimes_std2.size() > 0) {
                    ClassAvailListTemp2 = filterDays(daysAndTimes_std2, ClassAvailListTemp2);
                }
                if (daysAndTimes_std3.size() > 0) {
                    ClassAvailListTemp3 = filterDays(daysAndTimes_std3, ClassAvailListTemp3);
                }
                if (daysAndTimes_std4.size() > 0) {
                    ClassAvailListTemp4 = filterDays(daysAndTimes_std4, ClassAvailListTemp4);
                }
                if (daysAndTimes_std5.size() > 0) {
                    ClassAvailListTemp5 = filterDays(daysAndTimes_std5, ClassAvailListTemp5);
                }
            }

            filterAdvanced();

            ClassAvailListTempStud0 = ClassAvailListTemp0;
            ClassAvailListTempStud2 = ClassAvailListTemp2;
            ClassAvailListTempStud3 = ClassAvailListTemp3;
            ClassAvailListTempStud4 = ClassAvailListTemp4;
            ClassAvailListTempStud5 = ClassAvailListTemp5;
            fromFilter = true;
            finish();
        }
    }

    public ArrayList<ArrayList<ArrayList<String>>> filterInstructors(ArrayList<String> sendingID_current, ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempForScheduling) {

        ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempForScheduling1 = new ArrayList<>();
        ArrayList<ArrayList<String>> _ClassAvailList1 = new ArrayList<ArrayList<String>>();
        ArrayList<String> instructors = new ArrayList<String>();
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

        for (int i = 0; i < sendingID_current.size(); i++) {
            for (ArrayList<ArrayList<String>> innerlist : ClassAvailListTempForScheduling) {
                for (int j = 0; j < innerlist.get(0).size(); j++) {
                    if (innerlist.get(0).get(j).equalsIgnoreCase(sendingID_current.get(i))) {
                        instructors.add(innerlist.get(0).get(j));
                        tcm_time.add(innerlist.get(1).get(j));
                        tcm_dt.add(innerlist.get(2).get(j));
                        tcm_cond.add(innerlist.get(3).get(j));
                        tcm_select.add(innerlist.get(4).get(j));
                        tct_time.add(innerlist.get(5).get(j));
                        tct_dt.add(innerlist.get(6).get(j));
                        tct_cond.add(innerlist.get(7).get(j));
                        tct_select.add(innerlist.get(8).get(j));
                        tcw_time.add(innerlist.get(9).get(j));
                        tcw_dt.add(innerlist.get(10).get(j));
                        tcw_cond.add(innerlist.get(11).get(j));
                        tcw_select.add(innerlist.get(12).get(j));
                        tcth_time.add(innerlist.get(13).get(j));
                        tcth_dt.add(innerlist.get(14).get(j));
                        tcth_cond.add(innerlist.get(15).get(j));
                        tcth_select.add(innerlist.get(16).get(j));
                        tcf_time.add(innerlist.get(17).get(j));
                        tcf_dt.add(innerlist.get(18).get(j));
                        tcf_cond.add(innerlist.get(19).get(j));
                        tcf_select.add(innerlist.get(20).get(j));
                        tcsa_time.add(innerlist.get(21).get(j));
                        tcsa_dt.add(innerlist.get(22).get(j));
                        tcsa_cond.add(innerlist.get(23).get(j));
                        tcsa_select.add(innerlist.get(24).get(j));
                        tcsu_time.add(innerlist.get(25).get(j));
                        tcsu_dt.add(innerlist.get(26).get(j));
                        tcsu_cond.add(innerlist.get(27).get(j));
                        tcsu_select.add(innerlist.get(28).get(j));

                    }
                }
                _ClassAvailList1.add(instructors);
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
            }
        }

        ClassAvailListTempForScheduling1.add(_ClassAvailList1);
        return ClassAvailListTempForScheduling1;
    }

    public ArrayList<ArrayList<ArrayList<String>>> filterDays(ArrayList<String> tempCheckList, ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempForScheduling) {

        ArrayList<String> days = new ArrayList<>();
        for (String day : tempCheckList) {
            String[] tempdays = day.split("\\|");
            days.add(tempdays[0]);
        }

        if (!days.contains("Monday")) {
            ClassAvailListTempForScheduling = removeADay("Monday", ClassAvailListTempForScheduling);
        }
        if (!days.contains("Tuesday")) {
            ClassAvailListTempForScheduling = removeADay("Tuesday", ClassAvailListTempForScheduling);
        }
        if (!days.contains("Wednesday")) {
            ClassAvailListTempForScheduling = removeADay("Wednesday", ClassAvailListTempForScheduling);
        }
        if (!days.contains("Thursday")) {
            ClassAvailListTempForScheduling = removeADay("Thursday", ClassAvailListTempForScheduling);
        }
        if (!days.contains("Friday")) {
            ClassAvailListTempForScheduling = removeADay("Friday", ClassAvailListTempForScheduling);
        }
        if (!days.contains("Saturday")) {
            ClassAvailListTempForScheduling = removeADay("Saturday", ClassAvailListTempForScheduling);
        }
        if (!days.contains("Sunday")) {
            ClassAvailListTempForScheduling = removeADay("Sunday", ClassAvailListTempForScheduling);
        }

        for (String data : tempCheckList) {
            if (data.contains("Monday")) {
                String[] temp = data.split("\\|");
                temp[1] = temp[1].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                temp[2] = temp[2].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                if (temp[1].equalsIgnoreCase("08:00") && temp[2].equalsIgnoreCase("07:40")) {
                    //dont remove class
                } else {
                    ClassAvailListTempForScheduling = removeClasses("Monday", temp[1].toString(), temp[2].toString(), ClassAvailListTempForScheduling);
                }
            } else if (data.contains("Tuesday")) {
                String[] temp = data.split("\\|");
                temp[1] = temp[1].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                temp[2] = temp[2].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                if (temp[1].equalsIgnoreCase("08:00") && temp[2].equalsIgnoreCase("07:40")) {
                    //dont remove class
                } else {
                    ClassAvailListTempForScheduling = removeClasses("Tuesday", temp[1].toString(), temp[2].toString(), ClassAvailListTempForScheduling);
                }
            } else if (data.contains("Wednesday")) {
                String[] temp = data.split("\\|");
                temp[1] = temp[1].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                temp[2] = temp[2].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                if (temp[1].equalsIgnoreCase("08:00") && temp[2].equalsIgnoreCase("07:40")) {
                    //dont remove class
                } else {
                    ClassAvailListTempForScheduling = removeClasses("Wednesday", temp[1].toString(), temp[2].toString(), ClassAvailListTempForScheduling);
                }
            } else if (data.contains("Thursday")) {
                String[] temp = data.split("\\|");
                temp[1] = temp[1].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                temp[2] = temp[2].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                if (temp[1].equalsIgnoreCase("08:00") && temp[2].equalsIgnoreCase("07:40")) {
                    //dont remove class
                } else {
                    ClassAvailListTempForScheduling = removeClasses("Thursday", temp[1].toString(), temp[2].toString(), ClassAvailListTempForScheduling);
                }
            } else if (data.contains("Friday")) {
                String[] temp = data.split("\\|");
                temp[1] = temp[1].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                temp[2] = temp[2].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                if (temp[1].equalsIgnoreCase("08:00") && temp[2].equalsIgnoreCase("07:40")) {
                    //dont remove class
                } else {
                    ClassAvailListTempForScheduling = removeClasses("Friday", temp[1].toString(), temp[2].toString(), ClassAvailListTempForScheduling);
                }
            } else if (data.contains("Saturday")) {
                String[] temp = data.split("\\|");
                temp[1] = temp[1].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                temp[2] = temp[2].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                if (temp[1].equalsIgnoreCase("08:00") && temp[2].equalsIgnoreCase("07:40")) {
                    //dont remove class
                } else {
                    ClassAvailListTempForScheduling = removeClasses("Saturday", temp[1].toString(), temp[2].toString(), ClassAvailListTempForScheduling);
                }
            } else if (data.contains("Sunday")) {
                String[] temp = data.split("\\|");
                temp[1] = temp[1].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                temp[2] = temp[2].toString().replace("AM", "").replace("PM", "").replace("am", "").replace("pm", "").trim();
                if (temp[1].equalsIgnoreCase("08:00") && temp[2].equalsIgnoreCase("07:40")) {
                    //dont remove class
                } else {
                    ClassAvailListTempForScheduling = removeClasses("Sunday", temp[1].toString(), temp[2].toString(), ClassAvailListTempForScheduling);
                }
            }
        }

        return ClassAvailListTempForScheduling;
    }

    public ArrayList<ArrayList<ArrayList<String>>> removeClasses(String day, String minValue, String maxValue, ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempForScheduling) {
        String[] originalArray = getResources().getStringArray(R.array.markArray);
        ArrayList<String> newArray = new ArrayList<>();
        //finding range
        outerloop:
        for (int i = 0; i < originalArray.length; i++) {
            if (originalArray[i].contains(minValue)) {
                int j = i;
                newArray.add(originalArray[i]);
                for (int k = j + 1; k < originalArray.length; k++) {
                    if (!originalArray[k].contains(maxValue)) {
                        newArray.add(originalArray[k]);
                    } else {
                        newArray.add(originalArray[k]);
                        break outerloop;
                    }
                }
            }
        }

        ArrayList<String> missingArray = new ArrayList<>();
        for (int i = 0; i < originalArray.length; i++) {
            if (!newArray.contains(originalArray[i])) {
                missingArray.add(originalArray[i]);
            }
        }

        if (day.equalsIgnoreCase("Monday")) {
            for (int i = 0; i < missingArray.size(); i++) {
                for (int j = 0; j < ClassAvailListTempForScheduling.get(0).get(1).size(); j++) {
                    if (ClassAvailListTempForScheduling.get(0).get(1).get(j).contains(missingArray.get(i))) {
                        ClassAvailListTempForScheduling.get(0).get(1).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(2).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(3).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(4).set(j, "");
                    }
                }
            }
        } else if (day.equalsIgnoreCase("Tuesday")) {
            for (int i = 0; i < missingArray.size(); i++) {
                for (int j = 0; j < ClassAvailListTempForScheduling.get(0).get(5).size(); j++) {
                    if (ClassAvailListTempForScheduling.get(0).get(5).get(j).contains(missingArray.get(i))) {
                        ClassAvailListTempForScheduling.get(0).get(5).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(6).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(7).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(8).set(j, "");
                    }
                }
            }
        } else if (day.equalsIgnoreCase("Wednesday")) {
            for (int i = 0; i < missingArray.size(); i++) {
                for (int j = 0; j < ClassAvailListTempForScheduling.get(0).get(9).size(); j++) {
                    if (ClassAvailListTempForScheduling.get(0).get(9).get(j).contains(missingArray.get(i))) {
                        ClassAvailListTempForScheduling.get(0).get(9).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(10).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(11).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(12).set(j, "");
                    }
                }
            }
        } else if (day.equalsIgnoreCase("Thursday")) {
            for (int i = 0; i < missingArray.size(); i++) {
                for (int j = 0; j < ClassAvailListTempForScheduling.get(0).get(13).size(); j++) {
                    if (ClassAvailListTempForScheduling.get(0).get(13).get(j).contains(missingArray.get(i))) {
                        ClassAvailListTempForScheduling.get(0).get(13).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(14).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(15).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(16).set(j, "");
                    }
                }
            }
        } else if (day.equalsIgnoreCase("Friday")) {
            for (int i = 0; i < missingArray.size(); i++) {
                for (int j = 0; j < ClassAvailListTempForScheduling.get(0).get(17).size(); j++) {
                    if (ClassAvailListTempForScheduling.get(0).get(17).get(j).contains(missingArray.get(i))) {
                        ClassAvailListTempForScheduling.get(0).get(17).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(18).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(19).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(20).set(j, "");
                    }
                }
            }
        } else if (day.equalsIgnoreCase("Saturday")) {
            for (int i = 0; i < missingArray.size(); i++) {
                for (int j = 0; j < ClassAvailListTempForScheduling.get(0).get(21).size(); j++) {
                    if (ClassAvailListTempForScheduling.get(0).get(21).get(j).contains(missingArray.get(i))) {
                        ClassAvailListTempForScheduling.get(0).get(21).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(22).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(23).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(24).set(j, "");
                    }
                }
            }
        } else if (day.equalsIgnoreCase("Sunday")) {
            for (int i = 0; i < missingArray.size(); i++) {
                for (int j = 0; j < ClassAvailListTempForScheduling.get(0).get(25).size(); j++) {
                    if (ClassAvailListTempForScheduling.get(0).get(25).get(j).contains(missingArray.get(i))) {
                        ClassAvailListTempForScheduling.get(0).get(25).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(26).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(27).set(j, "");
                        ClassAvailListTempForScheduling.get(0).get(28).set(j, "");
                    }
                }
            }
        }
        return ClassAvailListTempForScheduling;
    }

    public ArrayList<ArrayList<ArrayList<String>>> removeADay(String day, ArrayList<ArrayList<ArrayList<String>>> ClassAvailListTempForScheduling) {
        if (day.equalsIgnoreCase("Monday")) {
            for (int i = 0; i < ClassAvailListTempForScheduling.get(0).get(0).size(); i++) {
                ClassAvailListTempForScheduling.get(0).get(1).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(2).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(3).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(4).set(i, "");
            }
        } else if (day.equalsIgnoreCase("Tuesday")) {
            for (int i = 0; i < ClassAvailListTempForScheduling.get(0).get(0).size(); i++) {
                ClassAvailListTempForScheduling.get(0).get(5).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(6).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(7).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(8).set(i, "");
            }
        } else if (day.equalsIgnoreCase("Wednesday")) {
            for (int i = 0; i < ClassAvailListTempForScheduling.get(0).get(0).size(); i++) {
                ClassAvailListTempForScheduling.get(0).get(9).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(10).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(11).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(12).set(i, "");
            }
        } else if (day.equalsIgnoreCase("Thursday")) {
            for (int i = 0; i < ClassAvailListTempForScheduling.get(0).get(0).size(); i++) {
                ClassAvailListTempForScheduling.get(0).get(13).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(14).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(15).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(16).set(i, "");
            }
        } else if (day.equalsIgnoreCase("Friday")) {
            for (int i = 0; i < ClassAvailListTempForScheduling.get(0).get(0).size(); i++) {
                ClassAvailListTempForScheduling.get(0).get(17).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(18).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(19).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(20).set(i, "");
            }
        } else if (day.equalsIgnoreCase("Saturday")) {
            for (int i = 0; i < ClassAvailListTempForScheduling.get(0).get(0).size(); i++) {
                ClassAvailListTempForScheduling.get(0).get(21).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(22).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(23).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(24).set(i, "");
            }
        } else if (day.equalsIgnoreCase("Sunday")) {
            for (int i = 0; i < ClassAvailListTempForScheduling.get(0).get(0).size(); i++) {
                ClassAvailListTempForScheduling.get(0).get(25).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(26).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(27).set(i, "");
                ClassAvailListTempForScheduling.get(0).get(28).set(i, "");
            }
        }
        return ClassAvailListTempForScheduling;
    }

    public String nextClass20(String currentClassTime) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dfNew = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        String newFormatedDate = "";
        try {
            Date d = dfNew.parse(currentClassTime);
            cal.setTime(d);
            cal.add(Calendar.MINUTE, 20);
            newFormatedDate = dfNew.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFormatedDate.replace("am", "AM").replace("pm", "PM");
    }

    public String previousClass20(String currentClassTime) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dfNew = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        String newFormatedDate = "";
        try {
            Date d = dfNew.parse(currentClassTime);
            cal.setTime(d);
            cal.add(Calendar.MINUTE, -20);
            newFormatedDate = dfNew.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFormatedDate.replace("am", "AM").replace("pm", "PM");
    }

    public String nextClass40(String currentClassTime) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dfNew = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        String newFormatedDate = "";
        try {
            Date d = dfNew.parse(currentClassTime);
            cal.setTime(d);
            cal.add(Calendar.MINUTE, 40);
            newFormatedDate = dfNew.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFormatedDate.replace("am", "AM").replace("pm", "PM");
    }

    public String previousClass40(String currentClassTime) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dfNew = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        String newFormatedDate = "";
        try {
            Date d = dfNew.parse(currentClassTime);
            cal.setTime(d);
            cal.add(Calendar.MINUTE, -40);
            newFormatedDate = dfNew.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFormatedDate.replace("am", "AM").replace("pm", "PM");
    }


    public void filterClasses(String day, int interval) {

        if (interval == 20) {
            if (AppConfiguration.selectedStudentsName.size() == 2) {
                ClassAvailListTemp0 = filterProcessing20new(day, ClassAvailListTemp0, ClassAvailListTemp2, null, null, null, 2);
                ClassAvailListTemp2 = filterProcessing20new(day, ClassAvailListTemp2, ClassAvailListTemp0, null, null, null, 2);

            } else if (AppConfiguration.selectedStudentsName.size() == 3) {
                ClassAvailListTemp0 = filterProcessing20new(day, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, null, null, 3);
                ClassAvailListTemp2 = filterProcessing20new(day, ClassAvailListTemp2, ClassAvailListTemp0, ClassAvailListTemp3, null, null, 3);
                ClassAvailListTemp3 = filterProcessing20new(day, ClassAvailListTemp3, ClassAvailListTemp2, ClassAvailListTemp0, null, null, 3);

            } else if (AppConfiguration.selectedStudentsName.size() == 4) {
                ClassAvailListTemp0 = filterProcessing20new(day, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp4, null, 4);
                ClassAvailListTemp2 = filterProcessing20new(day, ClassAvailListTemp2, ClassAvailListTemp0, ClassAvailListTemp3, ClassAvailListTemp4, null, 4);
                ClassAvailListTemp3 = filterProcessing20new(day, ClassAvailListTemp3, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp4, null, 4);
                ClassAvailListTemp4 = filterProcessing20new(day, ClassAvailListTemp4, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, null, 4);

            } else if (AppConfiguration.selectedStudentsName.size() == 5) {
                ClassAvailListTemp0 = filterProcessing20new(day, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp4, ClassAvailListTemp5, 5);
                ClassAvailListTemp2 = filterProcessing20new(day, ClassAvailListTemp2, ClassAvailListTemp0, ClassAvailListTemp3, ClassAvailListTemp4, ClassAvailListTemp5, 5);
                ClassAvailListTemp3 = filterProcessing20new(day, ClassAvailListTemp3, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp4, ClassAvailListTemp5, 5);
                ClassAvailListTemp4 = filterProcessing20new(day, ClassAvailListTemp4, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp5, 5);
                ClassAvailListTemp5 = filterProcessing20new(day, ClassAvailListTemp5, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp4, 5);

            }
        } else if (interval == 40) {
            if (AppConfiguration.selectedStudentsName.size() == 2) {
                ClassAvailListTemp0 = filterProcessing40new(day, ClassAvailListTemp0, ClassAvailListTemp2, null, null, null, 2);
                ClassAvailListTemp2 = filterProcessing40new(day, ClassAvailListTemp2, ClassAvailListTemp0, null, null, null, 2);

            } else if (AppConfiguration.selectedStudentsName.size() == 3) {
                ClassAvailListTemp0 = filterProcessing40new(day, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, null, null, 3);
                ClassAvailListTemp2 = filterProcessing40new(day, ClassAvailListTemp2, ClassAvailListTemp0, ClassAvailListTemp3, null, null, 3);
                ClassAvailListTemp3 = filterProcessing40new(day, ClassAvailListTemp3, ClassAvailListTemp2, ClassAvailListTemp0, null, null, 3);

            } else if (AppConfiguration.selectedStudentsName.size() == 4) {
                ClassAvailListTemp0 = filterProcessing40new(day, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp4, null, 4);
                ClassAvailListTemp2 = filterProcessing40new(day, ClassAvailListTemp2, ClassAvailListTemp0, ClassAvailListTemp3, ClassAvailListTemp4, null, 4);
                ClassAvailListTemp3 = filterProcessing40new(day, ClassAvailListTemp3, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp4, null, 4);
                ClassAvailListTemp4 = filterProcessing40new(day, ClassAvailListTemp4, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, null, 4);

            } else if (AppConfiguration.selectedStudentsName.size() == 5) {
                ClassAvailListTemp0 = filterProcessing40new(day, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp4, ClassAvailListTemp5, 5);
                ClassAvailListTemp2 = filterProcessing40new(day, ClassAvailListTemp2, ClassAvailListTemp0, ClassAvailListTemp3, ClassAvailListTemp4, ClassAvailListTemp5, 5);
                ClassAvailListTemp3 = filterProcessing40new(day, ClassAvailListTemp3, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp4, ClassAvailListTemp5, 5);
                ClassAvailListTemp4 = filterProcessing40new(day, ClassAvailListTemp4, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp5, 5);
                ClassAvailListTemp5 = filterProcessing40new(day, ClassAvailListTemp5, ClassAvailListTemp0, ClassAvailListTemp2, ClassAvailListTemp3, ClassAvailListTemp4, 5);

            }
        }
    }

    public ArrayList<ArrayList<ArrayList<String>>> filterProcessing20new(String day, ArrayList<ArrayList<ArrayList<String>>>
            stud1Temp, ArrayList<ArrayList<ArrayList<String>>>
                                                                                 stud2Temp, ArrayList<ArrayList<ArrayList<String>>>
                                                                                 stud3Temp, ArrayList<ArrayList<ArrayList<String>>>
                                                                                 stud4Temp, ArrayList<ArrayList<ArrayList<String>>>
                                                                                 stud5Temp, int tempcount) {
        int row1 = 0, row2 = 0, row3 = 0, row4 = 0;

        if (day.equalsIgnoreCase("Monday")) {
            row1 = 1;
            row2 = 2;
            row3 = 3;
            row4 = 4;
        } else if (day.equalsIgnoreCase("Tuesday")) {
            row1 = 5;
            row2 = 6;
            row3 = 7;
            row4 = 8;
        } else if (day.equalsIgnoreCase("Wednesday")) {
            row1 = 9;
            row2 = 10;
            row3 = 11;
            row4 = 12;
        } else if (day.equalsIgnoreCase("Thursday")) {
            row1 = 13;
            row2 = 14;
            row3 = 15;
            row4 = 16;
        } else if (day.equalsIgnoreCase("Friday")) {
            row1 = 17;
            row2 = 18;
            row3 = 19;
            row4 = 20;
        } else if (day.equalsIgnoreCase("Saturday")) {
            row1 = 21;
            row2 = 22;
            row3 = 23;
            row4 = 24;
        } else if (day.equalsIgnoreCase("Sunday")) {
            row1 = 25;
            row2 = 26;
            row3 = 27;
            row4 = 28;
        }

        if (tempcount == 2) {

            Log.d("ADMSSSSSSSSSS", stud1Temp.get(0).get(row1).toString());
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                String currentClassDate;
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
//                    currentClassDate = stud1Temp.get(0).get(row2).get(i);//.substring(0, 5).toString();
//                    Log.d("DAteSSSSS", currentClassDate);
                    if (stud2Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass20(currentClassTime))){
                           /* && stud2Temp.get(0).get(row2).contains(currentClassDate))*/

                    } else {
                        stud1Temp.get(0).get(row1).set(i, "");
                        stud1Temp.get(0).get(row2).set(i, "");
                        stud1Temp.get(0).get(row3).set(i, "");
                        stud1Temp.get(0).get(row4).set(i, "");
                    }
                }

            }
        }
        if (tempcount == 3) {
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
                    if (stud2Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {

                    } else {
                        if (stud3Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                                stud3Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {

                        } else {
                            stud1Temp.get(0).get(row1).set(i, "");
                            stud1Temp.get(0).get(row2).set(i, "");
                            stud1Temp.get(0).get(row3).set(i, "");
                            stud1Temp.get(0).get(row4).set(i, "");
                        }
                    }
                }
            }
        }
        if (tempcount == 4) {
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
                    if (stud2Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {

                    } else {
                        if (stud3Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                                stud3Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {

                        } else {
                            if (stud4Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                                    stud4Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {

                            } else {
                                stud1Temp.get(0).get(row1).set(i, "");
                                stud1Temp.get(0).get(row2).set(i, "");
                                stud1Temp.get(0).get(row3).set(i, "");
                                stud1Temp.get(0).get(row4).set(i, "");
                            }
                        }
                    }
                }
            }
        }
        if (tempcount == 5) {
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
                    if (stud2Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {
                    } else {
                        if (stud3Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                                stud3Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {
                        } else {
                            if (stud4Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                                    stud4Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {
                            } else {
                                if (stud5Temp.get(0).get(row1).contains(previousClass20(currentClassTime)) ||
                                        stud5Temp.get(0).get(row1).contains(nextClass20(currentClassTime))) {
                                } else {
                                    stud1Temp.get(0).get(row1).set(i, "");
                                    stud1Temp.get(0).get(row2).set(i, "");
                                    stud1Temp.get(0).get(row3).set(i, "");
                                    stud1Temp.get(0).get(row4).set(i, "");
                                }
                            }
                        }
                    }
                }
            }
        }
        return stud1Temp;
    }

    public ArrayList<ArrayList<ArrayList<String>>> filterProcessing40new(String day, ArrayList<ArrayList<ArrayList<String>>> stud1Temp, ArrayList<ArrayList<ArrayList<String>>> stud2Temp, ArrayList<ArrayList<ArrayList<String>>> stud3Temp, ArrayList<ArrayList<ArrayList<String>>> stud4Temp, ArrayList<ArrayList<ArrayList<String>>> stud5Temp, int tempcount) {
        int row1 = 0, row2 = 0, row3 = 0, row4 = 0;

        if (day.equalsIgnoreCase("Monday")) {
            row1 = 1;
            row2 = 2;
            row3 = 3;
            row4 = 4;
        } else if (day.equalsIgnoreCase("Tuesday")) {
            row1 = 5;
            row2 = 6;
            row3 = 7;
            row4 = 8;
        } else if (day.equalsIgnoreCase("Wednesday")) {
            row1 = 9;
            row2 = 10;
            row3 = 11;
            row4 = 12;
        } else if (day.equalsIgnoreCase("Thursday")) {
            row1 = 13;
            row2 = 14;
            row3 = 15;
            row4 = 16;
        } else if (day.equalsIgnoreCase("Friday")) {
            row1 = 17;
            row2 = 18;
            row3 = 19;
            row4 = 40;
        } else if (day.equalsIgnoreCase("Saturday")) {
            row1 = 21;
            row2 = 22;
            row3 = 23;
            row4 = 24;
        } else if (day.equalsIgnoreCase("Sunday")) {
            row1 = 25;
            row2 = 26;
            row3 = 27;
            row4 = 28;
        }

        if (tempcount == 2) {
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
                    if (stud2Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                    } else {
                        stud1Temp.get(0).get(row1).set(i, "");
                        stud1Temp.get(0).get(row2).set(i, "");
                        stud1Temp.get(0).get(row3).set(i, "");
                        stud1Temp.get(0).get(row4).set(i, "");
                    }
                }
            }
        }
        if (tempcount == 3) {
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
                    if (stud2Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                    } else {
                        if (stud3Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                                stud3Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                        } else {
                            stud1Temp.get(0).get(row1).set(i, "");
                            stud1Temp.get(0).get(row2).set(i, "");
                            stud1Temp.get(0).get(row3).set(i, "");
                            stud1Temp.get(0).get(row4).set(i, "");
                        }
                    }
                }
            }
        }
        if (tempcount == 4) {
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
                    if (stud2Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                    } else {
                        if (stud3Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                                stud3Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                        } else {
                            if (stud4Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                                    stud4Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                            } else {
                                stud1Temp.get(0).get(row1).set(i, "");
                                stud1Temp.get(0).get(row2).set(i, "");
                                stud1Temp.get(0).get(row3).set(i, "");
                                stud1Temp.get(0).get(row4).set(i, "");
                            }
                        }
                    }
                }
            }
        }
        if (tempcount == 5) {
            for (int i = 0; i < stud1Temp.get(0).get(row1).size(); i++) {
                String currentClassTime = (stud1Temp.get(0).get(row1).get(i).length() > 0) ? stud1Temp.get(0).get(row1).get(i) : "";
                if (currentClassTime.length() > 0) {
                    currentClassTime = stud1Temp.get(0).get(row1).get(i);//.substring(0, 5).toString();
                    if (stud2Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                            stud2Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                    } else {
                        if (stud3Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                                stud3Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                        } else {
                            if (stud4Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                                    stud4Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                            } else {
                                if (stud5Temp.get(0).get(row1).contains(previousClass40(currentClassTime)) ||
                                        stud5Temp.get(0).get(row1).contains(nextClass40(currentClassTime))) {

                                } else {
                                    stud1Temp.get(0).get(row1).set(i, "");
                                    stud1Temp.get(0).get(row2).set(i, "");
                                    stud1Temp.get(0).get(row3).set(i, "");
                                    stud1Temp.get(0).get(row4).set(i, "");
                                }
                            }
                        }
                    }
                }
            }
        }
        return stud1Temp;
    }

    public void filterAdvanced() {

        try {
            if (rbNoPrefNew.isChecked()) {
                //nothing happens

//                fromAdvancedFilter = false;
            } else if (rb20MinsNew.isChecked()) {
                filterClasses("Monday", 20);
                filterClasses("Tuesday", 20);
                filterClasses("Wednesday", 20);
                filterClasses("Thursday", 20);
                filterClasses("Friday", 20);
                filterClasses("Saturday", 20);
                filterClasses("Sunday", 20);

            } else if (rb40MinsNew.isChecked()) {
                filterClasses("Monday", 40);
                filterClasses("Tuesday", 40);
                filterClasses("Wednesday", 40);
                filterClasses("Thursday", 40);
                filterClasses("Friday", 40);
                filterClasses("Saturday", 40);
                filterClasses("Sunday", 40);
            } else {//when no option is checked
                //nothing happens
//                fromAdvancedFilter = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inittypeface() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        tab1_txt.setTypeface(regular);
        tab2_txt.setTypeface(regular);
        tab3_txt.setTypeface(regular);

        rbNoPref.setTypeface(regular);
        rb20Mins.setTypeface(regular);
        rb40Mins.setTypeface(regular);
        rbNoPrefNew.setTypeface(regular);
        rb20MinsNew.setTypeface(regular);
        rb40MinsNew.setTypeface(regular);
        bttnApplyFilter.setTypeface(regular);
    }

    public void animateView(LinearLayout llTimeLayout, View viewLine, int current_clicked_position, int last_clicked_position) {

        Animation animSlidInLeft = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
        Animation animSlidInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);

        if (current_clicked_position > last_clicked_position) {
            listInstructornew.setVisibility(View.VISIBLE);
            listInstructornew.startAnimation(animSlidInLeft);
            if (llTimeLayout != null)
                llTimeLayout.startAnimation(animSlidInLeft);
            viewLine.startAnimation(animSlidInLeft);

        } else if (current_clicked_position < last_clicked_position) {
            listInstructornew.setVisibility(View.VISIBLE);
            listInstructornew.startAnimation(animSlidInRight);
            if (llTimeLayout != null)
                llTimeLayout.startAnimation(animSlidInRight);
            viewLine.startAnimation(animSlidInRight);

        } else {

        }
    }

    public void distributeStudentsTop(final View view) {

        st_1 = (LinearLayout) view.findViewById(R.id.st_1);
        st_2 = (LinearLayout) view.findViewById(R.id.st_2);
        st_3 = (LinearLayout) view.findViewById(R.id.st_3);
        st_4 = (LinearLayout) view.findViewById(R.id.st_4);
        st_5 = (LinearLayout) view.findViewById(R.id.st_5);

        select_1 = (View) view.findViewById(R.id.select_1);
        select_2 = (View) view.findViewById(R.id.select_2);
        select_3 = (View) view.findViewById(R.id.select_3);
        select_4 = (View) view.findViewById(R.id.select_4);
        select_5 = (View) view.findViewById(R.id.select_5);

        name_1 = (TextView) view.findViewById(R.id.name_1);
        name_2 = (TextView) view.findViewById(R.id.name_2);
        name_3 = (TextView) view.findViewById(R.id.name_3);
        name_4 = (TextView) view.findViewById(R.id.name_4);
        name_5 = (TextView) view.findViewById(R.id.name_5);

        select_1.setTag(1);
        select_2.setTag(2);
        select_3.setTag(3);
        select_4.setTag(4);
        select_5.setTag(5);

        st_1.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                current_clicked_position = 1;
                listInstructornew.setVisibility(View.GONE);
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

                if (view.getId() == R.id.include_instructor_filter) {

                    if (!AppConfiguration.instructorListBuilder.toString().equalsIgnoreCase(""))
                        inst1 = AppConfiguration.instructorListBuilder.toString().substring(0, AppConfiguration.instructorListBuilder.toString().length() - 1).split("\\,");

                    tempIns1 = new ArrayList<String>();
                    ArrayList<String> helperArray = new ArrayList<String>();
                    for (int a = 0; a < inst1.length; a++) {
                        if (ClassAvailListTemp0.get(0).get(0).contains(inst1[a].substring(inst1[a].lastIndexOf("--") + 2, inst1[a].length()))) {
                            helperArray.add(inst1[a].substring(0, inst1[a].lastIndexOf("--") - 2));
                            tempIns1.add(inst1[a].substring(inst1[a].lastIndexOf("--") + 2, inst1[a].length()));

                        }

                    }

                    sendingID_current = new ArrayList<String>();

                    SchedulingFilterInstArrayModel tempModel;
                    if (schedulingFilterInstArrayModels1.size() < 1) {
                        for (int i = 0; i < tempIns1.size(); i++) {
                            tempModel = new SchedulingFilterInstArrayModel();
                            tempModel.setInstructorID(helperArray.get(i).substring(0, 4).replace("-", ""));
                            tempModel.setInstructorName(tempIns1.get(i));
                            tempModel.setInstructorGender(helperArray.get(i).contains("F") ? "F" : "M");
                            tempModel.setInstructorPhoto(instNamePic.get(tempIns1.get(i)));
                            tempModel.setIsSelected(true);
                            schedulingFilterInstArrayModels1.add(tempModel);
                        }
                    }

                    inflateInstructorLayout(schedulingFilterInstArrayModels1);

                } else if (view.getId() == R.id.include_days_and_times_filter) {
                    setDaysandTimesUINew();
                }

                animateView(llTimeLayout, select_1, current_clicked_position, last_clicked_position);

                last_clicked_position = 1;
            }
        });

        st_2.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                current_clicked_position = 2;
                listInstructornew.setVisibility(View.GONE);
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

                if (view.getId() == R.id.include_instructor_filter) {

                    if (!AppConfiguration.instructorListBuilder1.toString().equalsIgnoreCase(""))
                        inst2 = AppConfiguration.instructorListBuilder1.toString().substring(0, AppConfiguration.instructorListBuilder1.toString().length() - 1).split("\\,");

                    tempIns2 = new ArrayList<String>();
                    ArrayList<String> helperArray = new ArrayList<String>();
                    for (int a = 0; a < inst2.length; a++) {

                        if (ClassAvailListTemp2.get(0).get(0).contains(inst2[a].substring(inst2[a].lastIndexOf("--") + 2, inst2[a].length()))) {
                            helperArray.add(inst2[a].substring(0, inst2[a].lastIndexOf("--") - 2));
                            tempIns2.add(inst2[a].substring(inst2[a].lastIndexOf("--") + 2, inst2[a].length()));
                        }

                    }

                    sendingID_current = new ArrayList<String>();
                    SchedulingFilterInstArrayModel tempModel;
                    if (schedulingFilterInstArrayModels2.size() < 1) {
                        for (int i = 0; i < tempIns2.size(); i++) {
                            tempModel = new SchedulingFilterInstArrayModel();
                            tempModel.setInstructorID(helperArray.get(i).substring(0, 4).replace("-", ""));
                            tempModel.setInstructorName(tempIns2.get(i));
                            tempModel.setInstructorGender(helperArray.get(i).contains("F") ? "F" : "M");
                            tempModel.setInstructorPhoto(instNamePic.get(tempIns2.get(i)));
                            tempModel.setIsSelected(true);
                            schedulingFilterInstArrayModels2.add(tempModel);
                        }
                    }

                    inflateInstructorLayout(schedulingFilterInstArrayModels2);

                } else if (view.getId() == R.id.include_days_and_times_filter) {
                    setDaysandTimesUINew();
                }

                animateView(llTimeLayout, select_2, current_clicked_position, last_clicked_position);

                last_clicked_position = 2;
            }
        });

        st_3.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                current_clicked_position = 3;
                listInstructornew.setVisibility(View.GONE);
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

                if (view.getId() == R.id.include_instructor_filter) {

                    if (!AppConfiguration.instructorListBuilder2.toString().equalsIgnoreCase(""))
                        inst3 = AppConfiguration.instructorListBuilder2.toString().substring(0, AppConfiguration.instructorListBuilder2.toString().length() - 1).split("\\,");

                    tempIns3 = new ArrayList<String>();
                    ArrayList<String> helperArray = new ArrayList<String>();
                    for (int a = 0; a < inst3.length; a++) {

                        if (ClassAvailListTemp3.get(0).get(0).contains(inst3[a].substring(inst3[a].lastIndexOf("--") + 2, inst3[a].length()))) {
                            helperArray.add(inst3[a].substring(0, inst3[a].lastIndexOf("--") - 2));
                            tempIns3.add(inst3[a].substring(inst3[a].lastIndexOf("--") + 2, inst3[a].length()));

                        }

                    }

                    sendingID_current = new ArrayList<String>();
                    SchedulingFilterInstArrayModel tempModel;
                    if (schedulingFilterInstArrayModels3.size() < 1) {
                        for (int i = 0; i < tempIns3.size(); i++) {
                            tempModel = new SchedulingFilterInstArrayModel();
                            tempModel.setInstructorID(helperArray.get(i).substring(0, 4).replace("-", ""));
                            tempModel.setInstructorName(tempIns3.get(i));
                            tempModel.setInstructorGender(helperArray.get(i).contains("F") ? "F" : "M");
                            tempModel.setInstructorPhoto(instNamePic.get(tempIns3.get(i)));
                            tempModel.setIsSelected(true);
                            schedulingFilterInstArrayModels3.add(tempModel);
                        }
                    }

                    inflateInstructorLayout(schedulingFilterInstArrayModels3);

                } else if (view.getId() == R.id.include_days_and_times_filter) {
                    setDaysandTimesUINew();
                }

                animateView(llTimeLayout, select_3, current_clicked_position, last_clicked_position);

                last_clicked_position = 3;
            }
        });

        st_4.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                current_clicked_position = 4;
                listInstructornew.setVisibility(View.GONE);
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

                if (view.getId() == R.id.include_instructor_filter) {

                    if (!AppConfiguration.instructorListBuilder3.toString().equalsIgnoreCase(""))
                        inst4 = AppConfiguration.instructorListBuilder3.toString().substring(0, AppConfiguration.instructorListBuilder3.toString().length() - 1).split("\\,");

                    tempIns4 = new ArrayList<String>();
                    ArrayList<String> helperArray = new ArrayList<String>();
                    for (int a = 0; a < inst4.length; a++) {

                        if (ClassAvailListTemp4.get(0).get(0).contains(inst4[a].substring(inst4[a].lastIndexOf("--") + 2, inst4[a].length()))) {
                            helperArray.add(inst4[a].substring(0, inst4[a].lastIndexOf("--") - 2));
                            tempIns4.add(inst4[a].substring(inst4[a].lastIndexOf("--") + 2, inst4[a].length()));

                        }

                    }

                    sendingID_current = new ArrayList<String>();
                    SchedulingFilterInstArrayModel tempModel;
                    if (schedulingFilterInstArrayModels4.size() < 1) {
                        for (int i = 0; i < tempIns4.size(); i++) {
                            tempModel = new SchedulingFilterInstArrayModel();
                            tempModel.setInstructorID(helperArray.get(i).substring(0, 4).replace("-", ""));
                            tempModel.setInstructorName(tempIns4.get(i));
                            tempModel.setInstructorGender(helperArray.get(i).contains("F") ? "F" : "M");
                            tempModel.setInstructorPhoto(instNamePic.get(tempIns4.get(i)));
                            tempModel.setIsSelected(true);
                            schedulingFilterInstArrayModels4.add(tempModel);
                        }
                    }

                    inflateInstructorLayout(schedulingFilterInstArrayModels4);

                } else if (view.getId() == R.id.include_days_and_times_filter) {
                    setDaysandTimesUINew();
                }

                animateView(llTimeLayout, select_4, current_clicked_position, last_clicked_position);

                last_clicked_position = 4;
            }
        });

        st_5.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                current_clicked_position = 5;
                listInstructornew.setVisibility(View.GONE);
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

                if (view.getId() == R.id.include_instructor_filter) {

                    if (!AppConfiguration.instructorListBuilder4.toString().equalsIgnoreCase(""))
                        inst5 = AppConfiguration.instructorListBuilder4.toString().substring(0, AppConfiguration.instructorListBuilder4.toString().length() - 1).split("\\,");

                    tempIns5 = new ArrayList<String>();
                    ArrayList<String> helperArray = new ArrayList<String>();
                    for (int a = 0; a < inst5.length; a++) {

                        if (ClassAvailListTemp5.get(0).get(0).contains(inst4[a].substring(inst4[a].lastIndexOf("--") + 2, inst4[a].length()))) {
                            helperArray.add(inst5[a].substring(0, inst5[a].lastIndexOf("--") - 2));
                            tempIns5.add(inst4[a].substring(inst4[a].lastIndexOf("--") + 2, inst4[a].length()));
                        }
                    }
                    sendingID_current = new ArrayList<String>();
                    SchedulingFilterInstArrayModel tempModel;
                    if (schedulingFilterInstArrayModels5.size() < 1) {
                        for (int i = 0; i < tempIns5.size(); i++) {
                            tempModel = new SchedulingFilterInstArrayModel();
                            tempModel.setInstructorID(helperArray.get(i).substring(0, 4).replace("-", ""));
                            tempModel.setInstructorName(tempIns5.get(i));
                            tempModel.setInstructorGender(helperArray.get(i).contains("F") ? "F" : "M");
                            tempModel.setInstructorPhoto(instNamePic.get(tempIns5.get(i)));
                            tempModel.setIsSelected(true);
                            schedulingFilterInstArrayModels5.add(tempModel);
                        }
                    }

                    inflateInstructorLayout(schedulingFilterInstArrayModels5);

                } else if (view.getId() == R.id.include_days_and_times_filter) {
                    setDaysandTimesUINew();
                }

                animateView(llTimeLayout, select_5, current_clicked_position, last_clicked_position);

                last_clicked_position = 5;
            }
        });

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
    }

    //    public void inflateInstructorLayout(ArrayList<String> instructorList, ArrayList<String> helperArray) {//, int pos
    public void inflateInstructorLayout(ArrayList<SchedulingFilterInstArrayModel> instructorListData) {//, int pos
        ArrayList<String> instructorListTemp = new ArrayList<>();
        for (int i = 0; i < instructorListData.size(); i++) {
            instructorListTemp.add(instructorListData.get(i).getInstructorName());
        }

        if (current_clicked_position == 1) {
            if (sendingID_std1.size() == 0)
                sendingID_std1 = instructorListTemp;
            sendingID_current = sendingID_std1;
        } else if (current_clicked_position == 2) {
            if (sendingID_std2.size() == 0)
                sendingID_std2 = instructorListTemp;
            sendingID_current = sendingID_std2;
        } else if (current_clicked_position == 3) {
            if (sendingID_std3.size() == 0)
                sendingID_std3 = instructorListTemp;
            sendingID_current = sendingID_std3;
        } else if (current_clicked_position == 4) {
            if (sendingID_std4.size() == 0)
                sendingID_std4 = instructorListTemp;
            sendingID_current = sendingID_std4;
        } else if (current_clicked_position == 5) {
            if (sendingID_std5.size() == 0)
                sendingID_std5 = instructorListTemp;
            sendingID_current = sendingID_std5;
        }

//        CustomListNew customListAdapter = new CustomListNew(this, instructorListTemp, helperArray);
        CustomListNew customListAdapter = new CustomListNew(this, instructorListData);
        listInstructornew.setAdapter(customListAdapter);
        customListAdapter.notifyDataSetChanged();
    }

    public class CustomListNew extends ArrayAdapter<String> {
        private final Activity context;
        //        private final ArrayList<String> data, helperArray;
        private Animation fade_in = AnimationUtils.loadAnimation(ShedulingFilter.this, R.anim.fade_in);
        private ArrayList<SchedulingFilterInstArrayModel> instructorListData;
        String temp_photo;

        //        public CustomListNew(Activity context, ArrayList<String> list, ArrayList<String> helperArray) {
        public CustomListNew(Activity context, ArrayList<SchedulingFilterInstArrayModel> instructorListData) {
            super(context, R.layout.layout_instructor_list_row);
            this.context = context;
            this.instructorListData = instructorListData;
//            this.helperArray = helperArray;

        }

        @Override
        public int getCount() {
            return instructorListData.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            LinearLayout changing_lay;
            Button view_bio, view_shift;
            TextView inst_name;
            CheckBox checkbox;
            View vw_shadow;
            CircleImageView inst_DP;
            int id;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final SchedulingFilterInstArrayModel schedulingFilterInstArrayModel = instructorListData.get(position);

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_instructor_list_row, null);
                holder.changing_lay = (LinearLayout) convertView.findViewById(R.id.changing_lay);
                holder.inst_name = (TextView) convertView.findViewById(R.id.inst_name);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.check_selected);
                holder.view_bio = (Button) convertView.findViewById(R.id.view_bio);
                holder.view_shift = (Button) convertView.findViewById(R.id.view_shift);
                holder.vw_shadow = (View) convertView.findViewById(R.id.vw_shadow);
                holder.inst_DP = (CircleImageView) convertView.findViewById(R.id.inst_DP);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            temp_photo = instNamePic.get(data.get(position));
            temp_photo = schedulingFilterInstArrayModel.getInstructorPhoto();
            holder.inst_name.setText(schedulingFilterInstArrayModel.getInstructorName());

            if (temp_photo != null) {
                if (temp_photo.trim().contains("~")) {
                    temp_photo = temp_photo.replace("~", "");
                    if (temp_photo.trim().contains(" ")) {
                        temp_photo = AppConfiguration.PhotoDomain + temp_photo.replace(" ", "%20");
                        System.out.println("Pic URL" + temp_photo);
                    } else {
                        temp_photo = AppConfiguration.PhotoDomain + temp_photo;
                        System.out.println("Pic URL" + temp_photo);
                    }
                    imageLoader.displayImage(temp_photo, holder.inst_DP, options);
                }
            }

            if (schedulingFilterInstArrayModel.getInstructorGender().equalsIgnoreCase("F")) {
                holder.changing_lay.setBackgroundResource(R.drawable.female_selector_transition);
            } else {
                holder.changing_lay.setBackgroundResource(R.drawable.male_selector_transition);
            }

            /*holder.checkbox.setChecked(true);
            TransitionDrawable transition1 = (TransitionDrawable) holder.changing_lay.getBackground();
            transition1.startTransition(200);*/

            if (schedulingFilterInstArrayModel.isSelected()) {
                holder.checkbox.setButtonTintList(getResources().getColorStateList(R.color.white));
                holder.checkbox.setChecked(true);
                holder.changing_lay.setBackgroundResource(R.drawable.selected_transition);
//                holder.changing_lay.startAnimation(fade_in);
//                TransitionDrawable transition = (TransitionDrawable) holder.changing_lay.getBackground();
//                transition.startTransition(200);
                holder.vw_shadow.setVisibility(View.VISIBLE);

            } else {
                holder.checkbox.setButtonTintList(getResources().getColorStateList(R.color.design_change_d2));
                holder.checkbox.setChecked(false);
//                TransitionDrawable transition = (TransitionDrawable) holder.changing_lay.getBackground();
//                transition.reverseTransition(0);
//                holder.changing_lay.startAnimation(fade_in);
                if (schedulingFilterInstArrayModel.getInstructorGender().equalsIgnoreCase("F")) {
                    holder.changing_lay.setBackgroundResource(R.drawable.female_selector_transition);
                } else {
                    holder.changing_lay.setBackgroundResource(R.drawable.male_selector_transition);
                }
                holder.vw_shadow.setVisibility(View.INVISIBLE);
            }
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkbox.isChecked()) {
//                    if (isChecked) {
                        holder.checkbox.setButtonTintList(getResources().getColorStateList(R.color.white));
                        TransitionDrawable transition = (TransitionDrawable) holder.changing_lay.getBackground();
                        transition.startTransition(200);
//                            holder.changing_lay.startAnimation(fade_in);
//                            holder.changing_lay.setBackgroundResource(R.drawable.selected_transition);
                        holder.vw_shadow.setVisibility(View.VISIBLE);

                        sendingID_current.remove("" + schedulingFilterInstArrayModel.getInstructorName());
                        sendingID_current.add("" + schedulingFilterInstArrayModel.getInstructorName());
                        schedulingFilterInstArrayModel.setIsSelected(true);
//                            instructorListData.add(position, schedulingFilterInstArrayModel);

                    } else {
                        if (sendingID_current.size() == 1) {
                            holder.checkbox.setChecked(true);
                            schedulingFilterInstArrayModel.setIsSelected(true);
                            Utility.ping(mContext, "Atleast one instructor is needed for each sutdent");
                        } else {
                            holder.checkbox.setButtonTintList(getResources().getColorStateList(R.color.design_change_d2));
                            TransitionDrawable transition = (TransitionDrawable) holder.changing_lay.getBackground();
                            transition.reverseTransition(500);
//                                holder.changing_lay.startAnimation(fade_in);
                            if (schedulingFilterInstArrayModel.getInstructorGender().equalsIgnoreCase("F")) {
                                holder.changing_lay.setBackgroundResource(R.drawable.female_selector_transition);
                            } else {
                                holder.changing_lay.setBackgroundResource(R.drawable.male_selector_transition);
                            }
                            holder.vw_shadow.setVisibility(View.INVISIBLE);
                            schedulingFilterInstArrayModel.setIsSelected(false);
//                                instructorListData.add(position, schedulingFilterInstArrayModel);
                            sendingID_current.remove("" + schedulingFilterInstArrayModel.getInstructorName());
                        }
                    }
                    Log.e("Scheduling Filter Selected Instructors", "" + sendingID_current);
                }
            });
            holder.view_bio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedInstructor = schedulingFilterInstArrayModel.getInstructorID();
                    new getBio().execute();
                }
            });
            holder.view_shift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedInstructor = schedulingFilterInstArrayModel.getInstructorID() + ":" + schedulingFilterInstArrayModel.getInstructorName();
                    new InstrcutorAvailalibityAsyncTask().execute();
                }
            });
            if (current_clicked_position == 1) {
                sendingID_std1 = sendingID_current;
            } else if (current_clicked_position == 2) {
                sendingID_std2 = sendingID_current;
            } else if (current_clicked_position == 3) {
                sendingID_std3 = sendingID_current;
            } else if (current_clicked_position == 4) {
                sendingID_std4 = sendingID_current;
            } else if (current_clicked_position == 5) {
                sendingID_std5 = sendingID_current;
            }

            return convertView;

        }
    }

    public class ViewDialog {

        public void showDialog(Activity activity, final TextView textView) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_time_filter);
            Button btnMinPlus = (Button) dialog.findViewById(R.id.btnMinPlus);
            Button btnMinMinus = (Button) dialog.findViewById(R.id.btnMinMinus);
            Button btnApply = (Button) dialog.findViewById(R.id.btnApply);
            TextView txtBodyText = (TextView) dialog.findViewById(R.id.txtBodyText);
            TextView imgv_icon_new = (TextView) dialog.findViewById(R.id.imgv_icon_new);
            final TextView txtMinText = (TextView) dialog.findViewById(R.id.txtMinText);


            String timeString = textView.getText().toString();
            final String tagStr = textView.getTag().toString();
            final String[] tag = tagStr.split("\\|");

            if (tag[0].equalsIgnoreCase("min"))
                txtBodyText.setText("Adjust your start time");
            else
                txtBodyText.setText("Adjust your end time");

            txtMinText.setText(timeString);
            imgv_icon_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnMinPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat dfNew = new SimpleDateFormat("hh:mm a");
                    Calendar cal = Calendar.getInstance();
                    String newFormatedDate = "";
                    try {
                        Date d = dfNew.parse(txtMinText.getText().toString());
                        cal.setTime(d);
                        cal.add(Calendar.MINUTE, 20);
                        newFormatedDate = dfNew.format(cal.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txtMinText.setText(newFormatedDate.replace("am", "AM").replace("pm", "PM"));
                }
            });
            btnMinMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat dfNew = new SimpleDateFormat("hh:mm a");
                    Calendar cal = Calendar.getInstance();
                    String newFormatedDate = "";
                    try {
                        Date d = dfNew.parse(txtMinText.getText().toString());
                        cal.setTime(d);
                        cal.add(Calendar.MINUTE, -20);
                        newFormatedDate = dfNew.format(cal.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txtMinText.setText(newFormatedDate.replace("am", "AM").replace("pm", "PM"));
                }
            });
            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tempString = txtMinText.getText().toString();//txtHourText.getText().toString() + ":" + txtMinText.getText().toString() + " " + txtAMPMText.getText().toString();

                    if (current_clicked_position == 1) {

                        for (int i = 0; i < daysAndTimes_std1TempOri.size(); i++) {
                            if (daysAndTimes_std1TempOri.get(i).contains(tag[1])) {
                                String[] temp = daysAndTimes_std1TempOri.get(i).toString().split("\\|");
                                String minOri = temp[1];
                                String maxOri = temp[2];

                                if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    Utility.ping(mContext, "This time is not allowed");

                                } else if (tag[0].equalsIgnoreCase("min") && Utility.isFirstTimeBefore(tempString, minOri).equalsIgnoreCase("less")) {
                                    Utility.ping(mContext, "Time cannot be earlier than original time");

                                } else if (tag[0].equalsIgnoreCase("max") && Utility.isFirstTimeBefore(tempString, maxOri).equalsIgnoreCase("more")) {
                                    Utility.ping(mContext, "Time cannot be later than original time");

                                } else if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std1Temp = setTimesAndDaysValues();

                                } else {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std1Temp = setTimesAndDaysValues();
                                }
                            }
                        }

                    } else if (current_clicked_position == 2) {

                        for (int i = 0; i < daysAndTimes_std2TempOri.size(); i++) {
                            if (daysAndTimes_std2TempOri.get(i).contains(tag[1])) {
                                String[] temp = daysAndTimes_std2TempOri.get(i).toString().split("\\|");
                                String minOri = temp[1];
                                String maxOri = temp[2];

                                if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    Utility.ping(mContext, "This time is not allowed");

                                } else if (tag[0].equalsIgnoreCase("min") && Utility.isFirstTimeBefore(tempString, minOri).equalsIgnoreCase("less")) {
                                    Utility.ping(mContext, "Time cannot be earlier than original time");

                                } else if (tag[0].equalsIgnoreCase("max") && Utility.isFirstTimeBefore(tempString, maxOri).equalsIgnoreCase("more")) {
                                    Utility.ping(mContext, "Time cannot be later than original time");

                                } else if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std2Temp = setTimesAndDaysValues();

                                } else {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std2Temp = setTimesAndDaysValues();
                                }
                            }
                        }

                    } else if (current_clicked_position == 3) {

                        for (int i = 0; i < daysAndTimes_std3TempOri.size(); i++) {
                            if (daysAndTimes_std3TempOri.get(i).contains(tag[1])) {
                                String[] temp = daysAndTimes_std3TempOri.get(i).toString().split("\\|");
                                String minOri = temp[1];
                                String maxOri = temp[2];

                                if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    Utility.ping(mContext, "This time is not allowed");

                                } else if (tag[0].equalsIgnoreCase("min") && Utility.isFirstTimeBefore(tempString, minOri).equalsIgnoreCase("less")) {
                                    Utility.ping(mContext, "Time cannot be earlier than original time");

                                } else if (tag[0].equalsIgnoreCase("max") && Utility.isFirstTimeBefore(tempString, maxOri).equalsIgnoreCase("more")) {
                                    Utility.ping(mContext, "Time cannot be later than original time");

                                } else if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std3Temp = setTimesAndDaysValues();

                                } else {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std3Temp = setTimesAndDaysValues();
                                }
                            }
                        }

                    } else if (current_clicked_position == 4) {

                        for (int i = 0; i < daysAndTimes_std4TempOri.size(); i++) {
                            if (daysAndTimes_std4TempOri.get(i).contains(tag[1])) {
                                String[] temp = daysAndTimes_std4TempOri.get(i).toString().split("\\|");
                                String minOri = temp[1];
                                String maxOri = temp[2];

                                if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    Utility.ping(mContext, "This time is not allowed");

                                } else if (tag[0].equalsIgnoreCase("min") && Utility.isFirstTimeBefore(tempString, minOri).equalsIgnoreCase("less")) {
                                    Utility.ping(mContext, "Time cannot be earlier than original time");

                                } else if (tag[0].equalsIgnoreCase("max") && Utility.isFirstTimeBefore(tempString, maxOri).equalsIgnoreCase("more")) {
                                    Utility.ping(mContext, "Time cannot be later than original time");

                                } else if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std4Temp = setTimesAndDaysValues();

                                } else {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std4Temp = setTimesAndDaysValues();
                                }
                            }
                        }

                    } else if (current_clicked_position == 5) {

                        for (int i = 0; i < daysAndTimes_std5TempOri.size(); i++) {
                            if (daysAndTimes_std5TempOri.get(i).contains(tag[1])) {
                                String[] temp = daysAndTimes_std5TempOri.get(i).toString().split("\\|");
                                String minOri = temp[1];
                                String maxOri = temp[2];

                                if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    Utility.ping(mContext, "This time is not allowed");

                                } else if (tag[0].equalsIgnoreCase("min") && Utility.isFirstTimeBefore(tempString, minOri).equalsIgnoreCase("less")) {
                                    Utility.ping(mContext, "Time cannot be earlier than original time");

                                } else if (tag[0].equalsIgnoreCase("max") && Utility.isFirstTimeBefore(tempString, maxOri).equalsIgnoreCase("more")) {
                                    Utility.ping(mContext, "Time cannot be later than original time");

                                } else if (!Utility.isInAllowedTime(mContext, tempString)) {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std5Temp = setTimesAndDaysValues();

                                } else {
                                    dialog.dismiss();
                                    textView.setText(tempString);
                                    daysAndTimes_std5Temp = setTimesAndDaysValues();
                                }
                            }
                        }

                    }
                }
            });

            dialog.show();
        }
    }

    public void timeDateFilterListners() {

        txtStartTimeMonday.setOnClickListener(listener);
        txtStartTimeMonday.setTag("min|Monday");
        txtEndTimeMonday.setOnClickListener(listener);
        txtEndTimeMonday.setTag("max|Monday");
        txtStartTimeTuesday.setOnClickListener(listener);
        txtStartTimeTuesday.setTag("min|Tuesday");
        txtEndTimeTuesday.setOnClickListener(listener);
        txtEndTimeTuesday.setTag("max|Tuesday");
        txtStartTimeWednesday.setOnClickListener(listener);
        txtStartTimeWednesday.setTag("min|Wednesday");
        txtEndTimeWednesday.setOnClickListener(listener);
        txtEndTimeWednesday.setTag("max|Wednesday");
        txtStartTimeThursday.setOnClickListener(listener);
        txtStartTimeThursday.setTag("min|Thursday");
        txtEndTimeThursday.setOnClickListener(listener);
        txtEndTimeThursday.setTag("max|Thursday");
        txtStartTimeFriday.setOnClickListener(listener);
        txtStartTimeFriday.setTag("min|Friday");
        txtEndTimeFriday.setOnClickListener(listener);
        txtEndTimeFriday.setTag("max|Friday");
        txtStartTimeSaturday.setOnClickListener(listener);
        txtStartTimeSaturday.setTag("min|Saturday");
        txtEndTimeSaturday.setOnClickListener(listener);
        txtEndTimeSaturday.setTag("max|Saturday");
        txtStartTimeSunday.setOnClickListener(listener);
        txtStartTimeSunday.setTag("min|Sunday");
        txtEndTimeSunday.setOnClickListener(listener);
        txtEndTimeSunday.setTag("max|Sunday");

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(ShedulingFilter.this, (TextView) v);
        }
    };

    ArrayList<HashMap<String, String>> aboutInst = new ArrayList<HashMap<String, String>>();
    String successLoadChildList;

    public class getBio extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

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
                final PopupWindow popWindow = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//                popWindow.setAnimationStyle(R.style.Animation);
                popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//                Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
//                slide_up.setDuration(200);
                LinearLayout pop_square;

                pop_square = (LinearLayout) layout.findViewById(R.id.pop_square);
//                pop_square.startAnimation(slide_up);
                TextView tv_appname, inst_name, about, done1;
                CircleImageView inst_dp;
                Button done;
                done = (Button) layout.findViewById(R.id.done);
                tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
                inst_name = (TextView) layout.findViewById(R.id.inst_name);
                about = (TextView) layout.findViewById(R.id.about);
                inst_dp = (CircleImageView) layout.findViewById(R.id.inst_DP);

                done.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();
                    }
                });

                TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
                imgv_icon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();

                    }
                });
                Button button_ok = (Button) layout.findViewById(R.id.button_ok);
                button_ok.setOnClickListener(new View.OnClickListener() {

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
    String selectedInstructor = "";

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
                final PopupWindow popWindow = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//                popWindow.setAnimationStyle(R.style.Animation);
//                Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
//                slide_up.setDuration(200);
                LinearLayout pop_square;
                pop_square = (LinearLayout) layout.findViewById(R.id.pop_square);
//                pop_square.startAnimation(slide_up);
                popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
                tv_appname.setText("Shifts");

                TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
//                Animation animSlidup = AnimationUtils.loadAnimation(mContext, R.anim.slid_up_popup);
//                layout.startAnimation(animSlidup);
                imgv_icon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popWindow.dismiss();

                    }
                });
                Button button_ok = (Button) layout.findViewById(R.id.button_ok);
                button_ok.setOnClickListener(new View.OnClickListener() {

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

                done.setOnClickListener(new View.OnClickListener() {

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
        param.put("Makeupflg", "0");
        param.put("sselected", "0");
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
}
