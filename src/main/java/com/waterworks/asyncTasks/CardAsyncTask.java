package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class CardAsyncTask extends AsyncTask<Void, Void, String> {
    ProgressDialog pd;
    Context mContext;
    HashMap<String, String> param = new HashMap<String, String>();

    public CardAsyncTask(Context context, HashMap<String, String> param) {
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
            String responseString = null;
            try {
                responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.AddCardDetailsBySiteURL, param);
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