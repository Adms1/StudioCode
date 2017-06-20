package com.waterworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class LapSwimsActivity extends Activity {
	LinearLayout ll_ls_1,ll_ls_2,ll_ls_3;
//	ImageButton btn_back;
	String siteid;
	Button relMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lap_swims);
//		siteid = getIntent().getStringExtra("SiteId");
		Initialization();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	}

	private void Initialization() {
		// TODO Auto-generated method stub

		relMenu = (Button)findViewById(R.id.relMenu);
		ll_ls_1 = (LinearLayout)findViewById(R.id.ll_ls_1);
		ll_ls_2 = (LinearLayout)findViewById(R.id.ll_ls_2);
		ll_ls_3 = (LinearLayout)findViewById(R.id.ll_ls_3);
		relMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCheckin = new Intent(LapSwimsActivity.this, DashBoardActivity.class);
				intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCheckin);
				finish();
			}
		});
		ll_ls_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				Intent sessionsLapIntent = new Intent(getApplicationContext(),LapSwimsSessionActivity.class);
//				sessionsLapIntent.putExtra("siteid", siteid);
				startActivity(sessionsLapIntent);
//				finish();
			}
		});
		ll_ls_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
//				Intent discountLapIntent = new Intent(getApplicationContext(),LapSwimsMonthlyActivity.class);
				Intent monthLapIntent = new Intent(getApplicationContext(),LapSwimsDiscountActivity.class);
//				monthLapIntent.putExtra("siteid", siteid);
				startActivity(monthLapIntent);
//				finish();
			}
		});

		ll_ls_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				Intent discountLapIntent = new Intent(getApplicationContext(),LapSwimsMonthlyActivity.class);
//				discountLapIntent.putExtra("siteid", siteid);
				startActivity(discountLapIntent);
//				finish();
			}
		});
	}

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
