package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.UpcomingEventStudListModel;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class EventListOfStudentByMeetAsyncTask extends AsyncTask<Void, Void, ArrayList<UpcomingEventStudListModel>> {

    Activity activity;
    String swimmeetid;

    public EventListOfStudentByMeetAsyncTask(Activity activity, String swimmeetid) {
        this.activity = activity;
        this.swimmeetid = swimmeetid;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<UpcomingEventStudListModel> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("swimmeetid", swimmeetid);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmpt_EventListOfStudentByMeet, param);

        return GetStudEventList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<UpcomingEventStudListModel> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<UpcomingEventStudListModel> GetStudEventList(String response) {
        ArrayList<UpcomingEventStudListModel> upcomingEventStudListModels = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                UpcomingEventStudListModel upcomingEventStudListModel = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    upcomingEventStudListModel = new UpcomingEventStudListModel();
                    upcomingEventStudListModel.setStudentName(jsonChildNode.getString("StudentName"));

                    UpcomingEventStudListModel.Events events = null;
                    ArrayList<UpcomingEventStudListModel.Events> eventses = new ArrayList<>();
                    JSONArray jsonChildArray = jsonChildNode.optJSONArray("Events");
                    for(int j = 0;j < jsonChildArray.length();j++){
                        JSONObject jsonChildObj = jsonChildArray.getJSONObject(j);
                        events = upcomingEventStudListModel.new Events();

                        events.setEventNumber(jsonChildObj.getString("EventNumber"));
                        events.setDescription(jsonChildObj.getString("Description"));
                        events.setStrockDescription(jsonChildObj.getString("StrockDescription"));
                        eventses.add(events);

                    }
                    upcomingEventStudListModel.setEventsList(eventses);
                    upcomingEventStudListModels.add(upcomingEventStudListModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upcomingEventStudListModels;
    }
}