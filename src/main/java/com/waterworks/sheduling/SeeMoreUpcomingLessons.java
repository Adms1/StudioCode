/**
 * 
 */
package com.waterworks.sheduling;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.waterworks.HomeFragment;
import com.waterworks.R;
import com.waterworks.utils.AppConfiguration;

/**
 * @author Harsh Adms
 *
 */
public class SeeMoreUpcomingLessons  extends Activity{

	ImageLoader imageLoader;
	LinearLayout inflate_multiple;
	Button BackButton;
	Fade mFade;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seemore_lesson);

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(SeeMoreUpcomingLessons.this));
		inflate_multiple = (LinearLayout)findViewById(R.id.inflate_multiple);
		BackButton = (Button)findViewById(R.id.relMenu);

		if(AppConfiguration.animation){
			mFade = new Fade(Fade.IN);
		}
		BackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		for (int i = 0; i < HomeFragment.multiStudent.size(); i++) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View view = inflater.inflate(R.layout.d2_multiplestudent_nextschdl, null);

			TextView detai_text,inst;
			CircleImageView inst_dp_cust;


			detai_text = (TextView)view.findViewById(R.id.detai_text);
			inst_dp_cust =  (CircleImageView)view.findViewById(R.id.inst_DP);
			inst = (TextView)view.findViewById(R.id.inst_name);
			detai_text.setText(HomeFragment.multiStudent.get(i).get("ScheduleDate") +" - " +
					HomeFragment.multiStudent.get(i).get("StudentName"));

			inst.setText(HomeFragment.multiStudent.get(i).get("Instructor"));
			imageLoader.displayImage(HomeFragment.multiStudent.get(i).get("Photo") , inst_dp_cust);
			if(AppConfiguration.animation){
				TransitionManager.beginDelayedTransition(inflate_multiple, mFade);
			}
			inflate_multiple.addView(view);
		}
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}
}
