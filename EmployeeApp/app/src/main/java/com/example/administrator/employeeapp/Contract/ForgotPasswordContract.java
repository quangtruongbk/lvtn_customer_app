package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Account;

public interface ForgotPasswordContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void openLogin();
    }

    interface Presenter extends BasePresenter {
        void forgotPassword(String email);
    }
}
