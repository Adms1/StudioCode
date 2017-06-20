package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class RemovePaymentAccoutAsyncTask extends AsyncTask<Void, Void, String> {
    HashMap<String, String> param = new HashMap<String, String>();
    String TAG = "RemovePaymentAccoutAsyncTask";

    public RemovePaymentAccoutAsyncTask(HashMap<String, String> param) {
        this.param = param;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = null;
            try {
                responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.DeletePaymentDetailsBySiteURL, param);
                Log.d(TAG, responseString);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }