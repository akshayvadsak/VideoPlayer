package com.jv.mxvideoplayer.mxv.videoplayer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Application {

    @SerializedName("app_detail_id")
    @Expose

    private String appId;
    @SerializedName("app_marketing_name")

    @Expose
    private String appName;

    @SerializedName("app_marketing_icon")
    @Expose
    private String icon;

    @SerializedName("app_marketing_url")
    @Expose
    private String appUrl;



    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }



}
