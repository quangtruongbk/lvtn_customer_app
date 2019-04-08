package com.example.administrator.customerapp.Contract;

import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Account;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isVerifyEmail);
        void openMainActivity(Account account);
    }

    interface Presenter extends BasePresenter {
        void logIn(String email, String password);
    }
}
