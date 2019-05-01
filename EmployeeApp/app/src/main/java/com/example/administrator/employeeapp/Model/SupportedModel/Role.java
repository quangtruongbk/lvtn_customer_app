package com.example.administrator.employeeapp.Model.SupportedModel;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Role {

    @SerializedName("createBranch")
    @Expose
    private String createBranch;
    @SerializedName("branchRole")
    @Expose
    private List<BranchRole> branchRole = null;

    public String getCreateBranch() {
        return createBranch;
    }

    public void setCreateBranch(String createBranch) {
        this.createBranch = createBranch;
    }

    public List<BranchRole> getBranchRole() {
        return branchRole;
    }

    public void setBranchRole(List<BranchRole> branchRole) {
        this.branchRole = branchRole;
    }

    public Integer getBranchPosition(String branchID) {
        for (int i = 0; i < branchRole.size(); i++) {
            if (branchRole.get(i).getBranchID().equals(branchID)) return i;
        }
        return -1;
    }

    public Boolean checkHideBranch(String branchID){
        Integer position = getBranchPosition(branchID);
        if(position != -1){
            String role = branchRole.get(position).getRole();
            if(role.equals("00000")) return true;
        }
        return false;
    }

    public Boolean checkCreateQueue(String branchID){
        Integer position = getBranchPosition(branchID);
        if(position != -1){
            String role = branchRole.get(position).getRole();
            if(role.charAt(2) == '1') return true;
        }
        return false;
    }

    public Boolean checkEditQueue(String branchID){
        Integer position = getBranchPosition(branchID);
        if(position != -1){
            String role = branchRole.get(position).getRole();
            if(role.charAt(0) == '1') return true;
        }
        return false;
    }

    public Boolean checkControlQueue(String branchID){
        Integer position = getBranchPosition(branchID);
        if(position != -1){
            String role = branchRole.get(position).getRole();
            if(role.charAt(1) == '1') return true;
        }
        return false;
    }

    public Boolean checkEditBranch(String branchID){
        Integer position = getBranchPosition(branchID);
        if(position != -1){
            String role = branchRole.get(position).getRole();
            if(role.charAt(3) == '1') return true;
        }
        return false;
    }

    public Boolean checkControlBranch(String branchID){
        Integer position = getBranchPosition(branchID);
        if(position != -1){
            String role = branchRole.get(position).getRole();
            if(role.charAt(4) == '1') return true;
        }
        return false;
    }
}
