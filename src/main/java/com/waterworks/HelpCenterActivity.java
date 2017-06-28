package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressWarnings("deprecation")
public class HelpCenterActivity extends Activity {

    String TAG = "HelpCenter";
    LinearLayout ll_secondary_parent;
    Button btnSubmit;
    EditText edtDescription, edtStepReproduce;
    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> siteName = new ArrayList<String>();
    String description, stepReproduce;
    String successFeedback;
    String messageFeedback;

    Button btnSuggestChanges, btnProblemReport;
    int selectedButton = 1;
    Context mContext = this;
    Boolean isInternetPresent = false;
    String token, familyID;

    RelativeLayout rel_problem;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center_activity);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);
        ll_secondary_parent = (LinearLayout) findViewById(R.id.ll_secondary_parent);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        edtStepReproduce = (EditText) findViewById(R.id.edtStepReproduce);

        btnSuggestChanges = (Button) findViewById(R.id.btnSuggestChanges);
        btnProblemReport = (Button) findViewById(R.id.btnProblemReport);

        rel_problem = (RelativeLayout) findViewById(R.id.rel_problem);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        btnSuggestChanges.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btnProblemReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                description = edtDescription.getText().toString();
                stepReproduce = edtStepReproduce.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                } else {
                    isInternetPresent = Utility.isNetworkConnected(HelpCenterActivity.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new SuggestionOrProblemAsyncTask().execute();
                    }
                }
            }
        });
        // fetching header view
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.page_title);
        title.setText("Contact us");
        LinearLayout ll_ProgressReport, ll_ViewCertificate, ll_RibbonCount;
        ll_ProgressReport = (LinearLayout) view.findViewById(R.id.scdl_lsn);
        ll_ViewCertificate = (LinearLayout) view.findViewById(R.id.scdl_mkp);
        ll_RibbonCount = (LinearLayout) view.findViewById(R.id.waitlist);

        View vert_1 = (View) view.findViewById(R.id.vert_1);
        vert_1.setVisibility(View.GONE);
        ll_ProgressReport.setVisibility(View.GONE);

        TextView txt_2 = (TextView) view.findViewById(R.id.txt_2);
        TextView txt_3 = (TextView) view.findViewById(R.id.txt_3);

        txt_2.setText("Suggest Changes");
        txt_3.setText("Report a Problem");

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.menu_icon_new);

        Button relMenu = (Button) view.findViewById(R.id.returnStack);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_ViewCertificate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 1;
                txtTitle.setText(getResources().getString(R.string.suggest));
                rel_problem.setVisibility(View.GONE);

                btnSuggestChanges.setBackgroundColor(getResources().getColor(R.color.helpcenter_tabSelected));
                btnSuggestChanges.setTextColor(getResources().getColor(R.color.black));

                btnProblemReport.setBackgroundColor(getResources().getColor(R.color.text_blue));
                btnProblemReport.setTextColor(getResources().getColor(R.color.white));
            }
        });

        ll_RibbonCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 2;
                txtTitle.setText(getResources().getString(R.string.problem));
                //				edtDescription.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                rel_problem.setVisibility(View.VISIBLE);

                btnProblemReport.setBackgroundColor(getResources().getColor(R.color.helpcenter_tabSelected));
                btnProblemReport.setTextColor(getResources().getColor(R.color.black));

                btnSuggestChanges.setBackgroundColor(getResources().getColor(R.color.text_blue));
                btnSuggestChanges.setTextColor(getResources().getColor(R.color.white));
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


    class SuggestionOrProblemAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HelpCenterActivity.this);
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
                Toast.makeText(getApplicationContext(), "" + messageFeedback, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + messageFeedback, Toast.LENGTH_LONG).show();
            }


        }
    }


}

