package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.jv.mxvideoplayer.mxv.videoplayer.Acitivity.PrivateActivity;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.R;
import com.jv.mxvideoplayer.mxv.videoplayer.Utilitie;

import java.util.ArrayList;
import java.util.List;

import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.pinLock;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.securityAns;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.securityQue;


public class PinLockScreenPasswordActivity extends AppCompatActivity implements PinEntryAuthenticationListener {

    Activity activity = PinLockScreenPasswordActivity.this;
    PinView pinView;

    LinearLayout layoutForgotPass, layoutLock, btnDone;
    ImageView btnBack;
    InputMethodManager inputMethodManager;
    EditText edtPass;
    TextView edtQue, txtPass;

    int[] resourceLayout = {R.id.pin1, R.id.pin2, R.id.pin3, R.id.pin4, R.id.pin5, R.id.pin6, R.id.pin7, R.id.pin8, R.id.pin9, R.id.pin0};
    ImageView[] imgArray;
    RelativeLayout relDot;

    int wrongPassCounter = 0;
    TextView txtForgetPass;
    private LinearLayout ll_ad_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin_lock_screen_password);
//        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(this);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId(Utilitie.admobbanner);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        ll_ad_container = findViewById(R.id.ll_ad_container);
        if (ll_ad_container != null) {
            ll_ad_container.removeAllViews();
        }
//        ll_ad_container.addView(adView);
//        loadNativeAd();
        bindToolbar();
        bindControl();
        clickEvent();
    }

    private void bindToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.txtToolbarTitle)).setText(getResources().getString(R.string.private_video));
    }

    private void bindControl() {

//        layoutLock = findViewById(R.id.layoutLock);
        pinView = findViewById(R.id.pinView);
        relDot = findViewById(R.id.relDot);
        pinView.setModeAuthenticate(this);
        txtForgetPass = findViewById(R.id.txtForgetPass);

        layoutForgotPass = findViewById(R.id.layoutForgotPass);
        btnDone = findViewById(R.id.btnDone);
        btnBack = findViewById(R.id.btnBack);
        edtPass = findViewById(R.id.edtPass);
        edtQue = findViewById(R.id.edtQue);
        txtPass = findViewById(R.id.txtPass);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        edtQue.setText(SharedPref.getSharedPrefData(activity, securityQue));

        imgArray = new ImageView[10];

        for (int i = 0; i < 10; i++) {
            imgArray[i] = findViewById(resourceLayout[i]);
        }
    }

    private void clickEvent() {

        relDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutForgotPass.setVisibility(View.VISIBLE);
                // layoutLock.setVisibility(View.GONE);
                txtForgetPass.setVisibility(View.GONE);
                showSoftKeyboard(inputMethodManager, edtPass);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutForgotPass.setVisibility(View.GONE);
                //  layoutLock.setVisibility(View.VISIBLE);
                txtForgetPass.setVisibility(View.GONE);
                dismissSoftKeyboard(inputMethodManager, edtPass);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPass = SharedPref.getSharedPrefData(activity, pinLock);
                if (edtPass.getText().toString().equals(SharedPref.getSharedPrefData(activity, securityAns))) {
                    txtPass.setText("Your password is :- " + currentPass);
                    edtPass.setText("");
                } else {
                    edtPass.setText("");
                }
            }
        });
    }

    public void dismissSoftKeyboard(InputMethodManager inputMethodManager, EditText addTxtEditText) {
        addTxtEditText.setFocusable(false);
        addTxtEditText.setEnabled(false);
        addTxtEditText.setFocusableInTouchMode(false);
        addTxtEditText.setClickable(false);
        inputMethodManager.hideSoftInputFromWindow(addTxtEditText.getWindowToken(), 0);
        addTxtEditText.clearFocus();
    }

    public void showSoftKeyboard(InputMethodManager inputMethodManager, EditText addTxtEditText) {
        addTxtEditText.setFocusable(true);
        addTxtEditText.setEnabled(true);
        addTxtEditText.setFocusableInTouchMode(true);
        addTxtEditText.setClickable(true);
        inputMethodManager.toggleSoftInputFromWindow(addTxtEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        addTxtEditText.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_private, menu);
        return true;
    }

//    private NativeAdLayout nativeAdLayout;
//    private LinearLayout adView;
//    private NativeAd nativeAd;
//
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.menu_change_pin: {
                startActivity(new Intent(activity, ChangePasswordActivity.class));
                finish();
                break;
            }
            case R.id.menu_change_security: {
                startActivity(new Intent(activity, ChangeQuestionActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPinCorrect() {
        startActivity(new Intent(activity, PrivateActivity.class));
        finish();
    }

    @Override
    public void onPinWrong() {
        wrongPassCounter++;
        if (wrongPassCounter == 3)
            txtForgetPass.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        edtQue.setText(SharedPref.getSharedPrefData(activity, securityQue));
        super.onResume();
    }
}
