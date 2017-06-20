/**
 *
 */
package com.waterworks.manageProfile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.CancelLessonFragment;
import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.EmailPreferencesActivity;
import com.waterworks.HelpAndSupportFragment;
import com.waterworks.LoginActivity;
import com.waterworks.ProgressReportActivity;
import com.waterworks.R;
import com.waterworks.RegisterChildScreen1Activity;
import com.waterworks.SplashScreen;
import com.waterworks.SwimCompititionRegisterAcitivity;
import com.waterworks.SwimCompititionUpcomingEventsAcitivity;
import com.waterworks.ViewPaymentHistoryListActivity;
import com.waterworks.ViewPaymentHistoryListNewActivity;
import com.waterworks.adapter.menuoptionItemAdapter;
import com.waterworks.d2_ChangePassword;
import com.waterworks.model.menuoptionItem;
import com.waterworks.sheduling.BuyMoreSwimLession;
import com.waterworks.sheduling.BuyMoreThankYou;
import com.waterworks.sheduling.ByMoreOtherPrograms;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Harsh Adms
 */
public class d2_MyProfile extends Activity {

	/* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

    RelativeLayout changeEmail, changePassword, manageProfile, manageStudents, addStudents, payHistory, email_pref, rl_changeAddLoc, relProgressReport, rlAddPaymentAccount, rlManagePaymentAccount;
    TextView currentEmail, tv_location, tv_location_count;
    public static String ChangedEmail = "";
    Context mContext = this;
    Button relMenu, btnHome;
    String token, familyID;
    int locatonCount;
    ArrayList<String> siteName = new ArrayList<>();
    //SlideMenu
    static DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    static RelativeLayout leftRl;
    private ArrayList<menuoptionItem> navDrawerItems_main;
    private menuoptionItemAdapter adapter_menu_item;
    String MenuName[];
    int dispPOS = 0;
    public static String filename = "Valustoringfile";
    SharedPreferences SP;
    TextView student_count, manageStudents_txt;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_change_email);

        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        init();

        SP = getApplicationContext().getSharedPreferences(filename, 0);
        //SlideMenu
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            //			private_lessons boolean ok = false;

            @SuppressLint("NewApi")
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            @SuppressLint("NewApi")
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    //SlideMenu

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    Fragment fragment = null;
    int myid;

    private void displayView(int position) {

        switch (position) {
            case 0:
                Intent iDash = new Intent(d2_MyProfile.this, DashBoardActivity.class);
                iDash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iDash);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 1:
                Intent acc = new Intent(mContext, d2_MyProfile.class);
                startActivity(acc);
                break;
            case 2:
                Intent i = new Intent(mContext, CancelLessonFragment.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 3:
                Intent intentBuyMoreLessons = new Intent(this, BuyMoreSwimLession.class);
                startActivity(intentBuyMoreLessons);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 4:
                Intent can = new Intent(mContext, ScheduleLessonFragement.class);
                startActivity(can);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 5:
                AppConfiguration.CheckMeet = Utility.getCheckMeet(token);
                Intent intentSwimCompetion = null;
                if (AppConfiguration.CheckMeet.equalsIgnoreCase("0")) {
                    intentSwimCompetion = new Intent(mContext, SwimCompititionRegisterAcitivity.class);

                } else if (AppConfiguration.CheckMeet.equalsIgnoreCase("1")) {
                    intentSwimCompetion = new Intent(mContext, SwimCompititionUpcomingEventsAcitivity.class);

                } else if (AppConfiguration.CheckMeet.equalsIgnoreCase("2")) {
                    intentSwimCompetion = new Intent(mContext, SwimCompititionRegisterAcitivity.class);

                }
                startActivity(intentSwimCompetion);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 6:
                Intent can1 = new Intent(mContext, CancelLessonFragment.class);
                startActivity(can1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 7:
                Intent iByMoreOtherPrograms = new Intent(mContext, ByMoreOtherPrograms.class);
                startActivity(iByMoreOtherPrograms);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 8:
                Intent helpSupIntent = new Intent(mContext, HelpAndSupportFragment.class);
                startActivity(helpSupIntent);
                break;
            case 9:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                builder.setTitle("WaterWorks");
                builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
                builder.setMessage("Are you sure you want to Logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        SharedPreferences.Editor editor = SP.edit();
                        editor.putString("username", "");
                        editor.putString("password", "");
                        editor.putBoolean("AUTO_ISCHECK", false).commit();
                        editor.commit();
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        loginIntent.putExtra("DOMAIN", SplashScreen.DOMAIN);
                        startActivity(loginIntent);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
                        AppConfiguration.BasketID = "0";
                        AppConfiguration.basket_siteid = "Siteid";
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mDrawerLayout.closeDrawers();
                    }
                });
                builder.show();
                break;
            case 10:
                AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(d2_MyProfile.this);
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
                                            mDrawerLayout.closeDrawers();
                                        }

                                    })
                            .setNegativeButton("Logout",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                            dialog.cancel();

                                            SharedPreferences.Editor editor = SP.edit();

                                            editor.putString("username", "");
                                            editor.putString("password", "");
                                            editor.putBoolean("AUTO_ISCHECK", false).commit();
                                            editor.commit();
                                            Intent loginIntent = new Intent(
                                                    getApplicationContext(),
                                                    LoginActivity.class);
                                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            loginIntent.putExtra("DOMAIN", SplashScreen.DOMAIN);
                                            startActivity(loginIntent);
                                            finish();
                                            android.os.Process.killProcess(android.os.Process.myPid());
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                            AppConfiguration.BasketID = "0";
                                            AppConfiguration.basket_siteid = "Siteid";
                                        }
                                    });

                    AlertDialog alertDialog2 = alertdialogbuilder2.create();
                    alertDialog2.show();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                break;
        }


        if (fragment != null) {

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //			setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
            Log.e("Dashboard", "Error in creating fragment");
        }
    }

    private void init() {
        // TODO Auto-generated method stub

        student_count = (TextView) findViewById(R.id.student_count);
        manageStudents_txt = (TextView) findViewById(R.id.manageStudents_txt);

        MenuName = getResources().getStringArray(R.array.menuoption1);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftRl = (RelativeLayout) findViewById(R.id.whatYouWantInLeftDrawer);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems_main = new ArrayList<menuoptionItem>();
        adapter_menu_item = new menuoptionItemAdapter(d2_MyProfile.this, navDrawerItems_main);
        for (int i = 0; i < MenuName.length; i++) {
            navDrawerItems_main.add(new menuoptionItem(MenuName[i]));
        }
        mDrawerList.setAdapter(adapter_menu_item);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        payHistory = (RelativeLayout) findViewById(R.id.payHistory);
        addStudents = (RelativeLayout) findViewById(R.id.addStudents);
        manageStudents = (RelativeLayout) findViewById(R.id.manageStudents);
        changeEmail = (RelativeLayout) findViewById(R.id.changeEmail);
        changePassword = (RelativeLayout) findViewById(R.id.changePassword);
        manageProfile = (RelativeLayout) findViewById(R.id.manageProfile);
        email_pref = (RelativeLayout) findViewById(R.id.email_pref);
        relProgressReport = (RelativeLayout) findViewById(R.id.relProgressReport);
        rl_changeAddLoc = (RelativeLayout) findViewById(R.id.rl_changeAddLoc);
        rlAddPaymentAccount = (RelativeLayout) findViewById(R.id.rlAddPaymentAccount);
        rlManagePaymentAccount = (RelativeLayout) findViewById(R.id.rlManagePaymentAccount);
        currentEmail = (TextView) findViewById(R.id.currentEmail);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_location_count = (TextView) findViewById(R.id.tv_location_count);
        relMenu = (Button) findViewById(R.id.relMenu);
        btnHome = (Button) findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(d2_MyProfile.this, DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        currentEmail.setText("Email - " + AppConfiguration.EmailAdd);

    }

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        isInternetPresent = Utility.isNetworkConnected(d2_MyProfile.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new LoadMyAccountAsyncTask().execute();
            new GetStudentCount().execute();

            relMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(leftRl);
                }
            });

            if (ChangedEmail.toString().trim().length() > 0) {
                currentEmail.setText("Email - " + ChangedEmail);
            }

            changePassword.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    startActivity(new Intent(mContext, d2_ChangePassword.class));
                }
            });
            changeEmail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(mContext, ChangeEmailAddress2.class);
                    startActivity(i);
                }
            });
            manageProfile.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    startActivity(new Intent(mContext, ManageProfile.class));
                }
            });
            manageStudents.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ManageStudents.class);
//                Intent i = new Intent(mContext, ViewStudentsListActivity.class);
                    startActivity(i);
                }
            });
            addStudents.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new checkStudentCountAsyncTask().execute();
                }
            });
            payHistory.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ViewPaymentHistoryListNewActivity.class);
                    startActivity(i);
                }
            });
            email_pref.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, EmailPreferencesActivity.class);
                    startActivity(i);
                }
            });
            relProgressReport.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ProgressReportActivity.class);
                    startActivity(i);
                }
            });
            rl_changeAddLoc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, Locations.class);
                    startActivity(i);
//                finish();
                }
            });

            //Payment options
            rlAddPaymentAccount.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, AddPaymentAccount.class);
                    startActivity(i);
                }
            });

            rlManagePaymentAccount.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ManagePaymentAccount.class);
                    startActivity(i);
                }
            });

            new SiteAddressByFamilyAsyncTask().execute();
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

    String studentCount = "";

    class GetStudentCount extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("FamilyID", familyID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetStudentCount, param);

            try {
                JSONObject reader = new JSONObject(responseString);
                String success = reader.getString("Success");
                if (success.equalsIgnoreCase("True")) {
                    JSONArray jsonArray = reader.getJSONArray("Student");
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        studentCount = jsonObject.getString("StudentCount");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (!studentCount.equalsIgnoreCase("")) {
                if (studentCount.equalsIgnoreCase("0")) {
                    student_count.setBackgroundResource(R.drawable.rectangle_with_corner_red);
                    student_count.setText("!");
                    manageStudents_txt.setText("Register a Student");
                } else {
                    student_count.setBackgroundResource(R.drawable.rectangle_with_corner_green);
                    student_count.setText(studentCount);
                    manageStudents_txt.setText("Manage Students");
                }
            }
        }
    }

    class LoadMyAccountAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(mContext);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadingMyAccountInfo();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            currentEmail.setText("Email - " + AppConfiguration.EmailAdd);
        }
    }

    public void loadingMyAccountInfo() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
        params.put("FamilyID", familyID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getMyAccountURL, params);

        readAndParseJSON(responseString);
    }

    String successMyAccount, successStudentCount;

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successMyAccount = reader.getString("Success");
            if (successMyAccount.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("GetFamilyDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    AppConfiguration.status = jsonChildNode.getString("status");
                    AppConfiguration.statusID = jsonChildNode.getString("wu_MemStatus");
                    AppConfiguration.SiteID = jsonChildNode.getString("WU_siteid");
                    AppConfiguration.hearAbout = jsonChildNode.getString("WU_Hearabout");

                    AppConfiguration.hearAbout_first = jsonChildNode.getString("WU_Hearabout_Mst_Lable");
                    AppConfiguration.strMaster = jsonChildNode.getString("WU_Hearabout_Mst_Value");
                    AppConfiguration.hearAbout_second = jsonChildNode.getString("WU_Hearabout_Scd_Lable");
                    AppConfiguration.strSecondary = jsonChildNode.getString("WU_Hearabout_Scd_value");
                    AppConfiguration.hearAbout_third = jsonChildNode.getString("WU_Hearabout_Chd_Lable");
                    AppConfiguration.strChild = jsonChildNode.getString("WU_Hearabout_Chd_Value");

                    AppConfiguration.Company1 = jsonChildNode.getString("Company1");
                    AppConfiguration.Industry1 = jsonChildNode.getString("Industry1");
                    AppConfiguration.Company2 = jsonChildNode.getString("Company2");
                    AppConfiguration.Industry2 = jsonChildNode.getString("Industry2");
                    AppConfiguration.Password = jsonChildNode.getString("password");
                    AppConfiguration.ConfirmEmail = jsonChildNode.getString("ConfrmEmail");
                    AppConfiguration.strOther = jsonChildNode.getString("OtherTxt");
                    AppConfiguration.ConfrmPassword = jsonChildNode.getString("Confrmpassword");
                    AppConfiguration.Orgpassword = jsonChildNode.getString("Orgpassword");
                    AppConfiguration.OrgConfrmpassword = jsonChildNode.getString("OrgConfrmpassword");
                    AppConfiguration.EmailAdd = jsonChildNode.getString("Email");
                    AppConfiguration.PFirstName = jsonChildNode.getString("PFirstName");
                    AppConfiguration.PLastName = jsonChildNode.getString("PLastName");
                    AppConfiguration.SFirstName = jsonChildNode.getString("SFirstName");
                    AppConfiguration.SLastName = jsonChildNode.getString("SLastName");
                    AppConfiguration.Address = jsonChildNode.getString("Address");
                    AppConfiguration.City = jsonChildNode.getString("City");
                    AppConfiguration.State = jsonChildNode.getString("State");
                    AppConfiguration.Zipcode = jsonChildNode.getString("ZipeCode");
                    AppConfiguration.Phone1 = jsonChildNode.getString("Phone1");
                    AppConfiguration.Phone2 = jsonChildNode.getString("Phone2");
                    AppConfiguration.Phone3 = jsonChildNode.getString("Phone3");
                    AppConfiguration.Phone4 = jsonChildNode.getString("Phone4");
                    AppConfiguration.Phone5 = jsonChildNode.getString("Phone5");
                    AppConfiguration.Phone6 = jsonChildNode.getString("Phone6");
                    AppConfiguration.Phone7 = jsonChildNode.getString("Phone7");
                    AppConfiguration.Phone8 = jsonChildNode.getString("Phone8");

                    AppConfiguration.PhoneType1 = jsonChildNode.getString("PhoneType1");
                    AppConfiguration.PhoneType2 = jsonChildNode.getString("PhoneType2");
                    AppConfiguration.PhoneType3 = jsonChildNode.getString("PhoneType3");
                    AppConfiguration.PhoneType4 = jsonChildNode.getString("PhoneType4");
                    AppConfiguration.PhoneType5 = jsonChildNode.getString("PhoneType5");
                    AppConfiguration.PhoneType6 = jsonChildNode.getString("PhoneType6");
                    AppConfiguration.PhoneType7 = jsonChildNode.getString("PhoneType7");
                    AppConfiguration.PhoneType8 = jsonChildNode.getString("PhoneType8");

                    AppConfiguration.CurrentOccupation1 = jsonChildNode.getString("CurrentOccupation1");
                    AppConfiguration.CurrentOccupation2 = jsonChildNode.getString("CurrentOccupation2");
                    AppConfiguration.PLeavePrivate = jsonChildNode.getString("PLeavePrivate");
                    AppConfiguration.SLeavePrivate = jsonChildNode.getString("SLeavePrivate");
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String Message;

    class checkStudentCountAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(mContext);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            studentCountInfo();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successStudentCount.equalsIgnoreCase("True")) {
                Intent i = new Intent(mContext, RegisterChildScreen1Activity.class);
                startActivity(i);
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
                alertDialogBuilder.setMessage(Html.fromHtml("" + Message));
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    public void studentCountInfo() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Token", token);
        params.put("FamilyID", familyID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.getChildCountURL, params);
        readAndParseJSONStudntCount(responseString);
    }

    public void readAndParseJSONStudntCount(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successStudentCount = reader.getString("Success");
            if (successStudentCount.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    Message = jsonChildNode.getString("Msg").trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get site address by family
    class SiteAddressByFamilyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(d2_MyProfile.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadRegionLoction();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
//            19-06-2017 megha
            if (locatonCount == 1) {
                tv_location.setText("Locations" + " - " + siteName.get(0));
                tv_location_count.setText(String.valueOf(locatonCount));
                tv_location_count.setBackgroundResource(R.drawable.rectangle_with_corner_green);
            } else if (locatonCount == 2) {
                tv_location.setText("Locations" + " - " + siteName.get(0) + ", " + siteName.get(1));
                tv_location_count.setText(String.valueOf(locatonCount));
                tv_location_count.setBackgroundResource(R.drawable.rectangle_with_corner_green);
            } else if (locatonCount > 2) {
                tv_location.setText("Locations" + " - " + siteName.get(0) + ", " + siteName.get(1) + " & " + String.valueOf(locatonCount - 2) + " Others");
                tv_location_count.setText(String.valueOf(locatonCount));
                tv_location_count.setBackgroundResource(R.drawable.rectangle_with_corner_green);
            } else{
                tv_location_count.setText(String.valueOf(locatonCount));
                tv_location_count.setBackgroundResource(R.drawable.rectangle_with_corner_green);
            }
        }
    }

    String locationreader;

    public void loadRegionLoction() {
        HashMap<String, String> param = new HashMap<String, String>();
        Log.e("Token---", token);
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALesssionSiteListURL, param);
        readAndParseLOcJSON(responseString);
    }

    public void readAndParseLOcJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            //            19-06-2017 megha
            locationreader = reader.getString("Success");
            if (locationreader.equalsIgnoreCase("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SiteList");
                locatonCount = jsonMainNode.length();
                AppConfiguration.checklocationcount=locatonCount;
                Log.d("locatonCount--#-Loc", "" + locatonCount);
                Log.d("locatonCount-2-#-Loc", "" + (locatonCount - 2));
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String splitSiteName = jsonChildNode.getString("sitename").toString().split("@")[0];
                    siteName.add(splitSiteName);
                    Log.d("splitSiteName--#-Loc", "" + splitSiteName);
                    Log.d("siteName--#-Loc", "" + siteName.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
