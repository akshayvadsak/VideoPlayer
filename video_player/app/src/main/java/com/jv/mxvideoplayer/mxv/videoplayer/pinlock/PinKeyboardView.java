package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref;
import com.jv.mxvideoplayer.mxv.videoplayer.R;

import static com.jv.mxvideoplayer.mxv.videoplayer.CustomClass.SharedPref.vibrate;

public class PinKeyboardView extends LinearLayout {

    RelativeLayout rel0, rel1, rel2, rel3, rel4, rel5, rel6, rel7, rel8, rel9, relDot, relDelete;

    private PinEntryView pinEntryView;

    public PinKeyboardView(Context context) {
        super(context);
        init();
    }

    public PinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view = inflate(getContext(), R.layout.pin_keyboard, null);
        setupButtons(view);
        setupListeners(getContext());
        addView(view, params);
    }

    private void setupButtons(View view) {
        rel0 = view.findViewById(R.id.rel0);
        rel1 = view.findViewById(R.id.rel1);
        rel2 = view.findViewById(R.id.rel2);
        rel3 = view.findViewById(R.id.rel3);
        rel4 = view.findViewById(R.id.rel4);
        rel5 = view.findViewById(R.id.rel5);
        rel6 = view.findViewById(R.id.rel6);
        rel7 = view.findViewById(R.id.rel7);
        rel8 = view.findViewById(R.id.rel8);
        rel9 = view.findViewById(R.id.rel9);
        relDelete = view.findViewById(R.id.relDelete);
        relDot = view.findViewById(R.id.relDot);
    }

    private void setupListeners(final Context context) {

        rel0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_0);
            }
        });
        rel1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_1);
            }
        });

        rel2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_2);
            }
        });
        rel3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_3);
            }
        });
        rel4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_4);
            }
        });
        rel5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_5);
            }
        });
        rel6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_6);
            }
        });
        rel7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_7);
            }
        });
        rel8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_8);
            }
        });
        rel9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_9);
            }
        });

//        relDot.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getVibrate(view);
//                setListener(PinButtons.BUTTON_DOT);
//            }
//        });
        relDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getVibrate(view);
                setListener(PinButtons.BUTTON_DELETE);
            }
        });
    }

    public void getVibrate(View view) {
        //view.startAnimation(android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha));
        if (SharedPref.getSharedPrefData(getContext(), vibrate).equals("1")) {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(50);
            }
        }
    }

    private void setListener(PinButtons which) {
        try {
            if (pinEntryView != null) {
                pinEntryView.sendKey(which);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPinEntryView(PinEntryView view) {
        this.pinEntryView = view;
    }
}
