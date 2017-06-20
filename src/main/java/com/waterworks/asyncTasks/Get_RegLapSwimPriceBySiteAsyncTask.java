package com.waterworks.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Get_RegLapSwimPriceBySiteAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
    HashMap<String, String> param = new HashMap<String, String>();
    Activity activity;

    public Get_RegLapSwimPriceBySiteAsyncTask(Activity activity, HashMap<String, String> param) {
        this.activity = activity;
        this.param = param;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> jsonResponse = null;
            try {
                String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_RegLapSwimPriceBySite, param);
                Log.d("card responseString-", responseString);
                jsonResponse = parseJson(responseString);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
        }

        public ArrayList<String> parseJson(String responseString){
            ArrayList<String> jsonResponse = new ArrayList<>();
            try {
                JSONObject reader = new JSONObject(responseString);
                JSONArray readerArray = reader.getJSONArray("EmailPref");
                for(int i = 0;i < readerArray.length();i++){
                    JSONObject reader1 = readerArray.getJSONObject(i);
                    jsonResponse.add(reader1.getString("PerSession1"));
                    jsonResponse.add(reader1.getString("PerSession2"));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonResponse;
        }
    }