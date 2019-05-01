package com.example.administrator.employeeapp.Model;

import java.util.List;

import com.example.administrator.employeeapp.Model.SupportedModel.EmployeeListForStatistic;
import com.example.administrator.employeeapp.Model.SupportedModel.ReviewListForStatistic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistic {

    @SerializedName("noOfRequestPerDay")
    @Expose
    private List<Integer> noOfRequestPerDay = null;
    @SerializedName("noOfDonePerDay")
    @Expose
    private List<Integer> noOfDonePerDay = null;
    @SerializedName("noOfCancelPerDay")
    @Expose
    private List<Integer> noOfCancelPerDay = null;
    @SerializedName("noOfRequest")
    @Expose
    private Integer noOfRequest;
    @SerializedName("noOfWaiting")
    @Expose
    private Integer noOfWaiting;
    @SerializedName("noOfUsing")
    @Expose
    private Integer noOfUsing;
    @SerializedName("noOfCancel")
    @Expose
    private Integer noOfCancel;
    @SerializedName("noOfReview")
    @Expose
    private Integer noOfReview;
    @SerializedName("noOfDone")
    @Expose
    private Integer noOfDone;
    @SerializedName("serviceScore")
    @Expose
    private Double serviceScore;
    @SerializedName("waitingScore")
    @Expose
    private Double waitingScore;
    @SerializedName("spaceScore")
    @Expose
    private Double spaceScore;
    @SerializedName("review")
    @Expose
    private List<ReviewListForStatistic> review = null;
    @SerializedName("inLineTime")
    @Expose
    private Double inLineTime;
    @SerializedName("usingTime")
    @Expose
    private Double usingTime;
    @SerializedName("queue")
    @Expose
    private List<Queue> queue = null;
    @SerializedName("employee")
    @Expose
    private List<EmployeeListForStatistic> employee = null;

    public List<Integer> getNoOfRequestPerDay() {
        return noOfRequestPerDay;
    }

    public void setNoOfRequestPerDay(List<Integer> noOfRequestPerDay) {
        this.noOfRequestPerDay = noOfRequestPerDay;
    }

    public Integer getNoOfRequest() {
        return noOfRequest;
    }

    public void setNoOfRequest(Integer noOfRequest) {
        this.noOfRequest = noOfRequest;
    }

    public Integer getNoOfWaiting() {
        return noOfWaiting;
    }

    public void setNoOfWaiting(Integer noOfWaiting) {
        this.noOfWaiting = noOfWaiting;
    }

    public Integer getNoOfUsing() {
        return noOfUsing;
    }

    public void setNoOfUsing(Integer noOfUsing) {
        this.noOfUsing = noOfUsing;
    }

    public Integer getNoOfCancel() {
        return noOfCancel;
    }

    public void setNoOfCancel(Integer noOfCancel) {
        this.noOfCancel = noOfCancel;
    }

    public Integer getNoOfReview() {
        return noOfReview;
    }

    public void setNoOfReview(Integer noOfReview) {
        this.noOfReview = noOfReview;
    }

    public Integer getNoOfDone() {
        return noOfDone;
    }

    public void setNoOfDone(Integer noOfDone) {
        this.noOfDone = noOfDone;
    }

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Double getWaitingScore() {
        return waitingScore;
    }

    public void setWaitingScore(Double waitingScore) {
        this.waitingScore = waitingScore;
    }

    public Double getSpaceScore() {
        return spaceScore;
    }

    public void setSpaceScore(Double spaceScore) {
        this.spaceScore = spaceScore;
    }

    public List<ReviewListForStatistic> getReview() {
        return review;
    }

    public void setReview(List<ReviewListForStatistic> review) {
        this.review = review;
    }

    public Double getInLineTime() {
        return inLineTime;
    }

    public void setInLineTime(Double inLineTime) {
        this.inLineTime = inLineTime;
    }

    public Double getUsingTime() {
        return usingTime;
    }

    public void setUsingTime(Double usingTime) {
        this.usingTime = usingTime;
    }

    public List<Queue> getQueue() {
        return queue;
    }

    public void setQueue(List<Queue> queue) {
        this.queue = queue;
    }

    public List<EmployeeListForStatistic> getEmployee() {
        return employee;
    }

    public void setEmployee(List<EmployeeListForStatistic> employee) {
        this.employee = employee;
    }

    public List<Integer> getNoOfDonePerDay() {
        return noOfDonePerDay;
    }

    public void setNoOfDonePerDay(List<Integer> noOfDonePerDay) {
        this.noOfDonePerDay = noOfDonePerDay;
    }

    public List<Integer> getNoOfCancelPerDay() {
        return noOfCancelPerDay;
    }

    public void setNoOfCancelPerDay(List<Integer> noOfCancelPerDay) {
        this.noOfCancelPerDay = noOfCancelPerDay;
    }
}