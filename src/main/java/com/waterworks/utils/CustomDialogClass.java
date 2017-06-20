package com.waterworks.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.asyncTasks.RemovePaymentAccoutAsyncTask;
import com.waterworks.manageProfile.AddPaymentAccount;
import com.waterworks.sheduling.BuyMoreOrderSummary;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button btnYes, btnGoBack;
    public String msg;
    public TextView txtClose;
    RemovePaymentAccoutAsyncTask removePaymentAccoutAsyncTask = null;
    HashMap<String, String> hashMap = new HashMap<>();
    ProgressDialog progressDialog;

    public CustomDialogClass(Activity a, HashMap<String, String> hashMap) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.hashMap = hashMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_remove_payment_account);

        btnYes = (Button) findViewById(R.id.btnYes);
        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        txtClose = (TextView) findViewById(R.id.txtClose);

        btnYes.setOnClickListener(this);
        btnGoBack.setOnClickListener(this);
        txtClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnYes:
                sendRemoveRequest();
                c.finish();
                break;

            case R.id.btnGoBack:
                dismiss();
                break;

            case R.id.txtClose:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }

    public void sendRemoveRequest() {
        progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    removePaymentAccoutAsyncTask = new RemovePaymentAccoutAsyncTask(hashMap);
                    String s = removePaymentAccoutAsyncTask.execute().get();
                    readAndParseCardAndCheckingRemoveJSON(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void readAndParseCardAndCheckingRemoveJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if (success.equalsIgnoreCase("True")) {
                msg = reader.getString("Msg");
                c.runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Utility.ping(c.getApplicationContext(), msg);
                        c.finish();
                    }
                });
            } else {
                c.runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Utility.ping(c.getApplicationContext(), msg);
                        c.finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}