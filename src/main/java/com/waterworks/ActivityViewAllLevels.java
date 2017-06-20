package com.waterworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityViewAllLevels extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_levels);
        setHeaderView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void setHeaderView(){
        // fetching header view
        View view = findViewById(R.id.header);

        Button btnHome = (Button) view.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(ActivityViewAllLevels.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });

        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);

        TextView action_title = (TextView) view.findViewById(R.id.action_title);
        action_title.setText("View All Levels");

        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
