package com.example.administrator.employeeapp.Model.SupportedModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchRole {

    @SerializedName("branchID")
    @Expose
    private String branchID;
    @SerializedName("role")
    @Expose
    private String role;

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}

