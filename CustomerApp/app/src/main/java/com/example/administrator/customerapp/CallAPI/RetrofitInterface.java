package com.example.administrator.customerapp.CallAPI;

import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.History;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.Model.Review;

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

    @GET("account/logout")
    Call<Void> logout();

    @GET("branch/json")
    Call<ArrayList<Branch>> getBranch();

    @GET("branch/{branchID}/json")
    Call<ArrayList<Queue>> getQueue(@Path("branchID") String branchID);

    @GET("queuerequest/queueid={queueID}/json")
    Call<ArrayList<QueueRequest>> getQueueRequest(@Path("queueID") String queueID);

    @GET("history/accountid={accountID}/json")
    Call<ArrayList<History>> getHistory(@Path("accountID") String accountID);

    @FormUrlEncoded
    @POST("/account/verify/resend")
    Call<Void> resendVerifyEmail(@Field("accountid") String accountID, @Field("email") String email);

    @FormUrlEncoded
    @POST("/account/register")
    Call<Void> signUp(@Field("email")String email, @Field("name")String name, @Field("phone")String phone, @Field("password")String password);

    @FormUrlEncoded
    @POST("/account/changeinfo")
    Call<Void> changeInfo(@Field("accountid") String accountID, @Field("name")String name, @Field("phone")String phone);

    @FormUrlEncoded
    @POST("/account/changepassword")
    Call<Void> changePassword(@Field("accountid") String accountID, @Field("oldpassword")String oldPassword, @Field("newpassword")String newPassword);

    @FormUrlEncoded
    @POST("/review/create")
    Call<Void> createReview(@Field("accountid")String accountID, @Field("queuerequestid")String queueRequestID, @Field("waitingscore")Float waitingScore, @Field("servicescore")Float serviceScore, @Field("spacescore") Float spaceScore, @Field("comment") String comment);

    @GET("/review?")
    Call<Review> getReview(@Query("queuerequestid") String queueRequestID);

}
