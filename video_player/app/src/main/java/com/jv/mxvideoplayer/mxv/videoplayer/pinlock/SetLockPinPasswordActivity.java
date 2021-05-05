package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdOptionsView;
//import com.facebook.ads.MediaView;
//import com.facebook.ads.NativeAd;
//import com.facebook.ads.NativeAdLayout;
//import com.facebook.ads.NativeAdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DataBinder;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.Utilitie;

import java.util.ArrayList;
import java.util.List;

import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.oneTime;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.securityAns;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.securityQue;

public class SetLockPinPasswordActivity extends AppCompatActivity implements PinEntrySetupListener {
    Activity activity = SetLockPinPasswordActivity.this;
    PinView pinView;
    private LinearLayout ll_ad_container;
    private LinearLayout ll_ad_container1;
    private Dialog dialog;

    LinearLayout bottomLayout, topLayout, btnDone;
    MaterialSpinner spinner;
    EditText editText;
    Animation slide_up, slide_down;

    int[] resourceLayout = {R.id.pin1, R.id.pin2, R.id.pin3, R.id.pin4, R.id.pin5, R.id.pin6, R.id.pin7, R.id.pin8, R.id.pin9, R.id.pin0};
    ImageView[] imgArray;
    RelativeLayout relDot;
    TextView txtEnterPIN;
//    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;
//    private NativeAd nativeAd;

//    private void loadNativeAd() {
//        // Instantiate a NativeAd object.
//        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
//        // now, while you are testing and replace it later when you have signed up.
//        // While you are using this temporary code you will only get test ads and if you release
//        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
//        nativeAd = new NativeAd(this, "190108885922911_190111569255976");
//
//        NativeAdListener nativeAdListener = new NativeAdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                // Inflate Native Ad into Container
//                inflateAd(nativeAd);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//
//            }
////            @Override
////            public void onAdLoaded(Ad ad) {
////
////            }
//
//        };
//
//        // Request an ad
//        nativeAd.loadAd(
//                nativeAd.buildLoadAdConfig()
//                        .withAdListener(nativeAdListener)
//                        .build());
//    }
//
//    private void inflateAd(NativeAd nativeAd) {
//
//        nativeAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//        nativeAdLayout = findViewById(R.id.native_ad_container);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
//        nativeAdLayout.addView(adView);
//
//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(this, nativeAd, nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
//        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        nativeAd.registerViewForInteraction(
//                adView, nativeAdMedia, nativeAdIcon, clickableViews);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock_pin_pass);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(this);
//        adView.setAdUnitId(Utilitie.admobbanner);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.setAdSize(AdSize.BANNER);
//        adView.loadAd(adRequest);

//        ll_ad_container = findViewById(R.id.ll_ad_container);
//        if (ll_ad_container != null) {
//            ll_ad_container.removeAllViews();
//        }
//        ll_ad_container.addView(adView);

//        loadNativeAd();
//        com.google.android.gms.ads.AdView adView1 = new com.google.android.gms.ads.AdView(this);
//        adView1.setAdUnitId(Utilitie.admobbanner);
//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        adView1.setAdSize(AdSize.SMART_BANNER);
//        adView1.loadAd(adRequest1);
//
//        ll_ad_container1 = findViewById(R.id.ll_ad_container1);
//        if (ll_ad_container1 != null) {
//            ll_ad_container1.removeAllViews();
//        }
//        ll_ad_container1.addView(adView1);


        bindControls();
        bindToolbar();
        clickEvents();
    }

    private void bindToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.txtToolbarTitle)).setText(getResources().getString(R.string.private_video));
    }


    private void bindControls() {
        editText = findViewById(R.id.editText);
        spinner = findViewById(R.id.spinner);
        bottomLayout = findViewById(R.id.bottomLayout);
        topLayout = findViewById(R.id.topLayout);
        btnDone = findViewById(R.id.btnDone);
        pinView = findViewById(R.id.pinView);
        pinView.setModeSetup(this);

        relDot = findViewById(R.id.relDot);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        spinner.setItems(DataBinder.fetchQue());
        txtEnterPIN = (TextView) findViewById(R.id.txtEnterPIN);
        imgArray = new ImageView[10];

        for (int i = 0; i < 10; i++) {
            imgArray[i] = findViewById(resourceLayout[i]);
        }
    }

    private void clickEvents() {

        relDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals(""))
                    Toast.makeText(activity, "Please write your answer", Toast.LENGTH_SHORT).show();
                else
                    processDone();
            }
        });
    }

    @Override
    public void onPinEntered(String pin) {
        Log.d("PIN", "PIN Entered" + pin);
        txtEnterPIN.setText("Confirm Your PIN");
    }

    @Override
    public void onPinConfirmed(String pin) {
        Log.d("PIN", "PIN Confirmed" + pin);
    }

    @Override
    public void onPinMismatch() {
        Log.d("PIN", "PIN Pin Mismatch");
        txtEnterPIN.setText("Mismatch, Re-enter PIN");
    }

    @Override
    public void onPinSet(String pin) {
        Log.d("PIN", "PIN Set" + pin);
        bottomLayout.startAnimation(slide_up);
        bottomLayout.setVisibility(View.VISIBLE);
        topLayout.setVisibility(View.GONE);
    }

    @SuppressLint("WrongConstant")
    public void processDone() {

        SharedPref.setSharedPrefData(activity, oneTime, "1");
        SharedPref.setSharedPrefData(activity, securityQue, DataBinder.fetchQue().get(spinner.getSelectedIndex()));
        SharedPref.setSharedPrefData(activity, securityAns, editText.getText().toString());
//        Intent intent = new Intent(new Intent(activity, PrivateActivity.class));
//        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
