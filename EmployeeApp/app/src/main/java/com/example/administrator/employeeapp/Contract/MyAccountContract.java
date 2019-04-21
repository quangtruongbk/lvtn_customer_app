package com.example.administrator.employeeapp.Contract;

import android.content.Context;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;

import java.util.ArrayList;

public interface MyAccountContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);
        void openMainActivity();
        void setUpRoleAdapter(ArrayList<Branch> branch);
    }

    interface Presenter extends BasePresenter {
        void changeInfo(String token, String accountID, String name, String phone);
        void changePassword(String token, String accountID, String oldPassword, String newPassword);
        void setUpRole(Account account, Employee employee);
    }
}
