package com.example.administrator.employeeapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History extends QueueRequest {
    @SerializedName("branchName")
    @Expose
    private String branchName;

    @SerializedName("queueName")
    @Expose
    private String queueName;

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
