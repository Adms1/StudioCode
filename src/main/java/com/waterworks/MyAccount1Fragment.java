package com.waterworks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.waterworks.manageProfile.d2_MyProfile;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MyAccount1Fragment extends Fragment implements OnClickListener {
	public MyAccount1Fragment() {
	}

	String TAG = "MyAccoutScreen1";
	View rootView;
	Button relMenu;
	CheckBox chk_reg1;
	LinearLayout ll_secondary_parent;
	ImageView imgNext;

	EditText edtEmailAddress, edtConfirmEmailAddress, edtPassword, edtConfirmPassword, edtPrimaryFirstname,
	edtPrimaryLastname, edtSecondaryFirstName, edtSecondaryLastname;
	boolean checkSingleParent = false;
	String successMyAccount, successStudentCount;
	ImageView imgChangePassword, imgAddStudent, imgViewStudents, imgEmailPreferences, imgViewPaymentHistory,
	imgAPPsettings;
	LinearLayout ll_changeemail,ll_changepass, ll_addstudent, ll_viewstudents, ll_emailpref, ll_viewpayment_history, ll_appsettings;
	String Message;
	String token, familyID;
	String email, confirmEmail, password, confirmpass;

	LayoutParams params;
	LinearLayout next, prev;
	int viewWidth;
	GestureDetector gestureDetector = null;
	String successIsEmailExists;
	String messageEmailExists;
	Boolean isInternetPresent = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.myaccount1_fragment, container, false);

		// getting token
		SharedPreferences prefs = AppConfiguration.getSharedPrefs(getActivity());
		token = prefs.getString("Token", "");
		familyID = prefs.getString("FamilyID", "");
		Log.d(TAG, "Token=" + token + "\nFamilyID=" + familyID);

		Initialization();

		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtEmailAddress.getWindowToken(), 0);
		isInternetPresent = Utility.isNetworkConnected(getActivity());
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new LoadMyAccountAsyncTask().execute();
		}
		chk_reg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {

					ll_secondary_parent.setVisibility(View.GONE);
					checkSingleParent = true;
				} else {
					ll_secondary_parent.setVisibility(View.VISIBLE);
					checkSingleParent = false;
				}

			}
		});

		ll_changeemail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(),d2_MyProfile.class);
				startActivity(i);
				
			}
		});
		ll_changepass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
				startActivity(i);

			}
		});

		ll_emailpref.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), EmailPreferencesActivity.class);
				startActivity(i);
			}
		});

		ll_addstudent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isInternetPresent = Utility.isNetworkConnected(getActivity());
				if (!isInternetPresent) {
					onDetectNetworkState().show();
				} else {
					new checkStudentCountAsyncTask().execute();
				}
			}
		});

		ll_viewstudents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getActivity(), ViewStudentsListActivity.class);
				startActivity(i);
			}
		});

		ll_viewpayment_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), ViewPaymentHistoryListActivity.class);
				startActivity(i);
			}
		});

		ll_appsettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		imgNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d(" password  -----", AppConfiguration.Password);
				Log.d("confirm password  ---", AppConfiguration.ConfrmPassword);

				email = edtEmailAddress.getText().toString();
				confirmEmail = edtConfirmEmailAddress.getText().toString();
				password = edtPassword.getText().toString();
				confirmpass = edtConfirmPassword.getText().toString();

				// changed by Rakesh 27102015.......

				if (password.isEmpty()) {

					Log.d("password smae", AppConfiguration.Password);
				} else if (!password.equalsIgnoreCase(AppConfiguration.Password)) {

					AppConfiguration.Password = password;
					Log.d(" password   different", AppConfiguration.Password);
				}

				if (confirmpass.isEmpty()) {

					Log.d("confirm password smae", AppConfiguration.ConfrmPassword);
				} else if (!confirmpass.equalsIgnoreCase(AppConfiguration.ConfrmPassword)) {

					AppConfiguration.ConfrmPassword = confirmpass;
					Log.d("confirm password   different", AppConfiguration.ConfrmPassword);
				}
				String pFirstname = edtPrimaryFirstname.getText().toString();
				String pLastname = edtPrimaryLastname.getText().toString();
				String sFirstname = edtSecondaryFirstName.getText().toString();
				String sLastname = edtSecondaryLastname.getText().toString();
				AppConfiguration.EmailAdd = email;
				AppConfiguration.ConfirmEmail = confirmEmail;
				AppConfiguration.PFirstName = pFirstname;
				AppConfiguration.PLastName = pLastname;
				AppConfiguration.SFirstName = sFirstname;
				AppConfiguration.SLastName = sLastname;

				if (chk_reg1.isChecked() == true) {
					//	    changed by Rakesh 27102015...............		
					if (email.isEmpty() || confirmEmail.isEmpty() || pFirstname.isEmpty() || pLastname.isEmpty()) {
						Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					} else {
						if (!AppConfiguration.isValidEmail(email) && !AppConfiguration.isValidEmail(confirmEmail)) {
							Toast.makeText(getActivity(), R.string.invalid_email, Toast.LENGTH_LONG).show();
						} else if (!email.equalsIgnoreCase(confirmEmail)) {
							Toast.makeText(getActivity(), R.string.not_match_email, Toast.LENGTH_LONG).show();
						}
						else {
							MyAccount2Fragment fragment2 = new MyAccount2Fragment();
							FragmentManager fragmentManager = getFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
							fragmentTransaction.replace(R.id.frame_container, fragment2);
							fragmentTransaction.commit();

						}
					}
				} else {
					//	 changed by Rakesh 27102015...............

					if (email.isEmpty() || confirmEmail.isEmpty() || pFirstname.isEmpty() || pLastname.isEmpty()
							|| sFirstname.isEmpty() || sLastname.isEmpty()) {
						Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_LONG).show();
					} else {
						if (!AppConfiguration.isValidEmail(email) && !AppConfiguration.isValidEmail(confirmEmail)) {
							Toast.makeText(getActivity(), R.string.invalid_email, Toast.LENGTH_LONG).show();
						} else if (!email.equalsIgnoreCase(confirmEmail)) {
							Toast.makeText(getActivity(), R.string.not_match_email, Toast.LENGTH_LONG).show();
						}
						else {

							MyAccount2Fragment fragment2 = new MyAccount2Fragment();
							FragmentManager fragmentManager = getFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.replace(R.id.frame_container, fragment2);
							fragmentTransaction.commit();
						}
					}
				}

			}
		});

		edtPrimaryFirstname.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				password = edtPassword.getText().toString();
				confirmpass = edtConfirmPassword.getText().toString();

				if (hasFocus) {

					if (!password.equals(confirmpass)) {

						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setCancelable(true);
						builder.setTitle("WaterWorks");
						builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
						builder.setMessage(getResources().getString(R.string.not_match_pass));
						builder.setInverseBackgroundForced(true);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								edtConfirmPassword.setText("");

								edtConfirmPassword.requestFocus();
								InputMethodManager imm = (InputMethodManager) getActivity()
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.showSoftInput(edtConfirmPassword, InputMethodManager.SHOW_IMPLICIT);
								dialog.dismiss();
							}
						});

						AlertDialog alert = builder.create();
						alert.show();
					}

				} else {}
			}
		});
		return rootView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		ConstantData.destroyed = true;
	}

	private void Initialization() {

		relMenu = (Button) rootView.findViewById(R.id.relMenu);
		relMenu.setOnClickListener(this);
		ll_secondary_parent = (LinearLayout) rootView.findViewById(R.id.ll_secondary_parent);
		imgNext = (ImageView) rootView.findViewById(R.id.imgNext);
		chk_reg1 = (CheckBox) rootView.findViewById(R.id.chk_reg1);
		edtEmailAddress = (EditText) rootView.findViewById(R.id.edtEmailAddress);
		edtConfirmEmailAddress = (EditText) rootView.findViewById(R.id.edtConfirmEmailAddress);
		edtPassword = (EditText) rootView.findViewById(R.id.edtPassword);
		edtConfirmPassword = (EditText) rootView.findViewById(R.id.edtConfirmPassword);
		edtPrimaryFirstname = (EditText) rootView.findViewById(R.id.edtPrimaryFirstname);
		edtPrimaryLastname = (EditText) rootView.findViewById(R.id.edtPrimaryLastname);
		edtSecondaryFirstName = (EditText) rootView.findViewById(R.id.edtSecondaryFirstName);
		edtSecondaryLastname = (EditText) rootView.findViewById(R.id.edtSecondaryLastname);

		imgChangePassword = (ImageView) rootView.findViewById(R.id.imgChangePassword);
		imgAddStudent = (ImageView) rootView.findViewById(R.id.imgAddStudent);
		imgViewStudents = (ImageView) rootView.findViewById(R.id.imgViewStudents);
		imgEmailPreferences = (ImageView) rootView.findViewById(R.id.imgEmailPreferences);
		imgViewPaymentHistory = (ImageView) rootView.findViewById(R.id.imgViewPaymentHistory);
		imgAPPsettings = (ImageView) rootView.findViewById(R.id.imgAPPsettings);

		ll_changeemail = (LinearLayout)rootView.findViewById(R.id.ll_changeemail);
		ll_changepass = (LinearLayout) rootView.findViewById(R.id.ll_changepass);
		ll_addstudent = (LinearLayout) rootView.findViewById(R.id.ll_addstudent);
		ll_viewstudents = (LinearLayout) rootView.findViewById(R.id.ll_viewstudents);
		ll_emailpref = (LinearLayout) rootView.findViewById(R.id.ll_emailpref);
		ll_viewpayment_history = (LinearLayout) rootView.findViewById(R.id.ll_viewpayment_history);
		ll_appsettings = (LinearLayout) rootView.findViewById(R.id.ll_appsettings);
	}

	public void loadingMyAccountInfo() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Token", token);
		params.put("FamilyID", familyID);

		String responseString = WebServicesCall.RunScript(AppConfiguration.getMyAccountURL, params);

		readAndParseJSON(responseString);
	}

	public void readAndParseJSON(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successMyAccount = reader.getString("Success");
			if (successMyAccount.toString().equals("True")) {

				JSONArray jsonMainNode = reader.optJSONArray("GetFamilyDtl");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					AppConfiguration.status = jsonChildNode.getString("status");
					AppConfiguration.statusID = jsonChildNode.getString("wu_MemStatus");
					AppConfiguration.SiteID = jsonChildNode.getString("WU_siteid");
					AppConfiguration.hearAbout = jsonChildNode.getString("WU_Hearabout");

					AppConfiguration.hearAbout_first = jsonChildNode.getString("WU_Hearabout_Mst_Lable");
					AppConfiguration.strMaster = jsonChildNode.getString("WU_Hearabout_Mst_Value");
					AppConfiguration.hearAbout_second = jsonChildNode.getString("WU_Hearabout_Scd_Lable");
					AppConfiguration.strSecondary = jsonChildNode.getString("WU_Hearabout_Scd_value");
					AppConfiguration.hearAbout_third = jsonChildNode.getString("WU_Hearabout_Chd_Lable");
					AppConfiguration.strChild = jsonChildNode.getString("WU_Hearabout_Chd_Value");

					AppConfiguration.Company1 = jsonChildNode.getString("Company1");
					AppConfiguration.Industry1 = jsonChildNode.getString("Industry1");
					AppConfiguration.Company2 = jsonChildNode.getString("Company2");
					AppConfiguration.Industry2 = jsonChildNode.getString("Industry2");
					AppConfiguration.Password = jsonChildNode.getString("password");
					AppConfiguration.ConfirmEmail = jsonChildNode.getString("ConfrmEmail");
					AppConfiguration.strOther = jsonChildNode.getString("OtherTxt");
					AppConfiguration.ConfrmPassword = jsonChildNode.getString("Confrmpassword");
					AppConfiguration.Orgpassword = jsonChildNode.getString("Orgpassword");
					AppConfiguration.OrgConfrmpassword = jsonChildNode.getString("OrgConfrmpassword");
					AppConfiguration.EmailAdd = jsonChildNode.getString("Email");
					AppConfiguration.PFirstName = jsonChildNode.getString("PFirstName");
					AppConfiguration.PLastName = jsonChildNode.getString("PLastName");
					AppConfiguration.SFirstName = jsonChildNode.getString("SFirstName");
					AppConfiguration.SLastName = jsonChildNode.getString("SLastName");
					AppConfiguration.Address = jsonChildNode.getString("Address");
					AppConfiguration.City = jsonChildNode.getString("City");
					AppConfiguration.State = jsonChildNode.getString("State");
					AppConfiguration.Zipcode = jsonChildNode.getString("ZipeCode");
					AppConfiguration.Phone1 = jsonChildNode.getString("Phone1");
					AppConfiguration.Phone2 = jsonChildNode.getString("Phone2");
                    AppConfiguration.Phone3 = jsonChildNode.getString("Phone3");
                    AppConfiguration.Phone4 = jsonChildNode.getString("Phone4");
                    AppConfiguration.Phone5 = jsonChildNode.getString("Phone5");
                    AppConfiguration.Phone6 = jsonChildNode.getString("Phone6");
                    AppConfiguration.Phone7 = jsonChildNode.getString("Phone7");
                    AppConfiguration.Phone8 = jsonChildNode.getString("Phone8");

					AppConfiguration.PhoneType1 = jsonChildNode.getString("PhoneType1");
					AppConfiguration.PhoneType2 = jsonChildNode.getString("PhoneType2");
                    AppConfiguration.PhoneType3 = jsonChildNode.getString("PhoneType3");
                    AppConfiguration.PhoneType4 = jsonChildNode.getString("PhoneType4");
                    AppConfiguration.PhoneType5 = jsonChildNode.getString("PhoneType5");
                    AppConfiguration.PhoneType6 = jsonChildNode.getString("PhoneType6");
                    AppConfiguration.PhoneType7 = jsonChildNode.getString("PhoneType7");
                    AppConfiguration.PhoneType8 = jsonChildNode.getString("PhoneType8");

					AppConfiguration.CurrentOccupation1 = jsonChildNode.getString("CurrentOccupation1");
					AppConfiguration.CurrentOccupation2 = jsonChildNode.getString("CurrentOccupation2");
					AppConfiguration.PLeavePrivate = jsonChildNode.getString("PLeavePrivate");
					AppConfiguration.SLeavePrivate = jsonChildNode.getString("SLeavePrivate");
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	class LoadMyAccountAsyncTask extends AsyncTask<Void, Void, Void> {
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
			loadingMyAccountInfo();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successMyAccount.equalsIgnoreCase("True")) {

				edtEmailAddress.setText("" + AppConfiguration.EmailAdd);
				edtConfirmEmailAddress.setText("" + AppConfiguration.ConfirmEmail);
				//      changed by Rakesh 27102015.......
				edtPrimaryFirstname.setText("" + AppConfiguration.PFirstName);
				edtPrimaryLastname.setText("" + AppConfiguration.PLastName);
				edtSecondaryFirstName.setText("" + AppConfiguration.SFirstName);
				edtSecondaryLastname.setText("" + AppConfiguration.SLastName);
				//		      changed by Rakesh 27102015.......
				edtEmailAddress.setKeyListener(null);
				edtEmailAddress.setClickable(false);
				edtConfirmEmailAddress.setKeyListener(null);
				edtConfirmEmailAddress.setClickable(false);
				// If secondary parent's not available set check to single
				// parent.
				if (AppConfiguration.SFirstName.equals("")) {
					chk_reg1.setChecked(true);
				}

			}

		}

	}

	// =========================================== student count
	// ============================================

	public void studentCountInfo() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Token", token);
		params.put("FamilyID", familyID);

		String responseString = WebServicesCall.RunScript(AppConfiguration.getChildCountURL, params);
		readAndParseJSONStudntCount(responseString);
	}

	public void readAndParseJSONStudntCount(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successStudentCount = reader.getString("Success");
			if (successStudentCount.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
				for (int i = 0; i < jsonMainNode.length(); i++) {}
			} else {
				JSONArray jsonMainNode = reader.optJSONArray("AddChildCount");
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					Message = jsonChildNode.getString("Msg").trim();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class checkStudentCountAsyncTask extends AsyncTask<Void, Void, Void> {
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
			studentCountInfo();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successStudentCount.equalsIgnoreCase("True")) {
				Intent i = new Intent(getActivity(), RegisterChildScreen1Activity.class);
				startActivity(i);
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

				alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
				alertDialogBuilder.setMessage(Html.fromHtml("" + Message));
				// set positive button: Yes message
				alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				// show alert
				alertDialog.show();
			}
		}

	}

	public void isEmailExistsChcek() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("EmailID", email);

		String responseString = WebServicesCall.RunScript(AppConfiguration.isEmailExistSURL, params);
		readAndParseJSONEmailExists(responseString);
	}

	public void readAndParseJSONEmailExists(String in) {

		try {
			JSONObject reader = new JSONObject(in);
			successIsEmailExists = reader.getString("Success");
			if (successIsEmailExists.toString().equals("True")) {
				JSONArray jsonMainNode = reader.optJSONArray("CheckEmail");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					messageEmailExists = jsonChildNode.getString("Msg");
				}

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class isEmailExistsAsyncTask extends AsyncTask<Void, Void, Void> {
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
			isEmailExistsChcek();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}

			if (successIsEmailExists.toString().equals("True")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setCancelable(true);
				builder.setTitle("WaterWorks");
				builder.setIcon(getResources().getDrawable(R.drawable.alerticon));
				builder.setMessage(getResources().getString(R.string.existsEmail));
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

			} else {

			}

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
