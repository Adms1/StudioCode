package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class CheckingAcAsyncTask extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;
        Context mContext;
        HashMap<String, String> param = new HashMap<String, String>();

    public CheckingAcAsyncTask(Context context, HashMap<String, String> param) {
        mContext = context;
        this.param = param;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.AddACHDetailsBySiteURL, param);
            Log.d("Bank responseString-", responseString);

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }