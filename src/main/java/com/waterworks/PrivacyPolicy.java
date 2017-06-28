package com.waterworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Rakesh Tiwari ADMS on 2/1/2016.
 */
public class PrivacyPolicy extends Activity {
    RelativeLayout title_black;
    Button returnStack;
    TextView page_title,tv_privacy_policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        View view = (View)findViewById(R.id.header);
        title_black = (RelativeLayout)view.findViewById(R.id.title_black);
        title_black.setVisibility(View.GONE);
        returnStack=(Button)view.findViewById(R.id.returnStack);
        page_title=(TextView)view.findViewById(R.id.page_title);
        tv_privacy_policy=(TextView)findViewById(R.id.tv_privacy_policy);

        tv_privacy_policy.setText(Html.fromHtml("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n" +
                "<tbody>\n" +
                "\n" +
                "<tr>\n" +
                "<td colspan=\"3\">\n" +
                "<font color=\"#316595\"><strong>Our Commitment To Privacy</strong></font><br>" +
                "<span class=\"\">" +
                "Your privacy is important to us. To better protect your privacy we provide this notice explaining our online and mobile information practices and the choices you can make about the way your information is collected and used. To make this notice easy to find, we make it available on our homepage and at every point where personally identifiable information may be requested as well as on the mobile application in the Help & Support section. <br><br>\n" +
                "</span>\n" +

                "<font color=\"#316595\"><strong>The Information We Collect</strong></font><br>" +
                "<span class=\"\">"+
                "This notice applies to all information collected or submitted on the Waterworks Aquatics website or mobile application. On some pages, you can order products, make requests, and register to receive materials. The types of personal information collected at these pages are: <br><br>\n" +
                "     Name <br><br>\n" +
                "     Address <br><br>\n" +
                "     Email address <br><br>\n" +
                "     Phone number <br><br>\n" +
                "     Credit/Debit Card or Checking Account Information (via the TransFirst.com POS system – no credit card numbers are stored on our servers)<br><br>\n" +
                "     </span>\n" +

                "<font color=\"#316595\"><strong>The Way We Use Information</strong></font><br>" +
                "<span class=\"\">" +
                "We use the information you provide about yourself when placing an order only to complete that order. We do not share this information with any outside parties unless absolutely necessary to complete that order. We use return email addresses to answer the email we receive. Such addresses are not used for any other purpose and are not shared with outside parties. <br><br>\n" +
                "     </span>\n" +

                "<font color=\"#316595\"><strong>Our Commitment To Data Security</strong></font><br>" +
                "<span class=\"\">To prevent unauthorized access, maintain data accuracy, and ensure the correct use of information, we have put in place appropriate physical, electronic, and managerial procedures to safeguard and secure the information we collect online and via the mobile application. <br><br>\n" +
                "</span>\n" +

                "<font color=\"#316595\"><strong>Our Commitment To Children’s Privacy</strong></font><br>" +
                "<span class=\"\">Protecting the privacy of the very young is especially important. For that reason, we never collect or maintain information at our website from those we actually know are under 13, and no part of our website is structured to attract anyone under 13. <br><br>\n" +
                "</span>\n" +

                "<font color=\"#316595\"><strong>How You Can Access Or Correct Your Information</strong></font><br>" +
                "<span class=\"\">You can access and maintain all of your personally identifiable information that we collect either online or via mobile application. If assistance is required please send an email to us at <a href=\"mailto:info@waterworksswim.com\">info@waterworksswim.com</a>. Please include all information that needs to be updated or changed. Please include a phone number so we may contact you to verify the information that you have provided, if needed.<br><br>\n" +
                "</span>\n" +

                "<font color=\"#316595\"><strong>How To Contact Us</strong></font><br>" +
                "<span class=\"\">Should you have other questions or concerns about these privacy policies, please call us at (949)450-0777 or send us an email at <a href=\"mailto:info@waterworksswim.com\">info@waterworksswim.com</a>. <br>\n" +
                "     </span>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>"));

        if(tv_privacy_policy != null) {
           HelpAndSupportFragment.removeUnderlines((Spannable) tv_privacy_policy.getText());
        }
        page_title.setText("Waterworks Privacy Policy");
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
                Intent intentCheckin = new Intent(PrivacyPolicy.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
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
