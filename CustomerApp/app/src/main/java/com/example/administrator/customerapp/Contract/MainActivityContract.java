package com.example.administrator.customerapp.Contract;

import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;

public interface MainActivityContract {

    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);
        void openLoginActivity();
    }

    interface Presenter extends BasePresenter {
        void logout();
    }
}
