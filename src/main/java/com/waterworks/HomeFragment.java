package com.waterworks;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.waterworks.adapter.HomeGridMenuOptionAdapter;
import com.waterworks.asyncTasks.GetCheckInListAsyncTask;
import com.waterworks.sheduling.BuyMoreSwimLession;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.sheduling.SeeMoreUpcomingLessons;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class HomeFragment extends Fragment {
    public HomeFragment() {
    }
    GridView grid_home_option;
    String[] MenuName;
    //	TextView tv_notification;436512
    View rootView;//, singleLay;

    int[] imageId = {R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5,
            R.drawable.icon6, R.drawable.icon7, R.drawable.icon8, R.drawable.icon9, R.drawable.icon10,
    };
    HomeGridMenuOptionAdapter adapter;
    SharedPreferences SP;
    public static String filename = "Valustoringfile";
    //New Design
    Button relMenu, btnCheckIn;
    //	Toolbar actionbarTitle;
    TextView st_name, timing, inst_name, my_sch_btn, reschdl, err_msg, remaining_title;
    CircleImageView inst_dp;
    String data_load = "False";
    String token, familyID, error_msg;
    ImageLoader imageLoader;
    Button btn_my_schdl, btn_schdl_lsn, btn_buyMore_lsn;
    //Single Multiple Student
    LinearLayout inflate_multiple, multipleStudent, singleStudent;
    TextView show_more, headertitle, headertitle1;
    Fade mFade;
    boolean viewstatus = false;
    RippleView ripple_1, ripple_2, ripple_3;
    CardView btn_my_schdl_card, btn_schdl_lsn_card, btn_buyMore_lsn_card;
    ProgressDialog pd;
    Boolean isInternetPresent = false;
    //    megha 13-04-2017
    public static ArrayList<HashMap<String, String>> schedulePageLoadList = new ArrayList<HashMap<String, String>>();
    String successCout, pastdue, pastduemessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
        Initialization();
        SP = getActivity().getSharedPreferences(filename, 0);

        if (AppConfiguration.animation) {
            mFade = new Fade(Fade.IN);
        }
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ScrollView sView = (ScrollView) rootView.findViewById(R.id.scroll);
        sView.setVerticalScrollBarEnabled(false);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity().getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d("HomeFragment", "Token=" + token + "\nFamilyID=" + familyID);

//        showHideCheckinButton();
        isInternetPresent = Utility.isNetworkConnected(getActivity());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new GetRemDetail().execute();
            new GetStDetail().execute();
            new SchedulePageLoad().execute();
        }
        grid_home_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ConstantData.clicked_view = position;
                if (position == 0) {
                    ((DashBoardActivity) getActivity()).checkin();
                } else if (position == 1) {
                    ((DashBoardActivity) getActivity()).viewaccount();
                } else if (position == 2) {
                    ((DashBoardActivity) getActivity()).cancelLesson();
//					((DashBoardActivity)getActivity()).viewSchedule();
                } else if (position == 3) {
                    ((DashBoardActivity) getActivity()).makepurchase();
                } else if (position == 4) {
                    ((DashBoardActivity) getActivity()).scheduling();
                } else if (position == 6) {
                    ((DashBoardActivity) getActivity()).cancelLesson();
                }
                else if (position == 7) {
                    ((DashBoardActivity) getActivity()).helpAndSupport();
                } else if (position == 8) {
                    ((DashBoardActivity) getActivity()).Programs();
                } else if (position == 9) {
                    AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(
                            getActivity());
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

                                                Editor editor = SP.edit();

                                                editor.putString("username", "");
                                                editor.putString("password", "");
                                                editor.putBoolean("AUTO_ISCHECK", false).commit();
                                                editor.commit();

                                                Intent loginIntent = new Intent(
                                                        getActivity(),
                                                        LoginActivity.class);
                                                loginIntent.putExtra("DOMAIN", SplashScreen.DOMAIN);
                                                startActivity(loginIntent);
                                                getActivity().finish();
                                                android.os.Process.killProcess(android.os.Process.myPid());
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

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "You Clicked at " + MenuName[+position], Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
    private void Initialization() {
        // TODO Auto-generated method stub

        ripple_1 = (RippleView) rootView.findViewById(R.id.ripple_1);
        ripple_2 = (RippleView) rootView.findViewById(R.id.ripple_2);
        ripple_3 = (RippleView) rootView.findViewById(R.id.ripple_3);

        singleStudent = (LinearLayout) rootView.findViewById(R.id.singleStudent);
        inflate_multiple = (LinearLayout) rootView.findViewById(R.id.inflate_multiple);
        multipleStudent = (LinearLayout) rootView.findViewById(R.id.multipleStudent);
        show_more = (TextView) rootView.findViewById(R.id.show_more);
        btnCheckIn = (Button) rootView.findViewById(R.id.btnCheckIn);

        headertitle = (TextView) rootView.findViewById(R.id.headertitle);
        headertitle1 = (TextView) rootView.findViewById(R.id.headertitle1);
        String title = " <font ' color='#ffffff'>Waterworks Aquatics </font>" + "<small><font ' color='#f89522'>Beta</font></small>";

        headertitle.setText(Html.fromHtml(title));
        show_more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getActivity(), SeeMoreUpcomingLessons.class);
                startActivity(i);
            }
        });

        relMenu = (Button) rootView.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DashBoardActivity.onLeft();
            }
        });
        err_msg = (TextView) rootView.findViewById(R.id.err_msg);
        remaining_title = (TextView) rootView.findViewById(R.id.remaining_title);

        st_name = (TextView) rootView.findViewById(R.id.st_name);
        inst_dp = (CircleImageView) rootView.findViewById(R.id.inst_DP);
        inst_name = (TextView) rootView.findViewById(R.id.inst_name);
        my_sch_btn = (TextView) rootView.findViewById(R.id.my_sch_btn);
        timing = (TextView) rootView.findViewById(R.id.timing);
        MenuName = getResources().getStringArray(R.array.menuoption);
        btn_my_schdl_card = (CardView) rootView.findViewById(R.id.btn_my_schdl_card);
        btn_buyMore_lsn_card = (CardView) rootView.findViewById(R.id.btn_buyMore_lsn_card);
        btn_schdl_lsn_card = (CardView) rootView.findViewById(R.id.btn_schdl_lsn_card);
        reschdl = (TextView) rootView.findViewById(R.id.reschdl);

        btn_my_schdl_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((DashBoardActivity) getActivity()).cancelLesson();
                    }
                }, 100);
            }
        });

        btn_schdl_lsn_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pastdue.equalsIgnoreCase("0")) {
                    Intent i = new Intent(getActivity(), ScheduleLessonFragement.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.zoom_out, R.anim.zoom_out);
                } else {
                    showUnpaidClassInfom();
                }
            }
        });

        btn_buyMore_lsn_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent seeMULIntent = new Intent(getActivity(), BuyMoreSwimLession.class);
                        startActivity(seeMULIntent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }, 100);

            }
        });
        ripple_1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                ((DashBoardActivity) getActivity()).cancelLesson();

            }
        });

        ripple_2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(getActivity(), ScheduleLessonFragement.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.zoom_out, R.anim.zoom_out);

            }
        });

        ripple_3.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent seeMULIntent = new Intent(getActivity(), BuyMoreSwimLession.class);
                startActivity(seeMULIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnCheckIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(getActivity().getApplicationContext(), ActivityCheckin.class);
                startActivity(intentCheckin);
                getActivity().finish();
            }
        });
        reschdl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((DashBoardActivity) getActivity()).cancelLesson();
                    }
                }, 100);
//                ((DashBoardActivity) getActivity()).cancelLesson();
            }

        });
        remaining_lay = (LinearLayout) rootView.findViewById(R.id.remaining_lay);
        adapter = new HomeGridMenuOptionAdapter(getActivity().getApplicationContext(), MenuName, imageId);
        grid_home_option = (GridView) rootView.findViewById(R.id.grid_home_grid);
        grid_home_option.setAdapter(adapter);
        typeFace();
    }
//26-05-2017 megha
//    @Override
//    public void onPause() {
//        super.onPause();
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
//    }
@Override
    public void onResume() {
        super.onResume();
    SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity().getApplicationContext());
    token = prefs.getString("Token", "");
        getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
    public void typeFace() {
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "Roboto_Light.ttf");
        Typeface regular = Typeface.createFromAsset(getActivity().getAssets(),
                "RobotoRegular.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(),
                "RobotoBold.ttf");

        st_name.setTypeface(face);
        inst_name.setTypeface(face);
        err_msg.setTypeface(face);
        timing.setTypeface(bold);
    }


    String Err_Msg = "You are not currently scheduled for any lessons.";
    boolean single = true;
    public static ArrayList<HashMap<String, String>> multiStudent = new ArrayList<HashMap<String, String>>();

    private class GetStDetail extends AsyncTask<Void, Void, Void> {

        boolean showmr = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN
                    + AppConfiguration.Get_UpcomingClass, param);


            try {
                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                multiStudent.clear();
                if (data_load.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    if (jsonMainNode.length() > 1) {
                        single = false;
                    } else {
                        single = true;
                    }
                    if (jsonMainNode.length() > 3) {
                        showmr = true;
                    }
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        //MultiStudent
                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        hashmap.put("StudentName", jsonChildNode.getString("StudentName"));
                        hashmap.put("ScheduleDate", jsonChildNode.getString("Schedule Date"));
                        hashmap.put("Instructor", jsonChildNode.getString("Instructor"));

                        String temp = jsonChildNode.getString("Photo");
                        if (temp.trim().contains("~")) {
                            temp = temp.replace("~", "");
                            if (temp.trim().contains(" ")) {
                                temp = AppConfiguration.PhotoDomain + temp.replace(" ", "%20");
                                System.out.println("Pic URL" + temp);
                            } else {
                                temp = AppConfiguration.PhotoDomain + temp;
                                System.out.println("Pic URL" + temp);
                            }
                        }

                        hashmap.put("Photo", temp);
                        multiStudent.add(hashmap);

                        AppConfiguration.StudentName = jsonChildNode.getString("StudentName");
                        AppConfiguration.Photo = jsonChildNode.getString("Photo");
                        if (AppConfiguration.Photo.trim().contains("~")) {
                            AppConfiguration.Photo = AppConfiguration.Photo.replace("~", "");
                            if (AppConfiguration.Photo.trim().contains(" ")) {
                                AppConfiguration.Photo = AppConfiguration.PhotoDomain + AppConfiguration.Photo.replace(" ", "%20");
                                System.out.println("Pic URL" + AppConfiguration.Photo);
                            } else {
                                AppConfiguration.Photo = AppConfiguration.PhotoDomain + AppConfiguration.Photo;
                                System.out.println("Pic URL" + AppConfiguration.Photo);
                            }
                        }

                        AppConfiguration.Schedule_Date = jsonChildNode.getString("Schedule Date");
                        AppConfiguration.Instructor = jsonChildNode.getString("Instructor");
                    }
                } else {
                    Err_Msg = reader.getString("Msg");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            /*if (pd != null) {
                pd.dismiss();
            }*/
            if (data_load.toString().equals("True")) {
                if (AppConfiguration.LAFitnessResult.equalsIgnoreCase("true")) {
                    for (int k = 0; k < count.size(); k++) {
                        if (count.get(k).contains("Past due") || count.get(k).equalsIgnoreCase("0")) {
                            Log.d("mycount", "" + count);
                            viewstatus = false;
                            break;
                        } else {
                            viewstatus = true;
                        }
                    }
                } else {
                    viewstatus = true;
                }
                Log.d("viewstatus", "" + viewstatus + AppConfiguration.LAFitnessResult.equalsIgnoreCase("false") + count.size());
                if (viewstatus == true) {
                    if (single) {
                        singleStudent.setVisibility(View.VISIBLE);
                        multipleStudent.setVisibility(View.GONE);
                        //                    singleLay.setVisibility(View.VISIBLE);
                        timing.setVisibility(View.VISIBLE);
                        inst_name.setVisibility(View.VISIBLE);
                        inst_dp.setVisibility(View.VISIBLE);
                        reschdl.setVisibility(View.VISIBLE);
                        err_msg.setText("With");
                        err_msg.setTextColor(Color.BLACK);
                        st_name.setVisibility(View.VISIBLE);
                        st_name.setText(AppConfiguration.StudentName + "'s next Lesson:");
                        timing.setText(AppConfiguration.Schedule_Date);
                        inst_name.setText(AppConfiguration.Instructor);
                        if (AppConfiguration.Photo.trim().length() > 0) {
                            imageLoader.displayImage(AppConfiguration.Photo, inst_dp);
                        }
                    } else {
                        singleStudent.setVisibility(View.GONE);
                        multipleStudent.setVisibility(View.VISIBLE);

                        if (showmr) {
                            show_more.setVisibility(View.VISIBLE);
                        }
                        if (multiStudent.size() >= 2) {
                            if (multiStudent.size() >= 3) {
                                for (int i = 0; i < 3; i++) {
                                    if (getActivity() == null)
                                        return;
                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View view = inflater.inflate(R.layout.d2_multiplestudent_nextschdl, null);

                                    TextView inst, detai_text;
                                    CircleImageView inst_dp_cust;


                                    inst = (TextView) view.findViewById(R.id.inst_name);
                                    detai_text = (TextView) view.findViewById(R.id.detai_text);
                                    inst_dp_cust = (CircleImageView) view.findViewById(R.id.inst_DP);

                                    detai_text.setText(multiStudent.get(i).get("ScheduleDate") + " - " +
                                            multiStudent.get(i).get("StudentName"));
                                    inst.setText(multiStudent.get(i).get("Instructor"));
                                    imageLoader.displayImage(multiStudent.get(i).get("Photo"), inst_dp_cust);
                                    if (AppConfiguration.animation) {
                                        TransitionManager.beginDelayedTransition(inflate_multiple, mFade);
                                    }
                                    inflate_multiple.addView(view);
                                }
                            } else {
                                for (int i = 0; i < 2; i++) {
                                    if (getActivity() == null)
                                        return;
                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View view = inflater.inflate(R.layout.d2_multiplestudent_nextschdl, null);

                                    TextView inst, detai_text;
                                    CircleImageView inst_dp_cust;

                                    inst = (TextView) view.findViewById(R.id.inst_name);
                                    detai_text = (TextView) view.findViewById(R.id.detai_text);
                                    inst_dp_cust = (CircleImageView) view.findViewById(R.id.inst_DP);

                                    detai_text.setText(multiStudent.get(i).get("ScheduleDate") + " - " +
                                            multiStudent.get(i).get("StudentName"));
                                    inst.setText(multiStudent.get(i).get("Instructor"));
                                    imageLoader.displayImage(multiStudent.get(i).get("Photo"), inst_dp_cust);
                                    if (AppConfiguration.animation) {
                                        TransitionManager.beginDelayedTransition(inflate_multiple, mFade);
                                    }
                                    inflate_multiple.addView(view);
                                }
                            }

                        } else {
                            for (int i = 0; i < multiStudent.size(); i++) {
                                if (getActivity() == null)
                                    return;
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.d2_multiplestudent_nextschdl, null);
                                TextView detai_text;
                                CircleImageView inst_dp_cust;
                                detai_text = (TextView) view.findViewById(R.id.detai_text);
                                inst_dp_cust = (CircleImageView) view.findViewById(R.id.inst_DP);

                                detai_text.setText(multiStudent.get(i).get("ScheduleDate") + " - " +
                                        multiStudent.get(i).get("StudentName"));

                                imageLoader.displayImage(multiStudent.get(i).get("Photo"), inst_dp_cust);
                                if (AppConfiguration.animation) {
                                    TransitionManager.beginDelayedTransition(inflate_multiple, mFade);
                                }
                                inflate_multiple.addView(view);
                            }
                        }
                    }
                }
            }
            if (viewstatus == false) {
                singleStudent.setVisibility(View.VISIBLE);
                multipleStudent.setVisibility(View.GONE);

                err_msg.setText(Err_Msg);
                Log.d("Err_msg@@@@@@@@@@@@@@", Err_Msg);
                err_msg.setTextColor(Color.RED);
                err_msg.setGravity(Gravity.CENTER_HORIZONTAL);

                timing.setVisibility(View.INVISIBLE);
                inst_name.setVisibility(View.INVISIBLE);
                inst_dp.setVisibility(View.GONE);
                reschdl.setVisibility(View.INVISIBLE);
                st_name.setVisibility(View.GONE);
            }

        }

    }

    private class GetRemDetail extends AsyncTask<Void, Void, Void> {
//        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();*/
        }

        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("ReqFrom", "1");

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_RemainingLessons, param);
            try {
//                data_load = "False";

                JSONObject reader = new JSONObject(responseString);
                data_load = reader.getString("Success");
                if (data_load.toString().equalsIgnoreCase("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        lesson_type.add(jsonChildNode.getString("LessonType"));
                        count.add(jsonChildNode.getString("Count"));
                        AppConfiguration.CountArray = count;
                        Log.d("lessonArray", ""+ lesson_type);
                   }
                } else {
                    error_msg = reader.getString("Msg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
/*            if (pd != null) {
                pd.dismiss();
            }*/
            if (data_load.toString().equalsIgnoreCase("True"))
            {
                if (AppConfiguration.LAFitnessResult.equalsIgnoreCase("true")) {
                    if (!count.equals("0") || !count.equals("Past due")) {
                        if (lesson_type.size() > 0) {
                            for (int i = 0; i < lesson_type.size(); i++) {
                                if (getActivity() == null)
                                    return;
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.custom_remaining, null);
                                TextView text = (TextView) view.findViewById(R.id.text);
                                TextView tv_lesson_type = (TextView) view.findViewById(R.id.tv_lesson_type);


                                tv_lesson_type.setText(lesson_type.get(i).toString());
                                if (count.get(i).toString().contains("Past due")) {

                                    text.setText(Html.fromHtml(count.get(i)).toString());
                                    text.setTextColor(getResources().getColor(R.color.red));

                                } else {
                                    if(lesson_type.contains("")){
                                        text.setText("");
                                    }
                                    else {
                                        text.setText(Html.fromHtml(String.format(count.get(i).toString())));
                                    }
                                }
                                if (AppConfiguration.animation) {
                                    TransitionManager.beginDelayedTransition(remaining_lay, mFade);
                                }
                                remaining_lay.addView(view);
                            }
                        } else {
//                            remaining_title.setText(Html.fromHtml("Paid Lessons Remaining: <font color='red'> None</font>"));
                        }
                    } else {
                        singleStudent.setVisibility(View.VISIBLE);
                        multipleStudent.setVisibility(View.GONE);

                        err_msg.setText(Err_Msg);
                        err_msg.setTextColor(Color.RED);
                        err_msg.setGravity(Gravity.CENTER_HORIZONTAL);

                        timing.setVisibility(View.INVISIBLE);
                        inst_name.setVisibility(View.INVISIBLE);
                        inst_dp.setVisibility(View.GONE);
                        reschdl.setVisibility(View.INVISIBLE);
                        st_name.setVisibility(View.GONE);
                    }
                } else {
                    if (lesson_type.size() > 0) {
                        for (int i = 0; i < lesson_type.size(); i++) {
                            if (getActivity() == null)
                                return;
                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view = inflater.inflate(R.layout.custom_remaining, null);
                            TextView text = (TextView) view.findViewById(R.id.text);
                            TextView tv_lesson_type = (TextView) view.findViewById(R.id.tv_lesson_type);


                            tv_lesson_type.setText(lesson_type.get(i).toString());
                            if (count.get(i).toString().contains("Past due")) {

                                text.setText(Html.fromHtml(count.get(i)).toString());
                                text.setTextColor(getResources().getColor(R.color.red));

                            } else {
                                text.setText(Html.fromHtml(String.format(count.get(i).toString())));
                            }
                            if (AppConfiguration.animation) {
                                TransitionManager.beginDelayedTransition(remaining_lay, mFade);
                            }
                            remaining_lay.addView(view);
                        }
                    } else {
                        remaining_title.setText(Html.fromHtml("Paid Lessons Remaining: <font color='red'> None</font>"));
                    }
                }
            } else {
                remaining_title.setText(Html.fromHtml("Paid Lessons Remaining: <font color='red'> None</font>"));
            }
            showHideCheckinButton();
        }
    }

    LinearLayout remaining_lay;
    ArrayList<String> lesson_type = new ArrayList<String>();
    ArrayList<String> count = new ArrayList<String>();

    public void showHideCheckinButton() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GetCheckInListAsyncTask getCheckInListAsyncTask = new GetCheckInListAsyncTask(getActivity());
                    AppConfiguration.ShowHideScanProgramsList = getCheckInListAsyncTask.execute().get();
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (AppConfiguration.ShowHideScanProgramsList != null) {
                                if (AppConfiguration.ShowHideScanProgramsList.size() > 0 && AppConfiguration.ShowHideScanProgramsList.containsValue("1")) {
                                    btnCheckIn.setVisibility(View.VISIBLE);
                                }
                            } else {
                                btnCheckIn.setVisibility(View.GONE);
                            }
                            if (pd != null)
                                pd.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //  megha 13-04-2017
    public void showUnpaidClassInfom() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        if (getActivity() == null)
            return;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.pop_up_layout_unpaidclassinform, null);
        dialogBuilder.setView(dialogView);

        final TextView txt = (TextView) dialogView.findViewById(R.id.textView3);
        txt.setText(pastduemessage);
        final CardView btnok = (CardView) dialogView.findViewById(R.id.button_ok);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        btnok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }
    private class SchedulePageLoad extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
            schedulePageLoadList.clear();
        }

        @Override
        protected Void doInBackground(Void... params1) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("ScheduleType", "0");

            String responseString = WebServicesCall.RunScript(AppConfiguration.scheduleALessionPageLoad, params);
            try {
                JSONObject reader = new JSONObject(responseString);
                successCout = reader.getString("Success");
                if (successCout.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("LessonList");

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        pastdue = jsonChildNode.getString("pastdue").trim();
                        pastduemessage = jsonChildNode.getString("pastduemessage").trim();

                        hashmap.put("pastduemessage", pastduemessage);
                        hashmap.put("pastdue", pastdue);

                        AppConfiguration.PastDueForMessageCount = pastdue;
                        AppConfiguration.PastDuemessage = pastduemessage;
                        schedulePageLoadList.add(hashmap);
                        System.out.println("Schedule : " + schedulePageLoadList);
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
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

        }
    }
}
