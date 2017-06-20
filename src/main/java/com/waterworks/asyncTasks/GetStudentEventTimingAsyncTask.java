package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.R;
import com.waterworks.model.TokenStudentID;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

/**
 * Created by Harsh on 28-Apr-16.
 */
public class GetStudentEventTimingAsyncTask extends AsyncTask<Void, Void, String> {
    ProgressDialog pd;
    TokenStudentID tokenStudentID = null;
    Context mContext = null;

    public GetStudentEventTimingAsyncTask(Context context, TokenStudentID tokenStudentID){
        mContext = context;
        this.tokenStudentID = tokenStudentID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setMessage(mContext.getResources().getString(R.string.pleasewait));
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String, String > wsParam = new HashMap<String, String>();
        String responseString = null;

        wsParam.put("Token",tokenStudentID.getToken() );
        wsParam.put("studentid",tokenStudentID.getStudentID());

        try {
            responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetStudentEventTimingURL, wsParam);
            Log.e("Trophy Room : GetStudentEventTimingURL", responseString);
        }catch (Exception e){
            e.printStackTrace();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (pd != null) {
            pd.dismiss();
        }
    }
}