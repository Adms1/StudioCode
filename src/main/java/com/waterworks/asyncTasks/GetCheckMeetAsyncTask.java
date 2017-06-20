package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetCheckMeetAsyncTask extends AsyncTask<Void, Void, String> {
    ProgressDialog pd;
    HashMap<String, String> param = new HashMap<String, String>();
    String token;

    public GetCheckMeetAsyncTask(String token) {
        this.token = token;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        param.put("Token", token);
        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.SwimCmpt_CheckStudentMeetExist, param);

        return parseJson(responseString);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public String parseJson(String respString){
        String checkMeet = "";
        try {
            JSONObject reader = new JSONObject(respString);
            String data_load = reader.getString("Success");
            if(data_load.toString().equals("True"))
            {
                JSONArray jsonMainNode = reader.optJSONArray("EmailPref");
                for(int i = 0 ;i < jsonMainNode.length();i++)
                {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    checkMeet = jsonChildNode.getString("CheckMeet");
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkMeet;
    }
}