package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class StateListAsyncTask extends AsyncTask<Void, Void, String> {
    ProgressDialog pd;
    Context mContext;
    HashMap<String, String> param = new HashMap<String, String>();

    public StateListAsyncTask(Context context) {
        mContext = context;
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
            param.put("state", " ");
            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_StateList, param);
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