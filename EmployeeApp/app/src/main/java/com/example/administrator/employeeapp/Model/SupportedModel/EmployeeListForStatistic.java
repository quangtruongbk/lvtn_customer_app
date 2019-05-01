package com.example.administrator.employeeapp.Model.SupportedModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeListForStatistic {

    @SerializedName("accountID")
    @Expose
    private String accountID;
    @SerializedName("accountEmail")
    @Expose
    private String accountEmail;
    @SerializedName("employeeID")
    @Expose
    private String employeeID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean checkCreateQueue(){
        if(role != null){
            if(role.charAt(2) == '1') return true;
        }
        return false;
    }

    public Boolean checkEditQueue(){
        if(role != null){
            if(role.charAt(0) == '1') return true;
        }
        return false;
    }

    public Boolean checkControlQueue(){
        if(role != null){
            if(role.charAt(1) == '1') return true;
        }
        return false;
    }

    public Boolean checkEditBranch(){
        if(role != null){
            if(role.charAt(3) == '1') return true;
        }
        return false;
    }

    public Boolean checkControlBranch(){
        if(role != null){
            if(role.charAt(4) == '1') return true;
        }
        return false;
    }
}