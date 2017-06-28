package com.waterworks;

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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterworks.ConstantData;
import com.waterworks.DashBoardActivity;
import com.waterworks.FeedbackActivity;
import com.waterworks.HelpCenterActivity;
import com.waterworks.R;
import com.waterworks.adapter.menuoptionItemAdapter;
import com.waterworks.model.menuoptionItem;
import com.waterworks.sheduling.BuyMoreThankYou;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.URLSpanNoUnderline;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rakesh Tiwari ADMS on 2/1/2016.
 */
public class HelpAndSupportFragment extends Activity implements View.OnClickListener {

    public HelpAndSupportFragment() {
    }

    private static String TAG = "Contact Us";
    View rootView;
    Boolean isInternetPresent = false;
    String message;
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    String successContact;
    RelativeLayout title_black, rlPhoneNumber, rlSendMessage, rlReportBug, rlPrivacyPolicy, rlTermsOfUse;
    TextView tvPhoneNumber,tv_web,tv_version_numb_name;
    Fade mFade;
    Context mContext = this;
    //SlideMenu
    static DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    static RelativeLayout leftRl;
    private ArrayList<menuoptionItem> navDrawerItems_main;
    private menuoptionItemAdapter adapter_menu_item;
    public static String filename = "Valustoringfile";
    String MenuName[];
    int dispPOS = 0;
    SharedPreferences SP;

    String token, familyID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_and_support_fragment);
        tv_version_numb_name=(TextView)findViewById(R.id.tv_version_numb_name);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_version_numb_name.setText("Client Version "+pInfo.versionName.toString());
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(this.getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        SP = getApplicationContext().getSharedPreferences(filename, 0);
        initialization();

        isInternetPresent = Utility.isNetworkConnected(this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new LoadContactListAsyncTask().execute();
        }
        View view = findViewById(R.id.header);
        TextView title = (TextView) findViewById(R.id.page_title);
        title.setText("Help & Support");

        ImageButton ib_menusad = (ImageButton) findViewById(R.id.ib_menusad);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ib_menusad.setBackgroundResource(R.drawable.menu_icon_new);
        rlPhoneNumber = (RelativeLayout) findViewById(R.id.rlPhoneNumber);
        rlSendMessage = (RelativeLayout) findViewById(R.id.rlSendMessage);
        rlReportBug = (RelativeLayout) findViewById(R.id.rlReportBug);
        rlPrivacyPolicy = (RelativeLayout) findViewById(R.id.rlPrivacyPolicy);
        rlTermsOfUse = (RelativeLayout) findViewById(R.id.rlTermsOfUse);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        tv_web= (TextView) findViewById(R.id.tv_web);

        if(tv_web != null) {
            removeUnderlines((Spannable)tv_web.getText());
        }
        Button relMenu = (Button) findViewById(R.id.relMenu_thnx);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DashBoardActivity.onLeft();
                mDrawerLayout.openDrawer(leftRl);
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(HelpAndSupportFragment.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        rlPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = tvPhoneNumber.getText().toString().replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
                startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+1" + phoneNumber)));
            }
        });

        rlSendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sendMsgIntent = new Intent(HelpAndSupportFragment.this, SendUsMessage.class);
                startActivity(sendMsgIntent);
            }
        });
        rlReportBug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent reportBugIntent = new Intent(HelpAndSupportFragment.this, ReportBug.class);
                startActivity(reportBugIntent);
            }
        });
        rlPrivacyPolicy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent privacyPolicyIntent = new Intent(HelpAndSupportFragment.this, PrivacyPolicy.class);
                startActivity(privacyPolicyIntent);
            }
        });
        rlTermsOfUse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent termsUseIntent = new Intent(HelpAndSupportFragment.this, TermsOfUse.class);
                startActivity(termsUseIntent);
            }
        });

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

        MenuName = getResources().getStringArray(R.array.menuoption1);

        leftRl = (RelativeLayout) findViewById(R.id.whatYouWantInLeftDrawer);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems_main = new ArrayList<menuoptionItem>();
        adapter_menu_item = new menuoptionItemAdapter(HelpAndSupportFragment.this, navDrawerItems_main);
        for (int i = 0; i < MenuName.length; i++) {
            navDrawerItems_main.add(new menuoptionItem(MenuName[i]));
        }
        mDrawerList.setAdapter(adapter_menu_item);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
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

    Fragment fragment = null;
    int myid;
    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
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
    private void displayView(int position) {
        // update the main content by replacing fragments

        switch (position) {
            case 0:
                Intent iDash = new Intent(HelpAndSupportFragment.this, DashBoardActivity.class);
                iDash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iDash);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 1:
                gooo(1);
                break;
            case 2:
                gooo(2);
                break;
            case 3:
                gooo(3);
                break;
            case 4:
                gooo(4);
                break;
            case 5:
                gooo(5);
                break;
            case 7:
                gooo(7);
                break;
            case 8:
                gooo(8);
                break;
            case 9:
                gooo(9);
                break;
            case 10:
                AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(HelpAndSupportFragment.this);
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
            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
            Log.e("Dashboard", "Error in creating fragment");
        }
    }

    public void gooo(int numner) {
        AppConfiguration.custom_flow = true;
        AppConfiguration.transform = numner;
        Intent i = new Intent(mContext, DashBoardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        ConstantData.destroyed = true;
    }
    public void initialization() {
    }

    public void getContactList() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN +AppConfiguration.Get_ContactDtl_new, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successContact = reader.getString("Success");
            if (successContact.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SiteDetail");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String Sitename = jsonChildNode.getString("Sitename").trim();
                    String SiteDetails = jsonChildNode.getString("SiteDetails").trim();
                    String Pnoneno = jsonChildNode.getString("Pnoneno").trim();
                    String Details2 = jsonChildNode.getString("Details2").trim();

                    hashmap.put("Sitename", Sitename);
                    hashmap.put("SiteDetails", SiteDetails);
                    hashmap.put("Pnoneno", Pnoneno);
                    hashmap.put("Details2", Details2);

                    contactList.add(hashmap);

                }

            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoadContactListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HelpAndSupportFragment.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getContactList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successContact.toString().equals("True")) {
                tvPhoneNumber.setText(contactList.get(0).get("Pnoneno").toString());
            }
        }
    }
    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final ArrayList<HashMap<String, String>> data;

        public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_row_contact_us);
            this.context = context;
            this.data = list;
        }

        @Override
        public int getCount() {
            return data.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_row_contact_us, null, true);

            TextView txtSiteName = (TextView) rowView.findViewById(R.id.txtSiteName);
            txtSiteName.setText(data.get(position).get("Sitename"));

            TextView txtVenue = (TextView) rowView.findViewById(R.id.txtVenue);
            txtVenue.setText(Html.fromHtml(data.get(position).get("SiteDetails")));

            TextView txtPhone = (TextView) rowView.findViewById(R.id.txtPhone);
            txtPhone.setText(Html.fromHtml(data.get(position).get("Pnoneno")));

            TextView txtHrsOfOperation = (TextView) rowView.findViewById(R.id.txtHrsOfOperation);
            txtHrsOfOperation.setText(Html.fromHtml(data.get(position).get("Details2")));

            txtPhone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + data.get(position).get("Pnoneno")));
                    startActivity(callIntent);
                }
            });

            return rowView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relMenu:
                DashBoardActivity.onLeft();
                break;

            default:
                break;
        }
    }
    public static void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for(URLSpan span:spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
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
}
