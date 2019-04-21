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

}
