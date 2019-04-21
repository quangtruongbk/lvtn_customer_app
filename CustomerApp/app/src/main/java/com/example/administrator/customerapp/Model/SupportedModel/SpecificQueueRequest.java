package com.example.administrator.customerapp.Model.SupportedModel;

import com.example.administrator.customerapp.Model.QueueRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecificQueueRequest extends QueueRequest {

    @SerializedName("branchName")
    @Expose
    private String branchName;

    @SerializedName("queueName")
    @Expose
    private String queueName;


    @SerializedName("branchID")
    @Expose
    private String branchID;

    @SerializedName("queueID")
    @Expose
    private String queueID;

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    @Override
    public String getQueueID() {
        return queueID;
    }

    @Override
    public void setQueueID(String queueID) {
        this.queueID = queueID;
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
