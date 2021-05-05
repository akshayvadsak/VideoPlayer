package com.jv.mxvideoplayer.mxv.videoplayer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.InterstitialAd;
//import com.facebook.ads.InterstitialAdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class PageActivity extends AppCompatActivity {

    private CustomPagerAdapter customPagerAdapter;
    private ViewPager pager;
    private TabPageIndicator indicator;
    private ArrayList listIcon;
    private ArrayList fragmentContents;
    ImageView right, left;
    TextView ok;
    private RelativeLayout theme;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        theme = findViewById(R.id.theme);
//        loadFacebookInterstitial(PageActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.txtToolbarTitle)).setText(getResources().getString(R.string.theme));
        theme();
        ok = findViewById(R.id.ok);
        getTabsIcon();
        customPagerAdapter = new CustomPagerAdapter(
                getSupportFragmentManager(), listIcon, fragmentContents);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(customPagerAdapter);
//        loadFacebookInterstitial(PageActivity.this);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                SharedPreferences settings = getSharedPreferences("pref", 0);
                int id = settings.getInt("theme", -1);
                if (id > 0) {
                    pager.setCurrentItem(id);

                } else {
                    pager.setCurrentItem(3);
                }
            }
        }, 100);

        indicator = (TabPageIndicator) findViewById(R.id.tabs);


        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);

            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ShowInterstitial(PageActivity.this);
                SharedPreferences settings = getSharedPreferences("pref", 0);
                SharedPreferences.Editor editor = settings.edit();
                Log.println(Log.ASSERT, "====", "" + pager.getCurrentItem());
                editor.putInt("theme", pager.getCurrentItem());
                editor.commit();
//                ShowInterstitial(PageActivity.this);
                finish();
            }
        });


        indicator.setViewPager(pager);


    }

//    public static InterstitialAd interstitialAd ;
//
//    public static void loadFacebookInterstitial(Activity activity){
//        interstitialAd = new InterstitialAd(activity, "190108885922911_190111279256005");
//        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
////                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
////                interstitialAd.show();
////                loadFacebookInterstitial(activity);
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//            }
//        };
//
//        interstitialAd.loadAd(
//                interstitialAd.buildLoadAdConfig()
//                        .withAdListener(interstitialAdListener)
//                        .build());
//    }
//
//
//    public static void ShowInterstitial(Activity activity){
//        Handler handler=new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
//                    return;
//                }
//                if(interstitialAd.isAdInvalidated()) {
//                    return;
//                }
//                interstitialAd.show();
//                loadFacebookInterstitial(activity);
//            }
//        }, 500);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        theme();
    }

    public void theme() {
        theme = findViewById(R.id.theme);
        SharedPreferences settings = getSharedPreferences("pref", 0);
        int id = settings.getInt("theme", 3);
        if (theme != null) {
            if (id == 1) {
                theme.setBackgroundResource(R.drawable.theme2);
            } else if (id == 2) {
                theme.setBackgroundResource(R.drawable.theme3);
            } else if (id == 3) {
                theme.setBackgroundResource(R.drawable.theme4);
            } else if (id == 4) {
                theme.setBackgroundResource(R.drawable.theme5);
            } else if (id == 5) {
                theme.setBackgroundResource(R.drawable.theme6);
            } else if (id == 6) {
                theme.setBackgroundResource(R.drawable.theme7);
            } else if (id == 7) {
                theme.setBackgroundResource(R.drawable.theme8);
            } else if (id == 8) {
                theme.setBackgroundResource(R.drawable.theme9);
            } else if (id == 9) {
                theme.setBackgroundResource(R.drawable.theme10);
            } else {
                theme.setBackgroundResource(R.drawable.theme1);

            }
        }

    }

    private void getTabsIcon() {
        listIcon = new ArrayList();
        listIcon.add(R.drawable.tab_text_them);
        listIcon.add(R.drawable.tab_text_them1);
        listIcon.add(R.drawable.tab_text_them2);
        listIcon.add(R.drawable.tab_text_them3);
        listIcon.add(R.drawable.tab_text_them4);
        listIcon.add(R.drawable.tab_text_them5);
        listIcon.add(R.drawable.tab_text_them6);
        listIcon.add(R.drawable.tab_text_them7);
        listIcon.add(R.drawable.tab_text_them8);
        listIcon.add(R.drawable.tab_text_them9);

        fragmentContents = new ArrayList();
        fragmentContents.add("This is Musics Page");
        fragmentContents.add("This is Videos Page");
        fragmentContents.add("This is Foods Page");
        fragmentContents.add("This is Friends Page");
        fragmentContents.add("This is Friends Page");
        fragmentContents.add("This is Friends Page");
        fragmentContents.add("This is Friends Page");
        fragmentContents.add("This is Friends Page");
        fragmentContents.add("This is Friends Page");
        fragmentContents.add("This is Friends Page");
    }
}