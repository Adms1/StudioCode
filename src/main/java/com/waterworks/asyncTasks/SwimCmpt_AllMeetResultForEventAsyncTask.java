package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.UpcomingEventResultsDetailModel;
import com.waterworks.model.UpcomingEventResultsListModel;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SwimCmpt_AllMeetResultForEventAsyncTask extends AsyncTask<Void, Void, ArrayList<UpcomingEventResultsDetailModel>> {

    Activity activity;
    String swimmeetid;
    String eventID;

    public SwimCmpt_AllMeetResultForEventAsyncTask(Activity activity, String swimmeetid, String eventID) {
        this.activity = activity;
        this.swimmeetid = swimmeetid;
        this.eventID = eventID;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<UpcomingEventResultsDetailModel> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("swimmeetid", swimmeetid);
        param.put("eventnumber", eventID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmpt_AllMeetResultForEvent, param);

        return GetStudEventList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<UpcomingEventResultsDetailModel> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<UpcomingEventResultsDetailModel> GetStudEventList(String response) {
        ArrayList<UpcomingEventResultsDetailModel> upcomingEventStudListModel = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                UpcomingEventResultsDetailModel upcomingEventResultsListModel = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    upcomingEventResultsListModel = new UpcomingEventResultsDetailModel();
                    upcomingEventResultsListModel.setSwimmer(jsonChildNode.getString("Swimmer"));
                    upcomingEventResultsListModel.setStudentID(jsonChildNode.getString("StudentID"));
                    upcomingEventResultsListModel.setAge(jsonChildNode.getString("Age"));
                    upcomingEventResultsListModel.setPlaceno(jsonChildNode.getString("placeno"));
                    upcomingEventResultsListModel.setMeetTime(jsonChildNode.getString("MeetTime"));
                    upcomingEventResultsListModel.setTimeImprovement(jsonChildNode.getString("TimeImprovement"));

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