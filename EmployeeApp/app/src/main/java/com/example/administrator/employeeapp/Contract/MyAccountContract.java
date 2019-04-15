package com.example.administrator.employeeapp.Contract;

import android.content.Context;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;

public interface MyAccountContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void openMainActivity();
    }

    interface Presenter extends BasePresenter {
        void changeInfo(String token, String accountID, String name, String phone);
        void changePassword(String token, String accountID, String oldPassword, String newPassword);
    }
}
