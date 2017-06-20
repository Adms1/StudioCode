package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.CancelLessonFragmentAdapter;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;


@SuppressWarnings("deprecation")
public class CancelLessonFragment extends Activity {

    public CancelLessonFragment() {

    }

    private static String TAG = "Cancel Lesson";
    Button relMenu;
    Boolean isInternetPresent = false;
    ArrayList<String> Date, Day, Time, Student, Instructor, Attendance,
            LessonType, Count, Comment, NewDate, NewTime, HiddenFeildID, HiddenFeildValue, wu_lessoncount,
            CancelEnable, Abbr, temp, tempComment, Wu_photo, MissingClass;
    public HashMap<String, String> hashLastPaidClass = null;
    public static ArrayList<String> RemoveClassList, CancelID;
    //	LinearLayout ll_cl_data_body;
    ListView lv_cl_data;
    String message, formatedString;
    String data_load = "False";
    ProgressBar progres_bar;
    String token, familyID;
    TextView tv_cl_body_title1, tv_cl_body_title2;
    Button submit_cancels;
    int position = 0;
    LinearLayout cancel_less_lay, cancel_info_lay, cancel_permanent_lay, cancel_makeUp_lay, optionLay, ll_progres_bar;
    RelativeLayout ll_cl_body;
    TextView text_permanentlyCancel;
    Animation animBounce, animSlidup;
    private CancelLessonData cancelLessonData = null;
    public static Button btn_cancel_lsn;
    private boolean buttonPressed = false;
    String integervalue;
    public static int red_disable = R.color.red_disable;
    public static int red = R.color.red;
    public static int white = R.color.white;
    public static int lightGrey = R.color.gray_light;
    Context mContext = this;
    String listStatus = "load";
    String cancelId;
    CancelLessonFragmentAdapter thadapter = null;

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        ConstantData.destroyed = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cancel_lesson_new_d2);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        btn_cancel_lsn = (Button) findViewById(R.id.btn_cancel_lsn);
        Initialization();

        isInternetPresent = Utility
                .isNetworkConnected(mContext);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    cancelLessonData = new CancelLessonData();
                    cancelLessonData.execute();
                }
            }
        }, 500);

        SpannableString ss = new SpannableString(getResources().getString(R.string.cancellesson_title1));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(mContext, CancelRemoveLessonActivity.class);
                i.putExtra("from", "All");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        };
        ss.setSpan(clickableSpan, 72, 83, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_cl_body_title1.setText(ss);
        tv_cl_body_title1.setMovementMethod(LinkMovementMethod.getInstance());


        tv_cl_body_title2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // custom dialog
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtTitle.setText(getResources().getString(R.string.info));

                Button dialogButton = (Button) dialog.findViewById(R.id.btnClose);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        cancel_makeUp_lay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        text_permanentlyCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, CancelRemoveLessonActivity.class);
                i.putExtra("from", "All");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
//               finish ();
            }
        });
        cancel_permanent_lay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, CancelRemoveLessonActivity.class);
                i.putExtra("from", "All");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        cancel_info_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtTitle.setText(getResources().getString(R.string.info));

                Button dialogButton = (Button) dialog.findViewById(R.id.btnClose);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        cancel_less_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                optionLay.setVisibility(View.GONE);
                ll_cl_body.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (cancelLessonData.getStatus() == AsyncTask.Status.RUNNING) {
            cancelLessonData.cancel(true);
        }
        overridePendingTransition(R.animator.slide_right_out, R.anim.slide_in_left);
        Intent intentDashboard = new Intent(CancelLessonFragment.this, DashBoardActivity.class);
        intentDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentDashboard);
        finish();
    }
    private void Initialization() {
        text_permanentlyCancel = (TextView) findViewById(R.id.text_permanentlyCancel);
        cancel_makeUp_lay = (LinearLayout) findViewById(R.id.cancel_makeUp_lay);
        cancel_permanent_lay = (LinearLayout) findViewById(R.id.cancel_permanent_lay);
        cancel_info_lay = (LinearLayout) findViewById(R.id.cancel_info_lay);
        cancel_less_lay = (LinearLayout) findViewById(R.id.cancel_less_lay);
        optionLay = (LinearLayout) findViewById(R.id.optionLay);
        ll_cl_body = (RelativeLayout) findViewById(R.id.ll_cl_body);
        progres_bar = (ProgressBar) findViewById(R.id.progres_bar);
        ll_progres_bar = (LinearLayout) findViewById(R.id.ll_progres_bar);
        tv_cl_body_title1 = (TextView) findViewById(R.id.tv_cl_body_title1);
        tv_cl_body_title2 = (TextView) findViewById(R.id.tv_cl_body_title2);
        submit_cancels = (Button) findViewById(R.id.submit_cancels);
        ll_progres_bar.setVisibility(View.VISIBLE);
        relMenu = (Button) findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lv_cl_data = (ListView) findViewById(R.id.lv_cl_data);
        Date = new ArrayList<String>();
        Day = new ArrayList<String>();
        Time = new ArrayList<String>();
        Student = new ArrayList<String>();
        Instructor = new ArrayList<String>();
        Wu_photo = new ArrayList<String>();
        Attendance = new ArrayList<String>();
        LessonType = new ArrayList<String>();
        Count = new ArrayList<String>();
        Comment = new ArrayList<String>();
        NewDate = new ArrayList<String>();
        NewTime = new ArrayList<String>();
        HiddenFeildID = new ArrayList<String>();
        HiddenFeildValue = new ArrayList<String>();
        wu_lessoncount = new ArrayList<String>();
        CancelID = new ArrayList<String>();
        CancelEnable = new ArrayList<String>();
        MissingClass = new ArrayList<>();
        Abbr = new ArrayList<String>();
        CancelLessonFragmentAdapter.RemoveClassList = new ArrayList<String>();
        //		ll_cl_data_body.setVisibility(View.GONE);
        lv_cl_data.setVisibility(View.GONE);
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
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
    public static int mselecteditem;
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

        lv_cl_data.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv_cl_data.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mselecteditem = position;
                view.setSelected(true);
            }
        });
        btn_cancel_lsn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hashLastPaidClass = new HashMap<String, String>();
                get_final_Array();
                boolean askUserForMakeupDelete = false;
                for (String comment : tempComment) {
                    if (comment.contains("Makeup Used")) {
                        askUserForMakeupDelete = true;
                    }
                }
                if (temp.size() > 0) {

                    AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(mContext);
                    alertdialogbuilder2.setTitle("Cancel lesson");
                    alertdialogbuilder2.setIcon(getResources().getDrawable(R.drawable.alerticon));
                    try {
                        if (askUserForMakeupDelete) {
                            alertdialogbuilder2
                                    .setMessage("Makeup lessons cannot be rescheduled. If you choose to cancel this lesson, you will lose the makeup class.")
                                    .setCancelable(false)
                                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            buttonPressed = true;
                                            new CancelClass().execute();
                                        }
                                    })
                                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alertDialog2 = alertdialogbuilder2.create();
                            alertDialog2.show();
                        } else {
                            alertdialogbuilder2
                                    .setMessage("Are you sure want to cancel " + temp.size() + " selected " + (temp.size() > 1 ? "classes?" : "class?"))
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            buttonPressed = true;
                                            new CancelClass().execute();
                                        }
                                    })
                                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alertDialog2 = alertdialogbuilder2.create();
                            alertDialog2.show();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Please select at least one class.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void get_final_Array() {
        temp = new ArrayList<String>();
        tempComment = new ArrayList<>();
        for (int i = 0; i < CancelLessonFragmentAdapter.RemoveClassList.size(); i++) {
            if (CancelLessonFragmentAdapter.RemoveClassList.get(i).equals("True")) {

                if (HiddenFeildValue.get(i).equalsIgnoreCase("2") || HiddenFeildValue.get(i).equalsIgnoreCase("3")) {
                    integervalue = "4";
                } else if (HiddenFeildValue.get(i).equalsIgnoreCase("4")) {
                    integervalue = "6";
                } else if (HiddenFeildValue.get(i).equalsIgnoreCase("0") || HiddenFeildValue.get(i).equalsIgnoreCase("1")) {
                    integervalue = "0";
                }
                String value;
                value = CancelID.get(i) + "|" + integervalue;
                temp.add(value);
                tempComment.add(Comment.get(i));
            }
        }
        formatedString = temp.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();
        Log.e("final String", formatedString);
    }

    @Override
    public void onPause() {
        super.onPause();
        CancelLessonFragment.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressWarnings("unused")
    private class Cancel_Request extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
        }

        @Override
        protected Void doInBackground(Void... params1) {
            // TODO Auto-generated method stub

            get_final_Array();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("FamilyID", familyID);
            params.put("strCancelID", formatedString);

            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.RemoveLesson_PageLoad_FillData_SelectedID, params);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            progres_bar.setVisibility(View.GONE);
            ll_progres_bar.setVisibility(View.GONE);
        }
    }

    private class CancelLessonData extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(mContext);

        }

        @Override
        protected Void doInBackground(Void... params1) {
            // TODO Auto-generated method stub
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Token", token);
            params.put("FamilyID", familyID);

            String responseString = WebServicesCall.RunScript(AppConfiguration.cancelLessonLoad, params);

            readAndParseJSON(responseString);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            progres_bar.setVisibility(View.GONE);
            ll_progres_bar.setVisibility(View.GONE);

            if (data_load.toString().equals("True")) {
                if (!Date.isEmpty()) {
                    //				ll_cl_data_body.setVisibility(View.VISIBLE);
                    lv_cl_data.setVisibility(View.VISIBLE);
                    lv_cl_data.setAdapter(
                            new CancelLessonFragmentAdapter(mContext.getApplicationContext(), Date, Day,
                                    Time, Student, Instructor, Attendance,
                                    LessonType, Count, Comment, NewDate, NewTime, HiddenFeildID, HiddenFeildValue,
                                    CancelID, CancelEnable, Abbr, CancelLessonFragmentAdapter.RemoveClassList, Wu_photo
                                    , MissingClass, wu_lessoncount));
                } else {
                }
            } else {
                lv_cl_data.setVisibility(View.GONE);
                Toast.makeText(mContext, "You do not currently have any classes to cancel.", Toast.LENGTH_SHORT).show();
                if (buttonPressed) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }
            if (listStatus.equalsIgnoreCase("load")) {

            } else {
                Toast.makeText(mContext, "Your classes have been successfully cancelled.!!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            data_load = reader.getString("Success");
            if (data_load.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    Date.add(jsonChildNode.getString("Date"));
                    Day.add(jsonChildNode.getString("Day"));
                    Time.add(jsonChildNode.getString("StartTime"));
                    Student.add(jsonChildNode.getString("Student"));
                    Instructor.add(jsonChildNode.getString("Instructor"));

                    String temp_photo = "";
                    temp_photo = jsonChildNode.getString("wu_photo");

                    if (temp_photo.trim().contains("~")) {
                        temp_photo = temp_photo.replace("~", "");
                        if (temp_photo.trim().contains(" ")) {
                            temp_photo = AppConfiguration.PhotoDomain + temp_photo.replace(" ", "%20");
                            System.out.println("Pic URL" + temp_photo);
                        } else {
                            temp_photo = AppConfiguration.PhotoDomain + temp_photo;
                            System.out.println("Pic URL" + temp_photo);
                        }
                    }

                    Wu_photo.add(temp_photo);

                    Attendance.add(jsonChildNode.getString("Attendence"));
                    LessonType.add(jsonChildNode.getString("LessonType"));
                    Count.add(jsonChildNode.getString("Counts"));
                    Comment.add(jsonChildNode.getString("Comments"));
                    NewDate.add(jsonChildNode.getString("NewDate"));
                    NewTime.add(jsonChildNode.getString("NewTime"));
                    HiddenFeildID.add(jsonChildNode.getString("HiddenFeildID"));
                    HiddenFeildValue.add(jsonChildNode.getString("HiddenFeildValue"));
                    CancelID.add(jsonChildNode.getString("CancelID"));
                    CancelEnable.add(jsonChildNode.getString("CancelEnable"));
                    wu_lessoncount.add(jsonChildNode.getString("wu_lessoncount"));
                    MissingClass.add(jsonChildNode.getString("MissingClass"));
                    Abbr.add(jsonChildNode.getString("ABBR"));
                    CancelLessonFragmentAdapter.RemoveClassList.add("False");
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                msg = jsonChildNode.getString("Msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String msg;

    private class CancelClass extends AsyncTask<Integer, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
            progres_bar.setVisibility(View.VISIBLE);
            ll_progres_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... param) {
            String responseString = "";
            for (int i = 0; i < temp.size(); i++) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Token", token);
                params.put("FamilyID", familyID);
                params.put("CancelClass", temp.get(i));
                responseString = WebServicesCall.RunScript(AppConfiguration.cancelClass, params);
            }
            readAndParseJSON_Single(responseString);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            listStatus = "cancel";

            if (data_load.toString().equals("True")) {
                System.out.println("View Created : " + position);
                if (hashLastPaidClass.containsValue("0")) {
                    AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(mContext);
                    alertdialogbuilder2.setTitle("Cancel lesson");
                    alertdialogbuilder2.setIcon(getResources().getDrawable(R.drawable.alerticon));

                    alertdialogbuilder2
                            .setMessage("One of the lessons you are attempting to cancel is the last paid class in your package. You cannot cancel the last paid class at this time. Please renew your lessons or release your future schedule, then submit your cancellation.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog2 = alertdialogbuilder2.create();
                    alertDialog2.show();
                } else {
                    Initialization();
                    TransitionDrawable transition = (TransitionDrawable) CancelLessonFragment.btn_cancel_lsn.getBackground();
                    transition.reverseTransition(500);
                    new CancelLessonData().execute();
                    Log.e(TAG, "Your classes have been successfully cancelled.!!");
                }
            } else {
            }
        }
    }


    public void readAndParseJSON_Single(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            data_load = reader.getString("Success");
            hashLastPaidClass.put("continues", reader.getString("continues"));
            if (data_load.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                message = jsonChildNode.getString("Msg");
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
                message = jsonChildNode.getString("Msg");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
