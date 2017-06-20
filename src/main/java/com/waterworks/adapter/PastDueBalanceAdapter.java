package com.waterworks.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.PastDueBalActivity;
import com.waterworks.R;
import com.waterworks.ViewCartActivity;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.NonUnderlinedClickableSpan;
import com.wscall.WebServicesCall;

public class PastDueBalanceAdapter extends BaseAdapter {
    SparseBooleanArray mChecked = new SparseBooleanArray();
    Context context;
    Button btn_pay_bal;
    String data_load_page;
    private static final String TAG = "Past Due Balance Adapter";
    ArrayList<String> index, date, invoiceId, itemType, expDate, amountDue, amountPaid, amountTotal, SelectedInvID;
    String RES_BasketID, RES_Total, RES_PaidAmount, RES_DueAmount, RES_FinalArray;
    String InvoiceID;
    String token, familyID;

    public PastDueBalanceAdapter(Context context, ArrayList<String> index,
                                 ArrayList<String> date, ArrayList<String> invoiceId,
                                 ArrayList<String> itemType, ArrayList<String> expDate,
                                 ArrayList<String> amountDue, ArrayList<String> amountPaid,
                                 ArrayList<String> amountTotal, Button btn_pay_bal) {
        super();
        this.context = context;
        this.index = index;
        this.date = date;
        this.invoiceId = invoiceId;
        this.itemType = itemType;
        this.expDate = expDate;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.amountTotal = amountTotal;
        this.btn_pay_bal = btn_pay_bal;

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(context.getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return index.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class ViewHolder {
        TextView tv_date, tv_iv_id, tv_iv_total, tv_item_type, tv_exp_date, tv_amnt_paid, tv_bal_due;
        CheckBox chb_check;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.pastduebal_item, null);

                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_pb_item_date);
                holder.tv_iv_id = (TextView) convertView.findViewById(R.id.tv_pb_item_invoiceid);
                holder.tv_iv_total = (TextView) convertView.findViewById(R.id.tv_pb_item_invoice_total);
                holder.tv_item_type = (TextView) convertView.findViewById(R.id.tv_pb_item_it);
                holder.tv_exp_date = (TextView) convertView.findViewById(R.id.tv_pb_item_expdate);
                holder.tv_amnt_paid = (TextView) convertView.findViewById(R.id.tv_pb_item_amunt_paid);
                holder.tv_bal_due = (TextView) convertView.findViewById(R.id.tv_pb_item_bal_due);
                holder.chb_check = (CheckBox) convertView.findViewById(R.id.chb_pb_item_check);


                SelectedInvID = new ArrayList<String>();
                holder.tv_date.setText(Html.fromHtml("<b>Date: </b>" + date.get(position)));

                String invoice = "Invoice ID:";
                String clickhere = invoiceId.get(position);
                final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
                stringBuilder.append(invoice);
                stringBuilder.append(clickhere);
                stringBuilder.setSpan(
                        new NonUnderlinedClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                InvoiceID = invoiceId.get(position);
                                new InsertInvoice().execute();
                            }
                        },
                        invoice.length(), invoice.length() + clickhere.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.setSpan(bss, 0, invoice.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                holder.tv_iv_id.setText(stringBuilder);
                holder.tv_iv_id.setMovementMethod(LinkMovementMethod.getInstance());
                holder.tv_iv_id.setLinkTextColor(context.getResources().getColor(R.color.link));
                holder.tv_iv_total.setText(Html.fromHtml("<b>Invoice Total: </b>" + amountDue.get(position)));
                holder.tv_item_type.setText(Html.fromHtml("<b>Item Type: </b>" + itemType.get(position)));
                holder.tv_exp_date.setText(Html.fromHtml("<b>ExpDate: </b>" + expDate.get(position)));
                holder.tv_amnt_paid.setText(Html.fromHtml("<b>Amount Paid: </b>" + amountPaid.get(position)));
                holder.tv_bal_due.setText(Html.fromHtml("<b>Balance Due: </b>" + amountTotal.get(position)));

                holder.chb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            mChecked.put(position, isChecked);
                            Log.i("Value", "" + mChecked);

                        } else {
                            mChecked.delete(position);
                            Log.i("Value1", "" + mChecked);
                        }
                    }
                });

                btn_pay_bal.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (mChecked.size() == 0) {
                            Toast.makeText(context, "Please select invoice.", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < mChecked.size(); i++) {
                                int pos = mChecked.keyAt(i);
                                SelectedInvID.add(invoiceId.get(pos));
                            }
                            new PayInvoice().execute();
                        }
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class PayInvoice extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", familyID);
            param.put("SelectedInvID", SelectedInvID.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.payinvoice, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_page = reader.getString("Success");
                if (data_load_page.toString().equalsIgnoreCase("True")) {
                    RES_BasketID = reader.getString("BasketID");
                    RES_Total = reader.getString("Total");
                    RES_PaidAmount = reader.getString("PaidAmount");
                    RES_DueAmount = reader.getString("DueAmount");
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    RES_FinalArray = jsonMainNode.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (data_load_page.toString().equals("True")) {
                Log.i(TAG, "RES_BasketID = " + RES_BasketID);
                Log.i(TAG, "RES_Total = " + RES_Total);
                Log.i(TAG, "RES_PaidAmount = " + RES_PaidAmount);
                Log.i(TAG, "RES_DueAmount = " + RES_DueAmount);
                Log.i(TAG, "RES_FinalArray = " + RES_FinalArray);
                AppConfiguration.BasketID = RES_BasketID;
                Intent i = new Intent(context, ViewCartActivity.class);
                context.startActivity(i);
                ((PastDueBalActivity) context).finish();
            } else {
                Toast.makeText(context, "Something went wrong. Please try after sometime.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class InsertInvoice extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("Token", token);
            param.put("FamilyID", familyID);
            param.put("InvoiceID", InvoiceID);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.insertinvoice, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_page = reader.getString("Success");
                if (data_load_page.toString().equalsIgnoreCase("True")) {
                    RES_BasketID = reader.getString("BasketID");
                    RES_Total = reader.getString("Total");
                    RES_PaidAmount = reader.getString("PaidAmount");
                    RES_DueAmount = reader.getString("DueAmount");
                    JSONArray jsonMainNode = reader.optJSONArray("FinalArray");
                    RES_FinalArray = jsonMainNode.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (data_load_page.toString().equals("True")) {
                Log.i(TAG, "RES_BasketID = " + RES_BasketID);
                Log.i(TAG, "RES_Total = " + RES_Total);
                Log.i(TAG, "RES_PaidAmount = " + RES_PaidAmount);
                Log.i(TAG, "RES_DueAmount = " + RES_DueAmount);
                Log.i(TAG, "RES_FinalArray = " + RES_FinalArray);
                AppConfiguration.BasketID = RES_BasketID;
                Intent i = new Intent(context, ViewCartActivity.class);
                context.startActivity(i);
                ((PastDueBalActivity) context).finish();
            } else {
                Toast.makeText(context, "Something went wrong. Please try after sometime.", Toast.LENGTH_LONG).show();
            }
        }
    }

}