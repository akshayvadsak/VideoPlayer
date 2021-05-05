package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PinImageView extends ImageView {
    public PinImageView(Context context) {
        super(context);
    }

    public PinImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            AnimationUtils.animatePinEntered(this);
        }
    }

    public void animateError() {
        AnimationUtils.animatePinError(this);
    }
}
