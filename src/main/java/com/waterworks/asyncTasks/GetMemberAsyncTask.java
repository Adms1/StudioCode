package com.waterworks.asyncTasks;

import android.os.AsyncTask;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

/**
 * Created by Harsh on 04-Jul-16.
 */
public class GetMemberAsyncTask extends AsyncTask<Void, Void, String> {

    HashMap<String, String> param = null;

    public GetMemberAsyncTask(HashMap<String, String> param) {
        this.param = param;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String responseString = WebServicesCall.RunScript(AppConfiguration.swimCompititionConfirmationCalculation, param);
        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
