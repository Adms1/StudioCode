package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.waterworks.adapter.CheckInPageLoadAdapter;
import com.waterworks.sheduling.ScheduleLessonFragement3;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class CheckInFragment extends Activity{
	Boolean isInternetPresent = false;
	private static String TAG = "CheckInFragment";
	String token,familyID; 
	ListView listView;
    private LinearLayout ll_upcoming_meet, ll_register, ll_trophy_room;
    private TextView txt_1, txt_2, txt_3;
    private View selected_1, selected_2, selected_3;
	String data_load= "False",msg;
	ArrayList<String> t_fullname,wu_preference1,wu_studentid,wu_swimmeetid,wu_preference2,wu_eventnumber,
	wu_groupdescription,wu_description,wu_strokedescription,Time1,wu_checkin,listtbid;
	boolean getdata=false;
	TextView tv_info;
	WebView web;

	public static Button btn_proceed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_checkin);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);

        topTabsViewListnersAnimation();
        isInternetPresent = Utility.isNetworkConnected(this);
        if(!isInternetPresent){
            onDetectNetworkState().show();
        }
    }

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		ConstantData.destroyed=true;
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		listView =  (ListView) findViewById(R.id.lv_checkin_load_list);
		tv_info = (TextView)findViewById(R.id.tv_checkin_info);
		btn_proceed = (Button)findViewById(R.id.btn_checkin_proceed);
	}
	@SuppressWarnings("deprecation")
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        finish();
		    }
		})       
		.setPositiveButton("OK",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		    }
		});
		    return builder1.create();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Initialization();
			new GetCheckInData().execute();
		}
	}
	
	private class GetCheckInData extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(CheckInFragment.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
					
			String responseString = WebServicesCall.RunScript(AppConfiguration.checkinpageload, param);

            try {
    			JSONObject reader = new JSONObject(responseString);
    			data_load = reader.getString("Success");
    			if(data_load.toString().equals("True"))
    			{
    				
    				t_fullname = new ArrayList<String>();
    				wu_preference1 = new ArrayList<String>();
    				wu_studentid = new ArrayList<String>();
    				wu_swimmeetid = new ArrayList<String>();
    				wu_preference2= new ArrayList<String>();
    				wu_eventnumber = new ArrayList<String>();
    				wu_groupdescription = new ArrayList<String>();
    				wu_description = new ArrayList<String>();
    				wu_strokedescription = new ArrayList<String>();
    				Time1 = new ArrayList<String>();
    				wu_checkin = new ArrayList<String>();
    				listtbid = new ArrayList<String>();
    				
    				 JSONArray jsonMainNode = reader.optJSONArray("SwimComp_CheckIN");
    		         for(int i = 0 ;i < jsonMainNode.length();i++)
    		         {
    		        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
    		        	 
    		        	 t_fullname.add(jsonChildNode.getString("fullname"));
    		        	 wu_preference1.add(jsonChildNode.getString("wu_preference1"));
    		        	 wu_studentid.add(jsonChildNode.getString("wu_studentid"));
    		        	 wu_swimmeetid.add(jsonChildNode.getString("wu_swimmeetid"));
    		        	 wu_preference2.add(jsonChildNode.getString("wu_preference2"));
    		        	 wu_eventnumber.add(jsonChildNode.getString("wu_eventnumber"));
    		        	 wu_groupdescription.add(jsonChildNode.getString("wu_groupdescription"));
    		        	 wu_description.add(jsonChildNode.getString("wu_description"));
    		        	 wu_strokedescription.add(jsonChildNode.getString("wu_strokedescription"));
    		        	 Time1.add(jsonChildNode.getString("Time1"));
    		        	 wu_checkin.add(jsonChildNode.getString("wu_checkin"));
    		        	 listtbid.add(jsonChildNode.getString("listtbid"));
    		         }
    			}
    			else{
    				 JSONArray jsonMainNode = reader.optJSONArray("SwimComp_CheckIN");
    				 JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
    				 msg = jsonChildNode.getString("Msg");
    				 
    			}
    		}
    		catch(JSONException e){
    			e.printStackTrace();
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
			if(data_load.toString().equals("True"))
			{
				String res = "<p align=justify> <font size=2 color='#0a0669'>Select the check box for each event your child(ren) would like to swim today.You must select 'Proceed' in order to check-in for the selected events.</font></p>";
				web = (WebView)findViewById(R.id.web);
				web.getSettings().setJavaScriptEnabled(true);
				web.loadData(res, "text/html", "UTF-8");
				btn_proceed.setVisibility(View.VISIBLE);
				listView.setAdapter(new CheckInPageLoadAdapter(t_fullname, wu_preference1, wu_studentid,
						wu_swimmeetid, wu_preference2, wu_eventnumber, wu_groupdescription,
						wu_description, wu_strokedescription, Time1, wu_checkin, listtbid, CheckInFragment.this));
			}
			else{
				Toast.makeText(CheckInFragment.this, msg, Toast.LENGTH_LONG).show();
				btn_proceed.setVisibility(View.GONE);
				String res = "<p align=justify> <font size=2 color='#003d71'>Swim Meet check-in will be available on the day of the meet from 7am until 1 hour before the start time of the meet.Please call our office at 949-450-0777 with any questions.</font></p>";
				web = (WebView)findViewById(R.id.web);
				web.getSettings().setJavaScriptEnabled(true);
				web.loadData(res, "text/html", "UTF-8");
			}
		}
	}

    @Override
    public void onBackPressed() {
        finish();
    }

    public void topTabsViewListnersAnimation() {
        View view = findViewById(R.id.include_swim_comp_custom_top);

        Button BackButton = (Button) view.findViewById(R.id.returnStack);

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(CheckInFragment.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        selected_1 = (View) findViewById(R.id.selected_1);
        selected_2 = (View) findViewById(R.id.selected_2);
        selected_3 = (View) findViewById(R.id.selected_3);

        selected_1.setVisibility(View.VISIBLE);
        selected_2.setVisibility(View.GONE);
        selected_3.setVisibility(View.GONE);

        ((AnimationDrawable) selected_1.getBackground()).start();

        ll_upcoming_meet = (LinearLayout) view.findViewById(R.id.ll_upcoming_meet);
        ll_register = (LinearLayout) view.findViewById(R.id.ll_register);
        ll_trophy_room = (LinearLayout) view.findViewById(R.id.ll_trophy_room);
        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        ll_upcoming_meet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(CheckInFragment.this, SwimCompititionUpcomingEventsAcitivity.class);
                startActivity(i);
                CheckInFragment.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });
        ll_register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.GONE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(CheckInFragment.this, SwimCompititionRegisterAcitivity.class);
                startActivity(i);
                CheckInFragment.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });
        ll_trophy_room.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_upcoming_meet.setBackgroundResource(R.color.design_change_d2);
                ll_register.setBackgroundResource(R.color.design_change_d2);
                ll_trophy_room.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.GONE);
                selected_2.setVisibility(View.GONE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(CheckInFragment.this, SwimCompititionTrophyRoomAcitivity.class);
                startActivity(i);
                CheckInFragment.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }
}
