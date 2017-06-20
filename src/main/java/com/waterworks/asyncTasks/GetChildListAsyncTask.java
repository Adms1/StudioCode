package com.waterworks.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

import com.waterworks.ProgressReportActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class GetChildListAsyncTask extends AsyncTask<Void, Void, String> {

    private Activity mActivity = null;
    private HashMap<String, String> params = new HashMap<String, String>();

    public GetChildListAsyncTask(Activity activity, HashMap<String, String > params){
        mActivity = activity;
        this.params = params;
    }

    ProgressDialog pd;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mActivity);
        pd.setMessage(mActivity.getResources().getString(R.string.pleasewait));
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        String responseString = WebServicesCall.RunScript(AppConfiguration.progressReportURL, this.params);
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
