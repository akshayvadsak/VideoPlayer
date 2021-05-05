package com.jv.mxvideoplayer.mxv.videoplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSettings;
//import com.facebook.ads.AdView;
//
//import com.facebook.ads.InterstitialAd;
//import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.jv.mxvideoplayer.mxv.videoplayer.Acitivity.ExoPlayerActivity;
import com.jv.mxvideoplayer.mxv.videoplayer.Acitivity.SearchActivity;
import com.jv.mxvideoplayer.mxv.videoplayer.Acitivity.SettingActivity;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.Constants;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.model.AppCount;
import com.jv.mxvideoplayer.mxv.videoplayer.model.ApplicationList;
import com.jv.mxvideoplayer.mxv.videoplayer.network.ApiClient;
import com.jv.mxvideoplayer.mxv.videoplayer.network.ApiInterface;
import com.jv.mxvideoplayer.mxv.videoplayer.network.ConnectionDetector;
import com.jv.mxvideoplayer.mxv.videoplayer.network.DWIConst;
import com.jv.mxvideoplayer.mxv.videoplayer.pinlock.PinLockScreenPasswordActivity;
import com.jv.mxvideoplayer.mxv.videoplayer.pinlock.SetLockPinPasswordActivity;

import static com.jv.mxvideoplayer.mxv.videoplayer.network.DWIConst.mAppList;

public class MainActivity extends AppCompatActivity {

    public static DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    HomePressListener homePressListener;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    SpotsDialog dialogAds;
    private static final int PERMISSION_REQUEST_CODE = 1;

    LinearLayout theme;
    String wantPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static String TAG="sadkhjsakj";

    public interface HomePressListener {
        void handleHomeButtonPress();

        void handleBackButtonPress();
    }


//    public static void facebookbannerAds(Activity activity) {
//        AdView adView = new AdView(activity, "190108885922911_190110939256039", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//        AdSettings.addTestDevice("c94e5f42-0772-48d4-bb49-629a4ab1eecb");
//        LinearLayout adContainer = (LinearLayout) activity.findViewById(R.id.ll_ad_container);
//        adContainer.addView(adView);
//        AdListener adListener = new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//                Log.e("Erro", "Error"+adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                Log.e("onAdLoaded", "onAdLoaded");
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                Log.e("onAdClicked", "onAdClicked");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                Log.e("onLogging", "onLogging");
//            }
//        };
//        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//
//
//    }
//
//    public static InterstitialAd interstitialAd ;
//
//    public static void loadFacebookInterstitial(Activity activity){
//        interstitialAd = new InterstitialAd(activity, "190108885922911_190111279256005");
//        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//                Log.e(TAG, "Interstitial ad displayed.");
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                Log.e(TAG, "Interstitial ad dismissed.");
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//                interstitialAd.show();
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                Log.d(TAG, "Interstitial ad clicked!");
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                Log.d(TAG, "Interstitial ad impression logged!");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        theme = findViewById(R.id.theme);
        theme();
//        facebookbannerAds(MainActivity.this);

        /*com.google.android.gms.ads.AdView adView1 = new com.google.android.gms.ads.AdView(this);
        adView1.setAdSize(AdSize.LARGE_BANNER);
        adView1.setAdUnitId(Utilitie.admobbanner);*/
        //LinearLayout ll_ad_container;
        ///ll_ad_container = findViewById(R.id.ll_ad_container);
       /* AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);
        if (ll_ad_container != null) {
            ll_ad_container.removeAllViews();
        }
        ll_ad_container.addView(adView1);*/

//        if (!checkPermission(wantPermission)) {
//            requestPermission(wantPermission);
//        } else {
        Fragment fragment = new MainFragment();
        replaceFragment(fragment);
//        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        dialogAds = new SpotsDialog(this, "Showing ADS...", R.style.CustomSpotDialog);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerItemSelected(menuItem.getItemId());
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        pref = getSharedPreferences(DWIConst.PRFS_NAME, Context.MODE_PRIVATE);
        edit = pref.edit();
        edit.commit();

        if (pref.getString(DWIConst.PRFS_INSTALL_COUNT, "").equals("")) {
            if ((new ConnectionDetector(MainActivity.this).isConnectingToInternet())) {
                //   postAppInstallCount();
            }
        }

        if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
            getMoreApps();
        } else {
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commitAllowingStateLoss();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    @Override
    public void onBackPressed() {
        homePressListener = MainFragment.homePressListener;
        homePressListener.handleBackButtonPress();


//        showAdmobInter();
//        if(homePressListener!=null){
//            homePressListener = MainFragment.homePressListener;
//            homePressListener.handleBackButtonPress();
//        }else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                homePressListener = MainFragment.homePressListener;
                homePressListener.handleHomeButtonPress();

                return true;

//            case R.id.menu_lock: {
//                if (SharedPref.getSharedPrefData(MainActivity.this, SharedPref.oneTime, "0").equals("0")) {
//                    startActivity(new Intent(MainActivity.this, SetLockPinPasswordActivity.class));
//                }
//                else {
//                   startActivity(new Intent(MainActivity.this, PinLockScreenPasswordActivity.class));
//                }
//                break;
//            }

            case R.id.menu_setting: {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            }

            case R.id.menu_search: {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            }

//            case R.id.menu_sort: {
//                final Dialog mDialog = new Dialog(MainActivity.this, R.style.CustomDialog);
//                mDialog.setContentView(R.layout.dialog_sort_video);
//                mDialog.show();
//
//                final RadioGroup rgSortType = mDialog.findViewById(R.id.rgSortType);
//
//                String sortType = SharedPref.getSharedPrefData(MainActivity.this, SharedPref.sortType,getResources().getString(R.string.title));
//
//                if (sortType.equalsIgnoreCase(getResources().getString(R.string.title)))
//                    rgSortType.check(R.id.rbTitle);
//                else if (sortType.equalsIgnoreCase(getResources().getString(R.string.duration)))
//                    rgSortType.check(R.id.rbDuration);
//                else if (sortType.equalsIgnoreCase(getResources().getString(R.string.time)))
//                    rgSortType.check(R.id.rbTime);
//                else if (sortType.equalsIgnoreCase(getResources().getString(R.string.size)))
//                    rgSortType.check(R.id.rbSize);
//
//                (mDialog.findViewById(R.id.txtDialogCancel)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        DialogDismiss.dismissWithCheck(mDialog);
//                    }
//                });
//
//                (mDialog.findViewById(R.id.txtDialogConfirm)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        RadioButton rbType = rgSortType.findViewById(rgSortType.getCheckedRadioButtonId());
//                        SharedPref.setSharedPrefData(MainActivity.this, SharedPref.sortType, String.valueOf(rbType.getText()));
//                        DialogDismiss.dismissWithCheck(mDialog);
//
//                        if (MainFragment.mVideoAdapter != null) {
//                            AppUtils.sortVideo(MainActivity.this);
//                            MainFragment.mVideoAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//                break;
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_setting, menu);
        return true;
    }


    private void drawerItemSelected(int id) {
        navigationView.getMenu().findItem(id).setChecked(true);
        switch (id) {

            case R.id.nav_home: {
                mDrawerLayout.closeDrawers();
            }
            break;
            case R.id.nav_policy: {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.policy))));

            }
            break;
            case R.id.nav_more: {


                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.moreApps1))));

            }
            break;
            case R.id.nav_exit: {
                onBackPressed();
            }
            break;
            case R.id.nav_theme: {
                startActivity(new Intent(MainActivity.this, PageActivity.class));
            }
            break;
//            case R.id.nav_Download: {
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
//            }
//            break;

            case R.id.nav_setting: {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
            break;

            case R.id.nav_privacy: {
                if (SharedPref.getSharedPrefData(MainActivity.this, SharedPref.oneTime, "0").equals("0")) {
                    startActivity(new Intent(MainActivity.this, SetLockPinPasswordActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, PinLockScreenPasswordActivity.class));
                }
//                showAdmobInter();

            }
            break;

            case R.id.nav_like:
                mDrawerLayout.closeDrawers();
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
//                showAdmobInter();

                break;

            case R.id.nav_share:
                mDrawerLayout.closeDrawers();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String text = "Install this new Video Player";
                String link = "https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en";
                String shareBody = text + "\n" + link;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
//                showAdmobInter();

                break;


//            case R.id.nav_more_apps: {
//                mDrawerLayout.closeDrawers();
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.moreApps))));
//                if (mAppList != null) {
//                    if (mAppList.size() >= 1) {
//                        Intent in = new Intent(MainActivity.this, AppMarketingActivity.class);
//                        startActivity(in);
//
//                    }
//                }
//                return;
        }
    }


    public void btnRecentPlayed(View view) {
        String s = SharedPref.getSharedPrefData(MainActivity.this, SharedPref.lastPlayed);
        File file = new File(s);
        if (file.exists()) {
            Intent intent = new Intent(MainActivity.this, ExoPlayerActivity.class);
            intent.setData(Uri.fromFile(file));
            startActivity(intent);
//            showAdmobInter();
        }


    }


    private void postAppInstallCount() {
        ApiInterface apiService = ApiClient.getWebClient().create(ApiInterface.class);
        Call<AppCount> call = apiService.postApplicationCount(getApplicationContext().getPackageName());
        call.enqueue(new Callback<AppCount>() {

            @Override
            public void onResponse(Call<AppCount> call, Response<AppCount> response) {
                try {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getStatus().equals(DWIConst.SUCEESS)) {
                            Log.d("Response", "App Install  : " + response.body().getMessage());

                            pref = getSharedPreferences(DWIConst.PRFS_NAME, Context.MODE_PRIVATE);
                            edit = pref.edit();
                            edit.putString(DWIConst.PRFS_INSTALL_COUNT, DWIConst.PRFS_INSTALLED);
                            edit.commit();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AppCount> call, Throwable t) {

            }
        });

    }

    private void getMoreApps() {
        ApiInterface apiService = ApiClient.getWebClient().create(ApiInterface.class);
        Call<ApplicationList> call = apiService.getAppListByAppId(Constants.APP_ID);

        call.enqueue(new Callback<ApplicationList>() {
            @Override
            public void onResponse(Call<ApplicationList> call, Response<ApplicationList> response) {
                try {
                    Log.e("Response", response + "");
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getStatus().equals(DWIConst.SUCEESS)) {

                            mAppList = response.body().getResult();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Crashlytics.logException(e);
                }

            }

            @Override
            public void onFailure(Call<ApplicationList> call, Throwable t) {
                Log.e("TAG", t.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Fragment fragment = new MainFragment();
                    replaceFragment(fragment);
                    Toast.makeText(MainActivity.this, "Permission Granted. Now you can write data.",
                            Toast.LENGTH_LONG).show();
                } else {
                    finish();
                    Toast.makeText(MainActivity.this, "Permission Denied. You cannot write data.",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void requestPermission(String permission) {
        /*if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
            //Toast.makeText(getActivity(), "Write external storage permission allows us to write data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        }*/
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }


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
}
