package com.jv.mxvideoplayer.mxv.videoplayer.pinlock;

public interface PinEntrySetupListener {
    public void onPinEntered(String pin);

    public void onPinConfirmed(String pin);

    public void onPinMismatch();

    public void onPinSet(String pin);
}
