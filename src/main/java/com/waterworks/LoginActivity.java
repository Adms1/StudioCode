package com.waterworks;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.waterworks.manageProfile.AddStudent;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.ProgressWheel;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;
public class LoginActivity extends Activity {
    String DOMAIN = "", token = "", suceessLAFitenss;
    EditText edtUserEmail, edtPassword;
    CardView btnLogin;
    String userEmail, password;
    CheckBox chkrem;
    TextView txtForgtPass, txtNewAccount, tv_versionno_name_new;
    String userEmailForgot;
    SharedPreferences SP;
    public static String filename = "Valustoringfile";
    String message;
    String messageLogin;
    String successLogin = "False";
    Boolean isInternetPresent = false;
    ProgressDialog pd;
    RelativeLayout autoLogin;
    ScrollView loginScreen;
    Context mContext = this;
    String hi, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Crittercism.initialize(getApplicationContext(), "536f2c490729df392d000002");
        ConstantData.clicked_view = 0;

        edtUserEmail = (EditText) findViewById(R.id.edtUserEmail);
        edtPassword = (EditText) findViewById(R.id.edtPasword);
        btnLogin = (CardView) findViewById(R.id.btnLogin);
        chkrem = (CheckBox) findViewById(R.id.chk);
        txtForgtPass = (TextView) findViewById(R.id.txtForgtPass);
        txtNewAccount = (TextView) findViewById(R.id.txtNewAccount);
        tv_versionno_name_new = (TextView) findViewById(R.id.tv_versionno_name_new);
        autoLogin = (RelativeLayout) findViewById(R.id.autoLogin);
        loginScreen = (ScrollView) findViewById(R.id.loginScreen);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_versionno_name_new.setText("Version: " + pInfo.versionName.toString().trim());

        SP = getSharedPreferences(filename, 0);
        String getname = SP.getString("username", "");
        String getpass = SP.getString("password", "");
        String getLoginStatus = SP.getString("LoginStatus", "");
        boolean rem_check = SP.getBoolean("AUTO_ISCHECK", false);

        try {
            if (DOMAIN.isEmpty()) {
                DOMAIN = AppConfiguration.DOMAIN;
                Log.i("Login", "Url = " + DOMAIN);
                SetUp();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (getname.trim().length() > 0 && getpass.trim().length() > 0) {
            autoLogin.setVisibility(View.VISIBLE);
            loginScreen.setVisibility(View.GONE);
            AutoLogin(getname, getpass);
        } else {
            autoLogin.setVisibility(View.GONE);
            loginScreen.setVisibility(View.VISIBLE);
        }
        edtUserEmail.setText(getname);
        edtPassword.setText(getpass);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (rem_check == true)
            chkrem.setChecked(true);
        else
            chkrem.setChecked(false);

        chkrem.setChecked(true);

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                autologin = false;
                userEmail = edtUserEmail.getText().toString();
                password = edtPassword.getText().toString();
                AppConfiguration.LoginPass = password;
                if (userEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                } else {
                    if (!AppConfiguration.isValidEmail(userEmail)) {
                        Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    } else {
                        isInternetPresent = Utility.isNetworkConnected(LoginActivity.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            Editor editor = SP.edit();
                            if (chkrem.isChecked()) {
                                editor.putString("username", userEmail);
                                editor.putString("password", password);
                                editor.putBoolean("AUTO_ISCHECK", true).commit();
                            } else {
                                editor.putString("username", "");
                                editor.putString("password", "");
                                editor.putBoolean("AUTO_ISCHECK", false).commit();
                            }
                            editor.putString("username", userEmail);
                            editor.commit();

                            new LoginAsyncTask().execute();
                            new IsLAFitness_Check_LoginAsyncTask().execute();
                        }
                    }
                }
            }
        });
        txtNewAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity1.class);
                i.putExtra("DOMAIN", DOMAIN);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        txtForgtPass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ForgetPasswordDialog();
            }
        });
    }
    public void AutoLogin(String user, String Pass) {
        userEmail = user;
        password = Pass;
        autologin = true;
        if (userEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(),R.string.empty_fields, Toast.LENGTH_LONG).show();
        } else {
            if (!AppConfiguration.isValidEmail(userEmail)) {
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            } else {
                isInternetPresent = Utility.isNetworkConnected(LoginActivity.this);
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    Editor editor = SP.edit();
                    editor.putString("username", userEmail);
                    editor.putString("password", password);
                    editor.putBoolean("AUTO_ISCHECK", true).commit();
                    editor.commit();

                    new LoginAsyncTask().execute();
                    new IsLAFitness_Check_LoginAsyncTask().execute();
                }
            }
        }
    }
    private void ForgetPasswordDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_forgetpwd);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        ImageButton ib_close = (ImageButton) dialog.findViewById(R.id.ib_dialog_close);
        CardView btn_reset = (CardView) dialog.findViewById(R.id.btn_dialog_reset_pwd);
        final EditText et_emailaddress = (EditText) dialog.findViewById(R.id.edt_dialog_UserEmail);
        ib_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        btn_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfiguration.isConnectingToInternet(LoginActivity.this)) {
                    if (et_emailaddress.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter e-mail address", Toast.LENGTH_LONG).show();
                    } else {
                        userEmailForgot = et_emailaddress.getText().toString();
                        if (!AppConfiguration.isValidEmail(userEmailForgot)) {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid Email", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.cancel();
                            isInternetPresent = Utility.isNetworkConnected(LoginActivity.this);
                            if (!isInternetPresent) {
                                onDetectNetworkState().show();
                            } else {
                                new ForgotPasswordAsyncTask().execute();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public String authentication() {
        //Post Data
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", userEmail);
        params.put("password", password);

        AppConfiguration.currentPass = password;
        String responseString = WebServicesCall.RunScript(AppConfiguration.LoginURL, params);
        readAndParseJSON(responseString);

        return responseString;
    }
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successLogin = reader.getString("Success");
            if (successLogin.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("LoginDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String familyID = jsonChildNode.getString("FamilyID");
                    token = jsonChildNode.getString("Token");
                    AppConfiguration.loginParentFirstname = jsonChildNode.getString("WU_FParent1_FirstName");
                    AppConfiguration.loginParentEmail = jsonChildNode.getString("WU_FPrimaryEmail");
                    AppConfiguration.loginParentLastname = jsonChildNode.getString("WU_FParent1_LastName");
                    AppConfiguration.loginParentPhone1 = jsonChildNode.getString("WU_Phoneno");
                    AppConfiguration.logCount = jsonChildNode.getString("LogCount");

                    SharedPreferences prefs = AppConfiguration.getSharedPrefs(LoginActivity.this);
                    Editor editor = prefs.edit();
                    editor.putString("Token", token);
                    editor.putString("FamilyID", familyID);
                    editor.commit();
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("LoginDtl");
                if (pd != null) {
                    pd.dismiss();
                }
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageLogin = jsonChildNode.getString("Msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    boolean autologin = false;
    class LoginAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (autologin == false) {
                pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage(getResources().getString(R.string.pleasewait));
                pd.setCancelable(false);
                pd.show();
            }
        }
        @Override
        protected String doInBackground(Void... params) {
            String result = authentication();
            return result;
        }
        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (!result.equalsIgnoreCase("")) {
                if (successLogin.toString().equals("True")) {
                    if (Student_list_check() == true) {
                        if (Legal_DOC_chk_method() == true) {
                            Intent i = new Intent(LoginActivity.this, LegalDoc_Check_FamilyDoc.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("DOMAIN", DOMAIN);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            Log.d("SLiding", "DOC");
                        } else {
                            SharedPreferences sp1 = getSharedPreferences("key", 0);
                            String tValue = sp1.getString(userEmail, ""); //loginstatus

                            if (tValue.equalsIgnoreCase("yes")) {
                                Intent i = new Intent(LoginActivity.this, DashBoardActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("DOMAIN", DOMAIN);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Log.d("SLiding", "Home");
                            } else {
                                Intent i = new Intent(LoginActivity.this, Login_welcome_splashscreen.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("DOMAIN", DOMAIN);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                           }
                        }
                    } else {
                        Intent i = new Intent(LoginActivity.this, AddStudent.class);//RegisterChildScreen1Activity
                        i.putExtra("DOMAIN", DOMAIN);
                        i.putExtra("fromWhere", "register");
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                } else {
                    loginScreen.setVisibility(View.VISIBLE);
                    autoLogin.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "" + messageLogin, Toast.LENGTH_LONG).show();
                }
            } else {
                loginScreen.setVisibility(View.VISIBLE);
                autoLogin.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Server down.!! Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void forgotpasswordSubmitting() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Email", userEmailForgot);

        String responseString = WebServicesCall.RunScript(AppConfiguration.forgot_passwordURL, params);
        readAndParseJSONforgot(responseString);
    }
    public void readAndParseJSONforgot(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if (success.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("ForgotPass");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("ForgotPass");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    class ForgotPasswordAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            forgotpasswordSubmitting();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void SetUp() {
        AppConfiguration.LoginURL = DOMAIN + AppConfiguration.LoginURL;
        AppConfiguration.phoneTypesURL = DOMAIN + AppConfiguration.phoneTypesURL;
        AppConfiguration.getSiteListURL = DOMAIN + AppConfiguration.getSiteListURL;
        AppConfiguration.Get_DivesAndTurnsSiteList = DOMAIN + AppConfiguration.Get_DivesAndTurnsSiteList;
        AppConfiguration.Get_WaterpoloSiteList = DOMAIN + AppConfiguration.Get_WaterpoloSiteList;
        AppConfiguration.getSiteWiseSwimLessonURL = DOMAIN + AppConfiguration.getSiteWiseSwimLessonURL;
        AppConfiguration.getDataSpinner1URL = DOMAIN + AppConfiguration.getDataSpinner1URL;
        AppConfiguration.getDataChildLevel1URL = DOMAIN + AppConfiguration.getDataChildLevel1URL;
        AppConfiguration.getDataChildLevel2URL = DOMAIN + AppConfiguration.getDataChildLevel2URL;
        AppConfiguration.registration = DOMAIN + AppConfiguration.registration;
        AppConfiguration.forgot_passwordURL = DOMAIN + AppConfiguration.forgot_passwordURL;
        AppConfiguration.getMyAccountURL = DOMAIN + AppConfiguration.getMyAccountURL;
        AppConfiguration.updateMyAccountURL = DOMAIN + AppConfiguration.updateMyAccountURL;
        AppConfiguration.changePasswordURL = DOMAIN + AppConfiguration.changePasswordURL;
        AppConfiguration.viewschedule = DOMAIN + AppConfiguration.viewschedule;
        AppConfiguration.viewpastschedule = DOMAIN + AppConfiguration.viewpastschedule;
        AppConfiguration.requestShadowing = DOMAIN + AppConfiguration.requestShadowing;
        AppConfiguration.submitrequestshadowing = DOMAIN + AppConfiguration.submitrequestshadowing;
        AppConfiguration.cancelLessonLoad = DOMAIN + AppConfiguration.cancelLessonLoad;
        AppConfiguration.getEmailPreferences = DOMAIN + AppConfiguration.getEmailPreferences;
        AppConfiguration.saveEmailPreferences = DOMAIN + AppConfiguration.saveEmailPreferences;
        AppConfiguration.cancelClass = DOMAIN + AppConfiguration.cancelClass;
        AppConfiguration.validateZipCodeURL = DOMAIN + AppConfiguration.validateZipCodeURL;
        AppConfiguration.getInstructorPreferences = DOMAIN + AppConfiguration.getInstructorPreferences;
        AppConfiguration.MyAcnt_AgeCalculation = DOMAIN + AppConfiguration.MyAcnt_AgeCalculation;
        AppConfiguration.isEmailExistSURL = DOMAIN + AppConfiguration.isEmailExistSURL;
        AppConfiguration.registerChild = DOMAIN + AppConfiguration.registerChild;
        AppConfiguration.updateChildURL = DOMAIN + AppConfiguration.updateChildURL;
        AppConfiguration.getStudentListURL = DOMAIN + AppConfiguration.getStudentListURL;
        AppConfiguration.getChildCountURL = DOMAIN + AppConfiguration.getChildCountURL;
        AppConfiguration.viewPaymentHistory = DOMAIN + AppConfiguration.viewPaymentHistory;
        AppConfiguration.viewCertificateURL = DOMAIN + AppConfiguration.viewCertificateURL;
        AppConfiguration.progressReportURL = DOMAIN + AppConfiguration.progressReportURL;
        AppConfiguration.progressReportDetailsURL = DOMAIN + AppConfiguration.progressReportDetailsURL;
        AppConfiguration.ribbenCountURL = DOMAIN + AppConfiguration.ribbenCountURL;
        AppConfiguration.ribbenCountDetailsURL = DOMAIN + AppConfiguration.ribbenCountDetailsURL;
        AppConfiguration.sitebyFid = DOMAIN + AppConfiguration.sitebyFid;
        AppConfiguration.productbyFamily = DOMAIN + AppConfiguration.productbyFamily;
        AppConfiguration.swimpackage = DOMAIN + AppConfiguration.swimpackage;
        AppConfiguration.swimsubmit = DOMAIN + AppConfiguration.swimsubmit;
        AppConfiguration.Get_BasketID = DOMAIN + AppConfiguration.Get_BasketID;
        AppConfiguration.viewpricesheet = DOMAIN + AppConfiguration.viewpricesheet;
        AppConfiguration.viewcart__pasduebal_basketid = DOMAIN + AppConfiguration.viewcart__pasduebal_basketid;
        AppConfiguration.deleteinvoice = DOMAIN + AppConfiguration.deleteinvoice;
        AppConfiguration.applypromocode = DOMAIN + AppConfiguration.applypromocode;
        AppConfiguration.emptybasket_byid = DOMAIN + AppConfiguration.emptybasket_byid;
        AppConfiguration.pastduebalload = DOMAIN + AppConfiguration.pastduebalload;
        AppConfiguration.payinvoice = DOMAIN + AppConfiguration.payinvoice;
        AppConfiguration.transfermakeupload = DOMAIN + AppConfiguration.transfermakeupload;
        AppConfiguration.transfermakeup_lesson_transfer = DOMAIN + AppConfiguration.transfermakeup_lesson_transfer;
        AppConfiguration.transfermakeup_site_transfer = DOMAIN + AppConfiguration.transfermakeup_site_transfer;
        AppConfiguration.transfermakeup_privatelesson_transfer = DOMAIN + AppConfiguration.transfermakeup_privatelesson_transfer;
        AppConfiguration.insertinvoice = DOMAIN + AppConfiguration.insertinvoice;
        AppConfiguration.lapswimbutton1submit = DOMAIN + AppConfiguration.lapswimbutton1submit;
        AppConfiguration.lapswimbutton2submit = DOMAIN + AppConfiguration.lapswimbutton2submit;
        AppConfiguration.lapswimbutton3submit = DOMAIN + AppConfiguration.lapswimbutton3submit;
        AppConfiguration.WaterArobics = DOMAIN + AppConfiguration.WaterArobics;
        AppConfiguration.fsn_getdate = DOMAIN + AppConfiguration.fsn_getdate;
        AppConfiguration.fsn_getcost = DOMAIN + AppConfiguration.fsn_getcost;
        AppConfiguration.fsn_submit = DOMAIN + AppConfiguration.fsn_submit;
        AppConfiguration.swimCampRegister1 = DOMAIN + AppConfiguration.swimCampRegister1;
        AppConfiguration.swimCampRegister2 = DOMAIN + AppConfiguration.swimCampRegister2;
        AppConfiguration.swimCampRegister3 = DOMAIN + AppConfiguration.swimCampRegister3;
        AppConfiguration.swimCampRegister1_new = DOMAIN + AppConfiguration.swimCampRegister1_new;
        AppConfiguration.swimCampRegister3New = DOMAIN + AppConfiguration.swimCampRegister3New;
        AppConfiguration.makePurchaseAddToCart = DOMAIN + AppConfiguration.makePurchaseAddToCart;
        AppConfiguration.swimteamDescription = DOMAIN + AppConfiguration.swimteamDescription;
        AppConfiguration.swimteamGroup = DOMAIN + AppConfiguration.swimteamGroup;
        AppConfiguration.swimteamChild = DOMAIN + AppConfiguration.swimteamChild;
        AppConfiguration.add_swimteam1 = DOMAIN + AppConfiguration.add_swimteam1;
        AppConfiguration.feedbackURL = DOMAIN + AppConfiguration.feedbackURL;
        AppConfiguration.contactUsURL = DOMAIN + AppConfiguration.contactUsURL;
        AppConfiguration.suggestURL = DOMAIN + AppConfiguration.suggestURL;
        AppConfiguration.problemURL = DOMAIN + AppConfiguration.problemURL;
        AppConfiguration.HelpURLNEW = DOMAIN + AppConfiguration.HelpURLNEW;
        AppConfiguration.viewholds = DOMAIN + AppConfiguration.viewholds;
        AppConfiguration.viewwaitlistpageload = DOMAIN + AppConfiguration.viewwaitlistpageload;
        AppConfiguration.viewwaitlistsubmit = DOMAIN + AppConfiguration.viewwaitlistsubmit;
        AppConfiguration.removelesson = DOMAIN + AppConfiguration.removelesson;
        AppConfiguration.removelessonsubmit = DOMAIN + AppConfiguration.removelessonsubmit;
        AppConfiguration.GetChildDT = DOMAIN + AppConfiguration.GetChildDT;
        AppConfiguration.Savebasket = DOMAIN + AppConfiguration.Savebasket;
        AppConfiguration.DTPage2Load = DOMAIN + AppConfiguration.DTPage2Load;
        AppConfiguration.releaseClassURL = DOMAIN + AppConfiguration.releaseClassURL;
        AppConfiguration.checkinpageload = DOMAIN + AppConfiguration.checkinpageload;
        AppConfiguration.checkinproceed = DOMAIN + AppConfiguration.checkinproceed;
        AppConfiguration.checkin2title = DOMAIN + AppConfiguration.checkin2title;
        AppConfiguration.checkin2data = DOMAIN + AppConfiguration.checkin2data;
        AppConfiguration.swimCompititionMeetDatesURL = DOMAIN + AppConfiguration.swimCompititionMeetDatesURL;
        AppConfiguration.swimCompititionMeetDatesLocationURL = DOMAIN + AppConfiguration.swimCompititionMeetDatesLocationURL;
        AppConfiguration.swimCompititionMeetDateCheckURL = DOMAIN + AppConfiguration.swimCompititionMeetDateCheckURL;
        AppConfiguration.swimCompititionStep2URL = DOMAIN + AppConfiguration.swimCompititionStep2URL;
        AppConfiguration.swimCompititionStep3URL = DOMAIN + AppConfiguration.swimCompititionStep3URL;
        AppConfiguration.swimCompititionStep4URL = DOMAIN + AppConfiguration.swimCompititionStep4URL;
        AppConfiguration.swimCompititionStep5URL = DOMAIN + AppConfiguration.swimCompititionStep5URL;
        AppConfiguration.swimCompititionConfirmationCalculation = DOMAIN + AppConfiguration.swimCompititionConfirmationCalculation;
        AppConfiguration.swimCompititionConfirmationReminderText = DOMAIN + AppConfiguration.swimCompititionConfirmationReminderText;
        AppConfiguration.swimCompititionAddToCart = DOMAIN + AppConfiguration.swimCompititionAddToCart;
        AppConfiguration.scheduleALesssionSiteListURL = DOMAIN + AppConfiguration.scheduleALesssionSiteListURL;
        AppConfiguration.scheduleALessionStudentListURL = DOMAIN + AppConfiguration.scheduleALessionStudentListURL;
        AppConfiguration.scheduleALessionLessonTypesURL = DOMAIN + AppConfiguration.scheduleALessionLessonTypesURL;
        AppConfiguration.scheduleALessionInstructorListURL = DOMAIN + AppConfiguration.scheduleALessionInstructorListURL;
        AppConfiguration.scheduleALessionPageLoad = DOMAIN + AppConfiguration.scheduleALessionPageLoad;
        AppConfiguration.scheduleALessionStep2InstructorAvailabilityURL = DOMAIN + AppConfiguration.scheduleALessionStep2InstructorAvailabilityURL;
        AppConfiguration.scheduleALessionStep3 = DOMAIN + AppConfiguration.scheduleALessionStep3;
        AppConfiguration.LessonsCounts = DOMAIN + AppConfiguration.LessonsCounts;
        AppConfiguration.DetailsOfSwimCamp = DOMAIN + AppConfiguration.DetailsOfSwimCamp;
        AppConfiguration.transfermakeupcounts = DOMAIN + AppConfiguration.transfermakeupcounts;
        AppConfiguration.Schl_Get_LessonListByStudent = DOMAIN + AppConfiguration.Schl_Get_LessonListByStudent;
        AppConfiguration.Schl_Get_ActiveSiteStartEndDate = DOMAIN + AppConfiguration.Schl_Get_ActiveSiteStartEndDate;
        AppConfiguration.Schl_Get_Step3_AddSchedule = DOMAIN + "Schl_Get_Step3_AddSchedule";
        AppConfiguration.Schl_Get_Confirm_classesList = DOMAIN + "Schl_Get_Confirm_classesList";
        AppConfiguration.Schl_Add_Confirm_SchedulesLesson = DOMAIN + "Schl_Add_Confirm_SchedulesLesson";
        AppConfiguration.getFamilySwimSiteListURL = DOMAIN + "Get_FamilySwimSiteList";
        AppConfiguration.GetStudentLevelProgressURL = DOMAIN + AppConfiguration.GetStudentLevelProgressURL;
        AppConfiguration.GetStudentLevelprereqURL = DOMAIN + AppConfiguration.GetStudentLevelprereqURL;
        AppConfiguration.IsLAFitness_Check_Login = DOMAIN +"IsLAFitness_Check_Login";
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    public boolean Legal_DOC_chk_method() {
        boolean success_bul = false;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
        params.put("password", password);

        String responseString = WebServicesCall.RunScript(DOMAIN + AppConfiguration.legal_doc_URL, params);
        JSONObject obj;
        String success = "";

        try {
            obj = new JSONObject(responseString);
            success = obj.getString("Success");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (success.equals("True")) {
            success_bul = true;
        } else {
            success_bul = false;
        }
        return success_bul;
    }
    public boolean Student_list_check() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getStudentListURL, params);
        boolean student_available = false;

        try {
            JSONObject obj = new JSONObject(responseString);
            String success = obj.getString("Success");
            if (success.equals("True")) {
                JSONArray student_arr = obj.getJSONArray("StudentList");
                if (student_arr.length() > 0) {
                    student_available = true;
                } else {
                    student_available = false;
                }
            } else {
                student_available = false;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
        }
        return student_available;
    }
    //    new code 01-03-2017 megha
    public class IsLAFitness_Check_LoginAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd != null) {
                pd.dismiss();
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            load_LAFitness();
            return null;
        }
    }
    public void load_LAFitness() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        String responseString = WebServicesCall.RunScript(AppConfiguration.IsLAFitness_Check_Login, param);
        readAndParseJSONLAFitness(responseString);
    }
    public void readAndParseJSONLAFitness(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            suceessLAFitenss = reader.getString("Success");
            if (suceessLAFitenss.toString().equals("True")) {
                AppConfiguration.LAFitnessResult = reader.getString("IsLaFitness");
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



