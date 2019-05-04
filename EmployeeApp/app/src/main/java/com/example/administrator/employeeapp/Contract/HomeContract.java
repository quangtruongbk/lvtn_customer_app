package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.SupportedModel.Address;

import java.util.ArrayList;

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);

        void showProgressBar();

        void hideProgressBar();

        void setUpAdapter(ArrayList<Branch> branch);

    }

    interface Presenter extends BasePresenter {
        void getBranchFromServer();


        void changeInfoBranch(String token, String branchID, String name, String city, String district, String ward, String restAddress, String phone,
                              Integer capacity, String openHour, String closeHour, String workingDay, String note);

        void closeOpenBranch(String token, String branchID, String newStatus);

        }
}