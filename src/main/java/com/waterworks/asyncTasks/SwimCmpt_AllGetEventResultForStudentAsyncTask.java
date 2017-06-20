package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.UpcomingAllEventStudListModel;
import com.waterworks.model.UpcomingEventResultsListModel;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SwimCmpt_AllGetEventResultForStudentAsyncTask extends AsyncTask<Void, Void, ArrayList<UpcomingEventResultsListModel>> {

    Activity activity;
    String swimmeetid;
    String studID;

    public SwimCmpt_AllGetEventResultForStudentAsyncTask(Activity activity, String swimmeetid, String studID) {
        this.activity = activity;
        this.swimmeetid = swimmeetid;
        this.studID = studID;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<UpcomingEventResultsListModel> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("swimmeetid", swimmeetid);
        param.put("studentid", studID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmpt_AllGetEventResultForStudent, param);

        return GetStudEventList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<UpcomingEventResultsListModel> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<UpcomingEventResultsListModel> GetStudEventList(String response) {
        ArrayList<UpcomingEventResultsListModel> upcomingEventStudListModel = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                UpcomingEventResultsListModel upcomingEventResultsListModel = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    upcomingEventResultsListModel = new UpcomingEventResultsListModel();
                    upcomingEventResultsListModel.setEventNumber(jsonChildNode.getString("EventNumber"));
                    upcomingEventResultsListModel.setMeetTime(jsonChildNode.getString("MeetTime"));
                    upcomingEventResultsListModel.setTimeImprovement(jsonChildNode.getString("TimeImprovement"));
                    upcomingEventResultsListModel.setDistance(jsonChildNode.getString("Distance"));
                    upcomingEventResultsListModel.setStrokedescription(jsonChildNode.getString("strokedescription"));
                    upcomingEventResultsListModel.setPlaceno(jsonChildNode.getString("placeno"));

                    upcomingEventStudListModel.add(upcomingEventResultsListModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upcomingEventStudListModel;
    }
}