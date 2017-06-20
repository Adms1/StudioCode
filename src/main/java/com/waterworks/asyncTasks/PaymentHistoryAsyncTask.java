package com.waterworks.asyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.waterworks.model.PaymentHistoryModel;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentHistoryAsyncTask extends AsyncTask<Void, Void, ArrayList<PaymentHistoryModel>> {
    ProgressDialog pd;
    HashMap<String, String> param = new HashMap<String, String>();
    String token;
    String startDate;
    String endDate;
    String siteid;
    String programTypeid;

    public PaymentHistoryAsyncTask(String token, String startDate, String endDate, String siteid, String programTypeid) {
        this.token = token;
        this.startDate = startDate;
        this.endDate = endDate;
        this.siteid = siteid;
        this.programTypeid = programTypeid;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<PaymentHistoryModel> doInBackground(Void... params) {
        param.put("Token", token);
        param.put("strStartDate", startDate.equalsIgnoreCase("-Start Date-") ? "" : startDate);
        param.put("strEndDate", endDate.equalsIgnoreCase("-End Date-") ? "" : endDate);
        param.put("siteid", siteid);
        param.put("ProgramType", programTypeid.equalsIgnoreCase("0") ? "" : programTypeid);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_GetPaymentHistoryOfFamily, param);
        return result(responseString);
    }

    @Override
    protected void onPostExecute(ArrayList<PaymentHistoryModel> result) {
        super.onPostExecute(result);
    }

    public ArrayList<PaymentHistoryModel> result(String responseString){
        ArrayList<PaymentHistoryModel> paymentHistoryModels = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(responseString);
            JSONArray jsonMainNode = reader.optJSONArray("EmailPref");

            PaymentHistoryModel paymentHistoryModel;

            if(reader.getString("Success").toString().equalsIgnoreCase("True")) {

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    paymentHistoryModel = new PaymentHistoryModel();
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    paymentHistoryModel.setSitename(jsonChildNode.getString("sitename"));
                    paymentHistoryModel.setInvoicedate(jsonChildNode.getString("invoicedate"));
                    paymentHistoryModel.setPaymentType(jsonChildNode.getString("PaymentType"));
                    paymentHistoryModel.setTotalAmount(jsonChildNode.getString("TotalAmount"));
                    paymentHistoryModel.setAmountPaid(jsonChildNode.getString("AmountDue"));

                    JSONArray jsonMainNode1 = jsonChildNode.optJSONArray("Description");
                    ArrayList<HashMap<String, String>> Description = new ArrayList<>();
                    HashMap<String, String> hashmap;

                    for (int j = 0; j < jsonMainNode1.length(); j++) {
                        JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(j);
                        hashmap = new HashMap<>();
                        hashmap.put("ItemType", jsonChildNode1.getString("ItemType"));
                        hashmap.put("ItemName", jsonChildNode1.getString("ItemName"));
                        Description.add(hashmap);
                    }
                    paymentHistoryModel.setDescription(Description);
                    paymentHistoryModels.add(paymentHistoryModel);
                }
            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentHistoryModels;
    }

}