package com.example.administrator.customerapp.CallAPI;

import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.Queue;

import java.util.ArrayList;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("/login")
    Call<Account> logIn(@Field("email") String email, @Field("password") String password);

    @GET("branch/json")
    Call<ArrayList<Branch>> getBranch();

    @GET("branch/{branchID}/json")
    Call<ArrayList<Queue>> getQueue(@Path("branchID") String branchID);
}
