package com.waterworks;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.adapter.RemoveLessonAdapter;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class CancelSurvey extends Activity {

    EditText stoplessonreason, reconsider_reason;
    TextView returndate;
    LinearLayout llCommingBack, llEditDatePicker, llReconsiderRadio, llEditReason, llRecomentFriend, llEditReconsider;
    RadioGroup recommend, come_back, switch_time, reconsider;
    RadioButton rdbSwitchTimeYes, rdbSwitchTimeNo, rdbThinkComeBackYes, rdbThinkComeBackNo, rdbThinkComeBackThinking, rdbReconsiderYes, rdbReconsiderNo, rdbRecommendFriendYes, rdbRecommendFriendNo, rdbRecommendFriendThinkabout;
    String token, familyID, formatedString, successRelease;
    static final int DATE_DIALOG_ID = 0;
    private int mYear;
    private int mMonth;
    private int mDay;
    Calendar calendar;
    CardView submit;
    Context mContext = this;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelsurvey);
        setTitleBar();

        SharedPreferences prefs = AppConfiguration.getSharedPrefs(CancelSurvey.this);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        init();
        setListners();
    }

    public void init() {

        llCommingBack = (LinearLayout) findViewById(R.id.llCommingBack);
        llEditDatePicker = (LinearLayout) findViewById(R.id.llEditDatePicker);
        llReconsiderRadio = (LinearLayout) findViewById(R.id.llReconsiderRadio);
        llEditReason = (LinearLayout) findViewById(R.id.llEditReason);
        llRecomentFriend = (LinearLayout) findViewById(R.id.llRecomentFriend);
        llEditReconsider = (LinearLayout) findViewById(R.id.llEditReconsider);
        rdbSwitchTimeYes = (RadioButton) findViewById(R.id.rdbSwitchTimeYes);
        rdbSwitchTimeNo = (RadioButton) findViewById(R.id.rdbSwitchTimeNo);
        rdbThinkComeBackYes = (RadioButton) findViewById(R.id.rdbThinkComeBackYes);
        rdbThinkComeBackNo = (RadioButton) findViewById(R.id.rdbThinkComeBackNo);
        rdbThinkComeBackThinking = (RadioButton) findViewById(R.id.rdbThinkComeBackThinking);
        rdbReconsiderYes = (RadioButton) findViewById(R.id.rdbReconsiderYes);
        rdbReconsiderNo = (RadioButton) findViewById(R.id.rdbReconsiderNo);
        rdbRecommendFriendYes = (RadioButton) findViewById(R.id.rdbRecommendFriendYes);
        rdbRecommendFriendNo = (RadioButton) findViewById(R.id.rdbRecommendFriendNo);
        rdbRecommendFriendThinkabout = (RadioButton) findViewById(R.id.rdbRecommendFriendThinkabout);

        stoplessonreason = (EditText) findViewById(R.id.stoplessonreason);
        returndate = (TextView) findViewById(R.id.returndate);
        recommend = (RadioGroup) findViewById(R.id.recommend);
        reconsider_reason = (EditText) findViewById(R.id.reconsider_reason);
        come_back = (RadioGroup) findViewById(R.id.come_back);
        switch_time = (RadioGroup) findViewById(R.id.switch_time);
        reconsider = (RadioGroup) findViewById(R.id.reconsider);
        submit = (CardView) findViewById(R.id.submit);
    }

    public void setListners() {

        rdbRecommendFriendYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(reconsider_reason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbRecommendFriendYes.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbRecommendFriendYes.setButtonDrawable(R.drawable.r1);
                    submit.setVisibility(View.VISIBLE);
                } else {
                    rdbRecommendFriendYes.setChecked(false);
                    rdbRecommendFriendYes.setButtonDrawable(R.drawable.r0);
                    rdbRecommendFriendYes.clearAnimation();
                }
            }
        });

        rdbRecommendFriendNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(reconsider_reason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbRecommendFriendNo.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbRecommendFriendNo.setButtonDrawable(R.drawable.r1);

                    submit.setVisibility(View.VISIBLE);
                } else {
                    rdbRecommendFriendNo.setChecked(false);
                    rdbRecommendFriendNo.setButtonDrawable(R.drawable.r0);
                    rdbRecommendFriendNo.clearAnimation();
                }
            }
        });
        rdbRecommendFriendThinkabout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(reconsider_reason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbRecommendFriendThinkabout.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbRecommendFriendThinkabout.setButtonDrawable(R.drawable.r1);
                    submit.setVisibility(View.VISIBLE);
                } else {
                    rdbRecommendFriendThinkabout.setChecked(false);
                    rdbRecommendFriendThinkabout.setButtonDrawable(R.drawable.r0);
                    rdbRecommendFriendThinkabout.clearAnimation();
                }
            }
        });

        rdbReconsiderYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stoplessonreason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbReconsiderYes.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbReconsiderYes.setButtonDrawable(R.drawable.r1);
                    llEditReconsider.setVisibility(View.VISIBLE);
                    llRecomentFriend.setVisibility(View.VISIBLE);

                    rdbRecommendFriendYes.setChecked(false);
                    rdbRecommendFriendNo.setChecked(false);
                    rdbRecommendFriendThinkabout.setChecked(false);
                } else {
                    rdbReconsiderYes.setChecked(false);
                    rdbReconsiderYes.setButtonDrawable(R.drawable.r0);
                    rdbReconsiderYes.clearAnimation();
                }

            }
        });

        rdbReconsiderNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stoplessonreason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbReconsiderNo.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbReconsiderNo.setButtonDrawable(R.drawable.r1);
                    llEditReconsider.setVisibility(View.GONE);
                    llRecomentFriend.setVisibility(View.VISIBLE);
                } else {
                    rdbReconsiderNo.setChecked(false);
                    rdbReconsiderNo.setButtonDrawable(R.drawable.r0);
                    rdbReconsiderNo.clearAnimation();
                }
            }
        });

        rdbThinkComeBackYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stoplessonreason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbThinkComeBackYes.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbThinkComeBackYes.setButtonDrawable(R.drawable.r1);

                    llEditDatePicker.setVisibility(View.VISIBLE);
                    llReconsiderRadio.setVisibility(View.GONE);
                    llRecomentFriend.setVisibility(View.VISIBLE);
                    llEditReconsider.setVisibility(View.GONE);

                    rdbReconsiderYes.setChecked(false);
                    rdbReconsiderNo.setChecked(false);
                    rdbRecommendFriendYes.setChecked(false);
                    rdbRecommendFriendNo.setChecked(false);
                    rdbRecommendFriendThinkabout.setChecked(false);
                    submit.setVisibility(View.GONE);

                } else {
                    rdbThinkComeBackYes.setChecked(false);
                    rdbThinkComeBackYes.setButtonDrawable(R.drawable.r0);
                    rdbThinkComeBackYes.clearAnimation();
                }
            }
        });

        rdbThinkComeBackNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stoplessonreason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbThinkComeBackNo.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbThinkComeBackNo.setButtonDrawable(R.drawable.r1);
                    llReconsiderRadio.setVisibility(View.VISIBLE);
                    llEditDatePicker.setVisibility(View.GONE);
                    llRecomentFriend.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);

                } else {
                    rdbThinkComeBackNo.setChecked(false);
                    rdbThinkComeBackNo.setButtonDrawable(R.drawable.r0);
                    rdbThinkComeBackNo.clearAnimation();
                }
            }
        });

        rdbThinkComeBackThinking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stoplessonreason.getWindowToken(), 0);
                if (isChecked) {
//                    rdbThinkComeBackThinking.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    rdbThinkComeBackThinking.setButtonDrawable(R.drawable.r1);
                    llReconsiderRadio.setVisibility(View.VISIBLE);
                    llEditDatePicker.setVisibility(View.GONE);
                    llRecomentFriend.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                } else {
                    rdbThinkComeBackThinking.setChecked(false);
                    rdbThinkComeBackThinking.setButtonDrawable(R.drawable.r0);
                    rdbThinkComeBackThinking.clearAnimation();
                }
            }
        });

        rdbSwitchTimeNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stoplessonreason.getWindowToken(), 0);
                if (isChecked) {
                    rdbSwitchTimeNo.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    submit.setVisibility(View.GONE);
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(CancelSurvey.this);
                } else {
                    rdbRecommendFriendYes.setChecked(false);
                    rdbRecommendFriendNo.setChecked(false);
                    rdbRecommendFriendThinkabout.setChecked(false);
                    rdbReconsiderYes.setChecked(false);
                    rdbReconsiderNo.setChecked(false);
                    rdbThinkComeBackYes.setChecked(false);
                    rdbThinkComeBackNo.setChecked(false);
                    rdbThinkComeBackThinking.setChecked(false);

                    rdbThinkComeBackThinking.isChecked();
                }
            }
        });

        rdbSwitchTimeYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(stoplessonreason.getWindowToken(), 0);
                if (isChecked) {
                    rdbSwitchTimeYes.setButtonDrawable(R.drawable.custom_radiobutton_orange);
                    llEditReason.setVisibility(View.GONE);
                    llCommingBack.setVisibility(View.GONE);
                    llEditDatePicker.setVisibility(View.GONE);
                    llReconsiderRadio.setVisibility(View.GONE);
                    llRecomentFriend.setVisibility(View.GONE);
                    llEditReconsider.setVisibility(View.GONE);

                    rdbRecommendFriendYes.clearAnimation();
                    rdbRecommendFriendNo.clearAnimation();
                    rdbRecommendFriendThinkabout.clearAnimation();
                    rdbReconsiderYes.clearAnimation();
                    rdbReconsiderNo.clearAnimation();
                    rdbThinkComeBackYes.clearAnimation();
                    rdbThinkComeBackNo.clearAnimation();
                    rdbThinkComeBackThinking.clearAnimation();
                    rdbSwitchTimeNo.clearAnimation();
                    submit.setVisibility(View.VISIBLE);
                }

            }
        });

        returndate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(0);
            }
        });

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (rdbSwitchTimeYes.isChecked()) {
                    isInternetPresent = Utility.isNetworkConnected(CancelSurvey.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new ReleaseAsyncTask().execute();
                    }
                } else {
                    if (validation()) {
                        isInternetPresent = Utility.isNetworkConnected(CancelSurvey.this);
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new ReleaseAsyncTask().execute();
                        }
                    }
                }

            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
//        12-06-2017 megha
        Intent iback = new Intent(CancelSurvey.this, CancelRemoveLessonActivity.class);
        iback.putExtra("from", "All");
        iback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iback);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:

                calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                calendar.set(mYear, mMonth, mDay);
                Log.d("Date==", mYear + "=" + mMonth + "=" + mDay);

                DatePickerDialog dialog = new DatePickerDialog(this, dateListener, mYear, mMonth, mDay);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return dialog;
        }
        return null;
    }


    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int yr, int monthOfYear,
                              int dayOfMonth) {

            mDay = dayOfMonth;
            mYear = yr;
            mMonth = monthOfYear;
            String m, d;
            m = "" + (mMonth + 1);
            if (m.length() == 1) {
                m = "0" + m;
            }
            d = "" + mDay;
            if (d.length() == 1) {
                d = "0" + d;
            }
            String date = m + "/" + d + "/" + mYear;
            returndate.setText(date);
        }
    };


    @Override
    @SuppressWarnings("deprecation")
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    public boolean validation() {
        boolean value = false;
        if (!get_radio_index(switch_time).contains("-1")) {
            if (stoplessonreason.getText().toString().trim().length() > 0) {
                if (!get_radio_index(come_back).contains("-1")) {
                    if (!get_radio_index(reconsider).contains("-1")) {
                        if ((llEditReconsider.getVisibility() == View.VISIBLE && reconsider_reason.getText().toString().trim().length() > 0) ||
                                llEditReconsider.getVisibility() == View.GONE) {
                            if ((llEditDatePicker.getVisibility() == View.VISIBLE && returndate.getText().toString().trim().length() > 0) ||
                                    llEditDatePicker.getVisibility() == View.GONE) {
                                if (!get_radio_index(recommend).contains("-1")) {
                                    value = true;
                                } else {
                                    ping("Please select Recommend option");
                                }
                            } else {
                                if (llEditDatePicker.getVisibility() == View.VISIBLE && returndate.getText().toString().trim().length() == 0) {
                                    ping("Please select Date");
                                }
                            }
                        } else {
                            if ((llEditReconsider.getVisibility() == View.VISIBLE && reconsider_reason.getText().toString().trim().length() < 0)) {
                                ping("Please write Reconsideration");
                            }
                        }
                    } else {
                        ping("Please select Reconsider option");
                    }
                } else {
                    ping("Please select ComeBack option");
                }
            } else {
                ping("Please write Reason");
            }
        } else {
            ping("Please select Switch Time option");
        }
        return value;
    }

    public void ping(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    class ReleaseAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CancelSurvey.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            releasingClass();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successRelease.equalsIgnoreCase("true")) {
                //06-07-2017 megha
                if (rdbSwitchTimeNo.isChecked()) {
                    Intent i = new Intent(mContext, DashBoardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("POS", 0);
                    startActivity(i);
//                    finish();
                    Toast.makeText(getApplicationContext(), "Class time successfully removed!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CancelSurvey.this, CancelLessonFragment.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Class time successfully removed!", Toast.LENGTH_LONG).show();
                }
            } else {
            }
        }
    }
    public String get_radio_index(RadioGroup radioButtonGroup) {
        String value = "";
        int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
        View radioButton = radioButtonGroup.findViewById(radioButtonID);
        int idx = radioButtonGroup.indexOfChild(radioButton);

        if (idx == 0) {
            value = "Yes";
        } else if (idx == 1) {
            value = "No";
        } else if (idx == 2) {
            value = "Thinking about it";
        }
        return value;
    }

    public void releasingClass() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("FamilyID", familyID);

        String[] EndDtArr = CancelRemoveLessonActivity.EndDt.split(",");
        for (int i = 0; i < EndDtArr.length; i++) {
            EndDtArr[i] = EndDtArr[i] + "|" + AppConfiguration.ReleaseIDRemoveLesson.get(i);
        }

        param.put("RequestedEndDate", Arrays.toString(EndDtArr));//CancelRemoveLessonActivity.EndDt
        Log.d("EndDtArr", "" + EndDtArr);
        param.put("RequestedID", CancelRemoveLessonActivity.RequestedID);
        param.put("SwitchTime", get_radio_index(switch_time));
        param.put("StopLessonReason", stoplessonreason.getText().toString());

        param.put("ComeBackThink", get_radio_index(come_back));
        param.put("Reconsider", get_radio_index(reconsider));
        param.put("ReconsiderReason", reconsider_reason.getText().toString());

        param.put("ReturnDate", returndate.getText().toString());
        param.put("RecommendTofriend", get_radio_index(recommend));

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN
                + AppConfiguration.RemoveLesson_Request_ReleaseAllClass_Survey, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successRelease = reader.getString("Success");
            if (successRelease.toString().equals("True")) {
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewDialog {

        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_cancel_survey);

            TextView txtClose = (TextView) dialog.findViewById(R.id.txtClose);
            txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    llEditReason.setVisibility(View.VISIBLE);
                    llCommingBack.setVisibility(View.VISIBLE);
                }
            });

            CardView btnYes = (CardView) dialog.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    llEditReason.setVisibility(View.VISIBLE);
                    llCommingBack.setVisibility(View.VISIBLE);
                }
            });

            dialog.show();
        }
    }

    public void setTitleBar() {
        View view = (View) findViewById(R.id.actionBar);
        TextView txtTitleText = (TextView) view.findViewById(R.id.action_title);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        txtTitleText.setText("Remove Class Time");
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();
            }

        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(CancelSurvey.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
    }
}
