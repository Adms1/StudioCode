package com.waterworks.asyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.waterworks.model.PoolRecordsModel;
import com.waterworks.model.UpcomingEventResultsDetailModel;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetPoolDataAsyncTask extends AsyncTask<Void, Void, ArrayList<PoolRecordsModel>> {

    Activity activity;
    String agegroupid;
    String strokeid;

    public GetPoolDataAsyncTask(Activity activity, String agegroupid, String strokeid) {
        this.activity = activity;
        this.agegroupid = agegroupid;
        this.strokeid = strokeid;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<PoolRecordsModel> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(activity);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", prefs.getString("Token", ""));
        param.put("agegroupid", agegroupid);
        param.put("strokeid", strokeid);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmpt_GetPoolRecords, param);

        return GetStudEventList(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<PoolRecordsModel> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public ArrayList<PoolRecordsModel> GetStudEventList(String response) {
        ArrayList<PoolRecordsModel> poolRecordsModels = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                PoolRecordsModel poolRecordsModel = null;

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    poolRecordsModel = new PoolRecordsModel();
                    poolRecordsModel.setEventDescription(jsonChildNode.getString("EventDescription"));
                    HashMap<String, String> hashMap = null;
                    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
                    JSONArray jsonMainNode1 = jsonChildNode.optJSONArray("Events");
                    for(int j = 0;j < jsonMainNode1.length();j++){
                        JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(j);
                        hashMap = new HashMap<>();
                        hashMap.put("EventDate", jsonChildNode1.getString("EventDate").toString());
                        hashMap.put("Swimmer", jsonChildNode1.getString("Swimmer").toString());
                        hashMap.put("Age", jsonChildNode1.getString("Age").toString());
                        hashMap.put("MeetTime", jsonChildNode1.getString("MeetTime").toString());
                        hashMapArrayList.add(hashMap);
                    }
                    poolRecordsModel.setEventDetails(hashMapArrayList);
                    poolRecordsModels.add(poolRecordsModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poolRecordsModels;
    }
}