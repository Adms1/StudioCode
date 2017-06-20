package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class SiteListForFamilyPaymentsAsyncTask extends AsyncTask<Void, Void, String> {
    ProgressDialog pd;
    HashMap<String, String> param = new HashMap<String, String>();
    String token;

    public SiteListForFamilyPaymentsAsyncTask(String token) {
        this.token = token;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        param.put("Token", token);
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_SiteListForFamilyPayments, param);
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}