package com.jv.mxvideoplayer.mxv.videoplayer.CustomClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;

/**
 * Created by jigs patel on 10-1-2020.
 */

public class DialogDismiss {

    public static void dismissWithCheck(Dialog dialog) {
        Log.d("DIALOG", "Dismiss Dialog With Check");
        if (dialog != null) {
            if (dialog.isShowing()) {

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

                // if the Context used here was an activity AND it hasn't been finished or destroyed
                // then dismiss it
                if (context instanceof Activity) {

                    // Api >=17
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                            dismissWithTryCatch(dialog);
                        }
                    } else {

                        // Api < 17. Unfortunately cannot check for isDestroyed()
                        if (!((Activity) context).isFinishing()) {
                            dismissWithTryCatch(dialog);
                        }
                    }
                } else
                    // if the Context used wasn't an Activity, then dismiss it too
                    dismissWithTryCatch(dialog);
            }
            dialog = null;
        }
    }

    private static void dismissWithTryCatch(Dialog dialog) {
        try {
            dialog.dismiss();
        } catch (final IllegalArgumentException e) {
            Log.d("DIALOG", "Dismiss Dialog With IllegalArgumentException : " + e.getMessage());
            // Do nothing.
        } catch (final Exception e) {
            Log.d("DIALOG", "Dismiss Dialog With Exception : " + e.getMessage());
            // Do nothing.
        } finally {
            //Log.d("DIALOG", "Dismiss Dialog With Try Catch : finally ");
            dialog = null;
        }
    }
}
