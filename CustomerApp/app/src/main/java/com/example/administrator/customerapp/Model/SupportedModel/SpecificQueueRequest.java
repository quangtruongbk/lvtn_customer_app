package com.example.administrator.customerapp.Model.SupportedModel;

import android.support.annotation.Nullable;

import com.example.administrator.customerapp.Model.QueueRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecificQueueRequest extends QueueRequest {

    @SerializedName("branchName")
    @Expose
    @Nullable
    private String branchName;

    @SerializedName("queueName")
    @Expose
    @Nullable
    private String queueName;

    @SerializedName("branchID")
    @Expose
    @Nullable
    private String branchID;

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }
}
