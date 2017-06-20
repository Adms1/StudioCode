package com.waterworks.manageProfile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.waterworks.DashBoardActivity;
import com.waterworks.LapSwimsDiscountActivity;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Harsh Adms on 07/01/2016.
 */
public class AddStudent extends Activity {
    String token, familyID;

    EditText edtSFirstname, edtSLastname, allergy;
    Button spinnerGenderInstructor, spinner2_nature_type, studentgender, edtStudentBdate, btnHome;
    CardView btn_update;
    ListPopupWindow st_gen, inst_gen, inst_nature;
    Context mContext = this;

    LinearLayout lledtSFirstname, lledtSLastname, lledtStudentBdate, llstudentgender, llGenderInstructor, llspinner2_nature_type, llallergy_group;

    //List Arrays
    ArrayList<String> st_gen_Arr = new ArrayList<String>();
    ArrayList<String> inst_gen_Arr = new ArrayList<String>();
    ArrayList<String> inst_nature_Arr = new ArrayList<String>();

    //Calendar
    private Calendar calendar;
    private int year, month, day;

    TextView allergy_notice;
    RadioGroup allergy_group;
    String fromWhere = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstudent);

        //getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        fromWhere = getIntent().getStringExtra("fromWhere");

        init();
    }

    public void init() {
        View view = findViewById(R.id.header);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Register New Student");

        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        Button relMenu = (Button) view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(AddStudent.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        edtSFirstname = (EditText) findViewById(R.id.edtSFirstname);
        edtSLastname = (EditText) findViewById(R.id.edtSLastname);
        edtStudentBdate = (Button) findViewById(R.id.edtStudentBdate);
        allergy = (EditText) findViewById(R.id.allergy);

        allergy_group = (RadioGroup) findViewById(R.id.allergy_group);
        allergy_notice = (TextView) findViewById(R.id.allergy_notice);

        spinnerGenderInstructor = (Button) findViewById(R.id.spinnerGenderInstructor);
        spinner2_nature_type = (Button) findViewById(R.id.spinner2_nature_type);
        btn_update = (CardView) findViewById(R.id.btn_update);
        studentgender = (Button) findViewById(R.id.studentgender);

        lledtSFirstname = (LinearLayout) findViewById(R.id.lledtSFirstname);
        lledtSLastname = (LinearLayout) findViewById(R.id.lledtSLastname);
        lledtStudentBdate = (LinearLayout) findViewById(R.id.lledtStudentBdate);
        llstudentgender = (LinearLayout) findViewById(R.id.llstudentgender);
        llGenderInstructor = (LinearLayout) findViewById(R.id.llGenderInstructor);
        llspinner2_nature_type = (LinearLayout) findViewById(R.id.llspinner2_nature_type);
        llallergy_group = (LinearLayout) findViewById(R.id.llallergy_group);

        st_gen_Arr.add("Male");
        st_gen_Arr.add("Female");

        inst_gen_Arr.add("No Preference");
        inst_gen_Arr.add("Male");
        inst_gen_Arr.add("Female");

        inst_nature_Arr.add("No Preference");
        inst_nature_Arr.add("GENTLE");
        inst_nature_Arr.add("FIRM");


        st_gen = new ListPopupWindow(mContext);
        inst_gen = new ListPopupWindow(mContext);
        inst_nature = new ListPopupWindow(mContext);


        // current date setting
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    collectValues();
                    Intent i = new Intent(mContext, AddStudent2.class);
                    if(fromWhere != null){
                        i.putExtra("fromWhere", "register");
                    }
                    startActivity(i);
                } else {
                    Toast.makeText(mContext, "Please fill all Required Fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        edtSFirstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lledtSFirstname.setBackgroundResource(R.drawable.pure_error_border_white);
            }
        });

        edtSLastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lledtSLastname.setBackgroundResource(R.drawable.pure_error_border_white);
            }
        });

        edtStudentBdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lledtStudentBdate.setBackgroundResource(R.drawable.pure_error_border_white);

                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtStudentBdate.getWindowToken(), 0);
                showDialog(999);

            }
        });
        studentgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llstudentgender.setBackgroundResource(R.drawable.pure_error_border_white);

                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtStudentBdate.getWindowToken(), 0);

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

                llspinner2_nature_type.setBackgroundResource(R.drawable.pure_error_border_white);

                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtStudentBdate.getWindowToken(), 0);

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

                llGenderInstructor.setBackgroundResource(R.drawable.pure_error_border_white);

                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtStudentBdate.getWindowToken(), 0);

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

        allergy_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                llallergy_group.setBackgroundResource(R.drawable.pure_error_border_white);

                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtStudentBdate.getWindowToken(), 0);

                if (checkedId == R.id.yes) {
                    allergy.setVisibility(View.VISIBLE);
                    allergy_notice.setVisibility(View.VISIBLE);
                } else {
                    allergy.setVisibility(View.GONE);
                    allergy_notice.setVisibility(View.GONE);
                }
            }
        });
    }

    public boolean validation() {
        boolean done = false;

        if (getLength(edtSFirstname) > 0) {
            if (getLength(edtSLastname) > 0) {
                if (edtStudentBdate.getText().toString().trim().length() > 0) {
                    if (studentgender.getText().toString().trim().length() > 0) {
                        if (spinner2_nature_type.getText().toString().trim().length() > 0) {
                            if (spinnerGenderInstructor.getText().toString().trim().length() > 0) {
                                if (allergy_group.getCheckedRadioButtonId() != -1) {
                                    if (allergy_group.getCheckedRadioButtonId() == R.id.yes) {
                                        if (getLength(allergy) > 0) {
                                            done = true;
                                        }
                                    } else {
                                        done = true;
                                    }
                                }else {
                                    llallergy_group.setBackgroundResource(R.drawable.error_border);
                                }
                            }else {
                                llGenderInstructor.setBackgroundResource(R.drawable.error_border);
                            }
                        }else {
                            llspinner2_nature_type.setBackgroundResource(R.drawable.error_border);
                        }
                    }else {
                        llstudentgender.setBackgroundResource(R.drawable.error_border);
                    }
                }else {
                    lledtStudentBdate.setBackgroundResource(R.drawable.error_border);
                }
            }else {
                lledtSLastname.setBackgroundResource(R.drawable.error_border);
            }
        }else {
            lledtSFirstname.setBackgroundResource(R.drawable.error_border);
        }
        return done;
    }

    public int getLength(EditText et) {

        return et.getText().toString().trim().length();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ManageStudents.updated) {
            finish();
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
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    // mm/dd/yyyy
    String selected_date = "";

    private void showDate(int year, int month, int day) {
        AppConfiguration.studentAge = "";
        edtStudentBdate.setText(new StringBuilder().append(getMonth(month).substring(0,3)).append(" ")
                .append(day).append(", ").append(year));

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

        AppConfiguration.studentAge = String.valueOf(age);
    }

    public void collectValues() {
        AppConfiguration.studentFirstname = edtSFirstname.getText().toString().trim();
        AppConfiguration.studentLastname = edtSLastname.getText().toString().trim();
        AppConfiguration.studentDOB = selected_date;
        AppConfiguration.studentGender = studentgender.getText().toString().trim();
        AppConfiguration.instructorNatureType = spinner2_nature_type.getText().toString().trim();
        AppConfiguration.instructorGender = spinnerGenderInstructor.getText().toString().trim();
        if (AppConfiguration.instructorGender.equalsIgnoreCase("No Preference")) {
            AppConfiguration.instructorGender = "Any";
        }else if (AppConfiguration.instructorGender.equalsIgnoreCase("Male")) {
            AppConfiguration.instructorGender = "MALE";
        }else if (AppConfiguration.instructorGender.equalsIgnoreCase("Female")) {
            AppConfiguration.instructorGender = "FEMALE";
        }
        if (AppConfiguration.instructorNatureType.equalsIgnoreCase("No Preference")) {
            AppConfiguration.instructorNatureType = "Any";
        }

        AppConfiguration.strallergiesmedical = allergy.getText().toString().trim();
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }
}
