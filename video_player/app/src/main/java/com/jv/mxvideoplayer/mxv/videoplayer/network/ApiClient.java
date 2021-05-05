package com.jv.mxvideoplayer.mxv.videoplayer.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
 
    public static final String BASE_URL = "http://photoeditorzone.com/";
    private static Retrofit mRetrofit = null;


    public static Retrofit getWebClient() {
        if (mRetrofit ==null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}