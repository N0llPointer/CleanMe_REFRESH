package com.guglprogers.cleanme;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HereApi {
    @GET("/6.2/reversegeocode.json")
    Call<PostModel> getData(@Query("app_id") String appId, @Query("app_code") String appCode, @Query("mode") String mode, @Query("prox") String prox);
}
