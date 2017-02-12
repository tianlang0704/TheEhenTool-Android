package org.acrew.cmonk.theehentool_preview.common;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import org.acrew.cmonk.theehentool_preview.common.tools.EHenConfigHelper;
import org.acrew.cmonk.theehentool_preview.common.tools.SearchConfigHelper;

/**
 * Created by CMonk on 2/6/2017.
 */

public class TheEhenToolApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitializeFAN();
    }

    private void InitializeFAN() {
        AndroidNetworking.initialize(this);
    }
}
