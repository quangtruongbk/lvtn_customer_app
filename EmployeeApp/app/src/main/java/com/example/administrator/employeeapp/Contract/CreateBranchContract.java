package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Account;

public interface CreateBranchContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message);
    }

    interface Presenter extends BasePresenter {
        void createBranch(String token, String name, String city, String district, String ward, String restAddress, String phone,
                          Integer capacity, String openHour, String closeHour, String workingDay);
    }
}
