package com.example.administrator.customerapp.Contract;

import android.content.Context;

import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;

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
