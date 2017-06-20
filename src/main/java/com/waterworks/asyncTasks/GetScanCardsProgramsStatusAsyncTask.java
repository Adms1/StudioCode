package com.waterworks.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class GetScanCardsProgramsStatusAsyncTask extends AsyncTask<Void, Void, String> {
    Context context;
    HashMap<String, String> param;

    public GetScanCardsProgramsStatusAsyncTask(Context context, HashMap<String, String> param) {
        this.context = context;
        this.param = param;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.ScanCardsPrograms,
                    param);
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }