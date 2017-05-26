package com.waterworks.Unused;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.ViewCartActivity;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class SwimCompititionConfirmationPageAcitivity extends Activity {
	String TAG = "SwimCompetitionConfirmationPage";
	
	ArrayList<HashMap<String, String>> confirmationPage = new ArrayList<HashMap<String, String>>();
	
	
	
	Boolean isInternetPresent = false;
	String siteID;
	String successLoadChildList,successReminderText;
	String token,familyID;
	String messageMeetDate;
	String successMeetDate;
	Button btnAddToCart;
	boolean isSelectedAgreement = false;
	String msg1_Hours,msg2_meet,Msg3_Meet;
	String successSwimCompittionCheck1;
	String messageNormal;
	TextView txtSelectedStudent,txtMeetDate;
	String message;
	boolean registerAsVolunteer = false;
	Spinner spinner1_volunteers;
	TextView txtJumpingWall,txtSwimNext;
	//TextView txtReminders;
	String data_load_basket;
	String data_load ="";
	TextView txtNoofEvents;
	TextView txtNoofPricePrice,txtMemberEntryFeePrice,txtNonMemberRegistrationPrice,txtLateRegistration,txtLateRegistrationPrice,txtTotalAmount,txtPreviousBill,txtTotalBalanceDue;
	String successAddToCart;
	//DecimalFormat df ;
	String memberID;
	
	EditText edtFirstnameVol1,edtFirstnameVol2,edtFirstnameVol3,edtFirstnameVol4,edtFirstnameVol5,edtFirstnameVol6,edtFirstnameVol7,edtFirstnameVol8,edtFirstnameVol9,edtFirstnameVol10;
	EditText edtLastnameVol1,edtLastnameVol2,edtLastnameVol3,edtLastnameVol4,edtLastnameVol5,edtLastnameVol6,edtLastnameVol7,edtLastnameVol8,edtLastnameVol9,edtLastnameVol10;
	
	RadioButton radiobtnYesVol1,radiobtnYesVol2,radiobtnYesVol3,radiobtnYesVol4,radiobtnYesVol5,radiobtnYesVol6,radiobtnYesVol7,radiobtnYesVol8,radiobtnYesVol9,radiobtnYesVol10;
	RadioButton radiobtnNoVol1,radiobtnNoVol2,radiobtnNoVol3,radiobtnNoVol4,radiobtnNoVol5,radiobtnNoVol6,radiobtnNoVol7,radiobtnNoVol8,radiobtnNoVol9,radiobtnNoVol10;
	
	LinearLayout ll_vol_1,ll_vol_2,ll_vol_3,ll_vol_4,ll_vol_5,ll_vol_6,ll_vol_7,ll_vol_8,ll_vol_9,ll_vol_10;
	
	
	String vol1Firstname="",vol2Firstname="",vol3Firstname="",vol4Firstname="",vol5Firstname="",vol6Firstname="",vol7Firstname="",vol8Firstname="",vol9Firstname="",vol10Firstname="";
	String vol1Lastname="",vol2Lastname="",vol3Lastname="",vol4Lastname="",vol5Lastname="",vol6Lastname="",vol7Lastname="",vol8Lastname="",vol9Lastname="",vol10Lastname="";
	
	int beforeSwimCompetitionVol1=0,beforeSwimCompetitionVol2=0,beforeSwimCompetitionVol3=0,beforeSwimCompetitionVol4=0,beforeSwimCompetitionVol5=0,beforeSwimCompetitionVol6=0,beforeSwimCompetitionVol7=0,beforeSwimCompetitionVol8=0,beforeSwimCompetitionVol9=0,beforeSwimCompetitionVol10=0;
	int beforeSwimMeetsVol1=0,beforeSwimMeetsVol2=0,beforeSwimMeetsVol3=0,beforeSwimMeetsVol4=0,beforeSwimMeetsVol5=0,beforeSwimMeetsVol6=0,beforeSwimMeetsVol7=0,beforeSwimMeetsVol8=0,beforeSwimMeetsVol9=0,beforeSwimMeetsVol10=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swim_compitition_page_confirmation);
		
		initialization();
		//df = new DecimalFormat("#.##"); 
		//String twoDigitNum = df.format(myNum);
		
		//getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG,"Token="+token+"\nFamilyID="+familyID);
			
		
		if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
			new GetBasketID().execute();
		}
		
		isInternetPresent = Utility.isNetworkConnected(SwimCompititionConfirmationPageAcitivity.this);
		if(!isInternetPresent){
//			AppConfiguration.showAlertDialog(SwimCompititionConfirmationPageAcitivity.this, null, null);
			onDetectNetworkState().show();
		}
		else{
			new LoadSelectedChildDataListAsyncTask().execute();
			
			
		}
		
		txtSelectedStudent = (TextView)findViewById(R.id.txtSelectedStudent);
		txtSelectedStudent.setText("Participant:"+AppConfiguration.swimComptitionStudentName);
		
		txtMeetDate = (TextView)findViewById(R.id.txtMeetDate);
		txtMeetDate.setText(Html.fromHtml("<b>Meet Date: </b>" + AppConfiguration.SwimMeetText));
		
		
		// fetching header view

        View view = findViewById(R.id.layout_top);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Swim Competition Confirmation Page");

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(AppConfiguration.NoofVolunteersvalue == 0)
		{
			ll_vol_1.setVisibility(View.GONE);
			ll_vol_2.setVisibility(View.GONE);
			ll_vol_3.setVisibility(View.GONE);
			ll_vol_4.setVisibility(View.GONE);
			ll_vol_5.setVisibility(View.GONE);
			ll_vol_6.setVisibility(View.GONE);
			ll_vol_7.setVisibility(View.GONE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 1)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.GONE);
			ll_vol_3.setVisibility(View.GONE);
			ll_vol_4.setVisibility(View.GONE);
			ll_vol_5.setVisibility(View.GONE);
			ll_vol_6.setVisibility(View.GONE);
			ll_vol_7.setVisibility(View.GONE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 2)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.GONE);
			ll_vol_4.setVisibility(View.GONE);
			ll_vol_5.setVisibility(View.GONE);
			ll_vol_6.setVisibility(View.GONE);
			ll_vol_7.setVisibility(View.GONE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 3)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.GONE);
			ll_vol_5.setVisibility(View.GONE);
			ll_vol_6.setVisibility(View.GONE);
			ll_vol_7.setVisibility(View.GONE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 4)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.VISIBLE);
			ll_vol_5.setVisibility(View.GONE);
			ll_vol_6.setVisibility(View.GONE);
			ll_vol_7.setVisibility(View.GONE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 5)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.VISIBLE);
			ll_vol_5.setVisibility(View.VISIBLE);
			ll_vol_6.setVisibility(View.GONE);
			ll_vol_7.setVisibility(View.GONE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 6)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.VISIBLE);
			ll_vol_5.setVisibility(View.VISIBLE);
			ll_vol_6.setVisibility(View.VISIBLE);
			ll_vol_7.setVisibility(View.GONE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 7)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.VISIBLE);
			ll_vol_5.setVisibility(View.VISIBLE);
			ll_vol_6.setVisibility(View.VISIBLE);
			ll_vol_7.setVisibility(View.VISIBLE);
			ll_vol_8.setVisibility(View.GONE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 8)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.VISIBLE);
			ll_vol_5.setVisibility(View.VISIBLE);
			ll_vol_6.setVisibility(View.VISIBLE);
			ll_vol_7.setVisibility(View.VISIBLE);
			ll_vol_8.setVisibility(View.VISIBLE);
			ll_vol_9.setVisibility(View.GONE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		
		else if(AppConfiguration.NoofVolunteersvalue == 9)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.VISIBLE);
			ll_vol_5.setVisibility(View.VISIBLE);
			ll_vol_6.setVisibility(View.VISIBLE);
			ll_vol_7.setVisibility(View.VISIBLE);
			ll_vol_8.setVisibility(View.VISIBLE);
			ll_vol_9.setVisibility(View.VISIBLE);
			ll_vol_10.setVisibility(View.GONE);
			
			
		}
		else if(AppConfiguration.NoofVolunteersvalue == 10)
		{
			ll_vol_1.setVisibility(View.VISIBLE);
			ll_vol_2.setVisibility(View.VISIBLE);
			ll_vol_3.setVisibility(View.VISIBLE);
			ll_vol_4.setVisibility(View.VISIBLE);
			ll_vol_5.setVisibility(View.VISIBLE);
			ll_vol_6.setVisibility(View.VISIBLE);
			ll_vol_7.setVisibility(View.VISIBLE);
			ll_vol_8.setVisibility(View.VISIBLE);
			ll_vol_9.setVisibility(View.VISIBLE);
			ll_vol_10.setVisibility(View.VISIBLE);
			
		}
		
		
		btnAddToCart = (Button)findViewById(R.id.btnAddToCart);
		btnAddToCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(AppConfiguration.NoofVolunteersvalue == 0)
				{
					
					new AddToCartProcessAsyncTask().execute();
					
				}
				else if(AppConfiguration.NoofVolunteersvalue == 1)
				{
					
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 1;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 2)
				{
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 3)
				{
					
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 4)
				{
					
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					vol4Firstname = edtFirstnameVol4.getText().toString();
					vol4Lastname = edtLastnameVol4.getText().toString();
					
					if(radiobtnYesVol4.isChecked())
						beforeSwimCompetitionVol4 = 1;
					
					if(radiobtnNoVol4.isChecked())
						beforeSwimMeetsVol4 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty() || vol4Firstname.isEmpty() || vol4Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 5)
				{
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 =0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					vol4Firstname = edtFirstnameVol4.getText().toString();
					vol4Lastname = edtLastnameVol4.getText().toString();
					
					if(radiobtnYesVol4.isChecked())
						beforeSwimCompetitionVol4 = 1;
					
					if(radiobtnNoVol4.isChecked())
						beforeSwimMeetsVol4 = 0;
					
					vol5Firstname = edtFirstnameVol5.getText().toString();
					vol5Lastname = edtLastnameVol5.getText().toString();
					
					if(radiobtnYesVol5.isChecked())
						beforeSwimCompetitionVol5 = 1;
					
					if(radiobtnNoVol5.isChecked())
						beforeSwimMeetsVol5 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty() || vol4Firstname.isEmpty() || vol4Lastname.isEmpty() || vol5Firstname.isEmpty() || vol5Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 6)
				{
										
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					vol4Firstname = edtFirstnameVol4.getText().toString();
					vol4Lastname = edtLastnameVol4.getText().toString();
					
					if(radiobtnYesVol4.isChecked())
						beforeSwimCompetitionVol4 = 1;
					
					if(radiobtnNoVol4.isChecked())
						beforeSwimMeetsVol4 = 0;
					
					vol5Firstname = edtFirstnameVol5.getText().toString();
					vol5Lastname = edtLastnameVol5.getText().toString();
					
					if(radiobtnYesVol5.isChecked())
						beforeSwimCompetitionVol5 = 1;
					
					if(radiobtnNoVol5.isChecked())
						beforeSwimMeetsVol5 = 0;
					
					vol6Firstname = edtFirstnameVol6.getText().toString();
					vol6Lastname = edtLastnameVol6.getText().toString();
					
					if(radiobtnYesVol6.isChecked())
						beforeSwimCompetitionVol6 = 1;
					
					if(radiobtnNoVol6.isChecked())
						beforeSwimMeetsVol6 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty() || vol4Firstname.isEmpty() || vol4Lastname.isEmpty() || vol5Firstname.isEmpty() || vol5Lastname.isEmpty() || vol6Firstname.isEmpty() || vol6Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 7)
				{
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					vol4Firstname = edtFirstnameVol4.getText().toString();
					vol4Lastname = edtLastnameVol4.getText().toString();
					
					if(radiobtnYesVol4.isChecked())
						beforeSwimCompetitionVol4 = 1;
					
					if(radiobtnNoVol4.isChecked())
						beforeSwimMeetsVol4 = 0;
					
					vol5Firstname = edtFirstnameVol5.getText().toString();
					vol5Lastname = edtLastnameVol5.getText().toString();
					
					if(radiobtnYesVol5.isChecked())
						beforeSwimCompetitionVol5 = 1;
					
					if(radiobtnNoVol5.isChecked())
						beforeSwimMeetsVol5 = 0;
					
					vol6Firstname = edtFirstnameVol6.getText().toString();
					vol6Lastname = edtLastnameVol6.getText().toString();
					
					if(radiobtnYesVol6.isChecked())
						beforeSwimCompetitionVol6 = 1;
					
					if(radiobtnNoVol6.isChecked())
						beforeSwimMeetsVol6 = 0;
					
					vol7Firstname = edtFirstnameVol7.getText().toString();
					vol7Lastname = edtLastnameVol7.getText().toString();
					
					if(radiobtnYesVol7.isChecked())
						beforeSwimCompetitionVol7 = 1;
					
					if(radiobtnNoVol7.isChecked())
						beforeSwimMeetsVol7 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty() || vol4Firstname.isEmpty() || vol4Lastname.isEmpty() || vol5Firstname.isEmpty() || vol5Lastname.isEmpty() || vol6Firstname.isEmpty() || vol6Lastname.isEmpty() || vol7Firstname.isEmpty() || vol7Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 8)
				{
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					vol4Firstname = edtFirstnameVol4.getText().toString();
					vol4Lastname = edtLastnameVol4.getText().toString();
					
					if(radiobtnYesVol4.isChecked())
						beforeSwimCompetitionVol4 = 1;
					
					if(radiobtnNoVol4.isChecked())
						beforeSwimMeetsVol4 = 0;
					
					vol5Firstname = edtFirstnameVol5.getText().toString();
					vol5Lastname = edtLastnameVol5.getText().toString();
					
					if(radiobtnYesVol5.isChecked())
						beforeSwimCompetitionVol5 = 1;
					
					if(radiobtnNoVol5.isChecked())
						beforeSwimMeetsVol5 = 0;
					
					vol6Firstname = edtFirstnameVol6.getText().toString();
					vol6Lastname = edtLastnameVol6.getText().toString();
					
					if(radiobtnYesVol6.isChecked())
						beforeSwimCompetitionVol6 = 1;
					
					if(radiobtnNoVol6.isChecked())
						beforeSwimMeetsVol6 = 0;
					
					vol7Firstname = edtFirstnameVol7.getText().toString();
					vol7Lastname = edtLastnameVol7.getText().toString();
					
					if(radiobtnYesVol7.isChecked())
						beforeSwimCompetitionVol7 = 1;
					
					if(radiobtnNoVol7.isChecked())
						beforeSwimMeetsVol7 = 0;
					
					vol8Firstname = edtFirstnameVol8.getText().toString();
					vol8Lastname = edtLastnameVol8.getText().toString();
					
					if(radiobtnYesVol8.isChecked())
						beforeSwimCompetitionVol8 = 1;
					
					if(radiobtnNoVol8.isChecked())
						beforeSwimMeetsVol8 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty() || vol4Firstname.isEmpty() || vol4Lastname.isEmpty() || vol5Firstname.isEmpty() || vol5Lastname.isEmpty() || vol6Firstname.isEmpty() || vol6Lastname.isEmpty() || vol7Firstname.isEmpty() || vol7Lastname.isEmpty() || vol8Firstname.isEmpty() || vol8Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				
				else if(AppConfiguration.NoofVolunteersvalue == 9)
				{
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					vol4Firstname = edtFirstnameVol4.getText().toString();
					vol4Lastname = edtLastnameVol4.getText().toString();
					
					if(radiobtnYesVol4.isChecked())
						beforeSwimCompetitionVol4 = 1;
					
					if(radiobtnNoVol4.isChecked())
						beforeSwimMeetsVol4 = 0;
					
					vol5Firstname = edtFirstnameVol5.getText().toString();
					vol5Lastname = edtLastnameVol5.getText().toString();
					
					if(radiobtnYesVol5.isChecked())
						beforeSwimCompetitionVol5 = 1;
					
					if(radiobtnNoVol5.isChecked())
						beforeSwimMeetsVol5 = 0;
					
					vol6Firstname = edtFirstnameVol6.getText().toString();
					vol6Lastname = edtLastnameVol6.getText().toString();
					
					if(radiobtnYesVol6.isChecked())
						beforeSwimCompetitionVol6 = 1;
					
					if(radiobtnNoVol6.isChecked())
						beforeSwimMeetsVol6 = 0;
					
					vol7Firstname = edtFirstnameVol7.getText().toString();
					vol7Lastname = edtLastnameVol7.getText().toString();
					
					if(radiobtnYesVol7.isChecked())
						beforeSwimCompetitionVol7 = 1;
					
					if(radiobtnNoVol7.isChecked())
						beforeSwimMeetsVol7 = 0;
					
					vol8Firstname = edtFirstnameVol8.getText().toString();
					vol8Lastname = edtLastnameVol8.getText().toString();
					
					if(radiobtnYesVol8.isChecked())
						beforeSwimCompetitionVol8 = 1;
					
					if(radiobtnNoVol8.isChecked())
						beforeSwimMeetsVol8 = 0;
					
					vol9Firstname = edtFirstnameVol9.getText().toString();
					vol9Lastname = edtLastnameVol9.getText().toString();
					
					if(radiobtnYesVol9.isChecked())
						beforeSwimCompetitionVol9 = 1;
					
					if(radiobtnNoVol9.isChecked())
						beforeSwimMeetsVol9 = 0;
					

					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty() || vol4Firstname.isEmpty() || vol4Lastname.isEmpty() || vol5Firstname.isEmpty() || vol5Lastname.isEmpty() || vol6Firstname.isEmpty() || vol6Lastname.isEmpty() || vol7Firstname.isEmpty() || vol7Lastname.isEmpty() || vol8Firstname.isEmpty() || vol8Lastname.isEmpty() || vol9Firstname.isEmpty() || vol9Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				else if(AppConfiguration.NoofVolunteersvalue == 10)
				{
					
					vol1Firstname = edtFirstnameVol1.getText().toString();
					vol1Lastname = edtLastnameVol1.getText().toString();
					
					if(radiobtnYesVol1.isChecked())
						beforeSwimCompetitionVol1 = 1;
					
					if(radiobtnNoVol1.isChecked())
						beforeSwimMeetsVol1 = 0;
					
					vol2Firstname = edtFirstnameVol2.getText().toString();
					vol2Lastname = edtLastnameVol2.getText().toString();
					
					if(radiobtnYesVol2.isChecked())
						beforeSwimCompetitionVol2 = 1;
					
					if(radiobtnNoVol2.isChecked())
						beforeSwimMeetsVol2 = 0;
					
					vol3Firstname = edtFirstnameVol3.getText().toString();
					vol3Lastname = edtLastnameVol3.getText().toString();
					
					if(radiobtnYesVol3.isChecked())
						beforeSwimCompetitionVol3 = 1;
					
					if(radiobtnNoVol3.isChecked())
						beforeSwimMeetsVol3 = 0;
					
					vol4Firstname = edtFirstnameVol4.getText().toString();
					vol4Lastname = edtLastnameVol4.getText().toString();
					
					if(radiobtnYesVol4.isChecked())
						beforeSwimCompetitionVol4 = 1;
					
					if(radiobtnNoVol4.isChecked())
						beforeSwimMeetsVol4 = 0;
					
					vol5Firstname = edtFirstnameVol5.getText().toString();
					vol5Lastname = edtLastnameVol5.getText().toString();
					
					if(radiobtnYesVol5.isChecked())
						beforeSwimCompetitionVol5 = 1;
					
					if(radiobtnNoVol5.isChecked())
						beforeSwimMeetsVol5 = 0;
					
					vol6Firstname = edtFirstnameVol6.getText().toString();
					vol6Lastname = edtLastnameVol6.getText().toString();
					
					if(radiobtnYesVol6.isChecked())
						beforeSwimCompetitionVol6 = 1;
					
					if(radiobtnNoVol6.isChecked())
						beforeSwimMeetsVol6 = 0;
					
					vol7Firstname = edtFirstnameVol7.getText().toString();
					vol7Lastname = edtLastnameVol7.getText().toString();
					
					if(radiobtnYesVol7.isChecked())
						beforeSwimCompetitionVol7 = 1;
					
					if(radiobtnNoVol7.isChecked())
						beforeSwimMeetsVol7 = 0;
					
					vol8Firstname = edtFirstnameVol8.getText().toString();
					vol8Lastname = edtLastnameVol8.getText().toString();
					
					if(radiobtnYesVol8.isChecked())
						beforeSwimCompetitionVol8 = 1;
					
					if(radiobtnNoVol8.isChecked())
						beforeSwimMeetsVol8 = 0;
					
					vol9Firstname = edtFirstnameVol9.getText().toString();
					vol9Lastname = edtLastnameVol9.getText().toString();
					
					if(radiobtnYesVol9.isChecked())
						beforeSwimCompetitionVol9 = 1;
					
					if(radiobtnNoVol9.isChecked())
						beforeSwimMeetsVol9 = 0;
					
					vol10Firstname = edtFirstnameVol10.getText().toString();
					vol10Lastname = edtLastnameVol10.getText().toString();
					
					if(radiobtnYesVol10.isChecked())
						beforeSwimCompetitionVol10 = 1;
					
					if(radiobtnNoVol10.isChecked())
						beforeSwimMeetsVol10 = 0;
					
					if(vol1Firstname.isEmpty() || vol1Lastname.isEmpty() || vol2Firstname.isEmpty() || vol2Lastname.isEmpty() || vol3Firstname.isEmpty() || vol3Lastname.isEmpty() || vol4Firstname.isEmpty() || vol4Lastname.isEmpty() || vol5Firstname.isEmpty() || vol5Lastname.isEmpty() || vol6Firstname.isEmpty() || vol6Lastname.isEmpty() || vol7Firstname.isEmpty() || vol7Lastname.isEmpty() || vol8Firstname.isEmpty() || vol8Lastname.isEmpty() || vol9Firstname.isEmpty() || vol9Lastname.isEmpty() || vol10Firstname.isEmpty() || vol10Lastname.isEmpty())
					{
						Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					}
					else
					{
						new AddToCartProcessAsyncTask().execute();
					}
				}
				
			}
		});
		
		
		
	}

	public void initialization()
	{
		//txtReminders = (TextView)findViewById(R.id.txtReminders);
		
		//AppConfiguration.totalSelectedEvents
		txtNoofEvents = (TextView)findViewById(R.id.txtNoofEvents);
		txtNoofPricePrice = (TextView)findViewById(R.id.txtNoofPricePrice);
		txtMemberEntryFeePrice = (TextView)findViewById(R.id.txtMemberEntryFeePrice);
		txtNonMemberRegistrationPrice = (TextView)findViewById(R.id.txtNonMemberRegistrationPrice);
		txtLateRegistration = (TextView)findViewById(R.id.txtLateRegistration);
		
		txtLateRegistrationPrice = (TextView)findViewById(R.id.txtLateRegistrationPrice);
		txtTotalAmount = (TextView)findViewById(R.id.txtTotalAmount);
		txtPreviousBill = (TextView)findViewById(R.id.txtPreviousBill);
		txtTotalBalanceDue = (TextView)findViewById(R.id.txtTotalBalanceDue);
				
		edtFirstnameVol1 = (EditText)findViewById(R.id.edtFirstnameVol1);
		edtLastnameVol1 = (EditText)findViewById(R.id.edtLastnameVol1);
		
		edtFirstnameVol2 = (EditText)findViewById(R.id.edtFirstnameVol2);
		edtLastnameVol2 = (EditText)findViewById(R.id.edtLastnameVol2);
		
		edtFirstnameVol3 = (EditText)findViewById(R.id.edtFirstnameVol3);
		edtLastnameVol3 = (EditText)findViewById(R.id.edtLastnameVol3);
		
		edtFirstnameVol4 = (EditText)findViewById(R.id.edtFirstnameVol4);
		edtLastnameVol4 = (EditText)findViewById(R.id.edtLastnameVol4);
		
		edtFirstnameVol5 = (EditText)findViewById(R.id.edtFirstnameVol5);
		edtLastnameVol5 = (EditText)findViewById(R.id.edtLastnameVol5);
		
		edtFirstnameVol6 = (EditText)findViewById(R.id.edtFirstnameVol6);
		edtLastnameVol6 = (EditText)findViewById(R.id.edtLastnameVol6);
		
		edtFirstnameVol7 = (EditText)findViewById(R.id.edtFirstnameVol7);
		edtLastnameVol7 = (EditText)findViewById(R.id.edtLastnameVol7);
		
		edtFirstnameVol8 = (EditText)findViewById(R.id.edtFirstnameVol8);
		edtLastnameVol8 = (EditText)findViewById(R.id.edtLastnameVol8);
		
		edtFirstnameVol9 = (EditText)findViewById(R.id.edtFirstnameVol9);
		edtLastnameVol9 = (EditText)findViewById(R.id.edtLastnameVol9);
		
		edtFirstnameVol10 = (EditText)findViewById(R.id.edtFirstnameVol10);
		edtLastnameVol10 = (EditText)findViewById(R.id.edtLastnameVol10);
		
		radiobtnYesVol1 = (RadioButton)findViewById(R.id.radiobtnYesVol1);
		radiobtnNoVol1 = (RadioButton)findViewById(R.id.radiobtnNoVol1);
		
		radiobtnYesVol2 = (RadioButton)findViewById(R.id.radiobtnYesVol2);
		radiobtnNoVol2 = (RadioButton)findViewById(R.id.radiobtnNoVol2);
		
		radiobtnYesVol3 = (RadioButton)findViewById(R.id.radiobtnYesVol3);
		radiobtnNoVol3 = (RadioButton)findViewById(R.id.radiobtnNoVol3);
		
		radiobtnYesVol4 = (RadioButton)findViewById(R.id.radiobtnYesVol4);
		radiobtnNoVol4 = (RadioButton)findViewById(R.id.radiobtnNoVol4);
		
		radiobtnYesVol5 = (RadioButton)findViewById(R.id.radiobtnYesVol5);
		radiobtnNoVol5 = (RadioButton)findViewById(R.id.radiobtnNoVol5);
		
		radiobtnYesVol6 = (RadioButton)findViewById(R.id.radiobtnYesVol6);
		radiobtnNoVol6 = (RadioButton)findViewById(R.id.radiobtnNoVol6);
		
		radiobtnYesVol7 = (RadioButton)findViewById(R.id.radiobtnYesVol7);
		radiobtnNoVol7 = (RadioButton)findViewById(R.id.radiobtnNoVol7);
		
		radiobtnYesVol8 = (RadioButton)findViewById(R.id.radiobtnYesVol8);
		radiobtnNoVol8 = (RadioButton)findViewById(R.id.radiobtnNoVol8);
		
		radiobtnYesVol9 = (RadioButton)findViewById(R.id.radiobtnYesVol9);
		radiobtnNoVol9 = (RadioButton)findViewById(R.id.radiobtnNoVol9);
		
		radiobtnYesVol10 = (RadioButton)findViewById(R.id.radiobtnYesVol10);
		radiobtnNoVol10 = (RadioButton)findViewById(R.id.radiobtnNoVol10);
		
		ll_vol_1 = (LinearLayout)findViewById(R.id.ll_vol_1);
		ll_vol_2 = (LinearLayout)findViewById(R.id.ll_vol_2);
		ll_vol_3 = (LinearLayout)findViewById(R.id.ll_vol_3);
		ll_vol_4 = (LinearLayout)findViewById(R.id.ll_vol_4);
		ll_vol_5 = (LinearLayout)findViewById(R.id.ll_vol_5);
		ll_vol_6 = (LinearLayout)findViewById(R.id.ll_vol_6);
		ll_vol_7 = (LinearLayout)findViewById(R.id.ll_vol_7);
		ll_vol_8 = (LinearLayout)findViewById(R.id.ll_vol_8);
		ll_vol_9 = (LinearLayout)findViewById(R.id.ll_vol_9);
		ll_vol_10 = (LinearLayout)findViewById(R.id.ll_vol_10);
		
		
	}
	
	public void loadingChildList() {

		HashMap<String, String > param = new HashMap<String, String>();
		param.put("Token",token );
		param.put("MeetdatetimeValue", AppConfiguration.SwimMeetID);
		param.put("ChkChildListStp1", getChkChildListStp1Value());
//		param.put("ChkChildListStp1", AppConfiguration.swimComptitionStudentID+":"+AppConfiguration.swimComptitionStudentName);
		param.put("ChkChilsWiseDtlStp2", ""+AppConfiguration.swimComptitionStudentID);
		param.put("EventCounter", ""+AppConfiguration.totalSelectedEvents);
		
		String responseString = WebServicesCall
		.RunScript(AppConfiguration.swimCompititionConfirmationCalculation, param);
		
		readAndParseJSONReminderText(responseString);
		
	}

	public void readAndParseJSONReminderText(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successLoadChildList = reader.getString("Success");
			if (successLoadChildList.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck5_EventCalc");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					HashMap<String, String> hashmap = new HashMap<String, String>();

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					
					String wwmember = jsonChildNode.getString("wwmember").trim();
					String EventFees = jsonChildNode.getString("EventFees").trim();
					String EntryFeee = jsonChildNode.getString("EntryFeee").trim();
					String wwtype = jsonChildNode.getString("wwtype").trim();
					String EventFee = jsonChildNode.getString("EventFee").trim();
					String LateRegisterFee = jsonChildNode.getString("LateRegisterFee").trim();
					String Nonmember = jsonChildNode.getString("Nonmember").trim();
					String EntryFee = jsonChildNode.getString("EntryFee").trim();
					String Total = jsonChildNode.getString("Total").trim();
					String invoiceAmount = jsonChildNode.getString("invoiceAmount").trim();
					String TotalAmountduefornewinvoice = jsonChildNode.getString("TotalAmountduefornewinvoice").trim();
					
					hashmap.put("wwmember", wwmember);
					hashmap.put("EventFees", EventFees);
					hashmap.put("EntryFeee", EntryFeee);
					hashmap.put("wwtype", wwtype);
					hashmap.put("EventFee", EventFee);
					hashmap.put("LateRegisterFee", LateRegisterFee);
					hashmap.put("Nonmember", Nonmember);
					hashmap.put("EntryFee", EntryFee);
					hashmap.put("Total", Total);
					hashmap.put("invoiceAmount", invoiceAmount);
					hashmap.put("TotalAmountduefornewinvoice", TotalAmountduefornewinvoice);
					confirmationPage.add(hashmap);
				}

			} else {
				JSONArray jsonMainNode = reader.optJSONArray("SwimMeetCheck2");

				for (int i = 0; i < jsonMainNode.length(); i++) {

					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					
					message = jsonChildNode.getString("Msg").trim();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
		builder1.setIcon(R.drawable.logo);
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								finish();
							}
						})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		return builder1.create();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.swim_camps, menu);
		return true;
	}
	
	class LoadSelectedChildDataListAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(SwimCompititionConfirmationPageAcitivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

			confirmationPage.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			loadingChildList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			
			if (successLoadChildList.toString().equals("True")) {
				//				CustomList adapter = new CustomList(SwimCompititionConfirmationPageAcitivity.this,AppConfiguration.selectedChildDataMainList);
//				list.setAdapter(adapter);
				txtNoofEvents.setText("Total Number of Event Entered (# of total events selected)"+AppConfiguration.totalSelectedEvents +" x "+confirmationPage.get(0).get("EventFees"));
				
				txtNoofPricePrice.setText(AppConfiguration.currency+confirmationPage.get(0).get("EventFee"));
				double enryFee = Double.valueOf(confirmationPage.get(0).get("EntryFeee"));
				txtMemberEntryFeePrice.setText(AppConfiguration.currency+(enryFee*AppConfiguration.totalSelectedStudent));
				txtNonMemberRegistrationPrice.setText(AppConfiguration.currency+confirmationPage.get(0).get("Nonmember"));
				txtLateRegistration.setText("Late Registration: (# of students selected)"+AppConfiguration.totalSelectedStudent+" x $"+confirmationPage.get(0).get("LateRegisterFee")+
						"(This will be charged if you register the Same Day or after 8:15 the night before the swim competition)");
				txtLateRegistrationPrice.setText(AppConfiguration.currency+confirmationPage.get(0).get("LateRegisterFee"));
				txtTotalAmount.setText(AppConfiguration.currency+confirmationPage.get(0).get("Total"));
				txtPreviousBill.setText(AppConfiguration.currency+confirmationPage.get(0).get("invoiceAmount"));
				txtTotalBalanceDue.setText(AppConfiguration.currency+confirmationPage.get(0).get("TotalAmountduefornewinvoice"));
				
				memberID = confirmationPage.get(0).get("wwmember");
				

			} else {
				Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	
	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(SwimCompititionConfirmationPageAcitivity.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("siteid", "0");

			String responseString = WebServicesCall
			.RunScript(AppConfiguration.Get_BasketID, param);
			GetBasketID(responseString);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if(data_load_basket.toString().equalsIgnoreCase("True")){
				if(AppConfiguration.BasketID.equalsIgnoreCase("0")){
//					Toast.makeText(TransferMakeUpActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
				}
				else{
//					new swimlessonsubmit().execute();
				}
			}
			else{
//				Toast.makeText(TransferMakeUpActivity.this, "Please try after sometime", Toast.LENGTH_LONG).show();
			}
		}
	}
	public void GetBasketID(String response){
		try {
			JSONObject reader = new JSONObject(response);
			data_load_basket = reader.getString("Success");
			if(data_load_basket.toString().equals("True"))
			{
				 JSONArray jsonMainNode = reader.optJSONArray("BasketDtl");
		         if(jsonMainNode.toString().equalsIgnoreCase("")){
		         
		         }
		         else{
			         for(int i = 0 ;i < jsonMainNode.length();i++)
			         {
			        	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				        AppConfiguration.BasketID = jsonChildNode.getString("Basketid");
			         }
		         }
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	///================================= ADD TO CART ==================================================
	
	
	public String getChkChildListStp1Value(){
		String ChkChildListStp1="";
		String[] temp_id = AppConfiguration.swimComptitionStudentID.toString().split(",");
		String[] temp_name = AppConfiguration.swimComptitionStudentName.toString().split(",");
		String member="";
		for (int i = 0; i < temp_name.length; i++) {
			if (i==0) {
				ChkChildListStp1 = temp_id[i]+":"+temp_name[i];
				member = memberID+"|"+temp_id[i];
			}else{
				ChkChildListStp1 =ChkChildListStp1+","+temp_id[i]+":"+temp_name[i];
				member = member+","+memberID+"|"+temp_id[i];
						
			}
		}
		return ChkChildListStp1;
	}
	public void addToCartProcess() {

		String swimMeetID = AppConfiguration.SwimMeetID;
		String[] separated = swimMeetID.split("\\|");
		separated[0] = separated[0]; 
		separated[1] = separated[1];
		
		String finalSwimMeetID = separated[1];
		Log.e("AppConfiguration.SwimMeetID",AppConfiguration.SwimMeetID);
		
		Log.e("SwimID",finalSwimMeetID);
		
		String ChkChildListStp1="";
//		AppConfiguration.swimComptitionStudentID+":"+AppConfiguration.swimComptitionStudentName
		String[] temp_id = AppConfiguration.swimComptitionStudentID.toString().split(",");
		String[] temp_name = AppConfiguration.swimComptitionStudentName.toString().split(",");
		String member="";
		for (int i = 0; i < temp_name.length; i++) {
			if (i==0) {
				ChkChildListStp1 = temp_id[i]+":"+temp_name[i];
				member = memberID+"|"+temp_id[i];
			}else{
				ChkChildListStp1 =ChkChildListStp1+","+temp_id[i]+":"+temp_name[i];
				member = member+","+memberID+"|"+temp_id[i];
						
			}
		}
		
		HashMap<String, String > param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("MeetdatetimeValue", AppConfiguration.SwimMeetID);
        param.put("NoofVolunteersvalue", ""+AppConfiguration.NoofVolunteersvalue);
        param.put("flag", "0");
        param.put("swimmeetid", finalSwimMeetID);
        param.put("basketid", AppConfiguration.BasketID);
        param.put("ChkChildListStp1",ChkChildListStp1 );
        param.put("RdbChildLstStp3_1", SwimCompitition3Acitivity.step3_rg1.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
        param.put("RdbChildLstStp3_2", SwimCompitition3Acitivity.step3_rg2.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
        param.put("member", member);
        param.put("SelectedEventDataStep2", ""+AppConfiguration.SelectedEventDataStep2 );
        
        param.put("FirstName1", ""+vol1Firstname );
        param.put("LastName1", "" +vol1Lastname);
        param.put("RdbComp1", ""+beforeSwimCompetitionVol1 );
        param.put("RdbMeet1", ""+beforeSwimMeetsVol1 );
        
        param.put("FirstName2", ""+vol2Firstname );
        param.put("LastName2",  "" +vol2Lastname );
        param.put("RdbComp2", ""+beforeSwimCompetitionVol2);
        param.put("RdbMeet2", ""+beforeSwimMeetsVol2 );
        
        param.put("FirstName3", ""+vol3Firstname );
        param.put("LastName3",  "" +vol3Lastname);
        param.put("RdbComp3", ""+beforeSwimCompetitionVol3 );
        param.put("RdbMeet3", ""+beforeSwimMeetsVol3);
        
        param.put("FirstName4", ""+vol4Firstname);
        param.put("LastName4",  "" +vol4Lastname );
        param.put("RdbComp4", ""+beforeSwimCompetitionVol4 );
        param.put("RdbMeet4", ""+beforeSwimMeetsVol4 );
        
        param.put("FirstName5", ""+vol5Firstname );
        param.put("LastName5",  "" +vol5Lastname );
        param.put("RdbComp5", ""+beforeSwimCompetitionVol5 );
        param.put("RdbMeet5", ""+beforeSwimMeetsVol5 );
        
        param.put("FirstName6", ""+vol6Firstname );
        param.put("LastName6",  "" +vol6Lastname );
        param.put("RdbComp6", ""+beforeSwimCompetitionVol6 );
        param.put("RdbMeet6", ""+beforeSwimMeetsVol6);
        
        param.put("FirstName7", ""+vol7Firstname );
        param.put("LastName7",  "" +vol7Lastname);
        param.put("RdbComp7", ""+beforeSwimCompetitionVol7 );
        param.put("RdbMeet7", ""+beforeSwimMeetsVol7);
        
        param.put("FirstName8", ""+vol8Firstname );
        param.put("LastName8",  "" +vol8Lastname );
        param.put("RdbComp8", ""+beforeSwimCompetitionVol8);
        param.put("RdbMeet8", ""+beforeSwimMeetsVol8 );
        
        param.put("FirstName9", ""+vol9Firstname );
        param.put("LastName9",  "" +vol9Lastname);
        param.put("RdbComp9", ""+beforeSwimCompetitionVol9 );
        param.put("RdbMeet9", ""+beforeSwimMeetsVol9 );
        
        param.put("FirstName10", ""+vol10Firstname );
        param.put("LastName10",  "" +vol10Lastname );
        param.put("RdbComp10", ""+beforeSwimCompetitionVol10 );
        param.put("RdbMeet10", ""+beforeSwimMeetsVol10);
        String responseString = WebServicesCall
        		.RunScript(AppConfiguration.swimCompititionAddToCart, param);
        JSONObject reader;
        try {
			reader = new JSONObject(responseString);
			successAddToCart = reader.getString("Success");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	class AddToCartProcessAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(SwimCompititionConfirmationPageAcitivity.this);
			pd.setMessage(getResources().getString(R.string.pleasewait));
			pd.setCancelable(false);
			pd.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			addToCartProcess();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			
			if (successAddToCart.toString().equals("True")) {

				AppConfiguration.fromSwimCompetition = "true";

				Intent viewcart = new Intent(getApplicationContext(), ViewCartActivity.class);
//				viewcart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				viewcart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				viewcart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(viewcart);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Some internal error, Please try after sometime.", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
}
