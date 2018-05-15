package com.wateron.smartrhomes.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.wateron.smartrhomes.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Paranjay on 04-12-2017.
 */

public class App extends Application {

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/roboto_light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    synchronized public Tracker getDefaultTracker(){
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }
        return sTracker;
    }
}
