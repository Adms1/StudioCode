package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.RecordsAgeGroup;
import com.waterworks.model.RecordsStrokeGroup;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetAllEventAsyncTask extends AsyncTask<Void, Void, ArrayList<RecordsStrokeGroup>> {

    Activity activity;
    String studID;

    public GetAllEventAsyncTask(Activity activity, String studID) {
        this.activity = activity;
        this.studID = studID;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<RecordsStrokeGroup> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("studentid", studID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.GetStudentEvents, param);

        return GetStudAgeList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<RecordsStrokeGroup> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<RecordsStrokeGroup> GetStudAgeList(String response) {
        ArrayList<RecordsStrokeGroup> recordsAgeGroups = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EventList");
                RecordsStrokeGroup recordsAgeGroup = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    recordsAgeGroup = new RecordsStrokeGroup();
                    recordsAgeGroup.setEventID(jsonChildNode.getString("EventID"));
                    recordsAgeGroup.setEventName(jsonChildNode.getString("EventName"));

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