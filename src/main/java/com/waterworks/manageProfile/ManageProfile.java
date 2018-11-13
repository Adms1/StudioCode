/**
 *
 */
package com.waterworks.manageProfile;

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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Harsh Adms
 */
public class ManageProfile extends Activity {

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    CardView btn_update;
    String token, familyID;
    Button relMenu;
    TextView err_1, err_2;
    Context mContext = this;
    EditText et_primary, et_secondary, add_1, add_2, zipcode,
            f_name, l_name, f_name2, l_name2;
    LinearLayout whosTel1, whosTel2, add_addtional, custom_additional;
    ListPopupWindow lpw_list, lpw_list2, list_temp;
    ArrayList<String> listWhos = new ArrayList<String>();
    Button first_btn, second_btn;
    Boolean isInternetPresent = false;
    String phonetypeupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_profile);


        lpw_list = new ListPopupWindow(this);
        lpw_list2 = new ListPopupWindow(this);
        list_temp = new ListPopupWindow(this);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(ManageProfile.this);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        init();
    }

    private void init() {
        // TODO Auto-generated method stub

        listWhos.add("Home");
        listWhos.add("Mom Cell");
        listWhos.add("Dad Cell");
        listWhos.add("Mom Work");
        listWhos.add("Dad Work");
        listWhos.add("Grand Parent");
        listWhos.add("Babysitter");
        listWhos.add("Relative");
        listWhos.add("Fax");
        listWhos.add("Other");
//        listWhos.add("Other");
//        listWhos.add("Other");
//        listWhos.add("Other");

        err_1 = (TextView) findViewById(R.id.err_1);
        err_2 = (TextView) findViewById(R.id.err_2);

        relMenu = (Button) findViewById(R.id.relMenu);
        btn_update = (CardView) findViewById(R.id.btn_update);

        et_primary = (EditText) findViewById(R.id.et_primary);
        et_secondary = (EditText) findViewById(R.id.et_secondary);
        add_1 = (EditText) findViewById(R.id.add_1);
        add_2 = (EditText) findViewById(R.id.add_2);
        zipcode = (EditText) findViewById(R.id.zipcode);

        add_addtional = (LinearLayout) findViewById(R.id.add_addtional);
        custom_additional = (LinearLayout) findViewById(R.id.custom_additional);

        f_name = (EditText) findViewById(R.id.f_name);
        l_name = (EditText) findViewById(R.id.l_name);
        f_name2 = (EditText) findViewById(R.id.f_name2);
        l_name2 = (EditText) findViewById(R.id.l_name2);

        whosTel1 = (LinearLayout) findViewById(R.id.whosTel);
        whosTel2 = (LinearLayout) findViewById(R.id.whosTel1);

        first_btn = (Button) findViewById(R.id.first_btn);
        second_btn = (Button) findViewById(R.id.second_btn);

        setContent();
        AdditionalNumber();

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ManageProfile.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

    }

    public void setContent() {
        et_primary.setText(AppConfiguration.Phone1);
        et_secondary.setText(AppConfiguration.Phone2);

        if (!AppConfiguration.PhoneType1.equalsIgnoreCase(""))
            first_btn.setText(listWhos.get(Integer.parseInt(AppConfiguration.PhoneType1) - 1));


        if (!AppConfiguration.PhoneType2.equalsIgnoreCase(""))
            second_btn.setText(listWhos.get(Integer.parseInt(AppConfiguration.PhoneType2) - 1));

        zipcode.setText(AppConfiguration.Zipcode);
        f_name.setText(AppConfiguration.PFirstName);
        l_name.setText(AppConfiguration.PLastName);
        f_name2.setText(AppConfiguration.SFirstName);
        l_name2.setText(AppConfiguration.SLastName);
        add_1.setText(AppConfiguration.Address);

    }

    public void AdditionalNumber() {
        if (!AppConfiguration.Phone3.equalsIgnoreCase("") &&
                !AppConfiguration.Phone3.equalsIgnoreCase("()-"))
            addCustomViews(AppConfiguration.Phone3, Integer.parseInt(AppConfiguration.PhoneType3) - 1);
        if (!AppConfiguration.Phone4.equalsIgnoreCase("") &&
                !AppConfiguration.Phone4.equalsIgnoreCase("()-"))
            addCustomViews(AppConfiguration.Phone4, Integer.parseInt(AppConfiguration.PhoneType4) - 1);
        if (!AppConfiguration.Phone5.equalsIgnoreCase("") &&
                !AppConfiguration.Phone5.equalsIgnoreCase("()-"))
            addCustomViews(AppConfiguration.Phone5, Integer.parseInt(AppConfiguration.PhoneType5) - 1);
        if (!AppConfiguration.Phone6.equalsIgnoreCase("") &&
                !AppConfiguration.Phone6.equalsIgnoreCase("()-"))
            addCustomViews(AppConfiguration.Phone6, Integer.parseInt(AppConfiguration.PhoneType6) - 1);
        if (!AppConfiguration.Phone7.equalsIgnoreCase("") &&
                !AppConfiguration.Phone7.equalsIgnoreCase("()-"))
            addCustomViews(AppConfiguration.Phone7, Integer.parseInt(AppConfiguration.PhoneType7) - 1);
        if (!AppConfiguration.Phone8.equalsIgnoreCase("") &&
                !AppConfiguration.Phone8.equalsIgnoreCase("()-"))
            addCustomViews(AppConfiguration.Phone8, Integer.parseInt(AppConfiguration.PhoneType8) - 1);
    }

    public void addCustomViews(final String number, int pos) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(
                R.layout.d2_additional_telephone, null);
        final EditText number_format = (EditText) view.findViewById(R.id.et_secondary);
        final LinearLayout spinner = (LinearLayout) view.findViewById(R.id.whosTel1);
        final Button btn_sites = (Button) view.findViewById(R.id.btn_sites);

        btn_sites.setText(listWhos.get(pos));


        number_format.setText(number);
//26-06-2017 megha
        l_name2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_DONE){
                    btn_update.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        number_format.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (number_format.getText().toString().trim().length() > 9) {
                        number_format.setText(formatPhoneNumber(number_format.getText().toString().trim()));
                        number_format.setBackgroundResource(R.drawable.bordergray);
                    } else {
                        number_format.setBackgroundResource(R.drawable.error_border);
                    }
                } else {
                    number_format.setText(number_format.getText().toString().replaceAll("\\(", "")
                            .replaceAll("\\)", "")
                            .replaceAll("x", "")
                            .replaceAll("-", "")
                            .replaceAll(" ", "")
                    );
                }
            }
        });

        spinner.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                list_temp.setAdapter(new ArrayAdapter<String>(mContext,
                        R.layout.edittextpopup, listWhos));
                list_temp.setAnchorView(spinner);
                list_temp.setHeight(LayoutParams.WRAP_CONTENT);
                list_temp.setModal(true);
                list_temp.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        btn_sites.setText(listWhos.get(pos));
                        list_temp.dismiss();
                    }
                });
                list_temp.show();
            }
        });
        custom_additional.addView(view);
    }

    public void getAdditionalValue() {
        for (int i = 0; i < custom_additional.getChildCount(); i++) {
            LinearLayout root = (LinearLayout) custom_additional.getChildAt(i);
            LinearLayout secondTel = (LinearLayout) root.getChildAt(0);
            EditText et_secondary = (EditText) secondTel.getChildAt(0);
            LinearLayout whosTel1 = (LinearLayout) secondTel.getChildAt(1);
            RelativeLayout root1 = (RelativeLayout) whosTel1.getChildAt(0);
            Button btn_Sites = (Button) root1.getChildAt(0);

            if (i == 0) {
                AppConfiguration.Phone3 = et_secondary.getText().toString().trim();
                AppConfiguration.PhoneType3 = selectNumbers(btn_Sites.getText().toString());
            }

            if (i == 1) {
                if (getORnot(et_secondary, btn_Sites)) {
                    AppConfiguration.Phone4 = et_secondary.getText().toString().trim();
                    AppConfiguration.PhoneType4 = selectNumbers(btn_Sites.getText().toString());
                }

            }
            if (i == 2) {
                if (getORnot(et_secondary, btn_Sites)) {
                    AppConfiguration.Phone5 = et_secondary.getText().toString().trim();
                    AppConfiguration.PhoneType5 = selectNumbers(btn_Sites.getText().toString());
                }
            }
            if (i == 3) {
                if (getORnot(et_secondary, btn_Sites)) {
                    AppConfiguration.Phone6 = et_secondary.getText().toString().trim();
                    AppConfiguration.PhoneType6 = selectNumbers(btn_Sites.getText().toString());
                }
            }
            if (i == 4) {
                if (getORnot(et_secondary, btn_Sites)) {
                    AppConfiguration.Phone7 = et_secondary.getText().toString().trim();
                    AppConfiguration.PhoneType7 = selectNumbers(btn_Sites.getText().toString());
                }
            }
            if (i == 5) {
                if (getORnot(et_secondary, btn_Sites)) {
                    AppConfiguration.Phone8 = et_secondary.getText().toString().trim();
                    AppConfiguration.PhoneType8 = selectNumbers(btn_Sites.getText().toString());
                }
            }
        }

        if (validation_flg) {
            isInternetPresent = Utility.isNetworkConnected(ManageProfile.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new SubmitAsyncTask().execute();
            }
        } else {
            Toast.makeText(mContext, "Please fill valid information.!!", Toast.LENGTH_SHORT).show();
            validation_flg = true;
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

    boolean validation_flg = true;

    public boolean getORnot(EditText et_secondary, Button btn_Sites) {
        boolean get = false;
        if (et_secondary.getText().toString().trim().length() > 0
                || btn_Sites.getText().toString().trim().length() > 0) {
            if (et_secondary.getText().toString().trim().length() < 10) {
                if (validation_flg) {
                    validation_flg = false;
                }
            }
            get = true;
        } else {
            get = false;
        }
        return get;
    }

    public String selectNumbers(String content) {
        String number = "";
        for (int i = 0; i < listWhos.size(); i++) {
            if (listWhos.get(i).equalsIgnoreCase(content.trim()))
                number = String.valueOf(i + 1);
        }
        if (content.equalsIgnoreCase("")) {
            number = "0";
        }
        return number;
    }

    public void showView(int anchorViewId) {

    }
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
	 */

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        relMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        btn_update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getAdditionalValue();
//				Toast.makeText(mContext, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
//				finish();
            }
        });

        add_addtional.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (checkNumberCount()) {
                    LayoutInflater inflater = (LayoutInflater) mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater.inflate(
                            R.layout.d2_additional_telephone, null);
                    final EditText number_format = (EditText) view.findViewById(R.id.et_secondary);
                    final LinearLayout spinner = (LinearLayout) view.findViewById(R.id.whosTel1);
                    final Button btn_sites = (Button) view.findViewById(R.id.btn_sites);

                    number_format.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                number_format.setText(number_format.getText().toString().replaceAll("\\(", "")
                                        .replaceAll("\\) ", "")
                                        .replaceAll("x", "")
                                        .replaceAll("-", "")
                                        .replaceAll(" ", ""));
                            } else {
                                if (number_format.getText().toString().trim().length() > 9) {
                                    number_format.setText(formatPhoneNumber(number_format.getText().toString().trim()));
                                    number_format.setBackgroundResource(R.drawable.bordergray);
                                } else {
                                    number_format.setBackgroundResource(R.drawable.error_border);
                                }
                            }
                        }
                    });

                    spinner.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            list_temp.setAdapter(new ArrayAdapter<String>(mContext,
                                    R.layout.edittextpopup, listWhos));
                            list_temp.setAnchorView(spinner);
                            list_temp.setHeight(LayoutParams.WRAP_CONTENT);
                            list_temp.setModal(true);
                            list_temp.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int pos, long id) {
                                    btn_sites.setText(listWhos.get(pos));
                                    list_temp.dismiss();
                                }
                            });
                            list_temp.show();
                        }
                    });
                    custom_additional.addView(view);
                } else {
                    Toast.makeText(mContext, "Limit exceeded", Toast.LENGTH_SHORT).show();

                }

            }
        });
        whosTel1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lpw_list.setAdapter(new ArrayAdapter<String>(mContext,
                        R.layout.edittextpopup, listWhos));
                lpw_list.setAnchorView(whosTel1);
                lpw_list.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_list.setModal(true);

                lpw_list.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        first_btn.setText(listWhos.get(pos));
                        AppConfiguration.PhoneType1 = selectNumbers(first_btn.getText().toString());
                        Log.d("P0$ition", AppConfiguration.PhoneType1);
                        lpw_list.dismiss();
                    }
                });
                lpw_list.show();

            }
        });

        whosTel2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                lpw_list2.setAdapter(new ArrayAdapter<String>(mContext,
                        R.layout.edittextpopup, listWhos));
                lpw_list2.setAnchorView(whosTel2);
                lpw_list2.setHeight(LayoutParams.WRAP_CONTENT);
                lpw_list2.setModal(true);
                lpw_list2.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        second_btn.setText(listWhos.get(pos));
                        AppConfiguration.PhoneType2 = selectNumbers(second_btn.getText().toString());
                        lpw_list2.dismiss();
                    }
                });
                lpw_list2.show();
            }
        });
    }

    public boolean checkNumberCount() {
        boolean limit = true;
        if (custom_additional.getChildCount() >= 6) {
            limit = false;
        } else {
            limit = true;
        }
        return limit;
    }

    class SubmitAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
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

            if (registerSuccess.toString().equals("True")) {
                AppConfiguration.custom_flow = true;
                AppConfiguration.transform = 2;

                AppConfiguration.strOther = "";
                finish();
                Toast.makeText(mContext, "Profile Updated Successfully ", Toast.LENGTH_LONG).show();
            } else {
                AppConfiguration.strOther = "";
                Toast.makeText(mContext, "" + registerSuccessMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String formatPhoneNumber(String number) {
        if (number.trim().length() >= 10) {
            if (number.trim().length() == 10) {
                number = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, number.length());
            } else {
                number = "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, 10)
                        + "x" + number.substring(10, number.length());
            }
        }
        return number;
    }


    public String String(EditText txt) {
        return txt.getText().toString().trim();
    }

    public void submittingData() {

        if (String(zipcode).length() > 0) {
            AppConfiguration.Zipcode = String(zipcode);
        }
        if (String(et_primary).length() > 0) {
            AppConfiguration.Phone1 = String(et_primary);
        }
        if (String(et_secondary).length() > 0) {
            AppConfiguration.Phone2 = String(et_secondary);
        }
        if (String(f_name).length() > 0) {
            AppConfiguration.PFirstName = String(f_name);
        }
        if (String(l_name).length() > 0) {
            AppConfiguration.PLastName = String(l_name);
        }
        if (String(f_name2).length() > 0) {
            AppConfiguration.SFirstName = String(f_name2);
        } else {
            AppConfiguration.SFirstName = "";
        }
        if (String(l_name2).length() > 0) {
            AppConfiguration.SLastName = String(l_name2);
        } else {
            AppConfiguration.SLastName = "";
        }
        if (String(add_1).length() > 0) {
            AppConfiguration.Address = String(add_1) + " " + String(add_2);
        }

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("Token", token);
        params.put("EmailAdd", AppConfiguration.EmailAdd);
        params.put("ConfirmEmail", AppConfiguration.ConfirmEmail);
        params.put("Password", AppConfiguration.Password);
        params.put("ConfrmPassword", AppConfiguration.ConfrmPassword);
        params.put("PFirstName", AppConfiguration.PFirstName);
        params.put("PLastName", AppConfiguration.PLastName);
        params.put("SFirstName", AppConfiguration.SFirstName);
        params.put("SLastName", AppConfiguration.SLastName);
        params.put("Address", AppConfiguration.Address);
        params.put("Zipcode", AppConfiguration.Zipcode);
        params.put("State", AppConfiguration.State);
        params.put("City", AppConfiguration.City);
        params.put("SiteID", AppConfiguration.SiteID);
        params.put("OrgPass", AppConfiguration.Orgpassword);
        params.put("OrgCnfrmPass", AppConfiguration.OrgConfrmpassword);
        if (AppConfiguration.strMaster.equals(""))
            AppConfiguration.strMaster = "0";
        if (AppConfiguration.strSecondary.equals(""))
            AppConfiguration.strSecondary = "0";
        if (AppConfiguration.strChild.equals(""))
            AppConfiguration.strChild = "0";

        params.put("strMaster", AppConfiguration.strMaster);
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

    String registerSuccess = "";
    String registerSuccessMessage;

    public void readAndParseJSONSubmit(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            registerSuccess = reader.getString("Success");

            Log.d("registerSuccess", registerSuccess);

            if (registerSuccess.toString().equals("True")) {

                JSONArray jsonMainNode = reader.optJSONArray("UpdateFamilyDtl");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    registerSuccessMessage = jsonChildNode.getString("Msg");

                }
            } else {
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

}
