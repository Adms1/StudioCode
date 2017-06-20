package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class MyAccount3Fragment extends Fragment {
    public MyAccount3Fragment(){}

    String TAG = "MyAccount Screen3";
    LinearLayout ll_secondary_parent;
    ArrayList<HashMap<String, String>> FirstList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> thirdList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> secondaryList = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> siteMainList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> siteName = new ArrayList<String>();

    ArrayList<String> firstListName = new ArrayList<String>();
    ArrayList<String> thirdListName = new ArrayList<String>();
    ArrayList<String> secondaryListName = new ArrayList<String>();

    String siteID;

    String firstSiteSelected,thirdSiteSelected,secondSiteSelected;
    int isMasterDropDown = 0;
    int isChildDropDown = 0;
    int isChild2DropDown = 0;
    EditText edtThirdLevel,edtSecondaryLevel;
    Button btnSubmit;

    String successMaster;
    RelativeLayout rel_back;
    String registerSuccess = "";
    String message;
    //Spinner spinner1;
    Button btn_sites,btn_master,btn_secondary,btn_third_level;
    ListPopupWindow lpw_sitelist,lpw_master,lpw_secondary,lpw_thirdlevel;
    String token,familyID;
    View rootView;
    //EditText edtHearAbout;
    String registerSuccessMessage;
    Boolean isInternetPresent = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.myaccount3_fragment, container, false);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

        //spinner1 = (Spinner) findViewById(R.id.spinner1_sites);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        ll_secondary_parent = (LinearLayout)rootView.findViewById(R.id.ll_secondary_parent);
        //masterSpinner = (Spinner)findViewById(R.id.spinner_master);
        btn_master = (Button)rootView.findViewById(R.id.btn_master);
        //spinnerThirdlevel = (Spinner)findViewById(R.id.spinner_child_level1);
        btn_third_level = (Button)rootView.findViewById(R.id.btn_third_level);
        edtThirdLevel = (EditText)rootView.findViewById(R.id.edtChildLevel1);

        //spinnerSecondaryChild = (Spinner) findViewById(R.id.spinner_child_level2);
        btn_secondary = (Button)rootView.findViewById(R.id.btn_secondary);
        edtSecondaryLevel = (EditText)rootView.findViewById(R.id.edtChildLevel2);

        btn_sites = (Button)rootView.findViewById(R.id.btn_sites);
        lpw_sitelist = new ListPopupWindow(getActivity().getApplicationContext());
        lpw_master = new ListPopupWindow(getActivity().getApplicationContext());
        lpw_secondary = new ListPopupWindow(getActivity().getApplicationContext());
        lpw_thirdlevel = new ListPopupWindow(getActivity().getApplicationContext());

        isInternetPresent = Utility.isNetworkConnected(getActivity());
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new SitesListAsyncTask().execute();
        }
        btn_sites.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {

                lpw_sitelist.show();
                btn_master.setText("Select");
            }
        });


        btn_master.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                lpw_master.show();
                btn_secondary.setText("Select");

            }
        });

        btn_secondary.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                lpw_secondary.show();
            }
        });

        btn_third_level.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                lpw_thirdlevel.show();
            }
        });


        if(!AppConfiguration.strOther.equals("") && !AppConfiguration.strOther.equals("0"))
        {
            edtThirdLevel.setEnabled(true);
            edtThirdLevel.setVisibility(View.VISIBLE);
            edtThirdLevel.setText(""+AppConfiguration.strOther);
            edtThirdLevel.setEnabled(false);
        }
        else if(!AppConfiguration.hearAbout_third.equals("")){
            edtThirdLevel.setEnabled(true);
            edtThirdLevel.setVisibility(View.VISIBLE);
            edtThirdLevel.setText(""+AppConfiguration.hearAbout_third);
            edtThirdLevel.setEnabled(false);
        }
        else if(!AppConfiguration.hearAbout_second.equals("")){
            edtThirdLevel.setEnabled(true);
            edtSecondaryLevel.setVisibility(View.VISIBLE);
            edtSecondaryLevel.setText(""+AppConfiguration.hearAbout_second);
            edtThirdLevel.setEnabled(false);
        }
        else {
            edtThirdLevel.setEnabled(true);
            edtSecondaryLevel.setVisibility(View.VISIBLE);
            edtSecondaryLevel.setText(""+AppConfiguration.hearAbout_first);
            edtThirdLevel.setEnabled(false);
        }

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String edtsecondary = edtSecondaryLevel.getText().toString();
                String edtthirdLevel = edtThirdLevel.getText().toString();


                if(!edtthirdLevel.equals(""))
                    AppConfiguration.strOther = ""+edtthirdLevel;
                else if(!edtsecondary.equals(""))
                    AppConfiguration.strOther = ""+edtsecondary;
                else
                    AppConfiguration.strOther = ""+edtsecondary;

                isInternetPresent = Utility.isNetworkConnected(getActivity());
                if (!isInternetPresent) {
                    onDetectNetworkState().show();
                } else {
                    new SubmitAsyncTask().execute();
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


    public void loadMasterData() {

        FirstList.clear();
        firstListName.clear();

        HashMap<String, String > params = new HashMap<String, String>();
        params.put("Siteid",siteID );

        String responseString = WebServicesCall.RunScript(AppConfiguration.getDataSpinner1URL, params);
        readAndParseJSONMaster(responseString);
    }

    public void readAndParseJSONMaster(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            successMaster = reader.getString("Success");

            if(successMaster.toString().equals("True"))
            {

                //masterSpinner.setVisibility(View.VISIBLE);

                JSONArray jsonMainNode = reader.optJSONArray("MasterList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("master", jsonChildNode.getString("master"));
                    hashmap.put("hearaboutlabel",jsonChildNode.getString("hearaboutlabel"));

                    firstListName.add(jsonChildNode.getString("hearaboutlabel"));
                    FirstList.add(hashmap);
                }
            }
            else
            {
                //masterSpinner.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class MasterAsyncTask extends AsyncTask<Void, Void, Void> {
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
            loadMasterData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if(successMaster.toString().equals("True"))
            {

                btn_master.setVisibility(View.VISIBLE);
            }
            else
            {
                btn_master.setVisibility(View.GONE);

            }

            for(int i = 0 ; i < FirstList.size(); i++)
            {
                if(FirstList.get(i).get("master").contains(AppConfiguration.strMaster))
                {
                    btn_master.setText(firstListName.get(i));

                    btn_third_level.setVisibility(View.GONE);
                    edtThirdLevel.setVisibility(View.GONE);
                    btn_secondary.setVisibility(View.GONE);
                    edtSecondaryLevel.setVisibility(View.GONE);


                    firstSiteSelected = FirstList.get(i).get("master");
                    //masterSiteSelected = masterSiteSelected.replace(':','/');
                    AppConfiguration.strMaster = firstSiteSelected;

                    //dropdown or inputbox
                    String[] separated = firstSiteSelected.split("/");
                    separated[0] = separated[0];
                    separated[1] = separated[1];

                    isMasterDropDown = Integer.parseInt(separated[1]);

                    //next child is dropdown or input box
                    if(isMasterDropDown == 0)
                    {

                        btn_secondary.setVisibility(View.VISIBLE);
                        edtSecondaryLevel.setVisibility(View.GONE);
                    }
                    else
                    {
                        btn_secondary.setVisibility(View.GONE);
                        edtSecondaryLevel.setVisibility(View.VISIBLE);
                    }

                    isInternetPresent = Utility.isNetworkConnected(getActivity());
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new SecondaryAsyncTask().execute();
                    }
                }
            }


            lpw_master.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.edittextpopup,firstListName));
            lpw_master.setAnchorView(btn_master);
            lpw_master.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_master.setModal(true);
            lpw_master.setOnItemClickListener(
                    new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {


                            btn_master.setText(firstListName.get(position));
                            lpw_master.dismiss();

                            btn_third_level.setVisibility(View.GONE);
                            edtThirdLevel.setVisibility(View.GONE);
                            btn_secondary.setVisibility(View.GONE);
                            edtSecondaryLevel.setVisibility(View.GONE);


                            firstSiteSelected = FirstList.get(position).get("master");
                            //masterSiteSelected = masterSiteSelected.replace(':','/');
                            AppConfiguration.strMaster = firstSiteSelected;

                            //dropdown or inputbox
                            String[] separated = firstSiteSelected.split("/");
                            separated[0] = separated[0];
                            separated[1] = separated[1];

                            isMasterDropDown = Integer.parseInt(separated[1]);

                            //next child is dropdown or input box
                            if(isMasterDropDown == 0)
                            {

                                btn_secondary.setVisibility(View.VISIBLE);
                                edtSecondaryLevel.setVisibility(View.GONE);
                            }
                            else
                            {
                                btn_secondary.setVisibility(View.GONE);
                                edtSecondaryLevel.setVisibility(View.VISIBLE);
                            }

                            isInternetPresent = Utility.isNetworkConnected(getActivity());
                            if (!isInternetPresent) {
                                onDetectNetworkState().show();
                            } else {
                                new SecondaryAsyncTask().execute();
                            }

                        }
                    });


            //			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegisterActivity3.this,android.R.layout.simple_spinner_item, firstListName);
            //			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //			masterSpinner.setAdapter(dataAdapter);
            //
            //			masterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //
            //				@Override
            //				public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
            //
            //					spinnerThirdlevel.setVisibility(View.GONE);
            //					edtThirdLevel.setVisibility(View.GONE);
            //	            	spinnerSecondaryChild.setVisibility(View.GONE);
            //	            	edtSecondaryLevel.setVisibility(View.GONE);
            //
            //
            //	            	firstSiteSelected = FirstList.get(position).get("master");
            //					//masterSiteSelected = masterSiteSelected.replace(':','/');
            //					AppConfiguration.strMaster = firstSiteSelected;
            //
            //					//dropdown or inputbox
            //					String[] separated = firstSiteSelected.split("/");
            //					separated[0] = separated[0];
            //					separated[1] = separated[1];
            //
            //					isMasterDropDown = Integer.parseInt(separated[1]);
            //
            //					//next child is dropdown or input box
            //					if(isMasterDropDown == 0)
            //					{
            //
            //						spinnerSecondaryChild.setVisibility(View.VISIBLE);
            //						edtSecondaryLevel.setVisibility(View.GONE);
            //					}
            //					else
            //					{
            //						spinnerSecondaryChild.setVisibility(View.GONE);
            //						edtSecondaryLevel.setVisibility(View.VISIBLE);
            //					}
            //					new SecondaryAsyncTask().execute();
            //
            //
            //				}
            //
            //				@Override
            //				public void onNothingSelected(AdapterView<?> arg0) {
            //				}
            //			});



        }
    }


    //==========================================Start of Child Level 1 ===================================================

    public void loadThirdLevelData() {

        thirdList.clear();
        thirdListName.clear();

        HashMap<String, String > params = new HashMap<String, String>();
        params.put("strMaster",secondSiteSelected );

        String responseString = WebServicesCall.RunScript(AppConfiguration.getDataChildLevel1URL, params);
        readAndParseJSONChildLevel1(responseString);
    }

    public void readAndParseJSONChildLevel1(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if(success.toString().equals("True"))
            {

                JSONArray jsonMainNode = reader.optJSONArray("MasterList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("child", jsonChildNode.getString("child"));
                    hashmap.put("hearaboutlabel",jsonChildNode.getString("hearaboutlabel"));

                    thirdListName.add(jsonChildNode.getString("hearaboutlabel"));
                    thirdList.add(hashmap);
                }
            }
            else
            {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ThirdLevelAsyncTask extends AsyncTask<Void, Void, Void> {
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
            loadThirdLevelData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }




            for(int i = 0 ; i < thirdList.size(); i++)
            {
                if(thirdList.get(i).get("child").contains(AppConfiguration.strChild))
                {
                    btn_third_level.setText(thirdListName.get(i));

                    thirdSiteSelected = thirdList.get(i).get("child");
                    //child1SiteSelected = child1SiteSelected.replace(':','/');

                    if(isChildDropDown == 0)
                    {
                        AppConfiguration.strChild = thirdSiteSelected;

                    }
                    else
                    {

                        AppConfiguration.strOther = ""+edtThirdLevel.getText().toString();
                        Log.e("childLevelOne",""+AppConfiguration.strOther);
                    }
                }
            }

            if(isChildDropDown == 0)
            {

                if(thirdListName.size() > 0)
                    btn_third_level.setVisibility(View.VISIBLE);
                else
                    btn_third_level.setVisibility(View.GONE);

                edtThirdLevel.setVisibility(View.GONE);
            }
            else
            {
                edtThirdLevel.setVisibility(View.VISIBLE);
                btn_third_level.setVisibility(View.GONE);

            }

            lpw_thirdlevel.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.edittextpopup,thirdListName));
            lpw_thirdlevel.setAnchorView(btn_third_level);
            lpw_thirdlevel.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_thirdlevel.setModal(true);
            lpw_thirdlevel.setOnItemClickListener(
                    new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {


                            btn_third_level.setText(thirdListName.get(position));
                            lpw_thirdlevel.dismiss();


                            thirdSiteSelected = thirdList.get(position).get("child");
                            //child1SiteSelected = child1SiteSelected.replace(':','/');

                            if(isChildDropDown == 0)
                            {
                                AppConfiguration.strChild = thirdSiteSelected;

                            }
                            else
                            {

                                AppConfiguration.strOther = ""+edtThirdLevel.getText().toString();
                                Log.e("childLevelOne",""+AppConfiguration.strOther);
                            }



                        }
                    });

            //
            //				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegisterActivity3.this,android.R.layout.simple_spinner_item, thirdListName);
            //				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //				spinnerThirdlevel.setAdapter(dataAdapter);
            //
            //				if(isChildDropDown == 0)
            //				{
            //
            //					if(thirdListName.size() > 0)
            //						btn_third_level.setVisibility(View.VISIBLE);
            //					else
            //						btn_third_level.setVisibility(View.GONE);
            //
            //					edtThirdLevel.setVisibility(View.GONE);
            //				}
            //				else
            //				{
            //					edtThirdLevel.setVisibility(View.VISIBLE);
            //					btn_third_level.setVisibility(View.GONE);
            //
            //				}
            //
            //
            //				spinnerThirdlevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //
            //					@Override
            //					public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
            //
            //						thirdSiteSelected = thirdList.get(position).get("child");
            //						//child1SiteSelected = child1SiteSelected.replace(':','/');
            //
            //						if(isChildDropDown == 0)
            //						{
            //							AppConfiguration.strChild = thirdSiteSelected;
            //
            //						}
            //						else
            //						{
            //
            //							AppConfiguration.strOther = ""+edtThirdLevel.getText().toString();
            //							Log.e("childLevelOne",""+AppConfiguration.strOther);
            //						}
            //
            //
            //
            //
            //						//next child is dropdown or input box
            ////						if(isChildDropDown == 0)
            ////						{
            ////							spinnerSecondaryChild.setVisibility(View.VISIBLE);
            ////							edtSecondaryLevel.setVisibility(View.GONE);
            ////
            ////						}
            ////						else
            ////						{
            ////							spinnerSecondaryChild.setVisibility(View.GONE);
            ////							edtSecondaryLevel.setVisibility(View.VISIBLE);
            ////						}
            //
            //
            //					}
            //
            //					@Override
            //					public void onNothingSelected(AdapterView<?> arg0) {
            //					}
            //				});



        }
    }


    //====================================== child level 2 start ===============================


    public void loadSecondaryLevelData() {

        secondaryList.clear();
        secondaryListName.clear();

        HashMap<String, String > params = new HashMap<String, String>();
        params.put("strChild",firstSiteSelected );

        String responseString = WebServicesCall.RunScript(AppConfiguration.getDataChildLevel2URL, params);
        readAndParseJSONChildLevel2(responseString);
    }

    public void readAndParseJSONChildLevel2(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            String success = reader.getString("Success");
            if(success.toString().equals("True"))
            {

                JSONArray jsonMainNode = reader.optJSONArray("MasterList");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    hashmap.put("secondary", jsonChildNode.getString("secondary"));
                    hashmap.put("hearaboutlabel",jsonChildNode.getString("hearaboutlabel"));

                    secondaryListName.add(jsonChildNode.getString("hearaboutlabel"));
                    secondaryList.add(hashmap);
                }
            }
            else
            {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SecondaryAsyncTask extends AsyncTask<Void, Void, Void> {
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
            loadSecondaryLevelData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if(secondaryListName.size() > 0)
            {


                for(int i = 0 ; i < secondaryList.size(); i++)
                {
                    if(secondaryList.get(i).get("secondary").contains(AppConfiguration.strSecondary))
                    {
                        btn_secondary.setText(secondaryListName.get(i));
                        lpw_secondary.dismiss();


                        secondSiteSelected = secondaryList.get(i).get("secondary");
                        secondSiteSelected = secondSiteSelected.replace(':','/');

                        //dropdown or inputbox
                        String[] separated = secondSiteSelected.split("/");
                        separated[0] = separated[0];
                        separated[1] = separated[1];

                        isChildDropDown = Integer.parseInt(separated[1]);
                        if(isChildDropDown == 0)
                        {
                            AppConfiguration.strSecondary = ""+secondSiteSelected;
                            btn_third_level.setVisibility(View.VISIBLE);
                            edtThirdLevel.setVisibility(View.GONE);

                        }
                        else
                        {
                            btn_third_level.setVisibility(View.GONE);
                            edtThirdLevel.setVisibility(View.VISIBLE);

                            AppConfiguration.strOther = ""+ edtSecondaryLevel.getText().toString();
                            Log.e("Secondary strOther",""+AppConfiguration.strOther);
                        }

                        isInternetPresent = Utility.isNetworkConnected(getActivity());
                        if (!isInternetPresent) {
                            onDetectNetworkState().show();
                        } else {
                            new ThirdLevelAsyncTask().execute();
                        }
                    }
                }


                lpw_secondary.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.edittextpopup,secondaryListName));
                lpw_secondary.setAnchorView(btn_secondary);
                lpw_secondary.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_secondary.setModal(true);
                lpw_secondary.setOnItemClickListener(
                        new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                                btn_secondary.setText(secondaryListName.get(position));
                                lpw_secondary.dismiss();


                                secondSiteSelected = secondaryList.get(position).get("secondary");
                                secondSiteSelected = secondSiteSelected.replace(':','/');

                                //dropdown or inputbox
                                String[] separated = secondSiteSelected.split("/");
                                separated[0] = separated[0];
                                separated[1] = separated[1];

                                isChildDropDown = Integer.parseInt(separated[1]);
                                if(isChildDropDown == 0)
                                {
                                    AppConfiguration.strSecondary = ""+secondSiteSelected;
                                    btn_third_level.setVisibility(View.VISIBLE);
                                    edtThirdLevel.setVisibility(View.GONE);

                                }
                                else
                                {
                                    btn_third_level.setVisibility(View.GONE);
                                    edtThirdLevel.setVisibility(View.VISIBLE);

                                    AppConfiguration.strOther = ""+ edtSecondaryLevel.getText().toString();
                                    Log.e("Secondary strOther",""+AppConfiguration.strOther);
                                }

                                isInternetPresent = Utility.isNetworkConnected(getActivity());
                                if (!isInternetPresent) {
                                    onDetectNetworkState().show();
                                } else {
                                    new ThirdLevelAsyncTask().execute();
                                }
                            }
                        });

            }
            else
            {

                //next child is dropdown or input box
                if(isMasterDropDown == 0)
                {
                    btn_secondary.setVisibility(View.GONE);
                    edtSecondaryLevel.setVisibility(View.GONE);
                }
                else
                {
                    btn_secondary.setVisibility(View.GONE);
                    edtSecondaryLevel.setVisibility(View.VISIBLE);
                }

                //spinnerSecondaryChild.setVisibility(View.GONE);
                //edtSecondaryLevel.setVisibility(View.GONE);

                btn_third_level.setVisibility(View.GONE);
                edtThirdLevel.setVisibility(View.GONE);
            }

        }
    }

    //====================================== child level 2 end ===============================



    //==========================================Start of Site List ===================================================
    public void loadSitesList() {
        siteMainList.clear();

        HashMap<String, String > params = new HashMap<String, String>();

        String responseString = WebServicesCall.RunScript(AppConfiguration.getSiteListURL, params);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("Sites");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap<String, String>();

                hashmap.put("SiteID", jsonChildNode.getString("SiteID"));
                hashmap.put("SiteName",jsonChildNode.getString("SiteName"));

                siteName.add(""+jsonChildNode.getString("SiteName"));
                siteMainList.add(hashmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SitesListAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

            siteMainList.clear();
            siteName.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadSitesList();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            for(int i = 0 ; i < siteMainList.size(); i++)
            {
                if(AppConfiguration.SiteID.equals(siteMainList.get(i).get("SiteID")))
                {
                    btn_sites.setEnabled(true);
                    btn_sites.setText(siteMainList.get(i).get("SiteName"));

                    siteID = siteMainList.get(i).get("SiteID");
                    AppConfiguration.SiteID = siteID;

                    btn_sites.setEnabled(false);
                    btn_master.setVisibility(View.VISIBLE);
                    btn_third_level.setVisibility(View.GONE);
                    edtThirdLevel.setVisibility(View.GONE);
                    btn_secondary.setVisibility(View.GONE);
                    edtSecondaryLevel.setVisibility(View.GONE);

                    isInternetPresent = Utility.isNetworkConnected(getActivity());
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        new MasterAsyncTask().execute();
                    }
                }
            }


            lpw_sitelist.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.edittextpopup,siteName));
            lpw_sitelist.setAnchorView(btn_sites);
            lpw_sitelist.setHeight(LayoutParams.WRAP_CONTENT);
            lpw_sitelist.setModal(true);
            lpw_sitelist.setOnItemClickListener(
                    new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int pos, long id) {

                            btn_sites.setText(siteMainList.get(pos).get("SiteName"));
                            lpw_sitelist.dismiss();

                            siteID = siteMainList.get(pos).get("SiteID");
                            AppConfiguration.SiteID = siteID;

                            btn_master.setVisibility(View.VISIBLE);

                            btn_third_level.setVisibility(View.GONE);
                            edtThirdLevel.setVisibility(View.GONE);
                            btn_secondary.setVisibility(View.GONE);
                            edtSecondaryLevel.setVisibility(View.GONE);

                            isInternetPresent = Utility.isNetworkConnected(getActivity());
                            if (!isInternetPresent) {
                                onDetectNetworkState().show();
                            } else {
                                new MasterAsyncTask().execute();
                            }
                        }
                    });
        }
    }

    //==========================================End of Site List ===================================================

    //======================================== Submit button event =======================

    public void submittingData()
    {
        HashMap<String, String > params = new HashMap<String, String>();
        params.put("Token",token );
        params.put("EmailAdd",AppConfiguration.EmailAdd );
        params.put("ConfirmEmail",AppConfiguration.ConfirmEmail );
        params.put("Password",AppConfiguration.Password );
        params.put("ConfrmPassword",AppConfiguration.ConfrmPassword );
        params.put("PFirstName",AppConfiguration.PFirstName );
        params.put("PLastName",AppConfiguration.PLastName );
        params.put("SFirstName",AppConfiguration.SFirstName );
        params.put("SLastName",AppConfiguration.SLastName );
        params.put("Address",AppConfiguration.Address );
        params.put("Zipcode",AppConfiguration.Zipcode );
        params.put("State",AppConfiguration.State );
        params.put("City",AppConfiguration.City );
        params.put("SiteID",AppConfiguration.SiteID );
        params.put("OrgPass",AppConfiguration.Orgpassword);
        params.put("OrgCnfrmPass",AppConfiguration.OrgConfrmpassword);
        if(AppConfiguration.strMaster.equals("") || AppConfiguration.strMaster == null)
            AppConfiguration.strMaster = "0";
        if(AppConfiguration.strSecondary.equals("") || AppConfiguration.strSecondary == null)
            AppConfiguration.strSecondary = "0";
        if(AppConfiguration.strChild.equals("") || AppConfiguration.strChild == null)
            AppConfiguration.strChild = "0";

        params.put("strMaster",AppConfiguration.strMaster );
        params.put("strChild", AppConfiguration.strChild);
        params.put("strSecondary", AppConfiguration.strSecondary);
        params.put("strOther", AppConfiguration.strOther);
        params.put("status", AppConfiguration.status);

        params.put("Phone1", AppConfiguration.Phone1);
        params.put("Phone2", AppConfiguration.Phone2);
        params.put("Phone3", AppConfiguration.Phone3);
        params.put("Phone4", AppConfiguration.Phone4);
        params.put("Phone5", AppConfiguration.Phone5);
        params.put("Phone6", AppConfiguration.Phone6);
        params.put("Phone7", AppConfiguration.Phone7);
        params.put("Phone8", AppConfiguration.Phone8);

        params.put("PhoneType1", AppConfiguration.PhoneType1);
        params.put("PhoneType2", AppConfiguration.PhoneType2);
        params.put("PhoneType3", AppConfiguration.PhoneType3);
        params.put("PhoneType4", AppConfiguration.PhoneType4);
        params.put("PhoneType5", AppConfiguration.PhoneType5);
        params.put("PhoneType6", AppConfiguration.PhoneType6);
        params.put("PhoneType7", AppConfiguration.PhoneType7);
        params.put("PhoneType8", AppConfiguration.PhoneType8);

// changed by Rakesh 27102015.......
        params.put("wu_MemStatus", AppConfiguration.statusID);
        params.put("PLeavePrivate", AppConfiguration.PLeavePrivate);
        params.put("SLeavePrivate", AppConfiguration.SLeavePrivate);
        String responseString = WebServicesCall.RunScript(AppConfiguration.updateMyAccountURL, params);
        readAndParseJSONSubmit(responseString);
    }

    public void readAndParseJSONSubmit(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            registerSuccess= reader.getString("Success");

            Log.d("registerSuccess", registerSuccess);

            if(registerSuccess.toString().equals("True"))
            {

                JSONArray jsonMainNode = reader.optJSONArray("UpdateFamilyDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    registerSuccessMessage = jsonChildNode.getString("Msg");

                }
            }
            else
            {
                JSONArray jsonMainNode = reader.optJSONArray("UpdateFamilyDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    registerSuccessMessage = jsonChildNode.getString("Msg");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class SubmitAsyncTask extends AsyncTask<Void, Void, Void> {
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
            submittingData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if(registerSuccess.toString().equals("True"))
            {
                AppConfiguration.strOther = "";
                Intent i = new Intent(getActivity(),DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("POS", 2);
                startActivity(i);
                Toast.makeText(getActivity(), ""+registerSuccessMessage, Toast.LENGTH_LONG).show();
                //				((DashBoardActivity)getActivity()).viewaccount();

            }
            else
            {
                AppConfiguration.strOther = "";
                Toast.makeText(getActivity(), ""+registerSuccessMessage, Toast.LENGTH_LONG).show();
            }

        }
    }




}
