package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.waterworks.R;
import com.waterworks.model.TokenStudentID;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

/**
 * Created by Harsh on 28-Apr-16.
 */
public class RemainingSkillSetsAsyncTask extends AsyncTask<Void, Void, String> {
    TokenStudentID tokenStudentID = null;
    Context mContext = null;

    public RemainingSkillSetsAsyncTask(Context context, TokenStudentID tokenStudentID){
        mContext = context;
        this.tokenStudentID = tokenStudentID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String, String > wsParam = new HashMap<String, String>();
        String responseString = null;

        wsParam.put("Token",tokenStudentID.getToken() );
        wsParam.put("studentid",tokenStudentID.getStudentID());

        try {
            responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetStudentRemainingLevelprereqURL, wsParam);
        }catch (Exception e){
            e.printStackTrace();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}
