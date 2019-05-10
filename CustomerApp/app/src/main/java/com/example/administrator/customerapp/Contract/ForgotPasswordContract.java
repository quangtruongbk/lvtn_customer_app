package com.example.administrator.customerapp.Contract;

import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Account;

public interface ForgotPasswordContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String messag0e, Boolean isSuccess);
        void openLogin();
    }

    interface Presenter extends BasePresenter {
        void forgotPassword(String email);
    }
}
