package com.example.administrator.employeeapp.CallAPI;

import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.History;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.Model.QueueRequest;
import com.example.administrator.employeeapp.Model.Review;

import java.util.ArrayList;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("/login")
    Call<Account> logIn(@Field("email") String email, @Field("password") String password, @Field("type") String type );

    @FormUrlEncoded
    @POST("/account/getaccount")
    Call<Account> getAccount(@Header("token") String token, @Field("accountid")String accountID);

    @GET("account/logout")
    Call<Void> logout();

    @GET("branch/json")
    Call<ArrayList<Branch>> getBranch();

    @GET("branch/{branchID}/json")
    Call<ArrayList<Queue>> getQueue(@Path("branchID") String branchID);

    @GET("queuerequest/queueid={queueID}/json")
    Call<ArrayList<QueueRequest>> getQueueRequest(@Path("queueID") String queueID);

    @GET("history/accountid={accountID}/json")
    Call<ArrayList<History>> getHistory(@Header("token") String token, @Path("accountID") String accountID);

    @FormUrlEncoded
    @POST("/account/verify/resend")
    Call<Void> resendVerifyEmail(@Field("accountid") String accountID, @Field("email") String email);

    @FormUrlEncoded
    @POST("/account/register")
    Call<Void> signUp(@Field("email")String email, @Field("name")String name, @Field("phone")String phone, @Field("password")String password);

    @FormUrlEncoded
    @POST("/account/changeinfo")
    Call<Void> changeInfo(@Header("token") String token, @Field("accountid") String accountID, @Field("name")String name, @Field("phone")String phone);

    @FormUrlEncoded
    @POST("/account/changepassword")
    Call<Void> changePassword(@Header("token") String token, @Field("accountid") String accountID, @Field("oldpassword")String oldPassword, @Field("newpassword")String newPassword);

    @FormUrlEncoded
    @POST("/account/resetpassword")
    Call<Void> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("/review/create")
    Call<Void> createReview(@Header("token") String token, @Field("accountid")String accountID, @Field("queuerequestid")String queueRequestID, @Field("waitingscore")Float waitingScore, @Field("servicescore")Float serviceScore, @Field("spacescore") Float spaceScore, @Field("comment") String comment);

    @GET("/review?")
    Call<Review> getReview(@Query("queuerequestid") String queueRequestID);

    @FormUrlEncoded
    @POST("/queue/create")
    Call<Void> createQueue(@Header("token") String token, @Field("queuename")String queueName, @Field("branchid")String branchID,@Field("capacity") Integer capacity, @Field("waitingtime")Integer waitingTime);

    @FormUrlEncoded
    @PUT("/queue/changestatus")
    Call<Void> changeQueueStatus(@Header("token") String token, @Field("queueid")String queueID, @Field("status") String status);

    @FormUrlEncoded
    @POST("/queue/changeinfo")
    Call<Void> changeInfoQueue(@Header("token") String token, @Field("queueid")String queueID, @Field("queuename")String queueName, @Field("capacity") Integer capacity, @Field("waitingtime")Integer waitingTime);

    @FormUrlEncoded
    @POST("/queuerequest/sendemail")
    Call<Void> sendEmail(@Header("token") String token, @Field("email")String email, @Field("message")String message);
}