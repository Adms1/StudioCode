package com.waterworks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.waterworks.utils.AppConfiguration;

public class SwimCompRegisterSplash extends Activity {
  TextView displayText;
    SharedPreferences prefs;
    String token, familyID, DateValue, eventdates, time, MeetDate_Display;
    Context mContext=this;
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim_comp_register_splash);

        prefs = AppConfiguration.getSharedPrefs(getApplicationContext());
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");

        Intent intent = getIntent();
        if (null != intent) {
            eventdates = intent.getStringExtra("eventdates");
            DateValue = intent.getStringExtra("datevalue");
            time = intent.getStringExtra("time");
            MeetDate_Display = intent.getStringExtra("MeetDate_Display");
        }
        setTitleBar();
        displayText = (TextView) findViewById(R.id.displaymsgTxt);
        displayText.setText((Html.fromHtml("Here are your registration <br> details")));
        Animation zoom_out = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
        zoom_out.setDuration(100);
        displayText.startAnimation(zoom_out);

       Animation zoom_in = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
        zoom_in.setDuration(100);
        displayText.startAnimation(zoom_in);

        new Handler().postDelayed(new Runnable() {
                       @Override
            public void run() {

                Intent i = new Intent(SwimCompRegisterSplash.this, SwimcompititionRegisterStep6Activity.class);
                           i.putExtra("datevalue", DateValue);
                           i.putExtra("time", time);
                           i.putExtra("eventdates", eventdates);
                           i.putExtra("MeetDate_Display", MeetDate_Display);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    public void setTitleBar() {
        View view = findViewById(R.id.layout_top);
        TextView title = (TextView) view.findViewById(R.id.action_title);
        title.setText("Review");
        ImageButton ib_menusad = (ImageButton) view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button) view.findViewById(R.id.relMenu);

        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(SwimCompRegisterSplash.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
    }
}
