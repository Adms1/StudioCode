package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

public class ReceivePayment extends Activity{

	RelativeLayout new_pay_button,saved_pay_button; 
	ImageView new_pay_rotate,save_rotate;
	LinearLayout new_pay_lay_hide,check_lay,credit_card_lay,saved_method_inflate;
	RadioButton pay_by_check,pay_by_card,recurring,one_time_pay;
	RadioGroup full_half_payment,paygroup_options;
	LinearLayout conditions_scroll;
	Button confirm_payment,info_button_b;
	CheckBox save_detail,agreed,save_card_detail;
	String token,FamilyID,data_load_empty= "False",data_load_basket="False",Success_St="False";
	Context mContext=this;
	String DOMAIN = AppConfiguration.DOMAIN;
	ProgressDialog pd;
	//Pay by Check vars
	EditText name_check,routing_number,acc_number,bank_name,address_1,address_2,city,state,zipcode;
	Spinner account_type;
	boolean saved_detail=false;
	//Pay by Credit Card vars
	EditText f_name,l_name,c_number,cvv_number,c_address_1,c_address_2,c_city,c_state,c_zipcode;
	Spinner card_type,month,year;
	public static HashMap<String, String> netxt_page_value = new HashMap<String, String>();
	public static boolean pay_type_card=false;
	//Arraylists
	ArrayList<String> Confirm_msg_arr = new ArrayList<String>();
	ArrayList<String> rec_value_arr = new ArrayList<String>();
	ArrayList<String> rec_sh_arr = new ArrayList<String>();
	ArrayList<String> _wu_ClientName,_wu_CardAccNumber,_wu_PayType,_wu_ExpDate,_wu_BankName,_wu_BankRtngNum,_wu_PayTypeID,_wu_PmtID;
	ImageButton ib_back;
	public static String check_detail="";
	public static String card_detail="",address="",cred_exp="";
	Boolean isInternetPresent = false;
	
	//Custom Saved card layout
	RadioButton ahc_selection;
	RadioGroup ahc_selection_group;
	TextView CardAccNumber,pay_type,expdate,conditions_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_payment);

		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
		token = prefs.getString("Token", "");
		FamilyID= prefs.getString("FamilyID", "");
		init();


	}

	public void init(){
		View view = findViewById(R.id.layout_top);
		ib_back = (ImageButton)view.findViewById(R.id.ib_back);
		
		conditions_text = (TextView)findViewById(R.id.conditions_text);
		conditions_scroll = (LinearLayout)findViewById(R.id.conditions_scroll);

		new_pay_button = (RelativeLayout)findViewById(R.id.new_pay_button);
		saved_pay_button = (RelativeLayout)findViewById(R.id.saved_pay_button);

		new_pay_rotate = (ImageView)findViewById(R.id.new_pay_rotate);
		save_rotate = (ImageView)findViewById(R.id.save_rotate);

		new_pay_lay_hide = (LinearLayout)findViewById(R.id.new_pay_lay_hide);
		check_lay = (LinearLayout)findViewById(R.id.check_lay);
		credit_card_lay = (LinearLayout)findViewById(R.id.credit_card_lay);
		saved_method_inflate = (LinearLayout)findViewById(R.id.saved_method_inflate);

		pay_by_card = (RadioButton)findViewById(R.id.pay_by_card);
		pay_by_check = (RadioButton)findViewById(R.id.pay_by_check);
		one_time_pay = (RadioButton)findViewById(R.id.one_time_pay);
		recurring = (RadioButton)findViewById(R.id.recurring);

		full_half_payment = (RadioGroup)findViewById(R.id.full_half_payment);
		paygroup_options = (RadioGroup)findViewById(R.id.paygroup_options);
		ahc_selection_group = (RadioGroup)findViewById(R.id.ahc_selection_group);

		confirm_payment = (Button)findViewById(R.id.confirm_payment);
		info_button_b = (Button)findViewById(R.id.info_button_b);

		agreed = (CheckBox)findViewById(R.id.agreed);
		save_card_detail = (CheckBox)findViewById(R.id.save_card_detail);
		save_detail = (CheckBox)findViewById(R.id.save_detail);

		//Pay by Check vars
		name_check = (EditText)findViewById(R.id.name_check);
		routing_number = (EditText)findViewById(R.id.routing_number);
		bank_name = (EditText)findViewById(R.id.bank_name);
		acc_number = (EditText)findViewById(R.id.acc_number);
		address_1 = (EditText)findViewById(R.id.address_1);
		address_2 = (EditText)findViewById(R.id.address_2);
		city = (EditText)findViewById(R.id.city);
		state = (EditText)findViewById(R.id.state);
		zipcode = (EditText)findViewById(R.id.zipcode);

		account_type = (Spinner)findViewById(R.id.account_type);


		//Pay by Credit Card vars
		f_name = (EditText)findViewById(R.id.f_name);
		l_name = (EditText)findViewById(R.id.l_name);
		c_number = (EditText)findViewById(R.id.c_number);
		cvv_number = (EditText)findViewById(R.id.cvv_number);
		c_address_1 = (EditText)findViewById(R.id.c_address_1);
		c_address_2 = (EditText)findViewById(R.id.c_address_2);
		c_city = (EditText)findViewById(R.id.c_city);
		c_state = (EditText)findViewById(R.id.c_state);
		c_zipcode = (EditText)findViewById(R.id.c_zipcode);

		card_type = (Spinner)findViewById(R.id.card_type);
		month = (Spinner)findViewById(R.id.month);
		year = (Spinner)findViewById(R.id.year);

		int maxLength = 19;
		c_number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

		if(AppConfiguration.BasketID.toString().equalsIgnoreCase("0")){
			Toast.makeText(getApplicationContext(), "No balance due..", Toast.LENGTH_LONG).show();
		}else{
			isInternetPresent = Utility.isNetworkConnected(ReceivePayment.this);
			if (!isInternetPresent) {
				onDetectNetworkState().show();
			} else {
				new call_startup_services().execute();
			}
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
		// TODO Auto-generated method stub
		super.onResume();

		check_detail="";
		card_detail="";


				ib_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		info_button_b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pay_by_card.isChecked()){
					Intent i = new Intent(mContext,Info_Help.class);
					i.putExtra("which", "card");
					startActivity(i);
				}else if(pay_by_check.isChecked()){
					Intent i = new Intent(mContext,Info_Help.class);
					i.putExtra("which", "check");
					startActivity(i);
				}else{
					ping(mContext, "Please select Payment type");
				}
			}
		});


		new_pay_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new_pay_rotate.startAnimation(AnimationUtils.loadAnimation(ReceivePayment.this, R.drawable.rotate_down));
				save_rotate.startAnimation(AnimationUtils.loadAnimation(ReceivePayment.this, R.drawable.rotate_up));

				new_pay_lay_hide.setVisibility(View.VISIBLE);
				saved_method_inflate.setVisibility(View.GONE);

				full_half_payment.clearCheck();
			}
		});

		saved_pay_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(saved_detail==false){
					ping(mContext, "No saved detail found.");
				}else{
					full_half_payment.setVisibility(View.VISIBLE);
					agreed.setVisibility(View.VISIBLE);
					conditions_scroll.setVisibility(View.VISIBLE);

					confirm_payment.setVisibility(View.VISIBLE);

					new_pay_rotate.startAnimation(AnimationUtils.loadAnimation(ReceivePayment.this, R.drawable.rotate_up));
					save_rotate.startAnimation(AnimationUtils.loadAnimation(ReceivePayment.this, R.drawable.rotate_down));

					new_pay_lay_hide.setVisibility(View.GONE);
					saved_method_inflate.setVisibility(View.VISIBLE);
				}
			}
		});

		full_half_payment.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub


			}
		});

		c_zipcode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
					if(s.length()>4){
						full_half_payment.setVisibility(View.VISIBLE);
						agreed.setVisibility(View.VISIBLE);
						conditions_scroll.setVisibility(View.VISIBLE);
						confirm_payment.setVisibility(View.VISIBLE);
					}else{
						full_half_payment.setVisibility(View.GONE);
						agreed.setVisibility(View.GONE);
						conditions_scroll.setVisibility(View.GONE);
						confirm_payment.setVisibility(View.GONE);
					}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		zipcode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

					if(s.length()>4){
						full_half_payment.setVisibility(View.VISIBLE);
						agreed.setVisibility(View.VISIBLE);
						conditions_scroll.setVisibility(View.VISIBLE);
						confirm_payment.setVisibility(View.VISIBLE);
					}else{
						full_half_payment.setVisibility(View.GONE);
						agreed.setVisibility(View.GONE);
						conditions_scroll.setVisibility(View.GONE);
						confirm_payment.setVisibility(View.GONE);
					}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		paygroup_options.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(pay_by_card.isChecked()){
					check_lay.setVisibility(View.GONE);
					credit_card_lay.setVisibility(View.VISIBLE);
					c_zipcode.setText("");
				}else if(pay_by_check.isChecked()){
					check_lay.setVisibility(View.VISIBLE);
					credit_card_lay.setVisibility(View.GONE);
					zipcode.setText("");
				}
			}
		});


		confirm_payment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(new_pay_lay_hide.getVisibility()==View.VISIBLE){
					if(pay_by_card.isChecked()){
						if(pay_credit_chk()){
							if(agreed.isChecked()){
								isInternetPresent = Utility.isNetworkConnected(ReceivePayment.this);
								if (!isInternetPresent) {
									onDetectNetworkState().show();
								} else {
									new send_detail().execute();
								}
							}else{
								ping(mContext, "Please check agree button");
							}
						}
					}else if(pay_by_check.isChecked()){
						if(pay_check_chk()){
							if(agreed.isChecked()){
								isInternetPresent = Utility.isNetworkConnected(ReceivePayment.this);
								if (!isInternetPresent) {
									onDetectNetworkState().show();
								} else {
									new send_detail().execute();
								}
							}else{
								ping(mContext, "Please check agree button");
							}
						}
					}
				}else if(saved_method_inflate.getVisibility()==View.VISIBLE){
					if(agreed.isChecked()){
						if(ahc_selection_group.getChildCount()>0){

							int radioButtonID = ahc_selection_group.getCheckedRadioButtonId();
							View radioButton = ahc_selection_group.findViewById(radioButtonID);
							int idx = ahc_selection_group.indexOfChild(radioButton);
							if(idx!=-1){
								isInternetPresent = Utility.isNetworkConnected(ReceivePayment.this);
								if (!isInternetPresent) {
									onDetectNetworkState().show();
								} else {
									new send_detail().execute();
								}
							}else{
								ping(mContext, "Please select atleast one saved Account detail.");
							}
						}else{
							ping(mContext, "Sorry ! No saved credentials found.");
						}
					}else{
						ping(mContext, "Please check agree button");
					}

				}

			}
		});
		c_number.addTextChangedListener(new FourDigitCardFormatWatcher());
		c_number.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(c_number.getText().toString().trim().length()>0){
					if(c_number.getText().toString().trim().length()<19){
						Toast.makeText(mContext, "Card number is not Valid.", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

	}

	public class call_startup_services extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ReceivePayment.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//			new GetBasketID().execute();
			new confirm_msg().execute();
			new get_AHC_Detail().execute();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	public boolean pay_credit_chk(){
		boolean all_done=false;

		if(f_name.getText().toString().trim().length()>0){
			if(l_name.getText().toString().length()>0){
				if(c_number.getText().toString().length()>=19){
					if(cvv_number.getText().toString().length()>0){
						if(c_address_1.getText().toString().length()>0){
							if(c_city.getText().toString().length()>0){
								if(c_state.getText().toString().length()>0){
									if(c_zipcode.getText().toString().length()>0){
										all_done=true;
									}else{
										ping(mContext, "Zipcode is missing.");
									}
								}else{
									ping(mContext, "State is missing.");
								}
							}else{
								ping(mContext, "City name is missing.");
							}
						}else{
							ping(mContext, "Address is missing.");
						}
					}else{
						ping(mContext, "CVV is missing.");
					}
				}else{
					ping(mContext, "Card Number is not valid.");
				}
			}else{
				ping(mContext, "Last name is missing.");
			}
		}else{
			ping(mContext, "First name is missing.");
		}
		return all_done;
	}

	public boolean pay_check_chk(){
		boolean all_done=false;
		if(name_check.getText().toString().trim().length()>0){
			if(routing_number.getText().toString().trim().length()>0){
				if(acc_number.getText().toString().trim().length()>0){
					if(bank_name.getText().toString().trim().length()>0){
						if(address_1.getText().toString().trim().length()>0){
							if(city.getText().toString().trim().length()>0){
								if(state.getText().toString().trim().length()>0){
									if(zipcode.getText().toString().trim().length()>0){
										all_done=true;
									}else{
										ping(mContext, "Zipcode is missing.");
									}
								}else{
									ping(mContext, "State is missing.");
								}
							}else{
								ping(mContext, "City is missing.");
							}
						}else{
							ping(mContext, "Address is missing.");
						}
					}else{
						ping(mContext, "Bank name is missing.");
					}
				}else{
					ping(mContext, "Account number is missing.");
				}
			}else{
				ping(mContext, "Routing number is missing.");
			}
		}else{
			ping(mContext, "Name is missing.");
		}

		return all_done;
	}

	public String et_valu(EditText et){
		String value="";
		value = et.getText().toString().trim();
		return value;
	}
	public String spinner_value(Spinner sp){
		String value="";
		value = sp.getSelectedItem().toString();
		return value;
	}


	public void ping(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static class FourDigitCardFormatWatcher implements TextWatcher {

		// Change this to what you want... ' ', '-' etc..
		private static final char space = '-';

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Remove spacing char
			if (s.length() > 0 && (s.length() % 5) == 0) {
				final char c = s.charAt(s.length() - 1);
				if (space == c) {
					s.delete(s.length() - 1, s.length());
				}
			}
			// Insert char where needed.
			if (s.length() > 0 && (s.length() % 5) == 0) {
				char c = s.charAt(s.length() - 1);
				// Only if its a digit where there should be a space we insert a space
				if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
					s.insert(s.length() - 1, String.valueOf(space));
				}
			}
		}
	}

	//Conditions
	public class confirm_msg extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//Show progress dialogue
		}
		@Override
		protected Void doInBackground(Void... para1ms) {
			// TODO Auto-generated method stub
			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
			params.put("Basketid", AppConfiguration.BasketID);
					
			String responseString = WebServicesCall.RunScript(DOMAIN+AppConfiguration.Dis_confirm_msg, params);
			try {
				JSONObject obj = new JSONObject(responseString);
				String success = obj.getString("Success");

				if(success.equals("True")){
					JSONArray ConformMsgDisplay = obj.getJSONArray("ConformMsgDisplay");

					if(ConformMsgDisplay.length()>0){
						for (int i = 0; i < ConformMsgDisplay.length(); i++) {
							JSONObject confm = ConformMsgDisplay.getJSONObject(i);
							Confirm_msg_arr.add(confm.getString("ConformationMsg"));
							rec_sh_arr.add(confm.getString("RecurringSH"));
							rec_value_arr.add(confm.getString("RecurringVal"));
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			//Set condition message
			if(Confirm_msg_arr.size()>0){
				conditions_text.setText(Confirm_msg_arr.get(0));
			}
		}
	}

	public void dismiss_it(){
		for (int i = 0; i < 10; i++) {
			if(pd !=null && pd.isShowing() ){
				pd.dismiss();
			}
		}
	}

	//BasketID
	public class GetBasketID extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... pa1rams) {
			// TODO Auto-generated method stub
			HashMap<String, String > params = new HashMap<String, String>();
			params.put("Token",token );
			params.put("siteid", "1");
			String responseString = "";
			
			if(AppConfiguration.Get_BasketID.contains("http")){
				responseString	= WebServicesCall.RunScript(AppConfiguration.Get_BasketID, params);
			}else{
				responseString	= WebServicesCall.RunScript(DOMAIN+AppConfiguration.Get_BasketID, params);
			}
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
					Toast.makeText(ReceivePayment.this, "Please try after sometime", Toast.LENGTH_LONG).show();
				}
			}
			else{
				Toast.makeText(ReceivePayment.this, "Please try after sometime", Toast.LENGTH_LONG).show();
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

	//AHC Card Details
	public class get_AHC_Detail extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("Basketid", AppConfiguration.BasketID);
					
			String responseString = WebServicesCall.RunScript(DOMAIN+AppConfiguration.AHC_Card_detail, param);
			try {
				JSONObject obj = new JSONObject(responseString);
				Success_St= obj.getString("Success");

				if(Success_St.equals("True")){
					JSONArray ACHList = obj.getJSONArray("ACHList");

					_wu_ClientName = new ArrayList<String>();
					_wu_CardAccNumber = new ArrayList<String>();
					_wu_PayType = new ArrayList<String>();
					_wu_ExpDate = new ArrayList<String>();
					_wu_BankName = new ArrayList<String>();
					_wu_BankRtngNum = new ArrayList<String>();
					_wu_PayTypeID = new ArrayList<String>();
					_wu_PmtID = new ArrayList<String>();
					if(ACHList.length()>0){
						saved_detail = true;
						for (int i = 0; i < ACHList.length(); i++) {
							JSONObject confm = ACHList.getJSONObject(i);
							_wu_ClientName.add(confm.getString("wu_ClientName"));	
							_wu_CardAccNumber.add(confm.getString("wu_CardAccNumber"));
							_wu_PayType.add(confm.getString("wu_PayType"));
							_wu_ExpDate.add(confm.getString("wu_ExpDate"));
							_wu_BankName.add(confm.getString("wu_BankName"));
							_wu_BankRtngNum.add(confm.getString("wu_BankRtngNum"));
							_wu_PayTypeID.add(confm.getString("wu_PayTypeID"));
							_wu_PmtID.add(confm.getString("wu_PmtID"));
						}
					}else{
						saved_detail=false;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Exceptions", Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(Success_St.contains("True")){
				if(_wu_CardAccNumber.size()>0){
					AHCDetail_lay_set();
					save_rotate.startAnimation(AnimationUtils.loadAnimation(ReceivePayment.this, R.drawable.rotate_down));
					new_pay_rotate.startAnimation(AnimationUtils.loadAnimation(ReceivePayment.this, R.drawable.rotate_up));

					new_pay_lay_hide.setVisibility(View.GONE);
					saved_method_inflate.setVisibility(View.VISIBLE);
				}
			}else{
				new_pay_rotate.startAnimation(AnimationUtils.loadAnimation(ReceivePayment.this, R.drawable.rotate_down));
				new_pay_lay_hide.setVisibility(View.VISIBLE);
			}
			dismiss_it();
		}
	}

	public void AHCDetail_lay_set(){
		saved_method_inflate.setVisibility(View.VISIBLE);
		ahc_selection_group.removeAllViews();
		full_half_payment.setVisibility(View.VISIBLE);
		agreed.setVisibility(View.VISIBLE);
		conditions_scroll.setVisibility(View.VISIBLE);

		confirm_payment.setVisibility(View.VISIBLE);

		for (int i = 0; i < _wu_CardAccNumber.size(); i++) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View view = inflater.inflate(R.layout.custom_radio_button, null);

			ahc_selection = (RadioButton)view.findViewById(R.id.ahc_selection);
			ahc_selection.setId(i);
			String text="";
			if(_wu_PayType.get(i).contains("ACH")){
				text = _wu_ClientName.get(i) + "\n" +_wu_CardAccNumber.get(i) +"\n" +_wu_PayType.get(i) +"\n" +_wu_BankRtngNum.get(i) +"\n" + _wu_BankName.get(i);
			}else{
				text = _wu_ClientName.get(i) + "\n" +_wu_CardAccNumber.get(i) +"\n" +_wu_PayType.get(i) +"\n" +_wu_ExpDate.get(i)+"\n" +_wu_BankRtngNum.get(i)+"\n" + _wu_BankName.get(i);
			}
			ahc_selection.setText(text);
			ahc_selection_group.addView(view,i);
		}
	}

	public String get_address(){
		String final_address="";
		if(pay_by_card.isChecked()){
			final_address = et_valu(c_address_1)+et_valu(c_address_2);
		}else if(pay_by_check.isChecked()){
			final_address = et_valu(address_1)+et_valu(address_2);
		}
		return final_address;
	}

	public String get_payment_type(){
		String type="";
		if(pay_by_card.isChecked()){
			type="1";
		}else if(pay_by_check.isChecked()){
			type="0";
		}
		return type;
	}

	public String get_save_detail(){
		String save = "false";
		if(pay_by_check.isChecked()){
			if(save_detail.isChecked()){
				save="true";
			}
		}
		return save;
	}

	public String get_save_card_detail(){
		String save = "false";
		if(pay_by_card.isChecked()){
			if(save_card_detail.isChecked()){
				save="true";
			}
		}
		return save;
	}

	public String getCheckDetail(){
		
		if(pay_by_check.isChecked()){
			check_detail = et_valu(name_check) +"|" + et_valu(routing_number) +"|" + et_valu(acc_number) +"|" + 
					et_valu(bank_name) +"|" + spinner_value(account_type) +"|" + et_valu(address_1) +"|" + 
					et_valu(address_2) +"|" + et_valu(city) +"|" + et_valu(state) +"|" + 
					et_valu(zipcode);
			pay_type_card=false;
			netxt_page_value.put("check_name", et_valu(name_check));
			netxt_page_value.put("r_number",et_valu(routing_number) );
			netxt_page_value.put("acc_number",et_valu(acc_number) );
			netxt_page_value.put("bank_name",et_valu(bank_name) );
			netxt_page_value.put("acc_type",spinner_value(account_type) );
			netxt_page_value.put("ad_1", et_valu(address_1));
			netxt_page_value.put("ad_2",et_valu(address_2) );
			netxt_page_value.put("city",et_valu(city) );
			netxt_page_value.put("state",et_valu(state) );
			netxt_page_value.put("zip",et_valu(zipcode) );
		}
		return check_detail;
	}

	public String getCardDetail(){

		if(pay_by_card.isChecked()){
			card_detail = et_valu(f_name) +"|" + et_valu(l_name) +"|" + et_valu(c_number) +"|" + 
					spinner_value(card_type) +"|" + et_valu(cvv_number) +"|" + spinner_value(month) +"|" + spinner_value(year) +"|" + 
					et_valu(c_address_1) +"|" + et_valu(c_address_2) +"|" + et_valu(c_city) +"|" + 
					et_valu(c_state) +"|" + et_valu(c_zipcode);
			pay_type_card=true;
			netxt_page_value.put("fname", et_valu(f_name));
			netxt_page_value.put("lname",et_valu(l_name) );
			netxt_page_value.put("cnum",et_valu(c_number) );
			netxt_page_value.put("card_type", spinner_value(card_type));
			netxt_page_value.put("c_address_1",et_valu(c_address_1) );
			netxt_page_value.put("c_address_2",et_valu(c_address_2) );
			netxt_page_value.put("cvv",et_valu(cvv_number) );
			netxt_page_value.put("month",spinner_value(month) );
			netxt_page_value.put("year",spinner_value(year) );
			netxt_page_value.put("cstate",et_valu(c_state));
			netxt_page_value.put("c_zip",et_valu(c_zipcode) );
			netxt_page_value.put("c_city",et_valu(c_city) );
		}
		return card_detail;
	}

	public String getaddress(){
		if(pay_by_card.isChecked()){
			address = et_valu(c_address_1) +" " + et_valu(c_address_2) ;
		}else{
			address = et_valu(address_1) +" " + et_valu(address_2) ;
		}
		return address;
	}
	public void credexp(){
		if(pay_by_card.isChecked()){
			cred_exp = spinner_value(month)+ "-"+spinner_value(year);
		}
	}
	public String getReccuringRadio(){
		String value="";
		if(recurring.isChecked()){
			value="1";
		}else{
			value = "0";
		}
		return value;
	}
	public class send_detail extends AsyncTask<Void, Void, Void>{
		String success="";
		int selected=0;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(ReceivePayment.this);
			pd.setMessage("Please wait...");
			pd.setCancelable(true);
			pd.show();
			selected = ahc_selection_group.indexOfChild(findViewById(ahc_selection_group.getCheckedRadioButtonId()));
			System.out.println("Selected value : "+selected);
		} 
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			get_address();
			credexp();
			
			HashMap<String, String > param = new HashMap<String, String>();
			param.put("Token",token );
			param.put("BasketID",AppConfiguration.BasketID);
			param.put("wu_recurring", getReccuringRadio());
			if(saved_method_inflate.getVisibility()==View.VISIBLE){
				param.put("pmtid", _wu_PmtID.get(selected));
				param.put("strType", _wu_PayTypeID.get(selected));
				param.put("strCard", _wu_CardAccNumber.get(selected));
			}else{
				param.put("pmtid", "");
				param.put("strType","");
				param.put("strCard","");
			}
			param.put("ShippingAddress", "");
			param.put("chkagree", "True");
			param.put("ChkAddShippingAddress", "False");
			if(new_pay_lay_hide.getVisibility()==View.VISIBLE){
				param.put("RBPaymentType", get_payment_type());
				param.put("chkSaveACH", get_save_detail());
				param.put("chkSaveCard",get_save_card_detail());
				param.put("strCheckDtl",  getCheckDetail());
				param.put("strCreditDtl", getCardDetail());
			}else{
				param.put("RBPaymentType", "");
				param.put("chkSaveACH", "");
				param.put("chkSaveCard","");
				param.put("strCheckDtl",  "");
				param.put("strCreditDtl", "");
			}
			String responseString = WebServicesCall.RunScript(DOMAIN+AppConfiguration.confirm_pay, param);
			try{
				JSONObject obj = new JSONObject(responseString);
				success = obj.getString("Success");
				if(success.equals("True")){

					address="";
					card_detail="";
					check_detail="";

					JSONArray jarry = new JSONArray();
					jarry = obj.getJSONArray("ConformPayment");
					for (int i = 0; i < jarry.length(); i++) {
						JSONObject jobj = jarry.getJSONObject(i);
						address = jobj.getString("PaymentBillingAddress");
						if(address.trim().length()<=0){
							address = jobj.getString("PaymentShippingAddress");
						}
						card_detail = jobj.getString("PaymentCredit");
						check_detail = jobj.getString("PaymentCheck");
					}

				}
			}catch(Exception e){

			}
			dismiss_it();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dismiss_it();
			if(success.equals("True")){
				Intent i = new Intent(mContext,ReceivePaymentConfirm.class);
				i.putExtra("TOKEN", token);
				startActivity(i);
			}else{
				ping(mContext, "Something's went Wrong");
			}

		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
