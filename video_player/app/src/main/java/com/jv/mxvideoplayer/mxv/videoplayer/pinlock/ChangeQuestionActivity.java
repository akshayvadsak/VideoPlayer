package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.R;

import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.DataBinder.fetchQue;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.securityAns;
import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.securityQue;


public class ChangeQuestionActivity extends AppCompatActivity {

    Activity activity = ChangeQuestionActivity.this;
    MaterialSpinner spinner;
    EditText editText;
    ImageView backgroundImg;
    LinearLayout btnDone;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_change_question);
//        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(this);
//        adView.setAdUnitId(Utilitie.admobbanner);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.setAdSize(AdSize.SMART_BANNER);
//        adView.loadAd(adRequest);
//
//        ll_ad_container = findViewById(R.id.ll_ad_container);
//        if (ll_ad_container != null) {
//            ll_ad_container.removeAllViews();
//        }
//        ll_ad_container.addView(adView);

        Log.e("bhjsbhjds", "onCreate: testest" );

        bindToolbar();
        bindControls();
        clickEvents();
    }

    private void bindToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_btn_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = toolbar.findViewById(R.id.txtToolbarTitle);
        textView.setText("Security Question");
    }

    private void bindControls() {
        btnDone = findViewById(R.id.btnDone);
        backgroundImg = findViewById(R.id.backgroundImg);
        editText = findViewById(R.id.editText);
        spinner = findViewById(R.id.spinner);
        spinner.setItems(fetchQue());
        String pos = SharedPref.getSharedPrefData(activity, securityQue);

        for (int i = 0; i < fetchQue().size(); i++) {
            if (pos.equals(fetchQue().get(i))) {
                spinner.setSelectedIndex(i);
            }
        }
        if (flag == false) {
            spinner.setEnabled(false);
            spinner.setClickable(false);
        }

    }

    private void clickEvents() {
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

    public void processDone() {
        if (flag == false) {
            String ans = SharedPref.getSharedPrefData(activity, securityAns);

            if (editText.getText().toString().equals(ans)) {
                spinner.setSelectedIndex(0);

                editText.setText(null);
                spinner.setEnabled(true);
                spinner.setClickable(true);
                Toast.makeText(activity, "You can set new Que", Toast.LENGTH_LONG).show();
                flag = true;
            } else {
                editText.setText(null);
                Toast.makeText(activity, "Your ans is wrong", Toast.LENGTH_LONG).show();
            }
        } else if (flag == true) {
            if (editText.getText().toString().equals("")) {
                Toast.makeText(activity, "Please write your answer", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Security question changed successfully", Toast.LENGTH_SHORT).show();
                SharedPref.setSharedPrefData(activity, securityQue, fetchQue().get(spinner.getSelectedIndex()));
                SharedPref.setSharedPrefData(activity, securityAns, editText.getText().toString());
                finish();
            }
        }
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
        finish();
    }


}
