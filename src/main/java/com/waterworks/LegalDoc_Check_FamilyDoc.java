package com.waterworks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.ProgressWheel;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class LegalDoc_Check_FamilyDoc extends Activity {
    TextView Check_response, page_count, title, relation_title, check_text, agree_text;
    String response_leg;
    String Token = "", old_number = "";
    SharedPreferences token_detail;
    String DOMAIN = AppConfiguration.DOMAIN;
    Button submit, Disc_Submit;
    CheckBox agreed_1, email_this, desc_content;
    WebView html;
    ArrayList<String> content_arr = new ArrayList<String>();
    ArrayList<String> ConfrmText_arr = new ArrayList<String>();
    ArrayList<String> PageID_arr = new ArrayList<String>();
    ArrayList<String> PageNo_arr = new ArrayList<String>();
    ArrayList<String> TotalPages_arr = new ArrayList<String>();
    ArrayList<String> RequiredParent_arr = new ArrayList<String>();
    ArrayList<String> RequiredStudent_arr = new ArrayList<String>();
    ArrayList<String> RequiredStudentRelation_arr = new ArrayList<String>();
    ArrayList<String> RequiredSignature_arr = new ArrayList<String>();
    ArrayList<String> RequiredDate_arr = new ArrayList<String>();
    ArrayList<String> RequiredEmergency_arr = new ArrayList<String>();
    ArrayList<String> DisclaimerID_arr = new ArrayList<String>();
    ArrayList<String> DiscmrAfterBeforStatus_arr = new ArrayList<String>();
    ArrayList<String> SiteID_arr = new ArrayList<String>();
    ArrayList<String> DocCount_arr = new ArrayList<String>();
    ArrayList<String> DocID_arr = new ArrayList<String>();
    ArrayList<String> Children_arr = new ArrayList<String>();
    ArrayList<String> StudentID_arr = new ArrayList<String>();
    ArrayList<String> StudentID_arr_backup = new ArrayList<String>();//Backup Fix
    ArrayList<String> DocName_arr = new ArrayList<String>();
    ArrayList<String> StudentID_Family_Arr = new ArrayList<String>();
    ArrayList<String> DocGroupID_Arr = new ArrayList<String>();
    ArrayList<String> DisclaimerName_Arr = new ArrayList<String>();
    ArrayList<String> DisclDetailsID_Arr = new ArrayList<String>();
    ArrayList<String> DisclaimerContent_Arr = new ArrayList<String>();
    ArrayList<String> StudentList_Arr = new ArrayList<String>();
    List<String> final_student_list = new ArrayList<String>();
    String which_in = "";
    int studnt_count = 0, size_of_Array = 0;

    HashMap<String, String> student_ID = new HashMap<String, String>();
    //Current content is used to track the current position of doc.
    int current_content = 0;
    int page_id_content = 0;
    TableRow sign, parent, date, emr_name, emr_relation, emr_number;
    EditText sign_et, parent_et, date_et, relation, emr_name_et, emr_relation_et, emr_number_et;
    ScrollView scrollview;
    LinearLayout emr_lay, desclaimer_check_lay, page_lay;
    TableLayout student_list_lay, student_list_lay_et, student_list_lay_title;
    Context mContext = this;
    boolean with_et = false;
    ProgressDialog pd;
    Handler handler;
    boolean first_time = true, desclaimer_bool = false, Submit_Error = false;
    //	Dialog dialog;
    ProgressWheel progress_splash;
    String current_time = "";
    Boolean isInternetPresent = false;
    Fragment fragment = null;
    int myid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal_doc_check);
        current_time = setcurrent_time();
        token_detail = AppConfiguration.getSharedPrefs(LegalDoc_Check_FamilyDoc.this);
        Token = token_detail.getString("Token", "");
        initialize();
        Log.d("Response:", Token);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void initialize() {
        progress_splash = (ProgressWheel) findViewById(R.id.progressWheel1);
        Check_response = (TextView) findViewById(R.id.check_response);
        page_count = (TextView) findViewById(R.id.page_count);
        title = (TextView) findViewById(R.id.title);
        relation_title = (TextView) findViewById(R.id.realtion_title);
        agree_text = (TextView) findViewById(R.id.agree_text);

        email_this = (CheckBox) findViewById(R.id.email_this);
        agreed_1 = (CheckBox) findViewById(R.id.agreed_1);
        agreed_1.setFocusable(true);
        agreed_1.setFocusableInTouchMode(true);

        submit = (Button) findViewById(R.id.submit);
        Disc_Submit = (Button) findViewById(R.id.Disc_Submit);

        sign = (TableRow) findViewById(R.id.sign);
        parent = (TableRow) findViewById(R.id.parent);
        date = (TableRow) findViewById(R.id.date);
        emr_name = (TableRow) findViewById(R.id.emr_name);
        emr_number = (TableRow) findViewById(R.id.emr_number);
        emr_relation = (TableRow) findViewById(R.id.emr_relation);

        sign_et = (EditText) findViewById(R.id.sign_et);
        parent_et = (EditText) findViewById(R.id.parent_et);
        date_et = (EditText) findViewById(R.id.date_et);
        emr_name_et = (EditText) findViewById(R.id.emr_name_et);
        emr_number_et = (EditText) findViewById(R.id.emr_number_et);
        emr_relation_et = (EditText) findViewById(R.id.emr_relation_et);

        scrollview = (ScrollView) findViewById(R.id.scroll_view);
        student_list_lay = (TableLayout) findViewById(R.id.student_list_lay);
        student_list_lay_et = (TableLayout) findViewById(R.id.student_list_lay_et);
        student_list_lay_title = (TableLayout) findViewById(R.id.student_list_lay_title);

        emr_lay = (LinearLayout) findViewById(R.id.emr_lay);
        desclaimer_check_lay = (LinearLayout) findViewById(R.id.desclaimer_check_lay);
        page_lay = (LinearLayout) findViewById(R.id.page_lay);

        html = (WebView) findViewById(R.id.html);

        try {
            if (DOMAIN.isEmpty()) {
                DOMAIN = getIntent().getStringExtra("DOMAIN");
                Log.i("Login", "Url = " + DOMAIN);
                Legal_DOC_chk_method();
            } else {
                Legal_DOC_chk_method();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        emr_number_et.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                    if (emr_number_et.getText().toString().trim().length() >= 10) {
                        emr_number_et.setText(formatPhoneNumber(emr_number_et.getText().toString().trim()));
                    } else {
                        toast("Please insert valid number");
                    }
                }
                return false;
            }
        });
        emr_number_et.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                emr_number_et.setText("");
            }
        });
        html.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                view.clearCache(true);
            }
        });
        Disc_Submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "Processing...", Toast.LENGTH_LONG).show();

                isInternetPresent = Utility.isNetworkConnected(LegalDoc_Check_FamilyDoc.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    show_dialog();
                    if (check_Checks()) {
                        LegDoc__Insert_Disclaimer_Dtl();
                    } else {
                        dismiss_it();
                        scrollview.smoothScrollBy(0, -400);
                        toast("Please read and check all the Checkboxes");
                    }
                }
            }
        });
        submit.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "Processing...", Toast.LENGTH_SHORT).show();
                isInternetPresent = Utility.isNetworkConnected(LegalDoc_Check_FamilyDoc.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    show_dialog();
                    if (agreed_1.isChecked()) {
                        agreed_1.setBackgroundResource(0);
                        if (sign.getVisibility() == View.VISIBLE) {
                            //Sign field check
                            if (sign_et.getText().toString().trim().length() > 0) {
                                sign_et.setBackgroundResource(R.drawable.input_box);
                                //							if(RequiredEmergency_arr.get(current_content).contains("Y")){
                                if (emr_lay.getVisibility() == View.VISIBLE) {
                                    //Emr field check
                                    if (emr_name_et.getText().toString().trim().length() > 0) {
                                        emr_name_et.setBackgroundResource(R.drawable.input_box);
                                        if (emr_number_et.getText().toString().trim().length() >= 10) {
                                            emr_number_et.setBackgroundResource(R.drawable.input_box);

                                            if (emr_relation_et.getText().toString().trim().length() > 0) {
                                                emr_relation_et.setBackgroundResource(R.drawable.input_box);
                                                //EMR Check done
                                                if (RequiredStudentRelation_arr.get(current_content).contains("Y")) {
                                                    if (check_realtion_value()) {
                                                        updateandset();
                                                    }
                                                } else {
                                                    updateandset();
                                                }
                                            } else {
                                                dismiss_it();
                                                toast("Emergency Relation detail is missing.");
                                                emr_relation_et.setBackgroundResource(R.drawable.error_border);
                                            }
                                        } else {
                                            dismiss_it();
                                            toast("Emergency number detail is missing.");
                                            emr_number_et.setBackgroundResource(R.drawable.error_border);
                                        }
                                    } else {
                                        dismiss_it();
                                        toast("Emergency Name detail is missing.");
                                        emr_name_et.setBackgroundResource(R.drawable.error_border);
                                    }
                                } else {
                                    //EMR field missing
                                    //Student Relation Visible
                                    if (RequiredStudentRelation_arr.get(current_content).contains("Y")) {
                                        if (check_realtion_value()) {
                                            updateandset();
                                        }
                                    } else {
                                        updateandset();
                                    }
                                }
                            } else {
                                toast("Please sign the Doc.");
                                dismiss_it();
                                int sdk = android.os.Build.VERSION.SDK_INT;
                                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    sign_et.setBackgroundDrawable(getResources().getDrawable(R.drawable.error_border));
                                } else {
                                    sign_et.setBackground(getResources().getDrawable(R.drawable.error_border));
                                }
                            }
                        } else {
                            //Sign field missing
                            if (validation_withou_sign()) {
                                updateandset();
                            }
                        }
                    } else {
                        //Scroll scrollview to missing parameters.
                        toast("Please check the Checkbox");
                        dismiss_it();
//                        pd.dismiss();
                        //					scrollview.smoothScrollTo(0, 80);
                        if (emr_lay.getVisibility() == View.VISIBLE) {
                            scrollview.smoothScrollBy(0, -800);
                        } else {
                            scrollview.smoothScrollBy(0, -300);
                        }
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            agreed_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.error_border));
                        } else {
                            agreed_1.setBackground(getResources().getDrawable(R.drawable.error_border));
                        }
                    }
                }
            }
        });
    }
    public boolean validation_withou_sign() {
        boolean value = false;
//		if(RequiredEmergency_arr.get(current_content).contains("Y") ||
//				RequiredStudentRelation_arr.get(current_content).contains("Y")
//				){
//			//Emr field check
//			if(emr_name_et.getText().toString().trim().length()>0){
//				emr_name_et.setBackgroundResource(R.drawable.input_box);
//				if(emr_number_et.getText().toString().trim().length()>=10){
//					emr_number_et.setBackgroundResource(R.drawable.input_box);	
//
//					if(emr_relation_et.getText().toString().trim().length()>0){
//						emr_relation_et.setBackgroundResource(R.drawable.input_box);	
//						//EMR Check done
//						if(RequiredStudentRelation_arr.get(current_content).contains("Y")){
//							if(check_realtion_value()){
//								//								updateandset();
//								value=true;
//							}
//						}else{
//							//							updateandset();
//							value=true;
//						}
//					}else{
//						dismiss_it();
//						toast("Emergency Relation detail is missing.");
//						emr_relation_et.setBackgroundResource(R.drawable.error_border);
//					}
//				}else{
//					dismiss_it();
//					toast("Emergency number detail is missing.");
//					emr_number_et.setBackgroundResource(R.drawable.error_border);
//				}
//			}else{
//				dismiss_it();
//				toast("Emergency Name detail is missing.");
//				emr_name_et.setBackgroundResource(R.drawable.error_border);
//			}
//
//		}else{
//			value=true;
//		}
        if (emr_lay.getVisibility() == View.VISIBLE) {
            //Emr field check
            if (emr_name_et.getText().toString().trim().length() > 0) {
                emr_name_et.setBackgroundResource(R.drawable.input_box);
                if (emr_number_et.getText().toString().trim().length() >= 10) {
                    emr_number_et.setBackgroundResource(R.drawable.input_box);

                    if (emr_relation_et.getText().toString().trim().length() > 0) {
                        emr_relation_et.setBackgroundResource(R.drawable.input_box);
                        //EMR Check done
                        if (RequiredStudentRelation_arr.get(current_content).contains("Y")) {
                            if (check_realtion_value()) {
                                value = true;
                            }
                        } else {
                            value = true;
                        }
                    } else {
                        dismiss_it();
                        toast("Emergency Relation detail is missing.");
                        emr_relation_et.setBackgroundResource(R.drawable.error_border);
                    }
                } else {
                    dismiss_it();
                    toast("Emergency number detail is missing.");
                    emr_number_et.setBackgroundResource(R.drawable.error_border);
                }
            } else {
                dismiss_it();
                toast("Emergency Name detail is missing.");
                emr_name_et.setBackgroundResource(R.drawable.error_border);
            }
        } else {
            //EMR field missing
            //Student Relation Visible
            if (RequiredStudentRelation_arr.get(current_content).contains("Y")) {
                if (check_realtion_value()) {
                    value = true;
                }
            } else {
                value = true;
            }
        }
        return value;
    }
    public void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    @SuppressWarnings("deprecation")
    public boolean check_realtion_value() {
        boolean all_check = true;
        for (int i = 0; i < student_list_lay_et.getChildCount(); i++) {
            View child = student_list_lay_et.getChildAt(i);
            EditText uredt = (EditText) child.findViewById(R.id.relation);
            String value = uredt.getText().toString();
            if (value.equals("")) {
                uredt.setBackgroundDrawable(getResources().getDrawable(R.drawable.error_border));
                toast("Relation field blank.");
                all_check = false;
                dismiss_it();
                break;
            } else {
                uredt.setBackgroundResource(R.drawable.input_box);
            }
        }
        return all_check;
    }
   @SuppressWarnings("deprecation")
    public boolean check_Checks() {
        boolean all_check = true;
        for (int i = 0; i < desclaimer_check_lay.getChildCount(); i++) {
            View child = desclaimer_check_lay.getChildAt(i);
            CheckBox urchk = (CheckBox) child.findViewById(R.id.desclaimer_check);
            if (!urchk.isChecked()) {
                urchk.setBackgroundDrawable(getResources().getDrawable(R.drawable.error_border));
                all_check = false;
                toast("Please read and check the Checkboxes\".");
            } else {
                urchk.setBackgroundResource(0);
            }
        }
        return all_check;
    }
    public String get_relation_value(int j) {
        String realtion_value = "";
        if (j <= student_list_lay_et.getChildCount()) {
            if (student_list_lay_et.getChildCount() > 0) {
                View child = student_list_lay_et.getChildAt(j);
                EditText uredt = (EditText) child.findViewById(R.id.relation);
                if (uredt != null) {
                    realtion_value = uredt.getText().toString();
                } else {
                    toast("Relation field blank.");
                }
            }
        } else {
            System.out.println("Student list empty current view is : " + j + j--);
        }
        return realtion_value;
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
    public void updateandset() {
        if (parent.getVisibility() == View.VISIBLE) {
            if (parent_et.getText().toString().trim().length() > 0) {
                if (RequiredStudentRelation_arr.get(current_content).contains("Y")) {
                    update_in_which();
                } else {
                    if (which_in.contains("Student")) {
                        if (final_student_list.size() > 0) {
                            for (int i = 0; i < final_student_list.size(); i++) {
                                LegDoc_Insert_FamilyDocDetails("", emr_name_et.getText().toString(),
                                        emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, i);
                                if (i + 1 == final_student_list.size()) {
                                    page_id_content++;
                                }
                            }
                        } else {
                            for (int i = 0; i < StudentID_arr.size(); i++) {
                                LegDoc_Insert_FamilyDocDetails("", emr_name_et.getText().toString(),
                                        emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, i);
                                if (i + 1 == StudentID_arr.size()) {
                                    page_id_content++;
                                }
                            }
                        }
                    } else {
                        LegDoc_Insert_FamilyDocDetails("", emr_name_et.getText().toString(),
                               emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, current_content);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            current_content++;
                            resetandupdate();
                        }
                    }, 2000);
                }
            } else {
                dismiss_it();
                toast("Parent name is required.");
                parent_et.setBackgroundResource(R.drawable.error_border);
            }
        } else {
            if (which_in.contains("Student")) {
                update_in_which();
            } else if (which_in.contains("Family")) {
                LegDoc_Insert_FamilyDocDetails("", emr_name_et.getText().toString(),
                        emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, current_content);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        current_content++;
                        resetandupdate();
                    }
                }, 2000);
            }
        }
    }
    @SuppressWarnings("unused")
    public void update_in_which() {
        if (RequiredStudentRelation_arr.get(current_content).contains("Y")) {
            for (int i = 0; i < StudentID_arr.size(); i++) {
                if (StudentID_arr.get(i) != null) {
                    LegDoc_Insert_FamilyDocDetails(get_relation_value(i), emr_name_et.getText().toString(),
                            emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, i);
                    studnt_count = i;
                    if (i + 1 == StudentID_arr.size()) {
                        page_id_content++;
                    }
                    if (i == (StudentID_arr.size() - 1)) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                current_content++;
                                resetandupdate();
                            }
                        }, 2000);
                    }
                }
            }
        } else {
            if (StudentID_arr.size() > 0) {
                for (int i = 0; i < StudentID_arr.size(); i++) {
                    if (StudentID_arr.get(i) != null) {
                        LegDoc_Insert_FamilyDocDetails(get_relation_value(i), emr_name_et.getText().toString(),
                                emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, i);
                        studnt_count = i;
                        if (i + 1 == StudentID_arr.size()) {
                            page_id_content++;
                        }
                        if (i == (StudentID_arr.size() - 1)) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    current_content++;
                                    resetandupdate();
                                }
                            }, 2000);
                        }
                    }
                }
            } else if (StudentID_arr_backup.size() > 0) {
                for (int i = 0; i < StudentID_arr_backup.size(); i++) {
                    if (StudentID_arr_backup.get(i) != null) {
                        if (StudentID_arr_backup.size() > 1) {
                            LegDoc_Insert_FamilyDocDetails("", emr_name_et.getText().toString(),
                                    emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, i);
                            studnt_count = i;
                        } else {
                            LegDoc_Insert_FamilyDocDetails("", emr_name_et.getText().toString(),
                                    emr_relation_et.getText().toString(), emr_number_et.getText().toString(), which_in, 0);
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                current_content++;
                                resetandupdate();
                            }
                        }, 2000);
                    }
                    break;
                }
            }
        }
    }
    public void resetandupdate() {
        if (current_content < size_of_Array) {
            //Family Doc 3 Phase
            set_method(current_content);
            scroll_it();
        } else {
            //Waiver Form Phase
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (Submit_Error == false) {
                        clear_Array();
                    }
                    Legal_DOC_chk_method();
                    scroll_it();
                }
            }, 2000);
        }
    }
    public static String formatPhoneNumber(String number) {
        if (number.trim().length() == 10) {
            number = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, number.length());
        } else {
            number = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10)
                    + "x" + number.substring(10, number.length());
        }
        return number;
    }
    public void clear_Array() {
        sign_et.setText("");
        emr_name_et.setText("");
        emr_number_et.setText("");
        emr_relation_et.setText("");

        sign_et.setBackgroundResource(R.drawable.input_box);
        title.setText("");
        agree_text.setText("");
        page_count.setVisibility(View.GONE);
        Check_response.setVisibility(View.GONE);
        html.setVisibility(View.GONE);
        html = (WebView) findViewById(R.id.html);

        desclaimer_check_lay.removeAllViews();
        desclaimer_check_lay = (LinearLayout) findViewById(R.id.desclaimer_check_lay);

        agreed_1.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        sign.setVisibility(View.GONE);
        parent.setVisibility(View.GONE);
        student_list_lay.setVisibility(View.GONE);
        student_list_lay.removeAllViews();
        student_list_lay = (TableLayout) findViewById(R.id.student_list_lay);
        student_list_lay_et.setVisibility(View.GONE);
        student_list_lay_et.removeAllViews();
        student_list_lay_et = (TableLayout) findViewById(R.id.student_list_lay_et);
        emr_lay.setVisibility(View.GONE);
        email_this.setVisibility(View.GONE);
        student_list_lay_title.setVisibility(View.GONE);

        current_content = 0;

        DocName_arr.clear();
        PageNo_arr.clear();
        TotalPages_arr.clear();
        content_arr.clear();
        RequiredDate_arr.clear();
        RequiredEmergency_arr.clear();
        RequiredSignature_arr.clear();
        RequiredStudent_arr.clear();
        RequiredStudentRelation_arr.clear();
        student_ID.clear();
        StudentID_Family_Arr.clear();
        PageID_arr.clear();
        ConfrmText_arr.clear();
        RequiredParent_arr.clear();
        DisclaimerID_arr.clear();
        DiscmrAfterBeforStatus_arr.clear();
        RequiredSignature_arr.clear();
        DocCount_arr.clear();
        DocName_arr.clear();
        DocGroupID_Arr.clear();
        Children_arr.clear();
        StudentList_Arr.clear();
        DisclaimerName_Arr.clear();
        DisclDetailsID_Arr.clear();
        DisclaimerContent_Arr.clear();

        desclaimer_bool = false;
    }
    public void set_method(int pos) {
        email_this.setChecked(true);
        emr_name_et.setText("");
        emr_number_et.setText("");
        emr_relation_et.setText("");
        dismiss_it();
        try {
            if (desclaimer_bool == false) {
                html = (WebView) findViewById(R.id.html);
                show_dialog();
                html.setVisibility(View.GONE);
                parent.setVisibility(View.GONE);
                parent_et.setBackgroundResource(R.drawable.input_box);
                submit.setVisibility(View.VISIBLE);
                Disc_Submit.setVisibility(View.GONE);
                agreed_1.setBackgroundResource(0);
                agreed_1.setChecked(false);
                title.setText(DocName_arr.get(current_content));
                page_lay.setVisibility(View.VISIBLE);
                page_count.setVisibility(View.VISIBLE);
                if (PageNo_arr.size() > 0) {
                    page_count.setText(String.valueOf(PageNo_arr.get(pos)) + " of " + String.valueOf(TotalPages_arr.get(0)));
                } else {
                    page_count.setVisibility(View.GONE);
                }
                String res = content_arr.get(current_content);
                html.getSettings().setJavaScriptEnabled(true);
                html.loadUrl("about:blank");
                html.loadData(res, "text/html", "UTF-8");
                html.setVisibility(View.VISIBLE);
                System.out.println("Visibility : " + html.getVisibility());
                agree_text.setText(ConfrmText_arr.get(pos));
                agreed_1.setVisibility(View.VISIBLE);

                if (RequiredParent_arr.get(pos).equals("Y")) {
                    parent.setVisibility(View.VISIBLE);
                    parent_et.setText(AppConfiguration.loginParentFirstname + " " + AppConfiguration.loginParentLastname);
                } else {
                    parent.setVisibility(View.GONE);
                }
                if (RequiredDate_arr.get(pos).equals("Y")) {
                    date.setVisibility(View.VISIBLE);
                    date_et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    date_et.setText(current_time);
                    date_et.setEnabled(false);
                } else {
                    date.setVisibility(View.GONE);
                }
                if (RequiredSignature_arr.get(pos).equals("Y")) {
                    Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "mayqueen.otf");
                    sign_et.setTypeface(tf);
                    sign_et.setText("");
                    sign.setVisibility(View.VISIBLE);
                } else {
                    sign.setVisibility(View.GONE);
                }
                if (RequiredStudent_arr.get(pos).contains("Y")) {
                    student_list_lay_title.setVisibility(View.GONE);
                    student_list_lay_et.setVisibility(View.GONE);
                    relation_title.setVisibility(View.GONE);
                    student_list_lay.setVisibility(View.GONE);
                    student_list_lay.removeAllViews();
                    student_list_lay = (TableLayout) findViewById(R.id.student_list_lay);
                    student_list_lay_et.removeAllViews();
                    student_list_lay_et = (TableLayout) findViewById(R.id.student_list_lay);
                    Student_list(Token, DocID_arr.get(0), SiteID_arr.get(0));
                } else {
                    student_list_lay_title.setVisibility(View.GONE);
                    student_list_lay_et.setVisibility(View.GONE);
                    relation_title.setVisibility(View.GONE);
                    student_list_lay.setVisibility(View.GONE);
                    student_list_lay.removeAllViews();
                    student_list_lay = (TableLayout) findViewById(R.id.student_list_lay);
                    student_list_lay_et.removeAllViews();
                    student_list_lay_et = (TableLayout) findViewById(R.id.student_list_lay);
                    System.out.println("Skipped");
                }
                if (current_content == (PageNo_arr.size() - 1)) {
                    email_this.setVisibility(View.VISIBLE);
                } else {
                    email_this.setVisibility(View.GONE);
                }
                if (RequiredEmergency_arr.get(pos).contains("Y")) {
                    emr_lay.setVisibility(View.VISIBLE);
                } else {
                    emr_lay.setVisibility(View.GONE);
                }
                scroll_it();
            } else {
                agreed_1.setVisibility(View.GONE);
                Disc_Submit.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                title.setText(DisclaimerName_Arr.get(current_content));
                Desclaimer_Lay_Set();
                scroll_it();
                if (DiscmrAfterBeforStatus_arr.get(current_content).contains("B")) {
                    submit.setVisibility(View.GONE);
                    Disc_Submit.setVisibility(View.VISIBLE);
                }
                page_count.setVisibility(View.GONE);
                page_lay.setVisibility(View.GONE);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            toast("Error Occured");
        }
        scroll_it();
    }
    public void scroll_it() {
        progress_splash.setVisibility(View.GONE);
        scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 600);
    }
    public void Desclaimer_Lay_Set() {
        desclaimer_check_lay.setVisibility(View.VISIBLE);
        for (int i = 0; i < DisclDetailsID_Arr.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_desclaimer_checkbox, null);
            desc_content = (CheckBox) view.findViewById(R.id.desclaimer_check);
            check_text = (TextView) view.findViewById(R.id.check_text);
            check_text.setText(DisclaimerContent_Arr.get(i));
            desclaimer_check_lay.addView(view);
            desclaimer_check_lay.setTag(i);
        }
    }
    public void Student_list_set() {
        if (RequiredStudentRelation_arr.get(current_content).contains("Y")) {
            student_list_lay_title.setVisibility(View.VISIBLE);
            student_list_lay_et.setVisibility(View.VISIBLE);
            relation_title.setVisibility(View.VISIBLE);

            for (int i = 0; i < Children_arr.size(); i++) {
             LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.custom_student_list_with_et, null);
                with_et = true;
                TextView sname = (TextView) view.findViewById(R.id.sname);
                relation = (EditText) view.findViewById(R.id.relation);
                sname.setText(Children_arr.get(i));
                student_list_lay_et.addView(view);
                student_list_lay_et.setTag(i);
            }
        } else {
            student_list_lay.setVisibility(View.VISIBLE);
            relation_title.setVisibility(View.GONE);
            student_list_lay_title.setVisibility(View.VISIBLE);
            for (int i = 0; i < Children_arr.size(); i++) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.custom_student_list, null);
                with_et = false;
                TextView sname = (TextView) view.findViewById(R.id.sname);
                sname.setText(Children_arr.get(i));
                student_list_lay.addView(view);
                student_list_lay.setTag(i);
            }
        }
    }
    public Boolean write(String fname, String fcontent) {
        try {
            String fpath = "/sdcard/" + fname + ".txt";
            File file = new File(fpath);
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();
            Log.d("Suceess", "Sucess");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
     //StrRelation - for student relation.This ll contains the relation with student.
    public void LegDoc_Insert_FamilyDocDetails(String StrRelation, String EmergencyName, String EmergencyRelation, String Phoneno
            , String Which_Insert, int student_count) {
        String responseString = "";
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", Token);
        if (Which_Insert.contains("Family")) {
            param.put("StudentID", StudentID_Family_Arr.get(current_content));
        } else if (Which_Insert.contains("Student")) {
            if (student_count < Children_arr.size()) {
                if (Children_arr.size() > 0) {
                    if (Children_arr.size() > 0) {
                        param.put("StudentID", student_ID.get(Children_arr.get(student_count)));
                    } else if (StudentID_arr_backup.size() > 0) {
                        param.put("StudentID", student_ID.get(StudentID_arr_backup.get(student_count)));
                    }
                } else if (StudentID_Family_Arr.size() > 0) {
                    if (final_student_list.size() > 0) {
                        param.put("StudentID", final_student_list.get(student_count));
                    }
                }
            } else {//When children is one and page count is more then one
                if (Children_arr.size() > 0) {
                    if (Children_arr.size() > 0) {
                        param.put("StudentID", student_ID.get(Children_arr.get(0)));
                    } else if (StudentID_arr_backup.size() > 0) {
                        param.put("StudentID", student_ID.get(StudentID_arr_backup.get(0)));
                    }
                } else if (StudentID_Family_Arr.size() > 0) {
                    if (final_student_list.size() > 0) {
                        param.put("StudentID", final_student_list.get(student_count));
                    }
                }
            }
        }
        param.put("ParentName", parent_et.getText().toString());
        param.put("Signaturename", sign_et.getText().toString());
        param.put("DocID", DocID_arr.get(current_content));
        try {
            if (which_in.contains("Student")) {
                param.put("PageID", PageID_arr.get(page_id_content));
            } else {
                param.put("PageID", PageID_arr.get(page_id_content));
                page_id_content++;
            }
            param.put("SiteID", SiteID_arr.get(current_content));
            param.put("DocumentName", DocName_arr.get(current_content));
            param.put("PageContent", content_arr.get(current_content));
            param.put("ConfrmText", ConfrmText_arr.get(current_content));
            param.put("StrRelation", "");
            param.put("strPDFCheck", "");

            if (emr_lay.getVisibility() == View.VISIBLE) {
                param.put("EmergencyName", emr_name_et.getText().toString());
                param.put("EmergencyRelation", emr_relation_et.getText().toString());
                param.put("Phoneno", emr_number_et.getText().toString());
            } else {
                param.put("EmergencyName", EmergencyName);
                param.put("EmergencyRelation", EmergencyRelation);
                param.put("Phoneno", Phoneno);
            }
            param.put("RequiredParent", RequiredParent_arr.get(current_content));
            param.put("RequiredStudent", RequiredStudent_arr.get(current_content));
            param.put("RequiredStudentRelation", RequiredStudentRelation_arr.get(current_content));
            param.put("RequiredSignature", RequiredSignature_arr.get(current_content));
            param.put("RequiredDate", RequiredDate_arr.get(current_content));
            param.put("RequiredEmergency", RequiredEmergency_arr.get(current_content));

            if (RequiredDate_arr.get(current_content).equals("Y")) {
                param.put("calstartdate", current_time);
            } else {
                param.put("calstartdate", "");
            }
            param.put("DocGroupID", DocGroupID_Arr.get(current_content));
            if (Which_Insert.contains("Family")) {
                responseString = WebServicesCall
                        .RunScript(DOMAIN + AppConfiguration.legal_doc_URL_insert, param);
//			responseString=WebServicesCall.RunScript(DOMAIN+AppConfiguration.LegDoc_Insert_StudentDocDetails_FirstPart,param);
            } else if (Which_Insert.contains("Student")) {
                responseString = WebServicesCall
                        .RunScript(DOMAIN + AppConfiguration.legal_doc_URL_insert_student, param);
//			responseString =WebServicesCall.RunScript(DOMAIN+AppConfiguration.LegDoc_Insert_StudentDocDetails_New_Full,param);
            }
            try {
                JSONObject obj = new JSONObject(responseString);
                String success = obj.getString("Success");
                if (success.contains("True")) {
                    Submit_Error = false;
                    if (DiscmrAfterBeforStatus_arr.get(0).equals("B") || DiscmrAfterBeforStatus_arr.get(0).equals("N")) {
                        if (Which_Insert.contains("Family")) {
                          Legal_Doc_Update(Token, DocID_arr.get(0), SiteID_arr.get(0), StudentID_Family_Arr);
                        } else if (Which_Insert.contains("Student")) {
                            if (StudentID_arr.size() > 0) {
                                Set<String> duplicate_remover = new HashSet<String>();
                                duplicate_remover.addAll(StudentID_arr);
                                StudentID_arr.clear();
                                StudentID_arr.addAll(duplicate_remover);

                                Legal_Doc_Update(Token, DocID_arr.get(0), SiteID_arr.get(0), StudentID_arr);
                            }
                        }
                    } else {  }
                } else {
                    Submit_Error = true;
                    Intent i = getIntent();
                    finish();
                    startActivity(i);
                    Toast.makeText(mContext, "Error occured", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    public String array_spliter(ArrayList<String> list1, ArrayList<String> list2) {
        String listString = "";
        ArrayList<String> combine = new ArrayList<String>();
        for (int i = 0; i < list1.size(); i++) {
            combine.add(list1.get(i) + "_" + list2.get(i));
        }
        for (String s : combine) {
            listString += s + "*";
        }
        return listString;
    }
    public void LegDoc__Insert_Disclaimer_Dtl() {
        Toast.makeText(mContext, "Processing...", Toast.LENGTH_SHORT).show();
        HashMap<String, String> param = new HashMap<String, String>();
        try {
            param.put("Token", Token);
            param.put("DocID", DocID_arr.get(current_content));
            param.put("SiteID", SiteID_arr.get(current_content));
            param.put("StudentListAry", StudentList_Arr.get(current_content));
            param.put("DisclaimerListAry", array_spliter(DisclDetailsID_Arr, DisclaimerContent_Arr));
            param.put("DiscmrAfterBeforStatus", DiscmrAfterBeforStatus_arr.get(current_content));
            param.put("DisclaimerID", DisclDetailsID_Arr.get(current_content));
            param.put("DiclaimerName", DisclaimerName_Arr.get(current_content));
            param.put("DocGroupID", DocGroupID_Arr.get(current_content));

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.legal_doc_URL_insert_disclaimer, param);
            try {
                JSONObject obj = new JSONObject(responseString);
                String success = obj.getString("Success");
                if (success.contains("True")) {
                    Submit_Error = false;
                    current_content++;
                } else {
                    Submit_Error = true;
                    Intent i = getIntent();
                    finish();
                    startActivity(i);
                    Toast.makeText(mContext, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (DiscmrAfterBeforStatus_arr.get(0).contains("A")) {
            Set<String> duplicate_remover = new HashSet<String>();
            duplicate_remover.addAll(StudentList_Arr);
            StudentList_Arr.clear();
            StudentList_Arr.addAll(duplicate_remover);
            Legal_Doc_Update(Token, DocID_arr.get(0), SiteID_arr.get(0), StudentList_Arr);
        } else {
            resetandupdate();
        }
    }
    public ArrayList<String> duplicate_remover(ArrayList<String> complete) {
        Set<String> duplicate_remover = new HashSet<String>();
        duplicate_remover.addAll(complete);
        complete.clear();
        complete.addAll(duplicate_remover);
        return complete;
    }
    public void Legal_DOC_chk_method() {
        HashMap<String, String> param = new HashMap<String, String>();
        String responseString = "";
        param.put("Token", Token);
        if (first_time == true) {
            responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.legal_doc_URL, param);
            first_time = false;
        } else {
            responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.legal_doc_URL_Load, param);
        }
        try {
            JSONObject obj = new JSONObject(responseString);
            String success = obj.getString("Success");

            if (success.equals("True")) {
                JSONArray familydoc_Arr = obj.getJSONArray("FamilyDoc");
                JSONArray Student_Doc_Arr = obj.getJSONArray("StudentDoc");
                JSONArray Disclaimer_arr = obj.getJSONArray("Disclaimer");

                if (familydoc_Arr.length() > 0) {
                    which_in = "Family";
                    DocID_arr.clear();
                    SiteID_arr.clear();
                    size_of_Array = familydoc_Arr.length();
                    for (int i = 0; i < familydoc_Arr.length(); i++) {
                        System.out.println("Array : " + i);
                        String content = familydoc_Arr.getString(i);
                        JSONObject contnt = new JSONObject(content);
                        fetch_data(contnt);
                    }
                } else if (Student_Doc_Arr.length() > 0) {
                    which_in = "Student";
                    DocID_arr.clear();
                    SiteID_arr.clear();
                    size_of_Array = Student_Doc_Arr.length();
                    for (int i = 0; i < Student_Doc_Arr.length(); i++) {
                        System.out.println("Array : " + i);
                        String content = Student_Doc_Arr.getString(i);
                        JSONObject contnt = new JSONObject(content);
                        fetch_data(contnt);
                    }
                } else if (Disclaimer_arr.length() > 0) {
                    size_of_Array = 1;
                    DisclaimerName_Arr = new ArrayList<String>();
                    DisclDetailsID_Arr = new ArrayList<String>();
                    DisclaimerContent_Arr = new ArrayList<String>();
                    StudentList_Arr = new ArrayList<String>();

                    PageNo_arr.add("0");

                    DocID_arr.clear();
                    SiteID_arr.clear();
                    for (int i = 0; i < Disclaimer_arr.length(); i++) {
                        System.out.println("Array : " + i);
                        desclaimer_bool = true;
                        String content = Disclaimer_arr.getString(i);
                        JSONObject contnt = new JSONObject(content);
                        fetch_data_desclaimer(contnt);
                    }
                }
                if (familydoc_Arr.length() > 0 || Student_Doc_Arr.length() > 0 || Disclaimer_arr.length() > 0) {
                    set_method(current_content);
                } else {
                    Student_list_check();
                }
            } else {
                if (which_in.contains("Family")) {
                    Set<String> duplicate_remover = new HashSet<String>();
                    duplicate_remover.addAll(StudentID_Family_Arr);
                    StudentID_Family_Arr.clear();
                    StudentID_Family_Arr.addAll(duplicate_remover);
                    Legal_Doc_Update(Token, DocID_arr.get(0), SiteID_arr.get(0), StudentID_Family_Arr);
                } else if (which_in.contains("Student")) {
                    Set<String> duplicate_remover = new HashSet<String>();
                    duplicate_remover.addAll(StudentID_arr);
                    StudentID_arr.clear();
                    StudentID_arr.addAll(duplicate_remover);
                    Legal_Doc_Update(Token, DocID_arr.get(0), SiteID_arr.get(0), StudentID_arr);
                }
                Student_list_check();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
        }
    }
    public void fetch_data_desclaimer(JSONObject contnt) {
        page_id_content = 0;
        try {
            String DisclaimerName = contnt.getString("DisclaimerName");
            String DisclDetailsID = contnt.getString("DisclDetailsID");
            String DisclaimerContent = contnt.getString("DisclaimerContent");
            String DocID = contnt.getString("DocID");
            String SiteID = contnt.getString("SiteID");
            String DocGroupID = contnt.getString("DocGroupID");
            String DiscmrAfterBeforStatus = contnt.getString("DiscmrAfterBeforStatus");
            String StudentList = contnt.getString("StudentList");

            DocGroupID_Arr.add(DocGroupID);
            DiscmrAfterBeforStatus_arr.add(DiscmrAfterBeforStatus);
            StudentList_Arr.add(StudentList);
            DisclaimerName_Arr.add(DisclaimerName);
            DisclDetailsID_Arr.add(DisclDetailsID);
            DisclaimerContent_Arr.add(DisclaimerContent);

            DocID_arr.add(DocID);
            SiteID_arr.add(SiteID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void fetch_data(JSONObject contnt) {
        page_id_content = 0;
        try {
            String DocName = contnt.getString("DocName");
            String PageID = contnt.getString("PageID");
            String Page_Content = contnt.getString("PageContent");
            String Page_Content_1 = Page_Content.replace("<body>", "<body style=\"text-align:justify;background-color:#99eaff\" >");
            String ConfrmText = contnt.getString("ConfrmText");
            String TotalPages = contnt.getString("TotalPages");
            String DocCount = contnt.getString("DocCount");
            String RequiredParent = contnt.getString("RequiredParent");
            String RequiredStudent = contnt.getString("RequiredStudent");
            String RequiredStudentRelation = contnt.getString("RequiredStudentRelation");
            String RequiredSignature = contnt.getString("RequiredSignature");
            String RequiredDate = contnt.getString("RequiredDate");
            String RequiredEmergency = contnt.getString("RequiredEmergency");
            String DisclaimerID = contnt.getString("DisclaimerID");
            String DiscmrAfterBeforStatus = contnt.getString("DiscmrAfterBeforStatus");
            String SiteID = contnt.getString("SiteID");
            String PageNo = contnt.getString("PageNo");
            String DocID = contnt.getString("DocID");
            String DocGroupID = contnt.getString("DocGroupID");
            String StudentID = contnt.getString("StudentID");

            content_arr.add(Page_Content_1);
            PageID_arr.add(PageID);
            PageNo_arr.add(PageNo);
            ConfrmText_arr.add(ConfrmText);
            TotalPages_arr.add(TotalPages);
            RequiredParent_arr.add(RequiredParent);
            RequiredStudent_arr.add(RequiredStudent);
            RequiredStudentRelation_arr.add(RequiredStudentRelation);
            RequiredDate_arr.add(RequiredDate);
            RequiredEmergency_arr.add(RequiredEmergency);
            DisclaimerID_arr.add(DisclaimerID);
            DiscmrAfterBeforStatus_arr.add(DiscmrAfterBeforStatus);
            RequiredSignature_arr.add(RequiredSignature);
            SiteID_arr.add(SiteID);
            DocCount_arr.add(DocCount);
            DocID_arr.add(DocID);
            DocName_arr.add(DocName);
            StudentID_Family_Arr.add(StudentID);
            DocGroupID_Arr.add(DocGroupID);

            final_student_list = Arrays.asList(StudentID.split(","));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void Student_list(String Token, String DocID, String SiteID) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", Token);
        param.put("DocID", DocID);
        param.put("SiteID", SiteID);
        String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.legal_doc_StudentList, param);
        try {
            JSONObject obj = new JSONObject(responseString);
            String success = obj.getString("Success");

            System.out.println("Success : " + success);
            StudentID_arr = new ArrayList<String>();
            Children_arr.clear();
            if (success.equals("True")) {
                JSONArray jarray = obj.getJSONArray("PendingStudentList");
                System.out.println("Student List : " + jarray);
                for (int i = 0; i < jarray.length(); i++) {
                    System.out.println("Array : " + i);
                    String content = jarray.getString(i);
                    JSONObject contnt = new JSONObject(content);
                    String StudentID = contnt.getString("StudentID");
                    String Children = contnt.getString("Children");

                    Children_arr.add(Children);
                    StudentID_arr_backup.add(Children);
                    StudentID_arr.add(StudentID);
                    student_ID.put(Children, StudentID);
                }
                Student_list_set();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Check student list here to decide what ll be the next screen
     * if student list is empty then send user to add new users
     * or send it to Scheduling view.
     *@return
     */
    public boolean Student_list_check() {
        boolean student_available = false;

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", Token);
        String responseString = WebServicesCall.RunScript(AppConfiguration.getStudentListURL, param);
        try {
            JSONObject obj = new JSONObject(responseString);
            String success = obj.getString("Success");

            if (success.equals("True")) {
                JSONArray student_arr = obj.getJSONArray("StudentList");
                if (student_arr.length() > 0) {
                    Intent i = new Intent(mContext, DashBoardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("DOMAIN", DOMAIN);
                    i.putExtra("POS", 0);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Intent i = new Intent(mContext, RegisterChildScreen1Activity.class);
                    i.putExtra("DOMAIN", DOMAIN);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            } else {
                student_available = false;
                Intent i = new Intent(mContext, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DOMAIN", DOMAIN);
                i.putExtra("POS", 0);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
        }
        return student_available;
    }
    public void Legal_Doc_Update(String Token, String DocID, String SiteID, ArrayList<String> Student_ID) {

        for (int i = 0; i < Student_ID.size(); i++) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", Token);
            param.put("DocID", DocID);
            param.put("SiteID", SiteID);
            param.put("StudentID", Student_ID.get(i));

            String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.legal_doc_URL_update, param);
            try {
                JSONObject obj = new JSONObject(responseString);
                String success = obj.getString("Success");
                System.out.println("Success : " + success);
                if (success.equals("True")) {
                    Submit_Error = false;
                    System.out.println("Doc Updated Successfully");
                    if (DiscmrAfterBeforStatus_arr.size() > 0) {
                        if (DiscmrAfterBeforStatus_arr.get(0).contains("A")) {
                            if (i + 1 == Student_ID.size()) {
                                resetandupdate();
                            }
                        }
                    } else {
                        System.out.println("After before status array null");
                        Student_list_check();
                    }
                } else {
                    Submit_Error = true;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public String setcurrent_time() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        String formattedDate = df.format(c.getTime());
        System.out.println("Current time is : " + formattedDate);
        return formattedDate;
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        logout();
    }
    public void dismiss_it() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (progress_splash != null && progress_splash.isShown()) {
            progress_splash.setVisibility(View.GONE);
        }
    }
    @SuppressWarnings("deprecation")
    public void logout() {
        AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(mContext);
        alertdialogbuilder2.setTitle("WaterWorks");
        alertdialogbuilder2.setIcon(getResources().getDrawable(R.drawable.alerticon));
        try {
            alertdialogbuilder2
                    .setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }

                            })
                    .setNegativeButton("Logout",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.cancel();
                                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    loginIntent.putExtra("DOMAIN", SplashScreen.DOMAIN);
                                    startActivity(loginIntent);
                                    finish();
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    AppConfiguration.BasketID = "BasketID";
                                    AppConfiguration.basket_siteid = "Siteid";
                                }
                            });
            AlertDialog alertDialog2 = alertdialogbuilder2.create();
            alertDialog2.show();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    class progress extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            show_dialog();
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //			dialog.dismiss();
        }
    }
    public void show_dialog() {
        progress_splash = (ProgressWheel) findViewById(R.id.progressWheel1);
        progress_splash.setVisibility(View.VISIBLE);
        progress_splash.setProgress(0.0f);
        progress_splash.setBarColor(Color.BLUE);
        progress_splash.setCallback(new ProgressWheel.ProgressCallback() {
            @Override
            public void onProgressUpdate(float progress) {
                if (progress == 0) {
                    progress_splash.setProgress(1.0f);
                } else if (progress == 1.0f) {
                    progress_splash.setProgress(0.0f);
                }
            }
        });
    }
}
