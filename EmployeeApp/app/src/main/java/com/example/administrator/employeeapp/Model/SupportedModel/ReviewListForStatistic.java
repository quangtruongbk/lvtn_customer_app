package com.example.administrator.employeeapp.Model.SupportedModel;

import com.example.administrator.employeeapp.Model.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewListForStatistic extends Review {
    @SerializedName("queueName")
    @Expose
    private String queueName;
    @SerializedName("customerName")
    @Expose
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}