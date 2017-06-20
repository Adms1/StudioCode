package com.waterworks.asyncTasks;

import android.os.AsyncTask;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetProgramPricingTextAsyncTask extends AsyncTask<Void, Void, String> {

    String programID;
    String siteID;

    public GetProgramPricingTextAsyncTask(String programID, String siteID) {
        this.programID = programID;
        this.siteID = siteID;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... params){
        // TODO Auto-generated method stub
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("programid", programID);
        param.put("siteid", siteID);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_ProgramsPriceInfo, param);

        return GetInstructionText(responseString);
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    public String GetInstructionText(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            String data_load_basket = reader.getString("Success");
            if (data_load_basket.toString().equals("True")) {
                    JSONArray jsonMainNode = reader.optJSONArray("Instruction");
                if (jsonMainNode.toString().equalsIgnoreCase("")) {

                } else {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        return jsonChildNode.getString("Instruction");
                    }
                }
            }else{
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}