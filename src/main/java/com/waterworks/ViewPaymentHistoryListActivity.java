package com.waterworks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ViewPaymentHistoryListActivity extends Activity {

    String TAG = "ViewPaymentHistory";
    ArrayList<HashMap<String, String>> viewPaymentHistoryList = new ArrayList<HashMap<String, String>>();
    ListView list;
    TextView txtNoRecord;
    String successViewPaymentHistory;

    String token, familyID;
    DecimalFormat df;
    Boolean isInternetPresent = false;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_payment_history_activity);
        df = new DecimalFormat("####0.00");

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);


        txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ViewStudentsListActivity.this,"You Clicked at " + studentList.get(position).get("ChildrenName"),Toast.LENGTH_SHORT).show();


            }
        });


        // fetching header view
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("View Payment History");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ViewPaymentHistoryListActivity.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        isInternetPresent = Utility.isNetworkConnected(ViewPaymentHistoryListActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new ViewPaymentHistoryAsyncTask().execute();
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void getStudentList() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall
                .RunScript(AppConfiguration.viewPaymentHistory, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successViewPaymentHistory = reader.getString("Success");
            if (successViewPaymentHistory.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("ViewPayment");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String srno = jsonChildNode.getString("srno").trim();
                    String invoiceid = jsonChildNode.getString("invoiceid").trim();
                    String Itemtype = jsonChildNode.getString("Itemtype").trim();
                    String Itemname = jsonChildNode.getString("Itemname").trim();
                    String invoicedate = jsonChildNode.getString("invoicedate").trim();

                    String expirationdate = jsonChildNode.getString("expirationdate").trim();
                    String AmountDue = jsonChildNode.getString("AmountDue").trim();
                    String AmountPaid = jsonChildNode.getString("AmountPaid").trim();
                    String Balance = jsonChildNode.getString("Balance").trim();

                    hashmap.put("srno", srno);
                    hashmap.put("invoiceid", invoiceid);
                    hashmap.put("Itemtype", Itemtype);
                    hashmap.put("Itemname", Itemname);
                    hashmap.put("invoicedate", invoicedate);

                    hashmap.put("expirationdate", expirationdate);
                    hashmap.put("AmountDue", AmountDue);
                    hashmap.put("AmountPaid", AmountPaid);
                    hashmap.put("Balance", Balance);

                    viewPaymentHistoryList.add(hashmap);

                }

            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewPaymentHistoryAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ViewPaymentHistoryListActivity.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            viewPaymentHistoryList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getStudentList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (viewPaymentHistoryList.size() > 0) {
                txtNoRecord.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            } else {
                txtNoRecord.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }

            if (successViewPaymentHistory.toString().equals("True")) {

                CustomList adapter = new CustomList(ViewPaymentHistoryListActivity.this, viewPaymentHistoryList);
                list.setAdapter(adapter);


            } else {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final ArrayList<HashMap<String, String>> data;

        public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_single);
            this.context = context;
            this.data = list;
        }

        @Override
        public int getCount() {
            return data.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_row_view_payment_history, null, true);

            TextView txtSrNo = (TextView) rowView.findViewById(R.id.txtSrNo);
            String srno = data.get(position).get("srno");
            if (!srno.equals("")) {
                txtSrNo.setText("SrNo\n" + srno);
            } else {
                txtSrNo.setVisibility(View.GONE);
            }


            TextView txtInvoiceid = (TextView) rowView.findViewById(R.id.txtInvoiceid);
            String invoidID = data.get(position).get("invoiceid");
            if (!invoidID.equals("")) {
                txtInvoiceid.setText("Invoice\n" + invoidID);
            } else {
                txtInvoiceid.setVisibility(View.GONE);
            }


            TextView txtItemType = (TextView) rowView.findViewById(R.id.txtItemType);
            String lessonType = data.get(position).get("Itemtype");
            if (!lessonType.equals("")) {

                txtItemType.setText(Html.fromHtml("<b>Item Type:</b>" + lessonType));
            } else {
                txtItemType.setVisibility(View.GONE);
            }

            TextView txtItem = (TextView) rowView.findViewById(R.id.txtItem);
            txtItem.setText(Html.fromHtml("<b>Item:</b>" + data.get(position).get("Itemname")));

            TextView txtInvDate = (TextView) rowView.findViewById(R.id.txtInvDate);
            txtInvDate.setText("Inv Date:" + data.get(position).get("invoicedate"));

            TextView txtExpiry = (TextView) rowView.findViewById(R.id.txtExpiry);
            txtExpiry.setText("Expiry:" + data.get(position).get("expirationdate"));

            TextView txtAmountDue = (TextView) rowView.findViewById(R.id.txtAmountDue);
            txtAmountDue.setText(Html.fromHtml("<b>AmtDue:</b>" + AppConfiguration.currency + df.format(Double.parseDouble(data.get(position).get("AmountDue")))));

            TextView txtAmountPaid = (TextView) rowView.findViewById(R.id.txtAmountPaid);
            txtAmountPaid.setText(Html.fromHtml("<b>AmtPaid:</b>" + AppConfiguration.currency + df.format(Double.parseDouble(data.get(position).get("AmountPaid")))));

            TextView txtBalance = (TextView) rowView.findViewById(R.id.txtBalance);
            txtBalance.setText("Balance:" + AppConfiguration.currency + df.format(Double.parseDouble(data.get(position).get("Balance"))));

            return rowView;
        }
    }

}
