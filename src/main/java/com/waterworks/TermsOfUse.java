package com.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waterworks.sheduling.ScheduleLessonFragement;
import com.waterworks.utils.Utility;

/**
 * Created by Rakesh Tiwari ADMS on 2/1/2016.
 */
public class TermsOfUse extends Activity {
    RelativeLayout title_black;
    Button returnStack;
    TextView page_title,tv_termsOfUse;
    WebView wvTermsOfUse;
    Context mContext =this;
    Boolean isInternetPresent =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_of_use);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        View view = (View)findViewById(R.id.header);
        title_black = (RelativeLayout)view.findViewById(R.id.title_black);
        title_black.setVisibility(View.GONE);
        returnStack=(Button)view.findViewById(R.id.returnStack);
        page_title=(TextView)view.findViewById(R.id.page_title);
        page_title.setText("Waterworks Terms of Use");
        wvTermsOfUse = (WebView) findViewById(R.id.wvTermsOfUse);
        isInternetPresent = Utility.isNetworkConnected(TermsOfUse.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            wvTermsOfUse.loadUrl("file:///android_asset/termsofuse.html");
        }
        WebSettings webSettings = wvTermsOfUse.getSettings();
        webSettings.setDefaultFontSize(14);
        tv_termsOfUse=(TextView)findViewById(R.id.tv_termsOfUse);
        tv_termsOfUse.setText(Html.fromHtml("<tbody>\n" +
                "\n" +
                "<tr>\n" +
                "<td colspan=\"3\">\n" +
                "<p><b> Please call our office for details.</b><br>\n" +
                "<a href=\"http://www.waterworksswim.com\">www.waterworksswim.com  </a><br>\n" +
                "Tel: 949-450-0777   <br>\n" +
                "Email: <a href=\"mailto:info@waterworksswim.com\">info@waterworksswim.com </a><br>\n" +
                "</p>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>"));

        returnStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(TermsOfUse.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
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
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
}
