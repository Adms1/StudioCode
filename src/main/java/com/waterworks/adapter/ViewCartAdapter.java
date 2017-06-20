package com.waterworks.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.ViewCartActivity;
import com.waterworks.utils.AppConfiguration;
import com.wscall.WebServicesCall;

public class ViewCartAdapter extends BaseAdapter {
    Context context;
    private static final String TAG = "View Cart Adater";
    String deleteid;
    ArrayList<String> _index, _ItemTypeID, _Item, _Type, _Package, _Price, _Qty, _Tax, _Subtotal, _Delete, _DeleteEblDble;

    String token, familyID;

    public ViewCartAdapter(Context context, ArrayList<String> _index,
                           ArrayList<String> _ItemTypeID, ArrayList<String> _Item,
                           ArrayList<String> _Type,
                           ArrayList<String> _Package, ArrayList<String> _Price,
                           ArrayList<String> _Qty, ArrayList<String> _Tax,
                           ArrayList<String> _Subtotal, ArrayList<String> _Delete,
                           ArrayList<String> DeleteEblDble) {
        super();
        this.context = context;
        this._index = _index;
        this._ItemTypeID = _ItemTypeID;
        this._Item = _Item;
        this._Type = _Type;
        this._Package = _Package;
        this._Price = _Price;
        this._Qty = _Qty;
        this._Tax = _Tax;
        this._Subtotal = _Subtotal;
        this._Delete = _Delete;
        this._DeleteEblDble = DeleteEblDble;
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(context.getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return _index.size();
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
        TextView tv_type, tv_item, tv_package, tv_price, tv_qty, tv_tax, tv_subtotal;
        ImageButton ib_delete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.viewcart_items, null);
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_view_cart_item_type);
                holder.tv_item = (TextView) convertView.findViewById(R.id.tv_view_cart_item_items);
                holder.tv_package = (TextView) convertView.findViewById(R.id.tv_view_cart_item_package);
                holder.tv_price = (TextView) convertView.findViewById(R.id.tv_view_cart_item_price);
                holder.tv_qty = (TextView) convertView.findViewById(R.id.tv_view_cart_item_qty);
                holder.tv_tax = (TextView) convertView.findViewById(R.id.tv_view_cart_item_tax);
                holder.tv_subtotal = (TextView) convertView.findViewById(R.id.tv_view_cart_item_sub_total);
                holder.ib_delete = (ImageButton) convertView.findViewById(R.id.tv_view_cart_item_delete);

                boolean _item = Boolean.parseBoolean(_DeleteEblDble.get(position));
                Log.d("valueeee", "" + _item);

                if (_item == false) {
                    holder.ib_delete.setVisibility(View.GONE);
                } else {
                    holder.ib_delete.setVisibility(View.VISIBLE);
                }


                holder.ib_delete.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        deleteid = _Delete.get(position);
                        new deleteinvoice().execute();
                    }
                });
                String item = "Item: ", type = "Type: ", tax = "Tax: ", pack = "Package: ", price = "Price: ", qty = "Qty: ", subtotal = "SubTotal: ";

                holder.tv_item.setText(Html.fromHtml("<b>" + item + "</b>" + _Item.get(position)));
                holder.tv_type.setText(Html.fromHtml("<b>" + type + "</b>" + _Type.get(position)));

                holder.tv_tax.setVisibility(View.GONE);
                holder.tv_tax.setText(Html.fromHtml("<b>" + tax + "</b>" + _Tax.get(position)));
                holder.tv_package.setText(Html.fromHtml("<b>" + pack + "</b>" + _Package.get(position)));
                holder.tv_price.setText(Html.fromHtml("<b>" + price + "</b>" + _Price.get(position)));
                holder.tv_qty.setText(Html.fromHtml("<b>" + qty + "</b>" + _Qty.get(position)));
                holder.tv_subtotal.setText(Html.fromHtml("<b>" + subtotal + "</b>" + _Subtotal.get(position)));
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

    private class deleteinvoice extends AsyncTask<Void, Void, Void> {
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
            param.put("BasketID", AppConfiguration.BasketID);
            param.put("DetailID", deleteid);

            String responseString = WebServicesCall
                    .RunScript(AppConfiguration.deleteinvoice, param);
            try {
                JSONObject reader = new JSONObject(responseString);
                data_load_page = reader.getString("Success");

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
                Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_LONG).show();
                ((ViewCartActivity) context).new GetCart().execute();
                notifyDataSetChanged();
                ((ViewCartActivity) context).tv_total.setText("Total:");
            } else {
                Toast.makeText(context, "Not deleted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    String data_load_page;
}
