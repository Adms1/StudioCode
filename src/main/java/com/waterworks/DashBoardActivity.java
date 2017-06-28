package com.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterworks.adapter.menuoptionItemAdapter;
import com.waterworks.manageProfile.d2_MyProfile;
import com.waterworks.model.menuoptionItem;
import com.waterworks.sheduling.BuyMoreRetailStore;
import com.waterworks.sheduling.BuyMoreSwimLession;
import com.waterworks.sheduling.ByMoreOtherPrograms;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;


import java.util.ArrayList;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class DashBoardActivity extends Activity {
    static DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    static RelativeLayout leftRl;
    private ArrayList<menuoptionItem> navDrawerItems_main;
    private menuoptionItemAdapter adapter_menu_item;
    String MenuName[];
    String token;
    int dispPOS = 0;
    SharedPreferences SP;
    public static String filename = "Valustoringfile";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Boolean isInternetPresent = false;
    Context mContext = this;

//    18-05-2017 megha

//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//
//        if (AppConfiguration.custom_flow) {
//            displayView(AppConfiguration.transform);
//            AppConfiguration.custom_flow = false;
//        }
//
//        if (fragment != null && fragment instanceof HomeFragment) {
//            if (dispPOS == 0) {
//                AppConfiguration.makeup_Clicked = 0;
//            } else {
//            }
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        DashBoardActivity.this.overridePendingTransition(R.anim.zoom_out, R.anim.zoom_out);
      }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        Initialize();
        SP = getApplicationContext().getSharedPreferences(filename, 0);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(this);
        token = prefs.getString("Token", "");
        dispPOS = getIntent().getIntExtra("POS", 0);

        if (dispPOS == 0) {
            AppConfiguration.makeup_Clicked = 0;
        } else {
        }

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

        displayView(dispPOS);
    }

    private void Initialize() {
        // TODO Auto-generated method stub
        MenuName = getResources().getStringArray(R.array.menuoption1);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftRl = (RelativeLayout) findViewById(R.id.whatYouWantInLeftDrawer);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems_main = new ArrayList<menuoptionItem>();
        adapter_menu_item = new menuoptionItemAdapter(DashBoardActivity.this, navDrawerItems_main);
        for (int i = 0; i < MenuName.length; i++) {
            navDrawerItems_main.add(new menuoptionItem(MenuName[i]));
        }
        mDrawerList.setAdapter(adapter_menu_item);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

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

    public android.app.AlertDialog onDetectNetworkState() {
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(mContext);
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

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

    Fragment fragment = null;
    int myid;
    boolean first_time_trans = true;

    private void displayView(int position) {
        isInternetPresent = Utility.isNetworkConnected(DashBoardActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            switch (position) {
                case 0:
                    AppConfiguration.makeup_Clicked = 0;
                    fragment = new HomeFragment();
                    myid = fragment.getId();
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                case 1:
                    Intent acc = new Intent(DashBoardActivity.this, d2_MyProfile.class);
                    startActivity(acc);
                    break;
                case 2:
                    Intent i = new Intent(DashBoardActivity.this, CancelLessonFragment.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case 3:
                    Intent intentBuyMoreLessons = new Intent(this, BuyMoreSwimLession.class);
                    startActivity(intentBuyMoreLessons);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case 4:
                    if (AppConfiguration.PastDueForMessageCount.equalsIgnoreCase("0")) {
                        Intent can = new Intent(DashBoardActivity.this, ScheduleLessonFragement.class);
                        startActivity(can);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else {
                        showUnpaidClassInfom();
                    }
                    break;
                case 5:
                    AppConfiguration.CheckMeet = Utility.getCheckMeet(token);
                    Intent intentSwimCompetion = null;
                    if (AppConfiguration.CheckMeet.equalsIgnoreCase("0")) {
                        intentSwimCompetion = new Intent(DashBoardActivity.this, SwimCompititionRegisterAcitivity.class);

                    } else if (AppConfiguration.CheckMeet.equalsIgnoreCase("1")) {
                        intentSwimCompetion = new Intent(DashBoardActivity.this, SwimCompititionUpcomingEventsAcitivity.class);

                    } else if (AppConfiguration.CheckMeet.equalsIgnoreCase("2")) {
                        intentSwimCompetion = new Intent(DashBoardActivity.this, SwimCompititionUpcomingEventsAcitivity.class);

                    }
                    startActivity(intentSwimCompetion);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case 6:
                    Intent can1 = new Intent(DashBoardActivity.this, CancelLessonFragment.class);
                    startActivity(can1);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case 7:
                    Intent iByMoreOtherPrograms = new Intent(DashBoardActivity.this, ByMoreOtherPrograms.class);
                    startActivity(iByMoreOtherPrograms);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case 8:
                    Intent helpSupIntent = new Intent(DashBoardActivity.this, HelpAndSupportFragment.class);
                    startActivity(helpSupIntent);
                    break;
                case 9:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                    builder.setTitle("WaterWorks");
                    builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
                    builder.setMessage("Are you sure you want to Logout?");
                    builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Editor editor = SP.edit();
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
            }
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (fragment instanceof HomeFragment) {
                if (first_time_trans) {
                    first_time_trans = false;
                    fragmentManager.beginTransaction().setCustomAnimations(0, 0).replace(R.id.frame_container, fragment).commit();
                } else {
                    fragmentManager.beginTransaction().setCustomAnimations(0, 0).replace(R.id.frame_container, fragment).commit();
                }
            } else {
                fragmentManager.beginTransaction().setCustomAnimations(0, 0).replace(R.id.frame_container, fragment).commit();
            }
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
            Log.e("Dashboard", "Error in creating fragment");
        }
    }
    //megha 13-04-2017
    public void showUnpaidClassInfom() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(mContext);
        if (mContext == null)
            return;
        final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.pop_up_layout_unpaidclassinform, null);
        dialogBuilder.setView(dialogView);

        final TextView txt = (TextView) dialogView.findViewById(R.id.textView3);
        txt.setText(AppConfiguration.PastDuemessage);
        final CardView btnok = (CardView) dialogView.findViewById(R.id.button_ok);
        final android.app.AlertDialog b = dialogBuilder.create();
        b.show();
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    public static void onLeft() {
        // TODO Auto-generated method stub
        mDrawerLayout.openDrawer(leftRl);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isTaskRoot()) {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    return true;
                } else {
                    super.onKeyDown(keyCode, event);
                    return false;
                }
            default:
                super.onKeyDown(keyCode, event);
                return false;
        }
    }
    @Override
    public void onBackPressed() {
        if (fragment != null) {
            if (fragment instanceof HomeFragment) {
                displayView(10);
            } else {
                displayView(0);
            }
        } else {
            fragment = new HomeFragment();
            myid = fragment.getId();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            displayView(0);
        }
    }

    public void home() {
        displayView(0);
    }

    public void checkin() {
        displayView(1);
    }

    public void viewaccount() {
        displayView(2);
    }

    public void viewSchedule() {
        displayView(3);
    }

    public void makepurchase() {
        displayView(4);
    }

    public void scheduling() {
        displayView(5);
    }

    public void cancelLesson() {
        displayView(6);
    }

    public void helpAndSupport() {
        displayView(8);
    }

    public void Programs() {
        displayView(9);
    }

    public void which_view_calls() {
        if (ConstantData.clicked_view == 0) {
            if (ConstantData.destroyed == false) {
                displayView(0);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 1) {
            if (ConstantData.destroyed == false) {
                displayView(1);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 2) {
            if (ConstantData.destroyed == false) {
                displayView(2);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 3) {
            if (ConstantData.destroyed == false) {
                displayView(3);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 4) {
            if (ConstantData.destroyed == false) {
                displayView(4);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 5) {
            if (ConstantData.destroyed == false) {
                displayView(5);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 6) {
            if (ConstantData.destroyed == false) {
                displayView(6);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 7) {
            if (ConstantData.destroyed == false) {
                displayView(7);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 8) {
            if (ConstantData.destroyed == false) {
                displayView(8);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        } else if (ConstantData.clicked_view == 9) {
            if (ConstantData.destroyed == false) {
                displayView(9);
            } else {
                displayView(ConstantData.clicked_view + 1);
            }
        }
    }

    public void reShedule() {
        Intent i = new Intent(DashBoardActivity.this, ScheduleLessonFragement.class);
        startActivity(i);
    }
}
