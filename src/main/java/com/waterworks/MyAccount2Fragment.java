package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class MyAccount2Fragment extends Fragment {
    public MyAccount2Fragment() {
    }

    LinearLayout ll_secondary_parent;
    ImageView imgNext;
    Spinner spinner1, spinner2;
    ArrayList<HashMap<String, String>> phoneTypeList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> typeList = new ArrayList<String>();

    String primaryPhoneType;
    String secondaryPhoneType;

    EditText edtStreetAddress, edtApt, edtCity, edtState, edtZipCode, edtPrimarytele, edtSecondarytele;

    RelativeLayout rel_back;
    View rootView;
    String successPhoneType;
    ImageView btnValidZipCode;
    String zipcode;
    String getState = null, getCity = null;
    String successZipCode = "";
    String messageZipCode = "";
    Boolean isInternetPresent = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.myaccount2_fragment, container, false);

        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1_primary_tel);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2_secondary_tel);
        ll_secondary_parent = (LinearLayout) rootView.findViewById(R.id.ll_secondary_parent);
        imgNext = (ImageView) rootView.findViewById(R.id.imgNext);
        edtStreetAddress = (EditText) rootView.findViewById(R.id.edtStreetAddress);
        edtApt = (EditText) rootView.findViewById(R.id.edtApt);
        edtCity = (EditText) rootView.findViewById(R.id.edtCity);
        edtState = (EditText) rootView.findViewById(R.id.edtState);
        edtZipCode = (EditText) rootView.findViewById(R.id.edtZipCode);
        edtPrimarytele = (EditText) rootView.findViewById(R.id.edtPrimarytele);
        edtSecondarytele = (EditText) rootView.findViewById(R.id.edtSecondarytele);
        rel_back = (RelativeLayout) rootView.findViewById(R.id.rel_back);
        btnValidZipCode = (ImageView) rootView.findViewById(R.id.btnValidZipCode);

        isInternetPresent = Utility.isNetworkConnected(getActivity());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new PhoneTypeAsyncTask().execute();
        }
        btnValidZipCode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                zipcode = edtZipCode.getText().toString();
                isInternetPresent = Utility.isNetworkConnected(getActivity());
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new ValidatingZipCodeAsyncTask().execute();
                }
            }
        });

        edtZipCode.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtZipCode.getWindowToken(), 0);

                    zipcode = edtZipCode.getText().toString();
                    isInternetPresent = Utility.isNetworkConnected(getActivity());
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new ValidatingZipCodeAsyncTask().execute();
                    }
                    return true;

                } else {
                    return false;
                }
            }
        });


        imgNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String address = edtStreetAddress.getText().toString();
                String apt = edtApt.getText().toString();
                String city = edtCity.getText().toString();
                String state = edtState.getText().toString();
                zipcode = edtZipCode.getText().toString();
                String pTelephone = edtPrimarytele.getText().toString();
                String sTelephone = edtSecondarytele.getText().toString();


                if (address.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty() || pTelephone.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                } else {
                    if (!AppConfiguration.isValidMobile("" + pTelephone)) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                    } else if (!sTelephone.equals("")) {
                        if (!AppConfiguration.isValidMobile("" + sTelephone)) {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                        } else {

                            if (!pTelephone.equals("") && (!pTelephone.contains("(") || !pTelephone.contains("-"))) {
                                String part1 = pTelephone.substring(0, 3);
                                String part2 = pTelephone.substring(3, 6);
                                String part3 = pTelephone.substring(6, pTelephone.length());

                                pTelephone = "(" + part1 + ")" + part2 + "-" + part3;
                                Log.e("Formatttttt", pTelephone);
                            }

                            if (!sTelephone.equals("") && (!sTelephone.contains("(") || !sTelephone.contains("-"))) {
                                String part4 = sTelephone.substring(0, 3);
                                String part5 = sTelephone.substring(3, 6);
                                String part6 = sTelephone.substring(6, sTelephone.length());

                                sTelephone = "(" + part4 + ")" + part5 + "-" + part6;
                                Log.e("Formatttttt", sTelephone);

                            }

                            AppConfiguration.Address = address;
                            AppConfiguration.City = city;
                            AppConfiguration.Apt = "" + apt;
                            AppConfiguration.State = state;
                            AppConfiguration.Zipcode = zipcode;
                            AppConfiguration.Phone1 = pTelephone;
                            AppConfiguration.PhoneType1 = primaryPhoneType;
                            AppConfiguration.Phone2 = sTelephone;
                            AppConfiguration.PhoneType2 = secondaryPhoneType;

                            MyAccount3Fragment fragment3 = new MyAccount3Fragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.replace(R.id.frame_container, fragment3);
                            fragmentTransaction.commit();
                        }
                    }else
                    {

                        AppConfiguration.Address = address;
                        AppConfiguration.City = city;
                        AppConfiguration.Apt = "" + apt;
                        AppConfiguration.State = state;
                        AppConfiguration.Zipcode = zipcode;
                        AppConfiguration.Phone1 = pTelephone;
                        AppConfiguration.PhoneType1 = primaryPhoneType;
                        AppConfiguration.Phone2 = sTelephone;
                        AppConfiguration.PhoneType2 = secondaryPhoneType;

                        MyAccount3Fragment fragment3 = new MyAccount3Fragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.frame_container, fragment3);
                        fragmentTransaction.commit();

                    }
                }
            }

        });


        // fetching header view
        View headerLayout = rootView.findViewById(R.id.header);
        ImageView imgBack = (ImageView) headerLayout.findViewById(R.id.ib_back);
        imgBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return rootView;

    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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

    public void loadPhoneTypes() {

        typeList.clear();
        phoneTypeList.clear();

        HashMap<String, String> params = new HashMap<String, String>();
        String responseString = WebServicesCall.RunScript(AppConfiguration.phoneTypesURL, params);

        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successPhoneType = reader.getString("Success");
            if (successPhoneType.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("PhoneList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("PhoneType", jsonChildNode.getString("PhoneType"));
                    hashmap.put("Pk_PhoneTypeId", jsonChildNode.getString("Pk_PhoneTypeId"));

                    typeList.add(jsonChildNode.getString("PhoneType"));
                    phoneTypeList.add(hashmap);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class PhoneTypeAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadPhoneTypes();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successPhoneType.toString().equals("True")) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typeList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(dataAdapter);

                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typeList);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);

                // Spinner1 on item click listener
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {

                        primaryPhoneType = phoneTypeList.get(position).get("Pk_PhoneTypeId");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                spinner1.setSelection(Integer.parseInt(AppConfiguration.PhoneType1) - 1);


                // Spinner2 on item click listener
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {

                        secondaryPhoneType = phoneTypeList.get(position).get("Pk_PhoneTypeId");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                spinner2.setSelection(Integer.parseInt(AppConfiguration.PhoneType2) - 1);


                edtStreetAddress.setText(AppConfiguration.Address);
                edtApt.setText("");
                edtCity.setText(AppConfiguration.City);
                edtState.setText(AppConfiguration.State);
                edtZipCode.setText(AppConfiguration.Zipcode);
                edtPrimarytele.setText(AppConfiguration.Phone1);
                edtSecondarytele.setText(AppConfiguration.Phone2);

                for (int i = 0; i < phoneTypeList.size(); i++) {
                    if (AppConfiguration.Phone1.equals(phoneTypeList.get(i).get("Pk_PhoneTypeId"))) {
                        spinner1.setSelection(i);
                    }


                    if (AppConfiguration.Phone2.equals(phoneTypeList.get(i).get("Pk_PhoneTypeId"))) {
                        spinner2.setSelection(i);
                    }
                }

            }

        }
    }

    //========================================== Validating Zipcode =================================

    public void validatingZipcode() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("zipcode", zipcode);

        String responseString = WebServicesCall.RunScript(AppConfiguration.validateZipCodeURL, params);

        readAndParseJSONZipCode(responseString);

    }

    public void readAndParseJSONZipCode(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successZipCode = reader.getString("Success");
            if (successZipCode.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("LocationDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    getState = jsonChildNode.getString("State");
                    getCity = jsonChildNode.getString("City");

                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("LocationDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    messageZipCode = jsonChildNode.getString("Msg");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ValidatingZipCodeAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Validating...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            validatingZipcode();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }
            if (successZipCode.toString().equals("True")) {
                edtState.setText(getState);
                edtCity.setText(getCity);
            } else {
                edtState.setText("");
                edtCity.setText("");
                Toast.makeText(getActivity(), "" + messageZipCode, Toast.LENGTH_LONG).show();
            }
        }
    }


}
