package com.waterworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class Info_Help extends Activity{

	String imageUrl="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_help);

		Intent i = getIntent();
		String whc = i.getStringExtra("which");


		if(whc.contains("check")){
			imageUrl =  "file:///android_asset/check.png";
		}else if(whc.contains("card")){
			imageUrl =  "file:///android_asset/card.png";
		}

		WebView wv = (WebView) findViewById(R.id.help_pic);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.loadUrl(imageUrl);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
