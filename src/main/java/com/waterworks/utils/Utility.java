package com.waterworks.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import com.waterworks.R;
import com.waterworks.asyncTasks.GetBasketIDAsyncTask;
import com.waterworks.asyncTasks.GetCheckMeetAsyncTask;
import com.waterworks.asyncTasks.GetProgramInstructionTextAsyncTask;
import com.waterworks.asyncTasks.GetProgramPricingTextAsyncTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Utility {

    static Context Cntxt;

    /**
     * -------------------- Checking Internet Connection -------------------------------
     **/
    public static boolean isNetworkConnected(Context ctxt) {
        ConnectivityManager cm = (ConnectivityManager) ctxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
    public static void ping(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getBasketID(Activity activity, String siteID) {
        String basketID = "";
        if (AppConfiguration.BasketID.equals("0")) {
            try {
                basketID = new GetBasketIDAsyncTask(activity, siteID).execute().get();
                AppConfiguration.BasketID = basketID;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            basketID = AppConfiguration.BasketID;
        }

        return basketID;
    }

    public static String getProgramsInstructionText(String programID) {
        String programInstructionText = "";
        try {
            programInstructionText = new GetProgramInstructionTextAsyncTask(programID).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return programInstructionText;
    }

    public static String getProgramsPricingText(String programID, String siteID) {
        String programPricingText = "";
        try {
            programPricingText = new GetProgramPricingTextAsyncTask(programID, siteID).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return programPricingText;
    }

    public static String getCheckMeet(String token) {
        String CheckMeet = "";
        try {
            CheckMeet = new GetCheckMeetAsyncTask(token).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return CheckMeet;
    }

    public static String getTodaysDate() {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date()).toString().trim();
    }


    public static String getScheduleMakeupEndDate() {
        String dt = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date()).toString().trim();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 6);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        String output = sdf1.format(c.getTime());

        return output.trim();
    }

    public static String isFirstTimeBefore(String modifiedTime, String originalTime) {
        String result = "";
        String pattern = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(modifiedTime);
            Date date2 = sdf.parse(originalTime);

            if (date1.before(date2)) {
                result = "less";

            } else if (date1.after(date2)) {
                result = "more";

            } else {
                result = "same";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isInAllowedTime(Context context, String time) {
        String[] timeArrayToCheckValidTime = context.getResources().getStringArray(R.array.timeArrayToCheckValidTime);
        for (int i = 0; i < timeArrayToCheckValidTime.length; i++) {
            if (time.equalsIgnoreCase(timeArrayToCheckValidTime[i])) {
                return true;
            }
        }
        return false;
    }

//		/**--------------------------------------------- Hit Server code -----------------------------------------**/
//		public String Hit_Server(JSONObject mJsonToken, String string) {
//			String status = "";
//
//			HttpPost httppost = null;
//			try {
//				httppost = new HttpPost(string);
//				HttpParams myParams = new BasicHttpParams();
//				HttpConnectionParams.setConnectionTimeout(myParams, 10000);
//				HttpConnectionParams.setSoTimeout(myParams, 30 * 10000);
//				httppost.setHeader("Content-type", "application/json");
//				HttpClient httpclient = new DefaultHttpClient(myParams);
//				StringEntity se;
//				if (mJsonToken.toString() == null) {
//					se = new StringEntity("", HTTP.UTF_8);
//				} else {
//					se = new StringEntity(mJsonToken.toString(), HTTP.UTF_8);
//				}
//				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
//						"application/json"));
//				httppost.setEntity(se);
//				// Execute HTTP Post Request
//				HttpResponse response = httpclient.execute(httppost);
//				HttpEntity entity = response.getEntity();
//				String getResult = EntityUtils.toString(entity);
//				status = getResult;
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//
//			} finally {
//			}
//			return status;
//
//		}

}

