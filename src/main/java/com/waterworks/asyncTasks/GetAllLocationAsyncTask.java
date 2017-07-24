package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.RecordsLocationGroups;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admsandroid on 7/3/2017.
 */

public class GetAllLocationAsyncTask extends AsyncTask<Void, Void, ArrayList<RecordsLocationGroups>> {

    Activity activity;

    public GetAllLocationAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<RecordsLocationGroups> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_SiteListForFamilyPoolRecords, param);

        return GetStuLocationList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<RecordsLocationGroups> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<RecordsLocationGroups> GetStuLocationList(String response) {
        ArrayList<RecordsLocationGroups> recordsLocationGroups = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                RecordsLocationGroups recordsLocationGroup = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    recordsLocationGroup = new RecordsLocationGroups();
                    recordsLocationGroup.setLocationID(jsonChildNode.getString("SiteID"));
                    recordsLocationGroup.setLocationName(jsonChildNode.getString("SiteName"));

                    recordsLocationGroups.add(recordsLocationGroup);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recordsLocationGroups;
    }
}