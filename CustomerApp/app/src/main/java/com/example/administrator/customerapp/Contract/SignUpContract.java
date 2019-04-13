package com.example.administrator.customerapp.Contract;

import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Account;

public interface SignUpContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void openLoginActivity();
    }

    interface Presenter extends BasePresenter {
        void signUp(String email, String name, String phone, String password);
    }
}
