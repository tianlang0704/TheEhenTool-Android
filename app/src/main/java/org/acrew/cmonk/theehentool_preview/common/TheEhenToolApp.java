package org.acrew.cmonk.theehentool_preview.common;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by CMonk on 2/6/2017.
 */

public class TheEhenToolApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        this.InitializeFAN();
    }

    private void InitializeFAN() {
        AndroidNetworking.initialize(this);
    }
}
