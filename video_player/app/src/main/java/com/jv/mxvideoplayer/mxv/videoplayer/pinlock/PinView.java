package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jv.mxvideoplayer.mxv.videoplayer.R;

public class PinView extends LinearLayout {

    private PinEntryView pinEntryView;
    private PinKeyboardView pinKeyboardView;

    public PinView(Context context) {
        super(context);
        init();
    }

    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view = inflate(getContext(), R.layout.pin_view, null);
        pinEntryView = view.findViewById(R.id.pinEntryView);
        pinKeyboardView = view.findViewById(R.id.pinKeyboardView);
        pinKeyboardView.setPinEntryView(pinEntryView);
        addView(view, params);
    }

    public void setModeSetup(PinEntrySetupListener listener) {
        pinEntryView.setModeSetup();
        pinEntryView.setSetupListener(listener);
    }

    public void clearPin() {
        pinEntryView.unsetAllPins();
    }

    public void setModeAuthenticate(PinEntryAuthenticationListener listener) {
        pinEntryView.setModeAuthenticate();
        pinEntryView.setupAuthenticationListener(listener);
    }

    public void setMessage(String msg) {
        pinEntryView.setMsg(msg);
    }


}
