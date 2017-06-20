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
public class StudentProgressLevelSkillListAsyncTask extends AsyncTask<Void, Void, String> {
    String responseString = null;
    Context mContext = null;
    TokenStudentID tokenStudentID = null;


    public StudentProgressLevelSkillListAsyncTask(Context context, TokenStudentID tokenStudentID){
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
        wsParam.put("Token",tokenStudentID.getToken());
        wsParam.put("studentid",tokenStudentID.getStudentID());

        try {
            responseString = WebServicesCall.RunScript(AppConfiguration.GetStudentLevelprereqURL, wsParam);
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
