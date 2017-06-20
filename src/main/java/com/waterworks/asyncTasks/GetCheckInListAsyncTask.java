package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetCheckInListAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {

    Activity activity;
    HashMap<String, String> ShowHideScanProgramsList = new HashMap<String, String>();

    public GetCheckInListAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... params){
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.ShowHideScanPrograms, param);

        return GetCheckinList(responseString);
    }

    @Override
    protected void onPostExecute(HashMap<String, String> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public HashMap<String, String> GetCheckinList(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                if (jsonMainNode.toString().equalsIgnoreCase("")) {

                } else {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        ShowHideScanProgramsList.put("SwimLessons", jsonChildNode.getString("SwimLessons"));
                        ShowHideScanProgramsList.put("SwimTeam", jsonChildNode.getString("SwimTeam"));
                        ShowHideScanProgramsList.put("LapSwim", jsonChildNode.getString("LapSwim"));
                        ShowHideScanProgramsList.put("MonthlyLapSwim", jsonChildNode.getString("MonthlyLapSwim"));
                        ShowHideScanProgramsList.put("Aerobics", jsonChildNode.getString("Aerobics"));
                        ShowHideScanProgramsList.put("PhysicalTherapy", jsonChildNode.getString("PhysicalTherapy"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ShowHideScanProgramsList;
    }
}