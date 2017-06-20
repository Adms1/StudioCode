package com.waterworks.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Get_MonthlyLapSwimPriceBySiteAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {
    HashMap<String, String> param = new HashMap<String, String>();
    Activity activity;

    public Get_MonthlyLapSwimPriceBySiteAsyncTask(Activity activity, HashMap<String, String> param) {
        this.activity = activity;
        this.param = param;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {
            HashMap<String, String> jsonResponse = null;
            try {
                String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_MonthlyLapSwimPriceBySite, param);
                Log.d("card responseString-", responseString);
                jsonResponse = parseJson(responseString);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {
            super.onPostExecute(result);
        }

        public HashMap<String, String> parseJson(String responseString){
            HashMap<String, String> jsonResponse = new HashMap<>();
            try {
                JSONObject reader = new JSONObject(responseString);
                JSONArray readerArray = reader.getJSONArray("EmailPref");
                for(int i = 0;i < readerArray.length();i++){
                    JSONObject reader1 = readerArray.getJSONObject(i);
                    jsonResponse.put("1MonthReg", reader1.getString("1MonthReg"));
                    jsonResponse.put("1MonthDis", reader1.getString("1MonthDis"));
                    jsonResponse.put("1MonthSen", reader1.getString("1MonthSen"));
                    jsonResponse.put("3MonthReg", reader1.getString("3MonthReg"));
                    jsonResponse.put("3MonthDis", reader1.getString("3MonthDis"));
                    jsonResponse.put("3MonthSen", reader1.getString("3MonthSen"));
                    jsonResponse.put("6MonthReg", reader1.getString("6MonthReg"));
                    jsonResponse.put("6MonthDis", reader1.getString("6MonthDis"));
                    jsonResponse.put("6MonthSen", reader1.getString("6MonthSen"));
                    jsonResponse.put("12MonthReg", reader1.getString("12MonthReg"));
                    jsonResponse.put("12MonthDis", reader1.getString("12MonthDis"));
                    jsonResponse.put("12MonthSen", reader1.getString("12MonthSen"));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonResponse;
        }
    }