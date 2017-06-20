package com.waterworks;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by admsandroid on 5/10/2017.
 */
//@ReportsCrashes(mailTo = "shrenik.diwanji@gmail.com,jon@waterworksswim.com,navin@admsystems.net", // my email here
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.toast_crash)
@ReportsCrashes(mailTo = "admsbuild@gmail.com", // my email here
        mode = ReportingInteractionMode.SILENT,
        resToastText = R.string.toast_crash)

public class ClientApp extends Application {

    @Override
    public void onCreate() {

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
    }
    @Override
    protected void attachBaseContext(Context base) {

        MultiDex.install(this);
        super.attachBaseContext(base);
    }
}
