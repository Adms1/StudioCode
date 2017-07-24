package com.waterworks.manageProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.ChangeEmailAddress2;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Harsh Adms on 06/01/2016.
 */
public class EditStudent extends Activity {

    String token, familyID;

    EditText edtSFirstname, edtSLastname;
    Button first_btn, spinnerGenderInstructor, spinner2_nature_type, studentgender, edtStudentBdate;
    CardView btn_update;
    TextView level_number, level_no, btnLevelCalculator;
    CheckBox checkBox;
    LinearLayout llCheckbox;
    ListPopupWindow st_gen, inst_gen, inst_nature;
    Context mContext = this;

    //List Arrays
    ArrayList<String> st_gen_Arr = new ArrayList<String>();
    ArrayList<String> inst_gen_Arr = new ArrayList<String>();
    ArrayList<String> inst_nature_Arr = new ArrayList<String>();

    //Calendar
    private Calendar calendar;
    private int year, month, day;
    boolean recalculate = false;
    boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_students);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        init();

    }


    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void init() {
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText(AppConfiguration.studentFirstname + " " + AppConfiguration.studentLastname);

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStudent.this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
                finish();
            }
        });

        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(EditStudent.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        edtSFirstname = (EditText) findViewById(R.id.edtSFirstname);
        edtSLastname = (EditText) findViewById(R.id.edtSLastname);
        edtStudentBdate = (Button) findViewById(R.id.edtStudentBdate);

        first_btn = (Button) findViewById(R.id.first_btn);
        spinnerGenderInstructor = (Button) findViewById(R.id.spinnerGenderInstructor);
        spinner2_nature_type = (Button) findViewById(R.id.spinner2_nature_type);
        btn_update = (CardView) findViewById(R.id.btn_update);
        studentgender = (Button) findViewById(R.id.studentgender);

        level_number = (TextView) findViewById(R.id.level_number);
        level_no = (TextView) findViewById(R.id.level_no);
        btnLevelCalculator = (TextView) findViewById(R.id.btnLevelCalculator);

        llCheckbox = (LinearLayout) findViewById(R.id.llCheckbox);

        //Set Data
        edtSFirstname.setText("" + AppConfiguration.studentFirstname);
        edtSLastname.setText("" + AppConfiguration.studentLastname);

        edtStudentBdate.setText("" + convertDate(AppConfiguration.studentDOB));
        studentgender.setText(AppConfiguration.studentGender);
        if (AppConfiguration.instructorGender.equalsIgnoreCase("Any")) {
            spinnerGenderInstructor.setText("No Preference");
        } else {
            spinnerGenderInstructor.setText(AppConfiguration.instructorGender);
        }

        if (AppConfiguration.instructorNatureType.equalsIgnoreCase("Any")) {
            spinner2_nature_type.setText("No Preference");
        } else {
            spinner2_nature_type.setText(AppConfiguration.instructorNatureType);
        }

        level_number.setText("Level " + AppConfiguration.levelTypes + " -  ");
        level_no.setText(AppConfiguration.levelTypes);


        st_gen_Arr.add("Male");
        st_gen_Arr.add("Female");

        inst_gen_Arr.add("No Preference");
        inst_gen_Arr.add("Male");
        inst_gen_Arr.add("Female");

        inst_nature_Arr.add("No Preference");
        inst_nature_Arr.add("GENTLE");
        inst_nature_Arr.add("FIRM");

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    isInternetPresent = Utility.isNetworkConnected(EditStudent.this);
                    if (!isInternetPresent) {
                        onDetectNetworkState().show();
                    } else {
                        updateProcess();
                    }
                    ManageStudents.updated = true;
                } else {
                    Toast.makeText(mContext, "Please fill all Required Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLevelCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(mContext, AddStudent2.class), 11);
//05-07-2017 megha for recalculate level
                Intent iAddStudent2 = new Intent(EditStudent.this, AddStudent2.class);
                iAddStudent2.putExtra("fromWhere", "edit");
                startActivity(iAddStudent2);
                ManageStudents.edit = true;
                recalculate = true;
            }
        });
        st_gen = new ListPopupWindow(mContext);
        inst_gen = new ListPopupWindow(mContext);
        inst_nature = new ListPopupWindow(mContext);


        // current date setting
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        edtStudentBdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);

            }
        });
        studentgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_gen.setAdapter(new ArrayAdapter<String>(mContext,
                        R.layout.edittextpopup, st_gen_Arr));
                st_gen.setAnchorView(studentgender);
                st_gen.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                st_gen.setModal(true);

                st_gen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        studentgender.setText(st_gen_Arr.get(pos));
                        st_gen.dismiss();
                    }
                });
                st_gen.show();
            }
        });


        spinner2_nature_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inst_nature.setAdapter(new ArrayAdapter<String>(mContext,
                        R.layout.edittextpopup, inst_nature_Arr));
                inst_nature.setAnchorView(spinner2_nature_type);
                inst_nature.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                inst_nature.setModal(true);

                inst_nature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        spinner2_nature_type.setText(inst_nature_Arr.get(pos));
                        inst_nature.dismiss();
                    }
                });
                inst_nature.show();
            }
        });

        spinnerGenderInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inst_gen.setAdapter(new ArrayAdapter<String>(mContext,
                        R.layout.edittextpopup, inst_gen_Arr));
                inst_gen.setAnchorView(spinnerGenderInstructor);
                inst_gen.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                inst_gen.setModal(true);

                inst_gen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        spinnerGenderInstructor.setText(inst_gen_Arr.get(pos));
                        inst_gen.dismiss();
                    }
                });
                inst_gen.show();
            }
        });
        isInternetPresent = Utility.isNetworkConnected(EditStudent.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            new MyAccountAgeCalculationAsyncTask().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {
            ManageStudents.edit = false;
            level_number.setText("Level " + AppConfiguration.levelTypes + " -  ");
            level_no.setText(AppConfiguration.levelTypes);
            isInternetPresent = Utility.isNetworkConnected(EditStudent.this);
            if (!isInternetPresent) {
                onDetectNetworkState().show();
            } else {
                new MyAccountAgeCalculationAsyncTask().execute();
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

    public boolean Validation() {
        boolean validate = false;

        if (getLength(edtSFirstname) > 0) {
            if (getLength(edtSLastname) > 0) {
                if (edtStudentBdate.getText().toString().trim().length() > 0) {
                    validate = true;
                }
            }
        }

        return validate;
    }

    public int getLength(EditText et) {

        return et.getText().toString().trim().length();
    }

    public void preparingLessionTypeList() {
        llCheckbox.removeAllViews();
        for (int i = 0; i < AppConfiguration.lessionTypeList.size(); i++) {
            checkBox = new CheckBox(EditStudent.this);
            checkBox.setId(Integer.parseInt(AppConfiguration.lessionTypeList.get(i).get("lessonid")));
            checkBox.setText(AppConfiguration.lessionTypeList.get(i).get("lessonname"));
            checkBox.setPadding(5, 5, 5, 5);
            checkBox.setButtonDrawable(R.drawable.custom_check_orange);
            checkBox.setTextColor(getResources().getColor(R.color.black));

            checkBox.setEnabled(true);

//13-06-2017 megha
            if (!recalculate) {
                List<String> items = Arrays.asList(AppConfiguration.classTypes.split("\\s*,\\s*"));
                if (items.contains(AppConfiguration.lessionTypeList.get(i).get("lessonname"))) {
                    checkBox.setButtonDrawable(R.drawable.custom_check_orange);
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                llCheckbox.addView(checkBox);
            }


        }
    }

    //DatePicker for Age
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    // mm/dd/yyyy
    String selected_date = "";

    private void showDate(int year, int month, int day) {
        edtStudentBdate.setText(new StringBuilder().append(getMonth(month).substring(0, 3)).append(" ").append(day).append(", ").append(year));
        selected_date = month + "/" + day + "/" + year;
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.set(year, month, day);
        Date birthday = cal.getTime();

        long dateSubtract = today.getTime() - birthday.getTime();
        long time = 1000 * 60 * 60 * 24;
        System.out.println("I've lived " + dateSubtract / time + " Day");

        double age = (double) (((dateSubtract / time) + 30) / 365.25);
        DecimalFormat df = new DecimalFormat("#.##");
        age = Double.valueOf(df.format(age));

        age_value = String.valueOf(age);
        if (!age_value.equalsIgnoreCase("")) {
            AppConfiguration.studentAge = age_value;
        }
    }


    //AgeCalculation
    String successAgeCalculation = "", message = "";

    class MyAccountAgeCalculationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(EditStudent.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

            AppConfiguration.lessionTypeList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAgeCalculationLoading();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successAgeCalculation.toString().equals("True")) {
                preparingLessionTypeList();
            } else {

            }
        }
    }

    public void getAgeCalculationLoading() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("age", AppConfiguration.studentAge);
        param.put("Level", AppConfiguration.levelTypes);

        String responseString = WebServicesCall.RunScript(AppConfiguration.DOMAIN + AppConfiguration.Get_Lession_by_LevelAge, param);
        readAndParseJSON(responseString);
    }

    public void readAndParseJSON(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successAgeCalculation = reader.getString("Success");
            if (successAgeCalculation.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("FinalArray");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String lessonid = jsonChildNode.getString("LessionID").trim();
                    String lessonname = jsonChildNode.getString("LessionName").trim();

                    hashmap.put("lessonid", lessonid);
                    hashmap.put("lessonname", lessonname);

                    AppConfiguration.lessionTypeList.add(hashmap);
                    Log.d("meghalesson", AppConfiguration.lessionTypeList.toString());
                }
            } else {
                JSONArray jsonMainNode = reader.optJSONArray("ClassByAge");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    message = jsonChildNode.getString("Msg").trim();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //UpdateStudentInfo
    String successUpdateChild;

    class UpdateChildAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(EditStudent.this);
            pd.setMessage(getResources().getString(R.string.pleasewait));
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            updateChildProcess();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            if (successUpdateChild.toString().equals("True")) {
                Toast.makeText(getApplicationContext(), "" + message,
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateChildProcess() {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Token", token);
        param.put("studentid", ""
                + AppConfiguration.studentID);
        param.put("lessonlist", ""
                + AppConfiguration.lessionTypes);
        param.put("Description", ""
                + AppConfiguration.Description);
        param.put("ChildStrongWilledValue", ""
                + AppConfiguration.ChildStrongWilledValue);
        param.put("ChildSensitiveValue", ""
                + AppConfiguration.ChildSensitiveValue);
        param.put("ChildOutGoingValue", ""
                + AppConfiguration.ChildOutgoingValue);
        param.put("ChildStrongWilled", ""
                + AppConfiguration.ChildStrongWilled);
        param.put("ChildSensitive", ""
                + AppConfiguration.ChildSensitive);

        param.put("ChildOutGoing", ""
                + AppConfiguration.ChildOutgoing);
        param.put("chk1", ""
                + AppConfiguration.childApplyCheck1);
        param.put("chk2", ""
                + AppConfiguration.childApplyCheck2);
        param.put("chk3", ""
                + AppConfiguration.childApplyCheck3);
        param.put("Swimgoals", ""
                + AppConfiguration.Swimgoals);

        param.put("levels", ""
                + AppConfiguration.levelTypes);
        param.put("FName", ""
                + AppConfiguration.studentFirstname);
        param.put("LName", ""
                + AppConfiguration.studentLastname);
        param.put("Dob", ""
                + convertDateForWS(AppConfiguration.studentDOB));
        param.put("Age", ""
                + AppConfiguration.studentAge);

        param.put("Gender",
                AppConfiguration.studentGender);

        if (AppConfiguration.instructorGender.equalsIgnoreCase("No Preference")) {
            AppConfiguration.instructorGender = "Any";
        } else if (AppConfiguration.instructorGender.equalsIgnoreCase("Male")) {
            AppConfiguration.instructorGender = "MALE";
        } else if (AppConfiguration.instructorGender.equalsIgnoreCase("Female")) {
            AppConfiguration.instructorGender = "FEMALE";
        }

        if (AppConfiguration.instructorNatureType.equalsIgnoreCase("No Preference")) {
            AppConfiguration.instructorNatureType = "Any";
        }

        param.put("InstructorNature",
                AppConfiguration.instructorNatureType);
        param.put("InstructorGender",
                AppConfiguration.instructorGender);
        param.put("strYesNo1", ""
                + AppConfiguration.strYesNo1);
        param.put("strYesNo2", ""
                + AppConfiguration.strYesNo2);
        param.put("strallergiesmedical",
                AppConfiguration.strallergiesmedical);
        String responseString = WebServicesCall.RunScript(AppConfiguration.updateChildURL, param);

        readAndParseJSONUpdate(responseString);
    }

    public void readAndParseJSONUpdate(String in) {

        try {
            JSONObject reader = new JSONObject(in);
            successUpdateChild = reader.getString("Success");
            if (successUpdateChild.toString().equals("True")) {
                JSONArray jsonMainNode = reader.optJSONArray("EditChild");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    message = jsonChildNode.getString("Msg");
                }

            } else {
                JSONArray jsonMainNode = reader.optJSONArray("EditChild");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    message = jsonChildNode.getString("Msg");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String age_value = "";
    StringBuilder builder;

    public void updateProcess() {
        AppConfiguration.studentFirstname = edtSFirstname.getText().toString().trim();
        AppConfiguration.studentLastname = edtSLastname.getText().toString().trim();
        AppConfiguration.studentGender = studentgender.getText().toString().trim();

        if (!age_value.equalsIgnoreCase("")) {
            AppConfiguration.studentAge = age_value;
        }
        AppConfiguration.instructorGender = spinnerGenderInstructor.getText().toString().trim();
        AppConfiguration.instructorNatureType = spinner2_nature_type.getText().toString().trim();


        builder = new StringBuilder();

        for (int i = 0; i < llCheckbox.getChildCount(); i++) {
            View nextChild = llCheckbox.getChildAt(i);

            if (nextChild instanceof CheckBox) {
                CheckBox check = (CheckBox) nextChild;
                if (check.isChecked()) {
                    builder.append(check.getId() + ",");
                }
            }
        }
        AppConfiguration.levelTypes = level_no.getText().toString();
        AppConfiguration.lessionTypes = builder.toString();
        AppConfiguration.studentDOB = edtStudentBdate.getText().toString();
        new UpdateChildAsyncTask().execute();
    }

    String convertDate(String inputDate) {

        DateFormat theDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch (Exception exception) {
            // Generic catch. Do what you want.
        }

        theDateFormat = new SimpleDateFormat("MMM dd, yyyy");

        return theDateFormat.format(date);
    }

    String convertDateForWS(String inputDate) {

        DateFormat theDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch (Exception exception) {
            // Generic catch. Do what you want.
        }

        theDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        return theDateFormat.format(date);
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }
}
