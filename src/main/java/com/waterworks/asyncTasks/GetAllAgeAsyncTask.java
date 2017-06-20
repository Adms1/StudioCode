package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.RecordsAgeGroup;
import com.waterworks.model.UpcomingEventResultsDetailModel;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetAllAgeAsyncTask extends AsyncTask<Void, Void, ArrayList<RecordsAgeGroup>> {

    Activity activity;

    public GetAllAgeAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<RecordsAgeGroup> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetAgeGroupListForSwimMeet, param);

        return GetStudAgeList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<RecordsAgeGroup> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<RecordsAgeGroup> GetStudAgeList(String response) {
        ArrayList<RecordsAgeGroup> recordsAgeGroups = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("AgeList");
                RecordsAgeGroup recordsAgeGroup = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    recordsAgeGroup = new RecordsAgeGroup();
                    recordsAgeGroup.setAgeGroup(jsonChildNode.getString("AgeGroup"));
                    recordsAgeGroup.setAgeGroupID(jsonChildNode.getString("AgeGroupID"));

                    recordsAgeGroups.add(recordsAgeGroup);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recordsAgeGroups;
    }
}