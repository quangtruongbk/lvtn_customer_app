package com.example.administrator.customerapp.Contract;
import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Branch;

import java.util.ArrayList;

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void showProgressBar();
        void hideProgressBar();
        void setUpAdapter(ArrayList<Branch> branch);
    }

    interface Presenter extends BasePresenter {
        void getBranchFromServer();
    }
}