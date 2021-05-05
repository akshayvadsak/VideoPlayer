package com.jv.mxvideoplayer.mxv.videoplayer.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;

//import com.facebook.ads.AudienceNetworkAds;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.AppUtils;

/**
 * Created by jigs patel on 08-12-2019.
 */

public class MyApp extends Application {
    protected String userAgent;

    private static Context sContext;


    @Override
    public void onCreate() {
        super.onCreate();

        userAgent = Util.getUserAgent(this, "Video Player");
        //AdSettings.addTestDevice("16daf5f2cc09c96829f9fd031f9aeca8");

//        FontsOverride1.setDefaultFont(this, "MONOSPACE", "Traffolight.otf");
        sContext = getApplicationContext();
//        AudienceNetworkAds.initialize(this);

    }


    public static Context getContext() {
        return sContext;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppUtils.LOG("onLowMemory");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
