package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;

public interface MainActivityContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void openLoginActivity();
    }

    interface Presenter extends BasePresenter {
        void logout();
    }
}
