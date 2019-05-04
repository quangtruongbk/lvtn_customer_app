package com.example.administrator.customerapp.Model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class QueueRequest {
    @SerializedName("id")
    @Expose
    @Nullable
    private String id;
    @SerializedName("queueId")
    @Expose
    @Nullable
    private String queueId;
    @SerializedName("createdAt")
    @Expose
    @Nullable
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    @Nullable
    private String updatedAt;
    @SerializedName("customerName")
    @Expose
    @Nullable
    private String customerName;
    @SerializedName("customerPhone")
    @Expose
    @Nullable
    private String customerPhone;
    @SerializedName("customerEmail")
    @Expose
    @Nullable
    private String customerEmail;
    @SerializedName("queueID")
    @Expose
    @Nullable
    private String queueID;
    @SerializedName("accountID")
    @Expose
    @Nullable
    private String accountID;
    @SerializedName("status")
    @Expose
    @Nullable
    private String status;
    @SerializedName("startTime")
    @Expose
    @Nullable
    private String startTime;
    @SerializedName("endTime")
    @Expose
    @Nullable
    private String endTime;
    @SerializedName("expiredDate")
    @Expose
    @Nullable
    private long expiredDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getQueueID() {
        return queueID;
    }

    public void setQueueID(String queueID) {
        this.queueID = queueID;
    }

    public String getStatus() {
        return status;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setStatus(@Nullable String status) {
        this.status = status;
    }

    @Nullable
    public long getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(@Nullable long expiredDate) {
        this.expiredDate = expiredDate;
    }
}