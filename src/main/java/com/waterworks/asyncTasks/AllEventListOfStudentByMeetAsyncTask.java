package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.UpcomingAllEventStudListModel;
import com.waterworks.model.UpcomingEventStudListModel;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllEventListOfStudentByMeetAsyncTask extends AsyncTask<Void, Void, ArrayList<UpcomingAllEventStudListModel>> {

    Activity activity;
    String swimmeetid;

    public AllEventListOfStudentByMeetAsyncTask(Activity activity, String swimmeetid) {
        this.activity = activity;
        this.swimmeetid = swimmeetid;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<UpcomingAllEventStudListModel> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("swimmeetid", swimmeetid);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmpt_AllEventList, param);

        return GetStudEventList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<UpcomingAllEventStudListModel> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<UpcomingAllEventStudListModel> GetStudEventList(String response) {
        ArrayList<UpcomingAllEventStudListModel> upcomingAllEventStudListModels = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                UpcomingAllEventStudListModel upcomingEventStudListModel = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    upcomingEventStudListModel = new UpcomingAllEventStudListModel();
                    upcomingEventStudListModel.setStartTime(jsonChildNode.getString("StartTime"));
                    upcomingEventStudListModel.setEventDescription(jsonChildNode.getString("EventDescription"));

                    UpcomingAllEventStudListModel.Events events = null;
                    ArrayList<UpcomingAllEventStudListModel.Events> eventses = new ArrayList<>();
                    JSONArray jsonChildArray = jsonChildNode.optJSONArray("Events");
                    for(int j = 0;j < jsonChildArray.length();j++){
                        JSONObject jsonChildObj = jsonChildArray.getJSONObject(j);
                        events = upcomingEventStudListModel.new Events();

                        events.setEventNumber(jsonChildObj.getString("EventNumber"));
                        events.setGender(jsonChildObj.getString("Gender"));
                        events.setAge(jsonChildObj.getString("Age"));
                        events.setDistance(jsonChildObj.getString("Distance"));
                        events.setStrokeDesc(jsonChildObj.getString("StrokeDesc"));
                        eventses.add(events);

                    }
                    upcomingEventStudListModel.setEventsList(eventses);
                    upcomingAllEventStudListModels.add(upcomingEventStudListModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upcomingAllEventStudListModels;
    }
}