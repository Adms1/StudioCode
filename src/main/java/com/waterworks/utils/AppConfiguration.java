package com.waterworks.utils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.waterworks.R;
import com.waterworks.ViewWaitlistActivity;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.sheduling.ScheduleLessonFragement2;
import com.waterworks.sheduling.ScheduleLessonFragement3;
import com.waterworks.sheduling.ScheduleLessonFragement4;
import com.waterworks.sheduling.ScheduleLessonFragement5;
import com.waterworks.sheduling.ScheduleLessonFragement6;
import com.waterworks.sheduling.ScheduleMakeupFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Harsh Patel
public class AppConfiguration {


    Context context;
    static Activity mActivity;
    public static String currency = "$";
    public static final String PREFS_NAME = "MyToken";
    public static final String PREFS_NAME1 = "MyToken";

    public static SharedPreferences prefs;
    SharedPreferences.Editor editor;

    AppConfiguration(Context ctx) {
        context = ctx;

        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getSharedPrefs(Context c) {
        SharedPreferences sharedPrefs = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs;
    }

    //This local is in use
//    public static String SOAP_ADDRESS = "http://103.204.192.187:8081/WWWebService/Service.asmx?WSDL";
//    public static String DOMAIN = "http://103.204.192.187:8081/WWWebServices/Services_Front.asmx/";
//    public static String PhotoDomain = "http://103.204.192.187:8081/NewCode";

//    kishansir pc 07-06-2017
//    public static String DOMAIN = "http://103.204.192.187:8091/Services_Front.asmx/";


//    public static String DOMAIN ="http://dev1.office.waterworksswim.com/WWWebService/Services_Front.asmx/";
    //For Live Configration
//shrenik was here.

    // //U.S.
    // ="http://103.246.84.229:8081/WWWebServices/Services_Front.asmx/"; //

////    // live
    public static final String DOMAIN = "http://office.waterworksswimonline.com/WWWebService/Services_Front.asmx/";
    public static String SOAP_ADDRESS = "http://office.waterworksswimonline.com/WWWebService/Service.asmx?WSDL";
    public static String PhotoDomain = "http://office.waterworksswimonline.com/NewCode";

    public static int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    public static boolean animation = false;

    public static void checkVersion() {
        if (currentapiVersion >= android.os.Build.VERSION_CODES.KITKAT) {
            // Do something for lollipop and above versions
            animation = true;
        } else {
            animation = false;
            // do something for phones running an SDK before lollipop
        }
    }

    public static String currentPass = "";
    // FragmentData
    public static Bundle page1_schdl = new Bundle();
    public static ArrayList<String> selectedStudentsName = new ArrayList<String>();
    public static ArrayList<String> selectedStudent1 = new ArrayList<String>();
    public static ArrayList<String> selectedStudent2 = new ArrayList<String>();

    public static ArrayList<String> selectedStudent3 = new ArrayList<String>();
    public static ArrayList<String> selectedStudent4 = new ArrayList<String>();

    public static ArrayList<String> selectedStudent5 = new ArrayList<String>();
    public static ArrayList<String> selectedStudent6 = new ArrayList<String>();
    //    09-01-2017 megha
    public static ArrayList<String> selectedStudentEventID = new ArrayList<String>();
    public static ArrayList<String> CountArray = new ArrayList<String>();

    public static String GetAllRecentChatsByID="GetAllRecentChatsByID";

    public static String Get_RegLapSwimPriceBySite = "Get_RegLapSwimPriceBySite";
    public static String Get_MonthlyLapSwimPriceBySite = "Get_MonthlyLapSwimPriceBySite";
    public static String pair2Check = "0";
    public static String pair1Check = "0";
    public static String schedulechoices = "7";
    public static int makeup_Clicked = 0;
    public static String LoginURL = "Login";
    public static String Mup_cnt = "Mup_cnt";
    public static String phoneTypesURL = "Get_Phonetype_Load";
    public static String getSiteListURL = "Get_SiteList";
    public static String Get_GirlsScoutSiteList = "Get_GirlsScoutSiteList";
    public static String Get_DivesAndTurnsSiteList = "Get_DivesAndTurnsSiteList";
    public static String Get_WaterpoloSiteList = "Get_WaterpoloSiteList";
    public static String Get_SwimcompetitionsSiteURL = "Get_SwimcompetitionsSite";
    public static String getSiteWiseSwimLessonURL = "Get_SiteWiseSwimCampSessionList";
    public static String getDataSpinner1URL = "Get_MasterDtl";
    public static String getDataChildLevel1URL = "Get_ChildDtl";
    public static String getDataChildLevel2URL = "Get_SecondaryDtl";
    public static String registration = "Registration_Insert_CreateAccount";
    public static String forgot_passwordURL = "Forgot_Password";
    public static String getMyAccountURL = "Get_MyAccount";
    public static String GetStudentCount = "GetStudentCount";
    public static String updateMyAccountURL = "Update_MyAccount";
    public static String GetRegionList = "Get_RegionList";
    public static String GetSiteListByRegion = "Get_SiteListByRegion";
    public static String GetInsertSiteFromFamily = "Get_InsertSiteFromFamily";
    public static String GetRemoveSiteFromFamily = "Get_RemoveSiteFromFamily";
    public static String regionLocationAddress = "";
    public static String siteName = "";
    public static String siteid = "";
    public static String phoneAdd = "";
    public static String Address1 = "";
    public static String phone = "";
    public static String siteidAdd = "";
    public static String changePasswordURL = "MyAcnt_ChangePassword";
    public static String viewschedule = "ViewSchdl_PageLoad_ViewSchdl";
    public static String viewpastschedule = "ViewSchdl_Get_ViewSchdlDetail";
    public static String requestShadowing = "ViewSchdl_RqstShdw_PageLoad";
    public static String submitrequestshadowing = "ViewSchdl_RqstShdw_SubmitRqst";
    public static String cancelLessonLoad = "CancelCls_PageLoad_BindData";
    public static String RemoveLesson_PageLoad_FillData_SelectedID = "RemoveLesson_PageLoad_FillData_SelectedID";
    public static String getEmailPreferences = "MyAcnt_Get_EmailSubscription";
    public static String saveEmailPreferences = "MyAcnt_Insert_EmailSubscription";
    public static String cancelClass = "CancelCls_Cancel";
    public static String validateZipCodeURL = "GetLocationByZipCode";
    public static String getInstructorPreferences = "Get_LessonList";
    public static String MyAcnt_AgeCalculation = "MyAcnt_AgeCalculation";
    public static String Get_Lession_by_LevelAge = "Get_Lession_by_LevelAge";
    public static String isEmailExistSURL = "Check_User_EmailId";
    public static String getFamilySwimSiteListURL = "Get_FamilySwimSiteList";
    public static String registerChild = "Insert_MyAcnt_RegiserChild";
    public static String updateChildURL = "Update_MyAcnt_RegiserChild";
    public static String getStudentListURL = "MyAcnt_Get_StudentList";
    public static String getChildCountURL = "Get_AddChildCount";
    public static String viewPaymentHistory = "ViewSchl_Get_ViewPayment";
    public static ArrayList<String> global_StudIDChecked = new ArrayList<>();

    public static int cartBackScreen = -1;

    // Legal Doc Check
    public static String legal_doc_URL = "LegDoc_Check_LoginDoc";
    public static String legal_doc_URL_Load = "LegDoc_Check_LoadDoc";
    public static String legal_doc_URL_insert_disclaimer = "LegDoc_Insert_DisclaimerDtl";
    public static String legal_doc_URL_insert = "LegDoc_Insert_FamilyDocDetails";
    public static String legal_doc_URL_update = "LegDoc_Update_DocProcess";
    public static String legal_doc_URL_insert_student = "LegDoc_Insert_StudentDocDetails";
    public static String legal_doc_StudentList = "LegDoc_Bind_StudentList";
    public static String legal_doc_StudentDoc_Status = "LegDoc_Update_StudentDocStatus";
    public static String Get_GuardPrepSiteList = "Get_GuardPrepSiteList";

    //    For Testing Legal Doc
    public static String LegDoc_Insert_StudentDocDetails_New_Full = "LegDoc_Insert_StudentDocDetails_New_Full";
    public static String LegDoc_Insert_StudentDocDetails_FirstPart = "LegDoc_Insert_StudentDocDetails_FirstPart";


    // Checkout
    public static String Dis_confirm_msg = "Pay_DisplayConformationPart";
    public static String AHC_Card_detail = "Pay_DGetACHCardDetail";
    public static String confirm_pay = "Pay_ConformPaymnet";

    public static boolean custom_flow = false;
    public static int transform = 0;
    // Buy more lesson MakePurchase_PkgList_LessonWise

    public static String makePurchasePkgListLessonWise = "MakePurchase_PkgList_LessonWise";
    public static String monthlyPackageList_LessonWise = "MonthlyPackageList_LessonWise";
    public static String getBasketID = "Get_BasketID";
    public static String payDGetACHCardDetail = "Pay_DGetACHCardDetail";
    public static String Pay_DGetACHCardDetailBySiteURL = "Pay_DGetACHCardDetailBySite";
    public static String ShowHideProductCategory = "ShowHideProductCategory";
    public static String ShowHidePrograms = "ShowHidePrograms";
    public static String AddACHDetails = "AddACHDetails";
    public static String AddACHDetailsBySiteURL = "AddACHDetailsBySite";
    public static String AddCardDetails = "AddCardDetails";
    public static String AddCardDetailsBySiteURL = "AddCardDetailsBySite";
    public static String DeletePaymentDetailsBySiteURL = "DeletePaymentDetailsBySite";
    public static String Get_StateList = "Get_StateList";
    public static String GetCardDetailByPmtID = "GetCardDetailByPmtID";
    public static String EditCardDetails = "EditCardDetails";
    public static String buymorethankyou = "buymorethankyou";
    public static String totalQty = "";
    public static String LessinTypeId = "";
    public static String siteID = "";
    public static String pmtId = "pmtIdEdt";
    public static String cardType = "cardType";
    public static String CheckMeet = "";

    //		public static String PayConformPaymnet = "Pay_ConformPaymnet";
//    public static String Basketid = "Basketid";
    public static String categoryID = "";
    public static String lessionType = "";
    public static String wu_PayType = "";
    public static boolean addNewCheckPayment = false;
    public static boolean addNewCardPayment = false;
    public static ArrayList<HashMap<String, String>> myCartPackageArray = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> myCartMonthlyPlanArray = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> finalArrayPackage = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> finalArrayMonthlyPlan = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> lessonArray = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> cardDetailArray = new ArrayList<HashMap<String, String>>();
    public static ArrayList<String> allsitename = new ArrayList<String>();
    public static ArrayList<HashMap<String, String>> selectedCardDetailArray = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> totalPromoArray = new ArrayList<HashMap<String, String>>();
    /*public static ArrayList<HashMap<String, String>> insertedCheckDetailArray = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> insertedCardDetailArray = new ArrayList<HashMap<String, String>>();*/
    public static ArrayList<HashMap<String, String>> finalSelectedPaymentArray = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> editCardArray = new ArrayList<HashMap<String, String>>();
    // ViewStudents
    public static String viewCertificateURL = "MyAcnt_Get_Certificate";
    public static String progressReportURL = "MyAcnt_Get_ProgressRpt_List";
    public static String progressReportDetailsURL = "MyAcnt_Get_ProgressRpt_StudWiseDtl";
    public static String GetStudentLevelProgressURL = "GetStudentLevelProgress";
    public static String ribbenCountURL = "SwimCmpt_Get_RibbonCount";
    public static String ribbenCountDetailsURL = "SwimCmpt_Get_RibbonCountDtlByStudent";
    public static String GetStudentRibbonCountURL = "GetStudentRibbonCount";
    public static String Pay_EditPaymentDetails = "Pay_EditPaymentDetails";

    public static String LessonsCounts = "ViewSchdl_LessonCounts";
    public static String transfermakeupcounts = "MakePurchase_TransMakeup_CountWithExpiryDate";
    public static String swimCampRegister3New = "MakePurchase_LoadStep3New";
    public static String DetailsOfSwimCamp = "Program_GET_SwimCampRegisteredDtl";
    public static String GetSwimCampDefault = "GetSwimCampDefault";
    public static String Get_SwimCompetitions_Registrations = "Get_SwimCompetitions_Registrations";
    public static String Get_UpcomingSwimcompetitions = "Get_UpcomingSwimcompetitions";
    public static String Get_UpcomingDivesandTurns = "Get_UpcomingDivesandTurns";
    public static String Get_UpcomingGirlsPrograms = "Get_UpcomingGirlsPrograms";
    public static String Get_UpcomingSwimmingActivityBadgesPrograms = "Get_UpcomingSwimmingActivityBadgesPrograms";

    public static String Get_Divesandturnsregistrations = "Get_Divesandturnsregistrations";
    public static String Get_GirlsProgramsRegistrations = "Get_GirlsProgramsRegistrations";
    public static String Get_SwimmingActivityBadgesRegistrations = "Get_SwimmingActivityBadgesRegistrations";
    public static String Get_UpcomingFamilySwimNights = "Get_UpcomingFamilySwimNights";
    public static String Get_FamilySwimNightRegistrations = "Get_FamilySwimNightRegistrations";

    public static String Get_ScoutBadgeRegistrations = "Get_ScoutBadgeRegistrations";
    public static String Get_SwimmingMeritBadgesRegistrations = "Get_SwimmingMeritBadgesRegistrations";
    public static String Get_UpcomingScoutPrograms = "Get_UpcomingScoutPrograms";
    public static String GetStudentLevelprereqURL = "GetStudentLevelprereq";
    public static String Get_UpcomingSwimmingMeritBadgesPrograms = "Get_UpcomingSwimmingMeritBadgesPrograms";
    public static String Get_GuardPrepRegistrations = "Get_GuardPrepRegistrations";
    public static String Get_UpcomingGuardPrep = "Get_UpcomingGuardPrep";
    public static String Get_Upcomingwaterpolo = "Get_Upcomingwaterpolo";
    public static String Get_WaterpoloConditioningRegistrations = "Get_WaterpoloConditioningRegistrations";
    public static String GetStudentRemainingLevelprereqURL = "GetStudentRemainingLevelprereq";
    public static String GetStudentEventTimingURL = "GetStudentEventTiming";
    public static String GetStudentEventsURL = "GetStudentEvents";
    public static String Get_ThermalProductDetailsURL = "Get_ThermalProductDetails";
    public static String Get_ThermalProductAddToCartURL = "Get_ThermalProductAddToCart";
    public static String Get_InhouseProductDetailsURL = "Get_InhouseProductDetails";
    public static String Get_ProductDetailsByIDURL = "Get_ProductDetailsByID";
    public static String Get_ProgramsInstructions = "Get_ProgramsInstructions";
    public static String Get_ProgramsPriceInfo = "Get_ProgramsPriceInfo";

    public static String Schl_Get_LessonListByStudent = "Schl_Get_LessonListByStudent";
    public static String Schl_Get_ActiveSiteStartEndDate = "Schl_Get_ActiveSiteStartEndDate";
    public static String Schl_Get_Step3_AddSchedule = "Schl_Get_Step3_AddSchedule";
    // Make Purchase
    public static String sitebyFid = "MakePurchase_PageLoad_SiteByFamily";
    public static String productbyFamily = "MakePurchase_PageLoad_ItemsByFamily";
    public static String swimpackage = "MakePurchase_PackageList_LessonWise";
    public static String swimsubmit = "MakePurchase_Submit_LesPacWise";
    public static String Get_BasketID = "Get_BasketID";
    public static String MakePurchase_LapSwimMonthList = "MakePurchase_LapSwimMonthList";
    public static String ScheMakeup_recountProcessByFamily = "ScheMakeup_recountProcessByFamily";

    //	public static String BasketID = "BasketID";Old
    public static String BasketID = "0";//new
    public static String viewpricesheet = "ViewPriceSheet_PageLoadData";
    public static String viewcart__pasduebal_basketid = "ViewPasDueBal_FillData_BasketIDWise";
    public static String deleteinvoice = "MakePurchase_DeleteInvoice";
    public static String applypromocode = "MakePurchase_ApplyPromocode";
    public static String emptybasket_byid = "MakePurchase_EmptyBasket_ByBacketID";
    public static String basket_siteid = "Siteid";
    public static String pastduebalload = "ViewPasDueBal_PageLoad_FillData";
    public static String payinvoice = "ViewPasDueBal_PayInvoice";
    public static String transfermakeupload = "MakePurchase_TransMakeup_Site_PrivateData";
    public static String transfermakeup_lesson_transfer = "MakePurchase_LessonTransfer_Select";
    public static String transfermakeup_site_transfer = "MakePurchase_SiteTransfer_Select";
    public static String transfermakeup_privatelesson_transfer = "MakePurchase_PrivateLesson_Transfer";
    public static String transf_invoiceID = "invoiceid";
    public static String insertinvoice = "ViewPasDueBal_InvoiceData";
    public static String Pay_Conform_SubmitPayment = "Pay_Conform_SubmitPayment";
    public static String ViewPasDueBal_InvoiceData = "ViewPasDueBal_InvoiceData";
    public static String Pay_Invoice_Load = "Pay_Invoice_Load";
    public static String lapswimbutton1submit = "MakePurchase_AddLapSwim";
    public static String lapswimbutton2submit = "MakePurchase_AddMonthlyLapSwim";
    public static String lapswimbutton3submit = "MakePurchase_AddDiscountLapSwim";
    public static String WaterArobics = "MakePurchase_AddWaterArobics";
    public static String fsn_getdate = "MakePurchase_FamilySwimDate_SiteIDWise";
    public static String fsn_getcost = "MakePurchase_CostDetail_SwimDateWise";
    public static String fsn_submit = "MakePurchase_SubmitSwimData";
    public static String swimCampRegister1 = "MakePurchase_SwimCamp_StuList_FamilyWise";
    public static String swimCampRegister2 = "MakePurchase_LoadStep2";
    public static String swimCampRegister3 = "MakePurchase_LoadStep3";
    public static String swimCampRegister1_new = "SwimComp_StuList_FamilyWise";

    public static String makePurchaseAddToCart = "MakePurchase_SwimCampAddtoCart";

    public static String swimteamDescription = "MakePurchase_SwimTeamDescription";
    public static String swimteamGroup = "MakePurchase_SwimTeamGroup";
    public static String swimteamChild = "MakePurchase_SwimTeamChild";
    public static String add_swimteam1 = "MakePurchase_AddFirstSwimTeam";

    public static String MakePurchase_SwmTm_BindGroup = "MakePurchase_SwmTm_BindGroup";
    public static String MakePurchase_SwmTm_BindNoOfDay = "MakePurchase_SwmTm_BindNoOfDay";
    public static String MakePurchase_SwmTm_BindFillDay = "MakePurchase_SwmTm_BindFillDay";
    public static String MakePurchase_SwmTm_InsertSwimTeamData = "MakePurchase_SwmTm_InsertSwimTeamData";
    public static String MakePurchase_SwmTm_BindMonth = "MakePurchase_SwmTm_BindMonth";
    public static String MakePurchase_SwmTm_BindMonthAll = "MakePurchase_SwmTm_BindMonthAll";
    public static String MakePurchase_SwmTm_CardCheck = "MakePurchase_SwmTm_CardCheck";
    public static String ShowHideScanPrograms = "ShowHideScanPrograms";
    public static String ScanCardsPrograms = "ScanCardsPrograms";
    public static String SwimCmpt_EventListOfStudentByMeet = "SwimCmpt_EventListOfStudentByMeet";
    public static String SwimCmpt_AllEventList = "SwimCmpt_AllEventList";
    public static String SwimCmpt_AllGetEventResultForStudent = "SwimCmpt_AllGetEventResultForStudent";
    public static String SwimCmpt_AllMeetResultForEvent = "SwimCmpt_AllMeetResultForEvent";
    public static String GetAgeGroupListForSwimMeet = "GetAgeGroupListForSwimMeet";
    public static String GetStudentEvents = "GetStudentEvents";
    public static String Get_SiteListForFamilyPoolRecords = "Get_SiteListForFamilyPoolRecords";
    public static String SwimCmpt_GetPoolRecords = "SwimCmpt_GetPoolRecords";
    public static String SwimCmpt_AllEventList_MeetOverTime = "SwimCmpt_AllEventList_MeetOverTime";


    // New Design Scheduling
    public static String d2_startDate = "";
    public static String d2_endDate = "";
    public static ArrayList<String> builder, builder1, builder2, builder3,
            builder4, SelectedInstName;
    public static HashMap<String, String> gender_arry = new HashMap<String, String>();
    public static HashMap<String, String> ShowHideScanProgramsList = new HashMap<String, String>();
    public static String st_Student1, st_Student2, st_Student3, st_Student4,
            st_Student5, list1, list2, list3, list4, list5;
    public static ArrayList<String> st_Student1_check=new ArrayList<String>();


    // Contact Us
    public static String feedbackURL = "SendMail_ContactByFamily";
    public static String contactUsURL = "Get_ContactDtl";
    public static String Get_ContactDtl_new = "Get_ContactDtl_new";
    public static String suggestURL = "SendMail_SuggestionByFamily";
    public static String problemURL = "SendMail_ProblemByFamily";
    public static String HelpURLNEW = "SendMail_Help";

    // Scheduling
    public static String viewholds = "ViewHolds_PageLoad_FillData";
    public static String viewwaitlistpageload = "ViewWaitList_PageLoad_FillData";
    public static String viewwaitlistsubmit = "ViewWaitList_SubmitData";
    public static String removelesson = "RemoveLesson_PageLoad_FillData";
    public static String removelessonsubmit = "RemoveLesson_Onclick_ReleaseClass";
    public static String SwimCmptSchedule_EstimatedDurationForEvent = "SwimCmptSchedule_EstimatedDurationForEvent";

    // Programs
    public static String GetChildDT = "Prg_Get_StudentList";
    public static String Savebasket = "Prg_Get_Divesand_PageLoad_Step1";
    public static String DTPage2Load = "Prg_Get_Divesand_Step2";
    public static String DTPage3Load = "Prg_Get_Divesand_Step3";
    public static String Prg_Divesand_AddToCart = "Prg_Divesand_AddToCart";
    public static String Prg_Get_GuardPrep_Step2 = "Prg_Get_GuardPrep_Step2";
    public static String Prg_Get_GuardPrep_Step3 = "Prg_Get_GuardPrep_Step3";
    public static String Prg_GuardPrep_AddToCart = "Prg_GuardPrep_AddToCart";
    public static String Prg_Get_WaterPoloCond_Step2 = "Prg_Get_WaterPoloCond_Step2";
    public static String Prg_Get_WaterPoloCond_Step3 = "Prg_Get_WaterPoloCond_Step3";
    public static String Prg_WaterPoloCond_AddToCart = "Prg_WaterPoloCond_AddToCart";
    public static String Prg_Get_SwimmingActivityBadges_Step2 = "Prg_Get_SwimmingActivityBadges_Step2";
    public static String Prg_SwimmingActivityBadges_AddToCart = "Prg_SwimmingActivityBadges_AddToCart";
    public static String Prg_Get_SwimmingMeritBadges_Step2 = "Prg_Get_SwimmingMeritBadges_Step2";
    public static String Prg_Get_SwimmingMeritBadges_Step3 = "Prg_Get_SwimmingMeritBadges_Step3";
    public static String Prg_Get_Divesand_Step3 = "Prg_Get_Divesand_Step3";
    public static String Prg_Get_SwimmingActivityBadges_Step3 = "Prg_Get_SwimmingActivityBadges_Step3";
    public static String Prg_SwimmingMeritBadges_AddToCart = "Prg_SwimmingMeritBadges_AddToCart";
    public static String Prg_Get_GirlScoutBadges_Step2 = "Prg_Get_GirlScoutBadges_Step2";
    public static String Prg_GirlScoutBadges_AddToCart = "Prg_GirlScoutBadges_AddToCart";
    public static String Prg_Get_GirlScoutBadges_Step3 = "Prg_Get_GirlScoutBadges_Step3";
    public static String GetStudentPreviousLevelprereqURL = "GetStudentPreviousLevelprereq";
    public static String GetStudentTrophiesURL = "GetStudentTrophies";

    public static int addCheckedOptions1 = 0;
    public static int addCheckedOptions2 = 0;
    public static int addCheckedOptions3 = 0;
    public static int addCheckedOptions4 = 0;
    public static int addCheckedOptions5 = 0;

    // Release Class
    public static String releaseClassURL = "RemoveLesson_Onclick_ReleaseClass";
    public static String releaseClassURL_new = "RemoveLesson_Onclick_ReleaseClass_New";
    public static String releaseRequestClassURL = "RemoveLesson_Request_ReleaseClass";
    public static String RemoveLesson_Request_ReleaseAllClass_Survey = "RemoveLesson_Request_ReleaseAllClass_Survey";
    public static ArrayList<String> ReleaseIDRemoveLesson = new ArrayList<>();

    // HomeFragment
    public static String Get_UpcomingClass = "Get_UpcomingClass";
    public static String Get_RemainingLessons = "Get_RemainingLessons";
    public static String Schl_Get_MakeupCountByFamily = "Schl_Get_MakeupCountByFamily";
    public static String UpdateFamilyEmail = "UpdateFamilyEmail";

    public static String StudentName = "", Photo = "", Schedule_Date = "",
            Instructor = "";
    // CheckIn
    public static String SwimCmpt_Get_NextSwimCmpRegis = "SwimCmpt_Get_NextSwimCmpRegis";
    public static String checkinpageload = "SwimCmpt_Get_check_in";
    public static String checkinproceed = "SwimCmpt_Insert_checkinProceed";
    public static String checkin2title = "SwimCmpt_Get_checkin2Msg";
    public static String checkin2data = "SwimCmpt_Get_check_in2";

    public static String logCount = "";

    // Swim Camp
    public static String swimCampsRegister1SiteID = "";
    public static String swimCampsRegister1StudentID = "";
    public static String swimCampsRegister1StudentName = "";
    public static String selectedStudentSwimCampRegister2 = "";
    public static String selectedStudent = "";

    public static String divesAndTurnsRegister1StudentID = "";
    public static String divesAndTurnsRegister1StudentName = "";

    public static String jrLifeGuardStudentID = "";
    public static String jrLifeGuardStudentName = "";

    public static String meritBadgesStudentID = "";
    public static String meritBadgesStudentName = "";

    public static String girlScoutBadgesStudentID = "";
    public static String girlScoutBadgesStudentName = "";

    public static String activityBadgesStudentID = "";
    public static String activityBadgesStudentName = "";

    public static String waterPoloStudentID = "";
    public static String waterPoloStudentName = "";

    // Swim Compitition
    public static String swimCompititionMeetDatesURL = "SwimCmpt_Register_SwimMeetDateBySite";
    public static String swimCompititionMeetDateCheckURL = "SwimCmpt_Register_SwimMeetStep1Check";
    public static String swimCompititionStep2URL = "SwimCmpt_Register_SwimMeetStep2FillData";
    public static String swimCompititionStep3URL = "SwimCmpt_Register_SwimMeetStep3";
    public static String swimCompititionStep4URL = "SwimCmpt_Register_SwimMeetStep4";
    public static String swimCompititionStep5URL = "SwimCmpt_Register_SwimMeetStep5_ReminderList";
    public static String swimCompititionConfirmationCalculation = "SwimCmpt_Register_SwimMeetStep5_EventCalc";
    public static String swimCompititionConfirmationReminderText = "SwimCmpt_Register_SwimMeetStep5_ReminderList";
    public static String swimCompititionAddToCart = "SwimCmpt_Register_SwimMeetAddToCart";
    public static String Get_MeritBadgesSiteList = "Get_MeritBadgesSiteList";
    public static String Get_ActivityBadgesSiteList = "Get_ActivityBadgesSiteList";
    public static String SwimCmpt_CheckStudentMeetExist = "SwimCmpt_CheckStudentMeetExist";

    public static ArrayList<HashMap<String, String>> selectedChildDataMainList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<Boolean> selectedStudents = new ArrayList<Boolean>();
    public static String[] temp_id;
    //	public static HashSet<String> studentlist = new HashSet<String>();
    public static ArrayList<Boolean> itemChecked1 = new ArrayList<Boolean>();
    public static ArrayList<Boolean> itemChecked2 = new ArrayList<Boolean>();

    // Schedule a Lession
    public static ArrayList<HashMap<String, String>> instructorList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> instructorList_2 = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> instructorList_3 = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> instructorList_4 = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> instructorList_5 = new ArrayList<HashMap<String, String>>();


    public static String Schl_Get_StudentfnameByFamily = "Schl_Get_StudentfnameByFamily";
    public static String scheduleALesssionSiteListURL = "Schl_Get_SiteByFamily";
    public static String scheduleALessionStudentListURL = "Schl_Get_StudentListByFamily";
    public static String scheduleALessionLessonTypesURL = "Schl_Get_Display_options_Step1";
//    20-06-2017 megha
//        public static String scheduleALessionInstructorListURL = "Schl_Get_instrlistbyStudSelectionLevel";
    public static String scheduleALessionInstructorListURL = "Schl_Get_instrlistbyStudSelectionLevel_Android";
    public static String scheduleALessionPageLoad = "Schl_Page_Load";
    public static String scheduleALessionStep2InstructorAvailabilityURL = "Schl_Get_Step2";
    public static String GetInstructorBio = "GetInstructorBio";
    public static String swimCompititionMeetDatesLocationURL = "Get_SiteDetailsBySiteID";
    public static String Get_SiteListForFamilyPayments = "Get_SiteListForFamilyPayments";
    public static String Get_GetPaymentHistoryOfFamily = "Get_GetPaymentHistoryOfFamily";


    public static String scheduleALessionStep3 = "Schl_Get_Step3";
    public static String Schl_Get_Confirm_classesList = "Schl_Get_Confirm_classesList";
    public static String Schl_Add_Confirm_SchedulesLesson = "Schl_Add_Confirm_SchedulesLesson";

//megha
//    Acknowlegements API

    public static String AcknowlegementsStep4URL = "SwimCmpt_Register_GetAck";
    public static ArrayList<String> ForSchedule3instructorList = new ArrayList<String>();

    public static String PastDueForMessageCount = "";
    public static String PastDuemessage = "";
    public static String listQuantityotherproduct;

    //    Check LAFitness API
    public static String IsLAFitness_Check_Login = "IsLAFitness_Check_Login";
    public static String LAFitnessResult = "";
    public static String AddScheduleArray = "";
    public static String AddLaArray = "";

    public static int checklocationcount;


    // Schedule a Lession Variables
    public static String salStep1SiteID = "";
    public static String salStep1SiteName = "";
    public static String salStep1SiteLAFitness = "";
    public static String salStep1LessonID = "";
    public static String salStep1LessonName = "";
    public static String salStep1LessonText = "";
    public static String selectedStudentID = "";
    public static String selectedStudentIDforStep2 ="";
    public static String selectedStudentNameToSchedule = "";
    public static String makeUpFlag = "";
    public static String pastDueBalance = "";
    public static String schdl_startDate = "";
    public static String schdl_endDate = "";
    public static String reserverForever = "Yes";
    public static StringBuilder instructorListBuilder;
    public static StringBuilder instructorListBuilder1;
    public static StringBuilder instructorListBuilder2;
    public static StringBuilder instructorListBuilder3;
    public static StringBuilder instructorListBuilder4;

    public static String successMsg1 = "";
    public static String successMsg2 = "";

    public static String pair1_comboValue1 = "0";
    public static String pair1_comboValue2 = "0";
    public static String pair1_comboValue3 = "0";
    public static String pair1_comboValue4 = "0";

    public static String pair2_comboValue1 = "0";
    public static String pair2_comboValue2 = "0";
    public static String pair2_comboValue3 = "0";
    public static String pair2_comboValue4 = "0";

    public static String pair1lessontype = "";
    public static String pair2lessontype = "";

    public static String pair1lessontype_txt = "";
    public static String pair2lessontype_txt = "";

    public static String pair1_Cmbo1 = "0";
    public static String pair2_Cmbo1 = "0";
    public static String pair3_Cmbo1 = "0";
    public static String pair4_Cmbo1 = "0";

    public static String pair1_Cmbo2 = "0";
    public static String pair2_Cmbo2 = "0";
    public static String pair3_Cmbo2 = "0";
    public static String pair4_Cmbo2 = "0";

    public static ArrayList<String> comboID = new ArrayList<String>();
    public static ArrayList<String> comboID2 = new ArrayList<String>();

    public static String pair1_InstrList = "";
    public static String pair2_InstrList = "";

    public static String pair1_DayTime = "";
    public static String pair2_DayTime = "";

    public static StringBuilder instructorListBuilderForInstr;
    public static StringBuilder instructorListBuilderForInstr1,
            instructorListBuilderForInstr2, instructorListBuilderForInstr3,
            instructorListBuilderForInstr4;
    public static int totalInstructor_schedule = 0;

    public static int studentsize = 0;

    public static HashMap<String, Boolean> Checked_Bool = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> Checked_Bool_2 = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> Checked_Bool_3 = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> Checked_Bool_4 = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> Checked_Bool_5 = new HashMap<String, Boolean>();

    public static boolean SUN_Check = false;
    // Student==1
    public static boolean sun_1 = false;
    public static boolean sun_2 = false;
    public static boolean sun_3 = false;
    // Student==2
    public static boolean sun_1_2 = false;
    public static boolean sun_2_2 = false;
    public static boolean sun_3_2 = false;
    // Student==3

    // public static boolean sun_8_10 = false;
    // public static boolean sun_10_12 = false;
    // public static boolean sun_12_2 = false;
    // public static boolean sun_2_4 = false;
    // public static boolean sun_4_6 = false;
    // public static boolean sun_6_8 = false;

    public static boolean MON_Check = false;
    public static boolean mon_1 = false;
    public static boolean mon_2 = false;
    public static boolean mon_3 = false;
    // public static boolean mon_8_10 = false;
    // public static boolean mon_10_12 = false;
    // public static boolean mon_12_2 = false;
    // public static boolean mon_2_4 = false;
    // public static boolean mon_4_6 = false;
    // public static boolean mon_6_8 = false;

    public static boolean TUE_Check = false;
    public static boolean tue_1 = false;
    public static boolean tue_2 = false;
    public static boolean tue_3 = false;
    // public static boolean tue_8_10 = false;
    // public static boolean tue_10_12 = false;
    // public static boolean tue_12_2 = false;
    // public static boolean tue_2_4 = false;
    // public static boolean tue_4_6 = false;
    // public static boolean tue_6_8 = false;

    public static boolean WED_Check = false;
    public static boolean wed_1 = false;
    public static boolean wed_2 = false;
    public static boolean wed_3 = false;
    // public static boolean wed_8_10 = false;
    // public static boolean wed_10_12 = false;
    // public static boolean wed_12_2 = false;
    // public static boolean wed_2_4 = false;
    // public static boolean wed_4_6 = false;
    // public static boolean wed_6_8 = false;

    public static boolean THU_Check = false;
    public static boolean thu_1 = false;
    public static boolean thu_2 = false;
    public static boolean thu_3 = false;
    // public static boolean thu_8_10 = false;
    // public static boolean thu_10_12 = false;
    // public static boolean thu_12_2 = false;
    // public static boolean thu_2_4 = false;
    // public static boolean thu_4_6 = false;
    // public static boolean thu_6_8 = false;

    public static boolean FRI_Check = false;
    public static boolean fri_1 = false;
    public static boolean fri_2 = false;
    public static boolean fri_3 = false;
    // public static boolean fri_8_10 = false;
    // public static boolean fri_10_12 = false;
    // public static boolean fri_12_2 = false;
    // public static boolean fri_2_4 = false;
    // public static boolean fri_4_6 = false;
    // public static boolean fri_6_8 = false;

    public static boolean SAT_Check = false;
    public static boolean sat_1 = false;
    public static boolean sat_2 = false;
    public static boolean sat_3 = false;
    // public static boolean sat_8_10 = false;
    // public static boolean sat_10_12 = false;
    // public static boolean sat_12_2 = false;
    // public static boolean sat_2_4 = false;
    // public static boolean sat_4_6 = false;
    // public static boolean sat_6_8 = false;

    // public static String token = "";
    // public static String familyID = "";

    // Swim Compitition
    public static String SwimMeetID = "";
    public static String SwimMeetIDACK = "";
    public static String SwimMeetText = "";
    public static String SwimName = "";

    public static String swimComptitionStudentIDName = "";
    public static String swimComptitionStudentID = "";
    public static String swimComptitionStudentName = "";
    public static String SelectedEventDataStep2 = "";
    public static String SelectedEventDataStep2_1 = "";
    public static String SelectedEventDataStep2_2 = "";


    public static String SelectedStudentEventDataStep = "";

    public static String ChkChildListStp1 = "";
    public static int totalSelectedEvents = 0;
    public static int totalSelectedStudent = 0;

    public static String loginParentFirstname = "";
    public static String loginParentLastname = "";
    public static String loginParentEmail = "";
    public static String loginParentPhone1 = "";
    public static String siteAddress = "";

    // Registration varibales

    public static String EmailAdd = "";
    public static String ConfirmEmail = "";
    public static String Password = "";
    public static String ConfrmPassword = "";
    public static String Orgpassword = "";
    public static String OrgConfrmpassword = "";
    public static String PFirstName = "";
    public static String PLastName = "";
    public static String SFirstName = "";
    public static String SLastName = "";
    public static String Address = "";
    public static String Apt = "";
    public static String Zipcode = "";
    public static String State = "";
    public static String City = "";
    public static String SiteID = "0";
    public static String strMaster = "0";
    public static String strChild = "0";
    public static String strSecondary = "0";
    public static String strOther = "";
    public static String status = "0";
    public static String statusID = "0";
    public static String Phone1 = "";
    public static String PhoneType1 = "";
    public static String Phone2 = "";
    public static String Phone3 = "";
    public static String Phone4 = "";
    public static String Phone5 = "";
    public static String Phone6 = "";
    public static String Phone7 = "";
    public static String Phone8 = "";

    public static String PhoneType2 = "";
    public static String PhoneType3 = "";
    public static String PhoneType4 = "";
    public static String PhoneType5 = "";
    public static String PhoneType6 = "";
    public static String PhoneType7 = "";
    public static String PhoneType8 = "";

    public static String PLeavePrivate = "n";
    public static String SLeavePrivate = "n";

    public static String hearAbout = "";
    public static String hearAbout_first = "";
    public static String hearAbout_second = "";
    public static String hearAbout_third = "";

    public static String Company1 = "";
    public static String Company2 = "";
    public static String Industry1 = "";
    public static String Industry2 = "";

    public static String CurrentOccupation1 = "";
    public static String CurrentOccupation2 = "";

    // Student registration variables
    public static String studentID = "";
    public static String studentFirstname = "";
    public static String studentLastname = "";
    public static String studentDOB = "";
    public static String studentAge = "";
    public static String studentGender = "";

    //    Login password
    public static String LoginPass = "";
    // Instructor
    public static String instructorGender = "";
    public static String instructorNatureType = "";
    public static String lessionTypes = "";
    public static String classTypes = "";

    public static String levelTypes = "";
    public static String levelTypesID = "";
    public static String strYesNo1 = "";
    public static String strYesNo2 = "";

    public static String ChildStrongWilledValue = "";
    public static String ChildStrongWilled = "";

    public static String ChildSensitiveValue = "";
    public static String ChildSensitive = "";

    public static String ChildOutgoingValue = "";
    public static String ChildOutgoing = "";

    public static boolean childApplyCheck1 = false;
    public static boolean childApplyCheck2 = false;
    public static boolean childApplyCheck3 = false;

    public static String strallergiesmedical = "";
    public static String Description = "";
    public static String Swimgoals = "";

    public static int NoofVolunteersvalue = 0;
    public static String step3_jumpingwall = "";
    public static String step3_endlane = "";
    public static int NoofVolunteersvalue2 = 0;

    public static String fromSwimTeam = "false";
    public static String fromSwimCompetition = "false";

    public static String buyMoreRetailProductTypeID = "";

    public static String flagmeet = "";

    public static String ReleadIDPosition = "";


    public static ArrayList<HashMap<String, String>> lessionTypeList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> lessionTypeList_New = new ArrayList<HashMap<String, String>>();
    public static HashMap<String, String> genderInstructor = new HashMap<String, String>();

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public final static boolean isValidPhone(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public final static boolean isValidMobile(String phone2) {
        boolean check;
        if (phone2.length() < 10 || phone2.length() > 16) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    public final static boolean isConnectingToInternet(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressWarnings("deprecation")
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void showAlertDialog(Context context, String title,
                                       String message) {
        mActivity = (Activity) context;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // Setting Dialog Title

        if (title == null)
            title = context.getResources().getString(R.string.app_name);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setCancelable(false);
        // Setting Dialog Message
        if (message == null)
            // message =
            // context.getResources().getString(R.string.cant_get_loc);
            message = "No Internet Connection";
        alertDialogBuilder.setMessage(message);
        // Setting OK Button
        alertDialogBuilder.setPositiveButton("Ok", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (mActivity != null && !mActivity.isFinishing())
            alertDialog.show();
    }

    public static String Array2String(ArrayList<String> Array) {
        String converted = "";
        if (Array.size() > 0) {
            converted = Array.toString().replaceAll("\\[", "")
                    .replaceAll("\\]", "");
        }
        return converted;
    }

    public static String getFullDpURL(String dp_half) {
        String full_url = "";

        if (dp_half.trim().length() > 0) {
            if (dp_half.trim().contains("~")) {
                dp_half = dp_half.replace("~", "");
                if (dp_half.trim().contains(" ")) {
                    dp_half = AppConfiguration.PhotoDomain
                            + dp_half.replace(" ", "%20");
                    System.out.println("Pic URL" + dp_half);
                } else {
                    dp_half = AppConfiguration.PhotoDomain + dp_half;
                    System.out.println("Pic URL" + dp_half);
                }
            }
        }
        if (dp_half != null) {
            full_url = dp_half;
        }
        return full_url;
    }

    public static boolean redirect = false;

    @SuppressWarnings("deprecation")
    public static boolean ErrorPopup(Context mContext) {

        AlertDialog.Builder alertdialogbuilder3 = new AlertDialog.Builder(
                mContext);
        alertdialogbuilder3.setTitle("WaterWorks");
        alertdialogbuilder3
                .setMessage("Do you want to discontinue the scheduling process?");
        alertdialogbuilder3.setCancelable(true);
        alertdialogbuilder3.setIcon(mContext.getResources().getDrawable(
                R.drawable.alerticon));
        try {
            alertdialogbuilder3
                    .setMessage("")
                    .setCancelable(true)
                    .setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    redirect = false;
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    redirect = true;
                                    dialog.cancel();
                                }
                            });

            AlertDialog alertDialog2 = alertdialogbuilder3.create();
            alertDialog2.show();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return redirect;
    }

    @SuppressWarnings("rawtypes")
    public static Map activities = new HashMap();

    @SuppressWarnings("unchecked")
    public static void addclass() {
        activities.put("scd1", ScheduleLessonFragement.class);
        activities.put("scd2", ScheduleLessonFragement2.class);
        activities.put("scd3", ScheduleLessonFragement3.class);
        activities.put("scd4", ScheduleLessonFragement4.class);
        activities.put("scd5", ScheduleLessonFragement5.class);
        activities.put("scd6", ScheduleLessonFragement6.class);
        // Makeup
        activities.put("mkp1", ScheduleMakeupFragment.class);
        // ViewWaitList
        activities.put("wtlst", ViewWaitlistActivity.class);
    }

    public static boolean ErrorPopup(final Context mContext,
                                     final Class<?> nextClass) {

        LayoutInflater lInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = lInflater.inflate(R.layout.pop_up_layout, null);
        final PopupWindow popWindow = new PopupWindow(layout,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView tv_appname = (TextView) layout.findViewById(R.id.tv_appname);
        tv_appname.setText("Warning");

        TextView tv_description = (TextView) layout
                .findViewById(R.id.tv_description);
        tv_description
                .setText("Do you want to discontinue the scheduling process?");
        tv_description.setTextColor(Color.BLACK);
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "Roboto_Light.ttf");
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        tv_description.setTypeface(face);
        tv_appname.setTypeface(regular);

        TextView imgv_icon = (TextView) layout.findViewById(R.id.imgv_icon);
        imgv_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
            }
        });
        CardView button_ok = (CardView) layout.findViewById(R.id.button_ok);
        TextView tv_btn = (TextView) layout.findViewById(R.id.tv_btn);
//		button_ok.setText("Yes");
        tv_btn.setText("Yes");
        button_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, nextClass);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(i);
                popWindow.dismiss();
            }
        });

        CardView button_back;

        button_back = (CardView) layout.findViewById(R.id.button_back);
//		button_back.setText("No");
        TextView tv_btntxt = (TextView) layout.findViewById(R.id.tv_btntxt);
        tv_btntxt.setText("No");
        button_back.setVisibility(View.VISIBLE);

        button_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
            }
        });

        return redirect;
    }

    public static void AutoSmoothScroll(final ScrollView s) {
        s.postDelayed(new Runnable() {
            public void run() {
                s.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);
    }


}
