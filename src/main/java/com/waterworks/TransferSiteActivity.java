package com.waterworks;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.waterworks.adapter.TransferMakeupSiteAdapter;

public class TransferSiteActivity extends Activity {
    ImageButton ib_back;
    ListView lv_site;
    TextView tv_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_site);
        Initialization();
    }

    private void Initialization() {
        // TODO Auto-generated method stub
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tv_note = (TextView) findViewById(R.id.tv_tm_note2);
        if (NewTransferMakeUpActivity.message.toString().equalsIgnoreCase("")) {
            String next = "<font color='#EE0000'> * Transfer Makeups from one site to another.</font>";
            tv_note.setText(Html.fromHtml(next));
        } else {
            tv_note.setText(NewTransferMakeUpActivity.message);
        }
        lv_site = (ListView) findViewById(R.id.lv_tm_site_list);
        lv_site.setAdapter(new TransferMakeupSiteAdapter(
                NewTransferMakeUpActivity.tbid, NewTransferMakeUpActivity.date,
                NewTransferMakeUpActivity.time,
                NewTransferMakeUpActivity.sttimehr,
                NewTransferMakeUpActivity.sttimemin,
                NewTransferMakeUpActivity.lessontype,
                NewTransferMakeUpActivity.lessonId,
                NewTransferMakeUpActivity.attendancetype,
                NewTransferMakeUpActivity.attendanceId,
                NewTransferMakeUpActivity.Name,
                NewTransferMakeUpActivity.firstName,
                NewTransferMakeUpActivity.lastName,
                NewTransferMakeUpActivity.siteId,
                NewTransferMakeUpActivity.siteName,
                NewTransferMakeUpActivity.all_lessonid,
                NewTransferMakeUpActivity.all_lessonname,
                TransferSiteActivity.this));
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

}
