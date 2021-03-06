package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Branch;

import java.util.ArrayList;

public interface BranchListForStatisticContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);

        void showProgressBar();

        void hideProgressBar();

        void setUpAdapter(ArrayList<Branch> branch);
    }

    interface Presenter extends BasePresenter {
        void getBranch();
    }
}