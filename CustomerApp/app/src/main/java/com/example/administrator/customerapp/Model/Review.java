package com.example.administrator.customerapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("accountID")
    @Expose
    private String accountID;
    @SerializedName("queueRequestID")
    @Expose
    private String queueRequestID;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("waitingScore")
    @Expose
    private float waitingScore;
    @SerializedName("spaceScore")
    @Expose
    private float spaceScore;
    @SerializedName("serviceScore")
    @Expose
    private float serviceScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getQueueRequestID() {
        return queueRequestID;
    }

    public void setQueueRequestID(String queueRequestID) {
        this.queueRequestID = queueRequestID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getWaitingScore() {
        return waitingScore;
    }

    public void setWaitingScore(float waitingScore) {
        this.waitingScore = waitingScore;
    }

    public float getSpaceScore() {
        return spaceScore;
    }

    public void setSpaceScore(float spaceScore) {
        this.spaceScore = spaceScore;
    }

    public float getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(float serviceScore) {
        this.serviceScore = serviceScore;
    }
}
