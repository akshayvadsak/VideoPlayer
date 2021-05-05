package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.jv.mxvideoplayer.mxv.videoplayer.R;


public class ChangePasswordActivity extends AppCompatActivity implements PinEntrySetupListener, PinEntryAuthenticationListener {

    Activity activity = ChangePasswordActivity.this;
    PinView pinView;
    ImageView backgroundImg;
    int[] resourceLayout = {R.id.pin1, R.id.pin2, R.id.pin3, R.id.pin4, R.id.pin5, R.id.pin6, R.id.pin7, R.id.pin8, R.id.pin9, R.id.pin0};
    ImageView[] imgArray;
    RelativeLayout relDot;
    TextView txtEnterPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bindToolbar();
        bindControls();
    }

    private void bindToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_btn_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = toolbar.findViewById(R.id.txtToolbarTitle);
        textView.setText("Change password");
    }

    private void bindControls() {

        backgroundImg = findViewById(R.id.backgroundImg);
        pinView = findViewById(R.id.pinView);
        pinView.setMessage(getResources().getString(R.string.old_pin));
        pinView.setModeAuthenticate(this);
        txtEnterPIN = (TextView)findViewById(R.id.txtEnterPIN);
        relDot = findViewById(R.id.relDot);

        imgArray = new ImageView[10];

        for (int i = 0; i < 10; i++) {
            imgArray[i] = findViewById(resourceLayout[i]);
        }

        relDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
            }
        });
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

    @Override
    public void onPinCorrect() {
        pinView.setModeSetup(this);
        pinView.clearPin();
        txtEnterPIN.setText("Enter New PIN");
    }

    @Override
    public void onPinWrong() {
    }

    @Override
    public void onPinEntered(String pin) {
        txtEnterPIN.setText("Confirm Your PIN");
    }

    @Override
    public void onPinConfirmed(String pin) {

    }

    @Override
    public void onPinMismatch() {
        txtEnterPIN.setText("Wrong PIN , Try again");
    }

    @Override
    public void onPinSet(String pin) {
        Toast.makeText(activity, "Password changed successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

}
