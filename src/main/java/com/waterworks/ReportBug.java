package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AndroidBug5497Workaround;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Rakesh Tiwari ADMS on 2/1/2016.
 */
public class ReportBug extends Activity implements View.OnTouchListener {
    RelativeLayout title_black;
    Button returnStack, btnDoneIssue, btnDoneIssueDetail;
    CardView btnSubmit;
    TextView page_title;
    String token, familyID;
    String TAG = "ReportBug";
    String description, stepReproduce, subjectLine;
    String successFeedback;
    String messageFeedback;
    EditText edtSubject, edtIssue, edtIssueDetail;
    Button btnSuggestChanges, btnProblemReport;
    int selectedButton = 2;
    Boolean isInternetPresent = false;
    Context mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_bug);
        //getting token
//        AndroidBug5497Workaround.assistActivity(this);
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        View view = (View) findViewById(R.id.header);
        title_black = (RelativeLayout) view.findViewById(R.id.title_black);
        title_black.setVisibility(View.GONE);
        returnStack = (Button) view.findViewById(R.id.returnStack);
        page_title = (TextView) view.findViewById(R.id.page_title);
        btnDoneIssue = (Button) findViewById(R.id.btnDoneIssue);
        btnDoneIssueDetail = (Button) findViewById(R.id.btnDoneIssueDetail);
        edtIssue = (EditText) findViewById(R.id.edtIssue);
        edtIssueDetail = (EditText) findViewById(R.id.edtIssueDetail);
        page_title.setText("Report a Bug");

        btnSubmit = (CardView) findViewById(R.id.btnSend);
        edtSubject = (EditText) findViewById(R.id.edtSubject);
        edtSubject.setOnTouchListener(this);
        edtIssue.setOnTouchListener(this);
        edtIssueDetail.setOnTouchListener(this);
        btnSuggestChanges = (Button) findViewById(R.id.btnSuggestChanges);
        btnProblemReport = (Button) findViewById(R.id.btnProblemReport);
        returnStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                validation();
                description = edtIssue.getText().toString();
                stepReproduce = edtIssueDetail.getText().toString();
                subjectLine = edtSubject.getText().toString();
                if (description.isEmpty() || stepReproduce.isEmpty() || subjectLine.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please complete all required fields..", Toast.LENGTH_LONG).show();
                } else {
                    isInternetPresent = Utility.isNetworkConnected(ReportBug.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new SuggestionOrProblemAsyncTask().execute();
                    }

                }
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ReportBug.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        btnDoneIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtIssue.getText().length() == 0){
                    //nothing happens
                }else {
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    edtIssueDetail.requestFocus();
                }
            }
        });

        btnDoneIssueDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtIssueDetail.getText().length() == 0){
                    //nothing happens
                }else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

    //===================================== feedback =======================================

    public void submittingSuggestionProblem() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("subjectline", "" + subjectLine);
        param.put("Description", "" + description);
        param.put("Type", "" + selectedButton);
        param.put("StepReproduce", "" + stepReproduce);

        String responseString = WebServicesCall.RunScript(AppConfiguration.HelpURLNEW, param);
        readAndParseJSONFeedback(responseString);

    }

    public void readAndParseJSONFeedback(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successFeedback = reader.getString("Success");
            if (successFeedback.toString().equals("True")) {
                JSONArray jsonMainNode;

                jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageFeedback = jsonChildNode.getString("Msg");

                }
            } else {
                JSONArray jsonMainNode;

                jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageFeedback = jsonChildNode.getString("Msg");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v == edtIssue) {
            edtIssue.setBackgroundResource(R.drawable.gray_border);
        }
        if (v == edtIssueDetail) {
            edtIssueDetail.setBackgroundResource(R.drawable.gray_border);
        }
        if (v == edtSubject) {
            edtSubject.setBackgroundResource(R.drawable.gray_border);
        }

        return false;
    }

    class SuggestionOrProblemAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ReportBug.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            submittingSuggestionProblem();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successFeedback.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), "" + "Your bug report has been sent successfully.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + messageFeedback, Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void validation() {

        if (edtIssue.getText().toString().trim().length() == 0) {
            edtIssue.setBackgroundResource(R.drawable.error_border);
        }
        if (edtIssueDetail.getText().toString().trim().length() == 0) {
            edtIssueDetail.setBackgroundResource(R.drawable.error_border);
        }
        if (edtSubject.getText().toString().trim().length() == 0) {
            edtSubject.setBackgroundResource(R.drawable.error_border);
        }
    }
}

