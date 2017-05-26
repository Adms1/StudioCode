package com.waterworks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

/**
 * Created by AndroidPC on 2/6/2017.
 */

public class Login_welcome_splashscreen extends AppCompatActivity {
    Button buttonok;
    Context mContext=this;
    String DOMAIN = "", token = "";
    TextView txt1, txt2, txt3, txt4, txtheader, displaymsgTxt;
    LinearLayout border;
    Boolean isInternetPresent = false;
    SharedPreferences SP;
    String user;
    public static String filename = "Valustoringfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_welcome_splashscreen);

        buttonok = (Button) findViewById(R.id.btn_login_welcome_ok);
        txt1 = (TextView) findViewById(R.id.displaymsgTxt);
        txt2 = (TextView) findViewById(R.id.displaymsgTxt2);
        txt3 = (TextView) findViewById(R.id.displaymsgTxt3);
        txt4 = (TextView) findViewById(R.id.displaymsgTxt4);
        txtheader = (TextView) findViewById(R.id.displayheaderTxt);
        displaymsgTxt = (TextView) findViewById(R.id.displaymsgTxt1);
        border = (LinearLayout) findViewById(R.id.start_redBorder);

        txt1.setText(Html.fromHtml(" <font color='#003d71'>Thank you for being part of the Select Beta Test Program for this new Waterworks Aquatics mobile app.</font>"));
        txt2.setText(getResources().getString(R.string.bullet));


        try {
            if (DOMAIN.isEmpty()) {
                DOMAIN = AppConfiguration.DOMAIN;
                Log.i("Login", "Url = " + DOMAIN);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = SP.edit();
//                editor.putString("LoginStatus",user );
                isInternetPresent = Utility.isNetworkConnected(Login_welcome_splashscreen.this);
                if (!isInternetPresent)
                {
                    onDetectNetworkState().show();
                }
                else
                {
                            SP = getSharedPreferences(filename, 0);
                            String getname = SP.getString("username", "");
                            SharedPreferences sp1 = getSharedPreferences("key", 0);
                            SharedPreferences.Editor sedt = sp1.edit();
                            sedt.putString(getname, "yes");
                            sedt.commit();

                    Intent i = new Intent(Login_welcome_splashscreen.this, DashBoardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("DOMAIN", DOMAIN);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            }
        });
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}