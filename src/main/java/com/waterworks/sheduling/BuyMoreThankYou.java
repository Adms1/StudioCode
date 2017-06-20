package com.waterworks.sheduling;

import java.util.ArrayList;

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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterworks.CancelLessonFragment;
import com.waterworks.ConstantData;
import com.waterworks.DashBoardActivity;
import com.waterworks.HelpAndSupportFragment;
import com.waterworks.HomeFragment;
import com.waterworks.LoginActivity;
import com.waterworks.R;
import com.waterworks.SplashScreen;
import com.waterworks.SwimCompititionRegisterAcitivity;
import com.waterworks.SwimCompititionUpcomingEventsAcitivity;
import com.waterworks.adapter.menuoptionItemAdapter;
import com.waterworks.manageProfile.d2_MyProfile;
import com.waterworks.model.menuoptionItem;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

public class BuyMoreThankYou extends Activity {
    //	ButtonRectangle btn_schedule_lessons, btn_my_schedule;
    Context context = this;
    Button relMenu,btn_viewCart, btn_schedule_lessons, btn_my_schedule;
    View rootView;
    TextView tv_thankyou, tv_successful_msg, tv_confirmation_mail;
    Animation animFadein, slideup;
    SharedPreferences SP;
    String token;
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
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buymore_thankyou);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        clearAll();
        init();
        typeFace();
        SP = getApplicationContext().getSharedPreferences(filename, 0);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(this);
        token = prefs.getString("Token", "");
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


    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        relMenu.setTypeface(regular);
        btn_my_schedule.setTypeface(regular);
        btn_schedule_lessons.setTypeface(regular);
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


    public void goooo(int number) {
        AppConfiguration.custom_flow = true;
        AppConfiguration.transform = number;
        Intent i = new Intent(context, DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void init() {
        // TODO Auto-generated method stub
        MenuName = getResources().getStringArray(R.array.menuoption1);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftRl = (RelativeLayout) findViewById(R.id.whatYouWantInLeftDrawer);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems_main = new ArrayList<menuoptionItem>();
        adapter_menu_item = new menuoptionItemAdapter(BuyMoreThankYou.this, navDrawerItems_main);
        for (int i = 0; i < MenuName.length; i++) {
            navDrawerItems_main.add(new menuoptionItem(MenuName[i]));
        }
        mDrawerList.setAdapter(adapter_menu_item);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        tv_thankyou = (TextView) findViewById(R.id.tv_thankyou);
        relMenu = (Button) findViewById(R.id.relMenu_thnx);
        btn_viewCart=(Button)findViewById(R.id.btn_viewCart);
        btn_schedule_lessons = (Button) findViewById(R.id.btn_schedule_lessons);
        btn_my_schedule = (Button) findViewById(R.id.btn_my_schedule);
        tv_successful_msg = (TextView) findViewById(R.id.tv_successful_msg);
        tv_confirmation_mail = (TextView) findViewById(R.id.tv_confirmation_mail);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
        slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);

        tv_thankyou.startAnimation(animFadein);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                    if (AppConfiguration.myCartPackageArray.size() != 0) {

                       /* tv_successful_msg.setText("You have scuccessfully purchased" + "\n" + "              "
                                + AppConfiguration.myCartMonthlyPlanArray.get(0).get("Quantity") + "  swim lessons");
                        tv_confirmation_mail.setText(
                                "A confirmation email will be sent to the email \n \n                Jon@waterworksswim.com");
                        tv_successful_msg.startAnimation(slideup);
                        tv_confirmation_mail.startAnimation(slideup);
                        btn_schedule_lessons.setVisibility(View.VISIBLE);
                        btn_my_schedule.setVisibility(View.VISIBLE);*/
                    } else {

                        Log.e("loginParentEmail", AppConfiguration.loginParentEmail);
                        tv_successful_msg.setText(AppConfiguration.successMsg2);
                        tv_confirmation_mail.setText(AppConfiguration.successMsg1);
                        tv_successful_msg.startAnimation(slideup);
                        tv_confirmation_mail.startAnimation(slideup);
                        btn_schedule_lessons.setVisibility(View.VISIBLE);
                        btn_my_schedule.setVisibility(View.VISIBLE);
                    }

            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                btn_schedule_lessons.startAnimation(animFadein);
                btn_my_schedule.startAnimation(animFadein);
                btn_schedule_lessons.setVisibility(View.VISIBLE);
                btn_my_schedule.setVisibility(View.VISIBLE);
            }
        }, 1000);

        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ByMoreMyCart.class);
                startActivity(i);
                finish();
            }
        });
        btn_schedule_lessons.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    clearAll();
                    Intent i = new Intent(BuyMoreThankYou.this, ScheduleLessonFragement.class);
//                    Intent i = new Intent(BuyMoreThankYou.this, DashBoardActivity.class);
//                    AppConfiguration.custom_flow = true;
//                    AppConfiguration.transform = 5;
                    startActivity(i);
//                    finish();

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                /*Intent i = new Intent(getApplicationContext(), ScheduleLessonFragement.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				clearAll();
				finish();*/
            }
        });
        btn_my_schedule.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clearAll();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(BuyMoreThankYou.this, DashBoardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppConfiguration.custom_flow = true;
                            AppConfiguration.transform = 2;
                            startActivity(i);
                            finish();
                            clearAll();
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
						/*try {

							Fragment	 fragment1= new CancelLessonFragment();
							Bundle bundle = new Bundle();
							bundle.putString("buymorethankyou", "buymorethankyou");
							fragment1.setArguments(bundle);
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction()
									.replace(R.id.frame_container, fragment1).commit();
							clearAll();
						} catch (Exception e) {
							// TODO: handle exception
						}*/


                    }
                }, 700);


            }
        });
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                mDrawerLayout.openDrawer(leftRl);
                //DashBoardActivity.onLeft();
                //clearAll();
            }
        });


    }

    public static void onLeft() {
        // TODO Auto-generated method stub
        mDrawerLayout.openDrawer(leftRl);
    }

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

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//		relMenu.setOnClickListener(new OnClickListener() {
//
//
//			/* * @Override public void onClick(View v) { // TODO Auto-generated
//			 * method stub
//			 *
//			 * DashBoardActivity.onLeft(); Toast.makeText(context, "clicked",
//			 * Toast.LENGTH_SHORT).show(); }*/
//
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(context, DashBoardActivity.class);
//				i.putExtra("openedDrawer", 1);
//				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				clearAll();
//				startActivity(i);
//				finish();
//				DashBoardActivity.onLeft();
//			}
//		});

    }


	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(getApplicationContext(),
				BuyMoreSwimLession.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}*/

    public void clearAll() {
        AppConfiguration.myCartPackageArray.clear();
        AppConfiguration.myCartMonthlyPlanArray.clear();
        AppConfiguration.finalArrayPackage.clear();
        AppConfiguration.finalArrayMonthlyPlan.clear();
        AppConfiguration.lessonArray.clear();
        AppConfiguration.cardDetailArray.clear();
        AppConfiguration.selectedCardDetailArray.clear();
        AppConfiguration.totalPromoArray.clear();
        /*AppConfiguration.insertedCheckDetailArray.clear();
        AppConfiguration.insertedCardDetailArray.clear();*/
        AppConfiguration.finalSelectedPaymentArray.clear();
//        AppConfiguration.Basketid = "Basketid";
        AppConfiguration.BasketID = "0";

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
        displayView(7);
    }

    public void contactUs() {
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

    boolean first_time_trans = true;

    private void displayView(int position) {
        // update the main content by replacing fragments
        //		Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0:
                /*AppConfiguration.makeup_Clicked = 0;
                fragment = new HomeFragment();
                myid = fragment.getId();
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);*/
                Intent iDash = new Intent(BuyMoreThankYou.this, DashBoardActivity.class);
                iDash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iDash);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 1:
//                fragment = new CheckInFragment();
                Intent acc = new Intent(BuyMoreThankYou.this, d2_MyProfile.class);
                startActivity(acc);
//                fragment = new MyAccountw1Fragment();
                break;
            case 2:
                Intent i = new Intent(BuyMoreThankYou.this, CancelLessonFragment.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                fragment = new ViewScheduleFragment();
                break;
            case 3:
//                fragment = new MakePurchaseFragment();
                Intent intentBuyMoreLessons = new Intent(this, BuyMoreSwimLession.class);
                startActivity(intentBuyMoreLessons);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 4:
                Intent can = new Intent(BuyMoreThankYou.this, ScheduleLessonFragement.class);
                startActivity(can);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 5:

                /*Intent intentSwimCompetion = new Intent(DashBoardActivity.this, SwimCompetitionTabbedActivity.class);
                startActivity(intentSwimCompetion);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/
                AppConfiguration.CheckMeet = Utility.getCheckMeet(token);
                Intent intentSwimCompetion = null;
                if(AppConfiguration.CheckMeet.equalsIgnoreCase("0")){
                    intentSwimCompetion = new Intent(BuyMoreThankYou.this, SwimCompititionRegisterAcitivity.class);

                }else if(AppConfiguration.CheckMeet.equalsIgnoreCase("1")){
                    intentSwimCompetion = new Intent(BuyMoreThankYou.this, SwimCompititionUpcomingEventsAcitivity.class);

                }else if(AppConfiguration.CheckMeet.equalsIgnoreCase("2")){
                    intentSwimCompetion = new Intent(BuyMoreThankYou.this, SwimCompititionRegisterAcitivity.class);

                }
                startActivity(intentSwimCompetion);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            /*case 6:
                // no Kid's Corner Fragment present
                break;*/
            case 6:
                Intent can1 = new Intent(BuyMoreThankYou.this, CancelLessonFragment.class);
                startActivity(can1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case 7:
                Intent iByMoreOtherPrograms = new Intent(BuyMoreThankYou.this, ByMoreOtherPrograms.class);
                startActivity(iByMoreOtherPrograms);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 8:
                Intent helpSupIntent = new Intent(BuyMoreThankYou.this, HelpAndSupportFragment.class);
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

                        Editor editor = SP.edit();

                        editor.putString("username", "");
                        editor.putString("password", "");
                        editor.putBoolean("AUTO_ISCHECK", false).commit();
                        editor.commit();

                        //										startActivity(new Intent(DashboardActivity.this,WelcometoGPSActivity.class));
                        Intent loginIntent = new Intent(
                                getApplicationContext(),
                                LoginActivity.class);
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


        if (fragment != null) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            if (fragment instanceof HomeFragment) {
                if (first_time_trans) {
                    first_time_trans = false;
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0, 0)
                            .replace(R.id.frame_container, fragment).commit();

//                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                } else {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0, 0)
                            .replace(R.id.frame_container, fragment).commit();
//                    R.anim.slide_in_left, R.anim.slide_out_right
//                    fragmentTransaction.setCustomAnimations(R.animator.slide_out_right, R.animator.slide_in_left);
                }
            } else {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(0,0)
                        .replace(R.id.frame_container, fragment).commit();
            }


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


}





/*
package com.waterworks.sheduling;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.adapter.menuoptionItemAdapter;
import com.waterworks.model.menuoptionItem;
import com.waterworks.utils.AppConfiguration;

public class BuyMoreThankYou extends Activity {
	ButtonRectangle btn_schedule_lessons, btn_my_schedule;
	Context context = this;
	Button relMenu;
	View rootView;
	TextView tv_thankyou, tv_successful_msg, tv_confirmation_mail;
	Animation animFadein, slideup;

	//SlideMenu
	static DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	static RelativeLayout leftRl;
	private_lessons ArrayList<menuoptionItem> navDrawerItems_main;
	private_lessons menuoptionItemAdapter adapter_menu_item;
	String MenuName[];
	int dispPOS=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buymore_thankyou);

		init();

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

	private_lessons class SlideMenuClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			goooo(position);
			//				displayView(position);
		}
	}



	public void goooo(int number){
		AppConfiguration.custom_flow=true;
		AppConfiguration.transform = number;
		Intent i = new Intent(context,DashBoardActivity.class);
		startActivity(i);
		finish();
	}

	private_lessons void init() {
		// TODO Auto-generated method stub
		MenuName = getResources().getStringArray(R.array.menuoption1);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftRl = (RelativeLayout) findViewById(R.id.whatYouWantInLeftDrawer);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems_main = new ArrayList<menuoptionItem>();
		adapter_menu_item = new menuoptionItemAdapter(BuyMoreThankYou.this, navDrawerItems_main);
		for (int i = 0; i < MenuName.length; i++) {
			navDrawerItems_main.add(new menuoptionItem(MenuName[i]));
		}
		mDrawerList.setAdapter(adapter_menu_item);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		tv_thankyou = (TextView) findViewById(R.id.tv_thankyou);
		relMenu = (Button) findViewById(R.id.relMenu_thnx);
		btn_schedule_lessons = (ButtonRectangle) findViewById(R.id.btn_schedule_lessons);
		btn_my_schedule = (ButtonRectangle) findViewById(R.id.btn_my_schedule);
		tv_successful_msg = (TextView) findViewById(R.id.tv_successful_msg);
		tv_confirmation_mail = (TextView) findViewById(R.id.tv_confirmation_mail);

		animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
		slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);

		tv_thankyou.startAnimation(animFadein);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (AppConfiguration.myCartPackageArray.size() == 0) {

					tv_successful_msg.setText("You have scuccessfully purchased" + "\n" + "              "
							+ AppConfiguration.myCartMonthlyPlanArray.get(0).get("Quantity") + "  swim lessons");
					tv_confirmation_mail.setText(
							"A confirmation email will be sent to the email \n \n                Jon@waterworksswim.com");
					tv_successful_msg.startAnimation(slideup);
					tv_confirmation_mail.startAnimation(slideup);
					btn_schedule_lessons.setVisibility(View.VISIBLE);
					btn_my_schedule.setVisibility(View.VISIBLE);
				} else {
					tv_successful_msg.setText("You have scuccessfully purchased" + "\n" + "              "
							+ AppConfiguration.myCartPackageArray.get(0).get("Quantity") + "  swim lessons");
					tv_confirmation_mail.setText(
							"A confirmation email will be sent to the email \n \n                Jon@waterworksswim.com");
					tv_successful_msg.startAnimation(slideup);
					tv_confirmation_mail.startAnimation(slideup);
					btn_schedule_lessons.setVisibility(View.VISIBLE);
					btn_my_schedule.setVisibility(View.VISIBLE);
				}
			}
		}, 1000);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				btn_schedule_lessons.startAnimation(animFadein);
				btn_my_schedule.startAnimation(animFadein);
				btn_schedule_lessons.setVisibility(View.VISIBLE);
				btn_my_schedule.setVisibility(View.VISIBLE);
			}
		}, 1000);

		btn_schedule_lessons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), ScheduleLessonFragement.class);

				clearAll();

				//				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				finish();
			}
		});
		btn_my_schedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearAll();
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// ((DashBoardActivity)context).cancelLesson();
						// ((DashBoardActivity)getActivity()).viewSchedule();
					}
				}, 700);
			}
		});
		relMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
				mDrawerLayout.openDrawer(leftRl);
//				DashBoardActivity.onLeft();
			}
		});

	}

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


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		relMenu.setOnClickListener(new OnClickListener() {
//
//
//			*/
/* * @Override public void onClick(View v) { // TODO Auto-generated
//			 * method stub
//			 *
//			 * DashBoardActivity.onLeft(); Toast.makeText(context, "clicked",
//			 * Toast.LENGTH_SHORT).show(); }*//*

//
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(context, DashBoardActivity.class);
//				i.putExtra("openedDrawer", 1);
//				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				clearAll();
//				startActivity(i);
//				finish();
//				DashBoardActivity.onLeft();
//			}
//		});

	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(getApplicationContext(),
				BuyMoreSwimLession.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

	public void clearAll(){
		AppConfiguration.myCartPackageArray.clear();
		AppConfiguration. myCartMonthlyPlanArray .clear();
		AppConfiguration. finalArrayPackage .clear();
		AppConfiguration. finalArrayMonthlyPlan.clear();
		AppConfiguration. lessonArray .clear();
		AppConfiguration. cardDetailArray .clear();
		AppConfiguration.selectedCardDetailArray .clear();
		AppConfiguration.totalPromoArray .clear();
		AppConfiguration.insertedCheckDetailArray.clear();
		AppConfiguration.insertedCardDetailArray.clear();
		AppConfiguration.finalSelectedPaymentArray.clear();
		AppConfiguration.Basketid = "Basketid";




	}

}
*/
