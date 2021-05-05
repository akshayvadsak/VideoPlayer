package com.jv.mxvideoplayer.mxv.videoplayer.Acitivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DialogDismiss;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.R;

//import static com.jv.mxvideoplayer.mxv.videoplayer.MainActivity.facebookbannerAds;

public class SettingActivity extends AppCompatActivity {
    private Activity activity = SettingActivity.this;
    RelativeLayout theme;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        theme = findViewById(R.id.theme);

//        facebookbannerAds(SettingActivity.this);
        theme();

      /*  com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(this);
        adView.setAdUnitId(Utilitie.admobbanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
         LinearLayout ll_ad_container;
        ll_ad_container = findViewById(R.id.ll_ad_container);
        if (ll_ad_container != null) {
            ll_ad_container.removeAllViews();
        }
        ll_ad_container.addView(adView);*/
        bindToolBar();
    }

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
    public void onBackPressed() {

        super.onBackPressed();

    }

    public void bindToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) toolbar.findViewById(R.id.txtToolbarTitle)).setText(getResources().getString(R.string.setting));
    }

    public void btnEqualiser(View view) {
        try {
            Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);

            if ((intent.resolveActivity(getPackageManager()) != null)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No equalizer found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSubtitleText(View view) {
        final Dialog mDialog = new Dialog(activity, R.style.CustomDialog);
        mDialog.setContentView(R.layout.dialog_subtitle_font);
        mDialog.show();

        final RadioGroup rgSubtitleText = mDialog.findViewById(R.id.rgSubtitleText);

        final int typeFace = Integer.parseInt(SharedPref.getSharedPrefData(activity,
                SharedPref.textTypeFace, "1"));

        switch (typeFace) {
            case 0: {
                rgSubtitleText.check(R.id.rbDefault);
                break;
            }
            case 1: {
                rgSubtitleText.check(R.id.rbDefaultBold);
                break;
            }
            case 2: {
                rgSubtitleText.check(R.id.rbSansSerif);
                break;
            }
            case 3: {
                rgSubtitleText.check(R.id.rbSerif);
                break;
            }
            case 4: {
                rgSubtitleText.check(R.id.rbMonospace);
                break;
            }
            default:
                rgSubtitleText.check(R.id.rbDefault);
        }

        ((RadioButton) mDialog.findViewById(R.id.rbDefault)).setTypeface(Typeface.DEFAULT);
        ((RadioButton) mDialog.findViewById(R.id.rbDefaultBold)).setTypeface(Typeface.DEFAULT_BOLD);
        ((RadioButton) mDialog.findViewById(R.id.rbSansSerif)).setTypeface(Typeface.SANS_SERIF);
        ((RadioButton) mDialog.findViewById(R.id.rbSerif)).setTypeface(Typeface.SERIF);
        ((RadioButton) mDialog.findViewById(R.id.rbMonospace)).setTypeface(Typeface.MONOSPACE);

        (mDialog.findViewById(R.id.txtDialogCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDismiss.dismissWithCheck(mDialog);
            }
        });
        (mDialog.findViewById(R.id.txtDialogConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RadioButton rbType = rgSubtitleText.findViewById(rgSubtitleText.getCheckedRadioButtonId());

                int type = 0;
                String s = rbType.getText().toString();

                if (s.equalsIgnoreCase(getResources().getString(R.string.text_default))) {
                    type = 0;
                } else if (s.equalsIgnoreCase(getResources().getString(R.string.text_default_bold))) {
                    type = 1;
                } else if (s.equalsIgnoreCase(getResources().getString(R.string.text_sans_serif))) {
                    type = 2;
                } else if (s.equalsIgnoreCase(getResources().getString(R.string.text_serif))) {
                    type = 3;
                } else if (s.equalsIgnoreCase(getResources().getString(R.string.text_monospace))) {
                    type = 4;
                }

                SharedPref.setSharedPrefData(activity, SharedPref.textTypeFace, String.valueOf(type));

                DialogDismiss.dismissWithCheck(mDialog);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        theme();
    }

    public void btnRateUs(View view) {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void btnShare(View view) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String text = "Install this new Video Player";
        String link = "https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en";
        String shareBody = text + "\n" + link;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    public void btnUpdate(View view) {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
//        showAdmobInter();

    }

    public void btnMore(View view) {
//        showAdmobInter();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.moreApps))));

    }

    public void btnpolicy(View view) {
//        showAdmobInter();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.policy))));

    }
}
