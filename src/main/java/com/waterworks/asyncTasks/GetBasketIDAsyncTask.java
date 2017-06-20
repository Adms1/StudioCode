package com.waterworks.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.FamilySwimNightActivity_Register;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.HashMap;

public class GetBasketIDAsyncTask extends AsyncTask<Void, Void, String> {

    Activity activity;
    String siteID;

    public GetBasketIDAsyncTask(Activity activity, String siteID) {
        this.activity = activity;
        this.siteID = siteID;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... params){
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("siteid", siteID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.Get_BasketID, param);

        return GetBasketID(responseString);
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public String GetBasketID(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("BasketDtl");
                if (jsonMainNode.toString().equalsIgnoreCase("")) {

                } else {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        return jsonChildNode.getString("Basketid");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}