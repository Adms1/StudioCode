	/**
 * 
 */
package com.waterworks.sheduling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;

/**
 * @author Harsh Adms
 *
 */
public class WaitListFragment extends Activity{

	View selected_1,selected_2,selected_3;
	LinearLayout scdl_lsn,scdl_mkp,waitlist;
	TextView txt_1,txt_2,txt_3,noti_count;
	Context mContext=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d2_waitlist);

		selected_1 = (View)findViewById(R.id.selected_1);
		selected_2 = (View)findViewById(R.id.selected_2);
		selected_3 = (View)findViewById(R.id.selected_3);

		selected_1.setVisibility(View.GONE);
		selected_2.setVisibility(View.GONE);
		selected_3.setVisibility(View.VISIBLE);

		((AnimationDrawable) selected_3.getBackground()).start();

		init();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

			}
		}, 1000);
	}

	@Override
	public void onPause() {
		super.onPause();
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);

	}

	@Override
	public void onResume() {
		super.onResume();
		this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	}


	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub

		View view = findViewById(R.id.schdl_top);
		Button BackButton = (Button)view.findViewById(R.id.returnStack);
		
		BackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				WaitListFragment.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				WaitListFragment.this.overridePendingTransition(R.anim.fade_in, R.anim.zoom_in);
			}
		});
		
		noti_count = (TextView)findViewById(R.id.noti_count);
		if(!AppConfiguration.Mup_cnt.equals("0")){
//			Rakesh  20112015............
			noti_count.setVisibility(View.VISIBLE);
			noti_count.setText(AppConfiguration.Mup_cnt);
		}else{
			noti_count.setVisibility(View.GONE);
		}
		
		scdl_lsn = (LinearLayout)view.findViewById(R.id.scdl_lsn);
		scdl_mkp = (LinearLayout)view.findViewById(R.id.scdl_mkp);
		waitlist = (LinearLayout)view.findViewById(R.id.waitlist);
		txt_1 = (TextView)view.findViewById(R.id.txt_1);
		txt_2 = (TextView)view.findViewById(R.id.txt_2);
		txt_3 = (TextView)view.findViewById(R.id.txt_3);
		
		scdl_lsn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				scdl_lsn.setBackgroundResource(R.color.design_change_d2);
				scdl_mkp.setBackgroundResource(R.color.design_change_d2);
				waitlist.setBackgroundResource(R.color.design_change_d2);

				selected_1.setVisibility(View.VISIBLE);
				selected_2.setVisibility(View.GONE);
				selected_3.setVisibility(View.GONE);

				txt_1.setTextColor(Color.WHITE);
				txt_2.setTextColor(Color.WHITE);
				txt_3.setTextColor(Color.WHITE);
				
				Intent i = new Intent(mContext,ScheduleLessonFragement.class);
				startActivity(i);
				WaitListFragment.this.overridePendingTransition(0,0);
				finish();
				((AnimationDrawable) selected_1.getBackground()).start();
			}
		});
		scdl_mkp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scdl_lsn.setBackgroundResource(R.color.design_change_d2);
				scdl_mkp.setBackgroundResource(R.color.design_change_d2);
				waitlist.setBackgroundResource(R.color.design_change_d2);

				selected_1.setVisibility(View.GONE);
				selected_2.setVisibility(View.VISIBLE);
				selected_3.setVisibility(View.GONE);

				txt_1.setTextColor(Color.WHITE);
				txt_2.setTextColor(Color.WHITE);
				txt_3.setTextColor(Color.WHITE);
				
				Intent i = new Intent(mContext,ScheduleMakeupFragment.class);
				startActivity(i);
				WaitListFragment.this.overridePendingTransition(0,0);
				finish();
				
				((AnimationDrawable) selected_2.getBackground()).start();
			}
		});
		waitlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scdl_lsn.setBackgroundResource(R.color.design_change_d2);
				scdl_mkp.setBackgroundResource(R.color.design_change_d2);
				waitlist.setBackgroundResource(R.color.design_change_d2);

				selected_1.setVisibility(View.GONE);
				selected_2.setVisibility(View.GONE);
				selected_3.setVisibility(View.VISIBLE);

				txt_1.setTextColor(Color.WHITE);
				txt_2.setTextColor(Color.WHITE);
				txt_3.setTextColor(Color.WHITE);
				
				Intent i = new Intent(mContext,WaitListFragment.class);
				startActivity(i);
				WaitListFragment.this.overridePendingTransition(0,0);
				finish();
				
				((AnimationDrawable) selected_3.getBackground()).start();
			}
		});
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		WaitListFragment.this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//        ScheduleLessonFragement.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
}
