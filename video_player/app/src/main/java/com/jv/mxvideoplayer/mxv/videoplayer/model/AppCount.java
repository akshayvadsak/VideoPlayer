package com.jv.mxvideoplayer.mxv.videoplayer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jigs patel on 20-1-2020.
 */

public class AppCount {

    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;

    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("message")
    @Expose
    private String message;
}
