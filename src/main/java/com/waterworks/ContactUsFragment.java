package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ContactUsFragment extends Fragment implements OnClickListener {
    public ContactUsFragment() {
    }

    private static String TAG = "Contact Us";
    View rootView;
    Boolean isInternetPresent = false;
    String message;
    Button btnFeedback, btnHelpCenter;
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    String successContact;
    RelativeLayout title_black;
    ListView list;
    Context mContext = getActivity();
    String token, familyID;

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        ConstantData.destroyed = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contact_us_fragment, container, false);
        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity().getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

        initialization();

        isInternetPresent = Utility.isNetworkConnected(getActivity());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new LoadContactListAsyncTask().execute();

            btnFeedback.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            btnHelpCenter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        list = (ListView) rootView.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        title_black = (RelativeLayout) rootView.findViewById(R.id.title_black);

        View view = rootView.findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.page_title);
        title.setText("Contact us");

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.menu_icon_new);
        LinearLayout ll_ProgressReport, ll_ViewCertificate, ll_RibbonCount;
        ll_ProgressReport = (LinearLayout) view.findViewById(R.id.scdl_lsn);
        ll_ViewCertificate = (LinearLayout) view.findViewById(R.id.scdl_mkp);
        ll_RibbonCount = (LinearLayout) view.findViewById(R.id.waitlist);

        View vert_1 = (View) view.findViewById(R.id.vert_1);
        vert_1.setVisibility(View.GONE);
        ll_ProgressReport.setVisibility(View.GONE);

        TextView txt_2 = (TextView) view.findViewById(R.id.txt_2);
        TextView txt_3 = (TextView) view.findViewById(R.id.txt_3);

        txt_2.setText("Feedback");
        txt_3.setText("Help Center");

        Button relMenu = (Button) view.findViewById(R.id.returnStack);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity.onLeft();
            }
        });

        ll_ViewCertificate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(i);
            }
        });

        ll_RibbonCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HelpCenterActivity.class);
                startActivity(i);
            }
        });
        return rootView;
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
    public void initialization() {
        btnFeedback = (Button) rootView.findViewById(R.id.btnFeedback);
        btnHelpCenter = (Button) rootView.findViewById(R.id.btnHelpCenter);
    }
    public void getContactList() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);

        String responseString = WebServicesCall.RunScript(AppConfiguration.contactUsURL, param);
        readAndParseJSON(responseString);
    }
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successContact = reader.getString("Success");
            if (successContact.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("SiteDetail");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String Sitename = jsonChildNode.getString("Sitename").trim();
                    String SiteDetails = jsonChildNode.getString("SiteDetails").trim();
                    String Pnoneno = jsonChildNode.getString("Pnoneno").trim();
                    String Details2 = jsonChildNode.getString("Details2").trim();

                    hashmap.put("Sitename", Sitename);
                    hashmap.put("SiteDetails", SiteDetails);
                    hashmap.put("Pnoneno", Pnoneno);
                    hashmap.put("Details2", Details2);

                    contactList.add(hashmap);
                }
            } else {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class LoadContactListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            getContactList();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successContact.toString().equals("True")) {
                CustomList adapter = new CustomList(getActivity(), contactList);
                list.setAdapter(adapter);
            } else {}
        }
    }
    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final ArrayList<HashMap<String, String>> data;

        public CustomList(Activity context, ArrayList<HashMap<String, String>> list) {
            super(context, R.layout.list_row_contact_us);
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
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_row_contact_us, null, true);

            TextView txtSiteName = (TextView) rowView.findViewById(R.id.txtSiteName);
            txtSiteName.setText(data.get(position).get("Sitename"));

            TextView txtVenue = (TextView) rowView.findViewById(R.id.txtVenue);
            txtVenue.setText(Html.fromHtml(data.get(position).get("SiteDetails")));

            TextView txtPhone = (TextView) rowView.findViewById(R.id.txtPhone);
            txtPhone.setText(Html.fromHtml(data.get(position).get("Pnoneno")));

            TextView txtHrsOfOperation = (TextView) rowView.findViewById(R.id.txtHrsOfOperation);
            txtHrsOfOperation.setText(Html.fromHtml(data.get(position).get("Details2")));

            txtPhone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + data.get(position).get("Pnoneno")));
                    startActivity(callIntent);
                }
            });
            return rowView;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relMenu:
                DashBoardActivity.onLeft();
                break;
            default:
                break;
        }
    }
}
