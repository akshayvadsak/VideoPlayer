package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

import android.animation.ObjectAnimator;
import android.view.View;

public class AnimationUtils {

    public static void animatePinEntered(PinImageView view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 0.4f, 0.8f, 1.0f);
        animator.setDuration(200);
        animator.start();
    }

    public static void animatePinError(PinImageView view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -2, -4, -6, -8, -6, -4, -2, 0, 2, 4, 6, 8, 6, 4, 2, 0);
        animator.setDuration(200);
        animator.setRepeatCount(2);
        animator.start();
    }

    public static void fadeOutView(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.8f, 0.5f, 0.3f, 0.0f);
        animator.setStartDelay(5000);
        animator.setDuration(200);
        animator.start();
    }
}
