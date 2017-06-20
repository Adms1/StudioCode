package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.waterworks.sheduling.BuyMoreSelectPaymentMethod;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import java.util.HashMap;

public class cardDetailAsyncTask extends AsyncTask<Void, Void, String> {
    Context mContext;
    private HashMap<String, String> paramBasketIDToken = new HashMap<String, String>();

    public cardDetailAsyncTask(Context context, HashMap<String, String> paramBasketIDToken) {
        mContext = context;
        this.paramBasketIDToken = paramBasketIDToken;
    }

    @Override
        protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Pay_DGetACHCardDetailBySiteURL, paramBasketIDToken);
        Log.i("responseString", responseString);

        return responseString;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}