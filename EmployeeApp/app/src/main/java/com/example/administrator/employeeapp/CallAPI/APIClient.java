package com.example.administrator.employeeapp.CallAPI;

import com.example.administrator.employeeapp.Activity.GetIP;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
  //  public static final String BASE_URL = "http://192.168.1.6:1337/";
  private static final String BASE_URL = GetIP.IP + ":1337/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
