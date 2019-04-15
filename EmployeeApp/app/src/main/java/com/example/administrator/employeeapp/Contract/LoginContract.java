package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Account;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isVerifyEmail);
        void openMainActivity(Account account);
    }

    interface Presenter extends BasePresenter {
        void logIn(String email, String password);
        void getAccount(String token, String accountID);
        void resendVerifyEmail();
    }
}
