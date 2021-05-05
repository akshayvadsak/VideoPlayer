package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.R;


public class PinEntryView extends LinearLayout {

    public final static int MODE_AUTHENTICATE = 1;
    public final static int MODE_SETUP = 2;
    private final static String TAG = "PinEntryView";
    private final static int STATE_INITIAL = 1;
    private final static int STATE_CONFIRM = 2;
    String msg = "";
    private int mode = -1;
    private String pin;
    private int state;
    private int[] pinArray = new int[4];
    private int[] pinConfirmArray = new int[4];
    private int charIndex = -1;
    private TextView tvMessage;
    private PinImageView[] imgViews = new PinImageView[4];

    private PinEntrySetupListener setupListener;
    private PinEntryAuthenticationListener authenticationListener;


    public PinEntryView(Context context) {
        super(context);
        init();
    }

    public PinEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMsg(String msg) {
        this.msg = msg;
        tvMessage.setText(msg);
    }

    public void setModeSetup() {
        unsetVariables();
        tvMessage.setText(R.string.enter_pin);
        this.mode = MODE_SETUP;
        this.state = STATE_INITIAL;
    }

    public void setModeAuthenticate() {
        this.mode = MODE_AUTHENTICATE;
        unsetVariables();
        this.state = STATE_INITIAL;
        if (!msg.equals(getContext().getResources().getString(R.string.old_pin)))
            tvMessage.setText(R.string.enter_pin_to_unlock);
        this.pin = SharedPref.getSharedPrefData(getContext(), SharedPref.pinLock);
    }

    public void setSetupListener(PinEntrySetupListener listener) {
        this.setupListener = listener;
    }

    public void setupAuthenticationListener(PinEntryAuthenticationListener listener) {
        this.authenticationListener = listener;
    }

    public void sendKey(PinButtons key) throws Exception {
        if (mode == -1) {
            throw new Exception("Mode is not set");
        } else {
            processKey(key);
        }
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view = inflate(getContext(), R.layout.pin_entry, null);
        imgViews[0] = view.findViewById(R.id.pe0);
        imgViews[1] = view.findViewById(R.id.pe1);
        imgViews[2] = view.findViewById(R.id.pe2);
        imgViews[3] = view.findViewById(R.id.pe3);
        tvMessage = view.findViewById(R.id.tvMessage);
        addView(view, params);
    }

    private void processKey(PinButtons key) {
        if (mode == MODE_SETUP) {
            processKeyForSetup(key);
        } else if (mode == MODE_AUTHENTICATE) {
            processKeyForAuthentication(key);
        }
    }

    private void processKeyForSetup(PinButtons key) {

        switch (key) {
            case BUTTON_0:
            case BUTTON_1:
            case BUTTON_2:
            case BUTTON_3:
            case BUTTON_4:
            case BUTTON_5:
            case BUTTON_6:
            case BUTTON_7:
            case BUTTON_8:
            case BUTTON_9:


                if (charIndex == -1 && state == STATE_INITIAL) {
                    tvMessage.setText(R.string.enter_pin);
                }
                if (charIndex >= -1 && charIndex <= 2) {
                    charIndex++;
                    imgViews[charIndex].setSelected(true);

                    if (state == STATE_INITIAL) {
                        pinArray[charIndex] = key.ordinal();
                    } else if (state == STATE_CONFIRM) {
                        pinConfirmArray[charIndex] = key.ordinal();
                    }
                }
                if (charIndex == 3) {
                    processKeyEntryComplete();
                }
                break;
            case BUTTON_DOT:
                break;
            case BUTTON_DELETE:
                if (charIndex > -1) {
                    imgViews[charIndex].setSelected(false);
                    charIndex--;
                }
                break;
        }
    }

    private void processKeyEntryComplete() {
        if (mode == MODE_SETUP) {
            if (state == STATE_INITIAL) {
                tvMessage.setText(R.string.confirm_pin);
                state = STATE_CONFIRM;
                if (setupListener != null) {
                    setupListener.onPinEntered(getString(pinArray));
                }
                unsetAllPins();
            } else if (state == STATE_CONFIRM) {
                if (setupListener != null) {
                    setupListener.onPinConfirmed(getString(pinConfirmArray));
                }
                boolean pinEqual = true;
                String setPin = "";
                for (int i = 0; i < 4; i++) {
                    setPin = setPin + pinArray[i];
                    if (pinArray[i] != pinConfirmArray[i]) {
                        pinEqual = false;
                        break;
                    }
                }
                if (pinEqual) {

                    SharedPref.setSharedPrefData(getContext(), SharedPref.pinLock, setPin);
                    if (setupListener != null) {
                        setupListener.onPinSet(setPin);
                    }
                } else {
                    presentErrorUI();
                    if (setupListener != null) {
                        setupListener.onPinMismatch();
                    }
                    state = STATE_INITIAL;
                    tvMessage.setText(R.string.pin_mismatch);
                    unsetAllPins();
                    unsetVariables();
                }
            }
        } else if (mode == MODE_AUTHENTICATE) {
            if (pin.equalsIgnoreCase(getString(pinArray))) {
                if (authenticationListener != null) {
                    authenticationListener.onPinCorrect();
                }
            } else {
                presentErrorUI();
                unsetAllPins();
                unsetVariables();
                tvMessage.setText(R.string.wrong_pin);
                if (authenticationListener != null) {
                    authenticationListener.onPinWrong();
                }
            }
        }
    }

    private void processKeyForAuthentication(PinButtons key) {
        switch (key) {

            case BUTTON_0:
            case BUTTON_1:
            case BUTTON_2:
            case BUTTON_3:
            case BUTTON_4:
            case BUTTON_5:
            case BUTTON_6:
            case BUTTON_7:
            case BUTTON_8:
            case BUTTON_9:

                if (charIndex == -1 && state == STATE_INITIAL) {
                    if (!msg.equals(getContext().getResources().getString(R.string.old_pin)))
                        tvMessage.setText(R.string.enter_pin_to_unlock);
                    else
                        tvMessage.setText(R.string.old_pin);
                }
                if (charIndex >= -1 && charIndex <= 2) {
                    charIndex++;
                    imgViews[charIndex].setSelected(true);
                    pinArray[charIndex] = key.ordinal();
                }
                if (charIndex == 3) {
                    processKeyEntryComplete();
                }
                break;

            case BUTTON_DOT:
                break;

            case BUTTON_DELETE:
                if (charIndex > -1) {
                    imgViews[charIndex].setSelected(false);
                    charIndex--;
                }
                break;
        }
    }

    private void presentErrorUI() {
        for (PinImageView iv : imgViews) {
            iv.animateError();
        }

        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 200};
        v.vibrate(pattern, -1);
    }

    public void unsetAllPins() {
        charIndex = -1;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                for (ImageView ivs : imgViews) {
                    ivs.setSelected(false);
                }
            }
        }, 200);
    }

    private void unsetVariables() {
        for (int i = 0; i < 4; i++) {
            pinArray[i] = -1;
            pinConfirmArray[i] = -1;
        }
    }

    private String getString(int[] array) {
        String text = "";
        for (int i = 0; i < array.length; i++) {
            text = text + array[i];
        }
        return text;
    }

}
