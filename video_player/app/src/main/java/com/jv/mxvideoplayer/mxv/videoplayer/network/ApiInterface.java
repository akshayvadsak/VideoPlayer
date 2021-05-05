package com.jv.mxvideoplayer.mxv.videoplayer.network;




import com.jv.mxvideoplayer.mxv.videoplayer.model.AppCount;
import com.jv.mxvideoplayer.mxv.videoplayer.model.ApplicationList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("admin/api/Appbyaccount.php")
    @FormUrlEncoded
    Call<ApplicationList> getAppListByAppId(@Field("app_detail_id") int app_id);

    @POST("admin/api/Appdownload.php")
    @FormUrlEncoded
    Call<AppCount> postApplicationCount(@Field("app_detail_package_name") String package_name);


}