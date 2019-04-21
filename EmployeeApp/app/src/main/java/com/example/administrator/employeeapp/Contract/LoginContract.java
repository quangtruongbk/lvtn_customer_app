package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Employee;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isVerifyEmail);
        void openMainActivity(Account account, Employee employee);
    }

    interface Presenter extends BasePresenter {
        void logIn(String email, String password);
        void getAccount(String token, String accountID);
        void resendVerifyEmail();
        void getEmployeeInfo(String token, String accountID);
    }
}
