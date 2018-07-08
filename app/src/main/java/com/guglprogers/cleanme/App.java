package com.guglprogers.cleanme;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static HereApi here;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://reverse.geocoder.cit.api.here.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        here = retrofit.create(HereApi.class);
    }

    public static HereApi getApi(){
        return here;
    }


}
