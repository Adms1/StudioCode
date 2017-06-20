package com.waterworks.asyncTasks;

import android.os.AsyncTask;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class LoadCheckboxListAsyncTask extends AsyncTask<Void, Void, String> {

    HashMap<String, String> param = new HashMap<>();

    public LoadCheckboxListAsyncTask(HashMap<String, String> param) {
        this.param = param;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String responseString = WebServicesCall.RunScript(AppConfiguration.swimCompititionStep4URL, param);
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}