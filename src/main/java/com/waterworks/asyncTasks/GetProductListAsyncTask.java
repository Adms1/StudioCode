package com.waterworks.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class GetProductListAsyncTask extends AsyncTask<Void, Void, String> {
    ProgressDialog pd;
    HashMap<String, String> param = new HashMap<String, String>();
    Activity activity;

    public GetProductListAsyncTask(Activity activity, HashMap<String, String> param) {
        this.activity = activity;
        this.param = param;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(activity);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = null;
            try {
                responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_InhouseProductDetailsURL, param);
                Log.d("card responseString-", responseString);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }
    }