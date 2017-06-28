package com.waterworks;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.ProgressWheel;
import com.waterworks.utils.Utility;
@SuppressWarnings("deprecation")
public class SplashScreen extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 500;
//	ProgressWheel progress_splash;
	static String Tag = "Server activity";
	String result = "";
	boolean isInternetPresent = false, server_status = false,
			version_status = false, connectionout = false;
	SharedPreferences mPreferences;
	String version, web_version, web_path;
	public static String versionname;
	public static final String MyPREFERENCES = "Client_Version";
	public static final int progress_bar_type = 0;
	int id = 1;
	CharSequence title;

	final String strPref_Download_ID = "PREF_DOWNLOAD_ID";
	DownloadManager downloadManager;
	String Addess =
			"http://192.168.1.201/WWWebService/Service.asmx?WSDL";
	public static String DOMAIN =
			"http://192.168.1.100:8081/WWWebServices/Services_Front.asmx/"; // Local

	TextView versionText ;

	//Auto LoginParams
	SharedPreferences SP;
	public static String filename = "Valustoringfile";
	String userEmail,password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		versionname = pInfo.versionName;
		versionText= (TextView) findViewById(R.id.tv_versionno_name);
		versionText.setText("Version: " + versionname);
		
		AppConfiguration.checkVersion();
		SP = getSharedPreferences(filename, 0);
		userEmail = SP.getString("username", "");
		password = SP.getString("password", "");
		
	}

	@Override
	protected void onResume() { // TODO Auto-generated method stub
		super.onResume();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		isInternetPresent = Utility.isNetworkConnected(SplashScreen.this);
		if(isInternetPresent) {
			mPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
			if (mPreferences.contains("Version")) {
				version = mPreferences.getString("Version", "1");
				Log.i(Tag, "Version = " + version);
			} else {
				Log.i(Tag, "Version not set ");
			}
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					ClearSetUp();
					Intent i = new Intent(SplashScreen.this, LoginActivity.class);
					i.putExtra("DOMAIN", DOMAIN);
					startActivity(i);

					finish();
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}, SPLASH_TIME_OUT);

			IntentFilter intentFilter = new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE);
			registerReceiver(downloadReceiver, intentFilter);
		}else{
			onDetectNetworkState();
		}
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
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
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

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
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onBackPressed() {
	}
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(downloadReceiver);
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(mPreferences.getLong(strPref_Download_ID, 0));
			Cursor cursor = downloadManager.query(query);
			if (cursor.moveToFirst()) {
				int columnIndex = cursor
						.getColumnIndex(DownloadManager.COLUMN_STATUS);
				int status = cursor.getInt(columnIndex);
				if (status == DownloadManager.STATUS_SUCCESSFUL) {
					Toast.makeText(SplashScreen.this, "Download finish!!!", Toast.LENGTH_LONG)
					.show();
					File apkFile = new File(
							Environment.getExternalStorageDirectory()
							+ "/Android/data/waterworks.lafitnessapp/files/"
							+ "ClientApp.apk");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(apkFile),
							"application/vnd.android.package-archive");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		}
	};

	public void ClearSetUp() {
		AppConfiguration.LoginURL = "Login";
		AppConfiguration.phoneTypesURL = "Get_Phonetype_Load";
		AppConfiguration.getSiteListURL = "Get_SiteList";
		AppConfiguration.Get_DivesAndTurnsSiteList = "Get_DivesAndTurnsSiteList";
		AppConfiguration.Get_WaterpoloSiteList = "Get_WaterpoloSiteList";
		AppConfiguration.getSiteWiseSwimLessonURL = "Get_SiteWiseSwimCampSessionList";
		AppConfiguration.getDataSpinner1URL = "Get_MasterDtl";
		AppConfiguration.getDataChildLevel1URL = "Get_ChildDtl";
		AppConfiguration.getDataChildLevel2URL = "Get_SecondaryDtl";
		AppConfiguration.registration = "Registration_Insert_CreateAccount";
		AppConfiguration.forgot_passwordURL = "Forgot_Password";
		AppConfiguration.getMyAccountURL = "Get_MyAccount";
		AppConfiguration.updateMyAccountURL = "Update_MyAccount";
		AppConfiguration.changePasswordURL = "MyAcnt_ChangePassword";
		AppConfiguration.viewschedule = "ViewSchdl_PageLoad_ViewSchdl";
		AppConfiguration.viewpastschedule = "ViewSchdl_Get_ViewSchdlDetail";
		AppConfiguration.requestShadowing = "ViewSchdl_RqstShdw_PageLoad";
		AppConfiguration.submitrequestshadowing = "ViewSchdl_RqstShdw_SubmitRqst";
		AppConfiguration.cancelLessonLoad = "CancelCls_PageLoad_BindData";
		AppConfiguration.getEmailPreferences = "MyAcnt_Get_EmailSubscription";
		AppConfiguration.saveEmailPreferences = "MyAcnt_Insert_EmailSubscription";
		AppConfiguration.cancelClass = "CancelCls_Cancel";
		AppConfiguration.validateZipCodeURL = "GetLocationByZipCode";
		AppConfiguration.getInstructorPreferences = "Get_LessonList";
		AppConfiguration.MyAcnt_AgeCalculation = "MyAcnt_AgeCalculation";
		AppConfiguration.isEmailExistSURL = "Check_User_EmailId";
		AppConfiguration.LessonsCounts = "ViewSchdl_LessonCounts";
		AppConfiguration.registerChild = "Insert_MyAcnt_RegiserChild";
		AppConfiguration.updateChildURL = "Update_MyAcnt_RegiserChild";
		AppConfiguration.getStudentListURL = "MyAcnt_Get_StudentList";
		AppConfiguration.getChildCountURL = "Get_AddChildCount";
		AppConfiguration.viewPaymentHistory = "ViewSchl_Get_ViewPayment";
		AppConfiguration.viewCertificateURL = "MyAcnt_Get_Certificate";
		AppConfiguration.progressReportURL = "MyAcnt_Get_ProgressRpt_List";
		AppConfiguration.progressReportDetailsURL = "MyAcnt_Get_ProgressRpt_StudWiseDtl";
		AppConfiguration.ribbenCountURL = "SwimCmpt_Get_RibbonCount";
		AppConfiguration.ribbenCountDetailsURL = "SwimCmpt_Get_RibbonCountDtlByStudent";
		AppConfiguration.sitebyFid = "MakePurchase_PageLoad_SiteByFamily";
		AppConfiguration.productbyFamily = "MakePurchase_PageLoad_ItemsByFamily";
		AppConfiguration.swimpackage = "MakePurchase_PackageList_LessonWise";
		AppConfiguration.swimsubmit = "MakePurchase_Submit_LesPacWise";
		AppConfiguration.Get_BasketID = "Get_BasketID";
		AppConfiguration.BasketID = "0";
		AppConfiguration.viewpricesheet = "ViewPriceSheet_PageLoadData";
		AppConfiguration.viewcart__pasduebal_basketid = "ViewPasDueBal_FillData_BasketIDWise";
		AppConfiguration.deleteinvoice = "MakePurchase_DeleteInvoice";
		AppConfiguration.applypromocode = "MakePurchase_ApplyPromocode";
		AppConfiguration.emptybasket_byid = "MakePurchase_EmptyBasket_ByBacketID";
		AppConfiguration.basket_siteid = "Siteid";
		AppConfiguration.pastduebalload = "ViewPasDueBal_PageLoad_FillData";
		AppConfiguration.payinvoice = "ViewPasDueBal_PayInvoice";
		AppConfiguration.transfermakeupload = "MakePurchase_TransMakeup_Site_PrivateData";
		AppConfiguration.transfermakeup_lesson_transfer = "MakePurchase_LessonTransfer_Select";
		AppConfiguration.transfermakeup_site_transfer = "MakePurchase_SiteTransfer_Select";
		AppConfiguration.transfermakeup_privatelesson_transfer = "MakePurchase_PrivateLesson_Transfer";
		AppConfiguration.transfermakeupcounts = "MakePurchase_TransMakeup_CountWithExpiryDate";
		AppConfiguration.transf_invoiceID = "invoiceid";
		AppConfiguration.insertinvoice = "ViewPasDueBal_InvoiceData";
		AppConfiguration.lapswimbutton1submit = "MakePurchase_AddLapSwim";
		AppConfiguration.lapswimbutton2submit = "MakePurchase_AddMonthlyLapSwim";
		AppConfiguration.lapswimbutton3submit = "MakePurchase_AddDiscountLapSwim";
		AppConfiguration.WaterArobics = "MakePurchase_AddWaterArobics";
		AppConfiguration.fsn_getdate = "MakePurchase_FamilySwimDate_SiteIDWise";
		AppConfiguration.fsn_getcost = "MakePurchase_CostDetail_SwimDateWise";
		AppConfiguration.fsn_submit = "MakePurchase_SubmitSwimData";
		AppConfiguration.swimCampRegister1 = "MakePurchase_SwimCamp_StuList_FamilyWise";
		AppConfiguration.swimCampRegister2 = "MakePurchase_LoadStep2";
		AppConfiguration.swimCampRegister3 = "MakePurchase_LoadStep3";
		AppConfiguration.swimCampRegister3New = "MakePurchase_LoadStep3New";
		AppConfiguration.makePurchaseAddToCart = "MakePurchase_SwimCampAddtoCart";
		AppConfiguration.swimteamDescription = "MakePurchase_SwimTeamDescription";
		AppConfiguration.swimteamGroup = "MakePurchase_SwimTeamGroup";
		AppConfiguration.swimteamChild = "MakePurchase_SwimTeamChild";
		AppConfiguration.add_swimteam1 = "MakePurchase_AddFirstSwimTeam";
		AppConfiguration.feedbackURL = "SendMail_ContactByFamily";
		AppConfiguration.contactUsURL = "Get_ContactDtl";
		AppConfiguration.suggestURL = "SendMail_SuggestionByFamily";
		AppConfiguration.problemURL = "SendMail_ProblemByFamily";
		AppConfiguration.HelpURLNEW = "SendMail_Help";
		AppConfiguration.viewholds = "ViewHolds_PageLoad_FillData";
		AppConfiguration.viewwaitlistpageload = "ViewWaitList_PageLoad_FillData";
		AppConfiguration.viewwaitlistsubmit = "ViewWaitList_SubmitData";
		AppConfiguration.removelesson = "RemoveLesson_PageLoad_FillData";
		AppConfiguration.removelessonsubmit = "RemoveLesson_Onclick_ReleaseClass";
		AppConfiguration.GetChildDT = "Prg_Get_StudentList";
		AppConfiguration.Savebasket = "Prg_Get_Divesand_PageLoad_Step1";
		AppConfiguration.DTPage2Load = "Prg_Get_Divesand_Step2";
		AppConfiguration.DetailsOfSwimCamp = "Program_GET_SwimCampRegisteredDtl";
		AppConfiguration.releaseClassURL = "RemoveLesson_Onclick_ReleaseClass";
		AppConfiguration.checkinpageload = "SwimCmpt_Get_check_in";
		AppConfiguration.checkinproceed = "SwimCmpt_Insert_checkinProceed";
		AppConfiguration.checkin2title = "SwimCmpt_Get_checkin2Msg";
		AppConfiguration.checkin2data = "SwimCmpt_Get_check_in2";
		AppConfiguration.swimCompititionMeetDatesURL = "SwimCmpt_Register_SwimMeetDateBySite";
		AppConfiguration.swimCompititionMeetDatesLocationURL = "Get_SiteDetailsBySiteID";
		AppConfiguration.swimCompititionMeetDateCheckURL = "SwimCmpt_Register_SwimMeetStep1Check";
		AppConfiguration.swimCompititionStep2URL = "SwimCmpt_Register_SwimMeetStep2FillData";
		AppConfiguration.swimCompititionStep3URL = "SwimCmpt_Register_SwimMeetStep3";
		AppConfiguration.swimCompititionStep4URL = "SwimCmpt_Register_SwimMeetStep4";
		AppConfiguration.swimCompititionStep5URL = "SwimCmpt_Register_SwimMeetStep5_ReminderList";
		AppConfiguration.swimCompititionConfirmationCalculation = "SwimCmpt_Register_SwimMeetStep5_EventCalc";
		AppConfiguration.swimCompititionConfirmationReminderText = "SwimCmpt_Register_SwimMeetStep5_ReminderList";
		AppConfiguration.swimCompititionAddToCart = "SwimCmpt_Register_SwimMeetAddToCart";
		AppConfiguration.scheduleALesssionSiteListURL = "Schl_Get_SiteByFamily";
		AppConfiguration.scheduleALessionStudentListURL = "Schl_Get_StudentListByFamily";
		AppConfiguration.scheduleALessionLessonTypesURL = "Schl_Get_Display_options_Step1";
//		AppConfiguration.scheduleALessionInstructorListURL = "Schl_Get_instrlistbyStudSelectionLevel";
		AppConfiguration.scheduleALessionInstructorListURL = "Schl_Get_instrlistbyStudSelectionLevel_Android";

		AppConfiguration.scheduleALessionPageLoad = "Schl_Page_Load";
		AppConfiguration.scheduleALessionStep2InstructorAvailabilityURL = "Schl_Get_Step2";
		AppConfiguration.scheduleALessionStep3 = "Schl_Get_Step3";
		AppConfiguration.Schl_Get_LessonListByStudent = "Schl_Get_LessonListByStudent";
		AppConfiguration.Schl_Get_ActiveSiteStartEndDate = "Schl_Get_ActiveSiteStartEndDate";
		AppConfiguration.Schl_Get_Step3_AddSchedule = "Schl_Get_Step3_AddSchedule";
		AppConfiguration.Schl_Get_Confirm_classesList="Schl_Get_Confirm_classesList";
		AppConfiguration.Schl_Add_Confirm_SchedulesLesson ="Schl_Add_Confirm_SchedulesLesson";
		AppConfiguration.getFamilySwimSiteListURL ="Get_FamilySwimSiteList";

	}
}
