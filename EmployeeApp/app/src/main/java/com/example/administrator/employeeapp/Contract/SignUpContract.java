package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Account;

public interface SignUpContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void openLoginActivity();
    }

    interface Presenter extends BasePresenter {
        void signUp(String email, String name, String phone, String password);
    }
}
